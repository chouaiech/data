package io.piveau.hub.shell

import io.piveau.dcatap.*
import io.piveau.hub.services.index.IndexService
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.rdf.asNormalized
import io.piveau.utils.candidatesCompletion
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.transform
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.DCTerms
import java.util.concurrent.atomic.AtomicInteger

class RemoveDuplicatesCommand private constructor(vertx: Vertx) {
    private val indexService: IndexService =
        IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT)
    private val tripleStore: TripleStore

    private val catalogueManager: CatalogueManager
        get() = tripleStore.catalogueManager
    private val datasetManager: DatasetManager
        get() = tripleStore.datasetManager

    private val shadowTripleStore: TripleStore
    private val command: Command

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        shadowTripleStore = if (config.containsKey(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)) TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        else tripleStore

        command = CommandBuilder.command(
            CLI.create("removeDups")
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
                .setDescription("Remove duplicate datasets from catalogue")
        ).completionHandler { completion ->
            catalogueManager
                .listUris()
                .onSuccess { list ->
                    val ids = list.map { it.id }
                    completion.candidatesCompletion(ids)
                }
                .onFailure { completion.complete("", true) }
        }.scopedProcessHandler(this::handleCommand).build(vertx)
    }

    private suspend fun handleCommand(process: CommandProcess) {
        val catalogues = extractCatalogueList(catalogueManager.listUris().await().map { it.id }.sorted(), process)
        catalogues.forEach { removeDuplicates(it, process, catalogues.size == 1) }
    }

    private suspend fun removeDuplicates(catalogueId: String, process: CommandProcess, travers: Boolean) {

        val catalogues = mutableListOf(DCATAPUriSchema.createForCatalogue(catalogueId))

        if (travers) {
            val subCatalogues = catalogueManager.subCatalogues(catalogueId).await()
            if (subCatalogues.isNotEmpty()) {
                process.write("Found ${subCatalogues.size} sub-catalogues\n")
            }
            catalogues.addAll(subCatalogues)
        }

        catalogues.forEach { catalogueUriRef ->
            val identifiers = catalogueManager.allDatasetIdentifiers(catalogueUriRef.id).await().toSet()
            val datasets = catalogueManager.allDatasets(catalogueUriRef.id).await()
                .toMutableSet()

            process.write("Found ${identifiers.size} identifiers in ${catalogueUriRef.id}\n")
            process.write("Found ${datasets.size} datasets in ${catalogueUriRef.id}\n")

            process.write("Check 0")
            val counter = AtomicInteger()

            identifiers.asFlow()
                .transform { identifier ->
                    process.write("\rCheck ${counter.incrementAndGet()}")
                    val query =
                        """SELECT DISTINCT ?d WHERE { GRAPH <${catalogueUriRef.graphNameRef}> { <${catalogueUriRef.uriRef}> <${DCAT.record}> ?r } GRAPH ?g { ?r <${DCTerms.identifier}> "$identifier" ; <${FOAF.primaryTopic}> ?d } }"""
                    val resultSet = tripleStore.select(query).await()
                    val duplicates =
                        resultSet.asSequence().map { DCATAPUriSchema.parseUriRef(it.getResource("d").uri) }.toSet()
                    datasets.removeAll(duplicates)
                    if (duplicates.size > 1 || identifier.isBlank()) {
                        process.write("\n$identifier exists ${duplicates.size} times. keeping only one, deleting the rest.\n")

                        // We can keep at least the one where origin id is equal to the normalized id
                        emit(
                            duplicates
                                .filterNot { it.id == identifier.asNormalized() }
                                .toSet()
                        )
                    }
                }.transform { duplicates ->
                    process.write("Duplicates: ${duplicates.joinToString(",") { it.id }}\n")
                    duplicates.forEach { datasetUriRef ->
                        deleteDataset(datasetUriRef, catalogueUriRef).await()
                        process.write("Removed duplicate ${datasetUriRef.id}\n")
                        emit(datasetUriRef)
                    }
                }.collect { process.write("Dataset ${it.datasetUriRef} duplicates removed\n") }

            process.write("\n")

            if (datasets.isNotEmpty()) {
                process.write("Datasets without original identifier:\n${datasets.joinToString(",") { it.id }}\n")
                datasets.forEach { datasetUriRef ->
                    deleteDataset(datasetUriRef, catalogueUriRef).await()
                    process.write("Deleted ${datasetUriRef.id}\n")
                }
            }
        }
    }

    private fun deleteDataset(
        datasetUriRef: DCATAPUriRef,
        catalogueUriRef: DCATAPUriRef
    ): Future<Void> {
        datasetManager.deleteGraph(datasetUriRef.graphNameRef)
        tripleStore.deleteGraph(datasetUriRef.metricsGraphName)
        shadowTripleStore.deleteGraph(datasetUriRef.historicMetricsGraphName)
        indexService.deleteDataset(datasetUriRef.id) {}

        return catalogueManager
            .removeDatasetEntry(catalogueUriRef.uriRef, datasetUriRef.uriRef)
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return RemoveDuplicatesCommand(vertx).command
        }
    }

}
