/* eslint-disable no-console */
/**
 * @author Dennis ritter
 * @description
 */

 import axios from 'axios';
 import { has, isNil, isArray } from 'lodash';

 import dataGetters from './getters/data-getters';

 const getResponseData = (dataset) => {
   const ds = {};
   ds.accessRights = dataGetters.getObject(dataset, 'access_right', ['label', 'resource']);
   ds.accrualPeriodicity = dataGetters.getObject(dataset, 'accrual_periodicity', ['resource', 'label']);
   ds.admsIdentifiers = dataGetters.getArrayOfObjects(dataset, 'adms_identifier', ['identifier', 'schema', 'resource']);
   ds.attributes = dataGetters.getArrayOfStrings(dataset, 'attribute');
   ds.catalog = dataGetters.getObject(dataset, 'catalog', ['id', 'title', 'description', 'country', 'homepage', 'issued', 'modified', 'language', 'license', 'publisher', 'spatial', 'spatial_resource']);
   ds.catalogRecord = dataGetters.getObject(dataset, 'catalog_record', ['issued', 'modified']);
   ds.categories = dataGetters.getArrayOfObjects(dataset, 'categories', ['id', 'label']);
   ds.conformsTo = dataGetters.getArrayOfObjects(dataset, 'conforms_to', ['label', 'resource']);
   ds.contactPoints = dataGetters.getArrayOfObjects(dataset, 'contact_point', ['name', 'type', 'resource', 'email', 'telephone', 'address', 'url']);
   ds.country = dataGetters.getObject(dataset, 'country', [{ key: 'id', default: 'eu' }, { key: 'title', default: 'European Union' }]);
   ds.creator = dataGetters.getObject(dataset, 'creator', ['name', 'type', 'email', 'resource', 'homepage']);
   ds.deadline = dataGetters.getString(dataset, 'deadline');
   ds.description = dataGetters.getObjectLanguage(dataset, 'description', 'No description available');
   ds.dimensions = dataGetters.getArrayOfStrings(dataset, 'dimension');
   ds.distributions = [];
   ds.distributionFormats = [];
   ds.documentations = dataGetters.getArrayOfStrings(dataset, 'documentations');
   ds.extendetMetadata = dataGetters.getObject(dataset, 'extended_metadata', ['is_used_by']);
   ds.frequency = dataGetters.getObject(dataset, 'frequency', ['title', 'resource']);
   ds.geocodingDescription = dataGetters.getObject(dataset, 'geocoding_description', ['id', 'label']);
   ds.hasQualityAnnotations = dataGetters.getArrayOfStrings(dataset, 'has_quality_annotation');
   ds.hasVersion = dataGetters.getArrayOfStrings(dataset, 'has_version');
   ds.id = dataGetters.getString(dataset, 'id');
   ds.identifiers = dataGetters.getArrayOfStrings(dataset, 'identifier');
   ds.idName = dataGetters.getString(dataset, 'idName');
   ds.isReferencedBy = dataGetters.getArrayOfStrings(dataset, 'is_referenced_by');
   ds.isVersionOf = dataGetters.getArrayOfStrings(dataset, 'is_version_of');
   ds.keywords = dataGetters.getArrayOfObjects(dataset, 'keywords', ['id', 'label', 'language']);
   ds.landingPages = dataGetters.getArrayOfObjects(dataset, 'landing_page', ['format', 'description', 'title', 'resource']);
   ds.languages = dataGetters.getArrayOfObjects(dataset, 'language', ['id', 'label', 'resource']);
   ds.licences = [];
   ds.modificationDate = dataGetters.getString(dataset, 'modified');
   ds.numSeries = dataGetters.getNumber(dataset, 'num_series');
   ds.originalLanguage = dataGetters.getOriginalLanguage(dataset);
   ds.otherIdentifiers = dataGetters.getArrayOfObjects(dataset, 'adms_identifier', ['identifier', 'scheme', 'resource']);
   ds.pages = dataGetters.getArrayOfObjects(dataset, 'page', ['format', 'description', 'title', 'resource']);
   ds.provenances = dataGetters.getArrayOfObjects(dataset, 'provenance', ['label', 'resource']);
   ds.publisher = dataGetters.getObject(dataset, 'publisher', ['name', 'type', 'email', 'resource', 'homepage']);
   ds.qualifiedAttributions = dataGetters.getArrayOfStrings(dataset, 'qualified_attribution');
   ds.qualifiedRelations = dataGetters.getArrayOfObjects(dataset, 'qualified_relation', ['relation', 'had_role']);
   ds.relations = dataGetters.getArrayOfStrings(dataset, 'relation');
   ds.relatedResources = dataGetters.getArrayOfStrings(dataset, 'related_resources');
   ds.releaseDate = dataGetters.getString(dataset, 'issued');
   ds.sample = dataGetters.getArrayOfStrings(dataset, 'sample');
   ds.sources = dataGetters.getArrayOfStrings(dataset, 'source');
   ds.spatial = dataGetters.getArrayOfObjects(dataset, 'spatial', ['type', 'coordinates']);
   ds.spatialResolutionInMeters = dataGetters.getArrayOfNumbers(dataset, 'spatial_resolution_in_meters');
   ds.spatialResource = dataGetters.getArrayOfObjects(dataset, 'spatial_resource', ['label', 'resource']);
   ds.statUnitMeasures = dataGetters.getArrayOfStrings(dataset, 'stat_unit_measure');
   ds.subject = dataGetters.getArrayOfObjects(dataset, 'subject', ['resource', 'label']);
   ds.temporal = dataGetters.getArrayOfObjects(dataset, 'temporal', ['gte', 'lte']);
   ds.temporalResolution = dataGetters.getArrayOfStrings(dataset, 'temporal_resolution');
   ds.theme = dataGetters.getArrayOfObjects(dataset, 'categories', ['id', 'label', 'resource']);
   ds.translationMetaData = dataGetters.getTranslationMetaData(dataset);
   ds.title = dataGetters.getObjectLanguage(dataset, 'title', 'No title available');
   ds.type = dataGetters.getObject(dataset, 'type', ['id', 'label', 'resource']);
   ds.resource = dataGetters.getString(dataset, 'resource');
   ds.versionInfo = dataGetters.getString(dataset, 'version_info');
   ds.versionNotes = dataGetters.getObjectLanguage(dataset, 'version_notes');
   ds.visualisations = [];
   ds.wasGeneratedBy = dataGetters.getArrayOfStrings(dataset, 'was_generated_by');
   for (const dist of dataGetters.getDistributions(dataset)) {
     const distribution = {};
     distribution.accessUrl = dataGetters.getArrayOfStrings(dist, 'access_url');
     distribution.accessService = dataGetters.getArrayOfObjects(dist, 'access_service', ['title', 'description', 'endpoint_url']);
     // distribution.accessService = dataGetters.getArrayOfStrings(dist, 'access_service');
     distribution.availability = dataGetters.getObject(dist, 'availability', ['label', 'resource']);
     distribution.byteSize = dataGetters.getNumber(dist, 'byte_size');
     distribution.checksum = dataGetters.getObject(dist, 'checksum', ['algorithm', 'checksum_value']);
     distribution.compressFormat = dataGetters.getString(dist, 'compress_format');
     distribution.conformsTo = dataGetters.getArrayOfObjects(dist, 'conforms_to', ['label', 'resource']);
     distribution.description = dataGetters.getObjectLanguage(dist, 'description', 'No description available');
     distribution.downloadUrls = dataGetters.getArrayOfStrings(dist, 'download_url');
     distribution.format = dataGetters.getObject(dist, 'format', ['id', 'resource', { key: 'label', default: 'UNKNOWN' }]);
     distribution.hasPolicy = dataGetters.getString(dist, 'has_policy');
     distribution.id = dataGetters.getString(dist, 'id');
     distribution.languages = dataGetters.getArrayOfObjects(dist, 'language', ['id', 'label', 'resource']);
     distribution.licence = dataGetters.getObject(dist, 'license', ['id', 'label', 'description', 'resource', 'la_url']);
     distribution.mediaType = dataGetters.getString(dist, 'media_type');
     distribution.modificationDate = dataGetters.getString(dist, 'modified');
     distribution.packageFormat = dataGetters.getString(dist, 'package_format');
     distribution.pages = dataGetters.getArrayOfObjects(dist, 'page', ['format', 'title', 'description', 'resource']);
     distribution.releaseDate = dataGetters.getString(dist, 'issued');
     distribution.rights = dataGetters.getObject(dist, 'rights', ['label', 'resource']);
     distribution.spatialResolutionInMeters = dataGetters.getArrayOfNumbers(dist, 'spatial_resolution_in_meters');
     distribution.status = dataGetters.getObject(dist, 'status', ['label', 'resource']);
     distribution.temporalResolution = dataGetters.getArrayOfStrings(dist, 'temporal_resolution');
     distribution.title = dataGetters.getObjectLanguage(dist, 'title', 'No title available');
     distribution.type = dataGetters.getObject(dist, 'type', ['label', 'resource']);
     // Check type of ditribution
     if (distribution.type.resource === 'http://publications.europa.eu/resource/authority/distribution-type/VISUALIZATION') {
       ds.visualisations.push(distribution);
     } else {
       ds.distributions.push(distribution);
     }
     ds.distributionFormats.push(distribution.format);
     ds.licences.push(distribution.licence);
   }
   return ds;
 };

 const checkBounds = (bounds) => {
   try {
     let gb = bounds;
     if (isNil(gb)) return undefined;
     // If gb is a string, split to array
     if (!isArray(gb) && typeof gb === 'string') gb = gb.split(',');
     // Check number of elements. we need 4 for latMin, LonMin, LatMax, LonMax
     if (isArray(gb) && gb.length === 4) {
       for (const coord of gb) {
         // Check if items are floats
         if (!parseFloat(coord)) return undefined;
         return gb;
       }
     }
     return undefined;
   } catch (error) {
     console.error(error.message);
     return undefined;
   }
 };

 export default class Datasets {
   constructor(baseUrl, similarityBaseUrl, defaultScoringFacets, qualityBaseUrl, hubUrl) {
     this.baseUrl = baseUrl;
     this.similarityBaseUrl = similarityBaseUrl;
     this.defaultScoringFacets = defaultScoringFacets;
     this.qualityBaseUrl = qualityBaseUrl;
     this.hubUrl = hubUrl;
   }

   /**
      * @description GET dataset by given id.
      * @param id
      */
   getSingle(id) {
     return new Promise((resolve, reject) => {
       const endpoint = 'datasets';
       const reqStr = `${this.baseUrl}${endpoint}/${id}`;
       axios.get(reqStr, {
         params: {},
       })
         .then((response) => {
           const dataset = response.data.result;
           let ds = {};
           try {
             ds = getResponseData(dataset);
           } catch (error) {
             console.warn('Error in datasets.js while checking response:', error.message);
             console.error(error.stack);
           }
           resolve(ds);
         })
         .catch((error) => {
           reject(error);
         });
     });
   }

   /**
      * @description GET all datasets matching the given criteria.
      * @param q
      * @param locale
      * @param facets
      * @param geoBounds
      * @param minScoring
      * @param dataScope
      * @returns {Promise}
      */
   get(q, locale, limit, page = 0, sort = `relevance+desc, modified+desc, title.${locale}+asc`, facetOperator = 'AND', facetGroupOperator = 'AND', dataServices = 'false', facets, geoBounds, minScoring = 0, dataScope) {
     // The request parameters
     const params = {
       q,
       filter: 'dataset',
       limit,
       page: page - 1,
       sort,
       facetOperator,
       facetGroupOperator,
       dataServices,
       includes: `id,title.${locale},description.${locale},languages,modified,issued,catalog.id,catalog.title,catalog.country.id,distributions.id,distributions.format.label,distributions.format.id`,
       facets,
     };

     // Check if minScoring is set
     if (minScoring > 0) params.minScoring = minScoring;

     // Check if dataScope is set and then modify params to fullfil the corresponding data scope criterias
     if (!isNil(dataScope)) {
       // Set countryData param to true if Country data is requested
       params.countryData = dataScope === 'countryData';
       // Set country facets if EU or International data is requested
       if (!params.countryData) {
         params.facets.country = [];
         params.facets.country.push(dataScope);
       }
     }

     // Add geoBounds parameters if the bounds are valid
     const bounds = checkBounds(geoBounds);
     if (!isNil(bounds)) {
       params.bboxMinLat = bounds[0];
       params.bboxMaxLat = bounds[2];
       params.bboxMinLon = bounds[1];
       params.bboxMaxLon = bounds[3];
     }

     return new Promise((resolve, reject) => {
       const endpoint = 'search';
       const reqStr = `${this.baseUrl}${endpoint}`;
       axios.get(reqStr, {
         params,
       })
         .then((response) => {
           if (!has(response.data, 'result')) {
             console.warn('Error in datasets.js while checking response');
             return reject(new Error('Empty Response Data'));
           }
           /**
            * @property availableFacets
            * @type {availableFacets: Array, datasetsCount, datasets: Array}
            * @description The set union of all available facets for the .
            */
           const resData = {
             availableFacets: [],
             scoringCount: {},
             datasetsCount: response.data.result.count,
             datasets: [],
           };

           // Transform fetched facets
           for (const field of response.data.result.facets) {
             // Check for required field keys
             if (has(field, 'id') && has(field, 'title') && has(field, 'items')) {
               const items = [];
               for (const facet of field.items) {
                 const item = {};
                 // Check for required facet/item keys
                 if (has(facet, 'id') && has(facet, 'title') && has(facet, 'count')) {
                   item.id = facet.id;
                   item.title = facet.title;
                   item.count = facet.count;
                 }
                 // Handle Scoring Facets
                 if (has(facet, 'from') && has(facet, 'to')) {
                   const currentScoringFacet = this.defaultScoringFacets[facet.id];
                   item.minScoring = facet.from;
                   item.maxScoring = facet.to;

                   // Use config values to overwrite the default values form the backend
                   if (currentScoringFacet.title) item.title = currentScoringFacet.title;
                   if (currentScoringFacet.minScoring) item.minScoring = currentScoringFacet.minScoring;
                   if (currentScoringFacet.maxScoring) item.maxScoring = currentScoringFacet.maxScoring;
                 }
                 items.push(item);
               }
               // Add to response array
               resData.availableFacets.push({
                 id: field.id,
                 title: field.title,
                 items,
               });
             }
           }

           // Transform Datasets Data model
           const datasets = response.data.result.results;

           for (const dataset of datasets) {
             let ds = {};
             try {
               ds = getResponseData(dataset);
             } catch (error) {
               console.warn('Error in datasets.js while checking response:', error.message);
               console.error(error.stack);
             }
             resData.datasets.push(ds);
           }
           return resolve(resData);
         })
         .catch((error) => {
           console.error(error);
           reject(error);
         });
     });
   }

   /**
   * @description Get similar datasets to the dataset represented by the provided id.
   * @param id {int} The dataset id to get similar datasets for.
   */
   getSimilarDatasets(id) {
     return new Promise((resolve, reject) => {
       const params = {
         limit: 20,
       };
       const endpoint = 'similarity';
       const reqStr = `${this.similarityBaseUrl}${endpoint}/${id}`;
       axios.get(reqStr, {
         params,
       })
         .then((response) => {
           resolve(response);
         })
         .catch((error) => {
           reject(error);
         });
     });
   }

   getQualityData(id) {
     return new Promise((resolve, reject) => {
       const endpoint = 'datasets';
       const reqStr = `${this.qualityBaseUrl}${endpoint}/${id} `;
       axios.get(reqStr, {
         timeout: 30000,
       })
         .then((response) => {
           resolve(response);
         })
         .catch((error) => {
           reject(error);
         });
     });
   }

   getQualityDistributionData(id) {
     return new Promise((resolve, reject) => {
       const endpoint = 'distributions';
       const reqStr = `${this.qualityBaseUrl}${endpoint}/${id} `;
       axios.get(reqStr, {
         timeout: 30000,
       })
         .then((response) => {
           resolve(response);
         })
         .catch((error) => {
           reject(error);
         });
     });
   }

   getDQVDataHead(id, format, locale) {
     return new Promise((resolve, reject) => {
       const reqStr = `${this.hubUrl}metrics/${id}.${format}?useNormalizedId=true&locale=${locale}`;
       axios.head(reqStr, {
       })
         .then((response) => {
           resolve(response);
         })
         .catch((error) => {
           reject(error);
         });
     });
   }

   /**
   * @description Autocomplete the given query.
   * @param q {String} The Query to autocomplete.
   */
   autocomplete(q) {
     return new Promise((resolve, reject) => {
       const endpoint = 'autocomplete';
       const reqStr = `${this.baseUrl}${endpoint}`;
       axios.get(reqStr, {
         params: {
           q,
         },
       })
         .then((response) => {
           resolve(response);
         })
         .catch((err) => {
           reject(err);
         });
     });
   }
 }
