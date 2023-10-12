package io.piveau.metrics.reporter.model.chart;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public abstract class Chart {

    JsonObject chart;

    private String name;

    private String errorMessage;

    private byte[] imageBytes;

    public Chart(String name, String errorMessage) {
        this.name = name;
        this.errorMessage = errorMessage;
    }

    public JsonObject getChart() {
        return chart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    void initQuickchartTemplate(JsonObject template, List<String> keys, List<Integer> values) {
        chart = template.copy();

        chart.getJsonObject("chart")
                .getJsonObject("data")
                .getJsonArray("labels")
                .clear()
                .addAll(new JsonArray(keys));

        chart.getJsonObject("chart")
                .getJsonObject("data")
                .getJsonArray("datasets")
                .getJsonObject(0)
                .getJsonArray("data")
                .clear()
                .addAll(new JsonArray(values));
    }
}
