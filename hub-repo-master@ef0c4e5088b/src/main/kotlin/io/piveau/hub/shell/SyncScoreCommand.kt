package io.piveau.hub.shell

import io.piveau.dcatap.DCATAPUriSchema.parseUriRef
import io.piveau.dcatap.TripleStore
import io.piveau.hub.services.index.IndexService
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.vocabularies.vocabulary.DQV
import io.piveau.vocabularies.vocabulary.EDP
import io.piveau.vocabularies.vocabulary.PV
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.web.client.WebClient
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.DCTerms
import java.util.concurrent.atomic.AtomicInteger

class SyncScoreCommand private constructor(vertx: Vertx) {
    private val indexService: IndexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, 360000L)
    private val tripleStore: TripleStore
    private val command: Command

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        command = CommandBuilder.command(
            CLI.create("syncScore")
                .addOption(
                    Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help")
                        .setDescription("This help")
                )
                .setDescription("Synchronize score values between triple store and index.")
        ).processHandler { process: CommandProcess ->
            val queryMetrics = """
                PREFIX pv:      <${PV.NS}>
                PREFIX dqv:     <${DQV.NS}>
                PREFIX dcat:    <${DCAT.NS}>
                PREFIX dct:     <${DCTerms.NS}>
                PREFIX edp:     <${EDP.NS}>
                
                SELECT distinct ?ds ?score
                WHERE
                {
                    GRAPH ?g
                    {
                        ?ds a dcat:Dataset
                    }
                    
                    GRAPH ?g2
                    {
                        ?g2 dct:type edp:MetricsLatest .

                        ?ds dqv:hasQualityMeasurement [
                            dqv:isMeasurementOf pv:scoring ;
                            dqv:value ?score
                        ]
                    }
                } LIMIT 500"""

            process.session().put("successCounter", AtomicInteger())
            process.session().put("failureCounter", AtomicInteger())

            nextPage(0, queryMetrics, process)

        }.build(vertx)
    }

    private fun nextPage(offset: Int, query: String, process: CommandProcess) {
        tripleStore.select("$query OFFSET $offset").onSuccess {
            if (it.hasNext()) {
                val futures = mutableListOf<Future<JsonObject>>()
                while (it.hasNext()) {
                    val next = it.next()
                    if (next.contains("ds")) {
                        val ds = next.getResource("ds").uri
                        val score = next.getLiteral("score").int
                        val promise = Promise.promise<JsonObject>()
                        indexService.modifyDataset(
                            parseUriRef(ds).id, JsonObject().put(
                                "quality_meas",
                                JsonObject().put("scoring", score)
                            ), promise
                        )
                        promise.future()
                            .onFailure { cause ->
                                process.write("Indexing score of ${parseUriRef(ds).id} failed: ${cause.message}\n")
                                process.session().get<AtomicInteger>("failureCounter").incrementAndGet()
                            }
                            .onSuccess {
                                process.session().get<AtomicInteger>("successCounter").incrementAndGet()
                                process.write("Indexed score $score for ${parseUriRef(ds).id}\n")
                            }
                        futures.add(promise.future())
                    }
                }
                CompositeFuture.join(futures.toList()).onComplete {
                    process.write("${process.session().get<AtomicInteger>("successCounter").get()} scores successfully indexed, ${process.session().get<AtomicInteger>("failureCounter").get()} failures\n")
                    nextPage(offset + 500, query, process)
                }
            } else {
                process.write("Finished!\n").end()
            }
        }.onFailure { cause -> process.write("Error: ${cause.message}\n").end() }
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return SyncScoreCommand(vertx).command
        }
    }

}