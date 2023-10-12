<template>
  <div>
    <p v-for="fieldId in Object.keys(selectedFacets)" :key="fieldId"
       v-if="showSelectedFacet(fieldId)">
      <span>
        {{ `${findFacetFieldTitle(fieldId)}:` }}
      </span>
      <span v-for="(facetId, i) in selectedFacets[fieldId]" :key="i" class="badge badge-pill badge-highlight mr-1">
        {{ findFacetTitle(fieldId, facetId) }}
        <span @click="removeSelectedFacet(fieldId, facetId)" class="close-facet ml-2">&times;</span>
      </span>
    </p>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { helpers } from '@piveau/piveau-hub-ui-modules';
const { getFacetTranslation } = helpers;

export default {
  name: 'SelectedFacetsOverview',
  components: {},
  props: {
    selectedFacets: {
      required: true,
    },
  },
  data() {
    return {
      availableFacets: [],
      showCatalogDetails: false,
    };
  },
  computed: {
    ...mapGetters('datasets', [
      'getAllAvailableFacets',
    ]),
    showCatalogDetailsWatcher() {
      return this.$route.query.showcatalogdetails;
    },
  },
  methods: {
    showSelectedFacet(fieldId) {
      return this.selectedFacets[fieldId].length > 0
      && ((this.showCatalogDetails === (fieldId !== 'catalog')) || !this.showCatalogDetails)
      && this.showEUInternationalCountry(fieldId, this.selectedFacets[fieldId][0])
      && fieldId !== 'dataServices';
    },
    findFacetTitle(fieldId, facetId) {
      try {
        const facetTitle = this.getAllAvailableFacets.find(field => field.id === fieldId).items.find(facet => facet.id === facetId).title;
        return getFacetTranslation(fieldId, facetId, this.$route.query.locale, facetTitle);
      } catch {
        return facetId;
      }
    },
    findFacetFieldTitle(fieldId) {
      try {
        return this.getAllAvailableFacets.find(field => field.id === fieldId).title;
      } catch {
        return fieldId;
      }
    },
    removeSelectedFacet(field, facet) {
      this.toggleFacet(field, facet);
      this.$nextTick(() => {
        this.$emit('update-data');
      });
    },
    /**
     * @description Add/Remove a facet from the routers query parameters.
     * @param field - The field of the facet
     * @param facet - The given facet
     */
    toggleFacet(field, facet) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
        this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: [], page: 1 }) }).catch(() => {});
      }
      let facets = this.$route.query[field].slice();
      if (!Array.isArray(facets)) facets = [facets];
      if (field === 'categories') {
        // Ignore Case for categories
        facet.toUpperCase();
        facets = facets.map(f => f.toUpperCase());
      }
      const index = facets.indexOf(facet);
      if (index > -1) {
        facets.splice(index, 1);
      } else {
        facets.push(facet);
      }
      if (field === 'country' && (facet === 'eu' || facet === 'io')) {
        this.$router.push({ query: Object.assign({}, this.$route.query, { dataScope: [], page: 1 }) }).catch(() => {});
      }
      this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: facets, page: 1 }) }).catch(() => {});
    },
    initShowCatalogDetails() {
      const showCatalogDetails = this.$route.query.showcatalogdetails;
      if (showCatalogDetails === 'true') {
        this.showCatalogDetails = true;
      } else this.showCatalogDetails = false;
    },
    showEUInternationalCountry(fieldId, facetId) {
      if (fieldId !== 'country') return true;
      const dataScope = this.$route.query.dataScope;
      if (dataScope && dataScope !== 'countryData' && (facetId === 'eu' || facetId === 'io')) return false;
      return true;
    },
  },
  watch: {
    showCatalogDetailsWatcher: {
      handler(showCatalogDetails) {
        this.showCatalogDetails = showCatalogDetails;
      },
    },
  },
  created() {
    this.initShowCatalogDetails();
  },
  mounted() {
  },
  beforeDestroy() {
  },
};
</script>

<style lang="scss" scoped>
  @import '../styles/bootstrap_theme';

  .close-facet {
    cursor: pointer;
  }
</style>
