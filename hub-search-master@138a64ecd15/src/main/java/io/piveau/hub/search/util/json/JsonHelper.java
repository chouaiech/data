package io.piveau.hub.search.util.json;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static Map<String, Object> convertJsonObjectToMap(JsonObject payload) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : payload.getMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray valueArray) {
                map.putIfAbsent(key, convertJsonArrayToList(valueArray));
            } else if (value instanceof JsonObject valueObject) {
                map.putIfAbsent(key, convertJsonObjectToMap(valueObject));
            } else {
                map.putIfAbsent(key, value);
            }
        }
        return map;
    }

    public static List<Object> convertJsonArrayToList(JsonArray payload) {
        List<Object> list = new ArrayList<>();
        for (Object value : payload) {
            if (value instanceof JsonArray valueArray) {
                list.add(convertJsonArrayToList(valueArray));
            } else if (value instanceof JsonObject valueObject) {
                list.add(convertJsonObjectToMap(valueObject));
            } else {
                list.add(value);
            }
        }
        return list;
    }
}
