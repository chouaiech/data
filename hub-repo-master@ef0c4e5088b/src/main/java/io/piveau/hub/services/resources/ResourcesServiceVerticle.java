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
import io.piveau.hub.Constants;
import io.piveau.json.ConfigHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class ResourcesServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        JsonObject config = ConfigHelper.forConfig(config()).forceJsonObject(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG);
        TripleStore tripleStore = new TripleStore(vertx, config);
        ResourcesService.create(vertx, tripleStore, config(), serviceReady -> {
            if (serviceReady.succeeded()) {
                new ServiceBinder(vertx).setAddress(ResourcesService.SERVICE_ADDRESS)
                        .register(ResourcesService.class, serviceReady.result());
                startPromise.complete();
            } else {
                startPromise.fail(serviceReady.cause());
            }
        });
    }
}
