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

package io.piveau.indexing

import io.piveau.hub.indexing.indexConceptScheme
import io.piveau.rdf.toModel
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.Lang
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@DisplayName("Indexing geonames test")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexingVocabulariesTest {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @BeforeAll
    fun setUp(vertx: Vertx, testContext: VertxTestContext) {
        testContext.completeNow()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `Test local vocabulary indexing`(vertx: Vertx, testContext: VertxTestContext) {
        vertx.fileSystem().readFile("de-contributors-skos.rdf")
            .compose { buffer ->
                vertx.executeBlocking<Model> {
                    val model = buffer.bytes.toModel(Lang.RDFXML)
                    it.complete(model)
                }
            }.onSuccess { model ->
                var index: JsonObject;
                measureTime {
                    index = indexConceptScheme(
                        "contributors",
                        "http://dcat-ap.de/def/contributors",
                        model
                    )
                }.let { duration -> log.info("${duration.inWholeMilliseconds}") }
                testContext.completeNow()
            }
            .onFailure {
                testContext.failNow(it)
            }
    }

    @Test
    @Timeout(value = 5, timeUnit = TimeUnit.MINUTES)
    @Disabled
    fun `Test remote vocabulary indexing`(vertx: Vertx, testContext: VertxTestContext) {
        WebClient.create(vertx).getAbs("http://publications.europa.eu/resource/distribution/eurovoc/rdf/skos_ap_act/eurovoc-skos-ap-act.rdf")
            .expect(ResponsePredicate.SC_SUCCESS)
            .send()
            .map { it.body() }
            .compose { buffer ->
                vertx.executeBlocking<Model> {
                    val model = buffer.bytes.toModel(Lang.RDFXML)
                    it.complete(model)
                }
            }.onSuccess { model ->
                log.info("Number of triples: ${model.size()} / Chunks: ${model.size() / 2500}")
                var index = indexConceptScheme(
                    "eurovoc",
                    "http://publications.europa.eu/resource/authority/eurovoc",
                    model
                )
                testContext.completeNow()
            }
            .onFailure {
                testContext.failNow(it)
            }
    }

}