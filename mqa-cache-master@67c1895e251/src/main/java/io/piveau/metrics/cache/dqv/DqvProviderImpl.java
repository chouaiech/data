package io.piveau.metrics.cache.dqv;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.dcatap.TripleStore;
import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.piveau.metrics.cache.dqv.sparql.handler.*;
import io.piveau.metrics.cache.dqv.sparql.util.PercentageMath;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.piveau.vocabularies.vocabulary.PV;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Resource;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.piveau.utils.DateTimeKt.normalizeDateTime;

class DqvProviderImpl implements DqvProvider {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Cache<String, Future> violationsCache;
    private final Cache<String, Future> violationsCountCache;
    private final Cache<String, LocalDateTime> lastModifiedCache;
    private final TripleStore tripleStore;

    private final Map<String, JsonObject> catalogueInfos = new HashMap<>();

    DqvProviderImpl(TripleStore tripleStore, Handler<AsyncResult<DqvProvider>> readyHandler) {
        this.tripleStore = tripleStore;

        CacheConfigurationBuilder<String, Future> cacheConfigurationBuilder = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, Future.class, ResourcePoolsBuilder.heap(1000)).withExpiry(ExpiryPolicyBuilder.noExpiration());

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("violations", cacheConfigurationBuilder)
                .build(true);

        violationsCache = cacheManager.getCache("violations", String.class, Future.class);

        CacheManager cacheManager2 = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("violationsCount", cacheConfigurationBuilder)
                .build(true);

        violationsCountCache = cacheManager2.getCache("violationsCount", String.class, Future.class);


        CacheManager cacheManager3 = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("lastmodified",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, LocalDateTime.class,
                                        ResourcePoolsBuilder.heap(1000))
                                .withExpiry(ExpiryPolicyBuilder.noExpiration()))
                .build(true);

        lastModifiedCache = cacheManager3.getCache("lastmodified", String.class, LocalDateTime.class);

        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public void listCatalogues(Handler<AsyncResult<List<String>>> resultHandler) {
        getCatalogueList().onComplete(resultHandler);
    }


    @Override
    public void getKeywordAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.keywordAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getCategoryAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.categoryAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getSpatialAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.spatialAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getTemporalAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.temporalAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getAccessUrlStatusCodes(String id, DocumentScope documentScope, Handler<AsyncResult<StatusCodes>> resultHandler) {
        getStatusCodes(PV.accessUrlStatusCode, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getDownloadUrlAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.downloadUrlAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getDistributionReachabilityDetails(String catalogueId, int offset, int limit, String lang, Handler<AsyncResult<JsonObject>> resultHandler) {
        getErrorStatusCodes(catalogueId, offset, limit, lang).onComplete(resultHandler);
    }

    @Override
    public void getDownloadUrlStatusCodes(String catalogueId, DocumentScope documentScope, Handler<AsyncResult<StatusCodes>> resultHandler) {
        getStatusCodes(PV.downloadUrlStatusCode, documentScope, catalogueId).onComplete(resultHandler);
    }

    @Override
    public void getFormatAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.formatAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getMediaTypeAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.mediaTypeAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getFormatMediaTypeAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.formatMediaTypeVocabularyAlignment, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getFormatMediaTypeNonProprietary(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.formatMediaTypeNonProprietary, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getFormatMediaTypeMachineReadable(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.formatMediaTypeMachineInterpretable, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getDcatApCompliance(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.dcatApCompliance, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getLicenceAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.licenceAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getLicenceAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.knownLicence, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getAccessRightsAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.accessRightsAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getAccessRightsAlignment(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.accessRightsVocabularyAlignment, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getContactPointAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.contactPointAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getPublisherAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.publisherAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getRightsAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.rightsAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getByteSizeAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.byteSizeAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getDateIssuedAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.dateIssuedAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getDateModifiedAvailability(String id, DocumentScope documentScope, Handler<AsyncResult<Double>> resultHandler) {
        getYesPercentage(PV.dateModifiedAvailability, documentScope, id).onComplete(resultHandler);
    }

    @Override
    public void getAverageScore(String id, DocumentScope documentScope, String measurementUriRef, Handler<AsyncResult<Double>> resultHandler) {
        QueryHandler queryHandler = ScoreQueryHandler.create(id, documentScope, measurementUriRef);
        queryHandler.query(tripleStore).onSuccess(result -> {
            if (!result.isEmpty()) {
                double averageScore = Math.round(result.getDouble("averageScore"));
                resultHandler.handle(Future.succeededFuture(averageScore));
            } else {
                resultHandler.handle(Future.succeededFuture(0.0));
            }
        }).onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));
    }

    private void resolveDatasetTitle(Iterator<Object> iterator, String lang, Map<String, String> cache, Promise<Void> promise) {
        if (iterator.hasNext()) {
            JsonObject value = (JsonObject) iterator.next();
            String dataset = value.getString("reference");
            if (cache.containsKey(dataset)) {
                value.put("title", cache.get(dataset));
                resolveDatasetTitle(iterator, lang, cache, promise);
            } else {
                getDatasetTitle(dataset, lang, ar -> {
                    if (ar.succeeded()) {
                        value.put("title", ar.result());
                        cache.put(dataset, ar.result());
                    }
                    resolveDatasetTitle(iterator, lang, cache, promise);
                });
            }
        } else {
            promise.complete();
        }
    }

    private void getDatasetTitle(String uri, String prefLang, Handler<AsyncResult<String>> asyncResultHandler) {
        String query = String.format(QueryCollection.getQuery("DatasetTitle"), uri, prefLang, prefLang);
        tripleStore.select(query).onSuccess(resultSet -> {
            if (resultSet.hasNext()) {
                QuerySolution solution = resultSet.next();
                if (solution.contains("preferred")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("preferred").getLexicalForm()));
                } else if (solution.contains("default")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("default").getLexicalForm()));
                } else if (solution.contains("empty")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("empty").getLexicalForm()));
                } else if (solution.contains("any")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("any").getLexicalForm()));
                } else {
                    asyncResultHandler.handle(Future.failedFuture("No title found"));
                }
            } else {
                asyncResultHandler.handle(Future.failedFuture("No title found"));
            }
        }).onFailure(cause -> asyncResultHandler.handle(Future.failedFuture(cause)));
    }

    private Future<JsonObject> getErrorStatusCodes(String catalogueId, int offset, int limit, String lang) {
        Promise<JsonObject> promise = Promise.promise();

        JsonObject response = new JsonObject();

        DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(catalogueId);
//        QueryHandler queryHandler = new StatusCodeErrorsQueryHandler(uriRef.getCatalogueGraphName(), uriRef.getCatalogueUriRef(), offset, limit);
//        queryHandler.query(tripleStore)
//        QueryHandler queryAccessUrlCountHandler = new StatusCodeErrorsCountQueryHandler(uriRef.getCatalogueGraphName(), uriRef.getCatalogueUriRef(), DCAT.accessURL, PV.accessUrlStatusCode);
//        queryAccessUrlCountHandler.query(tripleStore)
//                .compose(count -> {
//                    response.put("count", response.getInteger("count", 0) + count.getInteger("count", 0));
//                    QueryHandler queryDownloadUrlCountHandler = new StatusCodeErrorsCountQueryHandler(uriRef.getCatalogueGraphName(), uriRef.getCatalogueUriRef(), DCAT.downloadURL, PV.downloadUrlStatusCode);
//                    return queryDownloadUrlCountHandler.query(tripleStore);
//                })
        QueryHandler queryAccessUrlCountHandler = new StatusCodeErrorsCountQueryHandler(uriRef.getCatalogueUriRef());
        queryAccessUrlCountHandler.query(tripleStore)
                .compose(count -> {
                    response.put("count", response.getInteger("count", 0) + count.getInteger("count", 0));
                    if (offset == 0 && limit == 1) {
                        Promise<JsonObject> emptyPromise = Promise.promise();
                        emptyPromise.complete(new JsonObject());
                        return emptyPromise.future();
                    } else {
                        QueryHandler queryHandler = new StatusCodeErrorsQueryHandler(uriRef.getCatalogueUriRef(), offset, limit);
                        return queryHandler.query(tripleStore);
                    }
                })
                .onSuccess(statusCodes -> {
                    Iterator<Object> it = statusCodes.getJsonArray("results", new JsonArray()).iterator();
                    Promise<Void> titlePromise = Promise.promise();
                    resolveDatasetTitle(it, lang, new HashMap<>(), titlePromise);
                    response.put("results", statusCodes.getJsonArray("results", new JsonArray()));
                    titlePromise.future().onComplete(v -> promise.complete(response));
                })
                .onFailure(promise::fail);

        return promise.future();
    }

    private Future<StatusCodes> getStatusCodes(Resource metric, DocumentScope documentScope, String id) {
        Promise<StatusCodes> promise = Promise.promise();

        QueryHandler queryHandler = StatusCodeQueryHandler.create(documentScope, id, metric);
        queryHandler.query(tripleStore).onSuccess(result -> {
            if (!result.isEmpty()) {
                int sum = result.fieldNames().stream().mapToInt(result::getInteger).sum();

                Map<String, Double> statusCodes = new HashMap<>();
                result.fieldNames().forEach(name -> statusCodes.put(name, result.getDouble(name) / sum * 100));

                promise.complete(new StatusCodes(PercentageMath.roundMap(statusCodes)));
            } else {
                promise.complete(new StatusCodes(Collections.emptyMap()));
            }
        }).onFailure(promise::fail);

        return promise.future();
    }

    private Future<Double> getYesPercentage(Resource metric, DocumentScope scope, String id) {
        Promise<Double> promise = Promise.promise();

        QueryHandler queryHandler = BooleanMeasurementsQueryHandler.create(scope, metric, id);
        queryHandler.query(tripleStore).onSuccess(result -> {
            if (result.isEmpty()) {
                promise.complete(-1.0);
            } else {
                promise.complete(PercentageMath.calculateYesPercentage(result));
            }
        }).onFailure(cause -> promise.complete(-1.0));

        return promise.future();
    }

    public void getCatalogueInfo(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(catalogueInfos.get(id)));
    }

    private Future<List<String>> getCatalogueList() {
        Promise<List<String>> promise = Promise.promise();

        CatalogueInfosQueryHandler queryHandler = new CatalogueInfosQueryHandler();
        queryHandler.query(tripleStore).onSuccess(catalogues -> {
            catalogueInfos.clear();
            catalogues.forEach(entry -> catalogueInfos.put(entry.getKey(), (JsonObject) entry.getValue()));
            promise.complete(new ArrayList<>(catalogueInfos.keySet()));
        }).onFailure(cause -> {
            // Log the failure and fail the promise.
            log.error("Failed to query catalogue infos", cause);
            promise.fail(cause);
        });

        return promise.future();
    }

    /**
     * Set the result Handler for the getViolationsCount
     *
     * @param id            Catalogue id
     * @param resultHandler Handler that will be called with the result
     */
    public void getCatalogueViolationsCount(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
        DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(id);
        QueryHandler queryHandler = new ViolationsCountQueryHandler(uriRef.getCatalogueUriRef());
        queryHandler.query(tripleStore).onSuccess(result ->
                resultHandler.handle(Future.succeededFuture(result.put("success", true)))
        ).onFailure(cause -> {
            JsonObject failure = new JsonObject()
                    .put("success", false)
                    .put("count", -1);
            resultHandler.handle(Future.succeededFuture(failure));
        });
    }

    /**
     * Set the result Handler for the getViolations
     *
     * @param id            Catalogue id
     * @param offset        Number of violations you wish to skip before selecting violations.
     * @param limit         Number of results returned
     * @param lang          Preferred language for the title
     * @param resultHandler handler that will be called with the result
     */
    public void getCatalogueViolations(String id, int offset, int limit, String lang, Handler<AsyncResult<JsonObject>> resultHandler) {

        DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(id);

        QueryHandler modifiedQueryHandler = new CatalogueModifiedQueryHandler(uriRef.getCatalogueUriRef());

        modifiedQueryHandler.query(tripleStore).onSuccess(catmodified -> {
            String normalizedTime = normalizeDateTime(catmodified.getString("modified", ""));
            boolean useCached = true;
            if (normalizedTime != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    LocalDateTime modified = LocalDateTime.parse(normalizedTime, formatter);
                    useCached = !lastModifiedCache.containsKey(id) || !lastModifiedCache.get(id).isBefore(modified);
                    if (!lastModifiedCache.containsKey(id)) {
                        lastModifiedCache.put(id, modified);
                    }
                } catch (Exception e) {
                    useCached = false;
                }


            }


            JsonObject result = new JsonObject();

            Future<JsonObject> countFuture;
            if (violationsCountCache.containsKey(id) && useCached) {
                countFuture = violationsCountCache.get(id);
            } else {


                QueryHandler countQueryHandler = new ViolationsCountQueryHandler(uriRef.getCatalogueUriRef());
                countFuture = countQueryHandler.query(tripleStore);
                violationsCountCache.put(id, countFuture);

            }

            Promise<Void> promise = Promise.promise();

            boolean finalUseCached = useCached;
            countFuture.compose(count -> {
                result.put("success", true);
                result.put("result", count);
                String cacheKey = id + "-" + offset + "-" + limit + "-" + lang;
                Future<JsonObject> violationsFuture;
                if (violationsCache.containsKey(cacheKey) && finalUseCached) {
                    violationsFuture = violationsCache.get(cacheKey);
                } else {
                    QueryHandler queryHandler = new ViolationsQueryHandler(uriRef.getCatalogueUriRef(), offset, limit);
                    violationsFuture = queryHandler.query(tripleStore);
                    violationsCache.put(cacheKey, violationsFuture);
                }

                return violationsFuture;


            }).compose(violations -> {
                result.getJsonObject("result").put("results", violations.getJsonArray("violations"));

                resolveDatasetTitle(violations.getJsonArray("violations").iterator(), lang, new HashMap<>(), promise);
                return promise.future();
            }).onSuccess(v -> resultHandler.handle(Future.succeededFuture(result))
            ).onFailure(cause ->
                    resultHandler.handle(Future.failedFuture(cause))
            );

        }).onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));


    }


    /**
     * Get all Metrics for a dataset and its distributions
     *
     * @param datasetId     Dataset id
     * @param resultHandler handler that will be called with the result
     */
    public void getDatasetMetrics(String datasetId, String lang, Handler<AsyncResult<JsonObject>> resultHandler) {
        DCATAPUriRef schema = DCATAPUriSchema.createFor(datasetId);
        String graphName = schema.getMetricsGraphName();

        tripleStore.getGraph(graphName)
                .onSuccess(model -> {
                    if (!model.isEmpty()) {
                        DatasetMetrics datasetMetrics = new DatasetMetrics(model, tripleStore, lang);
                        datasetMetrics.getDatasetMetrics(ar -> {
                            if (ar.succeeded()) {
                                resultHandler.handle(Future.succeededFuture(ar.result()));
                            } else {
                                resultHandler.handle(Future.failedFuture(ar.cause()));
                            }
                        });
                    } else {
                        resultHandler.handle(Future.failedFuture("DQV of dataset not found"));
                    }
                })
                .onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));
    }

    public void getDistributionMetricsPerDataset(String datasetId, String lang, Handler<AsyncResult<JsonObject>> resultHandler) {
        DCATAPUriRef schema = DCATAPUriSchema.createFor(datasetId);
        String graphName = schema.getMetricsGraphName();

        tripleStore.getGraph(graphName)
                .onSuccess(model -> {
                    if (model.isEmpty()) {
                        resultHandler.handle(Future.failedFuture("Dataset not found"));
                    } else {
                        DatasetMetrics datasetMetrics = new DatasetMetrics(model, tripleStore, lang);
                        datasetMetrics.getDistributionMetricsPerDataset(ar -> {
                            if (ar.succeeded()) {
                                resultHandler.handle(Future.succeededFuture(ar.result()));
                            } else {
                                resultHandler.handle(Future.failedFuture(ar.cause()));
                            }
                        });
                    }
                })
                .onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));
    }

}