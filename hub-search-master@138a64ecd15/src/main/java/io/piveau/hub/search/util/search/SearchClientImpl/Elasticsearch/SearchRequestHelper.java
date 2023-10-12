package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Field;
import io.piveau.hub.search.util.request.Query;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchRequestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SearchRequestHelper.class);

    public static SearchRequest buildSearchRequest(Query query) {
        BoolQueryBuilder fullQuery = SearchQueryBuilder.buildQuery(query);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(fullQuery);
        searchSourceBuilder.trackTotalHits(true);

        setRange(query, 10000, searchSourceBuilder);
        setInclude(query, searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest(
                Constants.getReadAlias("dataset"),
                Constants.getReadAlias("catalogue"),
                Constants.getReadAlias("dataservice"),
                "vocabulary_*",
                "resource_*"
        );
        searchRequest.source(searchSourceBuilder);

        if (query.isScroll())
            searchRequest.scroll(TimeValue.timeValueSeconds(60));

        return searchRequest;
    }

    public static SearchRequest buildSearchRequest(Query query, IndexManager indexManager) {
        String filter = query.getFilter();

        Integer maxResultWindow = indexManager.getMaxResultWindow().get(filter);
        Map<String, Field> fields = indexManager.getFields().get(filter);
        JsonObject boost = indexManager.getBoost().get(filter);
        Map<String, JsonObject> facets = indexManager.getFacets().get(filter);
        Map<String, JsonObject> searchParams = indexManager.getSearchParams().get(filter);

        BoolQueryBuilder fullQuery = SearchQueryBuilder.buildQuery(query, boost, facets, searchParams);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(fullQuery);
        searchSourceBuilder.trackTotalHits(true);

        setRange(query, maxResultWindow, searchSourceBuilder);
        setSort(query, fields, searchSourceBuilder);
        setInclude(query, searchSourceBuilder);

        SearchRequest searchRequest = createSearchRequest(query);
        searchRequest.source(searchSourceBuilder);

        if (query.isScroll())
            searchRequest.scroll(TimeValue.timeValueSeconds(60));

        return searchRequest;
    }

    public static SearchRequest buildAggregationRequest(Query query, IndexManager indexManager) {
        String filter = query.getFilter();

        Integer maxAggSize = indexManager.getMaxAggSize().get(filter);
        JsonObject boost = indexManager.getBoost().get(filter);
        Map<String, JsonObject> facets = indexManager.getFacets().get(filter);
        Map<String, JsonObject> searchParams = indexManager.getSearchParams().get(filter);

        BoolQueryBuilder fullQuery;

        if (query.getGlobalAggregation()) {
            fullQuery = SearchQueryBuilder.buildGlobalQuery(query, searchParams);
        } else {
            fullQuery = SearchQueryBuilder.buildQuery(query, boost, facets, searchParams);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(fullQuery);
        searchSourceBuilder.size(0);

        setAggregation(query, maxAggSize, facets, searchSourceBuilder);

        SearchRequest searchRequest = createSearchRequest(query);
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    private static void setRange(Query query, Integer maxResultWindow, SearchSourceBuilder searchSourceBuilder) {
        if (query.getFrom() + query.getSize() > maxResultWindow) {
            LOG.warn("from + size > max_result_window (" + maxResultWindow + ")");

            if (query.getFrom() > maxResultWindow) {
                LOG.warn("from > max_result_window; set from = 0 and size = 0");
                searchSourceBuilder.from(0);
                searchSourceBuilder.size(0);
            } else {
                LOG.warn("from <= max_result_window; set size = max_result_window - from = {}",
                        maxResultWindow - query.getFrom());
                searchSourceBuilder.from(query.getFrom());
                searchSourceBuilder.size(maxResultWindow - query.getFrom());
            }
        } else {
            searchSourceBuilder.from(query.getFrom());
            searchSourceBuilder.size(query.getSize());
        }
    }

    private static SearchRequest createSearchRequest(Query query) {
        SearchRequest searchRequest;
        if (query.getFilter().equals("vocabulary")) {
            if (query.getVocabulary() == null || query.getVocabulary().isEmpty()) {
                searchRequest = new SearchRequest("vocabulary_*");
            } else {
                String[] vocabularies = new String[query.getVocabulary().size()];
                int i = 0;
                for (String vocabularyId : query.getVocabulary()) {
                    vocabularies[i] = "vocabulary_" + vocabularyId;
                }
                searchRequest = new SearchRequest(vocabularies);
            }
        } else {
            if (query.getAlias() == null || query.getAlias().isEmpty()) {
                searchRequest = new SearchRequest(Constants.getReadAlias(query.getFilter()));
            } else {
                searchRequest = new SearchRequest(query.getAlias());
            }
        }
        return searchRequest;
    }

    private static void setSort(Query query, Map<String, Field> fields, SearchSourceBuilder searchSourceBuilder) {
        List<ImmutablePair<String, SortOrder>> sort = new ArrayList<>();
        if (query.getSort() != null && !query.getSort().isEmpty() && fields != null) {
            for (String currentSort : query.getSort()) {
                String[] sortSplit = currentSort.split("\\+");

                String sortField = sortSplit[0];
                SortOrder sortOrder = SortOrder.DESC;

                if (sortSplit.length >= 2) {
                    if (sortSplit[1].equalsIgnoreCase("asc")) {
                        sortOrder = SortOrder.ASC;
                    }
                }

                if (sortField.equalsIgnoreCase("relevance")) {
                    sort.add(new ImmutablePair<>("relevance", sortOrder));
                } else {
                    String[] path = sortField.split("\\.");

                    Field result = checkSortField(fields.get(path[0]), path, 0);

                    if (result != null) {
                        if (result.getType() != null) {
                            if (result.getType().equals("text")) {
                                sort.add(new ImmutablePair<>(sortField + ".raw", sortOrder));
                            } else if (result.getType().equals("keyword") || result.getType().equals("date")
                                    || result.getType().equals("integer")) {
                                sort.add(new ImmutablePair<>(sortField, sortOrder));
                            }
                        }
                    }
                }
            }
        }

        if (!sort.isEmpty()) {
            for (ImmutablePair<String, SortOrder> sortPair : sort) {
                if (sortPair.getLeft().equals("relevance")) {
                    searchSourceBuilder.sort(new ScoreSortBuilder().order((sortPair.getRight())));
                } else {
                    searchSourceBuilder.sort(new FieldSortBuilder(sortPair.getLeft()).order((sortPair.getRight())));
                }
            }
        }
    }

    private static void handleFacetType(SearchSourceBuilder searchSourceBuilder, Integer maxAggSize, JsonObject facetJson) {
        String facetName = facetJson.getString("name");
        String facetTitle = facetJson.getString("title");
        String facetPath = facetJson.getString("path");
        String facetType = facetJson.getString("type");
        switch (facetType) {
            case "min":
                searchSourceBuilder.aggregation(
                        SearchQueryBuilder.genMinAggregation(facetPath, facetName, facetTitle));
                break;
            case "max":
                searchSourceBuilder.aggregation(
                        SearchQueryBuilder.genMaxAggregation(facetPath, facetName, facetTitle));
                break;
            case "range":
                Double from = facetJson.getDouble("from", Double.MIN_VALUE);
                Double to = facetJson.getDouble("to", Double.MAX_VALUE);
                searchSourceBuilder.aggregation(
                        SearchQueryBuilder.genRangeAggregation(facetPath, facetName, facetTitle, from, to));
                break;
            case "mustMatch":
                Boolean boolMustMatch = facetJson.getBoolean("match");
                List valuesMustMatch = facetJson.getJsonArray("values").getList();
                searchSourceBuilder.aggregation(SearchQueryBuilder
                        .genMustMatchAggregation(facetPath, facetName, facetTitle, boolMustMatch, valuesMustMatch));
                break;
            case "mustNotMatch":
                Boolean boolMustNotMatch = facetJson.getBoolean("match");
                List valuesMustNotMatch = facetJson.getJsonArray("values").getList();
                searchSourceBuilder.aggregation(SearchQueryBuilder
                        .genMustNotMatchAggregation(facetPath, facetName, facetTitle, boolMustNotMatch, valuesMustNotMatch));
                break;
            default:
                Boolean plain = facetJson.getBoolean("plain", false);
                String facetAggregationTerm = facetJson.getString("aggregation_term", "id.raw");
                searchSourceBuilder.aggregation(SearchQueryBuilder.genTermsAggregation(facetPath, facetName, facetTitle,
                        facetAggregationTerm, maxAggSize, plain));
                break;
        }
    }

    private static void setAggregation(
            Query query,
            Integer maxAggSize,
            Map<String, JsonObject> facets,
            SearchSourceBuilder searchSourceBuilder) {

        for (String facetName : facets.keySet()) {
            if (query.isAggregationAllFields() || query.getAggregationFields().contains(facetName)) {
                JsonObject facetJson = facets.get(facetName);
                String facetTitle = facetJson.getString("title");
                String facetPath = facetJson.getString("path");
                String facetType = facetJson.getString("type");
                if (facetType == null) {
                    Boolean plain = facetJson.getBoolean("plain", false);
                    String facetAggregationType = facetJson.getString("aggregation_term", "id.raw");
                    searchSourceBuilder.aggregation(SearchQueryBuilder.genTermsAggregation(facetPath, facetName,
                            facetTitle, facetAggregationType, maxAggSize, plain));
                } else {
                    if (facetType.equals("nested")) {
                        JsonArray nestedFacets = facetJson.getJsonArray("facets");
                        for (Object obj : nestedFacets) {
                            JsonObject nestedFacet = (JsonObject) obj;
                            handleFacetType(searchSourceBuilder, maxAggSize, nestedFacet);
                        }
                    } else {
                        handleFacetType(searchSourceBuilder, maxAggSize, facetJson);
                    }
                }
            }
        }
    }

    private static void setInclude(Query query, SearchSourceBuilder searchSourceBuilder) {
        List<String> includes = query.getIncludes();

        if (includes != null && !includes.isEmpty()) {
            List<String> includesRevised = new ArrayList<>();
            for (String include : includes) {
                if (include.contains("title.")) {
                    includesRevised.add(include);
                    if (!includesRevised.contains("title._lang")) {
                        includesRevised.add("title._lang");
                    }
                } else if (include.contains("description.")) {
                    includesRevised.add(include);
                    if (!includesRevised.contains("description._lang")) {
                        includesRevised.add("description._lang");
                    }
                } else {
                    includesRevised.add(include);
                }
            }

            String[] includesAsArray = includesRevised.toArray(new String[0]);
            searchSourceBuilder.fetchSource(includesAsArray, null);
        }
    }

    private static Field checkSortField(Field current, String[] path, int i) {
        if (current == null) return null;

        if (path.length == ++i) {
            if (current.getSubFields() == null) {
                return current;
            } else {
                return null;
            }
        } else {
            if (current.getSubFields() != null) {
                Field result = null;
                for (Field subField : current.getSubFields()) {
                    if (subField.getName().equals(path[i])) {
                        result = checkSortField(subField, path, i);
                    }
                }
                return result;
            } else {
                return null;
            }
        }
    }

}
