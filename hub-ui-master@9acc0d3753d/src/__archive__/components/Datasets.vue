<template>
  <div class="d-flex flex-column p-0 bg-transparent">
    <sub-navigation>
      <div class="container-fluid justify-content-between">
        <div class="navbar-nav align-items-center justify-content-end">
          <div class="nav-item dropdown">
            <div v-if="useFeed" class="nav-link dropdown-toggle cursor-pointer"
              id="dropdown-feeds" data-toggle="dropdown"
              aria-haspopup="true" aria-expanded="false">
              <ins>{{ $t('message.datasets.datasetsFeed') }}</ins>
            </div>
            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdown-feeds">
              <app-link class="dropdown-item text-decoration-none"
                :path="getFeedLink('rss')"
                :query="getFeedQuery()"
                target="_blank"
                matomo-track-page-view>
                RSS Feed</app-link>
              <app-link class="dropdown-item text-decoration-none"
                :path="getFeedLink('atom')"
                :query="getFeedQuery()"
                target="_blank"
                matomo-track-page-view>
                ATOM Feed</app-link>
            </div>
          </div>
        </div>
      </div>
    </sub-navigation>
    <div class="container-fluid datasets content">
      <h1 class="row col-12 page-title text-primary">{{ $t('message.header.navigation.data.datasets') }}</h1>
      <div class="row">
        <div class="col d-flex d-md-none justify-content-end flex-wrap">
          <button class="btn btn-primary mb-3 text-right text-white" data-toggle="collapse" data-target="#datasetFacets" data-cy="btn-filter-toggle" @click="filterCollapsed = !filterCollapsed">
            {{ $t('message.datasetFacets.title') }}
            <i class="material-icons small-icon align-bottom" v-if="filterCollapsed">arrow_drop_up</i>
            <i class="material-icons small-icon align-bottom" v-else>arrow_drop_down</i>
          </button>
        </div>
        <dataset-facets v-if="useDatasetFacets" class="col-md-3 col-12 mb-3 mb-md-0 px-0 collapse" id="datasetFacets" :dataScope="dataScope"></dataset-facets>
        <section class="col-md-9 col-12">
          <div class="filters-group">
            <div class="row">
              <div class="col">
                <div class="input-group">
                  <input type="text" class="form-control rounded-lg"
                        :aria-label="$t('message.datasets.searchBar.placeholder')"
                        :placeholder="$t('message.datasets.searchBar.placeholder')"
                        v-model="query"
                        @keyup.enter="changeQuery(query)"
                        @click="autocompleteData.show = autocompleteData.suggestions.length > 0 && query.length != 0 ? !autocompleteData.show : false">
                  <div class="input-group-append ml-2">
                    <button class="btn btn-sm btn-primary d-flex align-items-center search-button" type="button" @click="changeQuery(query)">
                      <i class="material-icons align-bottom">search</i>
                    </button>
                  </div>
                  <div class="suggestion-list-group" v-if="autocompleteData.show">
                    <ul class="list-group suggestion-list">
                      <button class="list-group-item list-group-item-action"
                              v-for="suggestion in autocompleteData.suggestions"
                              :key="suggestion.id"
                              @click="handleSuggestionSelection(suggestion)">
                        {{ getTranslationFor(suggestion.title, $route.query.locale, suggestion.languages) }}
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
                          data-placement="top">
                        <router-link
                          :to="{name: 'Datasets', query: { locale: $route.query.locale }}"
                          class="nav-link router-link-active"
                          role="presentation">
                             {{ $t('message.header.navigation.data.datasets') }}
                        </router-link>

                    </li>
                    <li class="nav-item mb-0" role="tab"
                        :title="$t('message.tooltip.catalogues')"
                        data-toggle="tooltip"
                        data-placement="top">
                      <router-link
                        :to="{name: 'Catalogues', query: { locale: $route.query.locale }}"
                        v-if="useCatalogs"
                        class="nav-link router-link-inactive"
                        role="presentation">
                        {{ $t('message.header.navigation.data.catalogs') }}
                      </router-link>
                    </li>
                    <li class="nav-item mb-0" role="tab">
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
                  <div v-if="useSort" class="btn-group border-1 mb-1 double-button" role="group" aria-label="Button group with nested dropdown">
                    <button
                      type="button"
                      class="custom-button pl-2 pr-2 border-radius-start d-flex align-items-center inactive-styles"
                      :class="{'button-color-grey': isSortSelectedLabelActive($t('message.sort.lastUpdated'))}"
                      :title="$t('message.tooltip.lastModified')"
                      data-toggle="tooltip"
                      data-placement="top"
                      @click="setSortMethod('modified', 'desc', $t('message.sort.lastModified'))"
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
          <div class="alert alert-primary mt-3 d-flex flex-row"
              :class="{ 'alert-danger': getDatasetsCount <= 0 && !getLoading}">
            <div>
              {{ getLoading ? $t('message.datasets.loadingMessage'):`${getDatasetsCount.toLocaleString('fi')}
              ${$t('message.datasets.countMessage')}`}}
            </div>
            <div class="loading-spinner ml-3" v-if="getLoading"></div>
          </div>
          <div class="alert alert-warning mt-3 d-flex flex-row" v-if="showScoreDisclaimer">
            <i18n path="message.datasets.scoreDisclaimer" tag="span">
              <app-link path="/mqa" :query="{ locale: $route.query.locale }" target="_blank">
                <i18n path="message.metadata.methodologyPage"></i18n>
              </app-link>
            </i18n>
          </div>
          <!--
          <div class="alert alert-info mt-3" v-if="getGeoBoundsById('modal-map')">
            {{`${$t('message.datasets.geoBoundsMessagePre')}`}}<strong>{{getGeoBoundsById('modal-map')}}</strong>{{`. ${$t('message.datasets.geoBoundsMessageRemove')}`}}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close" @click="resetGeoBounds('modal-map'); loadDatasets({})">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          -->
          <selectedFacetsOverview
          v-if="getFacets"
          :selected-facets="getFacets"/>
          <data-info-box class="dataset" v-if="!getLoading" v-for="dataset in getDatasets" :key="dataset.id"
                        :link-to="`/datasets/${dataset.id}`"
                        :title="getTranslationFor(dataset.title, $route.query.locale, dataset.languages) || dataset.id"
                        :description="getTranslationFor(dataset.description, $route.query.locale, dataset.languages)"
                        :description-length="1000"
                        :body-tags="removeDuplicatesOf(dataset.distributionFormats)"
                        :source="{
                                      sourceImage: getCatalogImage(dataset.catalog),
                                      sourceTitle: dataset.catalog.title,
                                  }"
                        :metadata="{
                                      releaseDate: {
                                        title: $t('message.metadata.created'),
                                        value: dataset.releaseDate,
                                      },
                                      modificationDate: {
                                        title: $t('message.metadata.updated'),
                                        value: dataset.modificationDate,
                                      },
                                    }"
                        :data-cy="`dataset@${dataset.id}`">
          </data-info-box>
          <div class="loading-spinner mx-auto mt-3 mb-3" v-if="getLoading"></div>
        </section>
      </div>
      <div class="row">
        <div class="column col-12 col-md-8 offset-md-4">
          <div class="d-flex flex-row justify-content-center">
            <pagination class="mt-3"
                        v-if="pagination"
                        :items-count="getDatasetsCount"
                        :items-per-page="getLimit"
                        :click-handler="changePageTo"
                        :get-page="this.getPage"
                        :next-button-text="$t('message.pagination.nextPage')"
                        :prev-button-text="$t('message.pagination.previousPage')">
            </pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-undef */
import { mapActions, mapGetters } from 'vuex';
import {
  debounce,
  has,
  groupBy,
  uniqBy,
  toPairs,
  isArray,
} from 'lodash';
import $ from 'jquery';
import fileTypes from '../utils/fileTypes';
import DatasetFacets from './DatasetFacets';
import DataInfoBox from './DataInfoBox';
import Pagination from './Pagination';
import SelectedFacetsOverview from './SelectedFacetsOverview';
import { AppLink, SubNavigation, helpers } from "@piveau/piveau-hub-ui-modules";
const { getTranslationFor, truncate } = helpers;

export default {
  name: 'datasets',
  dependencies: ['DatasetService'],
  components: {
    appLink: AppLink,
    selectedFacetsOverview: SelectedFacetsOverview,
    dataInfoBox: DataInfoBox,
    datasetFacets: DatasetFacets,
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
      title: this.currentSearchQuery ? `${this.currentSearchQuery}` : `${this.$t('message.header.navigation.data.datasets')}`,
      meta: [
        { name: 'description', vmid: 'description', content: `${this.$t('message.header.navigation.data.datasets')} - data.europa.eu` },
        { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.header.navigation.data.datasets')}` },
        { name: 'robots', content: 'noindex, follow' },
      ],
    };
  },
  data() {
    return {
      baseUrl: this.$env.api.baseUrl,
      autocompleteData: {
        suggestions: {},
        show: true,
      },
      debouncedOnBottomScroll: debounce(this.onBottomScroll, 500),
      facetFields: [],
      lang: this.locale,
      query: '',
      sortSelected: '',
      sortSelectedLabel: this.$t('message.sort.relevance'),
      filterCollapsed: true,
      catalogDetailsMode: this.$route.query.showcatalogdetails === 'true',
      catalogAllowed: false,
      useCreateDatasetButton: this.$env.upload.useCreateDatasetButton,
      useCreateCatalogueButton: this.$env.upload.useCreateCatalogueButton,
      useDatasetFacets: this.$env.datasets.facets.useDatasetFacets,
      useSort: this.$env.datasets.useSort,
      useFeed: this.$env.datasets.useFeed,
      useCatalogs: this.$env.datasets.useCatalogs,
    };
  },
  computed: {
    ...mapGetters('datasets', [
      'getDatasets',
      'getDatasetsCount',
      'getFacets',
      'getLimit',
      'getLoading',
      'getOffset',
      'getPage',
      'getPageCount',
      'getAvailableFacets',
      'getSort',
      'getMinScoring',
    ]),
    /**
     * @description Returns the current page.
     * @returns {Number}
     *
     * @deprecated use getPage from datasets store instead
     */
    page() {
      return this.$route.query.page;
    },
    /**
     * @description Returns the active facets.
     * @returns {Object}
     */
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
    currentSearchQuery() {
      return this.$route.query.query;
    },
    showScoreDisclaimer() {
      return this.getMinScoring > 0;
    },
    dataScope() {
      if (!this.$route.query.dataScope) return null;
      if (isArray(this.$route.query.dataScope) && this.$route.query.dataScope.length > 0) return this.$route.query.dataScope[0];
      if (isArray(this.$route.query.dataScope) && this.$route.query.dataScope.length === 0) return null;
      return this.$route.query.dataScope;
    },
  },
  methods: {
    ...mapActions('datasets', [
      'autocompleteQuery',
      'loadDatasets',
      'loadAdditionalDatasets',
      'setQuery',
      'setPage',
      'useService',
      'addFacet',
      'removeFacet',
      'resetGeoBounds',
      'setFacets',
      'setFacetOperator',
      'setFacetGroupOperator',
      'setDataServices',
      'setPageCount',
      'setSort',
      'setLoading',
      'setDataScope',
    ]),
    // The imported Lodash has function. Must be defined in Methods so we can use it in template
    has,
    isArray,
    truncate,
    getTranslationFor,
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
      /* eslint-disable no-underscore-dangle */
      this.$router.push({ path: this.$route.path.slice(-1) === '/' ? `${this.$route.path}${suggestion.idName}` : `${this.$route.path}/${suggestion.idName}` }).catch(() => {});
    },
    changePageTo(page) {
      this.$router.replace({ query: Object.assign({}, this.$route.query, { page }) }).catch(() => {});
      this.scrollTo(0, 0);
    },
    /**
     * @description Handler-function for the scroll event.
     */
    onScroll() {
      const items = this.$el.querySelectorAll('.dataset');
      const lastItem = items[items.length - 1];
      if (lastItem) {
        const lastItemPos = lastItem.getBoundingClientRect();
        if (lastItemPos.bottom - window.innerHeight <= 0) {
          this.debouncedOnBottomScroll();
        }
      }
    },
    /**
     * @description Handler-function when bottom of the page is reached.
     */
    onBottomScroll() {
      this.$nextTick(() => {
        this.$Progress.start();
        this.loadAdditionalDatasets()
          .then(() => {
            this.$Progress.finish();
          })
          .catch(() => {
            this.$Progress.fail();
          });
      });
    },
    /**
     * @description The the current scroll-level to a given point.
     * @param x {Number} - The x-position to scroll to
     * @param y {Number} - The y-position to scroll to
     */
    scrollTo(x, y) {
      window.scrollTo(x, y);
    },
    /**
     * @description Cuts badge format string (max 8 chars)
     * @param label {String} - badge label or id (e.g. csv)
     */
    getBadgeFormat(label) {
      return this.truncate(label, 8, true);
    },
    /**
     * @description Removes the duplicates of the given array
     * @param array {Array} - The array to remove duplicates from
     * @returns {Array}
     */
    removeDuplicatesOf(array) {
      const correctedFormatArray = array.map(format => (
        {
          ...format,
          id: this.getBadgeFormat(format.id),
          label: this.getBadgeFormat(format.label),
        }
      ));
      // sorts after # of occurences (highest occurence first)
      // possibility #1
      const sortedArray = toPairs(groupBy(correctedFormatArray, 'id')).sort((a, b) => b[1].length - a[1].length);
      const onlyFormatObjectsArray = sortedArray.map(arr => arr[1][0]);
      // lodash uniqBy funtion removes duplicate id´s from array of objects
      const uniqById = uniqBy(onlyFormatObjectsArray, 'id');
      const uniqByIdAndLabel = uniqBy(uniqById, 'label');
      return uniqByIdAndLabel;
    },
    initDataScope() {
      this.setDataScope(this.dataScope);
    },
    /**
     * @description Determines the current page.
     */
    initPage() {
      const page = parseInt(this.$route.query.page, 10);
      if (page > 0) this.setPage(page);
      else this.setPage(1);
    },
    /**
     * @description Initialize the query String by checking the route parameters
     */
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
    /**
     * @descritption Initialize the active facets by checking the route parameters
     */
    initFacets() {
      const fields = this.$env.datasets.facets.defaultFacetOrder;
      for (const field of fields) {
        this.facetFields.push(field);
        if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
          this.$router.replace({
            query: Object.assign({}, this.$route.query, { [field]: [] }),
          }).catch(() => {});
        } else {
          for (const facet of this.$route.query[field]) {
            this.addFacet({ field, facet });
          }
        }
      }
    },
    initFacetOperator() {
      // Always set facet operator to AND when in catalog details mode
      if (this.$route.query.showcatalogdetails === 'true') this.setFacetOperator('AND');
      else {
        const op = this.$route.query.facetOperator;
        if (op === 'AND' || op === 'OR') this.setFacetOperator(op);
      }
    },
    initFacetGroupOperator() {
      // The facetGroupOperator should be the same as the facetOperator
      // Always set facet operator to AND when in catalog details mode
      if (this.$route.query.showcatalogdetails === 'true') this.setFacetGroupOperator('AND');
      else {
        const op = this.$route.query.facetOperator;
        if (op === 'AND' || op === 'OR') this.setFacetGroupOperator(op);
      }
    },
    /**
     * @descritption Initialize the active data services facet by checking the route parameters
     */
    initDataServices() {
      const ds = this.$route.query.dataServices;
      if (ds === 'true' || ds === 'false') this.setDataServices(ds);
      else {
        this.setDataServices('false');
      }
    },
    initSort() {
      let sort = this.$route.query.sort;
      if (sort) {
        sort = sort.split(',')[0].toLowerCase();
        if (sort.includes('title')) {
          if (sort.includes('desc')) {
            this.sortSelectedLabel = this.$t('message.sort.nameZA');
            this.setSortMethod(`title.${this.$route.query.locale}`, 'desc', this.$t('message.sort.nameZA'));
          } else {
            this.sortSelectedLabel = this.$t('message.sort.nameAZ');
            this.setSortMethod(`title.${this.$route.query.locale}`, 'asc', this.$t('message.sort.nameAZ'));
          }
        } else {
          if (sort === 'relevance+desc') {
            this.sortSelectedLabel = this.$t('message.sort.relevance');
            this.setSortMethod('relevance', 'desc', this.$t('message.sort.relevance'));
          }
          if (sort === 'modified+desc') {
            this.sortSelectedLabel = this.$t('message.sort.lastUpdated');
            this.setSortMethod('modified', 'desc', this.$t('message.sort.lastUpdated'));
          }
          if (sort === 'issued+desc') {
            this.sortSelectedLabel = this.$t('message.sort.lastCreated');
            this.setSortMethod('issued', 'desc', this.$t('message.sort.lastCreated'));
          }
        }
      } else this.setSort(`relevance+desc, modified+desc, title.${this.$route.query.locale}+asc`);
    },
    getFileTypeColor(format) {
      return fileTypes.getFileTypeColor(format);
    },
    setSortMethod(method, order, label) {
      this.sortSelectedLabel = label;
      if (method === 'relevance') this.sortSelected = `${method}+${order}, modified+desc, title.${this.$route.query.locale}+asc`;
      if (method === 'modified') this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      if (method === `title.${this.$route.query.locale}`) this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === 'issued') this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      return this.sortSelected;
    },
    getFeedLink(format) {
      return `${this.baseUrl}${this.$route.query.locale}/feeds/datasets.${format}`;
    },
    getFeedQuery() {
      const feedQuery = {};
      if (this.currentSearchQuery) feedQuery.q = this.currentSearchQuery;
      if (this.facetsNotEmpty() && JSON.stringify(this.facets)) feedQuery.facets = JSON.stringify(this.facets);
      if (this.getPage) feedQuery.page = Math.max(this.getPage - 1, 0);
      if (this.getLimit) feedQuery.limit = this.getLimit;
      feedQuery.facetOperator = this.$route.query.facetOperator || 'AND';
      feedQuery.facetGroupOperator = this.$route.query.facetOperator || 'AND';
      feedQuery.dataServices = this.$route.query.dataServices || 'false';
      if (this.getSort) feedQuery.sort = this.getSort;

      return feedQuery;
    },
    facetsNotEmpty() {
      return Object.values(this.facets).some(facet => facet.length > 0);
    },
    changeQuery(query) {
      this.$router.replace({ query: Object.assign({}, this.$route.query, { query }, { page: 1 }) }).catch(() => {});
      this.setQuery(query);
    },
    getCatalogImage(catalog) {
      return this.$env.catalogs.useCatalogCountries
        ? `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'country.id') ? catalog.country.id : this.$env.catalogs.defaultCatalogCountryID}`
        : `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'id') ? catalog.id : this.$env.catalogs.defaultCatalogID}`;
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
    /**
     * @description Watcher for active facets
     */
    // eslint-disable-next-line object-shorthand
    facets: {
      handler(facets) {
        this.setFacets(facets);
      },
      deep: true,
    },
    // eslint-disable-next-line object-shorthand
    page(pageStr) {
      const page = parseInt(pageStr, 10);
      if (page > 0) this.setPage(page);
      else this.setPage(1);
    },
    sortSelected: {
      handler(sort) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { sort }) }).catch(() => {});
        this.setSort(sort);
      },
      deep: true,
    },
    dataScope: {
      handler() {
        this.initDataScope();
      },
      deep: true,
    },
  },
  created() {
    this.useService(this.DatasetService);
    this.initDataScope();
    this.initPage();
    this.initQuery();
    this.initFacetOperator();
    this.initFacetGroupOperator();
    this.initDataServices();
    this.initFacets();
    this.$nextTick(() => {
      this.initSort();
      this.$nextTick(() => {
        this.$Progress.start();
        this.loadDatasets({ locale: this.$route.query.locale })
          .then(() => {
            this.setPageCount(Math.ceil(this.getDatasetsCount / this.getLimit));
            this.$Progress.finish();
            $('[data-toggle="tooltip"]').tooltip({
              container: 'body',
            });
          })
          .catch(() => {
            this.$Progress.fail();
          });
      });
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

  .content {
    padding: 30px 30px 0 30px;
    margin-top: 15px;
    margin-bottom: 15px;
    background-color: white;
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
    color: rgba(0, 0, 0, 0.7);

    &:hover {
      background-color: rgb(247, 247, 247);
      color: black;
    }
    &:focus {
      background-color: rgb(247, 247, 247);
      color: black;
    }
  }
  .border-radius-start {
    border-top-left-radius: 1.875rem;
    border-bottom-left-radius: 1.875rem;
  }
  .border-radius-end {
    border-top-right-radius: 1.875rem;
    border-bottom-right-radius: 1.875rem;
  }
  .router-link-active {
    color: #175baf !important;
    border: none !important;
    border-bottom: 2px solid #175baf !important;
  }
  .router-link-inactive {
    color: rgba(0, 0, 0, 0.7);
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

  .dropdown-item {
    &:active {
      background-color: $dropdown-item-active-bg;
    }
  }

  /*** MATERIAL ICONS ***/
  .material-icons.small-icon {
    font-size: 20px;
  }

  @media screen and (min-width:768px) {
    #datasetFacets {
      display:block
    }
  }
</style>
