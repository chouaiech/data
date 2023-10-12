package io.piveau.hub.services.catalogues;

import com.google.common.collect.Lists;
import io.piveau.dcatap.*;
import io.piveau.hub.Defaults;
import io.piveau.hub.indexing.Indexing;
import io.piveau.hub.security.KeyCloakService;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.util.CatalogueHelper;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.piveau.rdf.Piveau;
import io.piveau.vocabularies.vocabulary.EDP;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CataloguesServiceImpl implements CataloguesService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TripleStore tripleStore;
    private final CatalogueManager catalogueManager;

    private IndexService indexService;

    private final DatasetsService datasetsService;

    private final KeyCloakService keycloakService;

    CataloguesServiceImpl(TripleStore tripleStore, Vertx vertx, JsonObject config, Handler<AsyncResult<CataloguesService>> readyHandler) {
        this.tripleStore = tripleStore;
        catalogueManager = tripleStore.getCatalogueManager();

        JsonObject indexConfig = ConfigHelper.forConfig(config).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE);

        Boolean searchServiceEnabled = indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED);
        if (Boolean.TRUE.equals(searchServiceEnabled)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        }

        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
        keycloakService = KeyCloakService.createProxy(vertx, KeyCloakService.SERVICE_ADDRESS);

        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<String> listCatalogues(String acceptType, String valueType, Integer offset, Integer limit) {
        return Future.future(promise -> {
                    switch (valueType) {
                        case "uriRefs" -> catalogueManager.listUris()
                                .onSuccess(list -> {
                                    if (offset > list.size()) {
                                        promise.complete(new JsonArray().encode());
                                    } else if ((offset + limit) > list.size()) {
                                        promise.complete(new JsonArray(list.subList(offset, list.size()).stream().map(DCATAPUriRef::getUri).toList()).encodePrettily());
                                    } else {
                                        promise.complete(new JsonArray(list.subList(offset, offset + limit).stream().map(DCATAPUriRef::getUri).toList()).encodePrettily());
                                    }
                                })
                                .onFailure(promise::fail);
                        case "identifiers", "originalIds" -> catalogueManager.listUris()
                                .onSuccess(list -> {
                                    if (offset > list.size()) {
                                        promise.complete(new JsonArray().encode());
                                    } else if ((offset + limit) > list.size()) {
                                        promise.complete(new JsonArray(list.subList(offset, list.size()).stream().map(DCATAPUriRef::getId).toList()).encodePrettily());
                                    } else {
                                        promise.complete(new JsonArray(list.subList(offset, offset + limit).stream().map(DCATAPUriRef::getId).toList()).encodePrettily());
                                    }
                                })
                                .onFailure(promise::fail);
                        default -> catalogueManager.list(offset, limit)
                                .onSuccess(list -> promise.complete(Piveau.presentAs(list, acceptType, DCAT.Catalog)))
                                .onFailure(promise::fail);
                    }
                }
        );
    }

    @Override
    public Future<String> getCatalogue(String catalogueId, String acceptType) {
        return Future.future(promise -> catalogueManager.getStripped(catalogueId)
                .onSuccess(model -> {
                    if (model.isEmpty()) {
                        promise.fail(new ServiceException(404, "Catalogue not found"));
                    } else {
                        promise.complete(Piveau.presentAs(model, acceptType));
                    }
                })
                .onFailure(promise::fail)
        );
    }

    @Override
    public Future<String> putCatalogue(String catalogueId, String content, String contentType) {
        CatalogueHelper catalogueHelper = new CatalogueHelper(catalogueId, contentType, content);
        Model datasetsList = ModelFactory.createDefaultModel();
        return catalogueManager.exists(catalogueId)
                .compose(exists -> Future.<String>future(promise -> {
                            if (exists) {
                                // update
                                catalogueManager.allDatasets(catalogueId)
                                        .onSuccess(list -> {
                                            list.forEach(dataset -> {
                                                datasetsList.add(catalogueHelper.resource(), DCAT.dataset, datasetsList.createResource(dataset.getUri()));
                                                datasetsList.add(catalogueHelper.resource(), DCAT.record, datasetsList.createResource(dataset.getRecordUriRef()));
                                            });
                                            promise.complete("updated");
                                        })
                                        .onFailure(promise::fail);
                                catalogueHelper.modified();
                            } else {
                                // create
                                promise.complete("created");
                            }
                        })
                )
                .compose(result -> Future.<String>future(promise ->
                        catalogueManager.setGraph(catalogueHelper.uriRef(), catalogueHelper.getModel())
                                .onSuccess(uriRef -> {
                                    keycloakService.createResource(catalogueHelper.getId());
                                    promise.complete(result);
                                })
                                .onFailure(promise::fail)
                ))
                .compose(result -> Future.<String>future(promise -> {
                    if (datasetsList.isEmpty()) {
                        promise.complete(result);
                    } else {
                        List<List<Statement>> chunks = Lists.partition(datasetsList.listStatements().toList(), 2500);
                        Promise<String> chunkPromise = Promise.promise();
                        tripleStore.postChunks(catalogueHelper.uriRef(), chunks, chunkPromise);
                        chunkPromise.future()
                                .onSuccess(uriRef -> promise.complete(result))
                                .onFailure(promise::fail);
                    }
                }))
                .compose(result -> {
                    StmtIterator it = catalogueHelper.getModel().listStatements(
                            catalogueHelper.getModel().getResource(catalogueHelper.uriRef()),
                            EDP.visibility,
                            EDP.hidden
                    );

                    if (!it.hasNext()) {
                        if (indexService != null) {
                            Indexing.indexingCatalogue(catalogueHelper.getModel().getResource(catalogueHelper.uriRef()))
                                    .onSuccess(index -> indexService.addCatalog(index, ar -> {
                                                if (ar.failed()) {
                                                    logger.error("Indexing new catalogue", ar.cause());
                                                }
                                            })
                                    );
                        }
                    } else {
                        logger.info("Skip indexing for hidden catalogue");
                    }
                    return Future.succeededFuture(result);
                });
    }

    @Override
    public Future<Void> deleteCatalogue(String id) {
        return Future.future(promise -> catalogueManager.exists(id)
                .onSuccess(exists -> {
                    if (exists) {
                        catalogueManager.allDatasets(id)
                                .onSuccess(list -> {
                                    List<Future<Void>> futures = list.stream()
                                            .map(uriRef -> Future.<Void>future(prom -> datasetsService.deleteDataset(uriRef.getId()).onComplete(prom)))
                                            .toList();
                                    CompositeFuture.join(new ArrayList<>(futures))
                                            .onComplete(ar ->
                                                    catalogueManager.delete(id)
                                                            .onSuccess(v -> {
                                                                if (indexService != null) {
                                                                    indexService.deleteCatalog(id, as -> {
                                                                        if (as.failed()) {
                                                                            logger.error("Removing catalogue from index: {}", as.cause().getMessage());
                                                                        }
                                                                    });
                                                                }
                                                                keycloakService.deleteResource(id);
                                                                promise.complete();
                                                            })
                                                            .onFailure(promise::fail)
                                            );

                                })
                                .onFailure(promise::fail);
                    } else {
                        promise.fail(new ServiceException(404, "Catalogue not found"));
                    }
                })
                .onFailure(promise::fail)
        );
    }

}
