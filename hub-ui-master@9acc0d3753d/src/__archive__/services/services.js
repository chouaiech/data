/* eslint-disable */
/**
 * @author Dennis ritter
 * @description Register services and constants in the injector.
 */

// import vue-inject
import injector from 'vue-inject';

// Import glue-config.js
import { glueConfig as GLUE_CONFIG } from '../../config/user-config';

export default (envConfig) => {
  injector.constant('baseUrl', envConfig.api.baseUrl);
  injector.constant('qualityBaseUrl', envConfig.api.qualityBaseUrl);
  injector.constant('similarityBaseUrl', envConfig.api.similarityBaseUrl);
  injector.constant('gazetteerBaseUrl', envConfig.api.gazetteerBaseUrl);
  injector.constant('hubUrl', envConfig.api.hubUrl);
  injector.constant('keycloak', envConfig.keycloak);
  injector.constant('rtp', envConfig.rtp);
  injector.constant('useAuthService', envConfig.useAuthService);
  injector.constant('authToken', envConfig.api.authToken);
  injector.constant('defaultScoringFacets', envConfig.datasets.facets.scoringFacets.defaultScoringFacets);
  injector.service('DatasetService', ['baseUrl', 'similarityBaseUrl', 'defaultScoringFacets', 'qualityBaseUrl', 'hubUrl'], GLUE_CONFIG.services.datasetService);
  injector.service('catalogService', ['baseUrl'], GLUE_CONFIG.services.catalogService);
  if (GLUE_CONFIG.services.mapService) injector.service('MapService', ['baseUrl'], GLUE_CONFIG.services.mapService);
  if (GLUE_CONFIG.services.datastoreService) injector.service('DatastoreService', ['baseUrl'], GLUE_CONFIG.services.datastoreService);
  if (GLUE_CONFIG.services.gazetteerService) injector.service('GazetteerService', ['gazetteerBaseUrl'], GLUE_CONFIG.services.gazetteerService);
  if (GLUE_CONFIG.services.uploadService) injector.service('uploadService', ['hubUrl', 'authToken'], GLUE_CONFIG.services.uploadService);
  if (GLUE_CONFIG.services.authService) injector.service('authService', ['keycloak', 'rtp', 'useAuthService'], GLUE_CONFIG.services.authService);
  if (GLUE_CONFIG.services.jsonldService) injector.service('jsonldService', GLUE_CONFIG.services.jsonldService);
};
