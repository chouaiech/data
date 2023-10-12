<template>
  <div class="container" id="detail-table">
    <div class="total-number">
      <span class="ecl-u-mr-s">{{ $t("message.statistics.totalNumber") }}:</span>
      <span class="total-sum ecl-tag ecl-tag--display">{{ catalogues_Sum.toLocaleString('fi') }}</span>
    </div>
    <table class="table table-hover table-responsive">
      <thead id="sticky"> <!-- v-sticky sticky-side="top" id="sticky" -->
        <tr>
          <!-- COUNTRY -->
          <th v-on:click="sortTable(spatial)" class="table-img-sort">
            <span class="hide">{{spatial}} </span>
              <span :class="{ 'active': sortColumn === spatial }"> <!-- {{ $t("message.statistics.tableHead.country") }} --> </span>
              &nbsp;
            <!-- <span :class="{ 'up': sortColumn === spatial & ascending === true }"></span>
             <span :class="{ 'down': sortColumn === spatial & ascending === false}"></span>  -->
         </th>
         <!-- CATALOGUE -->
          <th v-on:click="sortTable(name)" class="table-name-small">
            <span class="hide">{{name}} </span>
            <span :class="{ 'active': sortColumn === name }"> {{ $t("message.statistics.tableHead.catalogue") }}  </span>
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
            <div><img :src="getFlagIcon(row.spatial)" width="30px" class="flag-icon"></div>
          </td>
          <!-- CATALOGUE -->
          <td class="row-name">
            {{row.name}} ({{row.spatial}})
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
            <span id="percent">{{round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),4)}}&nbsp;% </span>
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
/* eslint-disable */
import axios from 'axios'
import Vue from 'vue'


export default {
  name: 'CountryAndCatalogue',
  data () {
    return {
      chartdata: [10, 20],
      catalogues_Sum: '',
      catalogues: [],
      loading: true,
      desc: ['yes', 'no', 'unknown'],
      data: [],
      name: 'name',
      CountryCount: 'CountryCount',
      ascending: true,
      sortColumn: '',
      spatial: 'spatial',
      date_dummy: [],
      count_dummy: [],
      finalArray: [],
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
        this.ascending = true
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
      for (var l = 0; l < this.catalogues[0].stats.length; l++) {
        this.date_dummy.push(this.catalogues[0].stats[l].date)
      }
    },
    round (zahl, nStelle) {
      zahl = (Math.round(zahl * nStelle) / nStelle)
      return zahl
    },
    requestData () {
      axios.get(this.$env.ROOT_API + '/data/ds-per-country-and-catalogue?list=true', { // http://odp-lic.ppe-aws.europeandataportal.eu:9090/data/ds-per-catalogue'
      })
        .then(response => {
          this.catalogues = response.data.result
          this.catalogues_Sum = response.data.sum
          this.loading = false
          this.sortTableStart(this.spatial)
          this.$emit('changeLoading', this.loading)
          this.addSortableCountryCount(this.catalogues)
          this.addLabels()
        })
        .catch(() => {})
    },
    getFlagIcon(flag) {
      try {
        return require(`../../assets/img/flags/${flag}.png`)
      } catch (error) {
        return require(`../../assets/img/eu_flag.svg`)
      }
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
  created: function () {
    this.requestData()
  }
}
</script>

<style lang="scss" scoped></style>
