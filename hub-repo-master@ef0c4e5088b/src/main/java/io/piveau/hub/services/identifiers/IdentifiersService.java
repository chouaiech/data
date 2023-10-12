package io.piveau.hub.services.identifiers;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.services.translation.TranslationService;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

@ProxyGen
public interface IdentifiersService {
    String SERVICE_ADDRESS = "io.piveau.hub.identifiers.queue";

    static IdentifiersService create(Vertx vertx, WebClient client, JsonObject config, TripleStore tripleStore,
                                     Handler<AsyncResult<IdentifiersService>> readyHandler) {
        return new IdentifiersServiceImpl(vertx, client, config, tripleStore, readyHandler);
    }

    static IdentifiersService createProxy(Vertx vertx, String address) {
        return new IdentifiersServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    @Fluent
    IdentifiersService createIdentifier(String datasetId, String catalogueId, String type, Handler<AsyncResult<JsonObject>> handler);
    @Fluent
    IdentifiersService checkIdentifierRequirement(String datasetId, String catalogueId, String type, Handler<AsyncResult<JsonObject>> handler);


}
