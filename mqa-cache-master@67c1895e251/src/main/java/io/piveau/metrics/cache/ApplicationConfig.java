package io.piveau.metrics.cache;

public final class ApplicationConfig {

    static final String ENV_APPLICATION_PORT = "PORT";
    static final Integer DEFAULT_APPLICATION_PORT = 8080;

    static final String ENV_BASE_URI = "BASE_URI";
    static final String ENV_PIVEAU_DCATAP_SCHEMA_CONFIG = "PIVEAU_DCATAP_SCHEMA_CONFIG";


    public static final String ENV_H2_PASSWORD = "H2_PASSWORD";
    public static final String DEFAULT_H2_PASSWORD = "h2pass";

    public static final String ENV_MONGODB_CONNECTION = "MONGODB_CONNECTION";
    public static final String DEFAULT_MONGODB_CONNECTION = "mongodb://localhost:27019";

//    public static final String ENV_MONGODB_SERVER_HOST = "MONGODB_SERVER_HOST";
//    public static final String DEFAULT_MONGODB_SERVER_HOST = "localhost";
//
//    public static final String ENV_MONGODB_SERVER_PORT = "MONGODB_SERVER_PORT";
//    public static final Integer DEFAULT_MONGODB_SERVER_PORT = 27017;

    public static final String ENV_MONGODB_USERNAME = "MONGODB_USERNAME";
    public static final String DEFAULT_MONGODB_USERNAME = null;

    public static final String ENV_MONGODB_PASSWORD = "MONGODB_PASSWORD";
    public static final String DEFAULT_MONGODB_PASSWORD = null;

    public static final String ENV_MONGODB_DB_NAME = "MONGODB_DB_NAME";
    public static final String DEFAULT_MONGODB_DB_NAME = "metrics";

    public static final String ENV_CACHE_CORS_DOMAINS ="CACHE_CORS_DOMAINS";

    public static final String ENV_APIKEY = "CACHE_APIKEY";

}
