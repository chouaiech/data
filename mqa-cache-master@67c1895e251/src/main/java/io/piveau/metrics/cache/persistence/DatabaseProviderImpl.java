package io.piveau.metrics.cache.persistence;

import io.piveau.log.PiveauLogger;
import io.piveau.metrics.cache.dqv.Dimension;
import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.dqv.StatusCodes;
import io.piveau.metrics.cache.dqv.sparql.util.PercentageMath;
import io.piveau.metrics.cache.dqv.sparql.util.SparqlHelper;
import io.piveau.metrics.cache.persistence.util.Aggregation;
import io.piveau.utils.PiveauContext;
import io.piveau.vocabularies.vocabulary.PV;
import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static io.piveau.metrics.cache.ApplicationConfig.*;

import static io.piveau.metrics.cache.dqv.sparql.util.SparqlHelper.getCountries;

public class DatabaseProviderImpl implements DatabaseProvider {

    private static final Logger log = LoggerFactory.getLogger(DatabaseProviderImpl.class);

    private final DqvProvider dqvProvider;
    private final MongoClient dbClient;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final PiveauLogger piveauLog = new PiveauContext("metrics-cache", "cache").log();

    private boolean refreshRunning = false;

    /**
     * Default constructor
     *
     * @param vertx        the vert.x object
     * @param readyHandler called, when everything is set up, fails, when no db connection can be established
     */
    public DatabaseProviderImpl(Vertx vertx, Handler<AsyncResult<DatabaseProvider>> readyHandler) {

        //establish connection to the dqv verticle
        DeliveryOptions options = new DeliveryOptions().setSendTimeout(3000000);
        dqvProvider = DqvProvider.createProxy(vertx, DqvProvider.SERVICE_ADDRESS, options);

        //establish db connection
        JsonObject env = vertx.getOrCreateContext().config();

        JsonObject config = new JsonObject()
                .put("connection_string", env.getString(ENV_MONGODB_CONNECTION, DEFAULT_MONGODB_CONNECTION))
//                .put("connectTimeoutMS", 30000)
//                .put("socketTimeoutMS", 10000)
//                .put("serverSelectionTimeoutMS", 1000)
//                .put("keepAlive", true)
                .put("db_name", env.getString(ENV_MONGODB_DB_NAME, DEFAULT_MONGODB_DB_NAME));

        log.debug("MongoDB config: {}", config);
        dbClient = MongoClient.createShared(vertx, config);

        readyHandler.handle(Future.succeededFuture(this));
    }

    private final List<String> filterValues = Arrays.asList(
            "findability",
            "accessibility",
            "interoperability",
            "reusability",
            "contextuality",
            "score",
            "info"
    );

    /**
     * Get a single document
     *
     * @param documentScope the collection the document is in
     * @param id            the document ID, that is, the catalogue ID, a three letter abbreviation for a country or "global"
     * @param filter        list of dimensions that should should be returned in the document
     * @param resultHandler called with an result object
     */
    @Override
    public void getDocument(DocumentScope documentScope, String id, List<String> filter, Handler<AsyncResult<JsonObject>> resultHandler) {
        if (filter.isEmpty()) {
            filter.addAll(filterValues);
        } else {
            filter.retainAll(filterValues);
        }

        new Aggregation(id, dbClient, documentScope, filter).aggregateCurrent(resultHandler);
    }

    @Override
    public void getDocumentList(DocumentScope documentScope, List<String> filter, Handler<AsyncResult<JsonObject>> resultHandler) {
        if (filter.isEmpty()) {
            filter.addAll(filterValues);
        } else {
            filter.retainAll(filterValues);
        }
        new Aggregation(dbClient, documentScope, filter).aggregateCurrent(resultHandler);
    }

    @Override
    public void getHistory(DocumentScope documentScope, String id, List<String> filter, String resolution, String startDate, String endDate, Handler<AsyncResult<JsonObject>> resultHandler) {
        if (filter.isEmpty()) {
            filter.addAll(filterValues);
        } else {
            filter.retainAll(filterValues);
        }

        new Aggregation(id, dbClient, documentScope, filter).aggregateHistoric(startDate, endDate, resolution, resultHandler);
    }


    @Override
    public void getHistoryList(DocumentScope documentScope, List<String> filter, String resolution, String startDate, String endDate, Handler<AsyncResult<JsonObject>> resultHandler) {
        if (filter.isEmpty()) {
            filter.addAll(filterValues);
        } else {
            filter.retainAll(filterValues);
        }

        new Aggregation(dbClient, documentScope, filter).aggregateHistoric(startDate, endDate, resolution, resultHandler);
    }

    @Override
    public void deleteDocument(DocumentScope documentScope, String id, Handler<AsyncResult<String>> resultHandler) {
        JsonObject filter = new JsonObject().put("_id", id);
        dbClient.removeDocument(documentScope.name(), filter)
                .compose(result -> dbClient.removeDocument(DocumentScope.SCORE.name(), filter))
                .onSuccess(result -> resultHandler.handle(Future.succeededFuture("success")))
                .onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));
    }

    @Override
    public void refreshMetrics() {
        if (refreshRunning) {
            piveauLog.error("Refresh already running!");
            return;
        }
        refreshRunning = true;
        piveauLog.info("Start refreshing cache");
        long start = System.currentTimeMillis();
        dqvProvider.listCatalogues(ar -> {
            if (ar.succeeded()) {
                piveauLog.info("Found {} catalogues, first refreshing global", ar.result().size());
                collectMetrics(DocumentScope.GLOBAL, Collections.singletonList("global")).compose(v -> {
                    piveauLog.info("Global refreshed ({}), start refreshing catalogues", System.currentTimeMillis() - start);
                    return collectMetrics(DocumentScope.CATALOGUE, ar.result());
                }).compose(v -> {
                    piveauLog.info("Catalogues refreshed ({}), start refreshing countries", System.currentTimeMillis() - start);
                    return collectMetrics(DocumentScope.COUNTRY, new ArrayList<>(SparqlHelper.getCountries().keySet()));
                }).onComplete(v -> {
                    piveauLog.info("Cache refresh finished ({} ms)", System.currentTimeMillis() - start);
                    refreshRunning = false;
                });
            } else {
                piveauLog.error("Get catalogue list", ar.cause());
                refreshRunning = false;
            }
        });
    }

    @Override
    public void refreshSingleMetrics(String id) {
        collectMetricsForID(DocumentScope.CATALOGUE, id)
                .onSuccess(v -> {
                })
                .onFailure(cause -> {
                });
    }

    @Override
    public void moveScore() {
        dbClient.find(DocumentScope.SCORE.name(), new JsonObject(), handler -> {
            if (handler.succeeded()) {
                handler.result().forEach(doc -> {
                    String id = doc.getString("_id", "");
                    JsonArray scores = doc.getJsonArray("scores");
                    JsonArray newScores = new JsonArray();
                    //old score objects are in format {score: x, date:y}, we need {value:x, date:y}
                    scores.stream().iterator().forEachRemaining(sc ->
                            newScores.add(
                                    new JsonObject()
                                            .put("value", ((JsonObject) sc).getDouble("score"))
                                            .put("date", ((JsonObject) sc).getString("date"))));
                    //the update instructions, we want to add the score to an existing array, so we need to use $push,
                    //we have multiple values, so we need to use $each inside of the $push command
                    JsonObject update = new JsonObject().put("$push", new JsonObject().put("score", new JsonObject().put("$each", newScores)));

                    //this if is only there to see, which document scope we have to use, everything else stays the same
                    DocumentScope scope;
                    if (id.equals("global")) {
                        scope = DocumentScope.GLOBAL;
                    } else if (getCountries().containsKey(id)) {
                        scope = DocumentScope.COUNTRY;
                    } else {
                        scope = DocumentScope.CATALOGUE;
                    }
                    updateDocumentInDB(scope, id, update)
                            .onSuccess(result -> log.info("Score moved to new doc: {}", result.getDocUpsertedId()))
                            .onFailure(cause -> log.warn("Error while moving score", cause));
                });
            }
        });
    }

    /**
     * Wrapper for mongodb update collection for a single document
     *
     * @param documentScope the collection that document is stored in
     * @param id            the id of the document
     * @param update        the update object with instructions on how to update that document
     */
    private Future<MongoClientUpdateResult> updateDocumentInDB(DocumentScope documentScope, String id, JsonObject update) {
        JsonObject search = new JsonObject()
                .put("_id", id);

        return dbClient.updateCollectionWithOptions(
                documentScope.name(),
                search,
                update,
                new UpdateOptions().setUpsert(true));
    }

    @Override
    public void clearMetrics() {
        piveauLog.info("Start clearing metrics");
        for (DocumentScope metric : DocumentScope.values()) {
            dbClient.dropCollection(metric.name(), ar -> {
                if (ar.succeeded()) {
                    piveauLog.info("All metrics dropped");
                } else {
                    piveauLog.error("Dropping metrics failed", ar.cause());
                }
            });
        }
    }

    @Override
    public void getDistributionReachabilityDetails(String catalogueId, int offset, int limit, String lang, Handler<AsyncResult<JsonObject>> resultHandler) {
        Promise<JsonObject> statusCodes = Promise.promise();
        dqvProvider.getDistributionReachabilityDetails(catalogueId, offset, limit, lang, statusCodes);
        statusCodes.future().onComplete(retrieveStatusCodesList -> {
            if (retrieveStatusCodesList.succeeded()) {
                JsonObject document = new JsonObject().put("success", "true");
                document.put("result", retrieveStatusCodesList.result());
                resultHandler.handle(Future.succeededFuture(document));
            } else {
                resultHandler.handle(Future.failedFuture(retrieveStatusCodesList.cause()));
            }
        });
    }

    private Future<Void> collectMetrics(DocumentScope scope, List<String> collection) {
        Promise<Void> promise = Promise.promise();
        Iterator<String> iterator = collection.iterator();
        collectIterative(iterator, scope, promise);
        return promise.future();
    }

    private void collectIterative(Iterator<String> iterator, DocumentScope scope, Promise<Void> finishedPromise) {
        if (iterator.hasNext()) {
            String next = iterator.next();
            collectMetricsForID(scope, next)
                    .onSuccess(v -> collectIterative(iterator, scope, finishedPromise))
                    .onFailure(finishedPromise::fail);
        } else {
            finishedPromise.complete();
        }
    }

    private Future<Void> collectMetricsForID(DocumentScope scope, String documentId) {
        Promise<Void> promise = Promise.promise();
        JsonObject update = new JsonObject();
        JsonObject pushObject = new JsonObject();
        update.put("$push", pushObject);
        addFindability(pushObject, scope, documentId).compose(v ->
                addAccessibility(pushObject, scope, documentId)
        ).compose(v ->
                addInteroperability(pushObject, scope, documentId)
        ).compose(v ->
                addReusability(pushObject, scope, documentId)
        ).compose(result ->
                addContextuality(pushObject, scope, documentId)
        ).compose(v ->
                Future.<Double>future(pr -> dqvProvider.getAverageScore(documentId, scope, PV.scoring.getURI(), pr))
        ).compose(result -> {
            pushObject.put("score", new JsonObject()
                    .put("date", dateFormat.format(new Date())).put("value", result)
            );
            // saveScoreToTimeline(documentId, result);

            return Future.<Void>future(pr -> {
                if (scope == DocumentScope.CATALOGUE) {
                    Promise<JsonObject> infoPromise = Promise.promise();
                    dqvProvider.getCatalogueInfo(documentId, infoPromise);
                    infoPromise.future().onSuccess(info -> {
                        update.put("$set",
                                new JsonObject().put("info", info));

                        pr.complete();
                    }).onFailure(pr::fail);
                } else {
                    pr.complete();
                }
            });
        }).onSuccess(v ->
                updateDocumentInDB(scope, documentId, update)
                        .onSuccess(result -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Updated metrics for {}:\n{}", documentId, update.encodePrettily());
                            }
                            log.info("Saved metrics for document {}", documentId);
                            promise.complete();
                        })
                        .onFailure(cause -> {
                            log.error("Failed to save metrics for document ID {}", documentId, cause);
                            promise.fail(cause);
                        })
        ).onFailure(cause -> {
            log.error("Collecting metrics for {} failed", documentId, cause);
            promise.complete();
        });

        return promise.future();
    }

    private Future<Void> addFindability(JsonObject document, DocumentScope documentScope, String catalogueId) {
        Promise<Void> promise = Promise.promise();

        Promise<Double> dimensionScore = Promise.promise();
        dqvProvider.getAverageScore(catalogueId, documentScope, PV.findabilityScoring.getURI(), dimensionScore);
        dimensionScore.future().compose(score -> {
            document.put("findability.score", new JsonObject()
                    .put("date", dateFormat.format(new Date()))
                    .put("value", new JsonObject().put("points", score).put("percentage", getScorePercentage(score, Dimension.FINDABILITY))));
            return Future.<Double>future(pr -> dqvProvider.getKeywordAvailability(catalogueId, documentScope, pr));
        }).compose(availability -> {
            if (availability != -1.0) {
                document.put("findability.keywordAvailability", getPercentageWithDate(availability));
            }
            return Future.<Double>future(pr -> dqvProvider.getCategoryAvailability(catalogueId, documentScope, pr));
        }).compose(availability -> {
            if (availability != -1.0) {
                document.put("findability.categoryAvailability", getPercentageWithDate(availability));
            }
            return Future.<Double>future(pr -> dqvProvider.getSpatialAvailability(catalogueId, documentScope, pr));
        }).compose(availability -> {
            if (availability != -1.0) {
                document.put("findability.spatialAvailability", getPercentageWithDate(availability));
            }
            return Future.<Double>future(pr -> dqvProvider.getTemporalAvailability(catalogueId, documentScope, pr));
        }).onSuccess(availability -> {
            if (availability != -1.0) {
                document.put("findability.temporalAvailability", getPercentageWithDate(availability));
            }
            promise.complete();
        }).onFailure(promise::fail);

        return promise.future();
    }

    private Future<Void> addAccessibility(JsonObject document, DocumentScope documentScope, String catalogueId) {
        Promise<Void> promise = Promise.promise();

        Promise<Double> dimensionScore = Promise.promise();
        dqvProvider.getAverageScore(catalogueId, documentScope, PV.accessibilityScoring.getURI(), dimensionScore);
        dimensionScore.future().compose(score -> {
            document.put("accessibility.score", new JsonObject()
                    .put("date", dateFormat.format(new Date()))
                    .put("value", new JsonObject().put("points", score).put("percentage", getScorePercentage(score, Dimension.ACCESSIBILITY))));
            return Future.<StatusCodes>future(pr -> dqvProvider.getAccessUrlStatusCodes(catalogueId, documentScope, pr));
        }).compose(statusCodes -> {
            JsonArray accessUrlStatusCodeArray = new JsonArray();
            statusCodes.getStatusCodes().forEach((code, percentage) ->
                    accessUrlStatusCodeArray.add(new JsonObject().put("name", code).put("percentage", percentage)));
            document.put("accessibility.accessUrlStatusCodes", wrapWithDate(accessUrlStatusCodeArray));

            return Future.<StatusCodes>future(pr -> dqvProvider.getDownloadUrlStatusCodes(catalogueId, documentScope, pr));
        }).compose(statusCodes -> {
            JsonArray downloadUrlStatusCodeArray = new JsonArray();
            statusCodes.getStatusCodes().forEach((code, percentage) ->
                    downloadUrlStatusCodeArray.add(new JsonObject().put("name", code).put("percentage", percentage)));
            document.put("accessibility.downloadUrlStatusCodes", wrapWithDate(downloadUrlStatusCodeArray));

            return Future.<Double>future(pr -> dqvProvider.getDownloadUrlAvailability(catalogueId, documentScope, pr));
        }).onSuccess(availability -> {
            if (availability != -1.0) {
                document.put("accessibility.downloadUrlAvailability", getPercentageWithDate(availability));
            }
            promise.complete();
        }).onFailure(promise::fail);

        return promise.future();
    }

    private Future<Void> addInteroperability(JsonObject document, DocumentScope documentScope, String catalogueId) {
        Promise<Void> promise = Promise.promise();

        Promise<Double> dimensionScore = Promise.promise();
        dqvProvider.getAverageScore(catalogueId, documentScope, PV.interoperabilityScoring.getURI(), dimensionScore);
        dimensionScore.future().compose(score -> {
            document.put("interoperability.score", new JsonObject()
                    .put("date", dateFormat.format(new Date()))
                    .put("value", new JsonObject().put("points", score).put("percentage", getScorePercentage(score, Dimension.INTEROPERABILITY))));
            return Future.<Double>future(pr -> dqvProvider.getFormatAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("interoperability.formatAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getMediaTypeAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("interoperability.mediaTypeAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getFormatMediaTypeAlignment(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("interoperability.formatMediaTypeAlignment", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getFormatMediaTypeNonProprietary(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("interoperability.formatMediaTypeNonProprietary", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getFormatMediaTypeMachineReadable(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("interoperability.formatMediaTypeMachineReadable", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getDcatApCompliance(catalogueId, documentScope, pr));
        }).onSuccess(result -> {
            if (result != -1.0) {
                document.put("interoperability.dcatApCompliance", getPercentageWithDate(result));
            }
            promise.complete();
        }).onFailure(promise::fail);

        return promise.future();
    }

    private Future<Void> addReusability(JsonObject document, DocumentScope documentScope, String catalogueId) {
        Promise<Void> promise = Promise.promise();

        Promise<Double> dimensionScore = Promise.promise();
        dqvProvider.getAverageScore(catalogueId, documentScope, PV.reusabilityScoring.getURI(), dimensionScore);
        dimensionScore.future().compose(score -> {
            document.put("reusability.score", new JsonObject()
                    .put("date", dateFormat.format(new Date()))
                    .put("value", new JsonObject().put("points", score).put("percentage", getScorePercentage(score, Dimension.REUSABILIY))));
            return Future.<Double>future(pr -> dqvProvider.getLicenceAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("reusability.licenceAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getLicenceAlignment(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("reusability.licenceAlignment", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getAccessRightsAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("reusability.accessRightsAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getAccessRightsAlignment(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("reusability.accessRightsAlignment", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getContactPointAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("reusability.contactPointAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getPublisherAvailability(catalogueId, documentScope, pr));
        }).onSuccess(result -> {
            if (result != -1.0) {
                document.put("reusability.publisherAvailability", getPercentageWithDate(result));
            }
            promise.complete();
        }).onFailure(promise::fail);

        return promise.future();
    }

    private Future<Void> addContextuality(JsonObject document, DocumentScope documentScope, String catalogueId) {
        Promise<Void> promise = Promise.promise();

        Promise<Double> dimensionScore = Promise.promise();
        dqvProvider.getAverageScore(catalogueId, documentScope, PV.contextualityScoring.getURI(), dimensionScore);
        dimensionScore.future().compose(score -> {
            document.put("contextuality.score", new JsonObject()
                    .put("date", dateFormat.format(new Date()))
                    .put("value", new JsonObject().put("points", score).put("percentage", getScorePercentage(score, Dimension.CONTEXTUALITY))));
            return Future.<Double>future(pr -> dqvProvider.getRightsAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("contextuality.rightsAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getByteSizeAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("contextuality.byteSizeAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getDateIssuedAvailability(catalogueId, documentScope, pr));
        }).compose(result -> {
            if (result != -1.0) {
                document.put("contextuality.dateIssuedAvailability", getPercentageWithDate(result));
            }
            return Future.<Double>future(pr -> dqvProvider.getDateModifiedAvailability(catalogueId, documentScope, pr));
        }).onSuccess(result -> {
            if (result != -1.0) {
                document.put("contextuality.dateModifiedAvailability", getPercentageWithDate(result));
            }
            promise.complete();
        }).onFailure(promise::fail);

        return promise.future();
    }

    private JsonObject getPercentageWithDate(Double yesPercentage) {
        return wrapWithDate(PercentageMath.getYesNoPercentage(yesPercentage));
    }

    private JsonObject wrapWithDate(JsonArray jsonArray) {
        return new JsonObject()
                .put("date", dateFormat.format(new Date()))
                .put("value", jsonArray);
    }

    private Double getScorePercentage(Double score, Dimension dimension) {
        return (double) Math.round(score / dimension.getMaxScore() * 100);
    }

}
