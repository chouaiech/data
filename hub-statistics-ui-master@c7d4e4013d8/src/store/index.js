/**
 * @author Dennis Ritter
 * @description The Main vuex store.
 */

// Import Vue & Vuex Store
import Vue from 'vue'
import Vuex from 'vuex'
// Import store modules
import auth from './modules/auth/store'

Vue.use(Vuex)

const state = {}

const actions = {}

const mutations = {}

const getters = {
  /**
   * @description Returns the current route (name).
   * @param state
   */
  getCurrentRoute: state => state.route
}

const store = new Vuex.Store({
  state,
  actions,
  mutations,
  getters,
  modules: {
    auth
  }
})

export default store
