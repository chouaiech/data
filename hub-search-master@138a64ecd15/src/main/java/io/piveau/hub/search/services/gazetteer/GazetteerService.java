package io.piveau.hub.search.services.gazetteer;

import io.piveau.hub.search.util.gazetteer.GazetteerConnector;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface GazetteerService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.gazetteer.queue";

    static Future<GazetteerService> create(GazetteerConnector connector) {
        return Future.future(promise -> new GazetteerServiceImpl(connector, promise));
    }

    static GazetteerService createProxy(Vertx vertx, String address) {
        return new GazetteerServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonObject> autocomplete(String q);
}
