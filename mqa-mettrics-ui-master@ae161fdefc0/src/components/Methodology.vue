<template>
  <div class="about-metrics">
    <div class="container overview-intro" >
      <div class="row">
        <div class="col-sm px-0">
          <div class="catalogue_intro-section-text">
            <h2 class="ecl-u-mb-xl ecl-u-type-heading-2 ecl-u-mt-3xl">{{ $t("message.methodology.headline") }}</h2>
            <!-- <span class="doc-subtitle">{{ $t("message.methodology.sub_headline") }}</span> -->
          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-sm px-0">
          <div class="intro-doc doc-text px-0">
            <p v-html="$t('message.methodology.intro_text')" class="ecl-u-type-paragraph "></p>
          </div>
        </div>
      </div>
    </div>
    <div class="doc-space"></div>
    <p style="position:relative;">
      <a style="position:absolute; top:+30px;" id="scope"></a>
    </p>
    <inpage-navigation />
  </div>
</template>

<script>
import VueEvents from 'vue-events'
import Vue from 'vue'
import Sticky from 'vue-sticky-directive'
import appLink from './AppLink'
import AnchorRouterLink from 'vue-anchor-router-link'
import InpageNavigation from '../components/ec/InpageNav.vue'

Vue.use(VueEvents)

export default {
  name: 'Methodology',
  components: {
    appLink,
    AnchorRouterLink,
    InpageNavigation
  },
  metaInfo () {
    return {
      title: `${this.$t('message.navigation.methodology')} - ${this.$t('message.common.site_title')}`
    }
  },
  // data () {
  //   return {
  //     rootURL: this.$env.ROOT_URL,
  //     shaclValidatorUrl: this.$env.SHACL_VALIDATOR_URL,
  //     shaclApiUrl: this.$env.SHACL_API_URL
  //   }
  // },
  directives: {
    Sticky
  },
  mounted () {},
  methods: {},
  computed: {}
}
</script>

<style scoped>
.ecl-u-type-paragraph {
  min-width: 100%!important;
}
</style>
