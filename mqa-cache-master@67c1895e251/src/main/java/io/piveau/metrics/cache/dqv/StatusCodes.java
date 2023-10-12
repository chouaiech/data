package io.piveau.metrics.cache.dqv;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@DataObject
public class StatusCodes {

    private Map<String, Double> statusCodes;

    public StatusCodes(Map<String, Double> statusCodes) {
        this.statusCodes = statusCodes;
    }

    public StatusCodes(JsonObject jsonObject) {
        statusCodes = jsonObject.mapTo(Map.class);
    }

    public Map<String, Double> getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(Map<String, Double> statusCodes) {
        this.statusCodes = statusCodes;
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        statusCodes.forEach(result::put);
        return result;
    }


    public JsonArray toJsonArray() {
        JsonArray result = new JsonArray();
        statusCodes.forEach((k, v) -> {
            result.add(new JsonObject()
                    .put("name", k)
                    .put("percentage", v));
        });
        return result;
    }
}
