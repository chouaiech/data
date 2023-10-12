<template>
  <div id="app">
    <noscript>
    <iframe :src="'//opanalytics.containers.piwik.pro/' + this.piwikId + '/noscript.html'" height="0" width="0"
    style="display:none;visibility:hidden"></iframe>
    </noscript>
    <cookie-consent :piwik-instance="$piwik" />
    <back-to-top bottom="20px" right="20px" visibleoffsetbottom="200" >
      <button type="button" class="btn btn-info btn-to-top"><i class="fa fa-angle-up" aria-hidden="true"></i></button>
    </back-to-top>
    <deu-header
      project="metrics"
      active-menu-item="data"
      enable-authentication
      use-breadcrumbs
      use-breadcrumbs-route-meta
      @login="login"
      @logout="logout"
      :showSparql="showSparql"
    />
    <div class="bg container" >
<!--      <breadcrumb />-->
      <h1 class="ecl-u-type-heading-1 ecl-page-header__title ecl-u-mv-xl">Metadata quality</h1>
      <subnavigation :key= "$route.params.id"></subnavigation>
      <div class="main-content">
        <router-view></router-view>
      </div>
    </div>
    <deu-footer
      @click-follow-link="handleFollowClick"
    />
  </div>
</template>

<script>
import '@deu/deu-cookie-consent/dist/deu-cookie-consent.css'
import CookieConsent from '@deu/deu-cookie-consent'
import SubNavigation from './components/SubNavigation'
// import Breadcrumb from './components/ec/Breadcrumb'

export default {
  name: 'app',
  components: {
    CookieConsent,
    subnavigation: SubNavigation
    // breadcrumb: Breadcrumb
  },
  data () {
    return {
      tracker: null,
      matomoURL: this.$env.MATOMO_URL,
      lastRoute: null,
      piwikId: this.$env.PIWIK_ID,
      showSparql: this.$env.SHOW_SPARQL
    }
  },
  metaInfo () {
    return {
      htmlAttrs: {
        lang: this.$route.query.locale
      }
    }
  },
  methods: {
    handleFollowClick (url) {
      this.$piwik.trackOutlink(url)
    },
    login() {
      window.location.href = "https://data.europa.eu/euodp/data/user/login";
    },
    logout() {

    }
  }
}
</script>

<style lang="scss">
@import './styles/metrics-style.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';

//@font-face {
//  font-family: "Ubuntu";
//  src: local("Ubuntu"), url(../static/fonts/Ubuntu/Ubuntu-Regular.ttf) format("truetype");
//}

* {
  box-sizing: border-box;
}

.site-wrapper {
  border: 1px solid #83b4c2;
  margin: auto;
  max-width: 984px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
}

.spacer {
  margin: 500px;
}

#app {
  background-color: #fff;
}

.bg {
  background-color: #fff;
}

</style>
