package io.piveau.hub.shell

import io.piveau.dcatap.*
import io.piveau.hub.Constants
import io.piveau.hub.services.catalogues.CataloguesService
import io.piveau.json.asJsonObject
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.presentAs
import io.piveau.utils.candidatesCompletion
import io.piveau.vocabularies.CorporateBodies
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.kotlin.coroutines.await
import org.apache.jena.rdf.model.RDFNode
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.riot.Lang
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.DCTerms
import org.apache.jena.vocabulary.RDF

/**
 * Command for fixing publisher in catalogues.
 *
 */
class FixPublisherCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore

    private val cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS)

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG)
        )

        command = CommandBuilder.command(
            CLI.create("fixPublisher")
                .addArgument(
                    Argument()
                        .setArgName("catalogueId")
                        .setRequired(false)
                        .setDescription("The id of the catalogue to fix the publisher")
                )
                .addOption(
                    Option()
                        .setFlag(true)
                        .setArgName("final")
                        .setLongName("final")
                        .setShortName("f")
                        .setDescription("Store back fixed catalogue")
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
        ).completionHandler { completion ->
            tripleStore.catalogueManager.listUris().onSuccess { list ->
                val ids = list.map { it.id }
                completion.candidatesCompletion(ids)
            }.onFailure { completion.complete("", true) }
        }.scopedProcessHandler(this::handleCommand).build(vertx)
    }

    private suspend fun handleCommand(process: CommandProcess) {
        val catalogueId: String? = process.commandLine().getArgumentValue("catalogueId")
        val catalogues = if (catalogueId == null) {
            tripleStore.catalogueManager.listUris().await().map { it.id }
        } else listOf(catalogueId)
        catalogues.forEach { fixCatalogue(it, process) }
    }

    private suspend fun fixCatalogue(catalogueId: String, process: CommandProcess) {
        process.write("Fixing publisher in catalogue $catalogueId...\n")
        val catalogueModel = tripleStore.catalogueManager.getStripped(catalogueId).await()
        setNsPrefixesFiltered(catalogueModel)
        if (process.commandLine().isFlagEnabled("verbose") || !process.commandLine().isFlagEnabled("final")) {
            process.write("Before:\n${catalogueModel.presentAs(Lang.TURTLE)}\n")
        }

        try {
            val catalogueResource =
                ResourceFactory.createResource(DCATAPUriSchema.createFor(catalogueId).catalogueUriRef)
            val catalogue = catalogueModel.getResource(catalogueResource.uri)

            if (catalogueModel.containsResource(catalogueResource)) {
                val foafNames = catalogueModel.listStatements(null, FOAF.name, null as RDFNode?)
                    .filterDrop { it.subject.isURIResource || catalogue.hasProperty(DCTerms.publisher, it.subject) }
                    .toList()
                val foafMBoxes = catalogueModel.listStatements(null, FOAF.mbox, null as RDFNode?)
                    .filterDrop { it.subject.isURIResource || catalogue.hasProperty(DCTerms.publisher, it.subject) }
                    .toList()
                val foafHomepages = catalogueModel.listStatements(null, FOAF.homepage, null as RDFNode?)
                    .filterDrop { it.subject.isURIResource || catalogue.hasProperty(DCTerms.publisher, it.subject) }
                    .toList()
                val dctTypes = catalogueModel.listStatements(null, DCTerms.type, null as RDFNode?)
                    .filterDrop { it.subject.isURIResource || catalogue.hasProperty(DCTerms.publisher, it.subject) }
                    .toList()

                catalogueModel.removeAll(null, RDF.type, FOAF.Agent)

                val publisher =
                    catalogue.getProperty(DCTerms.publisher)?.resource ?: catalogueModel.createResource(FOAF.Agent)
                        .also {
                            catalogue.addProperty(DCTerms.publisher, it)
                        }

                if (publisher.isURIResource && CorporateBodies.isConcept(publisher)) {
                    process.write("Publisher is a corporate body, doing nothing.\n")
                } else {
                    publisher.addProperty(RDF.type, FOAF.Agent)
                    foafNames.map { it.`object` }.forEach { publisher.addProperty(FOAF.name, it) }
                    foafMBoxes.map { it.`object` }.forEach { publisher.addProperty(FOAF.mbox, it) }
                    foafHomepages.map { it.`object` }.forEach { publisher.addProperty(FOAF.homepage, it) }
                    dctTypes.map { it.`object` }.forEach { publisher.addProperty(DCTerms.type, it) }

                    foafNames.forEach { catalogueModel.remove(it) }
                    foafMBoxes.forEach { catalogueModel.remove(it) }
                    foafHomepages.forEach { catalogueModel.remove(it) }
                    dctTypes.forEach { catalogueModel.remove(it) }

                    if (process.commandLine().isFlagEnabled("verbose") || !process.commandLine()
                            .isFlagEnabled("final")
                    ) {
                        process.write("After:\n${catalogueModel.presentAs(Lang.TURTLE)}\n")
                    }

                    if (process.commandLine().isFlagEnabled("final")) {
                        cataloguesService.putCatalogue(
                            catalogueId,
                            catalogueModel.presentAs(Lang.NTRIPLES),
                            RDFMimeTypes.NTRIPLES
                        ).await()
                        process.write("Fixed catalogue $catalogueId stored\n")
                    }
                }
            } else {
                process.write("Catalogue resource not found\n")
            }
        } catch (t: Throwable) {
            process.write("ERROR: ${t.message}\n")
        }
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return FixPublisherCommand(vertx).command
        }
    }

}