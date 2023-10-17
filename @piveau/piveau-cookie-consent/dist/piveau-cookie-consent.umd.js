(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define([], factory);
	else if(typeof exports === 'object')
		exports["piveau-cookie-consent"] = factory();
	else
		root["piveau-cookie-consent"] = factory();
})((typeof self !== 'undefined' ? self : this), function() {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "fb15");
/******/ })
/************************************************************************/
/******/ ({

/***/ "0c10":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "8875":
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;// addapted from the document.currentScript polyfill by Adam Miller
// MIT license
// source: https://github.com/amiller-gh/currentScript-polyfill

// added support for Firefox https://bugzilla.mozilla.org/show_bug.cgi?id=1620505

(function (root, factory) {
  if (true) {
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__)) : __WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else {}
}(typeof self !== 'undefined' ? self : this, function () {
  function getCurrentScript () {
    var descriptor = Object.getOwnPropertyDescriptor(document, 'currentScript')
    // for chrome
    if (!descriptor && 'currentScript' in document && document.currentScript) {
      return document.currentScript
    }

    // for other browsers with native support for currentScript
    if (descriptor && descriptor.get !== getCurrentScript && document.currentScript) {
      return document.currentScript
    }
  
    // IE 8-10 support script readyState
    // IE 11+ & Firefox support stack trace
    try {
      throw new Error();
    }
    catch (err) {
      // Find the second match for the "at" string to get file src url from stack.
      var ieStackRegExp = /.*at [^(]*\((.*):(.+):(.+)\)$/ig,
        ffStackRegExp = /@([^@]*):(\d+):(\d+)\s*$/ig,
        stackDetails = ieStackRegExp.exec(err.stack) || ffStackRegExp.exec(err.stack),
        scriptLocation = (stackDetails && stackDetails[1]) || false,
        line = (stackDetails && stackDetails[2]) || false,
        currentLocation = document.location.href.replace(document.location.hash, ''),
        pageSource,
        inlineScriptSourceRegExp,
        inlineScriptSource,
        scripts = document.getElementsByTagName('script'); // Live NodeList collection
  
      if (scriptLocation === currentLocation) {
        pageSource = document.documentElement.outerHTML;
        inlineScriptSourceRegExp = new RegExp('(?:[^\\n]+?\\n){0,' + (line - 2) + '}[^<]*<script>([\\d\\D]*?)<\\/script>[\\d\\D]*', 'i');
        inlineScriptSource = pageSource.replace(inlineScriptSourceRegExp, '$1').trim();
      }
  
      for (var i = 0; i < scripts.length; i++) {
        // If ready state is interactive, return the script tag
        if (scripts[i].readyState === 'interactive') {
          return scripts[i];
        }
  
        // If src matches, return the script tag
        if (scripts[i].src === scriptLocation) {
          return scripts[i];
        }
  
        // If inline source matches, return the script tag
        if (
          scriptLocation === currentLocation &&
          scripts[i].innerHTML &&
          scripts[i].innerHTML.trim() === inlineScriptSource
        ) {
          return scripts[i];
        }
      }
  
      // If no match, return null
      return null;
    }
  };

  return getCurrentScript
}));


/***/ }),

/***/ "9c17":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_vue_cookie_accept_decline_vue_vue_type_style_index_0_id_44d7caae_scoped_true_lang_scss___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("0c10");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_vue_cookie_accept_decline_vue_vue_type_style_index_0_id_44d7caae_scoped_true_lang_scss___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_vue_cookie_accept_decline_vue_vue_type_style_index_0_id_44d7caae_scoped_true_lang_scss___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "fb15":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
// ESM COMPAT FLAG
__webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: ./node_modules/@vue/cli-service/lib/commands/build/setPublicPath.js
// This file is imported into lib/wc client bundles.

if (typeof window !== 'undefined') {
  var currentScript = window.document.currentScript
  if (true) {
    var getCurrentScript = __webpack_require__("8875")
    currentScript = getCurrentScript()

    // for backward compatibility, because previously we directly included the polyfill
    if (!('currentScript' in document)) {
      Object.defineProperty(document, 'currentScript', { get: getCurrentScript })
    }
  }

  var src = currentScript && currentScript.src.match(/(.+\/)[^/]+\.js(\?.*)?$/)
  if (src) {
    __webpack_require__.p = src[1] // eslint-disable-line
  }
}

// Indicate to webpack that this file can be concatenated
/* harmony default export */ var setPublicPath = (null);

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"c35d28e2-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/vue-cookie-accept-decline.vue?vue&type=template&id=44d7caae&scoped=true&
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('transition',{attrs:{"appear":"","name":_vm.transitionName}},[(_vm.isOpen)?_c('div',{staticClass:"cookie",class:['cookie__' + _vm.type, 'cookie__' + _vm.type + '--' + _vm.position],attrs:{"id":_vm.elementId}},[_c('div',{class:'cookie__' + _vm.type + '__wrap'},[(_vm.showPostponeButton === true)?_c('div',{class:'cookie__' + _vm.type + '__postpone-button',attrs:{"title":"Close"},on:{"click":_vm.postpone}},[_vm._t("postponeContent",function(){return [_vm._v(" × ")]})],2):_vm._e(),_c('div',{class:'cookie__' + _vm.type + '__content'},[_vm._t("message",function(){return [_vm._v(" We use cookies to ensure you get the best experience on our website. "),_c('a',{attrs:{"href":"https://cookiesandyou.com/","target":"_blank"}},[_vm._v("Learn More...")])]})],2),_c('div',{class:'cookie__' + _vm.type + '__buttons'},[_c('button',{class:['cookie__' + _vm.type + '__buttons__button', 'cookie__' + _vm.type + '__buttons__button--accept'],attrs:{"id":"cookie-btn-accept"},on:{"click":_vm.accept}},[_vm._t("acceptContent",function(){return [_vm._v(" Got It! ")]})],2),(_vm.disableDecline === false)?_c('button',{class:['cookie__' + _vm.type + '__buttons__button', 'cookie__' + _vm.type + '__buttons__button--decline'],attrs:{"id":"cookie-btn-decline"},on:{"click":_vm.decline}},[_vm._t("declineContent",function(){return [_vm._v(" Opt Out ")]})],2):_vm._e()])])]):_vm._e()])}
var staticRenderFns = []


// CONCATENATED MODULE: ./src/components/vue-cookie-accept-decline.vue?vue&type=template&id=44d7caae&scoped=true&

// CONCATENATED MODULE: ./node_modules/tiny-cookie/es/util.js
function hasOwn(obj, key) {
  return Object.prototype.hasOwnProperty.call(obj, key);
} // Escape special characters.


function escapeRe(str) {
  return str.replace(/[.*+?^$|[\](){}\\-]/g, '\\$&');
} // Return a future date by the given string.


function computeExpires(str) {
  var lastCh = str.charAt(str.length - 1);
  var value = parseInt(str, 10);
  var expires = new Date();

  switch (lastCh) {
    case 'Y':
      expires.setFullYear(expires.getFullYear() + value);
      break;

    case 'M':
      expires.setMonth(expires.getMonth() + value);
      break;

    case 'D':
      expires.setDate(expires.getDate() + value);
      break;

    case 'h':
      expires.setHours(expires.getHours() + value);
      break;

    case 'm':
      expires.setMinutes(expires.getMinutes() + value);
      break;

    case 's':
      expires.setSeconds(expires.getSeconds() + value);
      break;

    default:
      expires = new Date(str);
  }

  return expires;
} // Convert an object to a cookie option string.


function convert(opts) {
  var res = ''; // eslint-disable-next-line

  for (var key in opts) {
    if (hasOwn(opts, key)) {
      if (/^expires$/i.test(key)) {
        var expires = opts[key];

        if (typeof expires !== 'object') {
          expires += typeof expires === 'number' ? 'D' : '';
          expires = computeExpires(expires);
        }

        res += ";" + key + "=" + expires.toUTCString();
      } else if (/^secure$/.test(key)) {
        if (opts[key]) {
          res += ";" + key;
        }
      } else {
        res += ";" + key + "=" + opts[key];
      }
    }
  }

  if (!hasOwn(opts, 'path')) {
    res += ';path=/';
  }

  return res;
}


// CONCATENATED MODULE: ./node_modules/tiny-cookie/es/index.js
function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

 // Check if the browser cookie is enabled.

function isEnabled() {
  var key = '@key@';
  var value = '1';
  var re = new RegExp("(?:^|; )" + key + "=" + value + "(?:;|$)");
  document.cookie = key + "=" + value + ";path=/";
  var enabled = re.test(document.cookie);

  if (enabled) {
    // eslint-disable-next-line
    remove(key);
  }

  return enabled;
} // Get the cookie value by key.


function get(key, decoder) {
  if (decoder === void 0) {
    decoder = decodeURIComponent;
  }

  if (typeof key !== 'string' || !key) {
    return null;
  }

  var reKey = new RegExp("(?:^|; )" + escapeRe(key) + "(?:=([^;]*))?(?:;|$)");
  var match = reKey.exec(document.cookie);

  if (match === null) {
    return null;
  }

  return typeof decoder === 'function' ? decoder(match[1]) : match[1];
} // The all cookies


function getAll(decoder) {
  if (decoder === void 0) {
    decoder = decodeURIComponent;
  }

  var reKey = /(?:^|; )([^=]+?)(?:=([^;]*))?(?:;|$)/g;
  var cookies = {};
  var match;
  /* eslint-disable no-cond-assign */

  while (match = reKey.exec(document.cookie)) {
    reKey.lastIndex = match.index + match.length - 1;
    cookies[match[1]] = typeof decoder === 'function' ? decoder(match[2]) : match[2];
  }

  return cookies;
} // Set a cookie.


function set(key, value, encoder, options) {
  if (encoder === void 0) {
    encoder = encodeURIComponent;
  }

  if (typeof encoder === 'object' && encoder !== null) {
    /* eslint-disable no-param-reassign */
    options = encoder;
    encoder = encodeURIComponent;
    /* eslint-enable no-param-reassign */
  }

  var attrsStr = convert(options || {});
  var valueStr = typeof encoder === 'function' ? encoder(value) : value;
  var newCookie = key + "=" + valueStr + attrsStr;
  document.cookie = newCookie;
} // Remove a cookie by the specified key.


function remove(key, options) {
  var opts = {
    expires: -1
  };

  if (options) {
    opts = _extends({}, options, opts);
  }

  return set(key, 'a', opts);
} // Get the cookie's value without decoding.


function getRaw(key) {
  return get(key, null);
} // Set a cookie without encoding the value.


function setRaw(key, value, options) {
  return set(key, value, null, options);
}


// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/vue-cookie-accept-decline.vue?vue&type=script&lang=js&
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
 // eslint-disable-line

/* harmony default export */ var vue_cookie_accept_declinevue_type_script_lang_js_ = ({
  name: 'vue-cookie-accept-decline',
  props: {
    elementId: {
      type: String,
      required: true
    },
    debug: {
      type: Boolean,
      default: false
    },
    disableDecline: {
      type: Boolean,
      default: false
    },
    // floating: bottom-left, bottom-right, top-left, top-right
    // bar:  bottom, top
    position: {
      type: String,
      default: 'bottom-left'
    },
    // floating, bar
    type: {
      type: String,
      default: 'floating'
    },
    // slideFromBottom, slideFromTop, fade
    transitionName: {
      type: String,
      default: 'slideFromBottom'
    },
    showPostponeButton: {
      type: Boolean,
      default: false
    },
    cookie: {
      type: String,
      default: 'piveau-cookie-accept-decline-state'
    },
    cookieExpires: {
      type: String,
      default: '100D'
    },
    noCookiesUntilConsent: {
      type: Boolean,
      default: false
    },
    cookieConfig: {
      type: Object,
      default: function _default() {
        return {
          acceptValue: '2',
          declineValue: '0',
          postponeValue: '1'
        };
      }
    }
  },
  data: function data() {
    return {
      status: null,
      supportsLocalStorage: false,
      isOpen: false,
      cookiesAllowed: false
    };
  },
  computed: {
    containerPosition: function containerPosition() {
      return "cookie--".concat(this.position);
    },
    containerType: function containerType() {
      return "cookie--".concat(this.type);
    }
  },
  mounted: function mounted() {
    this.checkLocalStorageFunctionality();
    this.init();
  },
  methods: {
    init: function init() {
      if (!this.noCookiesUntilConsent) {
        this.cookiesAllowed = true;
      }

      var visitedType = this.getCookieStatus();

      if (visitedType && (visitedType === 'accept' || visitedType === 'decline' || visitedType === 'postpone')) {
        this.isOpen = false;
      }

      if (!visitedType) {
        this.isOpen = true;
      }

      this.status = visitedType;
      this.$emit('status', visitedType);
    },
    checkLocalStorageFunctionality: function checkLocalStorageFunctionality() {
      // Check for availability of localStorage
      //   try {
      //     const test = '__vue-cookie-accept-decline-check-localStorage';
      //     window.localStorage.setItem(test, test);
      //     window.localStorage.removeItem(test);
      //   } catch (e) {
      //     console.error('Local storage is not supported, falling back to cookie use');
      //     this.supportsLocalStorage = false;
      //   }
      this.supportsLocalStorage = false;
    },
    setCookieStatus: function setCookieStatus(type) {
      if (this.supportsLocalStorage) {
        if (type === 'accept') {
          localStorage.setItem("".concat(this.cookie), this.cookieConfig.acceptValue);
        }

        if (type === 'decline') {
          localStorage.setItem("".concat(this.cookie), this.cookieConfig.declineValue);
        }

        if (type === 'postpone') {
          localStorage.setItem("".concat(this.cookie), this.cookieConfig.postponeValue);
        }
      } else {
        if (type === 'accept') {
          this.setCookie("".concat(this.cookie), this.cookieConfig.acceptValue);
        }

        if (type === 'decline') {
          this.setCookie("".concat(this.cookie), this.cookieConfig.declineValue);
        }

        if (type === 'postpone') {
          this.setCookie("".concat(this.cookie), this.cookieConfig.postponeValue);
        }
      }
    },
    getCookieStatus: function getCookieStatus() {
      if (this.supportsLocalStorage) {
        return localStorage.getItem("".concat(this.cookie));
      }

      var cookieStatus = get("".concat(this.cookie));
      var _this$cookieConfig = this.cookieConfig,
          acceptValue = _this$cookieConfig.acceptValue,
          declineValue = _this$cookieConfig.declineValue,
          postponeValue = _this$cookieConfig.postponeValue;

      switch (cookieStatus) {
        case acceptValue:
          return 'accept';

        case declineValue:
          return 'decline';

        case postponeValue:
          return 'postpone';

        default:
          return cookieStatus || null;
      }
    },
    accept: function accept() {
      if (!this.debug) {
        this.cookiesAllowed = true;
        this.setCookieStatus('accept');
      }

      this.status = 'accept';
      this.isOpen = false;
      this.$emit('clicked-accept');
    },
    decline: function decline() {
      if (!this.debug) {
        this.setCookieStatus('decline');
      }

      this.status = 'decline';
      this.isOpen = false;
      this.$emit('clicked-decline');
    },
    postpone: function postpone() {
      if (!this.debug) {
        this.setCookieStatus('postpone');
      }

      this.status = 'postpone';
      this.isOpen = false;
      this.$emit('clicked-postpone');
    },
    removeCookie: function removeCookie() {
      localStorage.removeItem("".concat(this.cookie));
      remove(this.cookie);
      this.status = null;
      this.$emit('removed-cookie');
    },
    setCookie: function setCookie(key, value) {
      if (this.cookiesAllowed) {
        set("".concat(key), "".concat(value), {
          expires: this.cookieExpires,
          secure: true
        });
      }
    }
  }
});
// CONCATENATED MODULE: ./src/components/vue-cookie-accept-decline.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_vue_cookie_accept_declinevue_type_script_lang_js_ = (vue_cookie_accept_declinevue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/components/vue-cookie-accept-decline.vue?vue&type=style&index=0&id=44d7caae&scoped=true&lang=scss&
var vue_cookie_accept_declinevue_type_style_index_0_id_44d7caae_scoped_true_lang_scss_ = __webpack_require__("9c17");

// CONCATENATED MODULE: ./node_modules/vue-loader/lib/runtime/componentNormalizer.js
/* globals __VUE_SSR_CONTEXT__ */

// IMPORTANT: Do NOT use ES2015 features in this file (except for modules).
// This module is a runtime utility for cleaner component module output and will
// be included in the final webpack user bundle.

function normalizeComponent (
  scriptExports,
  render,
  staticRenderFns,
  functionalTemplate,
  injectStyles,
  scopeId,
  moduleIdentifier, /* server only */
  shadowMode /* vue-cli only */
) {
  // Vue.extend constructor export interop
  var options = typeof scriptExports === 'function'
    ? scriptExports.options
    : scriptExports

  // render functions
  if (render) {
    options.render = render
    options.staticRenderFns = staticRenderFns
    options._compiled = true
  }

  // functional template
  if (functionalTemplate) {
    options.functional = true
  }

  // scopedId
  if (scopeId) {
    options._scopeId = 'data-v-' + scopeId
  }

  var hook
  if (moduleIdentifier) { // server build
    hook = function (context) {
      // 2.3 injection
      context =
        context || // cached call
        (this.$vnode && this.$vnode.ssrContext) || // stateful
        (this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext) // functional
      // 2.2 with runInNewContext: true
      if (!context && typeof __VUE_SSR_CONTEXT__ !== 'undefined') {
        context = __VUE_SSR_CONTEXT__
      }
      // inject component styles
      if (injectStyles) {
        injectStyles.call(this, context)
      }
      // register component module identifier for async chunk inferrence
      if (context && context._registeredComponents) {
        context._registeredComponents.add(moduleIdentifier)
      }
    }
    // used by ssr in case component is cached and beforeCreate
    // never gets called
    options._ssrRegister = hook
  } else if (injectStyles) {
    hook = shadowMode
      ? function () {
        injectStyles.call(
          this,
          (options.functional ? this.parent : this).$root.$options.shadowRoot
        )
      }
      : injectStyles
  }

  if (hook) {
    if (options.functional) {
      // for template-only hot-reload because in that case the render fn doesn't
      // go through the normalizer
      options._injectStyles = hook
      // register for functional component in vue file
      var originalRender = options.render
      options.render = function renderWithStyleInjection (h, context) {
        hook.call(context)
        return originalRender(h, context)
      }
    } else {
      // inject component registration as beforeCreate hook
      var existing = options.beforeCreate
      options.beforeCreate = existing
        ? [].concat(existing, hook)
        : [hook]
    }
  }

  return {
    exports: scriptExports,
    options: options
  }
}

// CONCATENATED MODULE: ./src/components/vue-cookie-accept-decline.vue






/* normalize component */

var component = normalizeComponent(
  components_vue_cookie_accept_declinevue_type_script_lang_js_,
  render,
  staticRenderFns,
  false,
  null,
  "44d7caae",
  null
  
)

/* harmony default export */ var vue_cookie_accept_decline = (component.exports);
// CONCATENATED MODULE: ./src/index.js

/* harmony default export */ var src_0 = (vue_cookie_accept_decline);
// CONCATENATED MODULE: ./node_modules/@vue/cli-service/lib/commands/build/entry-lib.js


/* harmony default export */ var entry_lib = __webpack_exports__["default"] = (src_0);



/***/ })

/******/ });
});
//# sourceMappingURL=piveau-cookie-consent.umd.js.map