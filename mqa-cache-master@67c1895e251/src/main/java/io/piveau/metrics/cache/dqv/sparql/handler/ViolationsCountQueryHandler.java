
package io.piveau.metrics.cache.dqv.sparql.handler;

import org.apache.jena.query.ResultSet;

public class ViolationsCountQueryHandler extends QueryHandler {

    public ViolationsCountQueryHandler(String catalogueUriRef) {
        super("ViolationsCount", catalogueUriRef);
    }

    @Override
    public void handle(ResultSet resultSet) {
        if (resultSet.hasNext()) {
            result.put("count", resultSet.next().getLiteral("count").getInt());
        }
    }

}
