package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.translation.TranslationService;
import io.piveau.hub.util.logger.PiveauLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceException;

public class TranslationServiceHandler {

    private final TranslationService translationService;

    public TranslationServiceHandler(Vertx vertx) {
        translationService = TranslationService.createProxy(vertx, TranslationService.SERVICE_ADDRESS);
    }

    public void handlePostTranslation(RoutingContext context) {
        RequestParameters parameters = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        JsonObject translation = parameters.body().getJsonObject();

        PiveauLoggerFactory.getLogger(getClass()).debug(translation.encode());

        translationService.receiveTranslation(translation, ar -> {
            if (ar.succeeded()) {
                JsonObject result = ar.result();
                switch (result.getString("status")) {
                    case "success" -> context.response().setStatusCode(200).end();
                    case "Not found" -> context.response().setStatusCode(404).end();
                    default -> HubRepo.failureResponse(context, new ServiceException(500, "Unexpected status code"));
                }
            } else {
                HubRepo.failureResponse(context, ar.cause());
            }
        });
    }
}
