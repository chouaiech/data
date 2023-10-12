<template>
  <div class="container">
    <div class="line-chart">
     <line-chart
      v-if="loaded"
      :chartlabels="Catalogues.stats"
      :chartdata="hope"
      :date="date_dummy"/>
      </div>
  </div>
</template>

<script>
/* eslint-disable */
import axios from 'axios'
import Vue from 'vue'
import Trend from 'vuetrend'
import LineChart from './LineChartTotal.vue'


Vue.use(Trend)

export default {
  name: 'Evolution',
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
      EvolutionCountryCount: [],
      EvolutionCountryDate: [],
      labels: [],
      count: [],
      count_dummy: [],
      count_dummy1: [],
      Catalogues: [],
      total: [['Total']],
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
    addLabels () {
        for (var l = 0; l < this.Catalogues.stats.length; l++) {
            this.count_dummy.push(+this.Catalogues.stats[l].count)   //+ macht aus String zahl
        }
        this.hope.push(this.count_dummy)
        for (var l = 0; l < this.Catalogues.stats.length; l++) {
          this.date_dummy.push(this.Catalogues.stats[l].date)
        }
      },
    round (zahl, nStelle) {
      zahl = (Math.round(zahl * nStelle) / nStelle)
      return zahl
    },
    requestData () {
      axios.get(this.$env.ROOT_API + '/data/num-datasets', { // http://odp-lic.ppe-aws.europeandataportal.eu:9090/data/ds-per-catalogue'
      })
        .then(response => {
          this.Catalogues = response.data
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
