package io.piveau.metrics.cache;

import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.json.ConfigHelper;
import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.dqv.DqvVerticle;
import io.piveau.metrics.cache.dqv.sparql.QueryCollection;
import io.piveau.metrics.cache.persistence.DatabaseProvider;
import io.piveau.metrics.cache.persistence.DatabaseVerticle;
import io.piveau.metrics.cache.persistence.DocumentScope;
import io.piveau.metrics.cache.quartz.QuartzService;
import io.piveau.metrics.cache.quartz.QuartzServiceVerticle;
import io.piveau.security.ApiKeyAuthProvider;
import io.piveau.utils.ConfigurableAssetHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.APIKeyHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.serviceproxy.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    private DatabaseProvider databaseProvider;

    private QuartzService quartzService;
    private DqvProvider dqvProvider;

    public static void main(String[] args) {
        String[] params = Arrays.copyOf(args, args.length + 1);
        params[params.length - 1] = MainVerticle.class.getName();
        Launcher.executeCommand("run", params);
    }

    @Override
    public void start(Promise<Void> startPromise) {
        log.info("Launching cache service...");

        QueryCollection.init(vertx, "queries");

        // startup is only successful if no step failed
        loadConfig()
                .compose(this::bootstrapVerticles)
                .compose(this::startServer)
                .onComplete(startPromise);
    }

    private Future<JsonObject> loadConfig() {
        return Future.future(promise -> ConfigRetriever.create(vertx)
                .getConfig()
                .onSuccess(config -> {
                    if (log.isDebugEnabled()) {
                        log.debug(config.encodePrettily());
                    }
                    JsonObject schemaConfig = ConfigHelper.forConfig(config).forceJsonObject(ApplicationConfig.ENV_PIVEAU_DCATAP_SCHEMA_CONFIG);
                    if (schemaConfig.isEmpty()) {
                        schemaConfig.put("baseUri", config.getString(ApplicationConfig.ENV_BASE_URI, DCATAPUriSchema.DEFAULT_BASE_URI));
                    }
                    DCATAPUriSchema.INSTANCE.setConfig(schemaConfig);
                    promise.complete(config);
                })
                .onFailure(promise::fail));
    }

    private Future<JsonObject> bootstrapVerticles(JsonObject config) {
        return Future.future(promise ->
                vertx.deployVerticle(DqvVerticle.class, new DeploymentOptions()
                                .setConfig(ConfigHelper.forConfig(config).forceJsonObject("PIVEAU_TRIPLESTORE_CONFIG"))
                                .setWorker(true))
                        .compose(id -> vertx.deployVerticle(DatabaseVerticle.class, new DeploymentOptions()
                                .setConfig(config)
                                .setWorker(true)))
                        .compose(id -> vertx.deployVerticle(QuartzServiceVerticle.class, new DeploymentOptions()
                                .setWorker(true)
                                .setConfig(config)))
                        .onSuccess(id -> promise.complete(config))
                        .onFailure(promise::fail)
        );
    }

    private Future<Void> startServer(JsonObject config) {
        return Future.future(startServer -> {
            databaseProvider = DatabaseProvider.createProxy(vertx, DatabaseProvider.SERVICE_ADDRESS);
            Integer port = config.getInteger(ApplicationConfig.ENV_APPLICATION_PORT, ApplicationConfig.DEFAULT_APPLICATION_PORT);
//FIXME
            DeliveryOptions options1 = new DeliveryOptions().setSendTimeout(3000000);
            dqvProvider = DqvProvider.createProxy(vertx, DqvProvider.SERVICE_ADDRESS, options1);

            quartzService = QuartzService.createProxy(vertx, QuartzService.SERVICE_ADDRESS);

            RouterBuilder.create(vertx, "webroot/openapi.yaml")
                    .onSuccess(builder -> {

                        RouterBuilderOptions options = new RouterBuilderOptions().setMountNotImplementedHandler(true);
                        builder.setOptions(options);


                        JsonArray corsDomains = ConfigHelper.forConfig(config).forceJsonArray(ApplicationConfig.ENV_CACHE_CORS_DOMAINS);
                        if (!corsDomains.isEmpty()) {

                            Set<String> allowedHeaders = new HashSet<>();
                            allowedHeaders.add("x-requested-with");
                            allowedHeaders.add("Access-Control-Allow-Origin");
                            allowedHeaders.add("origin");
                            allowedHeaders.add("Content-Type");
                            allowedHeaders.add("accept");
                            allowedHeaders.add("Authorization");
                            allowedHeaders.add("Access-Control-Allow-Headers");
                            allowedHeaders.add("X-API-Key");

                            Set<HttpMethod> allowedMethods = new HashSet<>();
                            allowedMethods.add(HttpMethod.GET);
                            allowedMethods.add(HttpMethod.POST);
                            allowedMethods.add(HttpMethod.OPTIONS);
                            allowedMethods.add(HttpMethod.DELETE);
                            allowedMethods.add(HttpMethod.PATCH);
                            allowedMethods.add(HttpMethod.PUT);

                            ArrayList<String> corsArray = new ArrayList<>();
                            for (int i = 0; i < corsDomains.size(); i++) {
                                //convert into normal array and escape dots for regex compatibility
                                corsArray.add(corsDomains.getString(i).replace(".", "\\."));
                            }

                            //"^(https?:\\/\\/(?:.+\\.)?(?:fokus\\.fraunhofer\\.de|localhost)(?::\\d{1,5})?)$"
                            String corsString = "^(https?:\\/\\/(?:.+\\.)?(?:" + String.join("|", corsArray) + ")(?::\\d{1,5})?)$";
                            builder.rootHandler(CorsHandler.create(corsString).allowedHeaders(allowedHeaders).allowedMethods(allowedMethods).allowCredentials(true));
                        } else {
                            log.info("no CORS domains configured");
                        }
                        builder.rootHandler(BodyHandler.create());
                        builder.rootHandler(StaticHandler.create());

                        String apiKey = config.getString(ApplicationConfig.ENV_APIKEY, "anyApiKey");
                        builder.securityHandler("ApiKeyAuth", APIKeyHandler.create(new ApiKeyAuthProvider(apiKey)).header("Authorization"));

                        // Administration
                        builder.operation("refreshMetrics").handler(this::refreshMetrics);
                        builder.operation("refreshSingleMetrics").handler(this::refreshSingleMetrics);
                        builder.operation("clearMetrics").handler(this::clearMetrics);
                        builder.operation("migrateScore").handler(this::migrateScore);
                        builder.operation("scheduleRefresh").handler(this::scheduleRefresh);
                        builder.operation("getScheduleRefresh").handler(this::getSchedules);

                        // Global
                        builder.operation("getGlobalMetrics").handler(context -> fetchCurrent(DocumentScope.GLOBAL, context));
                        builder.operation("getHistoricGlobalMetrics").handler(context -> fetchHistory(DocumentScope.GLOBAL, context));

                        // Countries
                        builder.operation("getSingleCountryMetrics").handler(context -> fetchCurrent(DocumentScope.COUNTRY, context));
                        builder.operation("getHistoricSingleCountryMetrics").handler(context -> fetchHistory(DocumentScope.COUNTRY, context));
                        builder.operation("getCountryMetrics").handler(context -> fetchCurrentList(DocumentScope.COUNTRY, context));
                        builder.operation("getHistoricCountryMetrics").handler(context -> fetchHistoryList(DocumentScope.COUNTRY, context));

                        // Catalogues
                        builder.operation("getSingleCatalogueMetrics").handler(context -> fetchCurrent(DocumentScope.CATALOGUE, context));
                        builder.operation("getHistoricSingleCatalogueMetrics").handler(context -> fetchHistory(DocumentScope.CATALOGUE, context));
                        builder.operation("getCatalogueMetrics").handler(context -> fetchCurrentList(DocumentScope.CATALOGUE, context));
                        builder.operation("getHistoricCatalogueMetrics").handler(context -> fetchHistoryList(DocumentScope.CATALOGUE, context));
                        builder.operation("deleteCatalogueMetrics").handler(this::deleteCatalogueMetrics);
                        builder.operation("getCatalogueDistributionReachability").handler(this::getCatalogueDistributionReachability);
                        builder.operation("getCatalogueViolations").handler(this::getViolations);

                        // Datasets
                        builder.operation("getSingleDatasetMetrics").handler(this::getSingleDatasetMetrics);
                        builder.operation("getDistributionMetrics").handler(this::getAllDistributionMetrics);
                        builder.operation("getDistributionMetricsv2").handler(this::getAllDistributionMetrics);

                        WebClient client = WebClient.create(vertx);

                        Router router = builder.createRouter();
                        router.route("/images/favicon").handler(new ConfigurableAssetHandler(config.getString("PIVEAU_FAVICON_PATH", "webroot/images/favicon.png"), client));
                        router.route("/images/logo").handler(new ConfigurableAssetHandler(config.getString("PIVEAU_LOGO_PATH", "webroot/images/logo.png"), client));

                        router.route("/imprint").handler(context ->
                                context.redirect(config.getString("PIVEAU_IMPRINT_URL", "/")));

                        router.route("/privacy").handler(context ->
                                context.redirect(config.getString("PIVEAU_PRIVACY_URL", "/")));

                        router.errorHandler(400, context -> {
                            if (context.failure() instanceof BadRequestException bre) {
                                JsonObject error = new JsonObject()
                                        .put("status", "error")
                                        .put("message", bre.getMessage());
                                context.response().setStatusCode(400).putHeader("Content-Type", "application/json").end(error.encodePrettily());
                            }
                        });

                        vertx.createHttpServer(new HttpServerOptions().setPort(port)).requestHandler(router).listen()
                                .onSuccess(server -> {
                                    log.info("Cache service started on port " + port);
                                    startServer.complete();
                                })
                                .onFailure(cause -> {
                                    log.error("Failed to create server on port {}", port, cause);
                                    startServer.fail(cause);
                                });
                    })
                    .onFailure(cause -> {
                        // Something went wrong during router factory initialization
                        log.error("Failed to start server on port {}", port, cause);
                        startServer.fail(cause);
                    });

        });
    }

    private void fetchHistoryList(DocumentScope documentScope, RoutingContext context) {

        MultiMap queryParams = context.queryParams();

        String resolution = queryParams.contains("resolution") ? queryParams.get("resolution") : "month";

        String startDate = queryParams.get("startDate");
        String endDate = queryParams.contains("endDate") ? queryParams.get("endDate") : dateFormat.format(new Date());

        if (!validDates(startDate, endDate, context)) {
            return;
        }

        List<String> filters = getFilterParam(queryParams);

        databaseProvider.getHistoryList(documentScope, filters, resolution, startDate, endDate, fetchHandler(context));
    }

    /**
     * This method validates the formatting of the dates and that the timing of the start and end dates are valid.
     * When returning from a non valid input, it also sets the server response
     *
     * @param startDate
     * @param endDate
     * @param context
     * @return
     */
    private boolean validDates(String startDate, String endDate, RoutingContext context) {
        try {
            if (dateFormat.parse(startDate).after(dateFormat.parse(endDate))) {
                context.response().setStatusCode(400).setStatusMessage("Bad Request - startDate after endDate").end();
                return false;
            }
        } catch (ParseException e) {
            context.response().setStatusCode(400).end(e.getMessage());
            return false;
        }
        return true;
    }

    private void fetchCurrentList(DocumentScope documentScope, RoutingContext context) {

        List<String> filters = getFilterParam(context.queryParams());
        databaseProvider.getDocumentList(documentScope, filters, fetchHandler(context));

    }

    private void fetchHistory(DocumentScope documentScope, RoutingContext context) {

        String id = "global";
        if (documentScope != DocumentScope.GLOBAL) {
            id = context.pathParam("id");
            if (id == null) {
                context.response().setStatusCode(500).end("Cannot read document ID");
                return;
            }
        }

        MultiMap queryParams = context.queryParams();


        String resolution = queryParams.contains("resolution") ? queryParams.get("resolution") : "month";

        String startDate = queryParams.get("startDate");
        String endDate = queryParams.contains("endDate") ? queryParams.get("endDate") : dateFormat.format(new Date());

        if (!validDates(startDate, endDate, context)) {
            return;
        }

        List<String> filters = getFilterParam(queryParams);

        databaseProvider.getHistory(documentScope, id, filters, resolution, startDate, endDate, fetchHandler(context));

    }


    private void fetchCurrent(DocumentScope documentScope, RoutingContext context) {


        String id = "global";
        if (documentScope != DocumentScope.GLOBAL) {
            id = context.pathParam("id");
            if (id == null) {
                context.response().setStatusCode(500).end("Cannot read document ID");
            }
        }


        List<String> filters = getFilterParam(context.queryParams());

        if (!filters.isEmpty()) {
            log.info("Filter:");
            filters.forEach(log::info);
        }

        databaseProvider.getDocument(documentScope, id, filters, ar -> {
            if (ar.succeeded()) {
                if (ar.result() == null) {
                    context.response().setStatusCode(404).end();
                } else {
                    context.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(ar.result().encodePrettily());
                }
            } else {
                context.response().setStatusCode(500).end(ar.cause().getMessage());
            }
        });

    }

    private List<String> getFilterParam(MultiMap queryParams) {
        List<String> filters = new ArrayList<>();

        if (queryParams.contains("filter")) {
            filters = queryParams.getAll("filter");
        }

        //This param should work with comma separated values but something is not working here?!
        //see https://swagger.io/docs/specification/serialization/#query
        //
        //style     explode     Primitive value id = 5      Array id = [3, 4, 5]
        //form *    true *      /users?id=5                 /users?id=3&id=4&id=5
        //form 	    false       /users?id=5 	            /users?id=3,4,5  <- THIS!

        //So we have to split it ourselves
        if (filters.size() == 1) filters = Arrays.asList(filters.get(0).split(","));

        return filters;
    }

    private Promise<JsonObject> fetchHandler(RoutingContext context) {
        Promise<JsonObject> p = Promise.promise();

        p.future().onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));

        p.future().onSuccess(result -> {
            if (result == null) {
                context.response().setStatusCode(404).end();
            } else {
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(result.encodePrettily());
            }
        });
        return p;
    }


    private void deleteCatalogueMetrics(RoutingContext context) {
        databaseProvider.deleteDocument(DocumentScope.CATALOGUE, context.pathParam("id"), ar -> {
            if (ar.succeeded()) {
                String result = ar.result();
                if (result.equals("success")) {
                    context.response().setStatusCode(200).end();
                } else if (result.equals("not found")) {
                    context.response().setStatusCode(404).end();
                } else {
                    context.response().setStatusCode(400).setStatusMessage(result).end();
                }
            } else {
                context.response().setStatusCode(500).end(ar.cause().getMessage());
            }
        });
    }

    private void migrateScore(RoutingContext context) {
        databaseProvider.moveScore();
        context.response().setStatusCode(202).end();
    }

    private void refreshMetrics(RoutingContext context) {
        databaseProvider.refreshMetrics();
        context.response().setStatusCode(202).end();
    }

    private void refreshSingleMetrics(RoutingContext context) {
        String id = context.pathParam("id");
        dqvProvider.listCatalogues(ar -> {
            if (ar.succeeded()) {
                List<String> catalogues = ar.result();
                if (catalogues.contains(id)) {
                    databaseProvider.refreshSingleMetrics(id);
                    context.response().setStatusCode(202).end();
                } else {
                    context.response().setStatusCode(404);
                }
            } else {
                log.error("Fetching catalogue infos", ar.cause());
                context.response().setStatusCode(500);
            }
        });
    }

    private void clearMetrics(RoutingContext context) {
        databaseProvider.clearMetrics();
        context.response().setStatusCode(202).end();
    }

    /**
     * getViolations method is operation connected to endpoint  /metrics/catalogues/<catalogue>/violation
     *
     * @param context the RoutingContext
     */
    private void getViolations(RoutingContext context) {
        String id = getID(context);

        MultiMap params = context.queryParams();
        String lang = "en";
        if (params.contains("locale")) {
            lang = params.get("locale");
        } else if (!context.acceptableLanguages().isEmpty()) {
            lang = context.acceptableLanguages().get(0).tag();
        }

        int offset = params.contains("offset") ? Integer.parseInt(params.get("offset")) : 0;
        int limit = params.contains("limit") ? Integer.parseInt(params.get("limit")) : 100;

        dqvProvider.getCatalogueViolations(id, offset, limit, lang, ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(ar.result().encodePrettily());
            } else {
                log.error("Failed to get violations", ar.cause());
                context.response().setStatusCode(500).end();
            }
        });
    }

    /**
     * get error status codes method get error status codes for all distributions of a dataset for a specific catalogue.
     * and put it JSON structure for the response:
     * {
     * "success": true,
     * "result": {
     * "count": 47,
     * "results": [
     * {
     *
     * @param context the routing context
     */
    private void getCatalogueDistributionReachability(RoutingContext context) {
        String id = getID(context);

        MultiMap params = context.queryParams();
        String lang = "en";
        if (params.contains("locale")) {
            lang = params.get("locale");
        } else if (!context.acceptableLanguages().isEmpty()) {
            lang = context.acceptableLanguages().get(0).tag();
        }

        int offset = params.contains("offset") ? Integer.parseInt(params.get("offset")) : 0;
        int limit = params.contains("limit") ? Integer.parseInt(params.get("limit")) : 100;

        databaseProvider.getDistributionReachabilityDetails(id, offset, limit, lang, ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(ar.result().encodePrettily());
            } else {
                log.error("Failed to get reachability issues", ar.cause());
                // differentiate: e.g. id not found -> 404
                context.response().setStatusCode(500).end();
            }
        });
    }

    private String getID(RoutingContext context) {
        return context.pathParam("id");
    }

    private void getSingleDatasetMetrics(RoutingContext context) {
        MultiMap params = context.queryParams();
        String lang = "en";
        if (params.contains("locale")) {
            lang = params.get("locale");
        } else if (!context.acceptableLanguages().isEmpty()) {
            lang = context.acceptableLanguages().get(0).tag();
        }

        String datasetId = getID(context);
        dqvProvider.getDatasetMetrics(datasetId, lang, ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(ar.result().encodePrettily());
            } else {
                context.response().setStatusCode(404).end();
            }
        });
    }

    private void getAllDistributionMetrics(RoutingContext context) {
        MultiMap params = context.queryParams();
        String lang = "en";
        if (params.contains("locale")) {
            lang = params.get("locale");
        } else if (!context.acceptableLanguages().isEmpty()) {
            lang = context.acceptableLanguages().get(0).tag();
        }

        String datasetId = getID(context);
        dqvProvider.getDistributionMetricsPerDataset(datasetId, lang, ar -> {
            if (ar.succeeded()) {
                context.response()
                        .setStatusCode(200)
                        .putHeader("Content-Type", "application/json")
                        .end(ar.result().encodePrettily());
            } else {
                context.response().setStatusCode(404).end();
            }
        });
    }

    private void scheduleRefresh(RoutingContext context) {

        RequestParameters params = context.get(ValidationHandler.REQUEST_CONTEXT_KEY);

        JsonObject trigger = params.body().getJsonObject();
        String id = UUID.randomUUID().toString();

        quartzService.putTrigger(QuartzService.ALL_CATALOGUES, id, trigger)
                .onSuccess(a -> {
                    int code = "created".equalsIgnoreCase(a) ? 201 : 200;
                    context.response().setStatusCode(code).end();
                })
                .onFailure(err -> {
                    err.printStackTrace();
                    context.response().setStatusCode(((ServiceException) err).failureCode()).end(err.getMessage());
                });


    }

    private void getSchedules(RoutingContext context) {
        quartzService.listTriggers().onFailure(err -> context.response().setStatusCode(((ServiceException) err).failureCode()).end(err.getMessage()))
                .onSuccess(t -> {
                    context.response()
                            .setStatusCode(200)
                            .putHeader("Content-Type", "application/json")
                            .end(t.encodePrettily());
                });
    }

}
