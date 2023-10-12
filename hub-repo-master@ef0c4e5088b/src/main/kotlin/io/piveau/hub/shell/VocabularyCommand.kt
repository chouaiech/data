package io.piveau.hub.shell

import io.piveau.hub.services.vocabularies.VocabulariesService
import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.Option
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.CommandBuilder
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.flow.*

class VocabularyCommand private constructor(vertx: Vertx) {

    private val vocabularyService: VocabulariesService =
        VocabulariesService.createProxy(vertx, VocabulariesService.SERVICE_ADDRESS)

    private val command: Command

    init {
        command = CommandBuilder.command(
            CLI.create("vocabs")
                .addOption(
                    Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help")
                )
                .addArgument(
                    Argument().setRequired(true).setIndex(1).setArgName("action")
                        .setDescription("Possible actions are 'list', 'show', or 'indexing'.")
                )
                .addArgument(
                    Argument().setRequired(false).setArgName("vocabs").setMultiValued(true)
                        .setDescription("Specify one or more vocabularies for the 'show' or 'indexing' action.")
                )
        ).scopedProcessHandler(::action).build(vertx)
    }

    private suspend fun action(process: CommandProcess) {
        val commandLine = process.commandLine()
        when (process.commandLine().getArgumentValue<String>(1)) {
            "list" -> {
                vocabularyService.listVocabularies {
                    if (it.succeeded()) {
                        process.write("${it.result().joinToString("\n")}\n")
                    } else {
                        process.write("List vocabularies failed: ${it.cause().message}\n")
                    }
                }
            }

            "indexing" -> {
                val vocabularies = try {
                    Future.future { promise -> vocabularyService.listVocabularies(promise) }.await()
                        .map { it as String }.toSet()
                } catch (t: Throwable) {
                    process.write("Listing vocabularies failed: ${t.message}\n")
                    return
                }
                val vocabs = commandLine.getArgumentValues<String>(2)
                val processingVocabularies = when {
                    vocabs.isEmpty() -> vocabularies
                    else -> vocabs.toSet().intersect(vocabularies)
                }

                processingVocabularies.asFlow()
                    .transform {vocab ->
                        try {
                            process.write("Indexing vocabulary $vocab...")
                            val array = Future.future { promise -> vocabularyService.indexVocabulary(vocab, null, promise) }.await()
                            process.write("done\n")
                            emit(vocab to array)
                        } catch (t: Throwable) {
                            process.write("\nIndexing vocabulary $vocab failed: ${t.message}\n")
                        }
                    }
                    .collect { (vocab, array) ->
                        val errors = JsonArray(array.map { it as JsonObject }.filter { it.getInteger("status") != 200 && it.getInteger("status") != 200 })
                        if (!errors.isEmpty) {
                            process.write("Vocab $vocab errors: ${errors.encodePrettily()}\n")
                        }
                    }
            }

            "show" -> {
                val vocabs = commandLine.getArgumentValues<String>(2)
                vocabularyService.listVocabularies { listVocabulariesResult ->
                    if (listVocabulariesResult.succeeded()) {
                        val futures: MutableList<Future<Void>> = ArrayList()
                        when {
                            vocabs.isEmpty() -> {
                                listVocabulariesResult.result().forEach { vocab ->
                                    (vocab as? String)?.run {
                                        val promise = Promise.promise<Void>()
                                        futures.add(promise.future())
                                        vocabularyService.toJson(this, null) { toJsonResult ->
                                            if (toJsonResult.succeeded()) {
                                                process.write("${toJsonResult.result().encodePrettily()}\n")
                                                promise.complete()
                                            } else {
                                                process.write("Unknown vocabulary $vocab\n")
                                                promise.complete()
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                vocabs.forEach { vocab ->
                                    val promise = Promise.promise<Void>()
                                    futures.add(promise.future())
                                    if (listVocabulariesResult.result().contains(vocab)) {
                                        vocabularyService.toJson(vocab, null) { toJsonResult ->
                                            if (toJsonResult.succeeded()) {
                                                process.write("${toJsonResult.result().encodePrettily()}\n")
                                                promise.complete()
                                            } else {
                                                process.write("Unknown vocabulary $vocab\n")
                                                promise.complete()
                                            }
                                        }
                                    } else {
                                        process.write("Unknown vocabulary $vocab\n")
                                        promise.complete()
                                    }
                                }
                            }
                        }
                        CompositeFuture.all(futures.toList()).onSuccess {
                            process.end()
                        }
                    } else {
                        process.write(
                            "List vocabularies failed: " +
                                    "${listVocabulariesResult.cause().message}\n"
                        ).end()
                    }
                }
            }

            else -> {
                process.write("Unknown action.\n").end()
            }
        }
    }

    companion object {
        fun create(vertx: Vertx): Command { return VocabularyCommand(vertx).command }
    }
}
