<template>
  <div
    class="mb-2">
    <span class="font-weight-bold">{{ title }}</span>
    <i
      v-if="tooltip"
      class="tooltip-icon material-icons small-icon align-right text-dark pl-1"
      data-toggle="tooltip"
      data-placement="right"
      :title="tooltip">
      help_outline
    </i>
  </div>
</template>

<script>
export default {
  name: "ECFacetHeader",
  props: {
    title: String,
    tooltip: String
  }
}
</script>

<style scoped>

.tooltip-icon {
  font-size: 15px;
}

span {
  font-family: Arial, sans-serif;
  font-size: medium;
  color: #2c2c2c;
}

div {
  margin-top: 4px;
  padding: 0;
  font-family: Arial, sans-serif;
}

</style>
