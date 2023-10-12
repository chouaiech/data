package io.piveau.hub.search.util.search.SearchClientImpl.Elasticsearch;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.geo.BoundingBox;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.geoBoundingBoxQuery;

class SearchQueryBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryBuilder.class);

    static BoolQueryBuilder buildQuery(Query query) {
        BoolQueryBuilder fullQuery = QueryBuilders.boolQuery();
        QueryBuilder qQuery = buildQQuery(query, null, null);
        fullQuery.must(qQuery);
        return fullQuery;
    }

    static BoolQueryBuilder buildQuery(
            Query query,
            JsonObject boost,
            Map<String, JsonObject> facets,
            Map<String, JsonObject> searchParams) {

        BoolQueryBuilder fullQuery = QueryBuilders.boolQuery();

        QueryBuilder qQuery = buildQQuery(query, boost, searchParams);

        fullQuery.must(qQuery);

        if (query.getExists() != null && !query.getExists().isEmpty()) {
            QueryBuilder existsQuery = buildExistsQuery(query.getExists());
            fullQuery.must(existsQuery);
        }

        if (query.getFilter() != null && !query.getFilter().isEmpty()) {
            if (query.getFacets() != null) {
                BoolQueryBuilder facetQuery = buildFacetQuery(
                        query,
                        facets
                );

                fullQuery.must(facetQuery);
            }

            if (query.getSearchParams() != null) {
                if (searchParams.get("scoring") != null) {
                    String field = searchParams.get("scoring").getString("field");
                    if (field != null) {
                        RangeQueryBuilder scoreQuery = buildScoreQuery(
                                query.getSearchParams().getMinScoring(),
                                query.getSearchParams().getMaxScoring(),
                                field
                        );
                        if (scoreQuery != null) fullQuery.must(scoreQuery);
                    }
                }
                if (searchParams.get("temporal") != null) {
                    String field = searchParams.get("temporal").getString("field");
                    if (field != null) {
                        RangeQueryBuilder dateQuery = buildDateQuery(
                                query.getSearchParams().getMinDate(),
                                query.getSearchParams().getMaxDate(),
                                field
                        );
                        if (dateQuery != null) fullQuery.must(dateQuery);
                    }
                }
                if (searchParams.get("spatial") != null) {
                    String field = searchParams.get("spatial").getString("field");
                    if (field != null) {
                        GeoBoundingBoxQueryBuilder spatialQuery = buildSpatialQuery(
                                query.getSearchParams().getBoundingBox(),
                                field
                        );
                        if (spatialQuery != null) fullQuery.filter(spatialQuery);
                    }
                }
                if (searchParams.get("countryData") != null && query.getSearchParams().getCountryData() != null) {
                    JsonArray values = searchParams.get("countryData").getJsonArray("values");
                    if(query.getSearchParams().getCountryData()) {
                        if (values != null) fullQuery.mustNot(QueryBuilders.termsQuery("country.id.raw", values.getList()));
                    } else {
                        if (values != null) fullQuery.filter(QueryBuilders.termsQuery("country.id.raw", values.getList()));
                    }
                }
                if (searchParams.get("dataServices") != null && query.getSearchParams().getDataServices() != null) {
                    String dataServicesField = searchParams.get("dataServices").getString("field", "distributions.access_service");
                    if(query.getSearchParams().getDataServices() ) {
                        fullQuery.must(QueryBuilders.existsQuery(dataServicesField));
                    }
                }
            }
        }

        return fullQuery;
    }

    static BoolQueryBuilder buildGlobalQuery(
            Query query,
            Map<String, JsonObject> searchParams) {

        BoolQueryBuilder fullQuery = QueryBuilders.boolQuery();

        if (query.getFilter() != null && !query.getFilter().isEmpty()) {
            if (query.getSearchParams() != null) {
                if (searchParams.get("countryData") != null && query.getSearchParams().getCountryData() != null) {
                    JsonArray values = searchParams.get("countryData").getJsonArray("values");
                    if(query.getSearchParams().getCountryData()) {
                        if (values != null) fullQuery.mustNot(QueryBuilders.termsQuery("country.id.raw", values.getList()));
                    } else {
                        if (values != null) fullQuery.filter(QueryBuilders.termsQuery("country.id.raw", values.getList()));
                    }
                }
            }
        }

        return fullQuery;
    }

    private static QueryBuilder buildQQuery(Query query, JsonObject boost, Map<String, JsonObject> searchParams) {
        QueryBuilder qQuery;

        if (query.getQ() == null || query.getQ().isEmpty()) {
            qQuery = QueryBuilders.matchAllQuery();
        } else {
            Map<String, Float> multiMatchFields = determineMultiMatchFields(query, boost, searchParams);
            qQuery = parseQueryString(query.getQ(), multiMatchFields);
        }

        return qQuery;
    }

    private static QueryBuilder buildExistsQuery(List<String> exists) {
        BoolQueryBuilder existsQuery = QueryBuilders.boolQuery();
        for (String exist : exists) {
            existsQuery.must(QueryBuilders.existsQuery(exist));
        }
        return existsQuery;
    }

    private static BoolQueryBuilder buildFacetQuery(Query query, Map<String, JsonObject> facets) {
        BoolQueryBuilder facetQuery = QueryBuilders.boolQuery();

        HashMap<String, String[]> queryFacets = query.getFacets();
        for (String facetName : queryFacets.keySet()) {
            if (facets.containsKey(facetName)) {
                JsonObject facetJson = facets.get(facetName);
                String facetPath = facetJson.getString("path");
                String facetType = facetJson.getString("type");
                Boolean facetPlain = facetJson.getBoolean("plain", false);
                String facetAggregationTerm = facetJson.getString("aggregation_term", "id.raw");

                if (facetType == null || facetType.isEmpty()) {
                    String field = facetPlain ? facetPath : facetPath + "." + facetAggregationTerm;

                    if (query.getFacets().get(facetName) != null && query.getFacets().get(facetName).length != 0) {
                        BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
                        if (query.getFacetOperator().equals(Constants.Operator.AND)) {
                            for (String facet : query.getFacets().get(facetName)) {
                                subQuery.must(QueryBuilders
                                        .termQuery(field, facet));
                            }
                        } else {
                            for (String facet : query.getFacets().get(facetName)) {
                                subQuery.should(QueryBuilders
                                        .termQuery(field, facet));
                            }

                            subQuery.minimumShouldMatch(1);
                        }
                        if (query.getFacetGroupOperator().equals(Constants.Operator.AND)) {
                            facetQuery.must(subQuery);
                        } else {
                            facetQuery.should(subQuery);
                        }
                    }
                }
            } else {
                String[] values = queryFacets.get(facetName);
                BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
                for (String value : values) {
                    subQuery.should(QueryBuilders.termQuery(facetName, value));
                }
                if (!query.getFacetOperator().equals(Constants.Operator.AND)) {
                    subQuery.minimumShouldMatch(1);
                }
                if (query.getFacetGroupOperator().equals(Constants.Operator.AND)) {
                    facetQuery.must(subQuery);
                } else {
                    facetQuery.should(subQuery);
                }
            }
        }

        if (query.getFacetGroupOperator().equals(Constants.Operator.OR)) {
            facetQuery.minimumShouldMatch(1);
        }

        return facetQuery;
    }

    private static RangeQueryBuilder buildScoreQuery(Integer minScoring, Integer maxScoring, String scoreField) {
        if (scoreField != null && !scoreField.isEmpty()) {
            if (minScoring != null && maxScoring == null) {
                return QueryBuilders.rangeQuery(scoreField).gte(minScoring);
            }
            if (minScoring == null && maxScoring != null) {
                return QueryBuilders.rangeQuery(scoreField).lte(maxScoring);
            }
            if (minScoring != null /*&& maxDate != null*/) {
                return QueryBuilders.rangeQuery(scoreField).gte(minScoring).lte(maxScoring);
            }
        }
        return null;
    }

    private static RangeQueryBuilder buildDateQuery(Date minDate, Date maxDate, String dateField) {
        if (dateField != null && !dateField.isEmpty()) {
            if (minDate != null && maxDate == null) {
                return QueryBuilders.rangeQuery(dateField).gte(minDate);
            }
            if (minDate == null && maxDate != null) {
                return QueryBuilders.rangeQuery(dateField).lte(maxDate);
            }
            if (minDate != null /*&& maxDate != null*/) {
                return QueryBuilders.rangeQuery(dateField).gte(minDate).lte(maxDate);
            }
        }
        return null;
    }

    private static GeoBoundingBoxQueryBuilder buildSpatialQuery(BoundingBox boundingBox, String spatialField) {
        if (spatialField != null && !spatialField.isEmpty()) {
            if (boundingBox != null) {
                Float minLon = boundingBox.getMinLon();
                Float maxLon = boundingBox.getMaxLon();
                Float maxLat = boundingBox.getMaxLat();
                Float minLat = boundingBox.getMinLat();

                if (minLon != null && maxLon != null && maxLat != null && minLat != null) {
                    return geoBoundingBoxQuery(spatialField).setCorners(maxLon, minLat, minLon, maxLat);
                }
            }
        }
        return null;
    }

    private static QueryBuilder parseQueryString(String querystring, Map<String, Float> multiMatchFields) {
        querystring = querystring.trim();

        if (querystring.isEmpty()) {
            return QueryBuilders.matchAllQuery();
        }

        if (Stream.of("(", ")", "AND", "OR", "*", "?", "\"").anyMatch(querystring::contains)) {
            try {
                StandardQueryParser queryParser = new StandardQueryParser();
                queryParser.setAllowLeadingWildcard(false);
                queryParser.setAnalyzer(new WhitespaceAnalyzer());
                queryParser.parse(querystring, "dummyField");

                return buildQueryString(querystring, multiMatchFields);
            } catch (Exception e) {
                return buildMultiMatch(querystring, multiMatchFields);
            }
        } else {
            return buildMultiMatch(querystring, multiMatchFields);
        }
    }

    private static QueryBuilder buildQueryString(String querystring, Map<String, Float> fields) {
        if (querystring.startsWith("\"") && querystring.endsWith("\"")) {
            querystring = querystring.toLowerCase();
            return QueryBuilders.queryStringQuery(querystring)
                    .fields(fields)
                    .analyzer("whitespace");
        } else {
            return QueryBuilders.queryStringQuery(querystring)
                    .fields(fields)
                    .allowLeadingWildcard(false)
                    .analyzeWildcard(true);
        }
    }

    private static QueryBuilder buildMultiMatch(String querystring, Map<String, Float> multiMatchFields) {
        querystring = querystring.trim();

        if (querystring.isEmpty()) {
            return QueryBuilders.matchAllQuery();
        }

        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(querystring).lenient(true);

        multiMatchQuery.fields(multiMatchFields);

        return multiMatchQuery;
    }

    private static Map<String, Float> determineMultiMatchFields(Query query, JsonObject boost,
                                                                Map<String, JsonObject> searchParams) {
        Map<String, Float> multiMatchFields = new HashMap<>();

        if (query.getFilter() != null && !query.getFilter().isEmpty()) {
            if (query.isAutocomplete()) {
                String autoCompleteField = "title.*.autocomplete";
                if (searchParams != null) {
                    JsonObject autocompleteJson = searchParams.get("autocomplete");
                    if (autocompleteJson != null && !autocompleteJson.isEmpty()) {
                        autoCompleteField = autocompleteJson.getString("field");
                    }
                }
                multiMatchFields.put(autoCompleteField, 1.0f);
            } else {
                if (query.getFields() == null || query.getFields().isEmpty()) {
                    multiMatchFields.put("*", 1.0f);
                    for (String key : boost.getMap().keySet()) {
                        multiMatchFields.put(key, boost.getFloat(key));
                    }
                } else {
                    for (String field : query.getFields()) {
                        String[] split = field.split("\\^");
                        if (split.length == 2) {
                            try {
                                float boostValue = Float.parseFloat(split[1]);
                                multiMatchFields.put(split[0], boostValue);
                            } catch (NumberFormatException e) {
                                multiMatchFields.put(split[0], 1.0f);
                            }
                        } else {
                            multiMatchFields.put(split[0], 1.0f);
                        }
                    }
                }
            }
        }

        return multiMatchFields;
    }

    static MinAggregationBuilder genMinAggregation(String path, String name, String title) {
        return AggregationBuilders.min(name).field(path).setMetadata(genMetaData(title));
    }

    static MaxAggregationBuilder genMaxAggregation(String path, String name, String title) {
        return AggregationBuilders.max(name).field(path).setMetadata(genMetaData(title));
    }

    static RangeAggregationBuilder genRangeAggregation(String path, String name, String title, Double from, Double to) {
        return AggregationBuilders.range(name).addRange(from, to).field(path).setMetadata(genMetaData(title, from, to));
    }

    static TermsAggregationBuilder genTermsAggregation(String path, String name, String title, String aggregationTerm,
                                                       Integer maxAggSize, Boolean plain) {
        String[] includes = new String[1];
        includes[0] = path;

        String field = plain ? path : path + "." + aggregationTerm;

        return AggregationBuilders
                .terms(name)
                .size(maxAggSize)
                .field(field)
                .setMetadata(genMetaData(title));
    }

    static FilterAggregationBuilder genMustMatchAggregation(String path, String name, String title, boolean match,
                                                               List values) {
        QueryBuilder query;
        if (match) {
            query = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(path + ".id.raw", values));
        } else {
            query = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(path + ".id.raw", values));
        }

        return AggregationBuilders
                .filter(name, query)
                .setMetadata(genMetaData(title, match, "mustMatch"));
    }

    static FilterAggregationBuilder genMustNotMatchAggregation(String path, String name, String title, boolean match,
                                                         List values) {
        QueryBuilder query;
        if (match) {
            query = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(path + ".id.raw", values));
        } else {
            query = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery(path + ".id.raw", values));
        }

        return AggregationBuilders
                .filter(name, query)
                .setMetadata(genMetaData(title, match, "mustNotMatch"));
    }

    private static Map<String, Object> genMetaData(String title) {
        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("title", title);
        return metaData;
    }

    private static Map<String, Object> genMetaData(String title, Double from, Double to) {
        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("title", title);
        metaData.put("from", from);
        metaData.put("to", to);
        return metaData;
    }

    private static Map<String, Object> genMetaData(String title, Boolean match, String type) {
        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("title", title);
        metaData.put("match", match);
        metaData.put("type", type);
        return metaData;
    }

}
