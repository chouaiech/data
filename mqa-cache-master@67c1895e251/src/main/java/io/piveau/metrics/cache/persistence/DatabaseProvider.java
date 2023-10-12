package io.piveau.metrics.cache.persistence;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ProxyGen
public interface DatabaseProvider {

    String SERVICE_ADDRESS = "metric.cache.database";


    /**
     * This method is called in {DatabaseVerticle.java} when starting this verticle
     *
     * @param vertx the vert.x object
     * @param readyHandler called, when verticle is running
     * @return the Implementation of this class
     */
    static DatabaseProvider create(Vertx vertx, Handler<AsyncResult<DatabaseProvider>> readyHandler) {
        return new DatabaseProviderImpl(vertx, readyHandler);
    }

    /**
     * @param vertx the vert.x object
     * @param address the eventbus address for this verticle
     * @return the Implementation of this class
     */
    static DatabaseProvider createProxy(Vertx vertx, String address) {
        return new DatabaseProviderVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(3000000));
    }


    /**
     * Get the current data for a single document
     *
     * @param documentScope the collection the document is in
     * @param id the document ID, that is, the catalogue ID, a three letter abbreviation for a country or "global"
     * @param filter list of dimensions that should should be returned in the document
     * @param resultHandler called with an result object
     */
    void getDocument(DocumentScope documentScope, String id, List<String> filter, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Get the current data for all documents in this scope
     * @param documentScope the collection the document is in
     * @param filter list of dimensions that should should be returned in the document
     * @param resultHandler called with an result object
     */
    void getDocumentList(DocumentScope documentScope, List<String> filter, Handler<AsyncResult<JsonObject>> resultHandler);

    /**
     * Get the historic aggregated data for a single document
     *
     * @param documentScope the collection the document is in
     * @param id the document ID, that is, the catalogue ID, a three letter abbreviation for a country or "global"
     * @param filter list of dimensions that should should be returned in the document
     * @param resolution the averaging resolution, one of day, month or year
     * @param startDate the  date for the first value
     * @param endDate the date for the last value
     * @param resultHandler called with an result object
     */
    void getHistory(DocumentScope documentScope, String id, List<String> filter,String resolution, String startDate, String endDate, Handler<AsyncResult<JsonObject>> resultHandler);

    void getHistoryList(DocumentScope documentScope, List<String> filter, String resolution, String startDate, String endDate, Handler<AsyncResult<JsonObject>> resultHandler);


    /**
     * Delete a single document from the cache
     *
     * @param documentScope the collection the document is in
     * @param id the document ID, that is, the catalogue ID, a three letter abbreviation for a country or "global"
     * @param resultHandler called with an result object
     */
    void deleteDocument(DocumentScope documentScope, String id, Handler<AsyncResult<String>> resultHandler);


    /**
     * update the cache with metrics from the triplestore
     */
    void refreshMetrics();

    /**
     * Update a single document
     * @param id  the document ID, that is, the catalogue ID, a three letter abbreviation for a country or "global"
     */
    void refreshSingleMetrics(String id);

    /**
     * migrate score to new db schema
     */
    void moveScore();

    /**
     * Drop all metrics from database
     */
    void clearMetrics();


    /**
     *
     * @param catalogueId
     * @param offset
     * @param limit
     * @param lang
     * @param resultHandler
     */

    void getDistributionReachabilityDetails(String catalogueId, int offset, int limit, String lang, Handler<AsyncResult<JsonObject>> resultHandler);
}
