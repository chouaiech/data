package io.piveau.metrics.cache.ratelimiting;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public interface RatelimitHandler extends Handler<RoutingContext> {

    static RatelimitHandler create(Vertx vertx) {
        return new RatelimitHandlerImpl(vertx);
    }

}
