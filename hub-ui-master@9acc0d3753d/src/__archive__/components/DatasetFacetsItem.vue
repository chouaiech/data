<template>
  <button
    :title="tooltip"
    >
      <span ref="facetBtn" class="text-truncate">{{ title }}</span>
      <span v-if="!hideCount" class="facet-count badge">{{ count.toLocaleString('fi') }}</span>
  </button>
</template>

<script>
export default {
  name: 'DatasetFacetsItem',
  data() {
    return {
      tooltip: '',
    };
  },
  props: {
    title: {
      type: String,
      default: '',
    },
    count: {
      type: [Number, String],
      default: 0,
    },
    hideCount: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    // Sets tooltip text to title when title itself is rendered truncated
    this.tooltip = this.$refs.facetBtn.offsetWidth < this.$refs.facetBtn.scrollWidth
      ? this.title
      : '';
  },
};
</script>

<style>

</style>
