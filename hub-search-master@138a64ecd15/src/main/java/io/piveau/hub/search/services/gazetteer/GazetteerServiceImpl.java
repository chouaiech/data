package io.piveau.hub.search.services.gazetteer;

import io.piveau.hub.search.util.gazetteer.GazetteerConnector;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class GazetteerServiceImpl implements GazetteerService {

    private final GazetteerConnector connector;

    GazetteerServiceImpl(GazetteerConnector connector, Handler<AsyncResult<GazetteerService>> handler) {
        this.connector = connector;
        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<JsonObject> autocomplete(String q) {
        Promise<JsonObject> promise = Promise.promise();
        connector.autocomplete(q).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }
}
