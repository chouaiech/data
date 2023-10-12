<template>
  <div class="container catalog-facets">
    <div class="row mx-3 mr-md-0">
      <div class="col">
        <div class="row facet-field mb-3">
          <facet
            :header="$t('message.datasetFacets.settings')"
            :items="[]"
            :toolTipTitle="$t('message.helpIcon.settings')"
            class="col pr-0"
          >
            <template #after>
              <div class="form-group list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                {{ $t('message.datasetFacets.operator') }}
                <span class="ml-2 d-flex flex-wrap">
                  <div class="custom-control custom-radio">
                    <input type="radio" id="radio-and" name="radio-facet-operator" class="custom-control-input" @click="changeFacetOperator(FACET_OPERATORS.and)" :checked="getFacetOperator === FACET_OPERATORS.and">
                    <label class="custom-control-label" for="radio-and">{{ $t('message.datasetFacets.and') }}</label>
                  </div>
                  <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="radio-or" name="radio-facet-operator" @click="changeFacetOperator(FACET_OPERATORS.or)" :checked="getFacetOperator === FACET_OPERATORS.or">
                    <label class="custom-control-label" for="radio-or">{{ $t('message.datasetFacets.or').toUpperCase() }}</label>
                  </div>
                </span>
              </div>
            </template>
          </facet>
        </div>
        <div class="row facet-field mb-3"
              v-for="(field, index) in getSortedFacets"
              :key="index"
              :class="{'mt-3': (index > 0)}">
          <facet
            :header="field.id === 'scoring'
              ? $t('message.header.navigation.data.metadataquality')
              : $t(`message.datasetFacets.facets.${field.id.toLowerCase()}`)"
            :items="sortByCount(field.items, field.id)"
            :minItems="MIN_FACET_LIMIT"
            :maxItems="MAX_FACET_LIMIT"
            class="col pr-0"
            v-slot="{ item: facet }"
          >
            <dataset-facets-item
              class="d-flex facet list-group-item list-group-item-action justify-content-between align-items-center"
              :title="getFacetTranslation(field.id, facet.id, $route.query.locale, facet.title)"
              :count="getFacetCount(field, facet)"
              :class="{active: facetIsSelected(field.id, facet.id)}"
              @click.native="facetClicked(field.id, facet.id)"
            />
          </facet>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import {
  isEmpty,
  isBoolean,
  has,
  isNil,
} from 'lodash';
import Facet from './Facet';
import DatasetFacetsItem from './DatasetFacetsItem';
import { helpers } from '@piveau/piveau-hub-ui-modules';
const { getTranslationFor, getCountryFlagImg, getFacetTranslation } = helpers;


export default {
  name: 'catalogueFacets',
  components: {
    Facet,
    DatasetFacetsItem,
  },
  data() {
    return {
      showCatalogDetails: false,
      catalog: {},
      browser: {
        /* eslint-disable-next-line */
        isIE: /*@cc_on!@*/false || !!document.documentMode,
      },
      defaultFacetOrder: this.$env.catalogs.facets.defaultFacetOrder,
      MIN_FACET_LIMIT: this.$env.catalogs.facets.MIN_FACET_LIMIT,
      MAX_FACET_LIMIT: this.$env.catalogs.facets.MAX_FACET_LIMIT,
      FACET_OPERATORS: this.$env.catalogs.facets.FACET_OPERATORS,
      FACET_GROUP_OPERATORS: this.$env.catalogs.facets.FACET_GROUP_OPERATORS,
    };
  },
  computed: {
    ...mapGetters('catalogs', [
      'getAvailableFacets',
      'getCatalogsCount',
      'getFacetOperator',
      'getFacetGroupOperator',
      'getLimit',
      'getPage',
    ]),
    facetOperatorWatcher() {
      return this.getFacetOperator;
    },
    facetGroupOperatorWatcher() {
      return this.getFacetGroupOperator;
    },
    getSortedFacets() {
      const availableFacets = this.getAvailableFacets;
      const sortedFacets = [];

      this.defaultFacetOrder.forEach((facet) => {
        availableFacets.forEach((field) => {
          if (facet === field.id && field.items.length > 0) sortedFacets.push(field);
        });
      });

      return sortedFacets;
    },
  },
  methods: {
    isEmpty,
    isBoolean,
    has,
    isNil,
    getFacetTranslation,
    getCountryFlagImg,
    getTranslationFor,
    ...mapActions('catalogs', [
      'toggleFacet',
      'addFacet',
      'removeFacet',
      'setFacetOperator',
      'setFacetGroupOperator',
      'setPage',
      'setPageCount',
    ]),
    /**
     * @description Sorts an array of facets by their count.
     * @param {Array<Object>} facets - The facets to sort
     * @param {Number} facet.count - The amount of catalogs having this facet assigned
     * @param {String} facet.name - The name of this facet
     * @returns {Array<Object>}
     */
    sortByCount(facets, fieldId) {
      if (fieldId === 'scoring') return facets;
      return facets.slice().sort((a, b) => {
        const n = b.count - a.count;
        if (n !== 0) return b.count - a.count;
        if (a.name < b.name) return -1;
        return 1;
      });
    },
    /**
     * @description Returns whether a facet is selected or not.
     * @param field - The field of the facet to check.
     * @param facet - The facet to check.
     * @returns {boolean}
     */
    facetIsSelected(field, facet) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, field)) {
        return false;
      }
      let qField = this.$route.query[field];
      if (!Array.isArray(qField)) qField = [qField];
      return qField.indexOf(facet) > -1;
    },
    /**
     * @description Wrapping callback-function for a click on a facet.
     * @param field - The field of the clicked facet
     * @param facet - The clicked facet
     */
    facetClicked(field, facet) {
      this.toggleFacet(field, facet);
      this.resetPage();
    },
    /**
     * @description Add/Remove a facet from the routers query parameters.
     * @param field - The field of the facet
     * @param facet - The given facet
     */
    toggleFacet(field, facet) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
        this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: [] }) });
      }
      let facets = this.$route.query[field].slice();
      if (!Array.isArray(facets)) facets = [facets];
      const index = facets.indexOf(facet);
      if (index > -1) {
        facets.splice(index, 1);
      } else {
        facets.push(facet);
      }
      this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: facets }) });
    },
    changeFacetOperator(op) {
      this.setFacetOperator(op);
      this.setFacetGroupOperator(op);
    },
    /**
     * @description Toggles the facetoperator between 'or'/'and'.
     */
    toggleFacetGroupOperator() {
      let op = this.getFacetGroupOperator;
      op = op === this.FACET_GROUP_OPERATORS.and ? this.FACET_GROUP_OPERATORS.or : this.FACET_GROUP_OPERATORS.and;
      this.setFacetGroupOperator(op);
    },
    resetPage() {
      this.$router.replace({ query: Object.assign({}, this.$route.query, { page: 1 }) });
    },
    triggerResize() {
      // Note: Trigger resize after 200ms when Modal element is visible to properly display the map component
      setTimeout(() => {
        window.dispatchEvent(new Event('resize'));
      }, 200);
    },
    getFacetCount(field, facet) {
      if (field.id === 'scoring') return '';
      return facet.count;
    },
  },
  watch: {
    facetOperatorWatcher: {
      handler(facetOperator) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { facetOperator }) });
      },
    },
    facetGroupOperatorWatcher: {
      handler(facetGroupOperator) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { facetGroupOperator }) });
      },
    },
  },
  created() {},
  mounted() {
  },
};
</script>

<style lang="scss" scoped>

  .facet:hover {
    cursor: pointer;
  }

  .custom-control {
    padding-left: 1.5rem;
    margin-right: 1rem;
  }
  .custom-control-label {
    &::before {
      left: -1.5rem !important;
    }
    &::after {
      left: -1.5rem !important;
    }
  }
  .custom-control-input:checked ~ .custom-control-label::before {
    border-color: var(--primary);
    background-color: var(--primary);
  }
  .map {
    z-index: 0;
  }
  .suggestion-input-group {
    position: relative;
  }
  .suggestion-input {
    position: absolute;
    top: 0;
  }
  .suggestion-list-group {
    position: relative;
    width: 100%;
  }
  .suggestion-list {
    width: 100%;
    position: absolute;
    top: 0;
    z-index: 100;
  }

  .map-modal-button {
    position: absolute;
    bottom: 0;
    right: 0;
  }

  .reset-bounds-button {
    position: absolute;
    bottom: 0;
    left: 0;
  }

  #modal-map-wrapper {
    padding-right: 1.5rem !important;
    padding-left: 1.5rem !important;
  }

</style>
