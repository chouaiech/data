// type definitions for Cypress object "cy"
// /// <reference types="cypress" />

// type definitions for custom commands like "createDefaultTodos"
// /// <reference types="../support" />

// check this file using TypeScript if available
// @ts-nocheck

const _ = require('lodash-es');

describe('DEU piveau hub-ui', () => {
  afterEach(() => {
    // In firefox, blur handlers will fire upon navigation if there is an activeElement.
    // Since todos are updated on blur after editing,
    // this is needed to blur activeElement after each test to prevent state leakage between tests.
    cy.window().then((win) => {
      // @ts-ignore
      win.document.activeElement.blur();
    });
  });

  context('/datasets/', () => {
    beforeEach(() => {
      cy.intercept('GET', '**/search?*', { fixture: 'search.json' }).as('getDatasets');
      cy.visit('/datasets');
    });

    context('Mobile layout', () => {
      beforeEach(() => {
        cy.viewport('iphone-x');
      });

      it('should hide dataset facets behind a toggle button', () => {
        cy.get('.dataset-facets').should('not.be.visible');
        cy.get('[data-cy=btn-filter-toggle]').should('be.visible');
        cy.get('[data-cy=btn-filter-toggle]').click();
        cy.get('.dataset-facets').should('be.visible');
      });
    });

    it('should display dataset information correctly', () => {
      // Test, if (1) the correct number of datasets are displayed,
      // and (2) the first 5 datasets contain the correct title and description
      // in the correct location.

      cy.wait('@getDatasets')
        .its('response.body.result.results')
        .then((datasets) => {
          const numDatasets = datasets.length;
          // Have a maximum number of datasets to test for deterministic runtime.
          // Todo: prepare & test representative datasets to minimize number of
          // datasets to test.
          const numDatasetsToTest = _.min([5, numDatasets]);

          cy.get('[data-cy^="dataset@"]').should('have.length', numDatasets);

          _.each(datasets.slice(0, numDatasetsToTest), ({ id, title, description }) => {
            cy.log('🚩Testing dataset', id, title);
            cy.get(`[data-cy="dataset@${id}"]`).within(() => {
              cy.get('[data-cy=dataset-title]').should('exist');
              cy.get('[data-cy=dataset-description]').should('exist');

              if (title?.en) {
                cy.get('[data-cy=dataset-title]').contains(title.en);
              }
              if (description?.en) {
                cy.get('[data-cy=dataset-description]').contains(_.truncate(description.en, {
                  length: 70,
                  omission: '',
                }));
              }
            });
          });
        });
    });
  });
});
