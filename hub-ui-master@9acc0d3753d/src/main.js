/* eslint-disable no-new */
// Import IE Promise polyfill
import '@babel/polyfill';
import 'es6-promise/auto';
import $ from 'jquery';
import { sync } from 'vuex-router-sync';
import VueProgressBar from 'vue-progressbar';
import VueI18n from 'vue-i18n';
import VueFormulate from '@braid/vue-formulate';
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import Meta from 'vue-meta';
import injector from 'vue-inject';
import VeeValidate from 'vee-validate';
import DeuHeaderFooter from '@deu/deu-header-footer';
import UniversalPiwik from '@piveau/piveau-universal-piwik';
import 'vue-multiselect/dist/vue-multiselect.min.css';

// import AppToast from '@/components/AppToast';
// Import v-select
// Import i18n validation messages for vueformulate
// import {
// ca, cs, da, nl, de, en, fr, hu, it, lt, nb, pl, pt, ru, sr, sk, es, tr, sv,
// } from '@braid/vue-formulate-i18n';

// Import Font Awesome Icons Library for vue
import { library } from '@fortawesome/fontawesome-svg-core';
import {
  faGoogle,
  faGooglePlus,
  faGooglePlusG,
  faFacebook,
  faFacebookF,
  faInstagram,
  faTwitter,
  faLinkedinIn,
} from '@fortawesome/free-brands-svg-icons';
import {
  faComment,
  faExternalLinkAlt,
  faPlus,
  faMinus,
  faArrowDown,
  faArrowUp,
  faInfoCircle,
  faExclamationTriangle,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import VuePositionSticky from 'vue-position-sticky';
// import VueKeycloak from './plugins/vue-keycloak-js';
// Import main user configurations (glueConfig) and i18n configurations
import { glueConfig as GLUE_CONFIG, i18n as I18N_CONFIG } from '../config/user-config';
import runtimeConfig from '../config/runtime-config';
import router from './router';
// import store from './store/index';
import App from './App';
import {
  dateFilters,
  AppSnackbar,
  AppConfirmationDialog,
  vueKeycloak,
  bulkDownloadCorsProxyService ,
  corsProxyService,
  runtimeConfigurationService,
  store,
  configureModules,
  InfoSlot,
  ConditionalInput,
  AutocompleteInput,
  UniqueIdentifierInput,
  FileUpload,
  DatePicker,
  DateTimePicker,
} from '@piveau/piveau-hub-ui-modules';
import ECSelectFacet from "@/components/ECSelectFacet";
import ECRadioFacet from "@/components/ECRadioFacet";
import ECMore from "@/components/ECMore";
import ECButton from "@/components/ECButton";
import ECInfo from "./components/ECInfo.vue";
import ECLinkButton from "./components/ECLinkButton.vue";
import ECDataInfoBox from "./components/ECDataInfoBox.vue";
import ECDistributionDetails from "@/components/datasetDetails/ECDistributionDetails";
import ECDistributionsHeader from "@/components/datasetDetails/ECDistributionsHeader";
import SimpleFormAccessRequestDistribution from "@/components/datasetDetails/SimpleFormAccessRequestDistribution";
import ECSubNavigation from "@/components/ECSubNavigation.vue";

Vue.config.devtools = true;

Vue.use(runtimeConfigurationService, runtimeConfig, { baseConfig: GLUE_CONFIG, debug: false });
const env = Vue.prototype.$env;

configureModules({
  components: {
    SelectFacet: ECSelectFacet,
    RadioFacet: ECRadioFacet,
    PvShowMore: ECMore,
    PvButton: ECButton,
    PvBanner: ECInfo,
    PvDataInfoBox: ECDataInfoBox,
    DatasetDetailsNavigationPage: ECLinkButton,
    DistributionDetails: ECDistributionDetails,
    DistributionsHeader: ECDistributionsHeader,
    SubNavigation: ECSubNavigation,
    SimpleFormAccessRequestDistribution: SimpleFormAccessRequestDistribution
  },
  services: GLUE_CONFIG.services,
  serviceParams: {
    baseUrl: env.api.baseUrl,
    qualityBaseUrl: env.api.qualityBaseUrl,
    similarityBaseUrl: env.api.similarityBaseUrl,
    gazetteerBaseUrl: env.api.gazetteerBaseUrl,
    hubUrl: env.api.hubUrl,
    keycloak: env.keycloak,
    rtp: env.rtp,
    useAuthService: env.useAuthService,
    authToken: env.api.authToken,
    defaultScoringFacets: env.datasets.facets.scoringFacets.defaultScoringFacets,
  }
});

Vue.component('InfoSlot', InfoSlot);
Vue.component('ConditionalInput', ConditionalInput);
Vue.component('AutocompleteInput', AutocompleteInput);
Vue.component('UniqueIdentifierInput', UniqueIdentifierInput);
Vue.component('FileUpload', FileUpload);
Vue.component('DatePicker', DatePicker);
Vue.component('DateTimePicker', DateTimePicker);

// Vue.component('AppToast', AppToast);
Vue.component('AppSnackbar', AppSnackbar);
Vue.component('AppConfirmationDialog', AppConfirmationDialog);

const VueCookie = require('vue-cookie');

Vue.use(VueCookie);

Vue.use(VueFormulate, {
  // plugins: [ca, cs, da, nl, de, en, fr, hu, it, lt, nb, pl, pt, ru, sr, sk, es, tr, sv],
  validationNameStrategy: vm => vm.context.label,
  // Define our custom slot component(s)
  slotComponents: {
    label: 'InfoSlot',
  },
  // Define any props we want to pass to our slot component
  slotProps: {
    label: ['info', 'collapsed'],
  },
  components: {
    ConditionalInput,
  },
  library: {
    fileupload: {
      classification: 'text',
      component: 'FileUpload',
    },
    'conditional-input': {
      classification: 'text',
      component: 'ConditionalInput',
      slotProps: {
        component: ['data'],
      },
    },
    'autocomplete-input': {
      classification: 'text',
      component: 'AutocompleteInput',
      slotProps: {
        component: ['voc', 'multiple'],
      },
    },
    'unique-identifier-input': {
      classification: 'text',
      component: 'UniqueIdentifierInput',
    },
    'date-picker': {
      classification: 'date',
      component: 'DatePicker',
    },
    'datetime-picker': {
      classification: 'datetime-local',
      component: 'DateTimePicker',
    }
  },
});

Vue.use(corsProxyService, env.api.vueAppCorsproxyApiUrl);
Vue.use(bulkDownloadCorsProxyService, GLUE_CONFIG, env.api.vueAppCorsproxyApiUrl);

const { isPiwikPro, siteId, trackerUrl } = env.tracker;
Vue.use(UniversalPiwik, {
  router,
  isPiwikPro,
  trackerUrl,
  siteId,
  debug: process.env.NODE_ENV === 'development',
  useSuspendFeature: false,
  pageViewOptions: {
    // Set this to true as long as navigating to the /datasets/ route
    // adds a 'minScore' query to prevent duplicated tracking
    useDatasetsMinScoreFix: false,
    // Send empty dataset metadata for every page view
    // See https://gitlab.fokus.fraunhofer.de/piveau/organisation/piveau-scrum-board/-/issues/2098
    beforeTrackPageView: (to, from, tracker) => {
      if (to.name !== 'DatasetDetailsDataset') {
        tracker.trackDatasetDetailsPageView(null, null, {
          dataset_AccessRights: '',
          dataset_AccrualPeriodicity: '',
          dataset_Catalog: '',
          dataset_ID: '',
          dataset_Publisher: '',
          dataset_Title: '',
        });
      }
    },
  },
});


// Configured language
const LOCALE = env.languages.locale;
const FALLBACKLOCALE = env.languages.fallbackLocale;

Vue.use(VueI18n);
// eslint-disable-next-line
export const i18n = new VueI18n({
  locale: LOCALE,
  fallbackLocale: FALLBACKLOCALE,
  messages: I18N_CONFIG,
  silentTranslationWarn: true,
});
// Make i18n globally available
Vue.i18n = i18n;

// Make i18n globally available
Vue.i18n = i18n;

// Set locale for dateFilters
dateFilters.setLocale(LOCALE);

// Vue-meta setup
Vue.use(Meta, {
  refreshOnceOnNavigation: true,
  debounceWait: 100,
});

// Bootstrap requirements to use js-features of bs-components
require('popper.js');

require('bootstrap');

require('./styles/styles.scss');
require('./styles/ec-style.scss');

$(() => {
  $('[data-toggle="popover"]').popover({ container: 'body' });
  $('[data-toggle="tooltip"]').tooltip({ container: 'body' });
});

require('@fortawesome/fontawesome-free/css/all.css');

require('@deu/deu-header-footer/dist/deu-header-footer.css');

// OpenStreetMaps popup styles
require('leaflet/dist/leaflet.css');

require('@piveau/dcatap-frontend/dist/dcatap-frontend.css');

// Vue-progressbar setup
const progressBarOptions = {
  thickness: '5px',
  autoRevert: false,
  transition: {
    speed: '1.0s',
    opacity: '0.5s',
    termination: 1000,
  },
};
Vue.use(VueProgressBar, progressBarOptions);

// Vee-validate (form validation) setup
Vue.use(VeeValidate, { errorBagName: 'vee_validator_errors' });

// Vue-inject setup
Vue.use(injector, { components: true });

Vue.use(DeuHeaderFooter);

Vue.use(VuePositionSticky);

// Sync store and router
sync(store, router);

// Add Font Awesome Icons
library.add(faGoogle, faGooglePlus, faGooglePlusG, faFacebook, faFacebookF, faInstagram, faTwitter, faLinkedinIn, faComment, faExternalLinkAlt, faPlus, faMinus, faArrowDown, faArrowUp, faInfoCircle, faExclamationTriangle);
Vue.component('font-awesome-icon', FontAwesomeIcon);

Vue.config.productionTip = false;

// Creates the root Vue instance
const createVueApp = () => {
  const app = new Vue({
    router,
    store,
    i18n,
    render: h => h(App),
  });

  return app;
};

// Promise that timeouts after x seconds
let waitTimeoutHandle;
const wait = ms => new Promise((resolve, reject) => waitTimeoutHandle = setTimeout(() => {
  reject(new Error(`Keycloak failed to load after a timeout of ${ms} ms`));
}, ms));

const useVueWithKeycloakPromise = new Promise((resolve, reject) => {
  Vue.use(vueKeycloak, {
    config: {
      rtp: env.rtp,
      ...env.keycloak,
    },
    init: {
      ...window.Cypress && { checkLoginIframe: !window.Cypress },
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: `${window.location.origin}${process.env.buildconf.BASE_PATH}static/silent-check-sso.html`,
    },
    onReady: () => {
      resolve();
      if (waitTimeoutHandle) clearTimeout(waitTimeoutHandle);
    },    onInitError: reject,
  });
});

// Race promises
// Timeouts after ms seconds for error handling
// This is a workaround to keycloak-js adapter not passing exceptions properly in silent sso mode
// This issue is fixed via keycloak-js@15
// See https://github.com/keycloak/keycloak/pull/8161
// todo: refactor this when keycloak 15.x or higher backend is installed
const useVueWithKeycloakWithTimeout = ms => Promise.race([
  useVueWithKeycloakPromise,
  wait(ms),
]);

// Attempt to load Vue with Keycloak using recover mechanism
(async () => {
  if (!env.useAuthService) {
    createVueApp().$mount('#app');
    return {};
  }

  try {
    // Load Keycloak
    await useVueWithKeycloakWithTimeout(window.Cypress ? 200 : 7000);
  } catch (ex) {
    // eslint-disable-next-line no-console
    console.error(ex);
  } finally {
    // Initialize Vue, even if Keycloak failed to load
    createVueApp().$mount('#app');
  }

  return {};
})();
