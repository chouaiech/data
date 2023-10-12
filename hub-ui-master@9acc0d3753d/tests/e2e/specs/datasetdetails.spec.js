// type definitions for custom commands like "createDefaultTodos"
// /// <reference types="../support" />

// check this file using TypeScript if available
// @ts-nocheck

import { glueConfig } from '../../../config/user-config';

// @ts-ignore
import { dateFilters } from '@piveau/piveau-hub-ui-modules';

const formatEU = dateFilters.formatEU;

const _ = require('lodash-es');

describe('Dataset-details', () => {
  // afterEach(() => {
  //   // In firefox, blur handlers will fire upon navigation if there is an activeElement.
  //   // Since todos are updated on blur after editing,
  //   // this is needed to blur activeElement after each test to prevent state leakage between tests.
  //   cy.window().then((win) => {
  //     // @ts-ignore
  //     win.document.activeElement.blur();
  //   });
  // });

  context('/datasets/:ds_id', () => {
    const datasetId = 'test-dataset';

    beforeEach(() => {
      cy.setCookie('cookie_consent_drupal', '0');
      cy.intercept({ method: '*', url: `${glueConfig.api.hubUrl}metrics/*.+(rdf|ttl|n3|nt|jsonld)*` }, {
        statusCode: 200,
        body: '__stubbed response__',
      });
      cy.intercept(`${glueConfig.api.qualityBaseUrl}**`, {
        statusCode: 404,
        body: '__stubbed response__',
      }).as('metrics');

      cy.intercept(`${glueConfig.tracker.trackerUrl}${glueConfig.tracker.siteId}.js`, {
        statusCode: 200,
        body: '',
        contentType: 'application/javascript',
      }).as('getTracker');
    });

    describe('Test dataset', () => {
      beforeEach(() => {
        // Pin the current date due to time-dependent outputs
        cy.clock(Date.UTC(2022, 2, 29), ['Date']);
        // cy.intercept(`${glueConfig.api.qualityBaseUrl}**`, { statusCode: 200 });
        cy.intercept('GET', `${glueConfig.api.baseUrl}datasets/${datasetId}*`, { fixture: 'datasets/dataset.json' }).as('getDataset');
        cy.visit(`/datasets/${datasetId}?locale=en`);
      });

      it('should show dataset metadata correctly', () => {

        cy.wait('@getDataset')
          .its('response.body.result')
          .then((dataset) => {
            // @ts-ignore
            cy.getMagicParamBySel('dataset').then((locale) => {
              cy.getBySel('dataset-title').contains(dataset.title[locale]);
              cy.getBySel('dataset-description').should('be.visible');

              cy.log('Expand Additional Information');

              const propertiesShowMoreClass = ".dsd-properties .show-more";
              cy.get(propertiesShowMoreClass)
                .then(($el) => {
                  if (!$el.is(':visible')) {
                    cy.get(propertiesShowMoreClass).click();
                  }
                });

              const fieldsToTest = [
                'source',
                { name: 'modified', mapper: o => formatEU(o) },
                { name: 'issued', mapper: o => formatEU(o) },
                { name: 'language', mapper: o => o.label },
                { name: 'publisher', mapper: o => o.name },
                { name: 'contact_point', mapper: o => o.name },
                { name: 'contact_point', mapper: o => o.telephone },
                { name: 'contact_point', mapper: o => (o.email.startsWith('mailto:') ? o.email.substring(7) : o.email) },
                // { name: 'spatial', mapper: o => JSON.stringify(o.coordinates, null, 1).replace(/\\n/g, '') },
                { name: 'spatial', mapper: o => o.type },
                { name: 'conforms_to', mapper: o => o.resource },
                { name: 'conforms_to', mapper: o => o.label },
                { name: 'provenance', mapper: o => o.resource },
                { name: 'provenance', mapper: o => o.label },
                'identifier',
                { name: 'adms_identifier', mapper: o => o.scheme },
                { name: 'adms_identifier', mapper: o => o.identifier },
                { name: 'access_right', mapper: o => o.label },
                // { name: 'accrual_periodicity', mapper: o => o.label },
                { name: 'creator', mapper: o => o.name },
                { name: 'creator', mapper: o => o.email },
                { name: 'creator', mapper: o => o.homepage },
                'has_version',
                'is_version_of',
                { name: 'temporal', mapper: o => formatEU(o.lte) },
                { name: 'temporal', mapper: o => formatEU(o.gte) },
                'version_info',
                { name: 'version_notes', mapper: o => o[locale] },
              ];

              cy.log('🚩 Test additional information section');
              _.forEach(fieldsToTest, (field) => {
                const fieldName = _.isString(field)
                  ? field
                  : field.name;

                const fieldValue = dataset[fieldName];
                const hasMapper = _.get(field, 'mapper');

                if (!fieldValue) {
                  cy.log(`🔶 Skip field '${fieldName}' because its value is nil`);
                  return;
                }

                cy.log(`🔷 Check field '${fieldName}'`);

                _.forEach(_.isArray(fieldValue) ? fieldValue : [fieldValue], (el) => {
                  const expected = hasMapper
                    ? hasMapper(el)
                    : el;

                  if (expected) {
                    cy.getBySel('additional-information').contains(expected);
                  } else {
                    cy.log(`🔶 Skip field '${fieldName}' because its value is nil`);
                  }
                });
              });
            });
          });
      });

      it('should show citation content', () => {
        // Interact with UI and move time forward so that the citation is loaded
        // properly. The former needs to be done to trigger the app's debounce handler.
        cy.getBySel('citation-dropdown-expand').click().click();
        cy.tick(20000);

        cy.wait('@getDataset')
          .its('response.body.result')
          .then(() => {
            cy.getBySel('citation-dropdown-expand').should('be.visible');

            cy.log('🚩 Test citation');

            cy.getBySel('citation-dropdown-expand').click().then(($dropdown) => {
              // Citation dropdown is expanded

              // Trigger mouseout event so that tooltip doesn't obscure dropdown items
              $dropdown.trigger('mouseout').trigger('mouseleave');

              cy.getBySelLike('citation-dropdown-item').should('be.visible');
              cy.getBySelLike('citation-dropdown-item').first().click( { force: true } ).then(() => {
              // Citation modal open

                cy.getBySel('citation-modal').should('be.visible');

                // Test if the bibliography has loaded content
                cy.getBySel('bibliography').should('not.be.empty');

                cy.get('.csl-entry').invoke('text').then((content) => {
                  cy.log(`🔷 Check citation content: ${content}`);
                  cy.wrap(content).should('equal', 'Example Creator, ‘DCAT-AP 2 Example Dataset’, version 1.0.0, Customs Cooperation Council, 2020 (updated 2020-05-25), accessed 2022-03-29, http://data.europa.eu/88u/dataset/test-dataset');
                });

                // Only electron has access to clipboard API in CI mode
                if (Cypress.browser.name === 'electron') {
                // Test copy citation button
                  cy.getBySel('citation-copy-to-clipboard').should('be.visible');
                  cy.getBySel('citation-copy-to-clipboard').should('not.be.disabled');
                  cy.getBySel('citation-copy-to-clipboard').click();
                  cy.window().its('navigator.clipboard').invoke('readText').should('equal',
                    'Example Creator, ‘DCAT-AP 2 Example Dataset’, version 1.0.0, Customs Cooperation Council, 2020 (updated 2020-05-25), accessed 2022-03-29, http://data.europa.eu/88u/dataset/test-dataset');
                }
              });
            });
          });
      });

      it('should send dataset metadata to piwik', () => {
        cy.wait(['@getDataset', '@getTracker']).then((interceptions) => {
          cy.getBySel('dataset-title').should('be.visible');
          const dataset = interceptions[0].response.body.result;
          cy.waitUntil(() => cy.window().then(win => win.dataLayer
            && win.dataLayer.find(e => e.event_type === 'send_dataset_metadata')),
          {
            errorMsg: 'send_dataset_metadata event not found in dataLayer',
            timeout: 10000,
            interval: 500,
          }).then((event) => {
            cy.wrap(event).should('have.property', 'event', 'analytics_interaction');
            cy.wrap(event).should('have.property', 'dataset_ID', dataset.id);
          });
        });
      });
    });

    describe('Dataset citation', () => {
      const testScenarios = {
        online: {
          description: 'With working DOI service',
          expectedCitations: {},
        },
        offline: {
          description: 'With broken DOI service',
          expectedCitation: {},
        },
      };

      before(() => {
        cy.fixture('citation/expected-citations-online.json').then((expected) => {
          testScenarios.online.expectedCitations = expected.citations;
        });

        cy.fixture('citation/expected-citations-offline.json').then((expected) => {
          testScenarios.offline.expectedCitations = expected.citations;
        });
      });

      beforeEach(() => {
        // Pin the current date due to time-dependent outputs
        cy.clock(Date.UTC(2022, 2, 29), ['Date']);
        // Mock https://doi.org/10.5281/zenodo.827184
        cy.intercept('GET', `${glueConfig.api.baseUrl}datasets/${datasetId}*`, { fixture: 'datasets/dataset-with-doi.json' }).as('getDataset');
      });

      // Test citation contents for all styles against a given citations object
      const testCitation = (citations) => {
        cy.visit(`/datasets/${datasetId}?locale=en`);

        // Interact with UI and move time forward so that the citation is loaded
        // properly. The former needs to be done to trigger the app's debounce handler.
        cy.getBySel('citation-dropdown-expand').click().click();
        cy.tick(20000);

        cy.wait(['@getDataset', '@getCitation'])
          .then(() => {
            const openAndCheckCitation = (style = 'deu', expectedCitation) => {
              cy.getBySel('citation-dropdown-expand').should('be.visible');

              cy.log('🚩 Test citation', style, expectedCitation);

              cy.getBySel('citation-dropdown-expand').click().then(($dropdown) => {
              // Citation dropdown is expanded

                // Trigger mouseout event so that tooltip doesn't obscure dropdown items
                $dropdown.trigger('mouseout').trigger('mouseleave');

                cy.getBySelLike('citation-dropdown-item').should('be.visible');
                cy.getBySel(`"citation-dropdown-item@${style}"`).click({ force: true }).then(() => {
                  // Citation modal open

                  cy.getBySel('citation-modal').should('be.visible');

                  // Test if the bibliography has loaded content
                  cy.getBySel('bibliography').should('not.be.empty');

                  cy.get('.csl-entry').invoke('text').then((content) => {
                    cy.log(`🔷 Check citation content: ${content}`);
                    cy.wrap(content.trim()).should('equal', expectedCitation);

                    // Use jQuery click instead of Cypress click for retrying since the modal
                    // doesn't close if the close button is clicked too fast.
                    // See https://www.cypress.io/blog/2019/01/22/when-can-the-test-click/
                    cy.getBySel('citation-modal-close')
                      .should('be.visible')
                      .pipe($el => $el.click())
                      .should($el => expect($el).to.not.be.visible);

                    cy.getBySel('citation-modal').should('not.be.visible');
                  });
                });
              });
            };

            _.each(Object.keys(citations), (style) => {
              openAndCheckCitation(style, citations[style]);
            });
          });
      };

      context('With available DOI service', () => {
        it('should show correct citations', () => {
          cy.intercept('GET', 'https://data.crosscite.org/**', {
            fixture: 'citation/zenodo-response.json',
            headers: {
              'Content-Type': 'application/vnd.citationstyles.csl+json; charset=utf-8',
            },
          }).as('getCitation');
          testCitation(testScenarios.online.expectedCitations);
        });
      });

      context('With unavailable DOI service', () => {
        it('should show correct citations', () => {
        // Should fallback to offline citation creation in case
        // DOI service is down
          cy.intercept('GET', 'https://data.crosscite.org/**', {
            statusCode: 500,
            body: '{"error": "Internal Server Error"}',
          }).as('getCitation');
          testCitation(testScenarios.offline.expectedCitations);
        });
      });
    });
  });
});
