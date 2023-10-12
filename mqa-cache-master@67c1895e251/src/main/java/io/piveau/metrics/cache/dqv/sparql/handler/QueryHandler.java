package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.dcatap.TripleStore;
import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class QueryHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected String queryName = "";
    protected Object[] parameters;

    protected QueryHandler(String queryName, Object... parameters) {
        this.queryName = queryName;
        this.parameters = parameters;
    }

    protected final JsonObject result = new JsonObject();

    protected String getQuery() {
        return QueryCollection.getQuery(queryName);
    }

    protected String getFormattedQuery() {
        return String.format(getQuery(), parameters);
    };

    protected JsonObject getResult() {
        return result;
    }

    public abstract void handle(ResultSet resultSet);

    public Future<JsonObject> query(TripleStore tripleStore) {
        Promise<JsonObject> promise = Promise.promise();

        String formatted = getFormattedQuery();
        log.debug("Query: {}", formatted);
        tripleStore.select(formatted).onSuccess(resultSet -> {
            handle(resultSet);
            promise.complete(getResult());
        }).onFailure(promise::fail);

        return promise.future();
    }

}
