package io.piveau.metrics.similarities;

final class ApplicationConfig {

    static final String ENV_APPLICATION_PORT = "PORT";
    static final Integer DEFAULT_APPLICATION_PORT = 8080;

    static final String ENV_SPARQL_URL = "SPARQL_URL";
    static final String DEFAULT_SPARQL_URL = "http://virtuoso:8890";

    static final String ENV_DEFAULT_RESULT_SIZE = "DEFAULT_RESULT_SIZE";
    static final Integer DEFAULT_RESULT_SIZE = 10;

    public static final String ENV_PIVEAU_SIMILARITY_CRONDEF = "PIVEAU_SIMILARITY_CRONDEF";
    static final String ENV_CORS_DOMAINS = "CORS_DOMAINS";

    static final String WORK_DIR = "/fingerprints";

}
