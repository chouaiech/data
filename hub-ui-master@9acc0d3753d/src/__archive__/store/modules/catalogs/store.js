/* eslint-disable no-param-reassign,no-unused-vars,consistent-return,no-console */
/**
 * @author Dennis Ritter
 * @description Vuex store for the catalogs module
 */
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);
// Module Constants
/**
 * @description The Default amount of catalogs per page.
 * @type {number}
 */
const RESULTS_PER_PAGE = 10;

// catalogs Module State
const state = {
  /**
   * @property catalogs
   * @type Array
   * @description An array of catalogs.
   */
  catalogs: [],
  datasetCounts: {},
  loading: false,
  searchParameters: {
    // Text entered in the search input field
    query: '',
    limit: RESULTS_PER_PAGE,
    offset: 0,
    // The Facets to filter for
    facets: [],
    facetOperator: 'AND',
    facetGroupOperator: 'AND',
    sort: 'relevance+desc,modified+desc,title+asc',
  },
  /**
   * @property availableFacets
   * @type Array
   * @description The set union of all available facets for the .
   * @example availableFacets = [
   *  {
   *    items: [{
   *      count: 42,
   *      title: 'facet1',
   *      id: 'facet-1',
   *    }, {..}],
   *    id: 'tagsId'
   *    title: 'tags',
   *  }, {..}]
   */
  availableFacets: [],
  page: 1,
  // The total number of catalogs available with last request
  pageCount: 1,
  catalogsCount: 0,
  // The Service that implemented server requests for catalogs
  service: null,
};

const GETTERS = {
  getCatalogs: state => state.catalogs,
  getCatalogsCount: state => state.catalogsCount,
  getQuery: state => state.searchParameters.query,
  getLimit: state => state.searchParameters.limit,
  getLoading: state => state.loading,
  getOffset: state => state.searchParameters.offset,
  getFacets: state => state.searchParameters.facets,
  getFacetOperator: state => state.searchParameters.facetOperator,
  getFacetGroupOperator: state => state.searchParameters.facetGroupOperator,
  getAvailableFacets: state => state.availableFacets,
  getPage: state => state.page,
  getPageCount: state => state.pageCount,
  getService: state => state.service,
  getSort: state => state.searchParameters.sort,
};

const actions = {
  /**
   * @description Load all catalogs matching the given parameters.
   * @param commit
   * @param state
   * @param options {Object} - Given search parameters
   * @param options.query {String} - The given query string
   * @param options.facets {Array} - The active facets
   * @param options.limit {Number} - The maximum amount of catalogs to fetch
   * @param options.page {Number} - The current page
   * @param options.sort {String} - The sort method to use
   * @param options.append {Boolean} - Decides whether current catalogs in state will be replaced or fetched catalogs appended.
   */
  loadCatalogs(
    { commit, state },
    {
      query = GETTERS.getQuery(state),
      limit = RESULTS_PER_PAGE,
      page = GETTERS.getPage(state),
      sort = GETTERS.getSort(state),
      facetOperator = GETTERS.getFacetOperator(state),
      facetGroupOperator = GETTERS.getFacetGroupOperator(state),
      facets = GETTERS.getFacets(state),
      append = false,
    },
  ) {
    commit('SET_LOADING', true);
    return new Promise((resolve, reject) => {
      const service = GETTERS.getService(state);
      service.get(query, limit, page, sort, facetOperator, facetGroupOperator, facets)
        .then((response) => {
          commit('SET_AVAILABLE_FACETS', response.availableFacets);
          commit('SET_catalogS_COUNT', response.catalogsCount);
          if (append) commit('ADD_catalogS', response.catalogs);
          else commit('SET_catalogS', response.catalogs);
          commit('SET_LOADING', false);
          resolve();
        })
        .catch((error) => {
          console.error(error);
          commit('SET_LOADING', false);
          reject(error);
        });
    });
  },
  /**
   * @description Loads more catalogs.
   * @param commit
   * @param state
   * @param {number} amount - The amount of catalogs to add.
   */
  loadAdditionalCatalogs({ commit, state }, amount = RESULTS_PER_PAGE) {
    // const offset = GETTERS.getOffset(state) + amount;
    // commit('SET_OFFSET', offset);
    const page = GETTERS.getPage(state);
    const catalogsCount = GETTERS.getCatalogsCount(state);
    // If there are no more catalogs to load, cancel.
    // if (offset < catalogsCount) {
    // actions.loadCatalogs({ commit, state }, { offset, append: true });
    // }
    actions.loadCatalogs({ commit, state }, { page, append: true });
  },
  /**
   * @description Autocomplete a query String by using a autocompletion service
   * @param commit
   * @param q {String} The Query to autocomplete
   */
  autocompleteQuery({ commit }, q) {
    const service = GETTERS.getService(state);
    // If autocomplete function does not exist in this service -> Abort
    if (typeof service.autocomplete !== 'function') return;
    return new Promise((resolve, reject) => {
      service.autocomplete(q)
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  /**
   * @description Replace the current state facets by the given facets
   * @param commit
   * @param facets {Array} - The given facets
   */
  setFacets({ commit }, facets) {
    if (facets) commit('SET_FACETS', facets);
  },
  /**
   * @description Add the given facet to the states facets.
   * @param commit
   * @param params {Object} - The wrapped action parameters.
   * @param params.field {String} - The field of the given facet
   * @param params.facet {String} - The facet to add
   */
  addFacet({ commit }, { field, facet }) {
    commit('ADD_FACET', { field, facet });
  },
  /**
   * @description Remove the given facet from the states facets.
   * @param commit
   * @param params {Object} - The wrapped action parameters.
   * @param params.field {String} - The field of the given facet
   * @param params.facet {String} - The facet to remove
   */
  removeFacet({ commit }, { field, facet }) {
    commit('REMOVE_FACET', { field, facet });
  },
  /**
   * @description Remove the given facet from the states facets.
   * @param commit
   * @param operator {String} - The facet operator to set. Possible Operators : ['AND', 'OR'].
   */
  setFacetOperator({ commit }, operator) {
    commit('SET_FACET_OPERATOR', operator);
  },
  /**
   * @description Remove the given facet from the states facets.
   * @param commit
   * @param operator {String} - The facet operator to set. Possible Operators : ['AND', 'OR'].
   */
  setFacetGroupOperator({ commit }, operator) {
    commit('SET_FACET_GROUP_OPERATOR', operator);
  },
  /**
   * @description Handles page changes by through URL query.
   * @param commit
   * @param state
   * @param page {String} The given page number as a String
   */
  setPage({ commit }, page) {
    commit('SET_PAGE', page);
  },
  setPageCount({ commit }, count) {
    commit('SET_PAGE_COUNT', count);
  },
  /**
   * @description Replace the current state query by the given query
   * @param commit
   * @param query {String} - The given query
   */
  setQuery({ commit }, query) {
    commit('SET_QUERY', query);
  },
  /**
   * @description Replace the current sort method
   * @param commit
   * @param sort {String} - The given sort method to use now
   */
  setSort({ commit }, sort) {
    commit('SET_SORT', sort);
  },
  /**
   * @description Increases the limit that is stored in the state
   * @param commit
   * @param state
   * @param amount {Number} - The amount to add to the current state limit
   */
  incLimit({ commit, state, getters }, amount = RESULTS_PER_PAGE) {
    const limit = getters.getLimit(state) + amount;
    commit('SET_LIMIT', limit);
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
  SET_catalogS(state, data) {
    state.catalogs = data;
  },
  ADD_catalogS(state, data) {
    state.catalogs = state.catalogs.concat(data);
  },
  SET_LIMIT(state, limit) {
    state.searchParameters.limit = limit;
  },
  SET_OFFSET(state, offset) {
    state.searchParameters.offset = offset;
  },
  SET_FACETS(state, facets) {
    state.searchParameters.facets = facets;
  },
  ADD_FACET(state, { field, facet }) {
    // If the facetField is not defined in state..
    if (!Object.prototype.hasOwnProperty.call(state.searchParameters.facets, field)) {
      // ..define it by assigning an array containing {value} in it
      state.searchParameters.facets[field] = [facet];
    } else {
      state.searchParameters.facets[field].push(facet);
    }
  },
  REMOVE_FACET(state, { field, facet }) {
    const index = state.searchParameters.facets[field].indexOf(facet);
    state.searchParameters.facets[field].splice(index, 1);
  },
  SET_AVAILABLE_FACETS(state, facets) {
    state.availableFacets = facets;
  },
  SET_catalogS_COUNT(state, catalogsCount) {
    state.catalogsCount = catalogsCount;
  },
  SET_FACET_OPERATOR(state, operator) {
    state.searchParameters.facetOperator = operator;
  },
  SET_FACET_GROUP_OPERATOR(state, operator) {
    state.searchParameters.facetGroupOperator = operator;
  },
  SET_PAGE(state, page) {
    state.page = page;
  },
  SET_PAGE_COUNT(state, count) {
    state.pageCount = count;
  },
  SET_QUERY(state, query) {
    state.searchParameters.query = query;
  },
  SET_SERVICE(state, service) {
    state.service = service;
  },
  SET_SORT(state, sort) {
    state.searchParameters.sort = sort;
  },
  SET_LOADING(state, isLoading) {
    state.loading = isLoading;
  },
};

const module = {
  namespaced: true,
  state,
  actions,
  mutations,
  getters: GETTERS,
};

export default module;
