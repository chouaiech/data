<template>
  <div class="mt-0 d-flex flex-column p-0 bg-transparent">
    <datasets-top-controls
      :facets="facets"
      :getPage="getPage"
      :getLimit="getLimit"
      class="datasets-top-controls"
    />
    <div class="container-fluid datasets content">
      <h1
        class="row col-12 page-title catalog-title text-primary ec-ds-title"
        v-if="showCatalogDetails"
      >
        {{
          getTranslationFor(
            getCatalog.title,
            $route.query.locale,
            getCatalog.languages
          )
        }}
      </h1>
      <h1 class="row col-12 page-title" v-else>
        {{ $t("message.header.navigation.data.datasets") }}
      </h1>
      <div class="row">
        <!-- #Start# Mobile dropdown -->
        <div class=" d-md-none w-100 mx-3 my-2 btn-group ec-ds-scope-dropdown ds-input" role="group">
            <button
          class="

            value-display
            list-group-item
            col
            w-100
            d-flex
            flex-row
            justify-content-between
            p-0
            align-items-center
            ec-ds-scope-dropdown-inlay
          "
          data-toggle="collapse"
            data-target="#datasetFacets"
            data-cy="btn-filter-toggle"
            @click="filterCollapsed = !filterCollapsed"
          id="btnGroupDrop3"
          type="button"

          aria-expanded="false"
        >
          <div
            class="
              pl-2
              h-100
              d-flex
              align-items-center
              font-weight-bold
              ec-ds-scope-text
            "
          >
            <!-- {{ sortSelectedLabel }} -->
            {{ $t("message.datasetFacets.title") }}
          </div>
          <div class="ecl-select__icon ec-ds-scope-select">
            <svg
              class="
                ecl-icon ecl-icon--s ecl-icon--rotate-180
                ecl-select__icon-shape
                ec-ds-scope-arrow
              "
              focusable="false"
              aria-hidden="true"
            >
              <use xlink:href="../assets/img/ecl/icons.svg#corner-arrow"></use>
            </svg>
          </div>
        </button>
        </div>
        <!-- #End# Mobile dropdown -->

        <!--
        <div class="col d-none justify-content-end  flex-wrap">
          <button
            class="btn btn-primary mb-3 text-right text-white"
            data-toggle="collapse"
            data-target="#datasetFacets"
            data-cy="btn-filter-toggle"
            @click="filterCollapsed = !filterCollapsed"
          >
            {{ $t("message.datasetFacets.title") }}
            <i
              class="material-icons small-icon align-bottom"
              v-if="filterCollapsed"
              >arrow_drop_up</i
            >
            <i class="material-icons small-icon align-bottom" v-else
              >arrow_drop_down</i
            >
          </button>
        </div>
        -->
        <datasets-facets
          v-if="useDatasetFacets"
          class="col-md-3 col-12 mb-3 mb-md-0 px-0 collapse"
          id="datasetFacets"
          :dataScope="dataScope"
        ></datasets-facets>
        <section class="col-md-9 col-12">
          <!-- EC FILTER BOX -->
          <e-c-datasets-filters />
          <!-- EC INFO BOX here -->
          <div
            class="ecl-message ecl-message--info ml-3 mb-3"
            v-if="showScoreDisclaimer"
          >
            <svg
              class="ecl-icon ecl-icon--l ecl-message__icon"
              focusable="false"
              aria-hidden="true"
            >
              <use xlink:href="@/assets/img/ecl/icons.svg#information"></use>
            </svg>
            <i18n path="message.datasets.scoreDisclaimer" tag="span">
              <app-link
                path="/mqa"
                :query="{ locale: $route.query.locale }"
                target="_blank"
              >
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
            :selected-facets="getFacets"
            :available-facets="getAllAvailableFacets"
          ></selectedFacetsOverview>
          <template v-if="!getLoading">
            <pv-data-info-box
              v-for="dataset in getDatasets"
              :key="dataset.id"
              :to="`/datasets/${dataset.id}`"
              :src="getImg(getCatalogImage(dataset.catalog))"
              :dataset="{
                title:
                  getTranslationFor(
                    dataset.title,
                    $route.query.locale,
                    dataset.languages
                  ) || dataset.id,
                description: getTranslationFor(
                  dataset.description,
                  $route.query.locale,
                  dataset.languages
                ),
                catalog: getTranslationFor(
                  dataset.catalog.title,
                  $route.query.locale,
                  []
                ),
                createdDate: dataset.releaseDate,
                updatedDate: dataset.modificationDate,
                formats: removeDuplicatesOf(dataset.distributionFormats).filter(
                  (format) => format.id || format.label
                ),
              }"
              :description-max-length="1000"
              :data-cy="`dataset@${dataset.id}`"
              class="mt-3"
            />
          </template>
          <div
            class="loading-spinner mx-auto mt-3 mb-3"
            v-if="getLoading"
          ></div>
        </section>
      </div>
      <div class="row">
        <div class="column col-12 col-md-9 offset-md-3">
          <pagination
            class="mt-3"
            :items-count="getDatasetsCount"
            :items-per-page="getLimit"
            :get-page="getPage"
            :get-page-count="getPageCount"
            @setPageLimit="setPageLimit"
          ></pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable no-undef */
import { mapActions, mapGetters } from "vuex";
import { debounce, has, groupBy, uniqBy, toPairs, isArray } from "lodash-es";
import $ from "jquery";
import ECDatasetsFilters from "@/components/ECDatasetsFilters";
import {
  DatasetsTopControls,
  AppLink,
  SelectedFacetsOverview,
  DatasetsFacets,
  Pagination,
  fileTypes,
  helpers,
} from "@piveau/piveau-hub-ui-modules";
const { getTranslationFor, truncate, getImg } = helpers;

export default {
  name: "ECDatasets",
  dependencies: ["DatasetService"],
  components: {
    DatasetsTopControls,
    AppLink,
    SelectedFacetsOverview,
    DatasetsFacets,
    Pagination,
    ECDatasetsFilters,
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
        : `${this.$t("message.header.navigation.data.datasets")}`,
      meta: [
        {
          name: "description",
          vmid: "description",
          content: `${this.$t(
            "message.header.navigation.data.datasets"
          )}} - data.europa.eu`,
        },
        {
          name: "keywords",
          vmid: "keywords",
          content: `${this.$env.keywords} ${this.$t(
            "message.header.navigation.data.datasets"
          )}}`,
        },
        { name: "robots", content: "noindex, follow" },
      ],
    };
  },
  data() {
    return {
      baseUrl: this.$env.api.baseUrl,
      debouncedOnBottomScroll: debounce(this.onBottomScroll, 500),
      facetFields: [],
      lang: this.locale,
      filterCollapsed: true,
      catalogDetailsMode: this.$route.query.showcatalogdetails === "true",
      catalogAllowed: false,
      useCreateDatasetButton: this.$env.upload.useCreateDatasetButton,
      useCreateCatalogueButton: this.$env.upload.useCreateCatalogueButton,
      useDatasetFacets: this.$env.datasets.facets.useDatasetFacets,
      // useSort: this.$env.datasets.useSort,
    };
  },
  computed: {
    ...mapGetters("catalogDetails", ["getCatalog"]),
    ...mapGetters("datasets", [
      "getDatasets",
      "getDatasetsCount",
      "getFacets",
      "getLimit",
      "getLoading",
      "getOffset",
      "getPage",
      "getPageCount",
      "getAvailableFacets",
      "getAllAvailableFacets",
      "getMinScoring",
    ]),
    showCatalogDetails() {
      return this.$route.query.showcatalogdetails === "true";
    },
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
      if (
        isArray(this.$route.query.dataScope) &&
        this.$route.query.dataScope.length > 0
      )
        return this.$route.query.dataScope[0];
      if (
        isArray(this.$route.query.dataScope) &&
        this.$route.query.dataScope.length === 0
      )
        return null;
      return this.$route.query.dataScope;
    },
  },
  methods: {
    ...mapActions("datasets", [
      "loadDatasets",
      "loadAdditionalDatasets",
      "setPage",
      "useService",
      "addFacet",
      "removeFacet",
      "setFacets",
      "setFacetOperator",
      "setFacetGroupOperator",
      "setDataServices",
      "setPageCount",
      "setLimit",
      "setLoading",
      "setDataScope",
    ]),
    // The imported Lodash has function. Must be defined in Methods so we can use it in template
    has,
    isArray,
    truncate,
    getTranslationFor,
    getImg,
    /**
     * @description Handler-function for the scroll event.
     */
    onScroll() {
      const items = this.$el.querySelectorAll(".dataset");
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
      const correctedFormatArray = array.map((format) => ({
        ...format,
        id: this.getBadgeFormat(format.id),
        label: this.getBadgeFormat(format.label),
      }));
      // sorts after # of occurences (highest occurence first)
      // possibility #1
      const sortedArray = toPairs(groupBy(correctedFormatArray, "id")).sort(
        (a, b) => b[1].length - a[1].length
      );
      const onlyFormatObjectsArray = sortedArray.map((arr) => arr[1][0]);
      // lodash uniqBy funtion removes duplicate id´s from array of objects
      const uniqById = uniqBy(onlyFormatObjectsArray, "id");
      const uniqByIdAndLabel = uniqBy(uniqById, "label");
      return uniqByIdAndLabel;
    },
    initLimit() {
      const limit = parseInt(this.$route.query.limit, 10);
      if (limit > 0) this.setLimit(limit);
    },
    setPageLimit(value) {
      this.setLimit(value);
      this.initDatasets();
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
     * @descritption Initialize the active facets by checking the route parameters
     */
    initFacets() {
      const fields = this.$env.datasets.facets.defaultFacetOrder;
      for (const field of fields) {
        this.facetFields.push(field);
        if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
          this.$router
            .replace({
              query: Object.assign({}, this.$route.query, { [field]: [] }),
            })
            .catch((error) => {
              // eslint-disable-next-line no-console
              console.log(error);
            });
        } else {
          for (const facet of this.$route.query[field]) {
            // do not add duplicates!
            if (!this.getFacets[field]?.includes(facet)) {
              this.addFacet({ field, facet });
            }
          }
        }
      }
    },
    initFacetOperator() {
      // Always set facet operator to AND when in catalog details mode
      if (this.$route.query.showcatalogdetails === "true")
        this.setFacetOperator("AND");
      else {
        const op = this.$route.query.facetOperator;
        if (op === "AND" || op === "OR") this.setFacetOperator(op);
      }
    },
    initFacetGroupOperator() {
      // The facetGroupOperator should be the same as the facetOperator
      // Always set facet operator to AND when in catalog details mode
      if (this.$route.query.showcatalogdetails === "true")
        this.setFacetGroupOperator("AND");
      else {
        const op = this.$route.query.facetOperator;
        if (op === "AND" || op === "OR") this.setFacetGroupOperator(op);
      }
    },
    /**
     * @descritption Initialize the active data services facet by checking the route parameters
     */
    initDataServices() {
      const ds = this.$route.query.dataServices;
      if (ds === "true" || ds === "false") this.setDataServices(ds);
      else {
        this.setDataServices("false");
      }
    },
    initDatasets() {
      this.$nextTick(() => {
        this.$nextTick(() => {
          this.$Progress.start();
          this.loadDatasets({ locale: this.$route.query.locale })
            .then(() => {
              this.setPageCount(
                Math.ceil(this.getDatasetsCount / this.getLimit)
              );
              this.$Progress.finish();
              $('[data-toggle="tooltip"]').tooltip({
                container: "body",
              });
            })
            .catch(() => {
              this.$Progress.fail();
            });
        });
      });
    },
    initInfiniteScrolling() {
      if (this.infiniteScrolling)
        window.addEventListener("scroll", this.onScroll);
    },
    getFileTypeColor(format) {
      return fileTypes.getFileTypeColor(format);
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
    this.initLimit();
    this.initPage();
    this.initFacetOperator();
    this.initFacetGroupOperator();
    this.initDataServices();
    this.initFacets();
    this.initDatasets();
    this.initInfiniteScrolling();
  },
  mounted() {
    // This is supposed to fix the browser issue (https://gitlab.fokus.fraunhofer.de/piveau/organisation/piveau-scrum-board/-/issues/2344)
    if (this.$route.query.refresh === "true") {
      this.$nextTick(() => {
        this.$nextTick(() => {
          this.loadDatasets({ locale: this.$route.query.locale })
            .then(() => {
              this.$router.push({
                query: { locale: this.$route.query.locale },
              });
            })
            .catch(() => {
              this.$Progress.fail();
            });
        });
      });
    }
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

.content {
  padding: 30px 30px 0 30px;
  margin-top: 15px;
  margin-bottom: 15px;
  background-color: white;
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

.material-icons.small-icon {
  font-size: 20px;
}

@media screen and (min-width: 768px) {
  #datasetFacets {
    display: block;
  }
}
</style>
