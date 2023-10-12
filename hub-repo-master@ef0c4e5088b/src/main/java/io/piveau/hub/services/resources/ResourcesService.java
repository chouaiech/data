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

import io.piveau.dcatap.TripleStore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface ResourcesService {
    String SERVICE_ADDRESS = "io.piveau.hub.resources.queue";

    static ResourcesService create(Vertx vertx, TripleStore tripleStore, JsonObject config,
                                   Handler<AsyncResult<ResourcesService>> readyHandler) {
        return new ResourcesServiceImpl(vertx, tripleStore, config, readyHandler);
    }

    static ResourcesService createProxy(Vertx vertx, String address) {
        DeliveryOptions options = new DeliveryOptions().setSendTimeout(120000);
        return new ResourcesServiceVertxEBProxy(vertx, address, options);
    }

    Future<JsonArray> listResourceTypes();

    Future<JsonArray> listResources(String type);

    Future<String> getResource(String id, String type, String acceptType);

    Future<Void> deleteResource(String id, String type);

    Future<String> putResource(String id, String type, String content, String contentType);
}
