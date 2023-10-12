package io.piveau.metrics.cache.dqv;

import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.dcatap.TripleStore;
import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.piveau.metrics.cache.dqv.sparql.util.PercentageMath;
import io.piveau.vocabularies.vocabulary.DQV;
import io.piveau.vocabularies.vocabulary.PV;
import io.piveau.vocabularies.vocabulary.SHACL;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.OA;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DatasetMetrics {
    private static final  String NO_TITLE = "No title available";
    private static final  String COUNT_STRING = "count";

    private Resource dataset;

    private final Model model;
    private final ArrayList<Resource> distributions = new ArrayList<>();
    private final TripleStore tripleStore;
    private final String lang;

    // logger
    private static final Logger LOG = LoggerFactory.getLogger(DatasetMetrics.class);

    public DatasetMetrics(Model model, TripleStore tripleStore, String lang) {
        this.model = model;
        this.tripleStore = tripleStore;
        this.lang = lang;

        ResIterator resIterator = model.listResourcesWithProperty(DQV.hasQualityMeasurement);
        while (resIterator.hasNext()) {
            Resource resource = resIterator.next();
            if (resource.isURIResource()) {
                // check if resource is dataset or distribution
                if (DCATAPUriSchema.isDatasetUriRef(resource.getURI())) {
                    dataset = resource;
                } else if (DCATAPUriSchema.isDistributionUriRef(resource.getURI())) {
                    distributions.add(resource);
                }
            }
        }
    }

    /**
     * Extracts dataset and distribution resources and returns a JSON Object with all metrics data for a dataset
     */
    public void getDatasetMetrics(Handler<AsyncResult<JsonObject>> resultHandler) {
        createDatasetJson(resultHandler);
    }

    /**
     * Generates a JSON Object from the metrics data for a dataset and its distributions
     */
    private void createDatasetJson(Handler<AsyncResult<JsonObject>> resultHandler) {
        if (dataset == null) {
            resultHandler.handle(Future.failedFuture("No dataset found"));
        } else {
            JsonObject result = new JsonObject();

            result
                    .put("success", true)
                    .put("result", new JsonObject()
                                    .put(COUNT_STRING, 1)
                                    .put("results", new JsonArray()
                                                    .add(new JsonObject()
                                                                    .put("info", new JsonObject()
                                                                                    .put("dataset-id", dataset.getLocalName())
                                                                                    .put("dataset-uri", dataset.getURI())
//                                                        .put("distributions", distributionDetails)
                                                                                    .put("score", getScore())
                                                                    )
                                                                    .put("accessibility", new JsonArray()
                                                                            .add(calculateMetricsPercentageForDistribution(PV.downloadUrlAvailability))
                                                                            .add(generateStatusCodePercentage(PV.accessUrlStatusCode))
                                                                            .add(generateStatusCodePercentage(PV.downloadUrlStatusCode))
                                                                    )
                                                                    .put("reusability", new JsonArray()
                                                                            .add(extractMetric(dataset, PV.accessRightsAvailability))
                                                                            .add(calculateMetricsPercentageForDistribution(PV.licenceAvailability))
                                                                            .add(extractMetric(dataset, PV.accessRightsVocabularyAlignment))
                                                                            .add(extractMetric(dataset, PV.contactPointAvailability))
                                                                            .add(extractMetric(dataset, PV.publisherAvailability))
                                                                    )
                                                                    .put("contextuality", new JsonArray()
                                                                            .add(calculateMetricsPercentageForDistribution(PV.byteSizeAvailability))
                                                                            .add(calculateMetricsPercentageForDistribution(PV.rightsAvailability))
                                                                            // dateModifiedAvailability and dateIssuedAvailability need to be be added for both, the dataset itself and its distributions
                                                                            .add(new JsonObject().put("dataset", new JsonArray()
                                                                                            .add(extractMetric(dataset, PV.dateModifiedAvailability))
                                                                                            .add(extractMetric(dataset, PV.dateIssuedAvailability))
                                                                                    )
                                                                            )
                                                                            .add(new JsonObject().put("distributions", new JsonArray()
                                                                                            .add(calculateMetricsPercentageForDistribution(PV.dateModifiedAvailability))
                                                                                            .add(calculateMetricsPercentageForDistribution(PV.dateIssuedAvailability))
                                                                                    )
                                                                            )
                                                                    )
                                                                    .put("findability", new JsonArray()
                                                                            .add(extractMetric(dataset, PV.keywordAvailability))
                                                                            .add(extractMetric(dataset, PV.categoryAvailability))
                                                                            .add(extractMetric(dataset, PV.spatialAvailability))
                                                                            .add(extractMetric(dataset, PV.temporalAvailability))
                                                                    )
                                                                    .put("interoperability", new JsonArray()
                                                                            .add(extractMetric(dataset, PV.dcatApCompliance))
                                                                            .add(calculateMetricsPercentageForDistribution(PV.formatAvailability))
                                                                            .add(calculateMetricsPercentageForDistribution(PV.mediaTypeAvailability))
                                                                            .add(calculateMetricsPercentageForDistribution(PV.formatMediaTypeVocabularyAlignment))
                                                                    )
                                                    )
                                    )
                    );
//            }).onSuccess(v -> resultHandler.handle(Future.succeededFuture(result))
//            ).onFailure(cause -> resultHandler.handle(Future.failedFuture(cause)));
            resultHandler.handle(Future.succeededFuture(result));
        }
    }

    /**
     * Extracts distribution resources and returns a JSON Object with all distributions and their metrics for one dataset
     */
    public void getDistributionMetricsPerDataset(Handler<AsyncResult<JsonObject>> resultHandler) {
        createDistributionsJson(resultHandler);
    }

    /**
     * Generates a JSON Object from the metrics data for a dataset and its distributions
     */
    private void createDistributionsJson(Handler<AsyncResult<JsonObject>> resultHandler) {
        JsonArray distributionData = new JsonArray();
        List<Future<Void>> futures = new ArrayList<>();
        distributions.forEach(distribution ->
                futures.add(Future.future(promise ->
                        getDistributionTitle(distribution.getURI(), lang, ar -> {
                            if (ar.succeeded()) {
                                createDistributionInfo(distribution, distributionData, ar.result());
                            } else {
                                createDistributionInfo(distribution, distributionData, NO_TITLE);
                            }
                            promise.complete();
                        })
                ))
        );

        CompositeFuture.all(new ArrayList<>(futures)).onComplete(json ->
                resultHandler.handle(Future.succeededFuture(new JsonObject()
                        .put("success", true)
                        .put("result", new JsonObject()
                                .put(COUNT_STRING, distributions.size())
                                .put("results", distributionData)
                        ))
                ));
    }

    /**
     * Looks for a certain metric connected to a resource in the model and returns a JSON Object containing the metric name and its value
     *
     * @param resource Resource to extract the data from
     * @param metric   Metric to search for
     */
    private JsonObject extractMetric(Resource resource, Resource metric) {
        JsonObject output = new JsonObject().put(metric.getLocalName(), new JsonObject());
        StmtIterator computedOn = model.listStatements(null, DQV.computedOn, resource);
        while (computedOn.hasNext()) {
            Statement computedOnStatement = computedOn.nextStatement();
            StmtIterator metricIterator = model.listStatements(computedOnStatement.getSubject(), DQV.isMeasurementOf, metric);
            while (metricIterator.hasNext()) {
                Statement measurementOfStatement = metricIterator.nextStatement();
                Statement value = model.getProperty(measurementOfStatement.getSubject(), DQV.value);
                if (value != null) {
                    if (metric.equals(PV.accessUrlStatusCode) || metric.equals(PV.downloadUrlStatusCode)) {
                        output.put(metric.getLocalName(), value.getObject().asLiteral().getInt());
                    } else {
                        output.put(metric.getLocalName(), value.getObject().asLiteral().getBoolean());
                    }
                }
            }
        }
        return output;
    }

    /**
     * Calculates the yes and no percentages for one metric over several distributions
     *
     * @param metric Metric for which to calculate the percentages
     */
    private JsonObject calculateMetricsPercentageForDistribution(Resource metric) {
        if (!distributions.isEmpty()) {
            int yes = 0;
            int no = 0;
            for (Resource distribution : distributions) {
                JsonObject extracted = extractMetric(distribution, metric);
                // check for value of metric and convert true to yes and false to no
                if (extracted.getValue(metric.getLocalName()) instanceof Boolean &&
                        Boolean.TRUE.equals(extracted.getBoolean(metric.getLocalName(), false))
                )
                    yes++;
                else
                    no++;
            }
            JsonObject input = new JsonObject().put("yes", yes).put("no", no);
            Double yesPercentage = PercentageMath.calculateYesPercentage(input);
            return new JsonObject().put(metric.getLocalName(), PercentageMath.getYesNoPercentage(yesPercentage));
        } else {
            return new JsonObject().put(metric.getLocalName(), new JsonArray());
        }
    }

    /**
     * Get the score for a dataset
     */
    private int getScore() {
        int score = 0;
        StmtIterator computedOn = model.listStatements(null, DQV.computedOn, dataset);
        while (computedOn.hasNext()) {
            Statement computedOnStatement = computedOn.nextStatement();
            StmtIterator st = model.listStatements(computedOnStatement.getSubject(), DQV.isMeasurementOf, PV.scoring);
            while (st.hasNext()) {
                Statement measurementOfStatement = st.nextStatement();
                Statement x = measurementOfStatement.getSubject().getProperty(DQV.value);
                if (x != null) {
                    score = x.getObject().asLiteral().getInt();
                }
            }
        }
        return score;
    }

    /**
     * Generates the percentages for each status code of an access or download URL
     *
     * @param metric Metric for which to calculate the percentages
     */
    private JsonObject generateStatusCodePercentage(Resource metric) {
        ArrayList<Integer> allStatusCodes = new ArrayList<>();
        StmtIterator it = model.listStatements(null, DQV.isMeasurementOf, metric);
        while (it.hasNext()) {
            Statement statement = it.nextStatement();
            Statement stm = statement.getSubject().getProperty(DQV.value);
            if (stm != null) {
                allStatusCodes.add(stm.getObject().asLiteral().getInt());
            }
        }

        if (!allStatusCodes.isEmpty()) {
            LinkedHashSet<Integer> statusCodeNames = new LinkedHashSet<>(allStatusCodes);
            JsonObject statusCodeFrequency = new JsonObject();

            for (Integer statusCodeName : statusCodeNames) {
                statusCodeFrequency.put(statusCodeName.toString(), Collections.frequency(allStatusCodes, statusCodeName));
            }

            int sum = statusCodeFrequency.fieldNames().stream().mapToInt(statusCodeFrequency::getInteger).sum();
            Map<String, Double> statusCodes = new HashMap<>();
            statusCodeFrequency.fieldNames().forEach(name -> statusCodes.put(name, statusCodeFrequency.getDouble(name) / sum * 100));

            return new JsonObject().put(metric.getLocalName(), new StatusCodes(PercentageMath.roundMap(statusCodes)).toJsonArray());
        } else {
            return new JsonObject().put(metric.getLocalName(), new JsonArray());
        }
    }

    private JsonObject extractDistributionStatusCode(Resource distribution, Resource metric) {
        JsonObject output = new JsonObject().put(metric.getLocalName(), new JsonObject());
        StmtIterator distributionsWithQualityMeasurement = model.listStatements(distribution, DQV.hasQualityMeasurement, (RDFNode) null);
        while (distributionsWithQualityMeasurement.hasNext()) {
            Statement statement = distributionsWithQualityMeasurement.nextStatement();
            StmtIterator valueStatements = model.listStatements(statement.getObject().asResource(), DQV.isMeasurementOf, metric);
            while (valueStatements.hasNext()) {
                Statement valueStatement = valueStatements.nextStatement();
                Statement value = valueStatement.getSubject().getProperty(DQV.value);
                if (value != null) {
                    output.put(metric.getLocalName(), value.getObject().asLiteral().getInt());
                }
            }
        }
        return output;
    }

    private JsonObject extractValidationResults(Resource distribution) {
        JsonObject output = new JsonObject();
        StmtIterator distributionsWithQualityMeasurement = model.listStatements(distribution, DQV.hasQualityAnnotation, (RDFNode) null);
        int count = 0;
        LOG.info("Extracting Validation Results for: {}", distribution);
        while (distributionsWithQualityMeasurement.hasNext()) {
            Statement statement = distributionsWithQualityMeasurement.nextStatement();

            String errorsString = "errors";
            String warningsString = "warnings";
            String infosString = "infos";
            output.put(errorsString, output.getJsonObject(errorsString, new JsonObject().put(COUNT_STRING, 0)));
            output.put(warningsString, output.getJsonObject(warningsString, new JsonObject().put(COUNT_STRING, 0)));
            output.put(infosString, output.getJsonObject(infosString, new JsonObject().put(COUNT_STRING, 0)));

            StmtIterator bodyStatements = model.listStatements(statement.getObject().asResource(), OA.hasBody, (RDFNode) null);
            while (bodyStatements.hasNext()) {
                Statement bodyStatement = bodyStatements.nextStatement();

                Resource body = bodyStatement.getObject().asResource();

                Statement type = model.getProperty(body, RDF.type);



                //switch case for different types of validation results: CsvIndicator, CsvError, CsvWarning, CsvInfo


                if (type != null) { //type might be null if we have this star rating
                    switch (type.getObject().asResource().getLocalName()) {
                        case "CsvIndicator" -> {
                            // indicator is the meta object for the validation result
                            output.put("passed", model.getProperty(body, PV.passed).getObject().asLiteral().getBoolean());
                            output.put("rowCount", model.getProperty(body, PV.rowNumber).getObject().asLiteral().getLong());
                            if (model.getProperty(body, RDFS.comment) != null) {
                                try {
                                    LOG.info("Adding limit: {}", model.getProperty(body, RDFS.comment).getObject().asLiteral().getLong());
                                    output.put("limit", model.getProperty(body, RDFS.comment).getObject().asLiteral().getInt());
                                } catch (Exception e) {
                                    LOG.error("Could not get comment for indicator: {}|| {}", body, model.getProperty(body, PV.columnNumber).getObject().asLiteral().getString());
                                }

                            } else {

                                LOG.info("No limit");
                            }

                        }
                        case "CsvError" -> {
                            count += 1;
                            JsonObject errors = output.getJsonObject(errorsString, new JsonObject());

                            createCsvIndicatorItem(body, model, errors);
                            output.put(errorsString, errors);
                        }
                        case "CsvWarning" -> {
                            count+=1;
                            JsonObject warnings = output.getJsonObject(warningsString, new JsonObject());
                            createCsvIndicatorItem(body, model, warnings);
                            output.put(warningsString, warnings);
                        }
                        case "CsvInfo" -> {
                            count+=1;
                            JsonObject infos = output.getJsonObject(infosString, new JsonObject());
                            createCsvIndicatorItem(body, model, infos);
                            output.put(infosString, infos);
                        }
                        default -> LOG.info("Unknown type: {}", type);
                    }
                }


            }

        }
        output.put("itemCount", count);
        return output;
    }

    private void createCsvIndicatorItem(Resource body, Model model, JsonObject entries) {
        LOG.debug("Create CSV Indicator");
        entries.put(COUNT_STRING, entries.getInteger(COUNT_STRING, 0) + 1);
        JsonObject infoItem = new JsonObject();

        Resource indicator = model.getResource(body.getURI());
        model.listStatements(indicator, null, (RDFNode) null).forEachRemaining(s -> LOG.info("Things in indicator: {}", s));
        // column
        if (model.getProperty(body, PV.columnNumber) != null && model.getProperty(body, PV.columnNumber).getObject().isLiteral()) {
            infoItem.put("column", model.getProperty(body, PV.columnNumber).getObject().asLiteral().getInt());

        }
        // row
        if (model.getProperty(body, PV.rowNumber) != null && model.getProperty(body, PV.rowNumber).getObject().isLiteral()) {
            infoItem.put("row", model.getProperty(body, PV.rowNumber).getObject().asLiteral().getInt());
        }

        //title
        if (model.getProperty(body, DC_11.title) != null && model.getProperty(body, DC_11.title).getObject().isLiteral()) {
            infoItem.put("title", model.getProperty(body, DC_11.title).getObject().asLiteral().getString());
        }

        //message
        if (model.getProperty(body, SHACL.resultMessage) != null && model.getProperty(body, SHACL.resultMessage).getObject().isLiteral()) {
            infoItem.put("message", model.getProperty(body, SHACL.resultMessage).getObject().asLiteral().getString());

        }
        //indicator
        if (model.getProperty(body, DC_11.identifier) != null && model.getProperty(body, DC_11.identifier).getObject().isLiteral()) {
            infoItem.put("indicator", model.getProperty(body, DC_11.identifier).getObject().asLiteral().getString());
        }


        JsonArray infoItems = entries.getJsonArray("items", new JsonArray());
        infoItems.add(infoItem);
        entries.put("items", infoItems);
    }


    private void getDistributionTitle(String uri, String prefLang, Handler<AsyncResult<String>> asyncResultHandler) {
        String query = String.format(QueryCollection.getQuery("DistributionTitle"), uri, prefLang, prefLang);
        tripleStore.select(query).onSuccess(resultSet -> {
            if (resultSet.hasNext()) {
                QuerySolution solution = resultSet.next();
                if (solution.contains("preferred")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("preferred").getLexicalForm()));
                } else if (solution.contains("default")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("default").getLexicalForm()));
                } else if (solution.contains("empty")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("empty").getLexicalForm()));
                } else if (solution.contains("any")) {
                    asyncResultHandler.handle(Future.succeededFuture(solution.getLiteral("any").getLexicalForm()));
                } else {
                    asyncResultHandler.handle(Future.succeededFuture(NO_TITLE));
                }
            } else {
                asyncResultHandler.handle(Future.failedFuture(NO_TITLE));
            }
        }).onFailure(cause -> asyncResultHandler.handle(Future.failedFuture(cause)));
    }

    private void createDistributionInfo(Resource distribution, JsonArray distributionData, String distributionTitle) {
        distributionData.add(new JsonArray()
                .add(new JsonObject()
                        .put("info", new JsonObject()
                                .put("distribution-id", DCATAPUriSchema.parseUriRef(distribution.getURI()).getId())
                                .put("distribution-uri", distribution.getURI())
                                .put("distribution-title", distributionTitle)
                        )
                        .put("accessibility", new JsonArray()
                                .add(extractMetric(distribution, PV.downloadUrlAvailability))
                                .add(extractDistributionStatusCode(distribution, PV.accessUrlStatusCode))
                                .add(extractDistributionStatusCode(distribution, PV.downloadUrlStatusCode))
                        )
                        .put("reusability", new JsonArray()
                                .add(extractMetric(distribution, PV.licenceAvailability))
                        )
                        .put("contextuality", new JsonArray()
                                .add(extractMetric(distribution, PV.byteSizeAvailability))
                                .add(extractMetric(distribution, PV.rightsAvailability))
                                .add(extractMetric(distribution, PV.dateModifiedAvailability))
                                .add(extractMetric(distribution, PV.dateIssuedAvailability))
                        )
                        .put("interoperability", new JsonArray()
                                .add(extractMetric(distribution, PV.formatAvailability))
                                .add(extractMetric(distribution, PV.mediaTypeAvailability))
                                .add(extractMetric(distribution, PV.formatMediaTypeVocabularyAlignment))
                        )
                        .put("validation", extractValidationResults(distribution))
                )
        );
    }

}