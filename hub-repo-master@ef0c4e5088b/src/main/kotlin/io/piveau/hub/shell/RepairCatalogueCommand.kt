package io.piveau.hub.shell

import io.piveau.dcatap.DCATAPUriSchema
import io.piveau.dcatap.TripleStore
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.utils.candidatesCompletion
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.*
import org.apache.jena.vocabulary.DCAT

class RepairCatalogueCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore
    private val catalogueManager
        get() = tripleStore.catalogueManager

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )

        command = CommandBuilder.command(
            CLI.create("repair")
                .addArgument(
                    Argument()
                        .setArgName("catalogueId")
                        .setRequired(false)
                        .setDescription("The id of the catalogue")
                )
                .addOption(
                    Option()
                        .setArgName("offset")
                        .setLongName("offset")
                        .setShortName("o")
                        .setDefaultValue("0")
                        .setDescription("Catalogue list offset")
                        .setSingleValued(true)
                )
                .addOption(
                    Option()
                        .setArgName("limit")
                        .setLongName("limit")
                        .setShortName("l")
                        .setDefaultValue("500")
                        .setDescription("Catalogue list limit")
                        .setSingleValued(true)
                )
                .addOption(
                    Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help")
                        .setDescription("This help")
                )
        ).completionHandler { completion ->
            catalogueManager.listUris().onSuccess { list ->
                val ids = list.map { it.id }
                completion.candidatesCompletion(ids)
            }.onFailure { completion.complete("", true) }
        }.scopedProcessHandler(this::handleCommand).build(vertx)
    }

    private suspend fun handleCommand(process: CommandProcess) {
        val catalogues = extractCatalogueList(catalogueManager.listUris().await().map { it.id }.sorted(), process)
        catalogues.forEach { repairCatalogue(it, process, catalogues.size == 1) }
    }

    private suspend fun repairCatalogue(catalogueId: String, process: CommandProcess, travers: Boolean) {
        val catalogues = mutableListOf(DCATAPUriSchema.createForCatalogue(catalogueId))

        if (travers) {
            val subCatalogues = catalogueManager.subCatalogues(catalogueId).await()
            if (subCatalogues.isNotEmpty()) {
                process.write("Found ${subCatalogues.size} sub-catalogues\n")
            }
            catalogues.addAll(subCatalogues)
        }

        catalogues
            .forEach { catalogueUriRef ->
                val datasets = catalogueManager.allDatasets(catalogueUriRef.id).await().toSet()
                process.write("Found ${datasets.size} dataset entries in catalogue ${catalogueUriRef.id}\n")
                val validRecords = mutableSetOf<String>()
                datasets.asFlow()
                    .transform {
                        try {
                            val exists = tripleStore.datasetManager.existGraph(it.graphNameRef).await()
                            when {
                                exists -> emit(it)
                                else -> {
                                    process.write("Dataset ${it.id} does not exist. Removing from catalogue\n")
                                    catalogueManager.removeDatasetEntry(catalogueUriRef.uriRef, it.uriRef)
                                }
                            }
                        } catch (e: Exception) {
                            process.write("Dataset ${it.id} exists check failure: ${e.message}\n")
                        }
                    }
                    .collect { datasetUriRef ->
                        try {
                            catalogueManager
                                .addDatasetEntry(
                                    catalogueUriRef.graphName,
                                    catalogueUriRef.uriRef,
                                    datasetUriRef.uriRef,
                                    datasetUriRef.recordUriRef
                                ).await()

                            validRecords.add(datasetUriRef.recordUriRef)
                        } catch(e: Exception) {
                            process.write("Dataset ${datasetUriRef.id} refresh failure: ${e.message}\n")
                        }
                    }

                val allRecords = catalogueManager.allRecords(catalogueUriRef.id).await().toSet()
                process.write("Found ${allRecords.size} record entries in catalogue ${catalogueUriRef.id}\n")
                allRecords.asFlow()
                    .filterNot { validRecords.contains(it.recordUriRef) }
                    .collect {
                        try {
                            tripleStore.update(
                                "DELETE DATA { GRAPH <${catalogueUriRef.graphNameRef}> { <${catalogueUriRef.uriRef}> <${DCAT.record}> <${it.uriRef}> } }"
                            ).await()
                        } catch (e: Exception) {
                            process.write("ERROR removing record entry for ${it.id}: ${e.message}\n")
                        }
                    }
            }
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return RepairCatalogueCommand(vertx).command
        }
    }

}
