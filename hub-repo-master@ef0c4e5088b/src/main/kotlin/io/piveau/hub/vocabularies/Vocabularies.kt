@file:JvmName("Vocabularies")
/*
 * Copyright (c) 2023. European Commission
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package io.piveau.hub.vocabularies

import io.piveau.dcatap.TripleStore
import io.piveau.hub.Constants
import io.piveau.hub.services.catalogues.CataloguesService
import io.piveau.hub.services.datasets.DatasetsService
import io.piveau.hub.services.vocabularies.VocabulariesService
import io.piveau.json.asJsonObject
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.toNTriples
import io.piveau.utils.dcatap.dsl.dataset
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.kotlin.coroutines.await
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.vocabulary.DCAT
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.cast

private const val VOCABULARIES_CATALOGUE_ID = "vocabularies"

suspend fun installVocabularies(process: CommandProcess) {

    process.write("Installing vocabularies\n")

    val vertx = process.vertx()

    val list = process.commandLine().isFlagEnabled("list")
    if (list) {
        val buffer = vertx.fileSystem().readFile("vocabularies.json").await()
        process.write("${buffer.toJsonArray().encodePrettily()}\n")
        return
    }

    val config = vertx.orCreateContext.config()
    val tripleStore = TripleStore(
        vertx,
        config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
        name = "install-vocabularies"
    )

    val override = process.commandLine().isFlagEnabled("override")
    val remote = process.commandLine().isFlagEnabled("remote")

    val client = WebClient.create(vertx)

    try {
        val catalogueExists = tripleStore.catalogueManager.exists(VOCABULARIES_CATALOGUE_ID).await()
        if (override || !catalogueExists) {
            val cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS)
            val buffer = vertx.fileSystem().readFile("vocabulariesCatalogue.rdf").await()
            cataloguesService.putCatalogue(VOCABULARIES_CATALOGUE_ID, buffer.toString(), RDFMimeTypes.RDFXML).await()
        }

        val vocabulariesService = VocabulariesService.createProxy(vertx, VocabulariesService.SERVICE_ADDRESS)
        val datasetService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS)

        val tags = process.commandLine().getOptionValues<String>("tags").toMutableList()
        if (tags.isEmpty()) {
            tags.add("core")
        }

        val vocabularies = vertx.fileSystem().readFile("vocabularies.json").await().toJsonArray()

        vocabularies
            .map { it as JsonObject }
            .filter {
                val vocabularyTags = it.getJsonArray("tags", JsonArray()).map(String::class::cast).toSet()
                tags.any { tag -> vocabularyTags.contains(tag) }
            }
            .forEach { info ->
                val id = info.getString("id")
                val uriRef = info.getString("uriRef")

                process.write("Installing $id ($uriRef)...\n")

                if (override || !tripleStore.datasetManager.existGraph(uriRef).await()) {
                    try {
                        val status = when {
                            remote && info.containsKey("remote") -> {
                                val filename = fetchFromRemote(vertx, client, info.getString("remote")).await()
                                process.write("Vocabulary $id fetched\n")
                                mergeExtensions(info)
                                Future.future { p ->
                                    vocabulariesService.installVocabulary(
                                        id,
                                        uriRef,
                                        RDFMimeTypes.RDFXML,
                                        filename,
                                        p
                                    )
                                }.await()
                            }
                            info.containsKey("file") -> {
                                val content = loadFromLocal(vertx, info.getString("file")).await()
                                process.write("Vocabulary $id loaded\n")
                                mergeExtensions(info)
                                Future.future { p ->
                                    vocabulariesService.createOrUpdateVocabulary(
                                        id,
                                        uriRef,
                                        RDFMimeTypes.RDFXML,
                                        content,
                                        "",
                                        0,
                                        1,
                                        p
                                    )
                                }.await()
                            }
                            else -> throw Throwable("Vocabulary $id is not properly configured")
                        }
                        process.write("Vocabulary $id $status\n")

                        val dataset = dataset(id) {
                            title { id lang "en" }
                            description { "DCAT-AP related vocabulary" lang "en" }
                            // add issued and modified
                            distribution {
                                addProperty(DCAT.accessURL, ResourceFactory.createResource(uriRef))
                            }
                        }

                        val status2 = datasetService.putDatasetOrigin(
                            id,
                            dataset.model.toNTriples(),
                            RDFMimeTypes.NTRIPLES,
                            VOCABULARIES_CATALOGUE_ID,
                            false
                        ).await()

                        process.write("Vocabulary $id related dataset ${status2.getString("status")}\n")
                    } catch (t: Throwable) {
                        process.write("Vocabulary $id error: ${t.message}\n")
                    }
                } else {
                    process.write("Vocabulary $id already stored\n")
                }
            }
    } catch (t: Throwable) {
        process.write("Something went wrong: ${t.stackTraceToString()}\n")
    }
}

fun fetchFromRemote(vertx: Vertx, client: WebClient, url: String): Future<String> {
    return Future.future { promise ->
        val file = AtomicReference<String>()
        vertx.fileSystem().createTempFile("piveau", ".tmp")
            .compose { filename ->
                file.set(filename)
                vertx.fileSystem().open(filename, OpenOptions().setWrite(true))
            }.compose { stream ->
                client.getAbs(url)
                    .`as`(BodyCodec.pipe(stream, true))
                    .expect(ResponsePredicate.SC_SUCCESS)
                    .send()
            }
            .onSuccess {
                promise.complete(file.get())
            }
            .onFailure(promise::fail)
    }
}

fun loadFromLocal(vertx: Vertx, file: String): Future<String> = vertx.fileSystem().readFile(file).map { it.toString() }

suspend fun mergeExtensions(info: JsonObject) {
    if (info.containsKey("extensions")) {
//                            val model = vertx.executeBlocking { it.complete(content.toByteArray().toModel(Lang.RDFXML)) }.await()
//                            info.getJsonArray("extensions", JsonArray())
//                                .map { it as String }
//                                .forEach {
//                                    val extension = loadFromLocal(vertx, it).await()
//                                    val extensionModel = vertx.executeBlocking { p ->
//                                        p.complete(extension.toByteArray().toModel(Lang.RDFXML))
//                                    }.await()
//                                    model.add(extensionModel)
//                                }
//                            content = model.presentAs(Lang.RDFXML)
    }
}