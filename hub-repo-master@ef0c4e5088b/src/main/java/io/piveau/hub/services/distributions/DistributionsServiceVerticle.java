package io.piveau.hub.services.distributions;


import io.piveau.dcatap.TripleStore;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class DistributionsServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject conf = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG);
        TripleStore tripleStore = new TripleStore(vertx, conf, null, "distributions-main");

        DatasetsService datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);

        DistributionsService.create(tripleStore, datasetsService)
                .onSuccess(service -> {
                    new ServiceBinder(vertx).setAddress(DistributionsService.SERVICE_ADDRESS).register(DistributionsService.class, service);
                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }

}
