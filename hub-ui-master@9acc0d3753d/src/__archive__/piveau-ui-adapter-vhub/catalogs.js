/* eslint-disable no-console */
/**
 * @author Dennis ritter
 * @description
 */

import axios from 'axios';
import { has } from 'lodash';

import dataGetters from './getters/data-getters';

const getResponseData = (catalog) => {
  const cat = {};
  cat.catalog = dataGetters.getArrayOfStrings(catalog, 'catalog');
  cat.count = dataGetters.getCount(catalog);
  cat.country = dataGetters.getObject(catalog, 'country', ['id', 'title']);
  cat.creator = dataGetters.getObject(catalog, 'creator', ['name', 'type', 'email', 'resource', 'homepage']);
  cat.dataset = [];
  cat.description = dataGetters.getObjectLanguage(catalog, 'description', 'No description available');
  cat.hasPart = dataGetters.getArrayOfStrings(catalog, 'has_part');
  cat.homepage = dataGetters.getString(catalog, 'homepage');
  cat.id = dataGetters.getString(catalog, 'id');
  cat.idName = dataGetters.getString(catalog, 'idName');
  cat.isPartOf = dataGetters.getString(catalog, 'is_part_of');
  cat.languages = dataGetters.getArrayOfObjects(catalog, 'language', ['id', 'label', 'resource']);
  cat.licence = dataGetters.getObject(catalog, 'license', ['id', 'label', 'description', 'resource', 'la_url']);
  cat.modified = dataGetters.getString(catalog, 'modified');
  cat.publisher = dataGetters.getObject(catalog, 'publisher', ['name', 'type', 'email', 'resource', 'homepage']);
  cat.record = dataGetters.getObject(catalog, 'catalog_record', ['issued', 'modified']);
  cat.rights = dataGetters.getObject(catalog, 'rights', ['label', 'resource']);
  cat.issued = dataGetters.getString(catalog, 'issued');
  // cat.service = ... (not available)
  cat.spatial = dataGetters.getArrayOfObjects(catalog, 'spatial', ['type', 'coordinates']);
  cat.spatialResource = dataGetters.getArrayOfObjects(catalog, 'spatial_resource', ['label', 'resource']);
  cat.themeTaxonomy = dataGetters.getArrayOfStrings(catalog, 'theme_taxonomy');
  cat.title = dataGetters.getObjectLanguage(catalog, 'title', 'No title available');
  return cat;
};

export default class Catalogs {
  constructor(baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * @description GET catalog by given id.
   * @param id
   */
  getSingle(id) {
    return new Promise((resolve, reject) => {
      const endpoint = 'catalogues';
      const reqStr = `${this.baseUrl}${endpoint}/${id}`;
      axios.get(reqStr, {
        params: {},
      })
        .then((response) => {
          const catalog = response.data.result;
          let cat = {};
          try {
            cat = getResponseData(catalog);
          } catch (error) {
            console.warn('Error in catalogs.js while checking response:', error.message);
            console.error(error.stack);
          }
          resolve(cat);
        })
        .catch((error) => {
          reject(error);
        });
    });
  }

  /**
   * @description GET all catalogs matching the given criteria.
   * @param q
   * @returns {Promise}
   */
  get(q, limit, page = 1, sort = 'relevance+desc,modified+desc,title+asc', facetOperator = 'AND', facetGroupOperator = 'AND', facets) {
    // The request parameters
    const params = {
      q,
      filter: 'catalogue',
      sort,
      limit,
      page: page - 1,
      facetOperator,
      facetGroupOperator,
      includes: 'id,title,description,modified,issued,country,count',
      facets,
    };
    return new Promise((resolve, reject) => {
      const endpoint = 'search';
      const reqStr = `${this.baseUrl}${endpoint}`;
      axios.get(reqStr, {
        params,
      })
        .then((response) => {
          const resData = {
            availableFacets: [],
            catalogsCount: response.data.result.count,
            catalogs: [],
          };
          // transform fetched facets
          for (const field of response.data.result.facets) {
          // Check for required field keys
            if (has(field, 'id') && has(field, 'title') && has(field, 'items')) {
            // Transform Items of field to use its id as Obj key for easier search later in app
              const items = [];
              for (const facet of field.items) {
              // Check for required facet/item keys
                if (has(facet, 'id') && has(facet, 'title') && has(facet, 'count')) {
                // Build object for current facet/item
                  items.push({
                    id: facet.id,
                    count: facet.count,
                    title: facet.title,
                  });
                }
              }
              // Add to response array
              resData.availableFacets.push({
                id: field.id,
                title: field.title,
                items,
              });
            }
          }

          // Transform catalogs Data model
          const catalogs = response.data.result.results;
          for (const catalog of catalogs) {
            let cat = {};
            try {
              cat = getResponseData(catalog);
            } catch (error) {
              console.warn('Error in catalogs.js while checking response:', error.message);
              console.error(error.stack);
            }
            resData.catalogs.push(cat);
          }
          return resolve(resData);
        })
        .catch((error) => {
          console.error(error);
          reject(error);
        });
    });
  }
}
