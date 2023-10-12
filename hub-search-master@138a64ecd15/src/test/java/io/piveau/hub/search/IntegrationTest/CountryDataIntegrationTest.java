package io.piveau.hub.search.IntegrationTest;

import io.piveau.hub.search.MainVerticle;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Country Filtering")
@ExtendWith(VertxExtension.class)
class CountryDataIntegrationTest {

    private WebClient client;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(5);

        client = WebClient.create(vertx);

        vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions(), MainVerticleResult -> {
            if (MainVerticleResult.succeeded()) {
                vertx.fileSystem().readFile("test/mock/catalogue/mock_0.json", fileRes -> {
                    if (fileRes.succeeded()) {
                        JsonArray catalogues = new JsonArray(fileRes.result());
                        List<Future> cataloguesFuture = new ArrayList<>();
                        for (Object obj : catalogues) {
                            JsonObject catalogueJson = (JsonObject) obj;
                            Promise cataloguePromise = Promise.promise();
                            cataloguesFuture.add(cataloguePromise.future());
                            client.putAbs("http://localhost:8080/catalogues/" + catalogueJson.getString("id"))
                                    .putHeader("Content-Type", "application/json")
                                    .putHeader("Authorization", "########-####-####-####-############")
                                    .sendJson(catalogueJson, ar -> {
                                        if (ar.succeeded()) {
                                            cataloguePromise.complete();
                                        } else {
                                            ar.cause().printStackTrace();
                                            cataloguePromise.fail(ar.cause());
                                        }
                                    });
                        }
                        CompositeFuture.all(cataloguesFuture).onComplete(cataloguesFutureResult -> {
                            if (cataloguesFutureResult.succeeded()) {
                                List<Future> datasetsFuture = new ArrayList<>();
                                for (int i = 0; i < 8; i++) {
                                    Promise datasetPromise = Promise.promise();
                                    datasetsFuture.add(datasetPromise.future());
                                    vertx.fileSystem().readFile("test/mock/dataset/mock_" + i + ".json", datasetFileRes -> {
                                        if (datasetFileRes.succeeded()) {
                                            JsonArray datasets = new JsonArray(datasetFileRes.result());
                                            JsonObject datasetJson = new JsonObject().put("datasets", datasets);
                                            client.putAbs("http://localhost:8080/bulk/datasets/")
                                                    .putHeader("Content-Type", "application/json")
                                                    .putHeader("Authorization", "########-####-####-####-############")
                                                    .sendJson(datasetJson, ar -> {
                                                        if (ar.succeeded()) {
                                                            datasetPromise.complete();
                                                        } else {
                                                            ar.cause().printStackTrace();
                                                            datasetPromise.fail(ar.cause());
                                                        }
                                                    });

                                        } else {
                                            datasetFileRes.cause().printStackTrace();
                                            testContext.failNow(datasetFileRes.cause());
                                        }
                                    });
                                }
                                CompositeFuture.all(datasetsFuture).onComplete(datasetsFutureResult -> {
                                    if (datasetsFutureResult.succeeded()) {
                                        // wait for all datasets to be indexed
                                        vertx.setTimer(10000, ar -> checkpoint.flag());
                                    } else {
                                        datasetsFutureResult.cause().printStackTrace();
                                        testContext.failNow(datasetsFutureResult.cause());
                                    }
                                });
                                checkpoint.flag();
                            } else {
                                testContext.failNow(cataloguesFutureResult.cause());
                            }
                        });
                        checkpoint.flag();
                    } else {
                        fileRes.cause().printStackTrace();
                        testContext.failNow(fileRes.cause());
                    }
                    checkpoint.flag();
                });
            } else {
                testContext.failNow(MainVerticleResult.cause());
            }
        });
        checkpoint.flag();
    }

    @AfterEach
    void tearDown(Vertx vertx, VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(11);
        for(int i = 1; i <= 10; ++i) {
            client.deleteAbs("http://localhost:8080/catalogues/catalog-" + i)
                    .putHeader("Authorization", "########-####-####-####-############").send(ar -> {
                if (ar.succeeded()) {
                    checkpoint.flag();
                } else {
                    ar.cause().printStackTrace();
                    testContext.failNow(ar.cause());
                }
            });
        }
        // wait for all catalogues + datasets to be deleted
        vertx.setTimer(10000, ar -> checkpoint.flag());
    }

    @Test
    @DisplayName("Test with countryData=null and globalAggregation=true")
    void testWithCountryDataNullAndGlobalAggregationTrue(VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(2);
        client.getAbs("http://localhost:8080/search?filter=dataset&&globalAggregation=true").send(ar -> {
            if (ar.succeeded()) {
                JsonObject result = ar.result().bodyAsJsonObject().getJsonObject("result");
                JsonArray facets = result.getJsonArray("facets");
                assertEquals(1000, result.getInteger("count"));
                for (Object obj : facets) {
                    JsonObject facet = (JsonObject) obj;

                    if (facet.getString("id").equals("country")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertTrue(itemsIds.contains("DEU"));
                        assertTrue(itemsIds.contains("ITA"));
                        assertTrue(itemsIds.contains("io"));
                        assertTrue(itemsIds.contains("eu"));
                    }

                    if (facet.getString("id").equals("catalog")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertTrue(itemsIds.contains("catalog-1"));
                        assertTrue(itemsIds.contains("catalog-2"));
                        assertTrue(itemsIds.contains("catalog-4"));
                        assertTrue(itemsIds.contains("catalog-4"));
                        assertTrue(itemsIds.contains("catalog-5"));
                    }
                }
                checkpoint.flag();
            } else {
                ar.cause().printStackTrace();
                testContext.failNow(ar.cause());
            }
        });
        checkpoint.flag();
    }

    @Test
    @DisplayName("Test with countryData=true and globalAggregation=true")
    void testWithCountryDataTrueAndGlobalAggregationTrue(VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(2);
        client.getAbs("http://localhost:8080/search?filter=dataset&countryData=true&&globalAggregation=true").send(ar -> {
            if (ar.succeeded()) {
                JsonObject result = ar.result().bodyAsJsonObject().getJsonObject("result");
                JsonArray facets = result.getJsonArray("facets");
                assertEquals(579, result.getInteger("count"));
                for (Object obj : facets) {
                    JsonObject facet = (JsonObject) obj;

                    if (facet.getString("id").equals("country")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertTrue(itemsIds.contains("DEU"));
                        assertTrue(itemsIds.contains("ITA"));
                        assertFalse(itemsIds.contains("eu"));
                        assertFalse(itemsIds.contains("io"));
                    }

                    if (facet.getString("id").equals("catalog")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertFalse(itemsIds.contains("catalog-1"));
                        assertFalse(itemsIds.contains("catalog-2"));
                        assertTrue(itemsIds.contains("catalog-4"));
                        assertTrue(itemsIds.contains("catalog-4"));
                        assertTrue(itemsIds.contains("catalog-5"));
                    }
                }
                checkpoint.flag();
            } else {
                ar.cause().printStackTrace();
                testContext.failNow(ar.cause());
            }
        });
        checkpoint.flag();
    }

    @Test
    @DisplayName("Test with countryData=false and globalAggregation=true")
    void testWithCountryDataFalseAndGlobalAggregationTrue(VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(2);
        client.getAbs("http://localhost:8080/search?filter=dataset&countryData=false&&globalAggregation=true").send(ar -> {
            if (ar.succeeded()) {
                JsonObject result = ar.result().bodyAsJsonObject().getJsonObject("result");
                JsonArray facets = result.getJsonArray("facets");
                assertEquals(421, result.getInteger("count"));
                for (Object obj : facets) {
                    JsonObject facet = (JsonObject) obj;

                    if (facet.getString("id").equals("country")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertFalse(itemsIds.contains("DEU"));
                        assertFalse(itemsIds.contains("ITA"));
                        assertTrue(itemsIds.contains("eu"));
                        assertTrue(itemsIds.contains("io"));
                    }

                    if (facet.getString("id").equals("catalog")) {
                        JsonArray items = facet.getJsonArray("items");
                        JsonArray itemsIds = new JsonArray();
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;
                            itemsIds.add(itemJson.getString("id"));
                        }
                        assertTrue(itemsIds.contains("catalog-1"));
                        assertTrue(itemsIds.contains("catalog-2"));
                        assertFalse(itemsIds.contains("catalog-4"));
                        assertFalse(itemsIds.contains("catalog-4"));
                        assertFalse(itemsIds.contains("catalog-5"));
                    }
                }
                checkpoint.flag();
            } else {
                ar.cause().printStackTrace();
                testContext.failNow(ar.cause());
            }
        });
        checkpoint.flag();
    }
}
