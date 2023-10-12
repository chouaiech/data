package io.piveau.hub.services.distributions;

import io.piveau.dcatap.TripleStore;
import io.piveau.hub.services.datasets.DatasetsService;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;

@ProxyGen
public interface DistributionsService {

    String SERVICE_ADDRESS = "io.piveau.hub.distributions.queue";

    static Future<DistributionsService> create(TripleStore tripleStore, DatasetsService datasetsService) {
        return Future.future(promise -> new DistributionsServiceImpl(tripleStore, datasetsService, promise));
    }

    static DistributionsService createProxy(Vertx vertx, String address) {
        return new DistributionsServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<String> listDatasetDistributions(String datasetId, String valueType, String acceptType);

    Future<String> getDistribution(String id, String acceptType);

    Future<String> postDistribution(String datasetId, String content, String contentType);

    Future<String> putDistribution(String datasetId, String distributionId, String content, String contentType);

    Future<Void> deleteDistribution(String datasetId, String distributionId);

}
