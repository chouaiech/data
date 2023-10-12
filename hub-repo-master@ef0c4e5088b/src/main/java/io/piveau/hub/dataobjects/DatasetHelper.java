package io.piveau.hub.dataobjects;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.hub.util.DataUploadConnector;
import io.piveau.rdf.Piveau;
import io.piveau.utils.JenaUtils;
import io.piveau.vocabularies.vocabulary.DEFECTIVE_SPDX;
import io.piveau.vocabularies.vocabulary.DEXT;
import io.piveau.vocabularies.vocabulary.SPDX;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.json.JsonObject;
import org.apache.jena.arq.querybuilder.ConstructBuilder;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.shared.DoesNotExistException;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DataObject
public class DatasetHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private String originId;
    private String catalogueId;

    private String sourceType;
    private String sourceLang;

    private String visibility;

    private DCATAPUriRef uriSchema;

    private Model model;

    public DatasetHelper(JsonObject json) {
        originId = json.getString("originId");
        catalogueId = json.getString("catalogueId");
        sourceType = json.getString("sourceType");
        sourceLang = json.getString("sourceLang");
        visibility = json.getString("visibility");
        uriSchema = DCATAPUriSchema.parseUriRef(json.getString("uriRef"));
        model = JenaUtils.read(json.getString("model").getBytes(), Lang.NTRIPLES.getContentType().toString(), "");
    }

    /**
     * Create a DatasetHelper from another DatasetHelper
     *
     * @param another the DatasetHelper this new one should be copied from
     */
    public DatasetHelper(DatasetHelper another) {
        originId = another.originId;
        catalogueId = another.catalogueId;
        sourceType = another.sourceType;
        sourceLang = another.sourceLang;
        model = ModelFactory.createDefaultModel().add(another.model);
        uriSchema = another.uriSchema;
    }

    private DatasetHelper(Model sourceModel) {
        sourceModel.listResourcesWithProperty(RDF.type, DCAT.Dataset).nextOptional().ifPresentOrElse(res -> {
            model = sourceModel;
            extractId();
        }, () -> {
            throw new DoesNotExistException("http://www.w3.org/ns/dcat#Dataset");
        });
    }

    private DatasetHelper(String content, String contentType) {
        Model temp = JenaUtils.read(content.getBytes(), contentType, "");
        temp.listResourcesWithProperty(RDF.type, DCAT.Catalog).toList()
                .forEach(catalog -> {
                    Model catalogModel = Piveau.extractAsModel(catalog);
                    temp.remove(catalogModel);
                });
        temp.listResourcesWithProperty(RDF.type, DCAT.CatalogRecord).toList()
                .forEach(record -> {
                    if (!record.hasProperty(DCTerms.creator, temp.createResource("http://piveau.io"))) {
                        Model recordModel = Piveau.extractAsModel(record);
                        temp.remove(recordModel);
                    }
                });
        temp.listResourcesWithProperty(RDF.type, DCAT.Dataset).nextOptional().ifPresentOrElse(res -> {
            model = temp;
            extractId();
        }, () -> {
            throw new DoesNotExistException("http://www.w3.org/ns/dcat#Dataset");
        });
    }

    private DatasetHelper(String originId, String content, String contentType, String catalogueId) {
        this(content, contentType);

        this.originId = originId;
        this.catalogueId = catalogueId;

        uriSchema = DCATAPUriSchema.applyFor(originId, DCATAPUriSchema.getDatasetContext());
    }

    public static Future<DatasetHelper> create(Model model) {
        return Future.future(promise -> promise.complete(new DatasetHelper(model)));
    }

    public static Future<DatasetHelper> create(String content, String contentType) {
        Promise<DatasetHelper> promise = Promise.promise();
        try {
            promise.complete(new DatasetHelper(content, contentType));
        } catch (Exception e) {
            promise.fail(e);
        }
        return promise.future();
    }

    public static void create(String content, String contentType, Handler<AsyncResult<DatasetHelper>> handler) {
        create(content, contentType).onComplete(handler);
    }

    public static Future<DatasetHelper> create(String originId, String content, String contentType, String catalogueId) {
        return Future.future(promise -> {
            try {
                promise.complete(new DatasetHelper(originId, content, contentType, catalogueId));
            } catch (Exception e) {
                promise.fail(e);
            }
        });
    }

    public static void create(String originId, String content, String contentType, String catalogueId, Handler<AsyncResult<DatasetHelper>> handler) {
        create(originId, content, contentType, catalogueId).onComplete(handler);
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("originId", originId)
                .put("catalogueId", catalogueId)
                .put("sourceType", sourceType)
                .put("sourceLang", sourceLang)
                .put("visibility", visibility)
                .put("model", JenaUtils.write(model, Lang.NTRIPLES))
                .put("uriRef", uriSchema.getUri());
    }

    public String originId() {
        return originId;
    }

    public String piveauId() {
        return uriSchema.getId();
    }

    public String catalogueId() {
        return catalogueId;
    }

    public void catalogueId(String catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String hash() {
        try {
            return JenaUtils.canonicalHash(model);
        } catch (Exception e) {
            log.error("Calculating hash failure", e);
            return "";
        }
    }

    public String graphName() {
        return uriSchema.getGraphName();
    }

    public String uriRef() {
        return uriSchema.getUri();
    }

    public String recordUriRef() {
        return uriSchema.getRecordUriRef();
    }

    public String catalogueUriRef() {
        return DCATAPUriSchema.createFor(catalogueId, DCATAPUriSchema.getCatalogueContext()).getCatalogueUriRef();
    }

    public String catalogueGraphName() {
        return DCATAPUriSchema.createFor(catalogueId, DCATAPUriSchema.getCatalogueContext()).getCatalogueGraphName();
    }

    public String metricsUriRef() {
        return uriSchema.getMetricsUriRef();
    }

    public String metricsGraphName() {
        return uriSchema.getMetricsGraphName();
    }

    public Resource resource() {
        return model.getResource(uriRef());
    }

    public Resource recordResource() {
        return model.getResource(recordUriRef());
    }

    public void sourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String sourceType() {
        return sourceType;
    }

    public void sourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String sourceLang() {
        return sourceLang;
    }

    public void visibility(String visibility) {
        this.visibility = visibility;
    }

    public String visibility() {
        return visibility;
    }

    public Model model() {
        return model;
    }

    public String stringify(Lang lang) {
        return JenaUtils.write(model, lang);
    }

    public void update(Model oldModel, String recordUriRef) {
        uriSchema = DCATAPUriSchema.createFor(DCATAPUriSchema.parseUriRef(recordUriRef).getId(), DCATAPUriSchema.getDatasetContext());

        String hash = hash();

        if (model.containsResource(ResourceFactory.createResource(recordUriRef))) {
            Model recordModel = Piveau.extractAsModel(recordResource());
            model.remove(recordModel);
        }

        Model oldRecordModel = Piveau.extractAsModel(oldModel.getResource(recordUriRef), oldModel);
        model.add(oldRecordModel);

        Resource record = model.getResource(recordUriRef);
        updateRecord(record, hash);

        Map<String, Resource> dist = savedDistributionIds(oldModel);
        renameReferences(dist);

        extractTranslations(oldModel);
        extractMetadataExtensions(oldModel);
    }

    public void init(String piveauId) {
        String hash = hash();
        uriSchema = DCATAPUriSchema.createFor(piveauId, DCATAPUriSchema.getDatasetContext());
        renameReferences(Collections.emptyMap());
        initRecord(model.createResource(recordUriRef(), DCAT.CatalogRecord), hash);
    }

    public void setAccessURLs(DataUploadConnector dataUploadConnector) {
        model.listResourcesWithProperty(RDF.type, DCAT.Distribution).forEachRemaining(resource -> {
            String distId = DCATAPUriSchema.parseUriRef(resource.getURI()).getId();
            if (!resource.hasProperty(DCAT.accessURL)) {
                resource.addProperty(DCAT.accessURL, model.createResource(dataUploadConnector.getDataURL(distId)));
            }
        });
    }

    public Future<DatasetHelper> addDistribution(String content, String contentType) {
        return Future.future(promise -> {
            try {
                Model distModel = JenaUtils.read(content.getBytes(), contentType);

                Map<String, Resource> distIds = savedDistributionIds(model);

                String distid = getDistributionIdentifier(content, contentType);
                if (distIds.get(distid) != null) {
                    promise.fail(new ReplyException(ReplyFailure.RECIPIENT_FAILURE, 409, "Distribution already exists in Dataset. Use PUT for updating"));
                    return;
                }

                promise.handle(addDistribution(distModel, distIds));
            } catch (Exception e) {
                promise.fail(e);
            }
        });
    }

    public Future<DatasetHelper> updateDistribution(String content, String contentType, String oldDistUriRef) {
        Promise<DatasetHelper> helperPromise = Promise.promise();
        try {
            Model distModel = JenaUtils.read(content.getBytes(), contentType);

            Map<String, Resource> distIds = savedDistributionIds(model);

            removeDistribution(oldDistUriRef);
            addDistribution(distModel, distIds).onFailure(helperPromise::fail).onSuccess(helperPromise::complete);

        } catch (Exception e) {
            helperPromise.fail(e);
        }
        return helperPromise.future();
    }

    private Future<DatasetHelper> addDistribution(Model newDistribution, Map<String, Resource> distIds) {
        Promise<DatasetHelper> helperPromise = Promise.promise();

        try {
            model.add(newDistribution);

            // Get all distributions in the dist model and add them as DCAT:distribution reference to the Dataset
            newDistribution.listSubjectsWithProperty(RDF.type).forEachRemaining(s -> {
                s.listProperties(RDF.type).forEachRemaining(p -> {
                    if (p.getObject().equals(DCAT.Distribution)) {
                        model.listSubjectsWithProperty(RDF.type, DCAT.Dataset)
                                .forEachRemaining(ds ->
                                        ds.addProperty(DCAT.distribution, p.getSubject())
                                );
                    }

                });
            });

            renameReferences(distIds);

            updateRecord(recordResource(), hash());
            helperPromise.complete(this);

        } catch (Exception e) {
            helperPromise.fail(e);

        }
        return helperPromise.future();
    }


    public Future<String> removeDistribution(String distributionUriRef) {
        Promise<String> withOutDist = Promise.promise();

        ConstructBuilder cb = new ConstructBuilder()
                .addConstruct("<" + distributionUriRef + ">", "?q", "?x")
                .addConstruct("?x", "?p", "?y")
                .addWhere("<" + distributionUriRef + ">", "?q", "?x")
                .addOptional("?x", "?p", "?y");

        try (QueryExecution qexec = QueryExecutionFactory.create(cb.build(), model)) {
            Model result = qexec.execConstruct();
            Model diff = model.difference(result);
            model.getResource(uriRef())
                    .listProperties(DCAT.distribution)
                    .filterKeep(statement -> statement.getResource().getURI().equals(distributionUriRef))
                    .forEachRemaining(diff::remove);
            withOutDist.complete(JenaUtils.write(diff, Lang.NTRIPLES));

        } catch (Exception e) {
            withOutDist.fail(e);

        }

        return withOutDist.future();
    }


    private void initRecord(Resource record, String hash) {
        record.addProperty(FOAF.primaryTopic, record.getModel().createResource(uriRef()));
        record.addProperty(DCTerms.issued, ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);

        record.addProperty(DCTerms.modified, ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);

        record.addProperty(DCTerms.identifier, originId);

        Resource checksum = record.getModel().createResource(SPDX.Checksum);
        record.addProperty(SPDX.checksum, checksum);
        checksum.addProperty(SPDX.algorithm, SPDX.checksumAlgorithm_md5);
        checksum.addProperty(SPDX.checksumValue, hash);

        record.addProperty(DCTerms.creator, record.getModel().createResource("http://piveau.io"));
    }

    private void updateRecord(Resource record, String hash) {
        // We could clean up defective spdx checksum here
        record.getModel().removeAll(null, DEFECTIVE_SPDX.checksumValue, null);
        record.getModel().removeAll(null, DEFECTIVE_SPDX.checksum, null);
        record.getModel().removeAll(null, DEFECTIVE_SPDX.algorithm, null);
        record.getModel().removeAll(null, RDF.type, DEFECTIVE_SPDX.Checksum);

        Resource checksum = record.getPropertyResourceValue(SPDX.checksum);
        if (checksum == null) {
            // repair
            checksum = record.getModel().createResource(SPDX.Checksum);
            checksum.addProperty(SPDX.algorithm, SPDX.checksumAlgorithm_md5);
            record.addProperty(SPDX.checksum, checksum);
        }
        // repair
        if (!checksum.hasProperty(RDF.type, SPDX.Checksum)) {
            checksum.addProperty(RDF.type, SPDX.Checksum);
        }
        // repair
        if (!checksum.hasProperty(SPDX.algorithm)) {
            checksum.addProperty(SPDX.algorithm, SPDX.checksumAlgorithm_md5);
        }
        checksum.removeAll(SPDX.checksumValue);
        checksum.addProperty(SPDX.checksumValue, hash);

        record.removeAll(DCTerms.modified);
        record.addProperty(DCTerms.modified, ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);

        record.addProperty(DCTerms.creator, record.getModel().createResource("http://piveau.io"));
    }

    private void renameReferences(Map<String, Resource> identDist) {
        //rename datasets
        model.listSubjectsWithProperty(RDF.type, DCAT.Dataset).forEachRemaining(ds -> {
            Piveau.rename(ds, uriRef());
        });

        //rename distributions
        model.listResourcesWithProperty(RDF.type, DCAT.Distribution).forEachRemaining(resource -> {

            //set the id to lookup in the identDist map
            String id = "";

            if (resource.hasProperty(DCTerms.identifier) && resource.getProperty(DCTerms.identifier).getObject().isLiteral()) {
                id = resource.getProperty(DCTerms.identifier).getLiteral().getLexicalForm();
            } else if (resource.isURIResource()) {
                id = resource.getURI();
            } else if (resource.hasProperty(DCTerms.title)) {
                id = resource.getProperty(DCTerms.title).getLiteral().toString();
            } else if (resource.hasProperty(DCAT.accessURL)) {
                id = resource.getProperty(DCAT.accessURL).getResource().getURI();
            } else {
                id = UUID.randomUUID().toString();
            }

            //if the Distribution in the new model has an {@link DCTerms#identifier identifier} that was already present in the old model,
            //      give the new Distribution the same id/uriRef as the old distribution
            //else give it a new uriRef
            //
            if (identDist.containsKey(id)) {
                Resource renamed = Piveau.rename(resource, identDist.get(id).getURI());
                renamed.addProperty(DCTerms.identifier, id);
            } else {

                String newID = UUID.randomUUID().toString();

                Resource renamed = Piveau.rename(resource, DCATAPUriSchema.applyFor(newID, DCATAPUriSchema.getDistributionContext()).getDistributionUriRef());
                // if there is no {@link DCTerms#identifier identifier}, add the new one we can use on the next update
                if (!renamed.hasProperty(DCTerms.identifier)) {
                    renamed.addProperty(DCTerms.identifier, id);
                }
            }
        });
    }

    /**
     * This method collects all
     *
     * @param oldModel the old Model, from which the
     * @return A mapping from the distribution identifier to the distribution uri which looks like:
     * Map< Distribution {@link DCTerms#identifier identifier} as string, Map < Distribution Uri, Distribution {@link DCTerms#identifier identifier} as Statement>>
     */
    private Map<String, Resource> savedDistributionIds(Model oldModel) {
        Map<String, Resource> saved = new HashMap<>();
        oldModel.listResourcesWithProperty(RDF.type, DCAT.Distribution).forEachRemaining(dist -> dist.listProperties(DCTerms.identifier).forEachRemaining(id -> {
            if (id.getObject().isLiteral()) {
                saved.put(id.getLiteral().toString(), dist);
            } else if (id.getObject().isURIResource()) {
                saved.put(id.getResource().getURI(), dist);
            }
        }));
        return saved;
    }

    private void extractId() {
        ResIterator it = model.listSubjectsWithProperty(RDF.type, DCAT.CatalogRecord);
        if (it.hasNext()) {
            Resource record = it.next();
            if (record.isURIResource() && DCATAPUriSchema.isRecordUriRef(record.getURI()) && record.hasProperty(DCTerms.identifier)) {
                originId = record.getProperty(DCTerms.identifier).getLiteral().getLexicalForm();
            }
        }

        it = model.listSubjectsWithProperty(RDF.type, DCAT.Dataset);
        if (it.hasNext()) {
            Resource dataset = it.next();
            if (dataset.isURIResource() && DCATAPUriSchema.isDatasetUriRef(dataset.getURI())) {
                uriSchema = DCATAPUriSchema.parseUriRef(dataset.getURI());
            }
        }
    }

    private String getDistributionIdentifier(String distribution, String contentType) {
        Model model = JenaUtils.read(distribution.getBytes(), contentType);
        String identifier = "";

        ResIterator it = model.listSubjectsWithProperty(RDF.type, DCAT.Distribution);
        if (it.hasNext()) {
            Resource dist = it.next();

            identifier = JenaUtils.findIdentifier(dist);
            if (identifier == null && dist.isURIResource()) {
                identifier = dist.getURI();
            }
        }
        return identifier;
    }

    private void extractMetadataExtensions(Model oldModel) {
        if (oldModel.contains(resource(), DEXT.metadataExtension)) {
            Statement statement = oldModel.getProperty(resource(), DEXT.metadataExtension);
            if (statement.getObject().isResource()) {
                Resource metadataExtension = statement.getResource();
                Model extensionModel  = Piveau.extractAsModel(metadataExtension);
                model.add(extensionModel);
                model.add(resource(), DEXT.metadataExtension, metadataExtension);
            }
        }
    }

    private void extractTranslations(Model oldModel) {
        extractTranslationsFromResource(oldModel.getResource(uriRef()), resource(), DCTerms.title);
        extractTranslationsFromResource(oldModel.getResource(uriRef()), resource(), DCTerms.description);

        model.listResourcesWithProperty(RDF.type, DCAT.Distribution).forEachRemaining(dist -> {
            Resource oldDist = oldModel.getResource(dist.getURI());
            if (oldDist != null) {
                extractTranslationsFromResource(oldDist, dist, DCTerms.title);
                extractTranslationsFromResource(oldDist, dist, DCTerms.description);
            }
        });
    }

    private void extractTranslationsFromResource(Resource oldResource, Resource newResource, Property property) {
        oldResource.listProperties(property).filterKeep(pred -> pred.getLanguage().contains("mtec")).forEachRemaining(stm -> newResource.addLiteral(property, stm.getLiteral()));
    }

    private void extractDistributions(Resource oldResource, Resource newResource, Model translationModel) {
        StmtIterator iterator = oldResource.listProperties(DCAT.distribution);
        while (iterator.hasNext()) {
            Statement statement = iterator.nextStatement();
            if (statement.getSubject().isResource()) {
                Resource distribution = statement.getResource();
                String distributionUri = distribution.getURI();
                Resource newDistribution = translationModel.createResource(distributionUri);
                newResource.addProperty(DCAT.distribution, newDistribution);
                this.extractTranslationsFromResource(distribution, newDistribution, DCTerms.title);
                this.extractTranslationsFromResource(distribution, newDistribution, DCTerms.description);
            }
        }
    }
}
