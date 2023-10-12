package io.piveau.hub.shell

import io.piveau.dcatap.TripleStore
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.rdf.presentAs
import io.piveau.vocabularies.vocabulary.DEFECTIVE_SPDX
import io.piveau.vocabularies.vocabulary.SPDX
import io.vertx.core.Vertx
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.*
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.RDF
import java.util.concurrent.atomic.AtomicLong

/**
 * Command for fixing the wrong SPDX namespace for checksums.
 *
 * It iterates over all datasets containing wrong namespace, fix it and stores it back to triple store
 */
class FixChecksumCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG)
        )

        command = CommandBuilder.command(
            CLI.create("fixChecksum")
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

        process.write("Found a total of ${totalCount()} defective checksums\n")

        val counter = AtomicLong(0)

        val limit = process.commandLine().getOptionValue<String>("pageSize").toLong()
        val verbose = process.commandLine().isFlagEnabled("verbose")

        do {
            val graphList = nextPage(limit)

            graphList.asFlow()
                .transform {
                    try {
                        emit(it to tripleStore.getGraph(it).await())
                    } catch (e: Exception) {
                        process.write("\nERROR for $it: ${if (verbose) e.stackTraceToString() else e.message}\n")
                    }
                }
                .collect { (graphName, model) ->
                    fixChecksum(model)
                    try {
                        val result = tripleStore.setGraph(graphName, model, false).await()
                        if (result != "updated") {
                            process.write("\nUnexpected result for $graphName: $result\n")
                            if (verbose) {
                                process.write("\n${model.presentAs(Lang.TURTLE)}\n")
                            }
                        }
                    } catch (e: Exception) {
                        process.write(
                            "\nERROR storing back $graphName: ${if (verbose) e.stackTraceToString() else e.message}\n"
                        )
                    }
                }

            if (process.isForeground) {
                process.write("\rFixed ${graphList.size + counter.get()} checksums")
            }
            counter.addAndGet(limit)
        } while (graphList.size.toLong() == limit)

        process.write("\n")
    }

    private suspend fun totalCount(): Long {
        val resultSet = tripleStore.select(totalCountQuery).await()
        return resultSet.asSequence().map { it.getLiteral("count").long }.first()
    }

    private suspend fun nextPage(limit: Long): List<String> {
        val resultSet = tripleStore.select("$query limit $limit").await()
        return resultSet.asSequence().map { it.getResource("graph").uri }.toList()
    }

    private fun fixChecksum(model: Model) {
        // Keep and safe hash value
        val statements = model.listStatements(null, DEFECTIVE_SPDX.checksumValue, null as RDFNode?)
        val hash = statements.filterKeep { it.`object`.isLiteral }.mapWith { it.`object`.asLiteral().lexicalForm }
            .nextOptional().orElse("")

        // Clear debris
        model.removeAll(null, DEFECTIVE_SPDX.checksumValue, null)
        model.removeAll(null, DEFECTIVE_SPDX.checksum, null)
        model.removeAll(null, DEFECTIVE_SPDX.algorithm, null)
        model.removeAll(null, RDF.type, DEFECTIVE_SPDX.Checksum)

        // Get record and create new checksum
        if (hash.isNotBlank()) {
            model.listSubjectsWithProperty(RDF.type, DCAT.CatalogRecord).nextOptional().ifPresent {
                val checksum = model.createResource(SPDX.Checksum)
                checksum.addProperty(SPDX.checksumValue, hash)
                checksum.addProperty(SPDX.algorithm, SPDX.checksumAlgorithm_md5)

                it.addProperty(SPDX.checksum, checksum)
            }
        }
    }

    companion object {

        fun create(vertx: Vertx): Command {
            return FixChecksumCommand(vertx).command
        }

        val totalCountQuery = """
            select count(distinct ?graph) as ?count where
            {
                graph ?graph
                {
                    ?s <https://spdx.org/rdf/terms/#checksum>|<https://spdx.org/rdf/terms/#checksumValue> ?o 
                }
            }
        """.trimIndent()

        val query = """
            select distinct ?graph where
            {
                graph ?graph
                {
                    ?s <https://spdx.org/rdf/terms/#checksum>|<https://spdx.org/rdf/terms/#checksumValue> ?o 
                }
            }
        """.trimIndent()
    }

}
