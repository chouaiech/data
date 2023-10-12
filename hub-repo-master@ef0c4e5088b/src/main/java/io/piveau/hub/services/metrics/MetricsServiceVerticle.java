package io.piveau.hub.services.metrics;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class MetricsServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        ConfigHelper configHelper = ConfigHelper.forConfig(config());

        TripleStore tripleStore = new TripleStore(
                vertx,
                configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG),
                null,
                "metrics-main");
        TripleStore shadowTripleStore;
        if (config().containsKey(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG)) {
            shadowTripleStore = new TripleStore(vertx, configHelper.forceJsonObject(Constants.ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG), null, "metrics-shadow");
        } else {
            shadowTripleStore = null;
        }

        MetricsService.create(tripleStore, shadowTripleStore, vertx, config())
                .onSuccess(service -> {
                    new ServiceBinder(vertx).setAddress(MetricsService.SERVICE_ADDRESS).register(MetricsService.class, service);
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }

}
