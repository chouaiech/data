package io.piveau.metrics.cache.quartz;

import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.persistence.DatabaseProvider;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.quartz.spi.JobFactory;

@ProxyGen
public interface QuartzService {
    String SERVICE_ADDRESS = "io.piveau.metrics.cache.quartz.service";
    String ALL_CATALOGUES = "ALL_CATALOGUES";

    static QuartzService create(JobFactory jobFactory, DatabaseProvider databaseProvider, DqvProvider dqvProvider, Handler<AsyncResult<QuartzService>> readyHandler) {
        return new QuartzServiceImpl(jobFactory, databaseProvider, dqvProvider, readyHandler);
    }

    static QuartzService createProxy(Vertx vertx, String address) {
        return new QuartzServiceVertxEBProxy(vertx, address);
    }

    Future<JsonObject> listTriggers();

    Future<JsonArray> getTriggers(String pipeName);

    Future<Void> deleteTriggers(String pipeName);

    Future<JsonObject> getTrigger(String pipeName, String triggerId);

    Future<String> putTrigger(String pipeName, String triggerId, JsonObject trigger);

    Future<JsonObject> patchTrigger(String pipeName, String triggerId, JsonObject patch);

    Future<Void> deleteTrigger(String pipeName, String triggerId);

}
