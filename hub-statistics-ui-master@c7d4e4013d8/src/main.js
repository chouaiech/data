import '@babel/polyfill'
import Vue from 'vue'
import RuntimeConfiguration from './runtimeconfig'
import App from './App'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueI18n from 'vue-i18n'
import _ from 'lodash'
import Sticky from 'vue-sticky-directive'
import i18njson from './i18n/lang.js'
import VueMeta from 'vue-meta'
import DeuHeaderFooter from '@deu/deu-header-footer'
import UniversalPiwik from '@piveau/piveau-universal-piwik'

// Import Font Awesome Icons Library for vue
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  faGoogle,
  faGooglePlus,
  faGooglePlusG,
  faFacebook,
  faFacebookF,
  faInstagram,
  faTwitter,
  faLinkedinIn
} from '@fortawesome/free-brands-svg-icons'
import {
  faComment,
  faExternalLinkAlt,
  faPlus,
  faMinus,
  faArrowDown,
  faArrowUp,
  faInfoCircle
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// Import components
const SubNav = () => import(/* webpackChunkName: "SubNav" */'./components/SubNav.vue')

Vue.component('sub-nav', SubNav)


import 'bootstrap';
import './styles/styles.scss'
import '@deu/deu-header-footer/dist/deu-header-footer.css'
import '@ecl/preset-ec/dist/styles/ecl-ec.css'

/**********************************************
 *  Integrating the EC component library here *
 **********************************************/
import '@ecl/preset-ec/dist/styles/ecl-ec.css'

Vue.prototype.$_ = _

import './assets/img/data-europa-logo.svg'

Vue.config.productionTip = false

library.add(faGoogle, faGooglePlus, faGooglePlusG, faFacebook, faFacebookF, faInstagram, faTwitter, faLinkedinIn, faComment, faExternalLinkAlt, faPlus, faMinus, faArrowDown, faArrowUp, faInfoCircle)
Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.use(VueI18n)
Vue.use(Sticky)
Vue.use(VueMeta)
// eslint-disable-next-line no-undef
Vue.use(RuntimeConfiguration, { baseConfig: process.env, debug: false })
Vue.use(DeuHeaderFooter)
Vue.use(UniversalPiwik, {
  router,
  isPiwikPro: Vue.prototype.$env.TRACKER_IS_PIWIK_PRO,
  trackerUrl: Vue.prototype.$env.TRACKER_TRACKER_URL,
  siteId: Vue.prototype.$env.TRACKER_SITE_ID,
  // eslint-disable-next-line no-undef
  debug: process.env.NODE_ENV === 'development'
})

const i18n = new VueI18n({
  locale: 'en', // set locale
  messages: i18njson, // set locale messages
  silentTranslationWarn: true
})

/* eslint-disable no-new */
new Vue({
  i18n,
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
