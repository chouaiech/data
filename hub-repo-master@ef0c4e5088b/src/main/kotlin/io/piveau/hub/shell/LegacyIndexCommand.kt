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
import io.vertx.core.Future
import io.vertx.core.Vertx
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
import kotlinx.coroutines.flow.*
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.vocabulary.DCTerms
import java.util.concurrent.atomic.AtomicInteger

class LegacyIndexCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore
    private val indexService: IndexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT)

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        command = CommandBuilder.command(
            CLI.create("legacyIndex")
                .addArgument(
                    Argument()
                        .setArgName("catalogueIds")
                        .setRequired(false)
                        .setMultiValued(true)
                        .setDescription("The ids of the catalogues to index")
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
        ).completionHandler { completion ->
            tripleStore.catalogueManager.listUris().onSuccess { list ->
                val ids = list.map { it.id }
                completion.candidatesCompletion(ids)
            }.onFailure { completion.complete("", false) }
        }.scopedProcessHandler(this::index).build(vertx)
    }

    private suspend fun index(process: CommandProcess) {
        var catalogueIds = process.commandLine().allArguments()
        if (catalogueIds.isEmpty()) {
            process.write("Indexing all catalogues\n")
            val uris = tripleStore.catalogueManager.listUris().await()
            catalogueIds = uris.map { it.id }
        }
        catalogueIds.forEach {
            indexCatalogue(it, process)
        }
        if (catalogueIds.size > 1) {
            process.write("Indexing of catalogues finished.\n")
        }
    }

    private suspend fun indexCatalogue(catalogueId: String, process: CommandProcess) {
        process.write("Indexing catalogue $catalogueId\n")

        val catalogueModel = tripleStore.catalogueManager.getStripped(catalogueId).await()
        val catalogueResource = catalogueModel.getResource(DCATAPUriSchema.createFor(catalogueId).catalogueUriRef)

        if (catalogueResource.hasProperty(EDP.visibility, EDP.hidden)) {
            process.write("Catalogue $catalogueId is hidden and skipped for indexing\n")
        } else {
            val catalogueIndex = indexingCatalogue(catalogueResource).await()
            Future.future { indexService.addCatalog(catalogueIndex, it) }.await()

            process.write("Catalogue $catalogueId metadata indexed successfully\n")

            val lang = catalogueModel.listObjectsOfProperty(DCTerms.language).next().asResource()
            val defaultLang =
                Languages.iso6391Code(Languages.getConcept(lang) ?: Languages.getConcept("ENG") as Concept) ?: "en"

            val datasets = tripleStore.catalogueManager.allDatasets(catalogueId).await()
            process.write("Indexing ${datasets.size} datasets\n")

            val counter = AtomicInteger(0)

            val chunkSize = process.commandLine().getOptionValue<String>("chunkSize")

            datasets.chunked(chunkSize.toInt()).forEach { chunk ->
                chunk.asFlow()
                    .map { Pair(it.uri, tripleStore.datasetManager.getGraph(it.uriRef).await()) }
                    .catch {
                        process.write("Get dataset failure: ${it.message}\n")
                        emit(Pair("", ModelFactory.createDefaultModel()))
                    }
                    .onEach { (uri, model) ->
                        if (uri.isNotBlank()) {
                            Future.future { promise ->
                                indexService.addDatasetPut(
                                    indexingDataset(
                                        model.getResource(uri),
                                        model.getResource(DCATAPUriSchema.parseUriRef(uri).recordUriRef),
                                        catalogueId,
                                        defaultLang
                                    ), promise
                                )
                            }.await()
                        }
                    }
                    .catch {
                        process.write("Indexing dataset failure: ${it.message}\n")
                        emit(Pair("", ModelFactory.createDefaultModel()))
                    }
                    .collect()

                process.write("\rIndexed ${counter.addAndGet(chunk.size)}")
            }
            process.write("\nDatasets of $catalogueId indexed successfully\n")

            // remove obsolete indexed datasets
            val indexIds = Future.future<JsonArray> { promise ->
                indexService.listDatasets(catalogueId, "dataset_write") { ar ->
                    if (ar.succeeded()) {
                        promise.complete(ar.result())
                    } else {
                        promise.fail(ar.cause())
                    }
                }
            }.await()

            val datasetIds = datasets.map { it.id }.toSet()
            process.write("${indexIds.size()} datasets in index, ${datasetIds.size} datasets in store\n")

            val invalidIds = indexIds - datasetIds
            invalidIds.asFlow()
                .collect {
                    Future.future<JsonObject> { p -> indexService.deleteDataset(it as String, p) }.await()
                }

            process.write("Indexing of $catalogueId finished\n")
        }
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return LegacyIndexCommand(vertx).command
        }
    }

}
