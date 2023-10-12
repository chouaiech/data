package io.piveau.hub.services.vocabularies;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class VocabulariesServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject config = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG);
        TripleStore tripleStore = new TripleStore(vertx, config, null, "vocabulary-service");
        VocabulariesService.create(vertx, tripleStore, config(), serviceReady -> {
            if (serviceReady.succeeded()) {
                new ServiceBinder(vertx).setAddress(VocabulariesService.SERVICE_ADDRESS)
                        .register(VocabulariesService.class, serviceReady.result());
                startPromise.complete();
            } else {
                startPromise.fail(serviceReady.cause());
            }
        });
    }
}
