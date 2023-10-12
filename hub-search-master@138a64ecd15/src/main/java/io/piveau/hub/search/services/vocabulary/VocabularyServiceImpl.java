package io.piveau.hub.search.services.vocabulary;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.services.search.SearchService;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.utils.PiveauContext;
import io.piveau.vocabularies.VocabularyHelperKt;
import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class VocabularyServiceImpl implements VocabularyService {

    private final IndexManager indexManager;

    private final SearchClient searchClient;

    private final SearchService searchService;

    // cache for caching
    private final Cache<String, JsonObject> cache;

    private final PiveauContext serviceContext;

    VocabularyServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager,
                          Handler<AsyncResult<VocabularyService>> handler) {
        this.indexManager = indexManager;

        this.searchClient = SearchClient.build(vertx, config, indexManager);

        this.searchService = SearchService.createProxy(vertx, SearchService.SERVICE_ADDRESS);

        this.serviceContext = new PiveauContext("hub.search", "VocabularyService");

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("vocabularyData",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, JsonObject.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10000, EntryUnit.ENTRIES))
                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1))))
                .build(true);

        cache = cacheManager.getCache("vocabularyData", String.class, JsonObject.class);

        handler.handle(Future.succeededFuture(this));

        // Workaround to adjust old deployments to aliases of vocabularies
        vertx.setTimer(10000, ar -> {
            serviceContext.log().info("Adding aliases to all vocabulary indices");
            readVocabularies().onSuccess(result ->
                    result.stream().map(Object::toString).forEach(value -> {
                        String index = "vocabulary_" + value;
                        searchService.setIndexAlias(null, index, Constants.getWriteAlias(index))
                                .onFailure(failure -> serviceContext.log().error(failure.getMessage()));
                        searchService.setIndexAlias(null, index, Constants.getReadAlias(index))
                                .onFailure(failure -> serviceContext.log().error(failure.getMessage()));
                    })
            ).onFailure(failure -> serviceContext.log().error(failure.getMessage()));
        });
    }

    @Override
    public Future<JsonArray> readVocabularies() {
        Promise<JsonArray> promise = Promise.promise();
        searchService.getIndices("vocabulary_*").onComplete(getIndicesResult -> {
            if (getIndicesResult.succeeded()) {
                JsonArray vocabularies = new JsonArray();
                for (Object o : getIndicesResult.result()) {
                    String vocabulary = (String) o;
                    vocabularies.add(vocabulary.replaceFirst("vocabulary_", ""));
                }
                promise.complete(vocabularies);
            } else {
                promise.fail(getIndicesResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonArray> createOrUpdateVocabulary(String vocabulary, JsonObject payload) {
        PiveauContext resourceContext = serviceContext.extend(vocabulary);
        Promise<Void> indexExistsPromise = Promise.promise();
        searchService.indexExists("vocabulary_" + vocabulary).onComplete(indexExistsResult -> {
            if (Boolean.TRUE.equals(indexExistsResult.result())) {
                indexExistsPromise.complete();
            } else {
                searchService.indexCreate("vocabulary_" + vocabulary, null).onComplete(indexCreateResult -> {
                    if (indexCreateResult.succeeded()) {
                        searchService.putMapping("vocabulary_" + vocabulary).onComplete(putMappingResult -> {
                            if (putMappingResult.succeeded()) {
                                List<Future<Void>> futureList = new ArrayList<>();

                                Promise<Void> writeAliasPromise = Promise.promise();
                                futureList.add(writeAliasPromise.future());
                                searchService.setIndexAlias(null, "vocabulary_" + vocabulary,
                                        "vocabulary_" + vocabulary + Constants.ELASTIC_WRITE_ALIAS).onComplete(
                                        setAliasResult -> {
                                            if (setAliasResult.succeeded()) {
                                                writeAliasPromise.complete();
                                            } else {
                                                resourceContext.log().error(setAliasResult.cause().getMessage());
                                                writeAliasPromise.fail(setAliasResult.cause().getMessage());
                                            }
                                        });

                                Promise<Void> readAliasPromise = Promise.promise();
                                futureList.add(readAliasPromise.future());
                                searchService.setIndexAlias(null, "vocabulary_" + vocabulary,
                                        "vocabulary_" + vocabulary + Constants.ELASTIC_READ_ALIAS).onComplete(
                                        setAliasResult -> {
                                            if (setAliasResult.succeeded()) {
                                                readAliasPromise.complete();
                                            } else {
                                                resourceContext.log().error(setAliasResult.cause().getMessage());
                                                readAliasPromise.fail(setAliasResult.cause().getMessage());
                                            }
                                        });

                                Future.all(futureList)
                                        .onSuccess(result -> indexExistsPromise.complete())
                                        .onFailure(indexExistsPromise::fail);
                            } else {
                                indexExistsPromise.fail(putMappingResult.cause());
                            }
                        });
                    } else {
                        indexExistsPromise.fail(indexCreateResult.cause());
                    }
                });
            }
        });

        Promise<JsonArray> promise = Promise.promise();
        indexExistsPromise.future().onSuccess(promiseResult -> {
            JsonArray vocab = payload.getJsonArray("vocab");

            if (vocab.isEmpty()) {
                promise.complete();
                return;
            }

            String type = "vocabulary_" + vocabulary;
            searchClient.putDocumentsBulk(type, vocab, false).onSuccess(result -> {
                List<String> types = new ArrayList<>();
                types.add("catalogue");
                types.add("dataset");
                resourceContext.log().debug("Put success: " + payload);
                promise.complete(result);
                searchClient.updateVocabularyByQuery(vocabulary, vocab, types)
                        .onFailure(failure -> resourceContext.log().error(failure.getMessage()));
            }).onFailure(failure -> {
                resourceContext.log().error("Put failed: " + failure.getMessage());
                promise.fail(failure);
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> readVocabulary(String vocabulary) {
        Promise<JsonObject> promise = Promise.promise();

        List<String> vocabularyId = new ArrayList<>();
        vocabularyId.add(vocabulary);

        Query q = new Query();
        q.setFilter("vocabulary");
        q.setVocabulary(vocabularyId);
        q.setSize(indexManager.getMaxResultWindow().get("vocabulary"));
        searchService.search(Json.encode(q)).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<Void> deleteVocabulary(String vocabulary) {
        Promise<Void> promise = Promise.promise();
        searchService.indexDelete("vocabulary_" + vocabulary).onComplete(indexDeleteResult -> {
            if (indexDeleteResult.succeeded()) {
                promise.complete();
            } else {
                promise.fail(indexDeleteResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> createVocable(String vocabulary, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(vocabulary + ":id-not-available");
        String type = "vocabulary_" + vocabulary;
        searchClient.postDocument(type, false, payload).onSuccess(result -> {
            resourceContext.log().debug("Post success: " + payload);
            resourceContext.log().info("Create vocable: Vocable {} created in {}.", result, type);
            promise.complete(ReturnHelper.returnSuccess(201, new JsonObject().put("id", result)));
        }).onFailure(failure -> {
            resourceContext.log().error("Post failed: " + failure.getMessage());
            promise.fail(failure);
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> createOrUpdateVocable(String vocabulary, String vocableId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(vocabulary + ":" + vocableId);
        String type = "vocabulary_" + vocabulary;
        searchClient.putDocument(type, vocableId, false, payload).onSuccess(result -> {
            resourceContext.log().debug("Put success: " + payload);
            if (result == 200) {
                // updated
                resourceContext.log().info("Update vocable: Vocable {} updated in {}.", vocableId, type);
            } else {
                // created
                resourceContext.log().info("Create vocable: Vocable {} created in {}.", vocableId, type);
            }
            promise.complete(ReturnHelper.returnSuccess(result, new JsonObject().put("id", vocableId)));
        }).onFailure(failure -> {
            resourceContext.log().error("Put failed: " + failure.getMessage());
            promise.fail(failure);
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> modifyVocable(String vocabulary, String vocableId, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(vocabulary + ":" + vocableId);
        String type = "vocabulary_" + vocabulary;
        searchClient.patchDocument(type, vocableId, false, payload).onSuccess(result -> {
            resourceContext.log().debug("Patch success: " + payload);
            resourceContext.log().info("Modify vocable: Vocable {} modified in {}.", vocableId, type);
            promise.complete(ReturnHelper.returnSuccess(200, new JsonObject().put("id", vocableId)));
        }).onFailure(failure -> {
            resourceContext.log().error("Patch failed: " + failure.getMessage());
            promise.fail(failure);
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> readVocable(String vocabulary, String vocableId) {
        return readVocable(vocabulary, vocableId, false);
    }

    @Override
    public Future<Void> deleteVocable(String vocabulary, String vocableId) {
        Promise<Void> promise = Promise.promise();
        String type = "vocabulary_" + vocabulary;
        searchClient.deleteDocument(type, vocableId, false)
                .onSuccess(promise::complete)
                .onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> replaceVocabularyInFacets(JsonObject facetsObject) {
        Promise<JsonObject> promise = Promise.promise();
        Map<String, JsonObject> vocabularyConfig = indexManager.getVocabulary();
        List<Future<Void>> futureList = new ArrayList<>();
        for (Map.Entry<String, JsonObject> vocabulary : vocabularyConfig.entrySet()) {
            Promise<Void> replacementPromise = Promise.promise();
            futureList.add(replacementPromise.future());
            String field = vocabulary.getValue().getString("field");
            JsonArray items = facetsObject.getJsonArray(field);
            if (!vocabulary.getKey().equals("*")) {
                replaceVocableInItems(vocabulary.getKey(), items).onComplete(ar -> replacementPromise.complete());
            } else {
                replacementPromise.complete();
            }
        }
        Future.all(futureList).onComplete(ar -> promise.complete(facetsObject));
        return promise.future();
    }

    @Override
    public Future<JsonArray> replaceVocabularyInPayloadList(JsonArray payload) {
        if (payload == null || payload.isEmpty()) {
            return Future.succeededFuture(new JsonArray());
        }
        List<Future<JsonObject>> futureList = payload.stream()
                .map(JsonObject.class::cast)
                .map(this::replaceVocabularyInPayload)
                .toList();

        return Future.all(futureList).map(ar -> payload);
    }

    @Override
    public Future<JsonObject> replaceVocabularyInPayload(JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        Map<String, JsonObject> vocabularyConfig = indexManager.getVocabulary();
        List<Future<Void>> futureList = new ArrayList<>();
        for (Map.Entry<String, JsonObject> vocabulary : vocabularyConfig.entrySet()) {
            JsonArray fields = vocabulary.getValue().getJsonArray("fields");
            for (Object field : fields) {
                Promise<Void> replacementPromise = Promise.promise();
                futureList.add(replacementPromise.future());
                replaceVocableInPayloadPreprocessing(vocabulary.getKey(), payload, field.toString(),
                        indexManager.getVocabulary().get(vocabulary.getKey()).getJsonArray("includes", new JsonArray()),
                        indexManager.getVocabulary().get(vocabulary.getKey()).getJsonArray("excludes", new JsonArray()))
                        .onComplete(ar -> replacementPromise.complete());
            }
        }
        Future.all(futureList).onComplete(ar -> promise.complete(payload));
        return promise.future();
    }

    private Future<Void> replaceVocableInItems(String vocabulary, JsonArray items) {
        if (items == null || items.isEmpty()) {
            return Future.succeededFuture();
        }
        List<Future<Void>> futureList = items.stream()
                .map(JsonObject.class::cast)
                .map(obj -> replaceVocableInItem(vocabulary, obj))
                .toList();

        return Future.all(futureList).mapEmpty();
    }

    private Future<Void> replaceVocableInPayloadPreprocessing(String vocabulary, Object payload, String field,
                                                              JsonArray includes, JsonArray excludes) {
        Promise<Void> resultPromise = Promise.promise();
        List<Future<Void>> futureList = new ArrayList<>();
        if (payload instanceof JsonArray payloadArray) {
            for (Object value : payloadArray) {
                Promise<Void> promise = Promise.promise();
                futureList.add(promise.future());
                replaceVocableInPayloadPreprocessing(vocabulary, value, field, includes, excludes)
                        .onComplete(ar -> promise.complete());
            }
        } else if (payload instanceof JsonObject payloadJson) {
            for (String key : payloadJson.getMap().keySet()) {
                if ((includes.isEmpty() || includes.contains(key)) && !excludes.contains(key)) {
                    if (key.equals(field)) {
                        Object current = payloadJson.getValue(key);
                        if (current instanceof JsonArray currentArray) {
                            for (Object listItem : currentArray) {
                                if (listItem instanceof JsonObject itemJsonObject) {
                                    Promise<Void> promise = Promise.promise();
                                    futureList.add(promise.future());
                                    replaceVocableInItem(vocabulary, itemJsonObject).onComplete(ar -> promise.complete());
                                }
                            }
                        } else if (current instanceof JsonObject currentObject) {
                            Promise<Void> promise = Promise.promise();
                            futureList.add(promise.future());
                            replaceVocableInItem(vocabulary, currentObject).onComplete(ar -> promise.complete());
                        }
                    } else {
                        Promise<Void> promise = Promise.promise();
                        futureList.add(promise.future());

                        JsonArray nextIncludes = new JsonArray();
                        includes.stream().map(it -> (String) it).forEach(value -> {
                            if (value.contains(".")) {
                                nextIncludes.add(value.substring(value.lastIndexOf(".") + 1));
                            }
                        });

                        JsonArray nextExcludes = new JsonArray();
                        excludes.stream().map(it -> (String) it).forEach(value -> {
                            if (value.contains(".")) {
                                nextExcludes.add(value.substring(value.lastIndexOf(".") + 1));
                            }
                        });

                        replaceVocableInPayloadPreprocessing(vocabulary, payloadJson.getValue(key), field, nextIncludes,
                                nextExcludes).onComplete(ar -> promise.complete());
                    }
                }
            }
        }
        Future.all(futureList).onComplete(ar -> resultPromise.complete());
        return resultPromise.future();
    }

    private Future<Void> replaceVocableInItem(String vocabulary, JsonObject toReplace) {
        if (toReplace.isEmpty()) {
            return Future.succeededFuture();
        }

        AtomicReference<String> vocableId = new AtomicReference<>(toReplace.getString("id", ""));
        if (!vocableId.get().isBlank()) {
            String resource = toReplace.getString("resource");
            if (resource != null && !resource.isEmpty()) {
                int lastIndexOfSlash = resource.lastIndexOf("/");
                if (lastIndexOfSlash > 0) {
                    String prefix = indexManager.getVocabulary().get(vocabulary).getString("prefix");
                    if (prefix != null && !prefix.isEmpty()) {
                        String substring = resource.substring(prefix.length());
                        vocableId.set(VocabularyHelperKt.normalize(substring));
                    } else {
                        vocableId.set(resource.substring(lastIndexOfSlash + 1));
                    }
                }
            }
        } else {
            return Future.succeededFuture();
        }

        return Future.future(promise -> readVocable(vocabulary, vocableId.get(), true)
                .onSuccess(result -> {
                    JsonObject readVocable = result.getJsonObject("result");
                    JsonArray replacements =
                            indexManager.getVocabulary().get(vocabulary).getJsonArray("replacements");

                    for (Object obj : replacements) {
                        String replacement = (String) obj;
                        String[] split = replacement.split(":");

                        String key = split[0];
                        String valueName = split[1];

                        Object value;
                        if (valueName.contains(".")) {
                            split = valueName.split("\\.");
                            value = readVocable.getJsonObject(split[0]).getValue(split[1]);
                        } else {
                            value = readVocable.getValue(valueName);
                        }

                        toReplace.put(key, value);
                    }
                })
                .onComplete(ar -> promise.complete()));
    }

    private Future<JsonObject> readVocable(String vocabulary, String vocableId, boolean useCache) {
        String cacheKey = vocabulary + "_" + vocableId;
        if (useCache && cache.containsKey(cacheKey)) {
            return Future.succeededFuture(ReturnHelper.returnSuccess(200, cache.get(cacheKey)));
        } else {
            return searchClient.getDocument("vocable", "vocabulary_" + vocabulary, vocableId, false)
                    .map(result -> {
                        cache.put(cacheKey, result);
                        return ReturnHelper.returnSuccess(200, result);
                    });
        }
    }

}
