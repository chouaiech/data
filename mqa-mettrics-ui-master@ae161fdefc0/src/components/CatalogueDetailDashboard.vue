<template>
  <div class="dashboard">
    <!-- DIMENSIONS -->
    <dimensions class="p-0" dimensions-type="catalogue-dimensions-details"></dimensions>
  </div>
</template>

<script>
import Dimensions from './dimensions-cards/Dimensions.vue'

export default {
  name: 'CatalogueDetailDashboard',
  components: {
    Dimensions
  },
  metaInfo () {
    return {
      meta: [
        { name: 'title', vmid: 'title', content: `${this.title} - ${this.$t('message.common.site_title')}` }
      ]
    }
  },
  data () {
    return {}
  },
  props: {
    title: {
      type: String
    }
  },
  mounted () {},
  computed: {},
  methods: {}
}
</script>

<style scoped>
</style>
