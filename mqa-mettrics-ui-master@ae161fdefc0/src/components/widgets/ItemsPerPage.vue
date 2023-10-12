<template>
  <div class="items-per-page">
    <div class="d-inline align-middle mr-2">Items per Page:</div>
    <div class="col-right d-inline" role="group" aria-label="Items per Page Dropdown">
      <div class="btn-group items-per-page-dropdown mr-0" role="group">
        <button
          class="value-display list-group-item col d-flex flex-row justify-content-between p-0 align-items-center"
          id="itemsPerPageDropdown" type="button" data-toggle="dropdown" aria-expanded="false">
          <div class="pl-2 d-flex align-items-center">
            {{ value }}
          </div>
          <div class="items-per-page-icon ecl-select__icon text-white">
            <i class="material-icons">keyboard_arrow_down</i>
          </div>
        </button>
        <div class="dropdown-menu ec-ds-dropdown-items" aria-labelledby="itemsPerPageDropdown">
          <button
            class="dropdown-item"
            @click="setPageLimit(item)"
            v-for="(item, index) in options"
            :key="index">{{ item }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ItemsPerPage",
  props: {
    value: Number,
    onChange: Function,
    options: {
      type: Array,
      default: () => [5, 10, 25, 50]
    }
  },
  methods: {
    setPageLimit(limit) {
      if (limit !== this.value) this.onChange(limit);
    }
  }
}
</script>

<style lang="scss" scoped>
.items-per-page {
  display: inline;
  float: right;
  color: #404040;

  .items-per-page-dropdown {
    width: 100px;
    height: 48px;
  }

  .items-per-page-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    top: 0;
    right: 0;
    height: 100%;
    width: 2.75rem;
    .material-icons {
      font-size: 30px;
    }
  }
  .dropdown-menu {
    min-width: 6rem;
    button {
      text-align: center;
    }
  }
}
</style>
