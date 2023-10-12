package io.piveau.metrics.cache.dqv.sparql.handler;

import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogueModifiedQueryHandler extends QueryHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());
    public CatalogueModifiedQueryHandler(Object ... parameters) {
        super("CatalogueModified",parameters);
    }

    @Override
    public void handle(ResultSet resultSet) {
        resultSet.forEachRemaining(solution -> {
            if (solution.contains("modified")) {
                result.put("modified", solution.getLiteral("modified").getValue().toString());
            } else {
                log.warn("No modified in solution: {}", solution);
            }
        });
    }

}
