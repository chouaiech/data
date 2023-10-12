package io.piveau.metrics.reporter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.piveau.metrics.reporter.ApplicationConfig;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Locale;

public class ReportTask {
    private Locale languageCode;
    private List<JsonObject> metrics;

    // required by Json encode/decode
    public ReportTask() {
    }

    public ReportTask(Locale languageCode, List<JsonObject> metrics) {
        this.languageCode = languageCode;
        this.metrics = metrics;
    }

    public Locale getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(Locale languageCode) {
        this.languageCode = languageCode;
    }

    public List<JsonObject> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<JsonObject> metrics) {
        this.metrics = metrics;
    }

    @JsonIgnore
    public String getPath() {
        return ApplicationConfig.WORK_DIR + "/" + languageCode.toString() + "/";
    }
}
