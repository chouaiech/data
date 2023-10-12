package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.piveau.hub.search.util.request.Field;
import io.piveau.hub.search.util.request.Query;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;

import java.util.*;

public class SearchResponseHelper {

    public static JsonArray simpleProcessSearchResult(SearchResponse searchResponse, String field) {
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        return simpleProcessSearchResult(searchHits, field);
    }

    public static JsonArray simpleProcessSearchResult(SearchResponse searchResponse) {
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        return simpleProcessSearchResult(searchHits);
    }

    public static JsonArray processSearchResult(SearchResponse searchResponse,
                                                Query query,
                                                Map<String, JsonObject> datasetFacets,
                                                JsonArray datasets,
                                                JsonArray countDatasets,
                                                Map<String, Field> fields) {

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        return processSearchResult(searchHits, query, datasetFacets, datasets, countDatasets, fields);
    }

    private static JsonArray simpleProcessSearchResult(SearchHit[] searchHits, String field) {
        JsonArray results = new JsonArray();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            results.add(sourceAsMap.get(field));
        }
        return results;
    }

    private static JsonArray simpleProcessSearchResult(SearchHit[] searchHits)  {
        JsonArray results = new JsonArray();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            JsonObject hitResult;
            hitResult = new JsonObject(sourceAsMap);
            results.add(hitResult);
        }
        return results;
    }

    private static HashMap<String, List<String>> prepareLanguageFields(List<String> includes) {
        HashMap<String, List<String>> languageFields = new HashMap<>();
        languageFields.put("title", new ArrayList<>());
        languageFields.put("description", new ArrayList<>());
        if (includes != null) {
            for (String include : includes) {
                if (include.contains("title.")) {
                    String[] split = include.split("\\.");
                    if (split.length == 2) {
                        languageFields.get("title").add(include.split("\\.")[1]);
                    }
                } else if (include.contains("description.")) {
                    String[] split = include.split("\\.");
                    if (split.length == 2) {
                        languageFields.get("description").add(include.split("\\.")[1]);
                    }
                }
            }
        }
        return languageFields;
    }

    public static void processLanguageFields(JsonObject hitResult, String field, List<String> languages) {
        if (hitResult.getValue(field) instanceof JsonObject) {
            JsonObject fieldJson = hitResult.getJsonObject(field);
            if (fieldJson != null && !fieldJson.isEmpty()) {
                if (languages != null && !languages.isEmpty()) {
                    for (String language : languages) {
                        if (fieldJson.getString(language) == null) {
                            fieldJson.put(language, fieldJson.getString("_lang"));
                        }
                    }
                }
                fieldJson.remove("_lang");
            }
        }
    }

    public static JsonArray processSearchResult(SearchHit[] searchHits,
                                                Query query,
                                                Map<String, JsonObject> datasetFacets,
                                                JsonArray datasets,
                                                JsonArray countDatasets,
                                                Map<String, Field> fields) {

        JsonArray results = new JsonArray();

        HashMap<String, List<String>> languageFields =
                prepareLanguageFields(query != null ? query.getIncludes() : null);

        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            JsonObject hitResult;
            if (fields == null || (query != null && query.getIncludes() != null && !query.getIncludes().isEmpty())) {
                hitResult = new JsonObject(sourceAsMap);
            } else {
                hitResult = new JsonObject();
                for (String field : fields.keySet()) {
                    if (hit.getIndex().startsWith("dataset") && field.equals("distributions")
                            && sourceAsMap.get("distributions") == null) {
                        hitResult.put("distributions", new JsonArray());
                    } else {
                        hitResult.put(field, sourceAsMap.get(field));
                    }
                }
            }

            if (query != null && query.isShowScore()) {
                Float score = hit.getScore();
                hitResult.put("score", score);
            }

            for (String key : languageFields.keySet()) {
                processLanguageFields(hitResult, key, languageFields.get(key));
            }

            DocumentField doc = hit.getFields().get("_ignored");

            if (doc != null) {
                for (Object value : doc.getValues()) {
                    if (value.equals("modified")) {
                        String modified = hitResult.getString("modified");
                        if (modified != null && !modified.isEmpty() && modified.charAt(0) == '_') {
                            hitResult.put("modified", modified.substring(1));
                        }
                    } else if (value.equals("issued")) {
                        String issued = hitResult.getString("issued");
                        if (issued != null && !issued.isEmpty() && issued.charAt(0) == '_') {
                            hitResult.put("issued", issued.substring(1));
                        }
                    } else {
                        hitResult.putNull(value.toString());
                    }
                }
            }

            if (query != null && query.isElasticId()) {
                hitResult.put("_id", hit.getId());
            }

            if (hit.getIndex() != null && hit.getIndex().startsWith("catalogue")) {
                countDatasets.add(hitResult);
            }

            if (hit.getIndex() != null && hit.getIndex().startsWith("dataset")) {
                datasets.add(hitResult);
                if (query != null && query.isFilterDistributions()) {
                    for (String facet : datasetFacets.keySet()) {
                        String[] facetSplit = facet.split("\\.");
                        if (facetSplit.length == 2 && facetSplit[0].equals("distributions")) {
                            String facetSplitDist = facetSplit[1];
                            if (query.getFacets().get(facetSplitDist) != null) {
                                JsonArray distributions = hitResult.getJsonArray("distributions");
                                JsonArray distributionsFiltered = new JsonArray();

                                for (Object distribution : distributions) {
                                    JsonObject distJson = (JsonObject) distribution;
                                    JsonObject distFacet = distJson.getJsonObject(facetSplitDist);
                                    if (distFacet != null) {
                                        if (Arrays.asList(query.getFacets().get(facetSplitDist))
                                                .contains(distFacet.getString("id"))) {
                                            distributionsFiltered.add(distJson);
                                        }
                                    }
                                }
                                hitResult.put("distributions", distributionsFiltered);
                            }
                        }
                    }
                }
            }

            results.add(hitResult);
        }

        return results;
    }

    public static JsonArray processAggregationResult(Query query,
                                                     SearchResponse aggregationResponse,
                                                     List<JsonObject> facetOrder) {
        if (aggregationResponse != null && aggregationResponse.getAggregations() != null) {
            JsonObject facets = new JsonObject();

            Global globalAggregation = aggregationResponse.getAggregations().get("global");

            Aggregations aggregations;
            if (globalAggregation != null) {
                aggregations = globalAggregation.getAggregations();
            } else {
                aggregations = aggregationResponse.getAggregations();
            }

            for (Aggregation agg : aggregations) {
                JsonObject facet = new JsonObject()
                        .put("id", agg.getName())
                        .put("title", agg.getMetadata().get("title"));

                if (agg instanceof ParsedMin min) {
                    facet.put("min", min.getValue());
                }

                if (agg instanceof ParsedMax max) {
                    facet.put("max", max.getValue());
                }

                if (agg instanceof ParsedRange range) {
                    for (Range.Bucket bucket : range.getBuckets()) {
                        facet.put("count", bucket.getDocCount());
                    }
                    facet.put("from", agg.getMetadata().get("from"));
                    facet.put("to", agg.getMetadata().get("to"));
                }

                if (agg instanceof ParsedStringTerms terms) {
                    facet.put("items", new JsonArray());

                    int count = 0;

                    for (Terms.Bucket bucket : terms.getBuckets()) {
                        String id = bucket.getKey().toString();

                        JsonObject item = new JsonObject()
                                .put("count", bucket.getDocCount())
                                .put("id", id)
                                .put("title", id);

                        if ((query.getAggregationLimit() <= 0 || count < query.getAggregationLimit())
                                && bucket.getDocCount() >= query.getAggregationMinCount()) {
                            facet.getJsonArray("items").add(item);
                            count++;
                        }
                    }
                }

                if (agg instanceof Filter filter) {
                    facet.put("count", filter.getDocCount());
                }

                facets.put(facet.getString("id"), facet);
            }

            JsonArray facetsOrdered = new JsonArray();
            for (JsonObject f : facetOrder) {
                String facetName = f.getString("name");
                String facetType = f.getString("type");
                if (facetType != null && facetType.equals("nested")) {
                    JsonArray items = new JsonArray();
                    JsonObject parentFacet = new JsonObject()
                            .put("id", f.getString("name"))
                            .put("title", f.getString("title"))
                            .put("items", items);
                    JsonArray childFacets = f.getJsonArray("facets");
                    for (Object obj : childFacets) {
                        JsonObject childFacet = (JsonObject) obj;
                        String childFacetName = childFacet.getString("name");
                        items.add(facets.getJsonObject(childFacetName));
                    }
                    facetsOrdered.add(parentFacet);
                } else {
                    if (facets.getJsonObject(facetName) != null) {
                        facetsOrdered.add(facets.getJsonObject(facetName));
                    }
                }
            }
            return facetsOrdered;
        }

        return null;
    }

    private static JsonArray getFacetArray(JsonObject dataset, String displayId, String displayTitle) {
        JsonArray result = new JsonArray();
        for (String key : dataset.getMap().keySet()) {
            Object value = dataset.getValue(key);

            if (value instanceof JsonArray valueArray) {
                for (Object arrayValue : valueArray) {
                    if (arrayValue instanceof JsonObject valueObject) {
                        JsonObject arrayValueJson = valueObject;
                        if (arrayValueJson.getMap().containsKey(displayId)
                                && arrayValueJson.getMap().containsKey(displayTitle)) {
                            result.add(arrayValueJson);
                        } else {
                            result.addAll(getFacetArray(arrayValueJson, displayId, displayTitle));
                        }
                    }
                }
            }

            if (value instanceof JsonObject) {
                result.add(value);
            }
        }
        return result;
    }

    public static Object getFacetTitle(JsonObject dataset, String id, String displayId, String displayTitle) {
        JsonArray values = getFacetArray(dataset, displayId, displayTitle);
        for (Object value : values) {
            JsonObject current = (JsonObject) value;
            String currentId = current.getString(displayId);
            if (currentId != null && currentId.equalsIgnoreCase(id)) {
                Object title = current.getValue(displayTitle);
                if (title != null) {
                    return title;
                }
            }
        }
        return id;
    }

    public static String getCatalogTitle(JsonObject catalog) {
        JsonArray catalog_languages = catalog.getJsonArray("language");

        String catalog_language_id = "";
        if (catalog_languages != null && !catalog_languages.isEmpty()) {
            JsonObject catalogue_language = catalog_languages.getJsonObject(0);
            if (catalogue_language != null && !catalogue_language.isEmpty()) {
                catalog_language_id = catalogue_language.getString("id");
            }
        }

        String title = null;
        if (catalog_language_id != null && !catalog_language_id.isEmpty()) {
            JsonObject catalog_title = catalog.getJsonObject("title");
            if (catalog_title != null) {
                title = catalog_title.getString(catalog_language_id.toLowerCase());
            }
        }

        if (title == null) {
            JsonObject catalog_title = catalog.getJsonObject("title");
            if (catalog_title != null) {
                title = catalog_title.getString("en");
            }
        }

        if (title == null) {
            title = catalog.getString("id");
        }

        return title;
    }
}
