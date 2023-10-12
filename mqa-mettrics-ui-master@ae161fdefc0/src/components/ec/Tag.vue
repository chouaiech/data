<template>
  <span class="ecl-tag ecl-tag--display">
    <slot></slot>
  </span>
</template>
<script>
export default {
  name: 'Tag'
}
</script>
<style scoped>
.ecl-tag {
  background: #EBEBEB!important;
  color: #000000;
}
.active {
  background: #707070 !important;
  color: #FFFFFF!important;
}
</style>
