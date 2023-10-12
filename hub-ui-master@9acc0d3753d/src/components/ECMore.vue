<template>
  <button @click="action" class="p-0 ecl-button show-more" tabindex="-1">
    <span class="ecl-button__label" data-ecl-label="true">{{ label }}</span>
    <svg :class="`ecl-icon ecl-icon--fluid ${upArrow ? '' : 'ecl-icon--rotate-180'} ecl-button__icon ecl-button__icon--after`"
        focusable="false" aria-hidden="true" data-ecl-icon="">
      <use xlink:href="@/assets/img/ecl/icons.svg#corner-arrow"></use>
    </svg>
  </button>
</template>

<script>
export default {
  name: "ECMore",
  props: {
    label: String,
    upArrow: Boolean,
    action: Function
  }
}
</script>

<style scoped lang="scss">

button {
  margin-top: 20px;
}

</style>
