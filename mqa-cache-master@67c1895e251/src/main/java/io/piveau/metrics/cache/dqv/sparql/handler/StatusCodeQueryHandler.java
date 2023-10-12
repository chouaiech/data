package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.metrics.cache.dqv.sparql.util.SparqlHelper;
import io.piveau.metrics.cache.persistence.DocumentScope;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;

public class StatusCodeQueryHandler extends QueryHandler {

    public StatusCodeQueryHandler(String queryName, Object ... parameters) {
        super(queryName, parameters);
    }

    public static StatusCodeQueryHandler create(DocumentScope scope, String id, Resource metric) {
        switch (scope) {
            case CATALOGUE:
                DCATAPUriRef uriRef = DCATAPUriSchema.applyFor(id);
                return new StatusCodeQueryHandler("CatalogueStatusCodes", uriRef.getCatalogueUriRef(), metric.getURI());
            case COUNTRY:
                return new StatusCodeQueryHandler("CountryStatusCodes", SparqlHelper.getCountries().get(id), metric.getURI());
            case GLOBAL:
                return new StatusCodeQueryHandler("AllStatusCodes", metric.getURI());
            case SCORE:
            default:
                throw new IllegalArgumentException("Unknown scope '" + scope + "'");
        }
    }

    @Override
    public void handle(ResultSet resultSet) {
        resultSet.forEachRemaining(solution ->
                result.put(
                        solution.getLiteral("statusCode").getLexicalForm(),
                        solution.getLiteral("count").getInt())
        );
    }

}
