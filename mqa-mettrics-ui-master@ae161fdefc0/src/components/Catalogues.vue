<template>
  <div>
    <!-- CATALOGUES CONTENT -->
    <div v-if="$route.name === $options.name" class="catalogue">
      <div class="" id="overview-intro">
        <div class="">
          <div class="col-sm px-0">
            <div class="catalogue_intro-section-text">
              <h2 class="ecl-u-mb-xl ecl-u-type-heading-2 ">{{ $t("message.navigation.catalogues") }}</h2>
              <!-- <span class="doc-subtitle">{{ $t("message.catalogue_list.description") }}</span> -->
              <div class="filter-bar ui basic segment grid">
                <div class="ui form">
                  <div class="inline field">
                    <input type="text" id="search-input" class="ecl-text-input ecl-text-input--m"  v-model="search" :placeholder="$t('message.catalogue_list.search.placeholder')" />
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="container" id="catalogue-table">
            <div class="row">
              <div class="col px-0">
                <div class="table-responsive-sm">
                  <div v-if="!isMobile() && !isExplorer()">
                    <table class="ecl-table table table-hover">
                      <!--<thead v-sticky sticky-side="top" id="sticky">-->
                      <thead>
                        <tr class="ecl-table__row">
                          <th v-on:click="sortTable(spatial)" class="ecl-table__header table-title-catalogue sticky" width="110px">
                            <span class="hide">{{spatial}}</span>
                            <span :class="{ 'active': sortColumn === spatial }">{{ $t("message.dashboard.overview.table_title.country") }}</span>
                            <span :class="{ 'up': sortColumn === spatial & ascending === true}"></span>
                            <span :class="{ 'down': sortColumn === spatial & ascending === false}"></span>
                          </th>
                          <th v-on:click="sortTableAsc(name)" class="ecl-table__header table-title-catalogue sticky" width="297px">
                            <span :class="{ 'active': sortColumn === name }">{{ $t("message.dashboard.overview.table_title.name") }}</span>
                            &nbsp;
                            <span :class="{ 'up': sortColumn === name & ascending === true}"></span>
                            <span :class="{ 'down': sortColumn === name & ascending === false}"></span>
                            <span class="hide">{{name}}</span>
                          </th>
                          <th v-on:click="sortTable(description)" class="ecl-table__header table-title-catalogue sticky ecl-table__header" width="403px">
                            <span :class="{ 'active': sortColumn === description }">{{ $t("message.dashboard.overview.table_title.desc") }}</span>
                            &nbsp;
                            <span :class="{ 'up': sortColumn === description & ascending === true}"></span>
                            <span :class="{ 'down': sortColumn === description & ascending === false}"></span>
                            <span class="hide">{{description}}</span>
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        <div v-if="this.loading === true" class="spinner-space">
                          <div class="lds-ring">
                            <div></div>
                            <div></div>
                            <div></div>
                            <div></div>
                          </div>
                        </div>
                        <tr v-for="portal in filteredPortals" :key="portal.id">
                          <td class="spatial-row" width="110px">
                            <div>
                              <img v-if="portal.spatial == 'IOR'" :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon ior" alt="Catalog flag">
                              <img v-else :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon" alt="Catalog flag">
                            </div>
                          </td>
                          <td width="297px"><router-link :to="{ name: 'CatalogueDetailDashboard', params: { id: portal.id }, query: { locale: $i18n.locale }}">{{portal.title}} <span v-if="portal.title.includes('Europe')">(EUROPE)</span><span v-else><span v-if="portal.spatial != null">({{portal.spatial}})</span></span></router-link></td>
                          <td width="403px">{{portal.description }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div v-else-if="isExplorer()">
                    <table class="ecl-table table table-hover">
                      <thead class="ecl-table__head">
                      <tr class="ecl-table__row">
                        <th v-on:click="sortTable(spatial)" class="ecl-table__header table-title-catalogue" width="110px">
                          <span :class="{ 'active': sortColumn === spatial }">{{ $t("message.dashboard.overview.table_title.country") }}</span>
                          &nbsp;
                          <span :class="{ 'up': sortColumn === spatial & ascending === true}"></span>
                          <span :class="{ 'down': sortColumn === spatial & ascending === false}"></span>
                          <span class="hide">{{spatial}}</span>
                        </th>
                        <th v-on:click="sortTableAsc(name)" class="ecl-table__header table-title-catalogue" width="297px">
                          <span :class="{ 'active': sortColumn === name }">{{ $t("message.dashboard.overview.table_title.name") }}</span>
                          &nbsp;
                          <span :class="{ 'up': sortColumn === name & ascending === true}"></span>
                          <span :class="{ 'down': sortColumn === name & ascending === false}"></span>
                          <span class="hide">{{name}}</span>
                        </th>
                        <th v-on:click="sortTable(description)" class="ecl-table__header table-title-catalogue" width="403px">
                          <span :class="{ 'active': sortColumn === description }">{{ $t("message.dashboard.overview.table_title.desc") }}</span>
                          &nbsp;
                          <span :class="{ 'up': sortColumn === description & ascending === true}"></span>
                          <span :class="{ 'down': sortColumn === description & ascending === false}"></span>
                          <span class="hide">{{description}}</span>
                        </th>
                      </tr>
                      </thead>
                      <tbody>
                      <div v-if="this.loading === true" class="spinner-space">
                        <div class="lds-ring" id="spinner-catalogue">
                          <div></div>
                          <div></div>
                          <div></div>
                          <div></div>
                        </div>
                      </div>
                      <tr v-for="portal in filteredPortals" :key="portal.id">
                        <td class="spatial-row" width="110px">
                          <div>
                            <img v-if="portal.spatial == 'ior'" :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon ior" alt="Catalog flag">
                            <img v-else :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon" alt="Catalog flag">
                          </div>
                        </td>
                        <td width="297px"><router-link :to="{ name: 'CatalogueDetailDashboard', params: {id: portal.id, title: portal.title, spatial: portal.spatial, description: portal.description, name: portal.name} , query: { locale: $i18n.locale }}">{{portal.title}} <span v-if="portal.title.includes('Europe')">(EU)</span><span v-else><span v-if="portal.spatial != null">({{portal.spatial}})</span></span></router-link></td>
                        <td width="403px">{{portal.description }}</td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                  <div v-else>
                    <table class="ecl-table table">
                      <thead class="ecl-table__head">
                      <tr class="ecl-table__row">
                        <th @click="sort('spatial')" class="ecl-table__header"> {{ $t("message.dashboard.overview.table_title.country") }}</th>
                        <th @click="sort('name')" class="ecl-table__header"> {{ $t("message.dashboard.overview.table_title.name") }}</th>
                        <th @click="sort('desc')" class="ecl-table__header"> {{ $t("message.dashboard.overview.table_title.desc") }}</th>
                      </tr>
                      </thead>
                      <tbody>
                      <div v-if="this.loading === true" class="spinner-space">
                        <div class="lds-ring">
                          <div></div>
                          <div></div>
                          <div></div>
                          <div></div>
                        </div>
                      </div>
                      <tr v-for="portal in filteredPortals" :key="portal.id">
                        <td>
                          <div>
                            <img v-if="portal.spatial == 'ior'" :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon ior" alt="Catalog flag">
                            <img v-else :src="getCountryFlagImg(portal.spatial)" width="30px" height="30px" class="flag-icon" alt="Catalog flag">
                          </div>
                        </td>
                        <td>
                          <router-link :to="{ name: 'CatalogueDetailDashboard', params: {id: portal.id, title: portal.title, spatial: portal.spatial, description: portal.description, name: portal.name}, query: { locale: $i18n.locale }}">{{portal.title}} <span v-if="portal.title.includes('Europe')">(EU)</span><span v-else><span v-if="portal.spatial != null">({{portal.spatial}})</span></span></router-link>
                        </td>
                        <td>
                          <span class="ecl-u-type-color-grey-100">{{portal.description}}</span>
                        </td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- ROUTER-VIEW -->
    <router-view v-else name="catalogueDetail"></router-view>
  </div>
</template>

<script>
/* eslint-disable */
import VueEvents from 'vue-events'
import Vue from 'vue'
import Sticky from 'vue-sticky-directive'
import {mapActions, mapGetters} from 'vuex'

Vue.use(VueEvents)

export default {
  name: 'Catalogues',
  components: {},
   directives: {
    Sticky
  },
  metaInfo () {
    return {
      title: `${this.$t('message.navigation.catalogues')} - ${this.$t('message.common.site_title')}`
    }
  },
  data () {
    return {
      ascending: true,
      sortColumn: '',
      loading: true,
      spatial: 'spatial',
      search: '',
      currentSort:'name',
      name: 'name',
      description: 'description',
      currentSortDir:'asc',
    }
  },
  mounted () {},
  methods: {
    // import store-actions
    ...mapActions([
      'loadAllCatalogues'
    ]),
    sort: function (s) {
      //if s == current sort, reverse
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir==='asc'? 'desc':'asc'
      }
      this.currentSort = s
    },
    isMobile () {
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
    },
    isExplorer () {
      return navigator.userAgent.indexOf("MSIE ") > -1 || navigator.userAgent.indexOf("Trident/") > -1 || window.navigator.userAgent.indexOf("Edge") > -1
    },
    sortTable: function (x) {
      if (this.sortColumn === x) {
        this.ascending = !this.ascending
      } else {
        this.ascending = false
        this.sortColumn = x
      }
      let ascending = this.ascending
      this.getAllCatalogues.sort(function (a, b) {
        if (a[x] > b[x]) {
          return ascending ? 1 : -1
        } else if (a[x] < b[x]) {
          return ascending ? -1 : 1
        }
        return 0
      })
    },
    sortTableAsc: function (x) {
      if (this.sortColumn === x) {
        this.ascending = !this.ascending
      } else {
        this.ascending = true
        this.sortColumn = x
      }
      let ascending = this.ascending
      this.getAllCatalogues.sort(function (a, b) {
        if (a[x] > b[x]) {
          return ascending ? 1 : -1
        } else if (a[x] < b[x]) {
          return ascending ? -1 : 1
        }
        return 0
      })
    },
    init () {
      this.addSortableValues(this.filteredPortals)
      this.sortTableAsc(this.name)
    },
    addSortableValues (arr) {
      // Workaround Solution, all other cleaner ideas didn't work.
      for (var x = 0; x < arr.length; x++) {
        let catalogue = this.getAllCatalogues[x]
        Vue.set(this.getAllCatalogues[x], 'name', catalogue.info.title)
        Vue.set(this.getAllCatalogues[x], 'spatial', catalogue.info.spatial)
        Vue.set(this.getAllCatalogues[x], 'description', catalogue.info.description)
        // Set EU as default spatial value
        if (catalogue.spatial === null) Vue.set(this.getAllCatalogues[x], 'spatial', 'EUROPE')
      }
    },
    getCountryFlagImg (countryId) {
      let img
      try {
        img = require(`@/assets/img/flags/${countryId.toLowerCase()}.png`)
      } catch (err) {
        img = require('@/assets/img/flags/europe.png')
      }
      return img
    }
  },
  computed: {
    // import store-getters
    ...mapGetters([
      'getAllCatalogues'
    ]),
    filteredPortals: function () {
      let matcher = new RegExp(this.search, 'i')
      return this.getAllCatalogues.filter(function (portal) {
        return matcher.test([portal.info.title, portal.info.id, portal.info.description, portal.info.spatial])
      }).map(function (portal) {
        return portal.info
      })
    },
    sortedPortals: function () {
      return this.filteredPortals.sort((a, b) => {
        let modifier = 1
        if (this.currentSortDir === 'desc') modifier = -1
        if (a[this.currentSort] < b[this.currentSort]) return -1 * modifier
        if (a[this.currentSort] > b[this.currentSort]) return 1 * modifier
        return 0
      })
    }
  },
  created () {
    this.loading = true
    if (this.$route.name === this.$options.name) {
      this.loadAllCatalogues()
        .then(() => {
          this.loading = false;
          this.init()
        })
        .catch(() => this.loading = false)
        .finally(() => this.loading = false)
    } else this.loading = false
  }
}
</script>

<style lang="scss" scoped>
@import "../styles/metrics-style.scss";
@import "~bootstrap/scss/bootstrap";
.ior {
  opacity: 0.8;
}
.hide {
  visibility: hidden;
  font-size: 0;
}
.sticky {
   position: sticky;
  position: -webkit-sticky;
  top: 0;//52px;
  background-color: #fff;
  z-index: 5;
}
.sticky::after{
  content: '';
  width: 100%;
  height: 2px;
  position: absolute;
  bottom: 0;
  left: 0;
  //background: #dee2e6;
}
.table thead th{
  border-bottom: none;
}
.ecl-table {
  border: 1px solid #EBEBEB;
}
tbody a {
  color: #004494!important;
}
#search-input {
  width: 100%!important;
  border: 1px solid #404040!important;
  padding: 17px 16px;
}
@media only screen and (max-width: 1199.9px) {
  .sticky {
    top: 280px;
  }
}
@media only screen and (max-width: 767.9px) {
  .sticky {
    top: 127px;
  }
}
@media only screen and (max-width: 621.9px) {
  .sticky {
    top: 149px;
  }
}
@media only screen and (max-width: 575.9px) {
  .sticky {
    top: 0;
  }
}

</style>
