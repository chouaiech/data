/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.util;

import io.piveau.hub.indexing.Indexing;
import io.piveau.rdf.Piveau;
import io.piveau.vocabularies.vocabulary.DCATAP;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexMappingLoader {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Vertx vertx;

    private final String defaultShape = "shapes/dcat-ap.hub.shapes.ttl";

    public IndexMappingLoader(Vertx vertx) {
        this.vertx = vertx;
    }
    public Future<Void> load() {
        Promise<Void> promise = Promise.promise();

        findShapes().onSuccess(buffer -> {
            Model catModel = Piveau.toModel(buffer.getBytes(), Lang.TURTLE);
            JsonObject catalogInstr = io.piveau.indexing.Indexing.produceIndexInstructionsFromResource(catModel.getResource(DCATAP.CatalogShape.getURI()));
            JsonObject datasetInstr = io.piveau.indexing.Indexing.produceIndexInstructionsFromResource(catModel.getResource(DCATAP.DatasetShape.getURI()));
            Indexing.init(catalogInstr, datasetInstr);
            promise.complete();
        }).onFailure(error -> {
            promise.fail(error.getMessage());
        });

        return promise.future();
    };

    private Future<Buffer> findShapes() {
        Promise<Buffer> promise = Promise.promise();
        vertx.fileSystem().readFile("conf/shapes/dcat-ap.hub.shapes.ttl")
                .onSuccess(file -> {
                    log.info("Loaded overwrite schema");
                    promise.complete(file);
                })
                .onFailure(ar -> vertx.fileSystem().readFile("shapes/dcat-ap.hub.shapes.ttl").onSuccess(promise::complete).onFailure(promise::fail));
        return promise.future();
    }

}

