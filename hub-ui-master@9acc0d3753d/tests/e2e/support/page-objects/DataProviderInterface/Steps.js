function generateSelector(property, group, el) {
  return `.--dpi-${property}-${group} ${el}`;
}

// Essential Properties
export const DatasetStep1 = {
  getTitleInput: () => cy.get('[identifier=title]'),
  // getTitleLanguageSelect: () => cy.get(generateSelector('datasets', 'title', 'select')),
  getDatasetIdInput: () => cy.get('#datasetIDForm'),
  getDescriptionTitleInput: () => cy.get('[identifier=description]'),
  // getDescriptionLanguageSelect: () => cy.get(generateSelector('datasets', 'description', 'select')),
  getCatalogSelect: () => cy.get('[identifier=catalog]'),
  getPublisherInput: () => cy.get('[identifier=publisher]'),
  getCreatedSelect: () => cy.get('[identifier=issued]'),
  getModifiedSelect: () => cy.get('[identifier=modified]'),
  getNextStepButton: () => cy.get(':nth-child(2) > #nav > .right-form-nav > .submit-label'),
};

// Advised Properties
export const DatasetStep2 = {
  getNextStepButton: () => cy.get(':nth-child(1) > #nav > .right-form-nav > .submit-label'),
};

// Additional Properties
export const DatasetStep3 = {
  getNextStepButton: () => cy.get(':nth-child(2) > #nav > .right-form-nav > .submit-label'),
};

export const DatasetDistributionOverview = {
  getNewDistributionButton: () => cy.get(' .dist-list button'),
};

export const DistributionStep1 = {
  getAccessUrlSelect: () => cy.get('[id=formulate--dpi-distributions-step1-0-5]'), // find a better selector
  getAccessUrlInput: () => cy.get('#formulate--dpi-distributions-step1-0-19').last(),
  // getTitleInput: () => cy.get('[id=title]'), // differentiate from accessService title
  // getDescriptionInput: () => cy.get('[id=description]'), // differentiate from accessService description

  getNextStepButton: () => cy.get(':nth-child(2) > #nav > .right-form-nav > .submit-label'),
  getNextStepRealButton: () => cy.get('[id=formulate--dpi-distributions-distoverview-0-4]'), // find better selector
}

export const GeneralOverview = {
  getPublishButton: () => cy.get('#formulate--dpi-datasets-overview-3'),
  getSaveDraftButton: () => cy.get('#formulate--dpi-datasets-overview-4'),
};
