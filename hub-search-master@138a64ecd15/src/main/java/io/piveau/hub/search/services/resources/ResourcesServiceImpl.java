/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.search.services.resources;

import io.piveau.hub.search.Constants;
import io.piveau.hub.search.util.index.IndexManager;
import io.piveau.hub.search.util.request.Query;
import io.piveau.hub.search.util.response.ReturnHelper;
import io.piveau.hub.search.util.search.SearchClient;
import io.piveau.utils.PiveauContext;
import io.vertx.core.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;

public class ResourcesServiceImpl implements ResourcesService {

    private final SearchClient searchClient;

    private final PiveauContext serviceContext;

    ResourcesServiceImpl(Vertx vertx, JsonObject config, IndexManager indexManager,
                         Handler<AsyncResult<ResourcesService>> handler) {
        this.searchClient = SearchClient.build(vertx, config, indexManager);

        this.serviceContext = new PiveauContext("hub.search", "ResourcesService");
        handler.handle(Future.succeededFuture(this));
    }

    @Override
    public Future<JsonArray> listResourceTypes() {
        Promise<JsonArray> promise = Promise.promise();
        searchClient.getIndices("resource_*").onComplete(getIndicesResult -> {
            if (getIndicesResult.succeeded()) {
                JsonArray resourceTypes = new JsonArray();
                for(Object o : getIndicesResult.result()) {
                    String resourceType = (String) o;
                    resourceType = resourceType.replaceFirst("resource_", "");
                    int i = resourceType.lastIndexOf("_");
                    if (i > 0) {
                        resourceType = resourceType.substring(0, i);
                    }
                    resourceTypes.add(resourceType);
                }
                promise.complete(resourceTypes);
            } else {
                promise.fail(getIndicesResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonArray> listResources(String type, String alias) {
        Promise<JsonArray> promise = Promise.promise();
        String index = "resource_" + type;
        String defaultAlias = alias == null ? Constants.getReadAlias(index) : index + "_" + alias;
        searchClient.indexExists(defaultAlias).onComplete(indexExistsResult -> {
            if (indexExistsResult.succeeded()) {
                if (indexExistsResult.result()) {
                    JsonObject q = new JsonObject();
                    q.put("size", 10000);
                    q.put("filter", "resource");
                    q.put("aggregation", false);
                    q.put("includes", new JsonArray().add("id"));
                    q.put("scroll", true);
                    q.put("alias", defaultAlias);

                    Query query = Json.decodeValue(q.toString(), Query.class);

                    searchClient.listIds(query, false, true).onSuccess(promise::complete).onFailure(promise::fail);
                } else {
                    promise.fail(new ServiceException(500, "Resource type not found"));
                }
            } else {
                promise.fail(indexExistsResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> createOrUpdateResource(String resourceId, String type, JsonObject payload) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(resourceId);
        searchClient.indexExists(Constants.getWriteAlias("resource_" + type)).onComplete(indexExistsResult -> {
            if (indexExistsResult.succeeded()) {
                if (indexExistsResult.result()) {
                    searchClient.putDocument(type, resourceId, false, payload).onSuccess(result -> {
                        resourceContext.log().debug("Put success: " + payload);
                        if (result == 200) {
                            // updated
                            resourceContext.log().info("Update resource: Resource {} updated.", resourceId);
                        } else {
                            // created
                            resourceContext.log().info("Create resource: Resource {} created.", resourceId);
                        }
                        promise.complete(ReturnHelper.returnSuccess(result, new JsonObject().put("id", resourceId)));
                    }).onFailure(failure -> {
                        resourceContext.log().error("Put failed: " + failure.getMessage());
                        promise.fail(failure);
                    });
                } else {
                    resourceContext.log().error("Read failed: Resource type not found");
                    promise.fail(new ServiceException(500, "Resource type not found"));
                }
            } else {
                resourceContext.log().error("Put failed: " + indexExistsResult.cause().getMessage());
                promise.fail(indexExistsResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<JsonObject> readResource(String resourceId, String type) {
        Promise<JsonObject> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(resourceId);
        searchClient.indexExists(Constants.getReadAlias("resource_" + type)).onComplete(indexExistsResult -> {
            if (indexExistsResult.succeeded()) {
                if (indexExistsResult.result()) {
                    searchClient.getDocument("resource_" + type, false, resourceId, false)
                            .onSuccess(result -> promise.complete(ReturnHelper.returnSuccess(200, result)))
                            .onFailure(promise::fail);
                } else {
                    resourceContext.log().error("Read failed: Resource type not found");
                    promise.fail(new ServiceException(500, "Resource type not found"));
                }
            } else {
                resourceContext.log().error("Read failed: " + indexExistsResult.cause().getMessage());
                promise.fail(indexExistsResult.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<Void> deleteResource(String resourceId, String type) {
        Promise<Void> promise = Promise.promise();
        PiveauContext resourceContext = serviceContext.extend(resourceId);
        searchClient.indexExists(Constants.getReadAlias("resource_" + type)).onComplete(indexExistsResult -> {
            if (indexExistsResult.succeeded()) {
                if (indexExistsResult.result()) {
                    searchClient.deleteDocument("resource_" + type, resourceId, false)
                            .onSuccess(promise::complete)
                            .onFailure(promise::fail);
                } else {
                    resourceContext.log().error("Read failed: Resource type not found");
                    promise.fail(new ServiceException(500, "Resource type not found"));
                }
            } else {
                resourceContext.log().error("Read failed: " + indexExistsResult.cause().getMessage());
                promise.fail(indexExistsResult.cause());
            }
        });
        return promise.future();
    }

}
