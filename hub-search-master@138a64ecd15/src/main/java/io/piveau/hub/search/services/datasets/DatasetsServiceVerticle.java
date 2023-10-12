package io.piveau.hub.search.services.datasets;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class DatasetsServiceVerticle extends AbstractVerticle {

    private final IndexManager indexManager;

    public DatasetsServiceVerticle(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject config = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_ES_CONFIG);

        DatasetsService.create(vertx, config, indexManager).onSuccess(result -> {
            new ServiceBinder(vertx).setAddress(DatasetsService.SERVICE_ADDRESS)
                    .register(DatasetsService.class, result);
            startPromise.complete();
        }).onFailure(startPromise::fail);
    }
}
