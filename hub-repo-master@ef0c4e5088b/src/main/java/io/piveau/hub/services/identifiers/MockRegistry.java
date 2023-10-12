package io.piveau.hub.services.identifiers;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.apache.jena.rdf.model.Resource;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class MockRegistry extends IdentifierRegistry {

    public MockRegistry(Vertx vertx, WebClient webClient, JsonObject config) {
        super(vertx, webClient, config);
    }

    @Override
    public String getName() {
        return "Mock Identity Registry";
    }

    @Override
    public Future<Identifier> getIdentifier(String URI, DatasetHelper dataset, List<Identifier> existingIdentifiers) {
        Promise<Identifier> promise = Promise.promise();
        //promise.fail("This identifier is already set");
        Identifier identifier =  new Identifier();
        identifier.identifierURI = "http://local.io/doi/abcd/12345678";
        identifier.issued = ZonedDateTime.now(ZoneOffset.UTC);
        identifier.creatorURI = "http://piveau.eu/creator";
        identifier.identifier = "abcd/12345678";
        identifier.schema = "http://piveau.eu/identifiers/scheme";
        promise.complete(identifier);
        return promise.future();
    }

    @Override
    public JsonObject getIdentifierEligibility(JsonObject metadata) {
        JsonObject requiredElement = new JsonObject();
        requiredElement.put("none", true);

        JsonObject result = new JsonObject();
        result.put("identifierName", getName())
                .put("isEligible", true)
                .put("element", requiredElement);

        return result;
    }

}
