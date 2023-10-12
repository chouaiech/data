package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.hash.HashHelper;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.json.JsonHelper;
import io.piveau.hub.search.util.request.Field;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indexlifecycle.LifecyclePolicy;
import org.elasticsearch.client.indexlifecycle.PutLifecyclePolicyRequest;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch.ExceptionHandler.handleElasticException;
import static io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch.IndexHelper.processPayloadLanguageFields;
import static io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch.IndexHelper.processResultLanguageFields;

public class RestHighLevelClientWrapper implements SearchClient {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // Elasticsearch legacy client
    RestHighLevelClient client;

    // Circuit breaker
    CircuitBreaker breaker;

    // For id hashing
    String hashingAlgorithm;

    // IndexManager
    private final IndexManager indexManager;

    // vertx context
    private final Vertx vertx;

    public RestHighLevelClientWrapper(Vertx vertx, JsonObject config, IndexManager indexManager) {
        this.vertx = vertx;
        this.indexManager = indexManager;

        String host = config.getString("host", "elasticsearch");
        Integer port = config.getInteger("port", 9200);
        String scheme = config.getString("scheme", "http");
        String user = config.getString("user", "elastic");
        String password = config.getString("password", "");
        Integer circuitBreakerTries = config.getInteger("circuitBreakerTries", 10);
        this.hashingAlgorithm = config.getString("hashingAlgorithm", "SHA3-256");

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme))
                .setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000))
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        this.client = new RestHighLevelClient(builder);

        this.breaker = CircuitBreaker.create("elasticsearch-breaker", vertx,
                new CircuitBreakerOptions()
                        .setMaxRetries(circuitBreakerTries)
                        .setTimeout(-1)
        ).retryPolicy((t, c) -> {
            log.debug("Retry {}", c);
            return c * 1000L;
        });
    }

    @Override
    public Future<String> postDocument(String type, boolean hashId, JsonObject payload) {
        Promise<String> promise = Promise.promise();

        String documentId = UUID.randomUUID().toString();

        String originId = documentId;

        documentId = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

        payload.put("id", originId);
        processPayloadLanguageFields(payload);

        String alias = Constants.getWriteAlias(type);

        IndexRequest indexRequest = new IndexRequest(alias).id(documentId).opType("create")
                .source(payload.encode(), XContentType.JSON);
        breaker.execute(breakerPromise ->
                client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        promise.complete(originId);
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(originId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<Void> patchDocument(String type, String documentId, boolean hashId, JsonObject payload) {
        Promise<Void> promise = Promise.promise();

        String originId = documentId;

        documentId = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

        payload.put("id", originId);
        processPayloadLanguageFields(payload);

        String alias = Constants.getWriteAlias(type);

        UpdateRequest updateRequest = new UpdateRequest(alias, documentId).doc(payload.toString(), XContentType.JSON);
        breaker.execute(breakerPromise ->
                client.updateAsync(updateRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(UpdateResponse updateResponse) {
                        promise.complete();
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(originId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });
        return promise.future();
    }

    @Override
    public Future<Integer> putDocument(String type, String documentId, boolean hashId,
                                       JsonObject payload) {
        Promise<Integer> promise = Promise.promise();

        String originId = documentId;

        documentId = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

        payload.put("id", originId);
        processPayloadLanguageFields(payload);

        String alias = Constants.getWriteAlias(type);

        IndexRequest indexRequest = new IndexRequest(alias).id(documentId)
                .source(payload.encode(), XContentType.JSON);
        breaker.execute(breakerPromise ->
                client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        promise.complete(indexResponse.status().getStatus());
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(originId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<JsonObject> getDocument(String type, String documentId, boolean hashId) {
        Promise<JsonObject> promise = Promise.promise();
        getDocument(type, false, documentId, hashId)
                .onSuccess(promise::complete)
                .onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> getDocument(String type, boolean useWriteAlias, String documentId, boolean hashId) {
        Promise<JsonObject> promise = Promise.promise();
        String alias = useWriteAlias ? Constants.getWriteAlias(type) : Constants.getReadAlias(type);
        getDocument(type, alias, documentId, hashId)
                .onSuccess(promise::complete)
                .onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<JsonObject> getDocument(String type, String alias, String documentId, boolean hashId) {
        Promise<JsonObject> promise = Promise.promise();

        String originId = documentId;

        documentId = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

        GetRequest getRequest = new GetRequest(alias, documentId);
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, null, null);
        getRequest.fetchSourceContext(fetchSourceContext);
        breaker.execute(breakerPromise ->
                client.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(GetResponse getResponse) {
                        if (getResponse.isExists()) {
                            JsonObject response = getResponseToJson(getResponse, indexManager.getFields().get(type));
                            processResultLanguageFields(response);
                            promise.complete(response);
                        } else {
                            promise.fail(new ServiceException(404, "not found"));
                        }
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(originId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<Void> deleteDocument(String type, String documentId, boolean hashId) {
        Promise<Void> promise = Promise.promise();

        String originId = documentId;

        documentId = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

        String alias = Constants.getWriteAlias(type);

        DeleteRequest deleteRequest = new DeleteRequest(alias, documentId);
        breaker.execute(breakerPromise ->
                client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(DeleteResponse deleteResponse) {
                        if (deleteResponse.status() == RestStatus.NOT_FOUND) {
                            promise.fail(new ServiceException(404, "not found"));
                        } else {
                            promise.complete();
                        }
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(originId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<Long> countDocuments(String type, String idField, String documentId) {
        Promise<Long> promise = Promise.promise();

        QueryBuilder termQuery = QueryBuilders.termQuery(idField, documentId);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(termQuery);

        String alias = Constants.getReadAlias(type);

        CountRequest countRequest = new CountRequest(alias);
        countRequest.source(searchSourceBuilder);
        breaker.execute(breakerPromise ->
                client.countAsync(countRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(CountResponse countResponse) {
                        promise.complete(countResponse.getCount());
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ExceptionHandler.handleElasticException(documentId, e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });

        return promise.future();
    }

    public Future<Void> updateVocabularyByQuery(String vocabulary, JsonArray vocab, List<String> types) {
        Promise<Void> promise = Promise.promise();

        UpdateByQueryRequest updateByQueryRequest =
                new UpdateByQueryRequest(types.stream().map(Constants::getWriteAlias).toArray(String[]::new));
        updateByQueryRequest.setConflicts("proceed");

        JsonObject vocabularyConfig = indexManager.getVocabulary().get(vocabulary);
        if (vocabularyConfig == null) {
            promise.complete();
            return promise.future();
        }

        JsonArray fields = vocabularyConfig.getJsonArray("fields");
        String replacementKey = vocabularyConfig.getString("replacementKey", "resource");

        Map<String, Object> map = new HashMap<>();
        for (Object obj : vocab) {
            JsonObject vocable = (JsonObject) obj;
            map.putIfAbsent(vocable.getString(replacementKey), JsonHelper.convertJsonObjectToMap(vocable));
        }

        Map<String, Object> params = new HashMap<>();
        params.putIfAbsent("vocab", map);
        params.putIfAbsent("fields", fields.getList());

        JsonArray replacements = vocabularyConfig.getJsonArray("replacements");

        String script = ScriptBuilder.buildScript(replacementKey, replacements);

        if (!script.isEmpty()) {
            updateByQueryRequest.setScript(
                    new Script(
                            ScriptType.INLINE,
                            "painless",
                            script,
                            params
                    )
            );

            breaker.execute(breakerPromise ->
                    client.updateByQueryAsync(updateByQueryRequest, increasedTimeout(5000, 120000),
                            new ActionListener<>() {
                                @Override
                                public void onResponse(BulkByScrollResponse bulkResponse) {
                                    log.debug("UpdateByQuery vocab: {}. For updated all datasets.", vocabulary);
                                    promise.complete();
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
                }
            });
        }

        return promise.future();
    }

    @Override
    public Future<Void> updateByQuery(String type, String idField, String documentId, String field,
                                      List<String> globalReplacements, List<String> fieldReplacements,
                                      JsonObject payload, boolean replaceAll) {
        Promise<Void> promise = Promise.promise();

        String alias = Constants.getWriteAlias(type);

        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(alias);
        updateByQueryRequest.setConflicts("proceed");

        updateByQueryRequest.setQuery(new TermQueryBuilder(idField, documentId));

        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> entry : payload.getMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value.getClass().equals(JsonArray.class)) {
                List<Object> list = new ArrayList<>();
                for (Object obj : ((JsonArray) value)) {
                    if (obj.getClass().equals(JsonArray.class)) {
                        JsonArray payloadNo_lang = ((JsonArray) obj).copy();
                        payloadNo_lang.forEach(elem -> ((JsonObject) elem).remove("_lang"));
                        list.add(payloadNo_lang.getList());
                    } else if (obj.getClass().equals(JsonObject.class)) {
                        JsonObject payloadNo_lang = ((JsonObject) obj).copy();
                        payloadNo_lang.remove("_lang");
                        list.add(payloadNo_lang.getMap());
                    } else {
                        list.add(obj);
                    }
                }
                map.putIfAbsent(key, list);
            } else if (value.getClass().equals(JsonObject.class)) {
                JsonObject payloadNo_lang = ((JsonObject) value).copy();
                payloadNo_lang.remove("_lang");
                map.putIfAbsent(key, payloadNo_lang.getMap());
            } else {
                map.putIfAbsent(key, value);
            }
        }

        StringBuilder script = new StringBuilder();

        Map<String, Object> params = new HashMap<>();

        if (globalReplacements != null && !globalReplacements.isEmpty()) {
            for (String replacementKey : globalReplacements) {
                if (payload.getJsonObject(replacementKey) != null) {
                    script.append("ctx._source.")
                            .append(replacementKey)
                            .append(" = params.")
                            .append(replacementKey)
                            .append(";");
                } else if (replaceAll) {
                    script.append("ctx._source.remove('")
                            .append(replacementKey)
                            .append("');");
                }

                if (payload.containsKey(replacementKey))
                    params.putIfAbsent(replacementKey, payload.getJsonObject(replacementKey).getMap());
            }
        }

        if (fieldReplacements != null && !fieldReplacements.isEmpty()) {
            for (String replacementKey : fieldReplacements) {
                if (payload.getJsonObject(replacementKey) != null) {
                    script.append("ctx._source.")
                            .append(field)
                            .append(".")
                            .append(replacementKey)
                            .append(" = params.")
                            .append(field)
                            .append(".")
                            .append(replacementKey)
                            .append(";");
                } else if (replaceAll) {
                    script.append("ctx._source.")
                            .append(field)
                            .append(".remove('")
                            .append(replacementKey)
                            .append("');");
                }
            }
            params.putIfAbsent(field, map);
        }

        if (!script.toString().isEmpty()) {
            updateByQueryRequest.setScript(
                    new Script(
                            ScriptType.INLINE,
                            "painless",
                            script.toString(),
                            params
                    )
            );

            breaker.execute(breakerPromise ->
                    client.updateByQueryAsync(updateByQueryRequest, increasedTimeout(5000, 120000),
                            new ActionListener<>() {
                                @Override
                                public void onResponse(BulkByScrollResponse bulkResponse) {
                                    log.debug("Update {} in {}: {}.", field, type, documentId);
                                    promise.complete();
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Update catalogue: {}", breakerResult.cause().getMessage());
                    promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
                }
            });
        }
        return promise.future();
    }

    @Override
    public Future<Void> deleteByQuery(String type, String idField, String documentId) {
        Promise<Void> promise = Promise.promise();

        String alias = Constants.getWriteAlias(type);

        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(alias);
        deleteByQueryRequest.setConflicts("proceed");
        deleteByQueryRequest.setQuery(new TermQueryBuilder(idField, documentId));
        breaker.execute(breakerPromise ->
                client.deleteByQueryAsync(deleteByQueryRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(BulkByScrollResponse bulkResponse) {
                        log.debug("DeleteByQuery: {} deleted all documents inside alias {}.", documentId, alias);
                        promise.complete();
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonArray> putDocumentsBulk(String type, JsonArray payload, boolean hashId) {
        return putDocumentsBulk(type, null, null, payload, hashId);
    }

    @Override
    public Future<JsonArray> putDocumentsBulk(String type, String revision, List<String> restoreFields,
                                              JsonArray payload, boolean hashId) {
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        HashMap<String, JsonObject> updates = new HashMap<>();

        payload.stream()
                .map(JsonObject.class::cast)
                .forEach(document -> {
                    String documentId = document.getString("id");
                    String idHash = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;

                    processPayloadLanguageFields(document);

                    FetchSourceContext fetchSourceContext = new FetchSourceContext(true, null, null);
                    MultiGetRequest.Item multiGetRequestItem = new MultiGetRequest.Item(Constants.getReadAlias(type), idHash)
                            .fetchSourceContext(fetchSourceContext);

                    multiGetRequest.add(multiGetRequestItem);
                    updates.put(documentId, document);
                });

        return Future.<MultiGetResponse>future(promise -> client.mgetAsync(multiGetRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(MultiGetResponse response) {
                        promise.complete(response);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        promise.fail(e);
                    }
                }))
                .compose(response -> {
                    BulkRequest bulkRequest = new BulkRequest();

                    Map<String, MultiGetItemResponse> responses = Arrays.asList(response.getResponses())
                            .stream()
                            .collect(Collectors.toMap(MultiGetItemResponse::getId, itemResponse -> itemResponse));

                    updates.forEach((id, document) -> {
                        String idHash = hashId ? HashHelper.hashId(hashingAlgorithm, id) : id;
                        if (!responses.get(idHash).isFailed() && responses.get(idHash).getResponse().isExists()) {
                            JsonObject oldDocument = getResponseToJson(responses.get(idHash).getResponse(), indexManager.getFields().get(type));
                            if (!revision.isBlank()) {
                                IndexRequest indexRequest = new IndexRequest(Constants.getWriteAlias(revision))
                                        .id(idHash)
                                        .source(oldDocument.encode(), XContentType.JSON);
                                bulkRequest.add(indexRequest);
                            }

                            for (String field : restoreFields) {
                                if (document.getJsonObject(field) == null ||
                                        document.getJsonObject(field).isEmpty()) {
                                    document.put(field, oldDocument.getJsonObject(field));
                                }
                            }
                        }
                        IndexRequest indexRequest = new IndexRequest(Constants.getWriteAlias(type))
                                .id(idHash)
                                .source(document.encode(), XContentType.JSON);
                        bulkRequest.add(indexRequest);
                    });
                    return sendBulkRequest(bulkRequest, List.copyOf(updates.keySet()), hashId);
                });
    }

    public Future<Set<String>> getAliases(String type) {
        Promise<Set<String>> promise = Promise.promise();
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
        breaker.execute(breakerPromise ->
                client.indices().getAliasAsync(getAliasesRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(GetAliasesResponse getAliasesResponse) {
                        if (getAliasesResponse.status() == RestStatus.OK) {
                            Set<String> dsrSet = new HashSet<>();
                            getAliasesResponse.getAliases().keySet().forEach(index -> {
                                if (index.startsWith(type)) dsrSet.add(index);
                            });
                            promise.complete(dsrSet);
                        } else {
                            log.error("Get aliases not succeeded");
                            promise.fail(new ServiceException(500, "Get aliases not succeeded"));
                        }
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handleElasticException(e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });
        return promise.future();
    }

    public Future<Object> searchFacetTitle(Query query, String itemId, String facetId, boolean fromIndex) {
        Promise<Object> promise = Promise.promise();

        String filter = query.getFilter();

        JsonObject facetJson = indexManager.getFacets().get(filter).get(facetId);

        String displayId = facetJson.getString("display_id", "id");
        String displayTitle = facetJson.getString("display_title", "label");

        SearchRequest searchRequest = SearchRequestHelper.buildSearchRequest(query, indexManager);
        breaker.execute(breakerPromise ->
                client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                    @Override
                    public void onResponse(SearchResponse searchResponse) {
                        JsonArray response = SearchResponseHelper.simpleProcessSearchResult(searchResponse);
                        if (!response.isEmpty()) {
                            JsonObject firstResult = response.getJsonObject(0);
                            Object facetTitle;
                            if (!fromIndex) {
                                facetTitle = SearchResponseHelper.getFacetTitle(firstResult, itemId, displayId, displayTitle);
                            } else {
                                facetTitle = firstResult.getValue(query.getIncludes().get(0));
                            }
                            promise.complete(facetTitle);
                        } else {
                            promise.fail(new ServiceException(404, "Title " + itemId + " not found"));
                        }
                        breakerPromise.complete();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                    }
                })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(new ServiceException(500, breakerResult.cause().getMessage()));
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonArray> listIds(Query query, boolean subdivided, boolean onlyIds) {
        Promise<JsonArray> promise = Promise.promise();

        JsonArray ids = new JsonArray();

        SearchRequest searchRequest = SearchRequestHelper.buildSearchRequest(query, indexManager);
        client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                String scrollId = searchResponse.getScrollId();
                JsonArray results = onlyIds ?
                        SearchResponseHelper.simpleProcessSearchResult(searchResponse, "id") :
                        SearchResponseHelper.simpleProcessSearchResult(searchResponse);
                if (!results.isEmpty()) {
                    if (subdivided) ids.add(results);
                    else ids.addAll(results);
                    scrollIds(scrollId, subdivided, ids, onlyIds, promise);
                } else {
                    promise.complete(ids);
                }
            }

            @Override
            public void onFailure(Exception e) {
                ExceptionHandler.handleException(e, promise);
            }
        });

        return promise.future();
    }

    @Override
    public Future<JsonObject> search(Query query) {
        Promise<JsonObject> promise = Promise.promise();

        String filter = query.getFilter();

        SearchRequest searchRequest;
        SearchRequest aggregationRequest;

        if (Strings.isNullOrEmpty(filter) || filter.equals("autocomplete")) {
            searchRequest = SearchRequestHelper.buildSearchRequest(query);
            aggregationRequest = null;
        } else {
            if (indexManager.getIndexList().contains(filter)) {
                searchRequest = SearchRequestHelper.buildSearchRequest(query, indexManager);
                if (query.isAggregation()) {
                    aggregationRequest = SearchRequestHelper
                            .buildAggregationRequest(query, indexManager);
                } else {
                    aggregationRequest = null;
                }
            } else {
                promise.fail(new ServiceException(400, "Search filter unknown"));
                return promise.future();
            }
        }

        Promise<SearchResponse> search = doSearch(searchRequest);
        Promise<SearchResponse> agg = doSearch(aggregationRequest);

        Future.all(search.future(), agg.future()).onComplete(ar -> {
            if (ar.succeeded()) {
                buildResult(search.future().result(), agg.future().result(), query)
                        .onSuccess(result -> promise.complete(ReturnHelper.returnSuccess(200, result)))
                        .onFailure(promise::fail);
            } else {
                promise.fail(new ServiceException(500, ar.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<JsonObject> scroll(String scrollId) {
        Promise<JsonObject> promise = Promise.promise();

        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(TimeValue.timeValueSeconds(300));
        Promise<SearchResponse> search = doScroll(scrollRequest);

        Query query = new Query();
        query.setScroll(true);

        search.future().onComplete(ar -> {
            if (ar.succeeded()) {
                buildResult(search.future().result(), null, query)
                        .onSuccess(result -> promise.complete(ReturnHelper.returnSuccess(200, result)))
                        .onFailure(promise::fail);
            } else {
                promise.fail(new ServiceException(500, ar.cause().getMessage()));
            }
        });

        return promise.future();
    }

    @Override
    public Future<Void> ping() {
        return breaker.execute(promise -> {
            try {
                client.ping(RequestOptions.DEFAULT);
                promise.complete();
            } catch (IOException e) {
                promise.fail(e);
            }
        });
    }

    @Override
    public Future<Boolean> indexExists(String index) {
        Promise<Boolean> promise = Promise.promise();
        breaker.execute(breakerPromise ->
                client.indices().existsAsync(prepareGetIndexRequest(index), RequestOptions.DEFAULT,
                        new ActionListener<>() {
                            @Override
                            public void onResponse(Boolean exists) {
                                promise.complete(exists);
                                breakerPromise.complete();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                            }
                        })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(breakerResult.cause().getMessage());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonArray> getIndices(String index) {
        Promise<JsonArray> promise = Promise.promise();
        breaker.<JsonArray>execute(breakerPromise ->
                client.indices().getAsync(prepareGetIndexRequest(index), RequestOptions.DEFAULT,
                        new ActionListener<>() {
                            @Override
                            public void onResponse(GetIndexResponse getIndexResponse) {
                                promise.complete(new JsonArray(Arrays.asList(getIndexResponse.getIndices())));
                                breakerPromise.complete();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                            }
                        })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                promise.fail(breakerResult.cause().getMessage());
            }
        });
        return promise.future();
    }

    @Override
    public Future<String> indexCreate(String index, Integer numberOfShards) {
        Promise<String> promise = Promise.promise();
        prepareIndexCreateRequest(index, numberOfShards).onSuccess(result -> {
            breaker.execute(breakerPromise ->
                    client.indices().createAsync(result, RequestOptions.DEFAULT,
                            new ActionListener<>() {
                                @Override
                                public void onResponse(CreateIndexResponse createIndexResponse) {
                                    if (createIndexResponse.isAcknowledged()
                                            && createIndexResponse.isShardsAcknowledged()) {
                                        promise.complete("The index was successfully created (" + index + ")");
                                    } else {
                                        promise.fail("Failed to create index (" + index + ")");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed index create: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> indexDelete(String index) {
        Promise<String> promise = Promise.promise();
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        breaker.execute(breakerPromise ->
                client.indices().deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT,
                        new ActionListener<>() {
                            @Override
                            public void onResponse(AcknowledgedResponse deleteIndexResponse) {
                                if (deleteIndexResponse.isAcknowledged()) {
                                    promise.complete("The index was successfully deleted (" + index + ")");
                                } else {
                                    promise.fail("Failed to delete index (" + index + ")");
                                }
                                breakerPromise.complete();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                            }
                        })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                log.error("Failed index delete: {}", breakerResult.cause().getMessage());
                promise.fail(breakerResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<String> setIndexAlias(String oldIndex, String newIndex, String alias) {
        Promise<String> promise = Promise.promise();
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        if (oldIndex != null && !oldIndex.isEmpty()) {
            IndicesAliasesRequest.AliasActions removeOldIndexAlias =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                            .index(oldIndex)
                            .alias(alias);
            indicesAliasesRequest.addAliasAction(removeOldIndexAlias);
        }
        if (newIndex != null && !newIndex.isEmpty()) {
            IndicesAliasesRequest.AliasActions addNewIndexAlias =
                    new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                            .index(newIndex)
                            .alias(alias);
            indicesAliasesRequest.addAliasAction(addNewIndexAlias);
        }
        breaker.execute(breakerPromise ->
                client.indices().updateAliasesAsync(indicesAliasesRequest, RequestOptions.DEFAULT,
                        new ActionListener<>() {
                            @Override
                            public void onResponse(AcknowledgedResponse indicesAliasesResponse) {
                                if (indicesAliasesResponse.isAcknowledged()) {
                                    promise.complete("Successfully set: " + alias);
                                } else {
                                    promise.fail("Failed to set: " + alias);
                                }
                                breakerPromise.complete();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                            }
                        })
        ).onComplete(breakerResult -> {
            if (breakerResult.failed()) {
                log.error("Failed set index alias: {}", breakerResult.cause().getMessage());
                promise.fail(breakerResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<String> putIndexTemplate(String index) {
        Promise<String> promise = Promise.promise();

        preparePutIndexTemplateRequest(index).onSuccess(result -> {
            breaker.execute(breakerPromise ->
                    client.indices().putIndexTemplateAsync(result, RequestOptions.DEFAULT,
                            new ActionListener<>() {
                                @Override
                                public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                                    if (acknowledgedResponse.isAcknowledged()) {
                                        promise.complete("The index template was successfully created (" + index + ")");
                                    } else {
                                        promise.fail("Failed to create index template (" + index + ")");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed put index template: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> putLifecyclePolicy(String index) {
        Promise<String> promise = Promise.promise();

        preparePutLifecyclePolicyRequest(index).onSuccess(result -> {
            breaker.execute(breakerPromise ->
                    client.indexLifecycle().putLifecyclePolicyAsync(result, RequestOptions.DEFAULT,
                            new ActionListener<>() {
                                @Override
                                public void onResponse(
                                        org.elasticsearch.client.core.AcknowledgedResponse acknowledgedResponse) {
                                    if (acknowledgedResponse.isAcknowledged()) {
                                        promise.fail("The lifecycle policy was successfully created (" + index + ")");
                                    } else {
                                        promise.fail("Failed to create lifecycle policy (" + index + ")");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed put lifecycle policy: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> putMapping(String index) {
        Promise<String> promise = Promise.promise();
        preparePutMappingRequest(index).onSuccess(result -> {
            breaker.execute(breakerPromise ->
                    client.indices().putMappingAsync(result, RequestOptions.DEFAULT,
                            new ActionListener<>() {
                                @Override
                                public void onResponse(AcknowledgedResponse putMappingResponse) {
                                    if (putMappingResponse.isAcknowledged()) {
                                        promise.complete("The mapping was successfully added (" + index + ")");
                                    } else {
                                        promise.fail("Failed to put mapping (" + index + ")");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed put mapping: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }).onFailure(promise::fail);
        return promise.future();
    }

    @Override
    public Future<String> setMaxResultWindow(String index, Integer maxResultWindow) {
        Promise<String> promise = Promise.promise();

        if (maxResultWindow == null) {
            promise.fail("Max result window missing");
        } else if (maxResultWindow < 10000) {
            promise.fail("Max result window has to be greate than 10000");
        } else {
            Settings settings = Settings.builder().put("index.max_result_window", maxResultWindow).build();
            UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(index).settings(settings);
            breaker.execute(breakerPromise ->
                    client.indices().putSettingsAsync(updateSettingsRequest, RequestOptions.DEFAULT,
                            new ActionListener<>() {

                                @Override
                                public void onResponse(AcknowledgedResponse updateSettingsResponse) {
                                    if (updateSettingsResponse.isAcknowledged()) {
                                        promise.complete("Successfully set max_result_window = " + maxResultWindow);
                                        indexManager.setMaxResultWindow(index, maxResultWindow);
                                    } else {
                                        promise.fail("Failed to set max_result_window");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed set max result window: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }
        return promise.future();
    }

    @Override
    public Future<String> setNumberOfReplicas(String index, Integer numberOfReplicas) {
        Promise<String> promise = Promise.promise();
        if (numberOfReplicas == null) {
            promise.fail("Number of Replicas missing");
        } else if (numberOfReplicas < 0) {
            promise.fail("Number of Replicas must be greater or equal zero");
        } else {
            Settings settings = Settings.builder().put("index.number_of_replicas", numberOfReplicas).build();
            UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(index).settings(settings);
            breaker.execute(breakerPromise ->
                    client.indices().putSettingsAsync(updateSettingsRequest, RequestOptions.DEFAULT,
                            new ActionListener<>() {
                                @Override
                                public void onResponse(AcknowledgedResponse updateSettingsResponse) {
                                    if (updateSettingsResponse.isAcknowledged()) {
                                        promise.complete("Successfully set number_of_replicas = " + numberOfReplicas);
                                    } else {
                                        promise.fail("Failed to set number_of_replicas");
                                    }
                                    breakerPromise.complete();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                                }
                            })
            ).onComplete(breakerResult -> {
                if (breakerResult.failed()) {
                    log.error("Failed set max result window: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }
        return promise.future();
    }

    /* H E L P E R */

    private Future<PutMappingRequest> preparePutMappingRequest(String index) {
        Promise<PutMappingRequest> promise = Promise.promise();

        JsonObject mapping;

        if (index.startsWith("dataset")) {
            mapping = indexManager.getMappings().get("dataset");
        } else if (index.startsWith("catalogue")) {
            mapping = indexManager.getMappings().get("catalogue");
        } else if (index.startsWith("dataservice")) {
            mapping = indexManager.getMappings().get("dataservice");
        } else if (index.startsWith("vocabulary_")) {
            mapping = indexManager.getMappings().get("vocabulary");
        } else if (index.startsWith("resource_")) {
            mapping = indexManager.getMappings().get(index.substring(0, index.lastIndexOf("_")));
        } else if (index.startsWith("dataset-revisions_")) {
            mapping = indexManager.getMappings().get("dataset-revisions");
        } else {
            log.error("Wrong index name: {}", index);
            promise.fail("Wrong index name: " + index);
            return promise.future();
        }

        PutMappingRequest putMappingRequest = new PutMappingRequest(index);
        putMappingRequest.source(mapping.toString(), XContentType.JSON);
        promise.complete(putMappingRequest);

        return promise.future();
    }

    private Future<PutLifecyclePolicyRequest> preparePutLifecyclePolicyRequest(String index) {
        Promise<PutLifecyclePolicyRequest> promise = Promise.promise();

        String filePath;

        if (index.equals("dataset")) {
            filePath = indexManager.getPolicyFilepath().get("dataset");
        } else if (index.equals("catalogue")) {
            filePath = indexManager.getPolicyFilepath().get("catalogue");
        } else if (index.equals("dataservice")) {
            filePath = indexManager.getPolicyFilepath().get("dataservice");
        } else if (index.startsWith("vocabulary_")) {
            filePath = indexManager.getPolicyFilepath().get("vocabulary");
        } else if (index.startsWith("resource_")) {
            filePath = indexManager.getPolicyFilepath().get(index.substring(0, index.lastIndexOf("_")));
        } else if (index.equals("dataset-revisions")) {
            filePath = indexManager.getPolicyFilepath().get("dataset-revisions");
        } else {
            log.error("Wrong index name: {}", index);
            promise.fail("Wrong index name: " + index);
            return promise.future();
        }

        vertx.fileSystem().readFile(filePath, ar -> {
            if (ar.succeeded()) {
                IndexHelper.generatePhaseMap(ar.result().toString()).future().onSuccess(map -> {
                    LifecyclePolicy lifecyclePolicy = new LifecyclePolicy(index + "-policy", map);
                    PutLifecyclePolicyRequest putLifecyclePolicyRequest = new PutLifecyclePolicyRequest(lifecyclePolicy);
                    promise.complete(putLifecyclePolicyRequest);
                }).onFailure(promise::fail);
            } else {
                promise.fail("Failed to read template file: " + ar.cause().getMessage());
            }
        });

        return promise.future();
    }

    private Future<PutComposableIndexTemplateRequest> preparePutIndexTemplateRequest(String index) {
        Promise<PutComposableIndexTemplateRequest> promise = Promise.promise();

        String tFilepath;
        String sFilepath;
        JsonObject mapping;

        if (index.equals("dataset_write")) {
            tFilepath = indexManager.getTemplateFilepath().get("dataset");
            sFilepath = indexManager.getSettingsFilepath().get("dataset");
            mapping = indexManager.getMappings().get("dataset");
        } else if (index.equals("catalogue_write")) {
            tFilepath = indexManager.getTemplateFilepath().get("catalogue");
            sFilepath = indexManager.getSettingsFilepath().get("catalogue");
            mapping = indexManager.getMappings().get("catalogue");
        } else if (index.equals("dataservice_write")) {
            tFilepath = indexManager.getTemplateFilepath().get("dataservice");
            sFilepath = indexManager.getSettingsFilepath().get("dataservice");
            mapping = indexManager.getMappings().get("dataservice");
        } else if (index.startsWith("vocabulary_")) {
            tFilepath = indexManager.getTemplateFilepath().get("vocabulary");
            sFilepath = indexManager.getSettingsFilepath().get("vocabulary");
            mapping = indexManager.getMappings().get("vocabulary");
        } else if (index.startsWith("resource_")) {
            tFilepath = indexManager.getTemplateFilepath().get(index.substring(0, index.lastIndexOf("_")));
            sFilepath = indexManager.getSettingsFilepath().get(index.substring(0, index.lastIndexOf("_")));
            mapping = indexManager.getMappings().get(index.substring(0, index.lastIndexOf("_")));
        } else if (index.equals("dataset-revisions")) {
            tFilepath = indexManager.getTemplateFilepath().get("dataset-revisions");
            sFilepath = indexManager.getSettingsFilepath().get("dataset");
            mapping = indexManager.getMappings().get("dataset");
        } else {
            log.error("Wrong index name: {}", index);
            promise.fail("Wrong index name: " + index);
            return promise.future();
        }

        vertx.fileSystem().readFile(sFilepath, readSettingsFileResult -> {
                    if (readSettingsFileResult.succeeded()) {
                        vertx.fileSystem().readFile(tFilepath, readTemplateFileResult -> {
                            if (readTemplateFileResult.succeeded()) {
                                ComposableIndexTemplate composableIndexTemplate =
                                        IndexHelper.generateIndexTemplate(
                                                readTemplateFileResult.result().toString(),
                                                readSettingsFileResult.result().toString(),
                                                mapping.toString()
                                        );
                                PutComposableIndexTemplateRequest putIndexTemplateRequest =
                                        new PutComposableIndexTemplateRequest();
                                putIndexTemplateRequest.name(index + "-index-template");
                                putIndexTemplateRequest.indexTemplate(composableIndexTemplate);

                                promise.complete(putIndexTemplateRequest);
                            } else {
                                promise.fail("Failed to read template file: " +
                                        readTemplateFileResult.cause().getMessage());
                            }
                        });
                    } else {
                        promise.fail("Failed to read settings file: " + readSettingsFileResult.cause().getMessage());
                    }
                }
        );
        return promise.future();
    }

    private Future<CreateIndexRequest> prepareIndexCreateRequest(String index, Integer numberOfShards) {
        Promise<CreateIndexRequest> promise = Promise.promise();

        String filePath;

        if (index.startsWith("dataset_")) {
            filePath = indexManager.getSettingsFilepath().get("dataset");
        } else if (index.startsWith("catalogue_")) {
            filePath = indexManager.getSettingsFilepath().get("catalogue");
        } else if (index.startsWith("dataservice_")) {
            filePath = indexManager.getSettingsFilepath().get("dataservice");
        } else if (index.startsWith("vocabulary_")) {
            filePath = indexManager.getSettingsFilepath().get("vocabulary");
        } else if (index.startsWith("resource_")) {
            filePath = indexManager.getSettingsFilepath().get(index.substring(0, index.lastIndexOf("_")));
        } else if (index.startsWith("dataset-revisions_")) {
            filePath = indexManager.getSettingsFilepath().get("dataset-revisions");
        } else {
            log.error("Wrong index name: {}", index);
            promise.fail("Wrong index name: " + index);
            return promise.future();
        }

        vertx.fileSystem().readFile(filePath, ar -> {
            if (ar.succeeded()) {
                JsonObject settings = ar.result().toJsonObject();
                if (numberOfShards != null) {
                    settings.put("number_of_shards", numberOfShards);
                }

                CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
                createIndexRequest.settings(
                        Settings.builder().loadFromSource(settings.toString(), XContentType.JSON)
                );
                promise.complete(createIndexRequest);
            } else {
                log.error("Failed to read settings file: {}", ar.cause());
                promise.fail("Failed to read settings file: " + ar.cause().getMessage());
            }
        });

        return promise.future();
    }

    private GetIndexRequest prepareGetIndexRequest(String index) {
        IndicesOptions indicesOptions = IndicesOptions.fromOptions(false, false, true, true, true);
        return new GetIndexRequest(index).indicesOptions(indicesOptions);
    }

    private Promise<SearchResponse> doScroll(SearchScrollRequest searchScrollRequest) {
        Promise<SearchResponse> promise = Promise.promise();
        if (searchScrollRequest == null) {
            promise.complete(null);
        } else {
            breaker.<SearchResponse>execute(breakerPromise ->
                    client.scrollAsync(searchScrollRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                        @Override
                        public void onResponse(SearchResponse searchResponse) {
                            breakerPromise.complete(searchResponse);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                        }
                    })
            ).onComplete(breakerResult -> {
                if (breakerResult.succeeded()) {
                    promise.complete(breakerResult.result());
                } else {
                    log.error("Search: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }
        return promise;
    }

    private Promise<SearchResponse> doSearch(SearchRequest searchRequest) {
        Promise<SearchResponse> promise = Promise.promise();
        if (searchRequest == null) {
            promise.complete(null);
        } else {
            breaker.<SearchResponse>execute(breakerPromise ->
                    client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                        @Override
                        public void onResponse(SearchResponse searchResponse) {
                            breakerPromise.complete(searchResponse);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            ExceptionHandler.handleElasticException(e, breakerPromise, promise);
                        }
                    })
            ).onComplete(breakerResult -> {
                if (breakerResult.succeeded()) {
                    promise.complete(breakerResult.result());
                } else {
                    log.error("Search: {}", breakerResult.cause().getMessage());
                    promise.fail(breakerResult.cause());
                }
            });
        }
        return promise;
    }

    private Future<JsonObject> buildResult(SearchResponse searchResponse, SearchResponse aggregationResponse,
                                           Query query) {
        Promise<JsonObject> promise = Promise.promise();

        JsonObject result = new JsonObject();

        if (query.getFilter() != null && !query.getFilter().isEmpty()) {
            result.put("index", query.getFilter());
        }

        result.put(Constants.SEARCH_RESULT_COUNT_FIELD, searchResponse.getHits().getTotalHits().value);

        if (query.isScroll()) {
            result.put("scrollId", searchResponse.getScrollId());
        }

        List<Future<Void>> futureList = new ArrayList<>();

        if (aggregationResponse != null && aggregationResponse.getAggregations() != null) {
            JsonArray facets = SearchResponseHelper
                    .processAggregationResult(query, aggregationResponse,
                            indexManager.getFacetOrder().get(query.getFilter()));
            result.put("facets", facets);
        }

        JsonArray datasets = new JsonArray();
        JsonArray countDatasets = new JsonArray();

        JsonArray results = SearchResponseHelper.processSearchResult(
                searchResponse,
                query,
                indexManager.getFacets().get("dataset"),
                datasets,
                countDatasets,
                indexManager.getFields().get(query.getFilter())
        );

        result.put("results", results);

        for (Object value : countDatasets) {
            JsonObject hitResult = (JsonObject) value;

            Promise<Void> countPromise = Promise.promise();
            if (hitResult.getString("id") != null) {
                countDocuments("datasets", "catalog.id.raw", hitResult.getString("id")).onComplete(countDatasetsResult -> {
                    if (countDatasetsResult.succeeded()) {
                        hitResult.put(Constants.SEARCH_RESULT_COUNT_FIELD, countDatasetsResult.result());
                    } else {
                        hitResult.putNull(Constants.SEARCH_RESULT_COUNT_FIELD);
                    }
                    countPromise.complete();
                });
            } else {
                countPromise.complete();
            }
            futureList.add(countPromise.future());
        }

        Future.all(futureList).onComplete(ar -> promise.complete(result));

        return promise.future();
    }

    private void scrollIds(String scrollId, boolean subdivided, JsonArray ids, boolean onlyIds,
                           Promise<JsonArray> promise) {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(TimeValue.timeValueSeconds(60));

        client.scrollAsync(scrollRequest, RequestOptions.DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                JsonArray results = onlyIds ?
                        SearchResponseHelper.simpleProcessSearchResult(searchResponse, "id") :
                        SearchResponseHelper.simpleProcessSearchResult(searchResponse);
                if (!results.isEmpty()) {
                    if (subdivided) ids.add(results);
                    else ids.addAll(results);
                    scrollIds(searchResponse.getScrollId(), subdivided, ids, onlyIds, promise);
                } else {
                    promise.complete(ids);
                }
            }

            @Override
            public void onFailure(Exception e) {
                ExceptionHandler.handleException(e, promise);
            }
        });
    }

    private RequestOptions increasedTimeout(int connectTimeout, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        return RequestOptions.DEFAULT.toBuilder()
                .setRequestConfig(requestConfig)
                .build();
    }

    private JsonObject getResponseToJson(GetResponse getResponse, Map<String, Field> fields) {
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

        JsonObject result;
        if (fields == null) {
            result = new JsonObject(sourceAsMap);
        } else {
            result = new JsonObject();
            for (String field : fields.keySet()) {
                if (getResponse.getIndex().startsWith("dataset") && field.equals("distributions")
                        && sourceAsMap.get("distributions") == null) {
                    result.put("distributions", new JsonArray());
                } else {
                    result.put(field, sourceAsMap.get(field));
                }
            }
        }

        DocumentField doc = getResponse.getFields().get("_ignored");

        if (doc != null) {
            for (Object value : doc.getValues()) {
                if (value.equals("modified")) {
                    String modified = result.getString("modified");
                    if (modified != null && !modified.isEmpty() && modified.charAt(0) == '_') {
                        result.put("modified", modified.substring(1));
                    }
                } else if (value.equals("issued")) {
                    String issued = result.getString("issued");
                    if (issued != null && !issued.isEmpty() && issued.charAt(0) == '_') {
                        result.put("issued", issued.substring(1));
                    }
                } else {
                    result.remove(value.toString());
                }
            }
        }

        return result;
    }

    private Future<JsonArray> sendBulkRequest(BulkRequest bulkRequest, List<String> documentIdList, boolean hashId) {
        if (bulkRequest.numberOfActions() == 0) {
            return Future.succeededFuture(new JsonArray());
        }

        return breaker.<JsonArray>execute(promise -> {
                            long start = System.currentTimeMillis();
                            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<>() {
                                @Override
                                public void onResponse(BulkResponse bulkResponse) {
                                    log.debug("Bulk request to elasticsearch: {}", System.currentTimeMillis() - start);
                                    HashMap<String, JsonObject> results = new HashMap<>();
                                    for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
                                        String datasetId = bulkItemResponse.getId();
                                        JsonObject result = new JsonObject()
                                                .put("status", bulkItemResponse.status().getStatus());
                                        if (bulkItemResponse.isFailed()) {
                                            log.trace("Bulk success: Index {}. Document {}.", bulkItemResponse.getIndex(),
                                                    bulkItemResponse.getId());
                                        } else {
                                            result.put("message", bulkItemResponse.getFailureMessage());
                                            log.error("Bulk failure: Index {}. Document {}. Status {}. Message {}",
                                                    bulkItemResponse.getIndex(), bulkItemResponse.getId(),
                                                    bulkItemResponse.status().getStatus(),
                                                    bulkItemResponse.getFailureMessage());
                                        }
                                        results.put(datasetId, result);
                                    }
                                    JsonArray processedResults = new JsonArray();
                                    for (String documentId : documentIdList) {
                                        String idHash = hashId ? HashHelper.hashId(hashingAlgorithm, documentId) : documentId;
                                        if (results.containsKey(idHash)) {
                                            JsonObject current = results.get(idHash);
                                            current.put("id", documentId);
                                            processedResults.add(current);
                                        }
                                    }
                                    promise.complete(processedResults);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    if (e instanceof ElasticsearchStatusException esx) {
                                        promise.fail(new ServiceException(esx.status().getStatus(), esx.getMessage()));
                                    } else {
                                        promise.fail(new ServiceException(500, e.getMessage()));
                                    }
                                }
                            });
                        }
                )
                .onFailure(cause -> log.error("Bulk request", cause));
    }

}
