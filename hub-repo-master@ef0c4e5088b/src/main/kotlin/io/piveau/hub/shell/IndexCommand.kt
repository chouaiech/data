package io.piveau.hub.shell

import io.piveau.dcatap.DCATAPUriSchema
import io.piveau.dcatap.TripleStore
import io.piveau.hub.services.index.IndexService
import io.piveau.hub.Constants
import io.piveau.hub.indexing.indexingCatalogue
import io.piveau.hub.indexing.indexingDataset
import io.piveau.json.asJsonObject
import io.piveau.utils.candidatesCompletion
import io.piveau.vocabularies.Concept
import io.piveau.vocabularies.Languages
import io.piveau.vocabularies.vocabulary.EDP
import io.vertx.core.*
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.awaitEvent
import kotlinx.coroutines.flow.*
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.vocabulary.DCTerms
import java.util.concurrent.atomic.AtomicInteger

class IndexCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore
    private val catalogueManager
        get() = tripleStore.catalogueManager
    private val datasetManager
        get() = tripleStore.datasetManager
    private val indexService: IndexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT)

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        command = CommandBuilder.command(
            CLI.create("index")
                .addArgument(
                    Argument()
                        .setArgName("catalogueIds")
                        .setRequired(false)
                        .setMultiValued(true)
                        .setDescription("The ids of the catalogues to index")
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
                    Option().setArgName("help")
                        .setHelp(true)
                        .setFlag(true)
                        .setShortName("h")
                        .setLongName("help")
                        .setDescription("This help")
                )
                .addOption(
                    Option().setArgName("chunkSize")
                        .setRequired(false)
                        .setArgName("chunkSize")
                        .setDefaultValue("500")
                        .setShortName("c")
                        .setLongName("chunkSize")
                        .setDescription("The number of parallel requests")
                )
                .addOption(
                    Option().setArgName("checkInconsistency")
                        .setRequired(false)
                        .setFlag(true)
                        .setShortName("i")
                        .setLongName("checkInconsistency")
                        .setDescription("Check inconsistency for catalogues")
                )
                .addOption(
                    Option().setArgName("removeInconsistency")
                        .setRequired(false)
                        .setFlag(true)
                        .setShortName("r")
                        .setLongName("removeInconsistency")
                        .setDescription("Remove inconsistency for catalogues")
                )
                .addOption(
                    Option().setArgName("freshIndex")
                        .setRequired(false)
                        .setFlag(true)
                        .setShortName("f")
                        .setLongName("freshIndex")
                        .setDescription("Write to a fresh index")
                )
        ).completionHandler { completion ->
            catalogueManager.listUris().onSuccess { list ->
                val ids = list.map { it.id }.sorted()
                completion.candidatesCompletion(ids)
            }.onFailure { completion.complete("", false) }
        }.scopedProcessHandler(this::index).build(vertx)
    }

    private suspend fun index(process: CommandProcess) {
        var catalogueIds = process.commandLine().allArguments()
        if (process.commandLine().isFlagEnabled("checkInconsistency")) {
            if (catalogueIds.isEmpty()) {
                catalogueIds = extractCatalogueList(catalogueManager.listUris().await().map { it.id }.sorted(), process)
            }
            catalogueIds.forEach {
                checkInconsistency(it, process)
            }
        } else if (process.commandLine().isFlagEnabled("removeInconsistency")) {
            if (catalogueIds.isEmpty()) {
                catalogueIds = extractCatalogueList(catalogueManager.listUris().await().map { it.id }.sorted(), process)
            }
            catalogueIds.forEach {
                removeInconsistency(it, process)
            }
        } else {
            var subCatalogues = true
            if (catalogueIds.isEmpty()) {
                process.write("Indexing catalogues\n")
                catalogueIds = extractCatalogueList(catalogueManager.listUris().await().map { it.id }.sorted(), process)
                subCatalogues = false
            }
            catalogueIds.forEach {
                indexCatalogue(it, process, subCatalogues)
            }
            if (catalogueIds.size > 1) {
                process.write("Indexing of catalogues finished.\n")
            }
        }
    }

    private suspend fun indexCatalogue(catalogueId: String, process: CommandProcess, deep: Boolean) {
        process.write("Indexing catalogue $catalogueId\n")

        val catalogueModel = catalogueManager.getStripped(catalogueId).await()
        if (catalogueModel.isEmpty) {
            process.write("Catalogue $catalogueId not found\n")
            return
        }

        val subCatalogues = catalogueManager.subCatalogues(catalogueId).await().map { it.id }
        val catalogueResource = catalogueModel.getResource(DCATAPUriSchema.createFor(catalogueId).catalogueUriRef)

        if (deep && subCatalogues.isNotEmpty()) {
            subCatalogues.forEach { indexCatalogue(it, process, true) }
        }

        if (catalogueResource.hasProperty(EDP.visibility, EDP.hidden)) {
            process.write("Catalogue $catalogueId is hidden, and hence skipped for indexing\n")
        } else {
            val catalogueIndex = indexingCatalogue(catalogueResource).await()
            Future.future { indexService.addCatalog(catalogueIndex, it) }.await()

            process.write("Catalogue metadata for $catalogueId indexed successfully\n")

            val lang = catalogueModel.listObjectsOfProperty(DCTerms.language).next().asResource()
            val defaultLang =
                Languages.iso6391Code(Languages.getConcept(lang) ?: Languages.getConcept("ENG") as Concept) ?: "en"

            val datasets = catalogueManager.allDatasets(catalogueId).await().toSet()
            process.write("Indexing ${datasets.size} datasets\n")

            val counter = AtomicInteger(0)

            val chunkSize = process.commandLine().getOptionValue<String>("chunkSize").toInt()

            datasets.chunked(chunkSize).forEach { chunk ->
                val datasetsBulkRequest = JsonArray()

                chunk.asFlow()
                    .transform { uriSchema ->
                        try {
                            val model = datasetManager.getGraph(uriSchema.uriRef).await()
                            emit(uriSchema to model)
                        } catch (e: Exception) {
                            process.write("\nGet dataset failure: ${uriSchema.id} - ${e.message}\n")
                        }
                    }
                    .collect { (uriSchema, model) ->
                        try {
                            datasetsBulkRequest.add(
                                indexingDataset(
                                    model.getResource(uriSchema.datasetUriRef),
                                    model.getResource(uriSchema.recordUriRef),
                                    catalogueId,
                                    defaultLang
                                )
                            )
                        } catch (e: Exception) {
                            process.write("\nIndexing dataset failure: ${uriSchema.id} - ${e.message}\n")
                        }
                    }

                try {
                    val bulkResult = Future.future { promise ->
                        indexService.putDatasetBulk(datasetsBulkRequest, promise)
                    }.await()
                    bulkResult
                        .map { it as JsonObject }
                        .filter {
                            val status = it.getInteger("status")
                            status != 200 && status != 201
                        }
                        .forEach { dataset ->
                            process.write("\nIndexing dataset failure: ${dataset.getString("id")} " +
                                    "- ${dataset.getString("message")}\n")
                        }
                } catch(e: Exception) {
                    process.write("\nIndexing bulk request failure: ${e.message}\n")
                }

                process.write("\rProcessed ${counter.addAndGet(chunk.size)}")
            }
            process.write("\nDatasets of $catalogueId indexed successfully\n")

            if (process.commandLine().isFlagEnabled("freshIndex")) {
                process.write("Fresh index - skip deletion of obsolete indexed datasets\n")
            } else {
                // remove obsolete indexed datasets
                val indexIds = catalogueIndexIds(catalogueId)
                val datasetIds = catalogueStoreIds(catalogueId)

                val invalidIds = indexIds - datasetIds
                invalidIds.forEach {
                    try {
                        Future.future { p -> indexService.deleteDataset(it, p) }.await()
                    } catch (e: Exception) {
                        process.write("WARNING delete $it from index: ${e.message}\n")
                    }
                }
            }

            process.write("Indexing of $catalogueId finished\n")
        }
    }

    private suspend fun checkInconsistency(catalogueId: String, process: CommandProcess) {
        val catalogueModel = catalogueManager.getStripped(catalogueId).await()
        if (catalogueModel.isEmpty) {
            process.write("Catalogue $catalogueId not found\n")
            return
        }
        val catalogueResource = catalogueModel.getResource(DCATAPUriSchema.createForCatalogue(catalogueId).catalogueUriRef)
        if (catalogueResource.hasProperty(EDP.visibility, EDP.hidden)) {
            process.write("Catalogue $catalogueId is hidden and skipped\n")
        } else {
            process.write("Check inconsistency of $catalogueId\n")

            val indexIds = catalogueIndexIds(catalogueId)
            val datasetIds = catalogueStoreIds(catalogueId)

            if (indexIds.size != datasetIds.size) {
                process.write("Inconsistency for $catalogueId:\n")
                process.write("${indexIds.size} datasets in index, ${datasetIds.size} datasets in store\n")
            } else {
                process.write("No inconsistency for $catalogueId found\n")
            }
        }
    }

    private suspend fun removeInconsistency(catalogueId: String, process: CommandProcess) {
        val catalogueModel = catalogueManager.getStripped(catalogueId).await()
        if (catalogueModel.isEmpty) {
            process.write("Catalogue $catalogueId not found\n")
            return
        }
        val catalogueResource = catalogueModel.getResource(DCATAPUriSchema.createForCatalogue(catalogueId).catalogueUriRef)
        if (catalogueResource.hasProperty(EDP.visibility, EDP.hidden)) {
            process.write("Catalogue $catalogueId is hidden and skipped\n")
        } else {
            process.write("Check index inconsistency of $catalogueId\n")

            val indexIds = catalogueIndexIds(catalogueId)
            val datasetIds = catalogueStoreIds(catalogueId)
            process.write("${indexIds.size} datasets in index, ${datasetIds.size} datasets in store\n")

            val invalidIds = indexIds - datasetIds
            val missingIds = datasetIds - indexIds

            if (invalidIds.isNotEmpty()) {
                process.write("${invalidIds.size} datasets indexed but not existing\n")
                invalidIds.forEach {
                    Future.future { p -> indexService.deleteDataset(it, p) }.await()
                }
                process.write("${invalidIds.size} datasets deleted from index\n")
            }

            if (missingIds.isNotEmpty()) {
                process.write("${missingIds.size} datasets not indexed\n")

                val lang = catalogueModel.listObjectsOfProperty(DCTerms.language).next().asResource()
                val defaultLang =
                    Languages.iso6391Code(Languages.getConcept(lang) ?: Languages.getConcept("ENG") as Concept) ?: "en"

                val chunkSize = process.commandLine().getOptionValue<String>("chunkSize").toInt()

                val missingUriRefs = missingIds.map { DCATAPUriSchema.createForDataset(it) }.toSet()

                missingUriRefs.chunked(chunkSize).forEach { chunk ->
                    val datasetsBulkRequest = JsonArray()

                    chunk.asFlow()
                        .map { Pair(it, datasetManager.getGraph(it.graphName).await()) }
                        .catch {
                            process.write("Get dataset failure: ${it.message}\n")
                            emit(Pair(DCATAPUriSchema.createForDataset(""), ModelFactory.createDefaultModel()))
                        }
                        .onEach { (uri, model) ->
                            if (uri.id.isNotBlank()) {
                                try {
                                    datasetsBulkRequest.add(
                                        indexingDataset(
                                            model.getResource(uri.uriRef),
                                            model.getResource(uri.recordUriRef),
                                            catalogueId,
                                            defaultLang
                                        )
                                    )
                                } catch (e: Exception) {
                                    process.write("Indexing dataset failure: ${uri.id} - ${e.message}\n")
                                }
                            }
                        }
                        .catch {
                            process.write("Indexing dataset failure: ${it.message}\n")
                            emit(Pair(DCATAPUriSchema.createForDataset(""), ModelFactory.createDefaultModel()))
                        }
                        .collect()

                    val await = awaitEvent<AsyncResult<JsonArray>> { ar ->
                        indexService.putDatasetBulk(datasetsBulkRequest, ar)
                    }

                    if (await.succeeded()) {
                        await.result().forEach {
                            val datasetResult = it as JsonObject
                            val status = datasetResult.getInteger("status")
                            if (status != 200 && status != 201) {
                                process.write("Indexing dataset failure: ${datasetResult.getString("message")}\n")
                            }
                        }
                    } else {
                        process.write("Indexing dataset failure: ${await.cause().message}\n")
                    }
                }
            }
            if (invalidIds.isNotEmpty() || missingIds.isNotEmpty()) {
                process.write("Inconsistency removed\n")
            } else {
                process.write("No inconsistency for $catalogueId found\n")
            }
        }
    }

    private suspend fun catalogueIndexIds(catalogueId: String): Set<String> {
        return Future.future { promise ->
            indexService.listDatasets(catalogueId, "dataset_write") { ar ->
                if (ar.succeeded()) {
                    promise.complete(ar.result().map { it as String }.toSet())
                } else {
                    promise.fail(ar.cause())
                }
            }
        }.await()
    }

    private suspend fun catalogueStoreIds(catalogueId: String): Set<String> {
        val datasets = catalogueManager.allDatasets(catalogueId).await()
        return datasets.map { it.id }.toSet()
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return IndexCommand(vertx).command
        }
    }

}
