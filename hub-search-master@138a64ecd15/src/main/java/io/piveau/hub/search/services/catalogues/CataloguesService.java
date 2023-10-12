package io.piveau.hub.search.services.catalogues;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

@ProxyGen
public interface CataloguesService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.catalogues.queue";

    static Future<CataloguesService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new CataloguesServiceImpl(vertx, config, indexManager, promise));
    }

    static CataloguesService createProxy(Vertx vertx, String address) {
        return new CataloguesServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonArray> listCatalogues(String alias);
    Future<JsonObject> createCatalogue(JsonObject payload);
    Future<JsonObject> createOrUpdateCatalogue(String catalogueId, JsonObject payload);
    Future<JsonObject> modifyCatalogue(String catalogueId, JsonObject payload);
    Future<JsonObject> readCatalogue(String catalogueId);
    Future<Void> deleteCatalogue(String catalogueId);
    Future<JsonArray> replaceCatalogueInItems(JsonArray items);
    Future<JsonArray> replaceCatalogueInResultList(JsonArray results, List<String> includes);
    Future<JsonObject> replaceCatalogueInResponse(JsonObject dataset, List<String> includes);
    Future<JsonArray> checkCatalogueInPayloadArray(JsonArray payload);
    Future<JsonObject> checkCatalogueInPayloadObject(JsonObject payload);

}
