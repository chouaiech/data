package io.piveau.hub.services.vocabularies;

import io.piveau.dcatap.*;
import io.piveau.hub.Defaults;
import io.piveau.hub.indexing.IndexingVocabulariesKt;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.Constants;
import io.piveau.rdf.Piveau;
import io.piveau.utils.JenaUtils;
import io.piveau.vocabularies.VocabularyHelper;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.DCAT;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class VocabulariesServiceImpl implements VocabulariesService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String CACHE_ALIAS = "vocabulariesService";

    private final TripleStore tripleStore;

    private final IndexService indexService;

    private final CatalogueManager catalogueManager;
    private final DatasetManager datasetManager;

    private final Cache<String, String[]> payloadCache;
    private final Cache<String, Integer> countCache;

    private final VocabularyHelper vocabularyHelper;

    private final DatasetsService datasetsService;

    private final Vertx vertx;

    VocabulariesServiceImpl(Vertx vertx, TripleStore tripleStore, JsonObject config, Handler<AsyncResult<VocabulariesService>> handler) {

        this.vertx = vertx;
        this.tripleStore = tripleStore;

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(CACHE_ALIAS, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String[].class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(12))))
                .build(true);

        payloadCache = cacheManager.getCache(CACHE_ALIAS, String.class, String[].class);

        cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(CACHE_ALIAS, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Integer.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(12))))
                .build(true);

        countCache = cacheManager.getCache(CACHE_ALIAS, String.class, Integer.class);

        JsonObject indexConfig = config.getJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE, new JsonObject());
        if (indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        } else {
            indexService = null;
        }

        catalogueManager = tripleStore.getCatalogueManager();
        datasetManager = tripleStore.getDatasetManager();

        vocabularyHelper = new VocabularyHelper(tripleStore);

        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);

        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public VocabulariesService listVocabularies(Handler<AsyncResult<JsonArray>> handler) {
        catalogueManager.allDatasetIdentifiers("vocabularies")
                .onSuccess(list -> handler.handle(Future.succeededFuture(new JsonArray(list))))
                .onFailure(cause -> handler.handle(Future.failedFuture(cause)));

        return this;
    }

    @Override
    public VocabulariesService toJson(String vocabularyId, String vocabularyUri, Handler<AsyncResult<JsonObject>> handler) {
        Promise<String> readVocabularyUriPromise = Promise.promise();

        if (vocabularyUri != null) {
            readVocabularyUriPromise.complete(vocabularyUri);
        } else {
            readVocabularyUri(vocabularyId).onComplete(readVocabularyUriPromise);
        }

        readVocabularyUriPromise.future().onSuccess(readVocabularyUriResult -> {
            vocabularyHelper.indexingSKOSVocabulary(readVocabularyUriResult).onSuccess(
                    indexVocabularyResult -> handler.handle(Future.succeededFuture(indexVocabularyResult))
            ).onFailure(indexVocabularyResult -> {
                        log.error("toJson failed: " +
                                indexVocabularyResult.getCause().getMessage());
                        handler.handle(ServiceException.fail(500,
                                indexVocabularyResult.getCause().getMessage()));
                    }
            );
        }).onFailure(readVocabularyUriResult -> {
            log.error("toJson failed: " + readVocabularyUriResult.getCause().getMessage());
            handler.handle(ServiceException.fail(500, readVocabularyUriResult.getCause().getMessage()));
        });

        return this;
    }

    @Override
    public VocabulariesService indexVocabulary(String vocabularyId, String vocabularyUri, Handler<AsyncResult<JsonArray>> handler) {
        if (indexService == null) {
            handler.handle(Future.failedFuture("Indexing service disabled"));
            return this;
        }

        AtomicReference<String> uriRef = new AtomicReference<>();
        readVocabularyUri(vocabularyId)
                .compose(uri -> {
                    uriRef.set(uri);
                    return tripleStore.getGraph(uri);
                })
                .compose(model -> {
                    JsonObject index = IndexingVocabulariesKt.indexConceptScheme(vocabularyId, uriRef.get(), model);
                    return Future.<JsonArray>future(promise -> indexService.putVocabulary(vocabularyId, index, promise));
                })
                .onSuccess(result -> {
                    log.debug("Vocabulary {} successfully indexed", vocabularyId);
                    handler.handle(Future.succeededFuture(result));
                })
                .onFailure(cause -> {
                    log.error("Indexing vocabulary {} failed", vocabularyId, cause);
                    handler.handle(Future.failedFuture(cause));
                });

        return this;
    }

    private Future<String> readVocabularyUri(String vocabularyId) {
        return datasetManager.identify(vocabularyId, "vocabularies")
                .compose(pair -> {
                    DCATAPUriRef uriRef = DCATAPUriSchema.parseUriRef(pair.component1().getURI());
                    return datasetManager.getGraph(uriRef.getGraphName());
                })
                .map(model -> model.listObjectsOfProperty(DCAT.accessURL)
                        .toList()
                        .stream().filter(RDFNode::isURIResource)
                        .findFirst().orElseThrow().asResource().getURI());
    }

    @Override
    public VocabulariesService readVocabulary(String vocabularyId, String acceptType, Handler<AsyncResult<String>> handler) {
        readVocabularyUri(vocabularyId)
                .compose(tripleStore::getGraph)
                .onSuccess(model -> {
                    Prefixes.setNsPrefixesFiltered(model);
                    String result = Piveau.presentAs(model, acceptType);
                    handler.handle(Future.succeededFuture(result));
                })
                .onFailure(cause -> handler.handle(Future.failedFuture(cause)));

        return this;
    }

    private Promise<String> postChunks(String vocabularyUri, String[] chunks, int chunkId, int numberOfChunks,
                                       String contentType) {
        Promise<String> promise = Promise.promise();

        String chunk = chunks[chunkId];
        Model model = JenaUtils.read(chunk.getBytes(), contentType);

        tripleStore.postGraph(vocabularyUri, model).onSuccess(postResult -> {
            if (chunkId + 1 < numberOfChunks) {
                postChunks(vocabularyUri, chunks, chunkId + 1, numberOfChunks, contentType).future()
                        .onSuccess(promise::complete)
                        .onFailure(cause -> promise.fail(cause.getMessage()));
            } else {
                promise.complete(postResult);
            }
        }).onFailure(cause -> promise.fail(cause.getMessage()));

        return promise;
    }

    private Promise<String> putVocabulary(String vocabularyUri, String[] chunks, int numberOfChunks,
                                          String contentType) {
        Promise<String> promise = Promise.promise();

        String clearGraphQuery = "CLEAR GRAPH <" + vocabularyUri + ">;";
        tripleStore.update(clearGraphQuery).onSuccess(clearResult -> {
            String chunk = chunks[0];
            Model model = JenaUtils.read(chunk.getBytes(), contentType);

            tripleStore.putGraph(vocabularyUri, model).onSuccess(putResult -> {
                postChunks(vocabularyUri, chunks, 1, numberOfChunks, contentType).future()
                        .onSuccess(postResult -> {
                            promise.complete(putResult);
                        }).onFailure(cause -> {
                            promise.fail(cause.getMessage());
                        });
            }).onFailure(cause -> {
                promise.fail(cause.getMessage());
            });
        }).onFailure(cause -> {
            promise.fail(cause.getMessage());
        });

        return promise;
    }

    @Override
    public VocabulariesService installVocabulary(String vocabularyId, String vocabularyUri, String contentType,
                                                 String file, Handler<AsyncResult<String>> handler) {
        final AtomicReference<String> status = new AtomicReference<>();
        vertx.<JsonArray>executeBlocking(promise -> {
                    try {
                        Path path = new File(file).toPath();
                        Model model = Piveau.toModel(path, Piveau.asRdfLang(contentType));
                        tripleStore.setGraph(vocabularyUri, model, false)
                                .compose(result -> {
                                    log.debug("Put success");
                                    status.set(result);
                                    if (indexService != null) {
                                        JsonObject index = IndexingVocabulariesKt.indexConceptScheme(vocabularyId, vocabularyUri, model);
                                        return Future.future(p -> indexService.putVocabulary(vocabularyId, index, p));
                                    } else {
                                        return Future.succeededFuture(new JsonArray());
                                    }
                                }).onComplete(promise);
                    } catch (Throwable t) {
                        log.error("Something went wrong", t);
                        promise.fail(new ServiceException(500, t.getMessage()));
                    }
                })
                .onComplete(ar -> vertx.fileSystem().delete(file))
                .onSuccess(array -> {
                    log.debug("Put vocabulary succeeded");
                    handler.handle(Future.succeededFuture(status.get()));
                })
                .onFailure(cause -> {
                    log.error("Put failed", cause);
                    handler.handle(ServiceException.fail(500, cause.getMessage()));
                });

        return this;
    }

    @Override
    public VocabulariesService createOrUpdateVocabulary(String vocabularyId, String vocabularyUri, String contentType,
                                                        String payload, String hash, int chunkId, int numberOfChunks,
                                                        Handler<AsyncResult<String>> handler) {
        if (numberOfChunks == 1) {
            final AtomicReference<String> status = new AtomicReference<>();
            final AtomicReference<Model> model = new AtomicReference<>();

            vertx.<JsonArray>executeBlocking(promise -> {
                        try {
                            Model vocabularyModel = Piveau.toModel(payload.getBytes(), contentType);
                            model.set(vocabularyModel);
                            tripleStore.setGraph(vocabularyUri, vocabularyModel, false)
                                    .compose(result -> {
                                        log.debug("Put success");
                                        status.set(result);
                                        if (indexService != null) {
                                            JsonObject index = IndexingVocabulariesKt.indexConceptScheme(vocabularyId, vocabularyUri, model.get());
                                            return Future.future(p -> indexService.putVocabulary(vocabularyId, index, p));
                                        } else {
                                            return Future.succeededFuture(new JsonArray());
                                        }
                                    }).onComplete(promise);
                        } catch (Throwable t) {
                            log.error("Something went wrong", t);
                            handler.handle(ServiceException.fail(500, t.getMessage()));
                        }
                    })
                    .onSuccess(array -> {
                        log.debug("Put vocabulary succeeded");
                        handler.handle(Future.succeededFuture(status.get()));
                    })
                    .onFailure(cause -> {
                        log.error("Put failed", cause);
                        handler.handle(ServiceException.fail(500, cause.getMessage()));
                    });
        } else {
            if (hash == null || hash.isEmpty()) {
                log.error("Put failed: hash null or empty");
                handler.handle(ServiceException.fail(400, "Put failed: hash null or empty"));
            } else if (chunkId < 0 || numberOfChunks < 0 || chunkId > numberOfChunks) {
                log.error("Put failed: chunkId or numberOfChunks incorrect");
                handler.handle(ServiceException.fail(400,
                        "Put failed: chunkId or numberOfChunks incorrect"));
            } else {
                String[] currentPayload = payloadCache.get(hash);
                Integer currentCount = countCache.get(hash);

                if (currentPayload != null && currentCount != null) {
                    currentPayload[chunkId] = payload;

                    currentCount = currentCount + 1;
                    countCache.put(hash, currentCount);

                    if (currentCount == numberOfChunks) {
                        putVocabulary(vocabularyUri, currentPayload, numberOfChunks, contentType).future()
                                .onSuccess(result -> {
                                    log.debug("Put success");
                                    handler.handle(Future.succeededFuture(result));

                                    payloadCache.remove(hash);
                                    countCache.remove(hash);

                                    indexVocabulary(vocabularyId, vocabularyUri, indexVocabularyResult -> {
                                        if (!indexVocabularyResult.succeeded()) {
                                            log.error("Indexing failure", indexVocabularyResult.cause());
                                        }
                                    });
                                }).onFailure(cause -> {
                                    log.error("Put error", cause);
                                    handler.handle(ServiceException.fail(500, cause.getMessage()));

                                    payloadCache.remove(hash);
                                    countCache.remove(hash);
                                });
                    } else {
                        handler.handle(Future.succeededFuture("accepted"));
                    }
                } else {
                    currentPayload = new String[numberOfChunks];
                    currentPayload[chunkId] = payload;
                    payloadCache.put(hash, currentPayload);

                    countCache.put(hash, 1);

                    handler.handle(Future.succeededFuture("accepted"));
                }
            }
        }

        return this;
    }

    @Override
    public VocabulariesService deleteVocabulary(String vocabularyId, Handler<AsyncResult<Void>> handler) {
        readVocabularyUri(vocabularyId)
                .compose(tripleStore::deleteGraph)
                .compose(v -> datasetsService.deleteDatasetOrigin(vocabularyId, "vocabularies"))
                .onSuccess(v -> handler.handle(Future.succeededFuture()))
                .onFailure(cause -> handler.handle(Future.failedFuture(cause)));

        return this;
    }

}
