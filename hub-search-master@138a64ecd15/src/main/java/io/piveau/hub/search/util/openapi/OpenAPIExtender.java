/*
 * Copyright (c) Fraunhofer FOKUS
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package io.piveau.hub.search.util.openapi;

import io.piveau.hub.search.util.index.IndexManager;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OpenAPIExtender {

    public static void extend(Vertx vertx, IndexManager indexManager, String originPath, String targetPath) {
        Map<String, JsonObject> openAPIs = indexManager.getOpenAPIs();

        if (openAPIs.keySet().isEmpty()) return;

        String input = vertx.fileSystem().readFileBlocking(originPath).toString();

        Yaml yaml = new Yaml();
        Map<String, Object> openAPI = yaml.load(input);

        JsonObject openAPIJson = convertToJsonObject(openAPI);

        extend(openAPIJson, openAPIs);

        String yamlString = yaml.dump(convertToMap(openAPIJson));
        vertx.fileSystem().writeFileBlocking(targetPath, Buffer.buffer(yamlString));
    }

    private static void extend(JsonObject input, Map<String, JsonObject> openAPIs) {
        for (String index : openAPIs.keySet()) {
            if (index.equals("dataset")) {
                extendForSchema(input, "Dataset", openAPIs.get(index));
            } else if (index.equals("catalogue")) {
                extendForSchema(input, "Catalogue", openAPIs.get(index));
            } else if (index.equals("dataservice")) {
                extendForSchema(input, "DataService", openAPIs.get(index));
            } else if (index.startsWith("vocabulary_")) {
                extendForSchema(input, "Vocable", openAPIs.get(index));
            } else if (index.startsWith("resource_")) {
                extendForResource(input, index, openAPIs);
            }
        }
    }

    private static void extendForSchema(JsonObject input, String schemaName, JsonObject openapi) {
        JsonObject schemas = input.getJsonObject("components", new JsonObject())
                .getJsonObject("schemas", new JsonObject());

        if (schemas.isEmpty()) return;

        schemas.put(schemaName, openapi);
    }

    private static void extendForResource(JsonObject input, String index, Map<String, JsonObject> openAPIs) {
        String resourceType = getResourceType(index);
        String resourceName = toCamelCase(resourceType);

        JsonObject schemas = input.getJsonObject("components", new JsonObject())
                .getJsonObject("schemas", new JsonObject());
        if (schemas.isEmpty()) return;

        schemas.put(resourceName, openAPIs.get(index).put("additionalProperties", false));

        JsonObject paths = input.getJsonObject("paths");
        if (paths.isEmpty()) return;

        JsonArray xTagsGroups = input.getJsonArray("x-tagGroups");
        if (xTagsGroups.isEmpty()) return;

        JsonObject xTagGroup = xTagsGroups.getJsonObject(0);
        if (!xTagGroup.getString("name").equals("Resources")) {
            xTagsGroups.add(0, new JsonObject().put("name", "Resources").put("tags", new JsonArray()));
            xTagGroup = xTagsGroups.getJsonObject(0);

            xTagGroup.getJsonArray("tags").add("Resources");
            paths.put("/resources", generateListResourceTypes());
        }

        xTagGroup.getJsonArray("tags").add(resourceName);
        paths.put("/resources/" + resourceType, generateListResources(resourceName));
        paths.put("/resources/" + resourceType + "/{id}", generateCrudResources(resourceName));
    }

    private static JsonObject generateListResourceTypes() {
        JsonObject result = new JsonObject();

        JsonObject responses = generateResponses();
        responses.put("200", generateResponseArrayOfResourceTypes());

        JsonObject listResourceTypes = new JsonObject();
        listResourceTypes.put("description", "You can get a list of resource types.");
        listResourceTypes.put("summary", "List resource types");
        listResourceTypes.put("operationId", "listResourceTypes");
        listResourceTypes.put("tags", new JsonArray().add("Resources"));
        listResourceTypes.put("responses", responses);

        result.put("get", listResourceTypes);

        return result;
    }

    private static JsonObject generateListResources(String resourceName) {
        JsonObject result = new JsonObject();

        JsonObject responses = generateResponses();
        responses.put("200", generateResponseArrayOfResourceIds(resourceName));

        JsonObject listResources = new JsonObject();
        listResources.put("description", "You can get a list of " + resourceName + "s");
        listResources.put("summary", "List " + resourceName + "s");
        listResources.put("operationId", "list" + resourceName);
        listResources.put("tags", new JsonArray().add(resourceName));
        listResources.put("responses", responses);

        result.put("get", listResources);

        return result;
    }

    private static JsonObject generateCrudResources(String resourceName) {
        JsonObject result = new JsonObject();

        JsonObject responsesGet = generateResponses();
        responsesGet.put("200", generateResponseResource(resourceName));

        JsonObject readResource = new JsonObject();
        readResource.put("description", "You can get a resource of type " + resourceName + " with id.");
        readResource.put("summary", "Get a " + resourceName);
        readResource.put("operationId", "read" + resourceName);
        readResource.put("tags", new JsonArray().add(resourceName));
        readResource.put("responses", responsesGet);
        readResource.put("parameters", new JsonArray().add(generateQueryParameterId()));

        result.put("get", readResource);

        JsonObject responsesDelete = generateResponses();
        responsesDelete.put("204", generateResponseNoContent());

        JsonObject deleteResources = new JsonObject();
        deleteResources.put("description", "You can delete a resource of type " + resourceName + " with id.");
        deleteResources.put("summary", "Delete a " + resourceName);
        deleteResources.put("operationId", "delete" + resourceName);
        deleteResources.put("tags", new JsonArray().add(resourceName));
        deleteResources.put("security", generateSecurity());
        deleteResources.put("responses", responsesDelete);
        deleteResources.put("parameters", new JsonArray().add(generateQueryParameterId()));

        result.put("delete", deleteResources);

        JsonObject responsesPut = generateResponses();
        responsesPut.put("201", generateResponseCreated());
        responsesPut.put("202", generateResponseAccepted());
        responsesPut.put("204", generateResponseNoContent());

        JsonObject createOrUpdateResources = new JsonObject();
        createOrUpdateResources.put("description", "You can put a resource of type " + resourceName + " with id.");
        createOrUpdateResources.put("summary", "Create or Update a " + resourceName);
        createOrUpdateResources.put("operationId", "createOrUpdate" + resourceName);
        createOrUpdateResources.put("tags", new JsonArray().add(resourceName));
        createOrUpdateResources.put("security", generateSecurity());
        createOrUpdateResources.put("responses", responsesPut);
        createOrUpdateResources.put("parameters", new JsonArray()
                .add(generateQueryParameterId())
                .add(generateQueryParameterSynchronous())
        );
        createOrUpdateResources.put("requestBody", generateRequestBodyResource(resourceName));

        result.put("put", createOrUpdateResources);

        return result;
    }

    private static JsonObject generateQueryParameterId() {
        return new JsonObject()
                .put("name", "id")
                .put("in", "path")
                .put("description", "Id of the requested resource")
                .put("required", true)
                .put("schema", new JsonObject()
                        .put("type", "string")
                );
    }

    private static JsonObject generateQueryParameterSynchronous() {
        return new JsonObject()
                .put("name", "synchronous")
                .put("in", "query")
                .put("description", "If disabled the execution is asynchronous (response code 202)")
                .put("schema", new JsonObject()
                        .put("type", "boolean")
                        .put("default", "true")
                );
    }

    private static JsonObject generateRequestBodyResource(String resourceName) {
        return new JsonObject()
                .put("required", true)
                .put("content", new JsonObject()
                        .put("application/json", new JsonObject()
                                .put("schema", new JsonObject()
                                        .put("$ref", "#/components/schemas/" + resourceName)
                                )
                        )
                );
    }

    private static JsonArray generateSecurity() {
        return new JsonArray()
                .add(new JsonObject().put("ApiKeyAuth", new JsonArray()))
                .add(new JsonObject().put("ApiKeyAuth2", new JsonArray()));
    }

    private static JsonObject generateResponseResource(String resourceName) {
        return new JsonObject()
                .put("description", "Resource object of type " + resourceName)
                .put("content", new JsonObject()
                        .put("application/json", new JsonObject()
                                .put("schema", new JsonObject()
                                        .put("$ref", "#/components/schemas/" + resourceName)
                                )
                        )
                );
    }

    private static JsonObject generateResponseResourceId(String resourceName) {
        return new JsonObject()
                .put("description", "Resource object of type " + resourceName)
                .put("content", new JsonObject()
                        .put("application/json", new JsonObject()
                                .put("schema", new JsonObject()
                                        .put("type", "string")
                                )
                        )
                );
    }

    private static JsonObject generateResponseArrayOfResourceIds(String resourceName) {
        return new JsonObject()
                .put("description", "Array of resources IDs of type " + resourceName)
                .put("content", new JsonObject()
                        .put("application/json", new JsonObject()
                                .put("schema", new JsonObject()
                                        .put("type", "array")
                                        .put("items", new JsonObject()
                                                .put("type", "string")
                                        )
                                )
                        )
                );
    }

    private static JsonObject generateResponseArrayOfResourceTypes() {
        return new JsonObject()
                .put("description", "Array of resource types")
                .put("content", new JsonObject()
                        .put("application/json", new JsonObject()
                                .put("schema", new JsonObject()
                                        .put("type", "array")
                                        .put("items", new JsonObject()
                                                .put("type", "string")
                                        )
                                )
                        )
                );
    }

    private static JsonObject generateResponses() {
        return new JsonObject()
                .put("400", generateResponseBadRequest())
                .put("404", generateResponseNotFound())
                .put("500", generateResponseInternalServerError());
    }

    private static JsonObject generateResponseCreated() {
        return new JsonObject().put("$ref", "#/components/responses/Created");
    }

    private static JsonObject generateResponseAccepted() {
        return new JsonObject().put("$ref", "#/components/responses/Accepted");
    }

    private static JsonObject generateResponseNoContent() {
        return new JsonObject().put("$ref", "#/components/responses/NoContent");
    }

    private static JsonObject generateResponseBadRequest() {
        return new JsonObject().put("$ref", "#/components/responses/BadRequest");
    }

    private static JsonObject generateResponseNotFound() {
        return new JsonObject().put("$ref", "#/components/responses/NotFound");
    }

    private static JsonObject generateResponseInternalServerError() {
        return new JsonObject().put("$ref", "#/components/responses/InternalServerError");
    }

    private static JsonObject convertToJsonObject(Map<String, Object> input) {
        JsonObject result = new JsonObject();
        for (String key : input.keySet()) {
            Object value = input.get(key);
            if (value instanceof Map) {
                result.put(key, convertToJsonObject((Map<String, Object>) value));
            } else if (value instanceof List) {
                result.put(key, convertToJsonArray((List<Object>) value));
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private static JsonArray convertToJsonArray(List<Object> input) {
        JsonArray result = new JsonArray();
        for (Object value : input) {
            if (value instanceof Map) {
                result.add(convertToJsonObject((Map<String, Object>) value));
            } else if (value instanceof List) {
                result.add(convertToJsonArray((List<Object>) value));
            } else {
                result.add(value);
            }
        }
        return result;
    }

    private static Map<String, Object> convertToMap(JsonObject input) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : input.getMap().keySet()) {
            Object value = input.getValue(key);
            if (value instanceof JsonObject valueObject) {
                result.put(key, convertToMap(valueObject));
            } else if (value instanceof JsonArray valueArray) {
                result.put(key, convertToList(valueArray));
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private static List<Object> convertToList(JsonArray input) {
        List<Object> result = new ArrayList<>();
        for (Object value : input) {
            if (value instanceof JsonObject valueObject) {
                result.add(convertToMap(valueObject));
            } else if (value instanceof JsonArray valueArray) {
                result.add(convertToList(valueArray));
            } else {
                result.add(value);
            }
        }
        return result;
    }

    public static String getResourceType(String input) {
        return input.substring(input.lastIndexOf("_")+1);
    }

    public static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();

        String[] split = input.split("-");
        for (String part : split) {
            result.append(StringUtils.capitalize(part));
        }

        return result.toString();
    }

}
