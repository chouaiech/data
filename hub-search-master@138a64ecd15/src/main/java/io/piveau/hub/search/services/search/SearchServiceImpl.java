package io.piveau.hub.search.services.search;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.services.catalogues.CataloguesService;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchServiceImpl implements SearchService {
    
    private final IndexManager indexManager;

    private final SearchClient searchClient;

    private final PiveauContext serviceContext;

    private final CataloguesService cataloguesService;

    // cache for scroll ids
    private final Cache<String, Query> cacheScrollIds;

    // caches for facet titles
    private final Cache<String, String> cacheTitleString;
    private final Cache<String, JsonObject> cacheTitleJson;

    SearchServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager, Handler<AsyncResult<SearchService>> handler) {
        this.indexManager = indexManager;

        this.searchClient = SearchClient.build(vertx, config, indexManager);

        this.cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS);

        this.serviceContext = new PiveauContext("hub.search", "SearchService");

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("scrollIdData",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Query.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10000, EntryUnit.ENTRIES))
                                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(10))))
                .build(true);

        cacheScrollIds = cacheManager.getCache("scrollIdData", String.class, Query.class);

        CacheManager cacheManagerString = CacheManagerBuilder.newCacheManagerBuilder().withCache("titleDataString",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10000, EntryUnit.ENTRIES))
                                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(12))))
                .build(true);

        cacheTitleString = cacheManagerString.getCache("titleDataString", String.class, String.class);

        CacheManager cacheManagerJson = CacheManagerBuilder.newCacheManagerBuilder().withCache("titleDataJson",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, JsonObject.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10000, EntryUnit.ENTRIES))
                                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(12))))
                .build(true);

        cacheTitleJson = cacheManagerJson.getCache("titleDataJson", String.class, JsonObject.class);

        searchClient.ping().onSuccess(result -> {
            for (String index : indexManager.getIndexList()) {
                if (!index.equals("vocabulary")) this.initIndex(index);
            }
            handler.handle(Future.succeededFuture(this));
        }).onFailure(failure -> handler.handle(Future.failedFuture(failure)));
    }

    private Future<JsonObject> handleSearchResult(JsonObject searchResult, Query query) {
        Promise<JsonObject> promise = Promise.promise();
        JsonObject result = searchResult.getJsonObject("result");

        if (query.isScroll()) {
            cacheScrollIds.put(result.getString("scrollId"), query);
        }

        List<String> includesRevised = new ArrayList<>();
        List<String> includes = query.getIncludes();
        if (includes != null && !includes.isEmpty()) {
            for (String include : includes) {
                if (include.contains("catalog.")) {
                    includesRevised.add(include.replace("catalog.", ""));
                }
            }
        }

        Promise<Void> replaceCatalogueInResultListPromise = Promise.promise();
        cataloguesService.replaceCatalogueInResultList(result.getJsonArray("results"), includesRevised).onComplete(
                ar -> {
            if (ar.succeeded()) {
                result.put("results", ar.result());
            }
            replaceCatalogueInResultListPromise.complete();
        });

        Promise<Void> replaceTitleInItemsPromise = Promise.promise();
        Promise<Void> replaceCatalogueInItemsPromise = Promise.promise();

        JsonArray facets = result.getJsonArray("facets");
        String index = result.getString("index");
        if (facets != null && !facets.isEmpty()) {
            replaceTitleInFacets(index, facets).onComplete(replaceTitleInFacetsResult -> {
                replaceTitleInItemsPromise.complete();
                boolean found = false;
                for (Object value : facets) {
                    JsonObject facet = (JsonObject) value;
                    if (facet.getString("id").equals("catalog")) {
                        if (facet.getJsonArray(Constants.FACET_ITEMS) == null || facet.getJsonArray(Constants.FACET_ITEMS).isEmpty()) {
                            replaceCatalogueInItemsPromise.complete();
                        } else {
                            cataloguesService.replaceCatalogueInItems(facet.getJsonArray(Constants.FACET_ITEMS)).onComplete(ar -> {
                                if (ar.succeeded()) {
                                    facet.put(Constants.FACET_ITEMS, ar.result());
                                }
                                replaceCatalogueInItemsPromise.complete();
                            });
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    replaceCatalogueInItemsPromise.complete();
                }
            });
        } else {
            replaceTitleInItemsPromise.complete();
            replaceCatalogueInItemsPromise.complete();
        }

        Future.all(List.of(replaceCatalogueInResultListPromise.future(), replaceTitleInItemsPromise.future(),
                replaceCatalogueInItemsPromise.future())).onComplete(ar -> promise.complete(searchResult));
        
        return promise.future();
    }

    @Override
    public Future<JsonObject> search(String q) {
        Promise<JsonObject> promise = Promise.promise();
        Query query = Json.decodeValue(q, Query.class);
        searchClient.search(query)
                .onSuccess(searchResult -> handleSearchResult(searchResult, query)
                        .onSuccess(promise::complete)
                        .onFailure(promise::fail))
                .onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> scroll(String scrollId) {
        Promise<JsonObject> promise = Promise.promise();
        Query query = cacheScrollIds.get(scrollId);
        if (query != null) {
            searchClient.scroll(scrollId)
                    .onSuccess(searchResult -> handleSearchResult(searchResult, query)
                            .onSuccess(promise::complete)
                            .onFailure(promise::fail))
                    .onFailure(promise::fail);
        } else {
            promise.fail(new ServiceException(404, "ScrollId not found"));
        }
        return promise.future();
    }

    @Override
    public Future<Boolean> indexExists(String index) {
        Promise<Boolean> promise = Promise.promise();
        searchClient.indexExists(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> indexCreate(String index, Integer numberOfShards) {
        Promise<String> promise = Promise.promise();
        searchClient.indexCreate(index, numberOfShards).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> indexDelete(String index) {
        Promise<String> promise = Promise.promise();
        searchClient.indexDelete(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> indexReset() {
        Promise<String> promise = Promise.promise();
        for (String index : indexManager.getIndexList()) {
            if (index.equals("vocabulary")) indexDelete("vocabulary_*").onComplete(indexDeleteResult -> {});
            else indexDelete(index + "*").onComplete(indexDeleteResult -> initIndex(index));
        }
        promise.complete("Triggered index reset.");
        return promise.future();
    }

    @Override
    public Future<String> putMapping(String index) {
        Promise<String> promise = Promise.promise();
        searchClient.putMapping(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> setIndexAlias(String oldIndex, String newIndex, String alias) {
        Promise<String> promise = Promise.promise();
        searchClient.setIndexAlias(oldIndex, newIndex, alias).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> boost(String index, String field, Float value) {
        Promise<String> promise = Promise.promise();
        indexManager.boost(index, field, value, boostResult -> {
            if (boostResult.succeeded()) {
                promise.complete(boostResult.result());
            } else {
                promise.fail(boostResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<String> setMaxAggSize(String index, Integer maxAggSize) {
        Promise<String> promise = Promise.promise();
        indexManager.setMaxAggSize(index, maxAggSize, setMaxAggSizeResult -> {
            if (setMaxAggSizeResult.succeeded()) {
                promise.complete(setMaxAggSizeResult.result());
            } else {
                promise.fail(setMaxAggSizeResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<String> setMaxResultWindow(String index, Integer maxResultWindow) {
        Promise<String> promise = Promise.promise();
        searchClient.setMaxResultWindow(index, maxResultWindow).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> setNumberOfReplicas(String index, Integer numberOfReplicas) {
        Promise<String> promise = Promise.promise();
        searchClient.setNumberOfReplicas(index, numberOfReplicas).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> putIndexTemplate(String index) {
        Promise<String> promise = Promise.promise();
        searchClient.putIndexTemplate(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> putLifecyclePolicy(String index) {
        Promise<String> promise = Promise.promise();
        searchClient.putLifecyclePolicy(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonArray> getIndices(String index) {
        Promise<JsonArray> promise = Promise.promise();
        searchClient.getIndices(index).onSuccess(promise::complete).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonArray> listIds(String filter, String field, JsonArray terms, String alias) {
        Promise<JsonArray> promise = Promise.promise();

        JsonObject q = new JsonObject();
        q.put("size", 10000);
        q.put("filter", filter);
        q.put("aggregation", false);
        q.put("includes", new JsonArray().add("id"));
        q.put("scroll", true);
        q.put("alias", alias);

        if (field != null && !field.isEmpty() && terms != null && !terms.isEmpty()) {
            q.put("facets", new JsonObject().put(field, terms));
        }

        Query query = Json.decodeValue(q.toString(), Query.class);

        searchClient.listIds(query, false, true).onSuccess(promise::complete).onFailure(promise::fail);

        return promise.future();
    }

    private void initMapping(String index) {
        putMapping(index + "_write").onComplete(putMappingResult -> {
            if (putMappingResult.succeeded()) {
                setMaxResultWindow(index + "_write", indexManager.getMaxResultWindow().get(index)).onComplete(
                        setMaxResultWindowResult -> {
                    if (!setMaxResultWindowResult.succeeded()) {
                        serviceContext.log().error(setMaxResultWindowResult.cause().getMessage());
                    }
                });
            } else {
                serviceContext.log().error(putMappingResult.cause().getMessage());
            }
        });
    }

    private void initIndex(String index) {
        indexExists(index + "_*").onComplete(indexExistsResult -> {
            if (indexExistsResult.succeeded()) {
                Promise<String> p = Promise.promise();
                if (indexManager.lifecyclePolicyExists(index)) {
                    putLifecyclePolicy(index).onComplete(r -> p.complete(r.result()));
                } else {
                    p.complete();
                }

                p.future().compose(r -> {
                    Promise<String> tp = Promise.promise();
                    if (indexManager.indexTemplateExist(index)) {
                        putIndexTemplate(index).onComplete(s -> tp.complete(s.result()));
                    } else {
                        tp.complete();
                    }
                    return tp.future();
                }).onFailure(err -> serviceContext.log().error(err.getMessage())).onSuccess(r -> {

                    if (Boolean.FALSE.equals(indexExistsResult.result())) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
                        Date today = new Date(System.currentTimeMillis());

                        String indexToday = index + "_" + formatter.format(today) + "-000001";
                        indexCreate(indexToday, null).onComplete(indexCreateResult -> {
                            if (!indexCreateResult.succeeded()) {
                                serviceContext.log().error(indexCreateResult.cause().getMessage());
                            }

                            setIndexAlias(null, indexToday, index + Constants.ELASTIC_WRITE_ALIAS).onComplete(
                                    setIndexWriteAliasResult -> {
                                if (!setIndexWriteAliasResult.succeeded()) {
                                    serviceContext.log().error(setIndexWriteAliasResult.cause().getMessage());
                                }
                                setIndexAlias(null, indexToday, index + Constants.ELASTIC_READ_ALIAS).onComplete(
                                        setIndexReadAliasResult -> {
                                    if (!setIndexReadAliasResult.succeeded()) {
                                        serviceContext.log().error(
                                                setIndexReadAliasResult.cause().getMessage());
                                    }
                                    initMapping(index);
                                });
                            });
                        });
                    } else {
                        initMapping(index);
                    }
                });
            }
        });
    }

    /*
     * F A C E T S
     */

    private Future<Void> replaceTitleInFacets(String index, JsonArray facets) {
        Promise<Void> promise = Promise.promise();
        if (index == null || index.isEmpty() || index.equals("*")) {
            promise.complete();
        } else {
            List<Future<Void>> futureList = new ArrayList<>();
            for (Object obj : facets) {
                Promise<Void> replacementPromise = Promise.promise();
                futureList.add(replacementPromise.future());

                JsonObject facet = (JsonObject) obj;
                String facetId = facet.getString("id");
                if (facetId.equals("catalog")) {
                    replacementPromise.complete();
                } else {
                    String type = indexManager.getFacets().get(index).get(facetId).getString("type");
                    if (type != null && !type.isEmpty() && !type.equals("term")) {
                        replacementPromise.complete();
                    } else {
                        JsonArray facetItems = facet.getJsonArray(Constants.FACET_ITEMS);
                        replaceTitleInFacetItems(index, facetId, facetItems).onComplete(
                                ar -> replacementPromise.complete());
                    }
                }
            }
            Future.all(futureList).onComplete(ar -> promise.complete());
        }
        return promise.future();
    }

    private Future<Void> replaceTitleInFacetItems(String index, String facetId, JsonArray items) {
        Promise<Void> promise = Promise.promise();
        if (items != null && !items.isEmpty()) {
            List<Future<Void>> futureList = new ArrayList<>();
            for (Object obj : items) {
                Promise<Void> replacementPromise = Promise.promise();
                futureList.add(replacementPromise.future());
                replaceTitleInFacetItem(index, facetId, (JsonObject) obj).onComplete(
                        ar -> replacementPromise.complete());
            }
            Future.all(futureList).onComplete(ar -> promise.complete());
        } else {
            promise.complete();
        }
        return promise.future();
    }

    private Future<Void> replaceTitleInFacetItem(String index, String facetId, JsonObject toReplace) {
        Promise<Void> promise = Promise.promise();
        if (!toReplace.isEmpty()) {
            String itemId = toReplace.getString("id");
            readFacetTitle(index, facetId, itemId).onComplete(ar -> {
                if (ar.succeeded()) {
                    Object readTitle = ar.result().getValue("result");
                    toReplace.put("title", readTitle);
                }
                promise.complete();
            });
        } else {
            promise.complete();
        }
        return promise.future();
    }

    private Future<JsonObject> readFacetTitle(String index, String facetId, String itemId) {
        Promise<JsonObject> promise = Promise.promise();
        if (itemId == null) {
            promise.fail(new ServiceException(400, "ID is null"));
            return promise.future();
        }

        if (itemId.isEmpty()) {
            promise.fail(new ServiceException(400, "ID is empty"));
            return promise.future();
        }

        String cacheKey = index + "_" + facetId + "_" + itemId;
        if (cacheTitleString.containsKey(cacheKey)) {
            promise.complete(ReturnHelper.returnSuccess(200, cacheTitleString.get(cacheKey)));
        } else if (cacheTitleJson.containsKey(cacheKey)) {
            promise.complete(ReturnHelper.returnSuccess(200, cacheTitleJson.get(cacheKey)));
        } else {
            JsonObject facetJson = indexManager.getFacets().get(index).get(facetId);

            String path;
            String displayTitle = facetJson.getString("display_title", "label");

            final String fromIndex = facetJson.getString("fromIndex", "");
            if (!fromIndex.isEmpty()) {
                path = displayTitle;
                facetId = "resource";
                index = fromIndex;
            } else {
                path = facetJson.getString("path");
            }

            JsonObject facets = new JsonObject();
            facets.put(facetId, new JsonArray().add(itemId));

            JsonArray includes = new JsonArray().add(path);

            JsonObject q = new JsonObject();
            q.put("filter", index);
            q.put("facets", facets);
            q.put("includes", includes);
            q.put("aggregation", false);
            q.put("from", 0);
            q.put("size", 1);

            Query query = Json.decodeValue(q.toString(), Query.class);

            searchClient.searchFacetTitle(query, itemId, facetId, !fromIndex.isEmpty()).onSuccess(result -> {
                if (result instanceof String resultString) {
                    cacheTitleString.put(cacheKey, resultString);
                    promise.complete(ReturnHelper.returnSuccess(200,resultString));
                } else if (result instanceof JsonObject resultJsonObject) {
                    cacheTitleJson.put(cacheKey, resultJsonObject);
                    promise.complete(ReturnHelper.returnSuccess(200, resultJsonObject));
                } else {
                    promise.fail(new ServiceException(500, "Title must be string or json"));
                }
            }).onFailure(promise::fail);
        }
        return promise.future();
    }
}
