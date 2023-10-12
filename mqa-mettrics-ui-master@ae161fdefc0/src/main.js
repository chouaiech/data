import '@babel/polyfill'
import 'es6-promise/auto'
import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VModal from 'vue-js-modal'

import {VueMasonryPlugin} from 'vue-masonry'
import vueSmoothScroll from 'vue2-smooth-scroll'
import VueI18n from 'vue-i18n'
import i18njson from './i18n/lang.js'
import VueProgress from 'vue-progress'
import BackToTop from 'vue-backtotop'
import _ from 'lodash'
import VueMeta from 'vue-meta'
import DeuHeaderFooter from '@deu/deu-header-footer'
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
import UniversalPiwik from '@piveau/piveau-universal-piwik'
import VTooltip from 'v-tooltip'
import RuntimeConfiguration from './utils/runtimeconfig'

Vue.prototype.$_ = _

import 'es6-promise/auto';

import './assets/img/deulogo.png';
import '@deu/deu-header-footer/dist/deu-header-footer.css';
import '@ecl/preset-ec/dist/styles/ecl-ec.css';

/**********************************************
 *  Integrating the EC component library here *
 **********************************************/
import '@ecl/preset-ec/dist/styles/ecl-ec.css'

library.add(faGoogle, faGooglePlus, faGooglePlusG, faFacebook, faFacebookF, faInstagram, faTwitter, faLinkedinIn, faComment, faExternalLinkAlt, faPlus, faMinus, faArrowDown, faArrowUp, faInfoCircle);
Vue.component('font-awesome-icon', FontAwesomeIcon);

Vue.use(vueSmoothScroll);
Vue.use(VueI18n);
Vue.config.productionTip = false;
Vue.use(VueProgress);
Vue.use(BackToTop);
Vue.use(VTooltip);
Vue.use(VueMasonryPlugin);
Vue.use(VModal, { dynamic: true, injectModalsContainer: true })
Vue.use(RuntimeConfiguration, { debug: true });
Vue.use(VueMeta)
Vue.use(DeuHeaderFooter);
Vue.use(UniversalPiwik, {
  router,
  isPiwikPro: Vue.prototype.$env.TRACKER_IS_PIWIK_PRO,
  trackerUrl: Vue.prototype.$env.TRACKER_TRACKER_URL,
  siteId: Vue.prototype.$env.TRACKER_SITE_ID,
  // eslint-disable-next-line no-undef
  debug: process.env.NODE_ENV === 'development',
});

// const LOCALE = Vue.prototype.$env.languages.locale;
// const FALLBACKLOCALE = Vue.prototype.$env.languages.fallbackLocale;

const i18n = new VueI18n({
  locale: 'en',
  messages: i18njson,
  silentTranslationWarn: true,
});

/* eslint-disable no-new */
new Vue({
  i18n,
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
});
