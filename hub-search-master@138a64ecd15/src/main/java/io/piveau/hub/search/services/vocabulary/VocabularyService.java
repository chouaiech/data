package io.piveau.hub.search.services.vocabulary;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface VocabularyService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.vocabulary.queue";

    static Future<VocabularyService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new VocabularyServiceImpl(vertx, config, indexManager, promise));
    }

    static VocabularyService createProxy(Vertx vertx, String address) {
        return new VocabularyServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonArray> readVocabularies();
    Future<JsonArray> createOrUpdateVocabulary(String vocabulary, JsonObject payload);
    Future<JsonObject> readVocabulary(String vocabulary);
    Future<Void> deleteVocabulary(String vocabulary);
    Future<JsonObject> createVocable(String vocabulary, JsonObject payload);
    Future<JsonObject> createOrUpdateVocable(String vocabulary, String vocableId, JsonObject payload);
    Future<JsonObject> modifyVocable(String vocabulary, String vocableId, JsonObject payload);
    Future<JsonObject> readVocable(String vocabulary, String vocableId);
    Future<Void> deleteVocable(String vocabulary, String vocableId);
    Future<JsonObject> replaceVocabularyInFacets(JsonObject facetsObject);
    Future<JsonArray> replaceVocabularyInPayloadList(JsonArray payload);
    Future<JsonObject> replaceVocabularyInPayload(JsonObject payload);
}
