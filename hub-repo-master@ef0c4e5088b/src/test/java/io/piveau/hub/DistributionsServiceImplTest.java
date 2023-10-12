package io.piveau.hub;

import io.piveau.hub.services.datasets.DatasetsServiceVerticle;
import io.piveau.hub.services.distributions.DistributionsService;
import io.piveau.hub.services.distributions.DistributionsServiceVerticle;
import io.piveau.rdf.Piveau;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.test.MockTripleStore;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the Distributions service")
@ExtendWith(VertxExtension.class)
public class DistributionsServiceImplTest {

    private DistributionsService distributionsService;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject()
                        .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig()));

        Future<String> service1 = vertx.deployVerticle(DistributionsServiceVerticle.class.getName(), options);
        Future<String> service2 = vertx.deployVerticle(DatasetsServiceVerticle.class.getName(), options);

        CompositeFuture.all(service1, service2)
                .compose(id -> {
                    distributionsService = DistributionsService.createProxy(vertx, DistributionsService.SERVICE_ADDRESS);
                    return new MockTripleStore()
                            .loadGraph("https://piveau.io/id/catalogue/test-catalog", "distribution/example_catalog.ttl")
                            .loadGraph("https://piveau.io/set/data/test-dataset", "distribution/example_dataset.ttl")
                            .deploy(vertx);
                })
                .onSuccess(id -> testContext.completeNow())
                .onFailure(testContext::failNow);
    }

    @Test
    void testGetDistribution(Vertx vertx, VertxTestContext vertxTestContext) {
        distributionsService.getDistribution("02e103b1-ffc4-464d-8013-de1c435a5375", RDFMimeTypes.JSONLD)
                .onSuccess(content -> {
                    vertxTestContext.verify(() -> {
                        assertNotNull(content);
                        assertDoesNotThrow(() -> {
                            Model model = Piveau.toModel(content.getBytes(), RDFMimeTypes.JSONLD);
                            assertFalse(model.isEmpty());
                        });
                    });
                    vertxTestContext.completeNow();
                })
                .onFailure(vertxTestContext::failNow);
    }

    @Test
    void testPutDistribution(Vertx vertx, VertxTestContext vertxTestContext) {
        Buffer distribution = vertx.fileSystem().readFileBlocking("distribution/example_distribution_update.ttl");

        distributionsService.putDistribution("test-dataset", "02e103b1-ffc4-464d-8013-de1c435a5375", distribution.toString(), RDFMimeTypes.TURTLE)
                .compose(result -> {
                    vertxTestContext.verify(() -> {
                        assertEquals("updated", result);
                    });

                    return distributionsService.getDistribution("02e103b1-ffc4-464d-8013-de1c435a5375", RDFMimeTypes.JSONLD);
                })
                .onSuccess(content -> {
                    vertxTestContext.verify(() -> {
                        assertNotNull(content);
                        assertDoesNotThrow(() -> {
                            Model model = Piveau.toModel(content.getBytes(), RDFMimeTypes.JSONLD);
                            assertFalse(model.isEmpty());
                        });
                    });
                    vertxTestContext.completeNow();
                })
                .onFailure(vertxTestContext::failNow);
    }

}
