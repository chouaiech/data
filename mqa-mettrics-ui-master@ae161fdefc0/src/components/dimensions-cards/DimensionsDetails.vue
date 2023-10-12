<template>
  <div class="container-fluid dimensions-details-wrapper">
    <div class="row dimensions-details-content h-100">
      <div class="col-md-9 p-md-5 details-body">
        <div class="history-btn-group">
         <div v-if="this.dimension !== 'score'">
            <button v-on:click="isToday = true; isHistory = false" v-bind:class="{ 'active': isToday}" class="btn btn-light">
              {{ $t("message.dashboard.show_today") }}
            </button>
            <button class="btn btn-light" v-on:click="isHistory = true; isToday = false" v-bind:class="{ 'active': isHistory}">
              {{ $t("message.dashboard.show_history") }}
            </button>
          </div>
        </div>
        <div class="my-4">
          <span
            class="ecl-u-type-heading-1"
            :class="`primary-color-${attributes.className}`"
          >
            {{ $t(`message.dashboard.${attributes.translationKey}.title`) }}
          </span>
        </div>
        <p class="mb-4">
          {{ $t(`message.dashboard.${attributes.translationKey}.description`) }}
        </p>
        <!-- Graphs container SCORING-->
        <div v-if="dimension === 'score'">
          <div v-if="!metrics">
            {{ $t("message.no_data") }}
          </div>
          <div
            v-else
            class="graph-container position-relative"
            :class="attributes.indicators.timeBasedScoring.graphType"
          >
            <div class="d-flex justify-content-center">
            <anchor-router-link class="anchor-link" :to="{name: 'Methodology',hash: '#'+attributes.className}">{{$t(`message.dashboard.${attributes.translationKey}.plots.title.${'timeBasedScoring'}`)}}</anchor-router-link>
              <i class="material-icons small-icon pl-1 pt-1"
               v-tooltip="{
               placement: 'right',
               content: 'Click here to find out how this indicator is calculated',
               delay: { show: 500, hide: 100 }}" >help_outline</i></div>
            <component
              v-if="history && history.length > 0"
              :is="attributes.indicators.timeBasedScoring.graphType"
              :width="200"
              :height="400"
              :title="$t(`message.dashboard.${attributes.translationKey}.plots.title.${'timeBasedScoring'}`)"
              :chart-data="history.map(el => el[Object.keys(el)[0]])"
              :chart-labels="history.map(el => Object.keys(el)[0])"
              :chart-background-colors="attributes.chartStyle.backgroundColors"
            ></component>
            <div v-else class="d-flex flex-column justify-content-center align-items-center px-2">
              <div class="pt-2 pb-2 text-center" style="height:100%;font-size:12px;font-weight:bold;color:#666666;">
                {{ $t(`message.dashboard.${attributes.translationKey}.plots.title.${'timeBasedScoring'}`) }}
              </div>
              <div class="font-weight-light text-center">
                {{ $t("message.no_data") }}
              </div>
            </div>
          </div></div>
        <!-- Graphs container Today-->
        <div v-if="isToday && dimension !== 'score'"
          class="d-flex flex-wrap"
        >
          <div v-if="!metrics || metrics.length === 0">
            {{ $t("message.no_data") }}
          </div>
          <div
            v-else
            v-for="(value, key) in metrics"
            :key="`${dimension}:${key}`"
            class="graph-container position-relative"
            :class="attributes.indicators[key].graphType"
          >
            <component
              v-if="metrics[key] && metrics[key].length > 0 && attributes.indicators[key].graphType === 'doughnut-chart' && key === 'downloadUrlAvailability'"
              :is="attributes.indicators[key].graphType"
              :width="300"
              :height="220"
              :title="$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)"
              :chart-data="metrics[key].map(el => el.percentage)"
              :chart-labels="metrics[key].map(el => translateYesAndNo(el.name))"
              :chart-background-colors="attributes.chartStyle.backgroundColors"
            />
            <component
              v-else
              :is="attributes.indicators[key].graphType"
              :width="200"
              :height="attributes.indicators[key].graphType === 'line-chart' ? 400 : 200"
              :title="$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)"
              :chart-data="metrics[key] ? metrics[key].map(el => el.percentage) : []"
              :chart-labels="metrics[key] ? metrics[key].map(el => translateYesAndNo(el.name)) : []"
              :chart-background-colors="attributes.chartStyle.backgroundColors"
            />
            <div  class="d-flex justify-content-center" v-if="metrics[key] && metrics[key].length > 0">
              <anchor-router-link class="anchor-link" :to="{name: 'Methodology',hash: '#'+(`${key}`)}">{{$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)}}</anchor-router-link>
                <i class="material-icons small-icon pl-1"
                 v-tooltip="{
            placement: 'bottom',
            content: 'Click here to find out how this indicator is calculated',
            delay: { show: 500, hide: 100 }
          }" >help_outline</i>
            </div>
            <div v-else class="d-flex flex-column justify-content-center align-items-center px-2">
              <div class="pt-2 pb-2 text-center" style="height:100%;font-size:12px;font-weight:bold;color:#666666;">
                {{ $t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`) }}
              </div>
              <div class="font-weight-light text-center">
                {{ $t("message.no_data") }}
              </div>
            </div>
            </div>
        </div>
          <!-- Graphs container History-->
        <div v-if="isHistory && dimension !== 'score'"
          class="d-flex flex-wrap"
        >
          <div v-if="!getHistory || getHistory.length === 0">
            {{ $t("message.no_data") }}
          </div>
          <div
            v-else
            v-for="(value, key) in history"
            :key="`${dimension}:${key}`"
            class="graph-container position-relative"
            :class="attributes.indicators[key].graphType"
          >
          <div v-if="dimension === 'accessibility' && key !== 'downloadUrlAvailability'">
            <component
                v-if="history[key] && history[key].length > 0 && !isHistoryEmpty(history[key])"
                :is="attributes.indicators[key].graphTypeHistory"
                :width="200"
                :height="attributes.indicators[key].graphTypeHistory === 'line-chart-history' ? 400 : 200"
                :title="$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)"
                :chart-data="historyValues(history[key]).map((item) => item[1] && item[0].percentage)"
                :chart-labels="historyLabels(history[key])"
                :chart-background-colors="attributes.chartStyle.backgroundColors"
              ></component>
            <div  class="d-flex justify-content-center" v-if="metrics[key] && metrics[key].length > 0">
              <anchor-router-link class="anchor-link" :to="{name: 'Methodology',hash: '#'+(`${key}`)}">{{$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)}}</anchor-router-link>
              <i class="material-icons small-icon pl-1"
                 v-tooltip="{
            placement: 'bottom',
            content: 'Click here to find out how this indicator is calculated',
            delay: { show: 500, hide: 100 }
          }" >help_outline</i>
            </div>
              <div v-else class="d-flex flex-column justify-content-center align-items-center px-2">
                <div class="pt-2 pb-2 text-center" style="height:100%;font-size:12px;font-weight:bold;color:#666666;">
                  {{ $t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`) }}
                </div>
                <div class="font-weight-light text-center">
                  {{ $t("message.no_data") }}
                </div>
              </div>
            </div>
            <div v-else>
                <component
                v-if="history[key] && history[key].length > 0 && !isHistoryEmpty(history[key])"
                :is="attributes.indicators[key].graphTypeHistory"
                :width="200"
                :height="attributes.indicators[key].graphTypeHistory === 'line-chart-history' ? 400 : 200"
                :title="$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)"
                :chart-data="historyValues(history[key]).map((item) => item[1] && item[1].percentage)"
                :chart-labels="historyLabels(history[key])"
                :chart-background-colors="attributes.chartStyle.backgroundColors"
              ></component>
              <div  class="d-flex justify-content-center" v-if="metrics[key] && metrics[key].length > 0">
                <anchor-router-link class="anchor-link" :to="{name: 'Methodology',hash: '#'+(`${key}`)}">{{$t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`)}}</anchor-router-link>
                <i class="material-icons small-icon pl-1"
                   v-tooltip="{
            placement: 'bottom',
            content: 'Click here to find out how this indicator is calculated',
            delay: { show: 500, hide: 100 }
          }" >help_outline</i>
              </div>
              <div v-else class="d-flex flex-column justify-content-center align-items-center px-2">
                <div class="pt-2 pb-2 text-center" style="height:100%;font-size:12px;font-weight:bold;color:#666666;">
                  {{ $t(`message.dashboard.${attributes.translationKey}.plots.title.${key}`) }}
                </div>
                <div class="font-weight-light text-center">
                  {{ $t("message.no_data") }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- Sidebar -->
      <div class="col-md-3 pl-0 border-right sidebar order-first">
        <nav class="pt-md-5 pr-md-5 pl-md-5 sidebar-content">
          <ul class="nav d-flex flex-column">
            <!-- eslint-disable vue/no-unused-vars -->
            <li
              class="sidebar-nav-element mb-1 mb-md-2"
              v-for="({label, className}, identifier) in dimensionsAttributes"
              :key="identifier"
            >
              <router-link
                :class="`primary-color-${attributes.className}`"
                :active-class="`sidebar-nav-link-active primary-bg-color-${className}`"
                class="btn sidebar-nav-link pt-2 pb-2 text-left"
                :to="{ name: dimensionsType, params: {dimension: identifier}, query: Object.assign({}, { locale: $i18n.locale })}"
              >
                {{ $t(`message.dashboard.${identifier === 'score' ? 'scoring' : identifier}.title`) }}
              </router-link>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import DoughnutChart from './graphs/DoughnutChart'
import BarChart from './graphs/BarChart'
import LineChart from './graphs/LineChart'
import LineChartHistory from './graphs/LineChartHistory'
import dimensionsAttributes from './utils/dimensions-attributes'
import AnchorRouterLink from 'vue-anchor-router-link'

export default {
  name: 'DimensionsDetails',
  components: {
    DoughnutChart,
    BarChart,
    LineChart,
    LineChartHistory,
    AnchorRouterLink
  },
  data () {
    return {
      name: this.$route.params.name,
      dimensionsAttributes,
      loaded: false,
      isToday: true,
      isHistory: false
    }
  },
  props: {
    dimensionsType: {
      type: String,
      required: true,
      default () {
        return 'dimensions-details'
      }
    }
  },
  computed: {
    ...mapGetters({
      storeMetrics: 'metrics',
      storeHistory: 'history'
    }),
    getMetrics () {
      return this.storeMetrics
    },
    getHistory () {
      return this.storeHistory
    },
    dimension () {
      return this.$route.params.dimension
    },
    metrics () {
      // Returns metrics data of the current dimension
      const dimension = this.$route.params.dimension

      const metrics = this.getMetrics
        ? this.getMetrics[dimension] || undefined
        : undefined

      return this.dimension === 'score'
        ? metrics
        : this.transformToRenderableMetrics(metrics)
    },
    history () {
      // Returns history data of the current dimension
      const dimension = this.$route.params.dimension

      const history = this.getHistory
        ? this.getHistory[dimension] || undefined
        : undefined

      return this.dimension === 'score'
        ? history
        : this.transformToRenderableMetrics(history)
    },
    attributes () {
      return this.dimensionsAttributes[this.dimension]
    }
  },
  async created () {
  },
  methods: {
    graphType (indicator) {
      return this.attributes.indicators[indicator].graphType
    },
    /**
     * Returns an array of history label values only
     * @param indicatorHistory The history data object of an indicator in the shape of [{key1: val1}, {key2: val2}, ...]
     */
    historyLabels (indicatorHistory) {
      if (!indicatorHistory) return undefined
      return indicatorHistory.map(e => Object.keys(e)[0])
    },
    /**
     * Returns an array of history data values only
     * @param history The history data object of an indicator in the shape of [{key1: val1}, {key2: val2}, ...]
     */
    historyValues (indicatorHistory) {
      if (!indicatorHistory) return undefined
      return indicatorHistory.map(e => Object.values(e)[0])
    },
    /**
     * Returns true, if, and only if, there are no history data values
     * @param indicatorHistory The history data object of an indicator in the shape of [{key1: val1}, {key2: val2}, ...]
     */
    isHistoryEmpty (indicatorHistory) {
      return this.historyValues(indicatorHistory).every(e => !e || e.length === 0)
    },
    translateYesAndNo (string) {
      // eslint-disable-next-line no-console
      console.info("translateYesAndNo", string)
      // checks backend string (yes or no) and translates it
      const stringLowerCase = string.toLowerCase()
      if (stringLowerCase === 'yes') return this.$t('message.common.yes').toLowerCase();
      else if (stringLowerCase === 'no') return this.$t('message.common.no').toLowerCase();
      else return string;
    },
    /**
     * Returns a transformed (actually filtered) metrics object containing renderable indicators only,
     * i.e., all indicators that have an entry in this.attributes
     * @note Fixes bug mentioned in https://gitlab.fokus.fraunhofer.de/piveau/organisation/piveau-scrum-board/-/issues/1341#note_140426
     * @param metrics The metrics or history object containing indicators objects as keys
     */
    transformToRenderableMetrics (metrics = {}) {
      const allowedIndicators = Object.keys(this.attributes.indicators)
      const renderableMetrics = Object.keys(metrics)
        .filter((indicator) => allowedIndicators.includes(indicator)) // only allow indicators from allowedIndicators
        .reduce((obj, key) => ({ ...obj, [key]: metrics[key] }), {}) // build filtered metrics object from resulting array

      return renderableMetrics
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../styles/metrics-style.scss";
@import "~bootstrap/scss/bootstrap";

.btn-light {
  background: #F2F4F9;
  border-radius: 0 !important;//1.875rem !important;

  &:hover {
    background: #EFF2FE;
  }
}
.active {
  background: #D7DAE2 !important;
}

.dimensions-details-wrapper {
  max-height: 100%;
  height: 100vh;
  overflow: auto;

  .sidebar {
    background-color: $gray-lightest;
  }

  @include media-breakpoint-up(md) {
    .sidebar {
      // height: 100%;
    }
  }
}

@include media-breakpoint-up(md) {
  .dimensions-details-wrapper {
    max-height: 100%;
    height: 100vh;
  }
}

@include media-breakpoint-up(lg) {
  .dimensions-details-wrapper {
    max-height: 656px;
    height: 90vh;
  }
}

.title { font-size: 2.5rem; }
.graph-container {
  height: 25%;
  width: 100%;
  .no-data-container {
    min-width: 200px;
    height: 200px;
  }
  &.line-chart {
    width: 100% !important;
  }
  &.line-chart-history {
    width: 100% !important;
  }
}

@include media-breakpoint-up(sm) {
  .graph-container {
    height: 25%;
    width: 50%
  }
}

@include media-breakpoint-up(lg) {
  .graph-container {
    height: 25%;
    width: 25%
  }
}

@media (max-width: 575px) {
    .history-btn-group {
    float:left;
    margin-top: 10px;
    margin-bottom: 10px;}
   #methodologylink {
    position: unset;
  }
}

@media (min-width: 576px) {
    .title { font-size: 2.5rem; }
    .history-btn-group {
    float:right;
    margin-top: -34px;
    margin-right: 10px;
  }
}
@media (min-width: 768px) {
    .title { font-size: 3.25rem; }
    .history-btn-group {
    float:right;
    margin-top: -34px;
    margin-right: 10px;
  }
}
@media (min-width: 992px) {
    .title { font-size: 4.5rem; }
    .history-btn-group {
      float:right;
      margin-top: -34px;
      margin-right: 10px;
  }
}
@media (min-width: 1200px) {
    .title { font-size: 4.5rem; }
    .history-btn-group {
    float:right;
    margin-top: -34px;
    margin-right: 10px;
  }

}

.details-body {
  // Prozent Text Doughnut
  .absolute-center {
    position: absolute;
    top: 55%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: -30; //z achse nach hinten gerückt, damit die Hover der Diagramme besser sichtbarer sind.
  }

  .pulse {
    display: block;
    width: 22px;
    height: 22px;
    border-radius: 50%;
    background: $primary;
    cursor: pointer;
    box-shadow: 0 0 0 $primary;
    animation: pulse 2s infinite;
  }

  @-webkit-keyframes pulse {
    0% {
      -webkit-box-shadow: 0 0 0 0 $primary;
    }
    70% {
        -webkit-box-shadow: 0 0 0 10px rgba(204,169,44, 0);
    }
    100% {
        -webkit-box-shadow: 0 0 0 0 rgba(204,169,44, 0);
    }
  }
  @keyframes pulse {
    0% {
      -moz-box-shadow: 0 0 0 0 $primary;
      box-shadow: 0 0 0 0 $primary;
    }
    70% {
        -moz-box-shadow: 0 0 0 10px rgba(204,169,44, 0);
        box-shadow: 0 0 0 10px rgba(204,169,44, 0);
    }
    100% {
        -moz-box-shadow: 0 0 0 0 rgba(204,169,44, 0);
        box-shadow: 0 0 0 0 rgba(204,169,44, 0);
    }
  }
}

.sidebar {
  .sidebar-nav-link {
    font-size: 1.1rem;
    border-radius: 0 !important;//1.875rem;
  }

  .sidebar-nav-link  {
    &:hover {
      text-decoration: underline;
    }
    &.sidebar-nav-link-active {
      //color: white;
      font-weight: 400;
      &:hover {
        text-decoration: none!important;
      }
    }
  }
}

.primary-bg-color-accessibility,
.primary-bg-color-findability,
.primary-bg-color-interoperability,
.primary-bg-color-reusability,
.primary-bg-color-contextual,
.primary-bg-color-scoring
{
  background-color: #ddd;
}
//.primary-bg-color-accessibility {
//  background-color: $deu-logo-medium-blue;
//}
//
//.primary-bg-color-findability {
//  background-color: $deu-badge-green;
//}
//
//.primary-bg-color-interoperability {
//  background-color: $deu-logo-blue;
//}
//
//.primary-bg-color-reusability {
//  background-color: $deu-dark-orange;
//}
//
//.primary-bg-color-contextual {
//  background-color: $deu-logo-yellow;
//}
//
//.primary-bg-color-scoring {
//  background-color: $deu-badge-black;
//}

.primary-color-accessibility,
.primary-color-findability,
.primary-color-interoperability,
.primary-color-reusability
.primary-color-contextual,
.primary-color-scoring
{
  color: #404040;
}

//.primary-color-accessibility {
//  color: $deu-logo-medium-blue;
//}
//
//.primary-color-findability {
//  color: $deu-badge-green;
//}
//
//.primary-color-interoperability {
//  color: $deu-logo-blue;
//}
//
//.primary-color-reusability {
//  color: $deu-dark-orange;
//}
//
//.primary-color-contextual {
//  color: $deu-logo-yellow;
//}
//
//.primary-color-scoring {
//  color: $deu-badge-black;
//}

#today {
}

#history{
  display:none;
}
.anchor-link {
  text-align: center;
  font-size: small;
}
.material-icons.small-icon {
  font-size: 15px;
  cursor: pointer;
}

.bar-chart {
  width: 280px;
}
</style>
