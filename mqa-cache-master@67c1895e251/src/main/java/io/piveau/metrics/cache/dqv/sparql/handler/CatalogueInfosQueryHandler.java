package io.piveau.metrics.cache.dqv.sparql.handler;

import io.piveau.dcatap.DCATAPUriSchema;
import io.vertx.core.json.JsonObject;
import org.apache.jena.query.ResultSet;

public class CatalogueInfosQueryHandler extends QueryHandler {

    public CatalogueInfosQueryHandler() {
        super("CatalogueInfos");
    }

    @Override
    public void handle(ResultSet resultSet) {
        resultSet.forEachRemaining(solution -> {
            if (solution.contains("catalogue") && solution.getResource("catalogue").isURIResource()) {
                String catalogueUriRef = solution.getResource("catalogue").getURI();
                if (DCATAPUriSchema.isCatalogueUriRef(catalogueUriRef)) {
                    String identifier = DCATAPUriSchema.parseUriRef(solution.getResource("catalogue").getURI()).getId();
                    JsonObject catalogue = new JsonObject().put("id", identifier);
                    if (solution.contains("title")) {
                        catalogue.put("title", solution.getLiteral("title").getLexicalForm());
                    }
                    if (solution.contains("description")) {
                        catalogue.put("description", solution.getLiteral("description").getLexicalForm());
                    }
                    if (solution.contains("spatial")) {
                        String[] spa = solution.getResource("spatial").getURI().split("/");
                        catalogue.put("spatial", spa[spa.length - 1]);
                    }
                    if (solution.contains("type")) {
                        catalogue.put("type", solution.getLiteral("type").getLexicalForm());
                    }
                    result.put(identifier, catalogue);
                }
            }
        });
    }

}
