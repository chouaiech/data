<!-- ABOUT page -->
<template>
  <div class="about container">
    <div class="row">
      <div class="col">
        <slot name="about-content-slot">
        </slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'about',
};
</script>

<style lang="scss" scoped>
</style>
