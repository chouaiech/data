<template>
<div>
  <cookie-consent :piwik-instance="$piwik"></cookie-consent>
  <div id="app">
    <deu-header
      project="statistics"
      active-menu-item="data"
      use-breadcrumbs
      use-breadcrumbs-route-meta
      :showSparql="showSparql"
    ></deu-header>
    <sub-nav></sub-nav>
    <router-view></router-view>
    <deu-footer
      @click-follow-link="handleFollowClick"
    ></deu-footer>
  </div>
</div>
</template>

<script>

/*import '@deu/deu-cookie-consent/dist/deu-cookie-consent.css'
import CookieConsent from '@deu/deu-cookie-consent'*/

export default {
  name: 'App',
  /*components: {
    CookieConsent
  },*/
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
    menuItemsHook (menuItems) {
      // Adjust header menu items for this project
      const dataMenuItems = menuItems[0].subMenuItems

      const euInternational = dataMenuItems[0]
      const countryData = dataMenuItems[1]
      const statistics = dataMenuItems[3]

      euInternational.to = `/data/eu-international-datasets/?locale=${this.$route.query.locale}/`
      countryData.to = `/data/datasets/?locale=${this.$route.query.locale}/`
      statistics.to = { name: 'CurrentState', query: { locale: this.$i18n.locale } }

      return menuItems
    },
    handleFollowClick (url) {
      this.$piwik.trackOutlink(url)
    }
  }
}

// JavaScript code to hide the unwanted menu item
document.addEventListener('DOMContentLoaded', function () {
	document.querySelector('#menu-item-list').childNodes[2].textContent = ""
	document.querySelector('#menu-item-list').childNodes[3].textContent = ""
	document.querySelector('#menu-item-list').childNodes[4].textContent = ""
	document.querySelector('#menu-item-list').childNodes[5].textContent = ""
	document.getElementsByClassName("deu-menu__dropdown-list-item")[1].hidden = true
	
	document.querySelector('.deu-headline').textContent	= "health.data.eu - The official portal for European Health data"
});

</script>

<style lang="scss">
@import '../node_modules/bootstrap/scss/bootstrap.scss';

* {
  box-sizing: border-box;

}

.site-wrapper {
  border: 1px solid #83b4c2;
  margin: auto;
  max-width: 984px;
  box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
}

#app {
  background-color: #fff;
}

.spacer {
  margin: 500px;
}

.btn-light {
    color: #212529;
    background-color: #f8f9fa;
    border-color: #f8f9fa;
    margin-top: 5px;
}

h1, h2 {
  font-weight: 700 !important;
}
</style>
