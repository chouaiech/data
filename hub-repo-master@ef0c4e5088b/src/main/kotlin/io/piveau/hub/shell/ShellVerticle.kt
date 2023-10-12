package io.piveau.hub.shell

import io.piveau.hub.Constants
import io.piveau.json.asJsonObject
import io.vertx.core.CompositeFuture
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.ShellService
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.shell.command.CommandRegistry
import io.vertx.ext.shell.term.HttpTermOptions
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
class ShellVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val shellServiceOptions = ShellServiceOptions()
            .setWelcomeMessage("\n  Welcome to the piveau-hub-repo CLI!\n\n")
            .setSessionTimeout(21600000).apply {
                config.asJsonObject(Constants.ENV_PIVEAU_HUB_SHELL_CONFIG).forEach {
                    val options = it.value as JsonObject
                    when (it.key) {
                        "telnet" -> telnetOptions = TelnetTermOptions()
                            .setHost(options.getString("host", "0.0.0.0"))
                            .setPort(options.getInteger("port", 5000))
                        "http" -> httpOptions = HttpTermOptions()
                            .setHost(options.getString("host", "0.0.0.0"))
                            .setPort(options.getInteger("port", 8085))
                    }
                }
            }

        ShellService.create(vertx, shellServiceOptions).start()

        val registry = CommandRegistry.getShared(vertx)
        val futures = listOf(
            registry.registerCommand(InstallVocabulariesCommand.create(vertx)),
            registry.registerCommand(LegacyIndexCommand.create(vertx)),
            registry.registerCommand(IndexCommand.create(vertx)),
            registry.registerCommand(KeycloakCommand.create(vertx)),
            registry.registerCommand(ClearCatalogueCommand.create(vertx)),
            registry.registerCommand(RepairCatalogueCommand.create(vertx)),
            registry.registerCommand(LaunchCatalogueCommand.create(vertx)),
            registry.registerCommand(SyncScoreCommand.create(vertx)),
            registry.registerCommand(VocabularyCommand.create(vertx)),
            registry.registerCommand(RemoveDuplicatesCommand.create(vertx)),
            registry.registerCommand(TransferHistoryCommand.create(vertx)),
            registry.registerCommand(TranslateCommand.create(vertx)),
            registry.registerCommand(ZombieDatasetsCommand.create(vertx)),
            registry.registerCommand(FixPublisherCommand.create(vertx)),
            registry.registerCommand(FixChecksumCommand.create(vertx))
        )
        CompositeFuture.join(futures).await()
    }

}

@OptIn(ExperimentalTime::class)
fun CommandBuilder.scopedProcessHandler(handler: suspend (process: CommandProcess) -> Unit): CommandBuilder =
    processHandler { process ->
        val scope = CoroutineScope(process.vertx().dispatcher() as CoroutineContext)
        scope.launch {
            val duration = measureTime {
                handler(process)
            }
            process.write("Command finished in $duration\n").end()
        }
        process.endHandler { scope.cancel() }
        process.interruptHandler { process.write("Process interrupted\n").end() }
    }
