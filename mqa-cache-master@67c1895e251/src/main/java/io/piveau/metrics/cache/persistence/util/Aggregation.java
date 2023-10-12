package io.piveau.metrics.cache.persistence.util;

import io.piveau.metrics.cache.dqv.sparql.util.PercentageMath;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.AggregateOptions;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Wrapper class for aggregating data from a mongodb and then averaging it
 */
public class Aggregation {

    private static final Logger log = LoggerFactory.getLogger(Aggregation.class);

    private String id;

    private final MongoClient dbClient;
    private final DocumentScope documentScope;
    private final JsonArray pipeline = new JsonArray();
    private final List<String> filter;

    /**
     * Shortcut constructor for Aggregations without id, e.g. List aggregations
     *
     * @param dbClient      the mongodb client
     * @param documentScope the mongodb collection the document is stored in
     * @param filter        a list of dimensions that should be present ion the document
     */
    public Aggregation(MongoClient dbClient, DocumentScope documentScope, List<String> filter) {
        this.dbClient = dbClient;
        this.filter = filter;
        this.documentScope = documentScope;
    }

    /**
     * Default constructor
     *
     * @param id            the document id
     * @param dbClient      the mongodb client
     * @param documentScope the mongodb collection the document is stored in
     * @param filter        a list of dimensions that should be present ion the document
     */
    public Aggregation(String id, MongoClient dbClient, DocumentScope documentScope, List<String> filter) {
        this.id = id;
        pipeline.add(new JsonObject().put("$match", new JsonObject().put("_id", id)));
        this.dbClient = dbClient;
        this.documentScope = documentScope;
        this.filter = filter;
    }

    /**
     * Construct a aggregation stage for the projection stage which will
     * project the newest values for all dimensions included in this.filter
     *
     * @return a MongoDb aggregation stage
     */
    private JsonObject getProjectStageForCurrent() {

        JsonObject dimensions = new JsonObject();

        dimensions.put("_id", 0);

        for (String item : filter) {
            switch (item) {
                case "findability":
                    addCurrentProjection(dimensions, "findability.score");
                    addCurrentProjection(dimensions, "findability.keywordAvailability");
                    addCurrentProjection(dimensions, "findability.categoryAvailability");
                    addCurrentProjection(dimensions, "findability.spatialAvailability");
                    addCurrentProjection(dimensions, "findability.temporalAvailability");
                    break;
                case "accessibility":
                    addCurrentProjection(dimensions, "accessibility.score");
                    addCurrentProjection(dimensions, "accessibility.accessUrlStatusCodes");
                    addCurrentProjection(dimensions, "accessibility.downloadUrlStatusCodes");
                    addCurrentProjection(dimensions, "accessibility.downloadUrlAvailability");
                    break;
                case "interoperability":
                    addCurrentProjection(dimensions, "interoperability.score");
                    addCurrentProjection(dimensions, "interoperability.formatAvailability");
                    addCurrentProjection(dimensions, "interoperability.mediaTypeAvailability");
                    addCurrentProjection(dimensions, "interoperability.formatMediaTypeAlignment");
                    addCurrentProjection(dimensions, "interoperability.formatMediaTypeNonProprietary");
                    addCurrentProjection(dimensions, "interoperability.formatMediaTypeMachineReadable");
                    addCurrentProjection(dimensions, "interoperability.dcatApCompliance");
                    break;
                case "reusability":
                    addCurrentProjection(dimensions, "reusability.score");
                    addCurrentProjection(dimensions, "reusability.licenceAvailability");
                    addCurrentProjection(dimensions, "reusability.licenceAlignment");
                    addCurrentProjection(dimensions, "reusability.accessRightsAvailability");
                    addCurrentProjection(dimensions, "reusability.accessRightsAlignment");
                    addCurrentProjection(dimensions, "reusability.contactPointAvailability");
                    addCurrentProjection(dimensions, "reusability.publisherAvailability");
                    break;
                case "contextuality":
                    addCurrentProjection(dimensions, "contextuality.score");
                    addCurrentProjection(dimensions, "contextuality.rightsAvailability");
                    addCurrentProjection(dimensions, "contextuality.byteSizeAvailability");
                    addCurrentProjection(dimensions, "contextuality.dateIssuedAvailability");
                    addCurrentProjection(dimensions, "contextuality.dateModifiedAvailability");
                    break;
                case "score":
                    addCurrentProjection(dimensions, item);

                    //Add dimension scores, these should appear, when filtering for a specific dimension
                    //and also, when filtering for the score
                    // so we have to be careful to add it for each case we want to have it
                    // but not add it twice

                    if (!filter.contains("findability")) {
                        addCurrentProjection(dimensions, "findability.score");
                    }
                    if (!filter.contains("accessibility")) {
                        addCurrentProjection(dimensions, "accessibility.score");
                    }
                    if (!filter.contains("interoperability")) {
                        addCurrentProjection(dimensions, "interoperability.score");
                    }
                    if (!filter.contains("reusability")) {
                        addCurrentProjection(dimensions, "reusability.score");
                    }
                    if (!filter.contains("contextuality")) {
                        addCurrentProjection(dimensions, "contextuality.score");
                    }

                    break;
                case "info":
                    //A country does not have an ID object, so we have to add one manually
                    if (documentScope == DocumentScope.COUNTRY) {
                        dimensions.put("info.id", "$_id");
                    } else {
                        dimensions.put(item, 1);
                    }
                    break;
                default:
                    dimensions.put(item, 1);
            }

        }

        return new JsonObject().put("$project", dimensions);
    }


    /**
     * Adds one metric to the dimension for the current projection stage
     * that is, this added json object will be used to project the actual data into the result that will be returned from the db
     *
     * Specifically, that means that this projection will get the item from this metrics value-date-arry with the highest date.
     * This can be different than the date returned from other metrics. Also: the current date will not be regarded for this projection.
     *
     * @param dimensions     JsonObject containing all dimensions
     * @param dateValueArray a single metric, which is stored as an array of objects in the database
     */
    private void addCurrentProjection(JsonObject dimensions, String dateValueArray) {

        /*

        score: {
            $arrayElemAt: [{
                $map: {
                    input: {
                        $filter: {
                            input: "$score",
                            as: "item",
                            cond: {
                                $eq: ["$$item.date", {
                                    $max: "$score.date"
                                }]
                            }
                        }
                    },
                    as: "obj",
                    in: "$$obj.value"
                }
            }, 0]
        }
         */

        dimensions.put(dateValueArray, new JsonObject()
                .put("$arrayElemAt",
                        new JsonArray()
                                .add(
                                        new JsonObject().put("$map",
                                                new JsonObject()
                                                        .put("input",
                                                                new JsonObject().put("$filter",
                                                                        new JsonObject()
                                                                                .put("input", "$" + dateValueArray)
                                                                                .put("as", "item")
                                                                                .put("cond",
                                                                                        new JsonObject().put("$eq",
                                                                                                new JsonArray().add("$$item.date").add(
                                                                                                        new JsonObject().put("$max", "$" + dateValueArray + ".date"))))))
                                                        .put("as", "obj")
                                                        .put("in", "$$obj.value")))
                                .add(-1)));

    }


    /**
     * Construct a aggregation stage for the projection stage which will
     * project all values between startDate and endDate for all dimensions included in this.filter
     *
     *
     * @param startDate the first date included
     * @param endDate   the last date included
     * @return a MongoDb aggregation stage
     */
    private JsonObject getProjectStageForHistory(String startDate, String endDate) {

        JsonObject dimensions = new JsonObject();

        dimensions.put("_id", 0);

        for (String item : filter) {
            switch (item) {
                case "findability":
                    addHistoryProjection(dimensions, "findability.score", startDate, endDate);
                    addHistoryProjection(dimensions, "findability.keywordAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "findability.categoryAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "findability.spatialAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "findability.temporalAvailability", startDate, endDate);
                    break;
                case "accessibility":
                    addHistoryProjection(dimensions, "accessibility.score", startDate, endDate);
                    addHistoryProjection(dimensions, "accessibility.accessUrlStatusCodes", startDate, endDate);
                    addHistoryProjection(dimensions, "accessibility.downloadUrlStatusCodes", startDate, endDate);
                    addHistoryProjection(dimensions, "accessibility.downloadUrlAvailability", startDate, endDate);
                    break;
                case "interoperability":
                    addHistoryProjection(dimensions, "interoperability.score", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.formatAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.mediaTypeAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.formatMediaTypeAlignment", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.formatMediaTypeNonProprietary", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.formatMediaTypeMachineReadable", startDate, endDate);
                    addHistoryProjection(dimensions, "interoperability.dcatApCompliance", startDate, endDate);
                    break;
                case "reusability":
                    addHistoryProjection(dimensions, "reusability.score", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.licenceAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.licenceAlignment", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.accessRightsAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.accessRightsAlignment", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.contactPointAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "reusability.publisherAvailability", startDate, endDate);
                    break;
                case "contextuality":
                    addHistoryProjection(dimensions, "contextuality.score", startDate, endDate);
                    addHistoryProjection(dimensions, "contextuality.rightsAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "contextuality.byteSizeAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "contextuality.dateIssuedAvailability", startDate, endDate);
                    addHistoryProjection(dimensions, "contextuality.dateModifiedAvailability", startDate, endDate);
                    break;
                case "score":
                    addHistoryProjection(dimensions, item, startDate, endDate);
                    //Add dimension scores, these should appear, when filtering for a specific dimension
                    //and also, when filtering for the score
                    // so we have to be careful to add it for each case we want to have it
                    // but not add it twice

                    if (!filter.contains("findability")) {
                        addHistoryProjection(dimensions, "findability.score", startDate, endDate);
                    }
                    if (!filter.contains("accessibility")) {
                        addHistoryProjection(dimensions, "accessibility.score", startDate, endDate);
                    }
                    if (!filter.contains("interoperability")) {
                        addHistoryProjection(dimensions, "interoperability.score", startDate, endDate);
                    }
                    if (!filter.contains("reusability")) {
                        addHistoryProjection(dimensions, "reusability.score", startDate, endDate);
                    }
                    if (!filter.contains("contextuality")) {
                        addHistoryProjection(dimensions, "contextuality.score", startDate, endDate);
                    }
                    break;
                case "info":
                    if (documentScope == DocumentScope.COUNTRY) {
                        //A country does not have an ID object, so we have to add one manually
                        dimensions.put("info.id", "$_id");
                    } else {
                        dimensions.put(item, 1);
                    }
                    break;
                default:
                    dimensions.put(item, 1);
            }

        }

        return new JsonObject().put("$project", dimensions);
    }


    /**
     * Adds one metric to the dimension for the historic projection stage
     * that is, this added json object will be used to project the actual data into the result that will be returned from the db
     *
     *
     * @param dimensions     JsonObject containing all dimensions
     * @param dateValueArray a single metric, which is stored as an array of objects in the database
     * @param startDate      the first date included
     * @param endDate        the last date included
     */
    private void addHistoryProjection(JsonObject dimensions, String dateValueArray, String startDate, String endDate) {
/*
        "score": {
            $filter: {
                input: "$score",
                as: "item",
                cond: {
                    $and: [{$lte: [{$toDate: "$$item.date"}, {$toDate: "2020-05-01"}]}, {
                        $gte: [{$toDate: "$$item.date"}, {$toDate: "2020-04-01"}]
                    }]
                }
            }

        }
 */
        dimensions.put(dateValueArray, new JsonObject()
                .put("$filter",
                        new JsonObject()
                                .put("input", "$" + dateValueArray)
                                .put("as", "item")
                                .put("cond",
                                        new JsonObject().put("$and",
                                                new JsonArray()
                                                        .add(new JsonObject().put("$gte", new JsonArray()
                                                                .add(new JsonObject().put("$toDate", "$$item.date"))
                                                                .add(new JsonObject().put("$toDate", startDate))))
                                                        .add(new JsonObject().put("$lte", new JsonArray()
                                                                .add(new JsonObject().put("$toDate", "$$item.date"))
                                                                .add(new JsonObject().put("$toDate", endDate))))
                                        )
                                )
                )
        );


    }


    /**
     * Finalize pipeline and get the data from the database
     *
     * @param resultHandler handles the final result object
     */
    public void aggregateCurrent(Handler<AsyncResult<JsonObject>> resultHandler) {

        pipeline.add(getProjectStageForCurrent());
        aggregate(handler -> {
            if (handler.succeeded()) {
                JsonArray results = handler.result();
                JsonObject returnObject = new JsonObject()
                        .put("success", true)
                        .put("result", new JsonObject().put("count", results.size()).put("results", results));
                resultHandler.handle(Future.succeededFuture(returnObject));
            } else {
                resultHandler.handle(Future.failedFuture(handler.cause()));
            }
        });

    }

    /**
     * Finalize pipeline and get the data from the database
     *
     * @param startDate     the first date included
     * @param endDate       the last date included
     * @param resolution    the resolution of the average one of: day, month, year
     * @param resultHandler handles the final result object
     */
    public void aggregateHistoric(String startDate, String endDate, String resolution, Handler<AsyncResult<JsonObject>> resultHandler) {

        pipeline.add(getProjectStageForHistory(startDate, endDate));
        aggregate(h -> {
            if (h.succeeded()) {
                // average each array of date/value/objects
                JsonArray results = h.result();
                for (Object doc : results) {
                    averageDocument((JsonObject) doc, resolution);
                }
                JsonObject returnObject = new JsonObject()
                        .put("success", true)
                        .put("result", new JsonObject().put("count", results.size()).put("results", results));
                resultHandler.handle(Future.succeededFuture(returnObject));
            } else {
                resultHandler.handle(Future.failedFuture(h.cause()));
            }
        });

    }

    /**
     * Wrapper for mongodb.aggregate. This method collects all results from the aggregate call and returns them
     * via the resultHandler
     *
     * @param resultHandler the resultHandler which is called, after all documents are collected
     */
    private void aggregate(Handler<AsyncResult<JsonArray>> resultHandler) {

        JsonArray results = new JsonArray();

        /* FIXME: there is an error in vert.x when aggregation over a collection with more than 20 Documents.
        So we have to set this manually. It should be fixed in vert.x 4.0.
        Remove this workaround, when we are using 4.0: https://github.com/vert-x3/vertx-mongo-client/issues/195
         */
        dbClient.aggregateWithOptions(documentScope.name(), pipeline, new AggregateOptions().setBatchSize(500))
                .exceptionHandler(e -> resultHandler.handle(Future.failedFuture(e)))
                .handler(results::add)
                .endHandler(v -> resultHandler.handle(Future.succeededFuture(results)));
    }


    /**
     * @param document the document
     * @param resolution the resolution of the average one of: day, month, year
     */
    private void averageDocument(JsonObject document, String resolution) {

        for (String dimension : document.fieldNames()) {
            if (dimension.equals("info")) {
                continue;
            }
            if (dimension.equals("score")) {
                HashMap<String, ArrayList<Double>> buckets = getScoreBuckets(resolution, document.getJsonArray(dimension));
                document.put(dimension, averageAndSortScore(buckets));
            } else {
                JsonObject dimensionObject = document.getJsonObject(dimension);


                for (String metric : dimensionObject.fieldNames()) {
                    if (dimensionObject.getJsonArray(metric) != null&&!metric.contains("score")) {

                            HashMap<String, ArrayList<JsonObject>> buckets = getBuckets(resolution, dimensionObject.getJsonArray(metric, new JsonArray()));
                            dimensionObject.put(metric, averageAndSort(buckets));

                    }
                }
            }

        }

    }


    /**
     * put the date-value-objects into buckets depending on date and resolution
     *
     * @param resolution the resolution of the average one of: day, month, year
     * @param items      the list of objects that should be put into buckets
     * @return the buckets
     */
    private HashMap<String, ArrayList<JsonObject>> getBuckets(String resolution, JsonArray items) {


        List<JsonObject> dates = new ArrayList<>();

        if (items != null) {
            items.forEach(item -> dates.add((JsonObject) item));
        }

        HashMap<String, ArrayList<JsonObject>> counter = new HashMap<>();

/*
                depending of resolution we calculate the average depending of dates as discussed in the daily
*/
        switch (resolution) {
            case "year":
                for (JsonObject entries : dates) {
                    String date = getYear(entries.getString("date"));
                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    upsert(counter, date, entries.getJsonArray("value"));
                }
                /*we remove all dates which are not years */
                counter.keySet().forEach(key -> {
                    if (key.length() > 4)
                        counter.remove(key);
                });
                break;

            case "day":
                for (JsonObject entries : dates) {

                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    upsert(counter, entries.getString("date"), entries.getJsonArray("value"));
                }
                break;

            case "month":
                for (JsonObject entries : dates) {
                    String date = getMonth(entries.getString("date"));
                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    upsert(counter, date, entries.getJsonArray("value"));
                }

                /*we delete all entries which are not months*/
                counter.keySet().forEach(key -> {
                    if (key.length() > 8)
                        counter.remove(key);
                });
                break;
            default:
                log.error("Unexpected value as resolution: " + resolution);

                throw new IllegalStateException("Unexpected value: " + resolution);
        }
        return counter;

    }


    /**
     * Put the date-score-objects into buckets depending on date and resolution
     *
     * @param resolution the resolution of the average one of: day, month, year
     * @param items      the list of scores that should be put into buckets
     * @return the buckets
     */
    private HashMap<String, ArrayList<Double>> getScoreBuckets(String resolution, JsonArray items) {


        List<JsonObject> dates = new ArrayList<>();

        items.forEach(item -> dates.add((JsonObject) item));


        HashMap<String, ArrayList<Double>> counter = new HashMap<>();

/*
                depending of resolution we calculate the average depending of dates as discussed in the daily
*/
        switch (resolution) {
            case "year":
                for (JsonObject entries : dates) {
                    String date = getYear(entries.getString("date"));
                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    upsert(counter, date, entries.getDouble("value"));
                }
                /*we remove all dates which are not years */
                counter.keySet().forEach(key -> {
                    if (key.length() > 4)
                        counter.remove(key);
                });
                break;

            case "day":
                for (JsonObject score : dates) {
                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    String date = score.getString("date");
                    upsert(counter, date, score.getDouble("value"));
                }
                break;

            case "month":
                for (JsonObject score : dates) {
                    String date = getMonth(score.getString("date"));
                    /*check if the date is already in the array if not create a new list with scores else copy and replace the existing one*/
                    upsert(counter, date, score.getDouble("value"));
                }
                /*we delete all entries which are not months*/
                counter.keySet().forEach(key -> {
                    if (key.length() > 8)
                        counter.remove(key);
                });
                break;

            default:
                log.error("Unexpected value as resolution: " + resolution);
                throw new IllegalStateException("Unexpected value: " + resolution);
        }

        return counter;
    }

    /**
     * Split the date string and only return the year part
     *
     * @param date a ISO 8601 calendar date string in format YYYY-MM-DD
     * @return the year from the date
     */
    private String getYear(String date) {
        String[] splitDate = date.split("[-]");
        return splitDate[0];
    }

    /**
     * Split the date string and return the year & month part
     *
     * @param date a ISO 8601 calendar date string in format YYYY-MM-DD
     * @return the year and month from the date in format YYYY-MM
     */
    private String getMonth(String date) {
        String[] splitDate = date.split("[-]");
        return splitDate[0] + "-" + splitDate[1];
    }


    /**
     * insert list of values into an array stored in a map entry, if the entry does not exist, a new entry & list will be created
     *
     * @param map   the map into which the values should be inserted
     * @param key   the key for the map entry
     * @param items the list of items to be inserted
     */
    private void upsert(Map<String, ArrayList<JsonObject>> map, String key, JsonArray items) {
        ArrayList<JsonObject> values = map.getOrDefault(key, new ArrayList<>());
        items.forEach(i -> values.add((JsonObject) i));
        map.put(key, values);
    }

    /**
     * insert a value into an array stored in a map entry, if the entry does not exist, a new entry & list will be created
     *
     * @param map   the map into which the value should be inserted
     * @param key   the key for the map entry
     * @param value the value to be inserted
     */
    private void upsert(Map<String, ArrayList<Double>> map, String key, Double value) {
        ArrayList<Double> values = map.getOrDefault(key, new ArrayList<>());
        values.add(value);
        map.put(key, values);
    }

    /**
     * @param scoreBuckets a map of scores put into date buckets
     * @return a sorted JsonArray of score averages
     */
    private JsonArray averageAndSortScore(HashMap<String, ArrayList<Double>> scoreBuckets) {

        /*in order to sort we add the keys/dates to a list */
        List<String> listToStore = new ArrayList<>(scoreBuckets.keySet());

        /*sort it*/
        Collections.sort(listToStore);

        /*now we build our final result which shows the averages and is sorted*/
        JsonArray finalResult = new JsonArray();
        listToStore.forEach(key -> {
            ArrayList<Double> list = scoreBuckets.get(key);
            double total = list.stream().mapToDouble(score -> score).sum();
            finalResult.add(new JsonObject().put(key, total / list.size()));
        });
        return finalResult;
    }

    /**
     *
     *
     * @param buckets a map of jsonObjects put into date buckets
     * @return a sorted JsonArray of averages with name and value
     */
    private JsonArray averageAndSort(HashMap<String, ArrayList<JsonObject>> buckets) {

        /*in order to sort we add the keys/dates to a list */
        List<String> listToStore = new ArrayList<>(buckets.keySet());

        /*sort it*/
        Collections.sort(listToStore);

        /*now we build our final result which shows the averages and is sorted*/
        JsonArray finalResult = new JsonArray();
        listToStore.forEach(dateBucketKey -> {

            Map<String, ArrayList<Double>> listMap = new HashMap<>();
            buckets.get(dateBucketKey).forEach(entry -> upsert(listMap, entry.getString("name"), entry.getDouble("percentage")));

            Map<String, Double> avgMap = new HashMap<>();
            listMap.forEach((key1, value) -> avgMap.put(key1, value.stream().mapToDouble(d -> d).average().orElseThrow()));
            if (avgMap.size() > 0) PercentageMath.roundMap(avgMap);
            
            JsonArray jsonArray = new JsonArray();
            avgMap.forEach((k, v) -> jsonArray.add(new JsonObject().put("name", k).put("percentage", v)));
            finalResult.add(new JsonObject().put(dateBucketKey, jsonArray));

        });
        return finalResult;
    }
}
