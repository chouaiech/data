package io.piveau.hub.search.services.dataservices;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DataServicesService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.dataservices.queue";

    static Future<DataServicesService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new DataServicesServiceImpl(vertx, config, indexManager, promise));
    }

    static DataServicesService createProxy(Vertx vertx, String address) {
        return new DataServicesServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonObject> createDataService(JsonObject payload);
    Future<JsonObject> createOrUpdateDataService(String dataServiceId, JsonObject payload);
    Future<JsonObject> modifyDataService(String dataServiceId, JsonObject payload);
    Future<JsonObject> readDataService(String dataServiceId);
    Future<JsonObject> deleteDataService(String dataServiceId);

}
