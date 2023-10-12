package io.piveau.hub.shell

import io.piveau.hub.vocabularies.installVocabularies
import io.vertx.core.Vertx
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.kotlin.coroutines.await

class InstallVocabulariesCommand private constructor(vertx: Vertx) {

    private val command: Command

    init {
        command = CommandBuilder.command(
            CLI.create("installVocabularies")
                .setSummary("Install a selected list of vocabularies.")
                .addOption(
                    Option()
                        .setArgName("override")
                        .setShortName("o")
                        .setLongName("override")
                        .setDescription("Override any existing graphs and indexes.")
                        .setFlag(true)
                )
                .addOption(
                    Option()
                        .setArgName("remote")
                        .setShortName("r")
                        .setLongName("remote")
                        .setDescription("If possible, fetch vocabulary from remote, otherwise load from local file.")
                        .setFlag(true)
                )
                .addOption(
                    Option()
                        .setArgName("list")
                        .setShortName("l")
                        .setLongName("list")
                        .setDescription("List installable vocabularies and their configuration.")
                        .setFlag(true)
                )
                .addOption(
                    Option()
                        .setHelp(true)
                        .setFlag(true)
                        .setArgName("help")
                        .setShortName("h")
                        .setLongName("help")
                )
                .addOption(
                    Option()
                        .setArgName("tags")
                        .setShortName("t")
                        .setLongName("tags")
                        .setDescription("Install only tagged vocabularies. If you don't specify a tag, all 'core' tagged vocabularies will be installed.")
                        .setMultiValued(true)
                        .setDefaultValue("core")
                )
        ).scopedProcessHandler(::install).build(vertx)
    }

    private suspend fun install(process: CommandProcess) {
        installVocabularies(process)
        process.write("Vocabularies installed\n")
    }

    companion object {
        fun create(vertx: Vertx) = InstallVocabulariesCommand(vertx).command
    }
}
