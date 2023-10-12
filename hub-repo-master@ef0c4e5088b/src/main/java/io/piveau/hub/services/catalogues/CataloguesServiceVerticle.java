package io.piveau.hub.services.catalogues;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.hub.util.logger.PiveauLoggerFactory;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class CataloguesServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        PiveauLoggerFactory.getLogger(getClass()).info("Starting CataloguesService Verticle");

        ConfigHelper configHelper = ConfigHelper.forConfig(config());
        JsonObject conf = configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG);

        TripleStore tripleStore = new TripleStore(vertx, conf, null, "catalogues-main");

        CataloguesService.create(tripleStore, vertx, config())
                .onSuccess(service -> {
                    new ServiceBinder(vertx).setAddress(CataloguesService.SERVICE_ADDRESS).register(CataloguesService.class, service);
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }
}
