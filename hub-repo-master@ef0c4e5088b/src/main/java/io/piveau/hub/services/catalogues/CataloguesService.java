package io.piveau.hub.services.catalogues;

import io.piveau.dcatap.TripleStore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface CataloguesService {
    String SERVICE_ADDRESS="io.piveau.hub.catalogues.queue";

    static Future<CataloguesService> create(TripleStore tripleStore, Vertx vertx, JsonObject config) {
        return Future.future(promise -> new CataloguesServiceImpl(tripleStore, vertx, config, promise));
    }

    static CataloguesService createProxy(Vertx vertx, String address) {
        return new CataloguesServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<String> listCatalogues(String acceptType, String valueType, Integer offset, Integer limit);

    Future<String> getCatalogue(String id, String acceptType);

    Future<String> putCatalogue(String id, String content, String contentType);

    Future<Void> deleteCatalogue(String id);

}
