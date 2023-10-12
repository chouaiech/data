package io.piveau.metrics.reporter.model;

import io.piveau.metrics.reporter.model.chart.Chart;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MetricSection {
    private String heading;
    private final List<Chart> charts = new ArrayList<>();
    private JsonObject metrics;

    public MetricSection(JsonObject metrics, String heading) {
        this.heading = heading;
        this.metrics = metrics;
    }

    public String getSectionHeading() {
        return heading;
    }

    public void setSectionHeading(String heading) {
        this.heading = heading;
    }

    public JsonObject getMetrics() {
        return metrics;
    }

    public void setMetrics(JsonObject metrics) {
        this.metrics = metrics;
    }

    public List<Chart> getCharts() {
        return charts;
    }
}

