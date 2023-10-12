package io.piveau.hub.shell

import io.piveau.dcatap.*
import io.piveau.hub.dataobjects.DatasetHelper
import io.piveau.hub.services.translation.TranslationService
import io.piveau.hub.services.translation.TranslationServiceUtils
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.vocabularies.Concept
import io.piveau.vocabularies.Languages
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
import org.apache.jena.vocabulary.DCTerms

class TranslateCommand private constructor(vertx: Vertx) {
    private val command: Command

    private val catalogueManager: CatalogueManager
    private val datasetManager: DatasetManager

    private val translationService: TranslationService?

    init {
        val config = vertx.orCreateContext.config()
        val tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )

        catalogueManager = tripleStore.catalogueManager
        datasetManager = tripleStore.datasetManager

        translationService =
            if (config.asJsonObject(Constants.ENV_PIVEAU_TRANSLATION_SERVICE).getBoolean("enable", false)) {
                TranslationService.createProxy(vertx, TranslationService.SERVICE_ADDRESS)
            } else {
                null
            }

        command = CommandBuilder.command(
            CLI.create("translate")
                .addOption(
                    Option().setArgName("help")
                        .setHelp(true)
                        .setDescription("This help")
                        .setFlag(true)
                        .setShortName("h")
                        .setLongName("help")
                )
                .addOption(
                    Option().setArgName("force")
                        .setDescription("Forces translation also for already completed status.")
                        .setFlag(true)
                        .setShortName("f")
                        .setLongName("force")
                )
                .addArgument(
                    Argument()
                        .setArgName("objectType")
                        .setDescription("Object type of id. Possible values are 'dataset and 'catalogue'.")
                        .setRequired(true)
                )
                .addArgument(
                    Argument()
                        .setArgName("objectId")
                        .setDescription("The id of the object to translate")
                        .setMultiValued(true)
                        .setRequired(true)
                )
        ).scopedProcessHandler(this::translate).build(vertx)
    }

    private suspend fun translate(process: CommandProcess) {
        if (translationService != null) {
            val objectId = process.commandLine().getArgumentValue<String>("objectId")

            when (process.commandLine().getArgumentValue<String>("objectType")) {
                "dataset" -> translateDataset(objectId, process)
                "catalogue" -> translateCatalogue(objectId, process)
                else -> process.write("Unknown object type. Please, use 'dataset' or 'catalogue'.\n").end()
            }
        } else {
            process.write("[WARNING] Translation is disabled\n").end()
        }
    }

    private suspend fun translateDataset(datasetId: String, process: CommandProcess) {
        val datasetUriRef = DCATAPUriSchema.createForDataset(datasetId)
        val catalogueResource = datasetManager.catalogue(datasetUriRef.uriRef).await()
        val catalogueSchema = DCATAPUriSchema.parseUriRef(catalogueResource.uri)

        val datasetModel = datasetManager.getGraph(datasetUriRef.graphNameRef).await()

        val datasetHelper = DatasetHelper.create(datasetModel).await()

        val force = process.commandLine().isFlagEnabled("force")

        if (!force && TranslationServiceUtils.isTranslationCompleted(datasetHelper)) {
            process.write("[INFO] Translation already completed\n").end()
        } else {
            datasetHelper.catalogueId(catalogueSchema.id)
            datasetHelper.sourceLang(catalogueLanguage(catalogueSchema.id))

            translationService?.initializeTranslationProcess(datasetHelper, null, force) {
                if (it.succeeded()) {
                    process.write("[INFO] Translation initialized\n").end()
                } else {
                    process.write("[ERROR] Translation initialization: ${it.cause().message}\n").end()
                }
            }
        }
    }

    private suspend fun translateCatalogue(catalogueId: String, process: CommandProcess) {
        val defaultLanguage = catalogueLanguage(catalogueId)
        val force = process.commandLine().isFlagEnabled("force")

        catalogueManager
            .datasetsAsFlow(catalogueId)
            .map { model -> DatasetHelper.create(model).await() }
            .filterNot { helper ->
                (!force && TranslationServiceUtils.isTranslationCompleted(helper)).also {
                    if (it) process.write("[INFO] Dataset ${helper.piveauId()} already translated\n")
                }
            }
            .onCompletion { cause ->
                if (cause == null) process.write("[INFO] Catalogue translation finished\n")
                else process.write("[ERROR] Catalogue translation failure: ${cause.message}\n")
                process.end()
            }
            .collect { helper ->
                helper.catalogueId(catalogueId)
                helper.sourceLang(defaultLanguage)
                translationService?.initializeTranslationProcess(helper, null, force) {
                    if (it.failed()) {
                        process.write("[ERROR] Translation initialization failure for ${helper.piveauId()}: ${it.cause().message}\n")
                    }
                }
            }
    }

    private suspend fun catalogueLanguage(catalogueId: String): String? {
        val model = catalogueManager.getStripped(catalogueId).await()
        val resource = model.getResource(DCATAPUriSchema.createFor(catalogueId).catalogueUriRef)
        val language = resource.getPropertyResourceValue(DCTerms.language)
        val concept = if (Languages.isConcept(language)) {
            Languages.getConcept(language)
        } else {
            Languages.getConcept("ENG")
        }
        return Languages.iso6391Code(concept as Concept)
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return TranslateCommand(vertx).command
        }
    }

}