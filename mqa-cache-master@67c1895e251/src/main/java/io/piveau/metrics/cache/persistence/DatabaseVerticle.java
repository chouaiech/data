package io.piveau.metrics.cache.persistence;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;

public class DatabaseVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        DatabaseProvider.create(vertx, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx)
                    .setAddress(DatabaseProvider.SERVICE_ADDRESS)
                    .register(DatabaseProvider.class, ready.result());

                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

}
