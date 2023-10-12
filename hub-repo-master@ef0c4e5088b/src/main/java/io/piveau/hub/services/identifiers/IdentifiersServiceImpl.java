package io.piveau.hub.services.identifiers;

import io.piveau.dcatap.DatasetManager;
import io.piveau.dcatap.TripleStore;
import io.piveau.hub.dataobjects.DatasetHelper;
import io.piveau.hub.services.datasets.DatasetsService;
import io.piveau.hub.Constants;
import io.piveau.hub.indexing.Indexing;
import io.piveau.json.ConfigHelper;
import io.piveau.utils.JenaUtils;
import io.piveau.vocabularies.vocabulary.ADMS;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.datatypes.TypeMapper;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class IdentifiersServiceImpl implements IdentifiersService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TripleStore tripleStore;
    private final WebClient client;
    private final Vertx vertx;
    private final ConfigHelper configHelper;
    private final JsonArray identifiersConfig;
    private final DatasetManager datasetManager;
    private final DatasetsService datasetsService;
    private HashMap<String, IdentifierRegistry> registries = new HashMap<>();


    IdentifiersServiceImpl(Vertx vertx, WebClient client, JsonObject config, TripleStore tripleStore,
                           Handler<AsyncResult<IdentifiersService>> readyHandler) {
        this.tripleStore = tripleStore;
        this.vertx = vertx;
        this.client = client;
        this.configHelper = ConfigHelper.forConfig(config);
        identifiersConfig = configHelper.forceJsonArray(Constants.ENV_PIVEAU_IDENTIFIERS_REGISTRATION);
        this.datasetManager = tripleStore.getDatasetManager();
        datasetsService = DatasetsService.createProxy(vertx, DatasetsService.SERVICE_ADDRESS);
        if(identifiersConfig.isEmpty()) {
            logger.debug("There are no Identifiers Registries configured");
        } else {
            logger.debug("Identifiers Registries config " + identifiersConfig.encodePrettily());
        }
        initRegistries();
        readyHandler.handle(Future.succeededFuture(this));
    }

    /**
     * Init all Identifier Registries by iterating through the config
     */
    private void initRegistries() {
        identifiersConfig.forEach(o -> {
            if(o instanceof JsonObject) {
                JsonObject registry = (JsonObject) o;
                String type = registry.getString("type");
                String enabled = registry.getString("enabled");
                if(type != null && enabled != null) {
                    switch (type) {
                        case "mock":
                            MockRegistry mockRegistry = new MockRegistry(vertx, client, registry);
                            registries.put("mock", mockRegistry);
                            logger.debug(mockRegistry.getName() + " is enabled.");
                            break;
                        case "eu-ra-doi":
                            EURADOIRegistry euradoiRegistry = new EURADOIRegistry(vertx, client, registry);
                            registries.put("eu-ra-doi", euradoiRegistry);
                            logger.debug(euradoiRegistry.getName() + " is enabled.");
                            break;
                        default:
                            logger.debug("There is no Identity Registry for type " + type);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public IdentifiersService createIdentifier(String datasetId, String catalogueId, String type, Handler<AsyncResult<JsonObject>> handler) {
        if(!registries.containsKey(type)) {
            handler.handle(ServiceException.fail(500, "The identifier type is not configured"));
            return this;
        }

        IdentifierRegistry identifierRegistry = registries.get(type);

        AtomicReference<DatasetHelper> datasetHelper = new AtomicReference<>();
        AtomicReference<Identifier> identifier = new AtomicReference<>();

        // Get the dataset from the Triplestore
        datasetManager.get(datasetId, catalogueId)
                .onFailure(cause -> handler.handle(ServiceException.fail(404, "The dataset was not found.")))
                .compose(model -> DatasetHelper.create(model)
                        .onFailure(cause -> handler.handle(ServiceException.fail(500, "Something went wrong."))))
                .compose(helper -> {
                    datasetHelper.set(helper);
                    return identifierRegistry.getIdentifier(helper.uriRef(), helper, getExistingIdentifiers(helper))
                            .onFailure(cause -> handler.handle(ServiceException.fail(500, cause.getMessage())));
                })
                .compose(id -> {
                    identifier.set(id);
                    setIdentifier(identifier.get(), datasetHelper.get());

                    return datasetsService.putDatasetOrigin(datasetId, JenaUtils.write(datasetHelper.get().model(), "text/n-quads"), "text/n-quads", catalogueId, false)
                            .onFailure(cause -> {
                                logger.error("Store back dataset with new identifier", cause);
                                handler.handle(Future.failedFuture(cause));
                            });
                })
                .onSuccess(result -> {
                    JsonObject status = new JsonObject()
                            .put("status", "success")
                            .put("identifier", identifier.get().identifierURI);
                    handler.handle(Future.succeededFuture(status));
                });
        return this;
    }

    private void setIdentifier(Identifier identifier, DatasetHelper helper) {
        Resource resourceOld = helper.model().getResource(identifier.identifierURI);
        resourceOld.removeProperties();

        Resource resource = helper.model().createResource(identifier.identifierURI)
                .addProperty(RDF.type, ADMS.Identifier)
                .addProperty(SKOS.notation, identifier.identifier, TypeMapper.getInstance().getSafeTypeByName(identifier.schema))
                .addProperty(DCTerms.creator, helper.model().createResource(identifier.creatorURI))
                .addProperty(DCTerms.issued, identifier.issued.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);

        helper.resource().addProperty(ADMS.identifier, resource);
    }

    private List<Identifier> getExistingIdentifiers(DatasetHelper helper) {
        List<Identifier> identifiers = new ArrayList<>();
        helper.resource().listProperties(ADMS.identifier).forEach(statement -> {
            if(statement.getObject().isURIResource()) {
                Resource res = statement.getResource();
                Identifier identifier = new Identifier();
                identifier.identifierURI = res.getURI();
                if(res.hasProperty(SKOS.notation)) {
                    identifier.identifier = res.getProperty(SKOS.notation).getString();
                    identifier.schema = res.getProperty(SKOS.notation).getLiteral().getDatatypeURI();
                }
                identifiers.add(identifier);
            }
        });
        return identifiers;
    }

    @Override
    public IdentifiersService checkIdentifierRequirement(String datasetId, String catalogueId, String type, Handler<AsyncResult<JsonObject>> handler) {
        AtomicReference<DatasetHelper> datasetHelper = new AtomicReference<>();
        // Get the dataset from the Triplestore
        datasetManager.get(datasetId, catalogueId)
                .compose(model -> DatasetHelper.create(model)
                        .onFailure(cause -> handler.handle(ServiceException.fail(500, "Something went wrong.")))
                )
                .onSuccess(helper -> {
                    datasetHelper.set(helper);
                    JsonObject metadata = Indexing.indexingDataset(helper.resource(), helper.recordResource(), null, "en");

                    JsonArray statusArr = new JsonArray();
                    if (type == null) {
                        for (String registry : registries.keySet()) {
                            statusArr.add(registries.get(registry).getIdentifierEligibility(metadata));
                        }
                    } else {
                        statusArr.add(registries.get(type).getIdentifierEligibility(metadata));
                    }

                    JsonObject status = new JsonObject();
                    status.put("status", statusArr);
                    handler.handle(Future.succeededFuture(status));
                })
                .onFailure(cause -> handler.handle(ServiceException.fail(404, "The dataset was not found.")));

        return this;
    }

}
