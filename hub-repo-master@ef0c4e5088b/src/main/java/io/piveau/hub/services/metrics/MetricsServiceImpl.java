package io.piveau.hub.services.metrics;

import io.piveau.dcatap.*;
import io.piveau.dqv.PiveauMetrics;
import io.piveau.hub.Defaults;
import io.piveau.hub.indexing.Indexing;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.piveau.utils.JenaUtils;
import io.piveau.utils.PiveauContext;
import io.piveau.vocabularies.vocabulary.DQV;
import io.piveau.vocabularies.vocabulary.EDP;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RiotParseException;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MetricsServiceImpl implements MetricsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final TripleStore tripleStore;
    private final TripleStore shadowTripleStore;

    private final boolean historyEnabled;
    private final PiveauContext moduleContext = new PiveauContext("hub", "Metrics");
    private final IndexService indexService;
    private final JsonObject indexConfig;
    private final boolean prependXmlDeclaration;

    MetricsServiceImpl(TripleStore tripleStore, TripleStore shadowTripleStore, Vertx vertx, JsonObject config, Handler<AsyncResult<MetricsService>> readyHandler) {
        this.tripleStore = tripleStore;
        this.shadowTripleStore = shadowTripleStore != null ? shadowTripleStore : tripleStore;

        indexConfig = ConfigHelper.forConfig(config).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE);
        if (indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        } else {
            indexService = null;
        }

        JsonObject validationConfig = config.getJsonObject(Constants.ENV_PIVEAU_HUB_VALIDATOR, new JsonObject());
        historyEnabled = validationConfig.getBoolean("history", Defaults.METRICS_HISTORY);

        prependXmlDeclaration = config.getBoolean(Constants.ENV_PIVEAU_HUB_XML_DECLARATION, Defaults.XML_DECLARATION);
        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<String> getMetrics(String datasetId, boolean history, String contentType) {
        return Future.future(promise -> {
            DCATAPUriRef schema = DCATAPUriSchema.createForDataset(datasetId);
            fetch(schema, history, contentType, promise);
        });
    }

    private void fetch(DCATAPUriRef schema, boolean history, String contentType, Handler<AsyncResult<String>> handler) {
        String graphName = historyEnabled && history
                ? schema.getHistoricMetricsGraphName()
                : schema.getMetricsGraphName();

        TripleStore store = historyEnabled && history ? shadowTripleStore : tripleStore;

        store.getMetricsManager().getGraph(graphName)
                .onSuccess(model -> {
                    if (model.isEmpty()) {
                        handler.handle(ServiceException.fail(404, "Metrics not found"));
                    } else {
                        try {
                            Prefixes.setNsPrefixesFiltered(model);
                            String result = JenaUtils.write(model, contentType, prependXmlDeclaration);
                            handler.handle(Future.succeededFuture(result));
                        } catch (Exception e) {
                            handler.handle(Future.failedFuture(e));
                        }
                    }
                })
                .onFailure(cause -> handler.handle(Future.failedFuture(cause)));
    }

    @Override
    public Future<String> putMetrics(String datasetId, String content, String contentType) {
        return Future.future(promise -> {
            try {
                DCATAPUriRef datasetSchema = DCATAPUriSchema.createForDataset(datasetId);
                PiveauContext resourceContext = moduleContext.extend(datasetSchema.getId());

                Dataset dataset = JenaUtils.readDataset(content.getBytes(), contentType);

                List<Model> metrics = PiveauMetrics.listMetricsModels(dataset);
                if (!metrics.isEmpty()) {

                    Model dqvModel = metrics.get(0); // We assume there is only one named graph and it is the dqv graph

                    // Rename meta object
                    dqvModel.listSubjectsWithProperty(RDF.type, DQV.QualityMetadata)
                            .nextOptional()
                            .ifPresent(m -> {
                                m.removeAll(DCTerms.type);
                                m.addProperty(DCTerms.type, EDP.MetricsLatest);
                                ResourceUtils.renameResource(m, datasetSchema.getMetricsUriRef());
                            });

                    // replace existing "latest" graph
                    Future<String> latestResponse = tripleStore.getMetricsManager().setGraph(datasetSchema.getMetricsGraphName(), dqvModel);

                    // link
                    String query = "INSERT DATA { GRAPH <" + datasetSchema.getDatasetGraphName() + "> { <" + datasetSchema.getRecordUriRef() + "> <" + DQV.hasQualityMetadata + "> <" + datasetSchema.getMetricsGraphName() + "> } }";
                    tripleStore.update(query).onFailure(cause -> log.warn("Reference metrics graph failure: {}", datasetSchema.getUri(), cause));

                    if (historyEnabled) {
                        latestResponse.compose(response ->
                                // fetch existing "historic" graph if it exists
                                shadowTripleStore.ask("ASK WHERE { GRAPH <" + datasetSchema.getHistoricMetricsGraphName() + "> { ?s ?p ?o } }")
                        ).compose(historyGraphExists -> {
                            try {
                                dqvModel.listSubjectsWithProperty(RDF.type, DQV.QualityMetadata)
                                        .nextOptional()
                                        .ifPresent(m -> {
                                            m.removeAll(DCTerms.type);
                                            m.addProperty(DCTerms.type, EDP.MetricsHistory);
                                            ResourceUtils.renameResource(m, datasetSchema.getHistoricMetricsUriRef());
                                        });

//                    PiveauMetrics.removeAnnotation(historyGraph, historyGraph.getResource(datasetSchema.getDatasetUriRef()), OA.describing);

                                // add the latest metrics to existing "historic" graph
                                if (historyGraphExists) {
                                    return shadowTripleStore.postGraph(datasetSchema.getHistoricMetricsGraphName(), dqvModel);
                                } else {
                                    return shadowTripleStore.putGraph(datasetSchema.getHistoricMetricsGraphName(), dqvModel);
                                }
                            } catch (Exception e) {
                                return Future.failedFuture(e);
                            }
                        });
                    }

                    latestResponse
                            .onSuccess(response -> {
                                try {
                                    if (indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED)) {
                                        indexScore(datasetSchema.getId(), dqvModel);
                                    }
                                    promise.complete(response);
                                } catch (Throwable t) {
                                    log.error("Indexing score", t);
                                    promise.fail(new ServiceException(500, t.getMessage()));
                                }
                            })
                            .onFailure(cause -> promise.fail(new ServiceException(500, cause.getMessage())));

                } else {
                    resourceContext.log().warn("No metrics graph found");
                    promise.fail(new ServiceException(400, "No metrics graph found"));
                }
            } catch (RiotParseException e) {
                promise.fail(new ServiceException(400, e.getOriginalMessage()));
            } catch (Exception e) {
                log.error("General error", e);
                promise.fail(new ServiceException(500, e.getMessage()));
            }
        });
    }

    /**
     * Index the new score/ send it to the hub search
     *
     * @param id      the metrics id
     * @param metrics the model which contains the quality measurement with the score
     */
    private void indexScore(String id, Model metrics) {

        PiveauContext resourceContext = moduleContext.extend(id);

        JsonObject metricsIndex = Indexing.indexingMetrics(metrics);

        //we need the measurement of score and then the score value, so check, if these conditions are given
        if (metricsIndex.containsKey("quality_meas") && metricsIndex.getJsonObject("quality_meas").containsKey("scoring")) {
            JsonObject tmp = metricsIndex.getJsonObject("quality_meas");
            JsonObject q = new JsonObject().put("quality_meas", new JsonObject().put("scoring", tmp.getInteger("scoring")));
            indexService.modifyDataset(id, q, ar -> {
                if (ar.succeeded()) {
                    resourceContext.log().debug("Updated score in dataset index");
                } else {
                    resourceContext.log().warn("Could not update score in dataset index", ar.cause());
                }
            });
        }
    }

    @Override
    public Future<Void> deleteMetrics(String datasetId, String catalogueId) {
        return Future.future(promise -> tripleStore.getDatasetManager().identify(datasetId, catalogueId)
                .onSuccess(pair -> {
                    DCATAPUriRef schema = DCATAPUriSchema.parseUriRef(pair.getFirst().getURI());
                    // Silently delete history
                    shadowTripleStore.deleteGraph(schema.getHistoricMetricsGraphName());
                    tripleStore.deleteGraph(schema.getMetricsGraphName())
                            .onSuccess(v -> promise.complete())
                            .onFailure(cause -> promise.fail(new ServiceException(404, cause.getMessage())));
                })
                .onFailure(cause -> promise.fail(new ServiceException(500, cause.getMessage())))
        );
    }

}
