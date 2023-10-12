package io.piveau.hub.shell

import io.piveau.dcatap.DCATAPUriSchema
import io.piveau.dcatap.TripleStore
import io.piveau.hub.Constants
import io.piveau.hub.services.datasets.DatasetsService
import io.piveau.json.asJsonObject
import io.vertx.core.Vertx
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Command for fixing the wrong SPDX namespace for checksums.
 *
 * It iterates over all datasets containing wrong namespace, fix it and stores it back to triple store
 */
class ZombieDatasetsCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore
    private val shadowTripleStore: TripleStore

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG)
        )
        shadowTripleStore = if (config.containsKey(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)) TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)
        )
        else tripleStore

        command = CommandBuilder.command(
            CLI.create("zombies")
                .setSummary("Find and kill zombie datasets")
                .setDescription("Detect dataset graphs without a relation to any catalogue and delete them")
                .addOption(
                    Option()
                        .setArgName("pageSize")
                        .setLongName("pageSize")
                        .setShortName("p")
                        .setDefaultValue("100")
                        .setDescription("Page size (default 100)")
                        .setSingleValued(true)
                )
                .addOption(
                    Option()
                        .setFlag(true)
                        .setArgName("verbose")
                        .setLongName("verbose")
                        .setShortName("v")
                        .setDescription("Verbose output")
                )
                .addOption(
                    Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help")
                        .setDescription("This help")
                )
        ).scopedProcessHandler(this::handleCommand).build(vertx)
    }

    private suspend fun handleCommand(process: CommandProcess) {

        process.write("Found a total of ${totalCount()} zombies lurking around\n")

        val counter = AtomicLong(0)

        val limit = process.commandLine().getOptionValue<String>("pageSize").toLong()
        val verbose = process.commandLine().isFlagEnabled("verbose")

        do {
            val graphList = nextPage(limit)

            graphList.asFlow()
                .map { DCATAPUriSchema.parseUriRef(it) }
                .collect { graphUriRef ->
                    tripleStore.deleteGraph(graphUriRef.graphNameRef)
                        .onFailure {
                            if (it.message?.startsWith("404") != true) {
                                process.write(
                                    "\nERROR zombie ${graphUriRef.graphNameRef} will just not die: ${if (verbose) it.stackTraceToString() else it.message}\n"
                                )
                            }
                        }
                    tripleStore.deleteGraph(graphUriRef.metricsGraphName)
                        .onFailure {
                            if (it.message?.startsWith("404") != true) {
                                process.write(
                                    "\nERROR zombie ${graphUriRef.metricsGraphName} will just not die: ${if (verbose) it.stackTraceToString() else it.message}\n"
                                )
                            }
                        }
                    shadowTripleStore.deleteGraph(graphUriRef.historicMetricsGraphName)
                        .onFailure {
                            if (it.message?.startsWith("404") != true) {
                                process.write(
                                    "\nERROR zombie ${graphUriRef.historicMetricsGraphName} will just not die: ${if (verbose) it.stackTraceToString() else it.message}\n"
                                )
                            }
                        }
                }

            if (process.isForeground) {
                process.write("\rKilled ${graphList.size + counter.get()} zombies")
            }
            counter.addAndGet(limit)
        } while (graphList.size.toLong() == limit)

        process.write("\n")

        removeOldDuplicates(process)
    }

    private suspend fun totalCount(): Long {
        val resultSet = tripleStore.select(totalCountQuery).await()
        return resultSet.next().getLiteral("count").long
    }

    private suspend fun nextPage(limit: Long): List<String> {
        val resultSet = tripleStore.select("$query limit $limit").await()
        return resultSet.asSequence().map { it.getResource("graph").uri }.toList()
    }

    private suspend fun removeOldDuplicates(process: CommandProcess) {

        process.write("Removing '~*' identifiers\n")
        val datasetsService = DatasetsService.createProxy(process.vertx(), DatasetsService.SERVICE_ADDRESS)

        val result = tripleStore.select(old).await()
        result.forEach { querySolution ->
            val dataset = querySolution.getResource("dataset")
            process.write("Delete dataset ${dataset.uri}")
            datasetsService.deleteDataset(DCATAPUriSchema.parseUriRef(dataset.uri).id).await()
            process.write(" done\n")
        }
    }

    companion object {

        fun create(vertx: Vertx): Command {
            return ZombieDatasetsCommand(vertx).command
        }

        val totalCountQuery = """
            select count(distinct ?graph) as ?count where
            {
                graph ?graph
                {
                    ?d a <http://www.w3.org/ns/dcat#Dataset> 
                }
                filter(!EXISTS { ?c <http://www.w3.org/ns/dcat#dataset> ?d })
            }
        """.trimIndent()

        val query = """
            select distinct ?graph where
            {
                graph ?graph
                {
                    ?d a <http://www.w3.org/ns/dcat#Dataset> 
                }
                filter(!EXISTS { ?c <http://www.w3.org/ns/dcat#dataset> ?d })
            }
        """.trimIndent()

        val old = """
            select distinct ?dataset where
            {
                ?dataset a <http://www.w3.org/ns/dcat#Dataset> 
                filter(contains(str(?dataset), '~*'))
            }
        """.trimIndent()

    }

}
