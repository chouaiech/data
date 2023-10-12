<template>
  <div class="table" id="overview-table">
    <div class="row" id="top-catalogues">
      <div v-if="this.rows === getAllCatalogues.length" class="col-10">
         <p class="ecl-u-type-paragraph ecl-u-type-prolonged-xl m-0 ecl-u-type-color-black">
          {{ $t("message.dashboard.overview.header.allCatalogues" , { number: this.rows}) }}
         </p>
      </div>
      <div v-else-if="this.rows === 20" class="col-10" >
        <p class="ecl-u-type-paragraph ecl-u-type-prolonged-xl m-0 ecl-u-type-color-black">
        {{ $t("message.dashboard.overview.header.topNumberCatalogues", { number: this.rows}) }}
          </p>
      </div>
      <div v-else class="col-10">
        <p class="ecl-u-type-paragraph ecl-u-type-prolonged-xl m-0 ecl-u-type-color-black">
        {{ $t("message.dashboard.overview.header.topNumberCatalogues", { number: this.rows}) }}
          </p>
      </div>
      <div class="col-3" >
        <div class="overview-filter text-nowrap">
          <button class="btn btn-sm btn-tag" v-on:click="showMore(12)" >
            <tag :class="{ 'active': this.rows == 12}">
              {{ $t("message.dashboard.overview.header.topNumber", { number: 12}) }}
            </tag>
          </button>
          <button class="btn btn-sm btn-tag" v-on:click="showMore(20)" >
            <tag :class="{ 'active': this.rows == 20}">
              {{ $t("message.dashboard.overview.header.topNumber", { number: 20}) }}
            </tag>
          </button>
          <button class="btn btn-sm btn-tag" v-on:click="showMore(getAllCatalogues.length)">
            <tag :class="{ 'active': this.rows == getAllCatalogues.length}">
              {{ $t("message.dashboard.overview.header.allCatalogues", { number: 20}) }}
            </tag>
          </button>
        </div>
      </div>
    </div>
    <div>
      <span style="display: block; width:100%; background: #DFE2E6; height: 2px; margin: 12px 0px;"></span>
      <table class="ecl-table table table-hover">
        <thead>
          <tr class="ecl-table__row">
            <th v-on:click="sortTable(sortSpatial)" class="ecl-table__header overview-row-left" width="120px">
              <span class="d-inline-block spatial-mobile" tabindex="0" data-toggle="tooltip" title="Disabled tooltip">
                <span class="hide">{{sortSpatial}}</span>
                <span :class="{ 'active': sortColumn === sortSpatial }">{{ $t("message.dashboard.overview.table_title.country") }}</span>
                &nbsp;
                <span :class="{ 'up': sortColumn === sortSpatial & ascending === true}"></span>
                <span :class="{ 'down': sortColumn === sortSpatial & ascending === false}"></span>
              </span>
            </th>
            <th v-on:click="sortTable(sortTitle)" class="ecl-table__header overview-row-name" width="170px">
              <span class="hide">{{sortTitle}}</span>
              <span :class="{ 'active': sortColumn === sortTitle }">{{ $t("message.dashboard.overview.table_title.name") }}</span>
              &nbsp;
              <span :class="{ 'up': sortColumn === sortTitle & ascending === true }"></span>
              <span :class="{ 'down': sortColumn === sortTitle & ascending === false}"></span>
            </th>
              <th v-on:click="sortTable(sortFindability)" class="overview-row ecl-table__header" width="120px">
              <div v-tooltip.top-center="$t('message.dashboard.findability.title')" v-bind:style="{width: '120px'}">
                <div class="table-title">
                  <div class="weight">
                     <div class="weight-points">100 {{$t('message.dashboard.points')}}</div>
                    <div class="progress flex-row" style="height: 4px;">
                      <div class="progress-bar primary-color-findability" role="progressbar" :style="`width:25%;`"> </div>
                    </div>
                  </div>
                  <span :class="{ 'active': sortColumn === sortFindability }">{{$t('message.dashboard.findability.title') }}<span class="hide">{{sortAccessibility}}</span></span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortFindability & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortFindability & ascending === false}"></span>
                </div>
              </div>
            </th>
            <th v-on:click="sortTable(sortAccessibility)" class="overview-row ecl-table__header" width="120px">
              <div v-tooltip.top-center="$t('message.dashboard.accessibility.title')" v-bind:style="{width: '120px'}">
                <div class="table-title">
                  <div class="weight">
                     <div class="weight-points">100 {{$t('message.dashboard.points')}}</div>
                    <div class="progress flex-row" style="height: 4px;">
                      <div class="progress-bar primary-color-accessibility" role="progressbar" :style="`width:25%;`"> </div>
                    </div>
                  </div>
                  <span :class="{ 'active': sortColumn === sortAccessibility }">{{$t("message.dashboard.accessibility.title") }}<span class="hide">{{sortAccessibility}}</span></span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortAccessibility & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortAccessibility & ascending === false}"></span>
                </div>
              </div>
            </th>
            <th v-on:click="sortTable(sortInteroperability)" class="overview-row ecl-table__header" width="120px">
              <div v-tooltip.top-center="$t('message.dashboard.interoperability.title')" v-bind:style="{width: '120px'}">
                <div class="table-title">
                    <div class="weight">
                     <div class="weight-points">110 {{$t('message.dashboard.points')}}</div>
                    <div class="progress flex-row" style="height: 4px;">
                      <div class="progress-bar primary-color-interoperability" role="progressbar" :style="`width:30%;`"> </div>
                    </div>
                    </div>
                  <span  :class="{ 'active': sortInteroperability === sortInteroperability }">{{ $t("message.dashboard.interoperability.title") }}</span><span class="hide">{{sortInteroperability}}</span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortInteroperability & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortInteroperability & ascending === false}"></span>
                </div>
              </div>
            </th>
            <th v-on:click="sortTable(sortReusability)" class="overview-row ecl-table__header" width="120px">
              <div v-tooltip.top-center="$t('message.dashboard.reusability.title')" v-bind:style="{width: '120px'}" >
                <div class="table-title">
                    <div class="weight">
                     <div class="weight-points">75 {{$t('message.dashboard.points')}}</div>
                    <div class="progress flex-row" style="height: 4px;">
                      <div class="progress-bar primary-color-reusability" role="progressbar" :style="`width:18%;`"> </div>
                    </div>
                    </div>
                  <span :class="{ 'active': sortColumn === sortReusability }">{{ $t("message.dashboard.reusability.title") }}</span>
                  <span class="hide">{{sortReusability}}</span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortReusability & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortReusability & ascending === false}"></span>
                </div>
              </div>
            </th>
            <th v-on:click="sortTable(sortContextuality)" class="overview-row ecl-table__header" width="120px">
              <div v-tooltip.top-center="$t('message.dashboard.contextuality.title')" v-bind:style="{width: '120px'}">
                <div class="table-title">
                    <div class="weight">
                     <div class="weight-points">20 {{$t('message.dashboard.points')}}</div>
                    <div class="progress flex-row" style="height: 4px;">
                      <div class="progress-bar primary-color-contextual" role="progressbar" :style="`width:12%;`"> </div>
                    </div>
                    </div>
                  <span :class="{ 'active': sortColumn === sortContextuality }">{{ $t("message.dashboard.contextuality.title") }}</span>
                  <span class="hide">{{sortContextuality}}</span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortContextuality & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortContextuality & ascending === false}"></span>
                </div>
              </div>
            </th>
            <th v-on:click="sortTable(sortScoring)" class="overview-row ecl-table__header" width="100px">
              <div v-tooltip.top-center="$t('message.dashboard.overview.table_title.scoring')" v-bind:style="{width: '100px'}">
                <div class="table-title">
                   <div class="weight-title">
                     {{$t('message.dashboard.weight')}}
                     </div>
                    <div class="weight">
                    <div class="weight-points">405 {{$t('message.dashboard.points')}} </div>
                   <div class="weight-complete">
                    <div class="progress" style="height: 4px;">
                      <div class="progress-bar primary-color-findability" role="progressbar" :style="`width:25%;`"> </div>
                      <div class="progress-bar primary-color-accessibility" role="progressbar" :style="`width:25%;`"> </div>
                      <div class="progress-bar primary-color-interoperability" role="progressbar" :style="`width:30%;`"> </div>
                      <div class="progress-bar primary-color-reusability" role="progressbar" :style="`width:18%;`"> </div>
                      <div class="progress-bar primary-color-contextual" role="progressbar" :style="`width:12%;`"> </div>
                    </div>
                    </div>
                    </div>
                  <span :class="{ 'active': sortColumn === sortScoring }">{{ $t("message.dashboard.overview.table_title.scoring") }}</span>
                  <span class="hide">{{sortScoring}}</span>
                </div>
                <div class="sort-arrow">
                  <span :class="{ 'up': sortColumn === sortScoring & ascending === true}"></span>
                  <span :class="{ 'down': sortColumn === sortScoring & ascending === false}"></span>
                </div>
              </div>
            </th>
          </tr>
        </thead>
        <tbody class="ecl-table__body">
         <div v-if="this.loading === true" class="lds-ring">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </div>
          <tr v-for="row in getAllCatalogues.slice(0, rows)" :key="row.info.id">
            <td class="spatial-row" width="100px">
              <div>
                <img v-if="row.sortSpatial" :src="getCountryFlagImg(row.sortSpatial)" width="30px" height="30px" class="flag-icon" alt="Catalog flag">
                <img v-else :src="getCountryFlagImg('europe')" width="30px" height="30px" class="flag-icon" alt="Catalog flag">
              </div>
            </td>
            <td width="180px">
              <!----------------- isExplorer() ----------------->
              <router-link class="name-row" v-if="isExplorer()" :to="{ name: 'CatalogueDetailDashboard', params: {id: row.info.id, title: row.info.title, spatial: row.info.spatial, rating: row.score}}">{{row.sortTitle}} <span v-if="row.sortTitle && row.sortTitle.includes('Europe')">(EU)</span><span v-else><span v-if="sortSpatial != null">({{row.sortSpatial}})</span></span></router-link>
              <router-link class="name-row" v-else :to="{ name: 'CatalogueDetailDashboard', params: {id: row.info.id, title: row.info.title, spatial: row.info.spatial, rating: row.score}, query: { locale: $i18n.locale }}">{{row.sortTitle}} <span v-if="row.sortTitle && row.sortTitle.includes('Europe')">(EU)</span><span v-else><span v-if="sortSpatial != null">({{row.sortSpatial}})</span></span> </router-link>
            </td>
            <td v-for="(dimension, i) in dimensions[0]" :key="dimension+i" width="120px">
              <div v-if="row[`sort${dimension.name}`] == -1" class="overview-row">
                <!----------------- isMobile() ----------------->
                <p v-if="isMobile()" id="nodata">-</p>
                <img v-else src="../assets/img/na_4.png" width="86px" height="35px" alt="No data available">
              </div>
              <div v-else class="overview-row">
                <div v-if="dimension.name === 'Scoring'">
                  <div v-if="row[`sort${dimension.name}`] >= 351" class="scoring scoring-excellent">{{$t('message.methodology.scoring.table.excellent')}} </div>
                  <div v-else-if="row[`sort${dimension.name}`] >= 221 && row[`sort${dimension.name}`] <= 350 " class="scoring scoring-good">{{$t('message.methodology.scoring.table.good')}} </div>
                  <div v-else-if="row[`sort${dimension.name}`] >= 121 && row[`sort${dimension.name}`] <= 220 " class="scoring scoring-sufficient">{{$t('message.methodology.scoring.table.sufficient')}} </div>
                  <div v-else-if="row[`sort${dimension.name}`] <= 120 && row[`sort${dimension.name}`] >=0" class="scoring scoring-bad">{{$t('message.methodology.scoring.table.bad')}}</div>
                  <div v-else><img src="../assets/img/na_4.png" width="86px" height="35px" alt="No data available"></div>
                </div>
                <div v-else-if="row[`sort${dimension.name}`] === undefined"> <img src="../assets/img/na_4.png" width="86px" height="35px" alt="No data available"></div>
                <div v-else>
                <span class="ecl-u-type-color-grey-100 access-overview">{{row[`sort${dimension.name}`]}} / {{dimension.weight}} </span>
                <div class="progress flex-row" style="height: 4px;">
                    <div class="progress-bar" role="progressbar"  :style="`width:${row[`${dimension.name}`]}%`"> </div>
                    <!-- <div class="progress-bar" role="progressbar "  :style="`width:${row[`sort${dimension.name}`]}%`"> </div>
                -->
                 </div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
/* eslint-disable */
import VueEvents from 'vue-events'
import Vue from 'vue'
import { has, isNumber } from 'lodash'
// import Actions and Getters from Store Module
import { mapActions, mapGetters } from 'vuex'
import Sticky from 'vue-sticky-directive'
import VTooltip from 'v-tooltip'
import Tag from './ec/Tag.vue'

Vue.use(VTooltip)
Vue.use(VueEvents)

export default {
  components: {
    tag : Tag
  },
  directives: {
    Sticky
  },
  data () {
    return {
      loading: true,
      ascending: true,
      sortColumn: '',
      spatial: 'spatial',
      rating: 'rating',
      id: 'id',
      rows: '12',
      title: 'title',
      dimensions: [{Findability: {name: 'Findability', weight: 100}, Accessibility: {name: 'Accessibility', weight: 100}, Interoperability: {name: 'Interoperability', weight: 110}, Reusability: {name: 'Reusability', weight: 75}, Contextuality: {name: 'Contextuality', weight: 20}, Scoring: {name: 'Scoring', weight: 405}}],
      sortFindability: 'sortFindability',
      sortAccessibility: 'sortAccessibility',
      sortInteroperability: 'sortInteroperability',
      sortReusability: 'sortReusability',
      sortContextuality: 'sortContextuality',
      sortScoring: 'sortScoring',
      sortTitle: 'sortTitle',
      sortSpatial: 'sortSpatial'
    }
  },
  mounted () {
    // this.sortTableStart(this.sortSpatial)
  },
  methods: {
    // import store-actions
    ...mapActions([
      'loadAllCatalogues'
    ]),
    has,
    isNumber,
    sortTable: function (x) {
      if (this.sortColumn === x) {
        this.ascending = !this.ascending
      } else {
        this.ascending = false
        this.sortColumn = x
      }
      var ascending = this.ascending
      this.getAllCatalogues.sort(function (a, b) {
        if (a[x] > b[x]) {
          return ascending ? 1 : -1
        } else if (a[x] < b[x]) {
          return ascending ? -1 : 1
        }
        return 0
      })
    },
    sortTableStart: function (x) {
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
    activate: function (el) {
      this.active_el = el
    },
    init () {
      this.addSortableValues(this.getAllCatalogues)
      // this.sortTableStart(this.sortSpatial)
      this.sortTable(this.sortScoring);
    },
    addSortableValues (arr) {
      // Workaround Solution, all other cleaner ideas didn't work.
      const length = arr.length
      for (let x = 0; x < length; x++) {
        let catalogue = this.getAllCatalogues[x]
        if (has(catalogue.findability.score, 'points') && has(catalogue.findability.score, 'points') && isNumber(catalogue.findability.score.points)) {
          Vue.set(this.getAllCatalogues[x], 'sortFindability', catalogue.findability.score.points);
          Vue.set(this.getAllCatalogues[x], 'Findability', catalogue.findability.score.percentage);
        } else Vue.set(this.getAllCatalogues[x], 'sortFindability', -1);

        if (has(catalogue.reusability.score, 'points') && has(catalogue.reusability.score, 'points') && isNumber(catalogue.reusability.score.points)) {
          Vue.set(this.getAllCatalogues[x], 'sortReusability', catalogue.reusability.score.points);
          Vue.set(this.getAllCatalogues[x], 'Reusability', catalogue.reusability.score.percentage);
        } else Vue.set(this.getAllCatalogues[x], 'sortReusability', -1);

        if (has(catalogue.accessibility.score, 'points') && has(catalogue.accessibility.score, 'points') && isNumber(catalogue.accessibility.score.points)) {
          Vue.set(this.getAllCatalogues[x], 'sortAccessibility', catalogue.accessibility.score.points);
          Vue.set(this.getAllCatalogues[x], 'Accessibility', catalogue.accessibility.score.percentage);
        } else Vue.set(this.getAllCatalogues[x], 'sortAccessibility', -1);

        if (has(catalogue.interoperability.score, 'points') && has(catalogue.interoperability.score, 'points') && isNumber(catalogue.interoperability.score.points)) {
          Vue.set(this.getAllCatalogues[x], 'sortInteroperability', catalogue.interoperability.score.points);
          Vue.set(this.getAllCatalogues[x], 'Interoperability', catalogue.interoperability.score.percentage);
        } else Vue.set(this.getAllCatalogues[x], 'sortInteroperability', -1);

        if (has(catalogue.contextuality.score, 'points') && has(catalogue.contextuality.score, 'points') && isNumber(catalogue.contextuality.score.points)) {
          Vue.set(this.getAllCatalogues[x], 'sortContextuality', catalogue.contextuality.score.points);
          Vue.set(this.getAllCatalogues[x], 'Contextuality', catalogue.contextuality.score.percentage);
        } else Vue.set(this.getAllCatalogues[x], 'sortContextuality', -1);

        Vue.set(this.getAllCatalogues[x], 'sortScoring', catalogue.score)
        Vue.set(this.getAllCatalogues[x], 'sortTitle', catalogue.info.title)
        Vue.set(this.getAllCatalogues[x], 'sortSpatial', catalogue.info.spatial)

        // Set EU as default spatial value
        if (catalogue.spatial === null) Vue.set(this.getAllCatalogues[x], 'spatial', 'EU')
      }
    },
    isMobile () {
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)
    },
    isExplorer () {
      return navigator.userAgent.indexOf('MSIE ') > -1 || navigator.userAgent.indexOf('Trident/') > -1 || window.navigator.userAgent.indexOf('Edge') > -1
    },
    showMore (rows) {
      return this.rows = rows;
    },
    /**
     * @description         Returns an image of a flag.
     * @param { String }    countryId - The ID (example: 'en', 'de', 'fr') of a country to get the flag from.
     * @returns { String }  An image, represented by its absolute path.
     */
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
    ])
  },
  created () {
    this.loading = true
    this.loadAllCatalogues()
      .then(() => {
        this.loading = false
        this.init()
      })
      .catch(() => {})
      .finally(() => this.loading = false)
  }
}
</script>

<style lang="scss" scoped>
@import "../styles/metrics-style.scss";
@import "~bootstrap/scss/bootstrap";

.primary-color-accessibility {
  background-color: $deu-logo-medium-blue !important;
}

.primary-color-findability {
  background-color: $deu-badge-green !important;
}

.primary-color-interoperability {
 background-color: $deu-logo-blue !important;
}

.primary-color-reusability {
  background-color: $deu-dark-orange !important;
}

.primary-color-contextual {
  background-color: $deu-logo-yellow !important;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

.scoring-excellent {
  text-align: center;
  background-color: #006FB4;
  border-radius: 1.875rem;
  color: #fff;
}

.scoring-good {
  text-align: center;
  background-color: #467A39;
  border-radius: 1.875rem;
  color: #fff;
}
.scoring-sufficient {
  text-align: center;
  background-color: #FAB417;
  border-radius: 1.875rem;
  color: #fff;
}
.scoring-bad {
  text-align: center;
  background-color: #BB616B;
  border-radius: 1.875rem;
  color: #fff;
}

#top-catalogues {
  font-size: 16px;
  font-weight: 600;
}

.name-row {
  font-family: 'Arial';
  font-style: normal;
  font-weight: 400;
  font-size: 16px;
  color: #004494 !important;
}

.active-table-head{
  text-decoration:underline;
}
.is-red{
  background: red;
}
.btn-light {
  background: #F2F4F9;
  border-radius: 1.875rem !important;

  &:hover {
    background: #EFF2FE;
  }
}

.active{
   text-decoration: none;
}
.down {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  margin-bottom:3px
}
.up {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
  margin-bottom:1px
}
.sticky {
  position: sticky;
  position: -webkit-sticky;
  top: 52px;
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
  background: #dee2e6;
}

.weight {
  color: #000000;
  position: absolute;
    margin-top: -85px;
    /* margin-left: 25px; */
    width: 100px;
    text-align: left;
}

.weight-title {
   position: absolute;
    margin-top: -133px;
    /* margin-left: 25px; */
    width: 100px;
    text-align: right;
    font-weight: 700;
}

.weight-points {
  margin-bottom: 4px;
}

@media only screen and (max-width: 1199.9px) {
  .sticky {
    top: 0px;
  }
  table {
    display: block;
    width: 100%;
    overflow-x: auto;
  }

  .weight {
  position: absolute;
    margin-top: -37px;
    /* margin-left: 25px; */
    width: 100px;
    text-align: right;
}

.weight-title {
   position: absolute;
    margin-top: -60px;
    /* margin-left: 25px; */
    width: 100px;
    text-align: right;
    font-weight: 700;
}
 .overview-row-left {
    text-align: left;
    padding-top: 0px;
    font-weight: 500;
    padding-bottom: 19px !important;
    height: 110px;
 }
}
.ecl-u-type-paragraph {
  font-weight: 700!important;
  font-size: 16px!important;
  color: #000000;
}
.btn-tag {
  border: none;
  background: transparent;
  padding: 0;
}
.btn-tag:focus {
  outline:0!important;
  box-shadow: none;
}

.ecl-table__body .progress-bar {
  background: #004494!important;
}
.scoring {
  padding: 5px 0px;
  font-weight: 400;
}
</style>
