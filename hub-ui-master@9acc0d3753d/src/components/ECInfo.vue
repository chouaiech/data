<template>
  <div :class="`ecl-message ecl-message--${type}`" data-ecl-message="" ref="infobox">
    <svg class="ecl-icon ecl-icon--l ecl-message__icon" focusable="false" aria-hidden="true">
      <use v-if="type === 'info'" xlink:href="@/assets/img/ecl/icons.svg#information"></use>
      <use v-if="type === 'warning'" xlink:href="@/assets/img/ecl/icons.svg#warning"></use>
      <use v-if="type === 'success'" xlink:href="@/assets/img/ecl/icons.svg#success"></use>
      <use v-if="type === 'error'" xlink:href="@/assets/img/ecl/icons.svg#error"></use>
    </svg>
    <div class="ecl-message__content">
      <button @click="close()" class="ecl-button ecl-button--ghost ecl-message__close" type="button">
        <span class="ecl-button__container">
          <span class="ecl-button__label" data-ecl-label="true">Close</span>
          <svg class="ecl-icon ecl-icon--xs ecl-button__icon ecl-button__icon--after" focusable="false" aria-hidden="true">
            <use xlink:href="@/assets/img/ecl/icons.svg#close-filled"></use>
          </svg>
        </span>
      </button>
      <div v-if="title" class="ecl-message__title">{{ title }}</div>
      <div class="ecl-message__description">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ECInfo",
  props: {
    title: String,
    message: String,
    type: { // info, success, warning, error
      type: String,
      default: "info"
    }
  },
  methods: {
    close() {
      this.$refs.infobox.style.display = "none";
    }
  }
}
</script>
