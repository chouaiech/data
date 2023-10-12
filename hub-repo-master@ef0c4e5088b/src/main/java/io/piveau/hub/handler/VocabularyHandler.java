package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.vocabularies.VocabulariesService;
import io.piveau.hub.util.ContentNegotiation;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import io.vertx.serviceproxy.ServiceException;

import java.util.List;

public class VocabularyHandler {

    private final VocabulariesService vocabulariesService;

    public VocabularyHandler(Vertx vertx) {
        vocabulariesService = VocabulariesService.createProxy(vertx, VocabulariesService.SERVICE_ADDRESS);
    }

    public void listVocabularies(RoutingContext context) {
        vocabulariesService.listVocabularies(ar -> {
            if (ar.succeeded()) {
                context.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .end(ar.result().toString());
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }

    public void getVocabulary(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context);

        vocabulariesService.readVocabulary(contentNegotiation.getId(), contentNegotiation.getAcceptType(), ar -> {
            if (ar.succeeded()) {
                contentNegotiation.headOrGetResponse(ar.result());
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }

    public void createOrUpdateVocabulary(RoutingContext context) {
        String id = context.request().getParam("id");
        String uri = context.request().getParam("uri");
        String hash = context.request().getParam("hash");
        String chunkIdParam = context.request().getParam("chunkId");
        String numberOfChunksParam = context.request().getParam("numberOfChunks");

        int chunkId;
        int numberOfChunks;
        if (chunkIdParam == null && numberOfChunksParam == null) {
            chunkId = 0;
            numberOfChunks = 1;
        } else {
            try {
                chunkId = Integer.parseUnsignedInt(chunkIdParam);
                numberOfChunks = Integer.parseUnsignedInt(numberOfChunksParam);
            } catch (NumberFormatException e) {
                context.response().setStatusCode(400).end();
                return;
            }
        }

        String contentType = context.parsedHeaders().contentType().rawValue();
        String payload = context.body().asString();

        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabulariesService.createOrUpdateVocabulary(id, uri, contentType, payload, hash, chunkId, numberOfChunks, ar -> {
                if (ar.succeeded()) {
                    switch (ar.result()) {
                        case "created" -> context.response().setStatusCode(201).end();
                        case "accepted" -> context.response().setStatusCode(202).end();
                        case "updated" -> context.response().setStatusCode(204).end();
                        default -> HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code"));
                    }
                } else {
                    HubRepo.failureResponse(context, ar.cause());
                }
            });
        } else {
            vocabulariesService.createOrUpdateVocabulary(id, uri, contentType, payload, hash, chunkId, numberOfChunks, ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void deleteVocabulary(RoutingContext context) {
        String id = context.request().getParam("id");
        vocabulariesService.deleteVocabulary(id, ar -> {
            if (ar.succeeded()) {
                context.response().setStatusCode(200).end();
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }
}
