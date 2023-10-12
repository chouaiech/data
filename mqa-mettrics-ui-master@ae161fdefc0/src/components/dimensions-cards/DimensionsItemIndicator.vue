<template>
   <div class="dimensions-item-indicator">
      <i class="material-icons align-text-bottom small-icon ecl-u-type-color-blue-n"
         v-tooltip="{
         placement: 'right',
         content: getIndicatorTooltip,
         delay: { show: 500, hide: 100 }
         }">info_outline</i>
      <div class="indicator">
         <!-- Key (e.g. "Spatial Availability") -->
         <p style="max-width: 165px!important;" class="text-truncate ecl-u-type-paragraph-m  ecl-u-type-color-blue-130 ecl-u-pb-m d-inline-block text-truncate">{{ indicator }}</p>
         <div
            v-tooltip="{
            placement: 'top-center',
            content: percentageHintTooltip,
            delay: { show: 500, hide: 100 }
            }"
            class=""
            >
            <span v-if="displayName" :class="{'text-monospace' : primaryValue}">
               <!-- Display indicator value name e.g. "200" -->
               <p class="ecl-u-type-paragraph-m  ecl-u-type-color-blue-130  ecl-u-pb-m">{{ primaryValue | formatName }}</p>
            </span>
            <span v-else>
               <!-- Display indicator value percentage (e.g. "78%")  -->
               <p class="ecl-u-type-paragraph-m  ecl-u-type-color-blue-130 ecl-u-pb-m">{{ primaryValue | formatValue }}</p>
            </span>
         </div>
      </div>
   </div>
</template>
<script>

export default {
  name: 'DimensionsItemIndicator',
  filters: {
    formatName (value) {
      if (!value) return '??'
      return `${value.name}` || '??'
    },
    formatValue (value) {
      if (!value) return '??'
      return value.percentage || value.percentage === 0
        ? `${value.percentage}%`
        : '??'
    }
  },
  props: {
    // Name for this indicator e.g. "License available"
    indicator: {
      type: String,
      default: ''
    },
    indicatorTooltip: {
      type: String,
      default: undefined
    },
    // Array containing every indicator feature value of this indicator
    // e.g. [{name: "yes", percentage: 89}, {name: "no", percentage: 11}]
    values: {
      type: Array | Object,
      default () {
        return []
      }
    },
    filterPrimaryValue: {
      type: String | Function,
      default: undefined
    },
    displayName: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    // Returns for this indicator the 'primary' metric, if filterPrimaryValue is set.
    // The filterPrimaryValue can be either a String e.g. '200' or a function.
    // If it's a String, then primaryValue returns a value object for this indicator such that value.name === filterPrimaryValue
    // If it's a function, it will return filterPrimaryValue(values)
    // For example, for indicator 'most frequent acessURLs', the filterPrimaryValue is a function that returns that maximum indicator metric e.g. '200'
    primaryValue () {
      if (!this.values || this.values.length === 0) return null
      let primary = this.filterPrimaryValue ? this.filterPrimaryValue : this.values[0]

      if (typeof primary === 'function') {
        return primary(this.values) || this.values[0]
      }

      const value = this.values.find(value => value.name === primary.name)
      return value || this.values[0]
    },
    getIndicatorTooltip () {
      return `${this.indicatorTooltip ? this.indicatorTooltip : this.indicator}`
    },
    percentageHintTooltip () {
      if (!this.displayName || !this.primaryValue) return ''
      return this.primaryValue.percentage ? `${this.primaryValue.percentage}%` : ''
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/styles/metrics-style.scss";

/*** MATERIAL ICONS ***/
  .material-icons.small-icon {
    font-size: 20px;
  }
  .ecl-u-type-paragraph-m {
    margin: 0!important;
  }
  .indicator {
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #BFD0E4;
  }
</style>
