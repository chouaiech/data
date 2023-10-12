<template>
  <div class="filters-group ds">
    <!-- DESKTOP Version -->
    <!-- <div class="d-none d-md-block"> (Disabled for the first release on ppe2) -->
    <div class="">
      <div class="row margin-0-mobile px-4 py-2  p-md-0 blueBG-mobile">
        <div class="col padding-0-mobile">
          <div class="input-group flex-column-reverse flex-md-row ">
            <!-- #Start# Mobile version searchbutton -->
            <div class="input-group-append ec-ds-search-button-container d-flex mt-3 d-md-none">
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
              <!-- create Weblate object for translations-->
              <span>Search</span>
                <i class="material-icons align-bottom ml-1">search</i>
              </button>
            </div>
            <!-- #End# Mobile version searchbutton -->
            <input
              type="text"
              class="form-control rounded-lg ds-input ec-ds-search mt-3 mt-md-0"
              :aria-label="$t('message.datasets.searchBar.placeholder')"
              :placeholder="$t('message.datasets.searchBar.placeholder')"
              v-model="query"
              @keyup.enter="changeQuery(query)"
              @click="
                autocompleteData.show =
                  autocompleteData.suggestions.length > 0 && query.length > 0
                    ? !autocompleteData.show
                    : false
              "
            />
            <div class="d-flex cursor-pointer">
              <!-- TABLIST to Dropdown -->
              <div class="btn-group ec-ds-scope-dropdown ds-input" role="group">
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
                  id="btnGroupDrop2"
                  type="button"
                  data-toggle="dropdown"
                  aria-expanded="false"
                >
                  <div
                    class="
                      pl-2
                      h-100
                      d-flex
                      ds-input
                      align-items-center
                      font-weight-bold
                      ec-ds-scope-text
                    "
                  >
                    <!-- {{ sortSelectedLabel }} -->
                    {{ $t("message.header.navigation.data.datasets") }}
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
                      <use
                        xlink:href="../assets/img/ecl/icons.svg#corner-arrow"
                      ></use>
                    </svg>
                  </div>
                </button>
                <ul
                  class="dropdown-menu ec-ds-dropdown-items"
                  aria-labelledby="btnGroupDrop2"
                >
                  <!-- <li class="nav-item mb-0" role="tab"
                  :title="$t('message.tooltip.datasets')"
                  data-toggle="tooltip"
                  data-placement="top"> -->
                  <li class="nav-item mb-0" role="tab">
                    <router-link
                      :to="{
                        name: 'Datasets',
                        query: { locale: $route.query.locale },
                      }"
                      class="nav-link router-link-active"
                      role="presentation"
                    >
                      {{ $t("message.header.navigation.data.datasets") }}
                    </router-link>
                  </li>
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
                      v-if="useCatalogs"
                      class="nav-link router-link-inactive"
                      role="presentation"
                    >
                      {{ $t("message.header.navigation.data.catalogs") }}
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
              <!-- <ul class="d-flex justify-content-between flex-wrap-reverse nav nav-tabs" id="myTab" role="tablist">
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
            </ul> -->
            </div>
            <div class="input-group-append ec-ds-search-button-container d-none d-md-flex">
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
                      suggestion.languages
                    )
                  }}
                </button>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <!-- Search Result coloumn -->
      <div class="ec-search-result">
        <div class="row margin-0-mobile flex-column flex-md-row ">
          <!-- Headline & Count  -->
          <div class="ds-result-headline padding-0-mobile col">
            {{
              getLoading
                ? $t("message.datasets.loadingMessage")
                : `${$t("message.datasets.countMessage")}
            (${getDatasetsCount.toLocaleString("fi")})`
            }}
          </div>
          <!-- SORT Dropdown  -->
          <div class="ec-sort mr-md-2 mt-0 mt-md-3 ">
            <div class="d-inline align-middle colorPrimarySort font-weight-bold mr-2" >Sort by:</div>
            <div
              class="col-right mr-md-2 d-inline"
              role="group"
              aria-label="Sort Dropdown"
            >
              <div class="btn-group ec-sort-dropdown" role="group">
                <button
                  class="
                  colorPrimarySort
                    value-display
                    list-group-item
                    col
                    w-100
                    d-flex
                    flex-row
                    justify-content-md-between
                    p-0
                    align-items-center
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
        <hr class="d-none d-md-block"/>
      </div>
    </div>
    <!-- Mobile Version (Disabled for the first PPE2 release) -->
    <!-- <div class="d-block d-md-none">
      [Mobile Search here]
    </div> -->
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { helpers } from "@piveau/piveau-hub-ui-modules";
const { getTranslationFor } = helpers;

export default {
  name: "ECDatasetsFilters",
  data() {
    return {
      query: "",
      autocompleteData: {
        suggestions: {},
        show: true,
      },
      useCatalogs: this.$env.datasets.useCatalogs,
      // useSort: this.$env.datasets.useSort,
      sortSelected: "",
      sortSelectedLabel: this.$t("message.sort.nameAZ"),
    };
  },
  computed: {
    ...mapGetters("datasets", ["getDatasetsCount", "getLoading"]),
  },
  methods: {
    getTranslationFor,
    ...mapActions("datasets", ["setQuery", "autocompleteQuery", "setSort"]),
    /**
     * @description Initialize the query String by checking the route parameters
     */
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
    changeQuery(query) {
      this.$router
        .replace({
          query: Object.assign({}, this.$route.query, { query }, { page: 1 }),
        })
        .catch((error) => {
          // eslint-disable-next-line no-console
          console.log(error);
        });
      this.setQuery(query);
    },
    handleSuggestionSelection(suggestion) {
      /* eslint-disable no-underscore-dangle */
      this.$router
        .push({
          path:
            this.$route.path.slice(-1) === "/"
              ? `${this.$route.path}${suggestion.idName}`
              : `${this.$route.path}/${suggestion.idName}`,
        })
        .catch((error) => {
          // eslint-disable-next-line no-console
          console.log(error);
        });
    },
    initSort() {
      let sort = this.$route.query.sort;
      if (sort) {
        sort = sort.split(",")[0].toLowerCase();
        if (sort.includes("title")) {
          if (sort.includes("desc")) {
            this.sortSelectedLabel = this.$t("message.sort.nameZA");
            this.setSortMethod(
              `title.${this.$route.query.locale}`,
              "desc",
              this.$t("message.sort.nameZA")
            );
          } else {
            this.sortSelectedLabel = this.$t("message.sort.nameAZ");
            this.setSortMethod(
              `title.${this.$route.query.locale}`,
              "asc",
              this.$t("message.sort.nameAZ")
            );
          }
        } else {
          if (sort === "relevance+desc") {
            this.sortSelectedLabel = this.$t("message.sort.relevance");
            this.setSortMethod(
              "relevance",
              "desc",
              this.$t("message.sort.relevance")
            );
          }
          if (sort === "modified+desc") {
            this.sortSelectedLabel = this.$t("message.sort.lastUpdated");
            this.setSortMethod(
              "modified",
              "desc",
              this.$t("message.sort.lastUpdated")
            );
          }
          if (sort === "issued+desc") {
            this.sortSelectedLabel = this.$t("message.sort.lastCreated");
            this.setSortMethod(
              "issued",
              "desc",
              this.$t("message.sort.lastCreated")
            );
          }
        }
      } else
        this.setSort(
          `relevance+desc, modified+desc, title.${this.$route.query.locale}+asc`
        );
    },
    setSortMethod(method, order, label) {
      this.sortSelectedLabel = label;
      if (method === "relevance")
        this.sortSelected = `${method}+${order}, modified+desc, title.${this.$route.query.locale}+asc`;
      if (method === "modified")
        this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      if (method === `title.${this.$route.query.locale}`)
        this.sortSelected = `${method}+${order}, relevance+desc, modified+desc`;
      if (method === "issued")
        this.sortSelected = `${method}+${order}, relevance+desc, title.${this.$route.query.locale}+asc`;
      return this.sortSelected;
    },
    isSortSelectedLabelActive(label) {
      if (label === this.sortSelectedLabel) return true;
      return false;
    },
    isSortSelectedLabelInDropdown() {
      if (
        this.sortSelectedLabel === this.$t("message.sort.nameAZ") ||
        this.sortSelectedLabel === this.$t("message.sort.nameZA") ||
        this.sortSelectedLabel === this.$t("message.sort.lastCreated") ||
        this.sortSelectedLabel === this.$t("message.sort.relevance") ||
        this.sortSelectedLabel === this.$t("message.sort.lastUpdated")
      ) {
        return true;
      }
      return false;
    },
    // autocomplete(query) {
    //   this.autocompleteQuery(query)
    //     .then((response) => {
    //       this.autocompleteData.suggestions = [];
    //       const suggestions = response.data.result;
    //       const displayedSuggestions = [];
    //       for (const ds of suggestions.results) {
    //         displayedSuggestions.push(ds);
    //       }
    //       this.autocompleteData.suggestions = displayedSuggestions;
    //       this.autocompleteData.show = query.length !== 0;
    //     })
    //     .catch(() => {});
    // }
  },
  watch: {
    sortSelected: {
      handler(sort) {
        this.$router
          .replace({ query: Object.assign({}, this.$route.query, { sort }) })
          .catch((error) => {
            // eslint-disable-next-line no-console
            console.log(error);
          });
        this.setSort(sort);
      },
      deep: true,
    },
  },
  created() {
    this.initQuery();
    this.$nextTick(() => {
      this.initSort();
    });
  },
};

</script>

<style lang="scss" scoped>
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
    background-color: var(--dropdown-item-active-bg);
  }
}

.material-icons.small-icon {
  font-size: 20px;
}

.border-radius-start {
  border-top-left-radius: 1.875rem;
  border-bottom-left-radius: 1.875rem;
}

.border-radius-end {
  border-top-right-radius: 1.875rem;
  border-bottom-right-radius: 1.875rem;
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

.custom-dropdown-button {
  border: 1px solid black;
  padding: 0;
  background-color: white;
  outline: none;
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

.search-button {
  border-radius: 100% !important;

  &:hover {
    background-color: #196fd2;
    border-color: #196fd2;
  }
}
@media screen and (max-width: 768px) {
  .ec-ds-dropdown-items{
    width: 100% !important;
  }
  #btnGroupDrop1{
    justify-content: unset !important;
    border: none !important;
  }
  .colorPrimary{
    color:var(--primary)
  }

  .margin-0-mobile {
    margin: 0;
  }
  .ec-ds-search-button-container{
    margin-left:0;
  }
  .ec-ds-scope-dropdown{
    width: 100%;
  }
  .blueBG-mobile{
    background-color: #F2F5F9;
    padding: 15px 30px 15px 30px !important;
    margin: 0 -30px 0 -30px !important;
  }
  .ds-result-headline {
    margin-left: 0;
    margin-top: 20px;
  }
  .padding-0-mobile {
    padding: 0;
  }

}

.hideElement{
    display : none;
}
</style>
