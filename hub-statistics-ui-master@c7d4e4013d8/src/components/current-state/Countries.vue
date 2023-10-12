<template>
  <div class="container" id="detail-table">
        <div class="total-number">
      <span class="ecl-u-mr-s">{{ $t("message.statistics.totalNumber") }}:</span>
      <span class="total-sum ecl-tag ecl-tag--display">{{ catalogues_Sum.toLocaleString('fi') }}</span>
    </div>
    <table class="table table-hover table-responsive">
      <thead id="sticky">
        <tr>
          <!-- COUNTRY -->
          <th v-on:click="sortTable(name)" class="table-img">
            <span class="hide">{{name}} </span>
           <!-- {{ $t("message.statistics.tableHead.country") }} -->
          </th>
          <!-- NAME -->
          <th v-on:click="sortTable(name)" class="table-name">
            <span class="hide">{{name}} </span>
            <span :class="{ 'active': sortColumn === name }"> {{ $t("message.statistics.tableHead.name") }} </span>
              &nbsp;
              <span :class="{ 'up': sortColumn === name & ascending === true }"></span>
              <span :class="{ 'down': sortColumn === name & ascending === false}"></span>
          </th>
          <!-- TREND -->
          <th class="table-trend">
            {{ $t("message.statistics.tableHead.trend") }}
          </th>
          <!-- PERCENT -->
          <th v-on:click="sortTable(CountryCount)" class="table-percent">
            <span class="hide">{{CountryCount}} </span>
            <span :class="{ 'active': sortColumn === CountryCount }">{{ $t("message.statistics.tableHead.percent") }}</span>
              &nbsp;
              <span :class="{ 'up': sortColumn === CountryCount & ascending === true }"></span>
              <span :class="{ 'down': sortColumn === CountryCount & ascending === false}"></span>
          </th>
          <!-- NUMBER OF DATASETS -->
          <th v-on:click="sortTable(CountryCount)" class="table-number">
              <span class="hide">{{CountryCount}} </span>
            {{ $t("message.statistics.tableHead.numberOfDatasets") }}
          </th>
        </tr>
      </thead>
      <tbody>
        <div v-if="this.loading === true" class="lds-ring" id="spinner-overview"><div></div><div></div><div></div><div></div></div>
        <tr v-for="(row, index) in catalogues" :key="index">
            <!-- COUNTRY -->
            <td class="row-img">
              <div v-if="row.name === 'Czechia' "> <img src="../../assets/img/flags/cz.png" width="30px" class="flag-icon">  </div>
              <div v-else-if="row.name === 'Estonia' "> <img src="../../assets/img/flags/ee.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Spain' "> <img src="../../assets/img/flags/es.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Croatia' "> <img src="../../assets/img/flags/hr.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Poland' "> <img src="../../assets/img/flags/pl.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Denmark' "> <img src="../../assets/img/flags/dk.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Greece' "> <img src="../../assets/img/flags/gr.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Ireland' "> <img src="../../assets/img/flags/ie.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Netherlands' "> <img src="../../assets/img/flags/nl.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Finland' "> <img src="../../assets/img/flags/fi.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Germany' "> <img src="../../assets/img/flags/de.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Hungary' "> <img src="../../assets/img/flags/hu.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'France' "> <img src="../../assets/img/flags/fr.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Sweden' "> <img src="../../assets/img/flags/se.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Liechtenstein' "> <img src="../../assets/img/flags/li.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Norway' "> <img src="../../assets/img/flags/no.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Lithuania' "> <img src="../../assets/img/flags/lt.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Luxembourg' "> <img src="../../assets/img/flags/lu.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Romania' "> <img src="../../assets/img/flags/ro.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Moldova' "> <img src="../../assets/img/flags/md.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Slovakia' "> <img src="../../assets/img/flags/sk.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Slovenia' "> <img src="../../assets/img/flags/si.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Austria' "> <img src="../../assets/img/flags/at.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Latvia' "> <img src="../../assets/img/flags/lv.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'United Kingdom' "> <img src="../../assets/img/flags/gb.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Malta' "> <img src="../../assets/img/flags/mt.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Cyprus' "> <img src="../../assets/img/flags/cy.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Bulgaria' "> <img src="../../assets/img/flags/bg.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Iceland' "> <img src="../../assets/img/flags/is.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Serbia' "> <img src="../../assets/img/flags/rs.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Belgium' "> <img src="../../assets/img/flags/be.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Ukraine' "> <img src="../../assets/img/flags/ua.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Switzerland' "> <img src="../../assets/img/flags/ch.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Italy' "> <img src="../../assets/img/flags/it.png" width="30px" class="flag-icon"> </div>
              <div v-else-if="row.name === 'Portugal' "> <img src="../../assets/img/flags/pt.png" width="30px" class="flag-icon"> </div>
              <!--- all v-else-if flags here -->
              <div v-else> <img src="../../assets/img/eu_flag.svg" width="30px" class="flag-icon"></div>
            </td>
            <!-- NAME -->
            <td class="row-name">
              {{row.name}}
            </td>
            <!-- TREND -->
            <td class="row-trend">
              <span class="trend ecl-tag ecl-tag--display" :class="getTrendStatus(index)">
                {{ trendArray[index] }}
              </span>
            </td>
            <!-- PERCENT -->
            <td class="row-percent">
              <span id="percent" v-if="round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),4) == 0">  {{round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),100)}}&nbsp;%  </span>
              <span v-else>
                <span id="percent">{{round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),4)}}&nbsp;%  </span>
              </span>
              <div class="progress" style="height: 5px;">
                <div class="progress-bar" role="progressbar" :style="'width:'+100/(catalogues_Sum/row.stats[row.stats.length-1].count) +'%'"> </div>
              </div>
            </td>
            <!-- NUMBER OF DATASETS -->
            <td class="row-number">
                {{row.stats[row.stats.length-1].count.toLocaleString('fi')}}
            </td>
        </tr>
    </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'
import Vue from 'vue'
import Trend from 'vuetrend'

Vue.use(Trend)
export default {
  name: 'Countries',
  data () {
    return {
      loaded: false,
      chartdata: [10, 20],
      catalogues_Sum: '',
      catalogues_Sum_manuel: 838542,
      catalogues: [],
      loading: true,
      desc: ['yes', 'no', 'unknown'],
      data: [],
      name: 'name',
      CountryCount: 'CountryCount',
      ascending: true,
      sortColumn: 'name',
      finalArray: [],
      count_dummy: []
    }
  },
  mounted () {
    if (this.$route.params.package) {
      this.loading = true
      this.package = this.$route.params.package
    }
  },
  methods: {
    getTrendStatus(index) {
      let trend = parseFloat(this.trendArray[index]);
      return trend > 0 ? 'success'
        : trend < 0 ? 'error'
        : ''
    },
    sortTable (x) {
      if (this.sortColumn === x) {
        this.ascending = !this.ascending
      } else {
        this.ascending = false
        this.sortColumn = x
      }
      var ascending = this.ascending
      this.catalogues.sort(function (a, b) {
        if (a[x] > b[x]) {
          return ascending ? 1 : -1
        } else if (a[x] < b[x]) {
          return ascending ? -1 : 1
        }
        return 0
      })
    },
    sortTableStart (x) {
      if (this.sortColumn === x) {
        this.ascending = this.ascending
      } else {
        this.ascending = false
        this.sortColumn = x
      }
      var ascending = this.ascending
      this.catalogues.sort(function (a, b) {
        if (a[x] > b[x]) {
          return ascending ? 1 : -1
        } else if (a[x] < b[x]) {
          return ascending ? -1 : 1
        }
        return 0
      })
    },
    addSortableCountryCount (arr) {
      for (var x = 0; x < arr.length; x++) {
        Vue.set(this.catalogues[x], 'CountryCount', +this.catalogues[x].stats[this.catalogues[1].stats.length-1].count)
      }
      return arr
    },
    addLabels () {
      for (var i = 0; i < this.catalogues.length; i++){
        for (var l = 0; l < this.catalogues[i].stats.length; l++) {
          this.count_dummy.push(+this.catalogues[i].stats[l].count)   //+ macht aus String zahl
        }
      }
      for (i = 0; i < this.count_dummy.length ; i +=  this.catalogues[0].stats.length) {
        this.finalArray.push(this.count_dummy.slice(i, i+this.catalogues[0].stats.length));
      }
      for ( l = 0; l < this.catalogues[0].stats.length; l++) {
        this.date_dummy.push(this.catalogues[0].stats[l].date)
      }
    },
    round (zahl, nStelle) {
      zahl = (Math.round(zahl * nStelle) / nStelle)
      return zahl
    },
    requestData () {
      axios.get(this.$env.ROOT_API + '/data/ds-per-country?list=true', { // http://odp-lic.ppe-aws.europeandataportal.eu:9090/data/ds-per-catalogue'
      })
        .then(response => {
          this.catalogues = response.data.result
          this.catalogues_Sum = response.data.sum
          this.loaded = true
          this.loading = false
          this.sortTableStart(this.name)
          this.$emit('changeLoading', this.loading)
          this.addSortableCountryCount(this.catalogues)
          this.addLabels()
        })
        .catch(() => {})
    },
  },
  computed: {
    trendArray() {
      return this.finalArray.map(el => {
        let length = el.length;
        let currentValue = el[length-1];
        let trendValue = el[length-4];
        let result = ((currentValue - trendValue) / trendValue).toFixed(2);
        return (isNaN(currentValue) || isNaN(trendValue) || isNaN(result) || trendValue === 0)
          ? '-'
          : `${result}%`;
      });
    },
  },
  created() {
    this.requestData()
  }
}
</script>

<style lang="scss" scoped></style>
