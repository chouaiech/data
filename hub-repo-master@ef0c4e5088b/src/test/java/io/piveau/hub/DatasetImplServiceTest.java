package io.piveau.hub;

import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.services.datasets.DatasetsServiceVerticle;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.services.metrics.MetricsService;
import io.piveau.hub.services.metrics.MetricsServiceVerticle;
import io.piveau.hub.services.translation.TranslationService;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.test.MockTripleStore;
import io.piveau.utils.JenaUtils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the datasets service")
@ExtendWith(VertxExtension.class)
class DatasetImplServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetImplServiceTest.class);
    private static String exampleDataset;
    private final String catalogueID = "test-catalog";
    private DatasetsService datasetsService;
    private MetricsService metricsService;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        DeploymentOptions options = new DeploymentOptions()
                .setWorker(true)
                .setConfig(new JsonObject()
                        .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig())
                        .put(Constants.ENV_PIVEAU_HUB_VALIDATOR, new JsonObject().put("url", "localhost").put("enabled", false))
                        .put(Constants.ENV_PIVEAU_TRANSLATION_SERVICE, new JsonObject().put("enable", false)));

//        DCATAPUriSchema.setConfig(new JsonObject()
//                .put("baseUri", "https://example.com"));

        Checkpoint checkpoint = testContext.checkpoint(4);

        vertx.fileSystem().readFile("example_external_dataset.ttl", readResult -> {
            if (readResult.succeeded()) {
                exampleDataset = readResult.result().toString();
                checkpoint.flag();
            } else {
                testContext.failNow(readResult.cause());
            }
        });

        new MockTripleStore()
                .loadGraph("https://piveau.io/id/catalogue/test-catalog", "example_empty_catalog.ttl")
                .deploy(vertx)
                .onSuccess(v -> checkpoint.flag())
                .onFailure(testContext::failNow);

        vertx.deployVerticle(DatasetsServiceVerticle.class.getName(), options, ar -> {
            if (ar.succeeded()) {
                datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
                checkpoint.flag();
            } else {
                testContext.failNow(ar.cause());
            }
        });
        vertx.deployVerticle(MetricsServiceVerticle.class.getName(), options, ar -> {
            if (ar.succeeded()) {
                metricsService = MetricsService.createProxy(vertx, MetricsService.SERVICE_ADDRESS);
                checkpoint.flag();
            } else {
                testContext.failNow(ar.cause());
            }
        });

        vertx.eventBus().consumer(IndexService.SERVICE_ADDRESS, message -> {
            message.reply(new JsonObject());
        });
        vertx.eventBus().<JsonObject>consumer(TranslationService.SERVICE_ADDRESS, message -> {
            message.reply(message.body().getJsonObject("helper"));
        });
    }

    @Test
    @DisplayName("Update example dataset")
    void testUpdateExampleDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "update-test-dataset";

        datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .compose(result -> datasetsService.getDatasetOrigin(datasetID, catalogueID, "text/turtle"))
                .compose(result -> datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false))
                .onSuccess(result -> testContext.failNow("Expected failure"))
                .onFailure(cause -> {
                    testContext.verify(() -> {
                        assertEquals("Dataset is up to date", cause.getMessage());
                        testContext.completeNow();
                    });
                });
    }


    @Test
    @DisplayName("Create an example dataset")
    void testCreateExampleDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "create-test-dataset";

        datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .onSuccess(result -> testContext.verify(() -> {
                    assertEquals("created", result.getString("status", ""));
                    String location = result.getString(HttpHeaders.LOCATION.toString());
                    assertNotNull(location);
                    assertEquals(DCATAPUriSchema.applyFor(datasetID).getDatasetUriRef(), location);
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Delete an example dataset (inclusively Dqv)")
    void testDeleteExampleDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "delete-test-dataset";

        datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .onSuccess(result -> {
                    Buffer buffer = vertx.fileSystem().readFileBlocking("example_metric_1.ttl");
                    metricsService.putMetrics("https://piveau.io/set/data/delete-test-dataset", buffer.toString(), RDFMimeTypes.TRIG)
                            .onSuccess(result2 -> {
                                datasetsService.deleteDatasetOrigin(datasetID, catalogueID)
                                        .compose(v -> datasetsService.getDatasetOrigin(datasetID, catalogueID, RDFMimeTypes.TURTLE))
                                        .onSuccess(status -> testContext.failNow("Dataset still available"))
                                        .onFailure(cause -> {
                                            testContext.verify(() -> {
                                                assertTrue(cause instanceof ServiceException);
                                                assertEquals(404, ((ServiceException) cause).failureCode());
                                            });
                                            metricsService.getMetrics(datasetID, false, RDFMimeTypes.TURTLE)
                                                    .onSuccess(content -> testContext.failNow(new Throwable("Metrics still available")))
                                                    .onFailure(cause2 -> {
                                                        testContext.verify(() -> {
                                                            assertNotNull(cause2);
                                                            assertInstanceOf(ServiceException.class, cause2);
                                                            assertEquals(404, ((ServiceException) cause2).failureCode());
                                                        });
                                                        testContext.completeNow();
                                                    });
                                        });
                            })
                            .onFailure(testContext::failNow);
                })
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Receive an example dataset")
    void testGetExampleDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetId = "get-test-dataset";
        datasetsService.putDatasetOrigin(datasetId, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .compose(result -> datasetsService.getDatasetOrigin(datasetId, catalogueID, RDFMimeTypes.TURTLE))
                .onSuccess(content -> testContext.verify(() -> {
                    assertNotNull(content);
                    DatasetHelper.create(content, RDFMimeTypes.TURTLE, modelResult -> {
                        assertTrue(modelResult.succeeded());
                        assertEquals(datasetId, modelResult.result().piveauId());
                        testContext.completeNow();
                    });
                }))
                .onFailure(testContext::failNow);
    }

    //    @Test
    @DisplayName("Receive an example dataset from normalized id")
    void testGetExampleDatasetNormalizedID(Vertx vertx, VertxTestContext testContext) {
        String datasetId = "test-Get-normalized-Dataset .id";
        datasetsService.putDatasetOrigin(datasetId, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .compose(result -> datasetsService.getDataset(DCATAPUriSchema.applyFor(datasetId).getId(), RDFMimeTypes.TURTLE))
                .onSuccess(content -> testContext.verify(() -> {
                    Model jenaModel = JenaUtils.read(content.getBytes(), RDFMimeTypes.TURTLE);
                    assertNotNull(jenaModel);

                    Resource cat = jenaModel.getResource(DCATAPUriSchema.applyFor(datasetId).getDatasetUriRef());
                    assertNotNull(cat);
                    assertNotNull(cat.getProperty(RDF.type));
                    assertEquals(DCAT.Dataset, cat.getProperty(RDF.type).getObject());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Delete a non existing dataset")
    void testDeleteMissingDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetId = "delete-missing-test-dataset";
        testContext.assertFailure(datasetsService.deleteDatasetOrigin(datasetId, catalogueID));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Counting one dataset")
    void testCountOneDataset(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "count-one-test-dataset";
        datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .compose(result -> datasetsService.listCatalogueDatasets("application/json", "originalIds", catalogueID, 20, 0))
                .onSuccess(content -> {
                    JsonArray result = new JsonArray(content);
                    testContext.verify(() -> {
                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertEquals(1, result.size());
                        assertEquals(datasetID, result.getString(0));
                        testContext.completeNow();
                    });
                })
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Counting two datasets")
    void testCountTwoDatasetsInOneCat(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "count-two-test-dataset";
        datasetsService.putDatasetOrigin(datasetID, exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false)
                .compose(result -> datasetsService.putDatasetOrigin(datasetID + "2", exampleDataset, RDFMimeTypes.TURTLE, catalogueID, false))
                .compose(result -> datasetsService.listCatalogueDatasets("application/json", "originalIds", catalogueID, 20, 0))
                .onSuccess(content -> {
                    JsonArray result = new JsonArray(content);
                    testContext.verify(() -> {
                        assertNotNull(result);
                        assertFalse(result.isEmpty());
                        assertEquals(2, result.size());
                        testContext.completeNow();
                    });
                })
                .onFailure(testContext::failNow);
    }

    //TODO: create Dataset & check if distribution is renamed
    //TODO: add update dataset with a dataset that has an additional Dist (without dct:idenifier) & dist is correctly renamed
    //TODO: add update dataset with a dataset that has an additional Dist (with dct:idenifier)  & dist is correctly renamed
    //TODO: list from empty catalogue, list from nonexisting catalog, (list sources, list datasets )x(with&, without catalog)

}
