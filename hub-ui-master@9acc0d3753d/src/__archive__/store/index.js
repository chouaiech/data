/**
 * @author Dennis Ritter
 * @description The Main vuex store.
 */

// Import Vue & Vuex Store
import Vue from 'vue';
import Vuex from 'vuex';
// Import store modules
import catalogs from './modules/catalogs/store';
import catalogDetails from './modules/catalog-details/store';
import datasets from './modules/datasets/store';
import datasetDetails from './modules/dataset-details/store';
import dataProviderInterface from './modules/data-provider-interface/store';
import mapsData from './modules/mapsData/store';
import gazetteer from './modules/gazetteer/store';
import geo from './modules/geo/store';
import auth from './modules/auth/store';
import snackbar from './modules/snackbar/store';

Vue.use(Vuex);


const state = {};

const actions = {};

const mutations = {};

const getters = {
  /**
   * @description Returns the current route (name).
   * @param state
   */
  getCurrentRoute: state => state.route,
};

const store = new Vuex.Store({
  state,
  actions,
  mutations,
  getters,
  modules: {
    catalogs,
    catalogDetails,
    datasets,
    datasetDetails,
    dataProviderInterface,
    mapsData,
    gazetteer,
    geo,
    auth,
    snackbar,
  },
});

export default store;
