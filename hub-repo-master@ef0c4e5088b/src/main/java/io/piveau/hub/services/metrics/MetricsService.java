package io.piveau.hub.services.metrics;

import io.piveau.dcatap.TripleStore;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface MetricsService {
    String SERVICE_ADDRESS = "io.piveau.hub.metrics.queue";

    static Future<MetricsService> create(TripleStore tripleStore, TripleStore shadowTripleStore, Vertx vertx, JsonObject config) {
        return Future.future(promise -> new MetricsServiceImpl(tripleStore, shadowTripleStore, vertx, config, promise));
    }

    static MetricsService createProxy(Vertx vertx, String address) {
        return new io.piveau.hub.services.metrics.MetricsServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    /**
     * get a Metrics graph via the normalized dataset ID
     *
     * @param datasetId
     * @param history
     * @param contentType
     * @return Future
     */
    Future<String> getMetrics(String datasetId, boolean history, String contentType);

    Future<String> putMetrics(String datasetId, String content, String contentType);

    Future<Void> deleteMetrics(String datasetId, String catalogueId);

}
