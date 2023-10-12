package io.piveau.metrics.cache;

import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
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


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.piveau.metrics.cache.ApplicationConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTest {

    private WebClient webClient;
    private JsonObject testMetrics, testInfo;

    private MongoClient dbClient;

    @BeforeAll
    void setup(Vertx vertx, VertxTestContext testContext) {
        try {
            WebClientOptions clientOptions = new WebClientOptions()
                    .setDefaultHost("127.0.0.1")
                    .setDefaultPort(8080);
            webClient = WebClient.create(vertx, clientOptions);

            MongodStarter starter = MongodStarter.getDefaultInstance();
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net("localhost", 27019, Network.localhostIsIPv6()))
                    .build();

            starter.prepare(mongodConfig).start();

            JsonObject config = new JsonObject()
                    .put("connection_string", DEFAULT_MONGODB_CONNECTION);
            dbClient = MongoClient.createShared(vertx, config);

            testMetrics = generateMetrics();
            testInfo = generateInfo();
            initDBTestData(vertx)
                    .compose(v -> {
                        JsonObject testConfig = new JsonObject()
                                .put(ENV_APPLICATION_PORT, 8080)
                                .put(ENV_MONGODB_CONNECTION, DEFAULT_MONGODB_CONNECTION)
                                .put(ENV_APIKEY, "test-dummy");

                        return vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setConfig(testConfig));
                    })
                    .onSuccess(id -> testContext.completeNow())
                    .onFailure(testContext::failNow);

        } catch (Exception e) {
            testContext.failNow(e);
        }
    }

    @Test
    void testRetrieveGlobalMetrics(VertxTestContext testContext) {
        webClient.get("/global")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(1, response.getJsonObject("result").getInteger("count"));
                    assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                    JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                    assertEquals(2, result.getJsonObject("accessibility", new JsonObject()).getJsonArray("downloadUrlAvailability", new JsonArray()).size());
                    assertEquals(8, result.getJsonObject("accessibility", new JsonObject()).getJsonArray("accessUrlStatusCodes", new JsonArray()).size());
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }


    @Test
    @Disabled
    void testRetrieveGlobalHistoricMetrics(VertxTestContext testContext) {
        webClient.get("/global/history?startDate=2020-02-01")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(1, response.getJsonObject("result").getInteger("count"));
                    assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                    JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                    JsonObject accessibility = result.getJsonObject("accessibility");
                    assertNotNull(accessibility);
                    assertEquals(9, accessibility.getJsonArray("downloadUrlAvailability", new JsonArray()).size());

                    JsonArray accessUrlStatusCodes = accessibility.getJsonArray("accessUrlStatusCodes");
                    assertNotNull(accessUrlStatusCodes);
                    assertEquals(9, accessUrlStatusCodes.size());

                    List<String> dates = new ArrayList<>();
                    accessUrlStatusCodes.forEach(item -> dates.addAll(((JsonObject) item).fieldNames()));

                    assertTrue(dates.contains("2020-04"));
                    assertFalse(dates.contains("2020-04-02"));
                    assertFalse(dates.contains("2020"));

                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    /*
        @Test
        void testRetrieveScore(VertxTestContext testContext) {
            webClient.get("/score/5de12b5ecebc9b334023e033?startDate=2015-11-09&endDate=2019-11-09&resolution=month")
                .expect(ResponsePredicate.SC_OK)
                .send(testContext.succeeding(response -> testContext.verify(() -> {
                    assertEquals(new JsonObject("{\"Result\": [{\"2017-01\": 1768},{\"2017-02\": 318},{\"2017-04\": 318},{\"2019-11\": 93}]}"), response.bodyAsJsonObject());
                    testContext.completeNow();
                })));
        }
    */
    @Test
    void testRetrieveCatalogues(VertxTestContext testContext) {
        webClient.get("/catalogues/")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(8, response.getJsonObject("result").getInteger("count"));
                    assertEquals(8, response.getJsonObject("result").getJsonArray("results").size());
                    JsonArray results = response.getJsonObject("result").getJsonArray("results");

                    List<String> names = new ArrayList<>();
                    results.forEach(item -> names.add(((JsonObject) item).getJsonObject("info").getString("id")));

                    assertTrue(names.contains("edp"));
                    assertTrue(names.contains("dataportaal-van-de-nederlandse-overheid"));

                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void testRetrieveCatalogueMetrics(VertxTestContext testContext) {
        webClient.get("/catalogues/edp")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(1, response.getJsonObject("result").getInteger("count"));
                    assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                    JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                    assertEquals("edp", result.getJsonObject("info").getString("id"));
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    /*   @Test
       void testRetrieveCatalogueMetricsShort(VertxTestContext testContext) {
           //{"score":376,"info":{"id":"testCatalogue","title":"Test Catalogue","description":"This is a Test Catalogue","spatial":"DEU","type":"dcat-ap"}}
           webClient.get("/catalogues/testCatalogue/metrics?fields={\"info\":\"1\",\"score\":\"1\"}")
               .expect(ResponsePredicate.SC_OK)
               .expect(ResponsePredicate.JSON)
               .send(testContext.succeeding(response -> testContext.verify(() -> {
                   assertEquals(testCatalogueInfoShort, response.bodyAsJsonObject());
                   testContext.completeNow();
               })));
       }
   */
    @Test
    void testRetrieveCountries(VertxTestContext testContext) {
        webClient.get("/countries")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(8, response.getJsonObject("result").getInteger("count"));
                    assertEquals(8, response.getJsonObject("result").getJsonArray("results").size());


                    JsonArray results = response.getJsonObject("result").getJsonArray("results");

                    List<String> names = new ArrayList<>();
                    results.forEach(item -> names.add(((JsonObject) item).getJsonObject("info").getString("id")));

                    assertTrue(names.contains("SWE"));
                    assertTrue(names.contains("SVN"));

                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }


    /*   @Test
       void testRetrieveCountryMetricsFull(VertxTestContext testContext) {
           //{"findability":{"keywordAvailability":[{"name":"yes","percentage":21.76080821495069},{"name":"no","percentage":78.23919178504931}],"categoryAvailability":[{"name":"yes","percentage":93.38851790875661},{"name":"no","percentage":6.6114820912433885}],"spatialAvailability":[{"name":"yes","percentage":4.409622561142923},{"name":"no","percentage":95.59037743885708}],"temporalAvailability":[{"name":"yes","percentage":91.55483271385854},{"name":"no","percentage":8.445167286141455}]},"accessibility":{"accessUrlStatusCodes":[{"name":"200","percentage":70.0},{"name":"404","percentage":20.0},{"name":"500","percentage":10.0}],"downloadUrlAvailability":[{"name":"yes","percentage":91.82287109838109},{"name":"no","percentage":8.177128901618914}],"downloadUrlStatusCodes":[{"name":"200","percentage":70.0},{"name":"404","percentage":20.0},{"name":"500","percentage":10.0}]},"interoperability":{"formatAvailability":[{"name":"yes","percentage":93.7460830875921},{"name":"no","percentage":6.253916912407902}],"mediaTypeAvailability":[{"name":"yes","percentage":35.69372469677201},{"name":"no","percentage":64.30627530322799}],"formatMediaTypeAlignment":[{"name":"yes","percentage":60.38433683547125},{"name":"no","percentage":39.61566316452875}],"formatMediaTypeNonProprietary":[{"name":"yes","percentage":65.2741417896628},{"name":"no","percentage":34.725858210337194}],"formatMediaTypeMachineReadable":[{"name":"yes","percentage":25.728907545931147},{"name":"no","percentage":74.27109245406885}],"dcatApCompliance":[{"name":"yes","percentage":0.7835995384462002},{"name":"no","percentage":99.2164004615538}]},"reusability":{"licenceAvailability":[{"name":"yes","percentage":76.10652022616587},{"name":"no","percentage":23.89347977383413}],"licenceAlignment":[{"name":"yes","percentage":16.7974169013676},{"name":"no","percentage":83.2025830986324}],"accessRightsAvailability":[{"name":"yes","percentage":13.674588622300632},{"name":"no","percentage":86.32541137769937}],"accessRightsAlignment":[{"name":"yes","percentage":36.91475683265069},{"name":"no","percentage":63.08524316734931}],"contactPointAvailability":[{"name":"yes","percentage":56.64252894611983},{"name":"no","percentage":43.35747105388017}],"publisherAvailability":[{"name":"yes","percentage":11.888255468347541},{"name":"no","percentage":88.11174453165246}]},"contextuality":{"rightsAvailability":[{"name":"yes","percentage":32.30891238647951},{"name":"no","percentage":67.6910876135205}],"byteSizeAvailability":[{"name":"yes","percentage":68.44861952607197},{"name":"no","percentage":31.551380473928035}],"dateIssuedAvailability":[{"name":"yes","percentage":29.70508363072627},{"name":"no","percentage":70.29491636927372}],"dateModifiedAvailability":[{"name":"yes","percentage":49.103610642880646},{"name":"no","percentage":50.896389357119354}]},"score":19}
           //System.out.println("testRetrieveCountryMetricsFull:" + testMetricsFull);
           webClient.get("/countries/testCountry/metrics")
               .expect(ResponsePredicate.SC_OK)
               .expect(ResponsePredicate.JSON)
               .send(testContext.succeeding(response -> testContext.verify(() -> {
                   assertEquals(testMetricsFull, response.bodyAsJsonObject());

                   testContext.completeNow();
               })));
       }
   */
    @Test
    void testRetrieveCountryMetrics(VertxTestContext testContext) {
        webClient.get("/countries/SVN")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r -> testContext.verify(() -> {
                    JsonObject response = r.bodyAsJsonObject();
                    assertEquals(true, response.getBoolean("success"));
                    assertEquals(1, response.getJsonObject("result").getInteger("count"));
                    assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                    JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                    assertEquals("SVN", result.getJsonObject("info").getString("id"));

                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void testFilter(VertxTestContext testContext) {
        webClient.get("/catalogues/edp?filter=score,accessibility,info,interoperability")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r ->
                        testContext.verify(() -> {
                            JsonObject response = r.bodyAsJsonObject();
                            assertEquals(true, response.getBoolean("success"));
                            assertEquals(1, response.getJsonObject("result").getInteger("count"));
                            assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                            JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                            assertEquals("edp", result.getJsonObject("info").getString("id"));
                            assertNotNull(result.getJsonObject("interoperability"));
                            JsonObject ctx = result.getJsonObject("contextuality");

                            assertNotNull(ctx.getInteger("score"));
                            assertNull(ctx.getJsonObject("dateIssuedAvailability"));
                            testContext.completeNow();
                        }))
                .onFailure(testContext::failNow);
    }

    @Test
    void testCurrentScoreWithMoreThanOneValuesForADay(VertxTestContext testContext) {
        webClient.get("/catalogues/edp?filter=score")
                .expect(ResponsePredicate.SC_OK)
                .expect(ResponsePredicate.JSON)
                .send()
                .onSuccess(r ->
                        testContext.verify(() -> {
                            JsonObject response = r.bodyAsJsonObject();
                            assertEquals(true, response.getBoolean("success"));
                            assertEquals(1, response.getJsonObject("result").getInteger("count"));
                            assertEquals(1, response.getJsonObject("result").getJsonArray("results").size());
                            JsonObject result = response.getJsonObject("result").getJsonArray("results").getJsonObject(0);
                            assertNull(result.getJsonObject("info"));
                            assertEquals(300, result.getInteger("score"));
                            testContext.completeNow();
                        }))
                .onFailure(testContext::failNow);
    }


    /*   @Test
       void testRetrieveCatalogueInfoShort(VertxTestContext testContext) {
           //{"score":368,"info":{"id":"testCatalogue","title":"Test Catalogue","description":"This is a Test Catalogue","spatial":"DEU","type":"dcat-ap"}}
           //System.out.println("testRetrieveCatalogueInfoShort:" + testCatalogueInfoShort);
           webClient.get("/catalogues/metrics?fields={\"info\":\"1\",\"score\":\"1\"}")
               .expect(ResponsePredicate.SC_OK)
               .expect(ResponsePredicate.JSON)
               .send(testContext.succeeding(response -> testContext.verify(() -> {
                   assertEquals(1, response.bodyAsJsonArray().size());
                   assertEquals(testCatalogueInfoShort, response.bodyAsJsonArray().getJsonObject(0));
                   testContext.completeNow();
               })));
       }

   */
    private CompositeFuture initDBTestData(Vertx vertx) {
        List<Future<String>> futures = new ArrayList<>();

        Buffer globals = vertx.fileSystem().readFileBlocking("testMetrics/global.json");
        JsonArray globalJson = globals.toJsonArray();
        globalJson.forEach(item ->
                futures.add(dbClient.save(DocumentScope.GLOBAL.name(), (JsonObject) item))
        );

        Buffer catalogues = vertx.fileSystem().readFileBlocking("testMetrics/catalogues.json");
        JsonArray catalogueJson = catalogues.toJsonArray();
        catalogueJson.forEach(item ->
                futures.add(dbClient.save(DocumentScope.CATALOGUE.name(), (JsonObject) item))
        );

        Buffer countries = vertx.fileSystem().readFileBlocking("testMetrics/countries.json");
        JsonArray countryJson = countries.toJsonArray();
        countryJson.forEach(item ->
                futures.add(dbClient.save(DocumentScope.COUNTRY.name(), (JsonObject) item))
        );

        return CompositeFuture.all(new ArrayList<>(futures));
    }

    private JsonObject generateMetrics() {


        JsonObject findability = new JsonObject()
                .put("keywordAvailability", getYesNoPercentage())
                .put("categoryAvailability", getYesNoPercentage())
                .put("spatialAvailability", getYesNoPercentage())
                .put("temporalAvailability", getYesNoPercentage());

        JsonArray statusCodes = new JsonArray()
                .add(new JsonObject().put("name", "200").put("percentage", 70d))
                .add(new JsonObject().put("name", "404").put("percentage", 20d))
                .add(new JsonObject().put("name", "500").put("percentage", 10d));

        JsonObject accessibility = new JsonObject()
                .put("accessUrlStatusCodes", statusCodes)
                .put("downloadUrlAvailability", getYesNoPercentage())
                .put("downloadUrlStatusCodes", statusCodes);

        JsonObject interoperability = new JsonObject()
                .put("formatAvailability", getYesNoPercentage())
                .put("mediaTypeAvailability", getYesNoPercentage())
                .put("formatMediaTypeAlignment", getYesNoPercentage())
                .put("formatMediaTypeNonProprietary", getYesNoPercentage())
                .put("formatMediaTypeMachineReadable", getYesNoPercentage())
                .put("dcatApCompliance", getYesNoPercentage());

        JsonObject reusability = new JsonObject()
                .put("licenceAvailability", getYesNoPercentage())
                .put("licenceAlignment", getYesNoPercentage())
                .put("accessRightsAvailability", getYesNoPercentage())
                .put("accessRightsAlignment", getYesNoPercentage())
                .put("contactPointAvailability", getYesNoPercentage())
                .put("publisherAvailability", getYesNoPercentage());

        JsonObject contextuality = new JsonObject()
                .put("rightsAvailability", getYesNoPercentage())
                .put("byteSizeAvailability", getYesNoPercentage())
                .put("dateIssuedAvailability", getYesNoPercentage())
                .put("dateModifiedAvailability", getYesNoPercentage());

        return new JsonObject()
                .put("findability", findability)
                .put("accessibility", accessibility)
                .put("interoperability", interoperability)
                .put("reusability", reusability)
                .put("contextuality", contextuality)
                .put("score", ThreadLocalRandom.current().nextInt(0, 405));
    }

    /*
        private JsonObject generateMetricsShort() {
            return new JsonObject()
                .put("findability", testMetricsFull.getJsonObject("findability"));
        }

    */
    private JsonArray getYesNoPercentage() {
        double percentage = ThreadLocalRandom.current().nextDouble(0, 100);
        return new JsonArray()
                .add(new JsonObject().put("name", "yes").put("percentage", percentage))
                .add(new JsonObject().put("name", "no").put("percentage", 100 - percentage));
    }

    private JsonObject generateInfo() {
        JsonObject accessibility = new JsonObject()
                .put("accessUrlStatusCodes", testMetrics.getJsonObject("accessibility").getJsonArray("accessUrlStatusCodes"))
                .put("downloadUrlStatusCodes", testMetrics.getJsonObject("accessibility").getJsonArray("downloadUrlStatusCodes"));

        JsonObject interoperability = new JsonObject()
                .put("formatMediaTypeMachineReadable", testMetrics.getJsonObject("interoperability").getJsonArray("formatMediaTypeMachineReadable"))
                .put("dcatApCompliance", testMetrics.getJsonObject("interoperability").getJsonArray("dcatApCompliance"));

        return new JsonObject()
                .put("info", getInfoObject())
                .put("score", testMetrics.getDouble("score"))
                .put("accessibility", accessibility)
                .put("interoperability", interoperability);


    }

    private JsonObject getInfoObject() {

        return new JsonObject()
                .put("id", "testCatalogue")
                .put("title", "Test Catalogue")
                .put("description", "This is a Test Catalogue")
                .put("spatial", "DEU").put("type", "dcat-ap");
    }


}
