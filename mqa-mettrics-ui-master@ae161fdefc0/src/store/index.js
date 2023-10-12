import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

// Create a preconfigured axios instance
const api = axios.create({
  withCredentials: false,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  }
})

// Used to cancel certain API requests
let once = null

const store = new Vuex.Store({

  state: {
    /** CATALOGUES **/
    catalogues: [],
    catalogue: {},
    catalogueDistributions: [],
    catalogueDistributionsSize: 0,
    catalogueViolations: [],
    catalogueViolationsSize: 0,
    /** METRICS **/
    metrics: null,
    history: null,
    isLoadingMetrics: false,
    isLoadingHistoryMetrics: false,
    currentlyLoadedHistoryMetrics: null,
    currentlyLoadedMetrics: null
  },

  getters: {
    /** CATALOGUES **/
    getCatalogue (state) {
      return state.catalogue
    },
    getAllCatalogues (state) {
      if (state.catalogues && state.catalogues.success && state.catalogues.result.count > 0) {
        return state.catalogues.result.results || []
      }
      return []
    },
    getCatalogueDistributions (state) {
      return state.catalogueDistributions
    },
    getCatalogueDistributionsSize (state) {
      return state.catalogueDistributionsSize
    },
    getCatalogueViolations (state) {
      return state.catalogueViolations
    },
    getCatalogueViolationsSize (state) {
      return state.catalogueViolationsSize
    },
    /** METRICS **/
    metrics (state) {
      const metrics = state.metrics
      if (metrics && metrics.success && metrics.result.count > 0) {
        return metrics.result.results[0] || []
      }
      return {}
    },
    history (state) {
      const history = state.history
      if (history && history.success && history.result.count > 0) {
        return history.result.results[0] || []
      }
      return {}
    },
    isLoadingMetrics (state) {
      return state.isLoadingMetrics
    },
    getCurrentlyLoadedMetrics (state) {
      return state.currentlyLoadedMetrics
    },
    isLoadingHistoryMetrics (state) {
      return state.isLoadingHistoryMetrics
    },
    getCurrentlyLoadedHistoryMetrics (state) {
      return state.currentlyLoadedHistoryMetrics
    }
  },

  mutations: {
    /** CATALOGUES **/
    SET_ALL_CATALOGUES (state, payload) {
      state.catalogues = payload
    },
    SET_CATALOGUE (state, payload) {
      state.catalogue = payload
    },
    SET_CATALOGUE_DISTRIBUTIONS (state, payload) {
      state.catalogueDistributions = payload
    },
    SET_CATALOGUE_DISTRIBUTIONS_SIZE (state, payload) {
      state.catalogueDistributionsSize = payload
    },
    SET_CATALOGUE_VIOLATIONS (state, payload) {
      state.catalogueViolations = payload
    },
    SET_CATALOGUE_VIOLATIONS_SIZE (state, payload) {
      state.catalogueViolationsSize = payload
    },
    /** METRICS **/
    SET_LOADING_METRICS (state, payload) {
      state.isLoadingMetrics = payload
    },
    SET_METRICS (state, payload) {
      state.metrics = payload
    },
    SET_LOADING_METRICS_HISTORY (state, payload) {
      state.loadingMetricsHistory = payload
    },
    /** HISTORY */
    SET_LOADING_HISTORY_METRICS (state, payload) {
      state.isLoadingHistoryMetrics = payload
    },
    SET_HISTORY (state, payload) {
      state.history = payload
    },
    SET_CURRENTLY_LOADED_HISTORY_METRICS (state, payload) {
      state.currentlyLoadedHistoryMetrics = payload
    },
    SET_CURRENTLY_LOADED_METRICS (state, payload) {
      state.currentlyLoadedMetrics = payload
    }
  },

  actions: {
    /** CATALOGUES **/
    loadAllCatalogues ({ commit }) {
      return new Promise((resolve, reject) => {
        api
          .get(`${Vue.prototype.$env.ROOT_API}catalogues`, {
            params: {
              filter: 'interoperability,accessibility,info,score'
            }
          })
          // eslint-disable-next-line
          .then(response => {
            commit('SET_ALL_CATALOGUES', response.data)
            resolve(response)
          })
          .catch(error => {
            // eslint-disable-next-line no-console
            console.error(error)
            reject(error)
          })
      })
    },
    loadCatalogue ({ state, getters, commit }, id) {
      if (state.catalogues.length > 0) {
        commit('SET_CATALOGUE', { catalogues: state.catalogues, id })
      } else {
        return new Promise((resolve, reject) => {
          api
            .get(`${Vue.prototype.$env.ROOT_API}catalogues`, {
              params: {
                filter: 'interoperability,accessibility,info,score'
              }
            })
            // eslint-disable-next-line
            .then(response => {
              commit('SET_ALL_CATALOGUES', response.data)
              const myCatalogue = getters.getAllCatalogues.find((catalogue) => catalogue.info.id === id)
              commit('SET_CATALOGUE', myCatalogue)
              resolve(response)
            })
            .catch(error => {
              // eslint-disable-next-line no-console
              console.error(error)
              reject(error)
            })
        })
      }
    },
    loadCatalogueDistributions ({ commit }, { id, currentPage, itemsPerPage }) {
      return new Promise((resolve, reject) => {
        api
          .get(`${Vue.prototype.$env.ROOT_API}catalogues/${id}/distributions/reachability`, {
            params: {
              limit: itemsPerPage,
              offset: (parseInt(currentPage) - 1) * itemsPerPage
            }
          })
          // eslint-disable-next-line
          .then(response => {
            commit('SET_CATALOGUE_DISTRIBUTIONS', response.data.result.results)
            resolve(response)
          })
          .catch(error => {
            // eslint-disable-next-line no-console
            console.error(error)
            reject(error)
          })
      })
    },
    loadCatalogueDistributionsSize ({ commit }, id) {
      return new Promise((resolve, reject) => {
        api
          .get(`${Vue.prototype.$env.ROOT_API}catalogues/${id}/distributions/reachability`, {
            params: {
              limit: 1
            }
          })
          // eslint-disable-next-line
          .then(response => {
            commit('SET_CATALOGUE_DISTRIBUTIONS_SIZE', response.data.result.count)
            resolve(response)
          })
          .catch(error => {
            // eslint-disable-next-line no-console
            console.error(error)
            reject(error)
          })
      })
    },
    loadCatalogueViolations ({ commit }, { id, currentPage, itemsPerPage }) {
      return new Promise((resolve, reject) => {
        api
          .get(`${Vue.prototype.$env.ROOT_API}catalogues/${id}/violations`, {
            params: {
              limit: itemsPerPage,
              offset: (parseInt(currentPage) - 1) * itemsPerPage
            }
          })
          .then(response => {
            commit('SET_CATALOGUE_VIOLATIONS', response.data.result.results)
            resolve(response)
          })
          .catch(error => {
            // eslint-disable-next-line no-console
            console.error(error)
            reject(error)
          })
      })
    },
    loadCatalogueViolationsSize ({ commit }, id) {
      return new Promise((resolve, reject) => {
        api
          .get(`${Vue.prototype.$env.ROOT_API}catalogues/${id}/violations`, {
            params: {
              limit: 1
            }
          })
          .then(response => {
            commit('SET_CATALOGUE_VIOLATIONS_SIZE', response.data.result.count)
            resolve(response)
          })
          .catch(error => {
            // eslint-disable-next-line no-console
            console.error(error)
            reject(error)
          })
      })
    },
    /** HISTORY **/
    fetchHistoryMetrics ({ state, commit }, { path }) {
      if (state.isLoadingHistoryMetrics && path === state.currentlyLoadedHistoryMetrics) {
        return
      }
      // if (!state.isLoadingHistoryMetrics || path !== state.currentlyLoadedHistoryMetrics) {
      //   if (once) {
      //     once.cancel('Request cancelled by user')
      //   }

      once = axios.CancelToken.source()
      commit('SET_CURRENTLY_LOADED_HISTORY_METRICS', path)
      commit('SET_LOADING_HISTORY_METRICS', true)
      commit('SET_HISTORY', null)

      return api
        .get(`${Vue.prototype.$env.ROOT_API}${path}`, {
          cancelToken: once.token
        })
        .then(response => {
          const historyData = response.data
          commit('SET_HISTORY', historyData)
          commit('SET_LOADING_HISTORY_METRICS', false)
        })
        .catch(error => {
          if (axios.isCancel(error)) {
            // eslint-disable-next-line no-console
            console.error(error)
          } else {
            // eslint-disable-next-line no-console
            console.error(error)
          }
        })
      // }
    },
    /** METRICS **/
    fetchMetrics ({ state, commit }, { path }) {
      // Only do an API request when no one else is currently doing this request.
      // This prevents from unnecessarily making additional API requests.
      // Should not be used for loading catalogue metrics, as this leads to inconsistent data.

      // The complete payload is the basically a merged payload of all dimensions
      // and in addition a dimension called 'scoring' with indicator 'timeBasedScoring'
      if (!state.isLoadingMetrics || path !== state.currentlyLoadedMetrics) {
        if (once) {
          once.cancel('Request cancelled by user')
        }

        once = axios.CancelToken.source()
        commit('SET_CURRENTLY_LOADED_METRICS', path)
        commit('SET_LOADING_METRICS', true)
        commit('SET_METRICS', null)

        return api
          .get(`${Vue.prototype.$env.ROOT_API}${path}`, {
            cancelToken: once.token
          })
          .then(response => {
            commit('SET_METRICS', response.data)
            commit('SET_LOADING_METRICS', false)
          })
          .catch(error => {
            if (axios.isCancel(error)) {
              // eslint-disable-next-line no-console
              console.error(error)
            } else {
              // eslint-disable-next-line no-console
              console.error(error)
            }
          })
      }
    }
  },

  modules: {
    globalModule: {
      namespaced: true,
      actions: {
        fetchMetrics ({ dispatch }) {
          dispatch('fetchMetrics', { path: '/global', id: 'global' }, { root: true })
        }
        // fetchHistoryMetrics ({ dispatch }) {
        //   dispatch('fetchHistoryMetrics', { path: '/global/history?startDate=2020-02-01', id: 'globalhistory' }, { root: true })
        // }
      }
    },
    globalHistoryModule: {
      namespaced: true,
      actions: {
        fetchHistoryMetrics ({ dispatch }) {
          dispatch('fetchHistoryMetrics', { path: `global/history?startDate=${Vue.prototype.$env.HISTORY_START_DATE}&resolution=${Vue.prototype.$env.HISTORY_RESOLUTION}`, id: 'globalhistory' }, { root: true })
        }
      }
    },
    catalogueModule: {
      namespaced: true,
      actions: {
        fetchMetrics ({ dispatch }, id) {
          dispatch('fetchMetrics', { path: `/catalogues/${id}`, id }, { root: true })
        },
        fetchHistoryMetrics ({ dispatch }, id) {
          dispatch('fetchHistoryMetrics', { path: `catalogues/${id}/history?startDate=${Vue.prototype.$env.HISTORY_START_DATE}&resolution=${Vue.prototype.$env.HISTORY_RESOLUTION}`, id }, { root: true })
        }
      }
    },
    countryModule: {
      namespaced: true,
      actions: {
        fetchMetrics ({ dispatch }, id) {
          dispatch('fetchMetrics', { path: `/countries/${id}`, id }, { root: true })
        },
        fetchHistoryMetrics ({ dispatch }, id) {
          dispatch('fetchHistoryMetrics', { path: `countries/${id}/history?startDate=${Vue.prototype.$env.HISTORY_START_DATE}&resolution=${Vue.prototype.$env.HISTORY_RESOLUTION}`, id }, { root: true })
        }
      }
    }
  }
})

export default store
