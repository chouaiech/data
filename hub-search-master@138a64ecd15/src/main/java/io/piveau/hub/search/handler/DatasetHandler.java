package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.datasets.DatasetsService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DatasetHandler extends ContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DatasetHandler.class);

    DatasetsService datasetsService;

    public DatasetHandler(Vertx vertx, String address) {
        datasetsService = DatasetsService.createProxy(vertx, address);
    }

    public void listDatasets(RoutingContext context) {
        LOG.debug("List datasets, remote address: {}", context.request().connection().remoteAddress());
        String catalogue = context.request().getParam("catalogue");
        String alias = context.request().getParam("alias");
        datasetsService.listDatasets(catalogue, alias).onComplete(ar -> handleContextJsonArray(context, ar));
    }

    public void createDataset(RoutingContext context) {
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            datasetsService.createDataset(context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            datasetsService.createDataset(context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void createOrUpdateDataset(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            datasetsService.createOrUpdateDataset(id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            datasetsService.createOrUpdateDataset(id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void modifyDataset(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            datasetsService.modifyDataset(id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            datasetsService.modifyDataset(id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void readDataset(RoutingContext context) {
        LOG.debug("Read dataset, remote address: {}", context.request().connection().remoteAddress());
        String id = context.request().getParam("id");
        datasetsService.readDataset(id).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void readDatasetRevision(RoutingContext context) {
        LOG.debug("Read dataset, remote address: {}", context.request().connection().remoteAddress());
        String id = context.request().getParam("id");
        String revision = "dataset-revisions_" + context.request().getParam("revision");
        datasetsService.readDatasetRevision(id,revision).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void deleteDataset(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            datasetsService.deleteDataset(id).onComplete(ar -> handleContextVoid(context, ar));
        } else {
            datasetsService.deleteDataset(id).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void createOrUpdateDatasetBulk(RoutingContext context) {
        JsonArray datasets = context.body().asJsonObject().getJsonArray("datasets");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            datasetsService.createOrUpdateDatasetBulk(datasets).onComplete(ar -> handleContextJsonArray(context, ar));
        } else {
            datasetsService.createOrUpdateDatasetBulk(datasets);
            context.response().setStatusCode(202).end();
        }
    }
}
