package io.piveau.hub.services.identifiers;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import java.util.List;

/**
 * Examples: https://data.europa.eu/data/datasets/national-interoperability-framework-observatory-nifo-digital-government-factsheets-2020?locale=en
 * https://data.europa.eu/api/hub/repo/datasets/national-interoperability-framework-observatory-nifo-digital-government-factsheets-2020.ttl?useNormalizedId=true&locale=en
 */

public abstract class IdentifierRegistry {

    protected final Vertx vertx;
    protected final WebClient webClient;
    protected final JsonObject config;


    public IdentifierRegistry(Vertx vertx, WebClient webClient, JsonObject config) {
        this.vertx = vertx;
        this.webClient = webClient;
        this.config = config;
    }

    public abstract String getName();

    public abstract Future<Identifier> getIdentifier(String URI, DatasetHelper dataset, List<Identifier> existingIdentifiers);
    public abstract JsonObject getIdentifierEligibility(JsonObject metadata);

}
