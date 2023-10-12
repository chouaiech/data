<template>
  <div :class="`ecl-checkbox${ disabled ? ' ecl-checkbox--disabled' : ''}`" @click.prevent="handleClick">
    <input type="checkbox" :name="id" class="ecl-checkbox__input" v-model="state" :id="id"
           :disabled="disabled" />
    <label :for="id" class="ecl-checkbox__label">
      <span :class="`ecl-checkbox__box${ disabled ? ' ecl-checkbox__box--disabled' : ''}`">
        <svg class="ecl-icon ecl-icon--m ecl-checkbox__icon" focusable="false" aria-hidden="true">
          <use xlink:href="../assets/img/ecl/icons.svg#check"></use>
        </svg>
      </span>
      <span v-if="label && ! labelRight" class="ecl-checkbox__text">{{ label }}</span>
      <div class="labels" v-if="label && labelRight">
        <span class="ecl-checkbox__text">{{ label }}</span>
        <span class="ecl-checkbox__text">{{ labelRight }}</span>
      </div>
    </label>
    <div v-if="helpText" class="ecl-checkbox__help">{{ helpText }}</div>
  </div>
</template>

<script>
export default {
  name: "ECCheckbox",
  props: {
    id: String,
    checked: Boolean,
    label: [Number, String],
    labelRight: [Number, String],
    helpText: String,
    disabled: Boolean,
    onClick: Function
  },
  data() {
    return {
      state: this.checked
    }
  },
  methods: {
    handleClick() {
      this.onClick(this.state);
    }
  }
}
</script>

<style scoped lang="scss">

.ecl-checkbox__label {
  width: 100%;
}

.labels {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  flex: 1;
  span:last-child {
    margin-left: 6px;
  }
}

label {
  margin-bottom: 0;
}

</style>
