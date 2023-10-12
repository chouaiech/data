package io.piveau.hub.services.identifiers;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceBinder;

public class IdentifiersServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        ConfigHelper configHelper = ConfigHelper.forConfig(config());
        JsonObject conf = configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG);
        TripleStore tripleStore = new TripleStore(vertx, conf, null);
        IdentifiersService.create(vertx, WebClient.create(vertx), config(), tripleStore, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx).setAddress(IdentifiersService.SERVICE_ADDRESS).register(IdentifiersService.class, ready.result());
                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

}
