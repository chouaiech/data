package io.piveau.hub.services.drafts;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class DatasetDraftsServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        ConfigHelper configHelper = ConfigHelper.forConfig(config());
        JsonObject shadowConf = configHelper.forceJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG);

        TripleStore shadowTripleStore = new TripleStore(vertx, shadowConf, null, "drafts-shadow");
        DatasetDraftsService.create(shadowTripleStore, config(), vertx, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx).setAddress(DatasetDraftsService.SERVICE_ADDRESS).register(DatasetDraftsService.class, ready.result());
                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

}
