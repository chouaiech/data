package io.piveau.hub.search.util.search.SearchClientImpl.OpenSearch;

import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.search.SearchClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Set;

public class RestHighLevelClientWrapper implements SearchClient {

    public RestHighLevelClientWrapper(Vertx vertx, JsonObject config, IndexManager indexManager) {

    }

    @Override
    public Future<String> postDocument(String type, boolean hashId, JsonObject payload) {
        return null;
    }

    @Override
    public Future<Void> patchDocument(String type, String documentId, boolean hashId, JsonObject payload) {
        return null;
    }

    @Override
    public Future<Integer> putDocument(String type, String documentId, boolean hashId, JsonObject payload) {
        return null;
    }

    @Override
    public Future<JsonObject> getDocument(String type, String documentId, boolean hashId) {
        return null;
    }

    @Override
    public Future<JsonObject> getDocument(String type, boolean useWriteAlias, String documentId, boolean hashId) {
        return null;
    }

    @Override
    public Future<JsonObject> getDocument(String type, String alias, String documentId, boolean hashId) {
        return null;
    }

    @Override
    public Future<Void> deleteDocument(String type, String documentId, boolean hashId) {
        return null;
    }

    @Override
    public Future<Long> countDocuments(String type, String idField, String documentId) {
        return null;
    }

    @Override
    public Future<Void> deleteByQuery(String type, String idField, String documentId) {
        return null;
    }

    @Override
    public Future<Void> updateByQuery(String type, String idField, String documentId, String field, List<String> globalReplacements, List<String> fieldReplacments, JsonObject payload, boolean replaceAll) {
        return null;
    }

    @Override
    public Future<JsonArray> putDocumentsBulk(String type, JsonArray payload, boolean hashId) {
        return null;
    }

    @Override
    public Future<JsonArray> putDocumentsBulk(String type, String revision, List<String> restoreFields, JsonArray payload, boolean hashId) {
        return null;
    }

    @Override
    public Future<Void> updateVocabularyByQuery(String vocabulary, JsonArray vocab, List<String> types) {
        return null;
    }

    @Override
    public Future<Object> searchFacetTitle(Query query, String itemId, String facetId, boolean fromIndex) {
        return null;
    }

    @Override
    public Future<JsonArray> listIds(Query query, boolean subdivided, boolean onlyIds) {
        return null;
    }

    @Override
    public Future<JsonObject> search(Query query) {
        return null;
    }

    @Override
    public Future<JsonObject> scroll(String scrollId) {
        return null;
    }

    @Override
    public Future<Void> ping() {
        return null;
    }

    @Override
    public Future<Boolean> indexExists(String index) {
        return null;
    }

    @Override
    public Future<String> indexCreate(String index, Integer numberOfShards) {
        return null;
    }

    @Override
    public Future<String> indexDelete(String index) {
        return null;
    }

    @Override
    public Future<JsonArray> getIndices(String index) {
        return null;
    }

    @Override
    public Future<Set<String>> getAliases(String type) {
        return null;
    }

    @Override
    public Future<String> setIndexAlias(String oldIndex, String newIndex, String alias) {
        return null;
    }

    @Override
    public Future<String> putIndexTemplate(String index) {
        return null;
    }

    @Override
    public Future<String> putLifecyclePolicy(String index) {
        return null;
    }

    @Override
    public Future<String> putMapping(String index) {
        return null;
    }

    @Override
    public Future<String> setMaxResultWindow(String index, Integer maxResultWindow) {
        return null;
    }

    @Override
    public Future<String> setNumberOfReplicas(String index, Integer numberOfReplicas) {
        return null;
    }
}
