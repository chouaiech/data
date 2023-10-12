<template>
  <div
    class="dimension-card d-inline-block w-100 ecl-u-border-all ecl-u-border-width-1"
    :class="{ clickable: isClickable }">
    <div class="ecl-u-d-flex ecl-u-justify-content-between dimensions-item-head">
      <!-- Title -->
      <h3
        v-if="identifier === 'score'"
        class="ecl-u-type-heading-3 ecl-u-type-color-blue card-title pt-0 pt-md-2 pb-2 text-center"
        :class="{ 'mb-0': true }"
      >
        <slot name="title" />
      </h3>
      <h4 v-else
        class="ecl-u-type-heading-4 ecl-u-type-color-blue card-title pt-0 pt-md-2 pb-2 text-center "
        :class="{ 'mb-0': true }"
      >
        <slot name="title" />
      </h4>
      <!-- Score -->
      <div
        v-if="!!$slots.score && identifier === 'score'"
        class="ecl-u-bg-blue-n"
      >
        <slot
          name="score"
        >
          emptiness
        </slot>
      </div>
      <!-- Description -->
      <p v-if="$slots.default" class="card-text">
        <!-- Default slot for description text -->
        <slot />
      </p>
      <div v-if="identifier !== 'score'"
           class="ecl-u-type-prolonged-l d-flex justify-content-center align-items-center px-3 score"
           :class="scoreClasses(scorePoints, identifier)"
      >
        <div class="d-flex align-items-end">
          <div class="score-points">{{ scorePoints }}</div>
          <div class="score-base">/{{ scoreBase }}</div>
        </div>
      </div>
    </div>
    <!-- Indicators -->
    <div v-if="identifier !== 'score'" class="indicators-list ">
      <dimensions-item-indicator
        v-for="(_, key) in indicators"
        :key="key"
        :indicator="$t(`message.dashboard.${identifier}.plots.title.${key}`)"
        :indicator-tooltip="`${$t(`message.methodology.dimensions.${indicators[key].tooltipDescription}`)}<br><br>${$t(`message.methodology.dimensions.${indicators[key].tooltipMetrics}`)}`"
        :values="indicatorValues[key]"
        :filter-primary-value="indicators[key].primary"
        :display-name="indicators[key].displayName"
        data-html="true"
      />
    </div>
  </div>
</template>

<script>
import DimensionsItemIndicator from './DimensionsItemIndicator'
import {mapGetters} from 'vuex'
import {scoreClasses} from "./utils/score";

export default {
  name: 'DimensionsItem',
  components: { DimensionsItemIndicator },
  props: {
    // Identifier for this card
    identifier: {
      type: String,
      default () {
        return ''
      }
    },
    // Metric values for all indicator for this card
    indicatorValues: {
      // Must be Object (multiple indicators) or Number (for scoring card)
      type: [Object, Number],
      default () {
        return {}
      }
    },
    /**
     * Indicator attributes e.g. human readable labels
     */
    indicators: {
      type: Object,
      default () {
        return {}
      }
    },
    isClickable: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters([
      'metrics'
    ]),
    score() {
      const metrics = this.metrics[this.identifier];
      return metrics ? metrics.score : null;
    },
    scorePoints() {
      return this.score ? this.score.points : null;
    },
    scoreBase() {
      if ( ! this.score) return null;
      return Math.ceil(this.scorePoints / this.score.percentage * 100);
    }
  },
  methods: {
    scoreClasses
  }
}
</script>

<style lang="scss" scoped>
@import "../../styles/metrics-style.scss";

.score {
  min-width: 80px;
}

.score-points {
  font-weight: bold;
  line-height: normal;
}

.score-base {
  font-size: small;
  margin-left: 3px;
  line-height: normal;
}

.dimension-card {
  // Default background color to be overwritten
  background-color: #F5F5F5;
  color: rgba(white, 0.89);
  border-color: #CFCFCF!important;
  margin-bottom: 20px;

  &.clickable {
    transition: transform .4s ease;

    &:hover {
    transition: transform .4s ease;
    transform: scale(1.01);
    }

    &::after {
      content: '';
      position: absolute;
      z-index: -1;
      top: 0;
      right: 0;
      bottom: 0;
      right: 0;
      width: 100%;
      height: 100%;
      transition: opacity .5s cubic-bezier(.25, .8, .25, 1);
      box-shadow: 0 12px 24px rgba(0, 0, 0, 0.25), 0 4px 8px rgba(0, 0, 0, 0.22);
      opacity: 0;
    }

    &:hover::after {
      opacity: 1;
    }
  }

  .list-group {
    .list-group-item {
      margin-bottom: -1px;
      border: 0;
      background: #F5F5F5;
    }
  }

  .card-title {
    hyphens: auto;
    align-self: center;
    margin-left: 15px;
  }

}
.dimension-scoring {
  margin-bottom: 15px;
  border-left: 5px solid #006FB4!important;
}
.indicators-list {
  width: 100%;
  display: inline-grid;
  grid-template-columns: 256px 256px 256px 256px;
  padding: 5px 15px 28px 20px;
  gap: 15px;
}
.ecl-u-type-heading-4 {
  margin-left: 20px!important;
}
@media only screen and (max-width: 1200px) {
  .indicators-list {
     grid-template-columns: auto auto;
  }
}
@media only screen and (max-width: 600px) {
  .indicators-list {
     grid-template-columns: auto;
  }
}
</style>
