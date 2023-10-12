package io.piveau.hub.services.datasets;

import io.piveau.dcatap.*;
import io.piveau.hub.Constants;
import io.piveau.hub.Defaults;
import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.indexing.Indexing;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.services.translation.TranslationService;
import io.piveau.hub.util.DataUploadConnector;
import io.piveau.json.ConfigHelper;
import io.piveau.log.PiveauLogger;
import io.piveau.pipe.PipeLauncher;
import io.piveau.rdf.Piveau;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.utils.PiveauContext;
import io.piveau.vocabularies.Concept;
import io.piveau.vocabularies.Languages;
import io.piveau.vocabularies.vocabulary.LOCN;
import io.piveau.vocabularies.vocabulary.SPDX;
import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DatasetsServiceImpl implements DatasetsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PiveauContext piveauContext = new PiveauContext("hub", "repo");

    private final TripleStore tripleStore;
    private final TripleStore shadowTripleStore;

    private final CatalogueManager catalogueManager;
    private final DatasetManager datasetManager;

    private final DataUploadConnector dataUploadConnector;

    private IndexService indexService;
    private TranslationService translationService;

    private final JsonObject validationConfig;

    private final PipeLauncher launcher;

    private final boolean prependXmlDeclaration;

    private final Cache<String, JsonObject> cache;

    private final Set<String> clearGeoDataCatalogues = new HashSet<>();

    private Boolean forceUpdates = Defaults.FORCE_UPDATES;

    private Boolean archivingEnabled = Defaults.EXPERIMENTS;

    private final String catalogueQueryTemplate = """
            SELECT ?type ?lang ?visibility WHERE
            {
                GRAPH <%1$s>
                {
                    OPTIONAL { <%2$s> <http://purl.org/dc/terms/type> ?type }
                    OPTIONAL { <%2$s> <http://purl.org/dc/terms/language> ?lang }
                    OPTIONAL { <%2$s> <https://europeandataportal.eu/voc#visibility> ?visibility }
                }
            }""";

    DatasetsServiceImpl(TripleStore tripleStore, TripleStore shadowTripleStore, DataUploadConnector dataUploadConnector, JsonObject config, PipeLauncher launcher, Vertx vertx, Handler<AsyncResult<DatasetsService>> readyHandler) {

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("datasetsService", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, JsonObject.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES))
                        .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofHours(12))))
                .build(true);

        cache = cacheManager.getCache("datasetsService", String.class, JsonObject.class);

        this.launcher = launcher;

        this.tripleStore = tripleStore;
        this.shadowTripleStore = shadowTripleStore;

        ConfigHelper configHelper = ConfigHelper.forConfig(config);
        JsonArray list =
                configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG)
                        .getJsonArray("clearGeoDataCatalogues", new JsonArray());
        List<String> entries = list.stream().map(Object::toString).toList();
        clearGeoDataCatalogues.addAll(entries);

        forceUpdates = config.getBoolean(Constants.ENV_PIVEAU_HUB_FORCE_UPDATES, forceUpdates);

        archivingEnabled = config.getBoolean(Constants.ENV_PIVEAU_ARCHIVING_ENABLED, archivingEnabled);


        catalogueManager = tripleStore.getCatalogueManager();
        datasetManager = tripleStore.getDatasetManager();

        this.dataUploadConnector = dataUploadConnector;

        JsonObject indexConfig = configHelper.forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE);
        if (indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        }
        JsonObject translationConfig = configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRANSLATION_SERVICE);
        if (translationConfig.getBoolean("enable", Defaults.TRANSLATION_SERVICE_ENABLED)) {
            translationService = TranslationService.createProxy(vertx, TranslationService.SERVICE_ADDRESS);
        }

        validationConfig = configHelper.forceJsonObject(Constants.ENV_PIVEAU_HUB_VALIDATOR);

        prependXmlDeclaration = config.getBoolean(Constants.ENV_PIVEAU_HUB_XML_DECLARATION, Defaults.XML_DECLARATION);
        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<String> listCatalogueDatasets(String acceptType, String valueType, String catalogueId, Integer limit, Integer offset) {
        return Future.future(promise -> {
            catalogueManager.exists(catalogueId)
                    .onSuccess(exists -> {
                        if (!exists) {
                            promise.fail(new ServiceException(404, "Catalogue not found"));
                        } else {
                            switch (valueType) {
                                case "originalIds" -> catalogueManager.allDatasetIdentifiers(catalogueId)
                                        .onSuccess(list -> promise.complete(new JsonArray(list).encodePrettily()))
                                        .onFailure(promise::fail);
                                case "identifiers" -> catalogueManager.datasets(catalogueId, offset, limit)
                                        .onSuccess(list -> promise.complete(new JsonArray(list.stream().map(DCATAPUriRef::getId).toList()).encodePrettily()))
                                        .onFailure(promise::fail);
                                case "uriRefs" -> catalogueManager.datasets(catalogueId, offset, limit)
                                        .onSuccess(list -> promise.complete(new JsonArray(list.stream().map(DCATAPUriRef::getUri).toList()).encodePrettily()))
                                        .onFailure(promise::fail);
                                case "metadata" -> catalogueManager.listDatasets(catalogueId, offset, limit)
                                        .onSuccess(list -> {
                                            list.forEach(this::changeVirtuType);
                                            promise.complete(Piveau.presentAs(list, acceptType, DCAT.Dataset));})
                                        .onFailure(promise::fail);
                            }
                        }
                    })
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<String> listDatasets(String acceptType, String valueType, Integer limit, Integer offset) {
        return Future.future(promise -> {
            switch (valueType) {
                case "originalIds" ->
                        promise.fail(new ServiceException(400, "Value type originalIds only allowed when catalogueId is set"));
                case "identifiers" -> datasetManager.list(offset, limit)
                        .onSuccess(list -> promise.complete(new JsonArray(list.stream().map(DCATAPUriRef::getId).toList()).encodePrettily()))
                        .onFailure(promise::fail);
                case "uriRefs" -> datasetManager.list(offset, limit)
                        .onSuccess(list -> promise.complete(new JsonArray(list.stream().map(DCATAPUriRef::getUri).toList()).encodePrettily()))
                        .onFailure(promise::fail);
                case "metadata" -> datasetManager.listModels(offset, limit)
                        .onSuccess(list -> {
                            list.forEach(this::changeVirtuType);
                            promise.complete(Piveau.presentAs(list, acceptType, DCAT.Dataset));
                        })
                        .onFailure(promise::fail);
            }
        });
    }

    private Model changeVirtuType(Model model) {
        List<Statement> stmtList = new ArrayList<>();
        List<Statement> stmtListToRemove = new ArrayList<>();

        logger.info("listResourcesWithProperty");
        model.listResourcesWithProperty(LOCN.geometry).forEach(res -> {
            logger.info("RES: {}", res);
            res.listProperties(LOCN.geometry).forEach(stmt -> {
                logger.info("STMT: {}", stmt);
                if (stmt.getObject().isLiteral()) {
                    Literal l = stmt.getLiteral();
                    logger.info("DATATYPE: {}", l.getDatatype().getURI());
                    if (l.getDatatype().getURI().equals("http://www.openlinksw.com/schemas/virtrdf#Geometry")) {

                        stmtListToRemove.add(stmt);
                        String value = l.getString();
                        Literal modifiedLiteral = ResourceFactory.createTypedLiteral(value,
                                NodeFactory.getType("http://www.opengis.net/ont/geosparql#wktLiteral"));

                        Statement newStmt = ResourceFactory.createStatement(stmt.getSubject(), stmt.getPredicate(), modifiedLiteral);

                        stmtList.add(newStmt);
                    }

                }
            });
        });
        stmtListToRemove.forEach(model::remove);
        stmtList.forEach(model::add);
        return model;
    }

    @Override
    public Future<String> getDatasetOrigin(String originId, String catalogueId, String acceptType) {
        return Future.future(promise -> catalogueManager.exists(catalogueId)
                .compose(exists -> {
                    if (exists) {
                        return datasetManager.get(originId, catalogueId);
                    } else {
                        return Future.failedFuture(new ServiceException(404, "Catalogue not found"));
                    }
                })
                .onSuccess(model -> {
                    if (model.isEmpty()) {
                        promise.fail(new ServiceException(404, "Dataset not found"));
                    } else {
                        changeVirtuType(model);
                        promise.complete(Piveau.presentAs(model, acceptType));
                    }
                })
                .onFailure(cause -> {
                    if (cause.getMessage().startsWith("Not found")) {
                        promise.fail(new ServiceException(404, "Dataset not found"));
                    } else {
                        promise.fail(cause);
                    }
                })
        );
    }

    @Override
    public Future<String> getDataset(String id, String acceptType) {
        return Future.future(promise -> {
            DCATAPUriRef uriRef = DCATAPUriSchema.createFor(id, DCATAPUriSchema.getDatasetContext());
            datasetManager.getGraph(uriRef.getGraphName())
                    .onSuccess(model -> {
                        if (model.isEmpty()) {
                            promise.fail(new ServiceException(404, "Dataset not found"));
                        } else {
                            changeVirtuType(model);
                            Prefixes.setNsPrefixesFiltered(model);
                            promise.complete(Piveau.presentAs(model, acceptType));
                        }
                    })
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<String> getRecord(String id, String acceptType) {
        return Future.future(promise -> {
            DCATAPUriRef uriRef = DCATAPUriSchema.createForDataset(id);
            datasetManager.getGraph(uriRef.getDatasetGraphName())
                    .onSuccess(model -> {
                        if (model.isEmpty()) {
                            promise.fail(new ServiceException(404, "Catalogue record not found"));
                        } else {
                            if (Piveau.isQuadFormat(acceptType)) {
                                Dataset dataset = DatasetFactory.create(model);
                                promise.complete(Piveau.presentAs(dataset, acceptType));
                            } else {
                                promise.complete(Piveau.presentAs(model, acceptType));
                            }
                        }
                    })
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<JsonObject> putDataset(String datasetId, String content, String contentType) {
        return Future.future(promise -> {
            AtomicReference<DatasetHelper> datasetHelper = new AtomicReference<>();
            DCATAPUriRef datasetUriRef = DCATAPUriSchema.createForDataset(datasetId);
            datasetManager.existGraph(datasetUriRef.getGraphName())
                    .compose(exists -> {
                        if (!exists) {
                            return Future.failedFuture(new ServiceException(404, "Dataset not found"));
                        } else {
                            return DatasetHelper.create(content, contentType);
                        }
                    })
                    .compose(helper -> {
                        datasetHelper.set(helper);
                        return updateDataset(helper, datasetUriRef.getRecordUriRef());
                    })
                    .compose(this::store)
                    .onSuccess(result -> {
                        if (indexService != null) {
                            index(datasetHelper.get())
                                    .onFailure(cause -> {
                                        if (cause instanceof ServiceException) {
                                            logger.error("Indexing failure: {}", ((ServiceException) cause).getDebugInfo().encode(), cause);
                                        }
                                    });
                        }
                        if (validationConfig.getBoolean("enabled", Defaults.VALIDATOR_ENABLED)) {
                            systemPipes(datasetHelper.get());
                        }
                        //Log to archive
                        if (archivingEnabled) {
                            piveauContext.extend(datasetId).log().info("[Dataset] [0] [updated] " + Piveau.toRdfXml(datasetHelper.get().model()));
                        }
                        promise.complete(result);
                    })
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<JsonObject> putDatasetOrigin(String originalId, String content, String contentType, String catalogueId, Boolean createAccessURLs) {
        PiveauLogger logPiveau = piveauContext.extend(originalId).log();
        return Future.future(promise -> {
            AtomicReference<DatasetHelper> datasetHelper = new AtomicReference<>();

            Future<DatasetHelper> datasetHelperFuture = DatasetHelper.create(originalId, content, contentType, catalogueId);
            Future<JsonObject> catalogueInfo = catalogueInfo(catalogueId);

            CompositeFuture.all(datasetHelperFuture, catalogueInfo).compose(f -> {
                datasetHelper.set(datasetHelperFuture.result());
                JsonObject catalogue = catalogueInfo.result();

                datasetHelper.get().sourceLang(catalogue.getString("langCode"));
                datasetHelper.get().sourceType(catalogue.getString("type"));
                datasetHelper.get().visibility(catalogue.getString("visibility", ""));

                return getRecordHash(datasetHelper.get());
            }).compose(records -> {
                Promise<DatasetHelper> helperPromise = Promise.promise();

                if (records.isEmpty()) {
                    createDataset(datasetHelper.get(), createAccessURLs).onComplete(helperPromise);
                } else {
                    if (records.size() > 1) {
                        logPiveau.warn("Found duplicate entries: {}", records.size());
                        // deleting duplicates... keep one. but how?
                        Iterator<JsonObject> it = records.iterator();
                        while (it.hasNext()) {
                            JsonObject record = it.next();
                            DCATAPUriRef uriRef = DCATAPUriSchema.parseUriRef(record.getString("recordUri"));

                            if (!uriRef.getId().equals(Piveau.asNormalized(datasetHelper.get().originId()))) {
                                delete(uriRef).onComplete(ar -> logPiveau.warn("Duplicate {} removed", uriRef.getDatasetUriRef()));
                                it.remove();
                            }
                        }
                    }
                    if (records.isEmpty()) {
                        helperPromise.fail(new ServiceException(500, "Due to unclear duplicate detection removed completely"));
                    } else {
                        JsonObject recordInfo = records.get(0);
                        String record = recordInfo.getString("recordUri");
                        if (!DCATAPUriSchema.isRecordUriRef(record)) {
                            helperPromise.fail(new ServiceException(500, "Foreign catalogue record with dct:identifier detected"));
                        } else {
                            String oldHash = recordInfo.getString("hash");
                            String currentHash = datasetHelper.get().hash();
                            if (currentHash.equals(oldHash) && !forceUpdates) {
                                helperPromise.fail(new ServiceException(304, "Dataset is up to date"));
                            } else {
                                updateDataset(datasetHelper.get(), record).onComplete(helperPromise);
                            }
                        }
                    }
                }
                return helperPromise.future();
            }).compose(finalHelper -> {
                datasetHelper.set(finalHelper);
                return store(datasetHelper.get());
            }).onSuccess(status -> {
                if (!datasetHelper.get().visibility().equals("hidden") && indexService != null) {
                    index(datasetHelper.get())
                            .onFailure(cause -> {
                                if (cause instanceof ServiceException) {
                                    logPiveau.error("Indexing failure: {}", ((ServiceException) cause).getDebugInfo().encode(), cause);
                                }
                            });
                }
                if (validationConfig.getBoolean("enabled", Defaults.VALIDATOR_ENABLED)) {
                    systemPipes(datasetHelper.get());
                }
                if (archivingEnabled) {
                    piveauContext.extend(datasetHelper.get().piveauId()).log().info("[Dataset] ["+catalogueId+"] [" + status.getString("status") + "] " + Piveau.toRdfXml(datasetHelper.get().model()));
                }
                promise.complete(status);
            }).onFailure(cause -> {
                piveauContext.extend(originalId).log().error("Error storing dataset", cause);
                if (cause instanceof ServiceException) {
                    promise.fail(cause);
                } else {
                    promise.fail(new ServiceException(500, cause.getMessage()));
                }
            });
        });
    }

    @Override
    public Future<JsonObject> postDataset(String content, String contentType, String catalogueId, Boolean createAccessURLs) {
        return putDatasetOrigin(UUID.randomUUID().toString(), content, contentType, catalogueId, createAccessURLs);
    }

    @Override
    public Future<Void> deleteDataset(String id) {
        return delete(DCATAPUriSchema.createFor(id));
    }

    @Override
    public Future<Void> deleteDatasetOrigin(String originId, String catalogueId) {
        return Future.future(promise ->
                catalogueManager.exists(catalogueId)
                        .compose(exists -> {
                            if (exists) {
                                return datasetManager.identify(originId, catalogueId);
                            } else {
                                return Future.failedFuture(new ServiceException(404, "Catalogue not found"));
                            }
                        })
                        .onSuccess(pair -> {
                            delete(DCATAPUriSchema.parseUriRef(pair.getFirst().getURI())).onComplete(promise);

                        })
                        .onFailure(cause -> {
                            if (cause.getMessage().equals("Not found")) {
                                promise.fail(new ServiceException(404, "Dataset not found"));
                            } else {
                                promise.fail(cause);
                            }
                        })
        );
    }

    private Future<Void> delete(DCATAPUriRef schema) {
        return Future.future(promise -> datasetManager.deleteGraph(schema.getDatasetGraphName())
                .compose(v -> datasetManager.catalogue(schema.getDatasetUriRef()))
                .compose(catalogue -> catalogueManager.removeDatasetEntry(catalogue.getURI(), schema.getDatasetUriRef()))
                .onSuccess(v -> {
                    if (indexService != null) {
                        indexService.deleteDataset(schema.getId(), ar -> {
                            if (ar.failed()) {
                                logger.warn("Delete index", ar.cause());
                            }
                        });
                    }
                    if(validationConfig.getBoolean("enabled", Defaults.VALIDATOR_ENABLED)) {
                        tripleStore.deleteGraph(schema.getMetricsGraphName())
                                .onFailure(cause -> logger.warn("Delete metrics graph", cause));

                        shadowTripleStore.deleteGraph(schema.getHistoricMetricsGraphName())
                                .onFailure(cause -> logger.warn("Delete metrics history graph", cause));
                    }
                    promise.complete();
                })
                .onFailure(cause -> {
                    if (cause.getMessage().startsWith("Not found")) {
                        promise.fail(new ServiceException(404, "Dataset not found"));
                    } else {
                        promise.fail(cause);
                    }
                })
        );
    }

    @Override
    public Future<JsonObject> indexDataset(String originId, String catalogueId) {
        return Future.future(promise -> {
            if (indexService == null) {
                promise.fail("Indexing service disabled");
            } else {
                String contentType = "application/n-triples";
                getDatasetOrigin(originId, catalogueId, contentType)
                        .onSuccess(content -> DatasetHelper.create(originId, content, contentType, catalogueId)
                                .onSuccess(helper -> catalogueManager.getStripped(catalogueId)
                                        .onSuccess(catalogueModel -> {
                                            Concept language = Languages.INSTANCE
                                                    .getConcept(catalogueModel.getProperty(helper.resource(), DCTerms.language).getResource());
                                            String lang = Languages.INSTANCE.iso6391Code(language);
                                            JsonObject indexObject = Indexing.indexingDataset(helper.resource(), helper.recordResource(), catalogueId, lang);
                                            indexService.addDatasetPut(indexObject, ir -> {
                                                if (ir.failed()) {
                                                    promise.fail(ir.cause());
                                                } else {
                                                    promise.complete(new JsonObject());
                                                }
                                            });
                                        })
                                        .onFailure(promise::fail)
                                )
                                .onFailure(promise::fail)
                        )
                        .onFailure(promise::fail);
            }
        });
    }

    @Override
    public Future<JsonObject> getDataUploadInformation(String datasetId, String catalogueId, String resultDataset) {
        return Future.future(promise -> DatasetHelper.create(resultDataset, Lang.NTRIPLES.getHeaderString())
                .onSuccess(helper -> {
                    JsonArray uploadResponse = dataUploadConnector.getResponse(helper);
                    JsonObject result = new JsonObject();
                    result.put("status", "success");
                    result.put("distributions", uploadResponse);
                    promise.complete(result);
                }).onFailure(promise::fail));
    }

    private Future<List<JsonObject>> getRecordHash(DatasetHelper helper) {
        Promise<List<JsonObject>> promise = Promise.promise();
        String query = "SELECT DISTINCT ?record ?hash WHERE { GRAPH <" +
                helper.catalogueGraphName() +
                "> { <" +
                helper.catalogueUriRef() +
                "> <" +
                DCAT.record +
                "> ?record } GRAPH ?d { ?record <" +
                DCTerms.identifier +
                "> \"" +
                helper.originId() +
                "\" OPTIONAL { ?record <" +
                SPDX.checksum +
                ">/<" +
                SPDX.checksumValue +
                "> ?hash } } }";

        tripleStore.select(query)
                .onSuccess(resultSet -> {
                    List<JsonObject> records = new ArrayList<>();
                    while (resultSet.hasNext()) {
                        QuerySolution solution = resultSet.next();
                        JsonObject info = new JsonObject().put("recordUri", solution.getResource("record").getURI());
                        if (solution.contains("hash")) {
                            info.put("hash", solution.getLiteral("hash").getLexicalForm());
                        }
                        records.add(info);
                    }
                    promise.complete(records);
                })
                .onFailure(promise::fail);

        return promise.future();
    }

    private Future<JsonObject> store(DatasetHelper helper) {
        Promise<JsonObject> promise = Promise.promise();
        boolean clearGeoData = clearGeoDataCatalogues.contains(helper.catalogueId()) || clearGeoDataCatalogues.contains("*");
        datasetManager.setGraph(helper.graphName(), helper.model(), clearGeoData).onComplete(ar -> {
            if (ar.succeeded()) {
                switch (ar.result()) {
                    case "created" -> promise.complete(new JsonObject()
                            .put("status", "created")
                            .put("id", helper.piveauId())
                            .put("dataset", helper.stringify(Lang.NTRIPLES))
                            .put(HttpHeaders.LOCATION.toString(), helper.uriRef()));
                    case "updated" -> promise.complete(new JsonObject().put("status", "updated"));
                    default -> promise.fail(ar.result());
                }
            } else {
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    private Future<DatasetHelper> index(DatasetHelper helper) {
        Promise<DatasetHelper> datasetIndexed = Promise.promise();
        try {
            JsonObject indexMessage = Indexing.indexingDataset(helper.resource(), helper.recordResource(), helper.catalogueId(), helper.sourceLang());
            indexService.addDatasetPut(indexMessage, ar -> {
                if (ar.succeeded()) {
                    datasetIndexed.complete(helper);
                } else {
                    datasetIndexed.fail(ar.cause());
                }
            });
        } catch (Exception e) {
            datasetIndexed.fail(new ServiceException(500, e.getMessage()));
        }

        return datasetIndexed.future();
    }

    private void systemPipes(DatasetHelper helper) {
        String metricsPipe = validationConfig.getString("metricsPipeName", Defaults.METRICS_PIPE);
        if (launcher.isPipeAvailable(metricsPipe)) {
            JsonObject dataInfo = new JsonObject()
                    .put("originalId", helper.originId())
                    .put("catalogue", helper.catalogueId())
                    .put("identifier", helper.piveauId())
                    .put("source", helper.sourceType());

            Dataset dataset = DatasetFactory.create(helper.model());
            launcher.runPipeWithData(metricsPipe, Piveau.presentAs(dataset, Lang.TRIG), RDFMimeTypes.TRIG, dataInfo, new JsonObject(), null);
        }
    }

    private Future<DatasetHelper> createDataset(DatasetHelper datasetHelper, boolean createAccessURLs) {
        Promise<DatasetHelper> promise = Promise.promise();
        findPiveauId(datasetHelper.piveauId(), 0, ar -> {
            if (ar.succeeded()) {
                datasetHelper.init(ar.result());

                if (createAccessURLs) {
                    datasetHelper.setAccessURLs(dataUploadConnector);
                }

                catalogueManager.addDatasetEntry(
                                datasetHelper.catalogueGraphName(),
                                datasetHelper.catalogueUriRef(),
                                datasetHelper.uriRef(),
                                datasetHelper.recordUriRef())
                        .onFailure(cause -> {
                            logger.error("Add catalogue entry for {}", datasetHelper.piveauId(), cause);
                            if (cause instanceof TripleStoreException te) {
                                if (!te.getQuery().isBlank()) {
                                    logger.error("Query: {}", te.getQuery());
                                }
                            }
                        });

                if (translationService != null) {
                    Promise<DatasetHelper> translationPromise = Promise.promise();
                    translationService.initializeTranslationProcess(datasetHelper, null, false, translationPromise);
                    translationPromise.future()
                            .onSuccess(promise::complete)
                            .onFailure(cause -> promise.complete(datasetHelper));
                } else {
                    promise.complete(datasetHelper);
                }
            } else {
                promise.fail(new ServiceException(500, ar.cause().getMessage()));
            }
        });
        return promise.future();
    }

    private Future<DatasetHelper> updateDataset(DatasetHelper datasetHelper, String recordUriRef) {
        return Future.future(promise -> datasetManager.getGraph(DCATAPUriSchema.parseUriRef(recordUriRef).getDatasetGraphName())
                .onSuccess(model -> {
                    datasetHelper.update(model, recordUriRef);

                    if (translationService != null) {
                        DatasetHelper.create(Piveau.presentAs(model, Lang.NTRIPLES), Lang.NTRIPLES.getContentType().getContentTypeStr())
                                .compose(oldHelper -> Future.<DatasetHelper>future(trans -> translationService.initializeTranslationProcess(datasetHelper, oldHelper, false, trans)))
                                .onSuccess(promise::complete)
                                .onFailure(cause -> promise.complete(datasetHelper));
                    } else {
                        promise.complete(datasetHelper);
                    }
                })
                .onFailure(cause -> promise.fail(new ServiceException(500, cause.getMessage(), new JsonObject()
                        .put("recordUriRef", recordUriRef)
                        .put("datasetHelper", datasetHelper.toJson()))))
        );
    }

    private void findPiveauId(String id, int counter, Handler<AsyncResult<String>> handler) {
        AtomicReference<String> checkId = new AtomicReference<>();
        if (counter > 0) {
            checkId.set(id + "~~" + counter);
        } else {
            checkId.set(id);
        }
        tripleStore.ask("ASK WHERE { GRAPH ?catalogue { ?catalogue <" + DCAT.dataset + "> <" + DCATAPUriSchema.createForDataset(checkId.get()).getUriRef() + "> } }")
                .onSuccess(exist -> {
                    if (exist) {
                        findPiveauId(id, counter + 1, handler);
                    } else {
                        handler.handle(Future.succeededFuture(checkId.get()));
                    }
                }).onFailure(cause -> handler.handle(Future.failedFuture(cause)));
    }

    private Future<JsonObject> catalogueInfo(String catalogueId) {
        return Future.future(promise -> {
            JsonObject catalogueInfo = cache.get(catalogueId);
            if (catalogueInfo == null) {
                DCATAPUriRef catalogueUriRef = DCATAPUriSchema.createForCatalogue(catalogueId);
                tripleStore.select(String.format(catalogueQueryTemplate, catalogueUriRef.getGraphName(), catalogueUriRef.getUriRef()))
                        .onSuccess(resultSet -> {
                            if (resultSet.hasNext()) {
                                JsonObject result = new JsonObject();
                                QuerySolution solution = resultSet.next();
                                if (solution.contains("lang")) {
                                    Concept concept = Languages.INSTANCE.getConcept(solution.getResource("lang"));
                                    if (concept != null) {
                                        String langCode = Languages.INSTANCE.iso6391Code(concept);
                                        if (langCode == null) {
                                            langCode = Languages.INSTANCE.tedCode(concept);
                                        }
                                        if (langCode != null) {
                                            result.put("langCode", langCode.toLowerCase());
                                        }
                                    }
                                }
                                if (solution.contains("type")) {
                                    result.put("type", solution.getLiteral("type").getLexicalForm());
                                }
                                if (solution.contains("visibility")) {
                                    result.put("visibility", solution.getResource("visibility").getLocalName());
                                }
                                cache.putIfAbsent(catalogueId, result);
                                promise.complete(result);
                            } else {
                                promise.fail(new ServiceException(404, "Catalogue not found"));
                            }
                        })
                        .onFailure(promise::fail);
            } else {
                promise.complete(catalogueInfo);
            }
        });
    }

}
