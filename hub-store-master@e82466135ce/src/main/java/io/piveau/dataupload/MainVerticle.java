package io.piveau.dataupload;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class MainVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private DBHandler dbHandler;
    private JsonObject config;

    public static void main(String[] args) {
        Launcher.executeCommand("run", MainVerticle.class.getName());
    }

    @Override
    public void start(Promise<Void> promise) {

        loadConfig()
                .compose(handler -> setupMongoDBConnection())
                .compose(handler -> startServer())
                .onSuccess(v -> {
                    log.info("Fileuploader successfully launched");
                    promise.complete();
                })
                .onFailure(promise::fail);
    }

    private Future<Void> loadConfig() {
        Promise<Void> promise = Promise.promise();

        ConfigRetriever.create(vertx).getConfig(handler -> {
            if (handler.succeeded()) {
                config = handler.result();
                promise.complete();
                log.debug("Successfully loaded configuration.");
            } else {
                promise.fail("Failed to load config: " + handler.cause());
            }
        });

        return promise.future();
    }

    private Future<Void> setupMongoDBConnection() {
        Promise<Void> promise = Promise.promise();
        JsonObject dbconfig = new JsonObject()
                .put("connection_string", config.getString(Constants.ENV_MONGO_DB_URI))
                .put("db_name", config.getString(Constants.ENV_MONGO_DB));
//                .put("connectTimeoutMS", 30000)
//                .put("socketTimeoutMS", 10000)
//                .put("keepAlive", true);

        log.info("db config is: " + dbconfig);

        MongoClient mongoClient = MongoClient.createShared(vertx, dbconfig);
        if (ObjectUtils.allNotNull(mongoClient)) {
            promise.complete();
            log.debug("Successfully started mongodb connection.");
        } else {
            promise.fail("No DB-Connection.");
            log.error("Failed starting mongodb connection:" + promise.future().result());
        }
        dbHandler = new DBHandler(mongoClient, config.getString(Constants.ENV_MONGO_DB));

        return promise.future();
    }

    private Future<Void> startServer() {
        Promise<Void> promise = Promise.promise();

        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("Authorization");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        String corsDomains = "^(https?:\\/\\/(?:.+\\.)?(?:fokus\\.fraunhofer\\.de|localhost|europeandataportal\\.eu)(?::\\d{1,5})?)$";

        OpenAPI3RouterFactory.create(vertx, "webroot/openapi.yml", ar -> {
            if (ar.succeeded()) {
                // Spec loaded with success
                OpenAPI3RouterFactory routerFactory = ar.result();
                routerFactory.addGlobalHandler(CorsHandler.create(corsDomains).allowedHeaders(allowedHeaders).allowedMethods(allowedMethods).allowCredentials(true));
                routerFactory.addSecurityHandler("ApiKeyAuth", this::checkApiKey);
                routerFactory.addHandlerByOperationId("singleFileUpload", this::handleSingleFileUpload);
                routerFactory.addHandlerByOperationId("prepareUpload", this::handlePrepareUpload);
                routerFactory.addHandlerByOperationId("deleteEntry", this::handleDeleteEntry);
                routerFactory.addHandlerByOperationId("getFile", this::handleGetFile);

                Router router = routerFactory.getRouter();
                router.route("/*").handler(StaticHandler.create());


                HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8080)); //config.getInteger(Constants.ENV_HTTP_PORT)));
                server.requestHandler(router).listen();
                promise.complete();

            } else {
                // Something went wrong during router factory initialization
                promise.fail(ar.cause());
                log.error("Error starting server: " + ar.cause());
            }
        });

        return promise.future();

    }

    public void checkApiKey(RoutingContext context) {
        String apiKey = config.getString(Constants.ENV_API_KEY);

        final String authorization = context.request().headers().get(HttpHeaders.AUTHORIZATION);

        if (apiKey.isEmpty()) {
            JsonObject response = new JsonObject();
            context.response().putHeader("Content-Type", "application/json");
            context.response().setStatusCode(500);
            response.put("success", false);
            response.put("message", "Api-Key is not specified");
        } else if (authorization == null) {
            JsonObject response = new JsonObject();
            context.response().putHeader("Content-Type", "application/json");
            context.response().setStatusCode(401);
            response.put("success", false);
            response.put("message", "Header field Authorization is missing");
            context.response().end(response.toString());
        } else if (!authorization.equals(apiKey)) {
            JsonObject response = new JsonObject();
            context.response().putHeader("Content-Type", "application/json");
            context.response().setStatusCode(401);
            response.put("success", false);
            response.put("message", "Incorrect Api-Key");
            context.response().end(response.toString());
        } else {
            context.next();
        }
    }

    private void handlePrepareUpload(RoutingContext context) {
        dbHandler.prepareEntry(context.getBodyAsJsonArray())
                .onSuccess(status -> context.response().setStatusCode(200).end())
                .onFailure(cause -> context.response().setStatusCode(500).end("Could not create entry: " + cause.getMessage()));
    }

    private void handleSingleFileUpload(RoutingContext context) {
        if (context.fileUploads().size() != 1) {
            HttpServerResponse response = context.response();
            response.setStatusCode(500);
            response.end("Only one file allowed in upload");
            return;
        }

        String fileName = null;
        byte[] bytes = null;
        for (FileUpload f : context.fileUploads()) {
            File uploadFile = new File(f.uploadedFileName());

            fileName = f.fileName();

            try {
                bytes = Files.readAllBytes(uploadFile.toPath());
            } catch (IOException e) {
                log.error("Read uploaded file", e);
            }
        }


        dbHandler.createEntry(bytes, fileName, context.pathParam("id"), context.queryParam("token").get(0))
                .onSuccess(status -> {
                    if (status.equals("success")) {
                        context.response().setStatusCode(200).end();
                    } else {
                        context.response().setStatusCode(500).end("Status: " + status);
                    }
                })
                .onFailure(cause -> context.response().setStatusCode(500).end("Status: " + cause.getMessage()));
    }

    private void handleDeleteEntry(RoutingContext context) {
        dbHandler.deleteEntryByFileID(context.pathParam("id"))
                .onSuccess(status -> context.response().setStatusCode(200).end())
                .onFailure(cause -> context.response().end(cause.getMessage()));
    }

    private void handleGetFile(RoutingContext context) {
        HttpServerResponse response = context.response();

        dbHandler.getFile(context.request().getParam("id"), res -> {
            if (ObjectUtils.allNotNull(res)) {
                response.putHeader("Content-Disposition", "attachment; filename=\"" + res.getName() + "\"");
                response.sendFile(res.getAbsolutePath());
                response.setStatusCode(200);
                res.delete();
            } else {
                response.setStatusCode(500);
                response.end("File not found with fileID: " + context.request().getParam("id"));
            }
        });

    }

}
