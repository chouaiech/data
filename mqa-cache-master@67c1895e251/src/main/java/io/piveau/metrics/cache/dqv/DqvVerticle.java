package io.piveau.metrics.cache.dqv;

import io.piveau.dcatap.TripleStore;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DqvVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(DqvVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        log.info("Starting DQV service...");
        try {
            TripleStore tripleStore = new TripleStore(vertx, config());

            DqvProvider.create(tripleStore, ready -> {
                if (ready.succeeded()) {
                    new ServiceBinder(vertx)
                            .setAddress(DqvProvider.SERVICE_ADDRESS)
                            .register(DqvProvider.class, ready.result());

                    log.info("DQV service started");

                    startPromise.complete();
                } else {
                    log.error("DQV service failed", ready.cause());
                    startPromise.fail(ready.cause());
                }
            });
        } catch (Exception e) {
            startPromise.fail(e);
        }
    }
}
