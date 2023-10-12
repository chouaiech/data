package io.piveau.hub.security;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface KeyCloakService {
    String SERVICE_ADDRESS="io.piveau.hub.security.keycloakqueue";

    static KeyCloakService create(Vertx vertx, JsonObject config, Handler<AsyncResult<KeyCloakService>> readyHandler) {
        return new KeyCloakServiceImpl(vertx, config, readyHandler);
    }

    static KeyCloakService createProxy(Vertx vertx, String address) {
        return new KeyCloakServiceVertxEBProxy(vertx, address);
    }

    @Fluent
    KeyCloakService createResource(String catalogueId);

    @Fluent
    KeyCloakService deleteResource(String catalogueId);

}
