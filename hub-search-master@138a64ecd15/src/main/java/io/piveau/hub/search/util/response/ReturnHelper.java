package io.piveau.hub.search.util.response;

import io.vertx.core.json.JsonObject;

public class ReturnHelper {

    public static JsonObject returnSuccess(Integer status, JsonObject result) {
        JsonObject object = new JsonObject();
        if (status != null) object.put("status", status);
        if (result != null) object.put("result", result);
        return object;
    }

    public static JsonObject returnSuccess(Integer status, String result) {
        JsonObject object = new JsonObject();
        if (status != null) object.put("status", status);
        if (result != null) object.put("result", result);
        return object;
    }

    public static JsonObject returnSuccess(Integer status) {
        JsonObject object = new JsonObject();
        if (status != null) object.put("status", status);
        return object;
    }
}
