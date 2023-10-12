package io.piveau.hub.shell;

import io.piveau.dcatap.*;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.piveau.utils.Piveau;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClearCatalogueCommand {

    private final Command command;

    private final IndexService indexService;

    private final TripleStore tripleStore;
    private final TripleStore shadowTripleStore;
    private final CatalogueManager catalogueManager;
    private final DatasetManager datasetManager;

    private ClearCatalogueCommand(Vertx vertx) {
        indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);

        ConfigHelper configHelper = ConfigHelper.forConfig(vertx.getOrCreateContext().config());

        tripleStore = new TripleStore(vertx, configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG), null);
        if (vertx.getOrCreateContext().config().containsKey(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)) {
            shadowTripleStore = new TripleStore(vertx, configHelper.forceJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG), null);
        } else {
            shadowTripleStore = tripleStore;
        }

        catalogueManager = tripleStore.getCatalogueManager();
        datasetManager = tripleStore.getDatasetManager();

        command = CommandBuilder.command(
                CLI.create("clear")
                        .addArgument(
                                new Argument()
                                        .setArgName("catalogueId")
                                        .setRequired(true)
                                        .setDescription("The id of the catalogues to clear."))
                        .addOption(new Option().setHelp(true).setFlag(true).setArgName("help").setShortName("h").setLongName("help"))
                        .addOption(new Option().setFlag(true).setArgName("keepIndex").setShortName("k").setLongName("keepIndex"))
                        .addOption(new Option().setFlag(true).setArgName("verbose").setShortName("v").setLongName("verbose"))
        ).completionHandler(completion ->
            catalogueManager.listUris().onSuccess(list -> {
                List<String> ids = list.stream()
                        .map(DCATAPUriRef::getId)
                        .toList();
                Piveau.candidatesCompletion(completion, ids);
            }).onFailure(cause -> completion.complete("", true))
        ).processHandler(process -> {
            CommandLine commandLine = process.commandLine();
            clearCatalogue(process, commandLine.getArgumentValue(0), commandLine.isFlagEnabled("keepIndex"));
        }).build(vertx);
    }

    public static Command create(Vertx vertx) {
        return new ClearCatalogueCommand(vertx).command;
    }

    private void clearCatalogue(CommandProcess process, String catalogueId, boolean keepIndex) {
        DCATAPUriRef catalogueSchema = DCATAPUriSchema.createFor(catalogueId);

        process.write("Start clearing catalogue " + catalogueSchema.getId() + (keepIndex ? ", keeping index.\n" : "\n"));

        catalogueManager.get(catalogueId)
                .compose(model -> {
                    Resource catalogue = model.getResource(catalogueSchema.getCatalogueUriRef());
                    if (catalogue == null) {
                        return Future.failedFuture("No catalogue resource found");
                    }

                    List<Resource> datasets = model.listObjectsOfProperty(DCAT.dataset).mapWith(RDFNode::asResource).toList();
                    process.write("Found " + datasets.size() + " datasets\n");

                    List<List<Resource>> partitions = Lists.partition(datasets, 1000);
                    Promise<Model> partitionsPromise = Promise.promise();
                    nextPartition(catalogue, partitions, 0, process, partitionsPromise);
                    return partitionsPromise.future();
                })
                .compose(model -> {
                    if (model != null) {
                        model.removeAll(null, DCAT.dataset, null);
                        model.removeAll(null, DCAT.record, null);
                        process.write("Catalogue model cleared, storing...\n");
                        return catalogueManager.set(catalogueId, model);
                    } else {
                        return Future.failedFuture("Model is null");
                    }
                })
                .onSuccess(v -> process.write("Catalogue " + catalogueId + " cleared.\n").end())
                .onFailure(cause -> {
                    process.write("Clearing catalogue failed: " + cause.getClass().getName() + "\n");
                    process.write("Clearing catalogue failed: " + cause.getMessage() + "\n").end();
                });
    }

    private void nextPartition(Resource catalogue, List<List<Resource>> partitions, int index, CommandProcess process, Promise<Model> promise) {
        if (index >= partitions.size()) {
            process.write("No more partitions\n");
            promise.complete(catalogue.getModel());
        } else {
            List<Future<Void>> futures = new ArrayList<>();
            List<Resource> partition = partitions.get(index);
            partition.forEach(dataset -> {
                DCATAPUriRef datasetSchema = DCATAPUriSchema.parseUriRef(dataset.asResource().getURI());
                Promise<Void> datasetPromise = Promise.promise();
                futures.add(datasetPromise.future());
                datasetManager.deleteGraph(datasetSchema.getDatasetGraphName())
                        .onSuccess(v -> {
                            process.write("Dataset " + datasetSchema.getId() + " removed from triple store\n");

                            tripleStore.deleteGraph(datasetSchema.getMetricsGraphName());
                            shadowTripleStore.deleteGraph(datasetSchema.getHistoricMetricsGraphName());

                            if (!process.commandLine().isFlagEnabled("keepIndex")) {
                                indexService.deleteDataset(datasetSchema.getId(), ir -> {
                                    if (ir.failed()) {
                                        process.write("Dataset " + datasetSchema.getId() + " could not be removed from index: " + ir.cause().getMessage() + "\n");
                                    }
                                });
                            }

                            datasetPromise.complete();
                        })
                        .onFailure(datasetPromise::fail);

                Model catalogueModel = catalogue.getModel();
                catalogueModel
                        .remove(catalogue, DCAT.dataset, catalogueModel.createResource(datasetSchema.getDatasetUriRef()))
                        .remove(catalogue, DCAT.record, catalogueModel.createResource(datasetSchema.getRecordUriRef()));
            });
            CompositeFuture.join(new ArrayList<>(futures)).onComplete(cf ->
                    nextPartition(catalogue, partitions, index + 1, process, promise));
        }
    }

}
