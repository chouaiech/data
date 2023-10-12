/*
 * Copyright (c) 2023. European Commission
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package io.piveau.hub.indexing

import io.piveau.json.putIfNotEmpty
import io.piveau.rdf.asNormalized
import io.piveau.vocabularies.normalize
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.Resource
import org.apache.jena.vocabulary.DC_11
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.SKOS

fun indexConceptScheme(id: String, uriRef: String, model: Model): JsonObject {
    val index = JsonObject()
        .put("resource", uriRef)
        .put("id", id)
    val vocab = JsonArray()
    index.put("vocab", vocab)

    model.listSubjectsWithProperty(RDF.type, SKOS.Concept).forEachRemaining { concept ->
        val conceptIndex = indexConcept(concept)
        if (conceptIndex.containsKey("pref_label")) {
            if (!conceptIndex.containsKey("in_scheme")) {
                conceptIndex.put("in_scheme", uriRef)
            }
            vocab.add(conceptIndex)
        }
    }

    return index
}

fun indexConcept(concept: Resource): JsonObject {
    val index = JsonObject()
        .put("resource", concept.uri)

    concept.getProperty(DC_11.identifier)?.let {
        index.put("id", it.literal.lexicalForm)
    } ?: index.put("id", concept.uri.substringAfterLast("/", concept.uri.asNormalized()))

    concept.getPropertyResourceValue(SKOS.inScheme)?.let {
        index.put("in_scheme", it.uri)
    }

    // pref_label
    val prefLabels = JsonObject()
    concept.listProperties(SKOS.prefLabel).toList()
        .map { it.literal }
        .filter { it.language.length == 2 }
        .forEach { prefLabels.put(it.language, it.string) }
    index.putIfNotEmpty("pref_label", prefLabels)

    // alt_label
    val altLabels = JsonObject()
    concept.listProperties(SKOS.altLabel).toList()
        .map { it.literal }
        .filter { it.language.length == 2 }
        .forEach { prefLabels.put(it.language, it.string) }
    index.putIfNotEmpty("alt_label", altLabels)

    return index
}
