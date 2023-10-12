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

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface ResourcesService {

    String SERVICE_ADDRESS = "io.piveau.hub.search.services.resources.queue";

    static Future<ResourcesService> create(Vertx vertx, JsonObject config, IndexManager indexManager) {
        return Future.future(promise -> new ResourcesServiceImpl(vertx, config, indexManager, promise));
    }

    static ResourcesService createProxy(Vertx vertx, String address) {
        return new ResourcesServiceVertxEBProxy(vertx, address, new DeliveryOptions().setSendTimeout(120000));
    }

    Future<JsonArray> listResourceTypes();
    Future<JsonArray> listResources(String type, String alias);
    Future<JsonObject> createOrUpdateResource(String id, String type, JsonObject payload);
    Future<JsonObject> readResource(String id, String type);
    Future<Void> deleteResource(String id, String type);

}
