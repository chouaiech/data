package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.catalogues.CataloguesService;
import io.piveau.hub.util.ContentNegotiation;
import io.piveau.hub.util.ContentType;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

public class CatalogueHandler {

    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private static final String PARAM_CATALOGUE_ID = "catalogueId";

    private final CataloguesService cataloguesService;

    public CatalogueHandler(Vertx vertx) {
        this.cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS);
    }

    public void handleListCatalogues(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "id", ContentType.JSON_LD);
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        Integer offset = parameters.queryParameter(OFFSET).getInteger();
        Integer limit = parameters.queryParameter(LIMIT).getInteger();

        String valueType = parameters.queryParameter("valueType").getString();
        if (!valueType.equals("metadata")) {
            contentNegotiation.setAcceptType(ContentType.JSON.getMimeType());
        }

        cataloguesService.listCatalogues(contentNegotiation.getAcceptType(), valueType, offset, limit)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleGetCatalogue(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, PARAM_CATALOGUE_ID, ContentType.JSON_LD);

        String id = contentNegotiation.getId();

        cataloguesService.getCatalogue(id, contentNegotiation.getAcceptType())
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePutCatalogue(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String id = parameters.pathParameter(PARAM_CATALOGUE_ID).getString();

        String content = parameters.body().getBuffer().toString();
        String contentType = context.parsedHeaders().contentType().value();

        cataloguesService.putCatalogue(id, content, contentType)
                .onSuccess(result -> {
                    switch (result) {
                        case "created" -> context.response().setStatusCode(201).end();
                        case "updated" -> context.response().setStatusCode(204).end();
                        default -> context.response().setStatusCode(400).end(result);
                        // should not happen, succeeded path should only respond with 2xx codes
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));

    }

    public void handleDeleteCatalogue(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String id = parameters.pathParameter(PARAM_CATALOGUE_ID).getString();

        cataloguesService.deleteCatalogue(id)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

}
