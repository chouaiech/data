package io.piveau.hub.search.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.serviceproxy.ServiceException;

class ContextHandler {

    private void failureResponse(HttpServerResponse response, Throwable cause) {
        response.putHeader("Content-Type", "text/plain");
        if (cause instanceof ServiceException failure) {
            response.setStatusCode(failure.failureCode()).end(failure.getMessage());
        } else {
            response.setStatusCode(500).end(cause.getMessage());
        }
    }

    void handleFailure(RoutingContext context, AsyncResult<JsonObject> ar) {
        failureResponse(context.response(), ar.cause());
    }

    void handleContextNoContent(RoutingContext context, AsyncResult<JsonObject> ar) {
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        if (ar.succeeded()) {
            Integer status = (Integer) ar.result().remove("status");
            context.response().setStatusCode(status);
            context.response().end();
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }

    void handleContext(RoutingContext context, AsyncResult<JsonObject> ar) {
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        if (ar.succeeded()) {
            context.response().putHeader("Content-Type", "application/json");
            Integer status = (Integer) ar.result().remove("status");
            context.response().setStatusCode(status);
            context.response().end(ar.result().getJsonObject("result").toString());
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }

    void handleContextLegacy(RoutingContext context, AsyncResult<JsonObject> ar) {
        JsonObject response = new JsonObject();
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        if (ar.succeeded()) {
            context.response().putHeader("Content-Type", "application/json");
            response.put("result", ar.result().getJsonObject("result"));
            Integer status = (Integer) ar.result().remove("status");
            context.response().setStatusCode(status);
            context.response().end(response.toString());
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }

    void handleContextJsonArray(RoutingContext context, AsyncResult<JsonArray> ar) {
        context.response().putHeader("Content-Type", "application/json");
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        if (ar.succeeded()) {
            context.response().setStatusCode(200);
            context.response().end(ar.result().toString());
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }

    void handleContextVoid(RoutingContext context, AsyncResult<Void> ar) {
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        if (ar.succeeded()) {
            context.response().setStatusCode(204);
            context.response().end();
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }

    void handleContextXML(RoutingContext context, AsyncResult<JsonObject> ar) {
        if (ar.succeeded()) {
            context.response().putHeader("Content-Type", "application/xml");
            Integer status = (Integer) ar.result().remove("status");
            context.response().setStatusCode(status);
            context.response().end(ar.result().getString("result"));
        } else {
            failureResponse(context.response(), ar.cause());
        }
    }
}
