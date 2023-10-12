<template>
  <div :key="id">
    <div class="container">
      <div
        class="row"
        :lang="$i18n.locale"
      >
        <div
          fit-width="true"
          class="dimensions w-100 ecl-u-mt-l"
        >
          <div
            v-for="({label, className, description, indicators}, identifier) in dimensionsAttributes"
            :key="identifier"
          >
            <router-link active-class="" :to="{ name: dimensionsType, params: {dimension: identifier}, query: { locale: $i18n.locale } }">
              <dimensions-item
                v-masonry-tile
                :class="`dimension-item dimension-${className}`"
                :identifier="identifier"
                :indicator-values="getMetrics ? getMetrics[identifier] : undefined"
                :indicators="indicators"
              >
                <template #title>
                  <!-- todo: change i18n key scoring to score -->
                  {{ $t(`message.dashboard.${identifier === 'score' ? 'scoring' : identifier}.title`) }}
                </template>
                <template #score>
                  <span v-if="identifier === 'score'"
                        class="ecl-u-d-flex ecl-u-justify-content-between ecl-u-border-all ecl-u-border-width-1 ecl-u-border-color-blue-n total-score"
                        :class="scoreClasses(score)"
                  >
                    <!-- Treat the scoring card with special markup -->
                    <div style="margin-right:10px">
                      <i
                        class="score-icon far"
                        :class="{
                          'fa-question-circle': scoreLevel === -1,
                          'fa-times-circle': scoreLevel === 0,
                          'fa-check-circle': scoreLevel > 0
                        }"
                      />
                    </div>
                    <p class="ecl-u-type-prolonged-xl ecl-u-ma-none ecl-u-type-color-white">{{ scoreInfo }}</p>
                  </span>
                  <span v-else>
                    <p class="ecl-u-type-prolonged-xl ecl-u-ma-none ecl-u-type-color-white">{{ scoreInfo }}</p>
                  </span>
                </template>
                <!-- Optional description content here -->
                <!-- {{ description }} -->
              </dimensions-item>
            </router-link>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal window for displaying details view. See DimensionsDetailsModal -->
    <router-view />
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import DimensionsItem from './DimensionsItem.vue';
import dimensionsAttributes from './utils/dimensions-attributes';
import {scoreClasses, scoreLevel} from "./utils/score";

export default {
  name: 'Dimensions',
  components: {
    DimensionsItem
  },
  data () {
    return {
      id: this.$route.params.id,
      dimensionsAttributes
    }
  },
  props: {
    dimensionsType: {
      type: String,
      required: true,
      default() {
        return 'dimensions-details'
      }
    }
  },
  computed: {
    ...mapGetters([
      'metrics',
      'history',
      'isLoadingMetrics'
    ]),
    getMetrics() {
      return this.metrics
    },
    getHistory() {
      return this.history
    },
    useGlobalMetrics() {
      return this.dimensionsType === 'dimensions-details';
    },
    useCatalogMetrics() {
      return this.dimensionsType === 'catalogue-dimensions-details';
    },
    score() {
      const metrics = this.getMetrics;
      if (metrics) {
        const score = metrics.score;
        if (score >= 0) {
          return score;
        }
      }
      return -1;
    },
    scoreLevel() {
      return scoreLevel(this.score);
    },
    scoreInfo() {
      if (this.isLoadingMetrics) return "";
      switch (this.scoreLevel) {
        case 3:
          return this.$t('message.methodology.scoring.table.excellent');
        case 2:
          return this.$t('message.methodology.scoring.table.good');
        case 1:
          return this.$t('message.methodology.scoring.table.sufficient');
        case 0:
          return this.$t('message.methodology.scoring.table.bad');
        default:
          return this.$t('message.no_data');
      }
    }
  },
  created() {
    if (this.$route.params.dimension) {
      this.$modal.show('dimensions-details-modal')
    }
  },
  mounted() {
    this.loadMetrics()
    this.loadHistoryMetrics()
    this.setup()
  },
  methods: {
    ...mapActions('globalModule', {
      loadGlobalMetrics: 'fetchMetrics'
    }),
    ...mapActions('globalHistoryModule', {
      loadGlobalHistoryMetrics: 'fetchHistoryMetrics'
    }),
    ...mapActions('catalogueModule', {
      loadCatalogueMetrics: 'fetchMetrics',
      loadCatalogueHistoryMetrics: 'fetchHistoryMetrics'
    }),
    ...mapActions('countryModule', {
      loadCountryMetrics: 'fetchMetrics',
      loadCountryHistoryMetrics: 'fetchHistoryMetrics'
    }),
    scoreClasses,
    loadMetrics() {
      if (this.useGlobalMetrics) this.loadGlobalMetrics()
      else if (this.useCatalogMetrics) this.loadCatalogueMetrics(this.id)
      else this.loadCountryMetrics(this.id)
    },
    loadHistoryMetrics() {
      if (this.useGlobalMetrics) this.loadGlobalHistoryMetrics()
      else if (this.useCatalogMetrics) this.loadCatalogueHistoryMetrics(this.id)
      else this.loadCountryHistoryMetrics(this.id)
    },
    async setup() {
      // Wait for a bit and then update the masonry layout
      // to prevent the layout from overlapping occassionally at initialization
      await this.sleep(950);
      this.$redrawVueMasonry();
    },
    sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    }
  },
  watch: {
    'id': () => {
      this.loadMetrics()
      this.loadHistoryMetrics()
    },
    getMetrics: function () {
      this.setup()
    },
    getHistory: function () {
      this.setup()
    }
  }
}
</script>

<style lang="scss" scoped>

// @mixin gradient($colors...) {
//   background-color: nth($colors, 1);
//   background: -webkit-linear-gradient(left top, $colors);
//   background: linear-gradient(to left top, $colors);
// }

// .dimension-accessibility {
//   @include gradient($deu-logo-medium-blue-gradient);
// }

// .dimension-findability {
//   @include gradient($deu-badge-green-gradient);
// }

// .dimension-interoperability {
//   @include gradient($deu-logo-blue-gradient);
// }

// .dimension-reusability {
//   @include gradient($deu-dark-orange-gradient);
// }

// .dimension-contextual {
//   @include gradient($deu-logo-yellow-gradient);
// }

// .dimension-total {
//   background-color: #F5F5F5;
// }

.dimension-item {
  transition: width 2s, height 4s;
  .total-score {
    padding: 22px 25px;
    // font-size: 1.1rem;
    // font-weight: normal;
    .score-icon {
      font-size: 1.5rem;
    }
    //.score {
    //  &--bad {
    //    color: #f67280;
    //  }
    //  &--sufficient {
    //    color: white;
    //  }
    //  &--good {
    //    color: #a7e9af;
    //  }
    //  &--excellent {
    //    color: #b0deff;
    //  }
    //}
  }
}

.dimension-score {
  background-image: linear-gradient(
    to right top,
    #71e0b9,
    #26c5c1,
    #00a8c2,
    #008ab8,
    #3b6aa0
  );
}
</style>
