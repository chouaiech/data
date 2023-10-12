package io.piveau.hub.search.util.gazetteer;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GazetteerConnector {

    Logger LOG = LoggerFactory.getLogger(GazetteerConnector.class);

    // Gezetteer url
    String url;

    // Webclient
    WebClient client;

    public static GazetteerConnector create(WebClient client, JsonObject config, Handler<AsyncResult<GazetteerConnector>> handler) {
        String type = config.getString("type");
        if (type == null || type.isEmpty()) {
            return new GazetteerConnectorConterra(client, config, handler);
        } else {
            switch(type) {
                case "conterra":
                    return new GazetteerConnectorConterra(client, config, handler);
                case "osmnames":
                    return new GazetteerConnectorOsmnames(client, config, handler);
                default:
                    return new GazetteerConnectorConterra(client, config, handler);
            }
        }
    }

    GazetteerConnector(WebClient client, JsonObject config, Handler<AsyncResult<GazetteerConnector>> handler) {
        this.client = client;
        this.url = config.getString("url");
        handler.handle(Future.succeededFuture(this));
    }

    public Future<JsonObject> autocomplete(String q) {
        Promise<JsonObject> promise = Promise.promise();
        if(q == null || q.isEmpty()) {
            promise.fail(new ServiceException(404, "Query null or empty!"));
        } else {
            if (this.url == null || this.url.isEmpty()) {
                promise.fail(new ServiceException(404, "Gazetteer url missing!"));
            } else {
                client.getAbs(buildUrl(q)).send(ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();

                        if(response.statusCode() != 200) {
                            LOG.error("Gezetteer autocomplete: Received response with status code "
                                    + response.statusCode());
                            promise.fail(new ServiceException(response.statusCode(),
                                    "Gazetteer Service does not respond properly"));
                        } else {
                            try {
                                promise.complete(
                                        new JsonObject()
                                                .put("status", 200)
                                                .put("result", querySuggestion(new JsonObject(response.body().toString())))
                                );
                            } catch (DecodeException e) {
                                LOG.error("Gezetteer autocomplete: Gazetteer Service didn't respond a json");
                                promise.fail(new ServiceException(500,
                                        "Gazetteer Service didn't respond a json"));
                            }
                        }
                    } else {
                        LOG.error("Gezetteer autocomplete: " + ar.cause().getMessage());
                        promise.fail(new ServiceException(500, ar.cause().getMessage()));
                    }
                });
            }
        }
        return promise.future();
    }

    public abstract String buildUrl(String q);

    public abstract JsonObject querySuggestion(JsonObject message);
}
