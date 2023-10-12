<template>
  <div class="container-fluid catalogs content">
    <h1 class="row col-12 text-primary page-title">
      {{ $t("message.header.navigation.data.catalogs") }}
    </h1>
    <div class="row">
      <catalogues-facets
        v-if="useCatalogFacets"
        class="col-md-3 col-12 mb-3 mb-md-0 px-0"
      ></catalogues-facets>
      <section class="col-md col-12">
        <div class="filters-group">
          <div class="row blueBG-mobile-Cat px-3 py-2 p-md-0">
            <div class="col padding-0-mobile">
              <div
                class="input-group flex-column-reverse flex-md-row p-md-0 m-0"
              >
                <div
                  class="
                    input-group-append
                    ec-ds-search-button-container
                    d-flex
                    mt-3
                    d-md-none
                  "
                >
                  <button
                    class="
                      btn btn-sm btn-primary
                      d-flex
                      justify-content-center
                      align-items-center
                      ds-input
                      ec-ds-search-btn
                      w-100
                    "
                    type="button"
                    @click="changeQuery(query)"
                  >
                    <span>Search</span>
                    <i class="material-icons align-bottom ml-1">search</i>
                  </button>
                </div>
                <input
                  type="text"
                  class="
                    form-control
                    rounded-lg
                    ds-input
                    ec-ds-search
                    mt-3 mt-md-0
                  "
                  :aria-label="$t('message.catalogs.searchBar.placeholder')"
                  :placeholder="$t('message.catalogs.searchBar.placeholder')"
                  v-model="query"
                  @keyup.enter="changeQuery(query)"
                  @click="
                    autocompleteData.show =
                      autocompleteData.suggestions.length > 0 &&
                      query.length != 0
                        ? !autocompleteData.show
                        : false
                  "
                />
                <div class="d-flex cursor-pointer">
                  <!-- TABLIST to Dropdown -->
                  <div
                    class="btn-group ec-ds-scope-dropdown "
                    role="group"
                  >
                    <button
                      class="
                        value-display
                        list-group-item
                        col
                        w-100
                        w-md-50
                        flex-row
                        justify-content-between
                        p-0
                        align-items-center
                        ec-ds-scope-dropdown-inlay
                      "
                      id="btnGroupDrop2"
                      type="button"
                      data-toggle="dropdown"
                      aria-expanded="false"
                    >
                      <div
                        class="
                          pl-2
                          h-md-100
                          d-flex
                          align-items-center
                          ds-input
                          font-weight-bold
                          ec-ds-scope-text

                        "
                      >
                        {{ $t("message.header.navigation.data.catalogs") }}
                      </div>
                      <div class="ecl-select__icon ec-ds-scope-select">
                        <svg
                          class="
                            ecl-icon ecl-icon--s
                            position-absolute
                            ecl-icon--rotate-180
                            ecl-select__icon-shape
                            ec-ds-scope-arrow
                          "
                          focusable="false"
                          aria-hidden="true"
                        >
                          <use
                            xlink:href="../assets/img/ecl/icons.svg#corner-arrow"
                          ></use>
                        </svg>
                      </div>
                    </button>
                    <ul
                      class="dropdown-menu ec-ds-dropdown-items w-100 w-md-auto"
                      aria-labelledby="btnGroupDrop2"
                    >
                      <!-- <li class="nav-item mb-0" role="tab"
                                                :title="$t('message.tooltip.datasets')"
                                                data-toggle="tooltip"
                                                data-placement="top"> -->

                      <!-- <li class="nav-item mb-0" role="tab"
                                                :title="$t('message.tooltip.catalogues')"
                                                data-toggle="tooltip"
                                                data-placement="top"> -->
                      <li class="nav-item mb-0" role="tab">
                        <router-link
                          :to="{
                            name: 'Catalogues',
                            query: { locale: $route.query.locale },
                          }"
                          class="nav-link router-link-active"
                          role="presentation"
                        >
                          {{ $t("message.header.navigation.data.catalogs") }}
                        </router-link>
                      </li>
                      <li class="nav-item mb-0" role="tab">
                        <router-link
                          :to="{
                            name: 'Datasets',
                            query: { locale: $route.query.locale },
                          }"
                          class="nav-link router-link-inactive"
                          role="presentation"
                        >
                          {{ $t("message.header.navigation.data.datasets") }}
                        </router-link>
                      </li>
                      <li class="nav-item mb-0" role="tab">
                        <!-- <a
                                                    :href="`/${this.$route.query.locale}/search?term=${query}&searchdomain=site`"
                                                    class="nav-link router-link-inactive"
                                                    role="presentation"
                                                    :title="$t('message.tooltip.editorialContent')"
                                                    data-toggle="tooltip"
                                                    data-placement="top">
                                                    {{ $t('message.searchTabs.editorialContent') }}
                                                </a> -->
                        <a
                          :href="`/${this.$route.query.locale}/search?term=${query}&searchdomain=site`"
                          class="nav-link router-link-inactive hideElement"
                          role="presentation"
                        >
                          {{ $t("message.searchTabs.editorialContent") }}
                        </a>
                      </li>
                    </ul>
                  </div>
                </div>
                <div
                  class="
                    input-group-append
                    ec-ds-search-button-container
                    d-md-flex d-none
                  "
                >
                  <button
                    class="
                      btn btn-sm btn-primary
                      d-flex
                      align-items-center
                      ds-input
                      ec-ds-search-btn
                    "
                    type="button"
                    @click="changeQuery(query)"
                  >
                    <i class="material-icons align-bottom ml-1">search</i>
                  </button>
                </div>
                <div class="suggestion-list-group" v-if="autocompleteData.show">
                  <ul class="list-group suggestion-list">
                    <button
                      class="list-group-item list-group-item-action"
                      v-for="suggestion in autocompleteData.suggestions"
                      :key="suggestion.id"
                      @click="handleSuggestionSelection(suggestion)"
                    >
                      {{
                        getTranslationFor(
                          suggestion.title,
                          $route.query.locale,
                          [suggestion.country.id].concat(suggestion.languages)
                        )
                      }}
                    </button>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="ec-search-result">
          <div class="row margin-0-mobile flex-column flex-md-row">
            <!-- Headline & Count  -->
            <div class="ds-result-headline padding-0-mobile col">
              <div>
                {{
                  getLoading
                    ? $t("message.catalogs.loadingMessage")
                    : `${$t(
                        "message.catalogs.countMessage"
                      )} (${getCatalogsCount.toLocaleString("fi")})`
                }}
              </div>
              <!-- <div class="loading-spinner ml-3" v-if="getLoading"></div> -->
            </div>
            <!-- SORT Dropdown  -->
            <div class="ec-sort mr-md-2 mt-0 mt-md-3 ">
              <div class="d-inline align-middle colorPrimarySort font-weight-bold mr-2">Sort by:</div>
              <div
                class="col-right mr-2 d-inline"
                role="group"
                aria-label="Sort Dropdown"
              >
                <div class="btn-group ec-sort-dropdown" role="group">
                  <button
                    class="
                    colorPrimarySort value-display list-group-item col w-100 d-flex flex-row justify-content-md-between p-0 align-items-center
                    "
                    id="btnGroupDrop1"
                    type="button"
                    data-toggle="dropdown"
                    aria-expanded="false"
                  >
                    <div class="pl-2 h-100 d-flex align-items-center">
                      {{ sortSelectedLabel }}
                    </div>
                    <div class="ecl-select__icon ecl-select__icon-Sort">
                      <svg
                        class="
                          ecl-icon ecl-icon--s ecl-icon--rotate-180
                          ecl-select__icon-shape
                        "
                        focusable="false"
                        aria-hidden="true"
                      >
                        <use
                          xlink:href="../assets/img/ecl/icons.svg#corner-arrow"
                        ></use>
                      </svg>
                    </div>
                  </button>
                  <ul
                    class="dropdown-menu ec-ds-dropdown-items"
                    aria-labelledby="btnGroupDrop1"
                  >
                    <button
                      class="dropdown-item"
                      @click="
                        setSortMethod(
                          'modified',
                          'desc',
                          $t('message.sort.lastModified')
                        )
                      "
                    >
                      {{ $t("message.sort.lastUpdated") }}
                    </button>
                    <!--<button
                      class="dropdown-item"
                      @click="
                        setSortMethod(
                          'relevance',
                          'desc',
                          $t('message.sort.relevance')
                        )
                      "
                    >
                      {{ $t("message.sort.relevance") }}
                    </button>-->
                    <button
                      class="dropdown-item"
                      @click="
                        setSortMethod(
                          `title.${$route.query.locale}`,
                          'asc',
                          $t('message.sort.nameAZ')
                        )
                      "
                    >
                      {{ $t("message.sort.nameAZ") }}
                    </button>
                    <button
                      class="dropdown-item"
                      @click="
                        setSortMethod(
                          `title.${$route.query.locale}`,
                          'desc',
                          $t('message.sort.nameZA')
                        )
                      "
                    >
                      {{ $t("message.sort.nameZA") }}
                    </button>
                    <button
                      class="dropdown-item"
                      @click="
                        setSortMethod(
                          'issued',
                          'desc',
                          $t('message.sort.lastCreated')
                        )
                      "
                    >
                      {{ $t("message.sort.lastCreated") }}
                    </button>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <hr class="d-md-flex d-none" />
        </div>
        <selectedFacetsOverview
          v-if="getFacets"
          :selected-facets="getFacets"
          :available-facets="getAvailableFacets"
        ></selectedFacetsOverview>
        <pv-data-info-box
          v-for="catalog in getCatalogs"
          :key="`data-info-box@${catalog.id}`"
          catalog-mode
          :to="{
            name: 'Datasets',
            query: {
              catalog: catalog.id,
              showcatalogdetails: true,
              locale: $route.query.locale,
            },
          }"
          :src="getImg(getCatalogImage(catalog))"
          :dataset="{
            title: getTranslationFor(
              catalog.title,
              $route.query.locale,
              getCatalogLanguages(catalog)
            ),
            description: getTranslationFor(
              catalog.description,
              $route.query.locale,
              getCatalogLanguages(catalog)
            ),
            catalog: getTranslationFor(
              catalog.title,
              $route.query.locale,
              getCatalogLanguages(catalog)
            ),
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
      <div class="column col-12 col-md-9 offset-md-3">
        <pagination
          class="mt-3"
          :items-count="getCatalogsCount"
          :items-per-page="getLimit"
          :get-page="getPage"
          :get-page-count="getPageCount"
          @setPageLimit="setPageLimit"
        ></pagination>
      </div>
    </div>
  </div>
</template>

<script>
import $ from "jquery";
import { mapActions, mapGetters } from "vuex";
import { debounce, has } from "lodash-es";
import {
  CataloguesFacets,
  Pagination,
  SelectedFacetsOverview,
  dateFilters,
  fileTypes,
  helpers,
} from "@piveau/piveau-hub-ui-modules";
const { getImg, getCountryFlagImg, getTranslationFor } = helpers;

export default {
  name: "ECCatalogues",
  dependencies: ["catalogService"],
  components: {
    SelectedFacetsOverview,
    CataloguesFacets,
    Pagination,
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
      title: this.currentSearchQuery
        ? `${this.currentSearchQuery}`
        : `${this.$t("message.header.navigation.data.catalogs")}`,
      meta: [
        {
          name: "description",
          vmid: "description",
          content: `${this.$t(
            "message.header.navigation.data.catalogs"
          )} - data.europa.eu`,
        },
        {
          name: "keywords",
          vmid: "keywords",
          content: `${this.$env.keywords} ${this.$t(
            "message.header.navigation.data.catalogs"
          )}`,
        },
        { name: "robots", content: "noindex, follow" },
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
      query: "",
      sortSelected: "",
      sortSelectedLabel: this.$t("message.sort.nameAZ"),
      currentSearchQuery: this.$route.query.query,
      useCatalogFacets: this.$env.catalogs.facets.useCatalogFacets,
    };
  },
  computed: {
    ...mapGetters("catalogs", [
      "getCatalogs",
      "getCatalogsCount",
      "getFacets",
      "getLimit",
      "getLoading",
      "getOffset",
      "getPage",
      "getPageCount",
      "getAvailableFacets",
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
    ...mapActions("catalogs", [
      "autocompleteQuery",
      "loadCatalogs",
      "loadAdditionalCatalogs",
      "setQuery",
      "setPage",
      "useService",
      "addFacet",
      "setFacets",
      "setFacetOperator",
      "setFacetGroupOperator",
      "setPageCount",
      "setSort",
      "setLimit",
      "setLoading",
    ]),
    has,
    getImg,
    getTranslationFor,
    getCountryFlagImg,
    initLimit() {
      const limit = parseInt(this.$route.query.limit, 10);
      if (limit > 0) this.setLimit(limit);
    },
    setPageLimit(value) {
      this.setLimit(value);
      this.initCatalogues();
    },
    initPage() {
      const page = parseInt(this.$route.query.page, 10);
      if (page > 0) this.setPage(page);
      else this.setPage(1);
    },
    initQuery() {
      let query = this.$route.query.query;
      if (!query) {
        query = "";
        this.setQuery("");
      } else {
        this.query = query;
        this.setQuery(query);
      }
    },
    initSort() {
      let sort = this.$route.query.sort;
      if (sort) {
        sort = sort.split(",")[0].toLowerCase();
        if (sort === "relevance+desc")
          this.sortSelectedLabel = this.$t("message.sort.relevance");
        if (sort.includes("title") && sort.includes("asc"))
          this.sortSelectedLabel = this.$t("message.sort.nameAZ");
        if (sort.includes("title") && sort.includes("desc"))
          this.sortSelectedLabel = this.$t("message.sort.nameZA");
        if (sort === "modified+desc")
          this.sortSelectedLabel = this.$t("message.sort.lastUpdated");
        if (sort === "issued+desc")
          this.sortSelectedLabel = this.$t("message.sort.lastCreated");
        this.sortSelected = this.$route.query.sort;
      }
    },
    initFacetOperator() {
      const op = this.$route.query.facetOperator;
      if (op === "AND" || op === "OR") this.setFacetOperator(op);
    },
    initFacetGroupOperator() {
      // const op = this.$route.query.facetGroupOperator;
      const op = this.$route.query.facetOperator;
      if (op === "AND" || op === "OR") this.setFacetGroupOperator(op);
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
    initCatalogues() {
      this.$nextTick(() => {
        this.$Progress.start();
        this.loadCatalogs({})
          .then(() => {
            this.setPageCount(Math.ceil(this.getCatalogsCount / this.getLimit));
            this.$Progress.finish();
            $('[data-toggle="tooltip"]').tooltip({
              container: "body",
            });
          })
          .catch(() => this.$Progress.fail());
      });
    },
    initInfiniteScrolling() {
      if (this.infiniteScrolling)
        window.addEventListener("scroll", this.onScroll);
    },
    setSortMethod(method, order, label) {
      this.sortSelectedLabel = label;
      if (method === "relevance")
        this.sortSelected = `${method}+${order}, modified+desc, title.${this.$route.query.locale}+asc`;
      if (method === "modified")
        this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      if (method === `title.${this.$route.query.locale}`)
        this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === `title.${this.$route.query.locale}`)
        this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === "issued")
        this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
    },
    changeQuery(query) {
      // this.autocomplete(query);
      this.$router.replace({
        query: Object.assign({}, this.$route.query, { query }),
      });
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
        .catch((error) => {
          // eslint-disable-next-line no-console
          console.log(error);
        });
    },
    handleSuggestionSelection(suggestion) {
      this.$router.push({
        path:
          this.$route.path.slice(-1) === "/"
            ? `${this.$route.path}${suggestion.idName}`
            : `${this.$route.path}/${suggestion.idName}`,
      });
    },
    onScroll() {
      const items = this.$el.querySelectorAll(".catalog");
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
        ? `${this.$env.catalogs.defaultCatalogImagePath}/${
            has(catalog, "country.id")
              ? catalog.country.id
              : this.$env.catalogs.defaultCatalogCountryID
          }`
        : `${this.$env.catalogs.defaultCatalogImagePath}/${
            has(catalog, "id")
              ? catalog.id
              : this.$env.catalogs.defaultCatalogID
          }`;
    },
    getFooterTags(catalog) {
      return [`${has(catalog, "count") ? catalog.count : 0}`];
    },
    getCatalogLanguages(catalog) {
      return has(catalog, "country.id")
        ? [catalog.country.id].concat(catalog.languages)
        : catalog.languages;
    },
    isSortSelectedLabelInDropdown() {
      if (
        this.sortSelectedLabel === this.$t("message.sort.nameAZ") ||
        this.sortSelectedLabel === this.$t("message.sort.nameZA") ||
        this.sortSelectedLabel === this.$t("message.sort.lastCreated")
      ) {
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
        this.$router.replace({
          query: Object.assign({}, this.$route.query, { sort }),
        });
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
    this.initLimit();
    this.initPage();
    this.initQuery();
    this.initSort();
    this.initFacetOperator();
    this.initFacetGroupOperator();
    this.initFacets();
    this.initCatalogues();
    this.initInfiniteScrolling();
  },
  beforeDestroy() {
    $(".tooltip").remove();
    if (this.infiniteScrolling)
      window.removeEventListener("scroll", this.onScroll);
  },
};
</script>

<style lang="scss" scoped>
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
    border-color: #196fd2;
  }
}

.cursor-pointer {
  cursor: pointer;
}

.button-color-grey {
  background-color: rgba(0, 29, 133, 0.1) !important;
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
  border-right: 1px solid black;
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
@media screen and (max-width: 768px) {

  #btnGroupDrop1{
    border: none !important;
  }
}

.hideElement{
    display : none;
}
</style>
