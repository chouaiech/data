package io.piveau.hub.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final public class Constants {

    public static final String ENV_PIVEAU_HUB_SEARCH_API_KEY = "PIVEAU_HUB_SEARCH_API_KEY";
    public static final String ENV_PIVEAU_HUB_SEARCH_SERVICE_PORT = "PIVEAU_HUB_SEARCH_SERVICE_PORT";
    public static final String ENV_PIVEAU_HUB_SEARCH_CLI_CONFIG = "PIVEAU_HUB_SEARCH_CLI_CONFIG";
    public static final String ENV_PIVEAU_HUB_SEARCH_ES_CONFIG = "PIVEAU_HUB_SEARCH_ES_CONFIG";
    public static final String ENV_PIVEAU_HUB_SEARCH_SITEMAP_CONFIG = "PIVEAU_HUB_SEARCH_SITEMAP_CONFIG";
    public static final String ENV_PIVEAU_HUB_SEARCH_GAZETTEER_CONFIG = "PIVEAU_HUB_SEARCH_GAZETTEER_CONFIG";
    public static final String ENV_PIVEAU_HUB_SEARCH_FEED_CONFIG = "PIVEAU_HUB_SEARCH_FEED_CONFIG";

    public static final String ENV_PIVEAU_IMPRINT_URL = "PIVEAU_IMPRINT_URL";
    public static final String ENV_PIVEAU_PRIVACY_URL = "PIVEAU_PRIVACY_URL";
    public static final String ENV_PIVEAU_FAVICON_PATH = "PIVEAU_FAVICON_PATH";
    public static final String ENV_PIVEAU_LOGO_PATH = "PIVEAU_LOGO_PATH";
    public static final String ENV_PIVEAU_WEBROOT_PATH = "PIVEAU_WEBROOT_PATH";

    public static final String FACET_ITEMS = "items";

    public enum Operator {
        AND,
        OR
    }

    public static final String ELASTIC_READ_ALIAS = "_read";
    public static final String ELASTIC_WRITE_ALIAS = "_write";

    public static final String SEARCH_RESULT_COUNT_FIELD = "count";

    public static final List<Integer> SECURE_WITH_CIRCUIT_BREAKER = new ArrayList<>(Arrays.asList(502, 503));

    public static String getReadAlias(String index) {
        return index + ELASTIC_READ_ALIAS;
    }

    public static String getWriteAlias(String index) {
        return index + ELASTIC_WRITE_ALIAS;
    }

    public static String getIndexType(String index) {
        return index.substring(index.indexOf("_"));
    }

}
