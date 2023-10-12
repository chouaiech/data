package io.piveau.metrics.cache;

import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.dqv.DqvVerticle;
import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.piveau.test.MockTripleStore;
import io.piveau.vocabularies.vocabulary.PV;
import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Timeout(value = 6, timeUnit = TimeUnit.MINUTES)
@DisplayName("Testing mock dqvProvider metric functions")
@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetricTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    private DqvProvider dqvProvider;

    @BeforeAll
    void setup(Vertx vertx, VertxTestContext testContext) {
        DCATAPUriSchema.INSTANCE.setConfig(new JsonObject().put("baseUri", "https://example.eu/"));
        QueryCollection.init(vertx, "queries");

        Checkpoint checkpoint = testContext.checkpoint(2);

        new MockTripleStore()
                .loadGraph("https://example.eu/id/catalogue/example-catalogue", "Catalogue-Graph.ttl")
                .loadGraph("https://example.eu/set/data/test-dataset-1", "test-dataset-1-graph.ttl")
                .loadGraph("https://example.eu/id/metrics/test-dataset-1", "test-dataset-1-metrics-graph.ttl")
                .loadGraph("https://example.eu/set/data/test-dataset-2", "test-dataset-2-graph.ttl")
                .loadGraph("https://example.eu/id/metrics/test-dataset-2", "test-dataset-2-metrics-graph.ttl")
                .deploy(vertx)
                .onSuccess(v -> checkpoint.flag())
                .onFailure(testContext::failNow);

        DeploymentOptions options = new DeploymentOptions()
                .setConfig(MockTripleStore.getDefaultConfig());

        vertx.deployVerticle(DqvVerticle.class, options, ar -> {
            if (ar.succeeded()) {
                dqvProvider = DqvProvider.createProxy(vertx, DqvProvider.SERVICE_ADDRESS, new DeliveryOptions().setSendTimeout(3000000));
                checkpoint.flag();
            } else {
                testContext.failNow(ar.cause());
            }
        });

    }

    @Test
    @DisplayName("get Scores")
    void getScores(Vertx vertx, VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(3);

        dqvProvider.getAverageScore("example-catalogue", DocumentScope.CATALOGUE, PV.scoring.getURI(), handler -> {
            if (handler.succeeded()) {
                try {
                    assertNotNull (handler.result());
                    assertEquals(173, handler.result());
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }
            } else {
                testContext.failNow(handler.cause());
            }
        });
        dqvProvider.getAverageScore("GBR", DocumentScope.COUNTRY, PV.scoring.getURI(), handler -> {
            if (handler.succeeded()) {
                try {
                    assertNotNull(handler.result());
                    assertEquals(173, handler.result());
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }
            } else {
                testContext.failNow(handler.cause());
            }
        });
        dqvProvider.getAverageScore("global", DocumentScope.GLOBAL, PV.scoring.getURI(), handler -> {
            if (handler.succeeded()) {
                try {
                    assertNotNull(handler.result());
                    assertEquals(173, handler.result());
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }
            } else {
                testContext.failNow(handler.cause());
            }
        });

    }


    @Test
    @DisplayName("get a Distribution Metric")
    void getDistMetric(Vertx vertx, VertxTestContext testContext) {

        Checkpoint checkpoint = testContext.checkpoint(3);

        dqvProvider.getFormatAvailability("example-catalogue", DocumentScope.CATALOGUE, handler -> {
            if (handler.succeeded()) {
                try {
                    assert (handler.result() != null);
                    assert (67 == Math.round(handler.result()));
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }

            } else {
                testContext.failNow(handler.cause());
            }
        });
        dqvProvider.getFormatAvailability("GBR", DocumentScope.COUNTRY, handler -> {
            if (handler.succeeded()) {
                try {
                    assert (handler.result() != null);
                    assert (67 == Math.round(handler.result()));
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }
            } else {
                testContext.failNow(handler.cause());
            }
        });
        dqvProvider.getFormatAvailability("global", DocumentScope.GLOBAL, handler -> {
            if (handler.succeeded()) {
                try {
                    assert (handler.result() != null);
                    assert (67 == Math.round(handler.result()));
                    checkpoint.flag();
                } catch (AssertionError assertionError) {
                    testContext.failNow(assertionError);
                }
            } else {
                testContext.failNow(handler.cause());
            }
        });


    }

    @Test
    @DisplayName("get violations")
    void getViolations(Vertx vertx, VertxTestContext testContext) {
        List<Future<JsonObject>> futures = new ArrayList<>();

        Promise<JsonObject> promiseViolations = Promise.promise();
        dqvProvider.getCatalogueViolations("example-catalogue", 0, 25, "de", promiseViolations);
        futures.add(promiseViolations.future());

        Promise<JsonObject> promiseCount = Promise.promise();
        dqvProvider.getCatalogueViolationsCount("example-catalogue", promiseCount);
        futures.add(promiseCount.future());

        //if futures calls are completed
        CompositeFuture.all(new ArrayList<>(futures)).onSuccess(compositeFuture -> {
            JsonObject resultViolations = promiseViolations.future().result();
            JsonObject resultCount = promiseCount.future().result();
            testContext.verify(() -> {
                assertEquals(15, resultCount.getInteger("count", -1));
                assertEquals(15, resultViolations.getJsonObject("result").getJsonArray("results").size());
                assertEquals("Title in german", resultViolations.getJsonObject("result").getJsonArray("results").getJsonObject(11).getString("title"));
            });
            testContext.completeNow();
        }).onFailure(testContext::failNow);
    }


    @Test
    @DisplayName("get reachability")
    void getReachability(Vertx vertx, VertxTestContext testContext) {
        dqvProvider.getDistributionReachabilityDetails("example-catalogue", 0, 4, "en", handler -> {
            if (handler.succeeded()) {
                JsonObject result = handler.result();
                log.debug(result.encodePrettily());
                testContext.verify(() -> {
                    assertEquals(1, result.getInteger("count", -1));
                    assertEquals(1, result.getJsonArray("results").size());
                    assertEquals(404, result.getJsonArray("results").getJsonObject(0).getInteger("accessUrlStatusCode"));
                    assertEquals("Test Dataset", result.getJsonArray("results").getJsonObject(0).getString("title"));
                });

                testContext.completeNow();
            } else {
                testContext.failNow(handler.cause());
            }
        });
    }

    @Test
    @DisplayName("Get reachability languages")
    void getReachabilityLang(Vertx vertx, VertxTestContext testContext) {
        Checkpoint checkpoint = testContext.checkpoint(2);

        //get results with an german title
        dqvProvider.getDistributionReachabilityDetails("example-catalogue", 0, 4, "de", handler -> {
            if (handler.succeeded()) {
                testContext.verify(() -> {
                    JsonObject result = handler.result();
                    assertEquals(1, result.getInteger("count", -1));
                    assertEquals(1, result.getJsonArray("results").size());
                    assertEquals("Title in german", result.getJsonArray("results").getJsonObject(0).getString("title"));
                });
                checkpoint.flag();
            } else {
                testContext.failNow(handler.cause());
            }
        });

        //try to get results with italian title, but this language does not exist
        dqvProvider.getDistributionReachabilityDetails("example-catalogue", 0, 4, "es", handler -> {
            if (handler.succeeded()) {
                testContext.verify(() ->{
                    JsonObject result = handler.result();
                    assertEquals(1, result.getInteger("count", -1));
                    assertEquals(1, result.getJsonArray("results").size());
                    assertEquals("Title in spanish", result.getJsonArray("results").getJsonObject(0).getString("title"));

                });
                checkpoint.flag();
            } else {
                testContext.failNow(handler.cause());
            }
        });

    }

    @Test
    @DisplayName("Get catalogue infos")
    void getCatalogueInfos(Vertx vertx, VertxTestContext testContext) {
        dqvProvider.listCatalogues(ar -> {
            if (ar.succeeded()) {
                log.debug(ar.result().toString());
            } else {
                log.error("Get catalogue infos", ar.cause());
            }
            testContext.completeNow();
        });
    }

}