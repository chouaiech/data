/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.resources.ResourcesService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class ResourceHandler extends ContextHandler {

    private final ResourcesService resourcesService;

    public ResourceHandler(Vertx vertx, String address) {
        resourcesService = ResourcesService.createProxy(vertx, address);
    }

    public void listResourceTypes(RoutingContext context) {
        resourcesService.listResourceTypes().onComplete(ar -> handleContextJsonArray(context, ar));
    }

    public void listResources(RoutingContext context) {
        String path = context.request().path();
        String type = path.split("/")[2];
        String alias = context.request().getParam("alias");
        resourcesService.listResources(type, alias).onComplete(ar -> handleContextJsonArray(context, ar));
    }

    public void createOrUpdateResource(RoutingContext context) {
        String id = context.request().getParam("id");
        String path = context.request().path();
        String type = path.split("/")[2];
        List<String> synchronous = context.queryParam("synchronous");
        if (synchronous.isEmpty() || synchronous.contains("true")) {
            resourcesService.createOrUpdateResource(id, type, context.body().asJsonObject()).onComplete(ar ->
                    handleContextNoContent(context, ar));
        } else {
            resourcesService.createOrUpdateResource(id, type, context.body().asJsonObject()).onComplete(ar -> {});
            context.response().setStatusCode(202).end();
        }
    }

    public void readResource(RoutingContext context) {
        String id = context.request().getParam("id");
        String path = context.request().path();
        String type = path.split("/")[2];
        resourcesService.readResource(id, type).onComplete(ar -> handleContextLegacy(context, ar));
    }

    public void deleteResource(RoutingContext context) {
        String id = context.request().getParam("id");
        String path = context.request().path();
        String type = path.split("/")[2];
        resourcesService.deleteResource(id, type).onComplete(ar -> handleContextVoid(context, ar));
    }
}
