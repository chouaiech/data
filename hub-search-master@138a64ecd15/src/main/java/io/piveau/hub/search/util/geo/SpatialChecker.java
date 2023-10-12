package io.piveau.hub.search.util.geo;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class SpatialChecker {

    public static void check(JsonArray payload) {
        payload.stream()
                .map(JsonObject.class::cast)
                .forEach(SpatialChecker::check);
    }

    public static void check(JsonObject payload) {
        if (payload.containsKey("spatial")) {
            JsonArray spatial = payload.getJsonArray("spatial");
            if (spatial != null) {
                payload.put("spatial", SpatialChecker.checkSpatial(payload.getJsonArray("spatial")));
            }
        }
    }

    private static JsonArray checkGeometries(JsonArray geometries) {
        JsonArray geometriesChecked = new JsonArray();
        for (Object value : geometries) {
            if (value != null && value instanceof  JsonObject valueChecked) {
                geometriesChecked.add(valueChecked);
            } else {
                return null;
            }
        }
        return geometriesChecked;
    }

    private static JsonArray checkSpatial(JsonArray spatial) {
        JsonArray result = new JsonArray();
        for (Object obj : spatial) {
            JsonObject spatialObj = (JsonObject) obj;

            JsonObject checked = checkSpatial(spatialObj);
            if (checked != null) result.add(checked);
        }
        return result;
    }

    private static JsonObject checkSpatial(JsonObject spatial) {
        if (spatial == null) {
            return null;
        }

        String type = spatial.getString("type");

        if (type == null) {
            return null;
        }

        // feature is not supported in elasticsearch => translate to geometry
        // feature collection is not supported in elasticsearch => translate to geometry collection
        switch (type.toLowerCase()) {
            case "feature":
                return checkSpatial(spatial.getJsonObject("geometry"));
            case "featurecollection": {
                JsonObject spatialChecked = new JsonObject();

                Object featuresObj = spatial.getValue("features");
                if (featuresObj instanceof JsonArray || featuresObj == null) {
                    JsonArray features = (JsonArray) featuresObj;

                    if (features == null || features.isEmpty()) return null;

                    JsonArray geometriesChecked = checkGeometries(features);
                    if (geometriesChecked == null) return null;

                    spatialChecked.put("type", "GeometryCollection");
                    spatialChecked.put("geometries", geometriesChecked);

                    return spatialChecked;
                } else {
                    return null;
                }
            }
            case "geometrycollection": {
                JsonObject spatialChecked = new JsonObject();

                Object geometriesObj = spatial.getValue("geometries");
                if (geometriesObj instanceof JsonArray || geometriesObj == null) {
                    JsonArray geometries = (JsonArray) geometriesObj;

                    if (geometries == null || geometries.isEmpty()) return null;

                    JsonArray geometriesChecked = checkGeometries(geometries);
                    if (geometriesChecked == null) return null;

                    spatialChecked.put("type", type);
                    spatialChecked.put("geometries", geometriesChecked);

                    return spatialChecked;
                } else {
                    return null;
                }
            }
            default: {
                JsonObject spatialChecked = new JsonObject();

                Object coordinatesObj = spatial.getValue("coordinates");
                if (coordinatesObj instanceof JsonArray || coordinatesObj == null) {
                    JsonArray coordinates = (JsonArray) coordinatesObj;
                    spatialChecked.put("type", type);
                    spatialChecked.put("coordinates", coordinates);
                    return spatialChecked;
                } else {
                    return null;
                }
            }
        }
    }

}
