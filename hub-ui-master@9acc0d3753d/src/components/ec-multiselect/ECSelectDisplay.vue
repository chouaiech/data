<template>
  <div @click="activate"
       @keydown.esc="away"
       @keydown.enter="activate"
       class="value-display list-group-item col w-100 d-flex flex-row justify-content-between p-0 align-items-center"
       type="button"
       tabindex="0"
       ref="value-display"
       aria-haspopup="true"
       aria-expanded="false">
        <span data-toggle="tooltip"
              data-placement="center"
              class="ml-2 overflow-hidden"
              :class="{empty: !text}"
        >
          {{ text || placeholder || "Select" }}
        </span>
    <div class="ecl-select__icon">
      <svg class="ecl-icon ecl-icon--s ecl-icon--rotate-180 ecl-select__icon-shape" focusable="false" aria-hidden="true">
        <use xlink:href="@/assets/img/ecl/icons.svg#corner-arrow"></use>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "ECSelectDisplay",
  props: {
    title: String,
    text: String,
    placeholder: String,
    showOptions: Function,
    away: Function,
  },
  methods: {
    activate() {
      this.showOptions();
      this.$refs["value-display"].focus();
    }
  }
}
</script>

<style scoped lang="scss">

.value-display {
  min-height: 48px;
  border-color: #707070;
  &:focus {
     border-width: 3px;
  }
  &:hover {
    border-color: var(--primary);
    .ecl-select__icon {
      background: var(--primary);
    }
  }
}



</style>
