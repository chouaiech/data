<template>
  <div class="container-fluid catalogs content">
    <h1 class="row col-12 page-title text-primary">{{ $t('message.header.navigation.data.catalogs') }}</h1>
    <div class="row">
      <catalog-facets v-if="useCatalogFacets" class="col-md-3 col-12 mb-3 mb-md-0 px-0"></catalog-facets>
      <section class="col-md col-12">
        <div class="filters-group">
          <div class="row">
            <div class="col">
              <div class="input-group">
                <input type="text" class="form-control rounded-lg"
                       :aria-label="$t('message.catalogs.searchBar.placeholder')"
                       :placeholder="$t('message.catalogs.searchBar.placeholder')"
                       v-model="query"
                       @keyup.enter="changeQuery(query)"
                       @click="autocompleteData.show = autocompleteData.suggestions.length > 0 && query.length != 0 ? !autocompleteData.show : false">
                <div class="input-group-append ml-2">
                  <button class="btn btn-sm btn-primary d-flex align-items-center search-button" type="button" @click="changeQuery(query)">
                    <i class="material-icons">search</i>
                  </button>
                </div>
                <div class="suggestion-list-group" v-if="autocompleteData.show">
                  <ul class="list-group suggestion-list">
                    <button class="list-group-item list-group-item-action"
                            v-for="(suggestion) in autocompleteData.suggestions"
                            :key="suggestion.id"
                            @click="handleSuggestionSelection(suggestion)">
                      {{ getTranslationFor(suggestion.title, $route.query.locale, [suggestion.country.id].concat(suggestion.languages)) }}
                    </button>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div class="row mt-3">
            <div class="col">
              <ul class="d-flex justify-content-between flex-wrap-reverse nav nav-tabs" id="myTab" role="tablist">
                <div class="d-flex cursor-pointer">
                  <li class="nav-item mb-0" role="tab"
                      :title="$t('message.tooltip.datasets')"
                      data-toggle="tooltip"
                      data-placement="top"
                  >
                    <router-link
                      :to="{name: 'Datasets', query: { locale: $route.query.locale }}"
                      class="nav-link router-link-inactive"
                      role="presentation">
                      {{ $t('message.header.navigation.data.datasets') }}
                    </router-link>
                  </li>
                  <li class="nav-item mb-0"
                      :title="$t('message.tooltip.catalogues')"
                      data-toggle="tooltip"
                      data-placement="top">
                    <router-link
                      :to="{name: 'Catalogues', query: { locale: $route.query.locale }}"
                      class="nav-link router-link-active"
                      role="presentation">
                      {{ $t('message.header.navigation.data.catalogs') }}
                    </router-link>
                  </li>
                  <li class="nav-item mb-0">
                    <a
                      :href="`/${this.$route.query.locale}/search?term=${query}&searchdomain=site`"
                      class="nav-link router-link-inactive"
                      role="presentation"
                      :title="$t('message.tooltip.editorialContent')"
                      data-toggle="tooltip"
                      data-placement="top">
                      {{ $t('message.searchTabs.editorialContent') }}
                    </a>
                  </li>
                </div>
                <div class="btn-group mb-1 double-button" role="group" aria-label="Button group with nested dropdown">
                  <button
                    type="button"
                    class="custom-button pl-2 pr-2 border-radius-start d-flex align-items-center inactive-styles"
                    :class="{'button-color-grey': isSortSelectedLabelActive($t('message.sort.lastUpdated'))}"
                    :title="$t('message.tooltip.relevance')"
                    data-toggle="tooltip"
                    data-placement="top"
                    @click="setSortMethod('modified', 'desc', $t('message.sort.lastUpdated'))"
                  >
                    {{ $t('message.sort.lastUpdated') }}
                  </button>
                  <button
                    type="button"
                    class="custom-middle-button pl-2 pr-2 d-flex align-items-center inactive-styles"
                    :class="{'button-color-grey': isSortSelectedLabelActive($t('message.sort.relevance'))}"
                    :title="$t('message.tooltip.relevance')"
                    data-toggle="tooltip"
                    data-placement="top"
                    @click="setSortMethod('relevance', 'desc', $t('message.sort.relevance'))"
                  >
                    {{ $t('message.sort.relevance') }}
                  </button>
                  <div class="btn-group" role="group">
                    <button v-if="isSortSelectedLabelInDropdown()" class="button-color-grey d-flex align-items-center custom-dropdown-button border-radius-end" id="btnGroupDrop1" type="button" data-toggle="dropdown" aria-expanded="false">
                      <div class="pl-2 h-100 d-flex align-items-center">
                        {{ sortSelectedLabel }}
                      </div>
                      <i class="pr-2 material-icons small-icon dropdown-icon">arrow_drop_down</i>
                    </button>
                    <button v-else class="d-flex align-items-center custom-dropdown-button border-radius-end inactive-styles" id="btnGroupDrop1" type="button" data-toggle="dropdown" aria-expanded="false">
                      <div class="pl-2">
                        {{ $t('message.catalogsAndDatasets.more') }}
                      </div>
                      <i class="pr-2 material-icons small-icon dropdown-icon">arrow_drop_down</i>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="btnGroupDrop1">
                      <button class="dropdown-item" @click="setSortMethod(`title.${$route.query.locale}`, 'asc', $t('message.sort.nameAZ'))">
                        {{ $t('message.sort.nameAZ') }}</button>
                      <button class="dropdown-item" @click="setSortMethod(`title.${$route.query.locale}`, 'desc', $t('message.sort.nameZA'))">
                        {{ $t('message.sort.nameZA') }}</button>
                      <button class="dropdown-item" @click="setSortMethod('issued', 'desc', $t('message.sort.lastCreated'))">
                        {{ $t('message.sort.lastCreated') }}</button>
                    </ul>
                  </div>
                </div>
              </ul>
            </div>
          </div>
        </div>
        <div class="alert alert-primary mt-3 d-flex flex-row" :class="{ 'alert-danger': getCatalogsCount <= 0 && !getLoading}">
          <div>
            {{ getLoading ? $t('message.catalogs.loadingMessage'):`${getCatalogsCount.toLocaleString('fi')}
            ${$t('message.catalogs.countMessage')}`}}
          </div>
          <div class="loading-spinner ml-3" v-if="getLoading"></div>
        </div>
        <selectedFacetsOverview :selected-facets="getFacets"></selectedFacetsOverview>
        <pv-data-info-box
          v-for="catalog in getCatalogs"
          :key="`data-info-box@${catalog.id}`"
          catalog-mode
          :to="{name: 'Datasets', query: {catalog: catalog.id, showcatalogdetails: true, locale: $route.query.locale}}"
          :src="getImg(getCatalogImage(catalog))"
          :dataset="{
            title: getTranslationFor(catalog.title, $route.query.locale, getCatalogLanguages(catalog)),
            description:
              getTranslationFor(catalog.description, $route.query.locale, getCatalogLanguages(catalog)),
            catalog: getTranslationFor(catalog.title, $route.query.locale, getCatalogLanguages(catalog)),
            createdDate: null,
            updatedDate: null,
            formats: [],
          }"
          :description-max-length="1000"
          :data-cy="`catalog@${catalog.id}`"
          class="mt-3"
        />
        <div class="loading-spinner mx-auto mt-3 mb-3" v-if="getLoading"></div>
      </section>
    </div>
    <div class="row">
      <div class="column col-12 col-md-8 offset-md-4">
        <div class="d-flex flex-row justify-content-center">
          <pagination class="mt-3"
                      v-if="pagination"
                      :items-count="getCatalogsCount"
                      :items-per-page="getLimit"
                      :click-handler="changePageTo"
                      :get-page="getPage"
                      :next-button-text="$t('message.pagination.nextPage')"
                      :prev-button-text="$t('message.pagination.previousPage')">
          </pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';
import { mapActions, mapGetters } from 'vuex';
import { debounce, has } from 'lodash';
import fileTypes from '../utils/fileTypes';
import CatalogFacets from './CatalogueFacets';
import Pagination from './Pagination';
import SelectedFacetsOverview from './SelectedFacetsOverview';
import {
  helpers,
  dateFilters,
  SubNavigation,
  PvDataInfoBox,
} from '@piveau/piveau-hub-ui-modules';
const { getImg, getCountryFlagImg, getTranslationFor } = helpers;


export default {
  name: 'catalogs',
  dependencies: ['catalogService'],
  components: {
    selectedFacetsOverview: SelectedFacetsOverview,
    PvDataInfoBox,
    catalogFacets: CatalogFacets,
    pagination: Pagination,
    subNavigation: SubNavigation,
  },
  props: {
    infiniteScrolling: {
      type: Boolean,
      default: false,
    },
    pagination: {
      type: Boolean,
      default: true,
    },
  },
  metaInfo() {
    return {
      title: this.currentSearchQuery ? `${this.currentSearchQuery}` : `${this.$t('message.header.navigation.data.catalogs')}`,
      meta: [
        { name: 'description', vmid: 'description', content: `${this.$t('message.header.navigation.data.catalogs')} - data.europa.eu` },
        { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.header.navigation.data.catalogs')}` },
        { name: 'robots', content: 'noindex, follow' },
      ],
    };
  },
  data() {
    return {
      autocompleteData: {
        suggestions: {},
        show: true,
      },
      debouncedOnBottomScroll: debounce(this.onBottomScroll, 500),
      facetFields: [],
      query: '',
      sortSelected: `relevance desc, modified desc, title.${this.$route.query.locale} asc`,
      sortSelectedLabel: this.$t('message.sort.relevance'),
      currentSearchQuery: this.$route.query.query,
      useCatalogFacets: this.$env.catalogs.facets.useCatalogFacets,
    };
  },
  computed: {
    ...mapGetters('catalogs', [
      'getCatalogs',
      'getCatalogsCount',
      'getFacets',
      'getLimit',
      'getLoading',
      'getOffset',
      'getPage',
      'getPageCount',
      'getAvailableFacets',
    ]),
    page() {
      return this.$route.query.page;
    },
    facets() {
      const facets = {};
      for (const field of this.facetFields) {
        let urlFacets = this.$route.query[field];
        if (!urlFacets) urlFacets = [];
        else if (!Array.isArray(urlFacets)) urlFacets = [urlFacets];
        facets[field] = urlFacets;
      }
      return facets;
    },
  },
  methods: {
    ...mapActions('catalogs', [
      'autocompleteQuery',
      'loadCatalogs',
      'loadAdditionalCatalogs',
      'setQuery',
      'setPage',
      'useService',
      'addFacet',
      'setFacets',
      'setFacetOperator',
      'setFacetGroupOperator',
      'setPageCount',
      'setSort',
      'setLoading',
    ]),
    has,
    getImg,
    getTranslationFor,
    getCountryFlagImg,
    initPage() {
      const page = parseInt(this.$route.query.page, 10);
      if (page > 0) this.setPage(page);
      else this.setPage(1);
    },
    initQuery() {
      let query = this.$route.query.query;
      if (!query) {
        query = '';
        this.setQuery('');
      } else {
        this.query = query;
        this.setQuery(query);
      }
    },
    initSort() {
      let sort = this.$route.query.sort;
      if (sort) {
        sort = sort.split(',')[0].toLowerCase();
        if (sort === 'relevance+desc') this.sortSelectedLabel = this.$t('message.sort.relevance');
        if (sort.includes('title') && sort.includes('asc')) this.sortSelectedLabel = this.$t('message.sort.nameAZ');
        if (sort.includes('title') && sort.includes('desc')) this.sortSelectedLabel = this.$t('message.sort.nameZA');
        if (sort === 'modified+desc') this.sortSelectedLabel = this.$t('message.sort.lastUpdated');
        if (sort === 'issued+desc') this.sortSelectedLabel = this.$t('message.sort.lastCreated');
        this.sortSelected = this.$route.query.sort;
      }
    },
    initFacetOperator() {
      const op = this.$route.query.facetOperator;
      if (op === 'AND' || op === 'OR') this.setFacetOperator(op);
    },
    initFacetGroupOperator() {
      // const op = this.$route.query.facetGroupOperator;
      const op = this.$route.query.facetOperator;
      if (op === 'AND' || op === 'OR') this.setFacetGroupOperator(op);
    },
    initFacets() {
      const fields = this.$env.catalogs.facets.defaultFacetOrder;
      for (const field of fields) {
        this.facetFields.push(field);
        if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
          this.$router.replace({
            query: Object.assign({}, this.$route.query, { [field]: [] }),
          });
        } else {
          for (const facet of this.$route.query[field]) {
            this.addFacet({ field, facet });
          }
        }
      }
    },
    setSortMethod(method, order, label) {
      this.sortSelectedLabel = label;
      if (method === 'relevance') this.sortSelected = `${method}+${order}, modified+desc, title.${this.$route.query.locale}+asc`;
      if (method === 'modified') this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      if (method === `title.${this.$route.query.locale}`) this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === `title.${this.$route.query.locale}`) this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === 'issued') this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
    },
    changeQuery(query) {
      // this.autocomplete(query);
      this.$router.replace({ query: Object.assign({}, this.$route.query, { query }) });
      this.setQuery(query);
    },
    autocomplete(query) {
      this.autocompleteQuery(query)
        .then((response) => {
          this.autocompleteData.suggestions = [];
          const suggestions = response.data.result;
          const displayedSuggestions = [];
          for (const ds of suggestions.results) {
            displayedSuggestions.push(ds);
          }
          this.autocompleteData.suggestions = displayedSuggestions;
          this.autocompleteData.show = query.length !== 0;
        })
        .catch(() => {});
    },
    handleSuggestionSelection(suggestion) {
      this.$router.push({ path: this.$route.path.slice(-1) === '/' ? `${this.$route.path}${suggestion.idName}` : `${this.$route.path}/${suggestion.idName}` });
    },
    changePageTo(page) {
      this.$router.replace({ query: Object.assign({}, this.$route.query, { page }) });
      this.scrollTo(0, 0);
    },
    scrollTo(x, y) {
      window.scrollTo(x, y);
    },
    onScroll() {
      const items = this.$el.querySelectorAll('.catalog');
      const lastItem = items[items.length - 1];
      if (lastItem) {
        const lastItemPos = lastItem.getBoundingClientRect();
        if (lastItemPos.bottom - window.innerHeight <= 0) {
          this.debouncedOnBottomScroll();
        }
      }
    },
    onBottomScroll() {
      this.$nextTick(() => {
        this.$Progress.start();
        this.setLoading(true);
        this.loadAdditionalCatalogs()
          .then(() => {
            this.$Progress.finish();
            this.setLoading(false);
          })
          .catch(() => {
            this.$Progress.fail();
            this.setLoading(false);
          });
      });
    },
    removeDuplicatesOf(array) {
      return [...new Set(array)];
    },
    getFileTypeColor(format) {
      return fileTypes.getFileTypeColor(format);
    },
    filterDateFormatUS(date) {
      return dateFilters.formatUS(date);
    },
    filterDateFormatEU(date) {
      return dateFilters.formatEU(date);
    },
    filterDateFromNow(date) {
      return dateFilters.fromNow(date);
    },
    getCatalogLink(catalog) {
      return `/datasets?catalog=${catalog.id}&showcatalogdetails=true`;
    },
    getCatalogImage(catalog) {
      return this.$env.catalogs.useCatalogCountries
        ? `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'country.id') ? catalog.country.id : this.$env.catalogs.defaultCatalogCountryID}`
        : `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'id') ? catalog.id : this.$env.catalogs.defaultCatalogID}`;
    },
    getFooterTags(catalog) {
      return [`${has(catalog, 'count') ? catalog.count : 0}`];
    },
    getCatalogLanguages(catalog) {
      return has(catalog, 'country.id') ? [catalog.country.id].concat(catalog.languages) : catalog.languages;
    },
    isSortSelectedLabelInDropdown() {
      if (this.sortSelectedLabel === this.$t('message.sort.nameAZ')
        || this.sortSelectedLabel === this.$t('message.sort.nameZA')
        || this.sortSelectedLabel === this.$t('message.sort.lastCreated')) {
        return true;
      }
      return false;
    },
    isSortSelectedLabelActive(label) {
      if (label === this.sortSelectedLabel) return true;
      return false;
    },
  },
  watch: {
    facets: {
      handler(facets) {
        this.setFacets(facets);
      },
      deep: true,
    },
    page(pageStr) {
      const page = parseInt(pageStr, 10);
      if (page > 0) this.setPage(page);
      else this.setPage(1);
    },
    sortSelected: {
      handler(sort) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { sort }) });
        this.setSort(sort);
      },
      deep: true,
    },
    $route(to) {
      this.currentSearchQuery = to.query.query;
    },
  },
  created() {
    this.useService(this.catalogService);
    this.initPage();
    this.initQuery();
    this.initSort();
    this.initFacetOperator();
    this.initFacetGroupOperator();
    this.initFacets();
    this.$nextTick(() => {
      this.$Progress.start();
      this.loadCatalogs({})
        .then(() => {
          this.setPageCount(Math.ceil(this.getCatalogsCount / this.getLimit));
          this.$Progress.finish();
          $('[data-toggle="tooltip"]').tooltip({
            container: 'body',
          });
        })
        .catch(() => this.$Progress.fail());
    });
    if (this.infiniteScrolling) window.addEventListener('scroll', this.onScroll);
  },
  mounted() {
  },
  beforeDestroy() {
    $('.tooltip').remove();
    if (this.infiniteScrolling) window.removeEventListener('scroll', this.onScroll);
  },
};
</script>

<style lang="scss" scoped>
@import '../styles/bootstrap_theme';
@import '../styles/utils/css-animations';

.alert-primary {
  color: #042648;
  background-color: #cddbe8;
  border-color: #baccdf;
}

.page-title {
  // maybe we can change custom theme h1 to 3rem
  font-size: 3rem;
  margin-bottom: 15px;
}

.search-button {
  border-radius: 100% !important;

  &:hover {
    background-color: #196fd2;
    border-color: #196fd2
  }
}

.cursor-pointer {
  cursor: pointer;
}
.button-color-grey {
  background-color: rgba(0, 29, 133,0.1) !important;
  color: black !important;
}
.custom-button {
  border: 1px solid black;
  background-color: white;
  outline: none;
}
.custom-middle-button {
  border: 1px solid black;
  border-left: none;
  background-color: white;
  outline: none;
}
.custom-dropdown-button {
  border: 1px solid black;
  padding: 0;
  background-color: white;
  outline: none;
}
.inactive-styles {
  color: rgba(0, 0, 0, 0.5);

  &:hover {
    background-color: rgb(247, 247, 247);
    color: black;
  }
  &:focus {
    background-color: rgb(247, 247, 247);
    color: black;
  }
}
.custom-border-right {
  border-right: 1px solid black;
}
.border-radius-start {
  border-top-left-radius: 1.875rem;
  border-bottom-left-radius: 1.875rem;
}
.border-radius-end {
  border-top-right-radius: 1.875rem;
  border-bottom-right-radius: 1.875rem;
}
.border-right-custom {
  padding-right: 0.5rem;
  border-right: 1px solid black
}
.router-link-active {
  color: #175baf !important;
  border: none !important;
  border-bottom: 2px solid #175baf !important;
}
.router-link-inactive {
  color: #7f7f7f;
  border: none !important;
  &:hover {
    color: #175baf;
  }
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
</style>
