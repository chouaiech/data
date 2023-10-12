/* eslint-disable no-console,no-underscore-dangle */
/**
 * @author Har Preet Singh
 * @created 13.05.19
 * @description
 */

import Keycloak from 'keycloak-js';
import axios from 'axios';
import qs from 'qs';
import { store } from '@piveau/piveau-hub-ui-modules';
// import store from '../store/index';

let kc = null;

export default class AuthService {
  /**
   * @function constructor
   * @param redirectUrl redirection url used for logout redirection
   * @param keyclockConfig keyclock configuration from user-config.js
   */
  constructor(keyclockConfig, rtpConfig, useAuthService) {
    if (!useAuthService) return;
    // Init Keycloak configuration
    kc = kc || new Keycloak(keyclockConfig);
    // Auth Base URL
    this.baseUrl = keyclockConfig.url;
    this.realm = keyclockConfig.realm;
    this.rtpConfig = rtpConfig;
    // Check if the user has session
    kc.init({
      onLoad: 'check-sso',
      promiseType: 'native',
      checkLoginIframe: false,
      silentCheckSsoRedirectUri: `${window.location.origin}${process.env.buildconf.BASE_PATH}static/silent-check-sso.html`,
    })
      .then((authenticated) => {
        if (authenticated) {
          store.dispatch('auth/authLogin', kc.authenticated);
          this.getRTPToken(kc.token).then((res) => {
            store.dispatch('auth/rtpToken', res.data.access_token);
          });
        }
      })
      .catch((err) => {
        console.error(err);
      });
  }

  init() {
    kc.login()
      .then(() => {
        store.dispatch('auth/setKeycloak', kc);
        store.dispatch('auth/authLogin', kc.authenticated);
        this.$router.push('/');
      })
      .catch((err) => {
        console.error(`Error keycloak login: ${JSON.stringify(err)}`);
      });
  }

  /**
  * @description logout from keycloak
  * @param keycloak keycloak object
  * @param redirectURL redirect URL after logout
  */
  logout = (keycloak, redirectURL) => {
    keycloak.logout({ redirectUri: redirectURL });
  };

  /**
  * @description Method is to check either the user is authenticated or not and returns true or false
  * @param keycloak keycloak object
  */
  isAuthenticated = (keycloak) => {
    if (typeof keycloak !== 'undefined') {
      return keycloak.authenticated;
    }
    return false;
  };

  /**
  * @description get header for axios request
  * @param keycloak keycloak object
  */
  getHeader = keycloak => ({ Authorization: `Bearer ${keycloak.token}` });

  /**
  * @description get token for axios request
  * @param keycloak keycloak object
  */
  getToken = (keycloak) => {
    const _keycloack = keycloak;
    _keycloack.updateToken(10)
      .then(() => {
        store.dispatch('auth/setKeycloak', _keycloack);
        store.dispatch('auth/authLogin', _keycloack.authenticated);
        return _keycloack.token;
      });
  };

  /**
  * @description refresh or update the token on each Auth protected request
  * @param keycloak keycloak object
  */
  refreshToken = keycloak => (new Promise((resolve, reject) => {
    keycloak.updateToken(10)
      .then(() => {
        store.dispatch('auth/setKeycloak', keycloak);
        store.dispatch('auth/authLogin', keycloak.authenticated);
        resolve(keycloak.token);
      }).catch((err) => {
        reject(err);
      });
  }));

  /**
  * @description get role exist or not
  * @param keycloak keycloack object
  * @param role type of role
  */
  roles = (keycloak, role) => keycloak.hasRealmRole(role);

  /**
  * @description get RTP Token
  * @param keycloak keycloack token
  */
  getRTPToken = (token) => {
    const endpoint = `${this.baseUrl}/realms/${this.realm}/protocol/openid-connect/token`;
    const requestBody = {
      grant_type: this.rtpConfig.grand_type,
      audience: this.rtpConfig.audience,
    };
    return new Promise((resolve, reject) => {
      axios.post(endpoint, qs.stringify(requestBody), {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      }).then((response) => {
        resolve(response);
      }).catch((error) => {
        reject(error);
      });
    });
  };
}
