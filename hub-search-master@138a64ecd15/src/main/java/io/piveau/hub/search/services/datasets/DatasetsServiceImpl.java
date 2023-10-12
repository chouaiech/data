package io.piveau.hub.search.services.datasets;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.services.catalogues.CataloguesService;
import io.piveau.hub.search.services.search.SearchService;
import io.piveau.hub.search.services.vocabulary.VocabularyService;
import io.piveau.hub.search.util.date.DateChecker;
import io.piveau.hub.search.util.geo.SpatialChecker;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DatasetsServiceImpl implements DatasetsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Vertx vertx;

    private final SearchClient searchClient;

    private final SearchService searchService;
    private final CataloguesService cataloguesService;
    private final VocabularyService vocabularyService;

    private final PiveauContext serviceContext;

    DatasetsServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager,
                        Handler<AsyncResult<DatasetsService>> handler) {
        this.vertx = vertx;
        this.searchClient = SearchClient.build(vertx, config, indexManager);

        this.searchService = SearchService.createProxy(vertx, SearchService.SERVICE_ADDRESS);
        this.cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS);
        this.vocabularyService = VocabularyService.createProxy(vertx, VocabularyService.SERVICE_ADDRESS);

        this.serviceContext = new PiveauContext("hub.search", "DatasetsService");

        handler.handle(Future.succeededFuture(this));
    }

    private String getResourceContext(String datasetId, JsonObject payload) {
        String context = datasetId != null ? datasetId : "id-not-available";
        JsonObject catalog = payload.getJsonObject("catalog");
        if (catalog != null) {
            String catalogId = catalog.getString("id");
            if (catalogId != null && !catalogId.isEmpty()) {
                context += "; catalog: " + catalogId;
            }
        }
        return context;
    }

    @Override
    public Future<JsonArray> listDatasets(String catalogueId, String alias) {
        Promise<JsonArray> promise = Promise.promise();

        String defaultAlias = alias == null ? Constants.getReadAlias("dataset") : alias;

        JsonObject q = new JsonObject();
        q.put("size", 10000);
        q.put("filter", "dataset");
        q.put("aggregation", false);
        q.put("includes", new JsonArray().add("id"));
        q.put("scroll", true);
        q.put("alias", defaultAlias);

        q.put("facets", new JsonObject().put("catalog.id.raw", new JsonArray().add(catalogueId)));

        Query query = Json.decodeValue(q.toString(), Query.class);

        searchClient.listIds(query, false, true).onSuccess(promise::complete).onFailure(promise::fail);

        return promise.future();
    }

    @Override
    public Future<JsonObject> createDataset(JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();

        PiveauContext resourceContext = serviceContext.extend(getResourceContext(null, payload));
        DateChecker.check(payload);
        SpatialChecker.check(payload);

        cataloguesService.checkCatalogueInPayloadObject(payload)
                .compose(vocabularyService::replaceVocabularyInPayload)
                .onSuccess(replaceVocabularyResult ->
                        searchClient.postDocument("dataset", true, replaceVocabularyResult).onSuccess(result -> {
                            resourceContext.log().debug("Post success: " + result);
                            promise.complete(ReturnHelper.returnSuccess(201, new JsonObject().put("id", result)));
                        }).onFailure(promise::fail)
                ).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> createOrUpdateDataset(String datasetId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(getResourceContext(datasetId, payload));
        DateChecker.check(payload);
        SpatialChecker.check(payload);

        cataloguesService.checkCatalogueInPayloadObject(payload)
                .compose(vocabularyService::replaceVocabularyInPayload)
                .onSuccess(replaceVocabularyResult -> {
                    Promise<Void> getOldDatasetPromise = Promise.promise();
                    searchClient.getDocument("dataset", datasetId, true).onSuccess(result -> {
                        if (replaceVocabularyResult.getJsonObject("quality_meas") == null ||
                                replaceVocabularyResult.getJsonObject("quality_meas").isEmpty()) {
                            replaceVocabularyResult.put("quality_meas", result.getJsonObject("quality_meas"));
                        }
                        createDatasetRevision(datasetId, result).onComplete(a -> {
                            if (!a.succeeded()) {
                                resourceContext.log().error("Write dataset revision: {}", a.cause().getMessage());
                            }
                            getOldDatasetPromise.complete();
                        });
                    }).onFailure(failure -> {
                        if (failure.getMessage().equals("not found")) {
                            getOldDatasetPromise.complete();
                        } else {
                            getOldDatasetPromise.fail(failure);
                        }
                    });
                    getOldDatasetPromise.future().onComplete(getOldDatasetResult -> {
                        searchClient.putDocument("dataset", datasetId, true, replaceVocabularyResult)
                                .onSuccess(result -> {
                                    resourceContext.log().debug("Put success: " + payload);
                                    if (result == 200) {
                                        // updated
                                        resourceContext.log().info("Update dataset: Dataset {} updated.", datasetId);
                                    } else {
                                        // created
                                        resourceContext.log().info("Create dataset: Dataset {} created.", datasetId);
                                    }
                                    promise.complete(
                                            ReturnHelper.returnSuccess(result, new JsonObject().put("id", datasetId)));
                                }).onFailure(failure -> {
                                    resourceContext.log().error("Put failed: " +
                                            failure.getMessage());
                                    promise.fail(failure);
                                });
                    }).onFailure(promise::fail);
                }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> modifyDataset(String datasetId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(getResourceContext(datasetId, payload));
        DateChecker.check(payload);
        SpatialChecker.check(payload);

        Future.<JsonObject>future(p -> {
                    if (payload.containsKey("catalog")) {
                        cataloguesService.checkCatalogueInPayloadObject(payload).onComplete(p);
                    } else {
                        promise.complete(payload);
                    }
                })
                .compose(vocabularyService::replaceVocabularyInPayload)
                .onSuccess(replaceVocabularyResult -> {
                    Promise<Void> getOldDatasetPromise = Promise.promise();
                    searchClient.getDocument("dataset", datasetId, true).onSuccess(result -> {
                        createDatasetRevision(datasetId, result).onComplete(a -> {
                            if (!a.succeeded()) {
                                resourceContext.log().error("Write dataset revision: {}",
                                        a.cause().getMessage());
                            }
                            getOldDatasetPromise.complete();
                        });
                    }).onFailure(failure -> {
                        if (failure.getMessage().equals("not found")) {
                            getOldDatasetPromise.complete();
                        } else {
                            getOldDatasetPromise.fail(failure);
                        }
                    });
                    getOldDatasetPromise.future().onComplete(getOldDatasetResult -> {
                        searchClient.patchDocument("dataset", datasetId, true, replaceVocabularyResult).onSuccess(
                                result -> {
                                    resourceContext.log().debug("Patch success: " + replaceVocabularyResult);
                                    promise.complete(ReturnHelper
                                            .returnSuccess(200, new JsonObject().put("id", datasetId)));
                                }).onFailure(failure -> {
                            resourceContext.log().error("Patch failed: " + failure.getMessage());
                            promise.fail(failure);
                        });
                    }).onFailure(promise::fail);
                }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> readDataset(String datasetId) {
        Promise<JsonObject> promise = Promise.promise();
        searchClient.getDocument("dataset", datasetId, true).onSuccess(result ->
                cataloguesService.replaceCatalogueInResponse(result, Collections.emptyList())
                        .onSuccess(replaceCatalogResult ->
                                promise.complete(ReturnHelper.returnSuccess(200, replaceCatalogResult)))
                        .onFailure(promise::fail)
        ).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<Void> deleteDataset(String datasetId) {
        return searchClient.deleteDocument("dataset", datasetId, true);
    }

    @Override
    public Future<JsonArray> createOrUpdateDatasetBulk(JsonArray payload) {
        PiveauContext resourceContext = serviceContext.extend("bulk");

        DateChecker.check(payload);
        SpatialChecker.check(payload);

        List<String> restoreFields = List.of("quality_meas");

        final AtomicReference<Long> start = new AtomicReference<>(System.currentTimeMillis());

        return cataloguesService.checkCatalogueInPayloadArray(payload)
                .compose(content -> {
                    log.debug("Bulk catalogue check: {} ms", System.currentTimeMillis() - start.get());
                    start.set(System.currentTimeMillis());
                    return vocabularyService.replaceVocabularyInPayloadList(content);
                })
                .compose(content -> {
                    log.debug("Bulk vocabulary replacement: {} ms", System.currentTimeMillis() - start.get());
                    start.set(System.currentTimeMillis());
                    return searchClient.putDocumentsBulk("dataset", "dataset-revisions", restoreFields, content, true);
                })
                .onSuccess(result -> {
                    log.debug("Bulk sent to elasticsearch: {} ms", System.currentTimeMillis() - start.get());
                    resourceContext.log().debug("Put bulk success: {}", result);
                })
                .onFailure(failure -> resourceContext.log().error("Put bulk failed", failure));
    }

    @Override
    public Future<JsonObject> getDatasetRSS(String datasetId) {
        Promise<JsonObject> promise = Promise.promise();

        Promise<JsonObject> getDatasetPromise = Promise.promise();
        searchClient.getDocument("dataset", datasetId, true)
                .onSuccess(getDatasetPromise::complete).onFailure(getDatasetPromise::fail);

        getDatasetPromise.future().onSuccess(datasetJson -> {
            getRevisionIndices().onComplete(indicesList -> {
                if (indicesList.succeeded()) {
                    JsonArray revisions = new JsonArray();
                    revisions.add(datasetJson);
                    List<Future<Void>> futureList = new ArrayList<>();

                    indicesList.result().forEach(index -> {
                        Promise<Void> indexPromise = Promise.promise();
                        readDatasetRevision(datasetId, index).onComplete(asyncResult -> {
                            if (asyncResult.succeeded()) {
                                revisions.add(asyncResult.result().getJsonObject("result"));
                            }
                            indexPromise.complete();
                        });
                        futureList.add(indexPromise.future());
                    });

                    Future.all(futureList)
                            .onFailure(promise::fail)
                            .onSuccess(rev -> {
                                JsonObject object = new JsonObject();
                                object.put("status", 200);
                                object.put("id", datasetJson.getValue("id"));
                                object.put("title", datasetJson.getValue("title"));
                                object.put("translation_meta", datasetJson.getJsonObject("translation_meta"));
                                object.put("result", revisions);

                                promise.complete(object);
                            });
                } else {
                    promise.fail(indicesList.cause());
                }
            });
        }).onFailure(promise::fail);

        return promise.future();
    }

    @Override
    public Future<JsonObject> readDatasetRevision(String datasetId, String revision) {
        Promise<JsonObject> promise = Promise.promise();
        searchClient.getDocument("dataset", revision, datasetId, true).onSuccess(result -> {
            result.put("revision", revision);
            cataloguesService.replaceCatalogueInResponse(result, Collections.emptyList())
                    .onSuccess(replaceCatalogResult ->
                            promise.complete(ReturnHelper.returnSuccess(200, replaceCatalogResult)))
                    .onFailure(promise::fail);
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> triggerSyncScores() {
        Promise<String> promise = Promise.promise();

        PiveauContext resourceContext = serviceContext.extend("dataset");

        JsonArray includes = new JsonArray();
        includes.add("id");
        includes.add("quality_meas");

        JsonObject searchParams = new JsonObject();
        searchParams.put("minScoring", 0);

        int size = 500;

        JsonObject q = new JsonObject();
        q.put("size", size);
        q.put("filter", "dataset");
        q.put("aggregation", false);
        q.put("includes", includes);
        q.put("scroll", true);
        q.put("searchParams", searchParams);

        AtomicInteger atomicInteger = new AtomicInteger();
        searchService.search(q.toString()).onComplete(searchQueryResult -> {
            if (searchQueryResult.succeeded()) {
                JsonObject searchResultJson = searchQueryResult.result().getJsonObject("result");
                int count = searchResultJson.getInteger("count");
                String scrollId = searchResultJson.getString("scrollId");
                JsonArray searchResult = searchResultJson.getJsonArray("results");
                modifyScores(resourceContext, searchResult).onComplete(modifyScoresResult -> {
                    if (modifyScoresResult.succeeded()) {
                        for (int i = 1; i < (count / size) + 1; i++) {
                            vertx.setTimer(i * 1000L, ar ->
                                    searchService.scroll(scrollId).onComplete(scrollQueryResult -> {
                                        if (scrollQueryResult.succeeded()) {
                                            JsonObject scrollResultJson =
                                                    scrollQueryResult.result().getJsonObject("result");
                                            JsonArray scrollResult = scrollResultJson.getJsonArray("results");
                                            modifyScores(resourceContext, scrollResult).onComplete(modifyScores2Result -> {
                                                if (modifyScores2Result.failed()) {
                                                    resourceContext.log().error("Sync score failed: "
                                                            + modifyScoresResult.cause().getMessage());
                                                }
                                                resourceContext.log().info("SyncScore processed: " +
                                                        atomicInteger.incrementAndGet() + "; Size: " + scrollResult.size());
                                            });
                                        } else {
                                            resourceContext.log().error("Sync score failed: "
                                                    + modifyScoresResult.cause().getMessage());
                                        }
                                    })
                            );
                        }
                    } else {
                        resourceContext.log().error("Sync score failed: " + modifyScoresResult.cause().getMessage());
                    }
                    resourceContext.log().info("SyncScore processed: "
                            + atomicInteger.incrementAndGet() + "; Size: " + searchResult.size());
                });
            } else {
                resourceContext.log().error("Sync score failed: " + searchQueryResult.cause().getMessage());
            }
        });
        promise.complete("Triggered sync scores successfully");
        return promise.future();
    }

    private Future<JsonObject> createDatasetRevision(String datasetId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(getResourceContext(datasetId, payload));
        searchClient.putDocument("dataset-revisions", datasetId, true, payload).onSuccess(result -> {
            if (result == 200) {
                // updated
                resourceContext.log().info("Update dataset revision: Dataset {} updated.", datasetId);
            } else {
                // created
                resourceContext.log().info("Create dataset revision: Dataset {} created.", datasetId);
            }
            promise.complete(ReturnHelper.returnSuccess(result, new JsonObject().put("id", datasetId)));
        }).onFailure(promise::fail);
        return promise.future();
    }

    private Future<Void> modifyScores(PiveauContext resourceContext, JsonArray searchResult) {
        Promise<Void> promise = Promise.promise();

        List<Future<Void>> futureList = new ArrayList<>();
        for (Object obj : searchResult) {
            Promise<Void> searchResultPromise = Promise.promise();
            futureList.add(searchResultPromise.future());

            JsonObject dataset = (JsonObject) obj;
            String id = dataset.getString("id");
            JsonObject qualityMeas = dataset.getJsonObject("quality_meas");

            if (qualityMeas != null && !qualityMeas.isEmpty()) {
                modifyDataset(id, new JsonObject().put("quality_meas", qualityMeas)).onComplete(modifyDatasetResult -> {
                    if (modifyDatasetResult.failed()) {
                        resourceContext.extend(id).log().error("Patch score failed: "
                                + modifyDatasetResult.cause().getMessage());
                    }
                    searchResultPromise.complete();
                });
            } else {
                searchResultPromise.complete();
            }
        }
        Future.all(futureList).onComplete(ar -> promise.complete());
        return promise.future();
    }

    private Future<Set<String>> getRevisionIndices() {
        return searchClient.getAliases("dataset-revisions_");
    }

}
