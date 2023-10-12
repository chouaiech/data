<template>
  <div id="bg">
    <div class="dashboard">
      <h2 class="ecl-u-type-heading-2 ecl-u-mb-m ecl-u-mt-3xl">{{ $t("message.dashboard.dimensions") }}</h2>
      <div>
        <p class="ecl-u-type-paragraph-lead">
          <span v-html="$t('message.dashboard.intro.desc')"></span>
        </p>
      </div>
    </div>
    <!-- DIMENSIONS -->
    <dimensions dimensions-type="dimensions-details" />
    <!-- CATALOGUES / OVERVIEW -->
    <p style="position:relative;">
      <a style="position:absolute; top:-10px;" id="Overview"></a>
    </p>
    <div class="dimensions-intro">
      <h2 class="ecl-u-type-heading-2 ecl-u-mb-l"><a href="#Overview" class="toc-active" v-smooth-scroll="{ duration: 700, offset: -50}">{{ $t("message.dashboard.overview.headline") }}</a></h2>
    </div>
    <div class="p-0 m-0" id="overview-table" >
      <div class="row container mx-0 px-0">
       <div class="col mx-0 px-0">
         <dashboard-catalogue-table />
       </div>
      </div>
    </div>
  </div>
</template>

<script>
import DashboardCatalogueTable from './DashboardCatalogueTable.vue'
import Dimensions from './dimensions-cards/Dimensions.vue'

export default {
  name: 'Dashboard',
  components: {
    DashboardCatalogueTable,
    Dimensions
  },
  metaInfo () {
    return {
      title: `${this.$t('message.common.site_title')}`,
      meta: [{
        name: 'description',
        content: this.$t("message.meta.description")}
      ]
    }
  },
  data () {
    return {
      dashboard: true
    }
  },
  mounted () {},
  created () {},
  methods: {
  }
}
</script>

<style lang="scss" scoped>

#bg {
  background-color: #fff;
}

.ecl-u-type-paragraph-lead {
  max-width: 100%!important;
  font-size: 16px!important;
}
.toc-active {
  font-size: 28px;
}
</style>
