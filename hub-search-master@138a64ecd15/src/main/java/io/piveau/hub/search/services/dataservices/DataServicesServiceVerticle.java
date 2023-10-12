package io.piveau.hub.search.services.dataservices;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class DataServicesServiceVerticle extends AbstractVerticle {

    private final IndexManager indexManager;

    public DataServicesServiceVerticle(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject config = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_ES_CONFIG);

        DataServicesService.create(vertx, config, indexManager).onSuccess(result -> {
            new ServiceBinder(vertx).setAddress(DataServicesService.SERVICE_ADDRESS)
                    .register(DataServicesService.class, result);
            startPromise.complete();
        }).onFailure(startPromise::fail);
    }
}
