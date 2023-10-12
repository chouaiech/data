package io.piveau.metrics.reporter.model.chart;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all info needed for Doughnut Chart creation in one data structure:
 * - chartName of the chart
 * - data to insert into template
 * - Quickchart template (see insertIntoTemplate())
 * - chart image once it has been received by Quickchart
 * - boolean if no data for the chart is available and a matching error text
 */

public class DoughnutChart extends Chart {

    public static JsonObject template;

    public DoughnutChart(String name, JsonArray data, JsonObject translations, String errorMessage) {
        super(name, errorMessage);

        if (errorMessage == null) {
            List<String> keys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();

            data.forEach(item -> {
                JsonObject obj = (JsonObject) item;
                int percentage = (int) Math.floor(obj.getDouble("percentage"));

                if (obj.getString("name").equals("yes")) {
                    keys.add(translations.getString("yes"));
                } else {
                    keys.add(translations.getString("no"));
                }

                values.add(percentage);
            });

            initQuickchartTemplate(template, keys, values);
        }
    }
}