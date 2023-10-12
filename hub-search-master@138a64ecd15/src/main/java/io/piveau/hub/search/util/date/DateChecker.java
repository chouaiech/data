package io.piveau.hub.search.util.date;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

public class DateChecker {

    public static void check(JsonArray payload) {
        payload.stream()
                .map(JsonObject.class::cast)
                .forEach(DateChecker::check);
    }

    public static void check(JsonObject payload) {
        if (payload.containsKey("modified") || payload.containsKey("issued")) {
            String modified = payload.getString("modified");
            String issued = payload.getString("issued");

            DateTime now = DateTime.now();

            if (modified != null) {
                DateTime check = new DateTime(modified);
                if (check.isAfter(now)) {
                    payload.put("modified", "_" + modified);
                }
            }

            if (issued != null) {
                DateTime check = new DateTime(issued);
                if (check.isAfter(now)) {
                    payload.put("issued", "_" + issued);
                }
            }
        }
    }

}
