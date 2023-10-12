/* eslint-disable no-param-reassign, no-shadow, no-console */
import Vue from 'vue';
import Vuex from 'vuex';

import axios from 'axios';
import { get } from 'lodash';
import createDraftApi from '@/utils/draftApi';
import createIdentifiersApi from '@/utils/identifiersApi';
import { decode } from '@/utils/jwt';

let draftApi;
let identifiersApi;

Vue.use(Vuex);

const state = {
  authenticated: false,
  rtptoken: '',
  keycloak: null,
  userData: {
    authToken: '',
    rtpToken: '',
    userName: '',
    permissions: [],
    drafts: [],
  },
  isEditMode: false,
  isDraft: false,
};

const getters = {
  securityAuth: state => state.authenticated,
  getRTPToken: state => state.rtptoken,
  getKeycloak: state => state.keycloak,
  getUserData: state => state.userData,
  getUserName: state => state.userData.userName,
  /**
    * @description Get all catalogs associated to the user where they have access to.
    * @returns {Array} Array of catalogs
   */
  getUserCatalogs: state => state.userData.permissions.filter(
    // User must have CRUD authorization
    permission => ['dataset:update', 'dataset:delete', 'dataset:create']
      .every(scope => permission.scopes.includes(scope)),
  ),
  /**
   * @description Get all catalog IDs associated to the user where they have access to.
   */
  getUserCatalogIds: (state, getters) => getters.getUserCatalogs.map(
    catalog => catalog.rsname,
  ),
  getUserDrafts: state => state.userData.drafts,
  getUserDraftIds: state => state.userData.drafts.map(dataset => dataset.id),
  getIsEditMode: state => state.isEditMode,
  getIsDraft: state => state.isDraft,
};

const actions = {
  authLogin({ commit }, authenticated) {
    commit('SECURITY_AUTH', authenticated);
  },
  authLogout({ commit }) {
    commit('SECURITY_AUTH', false);
    commit('RTP_TOKEN', '');
  },
  rtpToken({ commit }, rtptoken) {
    commit('RTP_TOKEN', rtptoken);
  },
  setKeycloak({ commit }, keycloak) {
    commit('SET_KEYCLOAK', keycloak);
  },
  /**
   * Updates user data according to an authentication token and by supplying neccessary
   * information to retrieve tFhe user's permissions as well as their drafts.
   * @param {*} commit
   * @param {Object} params
   * @returns {Promise<Object>}
   */
  async updateUserData({ commit, dispatch }, { authToken, rtpTokenFn, hubUrl }) {
    if (!authToken || typeof rtpTokenFn !== 'function') return {};
    commit('UPDATE_USER_DATA_PENDING');

    try {
      // Get RTP token as JWT token
      const rtpTokenResponse = await rtpTokenFn();
      const rtpToken = rtpTokenResponse.status === 200
        && rtpTokenResponse.data
        && rtpTokenResponse.data.access_token;

      if (!rtpToken) throw new Error('Failed to retrieve RTP token');

      const decodedRtpToken = decode(rtpToken);
      const permissions = get(decodedRtpToken, 'authorization.permissions', []);

      commit('SET_USER_DATA', {
        authToken,
        rtpToken,
        userName: decodedRtpToken.preferred_username,
        permissions,
        drafts: [],
      });

      draftApi = createDraftApi({ baseURL: hubUrl, authToken: rtpToken });
      identifiersApi = createIdentifiersApi({ baseURL: hubUrl, authToken: rtpToken });

      dispatch('updateUserDrafts');

      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      console.error(ex);
      commit('UPDATE_USER_DATA_ERROR');
    }
    return {};
  },
  async updateUserDrafts({ commit, state }) {
    const rtpToken = state.userData.rtpToken;
    if (!rtpToken) throw new Error('Requires RTP token');

    // Get associated drafts
    const draftsResponse = await draftApi.getAllDatasetDrafts();
    const drafts = draftsResponse.status === 200
      && draftsResponse.data;
    commit('SET_USER_DATA', { drafts });
  },
  async createDataset({ commit }, actionParams) {
    const requestOptions = {
      method: 'PUT',
      url: actionParams.url,
      headers: {
        'Content-Type': 'application/ld+json',
        Authorization: `Bearer ${actionParams.token}`,
      },
      data: actionParams.data,
    };

    const result = await axios.request(requestOptions);

    if (result.status === 201 || result.status === 204) {
      commit('CHANGE_IS_EDIT_MODE', false);
      commit('CHANGE_IS_DRAFT', false);
    }
  },
  /**
   * Creates a draft dataset
   * @param {*} commit
   * @param {object} dataset - The dataset object
   * @param {string} dataset.id - The ID of the dataset
   * @param {string} dataset.catalog - The ID of the dataset
   * @param {object} dataset.description - The description object of the dataset containing different locales
   * @param {object} dataset.title - The title object of the dataset containing different locales
   * @param {object} dataset.body - the JSON-LD representation of the dataset
   * @returns {Promise<Object>}
   */
  async createUserDraft({ commit }, {
    id, catalog, description = { en: '' }, title = { en: '' }, body = {},
  }) {
    if (!draftApi) return {};

    commit('UPDATE_USER_DATA_PENDING');
    let response;
    try {
      response = await draftApi.createDatasetDraft({ id, catalogue: catalog, body });
      commit('CREATE_USER_DRAFT', {
        id, catalog, description, title, body,
      });
      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      commit('UPDATE_USER_DATA_ERROR');
      throw ex;
    }

    return response;
  },
  async deleteUserDraftById({ commit, state }, { id, catalog }) {
    if (!draftApi) return {};
    const draftExists = state.userData.drafts.find(draft => draft.id === id);
    if (!draftExists) return {};

    commit('UPDATE_USER_DATA_PENDING');
    let response;
    try {
      response = await draftApi.deleteDatasetDraft({ id, catalogue: catalog });

      commit('DELETE_USER_DRAFT', id);
      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      commit('UPDATE_USER_DATA_ERROR');
      throw ex;
    }

    return response;
  },
  async publishUserDraftById({ commit, state }, { id, catalog, body = {} }) {
    if (!draftApi) return {};
    const draftExists = state.userData.drafts.find(draft => draft.id === id);
    if (!draftExists) return {};

    commit('UPDATE_USER_DATA_PENDING');
    let response;
    try {
      response = await draftApi.publishDatasetDraft({ id, catalogue: catalog, body });
      commit('DELETE_USER_DRAFT', id);
      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      commit('UPDATE_USER_DATA_ERROR');
      throw ex;
    }

    return response;
  },
  async publishUserDraft({ dispatch }, { id, catalog, body }) {
    return dispatch('publishUserDraftById', { id, catalog, body });
  },
  async putDatasetToDraft({ commit }, {
    id, catalog, title, description,
  }) {
    if (!draftApi) return {};

    commit('UPDATE_USER_DATA_PENDING');
    let response;
    try {
      response = await draftApi.putDatasetToDraft({ id, catalogue: catalog });
      commit('PUT_DATASET_TO_DRAFT', {
        id, catalog, title, description,
      });
      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      commit('UPDATE_USER_DATA_ERROR');
      throw ex;
    }

    return response;
  },
  setIsEditMode({ commit }, bool) {
    commit('CHANGE_IS_EDIT_MODE', bool);
  },
  setIsDraft({ commit }, bool) {
    commit('CHANGE_IS_DRAFT', bool);
  },
  async createPersistentIdentifier({ commit }, { id, catalog, type = 'mock' }) {
    if (!identifiersApi) return {};

    commit('UPDATE_USER_DATA_PENDING');
    let response;
    try {
      response = await identifiersApi.createPersistentIdentifier({ id, catalogue: catalog, type });
      commit('UPDATE_USER_DATA_SUCCESS');
    } catch (ex) {
      commit('UPDATE_USER_DATA_ERROR');
      throw ex;
    }
    return response;
  },
};

const mutations = {
  SECURITY_AUTH(state, authenticated) {
    state.authenticated = authenticated;
  },
  RTP_TOKEN(state, rtpToken) {
    state.rtptoken = rtpToken;
  },
  SET_KEYCLOAK(state, keycloak) {
    state.keycloak = keycloak;
  },
  SET_USER_DATA(state, userData) {
    state.userData = { ...state.userData, ...userData };
  },
  UPDATE_USER_DATA_PENDING(state) { state.userData.pending = true; },
  UPDATE_USER_DATA_SUCCESS(state) { state.userData.pending = false; },
  UPDATE_USER_DATA_ERROR(state) { state.userData.pending = false; },

  CREATE_USER_DRAFT(state, {
    id, catalog, title, description,
  }) {
    state.userData.drafts.push({
      id,
      catalog,
      title,
      description,
    });
  },
  PUT_DATASET_TO_DRAFT(state, {
    id, catalog, title, description,
  }) {
    state.userData.drafts.push({
      id,
      catalog,
      title,
      description,
    });
  },
  DELETE_USER_DRAFT(state, id) {
    const draftIndex = state.userData.drafts.findIndex(draft => draft.id === id);
    if (draftIndex > -1) {
      state.userData.drafts.splice(draftIndex, 1);
    }
  },
  CHANGE_IS_EDIT_MODE(state, bool) {
    state.isEditMode = bool;
  },
  CHANGE_IS_DRAFT(state, bool) {
    state.isDraft = bool;
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
