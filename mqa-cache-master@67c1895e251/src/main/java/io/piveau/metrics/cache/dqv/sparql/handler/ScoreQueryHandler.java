package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.metrics.cache.dqv.sparql.util.SparqlHelper;
import io.piveau.metrics.cache.persistence.DocumentScope;
import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreQueryHandler extends QueryHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ScoreQueryHandler(String queryName, Object ... parameters) {
        super(queryName, parameters);
    }

    public static QueryHandler create(String id, DocumentScope scope, String scoringUriRef) {
        switch (scope) {
            case CATALOGUE:
                DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(id);
                return new ScoreQueryHandler("CatalogueScoreMeasurements", uriRef.getCatalogueUriRef(), scoringUriRef);
            case COUNTRY:
                return new ScoreQueryHandler("CountryScoreMeasurements", SparqlHelper.getCountries().get(id), scoringUriRef);
            case GLOBAL:
                return new ScoreQueryHandler("AllScoreMeasurements", scoringUriRef);
            case SCORE:
            default:
                throw new IllegalArgumentException("Unknown scope '" + scope + "'");
        }
    }

    @Override
    public void handle(ResultSet resultSet) {
        resultSet.forEachRemaining(solution -> {
            if (solution.contains("averageScore")) {
                result.put("averageScore", solution.getLiteral("averageScore").getDouble());
            } else {
                log.warn("No score in solution: {}", solution.toString());
            }
        });
    }

}
