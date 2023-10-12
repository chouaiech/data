<template>
  <div id="detail-table">
    <div class="total-number">
      <span class="ecl-u-mr-s">{{ $t("message.statistics.totalNumber") }}:</span>
      <span class="total-sum ecl-tag ecl-tag--display">{{ catalogues_Sum.toLocaleString('fi') }}</span>
    </div>
    <table class="table table-hover table-responsive">
      <thead id="sticky">
        <tr>
          <!-- CATEGORY -->
          <th v-on:click="sortTable(name)" class="table-img">
            <span class="hide">{{name}}</span>
            <!-- {{ $t("message.statistics.tableHead.category") }} -->
          </th>
          <!-- NAME -->
          <th v-on:click="sortTable(name)" class="table-name">
            <span class="hide">{{name}} </span>
            <span :class="{ 'active': sortColumn === name }">{{ $t("message.statistics.tableHead.name") }}</span>
              &nbsp;
              <span :class="{ 'up': sortColumn === name & ascending === true }"></span>
              <span :class="{ 'down': sortColumn === name & ascending === false}"></span>
          </th>
          <!-- TREND -->
          <th class="table-trend">
            <span class="hide"></span>
            {{ $t("message.statistics.tableHead.trend") }}
          </th>
          <!-- PERCENT -->
          <th v-on:click="sortTable(countryCount)" class="table-percent">
            <span class="hide">{{countryCount}} </span>
            <span :class="{ 'active': sortColumn === countryCount }">{{ $t("message.statistics.tableHead.percent") }}</span>
              &nbsp;
              <span :class="{ 'up': sortColumn === countryCount & ascending === true }"></span>
              <span :class="{ 'down': sortColumn === countryCount & ascending === false}"></span>
          </th>
          <!-- NUMBER OF DATASETS -->
          <th v-on:click="sortTable(countryCount)" class="table-number">
              <span class="hide">{{countryCount}} </span>
            {{ $t("message.statistics.tableHead.numberOfDatasets") }}
          </th>
        </tr>
      </thead>
      <tbody>
        <div v-if="this.loading === true" class="lds-ring" id="spinner-overview"><div></div><div></div><div></div><div></div></div>
        <tr v-for="(row, index) in catalogues" :key="index" :type="row.name" class="icon-change">
            <!-- CATEGORY -->
            <td class="row-img">
              <div v-if="row.name=='op: datpro'"></div>
              <div v-else :type="row.name" class="category-icon"></div>
            </td>
            <!-- NAME -->
            <td class="row-name">
              <div v-if="row.name=='op: datpro'"></div>
              <div v-else>
              {{row.name}}
              </div>
            </td>
            <!-- TREND -->
            <td class="row-trend">
              <span class="trend ecl-tag ecl-tag--display" :class="getTrendStatus(index)">
                {{ trendArray[index] }}
              </span>
            </td>
            <!-- PERCENT -->
            <td class="row-percent">
              <div v-if="row.name=='op: datpro'"></div>
              <div v-else>
                <span id="percent" v-if="round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),4) == 0">  {{round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),100)}}&nbsp;%   </span>
                <span v-else>
                  <span id="percent">{{round(100/(catalogues_Sum/row.stats[row.stats.length-1].count),4)}}&nbsp;% </span>
                </span>
                <div class="progress" style="height: 5px;">
                  <div class="progress-bar" role="progressbar" :style="'width:'+100/(catalogues_Sum/row.stats[row.stats.length-1].count) +'%'"> </div>
                </div>
              </div>
            </td>
            <!-- NUMBER OF DATASETS -->
            <td class="row-number">
              <div v-if="row.name=='op: datpro'"></div>
              <div v-else>
                {{row.stats[row.stats.length-1].count.toLocaleString('fi')}}
              </div>
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
import Trend from 'vuetrend'


Vue.use(Trend)

export default {
  name: 'Categories',
  data () {
    return {
      catalogues_Sum: '',
      catalogues: [],
      loading: true,
      data: [],
      name: 'name',
      countryCount: 'CountryCount',
      ascending: true,
      sortColumn: 'name',
      count_dummy: [],
      latest: 2,
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
    sortTable(x) {
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
        return 0;
      })
    },
    sortTableStart(x) {
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
            this.count_dummy.push(+this.catalogues[i].stats[l].count)
        }
      }
      for (i = 0; i < this.count_dummy.length ; i +=  this.catalogues[0].stats.length) {
        this.finalArray.push(this.count_dummy.slice(i, i+this.catalogues[0].stats.length));
      }
    },
    round(zahl, nStelle) {
      zahl = (Math.round(zahl * nStelle) / nStelle)
      return zahl
    },
    requestData() {
      axios.get(this.$env.ROOT_API + '/data/ds-per-category?list=true', {})
        .then(response => {
          this.catalogues = response.data.result
          this.catalogues_Sum = response.data.sum
          this.loading = false
          this.$emit('changeLoading', this.loading)
          this.sortTableStart(this.name)
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
    this.requestData();
  },
}
</script>

<style lang="scss" scoped></style>
