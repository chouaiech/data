/* eslint-disable no-param-reassign,no-console */
/**
 * @Publisher Dennis Ritter
 * @description Vuex store for the the details of a dataset.
 */
 import Vue from 'vue';
 import Vuex from 'vuex';

 import {
   has,
   isObject,
   isArray,
 } from 'lodash';

 import { helpers } from '@piveau/piveau-hub-ui-modules';
 const { mirrorPropertyFn } = helpers;

 // The helper functions below stabilize the store against API changes without changing everything
 // throughout the whole project.
 // See https://gitlab.fokus.fraunhofer.de/piveau/organisation/piveau-scrum-board/-/issues/1761
 // TODO: ideally, we want to get rid of using these functions, but they require significant effort

 // Example: mirrorLabelAsTitle({ label: "hello world" }) == { label: "hello world", title: "hello world" }
 const mirrorLabelAsTitle = mirrorPropertyFn('label', 'title');

 Vue.use(Vuex);

 // DatasetDetails Module State
 /**
  * @property dataset
  * @type JSON
  * @description A dataset object.
  * @example dataset = {
     *  accessRights: 'public',
     *  accrualPeriodicity: 'annual',
     *  catalog: { title: 'catalog One', description: 'This is catalog One.', id: 'catalog-1' },
     *  categories: [{ id: 'energy', title: 'Energy' }, {...}],
     *  conformsTo: [{ title: 'Title', resource: 'http://resource.eu' }, {...}],
     *  contactPoints: [{ name: 'Benedicto Rebanks', type: 'Individual', resource: 'http://demo-url-to-the-contact-point.com', email: 'brebanks0@posterous.com' }, {...}],
     *  country: { title: 'Germany', id: 'DE' },
     *  creator: { type : 'Agent', name : 'Fraunhofer FOKUS', email : 'mailto:info@fokus.fraunhofer.de', resource: 'http://resource.eu', homepage : 'http://www.fokus.fraunhofer.de' },
     *  description: { de: 'This is dataset1', en: 'This is dataset1' },
     *  distributions: [{
     *     accessUrl: 'http://demo-url-to-this-resource.org/demoID/accessPath',
     *     downloadUrl: 'http://demo-url-to-this-resource.org/demoID/filename.csv',
     *     description: 'A description of this distribution',
     *     format: 'csv',
     *     id: 'demoID',
     *     licence: '{ title: 'Licence One', id: 'licence-1', resource: 'https://demo-url-to-the-licence.com/licence-one' }',
     *     mediaType: 'text/plain',
     *     modificationDate: 2017-05-31T18:33:48.018695,
     *     releaseDate: 2017-05-31T18:33:48.018695,
     *     title: 'demoTitle',
     *     urlType: 'download',
     *   },
     *   {...},
     *  ],
     *  distributionFormats: [{ title: 'PDF', id: 'pdf' }, { title: 'CSV', id: 'csv' }, {...}],
     *  documentations: [...],
     *  frequency: { title: 'Frequency One', resource: 'http://demo-url-to-the-frequency.com' },
     *  hasVersion: [{ resource: 'https://piveau.eu/set/data/2222', id: '2222' }],
     *  id: 'abc123qwe345',
     *  identifiers: ['2222', 'dataset-1234', ...],
     *  idName: 'dataset-1',
     *  isVersionOf: [{ resource: 'https://piveau.eu/set/data/2222', id: '2222' }],
     *  keywords: [{ title: 'KEYWORD1', id: 'keyword1'}, {...}],
     *  landingPages: ['http://landingpage.de', ...],
     *  languages: ['http://publications.europa.eu/resource/authority/language/DEU', 'http://publications.europa.eu/resource/authority/language/ENG', ...],
     *  licences: [{ title: 'Licence One', id: 'licence-1', resource: 'https://asd.com/licence-one' }, {...}],
     *  modificationDate: '2002-02-02T00:00',
     *  originalLanguage: '...',
     *  otherIdentifiers: ['https://gnu.org/blandit/mi/in.xml', ...],
     *  pages: [{format: { title: 'HTML', id: 'HTML' }, description: { en: 'Placeholder description' }, title: { en: 'Placeholder title' }, resource: 'https://documentation-uri-placeholder', ...] or old data structure ['http://www.documentation.com', ...],
     *  provenances: [{ resource: 'https://diigo.com/cras/non/velit/nec/nisi.jpg', label: 'Label'}, {...}],
     *  publisher: { type : 'Agent', name : 'Fraunhofer FOKUS', email : 'mailto:info@fokus.fraunhofer.de', resource: 'http://resource.eu', homepage : 'http://www.fokus.fraunhofer.de' },
     *  relatedResources: ['https://bluehost.com/ac/est/lacinia/nisi/venenatis/tristique/fusce.xml', ...],
     *  releaseDate: '2001-01-01T00:00',
     *  similarDatasets: [{...}],
     *  sources: [{ resource: 'https://piveau.eu/set/data/2222', id: '2222' }],
     *  spatial: [{ coordinates: [52.526, 13.314], type: 'Point' }, {...}],
     *  spatialResource: ['http://publications.europa.eu/resource/authority/country/DEU', ...],
     *  temporal: [{ gte: '2015-06-09T00:00:00', lte: '2015-06-09T00:00:00'}, {...}],
     *  translationMetaData: {},
     *  title: { de: 'Der Titel', en: 'The Title' },
     *  versionInfo: '1.0.0',
     *  versionNotes: { en : 'Release', de: 'Veröffentlichung' },
     *  catalogRecord: { issued: "2021-08-03T13:52:11Z", modified: "2021-08-05T06:45:59Z" },
  * }
  */
 const state = {
   dataset: {
     accessRights: '',
     accrualPeriodicity: '',
     admsIdentifiers: [],
     attributes: [],
     catalog: {},
     categories: [{}],
     conformsTo: [{}],
     contactPoints: [{}],
     country: {},
     creator: {},
     deadline: '',
     description: {},
     dimensions: [],
     distributions: [{}],
     distributionFormats: [],
     documentations: [],
     frequency: {},
     geocodingDescription: {},
     hasQualityAnnotations: [],
     hasVersion: [{}],
     id: '',
     identifiers: [],
     idName: '',
     isReferencedBy: [],
     isVersionOf: [{}],
     keywords: [{}],
     landingPages: [],
     languages: [],
     licences: [],
     modificationDate: '',
     numSeries: 0,
     originalLanguage: '',
     otherIdentifiers: [],
     pages: [{}],
     provenances: [{}],
     publisher: {},
     qualifiedAttributions: [],
     qualifiedRelations: [],
     relations: [],
     relatedResources: [],
     releaseDate: '',
     resource: '',
     sample: [],
     similarDatasets: [{}],
     sources: [{}],
     spatial: [{}],
     spatialResource: [],
     spatialResolutionInMeters: [],
     statUnitMeasures: [],
     subject: [{}],
     temporal: [{}],
     temporalResolution: [],
     theme: [],
     translationMetaData: {},
     title: {},
     type: {},
     versionInfo: '',
     versionNotes: {},
     visualisations: [{}],
     wasGeneratedBy: [],
     qualityData: [''],
     qualityDistributionData: [''],
     isDQVDataRDFAvailable: false,
     catalogRecord: {},
     isUsedBy: {},
     extendetMetadata: {},
   },
   activeNavigationTab: 0,
   loading: false,
   service: null,
 };

 const getters = {
   getAccessRights: state => state.dataset.accessRights,
   getAccrualPeriodicity: state => state.dataset.accrualPeriodicity,
   getAdmsIdentifiers: state => state.dataset.admsIdentifiers,
   getAttributes: state => state.dataset.attributes,
   getCatalog: state => state.dataset.catalog,
   getCategories: state => (state.dataset.categories.length && state.dataset.categories.map(mirrorLabelAsTitle)) || [],
   getConformsTo: state => (state.dataset.conformsTo.length && state.dataset.conformsTo.map(mirrorLabelAsTitle)) || [],
   getContactPoints: state => state.dataset.contactPoints,
   getCountry: state => state.dataset.country,
   getCreator: state => state.dataset.creator,
   getDeadline: state => state.dataset.deadline,
   getDescription: state => state.dataset.description,
   getDimensions: state => state.dataset.dimensions,
   getDistributions: state => state.dataset.distributions,
   getDistributionFormats: state => state.dataset.distributionFormats,
   getDocumentations: state => state.dataset.documentations,
   getFrequency: state => state.dataset.frequency,
   getGeocodingDescription: state => state.dataset.geocodingDescription,
   getHasQualityAnnotations: state => state.dataset.hasQualityAnnotations,
   getHasVersion: state => state.dataset.hasVersion,
   getID: state => state.dataset.id,
   getIdentifiers: state => state.dataset.identifiers,
   getIdName: state => state.dataset.idName,
   getIsReferencedBy: state => state.dataset.isReferencedBy,
   getIsVersionOf: state => state.dataset.isVersionOf,
   getKeywords: state => (state.dataset.keywords.length && state.dataset.keywords.map(mirrorLabelAsTitle)) || [],
   getSubject: state => (state.dataset.subject.length && state.dataset.subject.map(mirrorLabelAsTitle)) || [],
   getLandingPages: state => state.dataset.landingPages,
   getLanguages: state => state.dataset.languages,
   getLicences: state => state.dataset.licences,
   getModificationDate: state => state.dataset.modificationDate,
   getNumSeries: state => state.dataset.numSeries,
   getOriginalLanguage: state => state.dataset.originalLanguage,
   getOtherIdentifiers: state => state.dataset.otherIdentifiers,
   getPages: state => state.dataset.pages,
   getProvenances: state => state.dataset.provenances,
   getPublisher: state => state.dataset.publisher,
   getQualifiedAttributions: state => state.dataset.qualifiedAttributions,
   getQualifiedRelations: state => state.dataset.qualifiedRelations,
   getRelations: state => state.dataset.relations,
   getRelatedResources: state => state.dataset.relatedResources,
   getReleaseDate: state => state.dataset.releaseDate,
   getSimilarDatasets: state => state.dataset.similarDatasets,
   getSample: state => state.dataset.sample,
   getSources: state => state.dataset.sources,
   getSpatial: state => state.dataset.spatial,
   getSpatialResource: state => (state.dataset.spatialResource.length && state.dataset.spatialResource.map(mirrorLabelAsTitle)) || [],
   getSpatialResolutionInMeters: state => state.dataset.spatialResolutionInMeters,
   getStatUnitMeasures: state => state.dataset.statUnitMeasures,
   getTheme: state => state.dataset.theme,
   getTemporal: state => state.dataset.temporal,
   getTemporalResolution: state => state.dataset.temporalResolution,
   getTranslationMetaData: state => state.dataset.translationMetaData,
   getTitle: state => state.dataset.title,
   getType: state => state.dataset.type,
   getResource: state => state.dataset.resource,
   getVersionInfo: state => state.dataset.versionInfo,
   getVersionNotes: state => state.dataset.versionNotes,
   getVisualisations: state => state.dataset.visualisations,
   getWasGeneratedBy: state => state.dataset.wasGeneratedBy,
   getLoading: state => state.loading,
   getService: state => state.service,
   getQualityData: state => state.dataset.qualityData,
   getQualityDistributionData: state => state.dataset.qualityDistributionData,
   getIsDQVDataRDFAvailable: state => state.dataset.isDQVDataRDFAvailable,
   getCatalogRecord: state => state.dataset.catalogRecord,
   getExtendedMetadata: state => state.dataset.extendetMetadata,
 };

 const actions = {
   /**
    * @description Loads details for the dataset with the given ID.
    * @param commit
    * @param state
    * @param id {String} The dataset ID.
    */
   loadDatasetDetails({ state, commit }, id) {
     commit('SET_LOADING', true);
     return new Promise((resolve, reject) => {
       commit('SET_ID', id);
       const service = getters.getService(state);
       service.getSingle(id)
         .then((response) => {
           commit('SET_ACCESS_RIGHTS', response.accessRights);
           commit('SET_ACCRUAL_PERIODICITY', response.accrualPeriodicity);
           commit('SET_ATTRIBUTES', response.attributes);
           commit('SET_catalog', response.catalog);
           commit('SET_CATEGORIES', response.categories);
           commit('SET_CONFORMS_TO', response.conformsTo);
           commit('SET_CONTACT_POINTS', response.contactPoints);
           commit('SET_COUNTRY', response.country);
           commit('SET_CREATOR', response.creator);
           commit('SET_DESCRIPTION', response.description);
           commit('SET_DIMENSIONS', response.dimensions);
           commit('SET_DISTRIBUTIONS', response.distributions);
           commit('SET_DISTRIBUTION_FORMATS', response.distributionFormats);
           commit('SET_DOCUMENTATIONS', response.documentations);
           commit('SET_FREQUENCY', response.frequency);
           commit('SET_HAS_QUALITY_ANNOTATIONS', response.hasQualityAnnotations);
           commit('SET_HAS_VERSION', response.hasVersion);
           commit('SET_IDENTIFIERS', response.identifiers);
           commit('SET_ID_NAME', response.idName);
           commit('SET_IS_REFERENCED_BY', response.isReferencedBy);
           commit('SET_IS_VERSION_OF', response.isVersionOf);
           commit('SET_KEYWORDS', response.keywords);
           commit('SET_LANDING_PAGES', response.landingPages);
           commit('SET_LANGUAGES', response.languages);
           commit('SET_LICENCES', response.licences);
           commit('SET_MODIFICATION_DATE', response.modificationDate);
           commit('SET_NUM_SERIES', response.numSeries);
           commit('SET_ORIGINAL_LANGUAGE', response.originalLanguage);
           commit('SET_OTHER_IDENTIFIERS', response.otherIdentifiers);
           commit('SET_PAGES', response.pages);
           commit('SET_PROVENANCES', response.provenances);
           commit('SET_PUBLISHER', response.publisher);
           commit('SET_RELATED_RESOURCES', response.relatedResources);
           commit('SET_RELEASE_DATE', response.releaseDate);
           commit('SET_RESOURCE', response.resource);
           commit('SET_SOURCES', response.sources);
           commit('SET_SPATIAL', response.spatial);
           commit('SET_SPATIAL_RESOURCE', response.spatialResource);
           commit('SET_STAT_UNIT_MEASURES', response.statUnitMeasures);
           commit('SET_TEMPORAL', response.temporal);
           commit('SET_TRANSLATION_META_DATA', response.translationMetaData);
           commit('SET_TITLE', response.title);
           commit('SET_VERSION_INFO', response.versionInfo);
           commit('SET_VERSION_NOTES', response.versionNotes);
           commit('SET_VISUALISATIONS', response.visualisations);
           commit('SET_WAS_GENERATED_BY', response.wasGeneratedBy);
           commit('SET_CATALOG_RECORD', response.catalogRecord);
           commit('SET_ADMS_IDENTIFIERS', response.admsIdentifiers);
           commit('SET_DEADLINE', response.deadline);
           commit('SET_GEOCODING_DESCRIPTION', response.geocodingDescription);
           commit('SET_QUALIFIED_ATTRIBUTIONS', response.qualifiedAttributions);
           commit('SET_QUALIFIED_RELATIONS', response.qualifiedRelations);
           commit('SET_RELATIONS', response.relations);
           commit('SET_SAMPLE', response.sample);
           commit('SET_SPATIAL_RESOLUTION_IN_METERS', response.spatialResolutionInMeters);
           commit('SET_SUBJECT', response.subject);
           commit('SET_TEMPORAL_RESOLUTION', response.temporalResolution);
           commit('SET_THEME', response.theme);
           commit('SET_TYPE', response.type);
           commit('SET_EXTENDET_METADATA', response.extendetMetadata);
           commit('SET_LOADING', false);
           resolve();
         })
         .catch((err) => {
           console.error(err);
           commit('SET_LOADING', false);
           reject(err);
         });
     });
   },
   /**
    * @description Loads details for the dataset with the given ID. But unlike loaddatasetDetails,
    * the Mutations after the request differ because this function is meant to fetch details for similar datasets of another dataset.
    * @param commit
    * @param state
    * @param id {String} The dataset ID.
    */
   loadSimilarDatasetDetails({ state, commit }, id) {
     commit('SET_LOADING', true);
     return new Promise((resolve, reject) => {
       const service = getters.getService(state);
       service.getSingle(id)
         .then((response) => {
           commit('SET_SD_DESCRIPTION', { id, description: response.description });
           commit('SET_SD_TITLE', { id, title: response.title });
           commit('SET_LOADING', false);
           resolve(response);
         })
         .catch((err) => {
           console.error(err);
           commit('SET_LOADING', false);
           reject(err);
         });
     });
   },
   /**
    * @description Fetches similar datasets of the provided dataset id
    * @param id {Int} - The given dataset id
    */
   loadSimilarDatasets({ commit }, id) {
     commit('SET_LOADING', true);
     return new Promise((resolve, reject) => {
       commit('SET_ID', id);
       const service = getters.getService(state);
       service.getSimilarDatasets(id)
         .then((response) => {
           commit('SET_SIMILAR_DATASETS', response.data);
           commit('SET_LOADING', false);
           resolve(response.data);
         })
         .catch((err) => {
           console.error(err);
           commit('SET_LOADING', false);
           reject(err);
         });
     });
   },
   loadQualityData({ commit }, id) {
     commit('SET_LOADING', true);
     return new Promise((resolve, reject) => {
       commit('SET_ID', id);
       const service = getters.getService(state);
       service.getQualityData(id)
         .then((response) => {
           commit('SET_QUALITY_DATA', response.data);
           commit('SET_LOADING', false);
           resolve(response.data);
         })
         .catch((err) => {
           console.error(err);
           commit('SET_LOADING', false);
           reject(err);
         });
     });
   },
   loadQualityDistributionData({ commit }, id) {
     commit('SET_LOADING', true);
     return new Promise((resolve, reject) => {
       commit('SET_ID', id);
       const service = getters.getService(state);
       service.getQualityDistributionData(id)
         .then((response) => {
           commit('SET_QUALITY_DISTRIBUTION_DATA', response.data);
           commit('SET_LOADING', false);
           resolve(response.data);
         })
         .catch((err) => {
           console.error(err);
           commit('SET_LOADING', false);
           reject(err);
         });
     });
   },
   /**
    * @description load dqv data
    * @param commit
    * @param id {String}
    * @param formats {Array}
    * @param locale {String}
    */
   loadDQVData({ commit }, { id, formats, locale }) {
     return new Promise((resolve, reject) => {
       commit('SET_ID', id);
       const service = getters.getService(state);
       formats.forEach(format => service.getDQVDataHead(id, format, locale)
         .then((response) => {
           const isAvailable = response.status === 200;
           commit(`SET_IS_DQV_DATA_${format.toUpperCase()}_AVAILABLE`, isAvailable);
           resolve(response);
         })
         .catch((err) => {
           commit(`SET_IS_DQV_DATA_${format.toUpperCase()}_AVAILABLE`, false);
           reject(err);
         }));
     });
   },
   setLoading({ commit }, isLoading) {
     commit('SET_LOADING', isLoading);
   },
   /**
    * @description Sets the Service to use when loading data.
    * @param commit
    * @param service - The service to use.
    */
   useService({ commit }, service) {
     commit('SET_SERVICE', service);
   },
 };

 const mutations = {
   SET_ACCESS_RIGHTS(state, accessRights) {
     state.dataset.accessRights = accessRights;
   },
   SET_ACCRUAL_PERIODICITY(state, accrualPeriodicity) {
     state.dataset.accrualPeriodicity = accrualPeriodicity;
   },
   SET_ATTRIBUTES(state, attributes) {
     state.dataset.attributes = attributes;
   },
   SET_catalog(state, catalog) {
     state.dataset.catalog = catalog;
   },
   SET_CATEGORIES(state, categories) {
     state.dataset.categories = categories;
   },
   SET_CONFORMS_TO(state, conformsTo) {
     state.dataset.conformsTo = conformsTo;
   },
   SET_CONTACT_POINTS(state, contactPoints) {
     state.dataset.contactPoints = contactPoints;
   },
   SET_COUNTRY(state, country) {
     state.dataset.country = country;
   },
   SET_CREATOR(state, creator) {
     state.dataset.creator = creator;
   },
   SET_DESCRIPTION(state, description) {
     state.dataset.description = description;
   },
   SET_DIMENSIONS(state, dimensions) {
     state.dataset.dimensions = dimensions;
   },
   SET_DISTRIBUTIONS(state, distributions) {
     state.dataset.distributions = distributions;
   },
   SET_DISTRIBUTION_FORMATS(state, distributionFormats) {
     state.dataset.distributionFormats = distributionFormats;
   },
   SET_DOCUMENTATIONS(state, documentations) {
     state.dataset.documentations = documentations;
   },
   SET_FREQUENCY(state, frequency) {
     state.dataset.frequency = frequency;
   },
   SET_HAS_QUALITY_ANNOTATIONS(state, hasQualityAnnotations) {
     state.dataset.hasQualityAnnotations = hasQualityAnnotations;
   },
   SET_HAS_VERSION(state, hasVersion) {
     state.dataset.hasVersion = hasVersion;
   },
   SET_ID(state, id) {
     state.dataset.id = id;
   },
   SET_IDENTIFIERS(state, identifiers) {
     state.dataset.identifiers = identifiers;
   },
   SET_ID_NAME(state, idName) {
     state.dataset.idName = idName;
   },
   SET_IS_REFERENCED_BY(state, isReferencedBy) {
     state.dataset.isReferencedBy = isReferencedBy;
   },
   SET_IS_VERSION_OF(state, isVersionOf) {
     state.dataset.isVersionOf = isVersionOf;
   },
   SET_KEYWORDS(state, keywords) {
     state.dataset.keywords = keywords;
   },
   SET_LANDING_PAGES(state, landingPages) {
     state.dataset.landingPages = landingPages;
   },
   SET_LANGUAGES(state, languages) {
     state.dataset.languages = languages;
   },
   SET_LICENCES(state, licences) {
     state.dataset.licences = licences;
   },
   SET_MODIFICATION_DATE(state, date) {
     state.dataset.modificationDate = date;
   },
   SET_NUM_SERIES(state, numSeries) {
     state.dataset.numSeries = numSeries;
   },
   SET_ORIGINAL_LANGUAGE(state, originalLanguage) {
     state.dataset.originalLanguage = originalLanguage;
   },
   SET_OTHER_IDENTIFIERS(state, otherIdentifiers) {
     state.dataset.otherIdentifiers = otherIdentifiers;
   },
   SET_PAGES(state, pages) {
     state.dataset.pages = pages;
   },
   SET_PROVENANCES(state, provenances) {
     state.dataset.provenances = provenances;
   },
   SET_PUBLISHER(state, publisher) {
     state.dataset.publisher = publisher;
   },
   SET_RELATED_RESOURCES(state, relatedResources) {
     state.dataset.relatedResources = relatedResources;
   },
   SET_RELEASE_DATE(state, date) {
     state.dataset.releaseDate = date;
   },
   SET_RESOURCE(state, resource) {
     state.dataset.resource = resource;
   },
   SET_SOURCES(state, sources) {
     state.dataset.sources = sources;
   },
   SET_SPATIAL(state, spatial) {
     state.dataset.spatial = spatial;
   },
   SET_SPATIAL_RESOURCE(state, spatialResource) {
     state.dataset.spatialResource = spatialResource;
   },
   SET_STAT_UNIT_MEASURES(state, statUnitMeasures) {
     state.dataset.statUnitMeasures = statUnitMeasures;
   },
   SET_TEMPORAL(state, temporal) {
     state.dataset.temporal = temporal;
   },
   SET_TRANSLATION_META_DATA(state, translationMetaData) {
     state.dataset.translationMetaData = translationMetaData;
   },
   SET_TITLE(state, title) {
     state.dataset.title = title;
   },
   SET_VERSION_INFO(state, versionInfo) {
     state.dataset.versionInfo = versionInfo;
   },
   SET_VERSION_NOTES(state, versionNotes) {
     state.dataset.versionNotes = versionNotes;
   },
   SET_VISUALISATIONS(state, visualisations) {
     state.dataset.visualisations = visualisations;
   },
   SET_WAS_GENERATED_BY(state, wasGeneratedBy) {
     state.dataset.wasGeneratedBy = wasGeneratedBy;
   },
   SET_SIMILAR_DATASETS(state, similarDatasets) {
     state.dataset.similarDatasets = similarDatasets;
   },
   SET_SD_DESCRIPTION(state, payload) {
     if (has(payload, 'id') && has(payload, 'description')) {
       const id = payload.id;
       const description = payload.description;
       if (isArray(state.dataset.similarDatasets)) {
         const similarDataset = state.dataset.similarDatasets.filter(el => el.id === id)[0];
         if (isObject(similarDataset)) Vue.set(similarDataset, 'description', description);
       }
     }
   },
   SET_SD_TITLE(state, payload) {
     if (has(payload, 'id') && has(payload, 'title')) {
       const id = payload.id;
       const title = payload.title;
       if (isArray(state.dataset.similarDatasets)) {
         const similarDataset = state.dataset.similarDatasets.filter(el => el.id === id)[0];
         if (isObject(similarDataset)) Vue.set(similarDataset, 'title', title);
       }
     }
   },
   SET_ACTIVE_NAVIGATION_TAB(state, tabIndex) {
     state.activeNavigationTab = tabIndex;
   },
   SET_LOADING(state, isLoading) {
     state.loading = isLoading;
   },
   SET_SERVICE(state, service) {
     state.service = service;
   },
   SET_QUALITY_DATA(state, qualityData) {
     state.dataset.qualityData = qualityData;
   },
   SET_QUALITY_DISTRIBUTION_DATA(state, qualityDistributionData) {
     state.dataset.qualityDistributionData = qualityDistributionData;
   },
   SET_IS_DQV_DATA_RDF_AVAILABLE(state, isDQVDataRDFAvailable) {
     state.dataset.isDQVDataRDFAvailable = isDQVDataRDFAvailable;
   },
   SET_CATALOG_RECORD(state, catalogRecord) {
     state.dataset.catalogRecord = catalogRecord;
   },
   SET_ADMS_IDENTIFIERS(state, admsIdentifiers) {
     state.dataset.admsIdentifiers = admsIdentifiers;
   },
   SET_DEADLINE(state, deadline) {
     state.dataset.deadline = deadline;
   },
   SET_GEOCODING_DESCRIPTION(state, geocodingDescription) {
     state.dataset.geocodingDescription = geocodingDescription;
   },
   SET_QUALIFIED_ATTRIBUTIONS(state, qualifiedAttributions) {
     state.dataset.qualifiedAttributions = qualifiedAttributions;
   },
   SET_QUALIFIED_RELATIONS(state, qualifiedRelations) {
     state.dataset.qualifiedRelations = qualifiedRelations;
   },
   SET_RELATIONS(state, relations) {
     state.dataset.relations = relations;
   },
   SET_SAMPLE(state, sample) {
     state.dataset.sample = sample;
   },
   SET_SPATIAL_RESOLUTION_IN_METERS(state, spatialResolutionInMeters) {
     state.dataset.spatialResolutionInMeters = spatialResolutionInMeters;
   },
   SET_SUBJECT(state, subject) {
     state.dataset.subject = subject;
   },
   SET_TEMPORAL_RESOLUTION(state, temporalResolution) {
     state.dataset.temporalResolution = temporalResolution;
   },
   SET_THEME(state, theme) {
     state.dataset.theme = theme;
   },
   SET_TYPE(state, type) {
     state.dataset.type = type;
   },
   SET_EXTENDET_METADATA(state, extendetMetadata) {
     state.dataset.extendetMetadata = extendetMetadata;
   },
 };

 const module = {
   namespaced: true,
   state,
   actions,
   mutations,
   getters,
 };

 export default module;
