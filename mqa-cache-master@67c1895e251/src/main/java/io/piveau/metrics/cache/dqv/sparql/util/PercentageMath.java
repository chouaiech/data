package io.piveau.metrics.cache.dqv.sparql.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PercentageMath {
    private PercentageMath() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Double> roundMap(Map<String, Double> values) {
        Map<String, Double> rounded = new HashMap<>();
        if(!values.isEmpty()){
            values.forEach((k, v) -> rounded.put(k, Math.floor(v)));

            Map<String, Double> sorted = values.entrySet().stream().sorted(Map.Entry.comparingByValue((o1, o2) -> {
                double d1 = o1 - Math.floor(o1);
                double d2 = o2 - Math.floor(o2);
                return Double.compare(d1, d2);
            })).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            int roundedSum = rounded.values().stream().mapToInt(Double::intValue).sum();

            int diff = 100 - roundedSum;
            for (int i = 0; i < diff; i++) {
                String key = (String) sorted.keySet().toArray()[sorted.size() - (i + 1)];
                Double value = rounded.get(key) + 1;
                rounded.put(key, value);
            }

            //remove status codes that are now on 0%
            rounded.values().removeIf(e -> e == 0);
        }
        return rounded;
    }

    public static Double calculateYesPercentage(JsonObject percentages){
        int yes = percentages.containsKey("yes") ? percentages.getInteger("yes") : 0;
        int no = percentages.containsKey("no") ? percentages.getInteger("no") : 0;
        double total = yes + no;
        return yes / total * 100;
    }

    public static JsonArray getYesNoPercentage(Double yesPercentage) {
        double roundedYes = Math.round(yesPercentage);
        return new JsonArray()
                .add(new JsonObject()
                        .put("name", "yes")
                        .put("percentage", roundedYes))
                .add(new JsonObject()
                        .put("name", "no")
                        .put("percentage", 100.0 - roundedYes));
    }

}
