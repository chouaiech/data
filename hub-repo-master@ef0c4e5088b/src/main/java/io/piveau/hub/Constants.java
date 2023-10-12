package io.piveau.hub;

public final class Constants {

    private Constants() {}

    public static final String ENV_PIVEAU_TRIPLESTORE_CONFIG = "PIVEAU_TRIPLESTORE_CONFIG";
    public static final String ENV_PIVEAU_SHADOW_TRIPLESTORE_CONFIG = "PIVEAU_SHADOW_TRIPLESTORE_CONFIG";

    public static final String ENV_PIVEAU_HUB_SERVICE_PORT = "PIVEAU_HUB_SERVICE_PORT";
    public static final String ENV_PIVEAU_HUB_API_KEY = "PIVEAU_HUB_API_KEY";
    public static final String ENV_PIVEAU_HUB_API_KEYS = "PIVEAU_HUB_API_KEYS";
    public static final String ENV_PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA = "PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA";
    public static final String ENV_PIVEAU_HUB_BASE_URI = "PIVEAU_HUB_BASE_URI";
    public static final String ENV_PIVEAU_HUB_VALIDATOR = "PIVEAU_HUB_VALIDATOR";
    public static final String ENV_PIVEAU_HUB_SEARCH_SERVICE = "PIVEAU_HUB_SEARCH_SERVICE";
    public static final String ENV_PIVEAU_HUB_XML_DECLARATION = "PIVEAU_HUB_XML_DECLARATION";
    public static final String ENV_PIVEAU_TRANSLATION_SERVICE = "PIVEAU_TRANSLATION_SERVICE";
    public static final String ENV_PIVEAU_DATA_UPLOAD = "PIVEAU_DATA_UPLOAD";
    public static final String ENV_PIVEAU_HUB_FORCE_UPDATES = "PIVEAU_HUB_FORCE_UPDATES";
    public static final String ENV_PIVEAU_DCATAP_SCHEMA_CONFIG = "PIVEAU_DCATAP_SCHEMA_CONFIG";
    public static final String ENV_PIVEAU_HUB_LOAD_VOCABULARIES = "PIVEAU_HUB_LOAD_VOCABULARIES";
    public static final String ENV_PIVEAU_HUB_LOAD_VOCABULARIES_FETCH = "PIVEAU_HUB_LOAD_VOCABULARIES_FETCH";
    public static final String ENV_PIVEAU_HUB_HISTORIC_METRICS = "PIVEAU_HUB_HISTORIC_METRICS";
    public static final String ENV_PIVEAU_HUB_CORS_DOMAINS = "PIVEAU_HUB_CORS_DOMAINS";
    public static final String ENV_PIVEAU_CLUSTER_CONFIG = "PIVEAU_CLUSTER_CONFIG";
    public static final String ENV_PIVEAU_LOGO_PATH = "PIVEAU_LOGO_PATH";
    public static final String ENV_PIVEAU_FAVICON_PATH = "PIVEAU_FAVICON_PATH";
    public static final String ENV_PIVEAU_IMPRINT_URL = "PIVEAU_IMPRINT_URL";
    public static final String ENV_PIVEAU_PRIVACY_URL = "PIVEAU_PRIVACY_URL";
    public static final String ENV_PIVEAU_HUB_SHELL_CONFIG = "PIVEAU_HUB_SHELL_CONFIG";
    public static final String ENV_PIVEAU_IDENTIFIERS_REGISTRATION = "PIVEAU_IDENTIFIERS_REGISTRATION";


    public static final String ENV_PIVEAU_ARCHIVING_ENABLED = "PIVEAU_ARCHIVING_ENABLED";
  




    // Authentication via JWT
    public static final String AUTHENTICATION_TYPE = "auth";
    public static final String JWT_AUTH = "jwtAuth";

    // Keycloak Scopes
    public static final String KEYCLOAK_SCOPE_CATALOGUE_CREATE = "catalogue:create";
    public static final String KEYCLOAK_SCOPE_CATALOGUE_UPDATE = "catalogue:update";
    public static final String KEYCLOAK_SCOPE_CATALOGUE_DELETE = "catalogue:delete";

    public static final String KEYCLOAK_SCOPE_DATASET_CREATE = "dataset:create";
    public static final String KEYCLOAK_SCOPE_DATASET_UPDATE = "dataset:update";
    public static final String KEYCLOAK_SCOPE_DATASET_DELETE = "dataset:delete";
}
