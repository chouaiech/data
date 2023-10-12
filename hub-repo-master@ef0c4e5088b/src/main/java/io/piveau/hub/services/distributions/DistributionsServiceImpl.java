package io.piveau.hub.services.distributions;

import io.piveau.dcatap.*;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.rdf.Piveau;
import io.piveau.rdf.RDFMimeTypes;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.DCAT;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistributionsServiceImpl implements DistributionsService {

    private static final String DISTRIBUTION_ORIGINALID_QUERY = """
            SELECT ?original WHERE
            {
                <%1$s> <http://purl.org/dc/terms/identifier> ?original
            }""";

    private final TripleStore tripleStore;
    private final DatasetsService datasetsService;

    DistributionsServiceImpl(TripleStore tripleStore, DatasetsService datasetsService, Promise<DistributionsService> promise) {
        this.tripleStore = tripleStore;
        this.datasetsService = datasetsService;
        promise.complete(this);
    }

    @Override
    public Future<String> listDatasetDistributions(String datasetId, String valueType, String acceptType) {
        return Future.future(promise -> {
            DatasetManager datasetManager = tripleStore.getDatasetManager();
            DCATAPUriRef datasetUriRef = DCATAPUriSchema.createForDataset(datasetId);
            datasetManager.existGraph(datasetUriRef.getGraphName())
                    .compose(exists -> {
                        if (exists) {
                            return datasetManager.distributions(datasetUriRef.getUriRef());
                        } else {
                            return Future.failedFuture(new ServiceException(404, "Dataset not found"));
                        }
                    })
                    .onSuccess(list -> {
                        switch (valueType) {
                            case "uriRefs" -> {
                                JsonArray result = new JsonArray(list.stream().map(DCATAPUriRef::getUriRef).toList());
                                promise.complete(result.encodePrettily());
                            }
                            case "identifiers" -> {
                                JsonArray result = new JsonArray(list.stream().map(DCATAPUriRef::getId).toList());
                                promise.complete(result.encodePrettily());
                            }
                            case "originalIds" -> {
                                List<Future<ResultSet>> futures = list.stream()
                                        .map(uriRef -> tripleStore.select(String.format(DISTRIBUTION_ORIGINALID_QUERY, uriRef.getUriRef())))
                                        .toList();
                                CompositeFuture.join(new ArrayList<>(futures))
                                        .onSuccess(v -> {
                                            JsonArray originalIds = new JsonArray();
                                            futures.stream()
                                                    .filter(Future::succeeded)
                                                    .map(Future::result)
                                                    .forEach(resultSet -> {
                                                        while (resultSet.hasNext()) {
                                                            QuerySolution querySolution = resultSet.next();
                                                            String originalId = querySolution.getLiteral("original").getString();
                                                            originalIds.add(originalId);
                                                        }
                                                    });
                                            promise.complete(originalIds.encodePrettily());
                                        })
                                        .onFailure(promise::fail);
                            }
                            case "metadata" -> tripleStore.getDatasetManager().getGraph(datasetUriRef.getGraphName())
                                    .onSuccess(model -> {
                                        if (model.isEmpty()) {
                                            promise.fail(new ServiceException(404, "Dataset not found"));
                                        } else {
                                            Model result = ModelFactory.createDefaultModel();
                                            model.listObjectsOfProperty(DCAT.distribution).forEachRemaining(obj -> {
                                                Model dist = Piveau.extractAsModel(obj.asResource());
                                                result.add(dist);
                                            });
                                            promise.complete(Piveau.asString(result, acceptType));
                                        }
                                    })
                                    .onFailure(promise::fail);
                        }
                    })
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<String> getDistribution(String distributionId, String acceptType) {
        return Future.future(promise -> {
            DCATAPUriRef uriRef = DCATAPUriSchema.createFor(distributionId, DCATAPUriSchema.getDistributionContext());

            tripleStore.getDatasetManager().identifyDistribution(uriRef.getUriRef())
                    .onSuccess(datasetUriRef -> tripleStore.getDatasetManager().getGraph(datasetUriRef.getGraphName())
                            .onSuccess(model -> {
                                if (model.isEmpty()) {
                                    promise.fail(new ServiceException(404, "Dataset not found"));
                                } else {
                                    Model dist = Piveau.extractAsModel(uriRef.getResource(), model);
                                    promise.complete(Piveau.asString(dist, acceptType));
                                }
                            })
                            .onFailure(promise::fail)
                    )
                    .onFailure(cause -> {
                        if (cause instanceof TripleStoreException && ((TripleStoreException) cause).getCode() == 404) {
                            promise.fail(new ServiceException(404, "Distribution not found"));
                        } else {
                            promise.fail(cause);
                        }
                    });
        });
    }

    @Override
    public Future<String> postDistribution(String datasetId, String content, String contentType) {
        String distributionId = UUID.randomUUID().toString().toLowerCase();
        return putDistribution(datasetId, distributionId, content, contentType);
    }

    @Override
    public Future<String> putDistribution(String datasetId, String distributionId, String content, String contentType) {
        return Future.future(promise -> {
            DCATAPUriRef distributionUriRef = DCATAPUriSchema.createFor(distributionId, DCATAPUriSchema.getDistributionContext());
            DCATAPUriRef datasetUriRef = DCATAPUriSchema.createForDataset(datasetId);
            tripleStore.getDatasetManager().getGraph(datasetUriRef.getGraphName())
                    .compose(model -> {
                        Model oldDistributionModel = Piveau.extractAsModel(model.createResource(distributionUriRef.getUri()));
                        Model newDistributionModel = Piveau.toModel(content.getBytes(), contentType);

                        // safe some values here
                        // rename new content

                        model.remove(oldDistributionModel);
                        model.add(newDistributionModel);

                        return datasetsService.putDataset(datasetId, Piveau.asString(model, Lang.NTRIPLES), RDFMimeTypes.NTRIPLES);
                    })
                    .onSuccess(result -> promise.complete("updated"))
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<Void> deleteDistribution(String datasetId, String distributionId) {
        return Future.future(promise -> {
            DCATAPUriRef distributionUriRef = DCATAPUriSchema.createFor(distributionId, DCATAPUriSchema.getDistributionContext());
            DCATAPUriRef datasetUriRef = DCATAPUriSchema.createForDataset(datasetId);
            tripleStore.getDatasetManager().getGraph(datasetUriRef.getGraphName())
                    .compose(model -> {
                        Model distributionModel = Piveau.extractAsModel(model.createResource(distributionUriRef.getUri()));
                        model.remove(distributionModel);

                        return datasetsService.putDataset(datasetId, Piveau.asString(model, Lang.NTRIPLES), RDFMimeTypes.NTRIPLES);
                    })
                    .onSuccess(result -> promise.complete())
                    .onFailure(promise::fail);
        });
    }

}
