package io.piveau.hub.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.piveau.hub.search.handler.CatalogueHandler;
import io.piveau.hub.search.handler.CkanHandler;
import io.piveau.hub.search.handler.DataServiceHandler;
import io.piveau.hub.search.handler.DatasetFeedHandler;
import io.piveau.hub.search.handler.DatasetHandler;
import io.piveau.hub.search.handler.FeedHandler;
import io.piveau.hub.search.handler.GazetteerHandler;
import io.piveau.hub.search.handler.ResourceHandler;
import io.piveau.hub.search.handler.SearchHandler;
import io.piveau.hub.search.handler.SitemapHandler;
import io.piveau.hub.search.handler.VocabularyHandler;
import io.piveau.hub.search.services.catalogues.CataloguesService;
import io.piveau.hub.search.services.catalogues.CataloguesServiceVerticle;
import io.piveau.hub.search.services.dataservices.DataServicesService;
import io.piveau.hub.search.services.dataservices.DataServicesServiceVerticle;
import io.piveau.hub.search.services.datasets.DatasetsService;
import io.piveau.hub.search.services.datasets.DatasetsServiceVerticle;
import io.piveau.hub.search.services.gazetteer.GazetteerService;
import io.piveau.hub.search.services.gazetteer.GazetteerServiceVerticle;
import io.piveau.hub.search.services.resources.ResourcesService;
import io.piveau.hub.search.services.resources.ResourcesServiceVerticle;
import io.piveau.hub.search.services.search.SearchService;
import io.piveau.hub.search.services.search.SearchServiceVerticle;
import io.piveau.hub.search.services.sitemaps.SitemapsService;
import io.piveau.hub.search.services.sitemaps.SitemapsServiceVerticle;
import io.piveau.hub.search.services.vocabulary.VocabularyService;
import io.piveau.hub.search.services.vocabulary.VocabularyServiceVerticle;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.openapi.OpenAPIExtender;
import io.piveau.hub.search.verticles.ShellVerticle;
import io.piveau.json.ConfigHelper;
import io.piveau.security.ApiKeyAuthProvider;
import io.piveau.utils.ConfigurableAssetHandler;
import io.piveau.utils.PiveauContext;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.APIKeyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.ext.web.validation.BodyProcessorException;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    private ResourceHandler resourceHandler;
    private DatasetHandler datasetHandler;
    private CatalogueHandler catalogueHandler;
    private DataServiceHandler dataServiceHandler;
    private VocabularyHandler vocabularyHandler;
    private SearchHandler searchHandler;
    private GazetteerHandler gazetteerHandler;
    private CkanHandler ckanHandler;
    private FeedHandler feedHandler;
    private DatasetFeedHandler datasetFeedHandler;
    private SitemapHandler sitemapHandler;

    private PiveauContext verticleContext;

    private IndexManager indexManager;

    @Override
    public void start(Promise<Void> startPromise) {
        loadConfig()
                .compose(this::initIndexManager)
                .compose(this::bootstrapVerticles)
                .compose(this::startServer)
                .onComplete(handler -> {
            if (handler.succeeded()) {
                LOG.info("Successfully launched hub-search");
                startPromise.complete();
            } else {
                LOG.error("Failed to launch hub-search: " + handler.cause());
                startPromise.fail(handler.cause());
            }
        });
        this.verticleContext = new PiveauContext("hub.search", "MainVerticle");
    }

    private Future<Void> startServer(JsonObject config) {
        Promise<Void> promise = Promise.promise();

        Integer servicePort = config.getInteger(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE_PORT, Defaults.SERVICE_PORT);

        Map<String, List<String>> apiKeys = new HashMap<>();
        apiKeys.put(config.getString(Constants.ENV_PIVEAU_HUB_SEARCH_API_KEY, Defaults.APIKEY), Collections.singletonList("*"));

        Set<String> allowedHeaders = Set.of(
                "x-requested-with",
                "Access-Control-Allow-Origin",
                "origin",
                "Content-Type",
                "accept",
                "Authorization",
                "X-API-Key"
        );

        Set<HttpMethod> allowedMethods = Set.of(
                HttpMethod.GET,
                HttpMethod.HEAD,
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.PATCH,
                HttpMethod.OPTIONS
        );

        final String staticWebrootPath = "webroot";
        final String staticOpenAPIPath = staticWebrootPath + "/openapi.yaml";

        final String webrootPath = config.getString(Constants.ENV_PIVEAU_WEBROOT_PATH, "conf/webroot");
        final String openAPIPath;

        if (indexManager.getOpenAPIs().isEmpty()) {
            openAPIPath = staticOpenAPIPath;
        } else {
            openAPIPath = webrootPath + "/openapi.yaml";
            if (!vertx.fileSystem().existsBlocking(webrootPath)) {
                vertx.fileSystem().copyRecursiveBlocking(staticWebrootPath, webrootPath, true);
            }
            OpenAPIExtender.extend(vertx, indexManager, staticOpenAPIPath, openAPIPath);
        }

        RouterBuilder.create(vertx, openAPIPath, handler -> {
            if (handler.succeeded()) {
                RouterBuilder builder = handler.result();

                RouterBuilderOptions options = new RouterBuilderOptions()
                        .setMountNotImplementedHandler(true)
                        .setRequireSecurityHandlers(true);

                builder.setOptions(options);

                builder.securityHandler("ApiKeyAuth", APIKeyHandler.create(
                        new ApiKeyAuthProvider(apiKeys)).header("X-API-Key"));
                builder.securityHandler("ApiKeyAuth2", APIKeyHandler.create(
                        new ApiKeyAuthProvider(apiKeys)).header("Authorization"));

                for (String index : indexManager.getIndexList()) {
                    if (index.startsWith("resource_")) {
                        String resourceType = OpenAPIExtender.getResourceType(index);
                        String resourceName = OpenAPIExtender.toCamelCase(resourceType);

                        builder.operation("list" + resourceName).handler(resourceHandler::listResources);
                        builder.operation("read" + resourceName).handler(resourceHandler::readResource);
                        builder.operation("delete" + resourceName).handler(resourceHandler::deleteResource);
                        builder.operation("createOrUpdate" + resourceName).handler(resourceHandler::createOrUpdateResource);

                        builder.operation("listResourceTypes").handler(resourceHandler::listResourceTypes);
                    }
                }

                builder.operation("listDatasets").handler(datasetHandler::listDatasets);
                builder.operation("createDataset").handler(datasetHandler::createDataset);
                builder.operation("createOrUpdateDataset").handler(datasetHandler::createOrUpdateDataset);
                builder.operation("modifyDataset").handler(datasetHandler::modifyDataset);
                builder.operation("readDataset").handler(datasetHandler::readDataset);
                builder.operation("deleteDataset").handler(datasetHandler::deleteDataset);
                builder.operation("createOrUpdateDatasetBulk").handler(datasetHandler::createOrUpdateDatasetBulk);
                builder.operation("readDatasetRevision").handler(datasetHandler::readDatasetRevision);

                builder.operation("listCatalogues").handler(catalogueHandler::listCatalogues);
                builder.operation("createCatalogue").handler(catalogueHandler::createCatalogue);
                builder.operation("createOrUpdateCatalogue").handler(catalogueHandler::createOrUpdateCatalogue);
                builder.operation("modifyCatalogue").handler(catalogueHandler::modifyCatalogue);
                builder.operation("readCatalogue").handler(catalogueHandler::readCatalogue);
                builder.operation("deleteCatalogue").handler(catalogueHandler::deleteCatalogue);

                // builder.operation("createDataService").handler(dataServiceHandler::createDataService);
                // builder.operation("createOrUpdateDataService").handler(dataServiceHandler::createOrUpdateDataService);
                // builder.operation("modifyDataService").handler(dataServiceHandler::modifyDataService);
                // builder.operation("readDataService").handler(dataServiceHandler::readDataService);
                // builder.operation("deleteDataService").handler(dataServiceHandler::deleteDataService);

                builder.operation("readVocabularies").handler(vocabularyHandler::readVocabularies);
                builder.operation("createOrUpdateVocabulary").handler(vocabularyHandler::createOrUpdateVocabulary);
                builder.operation("readVocabulary").handler(vocabularyHandler::readVocabulary);
                builder.operation("deleteVocabulary").handler(vocabularyHandler::deleteVocabulary);

                builder.operation("createVocable").handler(vocabularyHandler::createVocable);
                builder.operation("createOrUpdateVocable").handler(vocabularyHandler::createOrUpdateVocable);
                builder.operation("modifyVocable").handler(vocabularyHandler::modifyVocable);
                builder.operation("readVocable").handler(vocabularyHandler::readVocable);
                builder.operation("deleteVocable").handler(vocabularyHandler::deleteVocable);

                builder.operation("searchGet").handler(searchHandler::searchGet);
                builder.operation("scrollGet").handler(searchHandler::scrollGet);

                builder.operation("gazetteerAutocomplete").handler(gazetteerHandler::autocomplete);

                builder.operation("ckanPackageSearch").handler(ckanHandler::package_search);
                builder.operation("ckanPackageShow").handler(ckanHandler::package_show);

                builder.operation("datasets.atom").handler(feedHandler::atom);
                builder.operation("datasets.rss").handler(feedHandler::rss);

                builder.operation("datasetrevisions.rss").handler(datasetFeedHandler::rss);

                builder.operation("readSitemapIndex").handler(sitemapHandler::readSitemapIndex);
                builder.operation("readSitemap").handler(sitemapHandler::readSitemap);

                Router router = builder.createRouter();

                if (indexManager.getOpenAPIs().isEmpty()) {
                    router.route().handler(StaticHandler.create());
                } else {
                    router.route().handler(StaticHandler.create("conf/webroot"));
                }

                HealthCheckHandler hch = HealthCheckHandler.create(vertx);
                hch.register("buildInfo", status -> vertx.fileSystem()
                        .readFile("buildInfo.json")
                        .onSuccess(buffer -> status.complete(Status.OK(buffer.toJsonObject())))
                        .onFailure(status::fail));
                router.get("/health").handler(hch);

                WebClient webClient = WebClient.create(vertx);

                router.route("/images/favicon").handler(new ConfigurableAssetHandler(config.getString(
                        Constants.ENV_PIVEAU_FAVICON_PATH, "webroot/images/favicon.png"), webClient));
                router.route("/images/logo").handler(new ConfigurableAssetHandler(config.getString(
                        Constants.ENV_PIVEAU_LOGO_PATH, "webroot/images/logo.png"), webClient));

                router.route("/imprint").handler(context ->
                        context.redirect(config.getString(Constants.ENV_PIVEAU_IMPRINT_URL, "/")));

                router.route("/privacy").handler(context ->
                        context.redirect(config.getString(Constants.ENV_PIVEAU_PRIVACY_URL, "/")));

                router.route().handler(CorsHandler.create().addRelativeOrigin(".*")
                        .allowedHeaders(allowedHeaders)
                        .allowedMethods(allowedMethods)
                );

                router.errorHandler(400, context -> {
                    Throwable failure = context.failure();
                    if (failure instanceof BodyProcessorException) {
                        LOG.debug(failure.getMessage());
                        context.response().putHeader("Content-Type", "text/plain");
                        JsonObject body = context.body().asJsonObject();
                        if (body != null) {
                            String id = context.pathParams() != null ? context.pathParams().get("id") : null;
                            String path = context.normalizedPath();
                            if (path != null && path.length() > 2) {
                                if (id == null) id = "id-not-available";
                                else path = path.replaceFirst(id, "");
                                if (path.charAt(0) == '/') path = path.substring(1);
                                if (path.charAt(path.length() - 1) == '/') path = path.substring(0, path.length() - 1);
                                String resource = path + ": " + id;
                                if (path.equals("datasets")) {
                                    JsonObject catalog = body.getJsonObject("catalog");
                                    if (catalog != null) {
                                        String catalogId = catalog.getString("id");
                                        if (catalogId != null && !catalogId.isEmpty()) {
                                            resource += "; catalog: " + catalogId;
                                        }
                                    }
                                }
                                PiveauContext resourceContext = verticleContext.extend(resource);
                                resourceContext.log().error("Open API validation failure: " + failure.getCause().toString());
                            }
                        }
                        context.response().setStatusCode(400);
                        context.response().end(failure.getCause().toString());
                    }
                });

                HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(servicePort));
                server.requestHandler(router).listen((ar) -> {
                    if (ar.succeeded()) {
                        LOG.info("Successfully launched server on port [{}]", servicePort);
                        promise.complete();
                    } else {
                        LOG.error("Failed to start server at [{}]", servicePort, handler.cause());
                        promise.fail(ar.cause());
                    }
                });
            } else {
                // Something went wrong during router factory initialization
                LOG.error("Failed to start server at [{}]", servicePort, handler.cause());
                promise.fail(handler.cause());
            }
        });

        return promise.future();
    }

    private Future<JsonObject> loadConfig() {
        Promise<JsonObject> promise = Promise.promise();

        ConfigStoreOptions envStoreOptions = new ConfigStoreOptions()
                .setType("env")
                .setConfig(new JsonObject().put("keys", new JsonArray()
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE_PORT)
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_API_KEY)
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_ES_CONFIG)
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_CLI_CONFIG)
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_GAZETTEER_CONFIG)
                        .add(Constants.ENV_PIVEAU_HUB_SEARCH_SITEMAP_CONFIG)
                        .add(Constants.ENV_PIVEAU_LOGO_PATH)
                        .add(Constants.ENV_PIVEAU_FAVICON_PATH)
                        .add(Constants.ENV_PIVEAU_IMPRINT_URL)
                        .add(Constants.ENV_PIVEAU_PRIVACY_URL)
                ));

        ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions()
        	    .setType("file")
        	    .setOptional(true)
        	    .setConfig(new JsonObject().put("path", "conf/config.json"));

        	ConfigRetrieverOptions options = new ConfigRetrieverOptions();
        	options.addStore(fileStoreOptions)
        	.addStore(envStoreOptions);
        	

        	ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        	retriever.getConfig(ar -> {
        	    if (ar.succeeded()) {
        	        JsonObject config = ar.result();
        	        JsonObject interpolatedConfig = interpolateEnvironmentVariables(config);
        	        LOG.info(interpolatedConfig.encodePrettily());
        	        promise.complete(interpolatedConfig);
        	    } else {
        	        promise.fail(ar.cause());
        	    }
        	});

        return promise.future();
    }

    private Future<JsonObject> initIndexManager(JsonObject jsonArg ) {
        Promise<JsonObject> promise = Promise.promise();

        JsonObject config = interpolateEnvironmentVariables(jsonArg);
        
        JsonObject esConfig = ConfigHelper.forConfig(config).forceJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_ES_CONFIG);
        IndexManager.create(vertx, esConfig, indexManagerCreateResult -> {
            if (indexManagerCreateResult.succeeded()) {
                this.indexManager = indexManagerCreateResult.result();
                promise.complete(config);
            } else {
                promise.fail(indexManagerCreateResult.cause());
            }
        });

        return promise.future();
    }
    
    private JsonObject interpolateEnvironmentVariables(JsonObject config) {
        JsonObject interpolatedConfig = new JsonObject();
        for (Map.Entry<String, Object> entry : config) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                String interpolatedValue = interpolateString((String) value);
                interpolatedConfig.put(key, interpolatedValue);
            } else if (value instanceof JsonObject) {
                JsonObject nestedConfig = (JsonObject) value;
                JsonObject interpolatedNestedConfig = interpolateEnvironmentVariables(nestedConfig);
                interpolatedConfig.put(key, interpolatedNestedConfig);
            } else {
                interpolatedConfig.put(key, value);
            }
        }
        return interpolatedConfig;
    }

    private String interpolateString(String value) {
        String interpolatedValue = value;
        if (value.contains("{{env.") && value.contains("}}")) {
            int start = value.indexOf("{{env.") + 6;
            int end = value.indexOf("}}");
            String envVariable = value.substring(start, end);
            String envValue = System.getenv(envVariable);
            if (envValue != null) {
                interpolatedValue = value.replace("{{env." + envVariable + "}}", envValue);
            }
        }
        LOG.trace("interpolatedValue: {}", interpolatedValue);
        return interpolatedValue;
    }

    private Future<JsonObject> bootstrapVerticles(JsonObject config) {
        Promise<JsonObject> promise = Promise.promise();

        Promise<String> shellPromise = Promise.promise();
        vertx.deployVerticle(ShellVerticle.class.getName(),
                new DeploymentOptions().setConfig(config).setWorker(true), shellPromise);

        Promise<String> gazetteerPromise = Promise.promise();
        vertx.deployVerticle(GazetteerServiceVerticle.class.getName(),
                new DeploymentOptions().setConfig(config).setWorker(true), gazetteerPromise);

        Promise<String> resourcesPromise = Promise.promise();
        vertx.deployVerticle(() -> new ResourcesServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), resourcesPromise);

        Promise<String> datasetsPromise = Promise.promise();
        vertx.deployVerticle(() -> new DatasetsServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), datasetsPromise);

        Promise<String> cataloguesPromise = Promise.promise();
        vertx.deployVerticle(() -> new CataloguesServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), cataloguesPromise);

        Promise<String> dataServicePromise = Promise.promise();
        vertx.deployVerticle(() -> new DataServicesServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), dataServicePromise);

        Promise<String> vocabularyPromise = Promise.promise();
        vertx.deployVerticle(() -> new VocabularyServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), vocabularyPromise);

        Promise<String> searchServiceVerticle = Promise.promise();
        vertx.deployVerticle(() -> new SearchServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), searchServiceVerticle);

        Promise<String> sitemapsServiceVerticle = Promise.promise();
        vertx.deployVerticle(() -> new SitemapsServiceVerticle(this.indexManager),
                new DeploymentOptions().setConfig(config).setWorker(true), sitemapsServiceVerticle);

        Future.all(Arrays.asList(shellPromise.future(), gazetteerPromise.future(), resourcesPromise.future(),
                        datasetsPromise.future(), cataloguesPromise.future(), dataServicePromise.future(),
                        vocabularyPromise.future(), searchServiceVerticle.future(), sitemapsServiceVerticle.future()))
                .onComplete(ar -> {
                    if (ar.succeeded()) {
                        resourceHandler = new ResourceHandler(vertx, ResourcesService.SERVICE_ADDRESS);
                        datasetHandler = new DatasetHandler(vertx, DatasetsService.SERVICE_ADDRESS);
                        catalogueHandler = new CatalogueHandler(vertx, CataloguesService.SERVICE_ADDRESS);
                        dataServiceHandler = new DataServiceHandler(vertx, DataServicesService.SERVICE_ADDRESS);
                        vocabularyHandler = new VocabularyHandler(vertx, VocabularyService.SERVICE_ADDRESS);
                        gazetteerHandler = new GazetteerHandler(vertx, GazetteerService.SERVICE_ADDRESS);
                        searchHandler = new SearchHandler(vertx, SearchService.SERVICE_ADDRESS);
                        ckanHandler = new CkanHandler(vertx, SearchService.SERVICE_ADDRESS, DatasetsService.SERVICE_ADDRESS);
                        feedHandler = new FeedHandler(config.getJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_FEED_CONFIG),
                                vertx, SearchService.SERVICE_ADDRESS);
                        datasetFeedHandler = new DatasetFeedHandler(config.getJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_FEED_CONFIG),
                                vertx,DatasetsService.SERVICE_ADDRESS);
                        sitemapHandler = new SitemapHandler(vertx, SitemapsService.SERVICE_ADDRESS);
                        promise.complete(config);
                    } else {
                        promise.fail(ar.cause());
                    }
                });

        return promise.future();
    }

    public static void main(String[] args) {
        String[] params = Arrays.copyOf(args, args.length + 1);
        params[params.length - 1] = MainVerticle.class.getName();
        Launcher.executeCommand("run", params);
    }
}
