package io.piveau.metrics.cache.dqv.sparql.handler;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class StatusCodeErrorsCountQueryHandler extends QueryHandler {

    public StatusCodeErrorsCountQueryHandler(String catalogueUriRef) {
        super("StatusCodeErrorsCount", catalogueUriRef);
    }

    @Override
    public void handle(ResultSet resultSet) {
        if (resultSet.hasNext()) {
            result.put("count", resultSet.next().getLiteral("count").getInt());
        }
    }

}
