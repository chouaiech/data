package io.piveau.metrics.reporter.model;

import io.piveau.metrics.reporter.model.chart.BarChart;
import io.piveau.metrics.reporter.model.chart.DoughnutChart;
import io.piveau.metrics.reporter.util.Translator;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Each catalogue is contains five sections with metrics.
 */

public class Catalogue {

    private static final List<String> SECTION_NAMES = List.of(
            "contextuality",
            "accessibility",
            "reusability",
            "interoperability",
            "findability"
    );

    private JsonObject metrics;

    private String id;
    private String title;
    private String description;
    private String spatial;
    private String type;
    private List<MetricSection> metricSections;

    public Catalogue(JsonObject json) {
        this.metrics = json;
        this.metricSections = new ArrayList<>();

        JsonObject catalogueInfo = json.getJsonObject("info");
        this.id = catalogueInfo.getString("id");
        this.title = catalogueInfo.getString("title");
        this.description = catalogueInfo.getString("description");
        this.spatial = catalogueInfo.getString("spatial");
        this.type = catalogueInfo.getString("type");
    }

    public JsonObject getMetrics() {
        return metrics;
    }

    public void setMetrics(JsonObject metrics) {
        this.metrics = metrics;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }

    public List<MetricSection> getSections() {
        return metricSections;
    }

    public void setSections(List<MetricSection> sections) {
        this.metricSections = sections;
    }

    public void generateMetricSections(Translator translator) {
        JsonObject sectionTranslations = translator.getSectionTranslations();

        List<MetricSection> sections = SECTION_NAMES.stream().map(name -> {
            MetricSection section = new MetricSection(metrics.getJsonObject(name),
                    sectionTranslations.getString(name));

            JsonObject chartTranslations = new JsonObject()
                    .put("yes", sectionTranslations.getValue("yes"))
                    .put("no", sectionTranslations.getValue("no"));

            for (Map.Entry<String, Object> entry : section.getMetrics()) {
                // FIXME
                // dirty fix to prevent class cast errors when encountering dimension score
                if (entry.getValue() instanceof JsonArray) {
                    JsonArray chartData = (JsonArray) entry.getValue();

                    if (!chartData.isEmpty()) {
                        JsonObject obj = chartData.getJsonObject(0);
                        if (obj.getString("name").equals("yes") || obj.getString("name").equals("no")) {
                            section.getCharts().add(new DoughnutChart(sectionTranslations.getString(entry.getKey()), chartData, chartTranslations, null));
                        } else {
                            section.getCharts().add(new BarChart(sectionTranslations.getString(entry.getKey()), chartData, null));
                        }
                    } else {
                        section.getCharts().add(new DoughnutChart(sectionTranslations.getString(entry.getKey()), null, null, translator.getTranslations().getNoDataError()));
                    }
                }
            }

            return section;
        }).collect(Collectors.toList());

        this.setSections(sections);
    }
}
