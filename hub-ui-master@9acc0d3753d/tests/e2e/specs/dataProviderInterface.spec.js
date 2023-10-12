import { glueConfig } from '../../../config/user-config';
import {
  DatasetStep1, DatasetStep2, DatasetStep3, DatasetDistributionOverview, DistributionStep1, GeneralOverview,
} from '../support/page-objects/DataProviderInterface/Steps';

// todo: find cause for router navigation cancelled after publishing
Cypress.on('uncaught:exception', () => false);

describe('Data Provider Interface', {
  env: {
    auth_base_url: glueConfig.keycloak.url,
    auth_realm: 'piveau',
    auth_client_id: 'piveau-hub-ui',
  },
  scrollBehavior: 'center',
}, () => {
  context('Logged out user', () => {
    it('Data Provider Interface menu should not be visible', () => {
      cy.visit('/');
      cy.getBySel('dpi-menu').should('not.exist');
    });
  });

  context.skip('Logged in user', () => {
    let inputValues;
    // eslint-disable-next-line
    let datasetJsonldRepresentationPattern;
    let datasetHubResponse;
    let draftsResponse;

    before(() => {
      cy.fixture('dpi/mock-data').then((data) => {
        ({
          inputValues,
          datasetJsonldRepresentationPattern,
          datasetHubResponse,
          draftsResponse,
        } = data);
      });
    });

    beforeEach(() => {
      cy.clearCookies();
      cy.clearLocalStorage();
      cy.disableCookieBanner();

      // intercept requests to the hub
      // Stubs the duplicate dataset ID checker
      cy.intercept('HEAD', `${glueConfig.api.hubUrl}datasets/**`, { statusCode: 404 });
      // Stubs draft creation
      cy.intercept('PUT', `${glueConfig.api.hubUrl}drafts/datasets/**`, { statusCode: 201 }).as('createDraft');

      cy.intercept('GET', `${glueConfig.api.baseUrl}search**`, { statusCode: 404 }).as('search');

      // Mock datasets/drafts being indexed
      cy.intercept('GET', `${glueConfig.api.baseUrl}datasets/${inputValues.datasetId}`, datasetHubResponse).as('getDataset');
      cy.intercept('GET', `${glueConfig.api.hubUrl}drafts/datasets`, draftsResponse).as('getDrafts');

      cy.kcFakeLogin('cypress-user', 'datasets').as('tokens');

      // cy.visit('/');
    });

    const fillDatasetStep1 = () => {
      // Step 1
      cy.get('.form-container').log('🚩 Dataset Step 1');

      DatasetStep1.getTitleInput().type(inputValues.datasetTitle);
      // Step1.getTitleLanguageSelect().select(1);
      // cy.get('#formulate--dpi-datasets-step1-4').type(DATASET_TITLE);
      DatasetStep1.getDatasetIdInput().clear().type(inputValues.datasetId);
      DatasetStep1.getDescriptionTitleInput().type(inputValues.datasetDescription);
      // Step1.getDescriptionLanguageSelect().select(1);
      DatasetStep1.getCatalogSelect().select(1);
    };

    const fillDatasetStep2 = () => {
      // Step 2
      cy.get('.form-container').log('🚩 Dataset Step 2');

      // todo fill with data but for now, skip
    };

    const fillDatasetStep3 = () => {
      // Step 3
      cy.get('.form-container').log('🚩 Dataset Step 3');

      // todo fill with data but for now, skip
    };

    const createDistribution = () => {
      cy.get('.form-container').log('🚩 Datasets Distribution Overview');
    };

    const fillDistributionStep1 = () => {
      cy.get('.form-container').log('🚩 Distribution Step 1');

      DistributionStep1.getAccessUrlSelect().select('url');
      // Force input since the input is not guaranteed to be visible
      // due to animations(?)
      // todo: investigate on why exactly this is happening for inputs
      // that are not visible on init.
      DistributionStep1.getAccessUrlInput().type('http://a.o/', { force: true });

      // todo: input remaining values
      // Distribution Step 2 and Step 3
      DistributionStep1.getNextStepButton().click(); // Go to Step 2
      DistributionStep1.getNextStepButton().click(); // Go to Step 3
      DistributionStep1.getNextStepButton().click(); // Go to Step 4
      DistributionStep1.getNextStepButton().click(); // Go to distribution overview
      DistributionStep1.getNextStepRealButton().click(); // Go to general overview page
    };

    const fillInputPageUntilOverview = () => {
      fillDatasetStep1();
      DatasetStep1.getNextStepButton().click();
      fillDatasetStep2();
      DatasetStep2.getNextStepButton().click();
      fillDatasetStep3();
      DatasetStep3.getNextStepButton().click();
      createDistribution();
      DatasetDistributionOverview.getNewDistributionButton().click();
      fillDistributionStep1();
    };

    it('Create and publish dataset', () => {
      cy.wait('@search');
      // cy.wait('@keycloakToken');
      cy.getBySel('dpi-menu').should('exist');
      cy.get('.dpi-menu-dropup-btn').click();
      cy.get('[data-cy=create-dataset] > .dropdown-item').click();

      // todo: use better selectors for better test stability

      fillInputPageUntilOverview();

      // Stubs dataset creation
      cy.intercept('PUT', `${glueConfig.api.hubUrl}datasets?id=**`, { statusCode: 201 }).as('createDataset');


      // Publish dataset
      GeneralOverview.getPublishButton().click();

      // Check if dataset creation request was sent correctly
      // todo: check body of request
      cy.log('🚩 Check dataset creation request');
      cy.wait('@createDataset').its('request.body').should((jsonld) => {
        expect(jsonld).to.have.property('@context');
      });
      cy.log('🚩 Dataset created');
      // stub dataset creation

      // todo: confirm dataset is published
      cy.get('[data-cy=dataset-title]').should('contain', inputValues.datasetTitle);
    });

    it('Create and draft dataset', () => {
      cy.wait('@search');
      // cy.wait('@keycloakToken');
      cy.getBySel('dpi-menu').should('exist');
      cy.get('.dpi-menu-dropup-btn').click();
      cy.get('[data-cy=create-dataset] > .dropdown-item').click();

      // todo: use better selectors for better test stability

      fillInputPageUntilOverview();

      GeneralOverview.getSaveDraftButton().click();

      // Check if dataset creation request was sent correctly
      // todo: check body of request
      cy.wait('@createDraft');
      cy.log('🚩 Draft created');
      cy.getMagicParamBySel('draft').should('contain', inputValues.datasetId);
    });
  });
});
