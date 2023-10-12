package io.piveau.hub.security;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class KeyCloakServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        
        KeyCloakService.create(vertx, config(), ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx).setAddress(KeyCloakService.SERVICE_ADDRESS).register(KeyCloakService.class, ready.result());
                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

}
