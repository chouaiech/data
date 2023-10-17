(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
  typeof define === 'function' && define.amd ? define(factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.UniversalPiwik = factory());
})(this, (function () { 'use strict';

  function ownKeys(object, enumerableOnly) {
    var keys = Object.keys(object);

    if (Object.getOwnPropertySymbols) {
      var symbols = Object.getOwnPropertySymbols(object);

      if (enumerableOnly) {
        symbols = symbols.filter(function (sym) {
          return Object.getOwnPropertyDescriptor(object, sym).enumerable;
        });
      }

      keys.push.apply(keys, symbols);
    }

    return keys;
  }

  function _objectSpread2(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i] != null ? arguments[i] : {};

      if (i % 2) {
        ownKeys(Object(source), true).forEach(function (key) {
          _defineProperty(target, key, source[key]);
        });
      } else if (Object.getOwnPropertyDescriptors) {
        Object.defineProperties(target, Object.getOwnPropertyDescriptors(source));
      } else {
        ownKeys(Object(source)).forEach(function (key) {
          Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key));
        });
      }
    }

    return target;
  }

  function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
      throw new TypeError("Cannot call a class as a function");
    }
  }

  function _defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  function _createClass(Constructor, protoProps, staticProps) {
    if (protoProps) _defineProperties(Constructor.prototype, protoProps);
    if (staticProps) _defineProperties(Constructor, staticProps);
    return Constructor;
  }

  function _defineProperty(obj, key, value) {
    if (key in obj) {
      Object.defineProperty(obj, key, {
        value: value,
        enumerable: true,
        configurable: true,
        writable: true
      });
    } else {
      obj[key] = value;
    }

    return obj;
  }

  function _inherits(subClass, superClass) {
    if (typeof superClass !== "function" && superClass !== null) {
      throw new TypeError("Super expression must either be null or a function");
    }

    subClass.prototype = Object.create(superClass && superClass.prototype, {
      constructor: {
        value: subClass,
        writable: true,
        configurable: true
      }
    });
    if (superClass) _setPrototypeOf(subClass, superClass);
  }

  function _getPrototypeOf(o) {
    _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) {
      return o.__proto__ || Object.getPrototypeOf(o);
    };
    return _getPrototypeOf(o);
  }

  function _setPrototypeOf(o, p) {
    _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) {
      o.__proto__ = p;
      return o;
    };

    return _setPrototypeOf(o, p);
  }

  function _isNativeReflectConstruct() {
    if (typeof Reflect === "undefined" || !Reflect.construct) return false;
    if (Reflect.construct.sham) return false;
    if (typeof Proxy === "function") return true;

    try {
      Boolean.prototype.valueOf.call(Reflect.construct(Boolean, [], function () {}));
      return true;
    } catch (e) {
      return false;
    }
  }

  function _construct(Parent, args, Class) {
    if (_isNativeReflectConstruct()) {
      _construct = Reflect.construct;
    } else {
      _construct = function _construct(Parent, args, Class) {
        var a = [null];
        a.push.apply(a, args);
        var Constructor = Function.bind.apply(Parent, a);
        var instance = new Constructor();
        if (Class) _setPrototypeOf(instance, Class.prototype);
        return instance;
      };
    }

    return _construct.apply(null, arguments);
  }

  function _assertThisInitialized(self) {
    if (self === void 0) {
      throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
    }

    return self;
  }

  function _possibleConstructorReturn(self, call) {
    if (call && (typeof call === "object" || typeof call === "function")) {
      return call;
    }

    return _assertThisInitialized(self);
  }

  function _createSuper(Derived) {
    var hasNativeReflectConstruct = _isNativeReflectConstruct();

    return function _createSuperInternal() {
      var Super = _getPrototypeOf(Derived),
          result;

      if (hasNativeReflectConstruct) {
        var NewTarget = _getPrototypeOf(this).constructor;

        result = Reflect.construct(Super, arguments, NewTarget);
      } else {
        result = Super.apply(this, arguments);
      }

      return _possibleConstructorReturn(this, result);
    };
  }

  function _superPropBase(object, property) {
    while (!Object.prototype.hasOwnProperty.call(object, property)) {
      object = _getPrototypeOf(object);
      if (object === null) break;
    }

    return object;
  }

  function _get(target, property, receiver) {
    if (typeof Reflect !== "undefined" && Reflect.get) {
      _get = Reflect.get;
    } else {
      _get = function _get(target, property, receiver) {
        var base = _superPropBase(target, property);

        if (!base) return;
        var desc = Object.getOwnPropertyDescriptor(base, property);

        if (desc.get) {
          return desc.get.call(receiver);
        }

        return desc.value;
      };
    }

    return _get(target, property, receiver || target);
  }

  function _toConsumableArray(arr) {
    return _arrayWithoutHoles(arr) || _iterableToArray(arr) || _unsupportedIterableToArray(arr) || _nonIterableSpread();
  }

  function _arrayWithoutHoles(arr) {
    if (Array.isArray(arr)) return _arrayLikeToArray(arr);
  }

  function _iterableToArray(iter) {
    if (typeof Symbol !== "undefined" && iter[Symbol.iterator] != null || iter["@@iterator"] != null) return Array.from(iter);
  }

  function _unsupportedIterableToArray(o, minLen) {
    if (!o) return;
    if (typeof o === "string") return _arrayLikeToArray(o, minLen);
    var n = Object.prototype.toString.call(o).slice(8, -1);
    if (n === "Object" && o.constructor) n = o.constructor.name;
    if (n === "Map" || n === "Set") return Array.from(o);
    if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen);
  }

  function _arrayLikeToArray(arr, len) {
    if (len == null || len > arr.length) len = arr.length;

    for (var i = 0, arr2 = new Array(len); i < len; i++) arr2[i] = arr[i];

    return arr2;
  }

  function _nonIterableSpread() {
    throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
  }

  /* eslint-disable class-methods-use-this */

  /* eslint-disable no-underscore-dangle */
  var MatomoTracker = /*#__PURE__*/function () {
    function MatomoTracker(trackerUrl, siteId, options) {
      _classCallCheck(this, MatomoTracker);

      window._paq = window._paq || [];
      this.type = 'matomo';
      this.trackerUrl = trackerUrl.slice(-1) === '/' ? trackerUrl : "".concat(trackerUrl, "/");
      this.siteId = siteId;
      this.stopped = false;
      this.options = _objectSpread2({
        debug: false,
        disabled: false
      }, options);

      if (this.options.disabled) {
        return new Proxy(this, {
          get: function get(target, prop) {
            var origMethod = target[prop];

            if (typeof origMethod === 'function') {
              return function () {};
            }

            return undefined;
          }
        });
      }
    } // eslint-disable-next-line class-methods-use-this


    _createClass(MatomoTracker, [{
      key: "_init",
      value: function _init() {
        if (this.options.debug) console.log('init', this.type);
        var u = this.trackerUrl;

        window._paq.push(['requireConsent']);

        window._paq.push(['setTrackerUrl', "".concat(u, "matomo.php")]);

        window._paq.push(['setSiteId', "".concat(this.siteId)]);

        var d = document;
        var g = d.createElement('script');
        var s = d.getElementsByTagName('script')[0];
        g.type = 'text/javascript';
        g.async = true;
        g.defer = true;
        g.src = "".concat(u, "matomo.js");
        s.parentNode.insertBefore(g, s);
      }
    }, {
      key: "init",
      value: function init() {
        if (!this.options.immediate) {
          // eslint-disable-next-line no-underscore-dangle
          this._init();
        }
      }
    }, {
      key: "consentGiven",
      value: function consentGiven() {
        window._paq.push(['rememberConsentGiven']);
      }
    }, {
      key: "consentDeclined",
      value: function consentDeclined() {
        window._paq.push(['forgetConsentGiven']);
      } // eslint-disable-next-line class-methods-use-this

    }, {
      key: "consentNoDecision",
      value: function consentNoDecision() {}
    }, {
      key: "trackInteraction",
      value: function trackInteraction() {}
    }, {
      key: "trackPageView",
      value: function trackPageView(url, title) {
        if (url) window._paq.push(['setCustomUrl', url]);
        if (title) window._paq.push(['setDocumentTitle', title]);

        window._paq.push(['trackPageView']);
      }
    }, {
      key: "trackDatasetDetailsPageView",
      value: function trackDatasetDetailsPageView() {}
    }, {
      key: "trackDownload",
      value: function trackDownload(url) {
        window._paq.push(['trackLink', url, 'download']);
      }
    }, {
      key: "trackOutlink",
      value: function trackOutlink(url) {
        window._paq.push(['trackLink', url, 'link']);
      }
    }, {
      key: "trackEvent",
      value: function trackEvent(category, action, name, value) {
        window._paq.push(['trackEvent', category].concat(_toConsumableArray(action ? [action] : []), _toConsumableArray(name ? [name] : []), _toConsumableArray(value ? [value] : [])));
      }
    }, {
      key: "trackGotoResource",
      value: function trackGotoResource() {
        window._paq.push(['trackEvent', 'GoToResource', 'Clicked']);
      }
    }, {
      key: "stop",
      value: function stop() {
        if (this.options.debug) console.log('Disabling window._paq');
        window._paq = {
          push: function push() {}
        };
        this.stopped = true;
      }
    }, {
      key: "isStopped",
      get: function get() {
        return !!this.stopped;
      }
    }]);

    return MatomoTracker;
  }();

  function cookieDelete(cookieName) {
    document.cookie = "".concat(cookieName, "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;");
  }

  function deletePiwikCookies() {
    var allCookies = document.cookie.split(';');

    for (var cookie = 0; cookie < allCookies.length; cookie += 1) {
      if (allCookies[cookie].indexOf('_pk') !== -1 || allCookies[cookie].indexOf('ppms') !== -1) {
        var cookieName = allCookies[cookie].split('=')[0];
        cookieDelete(cookieName);
      }
    }
  }

  function deletePiwikWebStorage() {
    window.localStorage.removeItem('ppms_webstorage');
  }

  function clearPiwik() {
    deletePiwikCookies();
    deletePiwikWebStorage();
  }

  var PiwikProTracker = /*#__PURE__*/function (_MatomoTracker) {
    _inherits(PiwikProTracker, _MatomoTracker);

    var _super = _createSuper(PiwikProTracker);

    function PiwikProTracker(trackerUrl, siteId, options) {
      var _this;

      _classCallCheck(this, PiwikProTracker);

      _this = _super.call(this, trackerUrl, siteId, options);
      _this.type = 'piwikpro';
      _this._interceptCounter = 0;
      _this.suspended = _this.options.suspended || false;
      _this.useSuspendFeature = _this.options.useUseSuspendFeature || false;
      window.dataLayer = window.dataLayer || [];

      if (_this.useSuspendFeature) {
        _this._activateSuspendFeature();
      }

      var old = window._paq.push; // Intercept the following commands
      // Only intercept their first occurence

      var interceptCommands = ['enableLinkTracking', 'trackPageView'];

      var interceptor = function interceptor(args) {
        if (Array.isArray(args) && args.length > 0) {
          var command = args[0];

          if (interceptCommands.includes(command)) {
            if (_this.options.debug) console.log('intercept', command);
            interceptCommands.splice(interceptCommands.indexOf(command), 1);

            if (interceptCommands.length === 0) {
              if (_this.options.debug) console.log('restore original _paq');
              window._paq.push = old;
            }

            return;
          }
        }

        old.apply(_assertThisInitialized(_this), [args]);
      };

      window._paq.push = interceptor;
      return _this;
    }

    _createClass(PiwikProTracker, [{
      key: "_activateSuspendFeature",
      value: function _activateSuspendFeature() {
        this.tmpDataLayer = [];

        this.suspendFilterFn = function () {
          return false;
        };

        var that = this;
        window.dataLayer = new Proxy([], {
          get: function get(target, prop, receiver) {
            if (prop === 'push') {
              return new Proxy(target.push, {
                apply: function apply(_target, thisArg, argumentsList) {
                  return that.suspended && !that.suspendFilterFn(argumentsList) ? that.tmpDataLayer.push(argumentsList[0]) : _target.apply(thisArg, argumentsList);
                }
              });
            }

            return Reflect.get(target, prop, receiver);
          }
        });
      }
    }, {
      key: "suspendFilter",
      value: function suspendFilter(filterFn) {
        if (!this.useSuspendFeature) return;
        this.suspended = true;
        this.suspendFilterFn = typeof filterFn === 'function' ? filterFn : function () {
          return false;
        };
      }
    }, {
      key: "resume",
      value: function resume() {
        if (!this.useSuspendFeature) return;
        this.suspended = false;
        this.tmpDataLayer.forEach(function (item) {
          window.dataLayer.push(item);
        });
        this.tmpDataLayer = [];
      }
      /* eslint-disable */

    }, {
      key: "_init",
      value: function _init() {
        var _this2 = this;

        if (this.options.debug) if (this.options.debug) console.log('init', this.type);
        if (this.options.debug) _paq.push(['setUserId', 'dev__debug2']);

        (function (window, document, dataLayerName, id) {
          window[dataLayerName] = window[dataLayerName] || [], window[dataLayerName].push({
            start: new Date().getTime(),
            event: 'stg.start'
          });
          var scripts = document.getElementsByTagName('script')[0];
          var tags = document.createElement('script');

          function stgCreateCookie(a, b, c) {
            var d = '';

            if (c) {
              var e = new Date();
              e.setTime(e.getTime() + 24 * c * 60 * 60 * 1e3), d = "; expires=".concat(e.toUTCString());
            }

            document.cookie = "".concat(a, "=").concat(b).concat(d, "; path=/");
          }

          var isStgDebug = (window.location.href.match('stg_debug') || document.cookie.match('stg_debug')) && !window.location.href.match('stg_disable_debug');
          stgCreateCookie('stg_debug', isStgDebug ? 1 : '', isStgDebug ? 14 : -1);
          var qP = [];
          dataLayerName !== 'dataLayer' && qP.push("data_layer_name=".concat(dataLayerName)), isStgDebug && qP.push('stg_debug');
          var qPString = qP.length > 0 ? "?".concat(qP.join('&')) : '';
          tags.async = !0, tags.src = "".concat(_this2.trackerUrl).concat(id, ".js").concat(qPString), scripts.parentNode.insertBefore(tags, scripts); // eslint-disable-next-line no-unused-expressions

          !function (a, n, i) {
            a[n] = a[n] || {};

            for (var c = 0; c < i.length; c++) {
              !function (i) {
                a[n][i] = a[n][i] || {}, a[n][i].api = a[n][i].api || function () {
                  var a = [].slice.call(arguments, 0);
                  typeof a[0] === 'string' && window[dataLayerName].push({
                    event: "".concat(n, ".").concat(i, ":").concat(a[0]),
                    parameters: [].slice.call(arguments, 1)
                  });
                };
              }(i[c]);
            }
          }(window, 'ppms', ['tm', 'cm']);
        })(window, document, 'dataLayer', this.siteId);
      }
      /* eslint-enable */

    }, {
      key: "init",
      value: function init() {
        if (!this.options.immediate) {
          // eslint-disable-next-line no-underscore-dangle
          this._init();
        }
      }
      /* eslint-disable */

    }, {
      key: "consentGiven",
      value: function consentGiven() {
        var new_consent = {
          consents: {}
        };
        new_consent.consents = {
          analytics: {
            status: 1
          }
        };
        window.ppms && window.ppms.cm.api('setComplianceSettings', new_consent, function (new_consent) {
          console.log(new_consent);
        });
      }
      /* eslint-enable */

      /* eslint-disable */

    }, {
      key: "consentDeclined",
      value: function consentDeclined() {
        var _this3 = this;

        var new_consent = {
          consents: {}
        };
        new_consent.consents = {
          analytics: {
            status: 0
          }
        };
        window.ppms && window.ppms.cm.api('setComplianceSettings', new_consent, function (new_consent) {
          // Optionally do additional tasks (like gdpr compliancy) when user has given no consent
          if (_this3.options.removeCookiesWhenNoConsent) setTimeout(clearPiwik, 1000);
          if (_this3.options.stopWhenNoConsent) _this3.stop();
          console.log(new_consent);
        });
      }
      /* eslint-enable */

      /* eslint-disable */

    }, {
      key: "consentNoDecision",
      value: function consentNoDecision() {
        var new_consent = {
          consents: {}
        };
        new_consent.consents = {
          analytics: {
            status: -1
          }
        };
        window.ppms && window.ppms.cm.api('setComplianceSettings', new_consent, function (new_consent) {
          console.log(new_consent);
        });
      }
      /* eslint-enable */

      /* eslint-disable class-methods-use-this */

      /* eslint-disable no-underscore-dangle */

    }, {
      key: "trackInteraction",
      value: function trackInteraction() {
        var eventType = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 'screen_load';
        var variables = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
        window.dataLayer.push(_objectSpread2({
          event: 'analytics_interaction',
          event_type: eventType
        }, variables));
      }
    }, {
      key: "trackPageView",
      value: function trackPageView(url, title, _ref) {
        var _ref$eventType = _ref.eventType,
            eventType = _ref$eventType === void 0 ? 'screen_load' : _ref$eventType,
            _ref$metadata = _ref.metadata,
            metadata = _ref$metadata === void 0 ? {} : _ref$metadata;
        this.trackInteraction(eventType, _objectSpread2(_objectSpread2(_objectSpread2({}, title ? {
          screen_title: title
        } : {}), url ? {
          page_url: url
        } : {}), metadata));
      }
    }, {
      key: "trackDatasetDetailsPageView",
      value: function trackDatasetDetailsPageView(url, title, dataset) {
        this.trackPageView(url, title, {
          eventType: 'send_dataset_metadata',
          metadata: dataset
        });
      }
    }, {
      key: "trackDownload",
      value: function trackDownload(url) {
        this.trackInteraction('download', {
          page_url: url
        });

        _get(_getPrototypeOf(PiwikProTracker.prototype), "trackDownload", this).call(this, url);
      }
    }, {
      key: "trackOutlink",
      value: function trackOutlink(url) {
        this.trackInteraction('outlink', {
          page_url: url
        });

        _get(_getPrototypeOf(PiwikProTracker.prototype), "trackOutlink", this).call(this, url);
      }
    }, {
      key: "trackGotoResource",
      value: function trackGotoResource() {
        this.trackInteraction('go_to_resource');
      }
    }, {
      key: "stop",
      value: function stop() {
        if (this.options.debug) console.log('Disabling window.dataLayer');
        window.dataLayer = {
          push: function push() {}
        };

        _get(_getPrototypeOf(PiwikProTracker.prototype), "stop", this).call(this);
      }
    }, {
      key: "isSuspended",
      get: function get() {
        return this.suspended;
      }
    }]);

    return PiwikProTracker;
  }(MatomoTracker);

  /**
   * Relevant for PiwikPro only.
   * Prevents from the initial route from being tracked.
   *
   * @see https://gitlab.fokus.fraunhofer.de/piveau/organisation/piveau-scrum-board/-/issues/1602
   */

  var preventFirstRouteTrack = true;

  var beforeTrackPageViewFn = function beforeTrackPageViewFn() {};
  /**
   * Tracks page view on route changes
   * @param {*} router Vue router instance
   * @param {MatomoTracker | PiwikProTracker} tracker Piwik tracker
   */


  function trackRouteChanges(router, tracker, options) {
    var opts = _objectSpread2({
      onlyTrackWithLocale: true,
      useDatasetsMinScoreFix: false,
      delay: 1000,
      trackPagePredicate: true
    }, options);

    router.afterEach(function (to, from) {
      if (tracker.isStopped) return;
      if (to.meta.ignoreAnalytics) return;
      var isSamePath = to.fullPath === (from && from.fullPath);
      var isSameRouteName = to.name === from.name;
      if (isSamePath && isSameRouteName) return; // Workaround: remove duplicated page views for Datasets page
      // because some default queries being added.
      // Possible fixes are to either implement dynamic filtering option
      // or remove that default query mechanism from router
      // (and add fallback values to store instead)

      var query = to.query,
          name = to.name;
      var isDatasetsPage = name === 'Datasets';
      var hasMinScoring = to.query && 'minScoring' in query;
      var hasLocale = to.query && 'locale' in to.query;
      if (opts.onlyTrackWithLocale && !hasLocale) return;
      if (opts.useDatasetsMinScoreFix && isDatasetsPage && !hasMinScoring) return;

      if (opts.debug && opts.verbose) {
        console.log('Track page view', from.fullPath, to.fullPath);
      }

      var url = router.resolve(to.fullPath).href;

      if (beforeTrackPageViewFn && typeof beforeTrackPageViewFn === 'function') {
        if (opts.verbose) console.log('Before track page view');
        beforeTrackPageViewFn(to, from, tracker);
      } else if (typeof beforeTrackPageViewFn !== 'function') {
        console.warn('beforeTrackPageViewFn must be a function');
      }

      var trackPagePredicateEval = typeof opts.trackPagePredicate === 'function' ? opts.trackPagePredicate(to, from) : opts.trackPagePredicate;

      if (!trackPagePredicateEval) {
        if (opts.verbose) console.log('I will not track this page view due to trackPagePredicate');
        return;
      }

      if (tracker.type === 'piwikpro') {
        if (!preventFirstRouteTrack) {
          // Workaround: Wait for a set amount of time before tracking the page
          // in order to send correct page title.
          // NOTICE: will send incorrect page title when visitor navigates to
          // another page before the timeout is triggered.
          setTimeout(function () {
            tracker.trackPageView(url, "".concat(document.title || to.meta.title), {
              eventType: 'screen_load'
            });
          }, opts.delay);
        } else {
          // Workaround: send initial 'trackPageView' in the same delayed fashion
          // instead of letting the init script doing it for you and possibly
          // sending undefined title page
          preventFirstRouteTrack = false;
          if (opts.debug) console.log('Prevent first route track');
          setTimeout(function () {
            // eslint-disable-next-line no-underscore-dangle
            window._paq = window._paq || []; // eslint-disable-next-line no-underscore-dangle

            window._paq.push(['trackPageView']);
          }, opts.delay);
        }
      } else if (tracker.type === 'matomo') {
        tracker.trackPageView(url);
      }
    });
  }

  var UniversalPiwik = {
    install: function install(Vue, options) {
      var opts = _objectSpread2({
        trackerUrl: undefined,
        siteId: undefined,
        router: undefined,
        isPiwikPro: false,
        debug: false,
        immediate: false,
        removeCookiesWhenNoConsent: true,
        stopWhenNoConsent: true,
        useSuspendFeature: false,
        verbose: false,
        disabled: false,
        pageViewOptions: {
          useDatasetsMinScoreFix: false,
          onlyTrackWithLocale: true,
          delay: 1000,
          beforeTrackPageView: function beforeTrackPageView() {},
          trackPagePredicate: true
        }
      }, options);

      if (opts.debug && opts.verbose) {
        console.log('options = ', opts);
      }

      if (!opts.trackerUrl || !opts.siteId) {
        // eslint-disable-next-line no-console
        console.error('Error: trackerUrl or siteId is not specified');
      } // If no trackerUrl, use MatomoTracker as fallback so that tracking goes into limbo
      // inside _paq


      var trackerParams = [opts.trackerUrl, opts.siteId, {
        debug: opts.debug,
        immediate: opts.immediate,
        removeCookiesWhenNoConsent: opts.removeCookiesWhenNoConsent,
        stopWhenNoConsent: opts.stopWhenNoConsent,
        disabled: opts.disabled,
        useSuspendFeature: opts.useSuspendFeature
      }];
      var tracker = opts.isPiwikPro && opts.trackerUrl ? _construct(PiwikProTracker, trackerParams) : _construct(MatomoTracker, trackerParams); // eslint-disable-next-line no-underscore-dangle

      if (opts.immediate) tracker._init();

      if (opts.router) {
        if (typeof opts.pageViewOptions.beforeTrackPageView === 'function') {
          beforeTrackPageViewFn = opts.pageViewOptions.beforeTrackPageView;
        }

        trackRouteChanges(opts.router, tracker, _objectSpread2({
          debug: opts.debug,
          verbose: opts.verbose
        }, opts.pageViewOptions));
      }

      Vue.prototype.$piwik = tracker;
      Vue.prototype.$universalPiwik = {
        beforeTrackPageView: function beforeTrackPageView(cb) {
          beforeTrackPageViewFn = cb;
        }
      };
    }
  };

  return UniversalPiwik;

}));
//# sourceMappingURL=piveau-universal-piwik.umd.js.map
