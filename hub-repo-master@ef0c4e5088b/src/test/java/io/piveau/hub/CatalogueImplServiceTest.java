package io.piveau.hub;

import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.hub.services.catalogues.CataloguesService;
import io.piveau.hub.services.catalogues.CataloguesServiceVerticle;
import io.piveau.rdf.Piveau;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.test.MockTripleStore;
import io.piveau.utils.JenaUtils;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the catalogue service")
@ExtendWith(VertxExtension.class)
class CatalogueImplServiceTest {

    private CataloguesService cataloguesService;

    private String exampleCatalogue;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {

        DeploymentOptions options = new DeploymentOptions()
                .setWorker(true)
                .setConfig(new JsonObject()
                        .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig()));

        Checkpoint checkpoint = testContext.checkpoint(3);

        new MockTripleStore().deploy(vertx).onSuccess(v -> checkpoint.flag()).onFailure(testContext::failNow);

        vertx.deployVerticle(CataloguesServiceVerticle.class.getName(), options, ar -> {
            if (ar.succeeded()) {
                cataloguesService = CataloguesService.createProxy(vertx, CataloguesService.SERVICE_ADDRESS);
                checkpoint.flag();
            } else {
                testContext.failNow(ar.cause());
            }
        });

        vertx.fileSystem().readFile("example_empty_catalog.ttl", ar -> {
            if (ar.succeeded()) {
                exampleCatalogue = ar.result().toString();
                checkpoint.flag();
            } else {
                testContext.failNow(ar.cause());
            }
        });

        vertx.eventBus().consumer("io.piveau.hub.index.queue", message -> {
            message.reply(new JsonObject());
        });
        vertx.eventBus().consumer("io.piveau.hub.translationservice.queue", message -> {
            message.reply(new JsonObject());
        });
    }

    @Test
    @DisplayName("Create an example catalogue")
    void testCreateExampleCatalog(Vertx vertx, VertxTestContext testContext) {
        cataloguesService.putCatalogue("create-test-catalogue", exampleCatalogue, RDFMimeTypes.TURTLE)
                .onSuccess(result -> {
                    testContext.verify(() -> assertEquals("created", result));
                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Receive an example catalogue")
    void testGetExampleCatalog(Vertx vertx, VertxTestContext testContext) {
        cataloguesService.putCatalogue("get-test-catalogue", exampleCatalogue, RDFMimeTypes.TURTLE)
                .compose(result -> cataloguesService.getCatalogue("get-test-catalogue", RDFMimeTypes.TURTLE))
                .onSuccess(result -> testContext.verify(() -> {
                            assertNotNull(result);
                            Model jenaModel = JenaUtils.read(result.getBytes(), RDFMimeTypes.TURTLE);
                            assertNotNull(jenaModel);

                            Resource cat = jenaModel.getResource(DCATAPUriSchema.applyFor("get-test-catalogue").getCatalogueUriRef());
                            assertNotNull(cat);
                            assertNotNull(cat.getProperty(RDF.type));
                            assertEquals(DCAT.Catalog, cat.getProperty(RDF.type).getObject());
                            testContext.completeNow();
                        })
                )
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Delete an example catalogue")
    void testDeleteExampleCatalog(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "delete-test-catalogue";
        cataloguesService.putCatalogue(datasetID, exampleCatalogue, RDFMimeTypes.TURTLE)
                .compose(result -> cataloguesService.deleteCatalogue(datasetID))
                .compose(v -> cataloguesService.getCatalogue(datasetID, RDFMimeTypes.TURTLE))
                .onSuccess(content -> testContext.failNow("Still exists"))
                .onFailure(cause -> testContext.verify(() -> {
                    assertTrue(cause instanceof ServiceException);
                    assertEquals(404, ((ServiceException) cause).failureCode());
                    testContext.completeNow();
                }));
    }

    @Test
    @DisplayName("Delete an non existing catalogue")
    void testDeleteMissingCatalog(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "delete-missing-test-catalogue";
        testContext.assertFailure(cataloguesService.deleteCatalogue(datasetID));
        testContext.completeNow();
    }

    @Test
    @DisplayName("Counting zero catalogues")
    void testCountZeroCatalogs(Vertx vertx, VertxTestContext testContext) {
        cataloguesService.listCatalogues(RDFMimeTypes.NTRIPLES, "metadata", 0, 100)
                .onSuccess(content -> testContext.verify(() -> {
                    Model model = Piveau.toModel(content.getBytes(), RDFMimeTypes.NTRIPLES);
                    ResIterator it = model.listResourcesWithProperty(RDF.type, DCAT.Catalog);
                    assertTrue(it.toList().isEmpty());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Counting one catalogue")
    void testCountOneCatalog(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "count-one-test-catalogue";
        cataloguesService.putCatalogue(datasetID, exampleCatalogue, RDFMimeTypes.TURTLE)
                .compose(result -> cataloguesService.listCatalogues(RDFMimeTypes.NTRIPLES, "metadata", 0, 100))
                .onSuccess(content -> testContext.verify(() -> {
                    Model model = Piveau.toModel(content.getBytes(), RDFMimeTypes.NTRIPLES);
                    ResIterator it = model.listResourcesWithProperty(RDF.type, DCAT.Catalog);
                    assertEquals(1, it.toList().size());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Counting two catalogues")
    void testCountTwoCatalogs(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "count-two-test-catalogue";
        cataloguesService.putCatalogue(datasetID, exampleCatalogue, RDFMimeTypes.TURTLE)
                .compose(result -> cataloguesService.putCatalogue(datasetID + "2", exampleCatalogue, RDFMimeTypes.TURTLE))
                .compose(result -> cataloguesService.listCatalogues("application/json", "identifiers", 0, 100))
                .onSuccess(content -> testContext.verify(() -> {
                    JsonArray list = new JsonArray(content);
                    assertEquals(2, list.size());
                    testContext.completeNow();
                    Thread.sleep(1); // Don't know why, but this prevents exception
                }))
                .onSuccess(testContext::failNow);
    }

    @Test
    @DisplayName("Counting one thousand catalogues")
    void testCountOneThousandCatalogs(Vertx vertx, VertxTestContext testContext) {
        String datasetID = "count-hundred-test-catalogues";

        int numberToCount = 1000;

        ArrayList<Future<String>> futureList = new ArrayList<>();

        for (int i = 0; i < numberToCount; i++) {
            Promise<String> promise = Promise.promise();
            futureList.add(promise.future());
            cataloguesService.putCatalogue(datasetID + "-" + i, exampleCatalogue, RDFMimeTypes.TURTLE).onComplete(promise);
        }

        CompositeFuture.join(new ArrayList<>(futureList))
                .compose(j -> cataloguesService.listCatalogues("application/json", "identifiers", 0, 1500))
                .onSuccess(content -> testContext.verify(() -> {
                    JsonArray list = new JsonArray(content);
                    assertEquals(numberToCount, list.size());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

}
