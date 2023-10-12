package io.piveau.hub.search.util.index;

import io.piveau.hub.search.util.request.Field;
import io.piveau.json.ConfigHelper;
import io.piveau.schema.Mapping;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class IndexManager {

    private static final Logger LOG = LoggerFactory.getLogger(IndexManager.class);

    // index -> fields
    private final Map<String, Map<String, Field>> fields = new HashMap<>();

    // index -> boost
    private final Map<String, JsonObject> boost = new HashMap<>();

    // index -> facets
    private final Map<String, Map<String, JsonObject>> facets = new HashMap<>();

    // index -> facetOrder
    private final Map<String, List<JsonObject>> facetOrder = new HashMap<>();

    // index -> searchParams
    private final Map<String, Map<String, JsonObject>> searchParams = new HashMap<>();

    // index -> maxAggSize ; defines the maximum size of an aggregation
    private final Map<String, Integer> maxAggSize = new HashMap<>();

    // index -> maxResultWindow ; defines the maximum result of from + size
    private final Map<String, Integer> maxResultWindow = new HashMap<>();

    // index -> settings filepath
    private final Map<String, String> settingsFilepath = new HashMap<>();

    // index -> mapping filepath
    private final Map<String, String> mappingFilepath = new HashMap<>();

    // index -> index Template filepath
    private final Map<String, String> templateFilepath = new HashMap<>();

    // index -> ilm policy filepath
    private final Map<String, String> policyFilepath = new HashMap<>();

    // index list
    private final List<String> indexList = new ArrayList<>();

    // vertx context
    private final Vertx vertx;

    // vocabulary -> field etc.
    private final Map<String, JsonObject> vocabulary = new HashMap<>();

    // index -> shapes
    private final Map<String, JsonObject> shapes = new HashMap<>();

    // index -> mappings
    private final Map<String, JsonObject> mappings = new HashMap<>();

    // index -> open apis
    private final Map<String, JsonObject> openAPIs = new HashMap<>();

    public static IndexManager create(Vertx vertx, JsonObject config, Handler<AsyncResult<IndexManager>> handler) {
        return new IndexManager(vertx, config, handler);
    }

    private IndexManager(Vertx vertx, JsonObject config, Handler<AsyncResult<IndexManager>> handler) {
        this.vertx = vertx;

        // elasticsearch config
        JsonObject index = ConfigHelper.forConfig(config).forceJsonObject("index");

        JsonObject dataset = ConfigHelper.forConfig(index).forceJsonObject("dataset");
        JsonObject catalogue = ConfigHelper.forConfig(index).forceJsonObject("catalogue");
        JsonObject dataservice = ConfigHelper.forConfig(index).forceJsonObject("dataservice");
        JsonObject vocabulary = ConfigHelper.forConfig(index).forceJsonObject("vocabulary");
        JsonObject dataset_revisions = ConfigHelper.forConfig(index).forceJsonObject("dataset-revisions");
        JsonObject resource = ConfigHelper.forConfig(index).forceJsonObject("resource");

        JsonObject vocabularyConfig = ConfigHelper.forConfig(config).forceJsonObject("vocabulary");

        if (dataset.isEmpty() || catalogue.isEmpty() || dataservice.isEmpty() || vocabulary.isEmpty()) {
            handler.handle(Future.failedFuture("Index config is missing!"));
        } else {
            List<Future> indexFutureList = new ArrayList<>();
            indexFutureList.add(initIndex("dataset", dataset).future());
            indexFutureList.add(initIndex("catalogue", catalogue).future());
            indexFutureList.add(initIndex("dataservice", dataservice).future());
            indexFutureList.add(initIndex("vocabulary", vocabulary).future());
            indexFutureList.add(initIndex("dataset-revisions", dataset_revisions).future());

            for(String key : resource.getMap().keySet()) {
                indexFutureList.add(initIndex("resource_" + key, resource.getJsonObject(key)).future());
            }

            initVocabulary(vocabularyConfig);
            CompositeFuture.all(indexFutureList).onComplete(indexFutureHandler -> {
                if (indexFutureHandler.succeeded()) {
                    handler.handle(Future.succeededFuture(this));
                } else {
                    LOG.error("Init indexes: " + indexFutureHandler.cause());
                    handler.handle(Future.succeededFuture(this));
                }
            });
        }
    }

    private Promise<Void> initIndex(String index, JsonObject indexJson) {
        Promise<Void> indexPromise = Promise.promise();

        String indexSettingsFilepath = indexJson.getString("settings");
        String indexMappingFilepath = indexJson.getString("mapping");
        JsonObject indexShape = indexJson.getJsonObject("shape");
        JsonArray indexFacets = indexJson.getJsonArray("facets");
        JsonArray indexSearchParams = indexJson.getJsonArray("searchParams");
        Integer indexMaxAggSize = indexJson.getInteger("max_agg_size", 50);
        Integer indexMaxResultWindow = indexJson.getInteger("max_result_window", 10000);
        JsonObject indexBoost = indexJson.getJsonObject("boost", new JsonObject());
        String indexTemplateFilepath = indexJson.getString("indexTemplate");
        String indexPolicyFilepath = indexJson.getString("policy");

        if (indexSettingsFilepath == null || (indexMappingFilepath == null && indexShape == null)) {
            indexPromise.fail("Index config incorrect!");
        } else {
            facets.putIfAbsent(index, new HashMap<>());
            List<JsonObject> facetOrderList = new ArrayList<>();
            if (indexFacets != null) {
                for (Object facet : indexFacets) {
                    JsonObject facetJson = (JsonObject) facet;
                    String facetName = facetJson.getString("name");
                    String facetPath = facetJson.getString("path");
                    String facetType = facetJson.getString("type");
                    if (facetName == null || (facetPath == null && (facetType == null || !facetType.equals("nested")))) {
                        indexPromise.fail("Index config incorrect!");
                        return indexPromise;
                    }
                    facets.get(index).putIfAbsent(facetName, facetJson);
                    facetOrderList.add(facetJson);
                }
            }
            facetOrder.putIfAbsent(index, facetOrderList);

            searchParams.putIfAbsent(index, new HashMap<>());
            if (indexSearchParams != null) {
                for (Object searchParam : indexSearchParams) {
                    JsonObject searchParamJson = (JsonObject) searchParam;
                    if (searchParamJson == null || searchParamJson.isEmpty()) {
                        indexPromise.fail("Index config incorrect!");
                        return indexPromise;
                    }
                    String searchParamName = searchParamJson.getString("name");
                    if (searchParamName == null || searchParamName.isEmpty()) {
                        indexPromise.fail("Index config incorrect!");
                        return indexPromise;
                    }
                    searchParams.get(index).putIfAbsent(searchParamName, searchParamJson);
                }
            }

            maxAggSize.putIfAbsent(index, indexMaxAggSize);
            maxResultWindow.putIfAbsent(index, indexMaxResultWindow);

            settingsFilepath.putIfAbsent(index, indexSettingsFilepath);
            mappingFilepath.putIfAbsent(index, indexMappingFilepath);
            if (indexTemplateFilepath != null) {
                templateFilepath.putIfAbsent(index, indexTemplateFilepath);
            }
            if (indexPolicyFilepath != null) {
                policyFilepath.putIfAbsent(index, indexPolicyFilepath);
            }
            boost.putIfAbsent(index, indexBoost);

            shapes.putIfAbsent(index, indexShape);

            indexList.add(index);

            readMapping(index);
            indexPromise.complete();
        }

        return indexPromise;
    }

    private void initVocabulary(JsonObject vocabularyConfig) {
        for (String key : vocabularyConfig.getMap().keySet()) {
            vocabulary.put(key, vocabularyConfig.getJsonObject(key));
        }
    }

    private void readMapping(String index) {
        fields.putIfAbsent(index, new HashMap<>());
        JsonObject shape = shapes.get(index);
        JsonObject mappingJson;
        if (shape != null) {
            LOG.info("Found shape config for {}", index);

            Mapping mapping = Mapping.createMappingFromShaclShapes(
                    shape.getString("name"),
                    shape.getString("description"),
                    shape.getString("resource"),
                    shape.getString("path"),
                    Lang.TURTLE,
                    null
            );
            mappingJson = mapping.toMapping();

            openAPIs.put(index, mapping.toOpenAPI());

            LOG.info("Loaded shape successfully for {}", index);
        } else {
            mappingJson = vertx.fileSystem().readFileBlocking(mappingFilepath.get(index)).toJsonObject();
        }
        mappings.put(index, mappingJson);
        parseMapping(mappingJson.getJsonObject("properties"), boost.get(index), fields.get(index), null);
    }

    private void parseMapping(JsonObject mapping, JsonObject boost, Map<String, Field> fields, Field parent) {
        for (Map.Entry<String, Object> entry : mapping) {
            JsonObject fieldJson = (JsonObject) entry.getValue();
            Field field = new Field(entry.getKey(), ((JsonObject) entry.getValue()).getString("type"));
            if (boost != null)
                field.setBoost(boost.getFloat(field.getName(), 1.0f));
            JsonObject properties = fieldJson.getJsonObject("properties");

            if (properties != null) {
                field.setSubFields(new ArrayList<>());
                parseMapping(properties, boost, fields, field);
            } else {
                Boolean enabled = fieldJson.getBoolean("enabled");
                String type = fieldJson.getString("type");
                if ((enabled == null || enabled) && (type.equals("text") || type.equals("keyword"))) {
                    field.setSearchable(true);
                    if (parent != null) {
                        parent.setSearchable(true);
                    }
                }
            }

            if (parent == null) {
                fields.put(field.getName(), field);
            } else {
                parent.getSubFields().add(field);
            }
        }
    }

    public void boost(String filter, String field, Float value, Handler<AsyncResult<String>> handler) {
        String[] keys = field.split("\\.");

        if (fields.get(filter) == null) {
            handler.handle(Future.failedFuture("Filter doesn't exists, available filters=" + fields.keySet()));
        } else if (keys.length == 0) {
            handler.handle(Future.failedFuture("No field provided"));
        } else {
            Field current = fields.get(filter).get(keys[0]);

            for (int i = 1; i < keys.length; ++i) {
                if (current.getSubFields() != null) {
                    for (Field subField : current.getSubFields()) {
                        if (subField.getName().equals(keys[i])) {
                            current = subField;
                        }
                    }
                } else {
                    handler.handle(Future.failedFuture("Field not found"));
                    return;
                }
            }

            if (current != null) {
                current.setBoost(value);
                handler.handle(Future.succeededFuture(current.toString()));
            } else {
                handler.handle(Future.failedFuture("Field not found"));
            }
        }
    }

    public void setMaxAggSize(String filter, Integer maxAggSize, Handler<AsyncResult<String>> handler) {
        if (maxAggSize == null) {
            handler.handle(Future.failedFuture("Max aggregation size missing"));
        } else if (maxAggSize <= 0) {
            handler.handle(Future.failedFuture("Max aggregation size must be greater zero"));
        } else if (!this.maxAggSize.containsKey(filter)) {
            handler.handle(Future.failedFuture("Filter not found, available filters=" + this.maxAggSize));
        } else {
            this.maxAggSize.put(filter, maxAggSize);
            handler.handle(Future.succeededFuture("Successfully set maxAggSize = " + this.maxAggSize
                    + " for filter (" + filter + ")"));
        }
    }

    public boolean lifecyclePolicyExists(String index) {
        return policyFilepath.containsKey(index);
    }

    public boolean indexTemplateExist(String index) {
        return templateFilepath.containsKey(index);
    }

    public void setMaxResultWindow(String index, Integer maxResultWindow) {
        this.maxResultWindow.put(index, maxResultWindow);
    }

    public Map<String, Map<String, Field>> getFields() {
        return fields;
    }

    public Map<String, JsonObject> getBoost() {
        return boost;
    }

    public Map<String, Map<String, JsonObject>> getFacets() {
        return facets;
    }

    public Map<String, List<JsonObject>> getFacetOrder() {
        return facetOrder;
    }

    public Map<String, Map<String, JsonObject>> getSearchParams() {
        return searchParams;
    }

    public Map<String, Integer> getMaxAggSize() {
        return maxAggSize;
    }

    public Map<String, Integer> getMaxResultWindow() {
        return maxResultWindow;
    }

    public List<String> getIndexList() {
        return indexList;
    }

    public Map<String, JsonObject> getVocabulary() {
        return vocabulary;
    }

    public Map<String, String> getSettingsFilepath() {
        return settingsFilepath;
    }

    public Map<String, String> getMappingFilepath() {
        return mappingFilepath;
    }

    public Map<String, String> getTemplateFilepath() {
        return templateFilepath;
    }

    public Map<String, String> getPolicyFilepath() {
        return policyFilepath;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public Map<String, JsonObject> getShapes() {
        return shapes;
    }

    public Map<String, JsonObject> getMappings() {
        return mappings;
    }

    public Map<String, JsonObject> getOpenAPIs() {
        return openAPIs;
    }
}
