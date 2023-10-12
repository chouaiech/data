/**
 * The user-config that is used for testing purposes.
 */

 import merge from 'merge-anything'

 import { glueConfig as baseConfig, i18n } from './user-config.sample'

 const customConfig = {
   api: {
     baseUrl: 'http://cypress-stubbed.api.base-url/',
     qualityBaseUrl: 'http://cypress-stubbed.api.quality-base-url/',
     similarityBaseUrl: 'https://data.europa.eu/api/similarities/',
     gazetteerBaseUrl: 'https://data.europa.eu/api/hub/search/gazetteer/',
     hubUrl: 'http://cypress-stubbed.api-hub-url/',
     catalogBaseUrl: 'https://europeandataportal.eu/',
     authToken: '',
     vueAppCorsproxyApiUrl: 'https://piveau-corsproxy-piveau.apps.osc.fokus.fraunhofer.de',
     sparqlUrl: 'https://data.europa.eu/sparql',
   },
   tracker: {
     // Matomo/PiwikPro analytics config
     // If true, uses PiwikPro, if false, uses Matomo
     isPiwikPro: true,
     siteId: '__cypress__',
     trackerUrl: 'http://localhost/'
   },
   useAuthService: true,
   keycloak: {
    enableLogin: true,
    realm: 'piveau',
    url: 'http://cypress-stubbed.keycloak.url/auth',
    'ssl-required': 'external',
    clientId: 'piveau-hub-ui',
    'public-client': true,
    'verify-token-audience': true,
    'use-resource-role-mappings': true,
    'confidential-port': 0,
  },
 }

 const glueConfig = merge(baseConfig, customConfig)

 export {
   glueConfig,
   i18n,
 };
