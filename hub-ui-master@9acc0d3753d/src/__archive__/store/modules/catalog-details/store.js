/* eslint-disable no-param-reassign,no-console */
/**
 * @Publisher Dennis Ritter
 * @description Vuex store for the the details of a catalog.
 */
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
  catalog: {},
  service: null,
};

const getters = {
  getCatalog: state => state.catalog,
  getCatalogService: state => state.service,
};

const actions = {
  /**
   * @description Loads details for the dataset with the given ID.
   * @param commit
   * @param state
   * @param id {String} The dataset ID.
   */
  loadCatalog({ state, commit }, id) {
    return new Promise((resolve, reject) => {
      const service = getters.getCatalogService(state);
      service.getSingle(id)
        .then((response) => {
          commit('SET_catalog', response);
          resolve(response);
        })
        .catch((err) => {
          console.error(err);
          reject(err);
        });
    });
  },
  /**
   * @description Sets the Service to use when loading data.
   * @param commit
   * @param service - The service to use.
   */
  useCatalogService({ commit }, service) {
    commit('SET_SERVICE', service);
  },
};

const mutations = {
  SET_catalog(state, catalog) {
    state.catalog = catalog;
  },
  SET_SERVICE(state, service) {
    state.service = service;
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
