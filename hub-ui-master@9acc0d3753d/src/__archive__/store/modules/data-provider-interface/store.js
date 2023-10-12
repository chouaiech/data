/* eslint-disable no-param-reassign, no-shadow, no-console */
import Vue from 'vue';
import Vuex from 'vuex';

import axios from 'axios';

Vue.use(Vuex);

const state = {
};

const getters = {
};

const actions = {
  requestFirstEntrySuggestions({ commit }, voc) {
    return new Promise((resolve, reject) => {
      const req = `${Vue.prototype.$env.api.baseUrl}search?filter=vocabulary&vocabulary=${voc}&autocomplete=true`;
      axios.get(req)
        .then((res) => {
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  requestAutocompleteSuggestions({ commit }, { voc, text }) {
    return new Promise((resolve, reject) => {
      const input = text;
      const req = `${Vue.prototype.$env.api.baseUrl}search?filter=vocabulary&vocabulary=${voc}&autocomplete=true&q=${input}`;
      axios.get(req)
        .then((res) => {
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
  requestResourceName({ commit }, { voc, vocMatch, resource }) {
    const value = resource.substring(resource.lastIndexOf('/') + 1);
    const req = `${Vue.prototype.$env.api.baseUrl}vocabularies/${voc}/${value}`;
    return new Promise((resolve, reject) => {
      axios.get(req)
        .then((res) => {
          resolve(res);
        })
        .catch((err) => {
          reject(err);
        });
    });
  },
};

const mutations = {};

const module = {
  namespaced: true,
  state,
  actions,
  mutations,
  getters,
};

export default module;
