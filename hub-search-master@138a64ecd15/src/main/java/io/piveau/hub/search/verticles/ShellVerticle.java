package io.piveau.hub.search.verticles;

import io.piveau.hub.search.services.catalogues.CataloguesService;
import io.piveau.hub.search.services.datasets.DatasetsService;
import io.piveau.hub.search.services.search.SearchService;
import io.piveau.hub.search.services.sitemaps.SitemapsService;
import io.piveau.hub.search.services.vocabulary.VocabularyService;
import io.piveau.hub.search.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.web.client.WebClient;
import net.sf.saxon.TransformerFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ShellVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ShellVerticle.class);

    private SearchService searchService;
    private DatasetsService datasetsService;
    private CataloguesService cataloguesService;
    private SitemapsService sitemapsService;
    private VocabularyService vocabularyService;

    @Override
    public void start(Promise<Void> startPromise) {
        searchService = SearchService.createProxy(vertx, SearchService.SERVICE_ADDRESS);
        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
        cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS);
        sitemapsService = SitemapsService.createProxy(vertx, SitemapsService.SERVICE_ADDRESS);
        vocabularyService = VocabularyService.createProxy(vertx, VocabularyService.SERVICE_ADDRESS);

        WebClient webClient = WebClient.create(vertx);

        List<Command> commandList = new ArrayList<>();

        JsonObject cliConfig = ConfigHelper.forConfig(config())
                .forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_CLI_CONFIG);

        ShellServiceOptions shellServiceOptions = new ShellServiceOptions()
                .setWelcomeMessage("\nWelcome to piveau-hub-search CLI!\n\n");

        cliConfig.getMap().keySet().forEach(key -> {
            JsonObject options = cliConfig.getJsonObject(key);
            if (key.equals("http")) {
                shellServiceOptions.setHttpOptions(new HttpTermOptions()
                        .setHost(options.getString("host", "0.0.0.0"))
                        .setPort(options.getInteger("port", 8081))
                );
            }
            if (key.equals("telnet")) {
                shellServiceOptions.setTelnetOptions(new TelnetTermOptions()
                        .setHost(options.getString("host", "0.0.0.0"))
                        .setPort(options.getInteger("port", 5000))
                );
            }
        });

        ShellService shellService = ShellService.create(vertx, shellServiceOptions);

        shellService.start(handler -> {
            if (handler.succeeded()) {
                LOG.info("Successfully launched cli");
                startPromise.complete();
            } else {
                LOG.error("Failed to launch cli: {0}", handler.cause());
                startPromise.fail(handler.cause());
            }
        });

        CommandBuilder triggerSitemapGeneration = CommandBuilder.command("triggerSitemapGeneration");
        commandList.add(triggerSitemapGeneration.build(vertx));
        triggerSitemapGeneration.processHandler(process -> sitemapsService.triggerSitemapGeneration().onComplete(ar -> {
            if (ar.succeeded()) {
                process.write(ar.result().getString("result") + "\n");
                process.end();
            }
        }));

        CommandBuilder setMaxAggSize = CommandBuilder.command("setMaxAggSize");
        commandList.add(setMaxAggSize.build(vertx));
        setMaxAggSize.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 2) {
                process.write("setMaxAggSize: try 'setMaxAggSize index number (>0)'\n");
                process.end();
            } else {
                try {
                    String index = args.get(0);
                    Integer max_agg_size = Integer.parseInt(args.get(1));
                    searchService.setMaxAggSize(index, max_agg_size).onComplete(ar -> handleResponse(process, ar));
                } catch (NumberFormatException e) {
                    process.write("setMaxAggSize: try 'setMaxAggSize index number (>0)'\n");
                    process.end();
                }
            }
        });

        CommandBuilder setMapping = CommandBuilder.command("setMapping");
        commandList.add(setMapping.build(vertx));
        setMapping.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 1) {
                process.write("setMapping: try 'setMapping index'\n");
                process.end();
            } else {
                String index = args.get(0);
                searchService.putMapping(index).onComplete(ar -> handleResponse(process, ar));
            }
        });

        CommandBuilder createIndex = CommandBuilder.command("createIndex");
        commandList.add(createIndex.build(vertx));
        createIndex.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 1 && args.size() != 2) {
                process.write("createIndex: try 'createIndex index numberOfShards'\n");
                process.end();
            } else {
                try {
                    String index = args.get(0);
                    Integer numberOfShards = args.size() == 2 ? Integer.parseInt(args.get(1)) : null;
                    searchService.indexCreate(index, numberOfShards).onComplete(ar -> handleResponse(process, ar));
                } catch (NumberFormatException e) {
                    process.write("Value should be an integer\n");
                    process.end();
                }
            }
        });

        CommandBuilder removeIndex = CommandBuilder.command("removeIndex");
        commandList.add(removeIndex.build(vertx));
        removeIndex.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 1 && !(args.get(0).equals("vocabularies") && args.size() == 2)) {
                process.write("removeIndex: try 'removeIndex index'\n");
                process.end();
            } else {
                String index = args.get(0);
                searchService.indexDelete(index).onComplete(ar -> handleResponse(process, ar));
            }
        });

        CommandBuilder setReadAlias = CommandBuilder.command("setReadAlias");
        commandList.add(setReadAlias.build(vertx));
        setReadAlias.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 1) {
                process.write("setReadAlias: try 'setReadAlias index'\n");
                process.end();
            }
            String index = args.get(0);
            String prefix = index.substring(0, index.lastIndexOf("_"));
            searchService.setIndexAlias("*", index, prefix + "_read").onComplete(ar -> handleResponse(process, ar));
        });

        CommandBuilder setWriteAlias = CommandBuilder.command("setWriteAlias");
        commandList.add(setWriteAlias.build(vertx));
        setWriteAlias.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 1) {
                process.write("setWriteAlias: try 'setWriteAlias index'\n");
                process.end();
            }
            String index = args.get(0);
            String prefix = index.substring(0, index.lastIndexOf("_"));
            searchService.setIndexAlias("*", index, prefix + "_write").onComplete(ar -> handleResponse(process, ar));
        });

        CommandBuilder setNumberOfReplicas = CommandBuilder.command("setNumberOfReplicas");
        commandList.add(setNumberOfReplicas.build(vertx));
        setNumberOfReplicas.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 2) {
                process.write("setNumberOfReplicas: try 'setNumberOfReplicas index number'\n");
                process.end();
            } else {
                String index = args.get(0);
                Integer numberOfReplicas = Integer.parseInt(args.get(1));
                searchService.setNumberOfReplicas(index, numberOfReplicas).onComplete(
                        ar -> handleResponse(process, ar));
            }
        });

        CommandBuilder resetIndices = CommandBuilder.command("resetIndices");
        commandList.add(resetIndices.build(vertx));
        resetIndices.processHandler(process -> {
            process.write("Are you sure you want to reset all indexes? [y/n]\n");
            process.interruptHandler(v -> process.end());
            process.stdinHandler(data -> {
                if (data.equals("y") || data.equals("Y")) {
                    searchService.indexReset().onComplete(indexResetHandler -> {
                        if (indexResetHandler.succeeded()) {
                            process.write("Successfully reset index\n");
                        } else {
                            process.write(indexResetHandler.cause().toString());
                        }
                        process.end();
                    });
                } else if (data.equals("n") || data.equals("N")) {
                    process.end();
                }
            });
        });

        CommandBuilder setMaxResultWindow = CommandBuilder.command("setMaxResultWindow");
        commandList.add(setMaxResultWindow.build(vertx));
        setMaxResultWindow.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 2) {
                process.write("setMaxResultWindow: try 'setMaxResultWindow index value'\n");
                process.end();
            } else {
                try {
                    String index = args.get(0);
                    Integer max_result_window = Integer.parseInt(args.get(1));
                    searchService.setMaxResultWindow(index, max_result_window).onComplete(
                            ar -> handleResponse(process, ar));
                } catch (NumberFormatException e) {
                    process.write("Value should be an integer\n");
                    process.end();
                }
            }
        });

        CommandBuilder boostField = CommandBuilder.command("boostField");
        commandList.add(boostField.build(vertx));
        boostField.processHandler(process -> {
            List<String> args = process.args();
            if (args.size() != 3) {
                process.write("boostField: try 'boostField filter field value'\n");
                process.end();
            } else {
                try {
                    String indexPrefix = args.get(0);
                    String field = args.get(1);
                    Float value = Float.parseFloat(args.get(2));
                    searchService.boost(indexPrefix, field, value).onComplete(ar -> handleResponse(process, ar));
                } catch (NumberFormatException e) {
                    process.write("Value should be a float\n");
                    process.end();
                }
            }
        });

        CommandBuilder reindexCatalogues = CommandBuilder.command("reindexCatalogues");
        commandList.add(reindexCatalogues.build(vertx));
        reindexCatalogues.processHandler(process -> {
            process.write("Are you sure you want to reindex all catalogues? [y/n]\n");
            process.interruptHandler(v -> process.end());
            process.stdinHandler(data -> {
                if (data.equals("y") || data.equals("Y")) {
                    JsonObject query = new JsonObject();
                    query.put("filter", "catalogue");
                    query.put("from", 0);
                    query.put("size", 1000);
                    query.put("elasticId", true);

                    List<Future<Void>> futureList = new ArrayList<>();
                    searchService.search(query.toString()).onComplete(searchResult -> {
                        if (searchResult.succeeded()) {
                            JsonObject result = searchResult.result().getJsonObject("result");
                            JsonArray results = result.getJsonArray("results");
                            results.forEach(value -> {
                                Promise<Void> valuePromise = Promise.promise();
                                ((JsonObject) value).remove("count");
                                String id = ((JsonObject) value).remove("_id").toString();
                                final JsonObject valueJson = ((JsonObject) value);
                                cataloguesService.createOrUpdateCatalogue(id, valueJson).onComplete(replaceResult -> {
                                    if (replaceResult.succeeded()) {
                                        process.write(valueJson.getString("id") + "\n");
                                        valuePromise.complete();
                                    } else {
                                        process.write(replaceResult.cause().getMessage() + "\n");
                                        valuePromise.complete();
                                    }
                                });
                                futureList.add(valuePromise.future());
                            });

                            Future.all(futureList).onComplete(ar -> process.end());
                        } else {
                            process.write(searchResult.cause().getMessage() + "\n");
                            process.end();
                        }
                    });
                } else if (data.equals("n") || data.equals("N")) {
                    process.end();
                }
            });
        });

        CommandBuilder syncScores = CommandBuilder.command("syncScores");
        commandList.add(syncScores.build(vertx));
        syncScores.processHandler(process -> {
            datasetsService.triggerSyncScores().onSuccess(result -> {
                process.write(result + "\n");
                process.end();
            }).onFailure(failure -> {
                process.write("SyncScore failure: " + failure + "\n");
                process.end();
            });
        });

        CommandBuilder indexXmlVocabularies = CommandBuilder.command("indexXmlVocabularies");
        commandList.add(indexXmlVocabularies.build(vertx));
        indexXmlVocabularies.processHandler(process -> {
            List<Future<Void>> futureList = new ArrayList<>();
            Future<Void> ianaMediaTypesFuture = indexXmlVocabulary(webClient, "iana-media-types",
                    "https://www.iana.org/assignments/media-types/media-types.xml",
                    "conf/vocabularies/iana-media-types.xslt");
            futureList.add(ianaMediaTypesFuture);
            Future<Void> spdxChecksumAlgorithmFuture = indexXmlVocabulary(webClient, "spdx-checksum-algorithm",
                    "https://raw.githubusercontent.com/spdx/spdx-spec/development/v2.3/ontology/spdx-ontology.owl.xml",
                    "conf/vocabularies/spdx-checksum-algorithm.xslt");
            futureList.add(spdxChecksumAlgorithmFuture);
            Future.all(futureList).onSuccess(result -> {
                process.write("Successfully indexed xml vocabularies\n");
                process.end();
            }).onFailure(failure -> {
                process.write(failure.getMessage() + "\n");
                process.end();
            });
        });

        CommandBuilder resetWebroot = CommandBuilder.command("resetWebroot");
        commandList.add(resetWebroot.build(vertx));
        resetWebroot.processHandler(process -> {
            if (vertx.fileSystem().existsBlocking("conf/webroot")) {
                vertx.fileSystem().deleteRecursiveBlocking("conf/webroot", true);
                process.write("Successfully reset webroot.\n");
            } else {
                process.write("Nothing to do here.\n");
            }
            process.end();
        });

        CommandRegistry registry = CommandRegistry.getShared(vertx);
        for (Command command : commandList) {
            registry.registerCommand(command);
        }

        CommandBuilder listCommands = CommandBuilder.command("listCommands");
        listCommands.processHandler(process -> {
            for (Command command : commandList) {
                process.write(command.name() + "\n");
            }
            process.end();
        });

        registry.registerCommand(listCommands.build(vertx));
    }

    private Future<Void> indexXmlVocabulary(WebClient webClient, String id, String url, String xsltPath) {
        Promise<Void> promise = Promise.promise();
        webClient.getAbs(url).send()
                .onSuccess(result -> {
                    try {
                        // Use net.sf.saxon transformer for XSLT 2.0
                        TransformerFactory factory = TransformerFactory.newInstance();
                        Source xslt = new StreamSource(new File(xsltPath));
                        Transformer transformer = factory.newTransformer(xslt);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        transformer.transform(new StreamSource(new StringReader(result.bodyAsString())), new StreamResult(out));

                        String output = out.toString(StandardCharsets.UTF_8);

                        vocabularyService.createOrUpdateVocabulary(id, new JsonObject(output)).onComplete(ar -> {
                            if (ar.succeeded()) {
                                promise.complete();
                            } else {
                                promise.fail(ar.cause());
                            }
                        });

                    } catch (TransformerException e) {
                        promise.fail(e);
                    }
                }).onFailure(promise::fail);
        return promise.future();
    }

    private void handleResponse(CommandProcess process, AsyncResult<String> ar) {
        if (ar.succeeded()) {
            process.write(ar.result() + "\n");
        } else {
            process.write(ar.cause().getMessage() + "\n");
        }
        process.end();
    }

}
