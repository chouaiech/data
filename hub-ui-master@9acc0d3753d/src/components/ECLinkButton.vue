<template>
  <router-link :to="path" :exact="exact">
    <e-c-button
      :label="title"
      :tooltip="tooltip"
      :primary="active"
    />
  </router-link>
</template>

<script>
import ECButton from "@/components/ECButton";

export default {
  name: "ECLinkButton",
  components: {
    ECButton
  },
  props: {
    title: String,
    tooltip: String,
    path: String,
    exact: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    active() {
      return this.$route.path === this.path;
    }
  }
}
</script>

<style scoped>

</style>
