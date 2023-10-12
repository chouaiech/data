<template>
  <div class="container">
    <div class="line-chart d-flex">
      <div id="line-chart-country-js-legend" class="col-4"></div>
     <line-chart
       class="col-8"
      v-if="loaded"
      :chartlabels="Catalogues"
      :chartdata="finalArray"
      :date="date_dummy"/>
      </div>
  </div>
</template>

<script>
/* eslint-disable */
import axios from 'axios'
import Vue from 'vue'
import Trend from 'vuetrend'
import LineChart from './LineChartCountry.vue'


Vue.use(Trend)

export default {
  name: 'Evolution-Country',
  components: { LineChart },
  data () {
    return {
      loaded: false,
      chartdata: [10, 20, 100],
      Catalogues_Sum: '',
      Catalogues: [],
      loading: true,
      desc: ['yes', 'no', 'unknown'],
      data: [],
      showError: false,
      errorMessage: 'No Data availale.',
      name: 'name',
      CountryCount: 'CountryCount',
      ascending: true,
      sortColumn: '',
      isActive: false,
      isLoading: true,
      labels: [],
      count: [],
      count_dummy: [],
      date_dummy: [],
      hope: [],
      result: [],
      finalArray: []
      }
  },
  mounted () {
    if (this.$route.params.package) {
      this.loading = true
      this.package = this.$route.params.package
    }
  },
  methods: {
    addSortableCountryCount (arr) {
      for (var x = 0; x < arr.length; x++) {
        Vue.set(this.Catalogues[x], 'CountryCount', +this.Catalogues[x].stats[0].count)
      }
      return arr
    },
    addLabels () {
      for (var i = 0; i < this.Catalogues.length; i++){
        for (var l = 0; l < this.Catalogues[i].stats.length; l++) {
            this.count_dummy.push(+this.Catalogues[i].stats[l].count)   //+ macht aus String zahl
        }
      }
      this.hope.push(this.count_dummy)
        for (i = 0; i < this.count_dummy.length ; i +=  this.Catalogues[0].stats.length) {
            this.finalArray.push(this.count_dummy.slice(i, i+this.Catalogues[0].stats.length));
      }
      for (var l = 0; l < this.Catalogues[0].stats.length; l++) {
        this.date_dummy.push(this.Catalogues[0].stats[l].date)
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
          this.Catalogues = response.data.result
          this.Catalogues_Sum = response.data.sum
          this.labels = response.data.result.map(p => p.name)
          this.count = response.data.result.map(p => p.stats[0].count)
          this.date = response.data.result.map(p => p.stats[0].date)
          this.loaded = true
          this.loading = false
          this.$emit('changeLoading', this.loading)
          this.addLabels()
        })
        .catch(err => {
          this.errorMessage = err.response.data.error
          this.showError = true
        })
    },
  },
  computed: {},
  created () {
    this.requestData()
  }
}
</script>

<style lang="scss" scoped></style>
