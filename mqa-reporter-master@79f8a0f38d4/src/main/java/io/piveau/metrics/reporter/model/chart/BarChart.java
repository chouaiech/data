package io.piveau.metrics.reporter.model.chart;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all info needed for Bar Chart creation in one data structure:
 * - chartName of the chart
 * - data to insert into template
 * - Quickchart template (see insertIntoTemplate())
 * - chart image once it has been received by Quickchart
 * - boolean if no data for the chart is available and a matching error text
 */

public class BarChart extends Chart {

    public static JsonObject template;

    public BarChart(String name, JsonArray data, String errorMessage) {
        super(name, errorMessage);

        if (errorMessage == null) {
            List<String> keys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();

            data.forEach(item -> {
                JsonObject obj = (JsonObject) item;
                int percentage = (int) Math.floor(obj.getDouble("percentage"));

                if (percentage >= 1) {
                    keys.add(obj.getString("name"));
                    values.add(percentage);
                }
            });

            initQuickchartTemplate(template, keys, values);
        }
    }
}