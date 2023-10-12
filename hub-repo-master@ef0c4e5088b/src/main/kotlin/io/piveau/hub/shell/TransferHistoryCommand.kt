package io.piveau.hub.shell

import io.piveau.dcatap.TripleStore
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.vocabularies.vocabulary.EDP
import io.vertx.core.Vertx
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.*
import org.apache.jena.vocabulary.DCTerms

class TransferHistoryCommand private constructor(vertx: Vertx) {

    private val command: Command

    private val tripleStore: TripleStore
    private val shadowTripleStore: TripleStore

    private val client: WebClient = WebClient.create(vertx)

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            client
        )
        shadowTripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG),
            client
        )
        command = CommandBuilder.command(
            CLI.create("transferMetricsHistory")
                .setDescription("Transfer metrics history graphs to a shadow system")
                .addOption(
                    Option().setArgName("help")
                        .setHelp(true)
                        .setFlag(true)
                        .setShortName("h")
                        .setLongName("help")
                        .setDescription("This help")
                )
                .addOption(
                    Option().setArgName("copyMode")
                        .setFlag(true)
                        .setShortName("c")
                        .setLongName("copyMode")
                        .setDescription("Just copy metrics history to the shadow system")
                )
        ).scopedProcessHandler { process ->

            if (!config.containsKey(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)) {
                process.write("Failure: No shadow system configured\n")
                return@scopedProcessHandler
            }

            val query = "SELECT ?g WHERE { GRAPH ?g { ?g <${DCTerms.type}> <${EDP.MetricsHistory}> } }"

//                val promise = Promise.promise<List<ResultSet>>()
//                tripleStore.recursiveSelect(query, 0, 1000, mutableListOf(), promise)
//                val resultSets = promise.future().await()
//
//                val graphs = resultSets.flatMap { it.asSequence().map { qr -> qr.getResource("g") } }.toList()
//                process.write("Found ${graphs.size} metrics history graphs\n")

            selectAsFlow(query, tripleStore)
                .buffer()
                .map {
                    it to tripleStore.getGraph(it).await()
                }.buffer().onEach { pair ->
                    shadowTripleStore.setGraph(pair.first, pair.second, false).await()
                    if (!process.commandLine().isFlagEnabled("copyMode")) {
                        tripleStore.deleteGraph(pair.first).await()
                        process.write("Successfully transferred ${pair.first}\n")
                    } else {
                        process.write("Successfully copied ${pair.first}\n")
                    }
                }.catch { cause ->
                    process.write("Exception caught: ${cause.message}\n")
                }.onCompletion { cause ->
                    if (cause != null) {
                        process.write("Metrics history graphs transfer finished with error: ${cause.message}\n")
                    } else {
                        process.write("Metrics history graphs transfer finished\n")
                    }
                }.collect()

//                // Use flows
//                flow {
//                    graphs.forEach {
//                            try {
//                                emit(Pair(it.uri, tripleStore.getGraph(it.uri).await()))
//                            } catch (e: Exception) {
//                                process.write("Error getting graph ${it.uri}: ${e.message}\n")
//                            }
//                        }
//                }.buffer().onEach { pair ->
//                    shadowTripleStore.setGraph(pair.first, pair.second).await()
//                    if (!process.commandLine().isFlagEnabled("copyMode")) {
//                        tripleStore.deleteGraph(pair.first)
//                        process.write("Successfully transferred ${pair.first}\n")
//                    } else {
//                        process.write("Successfully copied ${pair.first}\n")
//                    }
//                }.catch { cause ->
//                    process.write("Exception caught: ${cause.message}\n")
//                }.onCompletion { cause ->
//                    if (cause != null) {
//                        process.write("Metrics history graphs transfer finished with error: ${cause.message}\n").end()
//                    } else {
//                        process.write("Metrics history graphs transfer finished\n").end()
//                    }
//                }.collect()
        }.build(vertx)
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return TransferHistoryCommand(vertx).command
        }
    }

}

private fun selectAsFlow(query: String, tripleStore: TripleStore): Flow<String> = flow {
    var offset = 0
    do {
        val resultSet = tripleStore.select("$query LIMIT 1000 OFFSET $offset").await()
        val histories = resultSet.asSequence().map { qr -> qr.getResource("g") }.toList()
        histories.forEach {
            emit(it.uri)
        }
        offset += 1000
    } while (histories.isNotEmpty())
}