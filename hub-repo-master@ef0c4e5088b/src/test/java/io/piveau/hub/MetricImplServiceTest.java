package io.piveau.hub;

import io.piveau.hub.services.metrics.MetricsService;
import io.piveau.hub.services.metrics.MetricsServiceVerticle;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.test.MockTripleStore;
import io.piveau.utils.JenaUtils;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testing the metrics service")
@ExtendWith(VertxExtension.class)
class MetricImplServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricImplServiceTest.class);
    private final String catalogueId = "test-catalog";
    private final String datasetId = "test-dataset";
    private MetricsService metricsService;
    private String exampleMetric_1;
    private String exampleMetric_2;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        DeploymentOptions options = new DeploymentOptions()
                .setWorker(true)
                .setConfig(new JsonObject()
                        .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig()));

        Future<String> mockFuture = new MockTripleStore()
                .loadGraph("https://piveau.io/id/catalogue/test-catalog", "example_catalog.ttl")
                .loadGraph("https://piveau.io/set/data/test-dataset", "example_dataset.ttl")
                .deploy(vertx);

        Promise<String> metricsPromise = Promise.promise();

        vertx.deployVerticle(MetricsServiceVerticle.class.getName(), options, metricsPromise);

        CompositeFuture.join(mockFuture, metricsPromise.future()).onComplete(testContext.succeeding(setup -> {
            metricsService = MetricsService.createProxy(vertx, MetricsService.SERVICE_ADDRESS);
            vertx.fileSystem().readFile("example_metric_1.ttl", testContext.succeeding(readMetric1 ->
                    vertx.fileSystem().readFile("example_metric_2.ttl", testContext.succeeding(readMetric2 -> {
                        exampleMetric_1 = readMetric1.toString();
                        exampleMetric_2 = readMetric2.toString();
                        testContext.completeNow();
                    }))
            ));
        }));
    }

    @Test
    @DisplayName("Add a metric")
    void testCreateMetric(VertxTestContext testContext) {
        metricsService.putMetrics(datasetId, exampleMetric_1, RDFMimeTypes.TURTLE)
                .compose(result -> metricsService.getMetrics(datasetId, false, RDFMimeTypes.TURTLE))
                .onSuccess(content -> compareModels(testContext, exampleMetric_1, content))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Replace an existing metric")
    void testReplaceMetric(VertxTestContext testContext) {
        metricsService.putMetrics(datasetId, exampleMetric_1, RDFMimeTypes.TURTLE)
                .compose(result -> metricsService.putMetrics(datasetId, exampleMetric_2, RDFMimeTypes.TURTLE))
                .compose(result -> metricsService.getMetrics(datasetId, false, RDFMimeTypes.TURTLE))
                .onSuccess(content -> compareModels(testContext, exampleMetric_2, content))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Receive a metric")
    void testGetMetric(VertxTestContext testContext) {
        metricsService.putMetrics(datasetId, exampleMetric_1, RDFMimeTypes.TURTLE)
                .compose(result -> metricsService.getMetrics(datasetId, false, RDFMimeTypes.TURTLE))
                .onSuccess(content -> compareModels(testContext, exampleMetric_1, content))
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Create historic graph and retrieve it")
    @Disabled
    void testHistoricMetrics(Vertx vertx, VertxTestContext testContext) {
        metricsService.putMetrics(datasetId, exampleMetric_1, RDFMimeTypes.TURTLE)
                .compose(result -> metricsService.putMetrics(datasetId, exampleMetric_2, RDFMimeTypes.TURTLE))
                .compose(result -> metricsService.getMetrics(datasetId, true, RDFMimeTypes.TURTLE))
                .onSuccess(content -> {
                    // TODO check history content
                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Delete a metric")
    @Timeout(timeUnit = TimeUnit.MINUTES, value = 5)
    void testDeleteMetric(VertxTestContext testContext) {
        metricsService.putMetrics(datasetId, exampleMetric_1, RDFMimeTypes.TURTLE)
                .compose(result -> metricsService.deleteMetrics(datasetId, catalogueId))
                .compose(v -> metricsService.getMetrics(datasetId, false, RDFMimeTypes.TURTLE))
                .onSuccess(content -> {
                    LOGGER.debug(content);
                    testContext.failNow("Metrics still exist");
                })
                .onFailure(cause -> testContext.completeNow());
    }

    private void compareModels(VertxTestContext testContext, String expected, String received) {
        testContext.verify(() -> {
            assertNotNull(received);
            Model receivedMetrics = JenaUtils.read(received.getBytes(), RDFMimeTypes.TURTLE);
            assertNotNull(receivedMetrics);

            Model expectedMetrics = JenaUtils.readDataset(expected.getBytes(), RDFMimeTypes.TURTLE).getNamedModel("urn:junit5-tests:test-pipe");

            // assertTrue(expectedMetrics.isIsomorphicWith(receivedMetrics));
            assertEquals(expectedMetrics.listStatements().toSet().size(), receivedMetrics.listStatements().toSet().size());
            testContext.completeNow();
        });
    }
}
