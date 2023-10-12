package io.piveau.hub.search.services.datasets;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DatasetsService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.datasets.queue";

    static Future<DatasetsService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new DatasetsServiceImpl(vertx, config, indexManager, promise));
    }

    static DatasetsService createProxy(Vertx vertx, String address) {
        return new DatasetsServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonArray> listDatasets(String catalogueId, String alias);
    Future<JsonObject> createDataset(JsonObject payload);
    Future<JsonObject> createOrUpdateDataset(String datasetId, JsonObject payload);
    Future<JsonObject> modifyDataset(String datasetId, JsonObject payload);
    Future<JsonObject> readDataset(String datasetId);
    Future<Void> deleteDataset(String datasetId);
    Future<JsonArray> createOrUpdateDatasetBulk(JsonArray payload);
    Future<JsonObject> getDatasetRSS(String datasetId);
    Future<JsonObject> readDatasetRevision(String datasetId, String revision);
    Future<String> triggerSyncScores();

}
