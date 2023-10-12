package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.hub.services.metrics.MetricsService;
import io.piveau.hub.util.ContentNegotiation;
import io.piveau.hub.util.ContentType;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceException;

public class MetricHandler {

    private final MetricsService metricsService;

    public MetricHandler(Vertx vertx) {
        metricsService = MetricsService.createProxy(vertx, MetricsService.SERVICE_ADDRESS);
    }

    public void handleGetMetric(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        boolean fetchHistoricGraph = parameters.queryParameter("historic").getBoolean();

        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "datasetId", ContentType.JSON_LD);

        String id = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        metricsService.getMetrics(id, fetchHistoricGraph, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePutMetric(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "datasetId", ContentType.JSON_LD);

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        RequestParameter body = parameters.body();
        if (body.isEmpty()) {
            context.fail(400, new ServiceException(400, "Body required"));
            return;
        }

        String content = body.toString();

        metricsService.putMetrics(contentNegotiation.getId(), content, contentNegotiation.getAcceptType())
                .onSuccess(result -> {
                    switch (result) {
                        case "created" -> context.response()
                                .setStatusCode(201)
                                .putHeader(
                                        HttpHeaders.LOCATION,
                                        DCATAPUriSchema.createFor(contentNegotiation.getId(), DCATAPUriSchema.getMetricsContext()).getMetricsUriRef())
                                .end();
                        case "updated" -> context.response().setStatusCode(204).end();
                        default ->
                                HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code"));
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleDeleteMetric(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        RequestParameter idParameter = parameters.pathParameter("id");
        String datasetId = idParameter == null ? parameters.queryParameter("id").getString() : idParameter.getString();

        // TODO check is parameter is required
        String catalogueId = parameters.queryParameter("catalogue").getString();

        metricsService.deleteMetrics(datasetId, catalogueId)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

}
