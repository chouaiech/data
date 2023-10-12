package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.distributions.DistributionsService;
import io.piveau.hub.util.ContentNegotiation;
import io.piveau.hub.util.ContentType;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;


public class DistributionHandler {

    private final DistributionsService distributionsService;

    public DistributionHandler(Vertx vertx) {
        distributionsService = DistributionsService.createProxy(vertx, DistributionsService.SERVICE_ADDRESS);
    }

    public void handleListDatasetDistributions(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "datasetId", ContentType.JSON_LD);
        String datasetId = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String valueType = parameters.queryParameter("valueType").getString();

        if (!valueType.equals("metadata")) {
            contentNegotiation.setAcceptType(ContentType.JSON.getMimeType());
        }

        distributionsService.listDatasetDistributions(datasetId, valueType, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleGetDistribution(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context, "distributionId", ContentType.JSON_LD);
        String distributionId = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        distributionsService.getDistribution(distributionId, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePostDatasetDistribution(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        String datasetId = parameters.pathParameter("datasetId").getString();
        String contentType = context.parsedHeaders().contentType().value();
        if (parameters.body().isEmpty()) {
            context.fail(400, new ServiceException(400, "Body required"));
            return;
        }

        String content = parameters.body().toString();

        distributionsService.postDistribution(datasetId, content, contentType)
                .onSuccess(result -> context.response()
                        .putHeader(HttpHeaders.LOCATION, context.request().absoluteURI() + "/" + result)
                        .setStatusCode(201)
                        .end()
                )
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handlePutDistribution(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String distributionId = parameters.pathParameter("distributionId").getString();

        String contentType = context.parsedHeaders().contentType().value();

        String content = context.body().asString();

        String datasetId = context.get("datasetId");

        distributionsService.putDistribution(datasetId, distributionId, content, contentType)
                .onSuccess(result -> {
                    if (result.equals("updated")) {
                        context.response().setStatusCode(HttpStatus.SC_NO_CONTENT).end();
                    } else {
                        HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code"));
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void handleDeleteDistribution(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        String distributionId = parameters.pathParameter("distributionId").getString();
        String datasetId = context.get("datasetId");

        distributionsService.deleteDistribution(datasetId, distributionId)
                .onSuccess(result -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

}
