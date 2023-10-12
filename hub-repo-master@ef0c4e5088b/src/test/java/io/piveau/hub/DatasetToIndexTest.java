package io.piveau.hub;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.indexing.Indexing;
import io.piveau.rdf.RDFMimeTypes;
import io.piveau.utils.JenaUtils;
import io.piveau.vocabularies.ConceptSchemes;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing indexing")
@ExtendWith(VertxExtension.class)
class DatasetToIndexTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetToIndexTest.class);

    @Test
    @DisplayName("Indexing an example DCAP-AP 2 dataset")
    @Timeout(timeUnit = TimeUnit.MINUTES, value = 5)
    void testIndexDatasetDCATAP2(Vertx vertx, VertxTestContext testContext) {
        ConceptSchemes.initRemotes(vertx, false, false);
        Buffer buffer = vertx.fileSystem().readFileBlocking("index_test_dataset_dcatap2.ttl");
        Buffer bufferResult = vertx.fileSystem().readFileBlocking("index_test_dataset_dcatap2.json");
        JsonObject expectedResult = new JsonObject(bufferResult.toString());
        DatasetHelper.create(buffer.toString(), RDFMimeTypes.TURTLE, ar -> {
            if (ar.succeeded()) {
                DatasetHelper helper = ar.result();
                JsonObject result = Indexing.indexingDataset(helper.resource(), helper.recordResource(), "test-catalog", "de");
                LOGGER.info(result.encodePrettily());
                assertEquals(expectedResult.getJsonObject("description"), result.getJsonObject("description"));
                assertEquals(expectedResult.getJsonObject("title"), result.getJsonObject("title"));
                assertEquals(expectedResult.getJsonObject("publisher"), result.getJsonObject("publisher"));
                assertEquals(expectedResult.getJsonObject("access_right"), result.getJsonObject("access_right"));
                assertEquals(expectedResult.getJsonObject("creator"), result.getJsonObject("creator"));
                assertEquals(expectedResult.getJsonObject("accrual_periodicity"), result.getJsonObject("accrual_periodicity"));
                assertEquals(expectedResult.getString("modified"), result.getString("modified"));
                assertEquals(expectedResult.getString("issued"), result.getString("issued"));
                assertEquals(expectedResult.getString("version_info"), result.getString("version_info"));
                assertEquals(expectedResult.getJsonObject("version_notes"), result.getJsonObject("version_notes"));

                // StatDCAT-AP
                assertEquals(expectedResult.getInteger("num_series"), result.getInteger("num_series"));

                try {
                    JSONAssert.assertEquals(expectedResult.getJsonArray("contact_point").toString(), result.getJsonArray("contact_point").toString(), JSONCompareMode.NON_EXTENSIBLE);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("categories").toString(), result.getJsonArray("categories").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("subject").toString(), result.getJsonArray("subject").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("keywords").toString(), result.getJsonArray("keywords").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("spatial").toString(), result.getJsonArray("spatial").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("temporal").toString(), result.getJsonArray("temporal").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("conforms_to").toString(), result.getJsonArray("conforms_to").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("page").toString(), result.getJsonArray("page").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("has_version").toString(), result.getJsonArray("has_version").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("is_version_of").toString(), result.getJsonArray("is_version_of").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("source").toString(), result.getJsonArray("source").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("identifier").toString(), result.getJsonArray("identifier").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("adms_identifier").toString(), result.getJsonArray("adms_identifier").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("language").toString(), result.getJsonArray("language").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("landing_page").toString(), result.getJsonArray("landing_page").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("provenance").toString(), result.getJsonArray("provenance").toString(), JSONCompareMode.LENIENT);
                    // StatDCAT-AP
                    JSONAssert.assertEquals(expectedResult.getJsonArray("attribute").toString(), result.getJsonArray("attribute").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("dimension").toString(), result.getJsonArray("dimension").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("has_quality_annotation").toString(), result.getJsonArray("has_quality_annotation").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedResult.getJsonArray("stat_unit_measure").toString(), result.getJsonArray("stat_unit_measure").toString(), JSONCompareMode.LENIENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObject resultDstr = result.getJsonArray("distributions").getJsonObject(0);
                JsonObject expectedDstr = expectedResult.getJsonArray("distributions").getJsonObject(0);

                assertEquals(expectedDstr.getJsonObject("availability"), resultDstr.getJsonObject("availability"));
                assertEquals(expectedDstr.getJsonObject("format"), resultDstr.getJsonObject("format"));
                assertEquals(expectedDstr.getJsonObject("license"), resultDstr.getJsonObject("license"));
                assertEquals(expectedDstr.getString("media_type"), resultDstr.getString("media_type"));
                assertEquals(expectedDstr.getInteger("byte_size"), resultDstr.getInteger("byte_size"));
                assertEquals(expectedDstr.getJsonObject("checksum"), resultDstr.getJsonObject("checksum"));
                assertEquals(expectedDstr.getString("issued"), resultDstr.getString("issued"));
                assertEquals(expectedDstr.getString("modified"), resultDstr.getString("modified"));
                assertEquals(expectedDstr.getJsonObject("status"), resultDstr.getJsonObject("status"));
                assertEquals(expectedDstr.getJsonObject("rights"), resultDstr.getJsonObject("rights"));
                try {
                    JSONAssert.assertEquals(expectedDstr.getJsonArray("access_url").toString(), resultDstr.getJsonArray("access_url").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedDstr.getJsonArray("download_url").toString(), resultDstr.getJsonArray("download_url").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedDstr.getJsonArray("page").toString(), resultDstr.getJsonArray("page").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedDstr.getJsonArray("language").toString(), resultDstr.getJsonArray("language").toString(), JSONCompareMode.LENIENT);
                    JSONAssert.assertEquals(expectedDstr.getJsonArray("conforms_to").toString(), resultDstr.getJsonArray("conforms_to").toString(), JSONCompareMode.LENIENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObject resultDataService = resultDstr.getJsonArray("access_service").getJsonObject(0);
                JsonObject expectedDataService = expectedDstr.getJsonArray("access_service").getJsonObject(0);

                assertEquals(expectedDataService.getJsonObject("title"), resultDataService.getJsonObject("title"));
                assertEquals(expectedDataService.getJsonObject("description"), resultDataService.getJsonObject("description"));
                try {
                    JSONAssert.assertEquals(expectedDataService.getJsonArray("endpoint_url").toString(), resultDataService.getJsonArray("endpoint_url").toString(), JSONCompareMode.LENIENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                testContext.completeNow();
            } else {
                testContext.failNow(ar.cause());
            }
        });
    }

    @Test
    @DisplayName("Indexing an example catalogue")
    @Timeout(timeUnit = TimeUnit.MINUTES, value = 5)
    void testExampleCatalog(Vertx vertx, VertxTestContext testContext) {
        ConceptSchemes.initRemotes(vertx, false, false);

        Buffer buffer = vertx.fileSystem().readFileBlocking("index_test_catalogue_dcatap2.ttl");
        Buffer bufferResult = vertx.fileSystem().readFileBlocking("index_test_catalogue_dcatap2.json");

        JsonObject expectedResult = new JsonObject(bufferResult.toString());

        Model model = JenaUtils.read(buffer.getBytes(), "text/turtle");
        Resource catalogue = model.listSubjectsWithProperty(RDF.type, DCAT.Catalog).next();
        Indexing.indexingCatalogue(catalogue)
                .onSuccess(result -> {
                    LOGGER.info(result.encodePrettily());

                    assertEquals(expectedResult.getJsonObject("description"), result.getJsonObject("description"));
                    assertEquals(expectedResult.getJsonObject("title"), result.getJsonObject("title"));
                    assertEquals(expectedResult.getJsonObject("publisher"), result.getJsonObject("publisher"));
                    assertEquals(expectedResult.getJsonObject("country"), result.getJsonObject("country"));
                    assertEquals(expectedResult.getJsonObject("creator"), result.getJsonObject("creator"));
                    assertEquals(expectedResult.getJsonObject("license"), result.getJsonObject("license"));
                    assertEquals(expectedResult.getJsonObject("rights"), result.getJsonObject("rights"));

                    assertEquals(expectedResult.getString("is_part_of"), result.getString("is_part_of"));
                    assertEquals(expectedResult.getString("homepage"), result.getString("homepage"));

                    try {
                        JSONAssert.assertEquals(expectedResult.getJsonArray("catalog").toString(), result.getJsonArray("catalog").toString(), JSONCompareMode.LENIENT);
                        JSONAssert.assertEquals(expectedResult.getJsonArray("language").toString(), result.getJsonArray("language").toString(), JSONCompareMode.LENIENT);
                        JSONAssert.assertEquals(expectedResult.getJsonArray("theme_taxonomy").toString(), result.getJsonArray("theme_taxonomy").toString(), JSONCompareMode.LENIENT);
                        JSONAssert.assertEquals(expectedResult.getJsonArray("has_part").toString(), result.getJsonArray("has_part").toString(), JSONCompareMode.LENIENT);
                        JSONAssert.assertEquals(expectedResult.getJsonArray("spatial").toString(), result.getJsonArray("spatial").toString(), JSONCompareMode.LENIENT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

}
