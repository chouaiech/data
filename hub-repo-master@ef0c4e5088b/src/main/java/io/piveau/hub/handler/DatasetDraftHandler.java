package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.services.drafts.DatasetDraftsService;
import io.piveau.hub.util.ContentNegotiation;
import io.piveau.utils.JenaUtils;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;

import java.util.ArrayList;
import java.util.List;

public class DatasetDraftHandler {

    private final DatasetsService datasetsService;
    private final DatasetDraftsService datasetDraftsService;

    public DatasetDraftHandler(Vertx vertx) {
        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
        datasetDraftsService = DatasetDraftsService.createProxy(vertx, DatasetDraftsService.SERVICE_ADDRESS);
    }

    public void listDatasetDrafts(RoutingContext context) {
        HttpServerResponse response = context.response();

        boolean filterByProvider = !context.queryParam("filterByProvider").isEmpty() &&
                !context.queryParam("filterByProvider").get(0).equals("false");

        List<String> authorizedResources = context.queryParam("authorizedResources");
        String provider = filterByProvider ? context.queryParam("provider").get(0) : "";

        datasetDraftsService.listDatasetDrafts(authorizedResources, provider, result -> {
            if (result.succeeded()) {
                response.putHeader("Content-Type", "application/json").end(result.result().toString());
            } else {
                HubRepo.failureResponse(context, result.cause());
            }
        });
    }

    public void createDatasetDraft(RoutingContext context) {
        String catalogueId = context.queryParam("catalogue").get(0);
        String contentType = context.parsedHeaders().contentType().value();

        String provider = context.queryParam("provider").get(0);

        datasetDraftsService.createDatasetDraft(catalogueId, context.body().asString(), contentType, provider, result -> {
            if (result.succeeded()) {
                if ("created".equals(result.result())) {
                    context.response().setStatusCode(201).end();
                } else {
                    // should not happen, succeeded path should only respond with 2xx codes
                    context.response().setStatusCode(400).end();
                }
            } else {
                HubRepo.failureResponse(context, result.cause());
            }
        });
    }

    public void readDatasetDraft(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context);
        String datasetId = contentNegotiation.getId();
        String acceptType = contentNegotiation.getAcceptType();

        String catalogueId = context.queryParam("catalogue").get(0);

        HttpServerResponse response = context.response();
        datasetDraftsService.readDatasetDraft(datasetId, catalogueId, acceptType, result -> {
            if (result.succeeded()) {
                response.putHeader("Content-Type", acceptType).end(result.result());
            } else {
                HubRepo.failureResponse(context, result.cause());
            }
        });
    }

    public void createOrUpdateDatasetDraft(RoutingContext context) {
        String datasetId = context.pathParam("id");
        String catalogueId = context.queryParam("catalogue").get(0);

        String contentType = context.parsedHeaders().contentType().value();

        String provider = context.queryParam("provider").isEmpty() ? "" : context.queryParam("provider").get(0);

        datasetDraftsService.createOrUpdateDatasetDraft(datasetId, catalogueId, context.body().asString(), contentType,
                provider, result -> {
            if (result.succeeded()) {
                switch (result.result()) {
                    case "created" -> context.response().setStatusCode(201).end();
                    case "updated" -> context.response().setStatusCode(204).end();
                    default ->
                        // should not happen, succeeded path should only respond with 2xx codes
                            context.response().setStatusCode(400).end();
                }
            } else {
                HubRepo.failureResponse(context, result.cause());
            }
        });
    }

    public void deleteDatasetDraft(RoutingContext context) {
        String datasetId = context.pathParam("id");
        String catalogueId = context.queryParam("catalogue").get(0);

        HttpServerResponse response = context.response();
        datasetDraftsService.deleteDatasetDraft(datasetId, catalogueId, result -> {
            if (result.succeeded()) {
                response.setStatusCode(204).end();
            } else {
                HubRepo.failureResponse(context, result.cause());
            }
        });
    }

    public void publishDatasetDraft(RoutingContext context) {
        String datasetId = context.pathParam("id");
        String catalogueId = context.queryParam("catalogue").get(0);
        String acceptType = "application/n-triples";

        HttpServerResponse response = context.response();
        datasetDraftsService.readDatasetDraft(datasetId, catalogueId, acceptType, readDatasetDraftResult -> {
            if (readDatasetDraftResult.succeeded()) {
                datasetsService.putDatasetOrigin(datasetId, removeCatalogRecord(readDatasetDraftResult.result(), acceptType),
                                acceptType, catalogueId, false)
                        .onSuccess(result -> datasetDraftsService.deleteDatasetDraft(datasetId, catalogueId, deleteDatasetDraftResult -> {
                            if (deleteDatasetDraftResult.succeeded()) {
                                response.setStatusCode(204).end();
                            } else {
                                HubRepo.failureResponse(context, deleteDatasetDraftResult.cause());
                            }
                        }))
                        .onFailure(cause -> HubRepo.failureResponse(context, cause));
            } else {
                HubRepo.failureResponse(context, readDatasetDraftResult.cause());
            }
        });
    }

    public void hideDataset(RoutingContext context) {
        String datasetId = context.pathParam("id");
        String catalogueId = context.queryParam("catalogue").get(0);
        String acceptType = "application/n-triples";

        String provider = context.queryParam("provider").isEmpty() ? "" : context.queryParam("provider").get(0);

        HttpServerResponse response = context.response();
        datasetsService.getDatasetOrigin(datasetId, catalogueId, acceptType)
                .onSuccess(content ->
                        datasetDraftsService.createOrUpdateDatasetDraft(datasetId, catalogueId,
                                removeCatalogRecord(content, acceptType), acceptType, provider,
                                createOrUpdateDatasetDraftResult -> {
                                    if (createOrUpdateDatasetDraftResult.succeeded()) {
                                        datasetsService.deleteDatasetOrigin(datasetId, catalogueId)
                                                .onSuccess(v -> response.setStatusCode(204).end())
                                                .onFailure(cause -> HubRepo.failureResponse(context, cause));
                                    } else {
                                        HubRepo.failureResponse(context, createOrUpdateDatasetDraftResult.cause());
                                    }
                                }))
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    private String removeCatalogRecord(String payload, String acceptType) {
        Model model = JenaUtils.read(payload.getBytes(), acceptType);
        ResIterator resIterator = model.listSubjectsWithProperty(RDF.type, DCAT.CatalogRecord);
        Resource catalogRecord = resIterator.next();
        List<Statement> toBeRemoved = new ArrayList<>();
        calcToBeRemoved(catalogRecord, toBeRemoved, model);
        model.remove(toBeRemoved);
        return JenaUtils.write(model, acceptType);
    }

    private void calcToBeRemoved(Resource rdfNode, List<Statement> toBeRemoved, Model model) {
        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.next();
            if (statement.getSubject().equals(rdfNode)) {
                toBeRemoved.add(statement);
                if (statement.getObject().isAnon()) {
                    calcToBeRemoved(statement.getObject().asResource(), toBeRemoved, model);
                }
            }
        }
    }

}
