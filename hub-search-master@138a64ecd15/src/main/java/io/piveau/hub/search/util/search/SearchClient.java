package io.piveau.hub.search.util.search;

import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Set;

public interface SearchClient {

    static SearchClient build(Vertx vertx, JsonObject config, IndexManager indexManager) {
        String clientType = config.getString("client", "ElasticsearchRestHighLevelClient");
        if (clientType.equals("ElasticsearchRestHighLevelClient")) {
            return new io.piveau.hub.search.util.search.SearchClientImpl.
                    Elasticsearch.RestHighLevelClientWrapper(vertx, config, indexManager);
        } else if (clientType.equals("OpenSearchRestHighLevelClient")) {
            return new io.piveau.hub.search.util.search.SearchClientImpl.
                    OpenSearch.RestHighLevelClientWrapper(vertx, config, indexManager);
        } else {
            return new io.piveau.hub.search.util.search.SearchClientImpl.
                    Elasticsearch.RestHighLevelClientWrapper(vertx, config, indexManager);
        }
    }

    Future<String> postDocument(String type, boolean hashId, JsonObject payload);
    Future<Void> patchDocument(String type, String documentId, boolean hashId, JsonObject payload);
    Future<Integer> putDocument(String type, String documentId, boolean hashId, JsonObject payload);
    Future<JsonObject> getDocument(String type, String documentId, boolean hashId);
    Future<JsonObject> getDocument(String type, boolean useWriteAlias, String documentId, boolean hashId);
    Future<JsonObject> getDocument(String type, String alias, String documentId, boolean hashId);
    Future<Void> deleteDocument(String type, String documentId, boolean hashId);
    Future<Long> countDocuments(String type, String idField, String documentId);
    Future<Void> deleteByQuery(String type, String idField, String documentId);
    Future<Void> updateByQuery(String type, String idField, String documentId, String field, List<String> globalReplacements, List<String> fieldReplacments, JsonObject payload, boolean replaceAll);
    Future<JsonArray> putDocumentsBulk(String type, JsonArray payload, boolean hashId);
    Future<JsonArray> putDocumentsBulk(String type, String revision, List<String> restoreFields, JsonArray payload, boolean hashId);
    Future<Void> updateVocabularyByQuery(String vocabulary, JsonArray vocab, List<String> types);
    Future<Object> searchFacetTitle(Query query, String itemId, String facetId, boolean fromIndex);
    Future<JsonArray> listIds(Query query, boolean subdivided, boolean onlyIds);
    Future<JsonObject> search(Query query);
    Future<JsonObject> scroll(String scrollId);
    Future<Void> ping();
    Future<Boolean> indexExists(String index);
    Future<String> indexCreate(String index, Integer numberOfShards);
    Future<String> indexDelete(String index);
    Future<JsonArray> getIndices(String index);
    Future<Set<String>> getAliases(String type);
    Future<String> setIndexAlias(String oldIndex, String newIndex, String alias);
    Future<String> putIndexTemplate(String index);
    Future<String> putLifecyclePolicy(String index);
    Future<String> putMapping(String index);
    Future<String> setMaxResultWindow(String index, Integer maxResultWindow);
    Future<String> setNumberOfReplicas(String index, Integer numberOfReplicas);
}
