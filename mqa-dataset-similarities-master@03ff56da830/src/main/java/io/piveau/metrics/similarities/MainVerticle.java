package io.piveau.metrics.similarities;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.cron.CronSchedule;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.json.ConfigHelper;
import io.piveau.metrics.similarities.model.SimilarityRequest;
import io.piveau.utils.ConfigurableAssetHandler;
import io.piveau.utils.CorsHelper;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static io.piveau.metrics.similarities.FingerprintVerticle.ADDRESS_START_FINGERPRINT;
import static io.piveau.metrics.similarities.SimilarityVerticle.ADDRESS_GET_SIMILARITY;

public class MainVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private JsonObject config;

    @Override
    public void start(Promise<Void> startPromise) {
        log.info("Launching Dataset Similarity Service...");

        Scheduler scheduler = new Scheduler();

        // startup is only successful if no step failed
        loadConfig()
                .compose(v -> bootstrapVerticles())
//                .compose(v -> scheduleDatasetFingerprinting())
                .compose(v -> startServer())
                .onComplete(handler -> {
                    if (handler.succeeded()) {
                        String cron = config.getString("PIVEAU_SIMILARITY_CRONDEF", " 0 0 0 1/1 * ? * ");
                        scheduler.schedule(
                                "fingerprinting",
                                () -> vertx.eventBus().send(ADDRESS_START_FINGERPRINT, ""),
                                CronSchedule.parseQuartzCron(cron));
                        log.info("Dataset Similarity Service successfully launched");
                        startPromise.complete();
                    } else {
                        log.error("Failed to launch dataset similarity service", handler.cause());
                        startPromise.fail(handler.cause());
                    }
                });
    }

    private Future<Void> loadConfig() {
        return Future.future(loadConfig -> {
            ConfigRetriever configRetriever = ConfigRetriever.create(vertx);

            configRetriever.getConfig()
                    .onSuccess(c -> {
                        this.config = c;
                        log.debug(config.encodePrettily());

                        DCATAPUriSchema.setConfig(config.getJsonObject("PIVEAU_DCATAP_SCHEMA_CONFIG", new JsonObject()));
                        loadConfig.complete();
                    })
                    .onFailure(cause -> loadConfig.fail("Failed to load config: " + cause.getMessage()));
        });
    }

    private CompositeFuture bootstrapVerticles() {
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(config)
                .setWorkerPoolName("extractor-pool")
                .setMaxWorkerExecuteTime(30)
                .setMaxWorkerExecuteTimeUnit(TimeUnit.MINUTES)
                .setWorker(true);

        return CompositeFuture.join(
                vertx.deployVerticle(SimilarityVerticle.class, options),
                vertx.deployVerticle(FingerprintVerticle.class, options.setWorker(false))
        );
    }

    private Future<Void> startServer() {
        return Future.future(startServer -> {
            Integer port = config.getInteger(ApplicationConfig.ENV_APPLICATION_PORT, ApplicationConfig.DEFAULT_APPLICATION_PORT);

            RouterBuilder.create(vertx, "webroot/openapi.yaml").onSuccess(routerBuilder -> {

                RouterBuilderOptions options = new RouterBuilderOptions().setMountNotImplementedHandler(true);
                routerBuilder.setOptions(options);

                if (config.containsKey(ApplicationConfig.ENV_CORS_DOMAINS)) {

                    JsonArray corsDomains = ConfigHelper.forConfig(config)
                            .forceJsonArray(ApplicationConfig.ENV_CORS_DOMAINS);

                    Set<String> allowedHeaders = Set.of(
                            "x-requested-with",
                            "Access-Control-Allow-Origin",
                            "origin",
                            "Content-Type",
                            "accept");

                    Set<HttpMethod> allowedMethods = Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS);

                    new CorsHelper(corsDomains, allowedHeaders, allowedMethods).addRootHandler(routerBuilder);
                }

                routerBuilder.operation("similaritiesForDataset")
                        .handler(this::handleSimilarityRequest);

                routerBuilder.operation("startFingerprinting")
                        .handler(context -> {
                            vertx.eventBus().send(ADDRESS_START_FINGERPRINT, "");
                            context.response().setStatusCode(202).end();
                        });

                Router router = routerBuilder.createRouter();
                WebClient webClient = WebClient.create(vertx);

                router.route("/images/favicon")
                        .handler(new ConfigurableAssetHandler(config.getString("PIVEAU_FAVICON_PATH", "webroot/images/favicon.png"), webClient));
                router.route("/images/logo")
                        .handler(new ConfigurableAssetHandler(config.getString("PIVEAU_LOGO_PATH", "webroot/images/logo.png"), webClient));
                router.route("/*")
                        .handler(StaticHandler.create());

                HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(port));
                server.requestHandler(router).listen().onSuccess(s -> {
                    log.info("Server successfully launched on port [{}]", port);
                    startServer.complete();
                }).onFailure(startServer::fail);

            }).onFailure(startServer::fail);
        });
    }

    private void handleSimilarityRequest(RoutingContext context) {
        String datasetId = context.pathParam("datasetId");

        int limit = context.queryParams().contains("limit")
                ? Integer.parseInt(context.queryParams().get("limit"))
                : config.getInteger(ApplicationConfig.ENV_DEFAULT_RESULT_SIZE, ApplicationConfig.DEFAULT_RESULT_SIZE);

        SimilarityRequest request =
                new SimilarityRequest(datasetId, limit);

        vertx.eventBus().request(ADDRESS_GET_SIMILARITY, Json.encode(request))
                .onSuccess(message ->
                        context.response()
                                .setStatusCode(200)
                                .end((String) message.body()))
                .onFailure(cause ->
                        context.response().setStatusCode(500).end());
    }

    public static void main(String[] args) {
        String[] params = Arrays.copyOf(args, args.length + 1);
        params[params.length - 1] = MainVerticle.class.getName();
        Launcher.executeCommand("run", params);
    }

}
