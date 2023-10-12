package io.piveau.hub.services.drafts;

import io.piveau.dcatap.TripleStore;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

@ProxyGen
public interface DatasetDraftsService {
    String SERVICE_ADDRESS = "io.piveau.hub.drafts.queue";

    static DatasetDraftsService create(TripleStore shadowTripleStore, JsonObject config, Vertx vertx,
                                       Handler<AsyncResult<DatasetDraftsService>> readyHandler) {
        return new DatasetDraftsServiceImpl(shadowTripleStore, config, vertx, readyHandler);
    }

    static DatasetDraftsService createProxy(Vertx vertx, String address) {
        return new DatasetDraftsServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    @Fluent
    DatasetDraftsService listDatasetDrafts(List<String> catalogueId, String provider,
                                           Handler<AsyncResult<JsonArray>> handler);

    @Fluent
    DatasetDraftsService createDatasetDraft(String catalogueId, String payload, String contentType, String provider,
                                            Handler<AsyncResult<String>> handler);

    @Fluent
    DatasetDraftsService readDatasetDraft(String datasetId, String catalogueId, String acceptType,
                                          Handler<AsyncResult<String>> handler);

    @Fluent
    DatasetDraftsService createOrUpdateDatasetDraft(String datasetId, String catalogueId, String payload,
                                                    String contentType, String provider,
                                                    Handler<AsyncResult<String>> handler);

    @Fluent
    DatasetDraftsService deleteDatasetDraft(String datasetId, String catalogueId, Handler<AsyncResult<Void>> handler);

}
