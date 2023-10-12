package io.piveau.hub.shell

import io.piveau.dcatap.TripleStore
import io.piveau.hub.security.KeyCloakService
import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.piveau.utils.candidatesCompletion
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.await
import kotlin.time.ExperimentalTime

@ExperimentalTime
class KeycloakCommand private constructor(vertx: Vertx) {
    private val command: Command
    private val tripleStore: TripleStore
    private val keycloakService: KeyCloakService = KeyCloakService.createProxy(vertx, KeyCloakService.SERVICE_ADDRESS)

    init {
        val config = vertx.orCreateContext.config()
        tripleStore = TripleStore(
            vertx,
            config.asJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
            WebClient.create(vertx)
        )
        command = CommandBuilder.command(
            CLI.create("syncKeycloakGroups")
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
        ).completionHandler { completion ->
            tripleStore.catalogueManager.listUris().onSuccess { list ->
                val ids = list.map { it.id }
                completion.candidatesCompletion(ids)
            }.onFailure { completion.complete("", false) }
        }.scopedProcessHandler { process ->
            var catalogueIds = process.commandLine().allArguments()
            if (catalogueIds.isEmpty()) {
                process.write("Syncing all catalogues\n")
                val uris = tripleStore.catalogueManager.listUris().await()
                catalogueIds = uris.map { it.id }
            }
            catalogueIds.forEach {
                process.write("Syncing catalogue $it\n")
                keycloakService.createResource(it)
            }
            if (catalogueIds.size > 1) {
                process.write("Syncing of catalogues finished.\n")
            }
        }.build(vertx)
    }

    companion object {
        fun create(vertx: Vertx): Command {
            return KeycloakCommand(vertx).command
        }
    }

}
