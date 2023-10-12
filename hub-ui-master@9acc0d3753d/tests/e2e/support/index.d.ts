declare namespace Cypress {
  interface Chainable {
    getBySel(selector: string, args) : Chainable
    getBySelLike(selector: string, args) : Chainable
    getMagicParamBySel(selector: string, args) : Chainable
  }
}
