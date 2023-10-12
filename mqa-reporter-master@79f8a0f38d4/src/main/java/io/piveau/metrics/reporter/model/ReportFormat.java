package io.piveau.metrics.reporter.model;

import java.util.Arrays;
import java.util.Optional;

public enum ReportFormat {

    PDF("application/pdf"),
    ODS("application/vnd.oasis.opendocument.spreadsheet"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final String contentType;

    ReportFormat(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static Optional<ReportFormat> formatByContentType(String contentType) {
        return Arrays.stream(values())
                .filter(format -> format.contentType.equals(contentType))
                .findAny();
    }
}
