package io.piveau.hub.search.services.catalogues;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class CataloguesServiceVerticle extends AbstractVerticle {

    private final IndexManager indexManager;

    public CataloguesServiceVerticle(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject config = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_ES_CONFIG);

        CataloguesService.create(vertx, config, indexManager).onSuccess(result -> {
            new ServiceBinder(vertx).setAddress(CataloguesService.SERVICE_ADDRESS)
                    .register(CataloguesService.class, result);
            startPromise.complete();
        }).onFailure(startPromise::fail);
    }
}
