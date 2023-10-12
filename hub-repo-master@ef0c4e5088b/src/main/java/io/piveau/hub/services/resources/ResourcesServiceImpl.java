/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.services.resources;

import io.piveau.dcatap.Prefixes;
import io.piveau.dcatap.TripleStore;
import io.piveau.hub.Constants;
import io.piveau.hub.Defaults;
import io.piveau.hub.services.index.IndexService;
import io.piveau.indexing.Indexing;
import io.piveau.rdf.Piveau;
import io.piveau.utils.JenaUtils;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcesServiceImpl implements ResourcesService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PiveauContext serviceContext;
    private final TripleStore tripleStore;

    private final boolean prependXmlDeclaration;

    private final IndexService indexService;

    private final String baseUri;

    private JsonObject shapes;

    ResourcesServiceImpl(Vertx vertx, TripleStore tripleStore, JsonObject config,
                                Handler<AsyncResult<ResourcesService>> handler) {
        this.tripleStore = tripleStore;

        serviceContext = new PiveauContext("hub-repo", "ResourcesService");

        prependXmlDeclaration = config.getBoolean(Constants.ENV_PIVEAU_HUB_XML_DECLARATION, Defaults.XML_DECLARATION);

        JsonObject indexConfig = config.getJsonObject(Constants.ENV_PIVEAU_HUB_SEARCH_SERVICE, new JsonObject());
        if (indexConfig.getBoolean("enabled", Defaults.SEARCH_SERVICE_ENABLED)) {
            indexService = IndexService.createProxy(vertx, IndexService.SERVICE_ADDRESS, IndexService.DEFAULT_TIMEOUT);
        } else {
            indexService = null;
        }

        JsonObject schemaConfig = config.getJsonObject(Constants.ENV_PIVEAU_DCATAP_SCHEMA_CONFIG, new JsonObject());
        baseUri = schemaConfig.getString("baseUri", "https://piveau.io/") +
                schemaConfig.getString("resource", "set/resource/");

        boolean shapesExists = vertx.fileSystem().existsBlocking("conf/shapes/index.json");
        if (shapesExists) {
            Buffer buffer = vertx.fileSystem().readFileBlocking("conf/shapes/index.json");
            try {
                shapes = buffer.toJsonObject();
            } catch (DecodeException e) {
                shapes = new JsonObject();
            }

            for (String key : shapes.getMap().keySet()) {
                JsonObject shape = shapes.getJsonObject(key);

                String path = shape.getString("path");
                String shapeUri = shape.getString("shapeUri");

                boolean pathExists = vertx.fileSystem().existsBlocking(path);
                if (pathExists) {
                    JsonObject indexInstructions = Indexing.produceIndexInstructionsFromShacl(path, Lang.TURTLE, shapeUri);
                    shape.put("indexInstructions", indexInstructions);
                }
            }
        } else {
            shapes = new JsonObject();
        }

        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<JsonArray> listResourceTypes() {
        Promise<JsonArray> promise = Promise.promise();
        JsonArray jsonResult = new JsonArray();
        for (String key : shapes.getMap().keySet()) {
            jsonResult.add(key);
        }
        promise.complete(jsonResult);
        return promise.future();
    }

    @Override
    public Future<JsonArray> listResources(String type) {
        Promise<JsonArray> promise = Promise.promise();

        if (shapes.containsKey(type)) {
            String typeUri = shapes.getJsonObject(type).getString("typeUri");

            tripleStore.select("SELECT ?resource WHERE { ?resource a <" + typeUri + "> }")
                    .onSuccess(result -> {
                        JsonArray jsonResult = new JsonArray();
                        while (result.hasNext()) {
                            QuerySolution qs = result.next();
                            jsonResult.add(qs.getResource("resource").getURI());
                        }
                        promise.complete(jsonResult);
                    })
                    .onFailure(promise::fail);
        } else {
            promise.fail(new ServiceException(404, "Resource type unknown"));
        }

        return promise.future();
    }

    @Override
    public Future<String> getResource(String id, String type, String acceptType) {
        Promise<String> promise = Promise.promise();

        String graphName = baseUri + type + "/" + id;

        tripleStore.construct("CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + graphName + "> { ?s ?p ?o } }")
                .onSuccess(model -> {
                    if (model.isEmpty()) {
                        promise.fail(new ServiceException(404, "Resource not found"));
                    } else {
                        Prefixes.setNsPrefixesFiltered(model);
                        promise.complete(JenaUtils.write(model, acceptType, prependXmlDeclaration));
                    }
                }).onFailure(promise::fail);

        return promise.future();
    }

    @Override
    public Future<Void> deleteResource(String id, String type) {
        Promise<Void> promise = Promise.promise();

        String graphName = baseUri + type + "/" + id;

        tripleStore.deleteGraph(graphName)
                .onSuccess(v -> {
                    indexService.deleteResource(id, type, deleteResourceResult -> {
                        if (deleteResourceResult.succeeded()) {
                            System.out.println("Successfully deleted resource");
                        } else {
                            System.out.println(deleteResourceResult.cause().getMessage());
                        }
                    });
                    promise.complete();
                })
                .onFailure(cause -> {
                    if (cause.getMessage().contains("Not Found")) {
                        promise.fail(new ServiceException(404, "Resource not found"));
                    } else {
                        promise.fail(cause);
                    }
                });

        return promise.future();
    }

    @Override
    public Future<String> putResource(String id, String type, String content, String contentType) {
        Promise<String> promise = Promise.promise();

        if (shapes.containsKey(type)) {
            JsonObject shape = shapes.getJsonObject(type);
            String typeUri = shape.getString("typeUri");

            Model model = JenaUtils.read(content.getBytes(), contentType);

            Property typeProperty = ModelFactory.createDefaultModel().createProperty(typeUri);

            String graphName = baseUri + type + "/" + id;

            ResIterator resIterator = model.listSubjectsWithProperty(RDF.type, typeProperty);
            if (resIterator.hasNext()) {
                resIterator.forEachRemaining(rs -> Piveau.rename(rs, graphName));
                tripleStore.setGraph(graphName, model, true)
                        .onSuccess(setGraphResult -> {
                            Indexing.indexingResource(
                                    model.getResource(graphName),                       // dataset resource
                                    null,                                               // catalog record resource
                                    shape.getJsonObject("indexInstructions"),           // instructions build from shacl
                                    "en",                                               // default language
                                    "other",                                            // schema
                                    false,                                              // generate modified and issued (now)
                                    null,                                               // catalog id
                                    false                                               // set country from spatial
                            ).onSuccess(indexingResult -> {
                                indexService.putResource(id, type, indexingResult, putResourceResult -> {});
                            }).onFailure(ar -> {
                                //
                            });
                            promise.complete(setGraphResult);
                        })
                        .onFailure(promise::fail);
            } else {
                promise.fail(new ServiceException(400, "Resource type not found in payload"));
            }
        } else {
            promise.fail(new ServiceException(404, "Resource type unknown"));
        }

        return promise.future();
    }
}
