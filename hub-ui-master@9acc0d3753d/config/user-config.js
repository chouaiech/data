import {
  uploadService,
  datasetService,
  catalogService,
  gazetteerService
} from '@piveau/piveau-hub-ui-modules';

import i18n from './i18n';

const glueConfig = {
  title: 'HealthData@EU',
  description: 'HealthData@EU',
  keywords: 'DEU',
  api: {
	  baseUrl: 'http://healthdata-local.com/hub/api/search/',
    //baseUrl: 'http://piveau-hub-search/api/hub/search/',
    // baseUrl: 'https://ppe.data.europa.eu/api/hub/search/',
    // baseUrl: 'https://piveau-hub-search-data-europa-eu.apps.osc.fokus.fraunhofer.de/',
    qualityBaseUrl: 'http://healthdata-local.com/mqa/api/cache/',    
    //qualityBaseUrl: 'https://data.europa.eu/api/mqa/cache/',
    // qualityBaseUrl: 'https://ppe.data.europa.eu/api/mqa/cache/',
    similarityBaseUrl: 'http://healthdata-local.com/mqa/api/similarities/',    
    //similarityBaseUrl: 'https://data.europa.eu/api/similarities/',
    gazetteerBaseUrl: 'https://data.europa.eu/api/hub/search/gazetteer/',
    hubUrl: 'http://healthdata-local.com/hub/api/repo/',
    // hubUrl: 'https://ppe.data.europa.eu/api/hub/repo/',
    // hubUrl: 'https://piveau-hub-repo-data-europa-eu.apps.osc.fokus.fraunhofer.de/',
    catalogBaseUrl: 'https://europeandataportal.eu/',
    authToken: '',
    vueAppCorsproxyApiUrl: 'http://healthdata-local.com/geoviewer/proxy/',
    sparqlUrl: 'http://healthdata-local.com/mqa/api/virtuoso/sparql?default-graph-uri=',
    fileUploadUrl: 'http://healthdata-local.com/hub/api/store/',    
    //fileUploadUrl: 'http://piveau-hub-simple-store/api/hub/store/',
    // fileUploadUrl: 'https://ppe.data.europa.eu/api/hub/store/',
    // fileUploadUrl: 'https://piveau-hub-store-data-europa-eu.apps.osc.fokus.fraunhofer.de/',
  },
  tracker: {
    // Matomo/PiwikPro analytics config
    // If true, uses PiwikPro, if false, uses Matomo
    isPiwikPro: true,
    siteId: 'fed9dbb7-42d1-4ebc-a8bf-3c0b8fd03e09',
    trackerUrl: 'https://opanalytics.containers.piwik.pro/'
  },
  useAuthService: false,
//  keycloak: {
//    enableLogin: false,
//    realm: 'piveau',
//    url: 'https://keycloak-piveau.apps.osc.fokus.fraunhofer.de/auth',
//    'ssl-required': 'external',
//    clientId: 'piveau-hub-ui',
//    'public-client': true,
//    'verify-token-audience': true,
//    'use-resource-role-mappings': true,
//    'confidential-port': 0,
//    loginRedirectUri: '/data',
//    logoutRedirectUri: '/data',
//  },
  rtp: {
    grand_type: 'urn:ietf:params:oauth:grant-type:uma-ticket',
    audience: 'piveau-hub-repo',
  },
  languages: {
    useLanguageSelector: true,
    locale: 'en',
    fallbackLocale: 'en',
  },
  services: {
    catalogService,
    datasetService,
    gazetteerService,
    uploadService
  },
  themes: {
    header: 'dark',
  },
  routerOptions: {
    base: '/data',
    mode: 'history',
  },
  navigation: {
    top: {
      main: {
        home: {
          // href: 'https://link-to-external-url.com' (optional)
          // target: ['_self' | '_blank'] (optional)
          show: true,
        },
        data: {
          show: true,
          sparql: {
            show: false,
          },
        },
        maps: {
          show: false,
        },
        about: {
          show: false,
        },
        append: [
          {
            href: 'https://www.fokus.fraunhofer.de/datenschutz',
            target: '_self',
            title: 'Privacy Policy',
          },
          {
            href: 'https://www.fokus.fraunhofer.de/9663f8cb2d267d4b',
            target: '_self',
            title: 'Imprint',
          },
        ],
        icons: false,
      },
      sub: {
        privacyPolicy: {
          show: false,
          href: 'https://www.fokus.fraunhofer.de/datenschutz',
          target: '_self',
        },
        imprint: {
          show: false,
          href: 'https://www.fokus.fraunhofer.de/9663f8cb2d267d4b',
          target: '_self',
        },
      },
    },
    bottom: {
      login: {
        useLogin: true,
        loginURL: '/login',
        loginTitle: 'Data Provider Interface Login',
        logoutURL: '/logout',
        logoutTitle: 'Data Provider Interface Logout',
      }
    }
  },
  pagination: {
    usePagination: true,
    usePaginationArrows: true,
    useItemsPerPage: true,
    defaultItemsPerPage: 10,
    defaultItemsPerPageOptions: [5, 10, 25, 50],
  },
  images: {
    top: [
      {
        src: 'https://i.imgur.com/lgtG4zB.png',
        // href: 'https://my-url.de'(optional)
        // target: ['_self' | '_blank'] (optional)
        description: 'Logo data.europa.eu',
        height: '60px',
        width: 'auto',
      },
    ],
    bottom: [],
  },
  datasets: {
    upload: {
      availableCategories: [
        'tran',
        'heal',
        'gove',
        'envi',
        'intr',
        'agri',
        'soci',
        'econ',
        'educ',
        'ener',
        'tech',
        'regi',
        'just',
      ],
    },
    facets: {
      cutoff: 5,
      showClearButton: true,
      showFacetsTitle: true, // Title on top of the facets
      useDatasetFacets: true, // Enable / Disable the facets on the Datasets page
      useDatasetFacetsMap: false, // Enable / Disable the map on the Datasets page
      //defaultFacetOrder: ['publisher', 'format', 'catalog', 'categories', 'keywords', 'dataScope', 'country', 'dataServices', 'scoring', 'license'],
      defaultFacetOrder: ['publisher', 'format', 'catalog', 'categories', 'keywords', 'dataScope', 'country', 'scoring', 'license'],
      scoringFacets: {
        useScoringFacets: true, // Enable / Disable the scoring facets
        defaultMinScore: 0, // Set the default mininimum score, the value should be one of the below listed minScores
        defaultScoringFacets: { // Set the properties of the scoring facets
          excellentScoring: {
            id: 'excellentScoring',
            title: 'Excellent',
            count: 0,
            minScoring: 351,
            maxScoring: 405,
          },
          goodScoring: {
            id: 'goodScoring',
            title: 'Good',
            count: 0,
            minScoring: 221,
            maxScoring: 350,
          },
          sufficientScoring: {
            id: 'sufficientScoring',
            title: 'Sufficient',
            count: 0,
            minScoring: 121,
            maxScoring: 220,
          },
          badScoring: {
            id: 'badScoring',
            title: 'Any',
            count: 0,
            minScoring: 0,
            maxScoring: 120,
          },
        },
      },
      // Set the minimum amount of Dataset facet items to be visible if collapsed
      MIN_FACET_LIMIT: 10,
      // Set the maximum amount of Dataset facet items to be visible, overflowing facets will not be shown!!!
      MAX_FACET_LIMIT: 50,
      FACET_OPERATORS: Object.freeze({ or: 'OR', and: 'AND' }),
      FACET_GROUP_OPERATORS: Object.freeze({ or: 'OR', and: 'AND' }),
    },
    // Set the relation of the DatasetDetails keyword links (e.g. rel="nofollow")
    followKeywordLinks: 'nofollow',
    // Maximum length for a keyword before being truncated
    maxKeywordLength: 15,
    // Enable / Disable the sort dropdown button on the Datasets page
    useSort: true,
    // Enable / Disable the RSS & Atom feed dropdown button on the Datasets page
    useFeed: true,
    // Enable / Disable the catalogs button on the Datasets page
    useCatalogs: true,
  },
  catalogs: {
    facets: {
      cutoff: 5,
      showClearButton: true,
      showFacetsTitle: true, // Title on top of the facets
      // Enable / Disable the facets on the Catalogues page
      useCatalogFacets: true,
      defaultFacetOrder: ['country'],
      // Set the minimum amount of Catalog facet items to be visible if collapsed
      MIN_FACET_LIMIT: 50,
      // Set the maximum amount of Catalog facet items to be visible, overflowing facets will not be shown!!!
      MAX_FACET_LIMIT: 100,
      FACET_OPERATORS: Object.freeze({ or: 'OR', and: 'AND' }),
      FACET_GROUP_OPERATORS: Object.freeze({ or: 'OR', and: 'AND' }),
    },
    // Enable / Disable the sort dropdown button on the Catalogues page
    useSort: true,
    // Use this option to achieve a more generic catalog page
    // If set to true, catalogs will be based on countries and therefore look for a "catalog.country.id" value to compute, which country flag to be used
    // If set to false, catalogs will not be based on countries and therefore look for a "catalog.id" value to compute, which catalog image to be used
    useCatalogCountries: true,
    // Set the default path to the catalog images (ROOT = "/src/assets/img")
    // If "useCatalogCountries" is set to true, this value should be equal to "/flags"
    // If "useCatalogCountries" is set to false, this value can be either:
    //    - an empty string to indicate, that the catalog images can be found inside "/src/assets/img" or
    //    - any directory name inside "/src/assets/img" starting with a "/"
    defaultCatalogImagePath: '/flags',
    // Set the default catalog.country.id of a catalog if not available, only applicable if "useCatalogCountries" is set to true
    // Country flags can be stored inside the "/flags" directory like "/src/assets/img/flags/<catalog.country.id>.png" with their filenames being equal to their catalog.country.id
    defaultCatalogCountryID: 'eu',
    // Set the default catalog.id of a catalog if not available, only applicable if "useCatalogCountries" is set to false
    // Catalog images can be stored inside any directory in "/src/assets/img/" like "/src/assets/img/catalogs/<catalog.id>.png" with their filenames being equal to their catalog.id
    defaultCatalogID: 'european-union-open-data-portal',
  },
  datasetDetails: {
    header: {
      navigation: "below", // "top", "below"
      hidePublisher: true,
      hideDate: true
    },
    keywords: {
      showTitle: true
    },
    description: {
      // If true, parses dataset description as Markdown formatted text content.
      enableMarkdownInterpretation: true,
    },
    // Configurations that changes the way distributions are displayed.
    distributions: {
      // If true, always display all distributions
      displayAll: false,
      // Number of first distributions to be shown before more need to be loaded.
      // Has no effect if displayAll is set to true.
      displayCount: 7,
      // Number of increment steps to be shown.
      // Has no effect if displayAll is set to true.
      incrementSteps: [10, 50],
      descriptionMaxLines: 3,
      descriptionMaxChars: 250,
      showValidationButton: false,
    },
    pages: {
      isVisible: false,
      // If true, always display all pages
      displayAll: false,
      // Number of first pages to be shown before more need to be loaded.
      // Has no effect if displayAll is set to true.
      displayCount: 7,
      // Number of increment steps to be shown.
      // Has no effect if displayAll is set to true.
      incrementSteps: [10, 50],
      descriptionMaxLines: 3,
      descriptionMaxChars: 250,
    },
    visualisations: {
      isVisible: false,
      // If true, always display all visualisations
      displayAll: false,
      // Number of first visualisations to be shown before more need to be loaded.
      // Has no effect if displayAll is set to true.
      displayCount: 7,
      // Number of increment steps to be shown.
      // Has no effect if displayAll is set to true.
      incrementSteps: [10, 50],
      descriptionMaxLines: 3,
      descriptionMaxChars: 250,
    },
    dataServices: {
      isVisible: false,
      // If true, always display all pages
      displayAll: false,
      // Number of first pages to be shown before more need to be loaded.
      // Has no effect if displayAll is set to true.
      displayCount: 7,
      // Number of increment steps to be shown.
      // Has no effect if displayAll is set to true.
      incrementSteps: [10, 50],
      descriptionMaxLines: 3,
      descriptionMaxChars: 250,
    },
    isUsedBy: {
      isVisible: false,
    },
    relatedResources: {
      isVisible: false,
    },
    bulkDownload: {
      buttonPosition: "bottom", // bottom or top
      // Maximum length for a file title before being truncated
      MAX_FILE_TITLE_LENGTH: 80,
      // Maximum parallel axios requests
      MAX_REQUESTS_COUNT: 5,
      // An interval which checks if PENDING_REQUESTS < MAX_REQUESTS_COUNT
      INTERVAL_MS: 10,
      // Timeout for axios request
      TIMEOUT_MS: 10000,
    },
    quality: {
      // If true, always display all distributions
      displayAll: false,
      // Number of distributions to be shown
      numberOfDisplayedQualityDistributions: 5,
      // CSV Linter Validation Results
      csvLinter: {
        // If true, always display all results
        displayAll: false,
        // Number of results to be shown
        numberOfDisplayedValidationResults: 5,
      },
    }
  },
  // Leaflet map configuration
  maps: {
    mapVisible: true,
    useAnimation: true,
    urlTemplate: 'https://gisco-services.ec.europa.eu/maps/wmts/1.0.0/WMTSCapabilities.xml/wmts/OSMCartoComposite/EPSG3857/{z}/{x}/{y}.png',
    options: {
      id: 'mapbox/streets-v11',
      accessToken: 'pk.eyJ1IjoiZmFiaWFwZmVsa2VybiIsImEiOiJja2x3MzlvZ3UwNG85MnBseXJ6aGI2MHdkIn0.bFs2g4bPMYULlvDSVsetJg',
      attribution: '&copy; <a href="https://ec.europa.eu/eurostat/web/gisco/">Eurostat - GISCO</a>',
    },
    geoBoundsId: 'ds-search-bounds',
    // MapBasic
    location: [[52.526, 13.314], 10],
    spatialType: 'Point',
    height: '400px',
    width: '100%',
    mapContainerId: 'mapid',
    mapStyle: {
      color: 'red',
      fillColor: 'red',
      fillOpacity: 0.5,
      weight: 2,
      radius: 1,
    },
    // MapBoundsSender
    sender: {
      startBounds: [[34.5970, -9.8437], [71.4691, 41.4843]],
      height: '200px',
      width: '100%',
      mapContainerId: 'modalMap',
    },
    // MapBoundsReceiver
    receiver: {
      startBounds: [[34.5970, -9.8437], [71.4691, 41.4843]],
      height: '250px',
      width: '100%',
      mapContainerId: 'mapid',
      attributionPosition: 'topright',
    },
  },
  upload: {
    useUpload: true,
    buttons: {
      Dataset: true,
      Catalogue: false,
    },
    basePath: '/dpi',
  },
  doiRegistrationService: {
    // Can be either 'eu-ra-doi' or 'mock'
    persistentIdentifierType: 'eu-ra-doi',
  }
};

export { glueConfig, i18n };

