package io.piveau.hub.search.IntegrationTest;

import io.piveau.hub.search.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing the Quality Meas Scoring")
@ExtendWith(VertxExtension.class)
class QualityMeasScoringIntegrationTest {

    private WebClient client;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        client = WebClient.create(vertx);

        vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions())
                .compose(id -> vertx.fileSystem().readFile("test/mock/catalogue/mock_0.json"))
                .compose(buffer -> {
                    JsonArray catalogues = buffer.toJsonArray();
                    List<Future<HttpResponse<Buffer>>> futures = new ArrayList<>();
                    catalogues.stream()
                            .map(obj -> (JsonObject)obj)
                            .forEach(catalogue -> {
                                futures.add(client.putAbs("http://localhost:8080/catalogues/" + catalogue.getString("id"))
                                        .putHeader("Content-Type", "application/json")
                                        .putHeader("Authorization", "########-####-####-####-############")
                                        .sendJson(catalogue));
                            });
                    return Future.all(futures);
                })
                .compose(cf -> {
                    List<Future<HttpResponse<Buffer>>> futures = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        futures.add(vertx.fileSystem().readFile("test/mock/dataset/mock_" + i + ".json")
                                .compose(buffer -> {
                                    JsonArray array = buffer.toJsonArray();
                                    JsonObject bulk = new JsonObject().put("datasets", array);
                                    return client.putAbs("http://localhost:8080/bulk/datasets/")
                                            .putHeader("Content-Type", "application/json")
                                            .putHeader("Authorization", "########-####-####-####-############")
                                            .sendJson(bulk);                                }));
                    }
                    return Future.all(futures);
                })
                .onSuccess(cf -> testContext.completeNow())
                .onFailure(testContext::failNow);
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
    @DisplayName("Test Check Quality Meas Scoring Facets")
    void testCheckQualityMeasScoringFacets(VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(2);
        client.getAbs("http://localhost:8080/search?filter=dataset").send(ar -> {
            if (ar.succeeded()) {
                JsonObject result = ar.result().bodyAsJsonObject().getJsonObject("result");
                JsonArray facets = result.getJsonArray("facets");
                Integer countFacets = 0;
                for (Object obj : facets) {
                    JsonObject facet = (JsonObject) obj;
                    if (facet.getString("id").equals("scoring")) {
                        assertEquals("Scoring", facet.getString("title"));
                        assertNotNull(facet.getJsonArray("items"));
                        JsonArray items = facet.getJsonArray("items");
                        System.out.println(items.encodePrettily());
                        for (Object item : items) {
                            JsonObject itemJson = (JsonObject) item;

                            if (itemJson.getString("id").equals("minScoring")) {
                                countFacets++;
                                assertEquals("Minimum Scoring", itemJson.getString("title"));
                                assertEquals(50, itemJson.getInteger("min"));
                            }

                            if (itemJson.getString("id").equals("maxScoring")) {
                                countFacets++;
                                assertEquals("Maximum Scoring", itemJson.getString("title"));
                                assertEquals(400, itemJson.getInteger("max"));
                            }

                            if (itemJson.getString("id").equals("badScoring")) {
                                countFacets++;
                                assertEquals("Bad Scoring", itemJson.getString("title"));
                                assertEquals(250, itemJson.getInteger("count"));
                            }

                            if (itemJson.getString("id").equals("sufficientScoring")) {
                                countFacets++;
                                assertEquals("Sufficient Scoring", itemJson.getString("title"));
                                assertEquals(250, itemJson.getInteger("count"));
                            }

                            if (itemJson.getString("id").equals("goodScoring")) {
                                countFacets++;
                                assertEquals("Good Scoring", itemJson.getString("title"));
                                assertEquals(375, itemJson.getInteger("count"));
                            }

                            if (itemJson.getString("id").equals("excellentScoring")) {
                                countFacets++;
                                assertEquals("Excellent Scoring", itemJson.getString("title"));
                                assertEquals(125, itemJson.getInteger("count"));
                            }
                        }
                    }
                }
                assertEquals(6, countFacets);
                checkpoint.flag();
            } else {
                ar.cause().printStackTrace();
                testContext.failNow(ar.cause());
            }
        });
        checkpoint.flag();
    }
}