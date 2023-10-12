package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.piveau.hub.search.Constants;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.elasticsearch.ElasticsearchStatusException;

public class ExceptionHandler {

    private ExceptionHandler() {
        throw new IllegalStateException("Utility class");
    }

    private static ServiceException handleElasticException(Exception e) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            return new ServiceException(statusException.status().getStatus(), statusException.getMessage());
        } else {
            return new ServiceException(500, e.getMessage());
        }
    }

    public static void handleElasticException(String id, Exception e, Promise breakerPromise, Handler handler) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            if (Constants.SECURE_WITH_CIRCUIT_BREAKER.contains(statusException.status().getStatus())) {
                breakerPromise.fail(ExceptionHandler.handleElasticException(e));
            } else if (statusException.status().getStatus() == 404) {
                handler.handle(ServiceException.fail(404, id + " not found"));
                breakerPromise.complete();
            } else {
                handler.handle(Future.failedFuture(
                        new ServiceException(statusException.status().getStatus(), statusException.getMessage())));
                breakerPromise.complete();
            }
        } else {
            handler.handle(ServiceException.fail(500, e.getMessage()));
            breakerPromise.complete();
        }
    }

    public static void handleElasticException(String id, Exception e, Promise breakerPromise, Promise anotherPromise) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            if (Constants.SECURE_WITH_CIRCUIT_BREAKER.contains(statusException.status().getStatus())) {
                breakerPromise.fail(ExceptionHandler.handleElasticException(e));
            } else if (statusException.status().getStatus() == 404) {
                anotherPromise.fail(new ServiceException(404, id + " not found"));
                breakerPromise.complete();
            } else {
                anotherPromise.fail(
                        new ServiceException(statusException.status().getStatus(), statusException.getMessage()));
                breakerPromise.complete();
            }
        } else {
            anotherPromise.fail(new ServiceException(500, e.getMessage()));
            breakerPromise.complete();
        }
    }

    public static void handleElasticException(Exception e, Promise breakerPromise, Handler handler) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            if (Constants.SECURE_WITH_CIRCUIT_BREAKER.contains(statusException.status().getStatus())) {
                breakerPromise.fail(ExceptionHandler.handleElasticException(e));
            } else {
                handler.handle(Future.failedFuture(
                        new ServiceException(statusException.status().getStatus(), statusException.getMessage())));
                breakerPromise.complete();
            }
        } else {
            handler.handle(ServiceException.fail(500, e.getMessage()));
            breakerPromise.complete();
        }
    }

    public static void handleElasticException(Exception e, Promise breakerPromise, Promise anotherPromise) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            if (Constants.SECURE_WITH_CIRCUIT_BREAKER.contains(statusException.status().getStatus())) {
                breakerPromise.fail(ExceptionHandler.handleElasticException(e));
            } else {
                anotherPromise.fail(
                        new ServiceException(statusException.status().getStatus(), statusException.getMessage()));
                breakerPromise.complete();
            }
        } else {
            anotherPromise.fail(new ServiceException(500, e.getMessage()));
            breakerPromise.complete();
        }
    }

    public static void handleException(Exception e, Handler<AsyncResult<JsonObject>> handler) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            handler.handle(ServiceException.fail(statusException.status().getStatus(), statusException.getMessage()));
        } else {
            handler.handle(ServiceException.fail(500, e.getMessage()));
        }
    }

    public static void handleException(Exception e, Promise promise) {
        if (e.getClass().equals(ElasticsearchStatusException.class)) {
            ElasticsearchStatusException statusException = (ElasticsearchStatusException) e;
            promise.fail(new ServiceException(statusException.status().getStatus(), statusException.getMessage()));
        } else {
            promise.fail(new ServiceException(500, e.getMessage()));
        }
    }
}
