package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.catalogues.CataloguesService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CatalogueHandler extends ContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogueHandler.class);

    private final CataloguesService cataloguesService;

    public CatalogueHandler(Vertx vertx, String address) {
        cataloguesService = CataloguesService.createProxy(vertx, address);
    }

    public void listCatalogues(RoutingContext context) {
        LOG.debug("List catalogues, remote address: {}", context.request().connection().remoteAddress());
        String alias = context.request().getParam("alias");
        cataloguesService.listCatalogues(alias).onComplete(ar -> handleContextJsonArray(context, ar));
    }

    public void createCatalogue(RoutingContext context) {
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            cataloguesService.createCatalogue(context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            cataloguesService.createCatalogue(context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void createOrUpdateCatalogue(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            cataloguesService.createOrUpdateCatalogue(id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            cataloguesService.createOrUpdateCatalogue(id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void modifyCatalogue(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            cataloguesService.modifyCatalogue(id, context.body().asJsonObject()).onComplete(ar -> handleContext(context, ar));
        } else {
            cataloguesService.modifyCatalogue(id, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void readCatalogue(RoutingContext context) {
        LOG.debug("Read dataset, remote address: {}", context.request().connection().remoteAddress());
        String id = context.request().getParam("id");
        cataloguesService.readCatalogue(id).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void deleteCatalogue(RoutingContext context) {
        String id = context.request().getParam("id");
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            cataloguesService.deleteCatalogue(id).onComplete(ar -> handleContextVoid(context, ar));
        } else {
            cataloguesService.deleteCatalogue(id).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }
}
