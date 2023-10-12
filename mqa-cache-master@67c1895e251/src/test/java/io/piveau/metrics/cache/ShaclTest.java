package io.piveau.metrics.cache;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @ExtendWith(VertxExtension.class)
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShaclTest {

    private WebClient webClient;

    @Disabled
  //  @BeforeAll
    void setup(Vertx vertx, VertxTestContext testContext) {
        try {
            WebClientOptions clientOptions = new WebClientOptions()
                .setDefaultHost("127.0.0.1")
                .setDefaultPort(8080);
            webClient = WebClient.create(vertx, clientOptions);
            vertx.deployVerticle(MainVerticle.class.getName(), testContext.completing());

        } catch (Exception e) {
            testContext.failNow(e);
        }
    }

    @Disabled
   // @Test
    void testRetrieveCatalogueShaclMetrics(VertxTestContext testContext) {
        webClient.get("/metrics/catalogues/govdata/violations")
            .expect(ResponsePredicate.SC_OK)
            .expect(ResponsePredicate.JSON)
            .send(testContext.succeeding(response -> testContext.verify(() -> {
                assertEquals(50, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").size());
                //JsonObject obj = new JsonObject().put("resultMessage", "Value must be an instance of vcard:Kind").put("resultPath", "Value must be an instance of vcard:Kind").put("resultValue", "https://opendata.leipzig.def089c3e4fb92cfb6833f93ce58d1e946").put("title", "Schankkonzession für Georg Kintschy und Kilian Valär").put("reference", "https://europeandataportal.eu/set/data/2272d3c6-8b57-5eec-834b-600c9df95152");
                //assertEquals(obj, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").getJsonObject(0));
                testContext.completeNow();
            })));
    }

    @Disabled
  //  @Test
    void testRetrieveCatalogueShaclMetricsPageSize(VertxTestContext testContext) {
        webClient.get("/metrics/catalogues/govdata/violations?&offset=2&limit=5")
            .expect(ResponsePredicate.SC_OK)
            .expect(ResponsePredicate.JSON)
            .send(testContext.succeeding(response -> testContext.verify(() -> {
                assertEquals(12738, response.bodyAsJsonObject().getJsonObject("result").getInteger("count"));
                assertEquals(5, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").size());
                testContext.completeNow();
            })));
    }

    @Disabled
 //   @Test
    void testRetrieveCatalogueShaclMetricsOffset(VertxTestContext testContext) {
        webClient.get("/metrics/catalogues/govdata/violations?&offset=2")
            .expect(ResponsePredicate.SC_OK)
            .expect(ResponsePredicate.JSON)
            .send(testContext.succeeding(response -> testContext.verify(() -> {
                assertEquals(50, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").size());
                testContext.completeNow();
            })));
    }

    @Disabled
  //  @Test
    void testRetrieveCatalogueShaclMetricsNoParams(VertxTestContext testContext) {
        webClient.get("/metrics/catalogues/govdata/violations")
            .expect(ResponsePredicate.SC_OK)
            .expect(ResponsePredicate.JSON)
            .send(testContext.succeeding(response -> testContext.verify(() -> {
                assertEquals(12738, response.bodyAsJsonObject().getJsonObject("result").getInteger("count"));
                assertEquals(50, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").size());
                testContext.completeNow();
            })));
    }

    @Disabled
   // @Test
    void testRetrieveCatalogueShaclMetricsNoCatalouge(VertxTestContext testContext) {
        webClient.get("/metrics/catalogues/nocatalouge/violations")
            .expect(ResponsePredicate.SC_OK)
            .expect(ResponsePredicate.JSON)
            .send(testContext.succeeding(response -> testContext.verify(() -> {
                assertEquals("true", response.bodyAsJsonObject().getString("success"));
                assertEquals(0, response.bodyAsJsonObject().getJsonObject("result").getInteger("count"));
                assertEquals(0, response.bodyAsJsonObject().getJsonObject("result").getJsonArray("results").size());
                testContext.completeNow();
            })));
    }


}
