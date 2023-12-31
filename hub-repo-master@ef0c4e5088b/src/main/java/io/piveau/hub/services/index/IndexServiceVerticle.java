package io.piveau.hub.services.index;

import io.piveau.hub.Constants;
import io.piveau.hub.util.logger.PiveauLoggerFactory;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.serviceproxy.ServiceBinder;

public class IndexServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        PiveauLoggerFactory.getLogger(getClass()).info("Starting IndexService Verticle");

        JsonObject conf = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE);
        WebClient client = WebClient.create(vertx, new WebClientOptions().setMaxPoolSize(100));

        IndexService.create(client, conf, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx).setAddress(IndexService.SERVICE_ADDRESS).register(IndexService.class, ready.result());
                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

}
