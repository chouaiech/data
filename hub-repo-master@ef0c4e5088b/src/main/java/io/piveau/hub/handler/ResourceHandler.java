/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.handler;

import io.piveau.HubRepo;
import io.piveau.hub.services.resources.ResourcesService;
import io.piveau.hub.util.ContentNegotiation;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class ResourceHandler {

    private final ResourcesService resourcesService;

    public ResourceHandler(Vertx vertx) {
        resourcesService = ResourcesService.createProxy(vertx, ResourcesService.SERVICE_ADDRESS);
    }

    public void listResourceTypes(RoutingContext context) {
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        resourcesService.listResourceTypes().onSuccess(result -> {
            context.response().putHeader("Content-Type", "application/json");
            context.response().setStatusCode(200).end(result.toString());
        }).onFailure(cause -> {
            context.response().putHeader("Content-Type", "text/plain");
            context.response().setStatusCode(400).end(cause.getMessage());
        });
    }

    public void listResources(RoutingContext context) {
        String type = context.request().getParam("type");
        context.response().putHeader("Access-Control-Allow-Origin", "*");
        resourcesService.listResources(type).onSuccess(result -> {
            context.response().putHeader("Content-Type", "application/json");
            context.response().setStatusCode(200).end(result.toString());
        }).onFailure(cause -> {
            context.response().putHeader("Content-Type", "text/plain");
            context.response().setStatusCode(400).end(cause.getMessage());
        });
    }

    public void getResource(RoutingContext context) {
        ContentNegotiation contentNegotiation = new ContentNegotiation(context);
        String id = contentNegotiation.getId();
        String type = context.request().getParam("type");
        String acceptType = contentNegotiation.getAcceptType();

        boolean headRequest = context.request().method() == HttpMethod.HEAD;

        context.response().putHeader("Access-Control-Allow-Origin", "*");
        context.response().putHeader("Content-Type", acceptType);
        resourcesService.getResource(id, type, acceptType)
                .onSuccess(contentNegotiation::headOrGetResponse)
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void deleteResource(RoutingContext context) {
        String id = context.request().getParam("id");
        String type = context.request().getParam("type");

        context.response().putHeader("Access-Control-Allow-Origin", "*");
        resourcesService.deleteResource(id, type)
                .onSuccess(v -> context.response().setStatusCode(204).end())
                .onFailure(cause -> HubRepo.failureResponse(context, cause));
    }

    public void postResource(RoutingContext context) {
        String id = UUID.randomUUID().toString();
        context.request().params().add("id", id);
        putResource(context);
    }

    public void putResource(RoutingContext context) {
        String id = context.request().getParam("id");
        String type = context.request().getParam("type");
        String content = context.body().asString();
        String contentType = context.parsedHeaders().contentType().value();

        context.response().putHeader("Access-Control-Allow-Origin", "*");
        resourcesService.putResource(id, type, content, contentType)
                .onSuccess(result -> {
                    switch (result) {
                        case "created" -> context.response().setStatusCode(201).end();
                        case "updated" -> context.response().setStatusCode(204).end();
                        default -> context.response().setStatusCode(400).end(result);
                        // should not happen, succeeded path should only respond with 2xx codes
                    }
                })
                .onFailure(cause -> HubRepo.failureResponse(context, cause));

    }
}
