package io.piveau.hub.shell

import io.piveau.dcatap.*
import io.piveau.hub.Constants
import io.piveau.json.ConfigHelper
import io.piveau.json.asJsonObject
import io.piveau.pipe.PipeLauncher
import io.piveau.pipe.PiveauCluster
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.presentAs
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.apache.jena.query.DatasetFactory
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.DCTerms
import org.apache.jena.vocabulary.RDF
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class LaunchCatalogueCommand private constructor(vertx: Vertx) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val command: Command
    private val tripleStore: TripleStore

    private val catalogueManager
        get() = tripleStore.catalogueManager

    private val metricsManager
        get() = tripleStore.datasetManager

    private lateinit var pipeLauncher: PipeLauncher

    init {
        val config = vertx.orCreateContext.config()
        val clusterConfig = ConfigHelper.forConfig(config).forceJsonObject(Constants.ENV_PIVEAU_CLUSTER_CONFIG)
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx),
            "launch-pipe"
        )

        PiveauCluster.create(vertx, clusterConfig)
            .compose { cluster -> cluster.pipeLauncher(vertx) }
            .onSuccess { launcher -> pipeLauncher = launcher }
            .onFailure { cause -> log.error("Create launcher", cause) }

        command = CommandBuilder.command(
            CLI.create("launch")
                .addArgument(
                    Argument()
                        .setArgName("catalogueId")
                        .setRequired(true)
                        .setDescription("The id of the catalogues to launch pipe with.")
                )
                .addArgument(
                    Argument()
                        .setArgName("pipeName")
                        .setRequired(true)
                        .setDescription("The name of the pipe to launch.")
                )
                .addOption(
                    Option()
                        .setArgName("pulse")
                        .setShortName("p")
                        .setLongName("pulse")
                        .setDefaultValue("0")
                        .setDescription("Pulse of pipe feeding in milliseconds")
                )
                .addOption(Option().setFlag(true).setArgName("update").setShortName("u").setLongName("update"))
                .addOption(
                    Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help")
                )
                .addOption(Option().setFlag(true).setArgName("verbose").setShortName("v").setLongName("verbose"))
        ).scopedProcessHandler(::launch).build(vertx)
    }

    private suspend fun launch(process: CommandProcess) {
        val commandLine = process.commandLine()

        val catalogueId: String = commandLine.getArgumentValue(0)
        if (!catalogueManager.exists(catalogueId).await()) {
            process.write("Catalogue $catalogueId does not exist.\n")
            return
        }

        val pipeName: String = commandLine.getArgumentValue(1)
        if (!pipeLauncher.isPipeAvailable(pipeName)) {
            process.write("Pipe $pipeName not available.\n")
            return
        }

        process.write("Start pipe $pipeName for catalogue $catalogueId\n")

        val pulse = commandLine.getOptionValue<String>("pulse").toLongOrNull() ?: 0L

        val verbose = commandLine.isFlagEnabled("verbose")
        val update = commandLine.isFlagEnabled("update")

        val catalogueModel = catalogueManager.getStripped(catalogueId).await()
        if (verbose) {
            process.write("Catalogue metadata fetched successfully\n")
        }
        val catalogue = catalogueModel.getResource(DCATAPUriSchema.createFor(catalogueId).catalogueUriRef)
        val source = catalogue.getProperty(DCTerms.type).literal.lexicalForm
        val dataInfo = JsonObject()
            .put("catalogue", catalogueId)
            .put("source", source)

        val datasetsList = catalogueManager.allDatasets(catalogueId).await()
        dataInfo.put("total", datasetsList.size)
        if (verbose) {
            process.write("Using data info ${dataInfo.encode()}\n")
        }

        val counter = AtomicInteger(0)

        catalogueManager.datasetsAsFlow(catalogueId)
            .onEach { delay(pulse) }
            .map { datasetModel ->
                val dataset = datasetModel.listResourcesWithProperty(RDF.type, DCAT.Dataset).nextResource()
                if (verbose) {
                    process.write("Dataset ${dataset.uri} fetched\n")
                }
                val metricsGraphName = DCATAPUriSchema.parseUriRef(dataset.uri).metricsGraphName
                val metricsModel =
                    metricsManager.getGraph(metricsGraphName).await()
                val metrics = metricsModel.getResource(metricsGraphName)
                dataset to metrics
            }
            .map { (dataset, metrics) ->
                val jenaDataset = DatasetFactory.create(dataset.model)
                if (update) {
                    jenaDataset.addNamedModel(metrics.uri, metrics.model)
                }

                pipeLauncher.runPipeWithData(
                    pipeName,
                    jenaDataset.presentAs(Lang.TRIG),
                    RDFMimeTypes.TRIG,
                    dataInfo.copy()
                        .put("identifier", DCATAPUriSchema.parseUriRef(dataset.uri).id)
                        .put("counter", counter.incrementAndGet())
                ).await()
            }
            .flowOn(Dispatchers.IO)
            .collect {
                if (verbose) {
                    process.write("Dataset launched:\n${it.encodePrettily()}\n")
                }
            }

        process.write("Catalogue $catalogueId finished\n")
    }

    companion object {
        fun create(vertx: Vertx) = LaunchCatalogueCommand(vertx).command
    }
}
