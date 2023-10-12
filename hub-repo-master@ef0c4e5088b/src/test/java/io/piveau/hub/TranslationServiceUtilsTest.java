package io.piveau.hub;

import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.services.translation.TranslationServiceUtils;
import io.piveau.rdf.RDFMimeTypes;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.DCTerms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing translation utilities")
@ExtendWith(VertxExtension.class)
class TranslationUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(TranslationUtilsTest.class);

    private DatasetHelper datasetHelper;
    private DatasetHelper oldDatasetHelper;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        TranslationServiceUtils.translationLanguages = List.of("en", "de", "fr", "nl", "es", "pt");
        TranslationServiceUtils.callbackParameters = new JsonObject()
                .put("url", "anyUrl")
                .put("method", HttpMethod.POST.name())
                .put("headers", new JsonObject().put("Authorization", "anyKey"));

        vertx.fileSystem().readFile("translation/translation_example_dataset.ttl")
                .compose(buffer -> DatasetHelper.create(buffer.toString(), RDFMimeTypes.TURTLE))
                .compose(helper -> {
                    datasetHelper = helper;
                    datasetHelper.sourceLang("en");
                    datasetHelper.catalogueId("test-catalogue");
                    return vertx.fileSystem().readFile("translation/translation_example_dataset_old.ttl");
                })
                .compose(buffer -> DatasetHelper.create(buffer.toString(), RDFMimeTypes.TURTLE))
                .onSuccess(helper -> {
                    oldDatasetHelper = helper;
                    oldDatasetHelper.sourceLang("en");
                    oldDatasetHelper.catalogueId("test-catalogue");
                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
    }

    @Test
    @DisplayName("Extract property languages map")
    void propertyToLanguageMap(VertxTestContext testContext) {
        Resource distribution = datasetHelper.model().getResource("https://piveau.io/set/distribution/3");
        Map<String, String> languages = TranslationServiceUtils.propertyToLanguageMap(distribution, DCTerms.title, "de");
        testContext.verify(() -> {
            assertEquals(1, languages.size());
            assertEquals("Beispiel-Distribution 3", languages.get("de"));
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Is translation completed")
    void translationCompleted(VertxTestContext testContext) {
        testContext.verify(() -> {
            assertFalse(TranslationServiceUtils.isTranslationCompleted(null));
            assertFalse(TranslationServiceUtils.isTranslationCompleted(datasetHelper));
            assertTrue(TranslationServiceUtils.isTranslationCompleted(oldDatasetHelper));
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Build translation request without old translations")
    void buildTranslationRequest(VertxTestContext testContext) {
        JsonObject request = TranslationServiceUtils.buildTranslationRequest(datasetHelper, null, false);
        if (log.isDebugEnabled()) {
            log.debug(request.encodePrettily());
        }
        testContext.verify(() -> {
            assertEquals(4, request.getJsonObject("dict").size());
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Build translation request with just one change")
    void buildTranslationRequest2(VertxTestContext testContext) {
        JsonObject request = TranslationServiceUtils.buildTranslationRequest(datasetHelper, oldDatasetHelper, false);
        if (log.isDebugEnabled()) {
            log.debug(request.encodePrettily());
        }
        testContext.verify(() -> {
            assertEquals(1, request.getJsonObject("dict").size());
            assertFalse(request.getJsonObject("dict").containsKey(datasetHelper.uriRef()));
            assertTrue(datasetHelper.resource().hasProperty(DCTerms.title, "Beispieldatensatz", "de-t-en-t0-mtec"));
            assertTrue(datasetHelper.resource().hasProperty(DCTerms.description, "This is an example dataset", "en-t-de-t0-mtec"));
            testContext.completeNow();
        });
    }

    @Test
    @DisplayName("Build translation request from old model")
    void buildTranslationRequest3(VertxTestContext testContext) {
        JsonObject request = TranslationServiceUtils.buildTranslationRequest(oldDatasetHelper, null, false);
        if (log.isDebugEnabled()) {
            log.debug(request.encodePrettily());
        }
        testContext.verify(() -> {

        });
        testContext.completeNow();
    }

    @Test
    @DisplayName("Apply translations to model")
    void applyTranslations(VertxTestContext testContext) {
        JsonObject dict = new JsonObject()
                .put("https://piveau.io/set/data/test-dataset", new JsonObject()
                        .put("http://purl.org/dc/terms/title", new JsonObject()
                                .put("sourceLanguage", "en")
                                .put("translations", new JsonObject()
                                        .put("fr", -90002)
                                        .put("de", "Beispieldatensatz")
                                )
                        )
                );
        TranslationServiceUtils.applyTranslations(datasetHelper.model(), dict);
        if (log.isDebugEnabled()) {
            log.debug(datasetHelper.stringify(Lang.TURTLE));
        }
        testContext.verify(() -> {

        });
        testContext.completeNow();
    }

}
