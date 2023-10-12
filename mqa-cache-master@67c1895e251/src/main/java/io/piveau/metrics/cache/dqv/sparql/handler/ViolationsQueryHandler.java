package io.piveau.metrics.cache.dqv.sparql.handler;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.ResultSet;

public class ViolationsQueryHandler extends QueryHandler {

    public static final String RESULT_MESSAGE = "resultMessage";
    public static final String RESULT_PATH = "resultPath";
    public static final String RESULT_VALUE = "resultValue";
    public static final String RESULT_SEVERITY = "resultSeverity";
    public static final String REFERENCE = "reference";
    public static final String DS = "ds";

    public ViolationsQueryHandler(String catalogueUriRef, int offset, int limit) {
        super("Violations", catalogueUriRef, offset, limit);
        result.put("violations", new JsonArray());
    }

    @Override
    public void handle(ResultSet resultSet) {
        JsonArray violations = result.getJsonArray("violations");

        resultSet.forEachRemaining(solution -> {
            JsonObject violation = new JsonObject();

            violation.put(RESULT_MESSAGE, solution.getLiteral(RESULT_MESSAGE).getLexicalForm());
            violation.put(RESULT_PATH, solution.getResource(RESULT_PATH).getURI());
            violation.put(RESULT_SEVERITY, solution.getResource(RESULT_SEVERITY).getURI());
            if (solution.get(RESULT_VALUE).isURIResource())
                violation.put(RESULT_VALUE, solution.getResource(RESULT_VALUE).getURI());
            else
                violation.put(RESULT_VALUE, solution.get(RESULT_VALUE).asNode().toString());

            violation.put(REFERENCE, solution.getResource(DS).getURI());

            violations.add(violation);
        });
    }

}
