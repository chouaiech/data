package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.search.SearchService;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchHandler extends ContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SearchHandler.class);

    SearchService searchService;

    public SearchHandler(Vertx vertx, String address) {
        searchService = SearchService.createProxy(vertx, address);
    }

    JsonObject paramsToQuery(MultiMap params) {
        JsonObject query = new JsonObject();

        String q = params.get("q");
        params.remove("q");
        if(q != null)
            query.put("q", q);

        int page;
        try {
            page = Integer.parseInt(params.get("page"));
        } catch (NumberFormatException e) {
            page = 0;
        }
        params.remove("page");

        int limit;
        try {
            limit = Integer.parseInt(params.get("limit"));
        } catch (NumberFormatException e) {
            limit = 10;
        }
        params.remove("limit");

        query.put("from", page*limit);
        query.put("size", limit);

        String scroll = params.get("scroll");
        params.remove("scroll");
        if(scroll != null)
            query.put("scroll", scroll);

        String showScore = params.get("showScore");
        params.remove("showScore");
        if(showScore != null)
            query.put("showScore", showScore);

        String aggregation = params.get("aggregation");
        params.remove("aggregation");
        if(aggregation != null)
            query.put("aggregation", aggregation);

        String onlyIds = params.get("onlyIds");
        params.remove("onlyIds");
        if(onlyIds != null)
            query.put("onlyIds", onlyIds);

        String globalAggregation = params.get("globalAggregation");
        params.remove("globalAggregation");
        if(globalAggregation != null)
            query.put("globalAggregation", globalAggregation);

        String aggregationAllFields = params.get("aggregationAllFields");
        params.remove("aggregationAllFields");
        if(aggregationAllFields != null)
            query.put("aggregationAllFields", aggregationAllFields);

        List<String> aggregationFields = params.getAll("aggregationFields");
        params.remove("aggregationFields");
        if(!aggregationFields.isEmpty())
            query.put("aggregationFields", new JsonArray(checkCommaDelimited(aggregationFields)));

        String facetOperator = params.get("facetOperator");
        params.remove("facetOperator");
        if(facetOperator != null)
            query.put("facetOperator", facetOperator);

        String facetGroupOperator = params.get("facetGroupOperator");
        params.remove("facetGroupOperator");
        if(facetGroupOperator != null)
            query.put("facetGroupOperator", facetGroupOperator);

        String filterDistributions = params.get("filterDistributions");
        params.remove("filterDistributions");
        if(filterDistributions != null)
            query.put("filterDistributions", filterDistributions);

        List<String> sort = params.getAll("sort");
        params.remove("sort");
        if(!sort.isEmpty()) {
            sort = checkCommaDelimited(sort);
            for (int i = 0; i < sort.size(); i++) {
                sort.set(i, sort.get(i).replaceAll(" ", "+"));
            }
            query.put("sort", new JsonArray(sort));
        }

        String autocomplete = params.get("autocomplete");
        params.remove("autocomplete");
        if(autocomplete != null)
            query.put("autocomplete", autocomplete);

        String filter = params.get("filter");
        params.remove("filter");
        if(filter != null) {
            query.put("filter", filter);

            String facets = params.get("facets");
            params.remove("facets");
            if (facets != null) {
                JsonObject facetsJson;
                try {
                    facetsJson = new JsonObject(facets);
                } catch (DecodeException e) {
                    facetsJson = new JsonObject();
                }

                if(!facetsJson.isEmpty())
                    query.put("facets", facetsJson);
            }
        }

        List<String> vocabulary = params.getAll("vocabulary");
        params.remove("vocabulary");
        if(!vocabulary.isEmpty())
            query.put("vocabulary", new JsonArray(checkCommaDelimited(vocabulary)));

        List<String> fields = params.getAll("fields");
        params.remove("fields");
        if(!fields.isEmpty())
            query.put("fields", new JsonArray(checkCommaDelimited(fields)));

        List<String> includes = params.getAll("includes");
        params.remove("includes");
        if(!includes.isEmpty())
            query.put("includes", new JsonArray(checkCommaDelimited(includes)));

        List<String> exists = params.getAll("exists");
        params.remove("exists");
        if(!exists.isEmpty())
            query.put("exists", new JsonArray(checkCommaDelimited(exists)));

        String minDate = params.get("minDate");
        params.remove("minDate");
        String maxDate = params.get("maxDate");
        params.remove("maxDate");
        String bboxMinLon = params.get("bboxMinLon");
        params.remove("bboxMinLon");
        String bboxMaxLon = params.get("bboxMaxLon");
        params.remove("bboxMaxLon");
        String bboxMaxLat = params.get("bboxMaxLat");
        params.remove("bboxMaxLat");
        String bboxMinLat = params.get("bboxMinLat");
        params.remove("bboxMinLat");
        String minScoring = params.get("minScoring");
        params.remove("minScoring");
        String maxScoring = params.get("maxScoring");
        params.remove("maxScoring");
        String countryData = params.get("countryData");
        params.remove("countryData");
        String dataServices = params.get("dataServices");
        params.remove("dataServices");

        if(filter != null && !filter.isEmpty() && !filter.equals("autocomplete")) {
            JsonObject searchParams = new JsonObject();
            if(minDate != null) {
                searchParams.put("minDate", minDate);
            }
            if(maxDate != null) {
                searchParams.put("maxDate", maxDate);
            }
            if(bboxMinLon != null && bboxMaxLon != null && bboxMaxLat != null && bboxMinLat != null) {
                JsonObject boundingBox = new JsonObject();
                boundingBox.put("minLon", bboxMinLon);
                boundingBox.put("maxLon", bboxMaxLon);
                boundingBox.put("maxLat", bboxMaxLat);
                boundingBox.put("minLat", bboxMinLat);
                searchParams.put("boundingBox", boundingBox);
            }
            if(minScoring != null) {
                searchParams.put("minScoring", minScoring);
            }
            if(maxScoring != null) {
                searchParams.put("maxScoring", maxScoring);
            }
            if(countryData != null) {
                if(countryData.equals("true")) {
                    searchParams.put("countryData", true);
                } else if(countryData.equals("false")) {
                    searchParams.put("countryData", false);
                }
            }
            if(dataServices != null) {
                if(dataServices.equals("true")) {
                    searchParams.put("dataServices", true);
                } else if(dataServices.equals("false")) {
                    searchParams.put("dataServices", false);
                }
            }
            if(!searchParams.isEmpty())
                query.put("searchParams", searchParams);
        }

        for(Map.Entry<String, String> entry : params.entries()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.contains(".")) {
                String[] splitKey = key.split("\\.");
                JsonObject keyObject = query.getJsonObject(splitKey[0], new JsonObject());

                if (splitKey[0].equals("boost")) {
                    try {
                        String boostField = "";
                        for(int i = 1; i < splitKey.length; ++i) {
                            boostField = boostField.concat(splitKey[i]);
                            if (i != splitKey.length-1) {
                                boostField = boostField.concat(".");
                            }
                        }
                        keyObject.put(boostField, Float.valueOf(value));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                } else {
                    keyObject.put(splitKey[1], value);
                }

                query.put(splitKey[0], keyObject);
            }
        }

        return query;
    }

    public void searchGet(RoutingContext context) {
        LOG.debug("Search, remote address: {}", context.request().connection().remoteAddress());
        MultiMap params = context.request().params();
        JsonObject query = paramsToQuery(params);
        searchService.search(query.toString()).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void scrollGet(RoutingContext context) {
        MultiMap params = context.request().params();
        String scrollId = params.get("scrollId");
        searchService.scroll(scrollId).onComplete(ar -> handleContextLegacy(context, ar));
    }

    private List<String> checkCommaDelimited(List<String> input) {
        List<String> output;

        if(input.size() == 1)
            output =  Arrays.asList(input.get(0).split(","));
        else
            output = input;

        for (int i = 0; i < output.size(); i++) {
            output.set(i, output.get(i).trim());
        }

        return output;
    }
}
