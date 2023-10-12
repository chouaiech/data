/* eslint-disable no-unused-vars */
/* eslint-disable camelcase */
import { createUUID, decodeToken } from './utils';

Cypress.Commands.add('kcFakeLogin', (user, visitUrl = '') => {
  Cypress.log({ name: 'Fake Login' });

  let userDataChainable;
  if (typeof user === 'string') {
    userDataChainable = cy.fixture(`users/${user}`);
  } else {
    userDataChainable = cy.wrap(user, { log: false });
  }

  return userDataChainable.then((userData) => {
    if (!userData.fakeLogin) {
      throw new Error(
        'To use kcFakeLogin command you should define fakeLogin data in fixture',
      );
    }

    const authBaseUrl = Cypress.env('auth_base_url');
    const realm = Cypress.env('auth_realm');
    const {
      account,
      access_token,
      refresh_token,
      id_token,
    } = userData.fakeLogin;

    const {
      access_token: rtpAccessToken,
      refresh_token: rtpRefreshToken,
    } = userData.fakeRtp || {};

    const state = createUUID();
    const { nonce } = decodeToken(access_token);

    const token = {
      access_token,
      expires_in: 300,
      refresh_expires_in: 1800,
      refresh_token,
      token_type: 'bearer',
      id_token,
      'not-before-policy': 0,
      session_state: createUUID(),
      scope: 'openid',
    };

    const rtpToken = {
      ...token,
      access_token: rtpAccessToken,
      refresh_token: rtpRefreshToken,
      token_type: 'Bearer',
      upgraded: false,
    };

    const localStorageObj = {
      state,
      nonce,
      expires: Date() + 3600,
    };

    const localStorageKey = `kc-callback-${state}`;

    window.localStorage.setItem(
      localStorageKey,
      JSON.stringify(localStorageObj),
    );

    // cy.server();

    // cy.route(
    //   "post",
    //   `${authBaseUrl}/realms/${realm}/protocol/openid-connect/token`,
    //   token
    // );

    // cy.route(`${authBaseUrl}/realms/${realm}/account`, account);

    // Use cy.intercept instead of cy.server and cy.route
    cy.intercept('POST', `${authBaseUrl}/realms/${realm}/protocol/openid-connect/token`, token);
    cy.intercept(`${authBaseUrl}/realms/${realm}/account`, account);

    // Mock RTP requests
    cy.intercept({
      method: 'POST',
      url: `${authBaseUrl}/realms/${realm}/protocol/openid-connect/token`,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Authorization: 'Bearer **',
      },
    }, rtpToken);

    // in case visitUrl is an url with a hash, a second hash should not be added to the url
    const joiningCharacter = visitUrl.indexOf('#') === -1 ? '#' : '&';

    const url = `/${visitUrl}${joiningCharacter}state=${state}&session_state=${createUUID()}&code=${createUUID()}`;

    cy.visit(url);
  });
});
