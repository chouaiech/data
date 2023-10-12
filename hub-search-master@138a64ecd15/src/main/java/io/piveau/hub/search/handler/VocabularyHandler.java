package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.vocabulary.VocabularyService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class VocabularyHandler extends ContextHandler {

    private final VocabularyService vocabularyService;

    public VocabularyHandler(Vertx vertx, String address) {
        vocabularyService = VocabularyService.createProxy(vertx, address);
    }

    public void readVocabularies(RoutingContext context) {
        vocabularyService.readVocabularies().onComplete(ar -> handleContextJsonArray(context, ar));
    }

    public void createOrUpdateVocabulary(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabularyService.createOrUpdateVocabulary(vocabulary, context.body().asJsonObject()).onComplete(ar -> handleContextJsonArray(context, ar));
        } else {
            vocabularyService.createOrUpdateVocabulary(vocabulary, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void readVocabulary(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        vocabularyService.readVocabulary(vocabulary).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void deleteVocabulary(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        vocabularyService.deleteVocabulary(vocabulary).onComplete(ar -> handleContextVoid(context, ar));
    }

    public void createVocable(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabularyService.createVocable(vocabulary, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            vocabularyService.createVocable(vocabulary, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void createOrUpdateVocable(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabularyService.createOrUpdateVocable(vocabulary, id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            vocabularyService.createOrUpdateVocable(vocabulary, id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void modifyVocable(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabularyService.modifyVocable(vocabulary, id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            vocabularyService.modifyVocable(vocabulary, id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void readVocable(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        String id = context.request().getParam("id");
        vocabularyService.readVocable(vocabulary, id).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void deleteVocable(RoutingContext context) {
        String vocabulary = context.request().getParam("vocabulary");
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            vocabularyService.deleteVocable(vocabulary, id).onComplete(ar -> handleContextVoid(context, ar));
        } else {
            vocabularyService.deleteVocable(vocabulary, id).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }
}
