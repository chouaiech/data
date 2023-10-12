package io.piveau.hub.services.translation;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.dcatap.DatasetManager;
import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Defaults;
import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.indexing.Indexing;
import io.piveau.hub.services.index.IndexService;
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.piveau.log.PiveauLogger;
import io.piveau.utils.PiveauContext;
import io.piveau.vocabularies.vocabulary.EDP;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.DCTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static io.piveau.hub.services.translation.TranslationServiceUtils.*;

public class TranslationServiceImpl implements TranslationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final DatasetManager datasetManager;

    private IndexService indexService;

    private final HttpRequest<Buffer> translationServiceRequest;

    private final PiveauContext moduleContext;

    TranslationServiceImpl(
            Vertx vertx,
            WebClient client,
            JsonObject config,
            TripleStore tripleStore,
            Handler<AsyncResult<TranslationService>> readyHandler) {

        ConfigHelper configHelper = ConfigHelper.forConfig(config);

        JsonObject translationConfig = configHelper.forceJsonObject(Constants.ENV_PIVEAU_TRANSLATION_SERVICE);
        translationLanguages = translationConfig.getJsonArray("accepted_languages", new JsonArray())
                .stream()
                .map(Object::toString).collect(Collectors.toList());

        translationServiceRequest = client.postAbs(translationConfig.getString("translation_service_url", "https://example.com/translation-service/"))
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/json")
                .expect(ResponsePredicate.SC_SUCCESS)
                .timeout(10000);

        callbackParameters = new JsonObject()
                .put("url", translationConfig.getString("callback_url", "http://localhost:8080/translation"))
                .put("method", HttpMethod.POST.name())
                .put("headers", new JsonObject()
                        .put("X-API-Key", config.getString(Constants.ENV_PIVEAU_HUB_API_KEY, Defaults.APIKEY)));

        datasetManager = tripleStore.getDatasetManager();

        JsonObject indexConfig = configHelper.forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE);
        if (indexConfig.getBoolean("enabled", false)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        }

        moduleContext = new PiveauContext("hub", "translation");

        readyHandler.handle(Future.succeededFuture(this));
    }

    /**
     * Extract title and description from Dataset and its Distributions and send them to the translation service.
     * <p>
     * If the Dataset is only an update of an old Dataset with no titles or descriptions changed, nothing will be send to the translation service.
     *
     * @param helper    the new Dataset with new and not yet translated fields
     * @param oldHelper the optional old Dataset with already translated fields, should be `null`, if there is no old Dataset
     * @return this {@link TranslationService} for a fluent usage
     */
    @Override
    public TranslationService initializeTranslationProcess(DatasetHelper helper, DatasetHelper oldHelper, Boolean force, Handler<AsyncResult<DatasetHelper>> handler) {
        try {
            PiveauLogger logger = moduleContext.extend(helper.piveauId()).log();

            final JsonObject translationRequest = TranslationServiceUtils.buildTranslationRequest(helper, oldHelper, force);
            if (!translationRequest.isEmpty()) {
                initTranslationProcess(helper.recordResource(), helper.sourceLang());
                translationServiceRequest.sendJsonObject(translationRequest)
                        .onSuccess(response -> {
                            logger.info("Translation initialized.");
                            if (logger.isDebugEnabled()) {
                                logger.debug("Translation request: {}", translationRequest.encodePrettily());
                            }
                        })
                        .onFailure(cause -> logger.error("Translation initialization failed.", cause));
            }
            handler.handle(Future.succeededFuture(helper));
        } catch (Exception e) {
            handler.handle(Future.failedFuture(e));
        }
        return this;
    }

    @Override
    public TranslationService receiveTranslation(JsonObject translation, Handler<AsyncResult<JsonObject>> handler) {
        DCATAPUriRef uriRef = DCATAPUriSchema.parseUriRef(translation.getString("id"));

        PiveauContext resourceContext = moduleContext.extend(uriRef.getId());
        if (resourceContext.log().isDebugEnabled()) {
            resourceContext.log().debug("Incoming translation: {}", translation.encodePrettily());
        }

        datasetManager.getGraph(uriRef.getDatasetGraphName())
                .compose(model -> {
                    TranslationServiceUtils.applyTranslations(model, translation.getJsonObject("dict", new JsonObject()));

                    // Updating catalog record with translation information
                    Resource record = model.getResource(uriRef.getRecordUriRef());
                    completeTranslationProcess(record);

                    return Future.<Model>future(promise ->
                        datasetManager.setGraph(uriRef.getDatasetGraphName(), model, false)
                                .onSuccess(id -> promise.complete(model))
                                .onFailure(promise::fail)
                    );
                })
                .onSuccess(model -> {
                    if (indexService != null) {
                        Resource record = model.getResource(uriRef.getRecordUriRef());
                        // We need catalogue id and sourceLang (default language)
                        JsonObject data = translation.getJsonObject("data", new JsonObject());
                        JsonObject index = Indexing.indexingDataset(
                                model.getResource(uriRef.getDatasetUriRef()),
                                record,
                                data.getString("catalogueId"),
                                data.getString("defaultLanguage"));

                        indexService.addDatasetPut(index, ar -> {
                            if (ar.succeeded()) {
                                log.debug("Dataset successfully indexed");
                            } else {
                                log.error("Index dataset", ar.cause());
                            }
                        });
                    }
                    resourceContext.log().info("Translation stored");
                    handler.handle(Future.succeededFuture(new JsonObject().put("status", "success")));
                })
                .onFailure(cause -> {
                    resourceContext.log().error("Translation", cause);
                    handler.handle(Future.failedFuture(cause));
                });
        return this;
    }

    private void initTranslationProcess(Resource record, String sourceLang) {
        record.removeAll(EDP.originalLanguage);
        record.addProperty(EDP.originalLanguage, sourceLang);
        record.removeAll(EDP.transReceived);
        record.removeAll(EDP.transIssued);
        record.addProperty(EDP.transIssued, ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);
        record.removeAll(EDP.transStatus);
        record.addProperty(EDP.transStatus, EDP.TransInProcess);
    }

    private void completeTranslationProcess(Resource record) {
        record.removeAll(EDP.transReceived);
        record.addProperty(EDP.transReceived, ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);
        record.removeAll(EDP.transStatus);
        record.addProperty(EDP.transStatus, EDP.TransCompleted);

        record.removeAll(DCTerms.modified);
        record.addProperty(DCTerms.modified, ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);
    }

}
