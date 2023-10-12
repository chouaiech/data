package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.vocabularies.vocabulary.PV;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.ResultSet;

public class StatusCodeErrorsQueryHandler extends QueryHandler {

    public StatusCodeErrorsQueryHandler(String catalogueUriRef, int offset, int limit) {
        super("StatusCodeErrors", catalogueUriRef, offset, limit);
        result.put("results", new JsonArray());
    }

    @Override
    public void handle(ResultSet resultSet) {

        resultSet.forEachRemaining(solution -> {
            JsonObject dataset = new JsonObject()
                    .put("reference", solution.getResource("ds").getURI())
                    .put("distribution", solution.getResource("dist").getURI());

            if (solution.contains("measurementOf")) {
                if (solution.getResource("measurementOf").equals(PV.accessUrlStatusCode)) {
                    //access URL
                    if (solution.contains("url")) {
                        dataset.put("accessUrl", solution.getResource("url").getURI());
                    }

                    //access Url status code
                    if (solution.contains("statusCode")) {
                        dataset.put("accessUrlStatusCode", solution.getLiteral("statusCode").getInt());
                    }

                    //timeStamp
                    if (solution.contains("time")) {
                        dataset.put("accessUrlTimeStamp", solution.getLiteral("time").getLexicalForm());
                    }
                } else {
                    //download URL
                    if (solution.contains("url")) {
                        dataset.put("downloadUrl", solution.getResource("url").getURI());
                    }

                    //download Url status code
                    if (solution.contains("statusCode")) {
                        dataset.put("downloadUrlStatusCode", solution.getLiteral("statusCode").getInt());
                    }

                    //timeStamp
                    if (solution.contains("time")) {
                        dataset.put("downloadUrlTimeStamp", solution.getLiteral("time").getLexicalForm());
                    }
                }
            }

            result.getJsonArray("results").add(dataset);
        });
    }

}
