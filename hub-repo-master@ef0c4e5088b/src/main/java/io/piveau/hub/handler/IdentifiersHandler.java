package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.identifiers.IdentifiersService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

public class IdentifiersHandler {

    private final IdentifiersService identifiersService;

    public IdentifiersHandler(Vertx vertx) {
       identifiersService = IdentifiersService.createProxy(vertx, IdentifiersService.SERVICE_ADDRESS);
    }

    public void createDatasetIdentifier(RoutingContext context) {
        String datasetId = context.pathParam("datasetId");
        String catalogueId = context.queryParam("catalogue").get(0);
        String type = context.queryParam("type").get(0);

        identifiersService.createIdentifier(datasetId, catalogueId, type, ar -> {
            if(ar.succeeded()) {
                context
                        .response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(200)
                        .end(ar.result().toString());
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }

    public void checkDatasetIdentifierEligibility(RoutingContext context) {
        String datasetId = context.pathParam("datasetId");
        String catalogueId = context.queryParam("catalogue").get(0);

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        RequestParameter type = parameters.queryParameter("type");
        String identifierType = type != null ? type.getString() : null;

        identifiersService.checkIdentifierRequirement(datasetId, catalogueId, identifierType, ar -> {
            if(ar.succeeded()) {
                context
                        .response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(200)
                        .end(ar.result().toString());
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }


}
