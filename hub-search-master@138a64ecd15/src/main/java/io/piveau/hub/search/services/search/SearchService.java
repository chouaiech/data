package io.piveau.hub.search.services.search;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface SearchService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.search.queue";

    static Future<SearchService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new SearchServiceImpl(vertx, config, indexManager, promise));
    }

    static SearchService createProxy(Vertx vertx, String address) {
        return new SearchServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(500000));
    }

    Future<JsonObject> search(String q);
    Future<JsonObject> scroll(String scrollId);
    Future<Boolean> indexExists(String index);
    Future<String> indexCreate(String index, Integer numberOfShards);
    Future<String> indexDelete(String index);
    Future<String> indexReset();
    Future<String> putMapping(String index);
    Future<String> setIndexAlias(String oldIndex, String newIndex, String alias);
    Future<String> boost(String index, String field, Float value);
    Future<String> setMaxAggSize(String index, Integer maxAggSize);
    Future<String> setMaxResultWindow(String index, Integer maxResultWindow);
    Future<String> setNumberOfReplicas(String index, Integer numberOfReplicas);
    Future<String> putIndexTemplate(String index);
    Future<String> putLifecyclePolicy(String index);
    Future<JsonArray> getIndices(String index);
    Future<JsonArray> listIds(String filter, String field, JsonArray terms, String alias);
}
