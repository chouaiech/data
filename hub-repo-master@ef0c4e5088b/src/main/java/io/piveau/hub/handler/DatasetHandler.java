package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.util.ContentNegotiation;
import io.piveau.hub.util.ContentType;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceException;

public class DatasetHandler {

    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private static final String QUERY_PARAM_CATALOGUE = "catalogue";
    private static final String PATH_PARAM_CATALOGUE = "catalogueId";
    private static final String PATH_PARAM_DATASET = "datasetId";
    private static final String QUERY_PARAM_DATASET = "originalId";

    private final DatasetsService datasetsService;

    public DatasetHandler(Vertx vertx) {
        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
    }

    public void handleListDatasets(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        Integer offset = parameters.queryParameter(OFFSET).getInteger();
        Integer limit = parameters.queryParameter(LIMIT).getInteger();

        String catalogueId = "";
        RequestParameter param = parameters.queryParameter(QUERY_PARAM_CATALOGUE);
        if (param != null) {
            catalogueId = param.getString();
        }
//        String catalogueId = parameters.queryParametersNames().contains(QUERY_PARAM_CATALOGUE) ? parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString() : "";

        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "dummyId", ContentType.JSON_LD);

        String valueType = parameters.queryParameter("valueType").getString();

        Boolean sourceIds = parameters.queryParameter("sourceIds").getBoolean();
        if (sourceIds) {
            valueType = "originalIds";
        }

        if (!valueType.equals("metadata")) {
            contentNegotiation.setAcceptType(ContentType.JSON.getMimeType());
        }

        if (catalogueId.isBlank()) {
            datasetsService.listDatasets(contentNegotiation.getAcceptType(), valueType, limit, offset)
                    .onSuccess(contentNegotiation::headOrGetResponse)
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        } else {
            datasetsService.listCatalogueDatasets(contentNegotiation.getAcceptType(), valueType, catalogueId, limit, offset)
                    .onSuccess(contentNegotiation::headOrGetResponse)
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        }
    }

    public void handleGetDataset(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, PATH_PARAM_DATASET, ContentType.JSON_LD);
        String id = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        if (parameters.queryParameter(QUERY_PARAM_CATALOGUE) == null) {
            datasetsService.getDataset(id, acceptType)
                    .onSuccess(contentNegotiation::headOrGetResponse)
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        } else {
            datasetsService.getDatasetOrigin(id, parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString(), acceptType)
                    .onSuccess(contentNegotiation::headOrGetResponse)
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        }
    }

    public void handleGetDatasetOrigin(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, PATH_PARAM_CATALOGUE, ContentType.JSON_LD);
        String catalogueId = contentNegotiation.getId();

        String acceptType = contentNegotiation.getAcceptType();

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String originalId = parameters.queryParameter(QUERY_PARAM_DATASET).getString();
        datasetsService.getDatasetOrigin(originalId, catalogueId, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleGetRecord(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, PATH_PARAM_DATASET, ContentType.JSON_LD);
        String datasetId = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        datasetsService.getRecord(datasetId, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePutDataset(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String id = parameters.pathParameter(PATH_PARAM_DATASET).getString();

        RequestParameter body = parameters.body();
        if (body.isEmpty()) {
            context.fail(400, new ServiceException(400, "Body required"));
            return;
        }
        String content = body.toString();

        String contentType = context.parsedHeaders().contentType().value();

        if (parameters.queryParameter(QUERY_PARAM_CATALOGUE) == null) {
            datasetsService.putDataset(id, content, contentType)
                    .onSuccess(status -> {
                        switch (status.getString("status")) {
                            case "updated" -> context.response()
                                    .setStatusCode(204)
                                    .putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), ""))
                                    .end();
                            default -> HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code"));
                        }
                    })
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        } else {
            String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();
            datasetsService.putDatasetOrigin(id, content, contentType, catalogueId, false)
                    .onSuccess(status -> {
                        switch (status.getString("status")) {
                            case "created" ->
                                    context.response().setStatusCode(201).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                            case "updated" ->
                                    context.response().setStatusCode(204).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                            default ->
                                // should not happen, succeeded path should only respond with 2xx codes
                                    context.response().setStatusCode(400).end();
                        }
                    })
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        }
    }

    public void handlePutDatasetOrigin(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String catalogueId = parameters.pathParameter(PATH_PARAM_CATALOGUE).getString();
        String originId = parameters.queryParameter(QUERY_PARAM_DATASET).getString();
//        Boolean dataUpload = parameters.queryParameter("data").getBoolean();

        String content = parameters.body().getBuffer().toString();
        String contentType = context.parsedHeaders().contentType().value();

        datasetsService.putDatasetOrigin(originId, content, contentType, catalogueId, false)
                .onSuccess(status -> {
                    switch (status.getString("status")) {
                        case "created" ->
                                context.response().setStatusCode(201).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                        case "updated" ->
                                context.response().setStatusCode(204).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                        default ->
                            // should not happen, succeeded path should only respond with 2xx codes
                                context.response().setStatusCode(400).end();
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePutDatasetLegacy(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();
        String originId = parameters.queryParameter("id").getString();
//        Boolean dataUpload = parameters.queryParameter("data").getBoolean();

        String content = parameters.body().getBuffer().toString();
        String contentType = context.parsedHeaders().contentType().value();

        datasetsService.putDatasetOrigin(originId, content, contentType, catalogueId, false)
                .onSuccess(status -> {
                    switch (status.getString("status")) {
                        case "created" ->
                                context.response().setStatusCode(201).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                        case "updated" ->
                                context.response().setStatusCode(204).putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), "")).end();
                        default ->
                            // should not happen, succeeded path should only respond with 2xx codes
                                context.response().setStatusCode(400).end();
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleDeleteDatasetOrigin(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String catalogueId = parameters.pathParameter(PATH_PARAM_CATALOGUE).getString();
        String originalId = parameters.queryParameter(QUERY_PARAM_DATASET).getString();

        datasetsService.deleteDatasetOrigin(originalId, catalogueId)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleDeleteDataset(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String id = parameters.pathParameter("datasetId").getString();

        if (parameters.queryParameter(QUERY_PARAM_CATALOGUE) == null) {
            datasetsService.deleteDataset(id)
                    .onSuccess(v -> context.response().setStatusCode(204).end())
                    .onFailure(cause -> HubRepo.failureResponse(context, cause));
        } else {
            String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();
            datasetsService.deleteDatasetOrigin(id, catalogueId)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
        }
    }

    public void handleDeleteDatasetLegacy(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String id = parameters.queryParameter("id").getString();
        String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();

        datasetsService.deleteDatasetOrigin(id, catalogueId)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleListCatalogueDatasets(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        Integer offset = parameters.queryParameter(OFFSET).getInteger();
        Integer limit = parameters.queryParameter(LIMIT).getInteger();

        String valueType = parameters.queryParameter("valueType").getString();

        ContentNegotiation contentNegotiation = new ContentNegotiation(context, PATH_PARAM_CATALOGUE, ContentType.JSON_LD);

        if (!valueType.equals("metadata")) {
            contentNegotiation.setAcceptType(ContentType.JSON.getMimeType());
        }

        datasetsService.listCatalogueDatasets(contentNegotiation.getAcceptType(), valueType, contentNegotiation.getId(), limit, offset)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePostCatalogueDataset(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String catalogueId = parameters.pathParameter(PATH_PARAM_CATALOGUE).getString();

        String contentType = context.parsedHeaders().contentType().value();
        if (context.body().asString() == null) {
            HubRepo.failureResponse(context, new ServiceException(400, "Body required"));
            return;
        }

        datasetsService.postDataset(context.body().asString(), contentType, catalogueId, false)
                .onSuccess(status -> {
                    switch (status.getString("status")) {
                        case "created" -> context.response()
                                .setStatusCode(201)
                                .putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), ""))
                                .end();
                        default -> HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code", status));
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePostCatalogueDatasetLegacy(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();

        RequestParameter body = parameters.body();
        if (body.isEmpty()) {
            context.fail(400, new ServiceException(400, "Body required"));
            return;
        }
        String content = body.getBuffer().toString();

        String contentType = context.parsedHeaders().contentType().value();

        // Handle Post Dataset
        datasetsService.postDataset(content, contentType, catalogueId, false)
                .onSuccess(status -> {
                    if (status.getString("status").equals("created")) {
                        context.response().setStatusCode(201)
                                .putHeader(HttpHeaders.LOCATION, status.getString(HttpHeaders.LOCATION.toString(), ""))
                                .end();
                    } else { // Should not happen
                        HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code", status));
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleIndexDataset(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String datasetId = parameters.pathParameter(PATH_PARAM_DATASET).getString();
        String catalogueId = parameters.queryParameter(QUERY_PARAM_CATALOGUE).getString();

        datasetsService.indexDataset(datasetId, catalogueId)
                .onSuccess(result -> context.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(200)
                        .end(result.encodePrettily()))
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

}
