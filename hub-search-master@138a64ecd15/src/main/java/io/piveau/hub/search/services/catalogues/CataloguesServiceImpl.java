package io.piveau.hub.search.services.catalogues;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.services.vocabulary.VocabularyService;
import io.piveau.hub.search.util.geo.SpatialChecker;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch.SearchResponseHelper;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.pointer.JsonPointer;
import io.vertx.serviceproxy.ServiceException;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CataloguesServiceImpl implements CataloguesService {

    private final SearchClient searchClient;

    private final VocabularyService vocabularyService;

    private final Cache<String, JsonObject> cache;

    private final PiveauContext serviceContext;

    CataloguesServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager,
                          Handler<AsyncResult<CataloguesService>> handler) {
        this.searchClient = SearchClient.build(vertx, config, indexManager);

        this.vocabularyService = VocabularyService.createProxy(vertx, VocabularyService.SERVICE_ADDRESS);

        this.serviceContext = new PiveauContext("hub.search", "CataloguesService");

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("catalogueData",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, JsonObject.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(200, EntryUnit.ENTRIES))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1))))
                .build(true);

        cache = cacheManager.getCache("catalogueData", String.class, JsonObject.class);

        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<JsonArray> listCatalogues(String alias) {
        Promise<JsonArray> promise = Promise.promise();

        String defaultAlias = alias == null ? Constants.getReadAlias("catalogue") : alias;

        JsonObject q = new JsonObject();
        q.put("size", 10000);
        q.put("filter", "catalogue");
        q.put("aggregation", false);
        q.put("includes", new JsonArray().add("id"));
        q.put("scroll", true);
        q.put("alias", defaultAlias);

        Query query = Json.decodeValue(q.toString(), Query.class);

        searchClient.listIds(query, false, true).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> createCatalogue(JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend("id-not-available");
        SpatialChecker.check(payload);

        vocabularyService.replaceVocabularyInPayload(payload).onComplete(replaceVocabularyInPayloadResult ->
                searchClient.postDocument("catalogue", false, payload).onSuccess(result -> {
                    resourceContext.log().debug("Post success: " + payload);
                    promise.complete(ReturnHelper.returnSuccess(201, new JsonObject().put("id", result)));
                }).onFailure(promise::fail)
        );
        return promise.future();
    }

    @Override
    public Future<JsonObject> createOrUpdateCatalogue(String catalogueId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(catalogueId);
        SpatialChecker.check(payload);
        vocabularyService.replaceVocabularyInPayload(payload).onComplete(replaceVocabularyInPayloadResult ->
                searchClient.putDocument("catalogue", catalogueId, false, payload).onSuccess(result -> {
                    resourceContext.log().debug("Put success: " + payload);
                    if (result == 200) {
                        // updated
                        resourceContext.log().info("Update catalogue: Catalogue {} updated.", catalogueId);
                        updateByQuery(catalogueId, payload, true);
                    } else {
                        // created
                        resourceContext.log().info("Create catalogue: Catalogue {} created.", catalogueId);
                    }
                    promise.complete(ReturnHelper.returnSuccess(result, new JsonObject().put("id", catalogueId)));
                }).onFailure(failure -> {
                    resourceContext.log().error("Put failed: " + failure.getMessage());
                    promise.fail(failure);
                }));
        return promise.future();
    }

    @Override
    public Future<JsonObject> modifyCatalogue(String catalogueId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(catalogueId);
        resourceContext.log().debug("PATCH - Payload: {}", payload);
        SpatialChecker.check(payload);
        vocabularyService.replaceVocabularyInPayload(payload).onComplete(replaceVocabularyInPayloadResult ->
                searchClient.patchDocument("catalogue", catalogueId, false, payload).onSuccess(result -> {
                    resourceContext.log().debug("Patch success: " + payload);
                    resourceContext.log().info("Modify catalogue: Catalogue {} modified.", catalogueId);
                    updateByQuery(catalogueId, payload, false);
                    promise.complete(ReturnHelper.returnSuccess(200, new JsonObject().put("id", catalogueId)));
                }).onFailure(failure -> {
                    resourceContext.log().error("Patch failed: " + failure.getMessage());
                    promise.fail(failure);
                }));
        return promise.future();
    }

    @Override
    public Future<JsonObject> readCatalogue(String catalogueId) {
        Promise<JsonObject> promise = Promise.promise();
        readCatalogue(catalogueId, false, false).onSuccess(result -> {
            searchClient.countDocuments("dataset", "catalog.id.raw", catalogueId).onComplete(countDatasetsResult -> {
                if (countDatasetsResult.succeeded()) {
                    result.getJsonObject("result").put("count", countDatasetsResult.result());
                } else {
                    result.getJsonObject("result").putNull("count");
                }
                promise.complete(result);
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<Void> deleteCatalogue(String catalogueId) {
        Promise<Void> promise = Promise.promise();
        searchClient.deleteDocument("catalogue", catalogueId, false).onSuccess(result -> {
            searchClient.deleteByQuery("dataset", "catalog.id.raw", catalogueId);
            promise.complete();
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonArray> replaceCatalogueInItems(JsonArray items) {
        Promise<JsonArray> promise = Promise.promise();
        if (items != null && !items.isEmpty()) {
            List<Future<Void>> futureList = new ArrayList<>();
            for (Object obj : items) {
                Promise<Void> readCataloguePromise = Promise.promise();
                futureList.add(readCataloguePromise.future());
                JsonObject catalogue = (JsonObject) obj;
                if (catalogue != null && !catalogue.isEmpty()) {
                    String catalogueId = catalogue.getString("id");
                    readCatalogue(catalogueId, false, true).onComplete(ar -> {
                        if (ar.succeeded()) {
                            JsonObject readCatalogue = ar.result().getJsonObject("result");
                            catalogue.put("title", SearchResponseHelper.getCatalogTitle(readCatalogue));
                        }
                        readCataloguePromise.complete();
                    });
                } else {
                    readCataloguePromise.complete();
                }
            }
            Future.all(futureList).onComplete(ar -> promise.complete(items));
        } else {
            promise.complete();
        }

        return promise.future();
    }

    @Override
    public Future<JsonArray> replaceCatalogueInResultList(JsonArray payload, List<String> includes) {
        Promise<JsonArray> promise = Promise.promise();
        JsonArray results = new JsonArray();
        if (payload != null && !payload.isEmpty()) {
            List<Future<Void>> futureList = new ArrayList<>();
            for (Object obj : payload) {
                Promise<Void> readCataloguePromise = Promise.promise();
                futureList.add(readCataloguePromise.future());
                JsonObject dataset = (JsonObject) obj;
                replaceCatalogueInResponse(dataset, includes)
                        .onSuccess(result -> {
                            results.add(result);
                            readCataloguePromise.complete();
                        })
                        .onFailure(readCataloguePromise::fail);
            }
            Future.all(futureList).onSuccess(ar -> promise.complete(results)).onFailure(promise::fail);
        } else {
            promise.complete(results);
        }

        return promise.future();
    }

    @Override
    public Future<JsonObject> replaceCatalogueInResponse(JsonObject dataset, List<String> includes) {
        Promise<JsonObject> promise = Promise.promise();
        try {
            JsonObject catalog = dataset.getJsonObject("catalog");
            if (catalog != null && !catalog.isEmpty()) {
                String catalogueId = dataset.getJsonObject("catalog").getString("id");
                readCatalogue(catalogueId, false, true).onComplete(ar -> {
                    if (ar.succeeded()) {
                        JsonObject readCatalog = ar.result().getJsonObject("result");
                        if (includes != null && !includes.isEmpty()) {
                            JsonObject filteredCatalog = new JsonObject();
                            for (String include : includes) {
                                String jsonPath = '/' + include.replace('.', '/');
                                JsonPointer jsonPointer = JsonPointer.from(jsonPath);
                                Object result = jsonPointer.queryJson(readCatalog);
                                if (result != null) {
                                    jsonPointer.writeJson(filteredCatalog, result, true);
                                }
                            }
                            readCatalog = filteredCatalog;
                        }
                        dataset.put("catalog", readCatalog);
                        promise.complete(dataset);
                    } else {
                        if (ar.cause() instanceof ServiceException se) {
                            promise.fail(new ServiceException(se.failureCode(), se.getMessage()));
                        } else {
                            promise.fail(new ServiceException(500, ar.cause().getMessage()));
                        }
                    }
                });
            } else {
                promise.fail(new ServiceException(400, "Catalog missing"));
            }
        } catch (ClassCastException e) {
            promise.fail(new ServiceException(500, "Catalog not json"));
        }

        return promise.future();
    }

    @Override
    public Future<JsonArray> checkCatalogueInPayloadArray(JsonArray payload) {
        List<Future<Void>> futures = new ArrayList<>();
        payload.stream()
                .map(JsonObject.class::cast)
                .forEach(obj -> futures.add(checkCatalogueInPayloadObject(obj).mapEmpty()));

        return Future.all(futures).map(cf -> payload);
    }

    @Override
    public Future<JsonObject> checkCatalogueInPayloadObject(JsonObject payload) {
        JsonObject catalog = payload.getJsonObject("catalog");
        if (catalog != null && !catalog.isEmpty()) {
            String catalogueId = payload.getJsonObject("catalog").getString("id");

            return readCatalogue(catalogueId, true, true)
                    .map(result -> {
                        JsonObject readCatalog = result.getJsonObject("result");
                        return payload.put("country", readCatalog.getJsonObject("country"));
                    });
        } else {
            return Future.failedFuture(new ServiceException(400, "Catalog missing"));
        }
    }

    private Future<JsonObject> readCatalogue(String catalogueId, boolean useWriteAlias, boolean useCache) {
        if (useCache && cache.containsKey(catalogueId)) {
            return Future.succeededFuture(ReturnHelper.returnSuccess(200, cache.get(catalogueId)));
        } else {
            return searchClient.getDocument("catalogue", useWriteAlias, catalogueId, false)
                    .map(result -> {
                        cache.put(catalogueId, result);
                        return ReturnHelper.returnSuccess(200, result);
                    });
        }
    }

    private void updateByQuery(String catalogueId, JsonObject payload, boolean replaceAll) {
        List<String> globalReplacements = new ArrayList<>();
        globalReplacements.add("country");

        List<String> fieldReplacements = new ArrayList<>();
        fieldReplacements.add("title");
        fieldReplacements.add("description");
        fieldReplacements.add("country");

        searchClient.updateByQuery("dataset", "catalog.id.raw", catalogueId, "catalog",
                globalReplacements, fieldReplacements, payload, replaceAll);
    }

}
