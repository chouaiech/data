package io.piveau.metrics.cache.dqv;

import io.piveau.dcatap.TripleStore;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

import java.util.List;

@ProxyGen
public interface DqvProvider {

    String SERVICE_ADDRESS = "metric.cache.dqv";

    static DqvProvider create(TripleStore tripleStore, Handler<AsyncResult<DqvProvider>> readyHandler) {
        return new DqvProviderImpl(tripleStore, readyHandler);
    }


    static DqvProvider createProxy(Vertx vertx, String address, DeliveryOptions options) {
        return new DqvProviderVertxEBProxy(vertx, address, options);
    }

    void listCatalogues(Handler<AsyncResult<List<String>>> resultHandler);

    // findability
    void getKeywordAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getCategoryAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getSpatialAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getTemporalAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    // accessibility
    void getAccessUrlStatusCodes(String id, DocumentScope documentScope, Handler<AsyncResult<StatusCodes>> resultHandler);

    void getDownloadUrlAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getDownloadUrlStatusCodes(String id, DocumentScope documentScope, Handler<AsyncResult<StatusCodes>> resultHandler);

    // error status codes
    void getDistributionReachabilityDetails(String catalogueId, int offset, int limit, String lang, Handler<AsyncResult<JsonObject>> resultHandler);

    // interoperability
    void getFormatAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getMediaTypeAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getFormatMediaTypeAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getFormatMediaTypeNonProprietary(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getFormatMediaTypeMachineReadable(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getDcatApCompliance(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    // reusability
    void getLicenceAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getLicenceAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getAccessRightsAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getAccessRightsAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getContactPointAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getPublisherAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    // contextuality
    void getRightsAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getByteSizeAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getDateIssuedAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    void getDateModifiedAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler);

    // scoring
    void getAverageScore(String id, DocumentScope documentScope, String measurementUriRef, Handler<AsyncResult<Double>> resultHandler);

    // catalogue info
    void getCatalogueInfo(String catalogueId, Handler<AsyncResult<JsonObject>> resultHandler);

    // catalogue violations
    void getCatalogueViolations(String catalogueId, int offset, int limit,String lang, Handler<AsyncResult<JsonObject>> resultHandler);
    void getCatalogueViolationsCount(String catalogueId,Handler<AsyncResult<JsonObject>> resultHandler);

    // dataset metrics
    void getDatasetMetrics(String datasetId, String lang, Handler<AsyncResult<JsonObject>> resultHandler);
    void getDistributionMetricsPerDataset(String datasetId, String lang, Handler<AsyncResult<JsonObject>> resultHandler);

}
