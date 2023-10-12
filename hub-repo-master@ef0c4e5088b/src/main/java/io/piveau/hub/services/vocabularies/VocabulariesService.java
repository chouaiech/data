package io.piveau.hub.services.vocabularies;

import io.piveau.dcatap.TripleStore;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface VocabulariesService {
    String SERVICE_ADDRESS = "io.piveau.hub.vocabularies.queue";

    static VocabulariesService create(Vertx vertx, TripleStore tripleStore, JsonObject config, Handler<AsyncResult<VocabulariesService>> readyHandler) {
        return new VocabulariesServiceImpl(vertx, tripleStore, config, readyHandler);
    }

    static VocabulariesService createProxy(Vertx vertx, String address) {
        DeliveryOptions options = new DeliveryOptions().setSendTimeout(300000);
        return new VocabulariesServiceVertxEBProxy(vertx, address, options);
    }

    @Fluent
    VocabulariesService listVocabularies(Handler<AsyncResult<JsonArray>> handler);

    @Fluent
    VocabulariesService toJson(String vocabularyId, String vocabularyUri, Handler<AsyncResult<JsonObject>> handler);

    @Fluent
    VocabulariesService indexVocabulary(String vocabularyId, String vocabularyUri, Handler<AsyncResult<JsonArray>> handler);

    @Fluent
    VocabulariesService readVocabulary(String vocabularyId, String acceptType, Handler<AsyncResult<String>> handler);

    @Fluent
    VocabulariesService installVocabulary(String vocabularyId, String vocabularyUri, String contentType,
                                                 String file, Handler<AsyncResult<String>> handler);

    @Fluent
    VocabulariesService createOrUpdateVocabulary(String vocabularyId, String vocabularyUri, String contentType,
                                                 String payload, String hash, int chunkId, int numberOfChunks,
                                                 Handler<AsyncResult<String>> handler);

    @Fluent
    VocabulariesService deleteVocabulary(String vocabularyId, Handler<AsyncResult<Void>> handler);
}
