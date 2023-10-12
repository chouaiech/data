package io.piveau.metrics.reporter;

import java.util.List;

public final class ApplicationConfig {

    static final String ENV_APPLICATION_PORT = "PORT";
    static final Integer DEFAULT_APPLICATION_PORT = 8080;

    public static final String ENV_METRICS_CATALOGUES_ADDRESS = "METRICS_CATALOGUES_ADDRESS";
    public static final String DEFAULT_METRICS_CATALOGUES_ADDRESS = "http://piveau-metrics-cache:8080/";

    public static final String ENV_SCHEDULES = "SCHEDULES";
    public static final String ENV_CORS_DOMAINS = "CORS_DOMAINS";


    public static final String ENV_SHACL_URL = "SHACL_URL";
    public static final String DEFAULT_SHACL_URL = "https://data.europa.eu/mqa/shacl-validator-ui";

    public static final String ENV_SHACL_API_URL = "SHACL_API_URL";
    public static final String DEFAULT_SHACL_API_URL = "https://data.europa.eu/api/mqa/shacl/";

    public static final String ENV_METHODOLOGY_URL = "METHODOLOGY_URL";
    public static final String DEFAULT_METHODOLOGY_URL = "https://data.europa.eu/mqa/methodology";

    public static final String ENV_FONT_REGULAR = "FONT_REGULAR";
    public static final String DEFAULT_FONT_REGULAR = "Roboto-Regular.ttf";

    public static final String ENV_FONT_BOLD = "FONT_BOLD";
    public static final String DEFAULT_FONT_BOLD = "Roboto-Bold.ttf";

    public static final String ENV_FONT_ITALIC = "FONT_ITALIC";
    public static final String DEFAULT_FONT_ITALIC = "Roboto-Italic.ttf";

    public static final String ENV_FONT_BOLD_ITALIC = "FONT_BOLD_ITALIC";
    public static final String DEFAULT_FONT_BOLD_ITALIC = "Roboto-BoldItalic.ttf";

    public static final String ENV_HEADER_IMAGE = "HEADER_IMAGE";
    public static final String DEFAULT_HEADER_IMAGE = "report_header.png";

    public static final String ENV_QUICKCHART_ADDRESS = "QUICKCHART_ADDRESS";

    public static final String ENV_API_KEY = "API_KEY";

    public static final String WORK_DIR = "reports";
    public static final List<String> AVAILABLE_LANGUAGES = List.of("bg", "cs", "da", "de", "el", "en", "es", "et", "fi", "fr", "ga", "hr", "hu", "it", "lt", "lv", "mt", "nl", "pl", "pt", "ro", "sk", "sl", "sv");
}
