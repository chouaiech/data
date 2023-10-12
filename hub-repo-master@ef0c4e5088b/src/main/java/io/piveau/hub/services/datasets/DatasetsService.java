package io.piveau.hub.services.datasets;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.util.DataUploadConnector;
import io.piveau.pipe.PipeLauncher;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DatasetsService {
    String SERVICE_ADDRESS = "io.piveau.hub.datasets.queue";

    static Future<DatasetsService> create(TripleStore tripleStore, TripleStore shadowTripleStore, DataUploadConnector dataUploadConnector, JsonObject config, PipeLauncher launcher, Vertx vertx) {
        return Future.future(promise -> new DatasetsServiceImpl(tripleStore, shadowTripleStore, dataUploadConnector, config, launcher, vertx, promise));
    }

    static DatasetsService createProxy(Vertx vertx, String address) {
        return new DatasetsServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<String> listDatasets(String acceptType, String valueType, Integer limit, Integer offset);

    Future<String> listCatalogueDatasets(String acceptType, String valueType, String catalogueId, Integer limit, Integer offset);

    Future<String> getDatasetOrigin(String originId, String catalogueId, String acceptType);

    Future<String> getDataset(String id, String acceptType);

    Future<JsonObject> putDataset(String id, String content, String contentType);

    Future<JsonObject> putDatasetOrigin(String id, String content, String contentType, String catalogueId, Boolean upload);

    Future<JsonObject> postDataset(String content, String contentType, String catalogueId, Boolean createAccessURLs);

    Future<Void> deleteDatasetOrigin(String originId, String catalogueId);

    Future<Void> deleteDataset(String id);

    Future<String> getRecord(String id, String acceptType);

    Future<JsonObject> indexDataset(String originId, String catalogueId);

    Future<JsonObject> getDataUploadInformation(String datasetId, String catalogueId, String resultDataset);

}
