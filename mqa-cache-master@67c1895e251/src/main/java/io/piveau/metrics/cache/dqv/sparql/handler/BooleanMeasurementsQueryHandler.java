package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.metrics.cache.dqv.sparql.util.SparqlHelper;
import io.piveau.metrics.cache.persistence.DocumentScope;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;

public class BooleanMeasurementsQueryHandler extends QueryHandler {

    private BooleanMeasurementsQueryHandler(String queryName, Object ... parameters) {
        super(queryName, parameters);
    }

    public static QueryHandler create(DocumentScope scope, Resource metric, String id) {
        boolean datasetMetric = SparqlHelper.isDatasetMetric(metric);
        String queryName;
        switch (scope) {
            case CATALOGUE:
                queryName = datasetMetric ? "CatalogueDatasetMeasurements" : "CatalogueDistributionMeasurements";
                DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(id);
                return new BooleanMeasurementsQueryHandler(queryName, uriRef.getCatalogueUriRef(), metric.getURI());
            case COUNTRY:
                queryName = datasetMetric ? "CountryDatasetMeasurements" : "CountryDistributionMeasurements";
                return new BooleanMeasurementsQueryHandler(queryName, SparqlHelper.getCountries().get(id), metric.getURI());
            case GLOBAL:
                queryName = datasetMetric ? "AllDatasetMeasurements" : "AllDistributionMeasurements";
                return new BooleanMeasurementsQueryHandler(queryName, metric.getURI());
            case SCORE:
            default:
                throw new IllegalArgumentException("Unknown scope '" + scope + "'");
        }
    }

    @Override
    public void handle(ResultSet resultSet) {
        resultSet.forEachRemaining(solution -> {
            String key = solution.getLiteral("value").getBoolean() ? "yes" : "no";
            result.put(key, solution.getLiteral("count").getInt());
        });
    }

}
