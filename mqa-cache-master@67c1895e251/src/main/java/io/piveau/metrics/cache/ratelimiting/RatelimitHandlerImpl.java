package io.piveau.metrics.cache.ratelimiting;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Map;

public class RatelimitHandlerImpl implements RatelimitHandler {

    Map<String, Long> buckets;
    Map<String, Long> pageBuckets;

    public RatelimitHandlerImpl(Vertx vertx ) {

        buckets = vertx.sharedData().getLocalMap("buckets");
    }

    //  rate limit for pages, if user tries to access a page more than once in a given time frame
    //  the page is rate limited
    // TODO: add a configurable time frame
    // TODO: add a configurable number of attempts per time frame for the whole application
    @Override
    public void handle(RoutingContext routingContext) {
        String host = routingContext.request().remoteAddress().host();
        String page = routingContext.request().absoluteURI();
        String key = host + page;



        long now = System.currentTimeMillis();
        if(buckets.containsKey(key)) {
            long last = buckets.get(key);

            if(now - last > 10000) {
                buckets.put(key, now);
                routingContext.next();
            } else {
                routingContext.response().setStatusCode(429).end();
            }
        } else {
            buckets.put(key, now);
            routingContext.next();
        }

    }
}
