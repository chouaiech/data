<template>
  <div class="sub-navigation container mt-4 mb-4">
    <h1>{{ $t("message.header.navigation.data.statistics") }}</h1>
    <div class="mt-4">
       <nav class="navbar navbar-expand-lg navbar-light pl-0">
          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
            <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
              <li class="ecl-u-mr-l">
                <router-link
                  :to="{ name: 'CurrentState - Datasets per Category', query: { locale: $i18n.locale } }"
                  class="ecl-link ecl-link--secondary ecl-u-pa-m ecl-u-type-color-blue"
                  :class="{ 'active': isActive('CurrentState')}">{{ $t("message.statistics.currentState") }}</router-link>
              </li>
              <li class="ecl-u-mr-l">
                <router-link
                  :to="{ name: 'Evolution - Total number of Datasets', query: { locale: $i18n.locale } }"
                  class="ecl-link ecl-link--secondary ecl-u-pa-m ecl-u-type-color-blue"
                  :class="{ 'active': isActive('Evolution')}">{{ $t("message.statistics.evolution") }}</router-link>
              </li>
            </ul>
            <span class="updated">
              <span class="ecl-u-mr-s">{{ $t('message.metadata.updated') }}:</span>
              <span class="date ecl-tag ecl-tag--display">{{ this.formattedDate }}</span>
            </span>
          </div>
       </nav>
     </div>
  </div>
</template>

<script>
import axios from 'axios'

import moment from 'moment';


export default {
  name: 'SubNav',
  data () {
    return {
      catalogues: [],
      date: [],
      formattedDate: '',
    }
  },
  methods: {
    isActive(element) {
      if (!this.$route || !this.$route.name) return false;
      return this.$route.name.startsWith(element);
    },
    getDate () {
      this.date = this.catalogues.stats[this.catalogues.stats.length - 1]
      if (this.date.date !== "") {
        this.formattedDate = moment(new Date(this.date.date)).format('DD MMMM YYYY');
      }
    },
    requestData () {
      axios.get(this.$env.ROOT_API + '/data/num-datasets', {})
        .then(response => {
          this.catalogues = response.data
          this.getDate()
        })
        .catch(() => {})
    }
  },
  created() {
    this.requestData();
  }
}
</script>

<style lang="scss" scoped>

.btn {
  border-radius: 1.875rem;
}

.btn-primary {
  background-color: var(--medium-blue);
  border-color: var(--medium-blue);

  &:hover {
    background-color: #4070d6;
    border-color: #4476e2;
  }
}

.active {
  background-color: #004494!important;
  color: #fff!important;
}

.weight{
  font-weight: 300;
}

.sub-navigation{
  padding-top:5px;
  background-color: #fff;
}

.updated {
  //font-family: 'Ubuntu', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
  text-align: right;
  font-weight: 100;
}

.date {
  font-size: 12px;
  font-weight: 100;
}
</style>
