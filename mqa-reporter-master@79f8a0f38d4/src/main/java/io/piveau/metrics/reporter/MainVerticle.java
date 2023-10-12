package io.piveau.metrics.reporter;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import io.piveau.json.ConfigHelper;
import io.piveau.metrics.reporter.model.ReportFormat;
import io.piveau.metrics.reporter.model.ReportTask;
import io.piveau.metrics.reporter.pdf.PdfReportVerticle;
import io.piveau.metrics.reporter.tabular.TabularReportVerticle;
import io.piveau.security.ApiKeyAuthProvider;
import io.piveau.utils.ConfigurableAssetHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.handler.APIKeyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static io.piveau.metrics.reporter.ApplicationConfig.*;


public class MainVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

    private JsonObject reporterConfig;

    private WebClient webClient;
    private Scheduler scheduler;

    private String metricsAddress;
    private String quickChartAddress;

    @Override
    public void start(Promise<Void> startPromise) {
        webClient = WebClient.create(vertx);

        loadConfig()
                .compose(config -> bootstrapVerticles())
                .compose(c -> createReportDirs())
                .compose(v -> startServer())
                .onSuccess(v -> {
                    scheduleReportGeneration();
                    startPromise.complete();
                })
                .onFailure(failure -> {
                    log.error("Failed to launch Metrics Reporter", failure);
                    startPromise.fail(failure);
                });
    }

    private Future<JsonObject> loadConfig() {
        return Future.future(loadConfig ->
                ConfigRetriever.create(vertx).getConfig(handler -> {
                    if (handler.succeeded()) {
                        reporterConfig = handler.result();
                        log.debug(reporterConfig.encodePrettily());

                        metricsAddress = reporterConfig.getString(ENV_METRICS_CATALOGUES_ADDRESS, DEFAULT_METRICS_CATALOGUES_ADDRESS);
                        quickChartAddress = reporterConfig.getString(ENV_QUICKCHART_ADDRESS, "");

                        loadConfig.complete(handler.result());
                    } else {
                        loadConfig.fail("Failed to load config: " + handler.cause());
                    }
                }));
    }

    private void scheduleReportGeneration() {
        scheduler = new Scheduler();
        reporterConfig.getJsonArray(ENV_SCHEDULES, new JsonArray())
                .stream().map(JsonObject.class::cast).forEach(scheduleConfig -> {
                            List<String> languageCodes = scheduleConfig.getJsonArray("languages", new JsonArray())
                                    .stream().map(String.class::cast).collect(Collectors.toUnmodifiableList());

                            String cron = scheduleConfig.getString("cron");

                            scheduler.schedule(
                                    "report_" + StringUtils.join(languageCodes, "_"),
                                    () -> generateReports(languageCodes),
                                    CronSchedule.parseQuartzCron(cron)
                            );
                        }
                );
    }

    private void generateReports(List<String> languages) {
        getCatalogueMetrics().onSuccess(cataloguesMetrics -> {
            if (cataloguesMetrics.getBoolean("success")) {

                List<JsonObject> catalogues = cataloguesMetrics.getJsonObject("result", new JsonObject())
                        .getJsonArray("results", new JsonArray())
                        .stream().map(JsonObject.class::cast)
                        .collect(Collectors.toList());

                languages.forEach(languageCode -> {
                    ReportTask task = new ReportTask(new Locale(languageCode), catalogues);
                    vertx.eventBus().send(TabularReportVerticle.ADDRESS, Json.encode(task));

                    if (!quickChartAddress.isBlank()) {
                        vertx.eventBus().send(PdfReportVerticle.ADDRESS, Json.encode(task));
                    }
                });
            } else {
                log.error("Failed to fetch metrics");
            }
        }).onFailure(failed -> log.error("Failed to fetch metrics", failed));
    }

    private Future<JsonObject> getCatalogueMetrics() {
        return Future.future(promise ->
                webClient.getAbs(metricsAddress + "/catalogues")
                        .expect(ResponsePredicate.SC_OK)
                        .send()
                        .onSuccess(response -> promise.complete(response.bodyAsJsonObject()))
                        .onFailure(promise::fail)
        );
    }

    private CompositeFuture createReportDirs() {
        return CompositeFuture.all(AVAILABLE_LANGUAGES.stream()
                .map(language ->
                        Future.<Void>future(promise -> vertx.fileSystem().mkdirs(WORK_DIR + "/" + language, promise)))
                .collect(Collectors.toList()));
    }

    private Future<Void> startServer() {
        return Future.future(startServer -> {
            Integer port = reporterConfig.getInteger(ENV_APPLICATION_PORT, DEFAULT_APPLICATION_PORT);

            RouterBuilder.create(vertx, "webroot/openapi.yaml").onSuccess(routerBuilder -> {

                RouterBuilderOptions options = new RouterBuilderOptions().setMountNotImplementedHandler(true);
                routerBuilder.setOptions(options);

                JsonArray corsDomains = ConfigHelper.forConfig(reporterConfig).forceJsonArray(ENV_CORS_DOMAINS);

                if (!corsDomains.isEmpty()) {
                    Set<String> allowedHeaders = new HashSet<>();
                    allowedHeaders.add("x-requested-with");
                    allowedHeaders.add("Access-Control-Allow-Origin");
                    allowedHeaders.add("origin");
                    allowedHeaders.add("Content-Type");
                    allowedHeaders.add("accept");
                    allowedHeaders.add("Authorization");
                    allowedHeaders.add("Access-Control-Allow-Headers");

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
                    routerBuilder.rootHandler(CorsHandler.create(corsString).allowedHeaders(allowedHeaders).allowedMethods(allowedMethods).allowCredentials(true));
                } else {
                    log.info("no CORS domains configured");
                }

                String apiKey = reporterConfig.getString(ApplicationConfig.ENV_API_KEY, "apiKey");
                routerBuilder.securityHandler("ApiKeyAuth", APIKeyHandler.create(new ApiKeyAuthProvider(apiKey)).header("Authorization"));

                routerBuilder.operation("generate").handler(routingContext -> {
                    generateReports(routingContext.queryParam("lang"));
                    routingContext.response().setStatusCode(202).end();
                });

                routerBuilder.operation("getCatalogueReport").handler(this::getCatalogueReport);

                routerBuilder.operation("getReport").handler(routingContext -> {

                    String languageCode = routingContext.pathParam("languageCode");
                    ReportFormat format = ReportFormat.valueOf(routingContext.pathParam("format").toUpperCase());

                    String catalogueId = routingContext.queryParam("catalogueId").stream()
                            .findFirst()
                            .orElse("global");

                    String fileName = WORK_DIR + "/" + languageCode + "/" + catalogueId + "." + format.name().toLowerCase();

                    vertx.fileSystem().exists(fileName, fileExists -> {
                        if (fileExists.succeeded() && fileExists.result()) {
                            routingContext.response()
                                    .putHeader("Content-Type", format.getContentType())
                                    .putHeader("Content-Disposition", "attachment; filename=\"MQA-Metrics-Report." + format.name().toLowerCase() + "\"")
                                    .sendFile(fileName);
                        } else {
                            log.debug("File [{}] does not exist", fileName, fileExists.cause());
                            routingContext.response().setStatusCode(404).end();
                        }
                    });
                });

                Router router = routerBuilder.createRouter();
                WebClient client = WebClient.create(vertx);

                router.route("/images/favicon").handler(new ConfigurableAssetHandler(reporterConfig.getString("PIVEAU_FAVICON_PATH", "webroot/images/favicon.png"), client));
                router.route("/images/logo").handler(new ConfigurableAssetHandler(reporterConfig.getString("PIVEAU_LOGO_PATH", "webroot/images/logo.png"), client));

                router.route("/imprint").handler(context ->
                        context.redirect(reporterConfig.getString("PIVEAU_IMPRINT_URL", "/")));

                router.route("/privacy").handler(context ->
                        context.redirect(reporterConfig.getString("PIVEAU_PRIVACY_URL", "/")));

                router.route().handler(StaticHandler.create());

                HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(port));
                server.requestHandler(router).listen();

                log.info("Server successfully launched on port [{}]", port);
                startServer.complete();
            }).onFailure(startServer::fail);
        });
    }

    private CompositeFuture bootstrapVerticles() {
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(reporterConfig)
                .setWorker(true);

        return CompositeFuture.join(
                vertx.deployVerticle(PdfReportVerticle.class, options),
                vertx.deployVerticle(TabularReportVerticle.class, options)
        );
    }

    private void getCatalogueReport(RoutingContext context) {
        String catalogueId = context.pathParam("catalogueId");
        String languageCode = context.pathParam("languageCode");

        String accept = context.getAcceptableContentType();

        long start = System.currentTimeMillis();

        switch (accept) {
            case "application/pdf":
                vertx.eventBus().<Buffer>request(PdfReportVerticle.CATALOGUE_ADDRESS, new JsonObject()
                                .put("catalogueId", catalogueId)
                                .put("languageCode", languageCode))
                        .onSuccess(reportMessage -> {
                            log.info("Report generated in {} milliseconds", System.currentTimeMillis() - start);
                            context.response()
                                    .putHeader("Content-Type", accept)
                                    .putHeader("Content-Disposition", "attachment; filename=\"MQA-Metrics-Report.pdf\"")
                                    .send(reportMessage.body());
                        })
                        .onFailure(cause -> context.response().setStatusCode(500).end(cause.getMessage()));
                break;
            case "application/vnd.oasis.opendocument.spreadsheet":
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                break;
            default:
                context.response().setStatusCode(400).end();
        }
    }

    static {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }
    public static void main(String[] args) {
        String[] params = Arrays.copyOf(args, args.length + 1);
        params[params.length - 1] = MainVerticle.class.getName();
        Launcher.executeCommand("run", params);
    }

}
