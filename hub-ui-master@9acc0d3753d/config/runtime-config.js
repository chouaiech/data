/**
 * Configuration template file to bind specific properties to environment variables.
 * All values must have the prefix $VUE_APP_.
 * Their corresponding environment variable key labels must be the values without the $ character.
 * This object should be structurally identical (name and path) to the standard configuration file.
 */
export default {
  title: "$VUE_APP_TITLE",
  description: "$VUE_APP_DESCRIPTION",
  keywords: "$VUE_APP_KEYWORDS",
  api: {
    baseUrl: "$VUE_APP_API_BASE_URL",
    qualityBaseUrl: "$VUE_APP_API_QUALITY_BASE_URL",
    similarityBaseUrl: "$VUE_APP_API_SIMILARITY_BASE_URL",
    gazetteerBaseUrl: "$VUE_APP_API_GAZETTEER_BASE_URL",
    hubUrl: "$VUE_APP_API_HUB_URL",
    catalogBaseUrl: "$VUE_APP_API_CATALOG_BASE_URL",
    authToken: "$VUE_APP_API_AUTH_TOKEN",
    vueAppCorsproxyApiUrl: "$VUE_APP_CORSPROXY_API_URL",
    sparqlUrl: '$VUE_APP_SPARQL_URL',
    fileUploadUrl: '$VUE_APP_FILEUPLOAD_URL',
  },
  tracker: {
    isPiwikPro: '$VUE_APP_TRACKER_IS_PIWIK_PRO',
    siteId: '$VUE_APP_TRACKER_SITE_ID',
    trackerUrl: '$VUE_APP_TRACKER_TRACKER_URL'
  },
  useAuthService: "$VUE_APP_USE_AUTH_SERVICE",
  keycloak: {
    enableLogin: "$VUE_APP_KEYCLOAK_ENABLE_LOGIN",
    realm: "$VUE_APP_KEYCLOAK_REALM",
    url: "$VUE_APP_KEYCLOAK_URL",
    "ssl-required": "$VUE_APP_KEYCLOAK_SSL_REQUIRED",
    clientId: "$VUE_APP_KEYCLOAK_CLIENT_ID",
    "public-client": "$VUE_APP_KEYCLOAK_PUBLIC_CLIENT",
    "verify-token-audience": "$VUE_APP_KEYCLOAK_VERIFY_TOKEN_AUDIENCE",
    "use-resource-role-mappings": "$VUE_APP_KEYCLOAK_USE_RESOURCE_ROLE_MAPPINGS",
    "confidential-port": "$VUE_APP_KEYCLOAK_CONFIDENTIAL_PORT"
  },
  rtp: {
    grand_type: "$VUE_APP_RTP_GRAND_TYPE",
    audience: "$VUE_APP_RTP_AUDIENCE"
  },
  languages: {
    useLanguageSelector: "$VUE_APP_LANGUAGES_USE_LANGUAGE_SELECTOR",
    locale: "$VUE_APP_LANGUAGES_LOCALE",
    fallbackLocale: "$VUE_APP_LANGUAGES_FALLBACK_LOCALE"
  },
  routerOptions: {
    base: "$VUE_APP_ROUTER_OPTIONS_BASE",
    mode: "$VUE_APP_ROUTER_OPTIONS_MODE"
  },
  navigation: {
    top: {
      main: {
        data: {
          sparql: {
            show: "$VUE_APP_NAVIGATION_TOP_MAIN_DATA_SPARQL_SHOW"
          }
        }
      }
    },
    bottom: {
      login: {
        useLogin: "$VUE_APP_NAVIGATION_BOTTOM_LOGIN_USE_LOGIN",
        loginURL: "$VUE_APP_NAVIGATION_BOTTOM_LOGIN_LOGIN_URL",
        loginTitle: "$VUE_APP_NAVIGATION_BOTTOM_LOGIN_LOGIN_TITLE",
        logoutURL: "$VUE_APP_NAVIGATION_BOTTOM_LOGIN_LOGOUT_URL",
        logoutTitle: "$VUE_APP_NAVIGATION_BOTTOM_LOGIN_LOGOUT_TITLE"
      }
    }
  },
  catalogs: {
    facets: {
      useDatasetFacetsMap: "$VUE_APP_CATALOGS_FACETS_USE_DATASET_FACETS_MAP",
      cutoff: "$VUE_APP_CATALOGS_FACETS_CUTOFF",
      showClearButton: "$VUE_APP_CATALOGS_FACETS_SHOW_CLEAR_BUTTON",
      showFacetsTitle: "$VUE_APP_CATALOGS_FACETS_SHOW_FACETS_TITLE"
    }
  },
  datasets: {
    facets: {
      useDatasetFacetsMap: "$VUE_APP_DATASETS_FACETS_USE_DATASET_FACETS_MAP",
      cutoff: "$VUE_APP_DATASETS_FACETS_CUTOFF",
      showClearButton: "$VUE_APP_DATASETS_FACETS_SHOW_CLEAR_BUTTON",
      showFacetsTitle: "$VUE_APP_DATASETS_FACETS_SHOW_FACETS_TITLE"
    }
  },
  datasetDetails: {
    header: {
      navigation: "$VUE_APP_DATASETDETAILS_HEADER_NAVIGATION",
      hidePublisher: "$VUE_APP_DATASETDETAILS_HEADER_HIDE_PUBLISHER",
      hideDate: "$VUE_APP_DATASETDETAILS_HEADER_HIDE_DATE"
    },
    keywords: {
      showTitle: "$VUE_APP_DATASETDETAILS_KEYWORDS_SHOW_TITLE"
    },
    description: {
      enableMarkdownInterpretation: "$VUE_APP_DATASETDETAILS_DESCRIPTION_ENABLE_MARKDOWN_INTERPRETATION"
    },
    distributions: {
      displayAll: "$VUE_APP_DATASETDETAILS_DISTRIBUTIONS_DISPLAY_ALL",
      displayCount: "$VUE_APP_DATASETDETAILS_DISTRIBUTIONS_DISPLAY_COUNT",
      incrementSteps: "$VUE_APP_DATASETDETAILS_DISTRIBUTIONS_INCREMENT_STEPS",
      descriptionMaxLines: "$VUE_APP_DATASETDETAILS_DISTRIBUTIONS_DESCRIPTION_MAX_LINES",
      descriptionMaxChars: "$VUE_APP_DATASETDETAILS_DISTRIBUTIONS_DESCRIPTION_MAX_CHARS",
    },
    pages: {
      isVisible: "$VUE_APP_DATASETDETAILS_PAGES_IS_VISIBLE",
      displayAll: "$VUE_APP_DATASETDETAILS_PAGES_DISPLAY_ALL",
      displayCount: "$VUE_APP_DATASETDETAILS_PAGES_DISPLAY_COUNT",
      incrementSteps: "$VUE_APP_DATASETDETAILS_PAGES_INCREMENT_STEPS",
      descriptionMaxLines: "$VUE_APP_DATASETDETAILS_PAGES_DESCRIPTION_MAX_LINES",
      descriptionMaxChars: "$VUE_APP_DATASETDETAILS_PAGES_DESCRIPTION_MAX_CHARS",
    },
    visualisations: {
      isVisible: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_IS_VISIBLE",
      displayAll: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_DISPLAY_ALL",
      displayCount: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_DISPLAY_COUNT",
      incrementSteps: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_INCREMENT_STEPS",
      descriptionMaxLines: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_DESCRIPTION_MAX_LINES",
      descriptionMaxChars: "$VUE_APP_DATASETDETAILS_VISUALISATIONS_DESCRIPTION_MAX_CHARS",
    },
    bulkDownload: {
      buttonPosition: "$VUE_APP_DATASETDETAILS_BULKDOWNLOAD_BUTTON_POSITION",
      MAX_FILE_TITLE_LENGTH: "$VUE_APP_DATASETDETAILS_BULKDOWNLOAD_MAX_FILE_TITLE_LENGTH",
      MAX_REQUESTS_COUNT: "$VUE_APP_DATASETDETAILS_BULKDOWNLOAD_MAX_REQUESTS_COUNT",
      INTERVAL_MS: "$VUE_APP_DATASETDETAILS_BULKDOWNLOAD_INTERVAL_MS",
      TIMEOUT_MS: "$VUE_APP_DATASETDETAILS_BULKDOWNLOAD_TIMEOUT_MS",
    },
  },
  maps: {
    useAnimation: "$VUE_APP_MAPS_USE_ANIMATION",
    urlTemplate: "$VUE_APP_MAPS_URL_TEMPLATE",
    options: {
      id: "$VUE_APP_MAPS_OPTIONS_ID",
      accessToken: "$VUE_APP_MAPS_OPTIONS_ACCESS_TOKEN",
      attribution: "$VUE_APP_MAPS_OPTIONS_ATTRIBUTION"
    },
    location: "$VUE_APP_MAPS_LOCATION",
    spatialType: "$VUE_APP_MAPS_SPATIAL_TYPE",
    height: "$VUE_APP_MAPS_HEIGHT",
    width: "$VUE_APP_MAPS_WIDTH",
    mapContainerId: "$VUE_APP_MAPS_MAP_CONTAINER_ID",
    mapStyle: {
      color: "$VUE_APP_MAPS_MAP_STYLE_COLOR",
      fillColor: "$VUE_APP_MAPS_MAP_STYLE_FILL_COLOR",
      fillOpacity: "$VUE_APP_MAPS_MAP_STYLE_FILL_OPACITY",
      weight: "$VUE_APP_MAPS_MAP_STYLE_WEIGHT",
      radius: "$VUE_APP_MAPS_MAP_STYLE_RADIUS",
    },
    sender: {
      startBounds: "$VUE_APP_MAPS_SENDER_START_BOUNDS",
      height: "$VUE_APP_MAPS_SENDER_HEIGHT",
      width: "$VUE_APP_MAPS_SENDER_WIDTH",
      mapContainerId: "$VUE_APP_MAPS_SENDER_MAP_CONTAINER_ID",
    },
    receiver: {
      startBounds: "$VUE_APP_MAPS_RECEIVER_START_BOUNDS",
      height: "$VUE_APP_MAPS_RECEIVER_HEIGHT",
      width: "$VUE_APP_MAPS_RECEIVER_WIDTH",
      mapContainerId: "$VUE_APP_MAPS_RECEIVER_MAP_CONTAINER_ID",
      attributionPosition: "$VUE_APP_MAPS_RECEIVER_ATTRIBUTION_POSITION",
    },
  },
  upload: {
    useUpload: "$VUE_APP_UPLOAD_USE_UPLOAD",
    buttons: {
      Dataset: "$VUE_APP_UPLOAD_BUTTONS_DATASET",
      Catalogue: "$VUE_APP_UPLOAD_BUTTONS_CATALOGUE"
    }
  },
  doiRegistrationService: {
    persistentIdentifierType: "$VUE_APP_DOI_REGISTRATION_SERVICE_PERSISTENT_IDENTIFIER_TYPE",
  }
}
