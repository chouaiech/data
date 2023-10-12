<template>
  <button
    class="d-flex justify-content-center"
    :class="` ecl-button ecl-button--${primary? 'primary' : 'secondary'}`"
    @click="onClick()"
    :title="tooltip"
  >
    {{ label }}
    <slot />
    <svg v-if="download" class="ecl-icon ecl-icon--fluid ecl-button__icon ecl-button__icon--after"
         focusable="false" aria-hidden="true" data-ecl-icon="">
      <use xlink:href="@/assets/img/ecl/icons.svg#download" />
    </svg>
  </button>
</template>

<script>
export default {
  name: "ECButton",
  props: {
    primary: Boolean,
    label: String,
    action: Function,
    tooltip: String,
    download: Boolean
  },
  methods: {
    onClick() {
      if (typeof this.action === "function") this.action();
    }
  }
}
</script>

<style lang="scss" scoped>
  .ecl-icon {
    margin-left: 8px !important;
  }
</style>
