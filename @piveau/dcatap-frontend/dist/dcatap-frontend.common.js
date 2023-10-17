module.exports =
/******/ (function(modules) { // webpackBootstrap
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

/***/ 0:
/***/ (function(module, exports) {

/* (ignored) */

/***/ }),

/***/ "00ee":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");

var TO_STRING_TAG = wellKnownSymbol('toStringTag');
var test = {};

test[TO_STRING_TAG] = 'z';

module.exports = String(test) === '[object z]';


/***/ }),

/***/ "0366":
/***/ (function(module, exports, __webpack_require__) {

var aFunction = __webpack_require__("1c0b");

// optional / simple context binding
module.exports = function (fn, that, length) {
  aFunction(fn);
  if (that === undefined) return fn;
  switch (length) {
    case 0: return function () {
      return fn.call(that);
    };
    case 1: return function (a) {
      return fn.call(that, a);
    };
    case 2: return function (a, b) {
      return fn.call(that, a, b);
    };
    case 3: return function (a, b, c) {
      return fn.call(that, a, b, c);
    };
  }
  return function (/* ...args */) {
    return fn.apply(that, arguments);
  };
};


/***/ }),

/***/ "057f":
/***/ (function(module, exports, __webpack_require__) {

/* eslint-disable es/no-object-getownpropertynames -- safe */
var toIndexedObject = __webpack_require__("fc6a");
var $getOwnPropertyNames = __webpack_require__("241c").f;

var toString = {}.toString;

var windowNames = typeof window == 'object' && window && Object.getOwnPropertyNames
  ? Object.getOwnPropertyNames(window) : [];

var getWindowNames = function (it) {
  try {
    return $getOwnPropertyNames(it);
  } catch (error) {
    return windowNames.slice();
  }
};

// fallback for IE11 buggy Object.getOwnPropertyNames with iframe and window
module.exports.f = function getOwnPropertyNames(it) {
  return windowNames && toString.call(it) == '[object Window]'
    ? getWindowNames(it)
    : $getOwnPropertyNames(toIndexedObject(it));
};


/***/ }),

/***/ "06cf":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var propertyIsEnumerableModule = __webpack_require__("d1e79");
var createPropertyDescriptor = __webpack_require__("5c6c");
var toIndexedObject = __webpack_require__("fc6a");
var toPrimitive = __webpack_require__("c04e");
var has = __webpack_require__("5135");
var IE8_DOM_DEFINE = __webpack_require__("0cfb");

// eslint-disable-next-line es/no-object-getownpropertydescriptor -- safe
var $getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;

// `Object.getOwnPropertyDescriptor` method
// https://tc39.es/ecma262/#sec-object.getownpropertydescriptor
exports.f = DESCRIPTORS ? $getOwnPropertyDescriptor : function getOwnPropertyDescriptor(O, P) {
  O = toIndexedObject(O);
  P = toPrimitive(P, true);
  if (IE8_DOM_DEFINE) try {
    return $getOwnPropertyDescriptor(O, P);
  } catch (error) { /* empty */ }
  if (has(O, P)) return createPropertyDescriptor(!propertyIsEnumerableModule.f.call(O, P), O[P]);
};


/***/ }),

/***/ "08d2":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "0cb2":
/***/ (function(module, exports, __webpack_require__) {

var toObject = __webpack_require__("7b0b");

var floor = Math.floor;
var replace = ''.replace;
var SUBSTITUTION_SYMBOLS = /\$([$&'`]|\d{1,2}|<[^>]*>)/g;
var SUBSTITUTION_SYMBOLS_NO_NAMED = /\$([$&'`]|\d{1,2})/g;

// https://tc39.es/ecma262/#sec-getsubstitution
module.exports = function (matched, str, position, captures, namedCaptures, replacement) {
  var tailPos = position + matched.length;
  var m = captures.length;
  var symbols = SUBSTITUTION_SYMBOLS_NO_NAMED;
  if (namedCaptures !== undefined) {
    namedCaptures = toObject(namedCaptures);
    symbols = SUBSTITUTION_SYMBOLS;
  }
  return replace.call(replacement, symbols, function (match, ch) {
    var capture;
    switch (ch.charAt(0)) {
      case '$': return '$';
      case '&': return matched;
      case '`': return str.slice(0, position);
      case "'": return str.slice(tailPos);
      case '<':
        capture = namedCaptures[ch.slice(1, -1)];
        break;
      default: // \d\d?
        var n = +ch;
        if (n === 0) return match;
        if (n > m) {
          var f = floor(n / 10);
          if (f === 0) return match;
          if (f <= m) return captures[f - 1] === undefined ? ch.charAt(1) : captures[f - 1] + ch.charAt(1);
          return match;
        }
        capture = captures[n - 1];
    }
    return capture === undefined ? '' : capture;
  });
};


/***/ }),

/***/ "0cfb":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var fails = __webpack_require__("d039");
var createElement = __webpack_require__("cc12");

// Thank's IE8 for his funny defineProperty
module.exports = !DESCRIPTORS && !fails(function () {
  // eslint-disable-next-line es/no-object-defineproperty -- requied for testing
  return Object.defineProperty(createElement('div'), 'a', {
    get: function () { return 7; }
  }).a != 7;
});


/***/ }),

/***/ "0d88":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2019 Digital Bazaar, Inc. All rights reserved.
 */


const {
  isArray: _isArray,
  isObject: _isObject,
  isString: _isString,
} = __webpack_require__("68fa");
const {
  asArray: _asArray
} = __webpack_require__("89ae");
const {prependBase} = __webpack_require__("ddd7");
const JsonLdError = __webpack_require__("5950");
const ResolvedContext = __webpack_require__("9093");

const MAX_CONTEXT_URLS = 10;

module.exports = class ContextResolver {
  /**
   * Creates a ContextResolver.
   *
   * @param sharedCache a shared LRU cache with `get` and `set` APIs.
   */
  constructor({sharedCache}) {
    this.perOpCache = new Map();
    this.sharedCache = sharedCache;
  }

  async resolve({
    activeCtx, context, documentLoader, base, cycles = new Set()
  }) {
    // process `@context`
    if(context && _isObject(context) && context['@context']) {
      context = context['@context'];
    }

    // context is one or more contexts
    context = _asArray(context);

    // resolve each context in the array
    const allResolved = [];
    for(const ctx of context) {
      if(_isString(ctx)) {
        // see if `ctx` has been resolved before...
        let resolved = this._get(ctx);
        if(!resolved) {
          // not resolved yet, resolve
          resolved = await this._resolveRemoteContext(
            {activeCtx, url: ctx, documentLoader, base, cycles});
        }

        // add to output and continue
        if(_isArray(resolved)) {
          allResolved.push(...resolved);
        } else {
          allResolved.push(resolved);
        }
        continue;
      }
      if(ctx === null) {
        // handle `null` context, nothing to cache
        allResolved.push(new ResolvedContext({document: null}));
        continue;
      }
      if(!_isObject(ctx)) {
        _throwInvalidLocalContext(context);
      }
      // context is an object, get/create `ResolvedContext` for it
      const key = JSON.stringify(ctx);
      let resolved = this._get(key);
      if(!resolved) {
        // create a new static `ResolvedContext` and cache it
        resolved = new ResolvedContext({document: ctx});
        this._cacheResolvedContext({key, resolved, tag: 'static'});
      }
      allResolved.push(resolved);
    }

    return allResolved;
  }

  _get(key) {
    // get key from per operation cache; no `tag` is used with this cache so
    // any retrieved context will always be the same during a single operation
    let resolved = this.perOpCache.get(key);
    if(!resolved) {
      // see if the shared cache has a `static` entry for this URL
      const tagMap = this.sharedCache.get(key);
      if(tagMap) {
        resolved = tagMap.get('static');
        if(resolved) {
          this.perOpCache.set(key, resolved);
        }
      }
    }
    return resolved;
  }

  _cacheResolvedContext({key, resolved, tag}) {
    this.perOpCache.set(key, resolved);
    if(tag !== undefined) {
      let tagMap = this.sharedCache.get(key);
      if(!tagMap) {
        tagMap = new Map();
        this.sharedCache.set(key, tagMap);
      }
      tagMap.set(tag, resolved);
    }
    return resolved;
  }

  async _resolveRemoteContext({activeCtx, url, documentLoader, base, cycles}) {
    // resolve relative URL and fetch context
    url = prependBase(base, url);
    const {context, remoteDoc} = await this._fetchContext(
      {activeCtx, url, documentLoader, cycles});

    // update base according to remote document and resolve any relative URLs
    base = remoteDoc.documentUrl || url;
    _resolveContextUrls({context, base});

    // resolve, cache, and return context
    const resolved = await this.resolve(
      {activeCtx, context, documentLoader, base, cycles});
    this._cacheResolvedContext({key: url, resolved, tag: remoteDoc.tag});
    return resolved;
  }

  async _fetchContext({activeCtx, url, documentLoader, cycles}) {
    // check for max context URLs fetched during a resolve operation
    if(cycles.size > MAX_CONTEXT_URLS) {
      throw new JsonLdError(
        'Maximum number of @context URLs exceeded.',
        'jsonld.ContextUrlError',
        {
          code: activeCtx.processingMode === 'json-ld-1.0' ?
            'loading remote context failed' :
            'context overflow',
          max: MAX_CONTEXT_URLS
        });
    }

    // check for context URL cycle
    // shortcut to avoid extra work that would eventually hit the max above
    if(cycles.has(url)) {
      throw new JsonLdError(
        'Cyclical @context URLs detected.',
        'jsonld.ContextUrlError',
        {
          code: activeCtx.processingMode === 'json-ld-1.0' ?
            'recursive context inclusion' :
            'context overflow',
          url
        });
    }

    // track cycles
    cycles.add(url);

    let context;
    let remoteDoc;

    try {
      remoteDoc = await documentLoader(url);
      context = remoteDoc.document || null;
      // parse string context as JSON
      if(_isString(context)) {
        context = JSON.parse(context);
      }
    } catch(e) {
      throw new JsonLdError(
        'Dereferencing a URL did not result in a valid JSON-LD object. ' +
        'Possible causes are an inaccessible URL perhaps due to ' +
        'a same-origin policy (ensure the server uses CORS if you are ' +
        'using client-side JavaScript), too many redirects, a ' +
        'non-JSON response, or more than one HTTP Link Header was ' +
        'provided for a remote context.',
        'jsonld.InvalidUrl',
        {code: 'loading remote context failed', url, cause: e});
    }

    // ensure ctx is an object
    if(!_isObject(context)) {
      throw new JsonLdError(
        'Dereferencing a URL did not result in a JSON object. The ' +
        'response was valid JSON, but it was not a JSON object.',
        'jsonld.InvalidUrl', {code: 'invalid remote context', url});
    }

    // use empty context if no @context key is present
    if(!('@context' in context)) {
      context = {'@context': {}};
    } else {
      context = {'@context': context['@context']};
    }

    // append @context URL to context if given
    if(remoteDoc.contextUrl) {
      if(!_isArray(context['@context'])) {
        context['@context'] = [context['@context']];
      }
      context['@context'].push(remoteDoc.contextUrl);
    }

    return {context, remoteDoc};
  }
};

function _throwInvalidLocalContext(ctx) {
  throw new JsonLdError(
    'Invalid JSON-LD syntax; @context must be an object.',
    'jsonld.SyntaxError', {
      code: 'invalid local context', context: ctx
    });
}

/**
 * Resolve all relative `@context` URLs in the given context by inline
 * replacing them with absolute URLs.
 *
 * @param context the context.
 * @param base the base IRI to use to resolve relative IRIs.
 */
function _resolveContextUrls({context, base}) {
  if(!context) {
    return;
  }

  const ctx = context['@context'];

  if(_isString(ctx)) {
    context['@context'] = prependBase(base, ctx);
    return;
  }

  if(_isArray(ctx)) {
    for(let i = 0; i < ctx.length; ++i) {
      const element = ctx[i];
      if(_isString(element)) {
        ctx[i] = prependBase(base, element);
        continue;
      }
      if(_isObject(element)) {
        _resolveContextUrls({context: {'@context': element}, base});
      }
    }
    return;
  }

  if(!_isObject(ctx)) {
    // no @context URLs can be found in non-object
    return;
  }

  // ctx is an object, resolve any context URLs in terms
  for(const term in ctx) {
    _resolveContextUrls({context: ctx[term], base});
  }
}


/***/ }),

/***/ "14c3":
/***/ (function(module, exports, __webpack_require__) {

var classof = __webpack_require__("c6b6");
var regexpExec = __webpack_require__("9263");

// `RegExpExec` abstract operation
// https://tc39.es/ecma262/#sec-regexpexec
module.exports = function (R, S) {
  var exec = R.exec;
  if (typeof exec === 'function') {
    var result = exec.call(R, S);
    if (typeof result !== 'object') {
      throw TypeError('RegExp exec method returned something other than an Object or null');
    }
    return result;
  }

  if (classof(R) !== 'RegExp') {
    throw TypeError('RegExp#exec called on incompatible receiver');
  }

  return regexpExec.call(R, S);
};



/***/ }),

/***/ "159b":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var DOMIterables = __webpack_require__("fdbc");
var forEach = __webpack_require__("17c2");
var createNonEnumerableProperty = __webpack_require__("9112");

for (var COLLECTION_NAME in DOMIterables) {
  var Collection = global[COLLECTION_NAME];
  var CollectionPrototype = Collection && Collection.prototype;
  // some Chrome versions have non-configurable methods on DOMTokenList
  if (CollectionPrototype && CollectionPrototype.forEach !== forEach) try {
    createNonEnumerableProperty(CollectionPrototype, 'forEach', forEach);
  } catch (error) {
    CollectionPrototype.forEach = forEach;
  }
}


/***/ }),

/***/ "17c2":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $forEach = __webpack_require__("b727").forEach;
var arrayMethodIsStrict = __webpack_require__("a640");

var STRICT_METHOD = arrayMethodIsStrict('forEach');

// `Array.prototype.forEach` method implementation
// https://tc39.es/ecma262/#sec-array.prototype.foreach
module.exports = !STRICT_METHOD ? function forEach(callbackfn /* , thisArg */) {
  return $forEach(this, callbackfn, arguments.length > 1 ? arguments[1] : undefined);
// eslint-disable-next-line es/no-array-prototype-foreach -- safe
} : [].forEach;


/***/ }),

/***/ "19aa":
/***/ (function(module, exports) {

module.exports = function (it, Constructor, name) {
  if (!(it instanceof Constructor)) {
    throw TypeError('Incorrect ' + (name ? name + ' ' : '') + 'invocation');
  } return it;
};


/***/ }),

/***/ "1be4":
/***/ (function(module, exports, __webpack_require__) {

var getBuiltIn = __webpack_require__("d066");

module.exports = getBuiltIn('document', 'documentElement');


/***/ }),

/***/ "1c0b":
/***/ (function(module, exports) {

module.exports = function (it) {
  if (typeof it != 'function') {
    throw TypeError(String(it) + ' is not a function');
  } return it;
};


/***/ }),

/***/ "1c7e":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");

var ITERATOR = wellKnownSymbol('iterator');
var SAFE_CLOSING = false;

try {
  var called = 0;
  var iteratorWithReturn = {
    next: function () {
      return { done: !!called++ };
    },
    'return': function () {
      SAFE_CLOSING = true;
    }
  };
  iteratorWithReturn[ITERATOR] = function () {
    return this;
  };
  // eslint-disable-next-line es/no-array-from, no-throw-literal -- required for testing
  Array.from(iteratorWithReturn, function () { throw 2; });
} catch (error) { /* empty */ }

module.exports = function (exec, SKIP_CLOSING) {
  if (!SKIP_CLOSING && !SAFE_CLOSING) return false;
  var ITERATION_SUPPORT = false;
  try {
    var object = {};
    object[ITERATOR] = function () {
      return {
        next: function () {
          return { done: ITERATION_SUPPORT = true };
        }
      };
    };
    exec(object);
  } catch (error) { /* empty */ }
  return ITERATION_SUPPORT;
};


/***/ }),

/***/ "1cdc":
/***/ (function(module, exports, __webpack_require__) {

var userAgent = __webpack_require__("342f");

module.exports = /(?:iphone|ipod|ipad).*applewebkit/i.test(userAgent);


/***/ }),

/***/ "1d80":
/***/ (function(module, exports) {

// `RequireObjectCoercible` abstract operation
// https://tc39.es/ecma262/#sec-requireobjectcoercible
module.exports = function (it) {
  if (it == undefined) throw TypeError("Can't call method on " + it);
  return it;
};


/***/ }),

/***/ "1da1":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return _asyncToGenerator; });
/* harmony import */ var core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("d3b7");
/* harmony import */ var core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__("e6cf");
/* harmony import */ var core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_1__);



function asyncGeneratorStep(gen, resolve, reject, _next, _throw, key, arg) {
  try {
    var info = gen[key](arg);
    var value = info.value;
  } catch (error) {
    reject(error);
    return;
  }

  if (info.done) {
    resolve(value);
  } else {
    Promise.resolve(value).then(_next, _throw);
  }
}

function _asyncToGenerator(fn) {
  return function () {
    var self = this,
        args = arguments;
    return new Promise(function (resolve, reject) {
      var gen = fn.apply(self, args);

      function _next(value) {
        asyncGeneratorStep(gen, resolve, reject, _next, _throw, "next", value);
      }

      function _throw(err) {
        asyncGeneratorStep(gen, resolve, reject, _next, _throw, "throw", err);
      }

      _next(undefined);
    });
  };
}

/***/ }),

/***/ "1dde":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");
var wellKnownSymbol = __webpack_require__("b622");
var V8_VERSION = __webpack_require__("2d00");

var SPECIES = wellKnownSymbol('species');

module.exports = function (METHOD_NAME) {
  // We can't use this feature detection in V8 since it causes
  // deoptimization and serious performance degradation
  // https://github.com/zloirock/core-js/issues/677
  return V8_VERSION >= 51 || !fails(function () {
    var array = [];
    var constructor = array.constructor = {};
    constructor[SPECIES] = function () {
      return { foo: 1 };
    };
    return array[METHOD_NAME](Boolean).foo !== 1;
  });
};


/***/ }),

/***/ "21d0":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_SearchableSelect_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("bd17");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_SearchableSelect_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_SearchableSelect_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "2266":
/***/ (function(module, exports, __webpack_require__) {

var anObject = __webpack_require__("825a");
var isArrayIteratorMethod = __webpack_require__("e95a");
var toLength = __webpack_require__("50c4");
var bind = __webpack_require__("0366");
var getIteratorMethod = __webpack_require__("35a1");
var iteratorClose = __webpack_require__("2a62");

var Result = function (stopped, result) {
  this.stopped = stopped;
  this.result = result;
};

module.exports = function (iterable, unboundFunction, options) {
  var that = options && options.that;
  var AS_ENTRIES = !!(options && options.AS_ENTRIES);
  var IS_ITERATOR = !!(options && options.IS_ITERATOR);
  var INTERRUPTED = !!(options && options.INTERRUPTED);
  var fn = bind(unboundFunction, that, 1 + AS_ENTRIES + INTERRUPTED);
  var iterator, iterFn, index, length, result, next, step;

  var stop = function (condition) {
    if (iterator) iteratorClose(iterator);
    return new Result(true, condition);
  };

  var callFn = function (value) {
    if (AS_ENTRIES) {
      anObject(value);
      return INTERRUPTED ? fn(value[0], value[1], stop) : fn(value[0], value[1]);
    } return INTERRUPTED ? fn(value, stop) : fn(value);
  };

  if (IS_ITERATOR) {
    iterator = iterable;
  } else {
    iterFn = getIteratorMethod(iterable);
    if (typeof iterFn != 'function') throw TypeError('Target is not iterable');
    // optimisation for array iterators
    if (isArrayIteratorMethod(iterFn)) {
      for (index = 0, length = toLength(iterable.length); length > index; index++) {
        result = callFn(iterable[index]);
        if (result && result instanceof Result) return result;
      } return new Result(false);
    }
    iterator = iterFn.call(iterable);
  }

  next = iterator.next;
  while (!(step = next.call(iterator)).done) {
    try {
      result = callFn(step.value);
    } catch (error) {
      iteratorClose(iterator);
      throw error;
    }
    if (typeof result == 'object' && result && result instanceof Result) return result;
  } return new Result(false);
};


/***/ }),

/***/ "23cb":
/***/ (function(module, exports, __webpack_require__) {

var toInteger = __webpack_require__("a691");

var max = Math.max;
var min = Math.min;

// Helper for a popular repeating case of the spec:
// Let integer be ? ToInteger(index).
// If integer < 0, let result be max((length + integer), 0); else let result be min(integer, length).
module.exports = function (index, length) {
  var integer = toInteger(index);
  return integer < 0 ? max(integer + length, 0) : min(integer, length);
};


/***/ }),

/***/ "23e7":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var getOwnPropertyDescriptor = __webpack_require__("06cf").f;
var createNonEnumerableProperty = __webpack_require__("9112");
var redefine = __webpack_require__("6eeb");
var setGlobal = __webpack_require__("ce4e");
var copyConstructorProperties = __webpack_require__("e893");
var isForced = __webpack_require__("94ca");

/*
  options.target      - name of the target object
  options.global      - target is the global object
  options.stat        - export as static methods of target
  options.proto       - export as prototype methods of target
  options.real        - real prototype method for the `pure` version
  options.forced      - export even if the native feature is available
  options.bind        - bind methods to the target, required for the `pure` version
  options.wrap        - wrap constructors to preventing global pollution, required for the `pure` version
  options.unsafe      - use the simple assignment of property instead of delete + defineProperty
  options.sham        - add a flag to not completely full polyfills
  options.enumerable  - export as enumerable property
  options.noTargetGet - prevent calling a getter on target
*/
module.exports = function (options, source) {
  var TARGET = options.target;
  var GLOBAL = options.global;
  var STATIC = options.stat;
  var FORCED, target, key, targetProperty, sourceProperty, descriptor;
  if (GLOBAL) {
    target = global;
  } else if (STATIC) {
    target = global[TARGET] || setGlobal(TARGET, {});
  } else {
    target = (global[TARGET] || {}).prototype;
  }
  if (target) for (key in source) {
    sourceProperty = source[key];
    if (options.noTargetGet) {
      descriptor = getOwnPropertyDescriptor(target, key);
      targetProperty = descriptor && descriptor.value;
    } else targetProperty = target[key];
    FORCED = isForced(GLOBAL ? key : TARGET + (STATIC ? '.' : '#') + key, options.forced);
    // contained in target
    if (!FORCED && targetProperty !== undefined) {
      if (typeof sourceProperty === typeof targetProperty) continue;
      copyConstructorProperties(sourceProperty, targetProperty);
    }
    // add a flag to not completely full polyfills
    if (options.sham || (targetProperty && targetProperty.sham)) {
      createNonEnumerableProperty(sourceProperty, 'sham', true);
    }
    // extend global
    redefine(target, key, sourceProperty, options);
  }
};


/***/ }),

/***/ "241c":
/***/ (function(module, exports, __webpack_require__) {

var internalObjectKeys = __webpack_require__("ca84");
var enumBugKeys = __webpack_require__("7839");

var hiddenKeys = enumBugKeys.concat('length', 'prototype');

// `Object.getOwnPropertyNames` method
// https://tc39.es/ecma262/#sec-object.getownpropertynames
// eslint-disable-next-line es/no-object-getownpropertynames -- safe
exports.f = Object.getOwnPropertyNames || function getOwnPropertyNames(O) {
  return internalObjectKeys(O, hiddenKeys);
};


/***/ }),

/***/ "2532":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var notARegExp = __webpack_require__("5a34");
var requireObjectCoercible = __webpack_require__("1d80");
var correctIsRegExpLogic = __webpack_require__("ab13");

// `String.prototype.includes` method
// https://tc39.es/ecma262/#sec-string.prototype.includes
$({ target: 'String', proto: true, forced: !correctIsRegExpLogic('includes') }, {
  includes: function includes(searchString /* , position = 0 */) {
    return !!~String(requireObjectCoercible(this))
      .indexOf(notARegExp(searchString), arguments.length > 1 ? arguments[1] : undefined);
  }
});


/***/ }),

/***/ "25f0":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var redefine = __webpack_require__("6eeb");
var anObject = __webpack_require__("825a");
var fails = __webpack_require__("d039");
var flags = __webpack_require__("ad6d");

var TO_STRING = 'toString';
var RegExpPrototype = RegExp.prototype;
var nativeToString = RegExpPrototype[TO_STRING];

var NOT_GENERIC = fails(function () { return nativeToString.call({ source: 'a', flags: 'b' }) != '/a/b'; });
// FF44- RegExp#toString has a wrong name
var INCORRECT_NAME = nativeToString.name != TO_STRING;

// `RegExp.prototype.toString` method
// https://tc39.es/ecma262/#sec-regexp.prototype.tostring
if (NOT_GENERIC || INCORRECT_NAME) {
  redefine(RegExp.prototype, TO_STRING, function toString() {
    var R = anObject(this);
    var p = String(R.source);
    var rf = R.flags;
    var f = String(rf === undefined && R instanceof RegExp && !('flags' in RegExpPrototype) ? flags.call(R) : rf);
    return '/' + p + '/' + f;
  }, { unsafe: true });
}


/***/ }),

/***/ "2626":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var getBuiltIn = __webpack_require__("d066");
var definePropertyModule = __webpack_require__("9bf2");
var wellKnownSymbol = __webpack_require__("b622");
var DESCRIPTORS = __webpack_require__("83ab");

var SPECIES = wellKnownSymbol('species');

module.exports = function (CONSTRUCTOR_NAME) {
  var Constructor = getBuiltIn(CONSTRUCTOR_NAME);
  var defineProperty = definePropertyModule.f;

  if (DESCRIPTORS && Constructor && !Constructor[SPECIES]) {
    defineProperty(Constructor, SPECIES, {
      configurable: true,
      get: function () { return this; }
    });
  }
};


/***/ }),

/***/ "2832":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

module.exports = function (Yallist) {
  Yallist.prototype[Symbol.iterator] = function* () {
    for (let walker = this.head; walker; walker = walker.next) {
      yield walker.value
    }
  }
}


/***/ }),

/***/ "2a62":
/***/ (function(module, exports, __webpack_require__) {

var anObject = __webpack_require__("825a");

module.exports = function (iterator) {
  var returnMethod = iterator['return'];
  if (returnMethod !== undefined) {
    return anObject(returnMethod.call(iterator)).value;
  }
};


/***/ }),

/***/ "2ca0":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var getOwnPropertyDescriptor = __webpack_require__("06cf").f;
var toLength = __webpack_require__("50c4");
var notARegExp = __webpack_require__("5a34");
var requireObjectCoercible = __webpack_require__("1d80");
var correctIsRegExpLogic = __webpack_require__("ab13");
var IS_PURE = __webpack_require__("c430");

// eslint-disable-next-line es/no-string-prototype-startswith -- safe
var $startsWith = ''.startsWith;
var min = Math.min;

var CORRECT_IS_REGEXP_LOGIC = correctIsRegExpLogic('startsWith');
// https://github.com/zloirock/core-js/pull/702
var MDN_POLYFILL_BUG = !IS_PURE && !CORRECT_IS_REGEXP_LOGIC && !!function () {
  var descriptor = getOwnPropertyDescriptor(String.prototype, 'startsWith');
  return descriptor && !descriptor.writable;
}();

// `String.prototype.startsWith` method
// https://tc39.es/ecma262/#sec-string.prototype.startswith
$({ target: 'String', proto: true, forced: !MDN_POLYFILL_BUG && !CORRECT_IS_REGEXP_LOGIC }, {
  startsWith: function startsWith(searchString /* , position = 0 */) {
    var that = String(requireObjectCoercible(this));
    notARegExp(searchString);
    var index = toLength(min(arguments.length > 1 ? arguments[1] : undefined, that.length));
    var search = String(searchString);
    return $startsWith
      ? $startsWith.call(that, search, index)
      : that.slice(index, index + search.length) === search;
  }
});


/***/ }),

/***/ "2cf4":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var fails = __webpack_require__("d039");
var bind = __webpack_require__("0366");
var html = __webpack_require__("1be4");
var createElement = __webpack_require__("cc12");
var IS_IOS = __webpack_require__("1cdc");
var IS_NODE = __webpack_require__("605d");

var location = global.location;
var set = global.setImmediate;
var clear = global.clearImmediate;
var process = global.process;
var MessageChannel = global.MessageChannel;
var Dispatch = global.Dispatch;
var counter = 0;
var queue = {};
var ONREADYSTATECHANGE = 'onreadystatechange';
var defer, channel, port;

var run = function (id) {
  // eslint-disable-next-line no-prototype-builtins -- safe
  if (queue.hasOwnProperty(id)) {
    var fn = queue[id];
    delete queue[id];
    fn();
  }
};

var runner = function (id) {
  return function () {
    run(id);
  };
};

var listener = function (event) {
  run(event.data);
};

var post = function (id) {
  // old engines have not location.origin
  global.postMessage(id + '', location.protocol + '//' + location.host);
};

// Node.js 0.9+ & IE10+ has setImmediate, otherwise:
if (!set || !clear) {
  set = function setImmediate(fn) {
    var args = [];
    var i = 1;
    while (arguments.length > i) args.push(arguments[i++]);
    queue[++counter] = function () {
      // eslint-disable-next-line no-new-func -- spec requirement
      (typeof fn == 'function' ? fn : Function(fn)).apply(undefined, args);
    };
    defer(counter);
    return counter;
  };
  clear = function clearImmediate(id) {
    delete queue[id];
  };
  // Node.js 0.8-
  if (IS_NODE) {
    defer = function (id) {
      process.nextTick(runner(id));
    };
  // Sphere (JS game engine) Dispatch API
  } else if (Dispatch && Dispatch.now) {
    defer = function (id) {
      Dispatch.now(runner(id));
    };
  // Browsers with MessageChannel, includes WebWorkers
  // except iOS - https://github.com/zloirock/core-js/issues/624
  } else if (MessageChannel && !IS_IOS) {
    channel = new MessageChannel();
    port = channel.port2;
    channel.port1.onmessage = listener;
    defer = bind(port.postMessage, port, 1);
  // Browsers with postMessage, skip WebWorkers
  // IE8 has postMessage, but it's sync & typeof its postMessage is 'object'
  } else if (
    global.addEventListener &&
    typeof postMessage == 'function' &&
    !global.importScripts &&
    location && location.protocol !== 'file:' &&
    !fails(post)
  ) {
    defer = post;
    global.addEventListener('message', listener, false);
  // IE8-
  } else if (ONREADYSTATECHANGE in createElement('script')) {
    defer = function (id) {
      html.appendChild(createElement('script'))[ONREADYSTATECHANGE] = function () {
        html.removeChild(this);
        run(id);
      };
    };
  // Rest old browsers
  } else {
    defer = function (id) {
      setTimeout(runner(id), 0);
    };
  }
}

module.exports = {
  set: set,
  clear: clear
};


/***/ }),

/***/ "2d00":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var userAgent = __webpack_require__("342f");

var process = global.process;
var versions = process && process.versions;
var v8 = versions && versions.v8;
var match, version;

if (v8) {
  match = v8.split('.');
  version = match[0] + match[1];
} else if (userAgent) {
  match = userAgent.match(/Edge\/(\d+)/);
  if (!match || match[1] >= 74) {
    match = userAgent.match(/Chrome\/(\d+)/);
    if (match) version = match[1];
  }
}

module.exports = version && +version;


/***/ }),

/***/ "2f62":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(global) {/* unused harmony export Store */
/* unused harmony export createLogger */
/* unused harmony export createNamespacedHelpers */
/* unused harmony export install */
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return mapActions; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return mapGetters; });
/* unused harmony export mapMutations */
/* unused harmony export mapState */
/*!
 * vuex v3.6.2
 * (c) 2021 Evan You
 * @license MIT
 */
function applyMixin (Vue) {
  var version = Number(Vue.version.split('.')[0]);

  if (version >= 2) {
    Vue.mixin({ beforeCreate: vuexInit });
  } else {
    // override init and inject vuex init procedure
    // for 1.x backwards compatibility.
    var _init = Vue.prototype._init;
    Vue.prototype._init = function (options) {
      if ( options === void 0 ) options = {};

      options.init = options.init
        ? [vuexInit].concat(options.init)
        : vuexInit;
      _init.call(this, options);
    };
  }

  /**
   * Vuex init hook, injected into each instances init hooks list.
   */

  function vuexInit () {
    var options = this.$options;
    // store injection
    if (options.store) {
      this.$store = typeof options.store === 'function'
        ? options.store()
        : options.store;
    } else if (options.parent && options.parent.$store) {
      this.$store = options.parent.$store;
    }
  }
}

var target = typeof window !== 'undefined'
  ? window
  : typeof global !== 'undefined'
    ? global
    : {};
var devtoolHook = target.__VUE_DEVTOOLS_GLOBAL_HOOK__;

function devtoolPlugin (store) {
  if (!devtoolHook) { return }

  store._devtoolHook = devtoolHook;

  devtoolHook.emit('vuex:init', store);

  devtoolHook.on('vuex:travel-to-state', function (targetState) {
    store.replaceState(targetState);
  });

  store.subscribe(function (mutation, state) {
    devtoolHook.emit('vuex:mutation', mutation, state);
  }, { prepend: true });

  store.subscribeAction(function (action, state) {
    devtoolHook.emit('vuex:action', action, state);
  }, { prepend: true });
}

/**
 * Get the first item that pass the test
 * by second argument function
 *
 * @param {Array} list
 * @param {Function} f
 * @return {*}
 */
function find (list, f) {
  return list.filter(f)[0]
}

/**
 * Deep copy the given object considering circular structure.
 * This function caches all nested objects and its copies.
 * If it detects circular structure, use cached copy to avoid infinite loop.
 *
 * @param {*} obj
 * @param {Array<Object>} cache
 * @return {*}
 */
function deepCopy (obj, cache) {
  if ( cache === void 0 ) cache = [];

  // just return if obj is immutable value
  if (obj === null || typeof obj !== 'object') {
    return obj
  }

  // if obj is hit, it is in circular structure
  var hit = find(cache, function (c) { return c.original === obj; });
  if (hit) {
    return hit.copy
  }

  var copy = Array.isArray(obj) ? [] : {};
  // put the copy into cache at first
  // because we want to refer it in recursive deepCopy
  cache.push({
    original: obj,
    copy: copy
  });

  Object.keys(obj).forEach(function (key) {
    copy[key] = deepCopy(obj[key], cache);
  });

  return copy
}

/**
 * forEach for object
 */
function forEachValue (obj, fn) {
  Object.keys(obj).forEach(function (key) { return fn(obj[key], key); });
}

function isObject (obj) {
  return obj !== null && typeof obj === 'object'
}

function isPromise (val) {
  return val && typeof val.then === 'function'
}

function assert (condition, msg) {
  if (!condition) { throw new Error(("[vuex] " + msg)) }
}

function partial (fn, arg) {
  return function () {
    return fn(arg)
  }
}

// Base data struct for store's module, package with some attribute and method
var Module = function Module (rawModule, runtime) {
  this.runtime = runtime;
  // Store some children item
  this._children = Object.create(null);
  // Store the origin module object which passed by programmer
  this._rawModule = rawModule;
  var rawState = rawModule.state;

  // Store the origin module's state
  this.state = (typeof rawState === 'function' ? rawState() : rawState) || {};
};

var prototypeAccessors = { namespaced: { configurable: true } };

prototypeAccessors.namespaced.get = function () {
  return !!this._rawModule.namespaced
};

Module.prototype.addChild = function addChild (key, module) {
  this._children[key] = module;
};

Module.prototype.removeChild = function removeChild (key) {
  delete this._children[key];
};

Module.prototype.getChild = function getChild (key) {
  return this._children[key]
};

Module.prototype.hasChild = function hasChild (key) {
  return key in this._children
};

Module.prototype.update = function update (rawModule) {
  this._rawModule.namespaced = rawModule.namespaced;
  if (rawModule.actions) {
    this._rawModule.actions = rawModule.actions;
  }
  if (rawModule.mutations) {
    this._rawModule.mutations = rawModule.mutations;
  }
  if (rawModule.getters) {
    this._rawModule.getters = rawModule.getters;
  }
};

Module.prototype.forEachChild = function forEachChild (fn) {
  forEachValue(this._children, fn);
};

Module.prototype.forEachGetter = function forEachGetter (fn) {
  if (this._rawModule.getters) {
    forEachValue(this._rawModule.getters, fn);
  }
};

Module.prototype.forEachAction = function forEachAction (fn) {
  if (this._rawModule.actions) {
    forEachValue(this._rawModule.actions, fn);
  }
};

Module.prototype.forEachMutation = function forEachMutation (fn) {
  if (this._rawModule.mutations) {
    forEachValue(this._rawModule.mutations, fn);
  }
};

Object.defineProperties( Module.prototype, prototypeAccessors );

var ModuleCollection = function ModuleCollection (rawRootModule) {
  // register root module (Vuex.Store options)
  this.register([], rawRootModule, false);
};

ModuleCollection.prototype.get = function get (path) {
  return path.reduce(function (module, key) {
    return module.getChild(key)
  }, this.root)
};

ModuleCollection.prototype.getNamespace = function getNamespace (path) {
  var module = this.root;
  return path.reduce(function (namespace, key) {
    module = module.getChild(key);
    return namespace + (module.namespaced ? key + '/' : '')
  }, '')
};

ModuleCollection.prototype.update = function update$1 (rawRootModule) {
  update([], this.root, rawRootModule);
};

ModuleCollection.prototype.register = function register (path, rawModule, runtime) {
    var this$1 = this;
    if ( runtime === void 0 ) runtime = true;

  if ((false)) {}

  var newModule = new Module(rawModule, runtime);
  if (path.length === 0) {
    this.root = newModule;
  } else {
    var parent = this.get(path.slice(0, -1));
    parent.addChild(path[path.length - 1], newModule);
  }

  // register nested modules
  if (rawModule.modules) {
    forEachValue(rawModule.modules, function (rawChildModule, key) {
      this$1.register(path.concat(key), rawChildModule, runtime);
    });
  }
};

ModuleCollection.prototype.unregister = function unregister (path) {
  var parent = this.get(path.slice(0, -1));
  var key = path[path.length - 1];
  var child = parent.getChild(key);

  if (!child) {
    if ((false)) {}
    return
  }

  if (!child.runtime) {
    return
  }

  parent.removeChild(key);
};

ModuleCollection.prototype.isRegistered = function isRegistered (path) {
  var parent = this.get(path.slice(0, -1));
  var key = path[path.length - 1];

  if (parent) {
    return parent.hasChild(key)
  }

  return false
};

function update (path, targetModule, newModule) {
  if ((false)) {}

  // update target module
  targetModule.update(newModule);

  // update nested modules
  if (newModule.modules) {
    for (var key in newModule.modules) {
      if (!targetModule.getChild(key)) {
        if ((false)) {}
        return
      }
      update(
        path.concat(key),
        targetModule.getChild(key),
        newModule.modules[key]
      );
    }
  }
}

var functionAssert = {
  assert: function (value) { return typeof value === 'function'; },
  expected: 'function'
};

var objectAssert = {
  assert: function (value) { return typeof value === 'function' ||
    (typeof value === 'object' && typeof value.handler === 'function'); },
  expected: 'function or object with "handler" function'
};

var assertTypes = {
  getters: functionAssert,
  mutations: functionAssert,
  actions: objectAssert
};

function assertRawModule (path, rawModule) {
  Object.keys(assertTypes).forEach(function (key) {
    if (!rawModule[key]) { return }

    var assertOptions = assertTypes[key];

    forEachValue(rawModule[key], function (value, type) {
      assert(
        assertOptions.assert(value),
        makeAssertionMessage(path, key, type, value, assertOptions.expected)
      );
    });
  });
}

function makeAssertionMessage (path, key, type, value, expected) {
  var buf = key + " should be " + expected + " but \"" + key + "." + type + "\"";
  if (path.length > 0) {
    buf += " in module \"" + (path.join('.')) + "\"";
  }
  buf += " is " + (JSON.stringify(value)) + ".";
  return buf
}

var Vue; // bind on install

var Store = function Store (options) {
  var this$1 = this;
  if ( options === void 0 ) options = {};

  // Auto install if it is not done yet and `window` has `Vue`.
  // To allow users to avoid auto-installation in some cases,
  // this code should be placed here. See #731
  if (!Vue && typeof window !== 'undefined' && window.Vue) {
    install(window.Vue);
  }

  if ((false)) {}

  var plugins = options.plugins; if ( plugins === void 0 ) plugins = [];
  var strict = options.strict; if ( strict === void 0 ) strict = false;

  // store internal state
  this._committing = false;
  this._actions = Object.create(null);
  this._actionSubscribers = [];
  this._mutations = Object.create(null);
  this._wrappedGetters = Object.create(null);
  this._modules = new ModuleCollection(options);
  this._modulesNamespaceMap = Object.create(null);
  this._subscribers = [];
  this._watcherVM = new Vue();
  this._makeLocalGettersCache = Object.create(null);

  // bind commit and dispatch to self
  var store = this;
  var ref = this;
  var dispatch = ref.dispatch;
  var commit = ref.commit;
  this.dispatch = function boundDispatch (type, payload) {
    return dispatch.call(store, type, payload)
  };
  this.commit = function boundCommit (type, payload, options) {
    return commit.call(store, type, payload, options)
  };

  // strict mode
  this.strict = strict;

  var state = this._modules.root.state;

  // init root module.
  // this also recursively registers all sub-modules
  // and collects all module getters inside this._wrappedGetters
  installModule(this, state, [], this._modules.root);

  // initialize the store vm, which is responsible for the reactivity
  // (also registers _wrappedGetters as computed properties)
  resetStoreVM(this, state);

  // apply plugins
  plugins.forEach(function (plugin) { return plugin(this$1); });

  var useDevtools = options.devtools !== undefined ? options.devtools : Vue.config.devtools;
  if (useDevtools) {
    devtoolPlugin(this);
  }
};

var prototypeAccessors$1 = { state: { configurable: true } };

prototypeAccessors$1.state.get = function () {
  return this._vm._data.$$state
};

prototypeAccessors$1.state.set = function (v) {
  if ((false)) {}
};

Store.prototype.commit = function commit (_type, _payload, _options) {
    var this$1 = this;

  // check object-style commit
  var ref = unifyObjectStyle(_type, _payload, _options);
    var type = ref.type;
    var payload = ref.payload;
    var options = ref.options;

  var mutation = { type: type, payload: payload };
  var entry = this._mutations[type];
  if (!entry) {
    if ((false)) {}
    return
  }
  this._withCommit(function () {
    entry.forEach(function commitIterator (handler) {
      handler(payload);
    });
  });

  this._subscribers
    .slice() // shallow copy to prevent iterator invalidation if subscriber synchronously calls unsubscribe
    .forEach(function (sub) { return sub(mutation, this$1.state); });

  if (
    false
  ) {}
};

Store.prototype.dispatch = function dispatch (_type, _payload) {
    var this$1 = this;

  // check object-style dispatch
  var ref = unifyObjectStyle(_type, _payload);
    var type = ref.type;
    var payload = ref.payload;

  var action = { type: type, payload: payload };
  var entry = this._actions[type];
  if (!entry) {
    if ((false)) {}
    return
  }

  try {
    this._actionSubscribers
      .slice() // shallow copy to prevent iterator invalidation if subscriber synchronously calls unsubscribe
      .filter(function (sub) { return sub.before; })
      .forEach(function (sub) { return sub.before(action, this$1.state); });
  } catch (e) {
    if ((false)) {}
  }

  var result = entry.length > 1
    ? Promise.all(entry.map(function (handler) { return handler(payload); }))
    : entry[0](payload);

  return new Promise(function (resolve, reject) {
    result.then(function (res) {
      try {
        this$1._actionSubscribers
          .filter(function (sub) { return sub.after; })
          .forEach(function (sub) { return sub.after(action, this$1.state); });
      } catch (e) {
        if ((false)) {}
      }
      resolve(res);
    }, function (error) {
      try {
        this$1._actionSubscribers
          .filter(function (sub) { return sub.error; })
          .forEach(function (sub) { return sub.error(action, this$1.state, error); });
      } catch (e) {
        if ((false)) {}
      }
      reject(error);
    });
  })
};

Store.prototype.subscribe = function subscribe (fn, options) {
  return genericSubscribe(fn, this._subscribers, options)
};

Store.prototype.subscribeAction = function subscribeAction (fn, options) {
  var subs = typeof fn === 'function' ? { before: fn } : fn;
  return genericSubscribe(subs, this._actionSubscribers, options)
};

Store.prototype.watch = function watch (getter, cb, options) {
    var this$1 = this;

  if ((false)) {}
  return this._watcherVM.$watch(function () { return getter(this$1.state, this$1.getters); }, cb, options)
};

Store.prototype.replaceState = function replaceState (state) {
    var this$1 = this;

  this._withCommit(function () {
    this$1._vm._data.$$state = state;
  });
};

Store.prototype.registerModule = function registerModule (path, rawModule, options) {
    if ( options === void 0 ) options = {};

  if (typeof path === 'string') { path = [path]; }

  if ((false)) {}

  this._modules.register(path, rawModule);
  installModule(this, this.state, path, this._modules.get(path), options.preserveState);
  // reset store to update getters...
  resetStoreVM(this, this.state);
};

Store.prototype.unregisterModule = function unregisterModule (path) {
    var this$1 = this;

  if (typeof path === 'string') { path = [path]; }

  if ((false)) {}

  this._modules.unregister(path);
  this._withCommit(function () {
    var parentState = getNestedState(this$1.state, path.slice(0, -1));
    Vue.delete(parentState, path[path.length - 1]);
  });
  resetStore(this);
};

Store.prototype.hasModule = function hasModule (path) {
  if (typeof path === 'string') { path = [path]; }

  if ((false)) {}

  return this._modules.isRegistered(path)
};

Store.prototype.hotUpdate = function hotUpdate (newOptions) {
  this._modules.update(newOptions);
  resetStore(this, true);
};

Store.prototype._withCommit = function _withCommit (fn) {
  var committing = this._committing;
  this._committing = true;
  fn();
  this._committing = committing;
};

Object.defineProperties( Store.prototype, prototypeAccessors$1 );

function genericSubscribe (fn, subs, options) {
  if (subs.indexOf(fn) < 0) {
    options && options.prepend
      ? subs.unshift(fn)
      : subs.push(fn);
  }
  return function () {
    var i = subs.indexOf(fn);
    if (i > -1) {
      subs.splice(i, 1);
    }
  }
}

function resetStore (store, hot) {
  store._actions = Object.create(null);
  store._mutations = Object.create(null);
  store._wrappedGetters = Object.create(null);
  store._modulesNamespaceMap = Object.create(null);
  var state = store.state;
  // init all modules
  installModule(store, state, [], store._modules.root, true);
  // reset vm
  resetStoreVM(store, state, hot);
}

function resetStoreVM (store, state, hot) {
  var oldVm = store._vm;

  // bind store public getters
  store.getters = {};
  // reset local getters cache
  store._makeLocalGettersCache = Object.create(null);
  var wrappedGetters = store._wrappedGetters;
  var computed = {};
  forEachValue(wrappedGetters, function (fn, key) {
    // use computed to leverage its lazy-caching mechanism
    // direct inline function use will lead to closure preserving oldVm.
    // using partial to return function with only arguments preserved in closure environment.
    computed[key] = partial(fn, store);
    Object.defineProperty(store.getters, key, {
      get: function () { return store._vm[key]; },
      enumerable: true // for local getters
    });
  });

  // use a Vue instance to store the state tree
  // suppress warnings just in case the user has added
  // some funky global mixins
  var silent = Vue.config.silent;
  Vue.config.silent = true;
  store._vm = new Vue({
    data: {
      $$state: state
    },
    computed: computed
  });
  Vue.config.silent = silent;

  // enable strict mode for new vm
  if (store.strict) {
    enableStrictMode(store);
  }

  if (oldVm) {
    if (hot) {
      // dispatch changes in all subscribed watchers
      // to force getter re-evaluation for hot reloading.
      store._withCommit(function () {
        oldVm._data.$$state = null;
      });
    }
    Vue.nextTick(function () { return oldVm.$destroy(); });
  }
}

function installModule (store, rootState, path, module, hot) {
  var isRoot = !path.length;
  var namespace = store._modules.getNamespace(path);

  // register in namespace map
  if (module.namespaced) {
    if (store._modulesNamespaceMap[namespace] && ("production" !== 'production')) {
      console.error(("[vuex] duplicate namespace " + namespace + " for the namespaced module " + (path.join('/'))));
    }
    store._modulesNamespaceMap[namespace] = module;
  }

  // set state
  if (!isRoot && !hot) {
    var parentState = getNestedState(rootState, path.slice(0, -1));
    var moduleName = path[path.length - 1];
    store._withCommit(function () {
      if ((false)) {}
      Vue.set(parentState, moduleName, module.state);
    });
  }

  var local = module.context = makeLocalContext(store, namespace, path);

  module.forEachMutation(function (mutation, key) {
    var namespacedType = namespace + key;
    registerMutation(store, namespacedType, mutation, local);
  });

  module.forEachAction(function (action, key) {
    var type = action.root ? key : namespace + key;
    var handler = action.handler || action;
    registerAction(store, type, handler, local);
  });

  module.forEachGetter(function (getter, key) {
    var namespacedType = namespace + key;
    registerGetter(store, namespacedType, getter, local);
  });

  module.forEachChild(function (child, key) {
    installModule(store, rootState, path.concat(key), child, hot);
  });
}

/**
 * make localized dispatch, commit, getters and state
 * if there is no namespace, just use root ones
 */
function makeLocalContext (store, namespace, path) {
  var noNamespace = namespace === '';

  var local = {
    dispatch: noNamespace ? store.dispatch : function (_type, _payload, _options) {
      var args = unifyObjectStyle(_type, _payload, _options);
      var payload = args.payload;
      var options = args.options;
      var type = args.type;

      if (!options || !options.root) {
        type = namespace + type;
        if (false) {}
      }

      return store.dispatch(type, payload)
    },

    commit: noNamespace ? store.commit : function (_type, _payload, _options) {
      var args = unifyObjectStyle(_type, _payload, _options);
      var payload = args.payload;
      var options = args.options;
      var type = args.type;

      if (!options || !options.root) {
        type = namespace + type;
        if (false) {}
      }

      store.commit(type, payload, options);
    }
  };

  // getters and state object must be gotten lazily
  // because they will be changed by vm update
  Object.defineProperties(local, {
    getters: {
      get: noNamespace
        ? function () { return store.getters; }
        : function () { return makeLocalGetters(store, namespace); }
    },
    state: {
      get: function () { return getNestedState(store.state, path); }
    }
  });

  return local
}

function makeLocalGetters (store, namespace) {
  if (!store._makeLocalGettersCache[namespace]) {
    var gettersProxy = {};
    var splitPos = namespace.length;
    Object.keys(store.getters).forEach(function (type) {
      // skip if the target getter is not match this namespace
      if (type.slice(0, splitPos) !== namespace) { return }

      // extract local getter type
      var localType = type.slice(splitPos);

      // Add a port to the getters proxy.
      // Define as getter property because
      // we do not want to evaluate the getters in this time.
      Object.defineProperty(gettersProxy, localType, {
        get: function () { return store.getters[type]; },
        enumerable: true
      });
    });
    store._makeLocalGettersCache[namespace] = gettersProxy;
  }

  return store._makeLocalGettersCache[namespace]
}

function registerMutation (store, type, handler, local) {
  var entry = store._mutations[type] || (store._mutations[type] = []);
  entry.push(function wrappedMutationHandler (payload) {
    handler.call(store, local.state, payload);
  });
}

function registerAction (store, type, handler, local) {
  var entry = store._actions[type] || (store._actions[type] = []);
  entry.push(function wrappedActionHandler (payload) {
    var res = handler.call(store, {
      dispatch: local.dispatch,
      commit: local.commit,
      getters: local.getters,
      state: local.state,
      rootGetters: store.getters,
      rootState: store.state
    }, payload);
    if (!isPromise(res)) {
      res = Promise.resolve(res);
    }
    if (store._devtoolHook) {
      return res.catch(function (err) {
        store._devtoolHook.emit('vuex:error', err);
        throw err
      })
    } else {
      return res
    }
  });
}

function registerGetter (store, type, rawGetter, local) {
  if (store._wrappedGetters[type]) {
    if ((false)) {}
    return
  }
  store._wrappedGetters[type] = function wrappedGetter (store) {
    return rawGetter(
      local.state, // local state
      local.getters, // local getters
      store.state, // root state
      store.getters // root getters
    )
  };
}

function enableStrictMode (store) {
  store._vm.$watch(function () { return this._data.$$state }, function () {
    if ((false)) {}
  }, { deep: true, sync: true });
}

function getNestedState (state, path) {
  return path.reduce(function (state, key) { return state[key]; }, state)
}

function unifyObjectStyle (type, payload, options) {
  if (isObject(type) && type.type) {
    options = payload;
    payload = type;
    type = type.type;
  }

  if ((false)) {}

  return { type: type, payload: payload, options: options }
}

function install (_Vue) {
  if (Vue && _Vue === Vue) {
    if ((false)) {}
    return
  }
  Vue = _Vue;
  applyMixin(Vue);
}

/**
 * Reduce the code which written in Vue.js for getting the state.
 * @param {String} [namespace] - Module's namespace
 * @param {Object|Array} states # Object's item can be a function which accept state and getters for param, you can do something for state and getters in it.
 * @param {Object}
 */
var mapState = normalizeNamespace(function (namespace, states) {
  var res = {};
  if (false) {}
  normalizeMap(states).forEach(function (ref) {
    var key = ref.key;
    var val = ref.val;

    res[key] = function mappedState () {
      var state = this.$store.state;
      var getters = this.$store.getters;
      if (namespace) {
        var module = getModuleByNamespace(this.$store, 'mapState', namespace);
        if (!module) {
          return
        }
        state = module.context.state;
        getters = module.context.getters;
      }
      return typeof val === 'function'
        ? val.call(this, state, getters)
        : state[val]
    };
    // mark vuex getter for devtools
    res[key].vuex = true;
  });
  return res
});

/**
 * Reduce the code which written in Vue.js for committing the mutation
 * @param {String} [namespace] - Module's namespace
 * @param {Object|Array} mutations # Object's item can be a function which accept `commit` function as the first param, it can accept another params. You can commit mutation and do any other things in this function. specially, You need to pass anthor params from the mapped function.
 * @return {Object}
 */
var mapMutations = normalizeNamespace(function (namespace, mutations) {
  var res = {};
  if (false) {}
  normalizeMap(mutations).forEach(function (ref) {
    var key = ref.key;
    var val = ref.val;

    res[key] = function mappedMutation () {
      var args = [], len = arguments.length;
      while ( len-- ) args[ len ] = arguments[ len ];

      // Get the commit method from store
      var commit = this.$store.commit;
      if (namespace) {
        var module = getModuleByNamespace(this.$store, 'mapMutations', namespace);
        if (!module) {
          return
        }
        commit = module.context.commit;
      }
      return typeof val === 'function'
        ? val.apply(this, [commit].concat(args))
        : commit.apply(this.$store, [val].concat(args))
    };
  });
  return res
});

/**
 * Reduce the code which written in Vue.js for getting the getters
 * @param {String} [namespace] - Module's namespace
 * @param {Object|Array} getters
 * @return {Object}
 */
var mapGetters = normalizeNamespace(function (namespace, getters) {
  var res = {};
  if (false) {}
  normalizeMap(getters).forEach(function (ref) {
    var key = ref.key;
    var val = ref.val;

    // The namespace has been mutated by normalizeNamespace
    val = namespace + val;
    res[key] = function mappedGetter () {
      if (namespace && !getModuleByNamespace(this.$store, 'mapGetters', namespace)) {
        return
      }
      if (false) {}
      return this.$store.getters[val]
    };
    // mark vuex getter for devtools
    res[key].vuex = true;
  });
  return res
});

/**
 * Reduce the code which written in Vue.js for dispatch the action
 * @param {String} [namespace] - Module's namespace
 * @param {Object|Array} actions # Object's item can be a function which accept `dispatch` function as the first param, it can accept anthor params. You can dispatch action and do any other things in this function. specially, You need to pass anthor params from the mapped function.
 * @return {Object}
 */
var mapActions = normalizeNamespace(function (namespace, actions) {
  var res = {};
  if (false) {}
  normalizeMap(actions).forEach(function (ref) {
    var key = ref.key;
    var val = ref.val;

    res[key] = function mappedAction () {
      var args = [], len = arguments.length;
      while ( len-- ) args[ len ] = arguments[ len ];

      // get dispatch function from store
      var dispatch = this.$store.dispatch;
      if (namespace) {
        var module = getModuleByNamespace(this.$store, 'mapActions', namespace);
        if (!module) {
          return
        }
        dispatch = module.context.dispatch;
      }
      return typeof val === 'function'
        ? val.apply(this, [dispatch].concat(args))
        : dispatch.apply(this.$store, [val].concat(args))
    };
  });
  return res
});

/**
 * Rebinding namespace param for mapXXX function in special scoped, and return them by simple object
 * @param {String} namespace
 * @return {Object}
 */
var createNamespacedHelpers = function (namespace) { return ({
  mapState: mapState.bind(null, namespace),
  mapGetters: mapGetters.bind(null, namespace),
  mapMutations: mapMutations.bind(null, namespace),
  mapActions: mapActions.bind(null, namespace)
}); };

/**
 * Normalize the map
 * normalizeMap([1, 2, 3]) => [ { key: 1, val: 1 }, { key: 2, val: 2 }, { key: 3, val: 3 } ]
 * normalizeMap({a: 1, b: 2, c: 3}) => [ { key: 'a', val: 1 }, { key: 'b', val: 2 }, { key: 'c', val: 3 } ]
 * @param {Array|Object} map
 * @return {Object}
 */
function normalizeMap (map) {
  if (!isValidMap(map)) {
    return []
  }
  return Array.isArray(map)
    ? map.map(function (key) { return ({ key: key, val: key }); })
    : Object.keys(map).map(function (key) { return ({ key: key, val: map[key] }); })
}

/**
 * Validate whether given map is valid or not
 * @param {*} map
 * @return {Boolean}
 */
function isValidMap (map) {
  return Array.isArray(map) || isObject(map)
}

/**
 * Return a function expect two param contains namespace and map. it will normalize the namespace and then the param's function will handle the new namespace and the map.
 * @param {Function} fn
 * @return {Function}
 */
function normalizeNamespace (fn) {
  return function (namespace, map) {
    if (typeof namespace !== 'string') {
      map = namespace;
      namespace = '';
    } else if (namespace.charAt(namespace.length - 1) !== '/') {
      namespace += '/';
    }
    return fn(namespace, map)
  }
}

/**
 * Search a special module from store by namespace. if module not exist, print error message.
 * @param {Object} store
 * @param {String} helper
 * @param {String} namespace
 * @return {Object}
 */
function getModuleByNamespace (store, helper, namespace) {
  var module = store._modulesNamespaceMap[namespace];
  if (false) {}
  return module
}

// Credits: borrowed code from fcomb/redux-logger

function createLogger (ref) {
  if ( ref === void 0 ) ref = {};
  var collapsed = ref.collapsed; if ( collapsed === void 0 ) collapsed = true;
  var filter = ref.filter; if ( filter === void 0 ) filter = function (mutation, stateBefore, stateAfter) { return true; };
  var transformer = ref.transformer; if ( transformer === void 0 ) transformer = function (state) { return state; };
  var mutationTransformer = ref.mutationTransformer; if ( mutationTransformer === void 0 ) mutationTransformer = function (mut) { return mut; };
  var actionFilter = ref.actionFilter; if ( actionFilter === void 0 ) actionFilter = function (action, state) { return true; };
  var actionTransformer = ref.actionTransformer; if ( actionTransformer === void 0 ) actionTransformer = function (act) { return act; };
  var logMutations = ref.logMutations; if ( logMutations === void 0 ) logMutations = true;
  var logActions = ref.logActions; if ( logActions === void 0 ) logActions = true;
  var logger = ref.logger; if ( logger === void 0 ) logger = console;

  return function (store) {
    var prevState = deepCopy(store.state);

    if (typeof logger === 'undefined') {
      return
    }

    if (logMutations) {
      store.subscribe(function (mutation, state) {
        var nextState = deepCopy(state);

        if (filter(mutation, prevState, nextState)) {
          var formattedTime = getFormattedTime();
          var formattedMutation = mutationTransformer(mutation);
          var message = "mutation " + (mutation.type) + formattedTime;

          startMessage(logger, message, collapsed);
          logger.log('%c prev state', 'color: #9E9E9E; font-weight: bold', transformer(prevState));
          logger.log('%c mutation', 'color: #03A9F4; font-weight: bold', formattedMutation);
          logger.log('%c next state', 'color: #4CAF50; font-weight: bold', transformer(nextState));
          endMessage(logger);
        }

        prevState = nextState;
      });
    }

    if (logActions) {
      store.subscribeAction(function (action, state) {
        if (actionFilter(action, state)) {
          var formattedTime = getFormattedTime();
          var formattedAction = actionTransformer(action);
          var message = "action " + (action.type) + formattedTime;

          startMessage(logger, message, collapsed);
          logger.log('%c action', 'color: #03A9F4; font-weight: bold', formattedAction);
          endMessage(logger);
        }
      });
    }
  }
}

function startMessage (logger, message, collapsed) {
  var startMessage = collapsed
    ? logger.groupCollapsed
    : logger.group;

  // render
  try {
    startMessage.call(logger, message);
  } catch (e) {
    logger.log(message);
  }
}

function endMessage (logger) {
  try {
    logger.groupEnd();
  } catch (e) {
    logger.log('—— log end ——');
  }
}

function getFormattedTime () {
  var time = new Date();
  return (" @ " + (pad(time.getHours(), 2)) + ":" + (pad(time.getMinutes(), 2)) + ":" + (pad(time.getSeconds(), 2)) + "." + (pad(time.getMilliseconds(), 3)))
}

function repeat (str, times) {
  return (new Array(times + 1)).join(str)
}

function pad (num, maxLength) {
  return repeat('0', maxLength - num.toString().length) + num
}

var index = {
  Store: Store,
  install: install,
  version: '3.6.2',
  mapState: mapState,
  mapMutations: mapMutations,
  mapGetters: mapGetters,
  mapActions: mapActions,
  createNamespacedHelpers: createNamespacedHelpers,
  createLogger: createLogger
};

/* unused harmony default export */ var _unused_webpack_default_export = (index);


/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__("c8ba")))

/***/ }),

/***/ "30ba":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * An implementation of the RDF Dataset Normalization specification.
 * This library works in the browser and node.js.
 *
 * BSD 3-Clause License
 * Copyright (c) 2016-2021 Digital Bazaar, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the Digital Bazaar, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


const URDNA2015 = __webpack_require__("ae29");
const URGNA2012 = __webpack_require__("458f");
const URDNA2015Sync = __webpack_require__("4284");
const URGNA2012Sync = __webpack_require__("a757");

// optional native support
let rdfCanonizeNative;
try {
  rdfCanonizeNative = __webpack_require__(0);
} catch(e) {}

const api = {};
module.exports = api;

// expose helpers
api.NQuads = __webpack_require__("65e1");
api.IdentifierIssuer = __webpack_require__("8acc");

/**
 * Get or set native API.
 *
 * @param api the native API.
 *
 * @return the currently set native API.
 */
api._rdfCanonizeNative = function(api) {
  if(api) {
    rdfCanonizeNative = api;
  }
  return rdfCanonizeNative;
};

/**
 * Asynchronously canonizes an RDF dataset.
 *
 * @param dataset the dataset to canonize.
 * @param options the options to use:
 *          algorithm the canonicalization algorithm to use, `URDNA2015` or
 *            `URGNA2012`.
 *          [useNative] use native implementation (default: false).
 *
 * @return a Promise that resolves to the canonicalized RDF Dataset.
 */
api.canonize = async function(dataset, options) {
  // back-compat with legacy dataset
  if(!Array.isArray(dataset)) {
    dataset = api.NQuads.legacyDatasetToQuads(dataset);
  }

  if(options.useNative) {
    if(!rdfCanonizeNative) {
      throw new Error('rdf-canonize-native not available');
    }
    // TODO: convert native algorithm to Promise-based async
    return new Promise((resolve, reject) =>
      rdfCanonizeNative.canonize(dataset, options, (err, canonical) =>
        err ? reject(err) : resolve(canonical)));
  }

  if(options.algorithm === 'URDNA2015') {
    return new URDNA2015(options).main(dataset);
  }
  if(options.algorithm === 'URGNA2012') {
    return new URGNA2012(options).main(dataset);
  }
  if(!('algorithm' in options)) {
    throw new Error('No RDF Dataset Canonicalization algorithm specified.');
  }
  throw new Error(
    'Invalid RDF Dataset Canonicalization algorithm: ' + options.algorithm);
};

/**
 * This method is no longer available in the public API, it is for testing
 * only. It synchronously canonizes an RDF dataset and does not work in the
 * browser.
 *
 * @param dataset the dataset to canonize.
 * @param options the options to use:
 *          algorithm the canonicalization algorithm to use, `URDNA2015` or
 *            `URGNA2012`.
 *          [useNative] use native implementation (default: false).
 *
 * @return the RDF dataset in canonical form.
 */
api._canonizeSync = function(dataset, options) {
  // back-compat with legacy dataset
  if(!Array.isArray(dataset)) {
    dataset = api.NQuads.legacyDatasetToQuads(dataset);
  }

  if(options.useNative) {
    if(rdfCanonizeNative) {
      return rdfCanonizeNative.canonizeSync(dataset, options);
    }
    throw new Error('rdf-canonize-native not available');
  }
  if(options.algorithm === 'URDNA2015') {
    return new URDNA2015Sync(options).main(dataset);
  }
  if(options.algorithm === 'URGNA2012') {
    return new URGNA2012Sync(options).main(dataset);
  }
  if(!('algorithm' in options)) {
    throw new Error('No RDF Dataset Canonicalization algorithm specified.');
  }
  throw new Error(
    'Invalid RDF Dataset Canonicalization algorithm: ' + options.algorithm);
};


/***/ }),

/***/ "342f":
/***/ (function(module, exports, __webpack_require__) {

var getBuiltIn = __webpack_require__("d066");

module.exports = getBuiltIn('navigator', 'userAgent') || '';


/***/ }),

/***/ "35a1":
/***/ (function(module, exports, __webpack_require__) {

var classof = __webpack_require__("f5df");
var Iterators = __webpack_require__("3f8c");
var wellKnownSymbol = __webpack_require__("b622");

var ITERATOR = wellKnownSymbol('iterator');

module.exports = function (it) {
  if (it != undefined) return it[ITERATOR]
    || it['@@iterator']
    || Iterators[classof(it)];
};


/***/ }),

/***/ "37e8":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var definePropertyModule = __webpack_require__("9bf2");
var anObject = __webpack_require__("825a");
var objectKeys = __webpack_require__("df75");

// `Object.defineProperties` method
// https://tc39.es/ecma262/#sec-object.defineproperties
// eslint-disable-next-line es/no-object-defineproperties -- safe
module.exports = DESCRIPTORS ? Object.defineProperties : function defineProperties(O, Properties) {
  anObject(O);
  var keys = objectKeys(Properties);
  var length = keys.length;
  var index = 0;
  var key;
  while (length > index) definePropertyModule.f(O, key = keys[index++], Properties[key]);
  return O;
};


/***/ }),

/***/ "3bbe":
/***/ (function(module, exports, __webpack_require__) {

var isObject = __webpack_require__("861d");

module.exports = function (it) {
  if (!isObject(it) && it !== null) {
    throw TypeError("Can't set " + String(it) + ' as a prototype');
  } return it;
};


/***/ }),

/***/ "3f8c":
/***/ (function(module, exports) {

module.exports = {};


/***/ }),

/***/ "40ce":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017-2019 Digital Bazaar, Inc. All rights reserved.
 */


module.exports = class RequestQueue {
  /**
   * Creates a simple queue for requesting documents.
   */
  constructor() {
    this._requests = {};
  }

  wrapLoader(loader) {
    const self = this;
    self._loader = loader;
    return function(/* url */) {
      return self.add.apply(self, arguments);
    };
  }

  async add(url) {
    let promise = this._requests[url];
    if(promise) {
      // URL already queued, wait for it to load
      return Promise.resolve(promise);
    }

    // queue URL and load it
    promise = this._requests[url] = this._loader(url);

    try {
      return await promise;
    } finally {
      delete this._requests[url];
    }
  }
};


/***/ }),

/***/ "4142":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const {
  isSubjectReference: _isSubjectReference
} = __webpack_require__("c92f");

const {
  createMergedNodeMap: _createMergedNodeMap
} = __webpack_require__("796c");

const api = {};
module.exports = api;

/**
 * Performs JSON-LD flattening.
 *
 * @param input the expanded JSON-LD to flatten.
 *
 * @return the flattened output.
 */
api.flatten = input => {
  const defaultGraph = _createMergedNodeMap(input);

  // produce flattened output
  const flattened = [];
  const keys = Object.keys(defaultGraph).sort();
  for(let ki = 0; ki < keys.length; ++ki) {
    const node = defaultGraph[keys[ki]];
    // only add full subjects to top-level
    if(!_isSubjectReference(node)) {
      flattened.push(node);
    }
  }
  return flattened;
};


/***/ }),

/***/ "4284":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


const IdentifierIssuer = __webpack_require__("8acc");
const MessageDigest = __webpack_require__("5a76");
const Permuter = __webpack_require__("c79e");
const NQuads = __webpack_require__("65e1");

module.exports = class URDNA2015Sync {
  constructor() {
    this.name = 'URDNA2015';
    this.blankNodeInfo = new Map();
    this.canonicalIssuer = new IdentifierIssuer('_:c14n');
    this.hashAlgorithm = 'sha256';
    this.quads = null;
  }

  // 4.4) Normalization Algorithm
  main(dataset) {
    this.quads = dataset;

    // 1) Create the normalization state.
    // 2) For every quad in input dataset:
    for(const quad of dataset) {
      // 2.1) For each blank node that occurs in the quad, add a reference
      // to the quad using the blank node identifier in the blank node to
      // quads map, creating a new entry if necessary.
      this._addBlankNodeQuadInfo({quad, component: quad.subject});
      this._addBlankNodeQuadInfo({quad, component: quad.object});
      this._addBlankNodeQuadInfo({quad, component: quad.graph});
    }

    // 3) Create a list of non-normalized blank node identifiers
    // non-normalized identifiers and populate it using the keys from the
    // blank node to quads map.
    // Note: We use a map here and it was generated during step 2.

    // 4) `simple` flag is skipped -- loop is optimized away. This optimization
    // is permitted because there was a typo in the hash first degree quads
    // algorithm in the URDNA2015 spec that was implemented widely making it
    // such that it could not be fixed; the result was that the loop only
    // needs to be run once and the first degree quad hashes will never change.
    // 5.1-5.2 are skipped; first degree quad hashes are generated just once
    // for all non-normalized blank nodes.

    // 5.3) For each blank node identifier identifier in non-normalized
    // identifiers:
    const hashToBlankNodes = new Map();
    const nonNormalized = [...this.blankNodeInfo.keys()];
    for(const id of nonNormalized) {
      // steps 5.3.1 and 5.3.2:
      this._hashAndTrackBlankNode({id, hashToBlankNodes});
    }

    // 5.4) For each hash to identifier list mapping in hash to blank
    // nodes map, lexicographically-sorted by hash:
    const hashes = [...hashToBlankNodes.keys()].sort();
    // optimize away second sort, gather non-unique hashes in order as we go
    const nonUnique = [];
    for(const hash of hashes) {
      // 5.4.1) If the length of identifier list is greater than 1,
      // continue to the next mapping.
      const idList = hashToBlankNodes.get(hash);
      if(idList.length > 1) {
        nonUnique.push(idList);
        continue;
      }

      // 5.4.2) Use the Issue Identifier algorithm, passing canonical
      // issuer and the single blank node identifier in identifier
      // list, identifier, to issue a canonical replacement identifier
      // for identifier.
      const id = idList[0];
      this.canonicalIssuer.getId(id);

      // Note: These steps are skipped, optimized away since the loop
      // only needs to be run once.
      // 5.4.3) Remove identifier from non-normalized identifiers.
      // 5.4.4) Remove hash from the hash to blank nodes map.
      // 5.4.5) Set simple to true.
    }

    // 6) For each hash to identifier list mapping in hash to blank nodes map,
    // lexicographically-sorted by hash:
    // Note: sort optimized away, use `nonUnique`.
    for(const idList of nonUnique) {
      // 6.1) Create hash path list where each item will be a result of
      // running the Hash N-Degree Quads algorithm.
      const hashPathList = [];

      // 6.2) For each blank node identifier identifier in identifier list:
      for(const id of idList) {
        // 6.2.1) If a canonical identifier has already been issued for
        // identifier, continue to the next identifier.
        if(this.canonicalIssuer.hasId(id)) {
          continue;
        }

        // 6.2.2) Create temporary issuer, an identifier issuer
        // initialized with the prefix _:b.
        const issuer = new IdentifierIssuer('_:b');

        // 6.2.3) Use the Issue Identifier algorithm, passing temporary
        // issuer and identifier, to issue a new temporary blank node
        // identifier for identifier.
        issuer.getId(id);

        // 6.2.4) Run the Hash N-Degree Quads algorithm, passing
        // temporary issuer, and append the result to the hash path list.
        const result = this.hashNDegreeQuads(id, issuer);
        hashPathList.push(result);
      }

      // 6.3) For each result in the hash path list,
      // lexicographically-sorted by the hash in result:
      hashPathList.sort(_stringHashCompare);
      for(const result of hashPathList) {
        // 6.3.1) For each blank node identifier, existing identifier,
        // that was issued a temporary identifier by identifier issuer
        // in result, issue a canonical identifier, in the same order,
        // using the Issue Identifier algorithm, passing canonical
        // issuer and existing identifier.
        const oldIds = result.issuer.getOldIds();
        for(const id of oldIds) {
          this.canonicalIssuer.getId(id);
        }
      }
    }

    /* Note: At this point all blank nodes in the set of RDF quads have been
    assigned canonical identifiers, which have been stored in the canonical
    issuer. Here each quad is updated by assigning each of its blank nodes
    its new identifier. */

    // 7) For each quad, quad, in input dataset:
    const normalized = [];
    for(const quad of this.quads) {
      // 7.1) Create a copy, quad copy, of quad and replace any existing
      // blank node identifiers using the canonical identifiers
      // previously issued by canonical issuer.
      // Note: We optimize with shallow copies here.
      const q = {...quad};
      q.subject = this._useCanonicalId({component: q.subject});
      q.object = this._useCanonicalId({component: q.object});
      q.graph = this._useCanonicalId({component: q.graph});
      // 7.2) Add quad copy to the normalized dataset.
      normalized.push(NQuads.serializeQuad(q));
    }

    // sort normalized output
    normalized.sort();

    // 8) Return the normalized dataset.
    return normalized.join('');
  }

  // 4.6) Hash First Degree Quads
  hashFirstDegreeQuads(id) {
    // 1) Initialize nquads to an empty list. It will be used to store quads in
    // N-Quads format.
    const nquads = [];

    // 2) Get the list of quads `quads` associated with the reference blank node
    // identifier in the blank node to quads map.
    const info = this.blankNodeInfo.get(id);
    const quads = info.quads;

    // 3) For each quad `quad` in `quads`:
    for(const quad of quads) {
      // 3.1) Serialize the quad in N-Quads format with the following special
      // rule:

      // 3.1.1) If any component in quad is an blank node, then serialize it
      // using a special identifier as follows:
      const copy = {
        subject: null, predicate: quad.predicate, object: null, graph: null
      };
      // 3.1.2) If the blank node's existing blank node identifier matches
      // the reference blank node identifier then use the blank node
      // identifier _:a, otherwise, use the blank node identifier _:z.
      copy.subject = this.modifyFirstDegreeComponent(
        id, quad.subject, 'subject');
      copy.object = this.modifyFirstDegreeComponent(
        id, quad.object, 'object');
      copy.graph = this.modifyFirstDegreeComponent(
        id, quad.graph, 'graph');
      nquads.push(NQuads.serializeQuad(copy));
    }

    // 4) Sort nquads in lexicographical order.
    nquads.sort();

    // 5) Return the hash that results from passing the sorted, joined nquads
    // through the hash algorithm.
    const md = new MessageDigest(this.hashAlgorithm);
    for(const nquad of nquads) {
      md.update(nquad);
    }
    info.hash = md.digest();
    return info.hash;
  }

  // 4.7) Hash Related Blank Node
  hashRelatedBlankNode(related, quad, issuer, position) {
    // 1) Set the identifier to use for related, preferring first the canonical
    // identifier for related if issued, second the identifier issued by issuer
    // if issued, and last, if necessary, the result of the Hash First Degree
    // Quads algorithm, passing related.
    let id;
    if(this.canonicalIssuer.hasId(related)) {
      id = this.canonicalIssuer.getId(related);
    } else if(issuer.hasId(related)) {
      id = issuer.getId(related);
    } else {
      id = this.blankNodeInfo.get(related).hash;
    }

    // 2) Initialize a string input to the value of position.
    // Note: We use a hash object instead.
    const md = new MessageDigest(this.hashAlgorithm);
    md.update(position);

    // 3) If position is not g, append <, the value of the predicate in quad,
    // and > to input.
    if(position !== 'g') {
      md.update(this.getRelatedPredicate(quad));
    }

    // 4) Append identifier to input.
    md.update(id);

    // 5) Return the hash that results from passing input through the hash
    // algorithm.
    return md.digest();
  }

  // 4.8) Hash N-Degree Quads
  hashNDegreeQuads(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    // Note: 2) and 3) handled within `createHashToRelated`
    const md = new MessageDigest(this.hashAlgorithm);
    const hashToRelated = this.createHashToRelated(id, issuer);

    // 4) Create an empty string, data to hash.
    // Note: We created a hash object `md` above instead.

    // 5) For each related hash to blank node list mapping in hash to related
    // blank nodes map, sorted lexicographically by related hash:
    const hashes = [...hashToRelated.keys()].sort();
    for(const hash of hashes) {
      // 5.1) Append the related hash to the data to hash.
      md.update(hash);

      // 5.2) Create a string chosen path.
      let chosenPath = '';

      // 5.3) Create an unset chosen issuer variable.
      let chosenIssuer;

      // 5.4) For each permutation of blank node list:
      const permuter = new Permuter(hashToRelated.get(hash));
      while(permuter.hasNext()) {
        const permutation = permuter.next();

        // 5.4.1) Create a copy of issuer, issuer copy.
        let issuerCopy = issuer.clone();

        // 5.4.2) Create a string path.
        let path = '';

        // 5.4.3) Create a recursion list, to store blank node identifiers
        // that must be recursively processed by this algorithm.
        const recursionList = [];

        // 5.4.4) For each related in permutation:
        let nextPermutation = false;
        for(const related of permutation) {
          // 5.4.4.1) If a canonical identifier has been issued for
          // related, append it to path.
          if(this.canonicalIssuer.hasId(related)) {
            path += this.canonicalIssuer.getId(related);
          } else {
            // 5.4.4.2) Otherwise:
            // 5.4.4.2.1) If issuer copy has not issued an identifier for
            // related, append related to recursion list.
            if(!issuerCopy.hasId(related)) {
              recursionList.push(related);
            }
            // 5.4.4.2.2) Use the Issue Identifier algorithm, passing
            // issuer copy and related and append the result to path.
            path += issuerCopy.getId(related);
          }

          // 5.4.4.3) If chosen path is not empty and the length of path
          // is greater than or equal to the length of chosen path and
          // path is lexicographically greater than chosen path, then
          // skip to the next permutation.
          // Note: Comparing path length to chosen path length can be optimized
          // away; only compare lexicographically.
          if(chosenPath.length !== 0 && path > chosenPath) {
            nextPermutation = true;
            break;
          }
        }

        if(nextPermutation) {
          continue;
        }

        // 5.4.5) For each related in recursion list:
        for(const related of recursionList) {
          // 5.4.5.1) Set result to the result of recursively executing
          // the Hash N-Degree Quads algorithm, passing related for
          // identifier and issuer copy for path identifier issuer.
          const result = this.hashNDegreeQuads(related, issuerCopy);

          // 5.4.5.2) Use the Issue Identifier algorithm, passing issuer
          // copy and related and append the result to path.
          path += issuerCopy.getId(related);

          // 5.4.5.3) Append <, the hash in result, and > to path.
          path += `<${result.hash}>`;

          // 5.4.5.4) Set issuer copy to the identifier issuer in
          // result.
          issuerCopy = result.issuer;

          // 5.4.5.5) If chosen path is not empty and the length of path
          // is greater than or equal to the length of chosen path and
          // path is lexicographically greater than chosen path, then
          // skip to the next permutation.
          // Note: Comparing path length to chosen path length can be optimized
          // away; only compare lexicographically.
          if(chosenPath.length !== 0 && path > chosenPath) {
            nextPermutation = true;
            break;
          }
        }

        if(nextPermutation) {
          continue;
        }

        // 5.4.6) If chosen path is empty or path is lexicographically
        // less than chosen path, set chosen path to path and chosen
        // issuer to issuer copy.
        if(chosenPath.length === 0 || path < chosenPath) {
          chosenPath = path;
          chosenIssuer = issuerCopy;
        }
      }

      // 5.5) Append chosen path to data to hash.
      md.update(chosenPath);

      // 5.6) Replace issuer, by reference, with chosen issuer.
      issuer = chosenIssuer;
    }

    // 6) Return issuer and the hash that results from passing data to hash
    // through the hash algorithm.
    return {hash: md.digest(), issuer};
  }

  // helper for modifying component during Hash First Degree Quads
  modifyFirstDegreeComponent(id, component) {
    if(component.termType !== 'BlankNode') {
      return component;
    }
    /* Note: A mistake in the URDNA2015 spec that made its way into
    implementations (and therefore must stay to avoid interop breakage)
    resulted in an assigned canonical ID, if available for
    `component.value`, not being used in place of `_:a`/`_:z`, so
    we don't use it here. */
    return {
      termType: 'BlankNode',
      value: component.value === id ? '_:a' : '_:z'
    };
  }

  // helper for getting a related predicate
  getRelatedPredicate(quad) {
    return `<${quad.predicate.value}>`;
  }

  // helper for creating hash to related blank nodes map
  createHashToRelated(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    const hashToRelated = new Map();

    // 2) Get a reference, quads, to the list of quads in the blank node to
    // quads map for the key identifier.
    const quads = this.blankNodeInfo.get(id).quads;

    // 3) For each quad in quads:
    for(const quad of quads) {
      // 3.1) For each component in quad, if component is the subject, object,
      // or graph name and it is a blank node that is not identified by
      // identifier:
      // steps 3.1.1 and 3.1.2 occur in helpers:
      this._addRelatedBlankNodeHash({
        quad, component: quad.subject, position: 's',
        id, issuer, hashToRelated
      });
      this._addRelatedBlankNodeHash({
        quad, component: quad.object, position: 'o',
        id, issuer, hashToRelated
      });
      this._addRelatedBlankNodeHash({
        quad, component: quad.graph, position: 'g',
        id, issuer, hashToRelated
      });
    }

    return hashToRelated;
  }

  _hashAndTrackBlankNode({id, hashToBlankNodes}) {
    // 5.3.1) Create a hash, hash, according to the Hash First Degree
    // Quads algorithm.
    const hash = this.hashFirstDegreeQuads(id);

    // 5.3.2) Add hash and identifier to hash to blank nodes map,
    // creating a new entry if necessary.
    const idList = hashToBlankNodes.get(hash);
    if(!idList) {
      hashToBlankNodes.set(hash, [id]);
    } else {
      idList.push(id);
    }
  }

  _addBlankNodeQuadInfo({quad, component}) {
    if(component.termType !== 'BlankNode') {
      return;
    }
    const id = component.value;
    const info = this.blankNodeInfo.get(id);
    if(info) {
      info.quads.add(quad);
    } else {
      this.blankNodeInfo.set(id, {quads: new Set([quad]), hash: null});
    }
  }

  _addRelatedBlankNodeHash(
    {quad, component, position, id, issuer, hashToRelated}) {
    if(!(component.termType === 'BlankNode' && component.value !== id)) {
      return;
    }
    // 3.1.1) Set hash to the result of the Hash Related Blank Node
    // algorithm, passing the blank node identifier for component as
    // related, quad, path identifier issuer as issuer, and position as
    // either s, o, or g based on whether component is a subject, object,
    // graph name, respectively.
    const related = component.value;
    const hash = this.hashRelatedBlankNode(related, quad, issuer, position);

    // 3.1.2) Add a mapping of hash to the blank node identifier for
    // component to hash to related blank nodes map, adding an entry as
    // necessary.
    const entries = hashToRelated.get(hash);
    if(entries) {
      entries.push(related);
    } else {
      hashToRelated.set(hash, [related]);
    }
  }

  _useCanonicalId({component}) {
    if(component.termType === 'BlankNode' &&
      !component.value.startsWith(this.canonicalIssuer.prefix)) {
      return {
        termType: 'BlankNode',
        value: this.canonicalIssuer.getId(component.value)
      };
    }
    return component;
  }
};

function _stringHashCompare(a, b) {
  return a.hash < b.hash ? -1 : a.hash > b.hash ? 1 : 0;
}


/***/ }),

/***/ "428f":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");

module.exports = global;


/***/ }),

/***/ "4362":
/***/ (function(module, exports, __webpack_require__) {

exports.nextTick = function nextTick(fn) {
    var args = Array.prototype.slice.call(arguments);
    args.shift();
    setTimeout(function () {
        fn.apply(null, args);
    }, 0);
};

exports.platform = exports.arch = 
exports.execPath = exports.title = 'browser';
exports.pid = 1;
exports.browser = true;
exports.env = {};
exports.argv = [];

exports.binding = function (name) {
	throw new Error('No such module. (Possibly not yet loaded)')
};

(function () {
    var cwd = '/';
    var path;
    exports.cwd = function () { return cwd };
    exports.chdir = function (dir) {
        if (!path) path = __webpack_require__("df7c");
        cwd = path.resolve(dir, cwd);
    };
})();

exports.exit = exports.kill = 
exports.umask = exports.dlopen = 
exports.uptime = exports.memoryUsage = 
exports.uvCounters = function() {};
exports.features = {};


/***/ }),

/***/ "44ad":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");
var classof = __webpack_require__("c6b6");

var split = ''.split;

// fallback for non-array-like ES3 and non-enumerable old V8 strings
module.exports = fails(function () {
  // throws an error in rhino, see https://github.com/mozilla/rhino/issues/346
  // eslint-disable-next-line no-prototype-builtins -- safe
  return !Object('z').propertyIsEnumerable(0);
}) ? function (it) {
  return classof(it) == 'String' ? split.call(it, '') : Object(it);
} : Object;


/***/ }),

/***/ "44d2":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");
var create = __webpack_require__("7c73");
var definePropertyModule = __webpack_require__("9bf2");

var UNSCOPABLES = wellKnownSymbol('unscopables');
var ArrayPrototype = Array.prototype;

// Array.prototype[@@unscopables]
// https://tc39.es/ecma262/#sec-array.prototype-@@unscopables
if (ArrayPrototype[UNSCOPABLES] == undefined) {
  definePropertyModule.f(ArrayPrototype, UNSCOPABLES, {
    configurable: true,
    value: create(null)
  });
}

// add a key to Array.prototype[@@unscopables]
module.exports = function (key) {
  ArrayPrototype[UNSCOPABLES][key] = true;
};


/***/ }),

/***/ "44de":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");

module.exports = function (a, b) {
  var console = global.console;
  if (console && console.error) {
    arguments.length === 1 ? console.error(a) : console.error(a, b);
  }
};


/***/ }),

/***/ "44e7":
/***/ (function(module, exports, __webpack_require__) {

var isObject = __webpack_require__("861d");
var classof = __webpack_require__("c6b6");
var wellKnownSymbol = __webpack_require__("b622");

var MATCH = wellKnownSymbol('match');

// `IsRegExp` abstract operation
// https://tc39.es/ecma262/#sec-isregexp
module.exports = function (it) {
  var isRegExp;
  return isObject(it) && ((isRegExp = it[MATCH]) !== undefined ? !!isRegExp : classof(it) == 'RegExp');
};


/***/ }),

/***/ "458f":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


const URDNA2015 = __webpack_require__("ae29");

module.exports = class URDNA2012 extends URDNA2015 {
  constructor() {
    super();
    this.name = 'URGNA2012';
    this.hashAlgorithm = 'sha1';
  }

  // helper for modifying component during Hash First Degree Quads
  modifyFirstDegreeComponent(id, component, key) {
    if(component.termType !== 'BlankNode') {
      return component;
    }
    if(key === 'graph') {
      return {
        termType: 'BlankNode',
        value: '_:g'
      };
    }
    return {
      termType: 'BlankNode',
      value: (component.value === id ? '_:a' : '_:z')
    };
  }

  // helper for getting a related predicate
  getRelatedPredicate(quad) {
    return quad.predicate.value;
  }

  // helper for creating hash to related blank nodes map
  async createHashToRelated(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    const hashToRelated = new Map();

    // 2) Get a reference, quads, to the list of quads in the blank node to
    // quads map for the key identifier.
    const quads = this.blankNodeInfo.get(id).quads;

    // 3) For each quad in quads:
    let i = 0;
    for(const quad of quads) {
      // 3.1) If the quad's subject is a blank node that does not match
      // identifier, set hash to the result of the Hash Related Blank Node
      // algorithm, passing the blank node identifier for subject as related,
      // quad, path identifier issuer as issuer, and p as position.
      let position;
      let related;
      if(quad.subject.termType === 'BlankNode' && quad.subject.value !== id) {
        related = quad.subject.value;
        position = 'p';
      } else if(
        quad.object.termType === 'BlankNode' && quad.object.value !== id) {
        // 3.2) Otherwise, if quad's object is a blank node that does not match
        // identifier, to the result of the Hash Related Blank Node algorithm,
        // passing the blank node identifier for object as related, quad, path
        // identifier issuer as issuer, and r as position.
        related = quad.object.value;
        position = 'r';
      } else {
        // 3.3) Otherwise, continue to the next quad.
        continue;
      }
      // Note: batch hashing related blank nodes 100 at a time
      if(++i % 100 === 0) {
        await this._yield();
      }
      // 3.4) Add a mapping of hash to the blank node identifier for the
      // component that matched (subject or object) to hash to related blank
      // nodes map, adding an entry as necessary.
      const hash = await this.hashRelatedBlankNode(
        related, quad, issuer, position);
      const entries = hashToRelated.get(hash);
      if(entries) {
        entries.push(related);
      } else {
        hashToRelated.set(hash, [related]);
      }
    }

    return hashToRelated;
  }
};


/***/ }),

/***/ "46bd":
/***/ (function(module, exports, __webpack_require__) {

/**
 * A JavaScript implementation of the JSON-LD API.
 *
 * @author Dave Longley
 *
 * @license BSD 3-Clause License
 * Copyright (c) 2011-2019 Digital Bazaar, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the Digital Bazaar, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
const canonize = __webpack_require__("4b45");
const platform = __webpack_require__("ee17");
const util = __webpack_require__("89ae");
const ContextResolver = __webpack_require__("0d88");
const IdentifierIssuer = util.IdentifierIssuer;
const JsonLdError = __webpack_require__("5950");
const LRU = __webpack_require__("f32c");
const NQuads = __webpack_require__("65c2");

const {expand: _expand} = __webpack_require__("50ca");
const {flatten: _flatten} = __webpack_require__("4142");
const {fromRDF: _fromRDF} = __webpack_require__("6f5c");
const {toRDF: _toRDF} = __webpack_require__("7646");

const {
  frameMergedOrDefault: _frameMergedOrDefault,
  cleanupNull: _cleanupNull
} = __webpack_require__("e6ed");

const {
  isArray: _isArray,
  isObject: _isObject,
  isString: _isString
} = __webpack_require__("68fa");

const {
  isSubjectReference: _isSubjectReference,
} = __webpack_require__("c92f");

const {
  expandIri: _expandIri,
  getInitialContext: _getInitialContext,
  process: _processContext,
  processingMode: _processingMode
} = __webpack_require__("d1e7");

const {
  compact: _compact,
  compactIri: _compactIri
} = __webpack_require__("4d68");

const {
  createNodeMap: _createNodeMap,
  createMergedNodeMap: _createMergedNodeMap,
  mergeNodeMaps: _mergeNodeMaps
} = __webpack_require__("796c");

/* eslint-disable indent */
// attaches jsonld API to the given object
const wrapper = function(jsonld) {

/** Registered RDF dataset parsers hashed by content-type. */
const _rdfParsers = {};

// resolved context cache
// TODO: consider basing max on context size rather than number
const RESOLVED_CONTEXT_CACHE_MAX_SIZE = 100;
const _resolvedContextCache = new LRU({max: RESOLVED_CONTEXT_CACHE_MAX_SIZE});

/* Core API */

/**
 * Performs JSON-LD compaction.
 *
 * @param input the JSON-LD input to compact.
 * @param ctx the context to compact with.
 * @param [options] options to use:
 *          [base] the base IRI to use.
 *          [compactArrays] true to compact arrays to single values when
 *            appropriate, false not to (default: true).
 *          [compactToRelative] true to compact IRIs to be relative to document
 *            base, false to keep absolute (default: true)
 *          [graph] true to always output a top-level graph (default: false).
 *          [expandContext] a context to expand with.
 *          [skipExpansion] true to assume the input is expanded and skip
 *            expansion, false not to, defaults to false.
 *          [documentLoader(url, options)] the document loader.
 *          [expansionMap(info)] a function that can be used to custom map
 *            unmappable values (or to throw an error when they are detected);
 *            if this function returns `undefined` then the default behavior
 *            will be used.
 *          [framing] true if compaction is occuring during a framing operation.
 *          [compactionMap(info)] a function that can be used to custom map
 *            unmappable values (or to throw an error when they are detected);
 *            if this function returns `undefined` then the default behavior
 *            will be used.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the compacted output.
 */
jsonld.compact = async function(input, ctx, options) {
  if(arguments.length < 2) {
    throw new TypeError('Could not compact, too few arguments.');
  }

  if(ctx === null) {
    throw new JsonLdError(
      'The compaction context must not be null.',
      'jsonld.CompactError', {code: 'invalid local context'});
  }

  // nothing to compact
  if(input === null) {
    return null;
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    compactArrays: true,
    compactToRelative: true,
    graph: false,
    skipExpansion: false,
    link: false,
    issuer: new IdentifierIssuer('_:b'),
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });
  if(options.link) {
    // force skip expansion when linking, "link" is not part of the public
    // API, it should only be called from framing
    options.skipExpansion = true;
  }
  if(!options.compactToRelative) {
    delete options.base;
  }

  // expand input
  let expanded;
  if(options.skipExpansion) {
    expanded = input;
  } else {
    expanded = await jsonld.expand(input, options);
  }

  // process context
  const activeCtx = await jsonld.processContext(
    _getInitialContext(options), ctx, options);

  // do compaction
  let compacted = await _compact({
    activeCtx,
    element: expanded,
    options,
    compactionMap: options.compactionMap
  });

  // perform clean up
  if(options.compactArrays && !options.graph && _isArray(compacted)) {
    if(compacted.length === 1) {
      // simplify to a single item
      compacted = compacted[0];
    } else if(compacted.length === 0) {
      // simplify to an empty object
      compacted = {};
    }
  } else if(options.graph && _isObject(compacted)) {
    // always use array if graph option is on
    compacted = [compacted];
  }

  // follow @context key
  if(_isObject(ctx) && '@context' in ctx) {
    ctx = ctx['@context'];
  }

  // build output context
  ctx = util.clone(ctx);
  if(!_isArray(ctx)) {
    ctx = [ctx];
  }
  // remove empty contexts
  const tmp = ctx;
  ctx = [];
  for(let i = 0; i < tmp.length; ++i) {
    if(!_isObject(tmp[i]) || Object.keys(tmp[i]).length > 0) {
      ctx.push(tmp[i]);
    }
  }

  // remove array if only one context
  const hasContext = (ctx.length > 0);
  if(ctx.length === 1) {
    ctx = ctx[0];
  }

  // add context and/or @graph
  if(_isArray(compacted)) {
    // use '@graph' keyword
    const graphAlias = _compactIri({
      activeCtx, iri: '@graph', relativeTo: {vocab: true}
    });
    const graph = compacted;
    compacted = {};
    if(hasContext) {
      compacted['@context'] = ctx;
    }
    compacted[graphAlias] = graph;
  } else if(_isObject(compacted) && hasContext) {
    // reorder keys so @context is first
    const graph = compacted;
    compacted = {'@context': ctx};
    for(const key in graph) {
      compacted[key] = graph[key];
    }
  }

  return compacted;
};

/**
 * Performs JSON-LD expansion.
 *
 * @param input the JSON-LD input to expand.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [keepFreeFloatingNodes] true to keep free-floating nodes,
 *            false not to, defaults to false.
 *          [documentLoader(url, options)] the document loader.
 *          [expansionMap(info)] a function that can be used to custom map
 *            unmappable values (or to throw an error when they are detected);
 *            if this function returns `undefined` then the default behavior
 *            will be used.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the expanded output.
 */
jsonld.expand = async function(input, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not expand, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    keepFreeFloatingNodes: false,
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });
  if(options.expansionMap === false) {
    options.expansionMap = undefined;
  }

  // build set of objects that may have @contexts to resolve
  const toResolve = {};

  // build set of contexts to process prior to expansion
  const contextsToProcess = [];

  // if an `expandContext` has been given ensure it gets resolved
  if('expandContext' in options) {
    const expandContext = util.clone(options.expandContext);
    if(_isObject(expandContext) && '@context' in expandContext) {
      toResolve.expandContext = expandContext;
    } else {
      toResolve.expandContext = {'@context': expandContext};
    }
    contextsToProcess.push(toResolve.expandContext);
  }

  // if input is a string, attempt to dereference remote document
  let defaultBase;
  if(!_isString(input)) {
    // input is not a URL, do not need to retrieve it first
    toResolve.input = util.clone(input);
  } else {
    // load remote doc
    const remoteDoc = await jsonld.get(input, options);
    defaultBase = remoteDoc.documentUrl;
    toResolve.input = remoteDoc.document;
    if(remoteDoc.contextUrl) {
      // context included in HTTP link header and must be resolved
      toResolve.remoteContext = {'@context': remoteDoc.contextUrl};
      contextsToProcess.push(toResolve.remoteContext);
    }
  }

  // set default base
  if(!('base' in options)) {
    options.base = defaultBase || '';
  }

  // process any additional contexts
  let activeCtx = _getInitialContext(options);
  for(const localCtx of contextsToProcess) {
    activeCtx = await _processContext({activeCtx, localCtx, options});
  }

  // expand resolved input
  let expanded = await _expand({
    activeCtx,
    element: toResolve.input,
    options,
    expansionMap: options.expansionMap
  });

  // optimize away @graph with no other properties
  if(_isObject(expanded) && ('@graph' in expanded) &&
    Object.keys(expanded).length === 1) {
    expanded = expanded['@graph'];
  } else if(expanded === null) {
    expanded = [];
  }

  // normalize to an array
  if(!_isArray(expanded)) {
    expanded = [expanded];
  }

  return expanded;
};

/**
 * Performs JSON-LD flattening.
 *
 * @param input the JSON-LD to flatten.
 * @param ctx the context to use to compact the flattened output, or null.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the flattened output.
 */
jsonld.flatten = async function(input, ctx, options) {
  if(arguments.length < 1) {
    return new TypeError('Could not flatten, too few arguments.');
  }

  if(typeof ctx === 'function') {
    ctx = null;
  } else {
    ctx = ctx || null;
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // expand input
  const expanded = await jsonld.expand(input, options);

  // do flattening
  const flattened = _flatten(expanded);

  if(ctx === null) {
    // no compaction required
    return flattened;
  }

  // compact result (force @graph option to true, skip expansion)
  options.graph = true;
  options.skipExpansion = true;
  const compacted = await jsonld.compact(flattened, ctx, options);

  return compacted;
};

/**
 * Performs JSON-LD framing.
 *
 * @param input the JSON-LD input to frame.
 * @param frame the JSON-LD frame to use.
 * @param [options] the framing options.
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [embed] default @embed flag: '@last', '@always', '@never', '@link'
 *            (default: '@last').
 *          [explicit] default @explicit flag (default: false).
 *          [requireAll] default @requireAll flag (default: true).
 *          [omitDefault] default @omitDefault flag (default: false).
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the framed output.
 */
jsonld.frame = async function(input, frame, options) {
  if(arguments.length < 2) {
    throw new TypeError('Could not frame, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    embed: '@once',
    explicit: false,
    requireAll: false,
    omitDefault: false,
    bnodesToClear: [],
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // if frame is a string, attempt to dereference remote document
  if(_isString(frame)) {
    // load remote doc
    const remoteDoc = await jsonld.get(frame, options);
    frame = remoteDoc.document;

    if(remoteDoc.contextUrl) {
      // inject link header @context into frame
      let ctx = frame['@context'];
      if(!ctx) {
        ctx = remoteDoc.contextUrl;
      } else if(_isArray(ctx)) {
        ctx.push(remoteDoc.contextUrl);
      } else {
        ctx = [ctx, remoteDoc.contextUrl];
      }
      frame['@context'] = ctx;
    }
  }

  const frameContext = frame ? frame['@context'] || {} : {};

  // process context
  const activeCtx = await jsonld.processContext(
    _getInitialContext(options), frameContext, options);

  // mode specific defaults
  if(!options.hasOwnProperty('omitGraph')) {
    options.omitGraph = _processingMode(activeCtx, 1.1);
  }
  if(!options.hasOwnProperty('pruneBlankNodeIdentifiers')) {
    options.pruneBlankNodeIdentifiers = _processingMode(activeCtx, 1.1);
  }

  // expand input
  const expanded = await jsonld.expand(input, options);

  // expand frame
  const opts = {...options};
  opts.isFrame = true;
  opts.keepFreeFloatingNodes = true;
  const expandedFrame = await jsonld.expand(frame, opts);

  // if the unexpanded frame includes a key expanding to @graph, frame the
  // default graph, otherwise, the merged graph
  const frameKeys = Object.keys(frame)
    .map(key => _expandIri(activeCtx, key, {vocab: true}));
  opts.merged = !frameKeys.includes('@graph');
  opts.is11 = _processingMode(activeCtx, 1.1);

  // do framing
  const framed = _frameMergedOrDefault(expanded, expandedFrame, opts);

  opts.graph = !options.omitGraph;
  opts.skipExpansion = true;
  opts.link = {};
  opts.framing = true;
  let compacted = await jsonld.compact(framed, frameContext, opts);

  // replace @null with null, compacting arrays
  opts.link = {};
  compacted = _cleanupNull(compacted, opts);

  return compacted;
};

/**
 * **Experimental**
 *
 * Links a JSON-LD document's nodes in memory.
 *
 * @param input the JSON-LD document to link.
 * @param [ctx] the JSON-LD context to apply.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the linked output.
 */
jsonld.link = async function(input, ctx, options) {
  // API matches running frame with a wildcard frame and embed: '@link'
  // get arguments
  const frame = {};
  if(ctx) {
    frame['@context'] = ctx;
  }
  frame['@embed'] = '@link';
  return jsonld.frame(input, frame, options);
};

/**
 * Performs RDF dataset normalization on the given input. The input is JSON-LD
 * unless the 'inputFormat' option is used. The output is an RDF dataset
 * unless the 'format' option is used.
 *
 * @param input the input to normalize as JSON-LD or as a format specified by
 *          the 'inputFormat' option.
 * @param [options] the options to use:
 *          [algorithm] the normalization algorithm to use, `URDNA2015` or
 *            `URGNA2012` (default: `URDNA2015`).
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [skipExpansion] true to assume the input is expanded and skip
 *            expansion, false not to, defaults to false.
 *          [inputFormat] the format if input is not JSON-LD:
 *            'application/n-quads' for N-Quads.
 *          [format] the format if output is a string:
 *            'application/n-quads' for N-Quads.
 *          [documentLoader(url, options)] the document loader.
 *          [useNative] true to use a native canonize algorithm
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the normalized output.
 */
jsonld.normalize = jsonld.canonize = async function(input, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not canonize, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    algorithm: 'URDNA2015',
    skipExpansion: false,
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });
  if('inputFormat' in options) {
    if(options.inputFormat !== 'application/n-quads' &&
      options.inputFormat !== 'application/nquads') {
      throw new JsonLdError(
        'Unknown canonicalization input format.',
        'jsonld.CanonizeError');
    }
    // TODO: `await` for async parsers
    const parsedInput = NQuads.parse(input);

    // do canonicalization
    return canonize.canonize(parsedInput, options);
  }

  // convert to RDF dataset then do normalization
  const opts = {...options};
  delete opts.format;
  opts.produceGeneralizedRdf = false;
  const dataset = await jsonld.toRDF(input, opts);

  // do canonicalization
  return canonize.canonize(dataset, options);
};

/**
 * Converts an RDF dataset to JSON-LD.
 *
 * @param dataset a serialized string of RDF in a format specified by the
 *          format option or an RDF dataset to convert.
 * @param [options] the options to use:
 *          [format] the format if dataset param must first be parsed:
 *            'application/n-quads' for N-Quads (default).
 *          [rdfParser] a custom RDF-parser to use to parse the dataset.
 *          [useRdfType] true to use rdf:type, false to use @type
 *            (default: false).
 *          [useNativeTypes] true to convert XSD types into native types
 *            (boolean, integer, double), false not to (default: false).
 *
 * @return a Promise that resolves to the JSON-LD document.
 */
jsonld.fromRDF = async function(dataset, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not convert from RDF, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    format: _isString(dataset) ? 'application/n-quads' : undefined
  });

  const {format} = options;
  let {rdfParser} = options;

  // handle special format
  if(format) {
    // check supported formats
    rdfParser = rdfParser || _rdfParsers[format];
    if(!rdfParser) {
      throw new JsonLdError(
        'Unknown input format.',
        'jsonld.UnknownFormat', {format});
    }
  } else {
    // no-op parser, assume dataset already parsed
    rdfParser = () => dataset;
  }

  // rdfParser must be synchronous or return a promise, no callback support
  const parsedDataset = await rdfParser(dataset);
  return _fromRDF(parsedDataset, options);
};

/**
 * Outputs the RDF dataset found in the given JSON-LD object.
 *
 * @param input the JSON-LD input.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [skipExpansion] true to assume the input is expanded and skip
 *            expansion, false not to, defaults to false.
 *          [format] the format to use to output a string:
 *            'application/n-quads' for N-Quads.
 *          [produceGeneralizedRdf] true to output generalized RDF, false
 *            to produce only standard RDF (default: false).
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the RDF dataset.
 */
jsonld.toRDF = async function(input, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not convert to RDF, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    skipExpansion: false,
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // TODO: support toRDF custom map?
  let expanded;
  if(options.skipExpansion) {
    expanded = input;
  } else {
    // expand input
    expanded = await jsonld.expand(input, options);
  }

  // output RDF dataset
  const dataset = _toRDF(expanded, options);
  if(options.format) {
    if(options.format === 'application/n-quads' ||
      options.format === 'application/nquads') {
      return NQuads.serialize(dataset);
    }
    throw new JsonLdError(
      'Unknown output format.',
      'jsonld.UnknownFormat', {format: options.format});
  }

  return dataset;
};

/**
 * **Experimental**
 *
 * Recursively flattens the nodes in the given JSON-LD input into a merged
 * map of node ID => node. All graphs will be merged into the default graph.
 *
 * @param input the JSON-LD input.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [issuer] a jsonld.IdentifierIssuer to use to label blank nodes.
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the merged node map.
 */
jsonld.createNodeMap = async function(input, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not create node map, too few arguments.');
  }

  // set default options
  options = _setDefaults(options, {
    base: _isString(input) ? input : '',
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // expand input
  const expanded = await jsonld.expand(input, options);

  return _createMergedNodeMap(expanded, options);
};

/**
 * **Experimental**
 *
 * Merges two or more JSON-LD documents into a single flattened document.
 *
 * @param docs the JSON-LD documents to merge together.
 * @param ctx the context to use to compact the merged result, or null.
 * @param [options] the options to use:
 *          [base] the base IRI to use.
 *          [expandContext] a context to expand with.
 *          [issuer] a jsonld.IdentifierIssuer to use to label blank nodes.
 *          [mergeNodes] true to merge properties for nodes with the same ID,
 *            false to ignore new properties for nodes with the same ID once
 *            the ID has been defined; note that this may not prevent merging
 *            new properties where a node is in the `object` position
 *            (default: true).
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the merged output.
 */
jsonld.merge = async function(docs, ctx, options) {
  if(arguments.length < 1) {
    throw new TypeError('Could not merge, too few arguments.');
  }
  if(!_isArray(docs)) {
    throw new TypeError('Could not merge, "docs" must be an array.');
  }

  if(typeof ctx === 'function') {
    ctx = null;
  } else {
    ctx = ctx || null;
  }

  // set default options
  options = _setDefaults(options, {
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // expand all documents
  const expanded = await Promise.all(docs.map(doc => {
    const opts = {...options};
    return jsonld.expand(doc, opts);
  }));

  let mergeNodes = true;
  if('mergeNodes' in options) {
    mergeNodes = options.mergeNodes;
  }

  const issuer = options.issuer || new IdentifierIssuer('_:b');
  const graphs = {'@default': {}};

  for(let i = 0; i < expanded.length; ++i) {
    // uniquely relabel blank nodes
    const doc = util.relabelBlankNodes(expanded[i], {
      issuer: new IdentifierIssuer('_:b' + i + '-')
    });

    // add nodes to the shared node map graphs if merging nodes, to a
    // separate graph set if not
    const _graphs = (mergeNodes || i === 0) ? graphs : {'@default': {}};
    _createNodeMap(doc, _graphs, '@default', issuer);

    if(_graphs !== graphs) {
      // merge document graphs but don't merge existing nodes
      for(const graphName in _graphs) {
        const _nodeMap = _graphs[graphName];
        if(!(graphName in graphs)) {
          graphs[graphName] = _nodeMap;
          continue;
        }
        const nodeMap = graphs[graphName];
        for(const key in _nodeMap) {
          if(!(key in nodeMap)) {
            nodeMap[key] = _nodeMap[key];
          }
        }
      }
    }
  }

  // add all non-default graphs to default graph
  const defaultGraph = _mergeNodeMaps(graphs);

  // produce flattened output
  const flattened = [];
  const keys = Object.keys(defaultGraph).sort();
  for(let ki = 0; ki < keys.length; ++ki) {
    const node = defaultGraph[keys[ki]];
    // only add full subjects to top-level
    if(!_isSubjectReference(node)) {
      flattened.push(node);
    }
  }

  if(ctx === null) {
    return flattened;
  }

  // compact result (force @graph option to true, skip expansion)
  options.graph = true;
  options.skipExpansion = true;
  const compacted = await jsonld.compact(flattened, ctx, options);

  return compacted;
};

/**
 * The default document loader for external documents.
 *
 * @param url the URL to load.
 *
 * @return a promise that resolves to the remote document.
 */
Object.defineProperty(jsonld, 'documentLoader', {
  get: () => jsonld._documentLoader,
  set: v => jsonld._documentLoader = v
});
// default document loader not implemented
jsonld.documentLoader = async url => {
  throw new JsonLdError(
    'Could not retrieve a JSON-LD document from the URL. URL ' +
    'dereferencing not implemented.', 'jsonld.LoadDocumentError',
    {code: 'loading document failed', url});
};

/**
 * Gets a remote JSON-LD document using the default document loader or
 * one given in the passed options.
 *
 * @param url the URL to fetch.
 * @param [options] the options to use:
 *          [documentLoader] the document loader to use.
 *
 * @return a Promise that resolves to the retrieved remote document.
 */
jsonld.get = async function(url, options) {
  let load;
  if(typeof options.documentLoader === 'function') {
    load = options.documentLoader;
  } else {
    load = jsonld.documentLoader;
  }

  const remoteDoc = await load(url);

  try {
    if(!remoteDoc.document) {
      throw new JsonLdError(
        'No remote document found at the given URL.',
        'jsonld.NullRemoteDocument');
    }
    if(_isString(remoteDoc.document)) {
      remoteDoc.document = JSON.parse(remoteDoc.document);
    }
  } catch(e) {
    throw new JsonLdError(
      'Could not retrieve a JSON-LD document from the URL.',
      'jsonld.LoadDocumentError', {
        code: 'loading document failed',
        cause: e,
        remoteDoc
      });
  }

  return remoteDoc;
};

/**
 * Processes a local context, resolving any URLs as necessary, and returns a
 * new active context.
 *
 * @param activeCtx the current active context.
 * @param localCtx the local context to process.
 * @param [options] the options to use:
 *          [documentLoader(url, options)] the document loader.
 *          [contextResolver] internal use only.
 *
 * @return a Promise that resolves to the new active context.
 */
jsonld.processContext = async function(
  activeCtx, localCtx, options) {
  // set default options
  options = _setDefaults(options, {
    base: '',
    contextResolver: new ContextResolver(
      {sharedCache: _resolvedContextCache})
  });

  // return initial context early for null context
  if(localCtx === null) {
    return _getInitialContext(options);
  }

  // get URLs in localCtx
  localCtx = util.clone(localCtx);
  if(!(_isObject(localCtx) && '@context' in localCtx)) {
    localCtx = {'@context': localCtx};
  }

  return _processContext({activeCtx, localCtx, options});
};

// backwards compatibility
jsonld.getContextValue = __webpack_require__("d1e7").getContextValue;

/**
 * Document loaders.
 */
jsonld.documentLoaders = {};

/**
 * Assigns the default document loader for external document URLs to a built-in
 * default. Supported types currently include: 'xhr' and 'node'.
 *
 * @param type the type to set.
 * @param [params] the parameters required to use the document loader.
 */
jsonld.useDocumentLoader = function(type) {
  if(!(type in jsonld.documentLoaders)) {
    throw new JsonLdError(
      'Unknown document loader type: "' + type + '"',
      'jsonld.UnknownDocumentLoader',
      {type});
  }

  // set document loader
  jsonld.documentLoader = jsonld.documentLoaders[type].apply(
    jsonld, Array.prototype.slice.call(arguments, 1));
};

/**
 * Registers an RDF dataset parser by content-type, for use with
 * jsonld.fromRDF. An RDF dataset parser will always be given one parameter,
 * a string of input. An RDF dataset parser can be synchronous or
 * asynchronous (by returning a promise).
 *
 * @param contentType the content-type for the parser.
 * @param parser(input) the parser function (takes a string as a parameter
 *          and either returns an RDF dataset or a Promise that resolves to one.
 */
jsonld.registerRDFParser = function(contentType, parser) {
  _rdfParsers[contentType] = parser;
};

/**
 * Unregisters an RDF dataset parser by content-type.
 *
 * @param contentType the content-type for the parser.
 */
jsonld.unregisterRDFParser = function(contentType) {
  delete _rdfParsers[contentType];
};

// register the N-Quads RDF parser
jsonld.registerRDFParser('application/n-quads', NQuads.parse);
jsonld.registerRDFParser('application/nquads', NQuads.parse);

/* URL API */
jsonld.url = __webpack_require__("ddd7");

/* Utility API */
jsonld.util = util;
// backwards compatibility
Object.assign(jsonld, util);

// reexpose API as jsonld.promises for backwards compatability
jsonld.promises = jsonld;

// backwards compatibility
jsonld.RequestQueue = __webpack_require__("40ce");

/* WebIDL API */
jsonld.JsonLdProcessor = __webpack_require__("8322")(jsonld);

platform.setupGlobals(jsonld);
platform.setupDocumentLoaders(jsonld);

function _setDefaults(options, {
  documentLoader = jsonld.documentLoader,
  ...defaults
}) {
  return Object.assign({}, {documentLoader}, defaults, options);
}

// end of jsonld API `wrapper` factory
return jsonld;
};

// external APIs:

// used to generate a new jsonld API instance
const factory = function() {
  return wrapper(function() {
    return factory();
  });
};

// wrap the main jsonld API instance
wrapper(factory);
// export API
module.exports = factory;


/***/ }),

/***/ "46e0":
/***/ (function(module, exports, __webpack_require__) {

!function(e,t){ true?module.exports=t():undefined}(self,(function(){return(()=>{"use strict";var e={d:(t,s)=>{for(var r in s)e.o(s,r)&&!e.o(t,r)&&Object.defineProperty(t,r,{enumerable:!0,get:s[r]})},o:(e,t)=>Object.prototype.hasOwnProperty.call(e,t),r:e=>{"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}},t={};e.r(t),e.d(t,{default:()=>o});var s=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"step-progress__wrapper"},[s("div",{staticClass:"step-progress__wrapper-before",style:{"background-color":e.passiveColor,height:e.lineThickness+"px"}}),s("div",{staticClass:"step-progress__bar"},e._l(e.steps,(function(t,r){return s("div",{staticClass:"step-progress__step",class:{"step-progress__step--active":r===e.currentStep,"step-progress__step--valid":r<e.currentStep},style:{"--activeColor":e.activeColor,"--passiveColor":e.passiveColor,"--activeBorder":e.activeThickness+"px","--passiveBorder":e.passiveThickness+"px"}},[s("span",[e._v(e._s(r+1))]),s("div",{staticClass:"step-progress__step-icon",class:e.iconClass}),s("div",{staticClass:"step-progress__step-label"},[e._v(e._s(t))])])})),0),s("div",{staticClass:"step-progress__wrapper-after",style:{transform:"scaleX("+e.scaleX+") translateY(-50%) perspective(1000px)","background-color":e.activeColor,height:e.lineThickness+"px"}})])};s._withStripped=!0;var r=function(e,t,s,r,o,n,i,a){var p,l="function"==typeof e?e.options:e;if(t&&(l.render=t,l.staticRenderFns=s,l._compiled=!0),r&&(l.functional=!0),n&&(l._scopeId="data-v-"+n),i?(p=function(e){(e=e||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(e=__VUE_SSR_CONTEXT__),o&&o.call(this,e),e&&e._registeredComponents&&e._registeredComponents.add(i)},l._ssrRegister=p):o&&(p=a?function(){o.call(this,(l.functional?this.parent:this).$root.$options.shadowRoot)}:o),p)if(l.functional){l._injectStyles=p;var c=l.render;l.render=function(e,t){return p.call(t),c(e,t)}}else{var u=l.beforeCreate;l.beforeCreate=u?[].concat(u,p):[p]}return{exports:e,options:l}}({name:"StepProgress",props:{steps:{type:Array,default:function(){return[]},validator:function(e){return e&&e.length>0}},currentStep:{type:Number,default:0},iconClass:{type:String,default:"fa fa-check"},activeColor:{type:String,default:"red"},passiveColor:{type:String,default:"gray"},activeThickness:{type:Number,default:5},passiveThickness:{type:Number,default:5},lineThickness:{type:Number,default:12}},computed:{scaleX:function(){var e=this.currentStep;return e<0?e=0:e>=this.steps.length&&(e=this.steps.length-1),e/(this.steps.length-1)}}},s,[],!1,null,null,null);r.options.__file="src/StepProgress.vue";const o=r.exports;return t})()}));
//# sourceMappingURL=vue-step-progress.min.js.map

/***/ }),

/***/ "4840":
/***/ (function(module, exports, __webpack_require__) {

var anObject = __webpack_require__("825a");
var aFunction = __webpack_require__("1c0b");
var wellKnownSymbol = __webpack_require__("b622");

var SPECIES = wellKnownSymbol('species');

// `SpeciesConstructor` abstract operation
// https://tc39.es/ecma262/#sec-speciesconstructor
module.exports = function (O, defaultConstructor) {
  var C = anObject(O).constructor;
  var S;
  return C === undefined || (S = anObject(C)[SPECIES]) == undefined ? defaultConstructor : aFunction(S);
};


/***/ }),

/***/ "4930":
/***/ (function(module, exports, __webpack_require__) {

var IS_NODE = __webpack_require__("605d");
var V8_VERSION = __webpack_require__("2d00");
var fails = __webpack_require__("d039");

// eslint-disable-next-line es/no-object-getownpropertysymbols -- required for testing
module.exports = !!Object.getOwnPropertySymbols && !fails(function () {
  // eslint-disable-next-line es/no-symbol -- required for testing
  return !Symbol.sham &&
    // Chrome 38 Symbol has incorrect toString conversion
    // Chrome 38-40 symbols are not inherited from DOM collections prototypes to instances
    (IS_NODE ? V8_VERSION === 38 : V8_VERSION > 37 && V8_VERSION < 41);
});


/***/ }),

/***/ "4a7a":
/***/ (function(module, exports, __webpack_require__) {

!function(t,e){ true?module.exports=e():undefined}("undefined"!=typeof self?self:this,(function(){return function(t){var e={};function n(o){if(e[o])return e[o].exports;var i=e[o]={i:o,l:!1,exports:{}};return t[o].call(i.exports,i,i.exports,n),i.l=!0,i.exports}return n.m=t,n.c=e,n.d=function(t,e,o){n.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:o})},n.r=function(t){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},n.t=function(t,e){if(1&e&&(t=n(t)),8&e)return t;if(4&e&&"object"==typeof t&&t&&t.__esModule)return t;var o=Object.create(null);if(n.r(o),Object.defineProperty(o,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var i in t)n.d(o,i,function(e){return t[e]}.bind(null,i));return o},n.n=function(t){var e=t&&t.__esModule?function(){return t.default}:function(){return t};return n.d(e,"a",e),e},n.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},n.p="/",n(n.s=8)}([function(t,e,n){var o=n(4),i=n(5),s=n(6);t.exports=function(t){return o(t)||i(t)||s()}},function(t,e){function n(e){return"function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?t.exports=n=function(t){return typeof t}:t.exports=n=function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t},n(e)}t.exports=n},function(t,e,n){},function(t,e){t.exports=function(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}},function(t,e){t.exports=function(t){if(Array.isArray(t)){for(var e=0,n=new Array(t.length);e<t.length;e++)n[e]=t[e];return n}}},function(t,e){t.exports=function(t){if(Symbol.iterator in Object(t)||"[object Arguments]"===Object.prototype.toString.call(t))return Array.from(t)}},function(t,e){t.exports=function(){throw new TypeError("Invalid attempt to spread non-iterable instance")}},function(t,e,n){"use strict";var o=n(2);n.n(o).a},function(t,e,n){"use strict";n.r(e);var o=n(0),i=n.n(o),s=n(1),r=n.n(s),a=n(3),l=n.n(a),c={props:{autoscroll:{type:Boolean,default:!0}},watch:{typeAheadPointer:function(){this.autoscroll&&this.maybeAdjustScroll()}},methods:{maybeAdjustScroll:function(){var t,e=(null===(t=this.$refs.dropdownMenu)||void 0===t?void 0:t.children[this.typeAheadPointer])||!1;if(e){var n=this.getDropdownViewport(),o=e.getBoundingClientRect(),i=o.top,s=o.bottom,r=o.height;if(i<n.top)return this.$refs.dropdownMenu.scrollTop=e.offsetTop;if(s>n.bottom)return this.$refs.dropdownMenu.scrollTop=e.offsetTop-(n.height-r)}},getDropdownViewport:function(){return this.$refs.dropdownMenu?this.$refs.dropdownMenu.getBoundingClientRect():{height:0,top:0,bottom:0}}}},u={data:function(){return{typeAheadPointer:-1}},watch:{filteredOptions:function(){for(var t=0;t<this.filteredOptions.length;t++)if(this.selectable(this.filteredOptions[t])){this.typeAheadPointer=t;break}}},methods:{typeAheadUp:function(){for(var t=this.typeAheadPointer-1;t>=0;t--)if(this.selectable(this.filteredOptions[t])){this.typeAheadPointer=t;break}},typeAheadDown:function(){for(var t=this.typeAheadPointer+1;t<this.filteredOptions.length;t++)if(this.selectable(this.filteredOptions[t])){this.typeAheadPointer=t;break}},typeAheadSelect:function(){var t=this.filteredOptions[this.typeAheadPointer];t&&this.select(t)}}},p={props:{loading:{type:Boolean,default:!1}},data:function(){return{mutableLoading:!1}},watch:{search:function(){this.$emit("search",this.search,this.toggleLoading)},loading:function(t){this.mutableLoading=t}},methods:{toggleLoading:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:null;return this.mutableLoading=null==t?!this.mutableLoading:t}}};function h(t,e,n,o,i,s,r,a){var l,c="function"==typeof t?t.options:t;if(e&&(c.render=e,c.staticRenderFns=n,c._compiled=!0),o&&(c.functional=!0),s&&(c._scopeId="data-v-"+s),r?(l=function(t){(t=t||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(t=__VUE_SSR_CONTEXT__),i&&i.call(this,t),t&&t._registeredComponents&&t._registeredComponents.add(r)},c._ssrRegister=l):i&&(l=a?function(){i.call(this,this.$root.$options.shadowRoot)}:i),l)if(c.functional){c._injectStyles=l;var u=c.render;c.render=function(t,e){return l.call(e),u(t,e)}}else{var p=c.beforeCreate;c.beforeCreate=p?[].concat(p,l):[l]}return{exports:t,options:c}}var d={Deselect:h({},(function(){var t=this.$createElement,e=this._self._c||t;return e("svg",{attrs:{xmlns:"http://www.w3.org/2000/svg",width:"10",height:"10"}},[e("path",{attrs:{d:"M6.895455 5l2.842897-2.842898c.348864-.348863.348864-.914488 0-1.263636L9.106534.261648c-.348864-.348864-.914489-.348864-1.263636 0L5 3.104545 2.157102.261648c-.348863-.348864-.914488-.348864-1.263636 0L.261648.893466c-.348864.348864-.348864.914489 0 1.263636L3.104545 5 .261648 7.842898c-.348864.348863-.348864.914488 0 1.263636l.631818.631818c.348864.348864.914773.348864 1.263636 0L5 6.895455l2.842898 2.842897c.348863.348864.914772.348864 1.263636 0l.631818-.631818c.348864-.348864.348864-.914489 0-1.263636L6.895455 5z"}})])}),[],!1,null,null,null).exports,OpenIndicator:h({},(function(){var t=this.$createElement,e=this._self._c||t;return e("svg",{attrs:{xmlns:"http://www.w3.org/2000/svg",width:"14",height:"10"}},[e("path",{attrs:{d:"M9.211364 7.59931l4.48338-4.867229c.407008-.441854.407008-1.158247 0-1.60046l-.73712-.80023c-.407008-.441854-1.066904-.441854-1.474243 0L7 5.198617 2.51662.33139c-.407008-.441853-1.066904-.441853-1.474243 0l-.737121.80023c-.407008.441854-.407008 1.158248 0 1.600461l4.48338 4.867228L7 10l2.211364-2.40069z"}})])}),[],!1,null,null,null).exports},f={inserted:function(t,e,n){var o=n.context;if(o.appendToBody){var i=o.$refs.toggle.getBoundingClientRect(),s=i.height,r=i.top,a=i.left,l=i.width,c=window.scrollX||window.pageXOffset,u=window.scrollY||window.pageYOffset;t.unbindPosition=o.calculatePosition(t,o,{width:l+"px",left:c+a+"px",top:u+r+s+"px"}),document.body.appendChild(t)}},unbind:function(t,e,n){n.context.appendToBody&&(t.unbindPosition&&"function"==typeof t.unbindPosition&&t.unbindPosition(),t.parentNode&&t.parentNode.removeChild(t))}};var y=function(t){var e={};return Object.keys(t).sort().forEach((function(n){e[n]=t[n]})),JSON.stringify(e)},b=0;var g=function(){return++b};function v(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);e&&(o=o.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,o)}return n}function m(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?v(Object(n),!0).forEach((function(e){l()(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):v(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var _={components:m({},d),mixins:[c,u,p],directives:{appendToBody:f},props:{value:{},components:{type:Object,default:function(){return{}}},options:{type:Array,default:function(){return[]}},disabled:{type:Boolean,default:!1},clearable:{type:Boolean,default:!0},searchable:{type:Boolean,default:!0},multiple:{type:Boolean,default:!1},placeholder:{type:String,default:""},transition:{type:String,default:"vs__fade"},clearSearchOnSelect:{type:Boolean,default:!0},closeOnSelect:{type:Boolean,default:!0},label:{type:String,default:"label"},autocomplete:{type:String,default:"off"},reduce:{type:Function,default:function(t){return t}},selectable:{type:Function,default:function(t){return!0}},getOptionLabel:{type:Function,default:function(t){return"object"===r()(t)?t.hasOwnProperty(this.label)?t[this.label]:console.warn('[vue-select warn]: Label key "option.'.concat(this.label,'" does not')+" exist in options object ".concat(JSON.stringify(t),".\n")+"https://vue-select.org/api/props.html#getoptionlabel"):t}},getOptionKey:{type:Function,default:function(t){if("object"!==r()(t))return t;try{return t.hasOwnProperty("id")?t.id:y(t)}catch(e){return console.warn("[vue-select warn]: Could not stringify this option to generate unique key. Please provide'getOptionKey' prop to return a unique key for each option.\nhttps://vue-select.org/api/props.html#getoptionkey",t,e)}}},onTab:{type:Function,default:function(){this.selectOnTab&&!this.isComposing&&this.typeAheadSelect()}},taggable:{type:Boolean,default:!1},tabindex:{type:Number,default:null},pushTags:{type:Boolean,default:!1},filterable:{type:Boolean,default:!0},filterBy:{type:Function,default:function(t,e,n){return(e||"").toLowerCase().indexOf(n.toLowerCase())>-1}},filter:{type:Function,default:function(t,e){var n=this;return t.filter((function(t){var o=n.getOptionLabel(t);return"number"==typeof o&&(o=o.toString()),n.filterBy(t,o,e)}))}},createOption:{type:Function,default:function(t){return"object"===r()(this.optionList[0])?l()({},this.label,t):t}},resetOnOptionsChange:{default:!1,validator:function(t){return["function","boolean"].includes(r()(t))}},clearSearchOnBlur:{type:Function,default:function(t){var e=t.clearSearchOnSelect,n=t.multiple;return e&&!n}},noDrop:{type:Boolean,default:!1},inputId:{type:String},dir:{type:String,default:"auto"},selectOnTab:{type:Boolean,default:!1},selectOnKeyCodes:{type:Array,default:function(){return[13]}},searchInputQuerySelector:{type:String,default:"[type=search]"},mapKeydown:{type:Function,default:function(t,e){return t}},appendToBody:{type:Boolean,default:!1},calculatePosition:{type:Function,default:function(t,e,n){var o=n.width,i=n.top,s=n.left;t.style.top=i,t.style.left=s,t.style.width=o}}},data:function(){return{uid:g(),search:"",open:!1,isComposing:!1,pushedTags:[],_value:[]}},watch:{options:function(t,e){var n=this;!this.taggable&&("function"==typeof n.resetOnOptionsChange?n.resetOnOptionsChange(t,e,n.selectedValue):n.resetOnOptionsChange)&&this.clearSelection(),this.value&&this.isTrackingValues&&this.setInternalValueFromOptions(this.value)},value:function(t){this.isTrackingValues&&this.setInternalValueFromOptions(t)},multiple:function(){this.clearSelection()},open:function(t){this.$emit(t?"open":"close")}},created:function(){this.mutableLoading=this.loading,void 0!==this.value&&this.isTrackingValues&&this.setInternalValueFromOptions(this.value),this.$on("option:created",this.pushTag)},methods:{setInternalValueFromOptions:function(t){var e=this;Array.isArray(t)?this.$data._value=t.map((function(t){return e.findOptionFromReducedValue(t)})):this.$data._value=this.findOptionFromReducedValue(t)},select:function(t){this.$emit("option:selecting",t),this.isOptionSelected(t)||(this.taggable&&!this.optionExists(t)&&this.$emit("option:created",t),this.multiple&&(t=this.selectedValue.concat(t)),this.updateValue(t),this.$emit("option:selected",t)),this.onAfterSelect(t)},deselect:function(t){var e=this;this.$emit("option:deselecting",t),this.updateValue(this.selectedValue.filter((function(n){return!e.optionComparator(n,t)}))),this.$emit("option:deselected",t)},clearSelection:function(){this.updateValue(this.multiple?[]:null)},onAfterSelect:function(t){this.closeOnSelect&&(this.open=!this.open,this.searchEl.blur()),this.clearSearchOnSelect&&(this.search="")},updateValue:function(t){var e=this;void 0===this.value&&(this.$data._value=t),null!==t&&(t=Array.isArray(t)?t.map((function(t){return e.reduce(t)})):this.reduce(t)),this.$emit("input",t)},toggleDropdown:function(t){var e=t.target!==this.searchEl;e&&t.preventDefault();var n=[].concat(i()(this.$refs.deselectButtons||[]),i()([this.$refs.clearButton]||false));void 0===this.searchEl||n.filter(Boolean).some((function(e){return e.contains(t.target)||e===t.target}))?t.preventDefault():this.open&&e?this.searchEl.blur():this.disabled||(this.open=!0,this.searchEl.focus())},isOptionSelected:function(t){var e=this;return this.selectedValue.some((function(n){return e.optionComparator(n,t)}))},optionComparator:function(t,e){return this.getOptionKey(t)===this.getOptionKey(e)},findOptionFromReducedValue:function(t){var e=this,n=[].concat(i()(this.options),i()(this.pushedTags)).filter((function(n){return JSON.stringify(e.reduce(n))===JSON.stringify(t)}));return 1===n.length?n[0]:n.find((function(t){return e.optionComparator(t,e.$data._value)}))||t},closeSearchOptions:function(){this.open=!1,this.$emit("search:blur")},maybeDeleteValue:function(){if(!this.searchEl.value.length&&this.selectedValue&&this.selectedValue.length&&this.clearable){var t=null;this.multiple&&(t=i()(this.selectedValue.slice(0,this.selectedValue.length-1))),this.updateValue(t)}},optionExists:function(t){var e=this;return this.optionList.some((function(n){return e.optionComparator(n,t)}))},normalizeOptionForSlot:function(t){return"object"===r()(t)?t:l()({},this.label,t)},pushTag:function(t){this.pushedTags.push(t)},onEscape:function(){this.search.length?this.search="":this.searchEl.blur()},onSearchBlur:function(){if(!this.mousedown||this.searching){var t=this.clearSearchOnSelect,e=this.multiple;return this.clearSearchOnBlur({clearSearchOnSelect:t,multiple:e})&&(this.search=""),void this.closeSearchOptions()}this.mousedown=!1,0!==this.search.length||0!==this.options.length||this.closeSearchOptions()},onSearchFocus:function(){this.open=!0,this.$emit("search:focus")},onMousedown:function(){this.mousedown=!0},onMouseUp:function(){this.mousedown=!1},onSearchKeyDown:function(t){var e=this,n=function(t){return t.preventDefault(),!e.isComposing&&e.typeAheadSelect()},o={8:function(t){return e.maybeDeleteValue()},9:function(t){return e.onTab()},27:function(t){return e.onEscape()},38:function(t){return t.preventDefault(),e.typeAheadUp()},40:function(t){return t.preventDefault(),e.typeAheadDown()}};this.selectOnKeyCodes.forEach((function(t){return o[t]=n}));var i=this.mapKeydown(o,this);if("function"==typeof i[t.keyCode])return i[t.keyCode](t)}},computed:{isTrackingValues:function(){return void 0===this.value||this.$options.propsData.hasOwnProperty("reduce")},selectedValue:function(){var t=this.value;return this.isTrackingValues&&(t=this.$data._value),t?[].concat(t):[]},optionList:function(){return this.options.concat(this.pushTags?this.pushedTags:[])},searchEl:function(){return this.$scopedSlots.search?this.$refs.selectedOptions.querySelector(this.searchInputQuerySelector):this.$refs.search},scope:function(){var t=this,e={search:this.search,loading:this.loading,searching:this.searching,filteredOptions:this.filteredOptions};return{search:{attributes:m({disabled:this.disabled,placeholder:this.searchPlaceholder,tabindex:this.tabindex,readonly:!this.searchable,id:this.inputId,"aria-autocomplete":"list","aria-labelledby":"vs".concat(this.uid,"__combobox"),"aria-controls":"vs".concat(this.uid,"__listbox"),ref:"search",type:"search",autocomplete:this.autocomplete,value:this.search},this.dropdownOpen&&this.filteredOptions[this.typeAheadPointer]?{"aria-activedescendant":"vs".concat(this.uid,"__option-").concat(this.typeAheadPointer)}:{}),events:{compositionstart:function(){return t.isComposing=!0},compositionend:function(){return t.isComposing=!1},keydown:this.onSearchKeyDown,blur:this.onSearchBlur,focus:this.onSearchFocus,input:function(e){return t.search=e.target.value}}},spinner:{loading:this.mutableLoading},noOptions:{search:this.search,loading:this.loading,searching:this.searching},openIndicator:{attributes:{ref:"openIndicator",role:"presentation",class:"vs__open-indicator"}},listHeader:e,listFooter:e,header:m({},e,{deselect:this.deselect}),footer:m({},e,{deselect:this.deselect})}},childComponents:function(){return m({},d,{},this.components)},stateClasses:function(){return{"vs--open":this.dropdownOpen,"vs--single":!this.multiple,"vs--searching":this.searching&&!this.noDrop,"vs--searchable":this.searchable&&!this.noDrop,"vs--unsearchable":!this.searchable,"vs--loading":this.mutableLoading,"vs--disabled":this.disabled}},searching:function(){return!!this.search},dropdownOpen:function(){return!this.noDrop&&(this.open&&!this.mutableLoading)},searchPlaceholder:function(){if(this.isValueEmpty&&this.placeholder)return this.placeholder},filteredOptions:function(){var t=[].concat(this.optionList);if(!this.filterable&&!this.taggable)return t;var e=this.search.length?this.filter(t,this.search,this):t;if(this.taggable&&this.search.length){var n=this.createOption(this.search);this.optionExists(n)||e.unshift(n)}return e},isValueEmpty:function(){return 0===this.selectedValue.length},showClearButton:function(){return!this.multiple&&this.clearable&&!this.open&&!this.isValueEmpty}}},O=(n(7),h(_,(function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"v-select",class:t.stateClasses,attrs:{dir:t.dir}},[t._t("header",null,null,t.scope.header),t._v(" "),n("div",{ref:"toggle",staticClass:"vs__dropdown-toggle",attrs:{id:"vs"+t.uid+"__combobox",role:"combobox","aria-expanded":t.dropdownOpen.toString(),"aria-owns":"vs"+t.uid+"__listbox","aria-label":"Search for option"},on:{mousedown:function(e){return t.toggleDropdown(e)}}},[n("div",{ref:"selectedOptions",staticClass:"vs__selected-options"},[t._l(t.selectedValue,(function(e){return t._t("selected-option-container",[n("span",{key:t.getOptionKey(e),staticClass:"vs__selected"},[t._t("selected-option",[t._v("\n            "+t._s(t.getOptionLabel(e))+"\n          ")],null,t.normalizeOptionForSlot(e)),t._v(" "),t.multiple?n("button",{ref:"deselectButtons",refInFor:!0,staticClass:"vs__deselect",attrs:{disabled:t.disabled,type:"button",title:"Deselect "+t.getOptionLabel(e),"aria-label":"Deselect "+t.getOptionLabel(e)},on:{click:function(n){return t.deselect(e)}}},[n(t.childComponents.Deselect,{tag:"component"})],1):t._e()],2)],{option:t.normalizeOptionForSlot(e),deselect:t.deselect,multiple:t.multiple,disabled:t.disabled})})),t._v(" "),t._t("search",[n("input",t._g(t._b({staticClass:"vs__search"},"input",t.scope.search.attributes,!1),t.scope.search.events))],null,t.scope.search)],2),t._v(" "),n("div",{ref:"actions",staticClass:"vs__actions"},[n("button",{directives:[{name:"show",rawName:"v-show",value:t.showClearButton,expression:"showClearButton"}],ref:"clearButton",staticClass:"vs__clear",attrs:{disabled:t.disabled,type:"button",title:"Clear Selected","aria-label":"Clear Selected"},on:{click:t.clearSelection}},[n(t.childComponents.Deselect,{tag:"component"})],1),t._v(" "),t._t("open-indicator",[t.noDrop?t._e():n(t.childComponents.OpenIndicator,t._b({tag:"component"},"component",t.scope.openIndicator.attributes,!1))],null,t.scope.openIndicator),t._v(" "),t._t("spinner",[n("div",{directives:[{name:"show",rawName:"v-show",value:t.mutableLoading,expression:"mutableLoading"}],staticClass:"vs__spinner"},[t._v("Loading...")])],null,t.scope.spinner)],2)]),t._v(" "),n("transition",{attrs:{name:t.transition}},[t.dropdownOpen?n("ul",{directives:[{name:"append-to-body",rawName:"v-append-to-body"}],key:"vs"+t.uid+"__listbox",ref:"dropdownMenu",staticClass:"vs__dropdown-menu",attrs:{id:"vs"+t.uid+"__listbox",role:"listbox",tabindex:"-1"},on:{mousedown:function(e){return e.preventDefault(),t.onMousedown(e)},mouseup:t.onMouseUp}},[t._t("list-header",null,null,t.scope.listHeader),t._v(" "),t._l(t.filteredOptions,(function(e,o){return n("li",{key:t.getOptionKey(e),staticClass:"vs__dropdown-option",class:{"vs__dropdown-option--selected":t.isOptionSelected(e),"vs__dropdown-option--highlight":o===t.typeAheadPointer,"vs__dropdown-option--disabled":!t.selectable(e)},attrs:{role:"option",id:"vs"+t.uid+"__option-"+o,"aria-selected":o===t.typeAheadPointer||null},on:{mouseover:function(n){t.selectable(e)&&(t.typeAheadPointer=o)},mousedown:function(n){n.preventDefault(),n.stopPropagation(),t.selectable(e)&&t.select(e)}}},[t._t("option",[t._v("\n          "+t._s(t.getOptionLabel(e))+"\n        ")],null,t.normalizeOptionForSlot(e))],2)})),t._v(" "),0===t.filteredOptions.length?n("li",{staticClass:"vs__no-options"},[t._t("no-options",[t._v("Sorry, no matching options.")],null,t.scope.noOptions)],2):t._e(),t._v(" "),t._t("list-footer",null,null,t.scope.listFooter)],2):n("ul",{staticStyle:{display:"none",visibility:"hidden"},attrs:{id:"vs"+t.uid+"__listbox",role:"listbox"}})]),t._v(" "),t._t("footer",null,null,t.scope.footer)],2)}),[],!1,null,null,null).exports),w={ajax:p,pointer:u,pointerScroll:c};n.d(e,"VueSelect",(function(){return O})),n.d(e,"mixins",(function(){return w}));e.default=O}])}));
//# sourceMappingURL=vue-select.js.map

/***/ }),

/***/ "4b45":
/***/ (function(module, exports, __webpack_require__) {

/**
 * An implementation of the RDF Dataset Normalization specification.
 *
 * @author Dave Longley
 *
 * Copyright 2010-2021 Digital Bazaar, Inc.
 */
module.exports = __webpack_require__("30ba");


/***/ }),

/***/ "4c53":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var createHTML = __webpack_require__("857a");
var forcedStringHTMLMethod = __webpack_require__("af03");

// `String.prototype.sub` method
// https://tc39.es/ecma262/#sec-string.prototype.sub
$({ target: 'String', proto: true, forced: forcedStringHTMLMethod('sub') }, {
  sub: function sub() {
    return createHTML(this, 'sub', '', '');
  }
});


/***/ }),

/***/ "4d64":
/***/ (function(module, exports, __webpack_require__) {

var toIndexedObject = __webpack_require__("fc6a");
var toLength = __webpack_require__("50c4");
var toAbsoluteIndex = __webpack_require__("23cb");

// `Array.prototype.{ indexOf, includes }` methods implementation
var createMethod = function (IS_INCLUDES) {
  return function ($this, el, fromIndex) {
    var O = toIndexedObject($this);
    var length = toLength(O.length);
    var index = toAbsoluteIndex(fromIndex, length);
    var value;
    // Array#includes uses SameValueZero equality algorithm
    // eslint-disable-next-line no-self-compare -- NaN check
    if (IS_INCLUDES && el != el) while (length > index) {
      value = O[index++];
      // eslint-disable-next-line no-self-compare -- NaN check
      if (value != value) return true;
    // Array#indexOf ignores holes, Array#includes - not
    } else for (;length > index; index++) {
      if ((IS_INCLUDES || index in O) && O[index] === el) return IS_INCLUDES || index || 0;
    } return !IS_INCLUDES && -1;
  };
};

module.exports = {
  // `Array.prototype.includes` method
  // https://tc39.es/ecma262/#sec-array.prototype.includes
  includes: createMethod(true),
  // `Array.prototype.indexOf` method
  // https://tc39.es/ecma262/#sec-array.prototype.indexof
  indexOf: createMethod(false)
};


/***/ }),

/***/ "4d68":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const JsonLdError = __webpack_require__("5950");

const {
  isArray: _isArray,
  isObject: _isObject,
  isString: _isString,
  isUndefined: _isUndefined
} = __webpack_require__("68fa");

const {
  isList: _isList,
  isValue: _isValue,
  isGraph: _isGraph,
  isSimpleGraph: _isSimpleGraph,
  isSubjectReference: _isSubjectReference
} = __webpack_require__("c92f");

const {
  expandIri: _expandIri,
  getContextValue: _getContextValue,
  isKeyword: _isKeyword,
  process: _processContext,
  processingMode: _processingMode
} = __webpack_require__("d1e7");

const {
  removeBase: _removeBase,
  prependBase: _prependBase
} = __webpack_require__("ddd7");

const {
  addValue: _addValue,
  asArray: _asArray,
  compareShortestLeast: _compareShortestLeast
} = __webpack_require__("89ae");

const api = {};
module.exports = api;

/**
 * Recursively compacts an element using the given active context. All values
 * must be in expanded form before this method is called.
 *
 * @param activeCtx the active context to use.
 * @param activeProperty the compacted property associated with the element
 *          to compact, null for none.
 * @param element the element to compact.
 * @param options the compaction options.
 * @param compactionMap the compaction map to use.
 *
 * @return a promise that resolves to the compacted value.
 */
api.compact = async ({
  activeCtx,
  activeProperty = null,
  element,
  options = {},
  compactionMap = () => undefined
}) => {
  // recursively compact array
  if(_isArray(element)) {
    let rval = [];
    for(let i = 0; i < element.length; ++i) {
      // compact, dropping any null values unless custom mapped
      let compacted = await api.compact({
        activeCtx,
        activeProperty,
        element: element[i],
        options,
        compactionMap
      });
      if(compacted === null) {
        compacted = await compactionMap({
          unmappedValue: element[i],
          activeCtx,
          activeProperty,
          parent: element,
          index: i,
          options
        });
        if(compacted === undefined) {
          continue;
        }
      }
      rval.push(compacted);
    }
    if(options.compactArrays && rval.length === 1) {
      // use single element if no container is specified
      const container = _getContextValue(
        activeCtx, activeProperty, '@container') || [];
      if(container.length === 0) {
        rval = rval[0];
      }
    }
    return rval;
  }

  // use any scoped context on activeProperty
  const ctx = _getContextValue(activeCtx, activeProperty, '@context');
  if(!_isUndefined(ctx)) {
    activeCtx = await _processContext({
      activeCtx,
      localCtx: ctx,
      propagate: true,
      overrideProtected: true,
      options
    });
  }

  // recursively compact object
  if(_isObject(element)) {
    if(options.link && '@id' in element &&
      options.link.hasOwnProperty(element['@id'])) {
      // check for a linked element to reuse
      const linked = options.link[element['@id']];
      for(let i = 0; i < linked.length; ++i) {
        if(linked[i].expanded === element) {
          return linked[i].compacted;
        }
      }
    }

    // do value compaction on @values and subject references
    if(_isValue(element) || _isSubjectReference(element)) {
      const rval =
        api.compactValue({activeCtx, activeProperty, value: element, options});
      if(options.link && _isSubjectReference(element)) {
        // store linked element
        if(!(options.link.hasOwnProperty(element['@id']))) {
          options.link[element['@id']] = [];
        }
        options.link[element['@id']].push({expanded: element, compacted: rval});
      }
      return rval;
    }

    // if expanded property is @list and we're contained within a list
    // container, recursively compact this item to an array
    if(_isList(element)) {
      const container = _getContextValue(
        activeCtx, activeProperty, '@container') || [];
      if(container.includes('@list')) {
        return api.compact({
          activeCtx,
          activeProperty,
          element: element['@list'],
          options,
          compactionMap
        });
      }
    }

    // FIXME: avoid misuse of active property as an expanded property?
    const insideReverse = (activeProperty === '@reverse');

    const rval = {};

    // original context before applying property-scoped and local contexts
    const inputCtx = activeCtx;

    // revert to previous context, if there is one,
    // and element is not a value object or a node reference
    if(!_isValue(element) && !_isSubjectReference(element)) {
      activeCtx = activeCtx.revertToPreviousContext();
    }

    // apply property-scoped context after reverting term-scoped context
    const propertyScopedCtx =
      _getContextValue(inputCtx, activeProperty, '@context');
    if(!_isUndefined(propertyScopedCtx)) {
      activeCtx = await _processContext({
        activeCtx,
        localCtx: propertyScopedCtx,
        propagate: true,
        overrideProtected: true,
        options
      });
    }

    if(options.link && '@id' in element) {
      // store linked element
      if(!options.link.hasOwnProperty(element['@id'])) {
        options.link[element['@id']] = [];
      }
      options.link[element['@id']].push({expanded: element, compacted: rval});
    }

    // apply any context defined on an alias of @type
    // if key is @type and any compacted value is a term having a local
    // context, overlay that context
    let types = element['@type'] || [];
    if(types.length > 1) {
      types = Array.from(types).sort();
    }
    // find all type-scoped contexts based on current context, prior to
    // updating it
    const typeContext = activeCtx;
    for(const type of types) {
      const compactedType = api.compactIri(
        {activeCtx: typeContext, iri: type, relativeTo: {vocab: true}});

      // Use any type-scoped context defined on this value
      const ctx = _getContextValue(inputCtx, compactedType, '@context');
      if(!_isUndefined(ctx)) {
        activeCtx = await _processContext({
          activeCtx,
          localCtx: ctx,
          options,
          propagate: false
        });
      }
    }

    // process element keys in order
    const keys = Object.keys(element).sort();
    for(const expandedProperty of keys) {
      const expandedValue = element[expandedProperty];

      // compact @id
      if(expandedProperty === '@id') {
        let compactedValue = _asArray(expandedValue).map(
          expandedIri => api.compactIri({
            activeCtx,
            iri: expandedIri,
            relativeTo: {vocab: false},
            base: options.base
          }));
        if(compactedValue.length === 1) {
          compactedValue = compactedValue[0];
        }

        // use keyword alias and add value
        const alias = api.compactIri(
          {activeCtx, iri: '@id', relativeTo: {vocab: true}});

        rval[alias] = compactedValue;
        continue;
      }

      // compact @type(s)
      if(expandedProperty === '@type') {
        // resolve type values against previous context
        let compactedValue = _asArray(expandedValue).map(
          expandedIri => api.compactIri({
            activeCtx: inputCtx,
            iri: expandedIri,
            relativeTo: {vocab: true}
          }));
        if(compactedValue.length === 1) {
          compactedValue = compactedValue[0];
        }

        // use keyword alias and add value
        const alias = api.compactIri(
          {activeCtx, iri: '@type', relativeTo: {vocab: true}});
        const container = _getContextValue(
          activeCtx, alias, '@container') || [];

        // treat as array for @type if @container includes @set
        const typeAsSet =
          container.includes('@set') &&
          _processingMode(activeCtx, 1.1);
        const isArray =
          typeAsSet || (_isArray(compactedValue) && expandedValue.length === 0);
        _addValue(rval, alias, compactedValue, {propertyIsArray: isArray});
        continue;
      }

      // handle @reverse
      if(expandedProperty === '@reverse') {
        // recursively compact expanded value
        const compactedValue = await api.compact({
          activeCtx,
          activeProperty: '@reverse',
          element: expandedValue,
          options,
          compactionMap
        });

        // handle double-reversed properties
        for(const compactedProperty in compactedValue) {
          if(activeCtx.mappings.has(compactedProperty) &&
            activeCtx.mappings.get(compactedProperty).reverse) {
            const value = compactedValue[compactedProperty];
            const container = _getContextValue(
              activeCtx, compactedProperty, '@container') || [];
            const useArray = (
              container.includes('@set') || !options.compactArrays);
            _addValue(
              rval, compactedProperty, value, {propertyIsArray: useArray});
            delete compactedValue[compactedProperty];
          }
        }

        if(Object.keys(compactedValue).length > 0) {
          // use keyword alias and add value
          const alias = api.compactIri({
            activeCtx,
            iri: expandedProperty,
            relativeTo: {vocab: true}
          });
          _addValue(rval, alias, compactedValue);
        }

        continue;
      }

      if(expandedProperty === '@preserve') {
        // compact using activeProperty
        const compactedValue = await api.compact({
          activeCtx,
          activeProperty,
          element: expandedValue,
          options,
          compactionMap
        });

        if(!(_isArray(compactedValue) && compactedValue.length === 0)) {
          _addValue(rval, expandedProperty, compactedValue);
        }
        continue;
      }

      // handle @index property
      if(expandedProperty === '@index') {
        // drop @index if inside an @index container
        const container = _getContextValue(
          activeCtx, activeProperty, '@container') || [];
        if(container.includes('@index')) {
          continue;
        }

        // use keyword alias and add value
        const alias = api.compactIri({
          activeCtx,
          iri: expandedProperty,
          relativeTo: {vocab: true}
        });
        _addValue(rval, alias, expandedValue);
        continue;
      }

      // skip array processing for keywords that aren't
      // @graph, @list, or @included
      if(expandedProperty !== '@graph' && expandedProperty !== '@list' &&
        expandedProperty !== '@included' &&
        _isKeyword(expandedProperty)) {
        // use keyword alias and add value as is
        const alias = api.compactIri({
          activeCtx,
          iri: expandedProperty,
          relativeTo: {vocab: true}
        });
        _addValue(rval, alias, expandedValue);
        continue;
      }

      // Note: expanded value must be an array due to expansion algorithm.
      if(!_isArray(expandedValue)) {
        throw new JsonLdError(
          'JSON-LD expansion error; expanded value must be an array.',
          'jsonld.SyntaxError');
      }

      // preserve empty arrays
      if(expandedValue.length === 0) {
        const itemActiveProperty = api.compactIri({
          activeCtx,
          iri: expandedProperty,
          value: expandedValue,
          relativeTo: {vocab: true},
          reverse: insideReverse
        });
        const nestProperty = activeCtx.mappings.has(itemActiveProperty) ?
          activeCtx.mappings.get(itemActiveProperty)['@nest'] : null;
        let nestResult = rval;
        if(nestProperty) {
          _checkNestProperty(activeCtx, nestProperty, options);
          if(!_isObject(rval[nestProperty])) {
            rval[nestProperty] = {};
          }
          nestResult = rval[nestProperty];
        }
        _addValue(
          nestResult, itemActiveProperty, expandedValue, {
            propertyIsArray: true
          });
      }

      // recusively process array values
      for(const expandedItem of expandedValue) {
        // compact property and get container type
        const itemActiveProperty = api.compactIri({
          activeCtx,
          iri: expandedProperty,
          value: expandedItem,
          relativeTo: {vocab: true},
          reverse: insideReverse
        });

        // if itemActiveProperty is a @nest property, add values to nestResult,
        // otherwise rval
        const nestProperty = activeCtx.mappings.has(itemActiveProperty) ?
          activeCtx.mappings.get(itemActiveProperty)['@nest'] : null;
        let nestResult = rval;
        if(nestProperty) {
          _checkNestProperty(activeCtx, nestProperty, options);
          if(!_isObject(rval[nestProperty])) {
            rval[nestProperty] = {};
          }
          nestResult = rval[nestProperty];
        }

        const container = _getContextValue(
          activeCtx, itemActiveProperty, '@container') || [];

        // get simple @graph or @list value if appropriate
        const isGraph = _isGraph(expandedItem);
        const isList = _isList(expandedItem);
        let inner;
        if(isList) {
          inner = expandedItem['@list'];
        } else if(isGraph) {
          inner = expandedItem['@graph'];
        }

        // recursively compact expanded item
        let compactedItem = await api.compact({
          activeCtx,
          activeProperty: itemActiveProperty,
          element: (isList || isGraph) ? inner : expandedItem,
          options,
          compactionMap
        });

        // handle @list
        if(isList) {
          // ensure @list value is an array
          if(!_isArray(compactedItem)) {
            compactedItem = [compactedItem];
          }

          if(!container.includes('@list')) {
            // wrap using @list alias
            compactedItem = {
              [api.compactIri({
                activeCtx,
                iri: '@list',
                relativeTo: {vocab: true}
              })]: compactedItem
            };

            // include @index from expanded @list, if any
            if('@index' in expandedItem) {
              compactedItem[api.compactIri({
                activeCtx,
                iri: '@index',
                relativeTo: {vocab: true}
              })] = expandedItem['@index'];
            }
          } else {
            _addValue(nestResult, itemActiveProperty, compactedItem, {
              valueIsArray: true,
              allowDuplicate: true
            });
            continue;
          }
        }

        // Graph object compaction cases
        if(isGraph) {
          if(container.includes('@graph') && (container.includes('@id') ||
            container.includes('@index') && _isSimpleGraph(expandedItem))) {
            // get or create the map object
            let mapObject;
            if(nestResult.hasOwnProperty(itemActiveProperty)) {
              mapObject = nestResult[itemActiveProperty];
            } else {
              nestResult[itemActiveProperty] = mapObject = {};
            }

            // index on @id or @index or alias of @none
            const key = (container.includes('@id') ?
              expandedItem['@id'] : expandedItem['@index']) ||
              api.compactIri({activeCtx, iri: '@none',
                relativeTo: {vocab: true}});
            // add compactedItem to map, using value of `@id` or a new blank
            // node identifier

            _addValue(
              mapObject, key, compactedItem, {
                propertyIsArray:
                  (!options.compactArrays || container.includes('@set'))
              });
          } else if(container.includes('@graph') &&
            _isSimpleGraph(expandedItem)) {
            // container includes @graph but not @id or @index and value is a
            // simple graph object add compact value
            // if compactedItem contains multiple values, it is wrapped in
            // `@included`
            if(_isArray(compactedItem) && compactedItem.length > 1) {
              compactedItem = {'@included': compactedItem};
            }
            _addValue(
              nestResult, itemActiveProperty, compactedItem, {
                propertyIsArray:
                  (!options.compactArrays || container.includes('@set'))
              });
          } else {
            // wrap using @graph alias, remove array if only one item and
            // compactArrays not set
            if(_isArray(compactedItem) && compactedItem.length === 1 &&
              options.compactArrays) {
              compactedItem = compactedItem[0];
            }
            compactedItem = {
              [api.compactIri({
                activeCtx,
                iri: '@graph',
                relativeTo: {vocab: true}
              })]: compactedItem
            };

            // include @id from expanded graph, if any
            if('@id' in expandedItem) {
              compactedItem[api.compactIri({
                activeCtx,
                iri: '@id',
                relativeTo: {vocab: true}
              })] = expandedItem['@id'];
            }

            // include @index from expanded graph, if any
            if('@index' in expandedItem) {
              compactedItem[api.compactIri({
                activeCtx,
                iri: '@index',
                relativeTo: {vocab: true}
              })] = expandedItem['@index'];
            }
            _addValue(
              nestResult, itemActiveProperty, compactedItem, {
                propertyIsArray:
                  (!options.compactArrays || container.includes('@set'))
              });
          }
        } else if(container.includes('@language') ||
          container.includes('@index') || container.includes('@id') ||
          container.includes('@type')) {
          // handle language and index maps
          // get or create the map object
          let mapObject;
          if(nestResult.hasOwnProperty(itemActiveProperty)) {
            mapObject = nestResult[itemActiveProperty];
          } else {
            nestResult[itemActiveProperty] = mapObject = {};
          }

          let key;
          if(container.includes('@language')) {
          // if container is a language map, simplify compacted value to
          // a simple string
            if(_isValue(compactedItem)) {
              compactedItem = compactedItem['@value'];
            }
            key = expandedItem['@language'];
          } else if(container.includes('@index')) {
            const indexKey = _getContextValue(
              activeCtx, itemActiveProperty, '@index') || '@index';
            const containerKey = api.compactIri(
              {activeCtx, iri: indexKey, relativeTo: {vocab: true}});
            if(indexKey === '@index') {
              key = expandedItem['@index'];
              delete compactedItem[containerKey];
            } else {
              let others;
              [key, ...others] = _asArray(compactedItem[indexKey] || []);
              if(!_isString(key)) {
                // Will use @none if it isn't a string.
                key = null;
              } else {
                switch(others.length) {
                  case 0:
                    delete compactedItem[indexKey];
                    break;
                  case 1:
                    compactedItem[indexKey] = others[0];
                    break;
                  default:
                    compactedItem[indexKey] = others;
                    break;
                }
              }
            }
          } else if(container.includes('@id')) {
            const idKey = api.compactIri({activeCtx, iri: '@id',
              relativeTo: {vocab: true}});
            key = compactedItem[idKey];
            delete compactedItem[idKey];
          } else if(container.includes('@type')) {
            const typeKey = api.compactIri({
              activeCtx,
              iri: '@type',
              relativeTo: {vocab: true}
            });
            let types;
            [key, ...types] = _asArray(compactedItem[typeKey] || []);
            switch(types.length) {
              case 0:
                delete compactedItem[typeKey];
                break;
              case 1:
                compactedItem[typeKey] = types[0];
                break;
              default:
                compactedItem[typeKey] = types;
                break;
            }

            // If compactedItem contains a single entry
            // whose key maps to @id, recompact without @type
            if(Object.keys(compactedItem).length === 1 &&
              '@id' in expandedItem) {
              compactedItem = await api.compact({
                activeCtx,
                activeProperty: itemActiveProperty,
                element: {'@id': expandedItem['@id']},
                options,
                compactionMap
              });
            }
          }

          // if compacting this value which has no key, index on @none
          if(!key) {
            key = api.compactIri({activeCtx, iri: '@none',
              relativeTo: {vocab: true}});
          }
          // add compact value to map object using key from expanded value
          // based on the container type
          _addValue(
            mapObject, key, compactedItem, {
              propertyIsArray: container.includes('@set')
            });
        } else {
          // use an array if: compactArrays flag is false,
          // @container is @set or @list , value is an empty
          // array, or key is @graph
          const isArray = (!options.compactArrays ||
            container.includes('@set') || container.includes('@list') ||
            (_isArray(compactedItem) && compactedItem.length === 0) ||
            expandedProperty === '@list' || expandedProperty === '@graph');

          // add compact value
          _addValue(
            nestResult, itemActiveProperty, compactedItem,
            {propertyIsArray: isArray});
        }
      }
    }

    return rval;
  }

  // only primitives remain which are already compact
  return element;
};

/**
 * Compacts an IRI or keyword into a term or prefix if it can be. If the
 * IRI has an associated value it may be passed.
 *
 * @param activeCtx the active context to use.
 * @param iri the IRI to compact.
 * @param value the value to check or null.
 * @param relativeTo options for how to compact IRIs:
 *          vocab: true to split after @vocab, false not to.
 * @param reverse true if a reverse property is being compacted, false if not.
 * @param base the absolute URL to use for compacting document-relative IRIs.
 *
 * @return the compacted term, prefix, keyword alias, or the original IRI.
 */
api.compactIri = ({
  activeCtx,
  iri,
  value = null,
  relativeTo = {vocab: false},
  reverse = false,
  base = null
}) => {
  // can't compact null
  if(iri === null) {
    return iri;
  }

  // if context is from a property term scoped context composed with a
  // type-scoped context, then use the previous context instead
  if(activeCtx.isPropertyTermScoped && activeCtx.previousContext) {
    activeCtx = activeCtx.previousContext;
  }

  const inverseCtx = activeCtx.getInverse();

  // if term is a keyword, it may be compacted to a simple alias
  if(_isKeyword(iri) &&
    iri in inverseCtx &&
    '@none' in inverseCtx[iri] &&
    '@type' in inverseCtx[iri]['@none'] &&
    '@none' in inverseCtx[iri]['@none']['@type']) {
    return inverseCtx[iri]['@none']['@type']['@none'];
  }

  // use inverse context to pick a term if iri is relative to vocab
  if(relativeTo.vocab && iri in inverseCtx) {
    const defaultLanguage = activeCtx['@language'] || '@none';

    // prefer @index if available in value
    const containers = [];
    if(_isObject(value) && '@index' in value && !('@graph' in value)) {
      containers.push('@index', '@index@set');
    }

    // if value is a preserve object, use its value
    if(_isObject(value) && '@preserve' in value) {
      value = value['@preserve'][0];
    }

    // prefer most specific container including @graph, prefering @set
    // variations
    if(_isGraph(value)) {
      // favor indexmap if the graph is indexed
      if('@index' in value) {
        containers.push(
          '@graph@index', '@graph@index@set', '@index', '@index@set');
      }
      // favor idmap if the graph is has an @id
      if('@id' in value) {
        containers.push(
          '@graph@id', '@graph@id@set');
      }
      containers.push('@graph', '@graph@set', '@set');
      // allow indexmap if the graph is not indexed
      if(!('@index' in value)) {
        containers.push(
          '@graph@index', '@graph@index@set', '@index', '@index@set');
      }
      // allow idmap if the graph does not have an @id
      if(!('@id' in value)) {
        containers.push('@graph@id', '@graph@id@set');
      }
    } else if(_isObject(value) && !_isValue(value)) {
      containers.push('@id', '@id@set', '@type', '@set@type');
    }

    // defaults for term selection based on type/language
    let typeOrLanguage = '@language';
    let typeOrLanguageValue = '@null';

    if(reverse) {
      typeOrLanguage = '@type';
      typeOrLanguageValue = '@reverse';
      containers.push('@set');
    } else if(_isList(value)) {
      // choose the most specific term that works for all elements in @list
      // only select @list containers if @index is NOT in value
      if(!('@index' in value)) {
        containers.push('@list');
      }
      const list = value['@list'];
      if(list.length === 0) {
        // any empty list can be matched against any term that uses the
        // @list container regardless of @type or @language
        typeOrLanguage = '@any';
        typeOrLanguageValue = '@none';
      } else {
        let commonLanguage = (list.length === 0) ? defaultLanguage : null;
        let commonType = null;
        for(let i = 0; i < list.length; ++i) {
          const item = list[i];
          let itemLanguage = '@none';
          let itemType = '@none';
          if(_isValue(item)) {
            if('@direction' in item) {
              const lang = (item['@language'] || '').toLowerCase();
              const dir = item['@direction'];
              itemLanguage = `${lang}_${dir}`;
            } else if('@language' in item) {
              itemLanguage = item['@language'].toLowerCase();
            } else if('@type' in item) {
              itemType = item['@type'];
            } else {
              // plain literal
              itemLanguage = '@null';
            }
          } else {
            itemType = '@id';
          }
          if(commonLanguage === null) {
            commonLanguage = itemLanguage;
          } else if(itemLanguage !== commonLanguage && _isValue(item)) {
            commonLanguage = '@none';
          }
          if(commonType === null) {
            commonType = itemType;
          } else if(itemType !== commonType) {
            commonType = '@none';
          }
          // there are different languages and types in the list, so choose
          // the most generic term, no need to keep iterating the list
          if(commonLanguage === '@none' && commonType === '@none') {
            break;
          }
        }
        commonLanguage = commonLanguage || '@none';
        commonType = commonType || '@none';
        if(commonType !== '@none') {
          typeOrLanguage = '@type';
          typeOrLanguageValue = commonType;
        } else {
          typeOrLanguageValue = commonLanguage;
        }
      }
    } else {
      if(_isValue(value)) {
        if('@language' in value && !('@index' in value)) {
          containers.push('@language', '@language@set');
          typeOrLanguageValue = value['@language'];
          const dir = value['@direction'];
          if(dir) {
            typeOrLanguageValue = `${typeOrLanguageValue}_${dir}`;
          }
        } else if('@direction' in value && !('@index' in value)) {
          typeOrLanguageValue = `_${value['@direction']}`;
        } else if('@type' in value) {
          typeOrLanguage = '@type';
          typeOrLanguageValue = value['@type'];
        }
      } else {
        typeOrLanguage = '@type';
        typeOrLanguageValue = '@id';
      }
      containers.push('@set');
    }

    // do term selection
    containers.push('@none');

    // an index map can be used to index values using @none, so add as a low
    // priority
    if(_isObject(value) && !('@index' in value)) {
      // allow indexing even if no @index present
      containers.push('@index', '@index@set');
    }

    // values without type or language can use @language map
    if(_isValue(value) && Object.keys(value).length === 1) {
      // allow indexing even if no @index present
      containers.push('@language', '@language@set');
    }

    const term = _selectTerm(
      activeCtx, iri, value, containers, typeOrLanguage, typeOrLanguageValue);
    if(term !== null) {
      return term;
    }
  }

  // no term match, use @vocab if available
  if(relativeTo.vocab) {
    if('@vocab' in activeCtx) {
      // determine if vocab is a prefix of the iri
      const vocab = activeCtx['@vocab'];
      if(iri.indexOf(vocab) === 0 && iri !== vocab) {
        // use suffix as relative iri if it is not a term in the active context
        const suffix = iri.substr(vocab.length);
        if(!activeCtx.mappings.has(suffix)) {
          return suffix;
        }
      }
    }
  }

  // no term or @vocab match, check for possible CURIEs
  let choice = null;
  // TODO: make FastCurieMap a class with a method to do this lookup
  const partialMatches = [];
  let iriMap = activeCtx.fastCurieMap;
  // check for partial matches of against `iri`, which means look until
  // iri.length - 1, not full length
  const maxPartialLength = iri.length - 1;
  for(let i = 0; i < maxPartialLength && iri[i] in iriMap; ++i) {
    iriMap = iriMap[iri[i]];
    if('' in iriMap) {
      partialMatches.push(iriMap[''][0]);
    }
  }
  // check partial matches in reverse order to prefer longest ones first
  for(let i = partialMatches.length - 1; i >= 0; --i) {
    const entry = partialMatches[i];
    const terms = entry.terms;
    for(const term of terms) {
      // a CURIE is usable if:
      // 1. it has no mapping, OR
      // 2. value is null, which means we're not compacting an @value, AND
      //   the mapping matches the IRI
      const curie = term + ':' + iri.substr(entry.iri.length);
      const isUsableCurie = (activeCtx.mappings.get(term)._prefix &&
        (!activeCtx.mappings.has(curie) ||
        (value === null && activeCtx.mappings.get(curie)['@id'] === iri)));

      // select curie if it is shorter or the same length but lexicographically
      // less than the current choice
      if(isUsableCurie && (choice === null ||
        _compareShortestLeast(curie, choice) < 0)) {
        choice = curie;
      }
    }
  }

  // return chosen curie
  if(choice !== null) {
    return choice;
  }

  // If iri could be confused with a compact IRI using a term in this context,
  // signal an error
  for(const [term, td] of activeCtx.mappings) {
    if(td && td._prefix && iri.startsWith(term + ':')) {
      throw new JsonLdError(
        `Absolute IRI "${iri}" confused with prefix "${term}".`,
        'jsonld.SyntaxError',
        {code: 'IRI confused with prefix', context: activeCtx});
    }
  }

  // compact IRI relative to base
  if(!relativeTo.vocab) {
    if('@base' in activeCtx) {
      if(!activeCtx['@base']) {
        // The None case preserves rval as potentially relative
        return iri;
      } else {
        return _removeBase(_prependBase(base, activeCtx['@base']), iri);
      }
    } else {
      return _removeBase(base, iri);
    }
  }

  // return IRI as is
  return iri;
};

/**
 * Performs value compaction on an object with '@value' or '@id' as the only
 * property.
 *
 * @param activeCtx the active context.
 * @param activeProperty the active property that points to the value.
 * @param value the value to compact.
 * @param {Object} [options] - processing options.
 *
 * @return the compaction result.
 */
api.compactValue = ({activeCtx, activeProperty, value, options}) => {
  // value is a @value
  if(_isValue(value)) {
    // get context rules
    const type = _getContextValue(activeCtx, activeProperty, '@type');
    const language = _getContextValue(activeCtx, activeProperty, '@language');
    const direction = _getContextValue(activeCtx, activeProperty, '@direction');
    const container =
      _getContextValue(activeCtx, activeProperty, '@container') || [];

    // whether or not the value has an @index that must be preserved
    const preserveIndex = '@index' in value && !container.includes('@index');

    // if there's no @index to preserve ...
    if(!preserveIndex && type !== '@none') {
      // matching @type or @language specified in context, compact value
      if(value['@type'] === type) {
        return value['@value'];
      }
      if('@language' in value && value['@language'] === language &&
         '@direction' in value && value['@direction'] === direction) {
        return value['@value'];
      }
      if('@language' in value && value['@language'] === language) {
        return value['@value'];
      }
      if('@direction' in value && value['@direction'] === direction) {
        return value['@value'];
      }
    }

    // return just the value of @value if all are true:
    // 1. @value is the only key or @index isn't being preserved
    // 2. there is no default language or @value is not a string or
    //   the key has a mapping with a null @language
    const keyCount = Object.keys(value).length;
    const isValueOnlyKey = (keyCount === 1 ||
      (keyCount === 2 && '@index' in value && !preserveIndex));
    const hasDefaultLanguage = ('@language' in activeCtx);
    const isValueString = _isString(value['@value']);
    const hasNullMapping = (activeCtx.mappings.has(activeProperty) &&
      activeCtx.mappings.get(activeProperty)['@language'] === null);
    if(isValueOnlyKey &&
      type !== '@none' &&
      (!hasDefaultLanguage || !isValueString || hasNullMapping)) {
      return value['@value'];
    }

    const rval = {};

    // preserve @index
    if(preserveIndex) {
      rval[api.compactIri({
        activeCtx,
        iri: '@index',
        relativeTo: {vocab: true}
      })] = value['@index'];
    }

    if('@type' in value) {
      // compact @type IRI
      rval[api.compactIri({
        activeCtx,
        iri: '@type',
        relativeTo: {vocab: true}
      })] = api.compactIri(
        {activeCtx, iri: value['@type'], relativeTo: {vocab: true}});
    } else if('@language' in value) {
      // alias @language
      rval[api.compactIri({
        activeCtx,
        iri: '@language',
        relativeTo: {vocab: true}
      })] = value['@language'];
    }

    if('@direction' in value) {
      // alias @direction
      rval[api.compactIri({
        activeCtx,
        iri: '@direction',
        relativeTo: {vocab: true}
      })] = value['@direction'];
    }

    // alias @value
    rval[api.compactIri({
      activeCtx,
      iri: '@value',
      relativeTo: {vocab: true}
    })] = value['@value'];

    return rval;
  }

  // value is a subject reference
  const expandedProperty = _expandIri(activeCtx, activeProperty, {vocab: true},
    options);
  const type = _getContextValue(activeCtx, activeProperty, '@type');
  const compacted = api.compactIri({
    activeCtx,
    iri: value['@id'],
    relativeTo: {vocab: type === '@vocab'},
    base: options.base});

  // compact to scalar
  if(type === '@id' || type === '@vocab' || expandedProperty === '@graph') {
    return compacted;
  }

  return {
    [api.compactIri({
      activeCtx,
      iri: '@id',
      relativeTo: {vocab: true}
    })]: compacted
  };
};

/**
 * Picks the preferred compaction term from the given inverse context entry.
 *
 * @param activeCtx the active context.
 * @param iri the IRI to pick the term for.
 * @param value the value to pick the term for.
 * @param containers the preferred containers.
 * @param typeOrLanguage either '@type' or '@language'.
 * @param typeOrLanguageValue the preferred value for '@type' or '@language'.
 *
 * @return the preferred term.
 */
function _selectTerm(
  activeCtx, iri, value, containers, typeOrLanguage, typeOrLanguageValue) {
  if(typeOrLanguageValue === null) {
    typeOrLanguageValue = '@null';
  }

  // preferences for the value of @type or @language
  const prefs = [];

  // determine prefs for @id based on whether or not value compacts to a term
  if((typeOrLanguageValue === '@id' || typeOrLanguageValue === '@reverse') &&
    _isObject(value) && '@id' in value) {
    // prefer @reverse first
    if(typeOrLanguageValue === '@reverse') {
      prefs.push('@reverse');
    }
    // try to compact value to a term
    const term = api.compactIri(
      {activeCtx, iri: value['@id'], relativeTo: {vocab: true}});
    if(activeCtx.mappings.has(term) &&
      activeCtx.mappings.get(term) &&
      activeCtx.mappings.get(term)['@id'] === value['@id']) {
      // prefer @vocab
      prefs.push.apply(prefs, ['@vocab', '@id']);
    } else {
      // prefer @id
      prefs.push.apply(prefs, ['@id', '@vocab']);
    }
  } else {
    prefs.push(typeOrLanguageValue);

    // consider direction only
    const langDir = prefs.find(el => el.includes('_'));
    if(langDir) {
      // consider _dir portion
      prefs.push(langDir.replace(/^[^_]+_/, '_'));
    }
  }
  prefs.push('@none');

  const containerMap = activeCtx.inverse[iri];
  for(const container of containers) {
    // if container not available in the map, continue
    if(!(container in containerMap)) {
      continue;
    }

    const typeOrLanguageValueMap = containerMap[container][typeOrLanguage];
    for(const pref of prefs) {
      // if type/language option not available in the map, continue
      if(!(pref in typeOrLanguageValueMap)) {
        continue;
      }

      // select term
      return typeOrLanguageValueMap[pref];
    }
  }

  return null;
}

/**
 * The value of `@nest` in the term definition must either be `@nest`, or a term
 * which resolves to `@nest`.
 *
 * @param activeCtx the active context.
 * @param nestProperty a term in the active context or `@nest`.
 * @param {Object} [options] - processing options.
 */
function _checkNestProperty(activeCtx, nestProperty, options) {
  if(_expandIri(activeCtx, nestProperty, {vocab: true}, options) !== '@nest') {
    throw new JsonLdError(
      'JSON-LD compact error; nested property must have an @nest value ' +
      'resolving to @nest.',
      'jsonld.SyntaxError', {code: 'invalid @nest value'});
  }
}


/***/ }),

/***/ "4de4":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var $filter = __webpack_require__("b727").filter;
var arrayMethodHasSpeciesSupport = __webpack_require__("1dde");

var HAS_SPECIES_SUPPORT = arrayMethodHasSpeciesSupport('filter');

// `Array.prototype.filter` method
// https://tc39.es/ecma262/#sec-array.prototype.filter
// with adding support of @@species
$({ target: 'Array', proto: true, forced: !HAS_SPECIES_SUPPORT }, {
  filter: function filter(callbackfn /* , thisArg */) {
    return $filter(this, callbackfn, arguments.length > 1 ? arguments[1] : undefined);
  }
});


/***/ }),

/***/ "50c4":
/***/ (function(module, exports, __webpack_require__) {

var toInteger = __webpack_require__("a691");

var min = Math.min;

// `ToLength` abstract operation
// https://tc39.es/ecma262/#sec-tolength
module.exports = function (argument) {
  return argument > 0 ? min(toInteger(argument), 0x1FFFFFFFFFFFFF) : 0; // 2 ** 53 - 1 == 9007199254740991
};


/***/ }),

/***/ "50ca":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const JsonLdError = __webpack_require__("5950");

const {
  isArray: _isArray,
  isObject: _isObject,
  isEmptyObject: _isEmptyObject,
  isString: _isString,
  isUndefined: _isUndefined
} = __webpack_require__("68fa");

const {
  isList: _isList,
  isValue: _isValue,
  isGraph: _isGraph,
  isSubject: _isSubject
} = __webpack_require__("c92f");

const {
  expandIri: _expandIri,
  getContextValue: _getContextValue,
  isKeyword: _isKeyword,
  process: _processContext,
  processingMode: _processingMode
} = __webpack_require__("d1e7");

const {
  isAbsolute: _isAbsoluteIri
} = __webpack_require__("ddd7");

const {
  addValue: _addValue,
  asArray: _asArray,
  getValues: _getValues,
  validateTypeValue: _validateTypeValue
} = __webpack_require__("89ae");

const api = {};
module.exports = api;
const REGEX_BCP47 = /^[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*$/;

/**
 * Recursively expands an element using the given context. Any context in
 * the element will be removed. All context URLs must have been retrieved
 * before calling this method.
 *
 * @param activeCtx the context to use.
 * @param activeProperty the property for the element, null for none.
 * @param element the element to expand.
 * @param options the expansion options.
 * @param insideList true if the element is a list, false if not.
 * @param insideIndex true if the element is inside an index container,
 *          false if not.
 * @param typeScopedContext an optional type-scoped active context for
 *          expanding values of nodes that were expressed according to
 *          a type-scoped context.
 * @param expansionMap(info) a function that can be used to custom map
 *          unmappable values (or to throw an error when they are detected);
 *          if this function returns `undefined` then the default behavior
 *          will be used.
 *
 * @return a Promise that resolves to the expanded value.
 */
api.expand = async ({
  activeCtx,
  activeProperty = null,
  element,
  options = {},
  insideList = false,
  insideIndex = false,
  typeScopedContext = null,
  expansionMap = () => undefined
}) => {
  // nothing to expand
  if(element === null || element === undefined) {
    return null;
  }

  // disable framing if activeProperty is @default
  if(activeProperty === '@default') {
    options = Object.assign({}, options, {isFrame: false});
  }

  if(!_isArray(element) && !_isObject(element)) {
    // drop free-floating scalars that are not in lists unless custom mapped
    if(!insideList && (activeProperty === null ||
      _expandIri(activeCtx, activeProperty, {vocab: true},
        options) === '@graph')) {
      const mapped = await expansionMap({
        unmappedValue: element,
        activeCtx,
        activeProperty,
        options,
        insideList
      });
      if(mapped === undefined) {
        return null;
      }
      return mapped;
    }

    // expand element according to value expansion rules
    return _expandValue({activeCtx, activeProperty, value: element, options});
  }

  // recursively expand array
  if(_isArray(element)) {
    let rval = [];
    const container = _getContextValue(
      activeCtx, activeProperty, '@container') || [];
    insideList = insideList || container.includes('@list');
    for(let i = 0; i < element.length; ++i) {
      // expand element
      let e = await api.expand({
        activeCtx,
        activeProperty,
        element: element[i],
        options,
        expansionMap,
        insideIndex,
        typeScopedContext
      });
      if(insideList && _isArray(e)) {
        e = {'@list': e};
      }

      if(e === null) {
        e = await expansionMap({
          unmappedValue: element[i],
          activeCtx,
          activeProperty,
          parent: element,
          index: i,
          options,
          expandedParent: rval,
          insideList
        });
        if(e === undefined) {
          continue;
        }
      }

      if(_isArray(e)) {
        rval = rval.concat(e);
      } else {
        rval.push(e);
      }
    }
    return rval;
  }

  // recursively expand object:

  // first, expand the active property
  const expandedActiveProperty = _expandIri(
    activeCtx, activeProperty, {vocab: true}, options);

  // Get any property-scoped context for activeProperty
  const propertyScopedCtx =
    _getContextValue(activeCtx, activeProperty, '@context');

  // second, determine if any type-scoped context should be reverted; it
  // should only be reverted when the following are all true:
  // 1. `element` is not a value or subject reference
  // 2. `insideIndex` is false
  typeScopedContext = typeScopedContext ||
    (activeCtx.previousContext ? activeCtx : null);
  let keys = Object.keys(element).sort();
  let mustRevert = !insideIndex;
  if(mustRevert && typeScopedContext && keys.length <= 2 &&
    !keys.includes('@context')) {
    for(const key of keys) {
      const expandedProperty = _expandIri(
        typeScopedContext, key, {vocab: true}, options);
      if(expandedProperty === '@value') {
        // value found, ensure type-scoped context is used to expand it
        mustRevert = false;
        activeCtx = typeScopedContext;
        break;
      }
      if(expandedProperty === '@id' && keys.length === 1) {
        // subject reference found, do not revert
        mustRevert = false;
        break;
      }
    }
  }

  if(mustRevert) {
    // revert type scoped context
    activeCtx = activeCtx.revertToPreviousContext();
  }

  // apply property-scoped context after reverting term-scoped context
  if(!_isUndefined(propertyScopedCtx)) {
    activeCtx = await _processContext({
      activeCtx,
      localCtx: propertyScopedCtx,
      propagate: true,
      overrideProtected: true,
      options
    });
  }

  // if element has a context, process it
  if('@context' in element) {
    activeCtx = await _processContext(
      {activeCtx, localCtx: element['@context'], options});
  }

  // set the type-scoped context to the context on input, for use later
  typeScopedContext = activeCtx;

  // Remember the first key found expanding to @type
  let typeKey = null;

  // look for scoped contexts on `@type`
  for(const key of keys) {
    const expandedProperty = _expandIri(activeCtx, key, {vocab: true}, options);
    if(expandedProperty === '@type') {
      // set scoped contexts from @type
      // avoid sorting if possible
      typeKey = typeKey || key;
      const value = element[key];
      const types =
        Array.isArray(value) ?
          (value.length > 1 ? value.slice().sort() : value) : [value];
      for(const type of types) {
        const ctx = _getContextValue(typeScopedContext, type, '@context');
        if(!_isUndefined(ctx)) {
          activeCtx = await _processContext({
            activeCtx,
            localCtx: ctx,
            options,
            propagate: false
          });
        }
      }
    }
  }

  // process each key and value in element, ignoring @nest content
  let rval = {};
  await _expandObject({
    activeCtx,
    activeProperty,
    expandedActiveProperty,
    element,
    expandedParent: rval,
    options,
    insideList,
    typeKey,
    typeScopedContext,
    expansionMap});

  // get property count on expanded output
  keys = Object.keys(rval);
  let count = keys.length;

  if('@value' in rval) {
    // @value must only have @language or @type
    if('@type' in rval && ('@language' in rval || '@direction' in rval)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; an element containing "@value" may not ' +
        'contain both "@type" and either "@language" or "@direction".',
        'jsonld.SyntaxError', {code: 'invalid value object', element: rval});
    }
    let validCount = count - 1;
    if('@type' in rval) {
      validCount -= 1;
    }
    if('@index' in rval) {
      validCount -= 1;
    }
    if('@language' in rval) {
      validCount -= 1;
    }
    if('@direction' in rval) {
      validCount -= 1;
    }
    if(validCount !== 0) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; an element containing "@value" may only ' +
        'have an "@index" property and either "@type" ' +
        'or either or both "@language" or "@direction".',
        'jsonld.SyntaxError', {code: 'invalid value object', element: rval});
    }
    const values = rval['@value'] === null ? [] : _asArray(rval['@value']);
    const types = _getValues(rval, '@type');

    // drop null @values unless custom mapped
    if(_processingMode(activeCtx, 1.1) && types.includes('@json') &&
      types.length === 1) {
      // Any value of @value is okay if @type: @json
    } else if(values.length === 0) {
      const mapped = await expansionMap({
        unmappedValue: rval,
        activeCtx,
        activeProperty,
        element,
        options,
        insideList
      });
      if(mapped !== undefined) {
        rval = mapped;
      } else {
        rval = null;
      }
    } else if(!values.every(v => (_isString(v) || _isEmptyObject(v))) &&
      '@language' in rval) {
      // if @language is present, @value must be a string
      throw new JsonLdError(
        'Invalid JSON-LD syntax; only strings may be language-tagged.',
        'jsonld.SyntaxError',
        {code: 'invalid language-tagged value', element: rval});
    } else if(!types.every(t =>
      (_isAbsoluteIri(t) && !(_isString(t) && t.indexOf('_:') === 0) ||
      _isEmptyObject(t)))) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; an element containing "@value" and "@type" ' +
        'must have an absolute IRI for the value of "@type".',
        'jsonld.SyntaxError', {code: 'invalid typed value', element: rval});
    }
  } else if('@type' in rval && !_isArray(rval['@type'])) {
    // convert @type to an array
    rval['@type'] = [rval['@type']];
  } else if('@set' in rval || '@list' in rval) {
    // handle @set and @list
    if(count > 1 && !(count === 2 && '@index' in rval)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; if an element has the property "@set" ' +
        'or "@list", then it can have at most one other property that is ' +
        '"@index".', 'jsonld.SyntaxError',
        {code: 'invalid set or list object', element: rval});
    }
    // optimize away @set
    if('@set' in rval) {
      rval = rval['@set'];
      keys = Object.keys(rval);
      count = keys.length;
    }
  } else if(count === 1 && '@language' in rval) {
    // drop objects with only @language unless custom mapped
    const mapped = await expansionMap(rval, {
      unmappedValue: rval,
      activeCtx,
      activeProperty,
      element,
      options,
      insideList
    });
    if(mapped !== undefined) {
      rval = mapped;
    } else {
      rval = null;
    }
  }

  // drop certain top-level objects that do not occur in lists, unless custom
  // mapped
  if(_isObject(rval) &&
    !options.keepFreeFloatingNodes && !insideList &&
    (activeProperty === null || expandedActiveProperty === '@graph')) {
    // drop empty object, top-level @value/@list, or object with only @id
    if(count === 0 || '@value' in rval || '@list' in rval ||
      (count === 1 && '@id' in rval)) {
      const mapped = await expansionMap({
        unmappedValue: rval,
        activeCtx,
        activeProperty,
        element,
        options,
        insideList
      });
      if(mapped !== undefined) {
        rval = mapped;
      } else {
        rval = null;
      }
    }
  }

  return rval;
};

/**
 * Expand each key and value of element adding to result
 *
 * @param activeCtx the context to use.
 * @param activeProperty the property for the element.
 * @param expandedActiveProperty the expansion of activeProperty
 * @param element the element to expand.
 * @param expandedParent the expanded result into which to add values.
 * @param options the expansion options.
 * @param insideList true if the element is a list, false if not.
 * @param typeKey first key found expanding to @type.
 * @param typeScopedContext the context before reverting.
 * @param expansionMap(info) a function that can be used to custom map
 *          unmappable values (or to throw an error when they are detected);
 *          if this function returns `undefined` then the default behavior
 *          will be used.
 */
async function _expandObject({
  activeCtx,
  activeProperty,
  expandedActiveProperty,
  element,
  expandedParent,
  options = {},
  insideList,
  typeKey,
  typeScopedContext,
  expansionMap
}) {
  const keys = Object.keys(element).sort();
  const nests = [];
  let unexpandedValue;

  // Figure out if this is the type for a JSON literal
  const isJsonType = element[typeKey] &&
    _expandIri(activeCtx,
      (_isArray(element[typeKey]) ? element[typeKey][0] : element[typeKey]),
      {vocab: true}, options) === '@json';

  for(const key of keys) {
    let value = element[key];
    let expandedValue;

    // skip @context
    if(key === '@context') {
      continue;
    }

    // expand property
    let expandedProperty = _expandIri(activeCtx, key, {vocab: true}, options);

    // drop non-absolute IRI keys that aren't keywords unless custom mapped
    if(expandedProperty === null ||
      !(_isAbsoluteIri(expandedProperty) || _isKeyword(expandedProperty))) {
      // TODO: use `await` to support async
      expandedProperty = expansionMap({
        unmappedProperty: key,
        activeCtx,
        activeProperty,
        parent: element,
        options,
        insideList,
        value,
        expandedParent
      });
      if(expandedProperty === undefined) {
        continue;
      }
    }

    if(_isKeyword(expandedProperty)) {
      if(expandedActiveProperty === '@reverse') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; a keyword cannot be used as a @reverse ' +
          'property.', 'jsonld.SyntaxError',
          {code: 'invalid reverse property map', value});
      }
      if(expandedProperty in expandedParent &&
         expandedProperty !== '@included' &&
         expandedProperty !== '@type') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; colliding keywords detected.',
          'jsonld.SyntaxError',
          {code: 'colliding keywords', keyword: expandedProperty});
      }
    }

    // syntax error if @id is not a string
    if(expandedProperty === '@id') {
      if(!_isString(value)) {
        if(!options.isFrame) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; "@id" value must a string.',
            'jsonld.SyntaxError', {code: 'invalid @id value', value});
        }
        if(_isObject(value)) {
          // empty object is a wildcard
          if(!_isEmptyObject(value)) {
            throw new JsonLdError(
              'Invalid JSON-LD syntax; "@id" value an empty object or array ' +
              'of strings, if framing',
              'jsonld.SyntaxError', {code: 'invalid @id value', value});
          }
        } else if(_isArray(value)) {
          if(!value.every(v => _isString(v))) {
            throw new JsonLdError(
              'Invalid JSON-LD syntax; "@id" value an empty object or array ' +
              'of strings, if framing',
              'jsonld.SyntaxError', {code: 'invalid @id value', value});
          }
        } else {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; "@id" value an empty object or array ' +
            'of strings, if framing',
            'jsonld.SyntaxError', {code: 'invalid @id value', value});
        }
      }

      _addValue(
        expandedParent, '@id',
        _asArray(value).map(v =>
          _isString(v) ? _expandIri(activeCtx, v, {base: true}, options) : v),
        {propertyIsArray: options.isFrame});
      continue;
    }

    if(expandedProperty === '@type') {
      // if framing, can be a default object, but need to expand
      // key to determine that
      if(_isObject(value)) {
        value = Object.fromEntries(Object.entries(value).map(([k, v]) => [
          _expandIri(typeScopedContext, k, {vocab: true}),
          _asArray(v).map(vv =>
            _expandIri(typeScopedContext, vv, {base: true, vocab: true})
          )
        ]));
      }
      _validateTypeValue(value, options.isFrame);
      _addValue(
        expandedParent, '@type',
        _asArray(value).map(v =>
          _isString(v) ?
            _expandIri(typeScopedContext, v,
              {base: true, vocab: true}, options) : v),
        {propertyIsArray: options.isFrame});
      continue;
    }

    // Included blocks are treated as an array of separate object nodes sharing
    // the same referencing active_property.
    // For 1.0, it is skipped as are other unknown keywords
    if(expandedProperty === '@included' && _processingMode(activeCtx, 1.1)) {
      const includedResult = _asArray(await api.expand({
        activeCtx,
        activeProperty,
        element: value,
        options,
        expansionMap
      }));

      // Expanded values must be node objects
      if(!includedResult.every(v => _isSubject(v))) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; ' +
          'values of @included must expand to node objects.',
          'jsonld.SyntaxError', {code: 'invalid @included value', value});
      }

      _addValue(
        expandedParent, '@included', includedResult, {propertyIsArray: true});
      continue;
    }

    // @graph must be an array or an object
    if(expandedProperty === '@graph' &&
      !(_isObject(value) || _isArray(value))) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; "@graph" value must not be an ' +
        'object or an array.',
        'jsonld.SyntaxError', {code: 'invalid @graph value', value});
    }

    if(expandedProperty === '@value') {
      // capture value for later
      // "colliding keywords" check prevents this from being set twice
      unexpandedValue = value;
      if(isJsonType && _processingMode(activeCtx, 1.1)) {
        // no coercion to array, and retain all values
        expandedParent['@value'] = value;
      } else {
        _addValue(
          expandedParent, '@value', value, {propertyIsArray: options.isFrame});
      }
      continue;
    }

    // @language must be a string
    // it should match BCP47
    if(expandedProperty === '@language') {
      if(value === null) {
        // drop null @language values, they expand as if they didn't exist
        continue;
      }
      if(!_isString(value) && !options.isFrame) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; "@language" value must be a string.',
          'jsonld.SyntaxError',
          {code: 'invalid language-tagged string', value});
      }
      // ensure language value is lowercase
      value = _asArray(value).map(v => _isString(v) ? v.toLowerCase() : v);

      // ensure language tag matches BCP47
      for(const lang of value) {
        if(_isString(lang) && !lang.match(REGEX_BCP47)) {
          console.warn(`@language must be valid BCP47: ${lang}`);
        }
      }

      _addValue(
        expandedParent, '@language', value, {propertyIsArray: options.isFrame});
      continue;
    }

    // @direction must be "ltr" or "rtl"
    if(expandedProperty === '@direction') {
      if(!_isString(value) && !options.isFrame) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; "@direction" value must be a string.',
          'jsonld.SyntaxError',
          {code: 'invalid base direction', value});
      }

      value = _asArray(value);

      // ensure direction is "ltr" or "rtl"
      for(const dir of value) {
        if(_isString(dir) && dir !== 'ltr' && dir !== 'rtl') {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; "@direction" must be "ltr" or "rtl".',
            'jsonld.SyntaxError',
            {code: 'invalid base direction', value});
        }
      }

      _addValue(
        expandedParent, '@direction', value,
        {propertyIsArray: options.isFrame});
      continue;
    }

    // @index must be a string
    if(expandedProperty === '@index') {
      if(!_isString(value)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; "@index" value must be a string.',
          'jsonld.SyntaxError',
          {code: 'invalid @index value', value});
      }
      _addValue(expandedParent, '@index', value);
      continue;
    }

    // @reverse must be an object
    if(expandedProperty === '@reverse') {
      if(!_isObject(value)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; "@reverse" value must be an object.',
          'jsonld.SyntaxError', {code: 'invalid @reverse value', value});
      }

      expandedValue = await api.expand({
        activeCtx,
        activeProperty:
        '@reverse',
        element: value,
        options,
        expansionMap
      });
      // properties double-reversed
      if('@reverse' in expandedValue) {
        for(const property in expandedValue['@reverse']) {
          _addValue(
            expandedParent, property, expandedValue['@reverse'][property],
            {propertyIsArray: true});
        }
      }

      // FIXME: can this be merged with code below to simplify?
      // merge in all reversed properties
      let reverseMap = expandedParent['@reverse'] || null;
      for(const property in expandedValue) {
        if(property === '@reverse') {
          continue;
        }
        if(reverseMap === null) {
          reverseMap = expandedParent['@reverse'] = {};
        }
        _addValue(reverseMap, property, [], {propertyIsArray: true});
        const items = expandedValue[property];
        for(let ii = 0; ii < items.length; ++ii) {
          const item = items[ii];
          if(_isValue(item) || _isList(item)) {
            throw new JsonLdError(
              'Invalid JSON-LD syntax; "@reverse" value must not be a ' +
              '@value or an @list.', 'jsonld.SyntaxError',
              {code: 'invalid reverse property value', value: expandedValue});
          }
          _addValue(reverseMap, property, item, {propertyIsArray: true});
        }
      }

      continue;
    }

    // nested keys
    if(expandedProperty === '@nest') {
      nests.push(key);
      continue;
    }

    // use potential scoped context for key
    let termCtx = activeCtx;
    const ctx = _getContextValue(activeCtx, key, '@context');
    if(!_isUndefined(ctx)) {
      termCtx = await _processContext({
        activeCtx,
        localCtx: ctx,
        propagate: true,
        overrideProtected: true,
        options
      });
    }

    const container = _getContextValue(termCtx, key, '@container') || [];

    if(container.includes('@language') && _isObject(value)) {
      const direction = _getContextValue(termCtx, key, '@direction');
      // handle language map container (skip if value is not an object)
      expandedValue = _expandLanguageMap(termCtx, value, direction, options);
    } else if(container.includes('@index') && _isObject(value)) {
      // handle index container (skip if value is not an object)
      const asGraph = container.includes('@graph');
      const indexKey = _getContextValue(termCtx, key, '@index') || '@index';
      const propertyIndex = indexKey !== '@index' &&
        _expandIri(activeCtx, indexKey, {vocab: true}, options);

      expandedValue = await _expandIndexMap({
        activeCtx: termCtx,
        options,
        activeProperty: key,
        value,
        expansionMap,
        asGraph,
        indexKey,
        propertyIndex
      });
    } else if(container.includes('@id') && _isObject(value)) {
      // handle id container (skip if value is not an object)
      const asGraph = container.includes('@graph');
      expandedValue = await _expandIndexMap({
        activeCtx: termCtx,
        options,
        activeProperty: key,
        value,
        expansionMap,
        asGraph,
        indexKey: '@id'
      });
    } else if(container.includes('@type') && _isObject(value)) {
      // handle type container (skip if value is not an object)
      expandedValue = await _expandIndexMap({
        // since container is `@type`, revert type scoped context when expanding
        activeCtx: termCtx.revertToPreviousContext(),
        options,
        activeProperty: key,
        value,
        expansionMap,
        asGraph: false,
        indexKey: '@type'
      });
    } else {
      // recurse into @list or @set
      const isList = (expandedProperty === '@list');
      if(isList || expandedProperty === '@set') {
        let nextActiveProperty = activeProperty;
        if(isList && expandedActiveProperty === '@graph') {
          nextActiveProperty = null;
        }
        expandedValue = await api.expand({
          activeCtx: termCtx,
          activeProperty: nextActiveProperty,
          element: value,
          options,
          insideList: isList,
          expansionMap
        });
      } else if(
        _getContextValue(activeCtx, key, '@type') === '@json') {
        expandedValue = {
          '@type': '@json',
          '@value': value
        };
      } else {
        // recursively expand value with key as new active property
        expandedValue = await api.expand({
          activeCtx: termCtx,
          activeProperty: key,
          element: value,
          options,
          insideList: false,
          expansionMap
        });
      }
    }

    // drop null values if property is not @value
    if(expandedValue === null && expandedProperty !== '@value') {
      // TODO: use `await` to support async
      expandedValue = expansionMap({
        unmappedValue: value,
        expandedProperty,
        activeCtx: termCtx,
        activeProperty,
        parent: element,
        options,
        insideList,
        key,
        expandedParent
      });
      if(expandedValue === undefined) {
        continue;
      }
    }

    // convert expanded value to @list if container specifies it
    if(expandedProperty !== '@list' && !_isList(expandedValue) &&
      container.includes('@list')) {
      // ensure expanded value in @list is an array
      expandedValue = {'@list': _asArray(expandedValue)};
    }

    // convert expanded value to @graph if container specifies it
    // and value is not, itself, a graph
    // index cases handled above
    if(container.includes('@graph') &&
      !container.some(key => key === '@id' || key === '@index')) {
      // ensure expanded values are arrays
      expandedValue = _asArray(expandedValue)
        .map(v => ({'@graph': _asArray(v)}));
    }

    // FIXME: can this be merged with code above to simplify?
    // merge in reverse properties
    if(termCtx.mappings.has(key) && termCtx.mappings.get(key).reverse) {
      const reverseMap =
        expandedParent['@reverse'] = expandedParent['@reverse'] || {};
      expandedValue = _asArray(expandedValue);
      for(let ii = 0; ii < expandedValue.length; ++ii) {
        const item = expandedValue[ii];
        if(_isValue(item) || _isList(item)) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; "@reverse" value must not be a ' +
            '@value or an @list.', 'jsonld.SyntaxError',
            {code: 'invalid reverse property value', value: expandedValue});
        }
        _addValue(reverseMap, expandedProperty, item, {propertyIsArray: true});
      }
      continue;
    }

    // add value for property
    // special keywords handled above
    _addValue(expandedParent, expandedProperty, expandedValue, {
      propertyIsArray: true
    });
  }

  // @value must not be an object or an array (unless framing) or if @type is
  // @json
  if('@value' in expandedParent) {
    if(expandedParent['@type'] === '@json' && _processingMode(activeCtx, 1.1)) {
      // allow any value, to be verified when the object is fully expanded and
      // the @type is @json.
    } else if((_isObject(unexpandedValue) || _isArray(unexpandedValue)) &&
      !options.isFrame) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; "@value" value must not be an ' +
        'object or an array.',
        'jsonld.SyntaxError',
        {code: 'invalid value object value', value: unexpandedValue});
    }
  }

  // expand each nested key
  for(const key of nests) {
    const nestedValues = _isArray(element[key]) ? element[key] : [element[key]];
    for(const nv of nestedValues) {
      if(!_isObject(nv) || Object.keys(nv).some(k =>
        _expandIri(activeCtx, k, {vocab: true}, options) === '@value')) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; nested value must be a node object.',
          'jsonld.SyntaxError',
          {code: 'invalid @nest value', value: nv});
      }
      await _expandObject({
        activeCtx,
        activeProperty,
        expandedActiveProperty,
        element: nv,
        expandedParent,
        options,
        insideList,
        typeScopedContext,
        typeKey,
        expansionMap});
    }
  }
}

/**
 * Expands the given value by using the coercion and keyword rules in the
 * given context.
 *
 * @param activeCtx the active context to use.
 * @param activeProperty the active property the value is associated with.
 * @param value the value to expand.
 * @param {Object} [options] - processing options.
 *
 * @return the expanded value.
 */
function _expandValue({activeCtx, activeProperty, value, options}) {
  // nothing to expand
  if(value === null || value === undefined) {
    return null;
  }

  // special-case expand @id and @type (skips '@id' expansion)
  const expandedProperty = _expandIri(
    activeCtx, activeProperty, {vocab: true}, options);
  if(expandedProperty === '@id') {
    return _expandIri(activeCtx, value, {base: true}, options);
  } else if(expandedProperty === '@type') {
    return _expandIri(activeCtx, value, {vocab: true, base: true}, options);
  }

  // get type definition from context
  const type = _getContextValue(activeCtx, activeProperty, '@type');

  // do @id expansion (automatic for @graph)
  if((type === '@id' || expandedProperty === '@graph') && _isString(value)) {
    return {'@id': _expandIri(activeCtx, value, {base: true}, options)};
  }
  // do @id expansion w/vocab
  if(type === '@vocab' && _isString(value)) {
    return {
      '@id': _expandIri(activeCtx, value, {vocab: true, base: true}, options)
    };
  }

  // do not expand keyword values
  if(_isKeyword(expandedProperty)) {
    return value;
  }

  const rval = {};

  if(type && !['@id', '@vocab', '@none'].includes(type)) {
    // other type
    rval['@type'] = type;
  } else if(_isString(value)) {
    // check for language tagging for strings
    const language = _getContextValue(activeCtx, activeProperty, '@language');
    if(language !== null) {
      rval['@language'] = language;
    }
    const direction = _getContextValue(activeCtx, activeProperty, '@direction');
    if(direction !== null) {
      rval['@direction'] = direction;
    }
  }
  // do conversion of values that aren't basic JSON types to strings
  if(!['boolean', 'number', 'string'].includes(typeof value)) {
    value = value.toString();
  }
  rval['@value'] = value;

  return rval;
}

/**
 * Expands a language map.
 *
 * @param activeCtx the active context to use.
 * @param languageMap the language map to expand.
 * @param direction the direction to apply to values.
 * @param {Object} [options] - processing options.
 *
 * @return the expanded language map.
 */
function _expandLanguageMap(activeCtx, languageMap, direction, options) {
  const rval = [];
  const keys = Object.keys(languageMap).sort();
  for(const key of keys) {
    const expandedKey = _expandIri(activeCtx, key, {vocab: true}, options);
    let val = languageMap[key];
    if(!_isArray(val)) {
      val = [val];
    }
    for(const item of val) {
      if(item === null) {
        // null values are allowed (8.5) but ignored (3.1)
        continue;
      }
      if(!_isString(item)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; language map values must be strings.',
          'jsonld.SyntaxError',
          {code: 'invalid language map value', languageMap});
      }
      const val = {'@value': item};
      if(expandedKey !== '@none') {
        val['@language'] = key.toLowerCase();
      }
      if(direction) {
        val['@direction'] = direction;
      }
      rval.push(val);
    }
  }
  return rval;
}

async function _expandIndexMap(
  {activeCtx, options, activeProperty, value, expansionMap, asGraph,
    indexKey, propertyIndex}) {
  const rval = [];
  const keys = Object.keys(value).sort();
  const isTypeIndex = indexKey === '@type';
  for(let key of keys) {
    // if indexKey is @type, there may be a context defined for it
    if(isTypeIndex) {
      const ctx = _getContextValue(activeCtx, key, '@context');
      if(!_isUndefined(ctx)) {
        activeCtx = await _processContext({
          activeCtx,
          localCtx: ctx,
          propagate: false,
          options
        });
      }
    }

    let val = value[key];
    if(!_isArray(val)) {
      val = [val];
    }

    val = await api.expand({
      activeCtx,
      activeProperty,
      element: val,
      options,
      insideList: false,
      insideIndex: true,
      expansionMap
    });

    // expand for @type, but also for @none
    let expandedKey;
    if(propertyIndex) {
      if(key === '@none') {
        expandedKey = '@none';
      } else {
        expandedKey = _expandValue(
          {activeCtx, activeProperty: indexKey, value: key, options});
      }
    } else {
      expandedKey = _expandIri(activeCtx, key, {vocab: true}, options);
    }

    if(indexKey === '@id') {
      // expand document relative
      key = _expandIri(activeCtx, key, {base: true}, options);
    } else if(isTypeIndex) {
      key = expandedKey;
    }

    for(let item of val) {
      // If this is also a @graph container, turn items into graphs
      if(asGraph && !_isGraph(item)) {
        item = {'@graph': [item]};
      }
      if(indexKey === '@type') {
        if(expandedKey === '@none') {
          // ignore @none
        } else if(item['@type']) {
          item['@type'] = [key].concat(item['@type']);
        } else {
          item['@type'] = [key];
        }
      } else if(_isValue(item) &&
        !['@language', '@type', '@index'].includes(indexKey)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; Attempt to add illegal key to value ' +
          `object: "${indexKey}".`,
          'jsonld.SyntaxError',
          {code: 'invalid value object', value: item});
      } else if(propertyIndex) {
        // index is a property to be expanded, and values interpreted for that
        // property
        if(expandedKey !== '@none') {
          // expand key as a value
          _addValue(item, propertyIndex, expandedKey, {
            propertyIsArray: true,
            prependValue: true
          });
        }
      } else if(expandedKey !== '@none' && !(indexKey in item)) {
        item[indexKey] = key;
      }
      rval.push(item);
    }
  }
  return rval;
}


/***/ }),

/***/ "5135":
/***/ (function(module, exports) {

var hasOwnProperty = {}.hasOwnProperty;

module.exports = function (it, key) {
  return hasOwnProperty.call(it, key);
};


/***/ }),

/***/ "5319":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var fixRegExpWellKnownSymbolLogic = __webpack_require__("d784");
var anObject = __webpack_require__("825a");
var toLength = __webpack_require__("50c4");
var toInteger = __webpack_require__("a691");
var requireObjectCoercible = __webpack_require__("1d80");
var advanceStringIndex = __webpack_require__("8aa5");
var getSubstitution = __webpack_require__("0cb2");
var regExpExec = __webpack_require__("14c3");

var max = Math.max;
var min = Math.min;

var maybeToString = function (it) {
  return it === undefined ? it : String(it);
};

// @@replace logic
fixRegExpWellKnownSymbolLogic('replace', 2, function (REPLACE, nativeReplace, maybeCallNative, reason) {
  var REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE = reason.REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE;
  var REPLACE_KEEPS_$0 = reason.REPLACE_KEEPS_$0;
  var UNSAFE_SUBSTITUTE = REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE ? '$' : '$0';

  return [
    // `String.prototype.replace` method
    // https://tc39.es/ecma262/#sec-string.prototype.replace
    function replace(searchValue, replaceValue) {
      var O = requireObjectCoercible(this);
      var replacer = searchValue == undefined ? undefined : searchValue[REPLACE];
      return replacer !== undefined
        ? replacer.call(searchValue, O, replaceValue)
        : nativeReplace.call(String(O), searchValue, replaceValue);
    },
    // `RegExp.prototype[@@replace]` method
    // https://tc39.es/ecma262/#sec-regexp.prototype-@@replace
    function (regexp, replaceValue) {
      if (
        (!REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE && REPLACE_KEEPS_$0) ||
        (typeof replaceValue === 'string' && replaceValue.indexOf(UNSAFE_SUBSTITUTE) === -1)
      ) {
        var res = maybeCallNative(nativeReplace, regexp, this, replaceValue);
        if (res.done) return res.value;
      }

      var rx = anObject(regexp);
      var S = String(this);

      var functionalReplace = typeof replaceValue === 'function';
      if (!functionalReplace) replaceValue = String(replaceValue);

      var global = rx.global;
      if (global) {
        var fullUnicode = rx.unicode;
        rx.lastIndex = 0;
      }
      var results = [];
      while (true) {
        var result = regExpExec(rx, S);
        if (result === null) break;

        results.push(result);
        if (!global) break;

        var matchStr = String(result[0]);
        if (matchStr === '') rx.lastIndex = advanceStringIndex(S, toLength(rx.lastIndex), fullUnicode);
      }

      var accumulatedResult = '';
      var nextSourcePosition = 0;
      for (var i = 0; i < results.length; i++) {
        result = results[i];

        var matched = String(result[0]);
        var position = max(min(toInteger(result.index), S.length), 0);
        var captures = [];
        // NOTE: This is equivalent to
        //   captures = result.slice(1).map(maybeToString)
        // but for some reason `nativeSlice.call(result, 1, result.length)` (called in
        // the slice polyfill when slicing native arrays) "doesn't work" in safari 9 and
        // causes a crash (https://pastebin.com/N21QzeQA) when trying to debug it.
        for (var j = 1; j < result.length; j++) captures.push(maybeToString(result[j]));
        var namedCaptures = result.groups;
        if (functionalReplace) {
          var replacerArgs = [matched].concat(captures, position, S);
          if (namedCaptures !== undefined) replacerArgs.push(namedCaptures);
          var replacement = String(replaceValue.apply(undefined, replacerArgs));
        } else {
          replacement = getSubstitution(matched, S, position, captures, namedCaptures, replaceValue);
        }
        if (position >= nextSourcePosition) {
          accumulatedResult += S.slice(nextSourcePosition, position) + replacement;
          nextSourcePosition = position + matched.length;
        }
      }
      return accumulatedResult + S.slice(nextSourcePosition);
    }
  ];
});


/***/ }),

/***/ "5342":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return convertToInput; });
/* harmony import */ var _builds_piveau_hub_piveau_hub_forms_node_modules_babel_runtime_helpers_esm_asyncToGenerator__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("1da1");
/* harmony import */ var regenerator_runtime_runtime_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__("96cf");
/* harmony import */ var regenerator_runtime_runtime_js__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(regenerator_runtime_runtime_js__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var core_js_modules_es_array_includes_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__("caad");
/* harmony import */ var core_js_modules_es_array_includes_js__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_array_includes_js__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var core_js_modules_es_string_includes_js__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__("2532");
/* harmony import */ var core_js_modules_es_string_includes_js__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_string_includes_js__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var core_js_modules_es_object_keys_js__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__("b64b");
/* harmony import */ var core_js_modules_es_object_keys_js__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_object_keys_js__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var core_js_modules_es_function_name_js__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__("b0c0");
/* harmony import */ var core_js_modules_es_function_name_js__WEBPACK_IMPORTED_MODULE_5___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_function_name_js__WEBPACK_IMPORTED_MODULE_5__);
/* harmony import */ var core_js_modules_es_array_map_js__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__("d81d");
/* harmony import */ var core_js_modules_es_array_map_js__WEBPACK_IMPORTED_MODULE_6___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_array_map_js__WEBPACK_IMPORTED_MODULE_6__);
/* harmony import */ var core_js_modules_es_array_filter_js__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__("4de4");
/* harmony import */ var core_js_modules_es_array_filter_js__WEBPACK_IMPORTED_MODULE_7___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_array_filter_js__WEBPACK_IMPORTED_MODULE_7__);
/* harmony import */ var core_js_modules_es_string_ends_with_js__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__("8a79");
/* harmony import */ var core_js_modules_es_string_ends_with_js__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_string_ends_with_js__WEBPACK_IMPORTED_MODULE_8__);
/* harmony import */ var core_js_modules_es_string_starts_with_js__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__("2ca0");
/* harmony import */ var core_js_modules_es_string_starts_with_js__WEBPACK_IMPORTED_MODULE_9___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_string_starts_with_js__WEBPACK_IMPORTED_MODULE_9__);
/* harmony import */ var core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__("d3b7");
/* harmony import */ var core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_10___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_object_to_string_js__WEBPACK_IMPORTED_MODULE_10__);
/* harmony import */ var core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__("e6cf");
/* harmony import */ var core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_11___default = /*#__PURE__*/__webpack_require__.n(core_js_modules_es_promise_js__WEBPACK_IMPORTED_MODULE_11__);













/**
 * @description Extracts value names defined within the given configuration
 * @param {Array} dataArray Array containing input configuration
 * @param {String} name String containing the name of the values 
 * @param {String} pagename String containing the name of the route
 * @param {Object} pagenameObject Object containing all value names
 * @returns Object containing the configuration's value names
 */
function getInputNames(dataArray, name, pagename, pagenameObject) {
  for (var arrayindex in dataArray) {
    var currentObject = dataArray[arrayindex]; // if the currentObject contains a route-property there is the possibility that the nested 
    // properties could also include a route property

    if (Object.keys(currentObject).includes("route")) {
      pagename = currentObject.route;
      name = currentObject.name;
      getInputNames(currentObject.children, name, pagename, pagenameObject);
    } else {
      // create property if not already existing
      if (!Object.keys(pagenameObject).includes(pagename)) {
        pagenameObject[pagename] = {};
      }

      if (!Object.keys(pagenameObject[pagename]).includes(name)) {
        pagenameObject[pagename][name] = [{}];
      }

      var valueName = currentObject.name;
      pagenameObject[pagename][name][0][valueName] = ""; // there is the possibility for deep nested group values 

      var subObject = {};

      if (Object.keys(currentObject).includes("children")) {
        var subValueNames = currentObject.children.map(function (dataset) {
          return dataset.name;
        });

        for (var subIndex in subValueNames) {
          subObject[subValueNames[subIndex]] = "";
        }

        pagenameObject[pagename][name][0][valueName] = [subObject];
      }
    }
  }

  return pagenameObject;
}
/**
 * @description Exracts all page names from configuration given by the route-property
 * @param {Object} config Object containing the input configuration properties
 * @returns Array containing all pagenames
 */


function getPageNames(config, profile) {
  var names = [];
  var mainNames = config.map(function (dataset) {
    return dataset.route;
  });

  for (var nameIndex in mainNames) {
    var subNames = config.filter(function (dataset) {
      return dataset.route === mainNames[nameIndex];
    }).map(function (dataset) {
      return dataset.children;
    })[0].filter(function (dataset) {
      return dataset.route;
    }).map(function (dataset) {
      return dataset.route;
    });

    if (subNames.length > 0) {
      for (var subNameIndex in subNames) {
        var inputName = "inputValues_" + profile + "_" + mainNames[nameIndex] + "_" + subNames[subNameIndex];

        if (!names.includes(inputName)) {
          names.push(inputName);
        }
      }
    } else {
      inputName = "inputValues_" + profile + "_" + mainNames[nameIndex];

      if (!names.includes(inputName)) {
        names.push(inputName);
      }
    }
  }

  return names;
}
/**
 * @description Changes pagenames within the inputValues Object to other pagenames
 * @param {Object} inputValues Object containing all input value names 
 * @param {Array} names Array containing all page names 
 * @returns Object containing all input value names with changed page name
 */


function changePageName(inputValues, names) {
  var newObject = {};

  for (var key in inputValues) {
    var new_key = names.filter(function (dataset) {
      return dataset.endsWith(key);
    })[0];
    newObject[new_key] = inputValues[key];
  }

  return newObject;
}
/**
 * @description Populates the inputValue object with values provided
 * @param {Object} inputValues Object conatining the input values 
 * @param {Array} jsonldData Array containing data to popoulate the inputValues object with 
 * @param {String} profile String determining where to populate values 
 * @returns Object containing input configurations with values
 */


function populateValues(inputValues, jsonldData, profile) {
  for (var pagename in inputValues) {
    var currentPage = inputValues[pagename];
    var inputName = Object.keys(currentPage)[0];

    if (inputName.startsWith(profile)) {
      for (var index in currentPage[inputName]) {
        var valueObject = currentPage[inputName][index];

        for (var key in valueObject) {
          var shortenedKey = key.substr(key.indexOf(":") + 1);

          try {
            var values = jsonldData.map(function (dataset) {
              return dataset[shortenedKey];
            });

            if (values.length > 1) {
              valueObject[key] = values;
            } else {
              try {
                valueObject[key][0]["@value"] = values[0];
              } catch (e) {
                valueObject[key] = values[0];
              }
            }
          } catch (e) {
            valueObject[key] = "";
          }
        }
      }
    }
  }

  return inputValues;
}
/**
 * @description Saves the values contained within the given Object into the localStorage
 * @param {Object} values Object containing input values
 */


function saveToLocalStorage(values) {
  localStorage.clear();

  for (var inputKey in values) {
    var currentData = values[inputKey];
    localStorage.setItem(inputKey, JSON.stringify(currentData));
  }
}
/**
 * @description Fetches JSON-LD data from endpoint
 * @param {String} endpoint Determining the endpoint from where the data should be fetched
 * @returns Object containing fetched data
 */


function fetchJsonld(_x) {
  return _fetchJsonld.apply(this, arguments);
}
/**
 * @description Converts JSON-LD data which is fetched using the given parameters into an object containing input values
 * which will be then stored into the localStorage where the input form will grep them and use them as default values
 * @param {String} profile Serves as snipped of fetch-ednpoint as well as determining which DCATAP-property is dealed with
 * @param {String} id Serving as snipped of tech-endpoint
 */


function _fetchJsonld() {
  _fetchJsonld = Object(_builds_piveau_hub_piveau_hub_forms_node_modules_babel_runtime_helpers_esm_asyncToGenerator__WEBPACK_IMPORTED_MODULE_0__[/* default */ "a"])( /*#__PURE__*/regeneratorRuntime.mark(function _callee(endpoint) {
    var response;
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            _context.next = 2;
            return fetch(endpoint).then(function (response) {
              return response.json();
            });

          case 2:
            response = _context.sent;
            return _context.abrupt("return", response);

          case 4:
          case "end":
            return _context.stop();
        }
      }
    }, _callee);
  }));
  return _fetchJsonld.apply(this, arguments);
}

function convertToInput(_x2, _x3) {
  return _convertToInput.apply(this, arguments);
}

function _convertToInput() {
  _convertToInput = Object(_builds_piveau_hub_piveau_hub_forms_node_modules_babel_runtime_helpers_esm_asyncToGenerator__WEBPACK_IMPORTED_MODULE_0__[/* default */ "a"])( /*#__PURE__*/regeneratorRuntime.mark(function _callee2(profile, id) {
    var valueNames, pageNames, endpoint, jsonld, graphdata, dataset_data, distribution_data, catalogue_data;
    return regeneratorRuntime.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            valueNames = getInputNames(this.$store.getters.getConfig(profile), "", "", {});
            pageNames = getPageNames(this.$store.getters.getConfig(profile), profile);
            valueNames = changePageName(valueNames, pageNames);
            id;
            endpoint = eval("`" + this.$store.getters.getEndpoint[profile] + "`");
            _context2.next = 7;
            return fetchJsonld(endpoint);

          case 7:
            jsonld = _context2.sent;
            graphdata = jsonld["@graph"];

            if (profile === "dataset") {
              dataset_data = graphdata.filter(function (dataset) {
                return dataset["@type"] === "dcat:Dataset";
              });
              valueNames = populateValues(valueNames, dataset_data, profile);
              distribution_data = graphdata.filter(function (dataset) {
                return dataset["@type"] === "dcat:Distribution";
              });
              valueNames = populateValues(valueNames, distribution_data, "distribution");
              saveToLocalStorage(valueNames);
            } else if (profile === "catalogue") {
              catalogue_data = graphdata.filter(function (dataset) {
                return dataset["@type"] === "dcat:Catalogue";
              });
              valueNames = populateValues(valueNames, catalogue_data, profile);
              saveToLocalStorage(valueNames);
            }

          case 10:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, this);
  }));
  return _convertToInput.apply(this, arguments);
}

/***/ }),

/***/ "5692":
/***/ (function(module, exports, __webpack_require__) {

var IS_PURE = __webpack_require__("c430");
var store = __webpack_require__("c6cd");

(module.exports = function (key, value) {
  return store[key] || (store[key] = value !== undefined ? value : {});
})('versions', []).push({
  version: '3.10.1',
  mode: IS_PURE ? 'pure' : 'global',
  copyright: '© 2021 Denis Pushkarev (zloirock.ru)'
});


/***/ }),

/***/ "56ef":
/***/ (function(module, exports, __webpack_require__) {

var getBuiltIn = __webpack_require__("d066");
var getOwnPropertyNamesModule = __webpack_require__("241c");
var getOwnPropertySymbolsModule = __webpack_require__("7418");
var anObject = __webpack_require__("825a");

// all object keys, includes non-enumerable and symbols
module.exports = getBuiltIn('Reflect', 'ownKeys') || function ownKeys(it) {
  var keys = getOwnPropertyNamesModule.f(anObject(it));
  var getOwnPropertySymbols = getOwnPropertySymbolsModule.f;
  return getOwnPropertySymbols ? keys.concat(getOwnPropertySymbols(it)) : keys;
};


/***/ }),

/***/ "5727":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* jshint esversion: 6 */
/* jslint node: true */


module.exports = function serialize (object) {
  if (object === null || typeof object !== 'object' || object.toJSON != null) {
    return JSON.stringify(object);
  }

  if (Array.isArray(object)) {
    return '[' + object.reduce((t, cv, ci) => {
      const comma = ci === 0 ? '' : ',';
      const value = cv === undefined || typeof cv === 'symbol' ? null : cv;
      return t + comma + serialize(value);
    }, '') + ']';
  }

  return '{' + Object.keys(object).sort().reduce((t, cv, ci) => {
    if (object[cv] === undefined ||
        typeof object[cv] === 'symbol') {
      return t;
    }
    const comma = t.length === 0 ? '' : ',';
    return t + comma + serialize(cv) + ':' + serialize(object[cv]);
  }, '') + '}';
};


/***/ }),

/***/ "5870":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_MainComponent_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("cfaa");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_MainComponent_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_MainComponent_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "5950":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


module.exports = class JsonLdError extends Error {
  /**
   * Creates a JSON-LD Error.
   *
   * @param msg the error message.
   * @param type the error type.
   * @param details the error details.
   */
  constructor(
    message = 'An unspecified JSON-LD error occurred.',
    name = 'jsonld.Error',
    details = {}) {
    super(message);
    this.name = name;
    this.message = message;
    this.details = details;
  }
};


/***/ }),

/***/ "5a34":
/***/ (function(module, exports, __webpack_require__) {

var isRegExp = __webpack_require__("44e7");

module.exports = function (it) {
  if (isRegExp(it)) {
    throw TypeError("The method doesn't accept regular expressions");
  } return it;
};


/***/ }),

/***/ "5a76":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


__webpack_require__("6017");

const crypto = self.crypto || self.msCrypto;

// TODO: synchronous version no longer supported in browser

module.exports = class MessageDigest {
  /**
   * Creates a new MessageDigest.
   *
   * @param algorithm the algorithm to use.
   */
  constructor(algorithm) {
    // check if crypto.subtle is available
    // check is here rather than top-level to only fail if class is used
    if(!(crypto && crypto.subtle)) {
      throw new Error('crypto.subtle not found.');
    }
    if(algorithm === 'sha256') {
      this.algorithm = {name: 'SHA-256'};
    } else if(algorithm === 'sha1') {
      this.algorithm = {name: 'SHA-1'};
    } else {
      throw new Error(`Unsupport algorithm "${algorithm}".`);
    }
    this._content = '';
  }

  update(msg) {
    this._content += msg;
  }

  async digest() {
    const data = new TextEncoder().encode(this._content);
    const buffer = new Uint8Array(
      await crypto.subtle.digest(this.algorithm, data));
    // return digest in hex
    let hex = '';
    for(let i = 0; i < buffer.length; ++i) {
      hex += buffer[i].toString(16).padStart(2, '0');
    }
    return hex;
  }
};


/***/ }),

/***/ "5c6c":
/***/ (function(module, exports) {

module.exports = function (bitmap, value) {
  return {
    enumerable: !(bitmap & 1),
    configurable: !(bitmap & 2),
    writable: !(bitmap & 4),
    value: value
  };
};


/***/ }),

/***/ "6017":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global, process) {(function (global, undefined) {
    "use strict";

    if (global.setImmediate) {
        return;
    }

    var nextHandle = 1; // Spec says greater than zero
    var tasksByHandle = {};
    var currentlyRunningATask = false;
    var doc = global.document;
    var registerImmediate;

    function setImmediate(callback) {
      // Callback can either be a function or a string
      if (typeof callback !== "function") {
        callback = new Function("" + callback);
      }
      // Copy function arguments
      var args = new Array(arguments.length - 1);
      for (var i = 0; i < args.length; i++) {
          args[i] = arguments[i + 1];
      }
      // Store and register the task
      var task = { callback: callback, args: args };
      tasksByHandle[nextHandle] = task;
      registerImmediate(nextHandle);
      return nextHandle++;
    }

    function clearImmediate(handle) {
        delete tasksByHandle[handle];
    }

    function run(task) {
        var callback = task.callback;
        var args = task.args;
        switch (args.length) {
        case 0:
            callback();
            break;
        case 1:
            callback(args[0]);
            break;
        case 2:
            callback(args[0], args[1]);
            break;
        case 3:
            callback(args[0], args[1], args[2]);
            break;
        default:
            callback.apply(undefined, args);
            break;
        }
    }

    function runIfPresent(handle) {
        // From the spec: "Wait until any invocations of this algorithm started before this one have completed."
        // So if we're currently running a task, we'll need to delay this invocation.
        if (currentlyRunningATask) {
            // Delay by doing a setTimeout. setImmediate was tried instead, but in Firefox 7 it generated a
            // "too much recursion" error.
            setTimeout(runIfPresent, 0, handle);
        } else {
            var task = tasksByHandle[handle];
            if (task) {
                currentlyRunningATask = true;
                try {
                    run(task);
                } finally {
                    clearImmediate(handle);
                    currentlyRunningATask = false;
                }
            }
        }
    }

    function installNextTickImplementation() {
        registerImmediate = function(handle) {
            process.nextTick(function () { runIfPresent(handle); });
        };
    }

    function canUsePostMessage() {
        // The test against `importScripts` prevents this implementation from being installed inside a web worker,
        // where `global.postMessage` means something completely different and can't be used for this purpose.
        if (global.postMessage && !global.importScripts) {
            var postMessageIsAsynchronous = true;
            var oldOnMessage = global.onmessage;
            global.onmessage = function() {
                postMessageIsAsynchronous = false;
            };
            global.postMessage("", "*");
            global.onmessage = oldOnMessage;
            return postMessageIsAsynchronous;
        }
    }

    function installPostMessageImplementation() {
        // Installs an event handler on `global` for the `message` event: see
        // * https://developer.mozilla.org/en/DOM/window.postMessage
        // * http://www.whatwg.org/specs/web-apps/current-work/multipage/comms.html#crossDocumentMessages

        var messagePrefix = "setImmediate$" + Math.random() + "$";
        var onGlobalMessage = function(event) {
            if (event.source === global &&
                typeof event.data === "string" &&
                event.data.indexOf(messagePrefix) === 0) {
                runIfPresent(+event.data.slice(messagePrefix.length));
            }
        };

        if (global.addEventListener) {
            global.addEventListener("message", onGlobalMessage, false);
        } else {
            global.attachEvent("onmessage", onGlobalMessage);
        }

        registerImmediate = function(handle) {
            global.postMessage(messagePrefix + handle, "*");
        };
    }

    function installMessageChannelImplementation() {
        var channel = new MessageChannel();
        channel.port1.onmessage = function(event) {
            var handle = event.data;
            runIfPresent(handle);
        };

        registerImmediate = function(handle) {
            channel.port2.postMessage(handle);
        };
    }

    function installReadyStateChangeImplementation() {
        var html = doc.documentElement;
        registerImmediate = function(handle) {
            // Create a <script> element; its readystatechange event will be fired asynchronously once it is inserted
            // into the document. Do so, thus queuing up the task. Remember to clean up once it's been called.
            var script = doc.createElement("script");
            script.onreadystatechange = function () {
                runIfPresent(handle);
                script.onreadystatechange = null;
                html.removeChild(script);
                script = null;
            };
            html.appendChild(script);
        };
    }

    function installSetTimeoutImplementation() {
        registerImmediate = function(handle) {
            setTimeout(runIfPresent, 0, handle);
        };
    }

    // If supported, we should attach to the prototype of global, since that is where setTimeout et al. live.
    var attachTo = Object.getPrototypeOf && Object.getPrototypeOf(global);
    attachTo = attachTo && attachTo.setTimeout ? attachTo : global;

    // Don't get fooled by e.g. browserify environments.
    if ({}.toString.call(global.process) === "[object process]") {
        // For Node.js before 0.9
        installNextTickImplementation();

    } else if (canUsePostMessage()) {
        // For non-IE10 modern browsers
        installPostMessageImplementation();

    } else if (global.MessageChannel) {
        // For web workers, where supported
        installMessageChannelImplementation();

    } else if (doc && "onreadystatechange" in doc.createElement("script")) {
        // For IE 6–8
        installReadyStateChangeImplementation();

    } else {
        // For older browsers
        installSetTimeoutImplementation();
    }

    attachTo.setImmediate = setImmediate;
    attachTo.clearImmediate = clearImmediate;
}(typeof self === "undefined" ? typeof global === "undefined" ? this : global : self));

/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__("c8ba"), __webpack_require__("4362")))

/***/ }),

/***/ "605d":
/***/ (function(module, exports, __webpack_require__) {

var classof = __webpack_require__("c6b6");
var global = __webpack_require__("da84");

module.exports = classof(global.process) == 'process';


/***/ }),

/***/ "60da":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var DESCRIPTORS = __webpack_require__("83ab");
var fails = __webpack_require__("d039");
var objectKeys = __webpack_require__("df75");
var getOwnPropertySymbolsModule = __webpack_require__("7418");
var propertyIsEnumerableModule = __webpack_require__("d1e79");
var toObject = __webpack_require__("7b0b");
var IndexedObject = __webpack_require__("44ad");

// eslint-disable-next-line es/no-object-assign -- safe
var $assign = Object.assign;
// eslint-disable-next-line es/no-object-defineproperty -- required for testing
var defineProperty = Object.defineProperty;

// `Object.assign` method
// https://tc39.es/ecma262/#sec-object.assign
module.exports = !$assign || fails(function () {
  // should have correct order of operations (Edge bug)
  if (DESCRIPTORS && $assign({ b: 1 }, $assign(defineProperty({}, 'a', {
    enumerable: true,
    get: function () {
      defineProperty(this, 'b', {
        value: 3,
        enumerable: false
      });
    }
  }), { b: 2 })).b !== 1) return true;
  // should work with symbols and should have deterministic property order (V8 bug)
  var A = {};
  var B = {};
  // eslint-disable-next-line es/no-symbol -- safe
  var symbol = Symbol();
  var alphabet = 'abcdefghijklmnopqrst';
  A[symbol] = 7;
  alphabet.split('').forEach(function (chr) { B[chr] = chr; });
  return $assign({}, A)[symbol] != 7 || objectKeys($assign({}, B)).join('') != alphabet;
}) ? function assign(target, source) { // eslint-disable-line no-unused-vars -- required for `.length`
  var T = toObject(target);
  var argumentsLength = arguments.length;
  var index = 1;
  var getOwnPropertySymbols = getOwnPropertySymbolsModule.f;
  var propertyIsEnumerable = propertyIsEnumerableModule.f;
  while (argumentsLength > index) {
    var S = IndexedObject(arguments[index++]);
    var keys = getOwnPropertySymbols ? objectKeys(S).concat(getOwnPropertySymbols(S)) : objectKeys(S);
    var length = keys.length;
    var j = 0;
    var key;
    while (length > j) {
      key = keys[j++];
      if (!DESCRIPTORS || propertyIsEnumerable.call(S, key)) T[key] = S[key];
    }
  } return T;
} : $assign;


/***/ }),

/***/ "610b":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_InfoSlot_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("b4bf");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_InfoSlot_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_InfoSlot_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "6547":
/***/ (function(module, exports, __webpack_require__) {

var toInteger = __webpack_require__("a691");
var requireObjectCoercible = __webpack_require__("1d80");

// `String.prototype.{ codePointAt, at }` methods implementation
var createMethod = function (CONVERT_TO_STRING) {
  return function ($this, pos) {
    var S = String(requireObjectCoercible($this));
    var position = toInteger(pos);
    var size = S.length;
    var first, second;
    if (position < 0 || position >= size) return CONVERT_TO_STRING ? '' : undefined;
    first = S.charCodeAt(position);
    return first < 0xD800 || first > 0xDBFF || position + 1 === size
      || (second = S.charCodeAt(position + 1)) < 0xDC00 || second > 0xDFFF
        ? CONVERT_TO_STRING ? S.charAt(position) : first
        : CONVERT_TO_STRING ? S.slice(position, position + 2) : (first - 0xD800 << 10) + (second - 0xDC00) + 0x10000;
  };
};

module.exports = {
  // `String.prototype.codePointAt` method
  // https://tc39.es/ecma262/#sec-string.prototype.codepointat
  codeAt: createMethod(false),
  // `String.prototype.at` method
  // https://github.com/mathiasbynens/String.prototype.at
  charAt: createMethod(true)
};


/***/ }),

/***/ "65c2":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


// TODO: move `NQuads` to its own package
module.exports = __webpack_require__("4b45").NQuads;


/***/ }),

/***/ "65e1":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


// eslint-disable-next-line no-unused-vars
const TERMS = ['subject', 'predicate', 'object', 'graph'];
const RDF = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#';
const RDF_LANGSTRING = RDF + 'langString';
const XSD_STRING = 'http://www.w3.org/2001/XMLSchema#string';

const TYPE_NAMED_NODE = 'NamedNode';
const TYPE_BLANK_NODE = 'BlankNode';
const TYPE_LITERAL = 'Literal';
const TYPE_DEFAULT_GRAPH = 'DefaultGraph';

// build regexes
const REGEX = {};
(() => {
  const iri = '(?:<([^:]+:[^>]*)>)';
  // https://www.w3.org/TR/turtle/#grammar-production-BLANK_NODE_LABEL
  const PN_CHARS_BASE =
    'A-Z' + 'a-z' +
    '\u00C0-\u00D6' +
    '\u00D8-\u00F6' +
    '\u00F8-\u02FF' +
    '\u0370-\u037D' +
    '\u037F-\u1FFF' +
    '\u200C-\u200D' +
    '\u2070-\u218F' +
    '\u2C00-\u2FEF' +
    '\u3001-\uD7FF' +
    '\uF900-\uFDCF' +
    '\uFDF0-\uFFFD';
    // TODO:
    //'\u10000-\uEFFFF';
  const PN_CHARS_U =
    PN_CHARS_BASE +
    '_';
  const PN_CHARS =
    PN_CHARS_U +
    '0-9' +
    '-' +
    '\u00B7' +
    '\u0300-\u036F' +
    '\u203F-\u2040';
  const BLANK_NODE_LABEL =
    '(_:' +
      '(?:[' + PN_CHARS_U + '0-9])' +
      '(?:(?:[' + PN_CHARS + '.])*(?:[' + PN_CHARS + ']))?' +
    ')';
  const bnode = BLANK_NODE_LABEL;
  const plain = '"([^"\\\\]*(?:\\\\.[^"\\\\]*)*)"';
  const datatype = '(?:\\^\\^' + iri + ')';
  const language = '(?:@([a-zA-Z]+(?:-[a-zA-Z0-9]+)*))';
  const literal = '(?:' + plain + '(?:' + datatype + '|' + language + ')?)';
  const ws = '[ \\t]+';
  const wso = '[ \\t]*';

  // define quad part regexes
  const subject = '(?:' + iri + '|' + bnode + ')' + ws;
  const property = iri + ws;
  const object = '(?:' + iri + '|' + bnode + '|' + literal + ')' + wso;
  const graphName = '(?:\\.|(?:(?:' + iri + '|' + bnode + ')' + wso + '\\.))';

  // end of line and empty regexes
  REGEX.eoln = /(?:\r\n)|(?:\n)|(?:\r)/g;
  REGEX.empty = new RegExp('^' + wso + '$');

  // full quad regex
  REGEX.quad = new RegExp(
    '^' + wso + subject + property + object + graphName + wso + '$');
})();

module.exports = class NQuads {
  /**
   * Parses RDF in the form of N-Quads.
   *
   * @param input the N-Quads input to parse.
   *
   * @return an RDF dataset (an array of quads per http://rdf.js.org/).
   */
  static parse(input) {
    // build RDF dataset
    const dataset = [];

    const graphs = {};

    // split N-Quad input into lines
    const lines = input.split(REGEX.eoln);
    let lineNumber = 0;
    for(const line of lines) {
      lineNumber++;

      // skip empty lines
      if(REGEX.empty.test(line)) {
        continue;
      }

      // parse quad
      const match = line.match(REGEX.quad);
      if(match === null) {
        throw new Error('N-Quads parse error on line ' + lineNumber + '.');
      }

      // create RDF quad
      const quad = {subject: null, predicate: null, object: null, graph: null};

      // get subject
      if(match[1] !== undefined) {
        quad.subject = {termType: TYPE_NAMED_NODE, value: match[1]};
      } else {
        quad.subject = {termType: TYPE_BLANK_NODE, value: match[2]};
      }

      // get predicate
      quad.predicate = {termType: TYPE_NAMED_NODE, value: match[3]};

      // get object
      if(match[4] !== undefined) {
        quad.object = {termType: TYPE_NAMED_NODE, value: match[4]};
      } else if(match[5] !== undefined) {
        quad.object = {termType: TYPE_BLANK_NODE, value: match[5]};
      } else {
        quad.object = {
          termType: TYPE_LITERAL,
          value: undefined,
          datatype: {
            termType: TYPE_NAMED_NODE
          }
        };
        if(match[7] !== undefined) {
          quad.object.datatype.value = match[7];
        } else if(match[8] !== undefined) {
          quad.object.datatype.value = RDF_LANGSTRING;
          quad.object.language = match[8];
        } else {
          quad.object.datatype.value = XSD_STRING;
        }
        quad.object.value = _unescape(match[6]);
      }

      // get graph
      if(match[9] !== undefined) {
        quad.graph = {
          termType: TYPE_NAMED_NODE,
          value: match[9]
        };
      } else if(match[10] !== undefined) {
        quad.graph = {
          termType: TYPE_BLANK_NODE,
          value: match[10]
        };
      } else {
        quad.graph = {
          termType: TYPE_DEFAULT_GRAPH,
          value: ''
        };
      }

      // only add quad if it is unique in its graph
      if(!(quad.graph.value in graphs)) {
        graphs[quad.graph.value] = [quad];
        dataset.push(quad);
      } else {
        let unique = true;
        const quads = graphs[quad.graph.value];
        for(const q of quads) {
          if(_compareTriples(q, quad)) {
            unique = false;
            break;
          }
        }
        if(unique) {
          quads.push(quad);
          dataset.push(quad);
        }
      }
    }

    return dataset;
  }

  /**
   * Converts an RDF dataset to N-Quads.
   *
   * @param dataset (array of quads) the RDF dataset to convert.
   *
   * @return the N-Quads string.
   */
  static serialize(dataset) {
    if(!Array.isArray(dataset)) {
      dataset = NQuads.legacyDatasetToQuads(dataset);
    }
    const quads = [];
    for(const quad of dataset) {
      quads.push(NQuads.serializeQuad(quad));
    }
    return quads.sort().join('');
  }

  /**
   * Converts an RDF quad to an N-Quad string (a single quad).
   *
   * @param quad the RDF quad convert.
   *
   * @return the N-Quad string.
   */
  static serializeQuad(quad) {
    const s = quad.subject;
    const p = quad.predicate;
    const o = quad.object;
    const g = quad.graph;

    let nquad = '';

    // subject can only be NamedNode or BlankNode
    if(s.termType === TYPE_NAMED_NODE) {
      nquad += `<${s.value}>`;
    } else {
      nquad += `${s.value}`;
    }

    // predicate can only be NamedNode
    nquad += ` <${p.value}> `;

    // object is NamedNode, BlankNode, or Literal
    if(o.termType === TYPE_NAMED_NODE) {
      nquad += `<${o.value}>`;
    } else if(o.termType === TYPE_BLANK_NODE) {
      nquad += o.value;
    } else {
      nquad += `"${_escape(o.value)}"`;
      if(o.datatype.value === RDF_LANGSTRING) {
        if(o.language) {
          nquad += `@${o.language}`;
        }
      } else if(o.datatype.value !== XSD_STRING) {
        nquad += `^^<${o.datatype.value}>`;
      }
    }

    // graph can only be NamedNode or BlankNode (or DefaultGraph, but that
    // does not add to `nquad`)
    if(g.termType === TYPE_NAMED_NODE) {
      nquad += ` <${g.value}>`;
    } else if(g.termType === TYPE_BLANK_NODE) {
      nquad += ` ${g.value}`;
    }

    nquad += ' .\n';
    return nquad;
  }

  /**
   * Converts a legacy-formatted dataset to an array of quads dataset per
   * http://rdf.js.org/.
   *
   * @param dataset the legacy dataset to convert.
   *
   * @return the array of quads dataset.
   */
  static legacyDatasetToQuads(dataset) {
    const quads = [];

    const termTypeMap = {
      'blank node': TYPE_BLANK_NODE,
      IRI: TYPE_NAMED_NODE,
      literal: TYPE_LITERAL
    };

    for(const graphName in dataset) {
      const triples = dataset[graphName];
      triples.forEach(triple => {
        const quad = {};
        for(const componentName in triple) {
          const oldComponent = triple[componentName];
          const newComponent = {
            termType: termTypeMap[oldComponent.type],
            value: oldComponent.value
          };
          if(newComponent.termType === TYPE_LITERAL) {
            newComponent.datatype = {
              termType: TYPE_NAMED_NODE
            };
            if('datatype' in oldComponent) {
              newComponent.datatype.value = oldComponent.datatype;
            }
            if('language' in oldComponent) {
              if(!('datatype' in oldComponent)) {
                newComponent.datatype.value = RDF_LANGSTRING;
              }
              newComponent.language = oldComponent.language;
            } else if(!('datatype' in oldComponent)) {
              newComponent.datatype.value = XSD_STRING;
            }
          }
          quad[componentName] = newComponent;
        }
        if(graphName === '@default') {
          quad.graph = {
            termType: TYPE_DEFAULT_GRAPH,
            value: ''
          };
        } else {
          quad.graph = {
            termType: graphName.startsWith('_:') ?
              TYPE_BLANK_NODE : TYPE_NAMED_NODE,
            value: graphName
          };
        }
        quads.push(quad);
      });
    }

    return quads;
  }
};

/**
 * Compares two RDF triples for equality.
 *
 * @param t1 the first triple.
 * @param t2 the second triple.
 *
 * @return true if the triples are the same, false if not.
 */
function _compareTriples(t1, t2) {
  // compare subject and object types first as it is the quickest check
  if(!(t1.subject.termType === t2.subject.termType &&
    t1.object.termType === t2.object.termType)) {
    return false;
  }
  // compare values
  if(!(t1.subject.value === t2.subject.value &&
    t1.predicate.value === t2.predicate.value &&
    t1.object.value === t2.object.value)) {
    return false;
  }
  if(t1.object.termType !== TYPE_LITERAL) {
    // no `datatype` or `language` to check
    return true;
  }
  return (
    (t1.object.datatype.termType === t2.object.datatype.termType) &&
    (t1.object.language === t2.object.language) &&
    (t1.object.datatype.value === t2.object.datatype.value)
  );
}

const _escapeRegex = /["\\\n\r]/g;
/**
 * Escape string to N-Quads literal
 */
function _escape(s) {
  return s.replace(_escapeRegex, function(match) {
    switch(match) {
      case '"': return '\\"';
      case '\\': return '\\\\';
      case '\n': return '\\n';
      case '\r': return '\\r';
    }
  });
}

const _unescapeRegex =
  /(?:\\([tbnrf"'\\]))|(?:\\u([0-9A-Fa-f]{4}))|(?:\\U([0-9A-Fa-f]{8}))/g;
/**
 * Unescape N-Quads literal to string
 */
function _unescape(s) {
  return s.replace(_unescapeRegex, function(match, code, u, U) {
    if(code) {
      switch(code) {
        case 't': return '\t';
        case 'b': return '\b';
        case 'n': return '\n';
        case 'r': return '\r';
        case 'f': return '\f';
        case '"': return '"';
        case '\'': return '\'';
        case '\\': return '\\';
      }
    }
    if(u) {
      return String.fromCharCode(parseInt(u, 16));
    }
    if(U) {
      // FIXME: support larger values
      throw new Error('Unsupported U escape');
    }
  });
}


/***/ }),

/***/ "65f0":
/***/ (function(module, exports, __webpack_require__) {

var isObject = __webpack_require__("861d");
var isArray = __webpack_require__("e8b5");
var wellKnownSymbol = __webpack_require__("b622");

var SPECIES = wellKnownSymbol('species');

// `ArraySpeciesCreate` abstract operation
// https://tc39.es/ecma262/#sec-arrayspeciescreate
module.exports = function (originalArray, length) {
  var C;
  if (isArray(originalArray)) {
    C = originalArray.constructor;
    // cross-realm fallback
    if (typeof C == 'function' && (C === Array || isArray(C.prototype))) C = undefined;
    else if (isObject(C)) {
      C = C[SPECIES];
      if (C === null) C = undefined;
    }
  } return new (C === undefined ? Array : C)(length === 0 ? 0 : length);
};


/***/ }),

/***/ "68fa":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const api = {};
module.exports = api;

/**
 * Returns true if the given value is an Array.
 *
 * @param v the value to check.
 *
 * @return true if the value is an Array, false if not.
 */
api.isArray = Array.isArray;

/**
 * Returns true if the given value is a Boolean.
 *
 * @param v the value to check.
 *
 * @return true if the value is a Boolean, false if not.
 */
api.isBoolean = v => (typeof v === 'boolean' ||
  Object.prototype.toString.call(v) === '[object Boolean]');

/**
 * Returns true if the given value is a double.
 *
 * @param v the value to check.
 *
 * @return true if the value is a double, false if not.
 */
api.isDouble = v => api.isNumber(v) &&
  (String(v).indexOf('.') !== -1 || Math.abs(v) >= 1e21);

/**
 * Returns true if the given value is an empty Object.
 *
 * @param v the value to check.
 *
 * @return true if the value is an empty Object, false if not.
 */
api.isEmptyObject = v => api.isObject(v) && Object.keys(v).length === 0;

/**
 * Returns true if the given value is a Number.
 *
 * @param v the value to check.
 *
 * @return true if the value is a Number, false if not.
 */
api.isNumber = v => (typeof v === 'number' ||
  Object.prototype.toString.call(v) === '[object Number]');

/**
 * Returns true if the given value is numeric.
 *
 * @param v the value to check.
 *
 * @return true if the value is numeric, false if not.
 */
api.isNumeric = v => !isNaN(parseFloat(v)) && isFinite(v);

/**
 * Returns true if the given value is an Object.
 *
 * @param v the value to check.
 *
 * @return true if the value is an Object, false if not.
 */
api.isObject = v => Object.prototype.toString.call(v) === '[object Object]';

/**
 * Returns true if the given value is a String.
 *
 * @param v the value to check.
 *
 * @return true if the value is a String, false if not.
 */
api.isString = v => (typeof v === 'string' ||
  Object.prototype.toString.call(v) === '[object String]');

/**
 * Returns true if the given value is undefined.
 *
 * @param v the value to check.
 *
 * @return true if the value is undefined, false if not.
 */
api.isUndefined = v => typeof v === 'undefined';


/***/ }),

/***/ "69f3":
/***/ (function(module, exports, __webpack_require__) {

var NATIVE_WEAK_MAP = __webpack_require__("7f9a");
var global = __webpack_require__("da84");
var isObject = __webpack_require__("861d");
var createNonEnumerableProperty = __webpack_require__("9112");
var objectHas = __webpack_require__("5135");
var shared = __webpack_require__("c6cd");
var sharedKey = __webpack_require__("f772");
var hiddenKeys = __webpack_require__("d012");

var WeakMap = global.WeakMap;
var set, get, has;

var enforce = function (it) {
  return has(it) ? get(it) : set(it, {});
};

var getterFor = function (TYPE) {
  return function (it) {
    var state;
    if (!isObject(it) || (state = get(it)).type !== TYPE) {
      throw TypeError('Incompatible receiver, ' + TYPE + ' required');
    } return state;
  };
};

if (NATIVE_WEAK_MAP) {
  var store = shared.state || (shared.state = new WeakMap());
  var wmget = store.get;
  var wmhas = store.has;
  var wmset = store.set;
  set = function (it, metadata) {
    metadata.facade = it;
    wmset.call(store, it, metadata);
    return metadata;
  };
  get = function (it) {
    return wmget.call(store, it) || {};
  };
  has = function (it) {
    return wmhas.call(store, it);
  };
} else {
  var STATE = sharedKey('state');
  hiddenKeys[STATE] = true;
  set = function (it, metadata) {
    metadata.facade = it;
    createNonEnumerableProperty(it, STATE, metadata);
    return metadata;
  };
  get = function (it) {
    return objectHas(it, STATE) ? it[STATE] : {};
  };
  has = function (it) {
    return objectHas(it, STATE);
  };
}

module.exports = {
  set: set,
  get: get,
  has: has,
  enforce: enforce,
  getterFor: getterFor
};


/***/ }),

/***/ "6eeb":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var createNonEnumerableProperty = __webpack_require__("9112");
var has = __webpack_require__("5135");
var setGlobal = __webpack_require__("ce4e");
var inspectSource = __webpack_require__("8925");
var InternalStateModule = __webpack_require__("69f3");

var getInternalState = InternalStateModule.get;
var enforceInternalState = InternalStateModule.enforce;
var TEMPLATE = String(String).split('String');

(module.exports = function (O, key, value, options) {
  var unsafe = options ? !!options.unsafe : false;
  var simple = options ? !!options.enumerable : false;
  var noTargetGet = options ? !!options.noTargetGet : false;
  var state;
  if (typeof value == 'function') {
    if (typeof key == 'string' && !has(value, 'name')) {
      createNonEnumerableProperty(value, 'name', key);
    }
    state = enforceInternalState(value);
    if (!state.source) {
      state.source = TEMPLATE.join(typeof key == 'string' ? key : '');
    }
  }
  if (O === global) {
    if (simple) O[key] = value;
    else setGlobal(key, value);
    return;
  } else if (!unsafe) {
    delete O[key];
  } else if (!noTargetGet && O[key]) {
    simple = true;
  }
  if (simple) O[key] = value;
  else createNonEnumerableProperty(O, key, value);
// add fake Function#toString for correct work wrapped methods / constructors with methods like LoDash isNative
})(Function.prototype, 'toString', function toString() {
  return typeof this == 'function' && getInternalState(this).source || inspectSource(this);
});


/***/ }),

/***/ "6f5c":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const JsonLdError = __webpack_require__("5950");
const graphTypes = __webpack_require__("c92f");
const types = __webpack_require__("68fa");
const util = __webpack_require__("89ae");

// constants
const {
  // RDF,
  RDF_LIST,
  RDF_FIRST,
  RDF_REST,
  RDF_NIL,
  RDF_TYPE,
  // RDF_PLAIN_LITERAL,
  // RDF_XML_LITERAL,
  RDF_JSON_LITERAL,
  // RDF_OBJECT,
  // RDF_LANGSTRING,

  // XSD,
  XSD_BOOLEAN,
  XSD_DOUBLE,
  XSD_INTEGER,
  XSD_STRING,
} = __webpack_require__("feb1");

const REGEX_BCP47 = /^[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*$/;

const api = {};
module.exports = api;

/**
 * Converts an RDF dataset to JSON-LD.
 *
 * @param dataset the RDF dataset.
 * @param options the RDF serialization options.
 *
 * @return a Promise that resolves to the JSON-LD output.
 */
api.fromRDF = async (
  dataset,
  {
    useRdfType = false,
    useNativeTypes = false,
    rdfDirection = null
  }
) => {
  const defaultGraph = {};
  const graphMap = {'@default': defaultGraph};
  const referencedOnce = {};

  for(const quad of dataset) {
    // TODO: change 'name' to 'graph'
    const name = (quad.graph.termType === 'DefaultGraph') ?
      '@default' : quad.graph.value;
    if(!(name in graphMap)) {
      graphMap[name] = {};
    }
    if(name !== '@default' && !(name in defaultGraph)) {
      defaultGraph[name] = {'@id': name};
    }

    const nodeMap = graphMap[name];

    // get subject, predicate, object
    const s = quad.subject.value;
    const p = quad.predicate.value;
    const o = quad.object;

    if(!(s in nodeMap)) {
      nodeMap[s] = {'@id': s};
    }
    const node = nodeMap[s];

    const objectIsNode = o.termType.endsWith('Node');
    if(objectIsNode && !(o.value in nodeMap)) {
      nodeMap[o.value] = {'@id': o.value};
    }

    if(p === RDF_TYPE && !useRdfType && objectIsNode) {
      util.addValue(node, '@type', o.value, {propertyIsArray: true});
      continue;
    }

    const value = _RDFToObject(o, useNativeTypes, rdfDirection);
    util.addValue(node, p, value, {propertyIsArray: true});

    // object may be an RDF list/partial list node but we can't know easily
    // until all triples are read
    if(objectIsNode) {
      if(o.value === RDF_NIL) {
        // track rdf:nil uniquely per graph
        const object = nodeMap[o.value];
        if(!('usages' in object)) {
          object.usages = [];
        }
        object.usages.push({
          node,
          property: p,
          value
        });
      } else if(o.value in referencedOnce) {
        // object referenced more than once
        referencedOnce[o.value] = false;
      } else {
        // keep track of single reference
        referencedOnce[o.value] = {
          node,
          property: p,
          value
        };
      }
    }
  }

  /*
  for(let name in dataset) {
    const graph = dataset[name];
    if(!(name in graphMap)) {
      graphMap[name] = {};
    }
    if(name !== '@default' && !(name in defaultGraph)) {
      defaultGraph[name] = {'@id': name};
    }
    const nodeMap = graphMap[name];
    for(let ti = 0; ti < graph.length; ++ti) {
      const triple = graph[ti];

      // get subject, predicate, object
      const s = triple.subject.value;
      const p = triple.predicate.value;
      const o = triple.object;

      if(!(s in nodeMap)) {
        nodeMap[s] = {'@id': s};
      }
      const node = nodeMap[s];

      const objectIsId = (o.type === 'IRI' || o.type === 'blank node');
      if(objectIsId && !(o.value in nodeMap)) {
        nodeMap[o.value] = {'@id': o.value};
      }

      if(p === RDF_TYPE && !useRdfType && objectIsId) {
        util.addValue(node, '@type', o.value, {propertyIsArray: true});
        continue;
      }

      const value = _RDFToObject(o, useNativeTypes);
      util.addValue(node, p, value, {propertyIsArray: true});

      // object may be an RDF list/partial list node but we can't know easily
      // until all triples are read
      if(objectIsId) {
        if(o.value === RDF_NIL) {
          // track rdf:nil uniquely per graph
          const object = nodeMap[o.value];
          if(!('usages' in object)) {
            object.usages = [];
          }
          object.usages.push({
            node: node,
            property: p,
            value: value
          });
        } else if(o.value in referencedOnce) {
          // object referenced more than once
          referencedOnce[o.value] = false;
        } else {
          // keep track of single reference
          referencedOnce[o.value] = {
            node: node,
            property: p,
            value: value
          };
        }
      }
    }
  }*/

  // convert linked lists to @list arrays
  for(const name in graphMap) {
    const graphObject = graphMap[name];

    // no @lists to be converted, continue
    if(!(RDF_NIL in graphObject)) {
      continue;
    }

    // iterate backwards through each RDF list
    const nil = graphObject[RDF_NIL];
    if(!nil.usages) {
      continue;
    }
    for(let usage of nil.usages) {
      let node = usage.node;
      let property = usage.property;
      let head = usage.value;
      const list = [];
      const listNodes = [];

      // ensure node is a well-formed list node; it must:
      // 1. Be referenced only once.
      // 2. Have an array for rdf:first that has 1 item.
      // 3. Have an array for rdf:rest that has 1 item.
      // 4. Have no keys other than: @id, rdf:first, rdf:rest, and,
      //   optionally, @type where the value is rdf:List.
      let nodeKeyCount = Object.keys(node).length;
      while(property === RDF_REST &&
        types.isObject(referencedOnce[node['@id']]) &&
        types.isArray(node[RDF_FIRST]) && node[RDF_FIRST].length === 1 &&
        types.isArray(node[RDF_REST]) && node[RDF_REST].length === 1 &&
        (nodeKeyCount === 3 ||
          (nodeKeyCount === 4 && types.isArray(node['@type']) &&
          node['@type'].length === 1 && node['@type'][0] === RDF_LIST))) {
        list.push(node[RDF_FIRST][0]);
        listNodes.push(node['@id']);

        // get next node, moving backwards through list
        usage = referencedOnce[node['@id']];
        node = usage.node;
        property = usage.property;
        head = usage.value;
        nodeKeyCount = Object.keys(node).length;

        // if node is not a blank node, then list head found
        if(!graphTypes.isBlankNode(node)) {
          break;
        }
      }

      // transform list into @list object
      delete head['@id'];
      head['@list'] = list.reverse();
      for(const listNode of listNodes) {
        delete graphObject[listNode];
      }
    }

    delete nil.usages;
  }

  const result = [];
  const subjects = Object.keys(defaultGraph).sort();
  for(const subject of subjects) {
    const node = defaultGraph[subject];
    if(subject in graphMap) {
      const graph = node['@graph'] = [];
      const graphObject = graphMap[subject];
      const graphSubjects = Object.keys(graphObject).sort();
      for(const graphSubject of graphSubjects) {
        const node = graphObject[graphSubject];
        // only add full subjects to top-level
        if(!graphTypes.isSubjectReference(node)) {
          graph.push(node);
        }
      }
    }
    // only add full subjects to top-level
    if(!graphTypes.isSubjectReference(node)) {
      result.push(node);
    }
  }

  return result;
};

/**
 * Converts an RDF triple object to a JSON-LD object.
 *
 * @param o the RDF triple object to convert.
 * @param useNativeTypes true to output native types, false not to.
 *
 * @return the JSON-LD object.
 */
function _RDFToObject(o, useNativeTypes, rdfDirection) {
  // convert NamedNode/BlankNode object to JSON-LD
  if(o.termType.endsWith('Node')) {
    return {'@id': o.value};
  }

  // convert literal to JSON-LD
  const rval = {'@value': o.value};

  // add language
  if(o.language) {
    rval['@language'] = o.language;
  } else {
    let type = o.datatype.value;
    if(!type) {
      type = XSD_STRING;
    }
    if(type === RDF_JSON_LITERAL) {
      type = '@json';
      try {
        rval['@value'] = JSON.parse(rval['@value']);
      } catch(e) {
        throw new JsonLdError(
          'JSON literal could not be parsed.',
          'jsonld.InvalidJsonLiteral',
          {code: 'invalid JSON literal', value: rval['@value'], cause: e});
      }
    }
    // use native types for certain xsd types
    if(useNativeTypes) {
      if(type === XSD_BOOLEAN) {
        if(rval['@value'] === 'true') {
          rval['@value'] = true;
        } else if(rval['@value'] === 'false') {
          rval['@value'] = false;
        }
      } else if(types.isNumeric(rval['@value'])) {
        if(type === XSD_INTEGER) {
          const i = parseInt(rval['@value'], 10);
          if(i.toFixed(0) === rval['@value']) {
            rval['@value'] = i;
          }
        } else if(type === XSD_DOUBLE) {
          rval['@value'] = parseFloat(rval['@value']);
        }
      }
      // do not add native type
      if(![XSD_BOOLEAN, XSD_INTEGER, XSD_DOUBLE, XSD_STRING].includes(type)) {
        rval['@type'] = type;
      }
    } else if(rdfDirection === 'i18n-datatype' &&
      type.startsWith('https://www.w3.org/ns/i18n#')) {
      const [, language, direction] = type.split(/[#_]/);
      if(language.length > 0) {
        rval['@language'] = language;
        if(!language.match(REGEX_BCP47)) {
          console.warn(`@language must be valid BCP47: ${language}`);
        }
      }
      rval['@direction'] = direction;
    } else if(type !== XSD_STRING) {
      rval['@type'] = type;
    }
  }

  return rval;
}


/***/ }),

/***/ "7156":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const {parseLinkHeader, buildHeaders} = __webpack_require__("89ae");
const {LINK_HEADER_CONTEXT} = __webpack_require__("feb1");
const JsonLdError = __webpack_require__("5950");
const RequestQueue = __webpack_require__("40ce");
const {prependBase} = __webpack_require__("ddd7");

const REGEX_LINK_HEADER = /(^|(\r\n))link:/i;

/**
 * Creates a built-in XMLHttpRequest document loader.
 *
 * @param options the options to use:
 *          secure: require all URLs to use HTTPS.
 *          headers: an object (map) of headers which will be passed as request
 *            headers for the requested document. Accept is not allowed.
 *          [xhr]: the XMLHttpRequest API to use.
 *
 * @return the XMLHttpRequest document loader.
 */
module.exports = ({
  secure,
  headers = {},
  xhr
} = {headers: {}}) => {
  headers = buildHeaders(headers);
  const queue = new RequestQueue();
  return queue.wrapLoader(loader);

  async function loader(url) {
    if(url.indexOf('http:') !== 0 && url.indexOf('https:') !== 0) {
      throw new JsonLdError(
        'URL could not be dereferenced; only "http" and "https" URLs are ' +
        'supported.',
        'jsonld.InvalidUrl', {code: 'loading document failed', url});
    }
    if(secure && url.indexOf('https') !== 0) {
      throw new JsonLdError(
        'URL could not be dereferenced; secure mode is enabled and ' +
        'the URL\'s scheme is not "https".',
        'jsonld.InvalidUrl', {code: 'loading document failed', url});
    }

    let req;
    try {
      req = await _get(xhr, url, headers);
    } catch(e) {
      throw new JsonLdError(
        'URL could not be dereferenced, an error occurred.',
        'jsonld.LoadDocumentError',
        {code: 'loading document failed', url, cause: e});
    }

    if(req.status >= 400) {
      throw new JsonLdError(
        'URL could not be dereferenced: ' + req.statusText,
        'jsonld.LoadDocumentError', {
          code: 'loading document failed',
          url,
          httpStatusCode: req.status
        });
    }

    let doc = {contextUrl: null, documentUrl: url, document: req.response};
    let alternate = null;

    // handle Link Header (avoid unsafe header warning by existence testing)
    const contentType = req.getResponseHeader('Content-Type');
    let linkHeader;
    if(REGEX_LINK_HEADER.test(req.getAllResponseHeaders())) {
      linkHeader = req.getResponseHeader('Link');
    }
    if(linkHeader && contentType !== 'application/ld+json') {
      // only 1 related link header permitted
      const linkHeaders = parseLinkHeader(linkHeader);
      const linkedContext = linkHeaders[LINK_HEADER_CONTEXT];
      if(Array.isArray(linkedContext)) {
        throw new JsonLdError(
          'URL could not be dereferenced, it has more than one ' +
          'associated HTTP Link Header.',
          'jsonld.InvalidUrl',
          {code: 'multiple context link headers', url});
      }
      if(linkedContext) {
        doc.contextUrl = linkedContext.target;
      }

      // "alternate" link header is a redirect
      alternate = linkHeaders['alternate'];
      if(alternate &&
        alternate.type == 'application/ld+json' &&
        !(contentType || '').match(/^application\/(\w*\+)?json$/)) {
        doc = await loader(prependBase(url, alternate.target));
      }
    }

    return doc;
  }
};

function _get(xhr, url, headers) {
  xhr = xhr || XMLHttpRequest;
  const req = new xhr();
  return new Promise((resolve, reject) => {
    req.onload = () => resolve(req);
    req.onerror = err => reject(err);
    req.open('GET', url, true);
    for(const k in headers) {
      req.setRequestHeader(k, headers[k]);
    }
    req.send();
  });
}


/***/ }),

/***/ "7418":
/***/ (function(module, exports) {

// eslint-disable-next-line es/no-object-getownpropertysymbols -- safe
exports.f = Object.getOwnPropertySymbols;


/***/ }),

/***/ "746f":
/***/ (function(module, exports, __webpack_require__) {

var path = __webpack_require__("428f");
var has = __webpack_require__("5135");
var wrappedWellKnownSymbolModule = __webpack_require__("e538");
var defineProperty = __webpack_require__("9bf2").f;

module.exports = function (NAME) {
  var Symbol = path.Symbol || (path.Symbol = {});
  if (!has(Symbol, NAME)) defineProperty(Symbol, NAME, {
    value: wrappedWellKnownSymbolModule.f(NAME)
  });
};


/***/ }),

/***/ "7646":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const {createNodeMap} = __webpack_require__("796c");
const {isKeyword} = __webpack_require__("d1e7");
const graphTypes = __webpack_require__("c92f");
const jsonCanonicalize = __webpack_require__("5727");
const types = __webpack_require__("68fa");
const util = __webpack_require__("89ae");

const {
  // RDF,
  // RDF_LIST,
  RDF_FIRST,
  RDF_REST,
  RDF_NIL,
  RDF_TYPE,
  // RDF_PLAIN_LITERAL,
  // RDF_XML_LITERAL,
  RDF_JSON_LITERAL,
  // RDF_OBJECT,
  RDF_LANGSTRING,

  // XSD,
  XSD_BOOLEAN,
  XSD_DOUBLE,
  XSD_INTEGER,
  XSD_STRING,
} = __webpack_require__("feb1");

const {
  isAbsolute: _isAbsoluteIri
} = __webpack_require__("ddd7");

const api = {};
module.exports = api;

/**
 * Outputs an RDF dataset for the expanded JSON-LD input.
 *
 * @param input the expanded JSON-LD input.
 * @param options the RDF serialization options.
 *
 * @return the RDF dataset.
 */
api.toRDF = (input, options) => {
  // create node map for default graph (and any named graphs)
  const issuer = new util.IdentifierIssuer('_:b');
  const nodeMap = {'@default': {}};
  createNodeMap(input, nodeMap, '@default', issuer);

  const dataset = [];
  const graphNames = Object.keys(nodeMap).sort();
  for(const graphName of graphNames) {
    let graphTerm;
    if(graphName === '@default') {
      graphTerm = {termType: 'DefaultGraph', value: ''};
    } else if(_isAbsoluteIri(graphName)) {
      if(graphName.startsWith('_:')) {
        graphTerm = {termType: 'BlankNode'};
      } else {
        graphTerm = {termType: 'NamedNode'};
      }
      graphTerm.value = graphName;
    } else {
      // skip relative IRIs (not valid RDF)
      continue;
    }
    _graphToRDF(dataset, nodeMap[graphName], graphTerm, issuer, options);
  }

  return dataset;
};

/**
 * Adds RDF quads for a particular graph to the given dataset.
 *
 * @param dataset the dataset to append RDF quads to.
 * @param graph the graph to create RDF quads for.
 * @param graphTerm the graph term for each quad.
 * @param issuer a IdentifierIssuer for assigning blank node names.
 * @param options the RDF serialization options.
 *
 * @return the array of RDF triples for the given graph.
 */
function _graphToRDF(dataset, graph, graphTerm, issuer, options) {
  const ids = Object.keys(graph).sort();
  for(const id of ids) {
    const node = graph[id];
    const properties = Object.keys(node).sort();
    for(let property of properties) {
      const items = node[property];
      if(property === '@type') {
        property = RDF_TYPE;
      } else if(isKeyword(property)) {
        continue;
      }

      for(const item of items) {
        // RDF subject
        const subject = {
          termType: id.startsWith('_:') ? 'BlankNode' : 'NamedNode',
          value: id
        };

        // skip relative IRI subjects (not valid RDF)
        if(!_isAbsoluteIri(id)) {
          continue;
        }

        // RDF predicate
        const predicate = {
          termType: property.startsWith('_:') ? 'BlankNode' : 'NamedNode',
          value: property
        };

        // skip relative IRI predicates (not valid RDF)
        if(!_isAbsoluteIri(property)) {
          continue;
        }

        // skip blank node predicates unless producing generalized RDF
        if(predicate.termType === 'BlankNode' &&
          !options.produceGeneralizedRdf) {
          continue;
        }

        // convert list, value or node object to triple
        const object =
          _objectToRDF(item, issuer, dataset, graphTerm, options.rdfDirection);
        // skip null objects (they are relative IRIs)
        if(object) {
          dataset.push({
            subject,
            predicate,
            object,
            graph: graphTerm
          });
        }
      }
    }
  }
}

/**
 * Converts a @list value into linked list of blank node RDF quads
 * (an RDF collection).
 *
 * @param list the @list value.
 * @param issuer a IdentifierIssuer for assigning blank node names.
 * @param dataset the array of quads to append to.
 * @param graphTerm the graph term for each quad.
 *
 * @return the head of the list.
 */
function _listToRDF(list, issuer, dataset, graphTerm, rdfDirection) {
  const first = {termType: 'NamedNode', value: RDF_FIRST};
  const rest = {termType: 'NamedNode', value: RDF_REST};
  const nil = {termType: 'NamedNode', value: RDF_NIL};

  const last = list.pop();
  // Result is the head of the list
  const result = last ? {termType: 'BlankNode', value: issuer.getId()} : nil;
  let subject = result;

  for(const item of list) {
    const object = _objectToRDF(item, issuer, dataset, graphTerm, rdfDirection);
    const next = {termType: 'BlankNode', value: issuer.getId()};
    dataset.push({
      subject,
      predicate: first,
      object,
      graph: graphTerm
    });
    dataset.push({
      subject,
      predicate: rest,
      object: next,
      graph: graphTerm
    });
    subject = next;
  }

  // Tail of list
  if(last) {
    const object = _objectToRDF(last, issuer, dataset, graphTerm, rdfDirection);
    dataset.push({
      subject,
      predicate: first,
      object,
      graph: graphTerm
    });
    dataset.push({
      subject,
      predicate: rest,
      object: nil,
      graph: graphTerm
    });
  }

  return result;
}

/**
 * Converts a JSON-LD value object to an RDF literal or a JSON-LD string,
 * node object to an RDF resource, or adds a list.
 *
 * @param item the JSON-LD value or node object.
 * @param issuer a IdentifierIssuer for assigning blank node names.
 * @param dataset the dataset to append RDF quads to.
 * @param graphTerm the graph term for each quad.
 *
 * @return the RDF literal or RDF resource.
 */
function _objectToRDF(item, issuer, dataset, graphTerm, rdfDirection) {
  const object = {};

  // convert value object to RDF
  if(graphTypes.isValue(item)) {
    object.termType = 'Literal';
    object.value = undefined;
    object.datatype = {
      termType: 'NamedNode'
    };
    let value = item['@value'];
    const datatype = item['@type'] || null;

    // convert to XSD/JSON datatypes as appropriate
    if(datatype === '@json') {
      object.value = jsonCanonicalize(value);
      object.datatype.value = RDF_JSON_LITERAL;
    } else if(types.isBoolean(value)) {
      object.value = value.toString();
      object.datatype.value = datatype || XSD_BOOLEAN;
    } else if(types.isDouble(value) || datatype === XSD_DOUBLE) {
      if(!types.isDouble(value)) {
        value = parseFloat(value);
      }
      // canonical double representation
      object.value = value.toExponential(15).replace(/(\d)0*e\+?/, '$1E');
      object.datatype.value = datatype || XSD_DOUBLE;
    } else if(types.isNumber(value)) {
      object.value = value.toFixed(0);
      object.datatype.value = datatype || XSD_INTEGER;
    } else if(rdfDirection === 'i18n-datatype' &&
      '@direction' in item) {
      const datatype = 'https://www.w3.org/ns/i18n#' +
        (item['@language'] || '') +
        `_${item['@direction']}`;
      object.datatype.value = datatype;
      object.value = value;
    } else if('@language' in item) {
      object.value = value;
      object.datatype.value = datatype || RDF_LANGSTRING;
      object.language = item['@language'];
    } else {
      object.value = value;
      object.datatype.value = datatype || XSD_STRING;
    }
  } else if(graphTypes.isList(item)) {
    const _list =
      _listToRDF(item['@list'], issuer, dataset, graphTerm, rdfDirection);
    object.termType = _list.termType;
    object.value = _list.value;
  } else {
    // convert string/node object to RDF
    const id = types.isObject(item) ? item['@id'] : item;
    object.termType = id.startsWith('_:') ? 'BlankNode' : 'NamedNode';
    object.value = id;
  }

  // skip relative IRIs, not valid RDF
  if(object.termType === 'NamedNode' && !_isAbsoluteIri(object.value)) {
    return null;
  }

  return object;
}


/***/ }),

/***/ "772c":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "7839":
/***/ (function(module, exports) {

// IE8- don't enum bug keys
module.exports = [
  'constructor',
  'hasOwnProperty',
  'isPrototypeOf',
  'propertyIsEnumerable',
  'toLocaleString',
  'toString',
  'valueOf'
];


/***/ }),

/***/ "796c":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const {isKeyword} = __webpack_require__("d1e7");
const graphTypes = __webpack_require__("c92f");
const types = __webpack_require__("68fa");
const util = __webpack_require__("89ae");
const JsonLdError = __webpack_require__("5950");

const api = {};
module.exports = api;

/**
 * Creates a merged JSON-LD node map (node ID => node).
 *
 * @param input the expanded JSON-LD to create a node map of.
 * @param [options] the options to use:
 *          [issuer] a jsonld.IdentifierIssuer to use to label blank nodes.
 *
 * @return the node map.
 */
api.createMergedNodeMap = (input, options) => {
  options = options || {};

  // produce a map of all subjects and name each bnode
  const issuer = options.issuer || new util.IdentifierIssuer('_:b');
  const graphs = {'@default': {}};
  api.createNodeMap(input, graphs, '@default', issuer);

  // add all non-default graphs to default graph
  return api.mergeNodeMaps(graphs);
};

/**
 * Recursively flattens the subjects in the given JSON-LD expanded input
 * into a node map.
 *
 * @param input the JSON-LD expanded input.
 * @param graphs a map of graph name to subject map.
 * @param graph the name of the current graph.
 * @param issuer the blank node identifier issuer.
 * @param name the name assigned to the current input if it is a bnode.
 * @param list the list to append to, null for none.
 */
api.createNodeMap = (input, graphs, graph, issuer, name, list) => {
  // recurse through array
  if(types.isArray(input)) {
    for(const node of input) {
      api.createNodeMap(node, graphs, graph, issuer, undefined, list);
    }
    return;
  }

  // add non-object to list
  if(!types.isObject(input)) {
    if(list) {
      list.push(input);
    }
    return;
  }

  // add values to list
  if(graphTypes.isValue(input)) {
    if('@type' in input) {
      let type = input['@type'];
      // rename @type blank node
      if(type.indexOf('_:') === 0) {
        input['@type'] = type = issuer.getId(type);
      }
    }
    if(list) {
      list.push(input);
    }
    return;
  } else if(list && graphTypes.isList(input)) {
    const _list = [];
    api.createNodeMap(input['@list'], graphs, graph, issuer, name, _list);
    list.push({'@list': _list});
    return;
  }

  // Note: At this point, input must be a subject.

  // spec requires @type to be named first, so assign names early
  if('@type' in input) {
    const types = input['@type'];
    for(const type of types) {
      if(type.indexOf('_:') === 0) {
        issuer.getId(type);
      }
    }
  }

  // get name for subject
  if(types.isUndefined(name)) {
    name = graphTypes.isBlankNode(input) ?
      issuer.getId(input['@id']) : input['@id'];
  }

  // add subject reference to list
  if(list) {
    list.push({'@id': name});
  }

  // create new subject or merge into existing one
  const subjects = graphs[graph];
  const subject = subjects[name] = subjects[name] || {};
  subject['@id'] = name;
  const properties = Object.keys(input).sort();
  for(let property of properties) {
    // skip @id
    if(property === '@id') {
      continue;
    }

    // handle reverse properties
    if(property === '@reverse') {
      const referencedNode = {'@id': name};
      const reverseMap = input['@reverse'];
      for(const reverseProperty in reverseMap) {
        const items = reverseMap[reverseProperty];
        for(const item of items) {
          let itemName = item['@id'];
          if(graphTypes.isBlankNode(item)) {
            itemName = issuer.getId(itemName);
          }
          api.createNodeMap(item, graphs, graph, issuer, itemName);
          util.addValue(
            subjects[itemName], reverseProperty, referencedNode,
            {propertyIsArray: true, allowDuplicate: false});
        }
      }
      continue;
    }

    // recurse into graph
    if(property === '@graph') {
      // add graph subjects map entry
      if(!(name in graphs)) {
        graphs[name] = {};
      }
      api.createNodeMap(input[property], graphs, name, issuer);
      continue;
    }

    // recurse into included
    if(property === '@included') {
      api.createNodeMap(input[property], graphs, graph, issuer);
      continue;
    }

    // copy non-@type keywords
    if(property !== '@type' && isKeyword(property)) {
      if(property === '@index' && property in subject &&
        (input[property] !== subject[property] ||
        input[property]['@id'] !== subject[property]['@id'])) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; conflicting @index property detected.',
          'jsonld.SyntaxError',
          {code: 'conflicting indexes', subject});
      }
      subject[property] = input[property];
      continue;
    }

    // iterate over objects
    const objects = input[property];

    // if property is a bnode, assign it a new id
    if(property.indexOf('_:') === 0) {
      property = issuer.getId(property);
    }

    // ensure property is added for empty arrays
    if(objects.length === 0) {
      util.addValue(subject, property, [], {propertyIsArray: true});
      continue;
    }
    for(let o of objects) {
      if(property === '@type') {
        // rename @type blank nodes
        o = (o.indexOf('_:') === 0) ? issuer.getId(o) : o;
      }

      // handle embedded subject or subject reference
      if(graphTypes.isSubject(o) || graphTypes.isSubjectReference(o)) {
        // skip null @id
        if('@id' in o && !o['@id']) {
          continue;
        }

        // relabel blank node @id
        const id = graphTypes.isBlankNode(o) ?
          issuer.getId(o['@id']) : o['@id'];

        // add reference and recurse
        util.addValue(
          subject, property, {'@id': id},
          {propertyIsArray: true, allowDuplicate: false});
        api.createNodeMap(o, graphs, graph, issuer, id);
      } else if(graphTypes.isValue(o)) {
        util.addValue(
          subject, property, o,
          {propertyIsArray: true, allowDuplicate: false});
      } else if(graphTypes.isList(o)) {
        // handle @list
        const _list = [];
        api.createNodeMap(o['@list'], graphs, graph, issuer, name, _list);
        o = {'@list': _list};
        util.addValue(
          subject, property, o,
          {propertyIsArray: true, allowDuplicate: false});
      } else {
        // handle @value
        api.createNodeMap(o, graphs, graph, issuer, name);
        util.addValue(
          subject, property, o, {propertyIsArray: true, allowDuplicate: false});
      }
    }
  }
};

/**
 * Merge separate named graphs into a single merged graph including
 * all nodes from the default graph and named graphs.
 *
 * @param graphs a map of graph name to subject map.
 *
 * @return the merged graph map.
 */
api.mergeNodeMapGraphs = graphs => {
  const merged = {};
  for(const name of Object.keys(graphs).sort()) {
    for(const id of Object.keys(graphs[name]).sort()) {
      const node = graphs[name][id];
      if(!(id in merged)) {
        merged[id] = {'@id': id};
      }
      const mergedNode = merged[id];

      for(const property of Object.keys(node).sort()) {
        if(isKeyword(property) && property !== '@type') {
          // copy keywords
          mergedNode[property] = util.clone(node[property]);
        } else {
          // merge objects
          for(const value of node[property]) {
            util.addValue(
              mergedNode, property, util.clone(value),
              {propertyIsArray: true, allowDuplicate: false});
          }
        }
      }
    }
  }

  return merged;
};

api.mergeNodeMaps = graphs => {
  // add all non-default graphs to default graph
  const defaultGraph = graphs['@default'];
  const graphNames = Object.keys(graphs).sort();
  for(const graphName of graphNames) {
    if(graphName === '@default') {
      continue;
    }
    const nodeMap = graphs[graphName];
    let subject = defaultGraph[graphName];
    if(!subject) {
      defaultGraph[graphName] = subject = {
        '@id': graphName,
        '@graph': []
      };
    } else if(!('@graph' in subject)) {
      subject['@graph'] = [];
    }
    const graph = subject['@graph'];
    for(const id of Object.keys(nodeMap).sort()) {
      const node = nodeMap[id];
      // only add full subjects
      if(!graphTypes.isSubjectReference(node)) {
        graph.push(node);
      }
    }
  }
  return defaultGraph;
};


/***/ }),

/***/ "7b0b":
/***/ (function(module, exports, __webpack_require__) {

var requireObjectCoercible = __webpack_require__("1d80");

// `ToObject` abstract operation
// https://tc39.es/ecma262/#sec-toobject
module.exports = function (argument) {
  return Object(requireObjectCoercible(argument));
};


/***/ }),

/***/ "7c73":
/***/ (function(module, exports, __webpack_require__) {

var anObject = __webpack_require__("825a");
var defineProperties = __webpack_require__("37e8");
var enumBugKeys = __webpack_require__("7839");
var hiddenKeys = __webpack_require__("d012");
var html = __webpack_require__("1be4");
var documentCreateElement = __webpack_require__("cc12");
var sharedKey = __webpack_require__("f772");

var GT = '>';
var LT = '<';
var PROTOTYPE = 'prototype';
var SCRIPT = 'script';
var IE_PROTO = sharedKey('IE_PROTO');

var EmptyConstructor = function () { /* empty */ };

var scriptTag = function (content) {
  return LT + SCRIPT + GT + content + LT + '/' + SCRIPT + GT;
};

// Create object with fake `null` prototype: use ActiveX Object with cleared prototype
var NullProtoObjectViaActiveX = function (activeXDocument) {
  activeXDocument.write(scriptTag(''));
  activeXDocument.close();
  var temp = activeXDocument.parentWindow.Object;
  activeXDocument = null; // avoid memory leak
  return temp;
};

// Create object with fake `null` prototype: use iframe Object with cleared prototype
var NullProtoObjectViaIFrame = function () {
  // Thrash, waste and sodomy: IE GC bug
  var iframe = documentCreateElement('iframe');
  var JS = 'java' + SCRIPT + ':';
  var iframeDocument;
  iframe.style.display = 'none';
  html.appendChild(iframe);
  // https://github.com/zloirock/core-js/issues/475
  iframe.src = String(JS);
  iframeDocument = iframe.contentWindow.document;
  iframeDocument.open();
  iframeDocument.write(scriptTag('document.F=Object'));
  iframeDocument.close();
  return iframeDocument.F;
};

// Check for document.domain and active x support
// No need to use active x approach when document.domain is not set
// see https://github.com/es-shims/es5-shim/issues/150
// variation of https://github.com/kitcambridge/es5-shim/commit/4f738ac066346
// avoid IE GC bug
var activeXDocument;
var NullProtoObject = function () {
  try {
    /* global ActiveXObject -- old IE */
    activeXDocument = document.domain && new ActiveXObject('htmlfile');
  } catch (error) { /* ignore */ }
  NullProtoObject = activeXDocument ? NullProtoObjectViaActiveX(activeXDocument) : NullProtoObjectViaIFrame();
  var length = enumBugKeys.length;
  while (length--) delete NullProtoObject[PROTOTYPE][enumBugKeys[length]];
  return NullProtoObject();
};

hiddenKeys[IE_PROTO] = true;

// `Object.create` method
// https://tc39.es/ecma262/#sec-object.create
module.exports = Object.create || function create(O, Properties) {
  var result;
  if (O !== null) {
    EmptyConstructor[PROTOTYPE] = anObject(O);
    result = new EmptyConstructor();
    EmptyConstructor[PROTOTYPE] = null;
    // add "__proto__" for Object.getPrototypeOf polyfill
    result[IE_PROTO] = O;
  } else result = NullProtoObject();
  return Properties === undefined ? result : defineProperties(result, Properties);
};


/***/ }),

/***/ "7dd0":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var createIteratorConstructor = __webpack_require__("9ed3");
var getPrototypeOf = __webpack_require__("e163");
var setPrototypeOf = __webpack_require__("d2bb");
var setToStringTag = __webpack_require__("d44e");
var createNonEnumerableProperty = __webpack_require__("9112");
var redefine = __webpack_require__("6eeb");
var wellKnownSymbol = __webpack_require__("b622");
var IS_PURE = __webpack_require__("c430");
var Iterators = __webpack_require__("3f8c");
var IteratorsCore = __webpack_require__("ae93");

var IteratorPrototype = IteratorsCore.IteratorPrototype;
var BUGGY_SAFARI_ITERATORS = IteratorsCore.BUGGY_SAFARI_ITERATORS;
var ITERATOR = wellKnownSymbol('iterator');
var KEYS = 'keys';
var VALUES = 'values';
var ENTRIES = 'entries';

var returnThis = function () { return this; };

module.exports = function (Iterable, NAME, IteratorConstructor, next, DEFAULT, IS_SET, FORCED) {
  createIteratorConstructor(IteratorConstructor, NAME, next);

  var getIterationMethod = function (KIND) {
    if (KIND === DEFAULT && defaultIterator) return defaultIterator;
    if (!BUGGY_SAFARI_ITERATORS && KIND in IterablePrototype) return IterablePrototype[KIND];
    switch (KIND) {
      case KEYS: return function keys() { return new IteratorConstructor(this, KIND); };
      case VALUES: return function values() { return new IteratorConstructor(this, KIND); };
      case ENTRIES: return function entries() { return new IteratorConstructor(this, KIND); };
    } return function () { return new IteratorConstructor(this); };
  };

  var TO_STRING_TAG = NAME + ' Iterator';
  var INCORRECT_VALUES_NAME = false;
  var IterablePrototype = Iterable.prototype;
  var nativeIterator = IterablePrototype[ITERATOR]
    || IterablePrototype['@@iterator']
    || DEFAULT && IterablePrototype[DEFAULT];
  var defaultIterator = !BUGGY_SAFARI_ITERATORS && nativeIterator || getIterationMethod(DEFAULT);
  var anyNativeIterator = NAME == 'Array' ? IterablePrototype.entries || nativeIterator : nativeIterator;
  var CurrentIteratorPrototype, methods, KEY;

  // fix native
  if (anyNativeIterator) {
    CurrentIteratorPrototype = getPrototypeOf(anyNativeIterator.call(new Iterable()));
    if (IteratorPrototype !== Object.prototype && CurrentIteratorPrototype.next) {
      if (!IS_PURE && getPrototypeOf(CurrentIteratorPrototype) !== IteratorPrototype) {
        if (setPrototypeOf) {
          setPrototypeOf(CurrentIteratorPrototype, IteratorPrototype);
        } else if (typeof CurrentIteratorPrototype[ITERATOR] != 'function') {
          createNonEnumerableProperty(CurrentIteratorPrototype, ITERATOR, returnThis);
        }
      }
      // Set @@toStringTag to native iterators
      setToStringTag(CurrentIteratorPrototype, TO_STRING_TAG, true, true);
      if (IS_PURE) Iterators[TO_STRING_TAG] = returnThis;
    }
  }

  // fix Array#{values, @@iterator}.name in V8 / FF
  if (DEFAULT == VALUES && nativeIterator && nativeIterator.name !== VALUES) {
    INCORRECT_VALUES_NAME = true;
    defaultIterator = function values() { return nativeIterator.call(this); };
  }

  // define iterator
  if ((!IS_PURE || FORCED) && IterablePrototype[ITERATOR] !== defaultIterator) {
    createNonEnumerableProperty(IterablePrototype, ITERATOR, defaultIterator);
  }
  Iterators[NAME] = defaultIterator;

  // export additional methods
  if (DEFAULT) {
    methods = {
      values: getIterationMethod(VALUES),
      keys: IS_SET ? defaultIterator : getIterationMethod(KEYS),
      entries: getIterationMethod(ENTRIES)
    };
    if (FORCED) for (KEY in methods) {
      if (BUGGY_SAFARI_ITERATORS || INCORRECT_VALUES_NAME || !(KEY in IterablePrototype)) {
        redefine(IterablePrototype, KEY, methods[KEY]);
      }
    } else $({ target: NAME, proto: true, forced: BUGGY_SAFARI_ITERATORS || INCORRECT_VALUES_NAME }, methods);
  }

  return methods;
};


/***/ }),

/***/ "7f9a":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var inspectSource = __webpack_require__("8925");

var WeakMap = global.WeakMap;

module.exports = typeof WeakMap === 'function' && /native code/.test(inspectSource(WeakMap));


/***/ }),

/***/ "825a":
/***/ (function(module, exports, __webpack_require__) {

var isObject = __webpack_require__("861d");

module.exports = function (it) {
  if (!isObject(it)) {
    throw TypeError(String(it) + ' is not an object');
  } return it;
};


/***/ }),

/***/ "8322":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


module.exports = jsonld => {
  class JsonLdProcessor {
    toString() {
      return '[object JsonLdProcessor]';
    }
  }
  Object.defineProperty(JsonLdProcessor, 'prototype', {
    writable: false,
    enumerable: false
  });
  Object.defineProperty(JsonLdProcessor.prototype, 'constructor', {
    writable: true,
    enumerable: false,
    configurable: true,
    value: JsonLdProcessor
  });

  // The Web IDL test harness will check the number of parameters defined in
  // the functions below. The number of parameters must exactly match the
  // required (non-optional) parameters of the JsonLdProcessor interface as
  // defined here:
  // https://www.w3.org/TR/json-ld-api/#the-jsonldprocessor-interface

  JsonLdProcessor.compact = function(input, ctx) {
    if(arguments.length < 2) {
      return Promise.reject(
        new TypeError('Could not compact, too few arguments.'));
    }
    return jsonld.compact(input, ctx);
  };
  JsonLdProcessor.expand = function(input) {
    if(arguments.length < 1) {
      return Promise.reject(
        new TypeError('Could not expand, too few arguments.'));
    }
    return jsonld.expand(input);
  };
  JsonLdProcessor.flatten = function(input) {
    if(arguments.length < 1) {
      return Promise.reject(
        new TypeError('Could not flatten, too few arguments.'));
    }
    return jsonld.flatten(input);
  };

  return JsonLdProcessor;
};


/***/ }),

/***/ "83ab":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");

// Detect IE8's incomplete defineProperty implementation
module.exports = !fails(function () {
  // eslint-disable-next-line es/no-object-defineproperty -- required for testing
  return Object.defineProperty({}, 1, { get: function () { return 7; } })[1] != 7;
});


/***/ }),

/***/ "8418":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var toPrimitive = __webpack_require__("c04e");
var definePropertyModule = __webpack_require__("9bf2");
var createPropertyDescriptor = __webpack_require__("5c6c");

module.exports = function (object, key, value) {
  var propertyKey = toPrimitive(key);
  if (propertyKey in object) definePropertyModule.f(object, propertyKey, createPropertyDescriptor(0, value));
  else object[propertyKey] = value;
};


/***/ }),

/***/ "857a":
/***/ (function(module, exports, __webpack_require__) {

var requireObjectCoercible = __webpack_require__("1d80");

var quot = /"/g;

// B.2.3.2.1 CreateHTML(string, tag, attribute, value)
// https://tc39.es/ecma262/#sec-createhtml
module.exports = function (string, tag, attribute, value) {
  var S = String(requireObjectCoercible(string));
  var p1 = '<' + tag;
  if (attribute !== '') p1 += ' ' + attribute + '="' + String(value).replace(quot, '&quot;') + '"';
  return p1 + '>' + S + '</' + tag + '>';
};


/***/ }),

/***/ "861d":
/***/ (function(module, exports) {

module.exports = function (it) {
  return typeof it === 'object' ? it !== null : typeof it === 'function';
};


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

/***/ "8925":
/***/ (function(module, exports, __webpack_require__) {

var store = __webpack_require__("c6cd");

var functionToString = Function.toString;

// this helper broken in `3.4.1-3.4.4`, so we can't use `shared` helper
if (typeof store.inspectSource != 'function') {
  store.inspectSource = function (it) {
    return functionToString.call(it);
  };
}

module.exports = store.inspectSource;


/***/ }),

/***/ "89ae":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017-2019 Digital Bazaar, Inc. All rights reserved.
 */


const graphTypes = __webpack_require__("c92f");
const types = __webpack_require__("68fa");
// TODO: move `IdentifierIssuer` to its own package
const IdentifierIssuer = __webpack_require__("4b45").IdentifierIssuer;
const JsonLdError = __webpack_require__("5950");

// constants
const REGEX_LINK_HEADERS = /(?:<[^>]*?>|"[^"]*?"|[^,])+/g;
const REGEX_LINK_HEADER = /\s*<([^>]*?)>\s*(?:;\s*(.*))?/;
const REGEX_LINK_HEADER_PARAMS =
  /(.*?)=(?:(?:"([^"]*?)")|([^"]*?))\s*(?:(?:;\s*)|$)/g;

const DEFAULTS = {
  headers: {
    accept: 'application/ld+json, application/json'
  }
};

const api = {};
module.exports = api;
api.IdentifierIssuer = IdentifierIssuer;

/**
 * Clones an object, array, Map, Set, or string/number. If a typed JavaScript
 * object is given, such as a Date, it will be converted to a string.
 *
 * @param value the value to clone.
 *
 * @return the cloned value.
 */
api.clone = function(value) {
  if(value && typeof value === 'object') {
    let rval;
    if(types.isArray(value)) {
      rval = [];
      for(let i = 0; i < value.length; ++i) {
        rval[i] = api.clone(value[i]);
      }
    } else if(value instanceof Map) {
      rval = new Map();
      for(const [k, v] of value) {
        rval.set(k, api.clone(v));
      }
    } else if(value instanceof Set) {
      rval = new Set();
      for(const v of value) {
        rval.add(api.clone(v));
      }
    } else if(types.isObject(value)) {
      rval = {};
      for(const key in value) {
        rval[key] = api.clone(value[key]);
      }
    } else {
      rval = value.toString();
    }
    return rval;
  }
  return value;
};

/**
 * Ensure a value is an array. If the value is an array, it is returned.
 * Otherwise, it is wrapped in an array.
 *
 * @param value the value to return as an array.
 *
 * @return the value as an array.
 */
api.asArray = function(value) {
  return Array.isArray(value) ? value : [value];
};

/**
 * Builds an HTTP headers object for making a JSON-LD request from custom
 * headers and asserts the `accept` header isn't overridden.
 *
 * @param headers an object of headers with keys as header names and values
 *          as header values.
 *
 * @return an object of headers with a valid `accept` header.
 */
api.buildHeaders = (headers = {}) => {
  const hasAccept = Object.keys(headers).some(
    h => h.toLowerCase() === 'accept');

  if(hasAccept) {
    throw new RangeError(
      'Accept header may not be specified; only "' +
      DEFAULTS.headers.accept + '" is supported.');
  }

  return Object.assign({Accept: DEFAULTS.headers.accept}, headers);
};

/**
 * Parses a link header. The results will be key'd by the value of "rel".
 *
 * Link: <http://json-ld.org/contexts/person.jsonld>;
 * rel="http://www.w3.org/ns/json-ld#context"; type="application/ld+json"
 *
 * Parses as: {
 *   'http://www.w3.org/ns/json-ld#context': {
 *     target: http://json-ld.org/contexts/person.jsonld,
 *     type: 'application/ld+json'
 *   }
 * }
 *
 * If there is more than one "rel" with the same IRI, then entries in the
 * resulting map for that "rel" will be arrays.
 *
 * @param header the link header to parse.
 */
api.parseLinkHeader = header => {
  const rval = {};
  // split on unbracketed/unquoted commas
  const entries = header.match(REGEX_LINK_HEADERS);
  for(let i = 0; i < entries.length; ++i) {
    let match = entries[i].match(REGEX_LINK_HEADER);
    if(!match) {
      continue;
    }
    const result = {target: match[1]};
    const params = match[2];
    while((match = REGEX_LINK_HEADER_PARAMS.exec(params))) {
      result[match[1]] = (match[2] === undefined) ? match[3] : match[2];
    }
    const rel = result['rel'] || '';
    if(Array.isArray(rval[rel])) {
      rval[rel].push(result);
    } else if(rval.hasOwnProperty(rel)) {
      rval[rel] = [rval[rel], result];
    } else {
      rval[rel] = result;
    }
  }
  return rval;
};

/**
 * Throws an exception if the given value is not a valid @type value.
 *
 * @param v the value to check.
 */
api.validateTypeValue = (v, isFrame) => {
  if(types.isString(v)) {
    return;
  }

  if(types.isArray(v) && v.every(vv => types.isString(vv))) {
    return;
  }
  if(isFrame && types.isObject(v)) {
    switch(Object.keys(v).length) {
      case 0:
        // empty object is wildcard
        return;
      case 1:
        // default entry is all strings
        if('@default' in v &&
          api.asArray(v['@default']).every(vv => types.isString(vv))) {
          return;
        }
    }
  }

  throw new JsonLdError(
    'Invalid JSON-LD syntax; "@type" value must a string, an array of ' +
    'strings, an empty object, ' +
    'or a default object.', 'jsonld.SyntaxError',
    {code: 'invalid type value', value: v});
};

/**
 * Returns true if the given subject has the given property.
 *
 * @param subject the subject to check.
 * @param property the property to look for.
 *
 * @return true if the subject has the given property, false if not.
 */
api.hasProperty = (subject, property) => {
  if(subject.hasOwnProperty(property)) {
    const value = subject[property];
    return (!types.isArray(value) || value.length > 0);
  }
  return false;
};

/**
 * Determines if the given value is a property of the given subject.
 *
 * @param subject the subject to check.
 * @param property the property to check.
 * @param value the value to check.
 *
 * @return true if the value exists, false if not.
 */
api.hasValue = (subject, property, value) => {
  if(api.hasProperty(subject, property)) {
    let val = subject[property];
    const isList = graphTypes.isList(val);
    if(types.isArray(val) || isList) {
      if(isList) {
        val = val['@list'];
      }
      for(let i = 0; i < val.length; ++i) {
        if(api.compareValues(value, val[i])) {
          return true;
        }
      }
    } else if(!types.isArray(value)) {
      // avoid matching the set of values with an array value parameter
      return api.compareValues(value, val);
    }
  }
  return false;
};

/**
 * Adds a value to a subject. If the value is an array, all values in the
 * array will be added.
 *
 * @param subject the subject to add the value to.
 * @param property the property that relates the value to the subject.
 * @param value the value to add.
 * @param [options] the options to use:
 *        [propertyIsArray] true if the property is always an array, false
 *          if not (default: false).
 *        [valueIsArray] true if the value to be added should be preserved as
 *          an array (lists) (default: false).
 *        [allowDuplicate] true to allow duplicates, false not to (uses a
 *          simple shallow comparison of subject ID or value) (default: true).
 *        [prependValue] false to prepend value to any existing values.
 *          (default: false)
 */
api.addValue = (subject, property, value, options) => {
  options = options || {};
  if(!('propertyIsArray' in options)) {
    options.propertyIsArray = false;
  }
  if(!('valueIsArray' in options)) {
    options.valueIsArray = false;
  }
  if(!('allowDuplicate' in options)) {
    options.allowDuplicate = true;
  }
  if(!('prependValue' in options)) {
    options.prependValue = false;
  }

  if(options.valueIsArray) {
    subject[property] = value;
  } else if(types.isArray(value)) {
    if(value.length === 0 && options.propertyIsArray &&
      !subject.hasOwnProperty(property)) {
      subject[property] = [];
    }
    if(options.prependValue) {
      value = value.concat(subject[property]);
      subject[property] = [];
    }
    for(let i = 0; i < value.length; ++i) {
      api.addValue(subject, property, value[i], options);
    }
  } else if(subject.hasOwnProperty(property)) {
    // check if subject already has value if duplicates not allowed
    const hasValue = (!options.allowDuplicate &&
      api.hasValue(subject, property, value));

    // make property an array if value not present or always an array
    if(!types.isArray(subject[property]) &&
      (!hasValue || options.propertyIsArray)) {
      subject[property] = [subject[property]];
    }

    // add new value
    if(!hasValue) {
      if(options.prependValue) {
        subject[property].unshift(value);
      } else {
        subject[property].push(value);
      }
    }
  } else {
    // add new value as set or single value
    subject[property] = options.propertyIsArray ? [value] : value;
  }
};

/**
 * Gets all of the values for a subject's property as an array.
 *
 * @param subject the subject.
 * @param property the property.
 *
 * @return all of the values for a subject's property as an array.
 */
api.getValues = (subject, property) => [].concat(subject[property] || []);

/**
 * Removes a property from a subject.
 *
 * @param subject the subject.
 * @param property the property.
 */
api.removeProperty = (subject, property) => {
  delete subject[property];
};

/**
 * Removes a value from a subject.
 *
 * @param subject the subject.
 * @param property the property that relates the value to the subject.
 * @param value the value to remove.
 * @param [options] the options to use:
 *          [propertyIsArray] true if the property is always an array, false
 *            if not (default: false).
 */
api.removeValue = (subject, property, value, options) => {
  options = options || {};
  if(!('propertyIsArray' in options)) {
    options.propertyIsArray = false;
  }

  // filter out value
  const values = api.getValues(subject, property).filter(
    e => !api.compareValues(e, value));

  if(values.length === 0) {
    api.removeProperty(subject, property);
  } else if(values.length === 1 && !options.propertyIsArray) {
    subject[property] = values[0];
  } else {
    subject[property] = values;
  }
};

/**
 * Relabels all blank nodes in the given JSON-LD input.
 *
 * @param input the JSON-LD input.
 * @param [options] the options to use:
 *          [issuer] an IdentifierIssuer to use to label blank nodes.
 */
api.relabelBlankNodes = (input, options) => {
  options = options || {};
  const issuer = options.issuer || new IdentifierIssuer('_:b');
  return _labelBlankNodes(issuer, input);
};

/**
 * Compares two JSON-LD values for equality. Two JSON-LD values will be
 * considered equal if:
 *
 * 1. They are both primitives of the same type and value.
 * 2. They are both @values with the same @value, @type, @language,
 *   and @index, OR
 * 3. They both have @ids they are the same.
 *
 * @param v1 the first value.
 * @param v2 the second value.
 *
 * @return true if v1 and v2 are considered equal, false if not.
 */
api.compareValues = (v1, v2) => {
  // 1. equal primitives
  if(v1 === v2) {
    return true;
  }

  // 2. equal @values
  if(graphTypes.isValue(v1) && graphTypes.isValue(v2) &&
    v1['@value'] === v2['@value'] &&
    v1['@type'] === v2['@type'] &&
    v1['@language'] === v2['@language'] &&
    v1['@index'] === v2['@index']) {
    return true;
  }

  // 3. equal @ids
  if(types.isObject(v1) &&
    ('@id' in v1) &&
    types.isObject(v2) &&
    ('@id' in v2)) {
    return v1['@id'] === v2['@id'];
  }

  return false;
};

/**
 * Compares two strings first based on length and then lexicographically.
 *
 * @param a the first string.
 * @param b the second string.
 *
 * @return -1 if a < b, 1 if a > b, 0 if a === b.
 */
api.compareShortestLeast = (a, b) => {
  if(a.length < b.length) {
    return -1;
  }
  if(b.length < a.length) {
    return 1;
  }
  if(a === b) {
    return 0;
  }
  return (a < b) ? -1 : 1;
};

/**
 * Labels the blank nodes in the given value using the given IdentifierIssuer.
 *
 * @param issuer the IdentifierIssuer to use.
 * @param element the element with blank nodes to rename.
 *
 * @return the element.
 */
function _labelBlankNodes(issuer, element) {
  if(types.isArray(element)) {
    for(let i = 0; i < element.length; ++i) {
      element[i] = _labelBlankNodes(issuer, element[i]);
    }
  } else if(graphTypes.isList(element)) {
    element['@list'] = _labelBlankNodes(issuer, element['@list']);
  } else if(types.isObject(element)) {
    // relabel blank node
    if(graphTypes.isBlankNode(element)) {
      element['@id'] = issuer.getId(element['@id']);
    }

    // recursively apply to all keys
    const keys = Object.keys(element).sort();
    for(let ki = 0; ki < keys.length; ++ki) {
      const key = keys[ki];
      if(key !== '@id') {
        element[key] = _labelBlankNodes(issuer, element[key]);
      }
    }
  }

  return element;
}


/***/ }),

/***/ "8a79":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var getOwnPropertyDescriptor = __webpack_require__("06cf").f;
var toLength = __webpack_require__("50c4");
var notARegExp = __webpack_require__("5a34");
var requireObjectCoercible = __webpack_require__("1d80");
var correctIsRegExpLogic = __webpack_require__("ab13");
var IS_PURE = __webpack_require__("c430");

// eslint-disable-next-line es/no-string-prototype-endswith -- safe
var $endsWith = ''.endsWith;
var min = Math.min;

var CORRECT_IS_REGEXP_LOGIC = correctIsRegExpLogic('endsWith');
// https://github.com/zloirock/core-js/pull/702
var MDN_POLYFILL_BUG = !IS_PURE && !CORRECT_IS_REGEXP_LOGIC && !!function () {
  var descriptor = getOwnPropertyDescriptor(String.prototype, 'endsWith');
  return descriptor && !descriptor.writable;
}();

// `String.prototype.endsWith` method
// https://tc39.es/ecma262/#sec-string.prototype.endswith
$({ target: 'String', proto: true, forced: !MDN_POLYFILL_BUG && !CORRECT_IS_REGEXP_LOGIC }, {
  endsWith: function endsWith(searchString /* , endPosition = @length */) {
    var that = String(requireObjectCoercible(this));
    notARegExp(searchString);
    var endPosition = arguments.length > 1 ? arguments[1] : undefined;
    var len = toLength(that.length);
    var end = endPosition === undefined ? len : min(toLength(endPosition), len);
    var search = String(searchString);
    return $endsWith
      ? $endsWith.call(that, search, end)
      : that.slice(end - search.length, end) === search;
  }
});


/***/ }),

/***/ "8aa5":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var charAt = __webpack_require__("6547").charAt;

// `AdvanceStringIndex` abstract operation
// https://tc39.es/ecma262/#sec-advancestringindex
module.exports = function (S, index, unicode) {
  return index + (unicode ? charAt(S, index).length : 1);
};


/***/ }),

/***/ "8acc":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


module.exports = class IdentifierIssuer {
  /**
   * Creates a new IdentifierIssuer. A IdentifierIssuer issues unique
   * identifiers, keeping track of any previously issued identifiers.
   *
   * @param prefix the prefix to use ('<prefix><counter>').
   * @param existing an existing Map to use.
   * @param counter the counter to use.
   */
  constructor(prefix, existing = new Map(), counter = 0) {
    this.prefix = prefix;
    this._existing = existing;
    this.counter = counter;
  }

  /**
   * Copies this IdentifierIssuer.
   *
   * @return a copy of this IdentifierIssuer.
   */
  clone() {
    const {prefix, _existing, counter} = this;
    return new IdentifierIssuer(prefix, new Map(_existing), counter);
  }

  /**
   * Gets the new identifier for the given old identifier, where if no old
   * identifier is given a new identifier will be generated.
   *
   * @param [old] the old identifier to get the new identifier for.
   *
   * @return the new identifier.
   */
  getId(old) {
    // return existing old identifier
    const existing = old && this._existing.get(old);
    if(existing) {
      return existing;
    }

    // get next identifier
    const identifier = this.prefix + this.counter;
    this.counter++;

    // save mapping
    if(old) {
      this._existing.set(old, identifier);
    }

    return identifier;
  }

  /**
   * Returns true if the given old identifer has already been assigned a new
   * identifier.
   *
   * @param old the old identifier to check.
   *
   * @return true if the old identifier has been assigned a new identifier,
   *   false if not.
   */
  hasId(old) {
    return this._existing.has(old);
  }

  /**
   * Returns all of the IDs that have been issued new IDs in the order in
   * which they were issued new IDs.
   *
   * @return the list of old IDs that has been issued new IDs in order.
   */
  getOldIds() {
    return [...this._existing.keys()];
  }
};


/***/ }),

/***/ "9093":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2019 Digital Bazaar, Inc. All rights reserved.
 */


const LRU = __webpack_require__("f32c");

const MAX_ACTIVE_CONTEXTS = 10;

module.exports = class ResolvedContext {
  /**
   * Creates a ResolvedContext.
   *
   * @param document the context document.
   */
  constructor({document}) {
    this.document = document;
    // TODO: enable customization of processed context cache
    // TODO: limit based on size of processed contexts vs. number of them
    this.cache = new LRU({max: MAX_ACTIVE_CONTEXTS});
  }

  getProcessed(activeCtx) {
    return this.cache.get(activeCtx);
  }

  setProcessed(activeCtx, processedCtx) {
    this.cache.set(activeCtx, processedCtx);
  }
};


/***/ }),

/***/ "90e3":
/***/ (function(module, exports) {

var id = 0;
var postfix = Math.random();

module.exports = function (key) {
  return 'Symbol(' + String(key === undefined ? '' : key) + ')_' + (++id + postfix).toString(36);
};


/***/ }),

/***/ "9112":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var definePropertyModule = __webpack_require__("9bf2");
var createPropertyDescriptor = __webpack_require__("5c6c");

module.exports = DESCRIPTORS ? function (object, key, value) {
  return definePropertyModule.f(object, key, createPropertyDescriptor(1, value));
} : function (object, key, value) {
  object[key] = value;
  return object;
};


/***/ }),

/***/ "9263":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var regexpFlags = __webpack_require__("ad6d");
var stickyHelpers = __webpack_require__("9f7f");
var shared = __webpack_require__("5692");

var nativeExec = RegExp.prototype.exec;
var nativeReplace = shared('native-string-replace', String.prototype.replace);

var patchedExec = nativeExec;

var UPDATES_LAST_INDEX_WRONG = (function () {
  var re1 = /a/;
  var re2 = /b*/g;
  nativeExec.call(re1, 'a');
  nativeExec.call(re2, 'a');
  return re1.lastIndex !== 0 || re2.lastIndex !== 0;
})();

var UNSUPPORTED_Y = stickyHelpers.UNSUPPORTED_Y || stickyHelpers.BROKEN_CARET;

// nonparticipating capturing group, copied from es5-shim's String#split patch.
// eslint-disable-next-line regexp/no-assertion-capturing-group, regexp/no-empty-group -- required for testing
var NPCG_INCLUDED = /()??/.exec('')[1] !== undefined;

var PATCH = UPDATES_LAST_INDEX_WRONG || NPCG_INCLUDED || UNSUPPORTED_Y;

if (PATCH) {
  patchedExec = function exec(str) {
    var re = this;
    var lastIndex, reCopy, match, i;
    var sticky = UNSUPPORTED_Y && re.sticky;
    var flags = regexpFlags.call(re);
    var source = re.source;
    var charsAdded = 0;
    var strCopy = str;

    if (sticky) {
      flags = flags.replace('y', '');
      if (flags.indexOf('g') === -1) {
        flags += 'g';
      }

      strCopy = String(str).slice(re.lastIndex);
      // Support anchored sticky behavior.
      if (re.lastIndex > 0 && (!re.multiline || re.multiline && str[re.lastIndex - 1] !== '\n')) {
        source = '(?: ' + source + ')';
        strCopy = ' ' + strCopy;
        charsAdded++;
      }
      // ^(? + rx + ) is needed, in combination with some str slicing, to
      // simulate the 'y' flag.
      reCopy = new RegExp('^(?:' + source + ')', flags);
    }

    if (NPCG_INCLUDED) {
      reCopy = new RegExp('^' + source + '$(?!\\s)', flags);
    }
    if (UPDATES_LAST_INDEX_WRONG) lastIndex = re.lastIndex;

    match = nativeExec.call(sticky ? reCopy : re, strCopy);

    if (sticky) {
      if (match) {
        match.input = match.input.slice(charsAdded);
        match[0] = match[0].slice(charsAdded);
        match.index = re.lastIndex;
        re.lastIndex += match[0].length;
      } else re.lastIndex = 0;
    } else if (UPDATES_LAST_INDEX_WRONG && match) {
      re.lastIndex = re.global ? match.index + match[0].length : lastIndex;
    }
    if (NPCG_INCLUDED && match && match.length > 1) {
      // Fix browsers whose `exec` methods don't consistently return `undefined`
      // for NPCG, like IE8. NOTE: This doesn' work for /(.?)?/
      nativeReplace.call(match[0], reCopy, function () {
        for (i = 1; i < arguments.length - 2; i++) {
          if (arguments[i] === undefined) match[i] = undefined;
        }
      });
    }

    return match;
  };
}

module.exports = patchedExec;


/***/ }),

/***/ "92c9":
/***/ (function(module, exports) {


/**
 * Expose `isUrl`.
 */

module.exports = isUrl;

/**
 * RegExps.
 * A URL must match #1 and then at least one of #2/#3.
 * Use two levels of REs to avoid REDOS.
 */

var protocolAndDomainRE = /^(?:\w+:)?\/\/(\S+)$/;

var localhostDomainRE = /^localhost[\:?\d]*(?:[^\:?\d]\S*)?$/
var nonLocalhostDomainRE = /^[^\s\.]+\.\S{2,}$/;

/**
 * Loosely validate a URL `string`.
 *
 * @param {String} string
 * @return {Boolean}
 */

function isUrl(string){
  if (typeof string !== 'string') {
    return false;
  }

  var match = string.match(protocolAndDomainRE);
  if (!match) {
    return false;
  }

  var everythingAfterProtocol = match[1];
  if (!everythingAfterProtocol) {
    return false;
  }

  if (localhostDomainRE.test(everythingAfterProtocol) ||
      nonLocalhostDomainRE.test(everythingAfterProtocol)) {
    return true;
  }

  return false;
}


/***/ }),

/***/ "94ca":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");

var replacement = /#|\.prototype\./;

var isForced = function (feature, detection) {
  var value = data[normalize(feature)];
  return value == POLYFILL ? true
    : value == NATIVE ? false
    : typeof detection == 'function' ? fails(detection)
    : !!detection;
};

var normalize = isForced.normalize = function (string) {
  return String(string).replace(replacement, '.').toLowerCase();
};

var data = isForced.data = {};
var NATIVE = isForced.NATIVE = 'N';
var POLYFILL = isForced.POLYFILL = 'P';

module.exports = isForced;


/***/ }),

/***/ "96cf":
/***/ (function(module, exports, __webpack_require__) {

/**
 * Copyright (c) 2014-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

var runtime = (function (exports) {
  "use strict";

  var Op = Object.prototype;
  var hasOwn = Op.hasOwnProperty;
  var undefined; // More compressible than void 0.
  var $Symbol = typeof Symbol === "function" ? Symbol : {};
  var iteratorSymbol = $Symbol.iterator || "@@iterator";
  var asyncIteratorSymbol = $Symbol.asyncIterator || "@@asyncIterator";
  var toStringTagSymbol = $Symbol.toStringTag || "@@toStringTag";

  function define(obj, key, value) {
    Object.defineProperty(obj, key, {
      value: value,
      enumerable: true,
      configurable: true,
      writable: true
    });
    return obj[key];
  }
  try {
    // IE 8 has a broken Object.defineProperty that only works on DOM objects.
    define({}, "");
  } catch (err) {
    define = function(obj, key, value) {
      return obj[key] = value;
    };
  }

  function wrap(innerFn, outerFn, self, tryLocsList) {
    // If outerFn provided and outerFn.prototype is a Generator, then outerFn.prototype instanceof Generator.
    var protoGenerator = outerFn && outerFn.prototype instanceof Generator ? outerFn : Generator;
    var generator = Object.create(protoGenerator.prototype);
    var context = new Context(tryLocsList || []);

    // The ._invoke method unifies the implementations of the .next,
    // .throw, and .return methods.
    generator._invoke = makeInvokeMethod(innerFn, self, context);

    return generator;
  }
  exports.wrap = wrap;

  // Try/catch helper to minimize deoptimizations. Returns a completion
  // record like context.tryEntries[i].completion. This interface could
  // have been (and was previously) designed to take a closure to be
  // invoked without arguments, but in all the cases we care about we
  // already have an existing method we want to call, so there's no need
  // to create a new function object. We can even get away with assuming
  // the method takes exactly one argument, since that happens to be true
  // in every case, so we don't have to touch the arguments object. The
  // only additional allocation required is the completion record, which
  // has a stable shape and so hopefully should be cheap to allocate.
  function tryCatch(fn, obj, arg) {
    try {
      return { type: "normal", arg: fn.call(obj, arg) };
    } catch (err) {
      return { type: "throw", arg: err };
    }
  }

  var GenStateSuspendedStart = "suspendedStart";
  var GenStateSuspendedYield = "suspendedYield";
  var GenStateExecuting = "executing";
  var GenStateCompleted = "completed";

  // Returning this object from the innerFn has the same effect as
  // breaking out of the dispatch switch statement.
  var ContinueSentinel = {};

  // Dummy constructor functions that we use as the .constructor and
  // .constructor.prototype properties for functions that return Generator
  // objects. For full spec compliance, you may wish to configure your
  // minifier not to mangle the names of these two functions.
  function Generator() {}
  function GeneratorFunction() {}
  function GeneratorFunctionPrototype() {}

  // This is a polyfill for %IteratorPrototype% for environments that
  // don't natively support it.
  var IteratorPrototype = {};
  IteratorPrototype[iteratorSymbol] = function () {
    return this;
  };

  var getProto = Object.getPrototypeOf;
  var NativeIteratorPrototype = getProto && getProto(getProto(values([])));
  if (NativeIteratorPrototype &&
      NativeIteratorPrototype !== Op &&
      hasOwn.call(NativeIteratorPrototype, iteratorSymbol)) {
    // This environment has a native %IteratorPrototype%; use it instead
    // of the polyfill.
    IteratorPrototype = NativeIteratorPrototype;
  }

  var Gp = GeneratorFunctionPrototype.prototype =
    Generator.prototype = Object.create(IteratorPrototype);
  GeneratorFunction.prototype = Gp.constructor = GeneratorFunctionPrototype;
  GeneratorFunctionPrototype.constructor = GeneratorFunction;
  GeneratorFunction.displayName = define(
    GeneratorFunctionPrototype,
    toStringTagSymbol,
    "GeneratorFunction"
  );

  // Helper for defining the .next, .throw, and .return methods of the
  // Iterator interface in terms of a single ._invoke method.
  function defineIteratorMethods(prototype) {
    ["next", "throw", "return"].forEach(function(method) {
      define(prototype, method, function(arg) {
        return this._invoke(method, arg);
      });
    });
  }

  exports.isGeneratorFunction = function(genFun) {
    var ctor = typeof genFun === "function" && genFun.constructor;
    return ctor
      ? ctor === GeneratorFunction ||
        // For the native GeneratorFunction constructor, the best we can
        // do is to check its .name property.
        (ctor.displayName || ctor.name) === "GeneratorFunction"
      : false;
  };

  exports.mark = function(genFun) {
    if (Object.setPrototypeOf) {
      Object.setPrototypeOf(genFun, GeneratorFunctionPrototype);
    } else {
      genFun.__proto__ = GeneratorFunctionPrototype;
      define(genFun, toStringTagSymbol, "GeneratorFunction");
    }
    genFun.prototype = Object.create(Gp);
    return genFun;
  };

  // Within the body of any async function, `await x` is transformed to
  // `yield regeneratorRuntime.awrap(x)`, so that the runtime can test
  // `hasOwn.call(value, "__await")` to determine if the yielded value is
  // meant to be awaited.
  exports.awrap = function(arg) {
    return { __await: arg };
  };

  function AsyncIterator(generator, PromiseImpl) {
    function invoke(method, arg, resolve, reject) {
      var record = tryCatch(generator[method], generator, arg);
      if (record.type === "throw") {
        reject(record.arg);
      } else {
        var result = record.arg;
        var value = result.value;
        if (value &&
            typeof value === "object" &&
            hasOwn.call(value, "__await")) {
          return PromiseImpl.resolve(value.__await).then(function(value) {
            invoke("next", value, resolve, reject);
          }, function(err) {
            invoke("throw", err, resolve, reject);
          });
        }

        return PromiseImpl.resolve(value).then(function(unwrapped) {
          // When a yielded Promise is resolved, its final value becomes
          // the .value of the Promise<{value,done}> result for the
          // current iteration.
          result.value = unwrapped;
          resolve(result);
        }, function(error) {
          // If a rejected Promise was yielded, throw the rejection back
          // into the async generator function so it can be handled there.
          return invoke("throw", error, resolve, reject);
        });
      }
    }

    var previousPromise;

    function enqueue(method, arg) {
      function callInvokeWithMethodAndArg() {
        return new PromiseImpl(function(resolve, reject) {
          invoke(method, arg, resolve, reject);
        });
      }

      return previousPromise =
        // If enqueue has been called before, then we want to wait until
        // all previous Promises have been resolved before calling invoke,
        // so that results are always delivered in the correct order. If
        // enqueue has not been called before, then it is important to
        // call invoke immediately, without waiting on a callback to fire,
        // so that the async generator function has the opportunity to do
        // any necessary setup in a predictable way. This predictability
        // is why the Promise constructor synchronously invokes its
        // executor callback, and why async functions synchronously
        // execute code before the first await. Since we implement simple
        // async functions in terms of async generators, it is especially
        // important to get this right, even though it requires care.
        previousPromise ? previousPromise.then(
          callInvokeWithMethodAndArg,
          // Avoid propagating failures to Promises returned by later
          // invocations of the iterator.
          callInvokeWithMethodAndArg
        ) : callInvokeWithMethodAndArg();
    }

    // Define the unified helper method that is used to implement .next,
    // .throw, and .return (see defineIteratorMethods).
    this._invoke = enqueue;
  }

  defineIteratorMethods(AsyncIterator.prototype);
  AsyncIterator.prototype[asyncIteratorSymbol] = function () {
    return this;
  };
  exports.AsyncIterator = AsyncIterator;

  // Note that simple async functions are implemented on top of
  // AsyncIterator objects; they just return a Promise for the value of
  // the final result produced by the iterator.
  exports.async = function(innerFn, outerFn, self, tryLocsList, PromiseImpl) {
    if (PromiseImpl === void 0) PromiseImpl = Promise;

    var iter = new AsyncIterator(
      wrap(innerFn, outerFn, self, tryLocsList),
      PromiseImpl
    );

    return exports.isGeneratorFunction(outerFn)
      ? iter // If outerFn is a generator, return the full iterator.
      : iter.next().then(function(result) {
          return result.done ? result.value : iter.next();
        });
  };

  function makeInvokeMethod(innerFn, self, context) {
    var state = GenStateSuspendedStart;

    return function invoke(method, arg) {
      if (state === GenStateExecuting) {
        throw new Error("Generator is already running");
      }

      if (state === GenStateCompleted) {
        if (method === "throw") {
          throw arg;
        }

        // Be forgiving, per 25.3.3.3.3 of the spec:
        // https://people.mozilla.org/~jorendorff/es6-draft.html#sec-generatorresume
        return doneResult();
      }

      context.method = method;
      context.arg = arg;

      while (true) {
        var delegate = context.delegate;
        if (delegate) {
          var delegateResult = maybeInvokeDelegate(delegate, context);
          if (delegateResult) {
            if (delegateResult === ContinueSentinel) continue;
            return delegateResult;
          }
        }

        if (context.method === "next") {
          // Setting context._sent for legacy support of Babel's
          // function.sent implementation.
          context.sent = context._sent = context.arg;

        } else if (context.method === "throw") {
          if (state === GenStateSuspendedStart) {
            state = GenStateCompleted;
            throw context.arg;
          }

          context.dispatchException(context.arg);

        } else if (context.method === "return") {
          context.abrupt("return", context.arg);
        }

        state = GenStateExecuting;

        var record = tryCatch(innerFn, self, context);
        if (record.type === "normal") {
          // If an exception is thrown from innerFn, we leave state ===
          // GenStateExecuting and loop back for another invocation.
          state = context.done
            ? GenStateCompleted
            : GenStateSuspendedYield;

          if (record.arg === ContinueSentinel) {
            continue;
          }

          return {
            value: record.arg,
            done: context.done
          };

        } else if (record.type === "throw") {
          state = GenStateCompleted;
          // Dispatch the exception by looping back around to the
          // context.dispatchException(context.arg) call above.
          context.method = "throw";
          context.arg = record.arg;
        }
      }
    };
  }

  // Call delegate.iterator[context.method](context.arg) and handle the
  // result, either by returning a { value, done } result from the
  // delegate iterator, or by modifying context.method and context.arg,
  // setting context.delegate to null, and returning the ContinueSentinel.
  function maybeInvokeDelegate(delegate, context) {
    var method = delegate.iterator[context.method];
    if (method === undefined) {
      // A .throw or .return when the delegate iterator has no .throw
      // method always terminates the yield* loop.
      context.delegate = null;

      if (context.method === "throw") {
        // Note: ["return"] must be used for ES3 parsing compatibility.
        if (delegate.iterator["return"]) {
          // If the delegate iterator has a return method, give it a
          // chance to clean up.
          context.method = "return";
          context.arg = undefined;
          maybeInvokeDelegate(delegate, context);

          if (context.method === "throw") {
            // If maybeInvokeDelegate(context) changed context.method from
            // "return" to "throw", let that override the TypeError below.
            return ContinueSentinel;
          }
        }

        context.method = "throw";
        context.arg = new TypeError(
          "The iterator does not provide a 'throw' method");
      }

      return ContinueSentinel;
    }

    var record = tryCatch(method, delegate.iterator, context.arg);

    if (record.type === "throw") {
      context.method = "throw";
      context.arg = record.arg;
      context.delegate = null;
      return ContinueSentinel;
    }

    var info = record.arg;

    if (! info) {
      context.method = "throw";
      context.arg = new TypeError("iterator result is not an object");
      context.delegate = null;
      return ContinueSentinel;
    }

    if (info.done) {
      // Assign the result of the finished delegate to the temporary
      // variable specified by delegate.resultName (see delegateYield).
      context[delegate.resultName] = info.value;

      // Resume execution at the desired location (see delegateYield).
      context.next = delegate.nextLoc;

      // If context.method was "throw" but the delegate handled the
      // exception, let the outer generator proceed normally. If
      // context.method was "next", forget context.arg since it has been
      // "consumed" by the delegate iterator. If context.method was
      // "return", allow the original .return call to continue in the
      // outer generator.
      if (context.method !== "return") {
        context.method = "next";
        context.arg = undefined;
      }

    } else {
      // Re-yield the result returned by the delegate method.
      return info;
    }

    // The delegate iterator is finished, so forget it and continue with
    // the outer generator.
    context.delegate = null;
    return ContinueSentinel;
  }

  // Define Generator.prototype.{next,throw,return} in terms of the
  // unified ._invoke helper method.
  defineIteratorMethods(Gp);

  define(Gp, toStringTagSymbol, "Generator");

  // A Generator should always return itself as the iterator object when the
  // @@iterator function is called on it. Some browsers' implementations of the
  // iterator prototype chain incorrectly implement this, causing the Generator
  // object to not be returned from this call. This ensures that doesn't happen.
  // See https://github.com/facebook/regenerator/issues/274 for more details.
  Gp[iteratorSymbol] = function() {
    return this;
  };

  Gp.toString = function() {
    return "[object Generator]";
  };

  function pushTryEntry(locs) {
    var entry = { tryLoc: locs[0] };

    if (1 in locs) {
      entry.catchLoc = locs[1];
    }

    if (2 in locs) {
      entry.finallyLoc = locs[2];
      entry.afterLoc = locs[3];
    }

    this.tryEntries.push(entry);
  }

  function resetTryEntry(entry) {
    var record = entry.completion || {};
    record.type = "normal";
    delete record.arg;
    entry.completion = record;
  }

  function Context(tryLocsList) {
    // The root entry object (effectively a try statement without a catch
    // or a finally block) gives us a place to store values thrown from
    // locations where there is no enclosing try statement.
    this.tryEntries = [{ tryLoc: "root" }];
    tryLocsList.forEach(pushTryEntry, this);
    this.reset(true);
  }

  exports.keys = function(object) {
    var keys = [];
    for (var key in object) {
      keys.push(key);
    }
    keys.reverse();

    // Rather than returning an object with a next method, we keep
    // things simple and return the next function itself.
    return function next() {
      while (keys.length) {
        var key = keys.pop();
        if (key in object) {
          next.value = key;
          next.done = false;
          return next;
        }
      }

      // To avoid creating an additional object, we just hang the .value
      // and .done properties off the next function object itself. This
      // also ensures that the minifier will not anonymize the function.
      next.done = true;
      return next;
    };
  };

  function values(iterable) {
    if (iterable) {
      var iteratorMethod = iterable[iteratorSymbol];
      if (iteratorMethod) {
        return iteratorMethod.call(iterable);
      }

      if (typeof iterable.next === "function") {
        return iterable;
      }

      if (!isNaN(iterable.length)) {
        var i = -1, next = function next() {
          while (++i < iterable.length) {
            if (hasOwn.call(iterable, i)) {
              next.value = iterable[i];
              next.done = false;
              return next;
            }
          }

          next.value = undefined;
          next.done = true;

          return next;
        };

        return next.next = next;
      }
    }

    // Return an iterator with no values.
    return { next: doneResult };
  }
  exports.values = values;

  function doneResult() {
    return { value: undefined, done: true };
  }

  Context.prototype = {
    constructor: Context,

    reset: function(skipTempReset) {
      this.prev = 0;
      this.next = 0;
      // Resetting context._sent for legacy support of Babel's
      // function.sent implementation.
      this.sent = this._sent = undefined;
      this.done = false;
      this.delegate = null;

      this.method = "next";
      this.arg = undefined;

      this.tryEntries.forEach(resetTryEntry);

      if (!skipTempReset) {
        for (var name in this) {
          // Not sure about the optimal order of these conditions:
          if (name.charAt(0) === "t" &&
              hasOwn.call(this, name) &&
              !isNaN(+name.slice(1))) {
            this[name] = undefined;
          }
        }
      }
    },

    stop: function() {
      this.done = true;

      var rootEntry = this.tryEntries[0];
      var rootRecord = rootEntry.completion;
      if (rootRecord.type === "throw") {
        throw rootRecord.arg;
      }

      return this.rval;
    },

    dispatchException: function(exception) {
      if (this.done) {
        throw exception;
      }

      var context = this;
      function handle(loc, caught) {
        record.type = "throw";
        record.arg = exception;
        context.next = loc;

        if (caught) {
          // If the dispatched exception was caught by a catch block,
          // then let that catch block handle the exception normally.
          context.method = "next";
          context.arg = undefined;
        }

        return !! caught;
      }

      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        var record = entry.completion;

        if (entry.tryLoc === "root") {
          // Exception thrown outside of any try block that could handle
          // it, so set the completion value of the entire function to
          // throw the exception.
          return handle("end");
        }

        if (entry.tryLoc <= this.prev) {
          var hasCatch = hasOwn.call(entry, "catchLoc");
          var hasFinally = hasOwn.call(entry, "finallyLoc");

          if (hasCatch && hasFinally) {
            if (this.prev < entry.catchLoc) {
              return handle(entry.catchLoc, true);
            } else if (this.prev < entry.finallyLoc) {
              return handle(entry.finallyLoc);
            }

          } else if (hasCatch) {
            if (this.prev < entry.catchLoc) {
              return handle(entry.catchLoc, true);
            }

          } else if (hasFinally) {
            if (this.prev < entry.finallyLoc) {
              return handle(entry.finallyLoc);
            }

          } else {
            throw new Error("try statement without catch or finally");
          }
        }
      }
    },

    abrupt: function(type, arg) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.tryLoc <= this.prev &&
            hasOwn.call(entry, "finallyLoc") &&
            this.prev < entry.finallyLoc) {
          var finallyEntry = entry;
          break;
        }
      }

      if (finallyEntry &&
          (type === "break" ||
           type === "continue") &&
          finallyEntry.tryLoc <= arg &&
          arg <= finallyEntry.finallyLoc) {
        // Ignore the finally entry if control is not jumping to a
        // location outside the try/catch block.
        finallyEntry = null;
      }

      var record = finallyEntry ? finallyEntry.completion : {};
      record.type = type;
      record.arg = arg;

      if (finallyEntry) {
        this.method = "next";
        this.next = finallyEntry.finallyLoc;
        return ContinueSentinel;
      }

      return this.complete(record);
    },

    complete: function(record, afterLoc) {
      if (record.type === "throw") {
        throw record.arg;
      }

      if (record.type === "break" ||
          record.type === "continue") {
        this.next = record.arg;
      } else if (record.type === "return") {
        this.rval = this.arg = record.arg;
        this.method = "return";
        this.next = "end";
      } else if (record.type === "normal" && afterLoc) {
        this.next = afterLoc;
      }

      return ContinueSentinel;
    },

    finish: function(finallyLoc) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.finallyLoc === finallyLoc) {
          this.complete(entry.completion, entry.afterLoc);
          resetTryEntry(entry);
          return ContinueSentinel;
        }
      }
    },

    "catch": function(tryLoc) {
      for (var i = this.tryEntries.length - 1; i >= 0; --i) {
        var entry = this.tryEntries[i];
        if (entry.tryLoc === tryLoc) {
          var record = entry.completion;
          if (record.type === "throw") {
            var thrown = record.arg;
            resetTryEntry(entry);
          }
          return thrown;
        }
      }

      // The context.catch method must only be called with a location
      // argument that corresponds to a known catch block.
      throw new Error("illegal catch attempt");
    },

    delegateYield: function(iterable, resultName, nextLoc) {
      this.delegate = {
        iterator: values(iterable),
        resultName: resultName,
        nextLoc: nextLoc
      };

      if (this.method === "next") {
        // Deliberately forget the last sent value so that we don't
        // accidentally pass it on to the delegate.
        this.arg = undefined;
      }

      return ContinueSentinel;
    }
  };

  // Regardless of whether this script is executing as a CommonJS module
  // or not, return the runtime object so that we can declare the variable
  // regeneratorRuntime in the outer scope, which allows this module to be
  // injected easily by `bin/regenerator --include-runtime script.js`.
  return exports;

}(
  // If this script is executing as a CommonJS module, use module.exports
  // as the regeneratorRuntime namespace. Otherwise create a new empty
  // object. Either way, the resulting object will be used to initialize
  // the regeneratorRuntime variable at the top of this file.
   true ? module.exports : undefined
));

try {
  regeneratorRuntime = runtime;
} catch (accidentalStrictMode) {
  // This module should not be running in strict mode, so the above
  // assignment should always work unless something is misconfigured. Just
  // in case runtime.js accidentally runs in strict mode, we can escape
  // strict mode using a global Function call. This could conceivably fail
  // if a Content Security Policy forbids using Function, but in that case
  // the proper solution is to fix the accidental strict mode problem. If
  // you've misconfigured your bundler to force strict mode and applied a
  // CSP to forbid Function, and you're not willing to fix either of those
  // problems, please detail your unique predicament in a GitHub issue.
  Function("r", "regeneratorRuntime = r")(runtime);
}


/***/ }),

/***/ "9bf2":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var IE8_DOM_DEFINE = __webpack_require__("0cfb");
var anObject = __webpack_require__("825a");
var toPrimitive = __webpack_require__("c04e");

// eslint-disable-next-line es/no-object-defineproperty -- safe
var $defineProperty = Object.defineProperty;

// `Object.defineProperty` method
// https://tc39.es/ecma262/#sec-object.defineproperty
exports.f = DESCRIPTORS ? $defineProperty : function defineProperty(O, P, Attributes) {
  anObject(O);
  P = toPrimitive(P, true);
  anObject(Attributes);
  if (IE8_DOM_DEFINE) try {
    return $defineProperty(O, P, Attributes);
  } catch (error) { /* empty */ }
  if ('get' in Attributes || 'set' in Attributes) throw TypeError('Accessors not supported');
  if ('value' in Attributes) O[P] = Attributes.value;
  return O;
};


/***/ }),

/***/ "9ed3":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var IteratorPrototype = __webpack_require__("ae93").IteratorPrototype;
var create = __webpack_require__("7c73");
var createPropertyDescriptor = __webpack_require__("5c6c");
var setToStringTag = __webpack_require__("d44e");
var Iterators = __webpack_require__("3f8c");

var returnThis = function () { return this; };

module.exports = function (IteratorConstructor, NAME, next) {
  var TO_STRING_TAG = NAME + ' Iterator';
  IteratorConstructor.prototype = create(IteratorPrototype, { next: createPropertyDescriptor(1, next) });
  setToStringTag(IteratorConstructor, TO_STRING_TAG, false, true);
  Iterators[TO_STRING_TAG] = returnThis;
  return IteratorConstructor;
};


/***/ }),

/***/ "9f7f":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var fails = __webpack_require__("d039");

// babel-minify transpiles RegExp('a', 'y') -> /a/y and it causes SyntaxError,
// so we use an intermediate function.
function RE(s, f) {
  return RegExp(s, f);
}

exports.UNSUPPORTED_Y = fails(function () {
  // babel-minify transpiles RegExp('a', 'y') -> /a/y and it causes SyntaxError
  var re = RE('a', 'y');
  re.lastIndex = 2;
  return re.exec('abcd') != null;
});

exports.BROKEN_CARET = fails(function () {
  // https://bugzilla.mozilla.org/show_bug.cgi?id=773687
  var re = RE('^r', 'gy');
  re.lastIndex = 2;
  return re.exec('str') != null;
});


/***/ }),

/***/ "9fe3":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

module.exports = Yallist

Yallist.Node = Node
Yallist.create = Yallist

function Yallist (list) {
  var self = this
  if (!(self instanceof Yallist)) {
    self = new Yallist()
  }

  self.tail = null
  self.head = null
  self.length = 0

  if (list && typeof list.forEach === 'function') {
    list.forEach(function (item) {
      self.push(item)
    })
  } else if (arguments.length > 0) {
    for (var i = 0, l = arguments.length; i < l; i++) {
      self.push(arguments[i])
    }
  }

  return self
}

Yallist.prototype.removeNode = function (node) {
  if (node.list !== this) {
    throw new Error('removing node which does not belong to this list')
  }

  var next = node.next
  var prev = node.prev

  if (next) {
    next.prev = prev
  }

  if (prev) {
    prev.next = next
  }

  if (node === this.head) {
    this.head = next
  }
  if (node === this.tail) {
    this.tail = prev
  }

  node.list.length--
  node.next = null
  node.prev = null
  node.list = null

  return next
}

Yallist.prototype.unshiftNode = function (node) {
  if (node === this.head) {
    return
  }

  if (node.list) {
    node.list.removeNode(node)
  }

  var head = this.head
  node.list = this
  node.next = head
  if (head) {
    head.prev = node
  }

  this.head = node
  if (!this.tail) {
    this.tail = node
  }
  this.length++
}

Yallist.prototype.pushNode = function (node) {
  if (node === this.tail) {
    return
  }

  if (node.list) {
    node.list.removeNode(node)
  }

  var tail = this.tail
  node.list = this
  node.prev = tail
  if (tail) {
    tail.next = node
  }

  this.tail = node
  if (!this.head) {
    this.head = node
  }
  this.length++
}

Yallist.prototype.push = function () {
  for (var i = 0, l = arguments.length; i < l; i++) {
    push(this, arguments[i])
  }
  return this.length
}

Yallist.prototype.unshift = function () {
  for (var i = 0, l = arguments.length; i < l; i++) {
    unshift(this, arguments[i])
  }
  return this.length
}

Yallist.prototype.pop = function () {
  if (!this.tail) {
    return undefined
  }

  var res = this.tail.value
  this.tail = this.tail.prev
  if (this.tail) {
    this.tail.next = null
  } else {
    this.head = null
  }
  this.length--
  return res
}

Yallist.prototype.shift = function () {
  if (!this.head) {
    return undefined
  }

  var res = this.head.value
  this.head = this.head.next
  if (this.head) {
    this.head.prev = null
  } else {
    this.tail = null
  }
  this.length--
  return res
}

Yallist.prototype.forEach = function (fn, thisp) {
  thisp = thisp || this
  for (var walker = this.head, i = 0; walker !== null; i++) {
    fn.call(thisp, walker.value, i, this)
    walker = walker.next
  }
}

Yallist.prototype.forEachReverse = function (fn, thisp) {
  thisp = thisp || this
  for (var walker = this.tail, i = this.length - 1; walker !== null; i--) {
    fn.call(thisp, walker.value, i, this)
    walker = walker.prev
  }
}

Yallist.prototype.get = function (n) {
  for (var i = 0, walker = this.head; walker !== null && i < n; i++) {
    // abort out of the list early if we hit a cycle
    walker = walker.next
  }
  if (i === n && walker !== null) {
    return walker.value
  }
}

Yallist.prototype.getReverse = function (n) {
  for (var i = 0, walker = this.tail; walker !== null && i < n; i++) {
    // abort out of the list early if we hit a cycle
    walker = walker.prev
  }
  if (i === n && walker !== null) {
    return walker.value
  }
}

Yallist.prototype.map = function (fn, thisp) {
  thisp = thisp || this
  var res = new Yallist()
  for (var walker = this.head; walker !== null;) {
    res.push(fn.call(thisp, walker.value, this))
    walker = walker.next
  }
  return res
}

Yallist.prototype.mapReverse = function (fn, thisp) {
  thisp = thisp || this
  var res = new Yallist()
  for (var walker = this.tail; walker !== null;) {
    res.push(fn.call(thisp, walker.value, this))
    walker = walker.prev
  }
  return res
}

Yallist.prototype.reduce = function (fn, initial) {
  var acc
  var walker = this.head
  if (arguments.length > 1) {
    acc = initial
  } else if (this.head) {
    walker = this.head.next
    acc = this.head.value
  } else {
    throw new TypeError('Reduce of empty list with no initial value')
  }

  for (var i = 0; walker !== null; i++) {
    acc = fn(acc, walker.value, i)
    walker = walker.next
  }

  return acc
}

Yallist.prototype.reduceReverse = function (fn, initial) {
  var acc
  var walker = this.tail
  if (arguments.length > 1) {
    acc = initial
  } else if (this.tail) {
    walker = this.tail.prev
    acc = this.tail.value
  } else {
    throw new TypeError('Reduce of empty list with no initial value')
  }

  for (var i = this.length - 1; walker !== null; i--) {
    acc = fn(acc, walker.value, i)
    walker = walker.prev
  }

  return acc
}

Yallist.prototype.toArray = function () {
  var arr = new Array(this.length)
  for (var i = 0, walker = this.head; walker !== null; i++) {
    arr[i] = walker.value
    walker = walker.next
  }
  return arr
}

Yallist.prototype.toArrayReverse = function () {
  var arr = new Array(this.length)
  for (var i = 0, walker = this.tail; walker !== null; i++) {
    arr[i] = walker.value
    walker = walker.prev
  }
  return arr
}

Yallist.prototype.slice = function (from, to) {
  to = to || this.length
  if (to < 0) {
    to += this.length
  }
  from = from || 0
  if (from < 0) {
    from += this.length
  }
  var ret = new Yallist()
  if (to < from || to < 0) {
    return ret
  }
  if (from < 0) {
    from = 0
  }
  if (to > this.length) {
    to = this.length
  }
  for (var i = 0, walker = this.head; walker !== null && i < from; i++) {
    walker = walker.next
  }
  for (; walker !== null && i < to; i++, walker = walker.next) {
    ret.push(walker.value)
  }
  return ret
}

Yallist.prototype.sliceReverse = function (from, to) {
  to = to || this.length
  if (to < 0) {
    to += this.length
  }
  from = from || 0
  if (from < 0) {
    from += this.length
  }
  var ret = new Yallist()
  if (to < from || to < 0) {
    return ret
  }
  if (from < 0) {
    from = 0
  }
  if (to > this.length) {
    to = this.length
  }
  for (var i = this.length, walker = this.tail; walker !== null && i > to; i--) {
    walker = walker.prev
  }
  for (; walker !== null && i > from; i--, walker = walker.prev) {
    ret.push(walker.value)
  }
  return ret
}

Yallist.prototype.splice = function (start, deleteCount, ...nodes) {
  if (start > this.length) {
    start = this.length - 1
  }
  if (start < 0) {
    start = this.length + start;
  }

  for (var i = 0, walker = this.head; walker !== null && i < start; i++) {
    walker = walker.next
  }

  var ret = []
  for (var i = 0; walker && i < deleteCount; i++) {
    ret.push(walker.value)
    walker = this.removeNode(walker)
  }
  if (walker === null) {
    walker = this.tail
  }

  if (walker !== this.head && walker !== this.tail) {
    walker = walker.prev
  }

  for (var i = 0; i < nodes.length; i++) {
    walker = insert(this, walker, nodes[i])
  }
  return ret;
}

Yallist.prototype.reverse = function () {
  var head = this.head
  var tail = this.tail
  for (var walker = head; walker !== null; walker = walker.prev) {
    var p = walker.prev
    walker.prev = walker.next
    walker.next = p
  }
  this.head = tail
  this.tail = head
  return this
}

function insert (self, node, value) {
  var inserted = node === self.head ?
    new Node(value, null, node, self) :
    new Node(value, node, node.next, self)

  if (inserted.next === null) {
    self.tail = inserted
  }
  if (inserted.prev === null) {
    self.head = inserted
  }

  self.length++

  return inserted
}

function push (self, item) {
  self.tail = new Node(item, self.tail, null, self)
  if (!self.head) {
    self.head = self.tail
  }
  self.length++
}

function unshift (self, item) {
  self.head = new Node(item, null, self.head, self)
  if (!self.tail) {
    self.tail = self.head
  }
  self.length++
}

function Node (value, prev, next, list) {
  if (!(this instanceof Node)) {
    return new Node(value, prev, next, list)
  }

  this.list = list
  this.value = value

  if (prev) {
    prev.next = this
    this.prev = prev
  } else {
    this.prev = null
  }

  if (next) {
    next.prev = this
    this.next = next
  } else {
    this.next = null
  }
}

try {
  // add if support for Symbol.iterator is present
  __webpack_require__("2832")(Yallist)
} catch (er) {}


/***/ }),

/***/ "a4b4":
/***/ (function(module, exports, __webpack_require__) {

var userAgent = __webpack_require__("342f");

module.exports = /web0s(?!.*chrome)/i.test(userAgent);


/***/ }),

/***/ "a4d3":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var global = __webpack_require__("da84");
var getBuiltIn = __webpack_require__("d066");
var IS_PURE = __webpack_require__("c430");
var DESCRIPTORS = __webpack_require__("83ab");
var NATIVE_SYMBOL = __webpack_require__("4930");
var USE_SYMBOL_AS_UID = __webpack_require__("fdbf");
var fails = __webpack_require__("d039");
var has = __webpack_require__("5135");
var isArray = __webpack_require__("e8b5");
var isObject = __webpack_require__("861d");
var anObject = __webpack_require__("825a");
var toObject = __webpack_require__("7b0b");
var toIndexedObject = __webpack_require__("fc6a");
var toPrimitive = __webpack_require__("c04e");
var createPropertyDescriptor = __webpack_require__("5c6c");
var nativeObjectCreate = __webpack_require__("7c73");
var objectKeys = __webpack_require__("df75");
var getOwnPropertyNamesModule = __webpack_require__("241c");
var getOwnPropertyNamesExternal = __webpack_require__("057f");
var getOwnPropertySymbolsModule = __webpack_require__("7418");
var getOwnPropertyDescriptorModule = __webpack_require__("06cf");
var definePropertyModule = __webpack_require__("9bf2");
var propertyIsEnumerableModule = __webpack_require__("d1e79");
var createNonEnumerableProperty = __webpack_require__("9112");
var redefine = __webpack_require__("6eeb");
var shared = __webpack_require__("5692");
var sharedKey = __webpack_require__("f772");
var hiddenKeys = __webpack_require__("d012");
var uid = __webpack_require__("90e3");
var wellKnownSymbol = __webpack_require__("b622");
var wrappedWellKnownSymbolModule = __webpack_require__("e538");
var defineWellKnownSymbol = __webpack_require__("746f");
var setToStringTag = __webpack_require__("d44e");
var InternalStateModule = __webpack_require__("69f3");
var $forEach = __webpack_require__("b727").forEach;

var HIDDEN = sharedKey('hidden');
var SYMBOL = 'Symbol';
var PROTOTYPE = 'prototype';
var TO_PRIMITIVE = wellKnownSymbol('toPrimitive');
var setInternalState = InternalStateModule.set;
var getInternalState = InternalStateModule.getterFor(SYMBOL);
var ObjectPrototype = Object[PROTOTYPE];
var $Symbol = global.Symbol;
var $stringify = getBuiltIn('JSON', 'stringify');
var nativeGetOwnPropertyDescriptor = getOwnPropertyDescriptorModule.f;
var nativeDefineProperty = definePropertyModule.f;
var nativeGetOwnPropertyNames = getOwnPropertyNamesExternal.f;
var nativePropertyIsEnumerable = propertyIsEnumerableModule.f;
var AllSymbols = shared('symbols');
var ObjectPrototypeSymbols = shared('op-symbols');
var StringToSymbolRegistry = shared('string-to-symbol-registry');
var SymbolToStringRegistry = shared('symbol-to-string-registry');
var WellKnownSymbolsStore = shared('wks');
var QObject = global.QObject;
// Don't use setters in Qt Script, https://github.com/zloirock/core-js/issues/173
var USE_SETTER = !QObject || !QObject[PROTOTYPE] || !QObject[PROTOTYPE].findChild;

// fallback for old Android, https://code.google.com/p/v8/issues/detail?id=687
var setSymbolDescriptor = DESCRIPTORS && fails(function () {
  return nativeObjectCreate(nativeDefineProperty({}, 'a', {
    get: function () { return nativeDefineProperty(this, 'a', { value: 7 }).a; }
  })).a != 7;
}) ? function (O, P, Attributes) {
  var ObjectPrototypeDescriptor = nativeGetOwnPropertyDescriptor(ObjectPrototype, P);
  if (ObjectPrototypeDescriptor) delete ObjectPrototype[P];
  nativeDefineProperty(O, P, Attributes);
  if (ObjectPrototypeDescriptor && O !== ObjectPrototype) {
    nativeDefineProperty(ObjectPrototype, P, ObjectPrototypeDescriptor);
  }
} : nativeDefineProperty;

var wrap = function (tag, description) {
  var symbol = AllSymbols[tag] = nativeObjectCreate($Symbol[PROTOTYPE]);
  setInternalState(symbol, {
    type: SYMBOL,
    tag: tag,
    description: description
  });
  if (!DESCRIPTORS) symbol.description = description;
  return symbol;
};

var isSymbol = USE_SYMBOL_AS_UID ? function (it) {
  return typeof it == 'symbol';
} : function (it) {
  return Object(it) instanceof $Symbol;
};

var $defineProperty = function defineProperty(O, P, Attributes) {
  if (O === ObjectPrototype) $defineProperty(ObjectPrototypeSymbols, P, Attributes);
  anObject(O);
  var key = toPrimitive(P, true);
  anObject(Attributes);
  if (has(AllSymbols, key)) {
    if (!Attributes.enumerable) {
      if (!has(O, HIDDEN)) nativeDefineProperty(O, HIDDEN, createPropertyDescriptor(1, {}));
      O[HIDDEN][key] = true;
    } else {
      if (has(O, HIDDEN) && O[HIDDEN][key]) O[HIDDEN][key] = false;
      Attributes = nativeObjectCreate(Attributes, { enumerable: createPropertyDescriptor(0, false) });
    } return setSymbolDescriptor(O, key, Attributes);
  } return nativeDefineProperty(O, key, Attributes);
};

var $defineProperties = function defineProperties(O, Properties) {
  anObject(O);
  var properties = toIndexedObject(Properties);
  var keys = objectKeys(properties).concat($getOwnPropertySymbols(properties));
  $forEach(keys, function (key) {
    if (!DESCRIPTORS || $propertyIsEnumerable.call(properties, key)) $defineProperty(O, key, properties[key]);
  });
  return O;
};

var $create = function create(O, Properties) {
  return Properties === undefined ? nativeObjectCreate(O) : $defineProperties(nativeObjectCreate(O), Properties);
};

var $propertyIsEnumerable = function propertyIsEnumerable(V) {
  var P = toPrimitive(V, true);
  var enumerable = nativePropertyIsEnumerable.call(this, P);
  if (this === ObjectPrototype && has(AllSymbols, P) && !has(ObjectPrototypeSymbols, P)) return false;
  return enumerable || !has(this, P) || !has(AllSymbols, P) || has(this, HIDDEN) && this[HIDDEN][P] ? enumerable : true;
};

var $getOwnPropertyDescriptor = function getOwnPropertyDescriptor(O, P) {
  var it = toIndexedObject(O);
  var key = toPrimitive(P, true);
  if (it === ObjectPrototype && has(AllSymbols, key) && !has(ObjectPrototypeSymbols, key)) return;
  var descriptor = nativeGetOwnPropertyDescriptor(it, key);
  if (descriptor && has(AllSymbols, key) && !(has(it, HIDDEN) && it[HIDDEN][key])) {
    descriptor.enumerable = true;
  }
  return descriptor;
};

var $getOwnPropertyNames = function getOwnPropertyNames(O) {
  var names = nativeGetOwnPropertyNames(toIndexedObject(O));
  var result = [];
  $forEach(names, function (key) {
    if (!has(AllSymbols, key) && !has(hiddenKeys, key)) result.push(key);
  });
  return result;
};

var $getOwnPropertySymbols = function getOwnPropertySymbols(O) {
  var IS_OBJECT_PROTOTYPE = O === ObjectPrototype;
  var names = nativeGetOwnPropertyNames(IS_OBJECT_PROTOTYPE ? ObjectPrototypeSymbols : toIndexedObject(O));
  var result = [];
  $forEach(names, function (key) {
    if (has(AllSymbols, key) && (!IS_OBJECT_PROTOTYPE || has(ObjectPrototype, key))) {
      result.push(AllSymbols[key]);
    }
  });
  return result;
};

// `Symbol` constructor
// https://tc39.es/ecma262/#sec-symbol-constructor
if (!NATIVE_SYMBOL) {
  $Symbol = function Symbol() {
    if (this instanceof $Symbol) throw TypeError('Symbol is not a constructor');
    var description = !arguments.length || arguments[0] === undefined ? undefined : String(arguments[0]);
    var tag = uid(description);
    var setter = function (value) {
      if (this === ObjectPrototype) setter.call(ObjectPrototypeSymbols, value);
      if (has(this, HIDDEN) && has(this[HIDDEN], tag)) this[HIDDEN][tag] = false;
      setSymbolDescriptor(this, tag, createPropertyDescriptor(1, value));
    };
    if (DESCRIPTORS && USE_SETTER) setSymbolDescriptor(ObjectPrototype, tag, { configurable: true, set: setter });
    return wrap(tag, description);
  };

  redefine($Symbol[PROTOTYPE], 'toString', function toString() {
    return getInternalState(this).tag;
  });

  redefine($Symbol, 'withoutSetter', function (description) {
    return wrap(uid(description), description);
  });

  propertyIsEnumerableModule.f = $propertyIsEnumerable;
  definePropertyModule.f = $defineProperty;
  getOwnPropertyDescriptorModule.f = $getOwnPropertyDescriptor;
  getOwnPropertyNamesModule.f = getOwnPropertyNamesExternal.f = $getOwnPropertyNames;
  getOwnPropertySymbolsModule.f = $getOwnPropertySymbols;

  wrappedWellKnownSymbolModule.f = function (name) {
    return wrap(wellKnownSymbol(name), name);
  };

  if (DESCRIPTORS) {
    // https://github.com/tc39/proposal-Symbol-description
    nativeDefineProperty($Symbol[PROTOTYPE], 'description', {
      configurable: true,
      get: function description() {
        return getInternalState(this).description;
      }
    });
    if (!IS_PURE) {
      redefine(ObjectPrototype, 'propertyIsEnumerable', $propertyIsEnumerable, { unsafe: true });
    }
  }
}

$({ global: true, wrap: true, forced: !NATIVE_SYMBOL, sham: !NATIVE_SYMBOL }, {
  Symbol: $Symbol
});

$forEach(objectKeys(WellKnownSymbolsStore), function (name) {
  defineWellKnownSymbol(name);
});

$({ target: SYMBOL, stat: true, forced: !NATIVE_SYMBOL }, {
  // `Symbol.for` method
  // https://tc39.es/ecma262/#sec-symbol.for
  'for': function (key) {
    var string = String(key);
    if (has(StringToSymbolRegistry, string)) return StringToSymbolRegistry[string];
    var symbol = $Symbol(string);
    StringToSymbolRegistry[string] = symbol;
    SymbolToStringRegistry[symbol] = string;
    return symbol;
  },
  // `Symbol.keyFor` method
  // https://tc39.es/ecma262/#sec-symbol.keyfor
  keyFor: function keyFor(sym) {
    if (!isSymbol(sym)) throw TypeError(sym + ' is not a symbol');
    if (has(SymbolToStringRegistry, sym)) return SymbolToStringRegistry[sym];
  },
  useSetter: function () { USE_SETTER = true; },
  useSimple: function () { USE_SETTER = false; }
});

$({ target: 'Object', stat: true, forced: !NATIVE_SYMBOL, sham: !DESCRIPTORS }, {
  // `Object.create` method
  // https://tc39.es/ecma262/#sec-object.create
  create: $create,
  // `Object.defineProperty` method
  // https://tc39.es/ecma262/#sec-object.defineproperty
  defineProperty: $defineProperty,
  // `Object.defineProperties` method
  // https://tc39.es/ecma262/#sec-object.defineproperties
  defineProperties: $defineProperties,
  // `Object.getOwnPropertyDescriptor` method
  // https://tc39.es/ecma262/#sec-object.getownpropertydescriptors
  getOwnPropertyDescriptor: $getOwnPropertyDescriptor
});

$({ target: 'Object', stat: true, forced: !NATIVE_SYMBOL }, {
  // `Object.getOwnPropertyNames` method
  // https://tc39.es/ecma262/#sec-object.getownpropertynames
  getOwnPropertyNames: $getOwnPropertyNames,
  // `Object.getOwnPropertySymbols` method
  // https://tc39.es/ecma262/#sec-object.getownpropertysymbols
  getOwnPropertySymbols: $getOwnPropertySymbols
});

// Chrome 38 and 39 `Object.getOwnPropertySymbols` fails on primitives
// https://bugs.chromium.org/p/v8/issues/detail?id=3443
$({ target: 'Object', stat: true, forced: fails(function () { getOwnPropertySymbolsModule.f(1); }) }, {
  getOwnPropertySymbols: function getOwnPropertySymbols(it) {
    return getOwnPropertySymbolsModule.f(toObject(it));
  }
});

// `JSON.stringify` method behavior with symbols
// https://tc39.es/ecma262/#sec-json.stringify
if ($stringify) {
  var FORCED_JSON_STRINGIFY = !NATIVE_SYMBOL || fails(function () {
    var symbol = $Symbol();
    // MS Edge converts symbol values to JSON as {}
    return $stringify([symbol]) != '[null]'
      // WebKit converts symbol values to JSON as null
      || $stringify({ a: symbol }) != '{}'
      // V8 throws on boxed symbols
      || $stringify(Object(symbol)) != '{}';
  });

  $({ target: 'JSON', stat: true, forced: FORCED_JSON_STRINGIFY }, {
    // eslint-disable-next-line no-unused-vars -- required for `.length`
    stringify: function stringify(it, replacer, space) {
      var args = [it];
      var index = 1;
      var $replacer;
      while (arguments.length > index) args.push(arguments[index++]);
      $replacer = replacer;
      if (!isObject(replacer) && it === undefined || isSymbol(it)) return; // IE8 returns string on undefined
      if (!isArray(replacer)) replacer = function (key, value) {
        if (typeof $replacer == 'function') value = $replacer.call(this, key, value);
        if (!isSymbol(value)) return value;
      };
      args[1] = replacer;
      return $stringify.apply(null, args);
    }
  });
}

// `Symbol.prototype[@@toPrimitive]` method
// https://tc39.es/ecma262/#sec-symbol.prototype-@@toprimitive
if (!$Symbol[PROTOTYPE][TO_PRIMITIVE]) {
  createNonEnumerableProperty($Symbol[PROTOTYPE], TO_PRIMITIVE, $Symbol[PROTOTYPE].valueOf);
}
// `Symbol.prototype[@@toStringTag]` property
// https://tc39.es/ecma262/#sec-symbol.prototype-@@tostringtag
setToStringTag($Symbol, SYMBOL);

hiddenKeys[HIDDEN] = true;


/***/ }),

/***/ "a640":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var fails = __webpack_require__("d039");

module.exports = function (METHOD_NAME, argument) {
  var method = [][METHOD_NAME];
  return !!method && fails(function () {
    // eslint-disable-next-line no-useless-call,no-throw-literal -- required for testing
    method.call(null, argument || function () { throw 1; }, 1);
  });
};


/***/ }),

/***/ "a691":
/***/ (function(module, exports) {

var ceil = Math.ceil;
var floor = Math.floor;

// `ToInteger` abstract operation
// https://tc39.es/ecma262/#sec-tointeger
module.exports = function (argument) {
  return isNaN(argument = +argument) ? 0 : (argument > 0 ? floor : ceil)(argument);
};


/***/ }),

/***/ "a757":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


const URDNA2015Sync = __webpack_require__("4284");

module.exports = class URDNA2012Sync extends URDNA2015Sync {
  constructor() {
    super();
    this.name = 'URGNA2012';
    this.hashAlgorithm = 'sha1';
  }

  // helper for modifying component during Hash First Degree Quads
  modifyFirstDegreeComponent(id, component, key) {
    if(component.termType !== 'BlankNode') {
      return component;
    }
    if(key === 'graph') {
      return {
        termType: 'BlankNode',
        value: '_:g'
      };
    }
    return {
      termType: 'BlankNode',
      value: (component.value === id ? '_:a' : '_:z')
    };
  }

  // helper for getting a related predicate
  getRelatedPredicate(quad) {
    return quad.predicate.value;
  }

  // helper for creating hash to related blank nodes map
  createHashToRelated(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    const hashToRelated = new Map();

    // 2) Get a reference, quads, to the list of quads in the blank node to
    // quads map for the key identifier.
    const quads = this.blankNodeInfo.get(id).quads;

    // 3) For each quad in quads:
    for(const quad of quads) {
      // 3.1) If the quad's subject is a blank node that does not match
      // identifier, set hash to the result of the Hash Related Blank Node
      // algorithm, passing the blank node identifier for subject as related,
      // quad, path identifier issuer as issuer, and p as position.
      let position;
      let related;
      if(quad.subject.termType === 'BlankNode' && quad.subject.value !== id) {
        related = quad.subject.value;
        position = 'p';
      } else if(
        quad.object.termType === 'BlankNode' && quad.object.value !== id) {
        // 3.2) Otherwise, if quad's object is a blank node that does not match
        // identifier, to the result of the Hash Related Blank Node algorithm,
        // passing the blank node identifier for object as related, quad, path
        // identifier issuer as issuer, and r as position.
        related = quad.object.value;
        position = 'r';
      } else {
        // 3.3) Otherwise, continue to the next quad.
        continue;
      }
      // 3.4) Add a mapping of hash to the blank node identifier for the
      // component that matched (subject or object) to hash to related blank
      // nodes map, adding an entry as necessary.
      const hash = this.hashRelatedBlankNode(related, quad, issuer, position);
      const entries = hashToRelated.get(hash);
      if(entries) {
        entries.push(related);
      } else {
        hashToRelated.set(hash, [related]);
      }
    }

    return hashToRelated;
  }
};


/***/ }),

/***/ "ab13":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");

var MATCH = wellKnownSymbol('match');

module.exports = function (METHOD_NAME) {
  var regexp = /./;
  try {
    '/./'[METHOD_NAME](regexp);
  } catch (error1) {
    try {
      regexp[MATCH] = false;
      return '/./'[METHOD_NAME](regexp);
    } catch (error2) { /* empty */ }
  } return false;
};


/***/ }),

/***/ "ac1f":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var exec = __webpack_require__("9263");

// `RegExp.prototype.exec` method
// https://tc39.es/ecma262/#sec-regexp.prototype.exec
$({ target: 'RegExp', proto: true, forced: /./.exec !== exec }, {
  exec: exec
});


/***/ }),

/***/ "ad6d":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var anObject = __webpack_require__("825a");

// `RegExp.prototype.flags` getter implementation
// https://tc39.es/ecma262/#sec-get-regexp.prototype.flags
module.exports = function () {
  var that = anObject(this);
  var result = '';
  if (that.global) result += 'g';
  if (that.ignoreCase) result += 'i';
  if (that.multiline) result += 'm';
  if (that.dotAll) result += 's';
  if (that.unicode) result += 'u';
  if (that.sticky) result += 'y';
  return result;
};


/***/ }),

/***/ "ae29":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


const IdentifierIssuer = __webpack_require__("8acc");
const MessageDigest = __webpack_require__("5a76");
const Permuter = __webpack_require__("c79e");
const NQuads = __webpack_require__("65e1");

module.exports = class URDNA2015 {
  constructor() {
    this.name = 'URDNA2015';
    this.blankNodeInfo = new Map();
    this.canonicalIssuer = new IdentifierIssuer('_:c14n');
    this.hashAlgorithm = 'sha256';
    this.quads = null;
  }

  // 4.4) Normalization Algorithm
  async main(dataset) {
    this.quads = dataset;

    // 1) Create the normalization state.
    // 2) For every quad in input dataset:
    for(const quad of dataset) {
      // 2.1) For each blank node that occurs in the quad, add a reference
      // to the quad using the blank node identifier in the blank node to
      // quads map, creating a new entry if necessary.
      this._addBlankNodeQuadInfo({quad, component: quad.subject});
      this._addBlankNodeQuadInfo({quad, component: quad.object});
      this._addBlankNodeQuadInfo({quad, component: quad.graph});
    }

    // 3) Create a list of non-normalized blank node identifiers
    // non-normalized identifiers and populate it using the keys from the
    // blank node to quads map.
    // Note: We use a map here and it was generated during step 2.

    // 4) `simple` flag is skipped -- loop is optimized away. This optimization
    // is permitted because there was a typo in the hash first degree quads
    // algorithm in the URDNA2015 spec that was implemented widely making it
    // such that it could not be fixed; the result was that the loop only
    // needs to be run once and the first degree quad hashes will never change.
    // 5.1-5.2 are skipped; first degree quad hashes are generated just once
    // for all non-normalized blank nodes.

    // 5.3) For each blank node identifier identifier in non-normalized
    // identifiers:
    const hashToBlankNodes = new Map();
    const nonNormalized = [...this.blankNodeInfo.keys()];
    let i = 0;
    for(const id of nonNormalized) {
      // Note: batch hashing first degree quads 100 at a time
      if(++i % 100 === 0) {
        await this._yield();
      }
      // steps 5.3.1 and 5.3.2:
      await this._hashAndTrackBlankNode({id, hashToBlankNodes});
    }

    // 5.4) For each hash to identifier list mapping in hash to blank
    // nodes map, lexicographically-sorted by hash:
    const hashes = [...hashToBlankNodes.keys()].sort();
    // optimize away second sort, gather non-unique hashes in order as we go
    const nonUnique = [];
    for(const hash of hashes) {
      // 5.4.1) If the length of identifier list is greater than 1,
      // continue to the next mapping.
      const idList = hashToBlankNodes.get(hash);
      if(idList.length > 1) {
        nonUnique.push(idList);
        continue;
      }

      // 5.4.2) Use the Issue Identifier algorithm, passing canonical
      // issuer and the single blank node identifier in identifier
      // list, identifier, to issue a canonical replacement identifier
      // for identifier.
      const id = idList[0];
      this.canonicalIssuer.getId(id);

      // Note: These steps are skipped, optimized away since the loop
      // only needs to be run once.
      // 5.4.3) Remove identifier from non-normalized identifiers.
      // 5.4.4) Remove hash from the hash to blank nodes map.
      // 5.4.5) Set simple to true.
    }

    // 6) For each hash to identifier list mapping in hash to blank nodes map,
    // lexicographically-sorted by hash:
    // Note: sort optimized away, use `nonUnique`.
    for(const idList of nonUnique) {
      // 6.1) Create hash path list where each item will be a result of
      // running the Hash N-Degree Quads algorithm.
      const hashPathList = [];

      // 6.2) For each blank node identifier identifier in identifier list:
      for(const id of idList) {
        // 6.2.1) If a canonical identifier has already been issued for
        // identifier, continue to the next identifier.
        if(this.canonicalIssuer.hasId(id)) {
          continue;
        }

        // 6.2.2) Create temporary issuer, an identifier issuer
        // initialized with the prefix _:b.
        const issuer = new IdentifierIssuer('_:b');

        // 6.2.3) Use the Issue Identifier algorithm, passing temporary
        // issuer and identifier, to issue a new temporary blank node
        // identifier for identifier.
        issuer.getId(id);

        // 6.2.4) Run the Hash N-Degree Quads algorithm, passing
        // temporary issuer, and append the result to the hash path list.
        const result = await this.hashNDegreeQuads(id, issuer);
        hashPathList.push(result);
      }

      // 6.3) For each result in the hash path list,
      // lexicographically-sorted by the hash in result:
      hashPathList.sort(_stringHashCompare);
      for(const result of hashPathList) {
        // 6.3.1) For each blank node identifier, existing identifier,
        // that was issued a temporary identifier by identifier issuer
        // in result, issue a canonical identifier, in the same order,
        // using the Issue Identifier algorithm, passing canonical
        // issuer and existing identifier.
        const oldIds = result.issuer.getOldIds();
        for(const id of oldIds) {
          this.canonicalIssuer.getId(id);
        }
      }
    }

    /* Note: At this point all blank nodes in the set of RDF quads have been
    assigned canonical identifiers, which have been stored in the canonical
    issuer. Here each quad is updated by assigning each of its blank nodes
    its new identifier. */

    // 7) For each quad, quad, in input dataset:
    const normalized = [];
    for(const quad of this.quads) {
      // 7.1) Create a copy, quad copy, of quad and replace any existing
      // blank node identifiers using the canonical identifiers
      // previously issued by canonical issuer.
      // Note: We optimize with shallow copies here.
      const q = {...quad};
      q.subject = this._useCanonicalId({component: q.subject});
      q.object = this._useCanonicalId({component: q.object});
      q.graph = this._useCanonicalId({component: q.graph});
      // 7.2) Add quad copy to the normalized dataset.
      normalized.push(NQuads.serializeQuad(q));
    }

    // sort normalized output
    normalized.sort();

    // 8) Return the normalized dataset.
    return normalized.join('');
  }

  // 4.6) Hash First Degree Quads
  async hashFirstDegreeQuads(id) {
    // 1) Initialize nquads to an empty list. It will be used to store quads in
    // N-Quads format.
    const nquads = [];

    // 2) Get the list of quads `quads` associated with the reference blank node
    // identifier in the blank node to quads map.
    const info = this.blankNodeInfo.get(id);
    const quads = info.quads;

    // 3) For each quad `quad` in `quads`:
    for(const quad of quads) {
      // 3.1) Serialize the quad in N-Quads format with the following special
      // rule:

      // 3.1.1) If any component in quad is an blank node, then serialize it
      // using a special identifier as follows:
      const copy = {
        subject: null, predicate: quad.predicate, object: null, graph: null
      };
      // 3.1.2) If the blank node's existing blank node identifier matches
      // the reference blank node identifier then use the blank node
      // identifier _:a, otherwise, use the blank node identifier _:z.
      copy.subject = this.modifyFirstDegreeComponent(
        id, quad.subject, 'subject');
      copy.object = this.modifyFirstDegreeComponent(
        id, quad.object, 'object');
      copy.graph = this.modifyFirstDegreeComponent(
        id, quad.graph, 'graph');
      nquads.push(NQuads.serializeQuad(copy));
    }

    // 4) Sort nquads in lexicographical order.
    nquads.sort();

    // 5) Return the hash that results from passing the sorted, joined nquads
    // through the hash algorithm.
    const md = new MessageDigest(this.hashAlgorithm);
    for(const nquad of nquads) {
      md.update(nquad);
    }
    info.hash = await md.digest();
    return info.hash;
  }

  // 4.7) Hash Related Blank Node
  async hashRelatedBlankNode(related, quad, issuer, position) {
    // 1) Set the identifier to use for related, preferring first the canonical
    // identifier for related if issued, second the identifier issued by issuer
    // if issued, and last, if necessary, the result of the Hash First Degree
    // Quads algorithm, passing related.
    let id;
    if(this.canonicalIssuer.hasId(related)) {
      id = this.canonicalIssuer.getId(related);
    } else if(issuer.hasId(related)) {
      id = issuer.getId(related);
    } else {
      id = this.blankNodeInfo.get(related).hash;
    }

    // 2) Initialize a string input to the value of position.
    // Note: We use a hash object instead.
    const md = new MessageDigest(this.hashAlgorithm);
    md.update(position);

    // 3) If position is not g, append <, the value of the predicate in quad,
    // and > to input.
    if(position !== 'g') {
      md.update(this.getRelatedPredicate(quad));
    }

    // 4) Append identifier to input.
    md.update(id);

    // 5) Return the hash that results from passing input through the hash
    // algorithm.
    return md.digest();
  }

  // 4.8) Hash N-Degree Quads
  async hashNDegreeQuads(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    // Note: 2) and 3) handled within `createHashToRelated`
    const md = new MessageDigest(this.hashAlgorithm);
    const hashToRelated = await this.createHashToRelated(id, issuer);

    // 4) Create an empty string, data to hash.
    // Note: We created a hash object `md` above instead.

    // 5) For each related hash to blank node list mapping in hash to related
    // blank nodes map, sorted lexicographically by related hash:
    const hashes = [...hashToRelated.keys()].sort();
    for(const hash of hashes) {
      // 5.1) Append the related hash to the data to hash.
      md.update(hash);

      // 5.2) Create a string chosen path.
      let chosenPath = '';

      // 5.3) Create an unset chosen issuer variable.
      let chosenIssuer;

      // 5.4) For each permutation of blank node list:
      const permuter = new Permuter(hashToRelated.get(hash));
      let i = 0;
      while(permuter.hasNext()) {
        const permutation = permuter.next();
        // Note: batch permutations 3 at a time
        if(++i % 3 === 0) {
          await this._yield();
        }

        // 5.4.1) Create a copy of issuer, issuer copy.
        let issuerCopy = issuer.clone();

        // 5.4.2) Create a string path.
        let path = '';

        // 5.4.3) Create a recursion list, to store blank node identifiers
        // that must be recursively processed by this algorithm.
        const recursionList = [];

        // 5.4.4) For each related in permutation:
        let nextPermutation = false;
        for(const related of permutation) {
          // 5.4.4.1) If a canonical identifier has been issued for
          // related, append it to path.
          if(this.canonicalIssuer.hasId(related)) {
            path += this.canonicalIssuer.getId(related);
          } else {
            // 5.4.4.2) Otherwise:
            // 5.4.4.2.1) If issuer copy has not issued an identifier for
            // related, append related to recursion list.
            if(!issuerCopy.hasId(related)) {
              recursionList.push(related);
            }
            // 5.4.4.2.2) Use the Issue Identifier algorithm, passing
            // issuer copy and related and append the result to path.
            path += issuerCopy.getId(related);
          }

          // 5.4.4.3) If chosen path is not empty and the length of path
          // is greater than or equal to the length of chosen path and
          // path is lexicographically greater than chosen path, then
          // skip to the next permutation.
          // Note: Comparing path length to chosen path length can be optimized
          // away; only compare lexicographically.
          if(chosenPath.length !== 0 && path > chosenPath) {
            nextPermutation = true;
            break;
          }
        }

        if(nextPermutation) {
          continue;
        }

        // 5.4.5) For each related in recursion list:
        for(const related of recursionList) {
          // 5.4.5.1) Set result to the result of recursively executing
          // the Hash N-Degree Quads algorithm, passing related for
          // identifier and issuer copy for path identifier issuer.
          const result = await this.hashNDegreeQuads(related, issuerCopy);

          // 5.4.5.2) Use the Issue Identifier algorithm, passing issuer
          // copy and related and append the result to path.
          path += issuerCopy.getId(related);

          // 5.4.5.3) Append <, the hash in result, and > to path.
          path += `<${result.hash}>`;

          // 5.4.5.4) Set issuer copy to the identifier issuer in
          // result.
          issuerCopy = result.issuer;

          // 5.4.5.5) If chosen path is not empty and the length of path
          // is greater than or equal to the length of chosen path and
          // path is lexicographically greater than chosen path, then
          // skip to the next permutation.
          // Note: Comparing path length to chosen path length can be optimized
          // away; only compare lexicographically.
          if(chosenPath.length !== 0 && path > chosenPath) {
            nextPermutation = true;
            break;
          }
        }

        if(nextPermutation) {
          continue;
        }

        // 5.4.6) If chosen path is empty or path is lexicographically
        // less than chosen path, set chosen path to path and chosen
        // issuer to issuer copy.
        if(chosenPath.length === 0 || path < chosenPath) {
          chosenPath = path;
          chosenIssuer = issuerCopy;
        }
      }

      // 5.5) Append chosen path to data to hash.
      md.update(chosenPath);

      // 5.6) Replace issuer, by reference, with chosen issuer.
      issuer = chosenIssuer;
    }

    // 6) Return issuer and the hash that results from passing data to hash
    // through the hash algorithm.
    return {hash: await md.digest(), issuer};
  }

  // helper for modifying component during Hash First Degree Quads
  modifyFirstDegreeComponent(id, component) {
    if(component.termType !== 'BlankNode') {
      return component;
    }
    /* Note: A mistake in the URDNA2015 spec that made its way into
    implementations (and therefore must stay to avoid interop breakage)
    resulted in an assigned canonical ID, if available for
    `component.value`, not being used in place of `_:a`/`_:z`, so
    we don't use it here. */
    return {
      termType: 'BlankNode',
      value: component.value === id ? '_:a' : '_:z'
    };
  }

  // helper for getting a related predicate
  getRelatedPredicate(quad) {
    return `<${quad.predicate.value}>`;
  }

  // helper for creating hash to related blank nodes map
  async createHashToRelated(id, issuer) {
    // 1) Create a hash to related blank nodes map for storing hashes that
    // identify related blank nodes.
    const hashToRelated = new Map();

    // 2) Get a reference, quads, to the list of quads in the blank node to
    // quads map for the key identifier.
    const quads = this.blankNodeInfo.get(id).quads;

    // 3) For each quad in quads:
    let i = 0;
    for(const quad of quads) {
      // Note: batch hashing related blank node quads 100 at a time
      if(++i % 100 === 0) {
        await this._yield();
      }
      // 3.1) For each component in quad, if component is the subject, object,
      // and graph name and it is a blank node that is not identified by
      // identifier:
      // steps 3.1.1 and 3.1.2 occur in helpers:
      await Promise.all([
        this._addRelatedBlankNodeHash({
          quad, component: quad.subject, position: 's',
          id, issuer, hashToRelated
        }),
        this._addRelatedBlankNodeHash({
          quad, component: quad.object, position: 'o',
          id, issuer, hashToRelated
        }),
        this._addRelatedBlankNodeHash({
          quad, component: quad.graph, position: 'g',
          id, issuer, hashToRelated
        })
      ]);
    }

    return hashToRelated;
  }

  async _hashAndTrackBlankNode({id, hashToBlankNodes}) {
    // 5.3.1) Create a hash, hash, according to the Hash First Degree
    // Quads algorithm.
    const hash = await this.hashFirstDegreeQuads(id);

    // 5.3.2) Add hash and identifier to hash to blank nodes map,
    // creating a new entry if necessary.
    const idList = hashToBlankNodes.get(hash);
    if(!idList) {
      hashToBlankNodes.set(hash, [id]);
    } else {
      idList.push(id);
    }
  }

  _addBlankNodeQuadInfo({quad, component}) {
    if(component.termType !== 'BlankNode') {
      return;
    }
    const id = component.value;
    const info = this.blankNodeInfo.get(id);
    if(info) {
      info.quads.add(quad);
    } else {
      this.blankNodeInfo.set(id, {quads: new Set([quad]), hash: null});
    }
  }

  async _addRelatedBlankNodeHash(
    {quad, component, position, id, issuer, hashToRelated}) {
    if(!(component.termType === 'BlankNode' && component.value !== id)) {
      return;
    }
    // 3.1.1) Set hash to the result of the Hash Related Blank Node
    // algorithm, passing the blank node identifier for component as
    // related, quad, path identifier issuer as issuer, and position as
    // either s, o, or g based on whether component is a subject, object,
    // graph name, respectively.
    const related = component.value;
    const hash = await this.hashRelatedBlankNode(
      related, quad, issuer, position);

    // 3.1.2) Add a mapping of hash to the blank node identifier for
    // component to hash to related blank nodes map, adding an entry as
    // necessary.
    const entries = hashToRelated.get(hash);
    if(entries) {
      entries.push(related);
    } else {
      hashToRelated.set(hash, [related]);
    }
  }

  _useCanonicalId({component}) {
    if(component.termType === 'BlankNode' &&
      !component.value.startsWith(this.canonicalIssuer.prefix)) {
      return {
        termType: 'BlankNode',
        value: this.canonicalIssuer.getId(component.value)
      };
    }
    return component;
  }

  async _yield() {
    return new Promise(resolve => setImmediate(resolve));
  }
};

function _stringHashCompare(a, b) {
  return a.hash < b.hash ? -1 : a.hash > b.hash ? 1 : 0;
}


/***/ }),

/***/ "ae93":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var fails = __webpack_require__("d039");
var getPrototypeOf = __webpack_require__("e163");
var createNonEnumerableProperty = __webpack_require__("9112");
var has = __webpack_require__("5135");
var wellKnownSymbol = __webpack_require__("b622");
var IS_PURE = __webpack_require__("c430");

var ITERATOR = wellKnownSymbol('iterator');
var BUGGY_SAFARI_ITERATORS = false;

var returnThis = function () { return this; };

// `%IteratorPrototype%` object
// https://tc39.es/ecma262/#sec-%iteratorprototype%-object
var IteratorPrototype, PrototypeOfArrayIteratorPrototype, arrayIterator;

/* eslint-disable es/no-array-prototype-keys -- safe */
if ([].keys) {
  arrayIterator = [].keys();
  // Safari 8 has buggy iterators w/o `next`
  if (!('next' in arrayIterator)) BUGGY_SAFARI_ITERATORS = true;
  else {
    PrototypeOfArrayIteratorPrototype = getPrototypeOf(getPrototypeOf(arrayIterator));
    if (PrototypeOfArrayIteratorPrototype !== Object.prototype) IteratorPrototype = PrototypeOfArrayIteratorPrototype;
  }
}

var NEW_ITERATOR_PROTOTYPE = IteratorPrototype == undefined || fails(function () {
  var test = {};
  // FF44- legacy iterators case
  return IteratorPrototype[ITERATOR].call(test) !== test;
});

if (NEW_ITERATOR_PROTOTYPE) IteratorPrototype = {};

// 25.1.2.1.1 %IteratorPrototype%[@@iterator]()
if ((!IS_PURE || NEW_ITERATOR_PROTOTYPE) && !has(IteratorPrototype, ITERATOR)) {
  createNonEnumerableProperty(IteratorPrototype, ITERATOR, returnThis);
}

module.exports = {
  IteratorPrototype: IteratorPrototype,
  BUGGY_SAFARI_ITERATORS: BUGGY_SAFARI_ITERATORS
};


/***/ }),

/***/ "aed6":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_DataInput_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("ee00");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_DataInput_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_DataInput_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "af03":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");

// check the existence of a method, lowercase
// of a tag and escaping quotes in arguments
module.exports = function (METHOD_NAME) {
  return fails(function () {
    var test = ''[METHOD_NAME]('"');
    return test !== test.toLowerCase() || test.split('"').length > 3;
  });
};


/***/ }),

/***/ "b041":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var TO_STRING_TAG_SUPPORT = __webpack_require__("00ee");
var classof = __webpack_require__("f5df");

// `Object.prototype.toString` method implementation
// https://tc39.es/ecma262/#sec-object.prototype.tostring
module.exports = TO_STRING_TAG_SUPPORT ? {}.toString : function toString() {
  return '[object ' + classof(this) + ']';
};


/***/ }),

/***/ "b0c0":
/***/ (function(module, exports, __webpack_require__) {

var DESCRIPTORS = __webpack_require__("83ab");
var defineProperty = __webpack_require__("9bf2").f;

var FunctionPrototype = Function.prototype;
var FunctionPrototypeToString = FunctionPrototype.toString;
var nameRE = /^\s*function ([^ (]*)/;
var NAME = 'name';

// Function instances `.name` property
// https://tc39.es/ecma262/#sec-function-instances-name
if (DESCRIPTORS && !(NAME in FunctionPrototype)) {
  defineProperty(FunctionPrototype, NAME, {
    configurable: true,
    get: function () {
      try {
        return FunctionPrototypeToString.call(this).match(nameRE)[1];
      } catch (error) {
        return '';
      }
    }
  });
}


/***/ }),

/***/ "b3dd":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Input_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("08d2");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Input_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Input_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "b4bf":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "b575":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var getOwnPropertyDescriptor = __webpack_require__("06cf").f;
var macrotask = __webpack_require__("2cf4").set;
var IS_IOS = __webpack_require__("1cdc");
var IS_WEBOS_WEBKIT = __webpack_require__("a4b4");
var IS_NODE = __webpack_require__("605d");

var MutationObserver = global.MutationObserver || global.WebKitMutationObserver;
var document = global.document;
var process = global.process;
var Promise = global.Promise;
// Node.js 11 shows ExperimentalWarning on getting `queueMicrotask`
var queueMicrotaskDescriptor = getOwnPropertyDescriptor(global, 'queueMicrotask');
var queueMicrotask = queueMicrotaskDescriptor && queueMicrotaskDescriptor.value;

var flush, head, last, notify, toggle, node, promise, then;

// modern engines have queueMicrotask method
if (!queueMicrotask) {
  flush = function () {
    var parent, fn;
    if (IS_NODE && (parent = process.domain)) parent.exit();
    while (head) {
      fn = head.fn;
      head = head.next;
      try {
        fn();
      } catch (error) {
        if (head) notify();
        else last = undefined;
        throw error;
      }
    } last = undefined;
    if (parent) parent.enter();
  };

  // browsers with MutationObserver, except iOS - https://github.com/zloirock/core-js/issues/339
  // also except WebOS Webkit https://github.com/zloirock/core-js/issues/898
  if (!IS_IOS && !IS_NODE && !IS_WEBOS_WEBKIT && MutationObserver && document) {
    toggle = true;
    node = document.createTextNode('');
    new MutationObserver(flush).observe(node, { characterData: true });
    notify = function () {
      node.data = toggle = !toggle;
    };
  // environments with maybe non-completely correct, but existent Promise
  } else if (Promise && Promise.resolve) {
    // Promise.resolve without an argument throws an error in LG WebOS 2
    promise = Promise.resolve(undefined);
    then = promise.then;
    notify = function () {
      then.call(promise, flush);
    };
  // Node.js without promises
  } else if (IS_NODE) {
    notify = function () {
      process.nextTick(flush);
    };
  // for other environments - macrotask based on:
  // - setImmediate
  // - MessageChannel
  // - window.postMessag
  // - onreadystatechange
  // - setTimeout
  } else {
    notify = function () {
      // strange IE + webpack dev server bug - use .call(global)
      macrotask.call(global, flush);
    };
  }
}

module.exports = queueMicrotask || function (fn) {
  var task = { fn: fn, next: undefined };
  if (last) last.next = task;
  if (!head) {
    head = task;
    notify();
  } last = task;
};


/***/ }),

/***/ "b622":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var shared = __webpack_require__("5692");
var has = __webpack_require__("5135");
var uid = __webpack_require__("90e3");
var NATIVE_SYMBOL = __webpack_require__("4930");
var USE_SYMBOL_AS_UID = __webpack_require__("fdbf");

var WellKnownSymbolsStore = shared('wks');
var Symbol = global.Symbol;
var createWellKnownSymbol = USE_SYMBOL_AS_UID ? Symbol : Symbol && Symbol.withoutSetter || uid;

module.exports = function (name) {
  if (!has(WellKnownSymbolsStore, name) || !(NATIVE_SYMBOL || typeof WellKnownSymbolsStore[name] == 'string')) {
    if (NATIVE_SYMBOL && has(Symbol, name)) {
      WellKnownSymbolsStore[name] = Symbol[name];
    } else {
      WellKnownSymbolsStore[name] = createWellKnownSymbol('Symbol.' + name);
    }
  } return WellKnownSymbolsStore[name];
};


/***/ }),

/***/ "b64b":
/***/ (function(module, exports, __webpack_require__) {

var $ = __webpack_require__("23e7");
var toObject = __webpack_require__("7b0b");
var nativeKeys = __webpack_require__("df75");
var fails = __webpack_require__("d039");

var FAILS_ON_PRIMITIVES = fails(function () { nativeKeys(1); });

// `Object.keys` method
// https://tc39.es/ecma262/#sec-object.keys
$({ target: 'Object', stat: true, forced: FAILS_ON_PRIMITIVES }, {
  keys: function keys(it) {
    return nativeKeys(toObject(it));
  }
});


/***/ }),

/***/ "b727":
/***/ (function(module, exports, __webpack_require__) {

var bind = __webpack_require__("0366");
var IndexedObject = __webpack_require__("44ad");
var toObject = __webpack_require__("7b0b");
var toLength = __webpack_require__("50c4");
var arraySpeciesCreate = __webpack_require__("65f0");

var push = [].push;

// `Array.prototype.{ forEach, map, filter, some, every, find, findIndex, filterOut }` methods implementation
var createMethod = function (TYPE) {
  var IS_MAP = TYPE == 1;
  var IS_FILTER = TYPE == 2;
  var IS_SOME = TYPE == 3;
  var IS_EVERY = TYPE == 4;
  var IS_FIND_INDEX = TYPE == 6;
  var IS_FILTER_OUT = TYPE == 7;
  var NO_HOLES = TYPE == 5 || IS_FIND_INDEX;
  return function ($this, callbackfn, that, specificCreate) {
    var O = toObject($this);
    var self = IndexedObject(O);
    var boundFunction = bind(callbackfn, that, 3);
    var length = toLength(self.length);
    var index = 0;
    var create = specificCreate || arraySpeciesCreate;
    var target = IS_MAP ? create($this, length) : IS_FILTER || IS_FILTER_OUT ? create($this, 0) : undefined;
    var value, result;
    for (;length > index; index++) if (NO_HOLES || index in self) {
      value = self[index];
      result = boundFunction(value, index, O);
      if (TYPE) {
        if (IS_MAP) target[index] = result; // map
        else if (result) switch (TYPE) {
          case 3: return true;              // some
          case 5: return value;             // find
          case 6: return index;             // findIndex
          case 2: push.call(target, value); // filter
        } else switch (TYPE) {
          case 4: return false;             // every
          case 7: push.call(target, value); // filterOut
        }
      }
    }
    return IS_FIND_INDEX ? -1 : IS_SOME || IS_EVERY ? IS_EVERY : target;
  };
};

module.exports = {
  // `Array.prototype.forEach` method
  // https://tc39.es/ecma262/#sec-array.prototype.foreach
  forEach: createMethod(0),
  // `Array.prototype.map` method
  // https://tc39.es/ecma262/#sec-array.prototype.map
  map: createMethod(1),
  // `Array.prototype.filter` method
  // https://tc39.es/ecma262/#sec-array.prototype.filter
  filter: createMethod(2),
  // `Array.prototype.some` method
  // https://tc39.es/ecma262/#sec-array.prototype.some
  some: createMethod(3),
  // `Array.prototype.every` method
  // https://tc39.es/ecma262/#sec-array.prototype.every
  every: createMethod(4),
  // `Array.prototype.find` method
  // https://tc39.es/ecma262/#sec-array.prototype.find
  find: createMethod(5),
  // `Array.prototype.findIndex` method
  // https://tc39.es/ecma262/#sec-array.prototype.findIndex
  findIndex: createMethod(6),
  // `Array.prototype.filterOut` method
  // https://github.com/tc39/proposal-array-filtering
  filterOut: createMethod(7)
};


/***/ }),

/***/ "bd17":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "c04e":
/***/ (function(module, exports, __webpack_require__) {

var isObject = __webpack_require__("861d");

// `ToPrimitive` abstract operation
// https://tc39.es/ecma262/#sec-toprimitive
// instead of the ES6 spec version, we didn't implement @@toPrimitive case
// and the second argument - flag - preferred type is a string
module.exports = function (input, PREFERRED_STRING) {
  if (!isObject(input)) return input;
  var fn, val;
  if (PREFERRED_STRING && typeof (fn = input.toString) == 'function' && !isObject(val = fn.call(input))) return val;
  if (typeof (fn = input.valueOf) == 'function' && !isObject(val = fn.call(input))) return val;
  if (!PREFERRED_STRING && typeof (fn = input.toString) == 'function' && !isObject(val = fn.call(input))) return val;
  throw TypeError("Can't convert object to primitive value");
};


/***/ }),

/***/ "c112":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Wrapper_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("772c");
/* harmony import */ var _node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Wrapper_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_node_modules_mini_css_extract_plugin_dist_loader_js_ref_8_oneOf_1_0_node_modules_css_loader_dist_cjs_js_ref_8_oneOf_1_1_node_modules_vue_loader_lib_loaders_stylePostLoader_js_node_modules_postcss_loader_src_index_js_ref_8_oneOf_1_2_node_modules_sass_loader_dist_cjs_js_ref_8_oneOf_1_3_node_modules_cache_loader_dist_cjs_js_ref_0_0_node_modules_vue_loader_lib_index_js_vue_loader_options_Wrapper_vue_vue_type_style_index_0_lang_scss___WEBPACK_IMPORTED_MODULE_0__);
/* unused harmony reexport * */


/***/ }),

/***/ "c430":
/***/ (function(module, exports) {

module.exports = false;


/***/ }),

/***/ "c6b6":
/***/ (function(module, exports) {

var toString = {}.toString;

module.exports = function (it) {
  return toString.call(it).slice(8, -1);
};


/***/ }),

/***/ "c6cd":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var setGlobal = __webpack_require__("ce4e");

var SHARED = '__core-js_shared__';
var store = global[SHARED] || setGlobal(SHARED, {});

module.exports = store;


/***/ }),

/***/ "c79e":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2016-2021 Digital Bazaar, Inc. All rights reserved.
 */


// TODO: convert to ES6 iterable?

module.exports = class Permuter {
  /**
   * A Permuter iterates over all possible permutations of the given array
   * of elements.
   *
   * @param list the array of elements to iterate over.
   */
  constructor(list) {
    // original array
    this.current = list.sort();
    // indicates whether there are more permutations
    this.done = false;
    // directional info for permutation algorithm
    this.dir = new Map();
    for(let i = 0; i < list.length; ++i) {
      this.dir.set(list[i], true);
    }
  }

  /**
   * Returns true if there is another permutation.
   *
   * @return true if there is another permutation, false if not.
   */
  hasNext() {
    return !this.done;
  }

  /**
   * Gets the next permutation. Call hasNext() to ensure there is another one
   * first.
   *
   * @return the next permutation.
   */
  next() {
    // copy current permutation to return it
    const {current, dir} = this;
    const rval = current.slice();

    /* Calculate the next permutation using the Steinhaus-Johnson-Trotter
     permutation algorithm. */

    // get largest mobile element k
    // (mobile: element is greater than the one it is looking at)
    let k = null;
    let pos = 0;
    const length = current.length;
    for(let i = 0; i < length; ++i) {
      const element = current[i];
      const left = dir.get(element);
      if((k === null || element > k) &&
        ((left && i > 0 && element > current[i - 1]) ||
        (!left && i < (length - 1) && element > current[i + 1]))) {
        k = element;
        pos = i;
      }
    }

    // no more permutations
    if(k === null) {
      this.done = true;
    } else {
      // swap k and the element it is looking at
      const swap = dir.get(k) ? pos - 1 : pos + 1;
      current[pos] = current[swap];
      current[swap] = k;

      // reverse the direction of all elements larger than k
      for(const element of current) {
        if(element > k) {
          dir.set(element, !dir.get(element));
        }
      }
    }

    return rval;
  }
};


/***/ }),

/***/ "c8ba":
/***/ (function(module, exports) {

var g;

// This works in non-strict mode
g = (function() {
	return this;
})();

try {
	// This works if eval is allowed (see CSP)
	g = g || new Function("return this")();
} catch (e) {
	// This works if the window reference is available
	if (typeof window === "object") g = window;
}

// g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}

module.exports = g;


/***/ }),

/***/ "c91a":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "c92f":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const types = __webpack_require__("68fa");

const api = {};
module.exports = api;

/**
 * Returns true if the given value is a subject with properties.
 *
 * @param v the value to check.
 *
 * @return true if the value is a subject with properties, false if not.
 */
api.isSubject = v => {
  // Note: A value is a subject if all of these hold true:
  // 1. It is an Object.
  // 2. It is not a @value, @set, or @list.
  // 3. It has more than 1 key OR any existing key is not @id.
  if(types.isObject(v) &&
    !(('@value' in v) || ('@set' in v) || ('@list' in v))) {
    const keyCount = Object.keys(v).length;
    return (keyCount > 1 || !('@id' in v));
  }
  return false;
};

/**
 * Returns true if the given value is a subject reference.
 *
 * @param v the value to check.
 *
 * @return true if the value is a subject reference, false if not.
 */
api.isSubjectReference = v =>
  // Note: A value is a subject reference if all of these hold true:
  // 1. It is an Object.
  // 2. It has a single key: @id.
  (types.isObject(v) && Object.keys(v).length === 1 && ('@id' in v));

/**
 * Returns true if the given value is a @value.
 *
 * @param v the value to check.
 *
 * @return true if the value is a @value, false if not.
 */
api.isValue = v =>
  // Note: A value is a @value if all of these hold true:
  // 1. It is an Object.
  // 2. It has the @value property.
  types.isObject(v) && ('@value' in v);

/**
 * Returns true if the given value is a @list.
 *
 * @param v the value to check.
 *
 * @return true if the value is a @list, false if not.
 */
api.isList = v =>
  // Note: A value is a @list if all of these hold true:
  // 1. It is an Object.
  // 2. It has the @list property.
  types.isObject(v) && ('@list' in v);

/**
 * Returns true if the given value is a @graph.
 *
 * @return true if the value is a @graph, false if not.
 */
api.isGraph = v => {
  // Note: A value is a graph if all of these hold true:
  // 1. It is an object.
  // 2. It has an `@graph` key.
  // 3. It may have '@id' or '@index'
  return types.isObject(v) &&
    '@graph' in v &&
    Object.keys(v)
      .filter(key => key !== '@id' && key !== '@index').length === 1;
};

/**
 * Returns true if the given value is a simple @graph.
 *
 * @return true if the value is a simple @graph, false if not.
 */
api.isSimpleGraph = v => {
  // Note: A value is a simple graph if all of these hold true:
  // 1. It is an object.
  // 2. It has an `@graph` key.
  // 3. It has only 1 key or 2 keys where one of them is `@index`.
  return api.isGraph(v) && !('@id' in v);
};

/**
 * Returns true if the given value is a blank node.
 *
 * @param v the value to check.
 *
 * @return true if the value is a blank node, false if not.
 */
api.isBlankNode = v => {
  // Note: A value is a blank node if all of these hold true:
  // 1. It is an Object.
  // 2. If it has an @id key its value begins with '_:'.
  // 3. It has no keys OR is not a @value, @set, or @list.
  if(types.isObject(v)) {
    if('@id' in v) {
      return (v['@id'].indexOf('_:') === 0);
    }
    return (Object.keys(v).length === 0 ||
      !(('@value' in v) || ('@set' in v) || ('@list' in v)));
  }
  return false;
};


/***/ }),

/***/ "ca53":
/***/ (function(module) {

module.exports = JSON.parse("{\"@context\":{\"adms\":\"http://www.w3.org/ns/adms#\",\"dcat\":\"http://www.w3.org/ns/dcat#\",\"dcatap\":\"http://data.europa.eu/r5r/\",\"dct\":\"http://purl.org/dc/terms/\",\"foaf\":\"http://xmlns.com/foaf/0.1/\",\"locn\":\"http://www.w3.org/ns/locn#\",\"owl\":\"http://www.w3.org/2002/07/owl#\",\"odrl\":\"http://www.w3.org/ns/odrl/2/\",\"prov\":\"http://www.w3.org/ns/prov\",\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"schema\":\"http://schema.org/\",\"skos\":\"http://www.w3.org/2004/02/skos/core#\",\"spdx\":\"http://spdx.org/rdf/terms#\",\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"vann\":\"http://purl.org/vocab/vann/\",\"voaf\":\"http://purl.org/vocommons/voaf#\",\"vcard\":\"http://www.w3.org/2006/vcard/ns#\",\"time\":\"http://www.w3.org/2006/time#\"}}");

/***/ }),

/***/ "ca84":
/***/ (function(module, exports, __webpack_require__) {

var has = __webpack_require__("5135");
var toIndexedObject = __webpack_require__("fc6a");
var indexOf = __webpack_require__("4d64").indexOf;
var hiddenKeys = __webpack_require__("d012");

module.exports = function (object, names) {
  var O = toIndexedObject(object);
  var i = 0;
  var result = [];
  var key;
  for (key in O) !has(hiddenKeys, key) && has(O, key) && result.push(key);
  // Don't enum bug & hidden keys
  while (names.length > i) if (has(O, key = names[i++])) {
    ~indexOf(result, key) || result.push(key);
  }
  return result;
};


/***/ }),

/***/ "caad":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var $includes = __webpack_require__("4d64").includes;
var addToUnscopables = __webpack_require__("44d2");

// `Array.prototype.includes` method
// https://tc39.es/ecma262/#sec-array.prototype.includes
$({ target: 'Array', proto: true }, {
  includes: function includes(el /* , fromIndex = 0 */) {
    return $includes(this, el, arguments.length > 1 ? arguments[1] : undefined);
  }
});

// https://tc39.es/ecma262/#sec-array.prototype-@@unscopables
addToUnscopables('includes');


/***/ }),

/***/ "cc12":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var isObject = __webpack_require__("861d");

var document = global.document;
// typeof document.createElement is 'object' in old IE
var EXISTS = isObject(document) && isObject(document.createElement);

module.exports = function (it) {
  return EXISTS ? document.createElement(it) : {};
};


/***/ }),

/***/ "cca6":
/***/ (function(module, exports, __webpack_require__) {

var $ = __webpack_require__("23e7");
var assign = __webpack_require__("60da");

// `Object.assign` method
// https://tc39.es/ecma262/#sec-object.assign
// eslint-disable-next-line es/no-object-assign -- required for testing
$({ target: 'Object', stat: true, forced: Object.assign !== assign }, {
  assign: assign
});


/***/ }),

/***/ "cdf9":
/***/ (function(module, exports, __webpack_require__) {

var anObject = __webpack_require__("825a");
var isObject = __webpack_require__("861d");
var newPromiseCapability = __webpack_require__("f069");

module.exports = function (C, x) {
  anObject(C);
  if (isObject(x) && x.constructor === C) return x;
  var promiseCapability = newPromiseCapability.f(C);
  var resolve = promiseCapability.resolve;
  resolve(x);
  return promiseCapability.promise;
};


/***/ }),

/***/ "ce4e":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var createNonEnumerableProperty = __webpack_require__("9112");

module.exports = function (key, value) {
  try {
    createNonEnumerableProperty(global, key, value);
  } catch (error) {
    global[key] = value;
  } return value;
};


/***/ }),

/***/ "cfaa":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "d012":
/***/ (function(module, exports) {

module.exports = {};


/***/ }),

/***/ "d039":
/***/ (function(module, exports) {

module.exports = function (exec) {
  try {
    return !!exec();
  } catch (error) {
    return true;
  }
};


/***/ }),

/***/ "d066":
/***/ (function(module, exports, __webpack_require__) {

var path = __webpack_require__("428f");
var global = __webpack_require__("da84");

var aFunction = function (variable) {
  return typeof variable == 'function' ? variable : undefined;
};

module.exports = function (namespace, method) {
  return arguments.length < 2 ? aFunction(path[namespace]) || aFunction(global[namespace])
    : path[namespace] && path[namespace][method] || global[namespace] && global[namespace][method];
};


/***/ }),

/***/ "d1e7":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017-2019 Digital Bazaar, Inc. All rights reserved.
 */


const util = __webpack_require__("89ae");
const JsonLdError = __webpack_require__("5950");

const {
  isArray: _isArray,
  isObject: _isObject,
  isString: _isString,
  isUndefined: _isUndefined
} = __webpack_require__("68fa");

const {
  isAbsolute: _isAbsoluteIri,
  isRelative: _isRelativeIri,
  prependBase
} = __webpack_require__("ddd7");

const {
  asArray: _asArray,
  compareShortestLeast: _compareShortestLeast
} = __webpack_require__("89ae");

const INITIAL_CONTEXT_CACHE = new Map();
const INITIAL_CONTEXT_CACHE_MAX_SIZE = 10000;
const KEYWORD_PATTERN = /^@[a-zA-Z]+$/;

const api = {};
module.exports = api;

/**
 * Processes a local context and returns a new active context.
 *
 * @param activeCtx the current active context.
 * @param localCtx the local context to process.
 * @param options the context processing options.
 * @param propagate `true` if `false`, retains any previously defined term,
 *   which can be rolled back when the descending into a new node object.
 * @param overrideProtected `false` allows protected terms to be modified.
 *
 * @return a Promise that resolves to the new active context.
 */
api.process = async ({
  activeCtx, localCtx, options,
  propagate = true,
  overrideProtected = false,
  cycles = new Set()
}) => {
  // normalize local context to an array of @context objects
  if(_isObject(localCtx) && '@context' in localCtx &&
    _isArray(localCtx['@context'])) {
    localCtx = localCtx['@context'];
  }
  const ctxs = _asArray(localCtx);

  // no contexts in array, return current active context w/o changes
  if(ctxs.length === 0) {
    return activeCtx;
  }

  // resolve contexts
  const resolved = await options.contextResolver.resolve({
    activeCtx,
    context: localCtx,
    documentLoader: options.documentLoader,
    base: options.base
  });

  // override propagate if first resolved context has `@propagate`
  if(_isObject(resolved[0].document) &&
    typeof resolved[0].document['@propagate'] === 'boolean') {
    // retrieve early, error checking done later
    propagate = resolved[0].document['@propagate'];
  }

  // process each context in order, update active context
  // on each iteration to ensure proper caching
  let rval = activeCtx;

  // track the previous context
  // if not propagating, make sure rval has a previous context
  if(!propagate && !rval.previousContext) {
    // clone `rval` context before updating
    rval = rval.clone();
    rval.previousContext = activeCtx;
  }

  for(const resolvedContext of resolved) {
    let {document: ctx} = resolvedContext;

    // update active context to one computed from last iteration
    activeCtx = rval;

    // reset to initial context
    if(ctx === null) {
      // We can't nullify if there are protected terms and we're
      // not allowing overrides (e.g. processing a property term scoped context)
      if(!overrideProtected &&
        Object.keys(activeCtx.protected).length !== 0) {
        const protectedMode = (options && options.protectedMode) || 'error';
        if(protectedMode === 'error') {
          throw new JsonLdError(
            'Tried to nullify a context with protected terms outside of ' +
            'a term definition.',
            'jsonld.SyntaxError',
            {code: 'invalid context nullification'});
        } else if(protectedMode === 'warn') {
          // FIXME: remove logging and use a handler
          console.warn('WARNING: invalid context nullification');

          // get processed context from cache if available
          const processed = resolvedContext.getProcessed(activeCtx);
          if(processed) {
            rval = activeCtx = processed;
            continue;
          }

          const oldActiveCtx = activeCtx;
          // copy all protected term definitions to fresh initial context
          rval = activeCtx = api.getInitialContext(options).clone();
          for(const [term, _protected] of
            Object.entries(oldActiveCtx.protected)) {
            if(_protected) {
              activeCtx.mappings[term] =
                util.clone(oldActiveCtx.mappings[term]);
            }
          }
          activeCtx.protected = util.clone(oldActiveCtx.protected);

          // cache processed result
          resolvedContext.setProcessed(oldActiveCtx, rval);
          continue;
        }
        throw new JsonLdError(
          'Invalid protectedMode.',
          'jsonld.SyntaxError',
          {code: 'invalid protected mode', context: localCtx, protectedMode});
      }
      rval = activeCtx = api.getInitialContext(options).clone();
      continue;
    }

    // get processed context from cache if available
    const processed = resolvedContext.getProcessed(activeCtx);
    if(processed) {
      rval = activeCtx = processed;
      continue;
    }

    // dereference @context key if present
    if(_isObject(ctx) && '@context' in ctx) {
      ctx = ctx['@context'];
    }

    // context must be an object by now, all URLs retrieved before this call
    if(!_isObject(ctx)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context must be an object.',
        'jsonld.SyntaxError', {code: 'invalid local context', context: ctx});
    }

    // TODO: there is likely a `previousContext` cloning optimization that
    // could be applied here (no need to copy it under certain conditions)

    // clone context before updating it
    rval = rval.clone();

    // define context mappings for keys in local context
    const defined = new Map();

    // handle @version
    if('@version' in ctx) {
      if(ctx['@version'] !== 1.1) {
        throw new JsonLdError(
          'Unsupported JSON-LD version: ' + ctx['@version'],
          'jsonld.UnsupportedVersion',
          {code: 'invalid @version value', context: ctx});
      }
      if(activeCtx.processingMode &&
        activeCtx.processingMode === 'json-ld-1.0') {
        throw new JsonLdError(
          '@version: ' + ctx['@version'] + ' not compatible with ' +
          activeCtx.processingMode,
          'jsonld.ProcessingModeConflict',
          {code: 'processing mode conflict', context: ctx});
      }
      rval.processingMode = 'json-ld-1.1';
      rval['@version'] = ctx['@version'];
      defined.set('@version', true);
    }

    // if not set explicitly, set processingMode to "json-ld-1.1"
    rval.processingMode =
      rval.processingMode || activeCtx.processingMode;

    // handle @base
    if('@base' in ctx) {
      let base = ctx['@base'];

      if(base === null || _isAbsoluteIri(base)) {
        // no action
      } else if(_isRelativeIri(base)) {
        base = prependBase(rval['@base'], base);
      } else {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; the value of "@base" in a ' +
          '@context must be an absolute IRI, a relative IRI, or null.',
          'jsonld.SyntaxError', {code: 'invalid base IRI', context: ctx});
      }

      rval['@base'] = base;
      defined.set('@base', true);
    }

    // handle @vocab
    if('@vocab' in ctx) {
      const value = ctx['@vocab'];
      if(value === null) {
        delete rval['@vocab'];
      } else if(!_isString(value)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; the value of "@vocab" in a ' +
          '@context must be a string or null.',
          'jsonld.SyntaxError', {code: 'invalid vocab mapping', context: ctx});
      } else if(!_isAbsoluteIri(value) && api.processingMode(rval, 1.0)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; the value of "@vocab" in a ' +
          '@context must be an absolute IRI.',
          'jsonld.SyntaxError', {code: 'invalid vocab mapping', context: ctx});
      } else {
        rval['@vocab'] = _expandIri(rval, value, {vocab: true, base: true},
          undefined, undefined, options);
      }
      defined.set('@vocab', true);
    }

    // handle @language
    if('@language' in ctx) {
      const value = ctx['@language'];
      if(value === null) {
        delete rval['@language'];
      } else if(!_isString(value)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; the value of "@language" in a ' +
          '@context must be a string or null.',
          'jsonld.SyntaxError',
          {code: 'invalid default language', context: ctx});
      } else {
        rval['@language'] = value.toLowerCase();
      }
      defined.set('@language', true);
    }

    // handle @direction
    if('@direction' in ctx) {
      const value = ctx['@direction'];
      if(activeCtx.processingMode === 'json-ld-1.0') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @direction not compatible with ' +
          activeCtx.processingMode,
          'jsonld.SyntaxError',
          {code: 'invalid context member', context: ctx});
      }
      if(value === null) {
        delete rval['@direction'];
      } else if(value !== 'ltr' && value !== 'rtl') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; the value of "@direction" in a ' +
          '@context must be null, "ltr", or "rtl".',
          'jsonld.SyntaxError',
          {code: 'invalid base direction', context: ctx});
      } else {
        rval['@direction'] = value;
      }
      defined.set('@direction', true);
    }

    // handle @propagate
    // note: we've already extracted it, here we just do error checking
    if('@propagate' in ctx) {
      const value = ctx['@propagate'];
      if(activeCtx.processingMode === 'json-ld-1.0') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @propagate not compatible with ' +
          activeCtx.processingMode,
          'jsonld.SyntaxError',
          {code: 'invalid context entry', context: ctx});
      }
      if(typeof value !== 'boolean') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @propagate value must be a boolean.',
          'jsonld.SyntaxError',
          {code: 'invalid @propagate value', context: localCtx});
      }
      defined.set('@propagate', true);
    }

    // handle @import
    if('@import' in ctx) {
      const value = ctx['@import'];
      if(activeCtx.processingMode === 'json-ld-1.0') {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @import not compatible with ' +
          activeCtx.processingMode,
          'jsonld.SyntaxError',
          {code: 'invalid context entry', context: ctx});
      }
      if(!_isString(value)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @import must be a string.',
          'jsonld.SyntaxError',
          {code: 'invalid @import value', context: localCtx});
      }

      // resolve contexts
      const resolvedImport = await options.contextResolver.resolve({
        activeCtx,
        context: value,
        documentLoader: options.documentLoader,
        base: options.base
      });
      if(resolvedImport.length !== 1) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @import must reference a single context.',
          'jsonld.SyntaxError',
          {code: 'invalid remote context', context: localCtx});
      }
      const processedImport = resolvedImport[0].getProcessed(activeCtx);
      if(processedImport) {
        // Note: if the same context were used in this active context
        // as a reference context, then processed_input might not
        // be a dict.
        ctx = processedImport;
      } else {
        const importCtx = resolvedImport[0].document;
        if('@import' in importCtx) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax: ' +
            'imported context must not include @import.',
            'jsonld.SyntaxError',
            {code: 'invalid context entry', context: localCtx});
        }

        // merge ctx into importCtx and replace rval with the result
        for(const key in importCtx) {
          if(!ctx.hasOwnProperty(key)) {
            ctx[key] = importCtx[key];
          }
        }

        // Note: this could potenially conflict if the import
        // were used in the same active context as a referenced
        // context and an import. In this case, we
        // could override the cached result, but seems unlikely.
        resolvedImport[0].setProcessed(activeCtx, ctx);
      }

      defined.set('@import', true);
    }

    // handle @protected; determine whether this sub-context is declaring
    // all its terms to be "protected" (exceptions can be made on a
    // per-definition basis)
    defined.set('@protected', ctx['@protected'] || false);

    // process all other keys
    for(const key in ctx) {
      api.createTermDefinition({
        activeCtx: rval,
        localCtx: ctx,
        term: key,
        defined,
        options,
        overrideProtected
      });

      if(_isObject(ctx[key]) && '@context' in ctx[key]) {
        const keyCtx = ctx[key]['@context'];
        let process = true;
        if(_isString(keyCtx)) {
          const url = prependBase(options.base, keyCtx);
          // track processed contexts to avoid scoped context recursion
          if(cycles.has(url)) {
            process = false;
          } else {
            cycles.add(url);
          }
        }
        // parse context to validate
        if(process) {
          try {
            await api.process({
              activeCtx: rval.clone(),
              localCtx: ctx[key]['@context'],
              overrideProtected: true,
              options,
              cycles
            });
          } catch(e) {
            throw new JsonLdError(
              'Invalid JSON-LD syntax; invalid scoped context.',
              'jsonld.SyntaxError',
              {
                code: 'invalid scoped context',
                context: ctx[key]['@context'],
                term: key
              });
          }
        }
      }
    }

    // cache processed result
    resolvedContext.setProcessed(activeCtx, rval);
  }

  return rval;
};

/**
 * Creates a term definition during context processing.
 *
 * @param activeCtx the current active context.
 * @param localCtx the local context being processed.
 * @param term the term in the local context to define the mapping for.
 * @param defined a map of defining/defined keys to detect cycles and prevent
 *          double definitions.
 * @param {Object} [options] - creation options.
 * @param {string} [options.protectedMode="error"] - "error" to throw error
 *   on `@protected` constraint violation, "warn" to allow violations and
 *   signal a warning.
 * @param overrideProtected `false` allows protected terms to be modified.
 */
api.createTermDefinition = ({
  activeCtx,
  localCtx,
  term,
  defined,
  options,
  overrideProtected = false,
}) => {
  if(defined.has(term)) {
    // term already defined
    if(defined.get(term)) {
      return;
    }
    // cycle detected
    throw new JsonLdError(
      'Cyclical context definition detected.',
      'jsonld.CyclicalContext',
      {code: 'cyclic IRI mapping', context: localCtx, term});
  }

  // now defining term
  defined.set(term, false);

  // get context term value
  let value;
  if(localCtx.hasOwnProperty(term)) {
    value = localCtx[term];
  }

  if(term === '@type' &&
     _isObject(value) &&
     (value['@container'] || '@set') === '@set' &&
     api.processingMode(activeCtx, 1.1)) {

    const validKeys = ['@container', '@id', '@protected'];
    const keys = Object.keys(value);
    if(keys.length === 0 || keys.some(k => !validKeys.includes(k))) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; keywords cannot be overridden.',
        'jsonld.SyntaxError',
        {code: 'keyword redefinition', context: localCtx, term});
    }
  } else if(api.isKeyword(term)) {
    throw new JsonLdError(
      'Invalid JSON-LD syntax; keywords cannot be overridden.',
      'jsonld.SyntaxError',
      {code: 'keyword redefinition', context: localCtx, term});
  } else if(term.match(KEYWORD_PATTERN)) {
    // FIXME: remove logging and use a handler
    console.warn('WARNING: terms beginning with "@" are reserved' +
      ' for future use and ignored', {term});
    return;
  } else if(term === '') {
    throw new JsonLdError(
      'Invalid JSON-LD syntax; a term cannot be an empty string.',
      'jsonld.SyntaxError',
      {code: 'invalid term definition', context: localCtx});
  }

  // keep reference to previous mapping for potential `@protected` check
  const previousMapping = activeCtx.mappings.get(term);

  // remove old mapping
  if(activeCtx.mappings.has(term)) {
    activeCtx.mappings.delete(term);
  }

  // convert short-hand value to object w/@id
  let simpleTerm = false;
  if(_isString(value) || value === null) {
    simpleTerm = true;
    value = {'@id': value};
  }

  if(!_isObject(value)) {
    throw new JsonLdError(
      'Invalid JSON-LD syntax; @context term values must be ' +
      'strings or objects.',
      'jsonld.SyntaxError',
      {code: 'invalid term definition', context: localCtx});
  }

  // create new mapping
  const mapping = {};
  activeCtx.mappings.set(term, mapping);
  mapping.reverse = false;

  // make sure term definition only has expected keywords
  const validKeys = ['@container', '@id', '@language', '@reverse', '@type'];

  // JSON-LD 1.1 support
  if(api.processingMode(activeCtx, 1.1)) {
    validKeys.push(
      '@context', '@direction', '@index', '@nest', '@prefix', '@protected');
  }

  for(const kw in value) {
    if(!validKeys.includes(kw)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a term definition must not contain ' + kw,
        'jsonld.SyntaxError',
        {code: 'invalid term definition', context: localCtx});
    }
  }

  // always compute whether term has a colon as an optimization for
  // _compactIri
  const colon = term.indexOf(':');
  mapping._termHasColon = (colon > 0);

  if('@reverse' in value) {
    if('@id' in value) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a @reverse term definition must not ' +
        'contain @id.', 'jsonld.SyntaxError',
        {code: 'invalid reverse property', context: localCtx});
    }
    if('@nest' in value) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a @reverse term definition must not ' +
        'contain @nest.', 'jsonld.SyntaxError',
        {code: 'invalid reverse property', context: localCtx});
    }
    const reverse = value['@reverse'];
    if(!_isString(reverse)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a @context @reverse value must be a string.',
        'jsonld.SyntaxError', {code: 'invalid IRI mapping', context: localCtx});
    }

    if(!api.isKeyword(reverse) && reverse.match(KEYWORD_PATTERN)) {
      // FIXME: remove logging and use a handler
      console.warn('WARNING: values beginning with "@" are reserved' +
        ' for future use and ignored', {reverse});
      if(previousMapping) {
        activeCtx.mappings.set(term, previousMapping);
      } else {
        activeCtx.mappings.delete(term);
      }
      return;
    }

    // expand and add @id mapping
    const id = _expandIri(
      activeCtx, reverse, {vocab: true, base: false}, localCtx, defined,
      options);
    if(!_isAbsoluteIri(id)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a @context @reverse value must be an ' +
        'absolute IRI or a blank node identifier.',
        'jsonld.SyntaxError', {code: 'invalid IRI mapping', context: localCtx});
    }

    mapping['@id'] = id;
    mapping.reverse = true;
  } else if('@id' in value) {
    let id = value['@id'];
    if(id && !_isString(id)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; a @context @id value must be an array ' +
        'of strings or a string.',
        'jsonld.SyntaxError', {code: 'invalid IRI mapping', context: localCtx});
    }
    if(id === null) {
      // reserve a null term, which may be protected
      mapping['@id'] = null;
    } else if(!api.isKeyword(id) && id.match(KEYWORD_PATTERN)) {
      // FIXME: remove logging and use a handler
      console.warn('WARNING: values beginning with "@" are reserved' +
        ' for future use and ignored', {id});
      if(previousMapping) {
        activeCtx.mappings.set(term, previousMapping);
      } else {
        activeCtx.mappings.delete(term);
      }
      return;
    } else if(id !== term) {
      // expand and add @id mapping
      id = _expandIri(
        activeCtx, id, {vocab: true, base: false}, localCtx, defined, options);
      if(!_isAbsoluteIri(id) && !api.isKeyword(id)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; a @context @id value must be an ' +
          'absolute IRI, a blank node identifier, or a keyword.',
          'jsonld.SyntaxError',
          {code: 'invalid IRI mapping', context: localCtx});
      }

      // if term has the form of an IRI it must map the same
      if(term.match(/(?::[^:])|\//)) {
        const termDefined = new Map(defined).set(term, true);
        const termIri = _expandIri(
          activeCtx, term, {vocab: true, base: false},
          localCtx, termDefined, options);
        if(termIri !== id) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; term in form of IRI must ' +
            'expand to definition.',
            'jsonld.SyntaxError',
            {code: 'invalid IRI mapping', context: localCtx});
        }
      }

      mapping['@id'] = id;
      // indicate if this term may be used as a compact IRI prefix
      mapping._prefix = (simpleTerm &&
        !mapping._termHasColon &&
        id.match(/[:\/\?#\[\]@]$/));
    }
  }

  if(!('@id' in mapping)) {
    // see if the term has a prefix
    if(mapping._termHasColon) {
      const prefix = term.substr(0, colon);
      if(localCtx.hasOwnProperty(prefix)) {
        // define parent prefix
        api.createTermDefinition({
          activeCtx, localCtx, term: prefix, defined, options
        });
      }

      if(activeCtx.mappings.has(prefix)) {
        // set @id based on prefix parent
        const suffix = term.substr(colon + 1);
        mapping['@id'] = activeCtx.mappings.get(prefix)['@id'] + suffix;
      } else {
        // term is an absolute IRI
        mapping['@id'] = term;
      }
    } else if(term === '@type') {
      // Special case, were we've previously determined that container is @set
      mapping['@id'] = term;
    } else {
      // non-IRIs *must* define @ids if @vocab is not available
      if(!('@vocab' in activeCtx)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; @context terms must define an @id.',
          'jsonld.SyntaxError',
          {code: 'invalid IRI mapping', context: localCtx, term});
      }
      // prepend vocab to term
      mapping['@id'] = activeCtx['@vocab'] + term;
    }
  }

  // Handle term protection
  if(value['@protected'] === true ||
    (defined.get('@protected') === true && value['@protected'] !== false)) {
    activeCtx.protected[term] = true;
    mapping.protected = true;
  }

  // IRI mapping now defined
  defined.set(term, true);

  if('@type' in value) {
    let type = value['@type'];
    if(!_isString(type)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; an @context @type value must be a string.',
        'jsonld.SyntaxError',
        {code: 'invalid type mapping', context: localCtx});
    }

    if((type === '@json' || type === '@none')) {
      if(api.processingMode(activeCtx, 1.0)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; an @context @type value must not be ' +
          `"${type}" in JSON-LD 1.0 mode.`,
          'jsonld.SyntaxError',
          {code: 'invalid type mapping', context: localCtx});
      }
    } else if(type !== '@id' && type !== '@vocab') {
      // expand @type to full IRI
      type = _expandIri(
        activeCtx, type, {vocab: true, base: false}, localCtx, defined,
        options);
      if(!_isAbsoluteIri(type)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; an @context @type value must be an ' +
          'absolute IRI.',
          'jsonld.SyntaxError',
          {code: 'invalid type mapping', context: localCtx});
      }
      if(type.indexOf('_:') === 0) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; an @context @type value must be an IRI, ' +
          'not a blank node identifier.',
          'jsonld.SyntaxError',
          {code: 'invalid type mapping', context: localCtx});
      }
    }

    // add @type to mapping
    mapping['@type'] = type;
  }

  if('@container' in value) {
    // normalize container to an array form
    const container = _isString(value['@container']) ?
      [value['@container']] : (value['@container'] || []);
    const validContainers = ['@list', '@set', '@index', '@language'];
    let isValid = true;
    const hasSet = container.includes('@set');

    // JSON-LD 1.1 support
    if(api.processingMode(activeCtx, 1.1)) {
      validContainers.push('@graph', '@id', '@type');

      // check container length
      if(container.includes('@list')) {
        if(container.length !== 1) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; @context @container with @list must ' +
            'have no other values',
            'jsonld.SyntaxError',
            {code: 'invalid container mapping', context: localCtx});
        }
      } else if(container.includes('@graph')) {
        if(container.some(key =>
          key !== '@graph' && key !== '@id' && key !== '@index' &&
          key !== '@set')) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; @context @container with @graph must ' +
            'have no other values other than @id, @index, and @set',
            'jsonld.SyntaxError',
            {code: 'invalid container mapping', context: localCtx});
        }
      } else {
        // otherwise, container may also include @set
        isValid &= container.length <= (hasSet ? 2 : 1);
      }

      if(container.includes('@type')) {
        // If mapping does not have an @type,
        // set it to @id
        mapping['@type'] = mapping['@type'] || '@id';

        // type mapping must be either @id or @vocab
        if(!['@id', '@vocab'].includes(mapping['@type'])) {
          throw new JsonLdError(
            'Invalid JSON-LD syntax; container: @type requires @type to be ' +
            '@id or @vocab.',
            'jsonld.SyntaxError',
            {code: 'invalid type mapping', context: localCtx});
        }
      }
    } else {
      // in JSON-LD 1.0, container must not be an array (it must be a string,
      // which is one of the validContainers)
      isValid &= !_isArray(value['@container']);

      // check container length
      isValid &= container.length <= 1;
    }

    // check against valid containers
    isValid &= container.every(c => validContainers.includes(c));

    // @set not allowed with @list
    isValid &= !(hasSet && container.includes('@list'));

    if(!isValid) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context @container value must be ' +
        'one of the following: ' + validContainers.join(', '),
        'jsonld.SyntaxError',
        {code: 'invalid container mapping', context: localCtx});
    }

    if(mapping.reverse &&
      !container.every(c => ['@index', '@set'].includes(c))) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context @container value for a @reverse ' +
        'type definition must be @index or @set.', 'jsonld.SyntaxError',
        {code: 'invalid reverse property', context: localCtx});
    }

    // add @container to mapping
    mapping['@container'] = container;
  }

  // property indexing
  if('@index' in value) {
    if(!('@container' in value) || !mapping['@container'].includes('@index')) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @index without @index in @container: ' +
        `"${value['@index']}" on term "${term}".`, 'jsonld.SyntaxError',
        {code: 'invalid term definition', context: localCtx});
    }
    if(!_isString(value['@index']) || value['@index'].indexOf('@') === 0) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @index must expand to an IRI: ' +
        `"${value['@index']}" on term "${term}".`, 'jsonld.SyntaxError',
        {code: 'invalid term definition', context: localCtx});
    }
    mapping['@index'] = value['@index'];
  }

  // scoped contexts
  if('@context' in value) {
    mapping['@context'] = value['@context'];
  }

  if('@language' in value && !('@type' in value)) {
    let language = value['@language'];
    if(language !== null && !_isString(language)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context @language value must be ' +
        'a string or null.', 'jsonld.SyntaxError',
        {code: 'invalid language mapping', context: localCtx});
    }

    // add @language to mapping
    if(language !== null) {
      language = language.toLowerCase();
    }
    mapping['@language'] = language;
  }

  // term may be used as a prefix
  if('@prefix' in value) {
    if(term.match(/:|\//)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context @prefix used on a compact IRI term',
        'jsonld.SyntaxError',
        {code: 'invalid term definition', context: localCtx});
    }
    if(api.isKeyword(mapping['@id'])) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; keywords may not be used as prefixes',
        'jsonld.SyntaxError',
        {code: 'invalid term definition', context: localCtx});
    }
    if(typeof value['@prefix'] === 'boolean') {
      mapping._prefix = value['@prefix'] === true;
    } else {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context value for @prefix must be boolean',
        'jsonld.SyntaxError',
        {code: 'invalid @prefix value', context: localCtx});
    }
  }

  if('@direction' in value) {
    const direction = value['@direction'];
    if(direction !== null && direction !== 'ltr' && direction !== 'rtl') {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @direction value must be ' +
        'null, "ltr", or "rtl".',
        'jsonld.SyntaxError',
        {code: 'invalid base direction', context: localCtx});
    }
    mapping['@direction'] = direction;
  }

  if('@nest' in value) {
    const nest = value['@nest'];
    if(!_isString(nest) || (nest !== '@nest' && nest.indexOf('@') === 0)) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; @context @nest value must be ' +
        'a string which is not a keyword other than @nest.',
        'jsonld.SyntaxError',
        {code: 'invalid @nest value', context: localCtx});
    }
    mapping['@nest'] = nest;
  }

  // disallow aliasing @context and @preserve
  const id = mapping['@id'];
  if(id === '@context' || id === '@preserve') {
    throw new JsonLdError(
      'Invalid JSON-LD syntax; @context and @preserve cannot be aliased.',
      'jsonld.SyntaxError', {code: 'invalid keyword alias', context: localCtx});
  }

  // Check for overriding protected terms
  if(previousMapping && previousMapping.protected && !overrideProtected) {
    // force new term to continue to be protected and see if the mappings would
    // be equal
    activeCtx.protected[term] = true;
    mapping.protected = true;
    if(!_deepCompare(previousMapping, mapping)) {
      const protectedMode = (options && options.protectedMode) || 'error';
      if(protectedMode === 'error') {
        throw new JsonLdError(
          `Invalid JSON-LD syntax; tried to redefine "${term}" which is a ` +
          'protected term.',
          'jsonld.SyntaxError',
          {code: 'protected term redefinition', context: localCtx, term});
      } else if(protectedMode === 'warn') {
        // FIXME: remove logging and use a handler
        console.warn('WARNING: protected term redefinition', {term});
        return;
      }
      throw new JsonLdError(
        'Invalid protectedMode.',
        'jsonld.SyntaxError',
        {code: 'invalid protected mode', context: localCtx, term,
          protectedMode});
    }
  }
};

/**
 * Expands a string to a full IRI. The string may be a term, a prefix, a
 * relative IRI, or an absolute IRI. The associated absolute IRI will be
 * returned.
 *
 * @param activeCtx the current active context.
 * @param value the string to expand.
 * @param relativeTo options for how to resolve relative IRIs:
 *          base: true to resolve against the base IRI, false not to.
 *          vocab: true to concatenate after @vocab, false not to.
 * @param {Object} [options] - processing options.
 *
 * @return the expanded value.
 */
api.expandIri = (activeCtx, value, relativeTo, options) => {
  return _expandIri(activeCtx, value, relativeTo, undefined, undefined,
    options);
};

/**
 * Expands a string to a full IRI. The string may be a term, a prefix, a
 * relative IRI, or an absolute IRI. The associated absolute IRI will be
 * returned.
 *
 * @param activeCtx the current active context.
 * @param value the string to expand.
 * @param relativeTo options for how to resolve relative IRIs:
 *          base: true to resolve against the base IRI, false not to.
 *          vocab: true to concatenate after @vocab, false not to.
 * @param localCtx the local context being processed (only given if called
 *          during context processing).
 * @param defined a map for tracking cycles in context definitions (only given
 *          if called during context processing).
 * @param {Object} [options] - processing options.
 *
 * @return the expanded value.
 */
function _expandIri(activeCtx, value, relativeTo, localCtx, defined, options) {
  // already expanded
  if(value === null || !_isString(value) || api.isKeyword(value)) {
    return value;
  }

  // ignore non-keyword things that look like a keyword
  if(value.match(KEYWORD_PATTERN)) {
    return null;
  }

  // define term dependency if not defined
  if(localCtx && localCtx.hasOwnProperty(value) &&
    defined.get(value) !== true) {
    api.createTermDefinition({
      activeCtx, localCtx, term: value, defined, options
    });
  }

  relativeTo = relativeTo || {};
  if(relativeTo.vocab) {
    const mapping = activeCtx.mappings.get(value);

    // value is explicitly ignored with a null mapping
    if(mapping === null) {
      return null;
    }

    if(_isObject(mapping) && '@id' in mapping) {
      // value is a term
      return mapping['@id'];
    }
  }

  // split value into prefix:suffix
  const colon = value.indexOf(':');
  if(colon > 0) {
    const prefix = value.substr(0, colon);
    const suffix = value.substr(colon + 1);

    // do not expand blank nodes (prefix of '_') or already-absolute
    // IRIs (suffix of '//')
    if(prefix === '_' || suffix.indexOf('//') === 0) {
      return value;
    }

    // prefix dependency not defined, define it
    if(localCtx && localCtx.hasOwnProperty(prefix)) {
      api.createTermDefinition({
        activeCtx, localCtx, term: prefix, defined, options
      });
    }

    // use mapping if prefix is defined
    const mapping = activeCtx.mappings.get(prefix);
    if(mapping && mapping._prefix) {
      return mapping['@id'] + suffix;
    }

    // already absolute IRI
    if(_isAbsoluteIri(value)) {
      return value;
    }
  }

  // prepend vocab
  if(relativeTo.vocab && '@vocab' in activeCtx) {
    return activeCtx['@vocab'] + value;
  }

  // prepend base
  if(relativeTo.base && '@base' in activeCtx) {
    if(activeCtx['@base']) {
      // The null case preserves value as potentially relative
      return prependBase(prependBase(options.base, activeCtx['@base']), value);
    }
  } else if(relativeTo.base) {
    return prependBase(options.base, value);
  }

  return value;
}

/**
 * Gets the initial context.
 *
 * @param options the options to use:
 *          [base] the document base IRI.
 *
 * @return the initial context.
 */
api.getInitialContext = options => {
  const key = JSON.stringify({processingMode: options.processingMode});
  const cached = INITIAL_CONTEXT_CACHE.get(key);
  if(cached) {
    return cached;
  }

  const initialContext = {
    processingMode: options.processingMode,
    mappings: new Map(),
    inverse: null,
    getInverse: _createInverseContext,
    clone: _cloneActiveContext,
    revertToPreviousContext: _revertToPreviousContext,
    protected: {}
  };
  // TODO: consider using LRU cache instead
  if(INITIAL_CONTEXT_CACHE.size === INITIAL_CONTEXT_CACHE_MAX_SIZE) {
    // clear whole cache -- assumes scenario where the cache fills means
    // the cache isn't being used very efficiently anyway
    INITIAL_CONTEXT_CACHE.clear();
  }
  INITIAL_CONTEXT_CACHE.set(key, initialContext);
  return initialContext;

  /**
   * Generates an inverse context for use in the compaction algorithm, if
   * not already generated for the given active context.
   *
   * @return the inverse context.
   */
  function _createInverseContext() {
    const activeCtx = this;

    // lazily create inverse
    if(activeCtx.inverse) {
      return activeCtx.inverse;
    }
    const inverse = activeCtx.inverse = {};

    // variables for building fast CURIE map
    const fastCurieMap = activeCtx.fastCurieMap = {};
    const irisToTerms = {};

    // handle default language
    const defaultLanguage = (activeCtx['@language'] || '@none').toLowerCase();

    // handle default direction
    const defaultDirection = activeCtx['@direction'];

    // create term selections for each mapping in the context, ordered by
    // shortest and then lexicographically least
    const mappings = activeCtx.mappings;
    const terms = [...mappings.keys()].sort(_compareShortestLeast);
    for(const term of terms) {
      const mapping = mappings.get(term);
      if(mapping === null) {
        continue;
      }

      let container = mapping['@container'] || '@none';
      container = [].concat(container).sort().join('');

      if(mapping['@id'] === null) {
        continue;
      }
      // iterate over every IRI in the mapping
      const ids = _asArray(mapping['@id']);
      for(const iri of ids) {
        let entry = inverse[iri];
        const isKeyword = api.isKeyword(iri);

        if(!entry) {
          // initialize entry
          inverse[iri] = entry = {};

          if(!isKeyword && !mapping._termHasColon) {
            // init IRI to term map and fast CURIE prefixes
            irisToTerms[iri] = [term];
            const fastCurieEntry = {iri, terms: irisToTerms[iri]};
            if(iri[0] in fastCurieMap) {
              fastCurieMap[iri[0]].push(fastCurieEntry);
            } else {
              fastCurieMap[iri[0]] = [fastCurieEntry];
            }
          }
        } else if(!isKeyword && !mapping._termHasColon) {
          // add IRI to term match
          irisToTerms[iri].push(term);
        }

        // add new entry
        if(!entry[container]) {
          entry[container] = {
            '@language': {},
            '@type': {},
            '@any': {}
          };
        }
        entry = entry[container];
        _addPreferredTerm(term, entry['@any'], '@none');

        if(mapping.reverse) {
          // term is preferred for values using @reverse
          _addPreferredTerm(term, entry['@type'], '@reverse');
        } else if(mapping['@type'] === '@none') {
          _addPreferredTerm(term, entry['@any'], '@none');
          _addPreferredTerm(term, entry['@language'], '@none');
          _addPreferredTerm(term, entry['@type'], '@none');
        } else if('@type' in mapping) {
          // term is preferred for values using specific type
          _addPreferredTerm(term, entry['@type'], mapping['@type']);
        } else if('@language' in mapping && '@direction' in mapping) {
          // term is preferred for values using specific language and direction
          const language = mapping['@language'];
          const direction = mapping['@direction'];
          if(language && direction) {
            _addPreferredTerm(term, entry['@language'],
              `${language}_${direction}`.toLowerCase());
          } else if(language) {
            _addPreferredTerm(term, entry['@language'], language.toLowerCase());
          } else if(direction) {
            _addPreferredTerm(term, entry['@language'], `_${direction}`);
          } else {
            _addPreferredTerm(term, entry['@language'], '@null');
          }
        } else if('@language' in mapping) {
          _addPreferredTerm(term, entry['@language'],
            (mapping['@language'] || '@null').toLowerCase());
        } else if('@direction' in mapping) {
          if(mapping['@direction']) {
            _addPreferredTerm(term, entry['@language'],
              `_${mapping['@direction']}`);
          } else {
            _addPreferredTerm(term, entry['@language'], '@none');
          }
        } else if(defaultDirection) {
          _addPreferredTerm(term, entry['@language'], `_${defaultDirection}`);
          _addPreferredTerm(term, entry['@language'], '@none');
          _addPreferredTerm(term, entry['@type'], '@none');
        } else {
          // add entries for no type and no language
          _addPreferredTerm(term, entry['@language'], defaultLanguage);
          _addPreferredTerm(term, entry['@language'], '@none');
          _addPreferredTerm(term, entry['@type'], '@none');
        }
      }
    }

    // build fast CURIE map
    for(const key in fastCurieMap) {
      _buildIriMap(fastCurieMap, key, 1);
    }

    return inverse;
  }

  /**
   * Runs a recursive algorithm to build a lookup map for quickly finding
   * potential CURIEs.
   *
   * @param iriMap the map to build.
   * @param key the current key in the map to work on.
   * @param idx the index into the IRI to compare.
   */
  function _buildIriMap(iriMap, key, idx) {
    const entries = iriMap[key];
    const next = iriMap[key] = {};

    let iri;
    let letter;
    for(const entry of entries) {
      iri = entry.iri;
      if(idx >= iri.length) {
        letter = '';
      } else {
        letter = iri[idx];
      }
      if(letter in next) {
        next[letter].push(entry);
      } else {
        next[letter] = [entry];
      }
    }

    for(const key in next) {
      if(key === '') {
        continue;
      }
      _buildIriMap(next, key, idx + 1);
    }
  }

  /**
   * Adds the term for the given entry if not already added.
   *
   * @param term the term to add.
   * @param entry the inverse context typeOrLanguage entry to add to.
   * @param typeOrLanguageValue the key in the entry to add to.
   */
  function _addPreferredTerm(term, entry, typeOrLanguageValue) {
    if(!entry.hasOwnProperty(typeOrLanguageValue)) {
      entry[typeOrLanguageValue] = term;
    }
  }

  /**
   * Clones an active context, creating a child active context.
   *
   * @return a clone (child) of the active context.
   */
  function _cloneActiveContext() {
    const child = {};
    child.mappings = util.clone(this.mappings);
    child.clone = this.clone;
    child.inverse = null;
    child.getInverse = this.getInverse;
    child.protected = util.clone(this.protected);
    if(this.previousContext) {
      child.previousContext = this.previousContext.clone();
    }
    child.revertToPreviousContext = this.revertToPreviousContext;
    if('@base' in this) {
      child['@base'] = this['@base'];
    }
    if('@language' in this) {
      child['@language'] = this['@language'];
    }
    if('@vocab' in this) {
      child['@vocab'] = this['@vocab'];
    }
    return child;
  }

  /**
   * Reverts any type-scoped context in this active context to the previous
   * context.
   */
  function _revertToPreviousContext() {
    if(!this.previousContext) {
      return this;
    }
    return this.previousContext.clone();
  }
};

/**
 * Gets the value for the given active context key and type, null if none is
 * set or undefined if none is set and type is '@context'.
 *
 * @param ctx the active context.
 * @param key the context key.
 * @param [type] the type of value to get (eg: '@id', '@type'), if not
 *          specified gets the entire entry for a key, null if not found.
 *
 * @return the value, null, or undefined.
 */
api.getContextValue = (ctx, key, type) => {
  // invalid key
  if(key === null) {
    if(type === '@context') {
      return undefined;
    }
    return null;
  }

  // get specific entry information
  if(ctx.mappings.has(key)) {
    const entry = ctx.mappings.get(key);

    if(_isUndefined(type)) {
      // return whole entry
      return entry;
    }
    if(entry.hasOwnProperty(type)) {
      // return entry value for type
      return entry[type];
    }
  }

  // get default language
  if(type === '@language' && type in ctx) {
    return ctx[type];
  }

  // get default direction
  if(type === '@direction' && type in ctx) {
    return ctx[type];
  }

  if(type === '@context') {
    return undefined;
  }
  return null;
};

/**
 * Processing Mode check.
 *
 * @param activeCtx the current active context.
 * @param version the string or numeric version to check.
 *
 * @return boolean.
 */
api.processingMode = (activeCtx, version) => {
  if(version.toString() >= '1.1') {
    return !activeCtx.processingMode ||
      activeCtx.processingMode >= 'json-ld-' + version.toString();
  } else {
    return activeCtx.processingMode === 'json-ld-1.0';
  }
};

/**
 * Returns whether or not the given value is a keyword.
 *
 * @param v the value to check.
 *
 * @return true if the value is a keyword, false if not.
 */
api.isKeyword = v => {
  if(!_isString(v) || v[0] !== '@') {
    return false;
  }
  switch(v) {
    case '@base':
    case '@container':
    case '@context':
    case '@default':
    case '@direction':
    case '@embed':
    case '@explicit':
    case '@graph':
    case '@id':
    case '@included':
    case '@index':
    case '@json':
    case '@language':
    case '@list':
    case '@nest':
    case '@none':
    case '@omitDefault':
    case '@prefix':
    case '@preserve':
    case '@protected':
    case '@requireAll':
    case '@reverse':
    case '@set':
    case '@type':
    case '@value':
    case '@version':
    case '@vocab':
      return true;
  }
  return false;
};

function _deepCompare(x1, x2) {
  // compare `null` or primitive types directly
  if((!(x1 && typeof x1 === 'object')) ||
     (!(x2 && typeof x2 === 'object'))) {
    return x1 === x2;
  }
  // x1 and x2 are objects (also potentially arrays)
  const x1Array = Array.isArray(x1);
  if(x1Array !== Array.isArray(x2)) {
    return false;
  }
  if(x1Array) {
    if(x1.length !== x2.length) {
      return false;
    }
    for(let i = 0; i < x1.length; ++i) {
      if(!_deepCompare(x1[i], x2[i])) {
        return false;
      }
    }
    return true;
  }
  // x1 and x2 are non-array objects
  const k1s = Object.keys(x1);
  const k2s = Object.keys(x2);
  if(k1s.length !== k2s.length) {
    return false;
  }
  for(const k1 in x1) {
    let v1 = x1[k1];
    let v2 = x2[k1];
    // special case: `@container` can be in any order
    if(k1 === '@container') {
      if(Array.isArray(v1) && Array.isArray(v2)) {
        v1 = v1.slice().sort();
        v2 = v2.slice().sort();
      }
    }
    if(!_deepCompare(v1, v2)) {
      return false;
    }
  }
  return true;
}


/***/ }),

/***/ "d1e79":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $propertyIsEnumerable = {}.propertyIsEnumerable;
// eslint-disable-next-line es/no-object-getownpropertydescriptor -- safe
var getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;

// Nashorn ~ JDK8 bug
var NASHORN_BUG = getOwnPropertyDescriptor && !$propertyIsEnumerable.call({ 1: 2 }, 1);

// `Object.prototype.propertyIsEnumerable` method implementation
// https://tc39.es/ecma262/#sec-object.prototype.propertyisenumerable
exports.f = NASHORN_BUG ? function propertyIsEnumerable(V) {
  var descriptor = getOwnPropertyDescriptor(this, V);
  return !!descriptor && descriptor.enumerable;
} : $propertyIsEnumerable;


/***/ }),

/***/ "d2bb":
/***/ (function(module, exports, __webpack_require__) {

/* eslint-disable no-proto -- safe */
var anObject = __webpack_require__("825a");
var aPossiblePrototype = __webpack_require__("3bbe");

// `Object.setPrototypeOf` method
// https://tc39.es/ecma262/#sec-object.setprototypeof
// Works with __proto__ only. Old v8 can't work with null proto objects.
// eslint-disable-next-line es/no-object-setprototypeof -- safe
module.exports = Object.setPrototypeOf || ('__proto__' in {} ? function () {
  var CORRECT_SETTER = false;
  var test = {};
  var setter;
  try {
    // eslint-disable-next-line es/no-object-getownpropertydescriptor -- safe
    setter = Object.getOwnPropertyDescriptor(Object.prototype, '__proto__').set;
    setter.call(test, []);
    CORRECT_SETTER = test instanceof Array;
  } catch (error) { /* empty */ }
  return function setPrototypeOf(O, proto) {
    anObject(O);
    aPossiblePrototype(proto);
    if (CORRECT_SETTER) setter.call(O, proto);
    else O.__proto__ = proto;
    return O;
  };
}() : undefined);


/***/ }),

/***/ "d3b7":
/***/ (function(module, exports, __webpack_require__) {

var TO_STRING_TAG_SUPPORT = __webpack_require__("00ee");
var redefine = __webpack_require__("6eeb");
var toString = __webpack_require__("b041");

// `Object.prototype.toString` method
// https://tc39.es/ecma262/#sec-object.prototype.tostring
if (!TO_STRING_TAG_SUPPORT) {
  redefine(Object.prototype, 'toString', toString, { unsafe: true });
}


/***/ }),

/***/ "d44e":
/***/ (function(module, exports, __webpack_require__) {

var defineProperty = __webpack_require__("9bf2").f;
var has = __webpack_require__("5135");
var wellKnownSymbol = __webpack_require__("b622");

var TO_STRING_TAG = wellKnownSymbol('toStringTag');

module.exports = function (it, TAG, STATIC) {
  if (it && !has(it = STATIC ? it : it.prototype, TO_STRING_TAG)) {
    defineProperty(it, TO_STRING_TAG, { configurable: true, value: TAG });
  }
};


/***/ }),

/***/ "d784":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

// TODO: Remove from `core-js@4` since it's moved to entry points
__webpack_require__("ac1f");
var redefine = __webpack_require__("6eeb");
var fails = __webpack_require__("d039");
var wellKnownSymbol = __webpack_require__("b622");
var createNonEnumerableProperty = __webpack_require__("9112");

var SPECIES = wellKnownSymbol('species');

var REPLACE_SUPPORTS_NAMED_GROUPS = !fails(function () {
  // #replace needs built-in support for named groups.
  // #match works fine because it just return the exec results, even if it has
  // a "grops" property.
  var re = /./;
  re.exec = function () {
    var result = [];
    result.groups = { a: '7' };
    return result;
  };
  return ''.replace(re, '$<a>') !== '7';
});

// IE <= 11 replaces $0 with the whole match, as if it was $&
// https://stackoverflow.com/questions/6024666/getting-ie-to-replace-a-regex-with-the-literal-string-0
var REPLACE_KEEPS_$0 = (function () {
  // eslint-disable-next-line regexp/prefer-escape-replacement-dollar-char -- required for testing
  return 'a'.replace(/./, '$0') === '$0';
})();

var REPLACE = wellKnownSymbol('replace');
// Safari <= 13.0.3(?) substitutes nth capture where n>m with an empty string
var REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE = (function () {
  if (/./[REPLACE]) {
    return /./[REPLACE]('a', '$0') === '';
  }
  return false;
})();

// Chrome 51 has a buggy "split" implementation when RegExp#exec !== nativeExec
// Weex JS has frozen built-in prototypes, so use try / catch wrapper
var SPLIT_WORKS_WITH_OVERWRITTEN_EXEC = !fails(function () {
  // eslint-disable-next-line regexp/no-empty-group -- required for testing
  var re = /(?:)/;
  var originalExec = re.exec;
  re.exec = function () { return originalExec.apply(this, arguments); };
  var result = 'ab'.split(re);
  return result.length !== 2 || result[0] !== 'a' || result[1] !== 'b';
});

module.exports = function (KEY, length, exec, sham) {
  var SYMBOL = wellKnownSymbol(KEY);

  var DELEGATES_TO_SYMBOL = !fails(function () {
    // String methods call symbol-named RegEp methods
    var O = {};
    O[SYMBOL] = function () { return 7; };
    return ''[KEY](O) != 7;
  });

  var DELEGATES_TO_EXEC = DELEGATES_TO_SYMBOL && !fails(function () {
    // Symbol-named RegExp methods call .exec
    var execCalled = false;
    var re = /a/;

    if (KEY === 'split') {
      // We can't use real regex here since it causes deoptimization
      // and serious performance degradation in V8
      // https://github.com/zloirock/core-js/issues/306
      re = {};
      // RegExp[@@split] doesn't call the regex's exec method, but first creates
      // a new one. We need to return the patched regex when creating the new one.
      re.constructor = {};
      re.constructor[SPECIES] = function () { return re; };
      re.flags = '';
      re[SYMBOL] = /./[SYMBOL];
    }

    re.exec = function () { execCalled = true; return null; };

    re[SYMBOL]('');
    return !execCalled;
  });

  if (
    !DELEGATES_TO_SYMBOL ||
    !DELEGATES_TO_EXEC ||
    (KEY === 'replace' && !(
      REPLACE_SUPPORTS_NAMED_GROUPS &&
      REPLACE_KEEPS_$0 &&
      !REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE
    )) ||
    (KEY === 'split' && !SPLIT_WORKS_WITH_OVERWRITTEN_EXEC)
  ) {
    var nativeRegExpMethod = /./[SYMBOL];
    var methods = exec(SYMBOL, ''[KEY], function (nativeMethod, regexp, str, arg2, forceStringMethod) {
      if (regexp.exec === RegExp.prototype.exec) {
        if (DELEGATES_TO_SYMBOL && !forceStringMethod) {
          // The native String method already delegates to @@method (this
          // polyfilled function), leasing to infinite recursion.
          // We avoid it by directly calling the native @@method method.
          return { done: true, value: nativeRegExpMethod.call(regexp, str, arg2) };
        }
        return { done: true, value: nativeMethod.call(str, regexp, arg2) };
      }
      return { done: false };
    }, {
      REPLACE_KEEPS_$0: REPLACE_KEEPS_$0,
      REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE: REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE
    });
    var stringMethod = methods[0];
    var regexMethod = methods[1];

    redefine(String.prototype, KEY, stringMethod);
    redefine(RegExp.prototype, SYMBOL, length == 2
      // 21.2.5.8 RegExp.prototype[@@replace](string, replaceValue)
      // 21.2.5.11 RegExp.prototype[@@split](string, limit)
      ? function (string, arg) { return regexMethod.call(string, this, arg); }
      // 21.2.5.6 RegExp.prototype[@@match](string)
      // 21.2.5.9 RegExp.prototype[@@search](string)
      : function (string) { return regexMethod.call(string, this); }
    );
  }

  if (sham) createNonEnumerableProperty(RegExp.prototype[SYMBOL], 'sham', true);
};


/***/ }),

/***/ "d81d":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var $map = __webpack_require__("b727").map;
var arrayMethodHasSpeciesSupport = __webpack_require__("1dde");

var HAS_SPECIES_SUPPORT = arrayMethodHasSpeciesSupport('map');

// `Array.prototype.map` method
// https://tc39.es/ecma262/#sec-array.prototype.map
// with adding support of @@species
$({ target: 'Array', proto: true, forced: !HAS_SPECIES_SUPPORT }, {
  map: function map(callbackfn /* , thisArg */) {
    return $map(this, callbackfn, arguments.length > 1 ? arguments[1] : undefined);
  }
});


/***/ }),

/***/ "da84":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(global) {var check = function (it) {
  return it && it.Math == Math && it;
};

// https://github.com/zloirock/core-js/issues/86#issuecomment-115759028
module.exports =
  // eslint-disable-next-line es/no-global-this -- safe
  check(typeof globalThis == 'object' && globalThis) ||
  check(typeof window == 'object' && window) ||
  // eslint-disable-next-line no-restricted-globals -- safe
  check(typeof self == 'object' && self) ||
  check(typeof global == 'object' && global) ||
  // eslint-disable-next-line no-new-func -- fallback
  (function () { return this; })() || Function('return this')();

/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__("c8ba")))

/***/ }),

/***/ "dbb4":
/***/ (function(module, exports, __webpack_require__) {

var $ = __webpack_require__("23e7");
var DESCRIPTORS = __webpack_require__("83ab");
var ownKeys = __webpack_require__("56ef");
var toIndexedObject = __webpack_require__("fc6a");
var getOwnPropertyDescriptorModule = __webpack_require__("06cf");
var createProperty = __webpack_require__("8418");

// `Object.getOwnPropertyDescriptors` method
// https://tc39.es/ecma262/#sec-object.getownpropertydescriptors
$({ target: 'Object', stat: true, sham: !DESCRIPTORS }, {
  getOwnPropertyDescriptors: function getOwnPropertyDescriptors(object) {
    var O = toIndexedObject(object);
    var getOwnPropertyDescriptor = getOwnPropertyDescriptorModule.f;
    var keys = ownKeys(O);
    var result = {};
    var index = 0;
    var key, descriptor;
    while (keys.length > index) {
      descriptor = getOwnPropertyDescriptor(O, key = keys[index++]);
      if (descriptor !== undefined) createProperty(result, key, descriptor);
    }
    return result;
  }
});


/***/ }),

/***/ "ddb0":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");
var DOMIterables = __webpack_require__("fdbc");
var ArrayIteratorMethods = __webpack_require__("e260");
var createNonEnumerableProperty = __webpack_require__("9112");
var wellKnownSymbol = __webpack_require__("b622");

var ITERATOR = wellKnownSymbol('iterator');
var TO_STRING_TAG = wellKnownSymbol('toStringTag');
var ArrayValues = ArrayIteratorMethods.values;

for (var COLLECTION_NAME in DOMIterables) {
  var Collection = global[COLLECTION_NAME];
  var CollectionPrototype = Collection && Collection.prototype;
  if (CollectionPrototype) {
    // some Chrome versions have non-configurable methods on DOMTokenList
    if (CollectionPrototype[ITERATOR] !== ArrayValues) try {
      createNonEnumerableProperty(CollectionPrototype, ITERATOR, ArrayValues);
    } catch (error) {
      CollectionPrototype[ITERATOR] = ArrayValues;
    }
    if (!CollectionPrototype[TO_STRING_TAG]) {
      createNonEnumerableProperty(CollectionPrototype, TO_STRING_TAG, COLLECTION_NAME);
    }
    if (DOMIterables[COLLECTION_NAME]) for (var METHOD_NAME in ArrayIteratorMethods) {
      // some Chrome versions have non-configurable methods on DOMTokenList
      if (CollectionPrototype[METHOD_NAME] !== ArrayIteratorMethods[METHOD_NAME]) try {
        createNonEnumerableProperty(CollectionPrototype, METHOD_NAME, ArrayIteratorMethods[METHOD_NAME]);
      } catch (error) {
        CollectionPrototype[METHOD_NAME] = ArrayIteratorMethods[METHOD_NAME];
      }
    }
  }
}


/***/ }),

/***/ "ddd7":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const types = __webpack_require__("68fa");

const api = {};
module.exports = api;

// define URL parser
// parseUri 1.2.2
// (c) Steven Levithan <stevenlevithan.com>
// MIT License
// with local jsonld.js modifications
api.parsers = {
  simple: {
    // RFC 3986 basic parts
    keys: [
      'href', 'scheme', 'authority', 'path', 'query', 'fragment'
    ],
    /* eslint-disable-next-line max-len */
    regex: /^(?:([^:\/?#]+):)?(?:\/\/([^\/?#]*))?([^?#]*)(?:\?([^#]*))?(?:#(.*))?/
  },
  full: {
    keys: [
      'href', 'protocol', 'scheme', 'authority', 'auth', 'user', 'password',
      'hostname', 'port', 'path', 'directory', 'file', 'query', 'fragment'
    ],
    /* eslint-disable-next-line max-len */
    regex: /^(([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?(?:(((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/
  }
};
api.parse = (str, parser) => {
  const parsed = {};
  const o = api.parsers[parser || 'full'];
  const m = o.regex.exec(str);
  let i = o.keys.length;
  while(i--) {
    parsed[o.keys[i]] = (m[i] === undefined) ? null : m[i];
  }

  // remove default ports in found in URLs
  if((parsed.scheme === 'https' && parsed.port === '443') ||
    (parsed.scheme === 'http' && parsed.port === '80')) {
    parsed.href = parsed.href.replace(':' + parsed.port, '');
    parsed.authority = parsed.authority.replace(':' + parsed.port, '');
    parsed.port = null;
  }

  parsed.normalizedPath = api.removeDotSegments(parsed.path);
  return parsed;
};

/**
 * Prepends a base IRI to the given relative IRI.
 *
 * @param base the base IRI.
 * @param iri the relative IRI.
 *
 * @return the absolute IRI.
 */
api.prependBase = (base, iri) => {
  // skip IRI processing
  if(base === null) {
    return iri;
  }
  // already an absolute IRI
  if(api.isAbsolute(iri)) {
    return iri;
  }

  // parse base if it is a string
  if(!base || types.isString(base)) {
    base = api.parse(base || '');
  }

  // parse given IRI
  const rel = api.parse(iri);

  // per RFC3986 5.2.2
  const transform = {
    protocol: base.protocol || ''
  };

  if(rel.authority !== null) {
    transform.authority = rel.authority;
    transform.path = rel.path;
    transform.query = rel.query;
  } else {
    transform.authority = base.authority;

    if(rel.path === '') {
      transform.path = base.path;
      if(rel.query !== null) {
        transform.query = rel.query;
      } else {
        transform.query = base.query;
      }
    } else {
      if(rel.path.indexOf('/') === 0) {
        // IRI represents an absolute path
        transform.path = rel.path;
      } else {
        // merge paths
        let path = base.path;

        // append relative path to the end of the last directory from base
        path = path.substr(0, path.lastIndexOf('/') + 1);
        if((path.length > 0 || base.authority) && path.substr(-1) !== '/') {
          path += '/';
        }
        path += rel.path;

        transform.path = path;
      }
      transform.query = rel.query;
    }
  }

  if(rel.path !== '') {
    // remove slashes and dots in path
    transform.path = api.removeDotSegments(transform.path);
  }

  // construct URL
  let rval = transform.protocol;
  if(transform.authority !== null) {
    rval += '//' + transform.authority;
  }
  rval += transform.path;
  if(transform.query !== null) {
    rval += '?' + transform.query;
  }
  if(rel.fragment !== null) {
    rval += '#' + rel.fragment;
  }

  // handle empty base
  if(rval === '') {
    rval = './';
  }

  return rval;
};

/**
 * Removes a base IRI from the given absolute IRI.
 *
 * @param base the base IRI.
 * @param iri the absolute IRI.
 *
 * @return the relative IRI if relative to base, otherwise the absolute IRI.
 */
api.removeBase = (base, iri) => {
  // skip IRI processing
  if(base === null) {
    return iri;
  }

  if(!base || types.isString(base)) {
    base = api.parse(base || '');
  }

  // establish base root
  let root = '';
  if(base.href !== '') {
    root += (base.protocol || '') + '//' + (base.authority || '');
  } else if(iri.indexOf('//')) {
    // support network-path reference with empty base
    root += '//';
  }

  // IRI not relative to base
  if(iri.indexOf(root) !== 0) {
    return iri;
  }

  // remove root from IRI and parse remainder
  const rel = api.parse(iri.substr(root.length));

  // remove path segments that match (do not remove last segment unless there
  // is a hash or query)
  const baseSegments = base.normalizedPath.split('/');
  const iriSegments = rel.normalizedPath.split('/');
  const last = (rel.fragment || rel.query) ? 0 : 1;
  while(baseSegments.length > 0 && iriSegments.length > last) {
    if(baseSegments[0] !== iriSegments[0]) {
      break;
    }
    baseSegments.shift();
    iriSegments.shift();
  }

  // use '../' for each non-matching base segment
  let rval = '';
  if(baseSegments.length > 0) {
    // don't count the last segment (if it ends with '/' last path doesn't
    // count and if it doesn't end with '/' it isn't a path)
    baseSegments.pop();
    for(let i = 0; i < baseSegments.length; ++i) {
      rval += '../';
    }
  }

  // prepend remaining segments
  rval += iriSegments.join('/');

  // add query and hash
  if(rel.query !== null) {
    rval += '?' + rel.query;
  }
  if(rel.fragment !== null) {
    rval += '#' + rel.fragment;
  }

  // handle empty base
  if(rval === '') {
    rval = './';
  }

  return rval;
};

/**
 * Removes dot segments from a URL path.
 *
 * @param path the path to remove dot segments from.
 */
api.removeDotSegments = path => {
  // RFC 3986 5.2.4 (reworked)

  // empty path shortcut
  if(path.length === 0) {
    return '';
  }

  const input = path.split('/');
  const output = [];

  while(input.length > 0) {
    const next = input.shift();
    const done = input.length === 0;

    if(next === '.') {
      if(done) {
        // ensure output has trailing /
        output.push('');
      }
      continue;
    }

    if(next === '..') {
      output.pop();
      if(done) {
        // ensure output has trailing /
        output.push('');
      }
      continue;
    }

    output.push(next);
  }

  // if path was absolute, ensure output has leading /
  if(path[0] === '/' && output.length > 0 && output[0] !== '') {
    output.unshift('');
  }
  if(output.length === 1 && output[0] === '') {
    return '/';
  }

  return output.join('/');
};

// TODO: time better isAbsolute/isRelative checks using full regexes:
// http://jmrware.com/articles/2009/uri_regexp/URI_regex.html

// regex to check for absolute IRI (starting scheme and ':') or blank node IRI
const isAbsoluteRegex = /^([A-Za-z][A-Za-z0-9+-.]*|_):[^\s]*$/;

/**
 * Returns true if the given value is an absolute IRI or blank node IRI, false
 * if not.
 * Note: This weak check only checks for a correct starting scheme.
 *
 * @param v the value to check.
 *
 * @return true if the value is an absolute IRI, false if not.
 */
api.isAbsolute = v => types.isString(v) && isAbsoluteRegex.test(v);

/**
 * Returns true if the given value is a relative IRI, false if not.
 * Note: this is a weak check.
 *
 * @param v the value to check.
 *
 * @return true if the value is a relative IRI, false if not.
 */
api.isRelative = v => types.isString(v);


/***/ }),

/***/ "df75":
/***/ (function(module, exports, __webpack_require__) {

var internalObjectKeys = __webpack_require__("ca84");
var enumBugKeys = __webpack_require__("7839");

// `Object.keys` method
// https://tc39.es/ecma262/#sec-object.keys
// eslint-disable-next-line es/no-object-keys -- safe
module.exports = Object.keys || function keys(O) {
  return internalObjectKeys(O, enumBugKeys);
};


/***/ }),

/***/ "df7c":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(process) {// .dirname, .basename, and .extname methods are extracted from Node.js v8.11.1,
// backported and transplited with Babel, with backwards-compat fixes

// Copyright Joyent, Inc. and other Node contributors.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.

// resolves . and .. elements in a path array with directory names there
// must be no slashes, empty elements, or device names (c:\) in the array
// (so also no leading and trailing slashes - it does not distinguish
// relative and absolute paths)
function normalizeArray(parts, allowAboveRoot) {
  // if the path tries to go above the root, `up` ends up > 0
  var up = 0;
  for (var i = parts.length - 1; i >= 0; i--) {
    var last = parts[i];
    if (last === '.') {
      parts.splice(i, 1);
    } else if (last === '..') {
      parts.splice(i, 1);
      up++;
    } else if (up) {
      parts.splice(i, 1);
      up--;
    }
  }

  // if the path is allowed to go above the root, restore leading ..s
  if (allowAboveRoot) {
    for (; up--; up) {
      parts.unshift('..');
    }
  }

  return parts;
}

// path.resolve([from ...], to)
// posix version
exports.resolve = function() {
  var resolvedPath = '',
      resolvedAbsolute = false;

  for (var i = arguments.length - 1; i >= -1 && !resolvedAbsolute; i--) {
    var path = (i >= 0) ? arguments[i] : process.cwd();

    // Skip empty and invalid entries
    if (typeof path !== 'string') {
      throw new TypeError('Arguments to path.resolve must be strings');
    } else if (!path) {
      continue;
    }

    resolvedPath = path + '/' + resolvedPath;
    resolvedAbsolute = path.charAt(0) === '/';
  }

  // At this point the path should be resolved to a full absolute path, but
  // handle relative paths to be safe (might happen when process.cwd() fails)

  // Normalize the path
  resolvedPath = normalizeArray(filter(resolvedPath.split('/'), function(p) {
    return !!p;
  }), !resolvedAbsolute).join('/');

  return ((resolvedAbsolute ? '/' : '') + resolvedPath) || '.';
};

// path.normalize(path)
// posix version
exports.normalize = function(path) {
  var isAbsolute = exports.isAbsolute(path),
      trailingSlash = substr(path, -1) === '/';

  // Normalize the path
  path = normalizeArray(filter(path.split('/'), function(p) {
    return !!p;
  }), !isAbsolute).join('/');

  if (!path && !isAbsolute) {
    path = '.';
  }
  if (path && trailingSlash) {
    path += '/';
  }

  return (isAbsolute ? '/' : '') + path;
};

// posix version
exports.isAbsolute = function(path) {
  return path.charAt(0) === '/';
};

// posix version
exports.join = function() {
  var paths = Array.prototype.slice.call(arguments, 0);
  return exports.normalize(filter(paths, function(p, index) {
    if (typeof p !== 'string') {
      throw new TypeError('Arguments to path.join must be strings');
    }
    return p;
  }).join('/'));
};


// path.relative(from, to)
// posix version
exports.relative = function(from, to) {
  from = exports.resolve(from).substr(1);
  to = exports.resolve(to).substr(1);

  function trim(arr) {
    var start = 0;
    for (; start < arr.length; start++) {
      if (arr[start] !== '') break;
    }

    var end = arr.length - 1;
    for (; end >= 0; end--) {
      if (arr[end] !== '') break;
    }

    if (start > end) return [];
    return arr.slice(start, end - start + 1);
  }

  var fromParts = trim(from.split('/'));
  var toParts = trim(to.split('/'));

  var length = Math.min(fromParts.length, toParts.length);
  var samePartsLength = length;
  for (var i = 0; i < length; i++) {
    if (fromParts[i] !== toParts[i]) {
      samePartsLength = i;
      break;
    }
  }

  var outputParts = [];
  for (var i = samePartsLength; i < fromParts.length; i++) {
    outputParts.push('..');
  }

  outputParts = outputParts.concat(toParts.slice(samePartsLength));

  return outputParts.join('/');
};

exports.sep = '/';
exports.delimiter = ':';

exports.dirname = function (path) {
  if (typeof path !== 'string') path = path + '';
  if (path.length === 0) return '.';
  var code = path.charCodeAt(0);
  var hasRoot = code === 47 /*/*/;
  var end = -1;
  var matchedSlash = true;
  for (var i = path.length - 1; i >= 1; --i) {
    code = path.charCodeAt(i);
    if (code === 47 /*/*/) {
        if (!matchedSlash) {
          end = i;
          break;
        }
      } else {
      // We saw the first non-path separator
      matchedSlash = false;
    }
  }

  if (end === -1) return hasRoot ? '/' : '.';
  if (hasRoot && end === 1) {
    // return '//';
    // Backwards-compat fix:
    return '/';
  }
  return path.slice(0, end);
};

function basename(path) {
  if (typeof path !== 'string') path = path + '';

  var start = 0;
  var end = -1;
  var matchedSlash = true;
  var i;

  for (i = path.length - 1; i >= 0; --i) {
    if (path.charCodeAt(i) === 47 /*/*/) {
        // If we reached a path separator that was not part of a set of path
        // separators at the end of the string, stop now
        if (!matchedSlash) {
          start = i + 1;
          break;
        }
      } else if (end === -1) {
      // We saw the first non-path separator, mark this as the end of our
      // path component
      matchedSlash = false;
      end = i + 1;
    }
  }

  if (end === -1) return '';
  return path.slice(start, end);
}

// Uses a mixed approach for backwards-compatibility, as ext behavior changed
// in new Node.js versions, so only basename() above is backported here
exports.basename = function (path, ext) {
  var f = basename(path);
  if (ext && f.substr(-1 * ext.length) === ext) {
    f = f.substr(0, f.length - ext.length);
  }
  return f;
};

exports.extname = function (path) {
  if (typeof path !== 'string') path = path + '';
  var startDot = -1;
  var startPart = 0;
  var end = -1;
  var matchedSlash = true;
  // Track the state of characters (if any) we see before our first dot and
  // after any path separator we find
  var preDotState = 0;
  for (var i = path.length - 1; i >= 0; --i) {
    var code = path.charCodeAt(i);
    if (code === 47 /*/*/) {
        // If we reached a path separator that was not part of a set of path
        // separators at the end of the string, stop now
        if (!matchedSlash) {
          startPart = i + 1;
          break;
        }
        continue;
      }
    if (end === -1) {
      // We saw the first non-path separator, mark this as the end of our
      // extension
      matchedSlash = false;
      end = i + 1;
    }
    if (code === 46 /*.*/) {
        // If this is our first dot, mark it as the start of our extension
        if (startDot === -1)
          startDot = i;
        else if (preDotState !== 1)
          preDotState = 1;
    } else if (startDot !== -1) {
      // We saw a non-dot and non-path separator before our dot, so we should
      // have a good chance at having a non-empty extension
      preDotState = -1;
    }
  }

  if (startDot === -1 || end === -1 ||
      // We saw a non-dot character immediately before the dot
      preDotState === 0 ||
      // The (right-most) trimmed path component is exactly '..'
      preDotState === 1 && startDot === end - 1 && startDot === startPart + 1) {
    return '';
  }
  return path.slice(startDot, end);
};

function filter (xs, f) {
    if (xs.filter) return xs.filter(f);
    var res = [];
    for (var i = 0; i < xs.length; i++) {
        if (f(xs[i], i, xs)) res.push(xs[i]);
    }
    return res;
}

// String.prototype.substr - negative index don't work in IE8
var substr = 'ab'.substr(-1) === 'b'
    ? function (str, start, len) { return str.substr(start, len) }
    : function (str, start, len) {
        if (start < 0) start = str.length + start;
        return str.substr(start, len);
    }
;

/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__("4362")))

/***/ }),

/***/ "e163":
/***/ (function(module, exports, __webpack_require__) {

var has = __webpack_require__("5135");
var toObject = __webpack_require__("7b0b");
var sharedKey = __webpack_require__("f772");
var CORRECT_PROTOTYPE_GETTER = __webpack_require__("e177");

var IE_PROTO = sharedKey('IE_PROTO');
var ObjectPrototype = Object.prototype;

// `Object.getPrototypeOf` method
// https://tc39.es/ecma262/#sec-object.getprototypeof
// eslint-disable-next-line es/no-object-getprototypeof -- safe
module.exports = CORRECT_PROTOTYPE_GETTER ? Object.getPrototypeOf : function (O) {
  O = toObject(O);
  if (has(O, IE_PROTO)) return O[IE_PROTO];
  if (typeof O.constructor == 'function' && O instanceof O.constructor) {
    return O.constructor.prototype;
  } return O instanceof Object ? ObjectPrototype : null;
};


/***/ }),

/***/ "e177":
/***/ (function(module, exports, __webpack_require__) {

var fails = __webpack_require__("d039");

module.exports = !fails(function () {
  function F() { /* empty */ }
  F.prototype.constructor = null;
  // eslint-disable-next-line es/no-object-getprototypeof -- required for testing
  return Object.getPrototypeOf(new F()) !== F.prototype;
});


/***/ }),

/***/ "e260":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var toIndexedObject = __webpack_require__("fc6a");
var addToUnscopables = __webpack_require__("44d2");
var Iterators = __webpack_require__("3f8c");
var InternalStateModule = __webpack_require__("69f3");
var defineIterator = __webpack_require__("7dd0");

var ARRAY_ITERATOR = 'Array Iterator';
var setInternalState = InternalStateModule.set;
var getInternalState = InternalStateModule.getterFor(ARRAY_ITERATOR);

// `Array.prototype.entries` method
// https://tc39.es/ecma262/#sec-array.prototype.entries
// `Array.prototype.keys` method
// https://tc39.es/ecma262/#sec-array.prototype.keys
// `Array.prototype.values` method
// https://tc39.es/ecma262/#sec-array.prototype.values
// `Array.prototype[@@iterator]` method
// https://tc39.es/ecma262/#sec-array.prototype-@@iterator
// `CreateArrayIterator` internal method
// https://tc39.es/ecma262/#sec-createarrayiterator
module.exports = defineIterator(Array, 'Array', function (iterated, kind) {
  setInternalState(this, {
    type: ARRAY_ITERATOR,
    target: toIndexedObject(iterated), // target
    index: 0,                          // next index
    kind: kind                         // kind
  });
// `%ArrayIteratorPrototype%.next` method
// https://tc39.es/ecma262/#sec-%arrayiteratorprototype%.next
}, function () {
  var state = getInternalState(this);
  var target = state.target;
  var kind = state.kind;
  var index = state.index++;
  if (!target || index >= target.length) {
    state.target = undefined;
    return { value: undefined, done: true };
  }
  if (kind == 'keys') return { value: index, done: false };
  if (kind == 'values') return { value: target[index], done: false };
  return { value: [index, target[index]], done: false };
}, 'values');

// argumentsList[@@iterator] is %ArrayProto_values%
// https://tc39.es/ecma262/#sec-createunmappedargumentsobject
// https://tc39.es/ecma262/#sec-createmappedargumentsobject
Iterators.Arguments = Iterators.Array;

// https://tc39.es/ecma262/#sec-array.prototype-@@unscopables
addToUnscopables('keys');
addToUnscopables('values');
addToUnscopables('entries');


/***/ }),

/***/ "e2cc":
/***/ (function(module, exports, __webpack_require__) {

var redefine = __webpack_require__("6eeb");

module.exports = function (target, src, options) {
  for (var key in src) redefine(target, key, src[key], options);
  return target;
};


/***/ }),

/***/ "e439":
/***/ (function(module, exports, __webpack_require__) {

var $ = __webpack_require__("23e7");
var fails = __webpack_require__("d039");
var toIndexedObject = __webpack_require__("fc6a");
var nativeGetOwnPropertyDescriptor = __webpack_require__("06cf").f;
var DESCRIPTORS = __webpack_require__("83ab");

var FAILS_ON_PRIMITIVES = fails(function () { nativeGetOwnPropertyDescriptor(1); });
var FORCED = !DESCRIPTORS || FAILS_ON_PRIMITIVES;

// `Object.getOwnPropertyDescriptor` method
// https://tc39.es/ecma262/#sec-object.getownpropertydescriptor
$({ target: 'Object', stat: true, forced: FORCED, sham: !DESCRIPTORS }, {
  getOwnPropertyDescriptor: function getOwnPropertyDescriptor(it, key) {
    return nativeGetOwnPropertyDescriptor(toIndexedObject(it), key);
  }
});


/***/ }),

/***/ "e538":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");

exports.f = wellKnownSymbol;


/***/ }),

/***/ "e667":
/***/ (function(module, exports) {

module.exports = function (exec) {
  try {
    return { error: false, value: exec() };
  } catch (error) {
    return { error: true, value: error };
  }
};


/***/ }),

/***/ "e6cf":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var $ = __webpack_require__("23e7");
var IS_PURE = __webpack_require__("c430");
var global = __webpack_require__("da84");
var getBuiltIn = __webpack_require__("d066");
var NativePromise = __webpack_require__("fea9");
var redefine = __webpack_require__("6eeb");
var redefineAll = __webpack_require__("e2cc");
var setToStringTag = __webpack_require__("d44e");
var setSpecies = __webpack_require__("2626");
var isObject = __webpack_require__("861d");
var aFunction = __webpack_require__("1c0b");
var anInstance = __webpack_require__("19aa");
var inspectSource = __webpack_require__("8925");
var iterate = __webpack_require__("2266");
var checkCorrectnessOfIteration = __webpack_require__("1c7e");
var speciesConstructor = __webpack_require__("4840");
var task = __webpack_require__("2cf4").set;
var microtask = __webpack_require__("b575");
var promiseResolve = __webpack_require__("cdf9");
var hostReportErrors = __webpack_require__("44de");
var newPromiseCapabilityModule = __webpack_require__("f069");
var perform = __webpack_require__("e667");
var InternalStateModule = __webpack_require__("69f3");
var isForced = __webpack_require__("94ca");
var wellKnownSymbol = __webpack_require__("b622");
var IS_NODE = __webpack_require__("605d");
var V8_VERSION = __webpack_require__("2d00");

var SPECIES = wellKnownSymbol('species');
var PROMISE = 'Promise';
var getInternalState = InternalStateModule.get;
var setInternalState = InternalStateModule.set;
var getInternalPromiseState = InternalStateModule.getterFor(PROMISE);
var PromiseConstructor = NativePromise;
var TypeError = global.TypeError;
var document = global.document;
var process = global.process;
var $fetch = getBuiltIn('fetch');
var newPromiseCapability = newPromiseCapabilityModule.f;
var newGenericPromiseCapability = newPromiseCapability;
var DISPATCH_EVENT = !!(document && document.createEvent && global.dispatchEvent);
var NATIVE_REJECTION_EVENT = typeof PromiseRejectionEvent == 'function';
var UNHANDLED_REJECTION = 'unhandledrejection';
var REJECTION_HANDLED = 'rejectionhandled';
var PENDING = 0;
var FULFILLED = 1;
var REJECTED = 2;
var HANDLED = 1;
var UNHANDLED = 2;
var Internal, OwnPromiseCapability, PromiseWrapper, nativeThen;

var FORCED = isForced(PROMISE, function () {
  var GLOBAL_CORE_JS_PROMISE = inspectSource(PromiseConstructor) !== String(PromiseConstructor);
  if (!GLOBAL_CORE_JS_PROMISE) {
    // V8 6.6 (Node 10 and Chrome 66) have a bug with resolving custom thenables
    // https://bugs.chromium.org/p/chromium/issues/detail?id=830565
    // We can't detect it synchronously, so just check versions
    if (V8_VERSION === 66) return true;
    // Unhandled rejections tracking support, NodeJS Promise without it fails @@species test
    if (!IS_NODE && !NATIVE_REJECTION_EVENT) return true;
  }
  // We need Promise#finally in the pure version for preventing prototype pollution
  if (IS_PURE && !PromiseConstructor.prototype['finally']) return true;
  // We can't use @@species feature detection in V8 since it causes
  // deoptimization and performance degradation
  // https://github.com/zloirock/core-js/issues/679
  if (V8_VERSION >= 51 && /native code/.test(PromiseConstructor)) return false;
  // Detect correctness of subclassing with @@species support
  var promise = PromiseConstructor.resolve(1);
  var FakePromise = function (exec) {
    exec(function () { /* empty */ }, function () { /* empty */ });
  };
  var constructor = promise.constructor = {};
  constructor[SPECIES] = FakePromise;
  return !(promise.then(function () { /* empty */ }) instanceof FakePromise);
});

var INCORRECT_ITERATION = FORCED || !checkCorrectnessOfIteration(function (iterable) {
  PromiseConstructor.all(iterable)['catch'](function () { /* empty */ });
});

// helpers
var isThenable = function (it) {
  var then;
  return isObject(it) && typeof (then = it.then) == 'function' ? then : false;
};

var notify = function (state, isReject) {
  if (state.notified) return;
  state.notified = true;
  var chain = state.reactions;
  microtask(function () {
    var value = state.value;
    var ok = state.state == FULFILLED;
    var index = 0;
    // variable length - can't use forEach
    while (chain.length > index) {
      var reaction = chain[index++];
      var handler = ok ? reaction.ok : reaction.fail;
      var resolve = reaction.resolve;
      var reject = reaction.reject;
      var domain = reaction.domain;
      var result, then, exited;
      try {
        if (handler) {
          if (!ok) {
            if (state.rejection === UNHANDLED) onHandleUnhandled(state);
            state.rejection = HANDLED;
          }
          if (handler === true) result = value;
          else {
            if (domain) domain.enter();
            result = handler(value); // can throw
            if (domain) {
              domain.exit();
              exited = true;
            }
          }
          if (result === reaction.promise) {
            reject(TypeError('Promise-chain cycle'));
          } else if (then = isThenable(result)) {
            then.call(result, resolve, reject);
          } else resolve(result);
        } else reject(value);
      } catch (error) {
        if (domain && !exited) domain.exit();
        reject(error);
      }
    }
    state.reactions = [];
    state.notified = false;
    if (isReject && !state.rejection) onUnhandled(state);
  });
};

var dispatchEvent = function (name, promise, reason) {
  var event, handler;
  if (DISPATCH_EVENT) {
    event = document.createEvent('Event');
    event.promise = promise;
    event.reason = reason;
    event.initEvent(name, false, true);
    global.dispatchEvent(event);
  } else event = { promise: promise, reason: reason };
  if (!NATIVE_REJECTION_EVENT && (handler = global['on' + name])) handler(event);
  else if (name === UNHANDLED_REJECTION) hostReportErrors('Unhandled promise rejection', reason);
};

var onUnhandled = function (state) {
  task.call(global, function () {
    var promise = state.facade;
    var value = state.value;
    var IS_UNHANDLED = isUnhandled(state);
    var result;
    if (IS_UNHANDLED) {
      result = perform(function () {
        if (IS_NODE) {
          process.emit('unhandledRejection', value, promise);
        } else dispatchEvent(UNHANDLED_REJECTION, promise, value);
      });
      // Browsers should not trigger `rejectionHandled` event if it was handled here, NodeJS - should
      state.rejection = IS_NODE || isUnhandled(state) ? UNHANDLED : HANDLED;
      if (result.error) throw result.value;
    }
  });
};

var isUnhandled = function (state) {
  return state.rejection !== HANDLED && !state.parent;
};

var onHandleUnhandled = function (state) {
  task.call(global, function () {
    var promise = state.facade;
    if (IS_NODE) {
      process.emit('rejectionHandled', promise);
    } else dispatchEvent(REJECTION_HANDLED, promise, state.value);
  });
};

var bind = function (fn, state, unwrap) {
  return function (value) {
    fn(state, value, unwrap);
  };
};

var internalReject = function (state, value, unwrap) {
  if (state.done) return;
  state.done = true;
  if (unwrap) state = unwrap;
  state.value = value;
  state.state = REJECTED;
  notify(state, true);
};

var internalResolve = function (state, value, unwrap) {
  if (state.done) return;
  state.done = true;
  if (unwrap) state = unwrap;
  try {
    if (state.facade === value) throw TypeError("Promise can't be resolved itself");
    var then = isThenable(value);
    if (then) {
      microtask(function () {
        var wrapper = { done: false };
        try {
          then.call(value,
            bind(internalResolve, wrapper, state),
            bind(internalReject, wrapper, state)
          );
        } catch (error) {
          internalReject(wrapper, error, state);
        }
      });
    } else {
      state.value = value;
      state.state = FULFILLED;
      notify(state, false);
    }
  } catch (error) {
    internalReject({ done: false }, error, state);
  }
};

// constructor polyfill
if (FORCED) {
  // 25.4.3.1 Promise(executor)
  PromiseConstructor = function Promise(executor) {
    anInstance(this, PromiseConstructor, PROMISE);
    aFunction(executor);
    Internal.call(this);
    var state = getInternalState(this);
    try {
      executor(bind(internalResolve, state), bind(internalReject, state));
    } catch (error) {
      internalReject(state, error);
    }
  };
  // eslint-disable-next-line no-unused-vars -- required for `.length`
  Internal = function Promise(executor) {
    setInternalState(this, {
      type: PROMISE,
      done: false,
      notified: false,
      parent: false,
      reactions: [],
      rejection: false,
      state: PENDING,
      value: undefined
    });
  };
  Internal.prototype = redefineAll(PromiseConstructor.prototype, {
    // `Promise.prototype.then` method
    // https://tc39.es/ecma262/#sec-promise.prototype.then
    then: function then(onFulfilled, onRejected) {
      var state = getInternalPromiseState(this);
      var reaction = newPromiseCapability(speciesConstructor(this, PromiseConstructor));
      reaction.ok = typeof onFulfilled == 'function' ? onFulfilled : true;
      reaction.fail = typeof onRejected == 'function' && onRejected;
      reaction.domain = IS_NODE ? process.domain : undefined;
      state.parent = true;
      state.reactions.push(reaction);
      if (state.state != PENDING) notify(state, false);
      return reaction.promise;
    },
    // `Promise.prototype.catch` method
    // https://tc39.es/ecma262/#sec-promise.prototype.catch
    'catch': function (onRejected) {
      return this.then(undefined, onRejected);
    }
  });
  OwnPromiseCapability = function () {
    var promise = new Internal();
    var state = getInternalState(promise);
    this.promise = promise;
    this.resolve = bind(internalResolve, state);
    this.reject = bind(internalReject, state);
  };
  newPromiseCapabilityModule.f = newPromiseCapability = function (C) {
    return C === PromiseConstructor || C === PromiseWrapper
      ? new OwnPromiseCapability(C)
      : newGenericPromiseCapability(C);
  };

  if (!IS_PURE && typeof NativePromise == 'function') {
    nativeThen = NativePromise.prototype.then;

    // wrap native Promise#then for native async functions
    redefine(NativePromise.prototype, 'then', function then(onFulfilled, onRejected) {
      var that = this;
      return new PromiseConstructor(function (resolve, reject) {
        nativeThen.call(that, resolve, reject);
      }).then(onFulfilled, onRejected);
    // https://github.com/zloirock/core-js/issues/640
    }, { unsafe: true });

    // wrap fetch result
    if (typeof $fetch == 'function') $({ global: true, enumerable: true, forced: true }, {
      // eslint-disable-next-line no-unused-vars -- required for `.length`
      fetch: function fetch(input /* , init */) {
        return promiseResolve(PromiseConstructor, $fetch.apply(global, arguments));
      }
    });
  }
}

$({ global: true, wrap: true, forced: FORCED }, {
  Promise: PromiseConstructor
});

setToStringTag(PromiseConstructor, PROMISE, false, true);
setSpecies(PROMISE);

PromiseWrapper = getBuiltIn(PROMISE);

// statics
$({ target: PROMISE, stat: true, forced: FORCED }, {
  // `Promise.reject` method
  // https://tc39.es/ecma262/#sec-promise.reject
  reject: function reject(r) {
    var capability = newPromiseCapability(this);
    capability.reject.call(undefined, r);
    return capability.promise;
  }
});

$({ target: PROMISE, stat: true, forced: IS_PURE || FORCED }, {
  // `Promise.resolve` method
  // https://tc39.es/ecma262/#sec-promise.resolve
  resolve: function resolve(x) {
    return promiseResolve(IS_PURE && this === PromiseWrapper ? PromiseConstructor : this, x);
  }
});

$({ target: PROMISE, stat: true, forced: INCORRECT_ITERATION }, {
  // `Promise.all` method
  // https://tc39.es/ecma262/#sec-promise.all
  all: function all(iterable) {
    var C = this;
    var capability = newPromiseCapability(C);
    var resolve = capability.resolve;
    var reject = capability.reject;
    var result = perform(function () {
      var $promiseResolve = aFunction(C.resolve);
      var values = [];
      var counter = 0;
      var remaining = 1;
      iterate(iterable, function (promise) {
        var index = counter++;
        var alreadyCalled = false;
        values.push(undefined);
        remaining++;
        $promiseResolve.call(C, promise).then(function (value) {
          if (alreadyCalled) return;
          alreadyCalled = true;
          values[index] = value;
          --remaining || resolve(values);
        }, reject);
      });
      --remaining || resolve(values);
    });
    if (result.error) reject(result.value);
    return capability.promise;
  },
  // `Promise.race` method
  // https://tc39.es/ecma262/#sec-promise.race
  race: function race(iterable) {
    var C = this;
    var capability = newPromiseCapability(C);
    var reject = capability.reject;
    var result = perform(function () {
      var $promiseResolve = aFunction(C.resolve);
      iterate(iterable, function (promise) {
        $promiseResolve.call(C, promise).then(capability.resolve, reject);
      });
    });
    if (result.error) reject(result.value);
    return capability.promise;
  }
});


/***/ }),

/***/ "e6ed":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const {isKeyword} = __webpack_require__("d1e7");
const graphTypes = __webpack_require__("c92f");
const types = __webpack_require__("68fa");
const util = __webpack_require__("89ae");
const url = __webpack_require__("ddd7");
const JsonLdError = __webpack_require__("5950");
const {
  createNodeMap: _createNodeMap,
  mergeNodeMapGraphs: _mergeNodeMapGraphs
} = __webpack_require__("796c");

const api = {};
module.exports = api;

/**
 * Performs JSON-LD `merged` framing.
 *
 * @param input the expanded JSON-LD to frame.
 * @param frame the expanded JSON-LD frame to use.
 * @param options the framing options.
 *
 * @return the framed output.
 */
api.frameMergedOrDefault = (input, frame, options) => {
  // create framing state
  const state = {
    options,
    embedded: false,
    graph: '@default',
    graphMap: {'@default': {}},
    subjectStack: [],
    link: {},
    bnodeMap: {}
  };

  // produce a map of all graphs and name each bnode
  // FIXME: currently uses subjects from @merged graph only
  const issuer = new util.IdentifierIssuer('_:b');
  _createNodeMap(input, state.graphMap, '@default', issuer);
  if(options.merged) {
    state.graphMap['@merged'] = _mergeNodeMapGraphs(state.graphMap);
    state.graph = '@merged';
  }
  state.subjects = state.graphMap[state.graph];

  // frame the subjects
  const framed = [];
  api.frame(state, Object.keys(state.subjects).sort(), frame, framed);

  // If pruning blank nodes, find those to prune
  if(options.pruneBlankNodeIdentifiers) {
    // remove all blank nodes appearing only once, done in compaction
    options.bnodesToClear =
      Object.keys(state.bnodeMap).filter(id => state.bnodeMap[id].length === 1);
  }

  // remove @preserve from results
  options.link = {};
  return _cleanupPreserve(framed, options);
};

/**
 * Frames subjects according to the given frame.
 *
 * @param state the current framing state.
 * @param subjects the subjects to filter.
 * @param frame the frame.
 * @param parent the parent subject or top-level array.
 * @param property the parent property, initialized to null.
 */
api.frame = (state, subjects, frame, parent, property = null) => {
  // validate the frame
  _validateFrame(frame);
  frame = frame[0];

  // get flags for current frame
  const options = state.options;
  const flags = {
    embed: _getFrameFlag(frame, options, 'embed'),
    explicit: _getFrameFlag(frame, options, 'explicit'),
    requireAll: _getFrameFlag(frame, options, 'requireAll')
  };

  // get link for current graph
  if(!state.link.hasOwnProperty(state.graph)) {
    state.link[state.graph] = {};
  }
  const link = state.link[state.graph];

  // filter out subjects that match the frame
  const matches = _filterSubjects(state, subjects, frame, flags);

  // add matches to output
  const ids = Object.keys(matches).sort();
  for(const id of ids) {
    const subject = matches[id];

    /* Note: In order to treat each top-level match as a compartmentalized
    result, clear the unique embedded subjects map when the property is null,
    which only occurs at the top-level. */
    if(property === null) {
      state.uniqueEmbeds = {[state.graph]: {}};
    } else {
      state.uniqueEmbeds[state.graph] = state.uniqueEmbeds[state.graph] || {};
    }

    if(flags.embed === '@link' && id in link) {
      // TODO: may want to also match an existing linked subject against
      // the current frame ... so different frames could produce different
      // subjects that are only shared in-memory when the frames are the same

      // add existing linked subject
      _addFrameOutput(parent, property, link[id]);
      continue;
    }

    // start output for subject
    const output = {'@id': id};
    if(id.indexOf('_:') === 0) {
      util.addValue(state.bnodeMap, id, output, {propertyIsArray: true});
    }
    link[id] = output;

    // validate @embed
    if((flags.embed === '@first' || flags.embed === '@last') && state.is11) {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; invalid value of @embed.',
        'jsonld.SyntaxError', {code: 'invalid @embed value', frame});
    }

    if(!state.embedded && state.uniqueEmbeds[state.graph].hasOwnProperty(id)) {
      // skip adding this node object to the top level, as it was
      // already included in another node object
      continue;
    }

    // if embed is @never or if a circular reference would be created by an
    // embed, the subject cannot be embedded, just add the reference;
    // note that a circular reference won't occur when the embed flag is
    // `@link` as the above check will short-circuit before reaching this point
    if(state.embedded &&
      (flags.embed === '@never' ||
      _createsCircularReference(subject, state.graph, state.subjectStack))) {
      _addFrameOutput(parent, property, output);
      continue;
    }

    // if only the first (or once) should be embedded
    if(state.embedded &&
       (flags.embed == '@first' || flags.embed == '@once') &&
       state.uniqueEmbeds[state.graph].hasOwnProperty(id)) {
      _addFrameOutput(parent, property, output);
      continue;
    }

    // if only the last match should be embedded
    if(flags.embed === '@last') {
      // remove any existing embed
      if(id in state.uniqueEmbeds[state.graph]) {
        _removeEmbed(state, id);
      }
    }

    state.uniqueEmbeds[state.graph][id] = {parent, property};

    // push matching subject onto stack to enable circular embed checks
    state.subjectStack.push({subject, graph: state.graph});

    // subject is also the name of a graph
    if(id in state.graphMap) {
      let recurse = false;
      let subframe = null;
      if(!('@graph' in frame)) {
        recurse = state.graph !== '@merged';
        subframe = {};
      } else {
        subframe = frame['@graph'][0];
        recurse = !(id === '@merged' || id === '@default');
        if(!types.isObject(subframe)) {
          subframe = {};
        }
      }

      if(recurse) {
        // recurse into graph
        api.frame(
          {...state, graph: id, embedded: false},
          Object.keys(state.graphMap[id]).sort(), [subframe], output, '@graph');
      }
    }

    // if frame has @included, recurse over its sub-frame
    if('@included' in frame) {
      api.frame(
        {...state, embedded: false},
        subjects, frame['@included'], output, '@included');
    }

    // iterate over subject properties
    for(const prop of Object.keys(subject).sort()) {
      // copy keywords to output
      if(isKeyword(prop)) {
        output[prop] = util.clone(subject[prop]);

        if(prop === '@type') {
          // count bnode values of @type
          for(const type of subject['@type']) {
            if(type.indexOf('_:') === 0) {
              util.addValue(
                state.bnodeMap, type, output, {propertyIsArray: true});
            }
          }
        }
        continue;
      }

      // explicit is on and property isn't in the frame, skip processing
      if(flags.explicit && !(prop in frame)) {
        continue;
      }

      // add objects
      for(const o of subject[prop]) {
        const subframe = (prop in frame ?
          frame[prop] : _createImplicitFrame(flags));

        // recurse into list
        if(graphTypes.isList(o)) {
          const subframe =
            (frame[prop] && frame[prop][0] && frame[prop][0]['@list']) ?
              frame[prop][0]['@list'] :
              _createImplicitFrame(flags);

          // add empty list
          const list = {'@list': []};
          _addFrameOutput(output, prop, list);

          // add list objects
          const src = o['@list'];
          for(const oo of src) {
            if(graphTypes.isSubjectReference(oo)) {
              // recurse into subject reference
              api.frame(
                {...state, embedded: true},
                [oo['@id']], subframe, list, '@list');
            } else {
              // include other values automatically
              _addFrameOutput(list, '@list', util.clone(oo));
            }
          }
        } else if(graphTypes.isSubjectReference(o)) {
          // recurse into subject reference
          api.frame(
            {...state, embedded: true},
            [o['@id']], subframe, output, prop);
        } else if(_valueMatch(subframe[0], o)) {
          // include other values, if they match
          _addFrameOutput(output, prop, util.clone(o));
        }
      }
    }

    // handle defaults
    for(const prop of Object.keys(frame).sort()) {
      // skip keywords
      if(prop === '@type') {
        if(!types.isObject(frame[prop][0]) ||
           !('@default' in frame[prop][0])) {
          continue;
        }
        // allow through default types
      } else if(isKeyword(prop)) {
        continue;
      }

      // if omit default is off, then include default values for properties
      // that appear in the next frame but are not in the matching subject
      const next = frame[prop][0] || {};
      const omitDefaultOn = _getFrameFlag(next, options, 'omitDefault');
      if(!omitDefaultOn && !(prop in output)) {
        let preserve = '@null';
        if('@default' in next) {
          preserve = util.clone(next['@default']);
        }
        if(!types.isArray(preserve)) {
          preserve = [preserve];
        }
        output[prop] = [{'@preserve': preserve}];
      }
    }

    // if embed reverse values by finding nodes having this subject as a value
    // of the associated property
    for(const reverseProp of Object.keys(frame['@reverse'] || {}).sort()) {
      const subframe = frame['@reverse'][reverseProp];
      for(const subject of Object.keys(state.subjects)) {
        const nodeValues =
          util.getValues(state.subjects[subject], reverseProp);
        if(nodeValues.some(v => v['@id'] === id)) {
          // node has property referencing this subject, recurse
          output['@reverse'] = output['@reverse'] || {};
          util.addValue(
            output['@reverse'], reverseProp, [], {propertyIsArray: true});
          api.frame(
            {...state, embedded: true},
            [subject], subframe, output['@reverse'][reverseProp],
            property);
        }
      }
    }

    // add output to parent
    _addFrameOutput(parent, property, output);

    // pop matching subject from circular ref-checking stack
    state.subjectStack.pop();
  }
};

/**
 * Replace `@null` with `null`, removing it from arrays.
 *
 * @param input the framed, compacted output.
 * @param options the framing options used.
 *
 * @return the resulting output.
 */
api.cleanupNull = (input, options) => {
  // recurse through arrays
  if(types.isArray(input)) {
    const noNulls = input.map(v => api.cleanupNull(v, options));
    return noNulls.filter(v => v); // removes nulls from array
  }

  if(input === '@null') {
    return null;
  }

  if(types.isObject(input)) {
    // handle in-memory linked nodes
    if('@id' in input) {
      const id = input['@id'];
      if(options.link.hasOwnProperty(id)) {
        const idx = options.link[id].indexOf(input);
        if(idx !== -1) {
          // already visited
          return options.link[id][idx];
        }
        // prevent circular visitation
        options.link[id].push(input);
      } else {
        // prevent circular visitation
        options.link[id] = [input];
      }
    }

    for(const key in input) {
      input[key] = api.cleanupNull(input[key], options);
    }
  }
  return input;
};

/**
 * Creates an implicit frame when recursing through subject matches. If
 * a frame doesn't have an explicit frame for a particular property, then
 * a wildcard child frame will be created that uses the same flags that the
 * parent frame used.
 *
 * @param flags the current framing flags.
 *
 * @return the implicit frame.
 */
function _createImplicitFrame(flags) {
  const frame = {};
  for(const key in flags) {
    if(flags[key] !== undefined) {
      frame['@' + key] = [flags[key]];
    }
  }
  return [frame];
}

/**
 * Checks the current subject stack to see if embedding the given subject
 * would cause a circular reference.
 *
 * @param subjectToEmbed the subject to embed.
 * @param graph the graph the subject to embed is in.
 * @param subjectStack the current stack of subjects.
 *
 * @return true if a circular reference would be created, false if not.
 */
function _createsCircularReference(subjectToEmbed, graph, subjectStack) {
  for(let i = subjectStack.length - 1; i >= 0; --i) {
    const subject = subjectStack[i];
    if(subject.graph === graph &&
      subject.subject['@id'] === subjectToEmbed['@id']) {
      return true;
    }
  }
  return false;
}

/**
 * Gets the frame flag value for the given flag name.
 *
 * @param frame the frame.
 * @param options the framing options.
 * @param name the flag name.
 *
 * @return the flag value.
 */
function _getFrameFlag(frame, options, name) {
  const flag = '@' + name;
  let rval = (flag in frame ? frame[flag][0] : options[name]);
  if(name === 'embed') {
    // default is "@last"
    // backwards-compatibility support for "embed" maps:
    // true => "@last"
    // false => "@never"
    if(rval === true) {
      rval = '@once';
    } else if(rval === false) {
      rval = '@never';
    } else if(rval !== '@always' && rval !== '@never' && rval !== '@link' &&
      rval !== '@first' && rval !== '@last' && rval !== '@once') {
      throw new JsonLdError(
        'Invalid JSON-LD syntax; invalid value of @embed.',
        'jsonld.SyntaxError', {code: 'invalid @embed value', frame});
    }
  }
  return rval;
}

/**
 * Validates a JSON-LD frame, throwing an exception if the frame is invalid.
 *
 * @param frame the frame to validate.
 */
function _validateFrame(frame) {
  if(!types.isArray(frame) || frame.length !== 1 || !types.isObject(frame[0])) {
    throw new JsonLdError(
      'Invalid JSON-LD syntax; a JSON-LD frame must be a single object.',
      'jsonld.SyntaxError', {frame});
  }

  if('@id' in frame[0]) {
    for(const id of util.asArray(frame[0]['@id'])) {
      // @id must be wildcard or an IRI
      if(!(types.isObject(id) || url.isAbsolute(id)) ||
        (types.isString(id) && id.indexOf('_:') === 0)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; invalid @id in frame.',
          'jsonld.SyntaxError', {code: 'invalid frame', frame});
      }
    }
  }

  if('@type' in frame[0]) {
    for(const type of util.asArray(frame[0]['@type'])) {
      // @id must be wildcard or an IRI
      if(!(types.isObject(type) || url.isAbsolute(type)) ||
        (types.isString(type) && type.indexOf('_:') === 0)) {
        throw new JsonLdError(
          'Invalid JSON-LD syntax; invalid @type in frame.',
          'jsonld.SyntaxError', {code: 'invalid frame', frame});
      }
    }
  }
}

/**
 * Returns a map of all of the subjects that match a parsed frame.
 *
 * @param state the current framing state.
 * @param subjects the set of subjects to filter.
 * @param frame the parsed frame.
 * @param flags the frame flags.
 *
 * @return all of the matched subjects.
 */
function _filterSubjects(state, subjects, frame, flags) {
  // filter subjects in @id order
  const rval = {};
  for(const id of subjects) {
    const subject = state.graphMap[state.graph][id];
    if(_filterSubject(state, subject, frame, flags)) {
      rval[id] = subject;
    }
  }
  return rval;
}

/**
 * Returns true if the given subject matches the given frame.
 *
 * Matches either based on explicit type inclusion where the node has any
 * type listed in the frame. If the frame has empty types defined matches
 * nodes not having a @type. If the frame has a type of {} defined matches
 * nodes having any type defined.
 *
 * Otherwise, does duck typing, where the node must have all of the
 * properties defined in the frame.
 *
 * @param state the current framing state.
 * @param subject the subject to check.
 * @param frame the frame to check.
 * @param flags the frame flags.
 *
 * @return true if the subject matches, false if not.
 */
function _filterSubject(state, subject, frame, flags) {
  // check ducktype
  let wildcard = true;
  let matchesSome = false;

  for(const key in frame) {
    let matchThis = false;
    const nodeValues = util.getValues(subject, key);
    const isEmpty = util.getValues(frame, key).length === 0;

    if(key === '@id') {
      // match on no @id or any matching @id, including wildcard
      if(types.isEmptyObject(frame['@id'][0] || {})) {
        matchThis = true;
      } else if(frame['@id'].length >= 0) {
        matchThis = frame['@id'].includes(nodeValues[0]);
      }
      if(!flags.requireAll) {
        return matchThis;
      }
    } else if(key === '@type') {
      // check @type (object value means 'any' type,
      // fall through to ducktyping)
      wildcard = false;
      if(isEmpty) {
        if(nodeValues.length > 0) {
          // don't match on no @type
          return false;
        }
        matchThis = true;
      } else if(frame['@type'].length === 1 &&
        types.isEmptyObject(frame['@type'][0])) {
        // match on wildcard @type if there is a type
        matchThis = nodeValues.length > 0;
      } else {
        // match on a specific @type
        for(const type of frame['@type']) {
          if(types.isObject(type) && '@default' in type) {
            // match on default object
            matchThis = true;
          } else {
            matchThis = matchThis || nodeValues.some(tt => tt === type);
          }
        }
      }
      if(!flags.requireAll) {
        return matchThis;
      }
    } else if(isKeyword(key)) {
      continue;
    } else {
      // Force a copy of this frame entry so it can be manipulated
      const thisFrame = util.getValues(frame, key)[0];
      let hasDefault = false;
      if(thisFrame) {
        _validateFrame([thisFrame]);
        hasDefault = '@default' in thisFrame;
      }

      // no longer a wildcard pattern if frame has any non-keyword properties
      wildcard = false;

      // skip, but allow match if node has no value for property, and frame has
      // a default value
      if(nodeValues.length === 0 && hasDefault) {
        continue;
      }

      // if frame value is empty, don't match if subject has any value
      if(nodeValues.length > 0 && isEmpty) {
        return false;
      }

      if(thisFrame === undefined) {
        // node does not match if values is not empty and the value of property
        // in frame is match none.
        if(nodeValues.length > 0) {
          return false;
        }
        matchThis = true;
      } else {
        if(graphTypes.isList(thisFrame)) {
          const listValue = thisFrame['@list'][0];
          if(graphTypes.isList(nodeValues[0])) {
            const nodeListValues = nodeValues[0]['@list'];

            if(graphTypes.isValue(listValue)) {
              // match on any matching value
              matchThis = nodeListValues.some(lv => _valueMatch(listValue, lv));
            } else if(graphTypes.isSubject(listValue) ||
              graphTypes.isSubjectReference(listValue)) {
              matchThis = nodeListValues.some(lv => _nodeMatch(
                state, listValue, lv, flags));
            }
          }
        } else if(graphTypes.isValue(thisFrame)) {
          matchThis = nodeValues.some(nv => _valueMatch(thisFrame, nv));
        } else if(graphTypes.isSubjectReference(thisFrame)) {
          matchThis =
            nodeValues.some(nv => _nodeMatch(state, thisFrame, nv, flags));
        } else if(types.isObject(thisFrame)) {
          matchThis = nodeValues.length > 0;
        } else {
          matchThis = false;
        }
      }
    }

    // all non-defaulted values must match if requireAll is set
    if(!matchThis && flags.requireAll) {
      return false;
    }

    matchesSome = matchesSome || matchThis;
  }

  // return true if wildcard or subject matches some properties
  return wildcard || matchesSome;
}

/**
 * Removes an existing embed.
 *
 * @param state the current framing state.
 * @param id the @id of the embed to remove.
 */
function _removeEmbed(state, id) {
  // get existing embed
  const embeds = state.uniqueEmbeds[state.graph];
  const embed = embeds[id];
  const parent = embed.parent;
  const property = embed.property;

  // create reference to replace embed
  const subject = {'@id': id};

  // remove existing embed
  if(types.isArray(parent)) {
    // replace subject with reference
    for(let i = 0; i < parent.length; ++i) {
      if(util.compareValues(parent[i], subject)) {
        parent[i] = subject;
        break;
      }
    }
  } else {
    // replace subject with reference
    const useArray = types.isArray(parent[property]);
    util.removeValue(parent, property, subject, {propertyIsArray: useArray});
    util.addValue(parent, property, subject, {propertyIsArray: useArray});
  }

  // recursively remove dependent dangling embeds
  const removeDependents = id => {
    // get embed keys as a separate array to enable deleting keys in map
    const ids = Object.keys(embeds);
    for(const next of ids) {
      if(next in embeds && types.isObject(embeds[next].parent) &&
        embeds[next].parent['@id'] === id) {
        delete embeds[next];
        removeDependents(next);
      }
    }
  };
  removeDependents(id);
}

/**
 * Removes the @preserve keywords from expanded result of framing.
 *
 * @param input the framed, framed output.
 * @param options the framing options used.
 *
 * @return the resulting output.
 */
function _cleanupPreserve(input, options) {
  // recurse through arrays
  if(types.isArray(input)) {
    return input.map(value => _cleanupPreserve(value, options));
  }

  if(types.isObject(input)) {
    // remove @preserve
    if('@preserve' in input) {
      return input['@preserve'][0];
    }

    // skip @values
    if(graphTypes.isValue(input)) {
      return input;
    }

    // recurse through @lists
    if(graphTypes.isList(input)) {
      input['@list'] = _cleanupPreserve(input['@list'], options);
      return input;
    }

    // handle in-memory linked nodes
    if('@id' in input) {
      const id = input['@id'];
      if(options.link.hasOwnProperty(id)) {
        const idx = options.link[id].indexOf(input);
        if(idx !== -1) {
          // already visited
          return options.link[id][idx];
        }
        // prevent circular visitation
        options.link[id].push(input);
      } else {
        // prevent circular visitation
        options.link[id] = [input];
      }
    }

    // recurse through properties
    for(const prop in input) {
      // potentially remove the id, if it is an unreference bnode
      if(prop === '@id' && options.bnodesToClear.includes(input[prop])) {
        delete input['@id'];
        continue;
      }

      input[prop] = _cleanupPreserve(input[prop], options);
    }
  }
  return input;
}

/**
 * Adds framing output to the given parent.
 *
 * @param parent the parent to add to.
 * @param property the parent property.
 * @param output the output to add.
 */
function _addFrameOutput(parent, property, output) {
  if(types.isObject(parent)) {
    util.addValue(parent, property, output, {propertyIsArray: true});
  } else {
    parent.push(output);
  }
}

/**
 * Node matches if it is a node, and matches the pattern as a frame.
 *
 * @param state the current framing state.
 * @param pattern used to match value
 * @param value to check
 * @param flags the frame flags.
 */
function _nodeMatch(state, pattern, value, flags) {
  if(!('@id' in value)) {
    return false;
  }
  const nodeObject = state.subjects[value['@id']];
  return nodeObject && _filterSubject(state, nodeObject, pattern, flags);
}

/**
 * Value matches if it is a value and matches the value pattern
 *
 * * `pattern` is empty
 * * @values are the same, or `pattern[@value]` is a wildcard, and
 * * @types are the same or `value[@type]` is not null
 *   and `pattern[@type]` is `{}`, or `value[@type]` is null
 *   and `pattern[@type]` is null or `[]`, and
 * * @languages are the same or `value[@language]` is not null
 *   and `pattern[@language]` is `{}`, or `value[@language]` is null
 *   and `pattern[@language]` is null or `[]`.
 *
 * @param pattern used to match value
 * @param value to check
 */
function _valueMatch(pattern, value) {
  const v1 = value['@value'];
  const t1 = value['@type'];
  const l1 = value['@language'];
  const v2 = pattern['@value'] ?
    (types.isArray(pattern['@value']) ?
      pattern['@value'] : [pattern['@value']]) :
    [];
  const t2 = pattern['@type'] ?
    (types.isArray(pattern['@type']) ?
      pattern['@type'] : [pattern['@type']]) :
    [];
  const l2 = pattern['@language'] ?
    (types.isArray(pattern['@language']) ?
      pattern['@language'] : [pattern['@language']]) :
    [];

  if(v2.length === 0 && t2.length === 0 && l2.length === 0) {
    return true;
  }
  if(!(v2.includes(v1) || types.isEmptyObject(v2[0]))) {
    return false;
  }
  if(!(!t1 && t2.length === 0 || t2.includes(t1) || t1 &&
    types.isEmptyObject(t2[0]))) {
    return false;
  }
  if(!(!l1 && l2.length === 0 || l2.includes(l1) || l1 &&
    types.isEmptyObject(l2[0]))) {
    return false;
  }
  return true;
}


/***/ }),

/***/ "e893":
/***/ (function(module, exports, __webpack_require__) {

var has = __webpack_require__("5135");
var ownKeys = __webpack_require__("56ef");
var getOwnPropertyDescriptorModule = __webpack_require__("06cf");
var definePropertyModule = __webpack_require__("9bf2");

module.exports = function (target, source) {
  var keys = ownKeys(source);
  var defineProperty = definePropertyModule.f;
  var getOwnPropertyDescriptor = getOwnPropertyDescriptorModule.f;
  for (var i = 0; i < keys.length; i++) {
    var key = keys[i];
    if (!has(target, key)) defineProperty(target, key, getOwnPropertyDescriptor(source, key));
  }
};


/***/ }),

/***/ "e8b5":
/***/ (function(module, exports, __webpack_require__) {

var classof = __webpack_require__("c6b6");

// `IsArray` abstract operation
// https://tc39.es/ecma262/#sec-isarray
// eslint-disable-next-line es/no-array-isarray -- safe
module.exports = Array.isArray || function isArray(arg) {
  return classof(arg) == 'Array';
};


/***/ }),

/***/ "e95a":
/***/ (function(module, exports, __webpack_require__) {

var wellKnownSymbol = __webpack_require__("b622");
var Iterators = __webpack_require__("3f8c");

var ITERATOR = wellKnownSymbol('iterator');
var ArrayPrototype = Array.prototype;

// check on default Array iterator
module.exports = function (it) {
  return it !== undefined && (Iterators.Array === it || ArrayPrototype[ITERATOR] === it);
};


/***/ }),

/***/ "ee00":
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin

/***/ }),

/***/ "ee17":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2021 Digital Bazaar, Inc. All rights reserved.
 */


const xhrLoader = __webpack_require__("7156");

const api = {};
module.exports = api;

/**
 * Setup browser document loaders.
 *
 * @param jsonld the jsonld api.
 */
api.setupDocumentLoaders = function(jsonld) {
  if(typeof XMLHttpRequest !== 'undefined') {
    jsonld.documentLoaders.xhr = xhrLoader;
    // use xhr document loader by default
    jsonld.useDocumentLoader('xhr');
  }
};

/**
 * Setup browser globals.
 *
 * @param jsonld the jsonld api.
 */
api.setupGlobals = function(jsonld) {
  // setup browser global JsonLdProcessor
  if(typeof globalThis.JsonLdProcessor === 'undefined') {
    Object.defineProperty(globalThis, 'JsonLdProcessor', {
      writable: true,
      enumerable: false,
      configurable: true,
      value: jsonld.JsonLdProcessor
    });
  }
};


/***/ }),

/***/ "ee2b":
/***/ (function(module, exports) {

// This alphabet uses a-z A-Z 0-9 _- symbols.
// Symbols are generated for smaller size.
// -_zyxwvutsrqponmlkjihgfedcba9876543210ZYXWVUTSRQPONMLKJIHGFEDCBA
var url = '-_'
// Loop from 36 to 0 (from z to a and 9 to 0 in Base36).
var i = 36
while (i--) {
  // 36 is radix. Number.prototype.toString(36) returns number
  // in Base36 representation. Base36 is like hex, but it uses 0–9 and a-z.
  url += i.toString(36)
}
// Loop from 36 to 10 (from Z to A in Base36).
i = 36
while (i-- - 10) {
  url += i.toString(36).toUpperCase()
}

/**
 * Generate URL-friendly unique ID. This method use non-secure predictable
 * random generator with bigger collision probability.
 *
 * @param {number} [size=21] The number of symbols in ID.
 *
 * @return {string} Random string.
 *
 * @example
 * const nanoid = require('nanoid/non-secure')
 * model.id = nanoid() //=> "Uakgb_J5m9g-0JDMbcJqL"
 *
 * @name nonSecure
 * @function
 */
module.exports = function (size) {
  var id = ''
  i = size || 21
  // Compact alternative for `for (var i = 0; i < size; i++)`
  while (i--) {
    // `| 0` is compact and faster alternative for `Math.floor()`
    id += url[Math.random() * 64 | 0]
  }
  return id
}


/***/ }),

/***/ "f069":
/***/ (function(module, exports, __webpack_require__) {

"use strict";

var aFunction = __webpack_require__("1c0b");

var PromiseCapability = function (C) {
  var resolve, reject;
  this.promise = new C(function ($$resolve, $$reject) {
    if (resolve !== undefined || reject !== undefined) throw TypeError('Bad Promise constructor');
    resolve = $$resolve;
    reject = $$reject;
  });
  this.resolve = aFunction(resolve);
  this.reject = aFunction(reject);
};

// 25.4.1.5 NewPromiseCapability(C)
module.exports.f = function (C) {
  return new PromiseCapability(C);
};


/***/ }),

/***/ "f32c":
/***/ (function(module, exports, __webpack_require__) {

"use strict";


// A linked list to keep track of recently-used-ness
const Yallist = __webpack_require__("9fe3")

const MAX = Symbol('max')
const LENGTH = Symbol('length')
const LENGTH_CALCULATOR = Symbol('lengthCalculator')
const ALLOW_STALE = Symbol('allowStale')
const MAX_AGE = Symbol('maxAge')
const DISPOSE = Symbol('dispose')
const NO_DISPOSE_ON_SET = Symbol('noDisposeOnSet')
const LRU_LIST = Symbol('lruList')
const CACHE = Symbol('cache')
const UPDATE_AGE_ON_GET = Symbol('updateAgeOnGet')

const naiveLength = () => 1

// lruList is a yallist where the head is the youngest
// item, and the tail is the oldest.  the list contains the Hit
// objects as the entries.
// Each Hit object has a reference to its Yallist.Node.  This
// never changes.
//
// cache is a Map (or PseudoMap) that matches the keys to
// the Yallist.Node object.
class LRUCache {
  constructor (options) {
    if (typeof options === 'number')
      options = { max: options }

    if (!options)
      options = {}

    if (options.max && (typeof options.max !== 'number' || options.max < 0))
      throw new TypeError('max must be a non-negative number')
    // Kind of weird to have a default max of Infinity, but oh well.
    const max = this[MAX] = options.max || Infinity

    const lc = options.length || naiveLength
    this[LENGTH_CALCULATOR] = (typeof lc !== 'function') ? naiveLength : lc
    this[ALLOW_STALE] = options.stale || false
    if (options.maxAge && typeof options.maxAge !== 'number')
      throw new TypeError('maxAge must be a number')
    this[MAX_AGE] = options.maxAge || 0
    this[DISPOSE] = options.dispose
    this[NO_DISPOSE_ON_SET] = options.noDisposeOnSet || false
    this[UPDATE_AGE_ON_GET] = options.updateAgeOnGet || false
    this.reset()
  }

  // resize the cache when the max changes.
  set max (mL) {
    if (typeof mL !== 'number' || mL < 0)
      throw new TypeError('max must be a non-negative number')

    this[MAX] = mL || Infinity
    trim(this)
  }
  get max () {
    return this[MAX]
  }

  set allowStale (allowStale) {
    this[ALLOW_STALE] = !!allowStale
  }
  get allowStale () {
    return this[ALLOW_STALE]
  }

  set maxAge (mA) {
    if (typeof mA !== 'number')
      throw new TypeError('maxAge must be a non-negative number')

    this[MAX_AGE] = mA
    trim(this)
  }
  get maxAge () {
    return this[MAX_AGE]
  }

  // resize the cache when the lengthCalculator changes.
  set lengthCalculator (lC) {
    if (typeof lC !== 'function')
      lC = naiveLength

    if (lC !== this[LENGTH_CALCULATOR]) {
      this[LENGTH_CALCULATOR] = lC
      this[LENGTH] = 0
      this[LRU_LIST].forEach(hit => {
        hit.length = this[LENGTH_CALCULATOR](hit.value, hit.key)
        this[LENGTH] += hit.length
      })
    }
    trim(this)
  }
  get lengthCalculator () { return this[LENGTH_CALCULATOR] }

  get length () { return this[LENGTH] }
  get itemCount () { return this[LRU_LIST].length }

  rforEach (fn, thisp) {
    thisp = thisp || this
    for (let walker = this[LRU_LIST].tail; walker !== null;) {
      const prev = walker.prev
      forEachStep(this, fn, walker, thisp)
      walker = prev
    }
  }

  forEach (fn, thisp) {
    thisp = thisp || this
    for (let walker = this[LRU_LIST].head; walker !== null;) {
      const next = walker.next
      forEachStep(this, fn, walker, thisp)
      walker = next
    }
  }

  keys () {
    return this[LRU_LIST].toArray().map(k => k.key)
  }

  values () {
    return this[LRU_LIST].toArray().map(k => k.value)
  }

  reset () {
    if (this[DISPOSE] &&
        this[LRU_LIST] &&
        this[LRU_LIST].length) {
      this[LRU_LIST].forEach(hit => this[DISPOSE](hit.key, hit.value))
    }

    this[CACHE] = new Map() // hash of items by key
    this[LRU_LIST] = new Yallist() // list of items in order of use recency
    this[LENGTH] = 0 // length of items in the list
  }

  dump () {
    return this[LRU_LIST].map(hit =>
      isStale(this, hit) ? false : {
        k: hit.key,
        v: hit.value,
        e: hit.now + (hit.maxAge || 0)
      }).toArray().filter(h => h)
  }

  dumpLru () {
    return this[LRU_LIST]
  }

  set (key, value, maxAge) {
    maxAge = maxAge || this[MAX_AGE]

    if (maxAge && typeof maxAge !== 'number')
      throw new TypeError('maxAge must be a number')

    const now = maxAge ? Date.now() : 0
    const len = this[LENGTH_CALCULATOR](value, key)

    if (this[CACHE].has(key)) {
      if (len > this[MAX]) {
        del(this, this[CACHE].get(key))
        return false
      }

      const node = this[CACHE].get(key)
      const item = node.value

      // dispose of the old one before overwriting
      // split out into 2 ifs for better coverage tracking
      if (this[DISPOSE]) {
        if (!this[NO_DISPOSE_ON_SET])
          this[DISPOSE](key, item.value)
      }

      item.now = now
      item.maxAge = maxAge
      item.value = value
      this[LENGTH] += len - item.length
      item.length = len
      this.get(key)
      trim(this)
      return true
    }

    const hit = new Entry(key, value, len, now, maxAge)

    // oversized objects fall out of cache automatically.
    if (hit.length > this[MAX]) {
      if (this[DISPOSE])
        this[DISPOSE](key, value)

      return false
    }

    this[LENGTH] += hit.length
    this[LRU_LIST].unshift(hit)
    this[CACHE].set(key, this[LRU_LIST].head)
    trim(this)
    return true
  }

  has (key) {
    if (!this[CACHE].has(key)) return false
    const hit = this[CACHE].get(key).value
    return !isStale(this, hit)
  }

  get (key) {
    return get(this, key, true)
  }

  peek (key) {
    return get(this, key, false)
  }

  pop () {
    const node = this[LRU_LIST].tail
    if (!node)
      return null

    del(this, node)
    return node.value
  }

  del (key) {
    del(this, this[CACHE].get(key))
  }

  load (arr) {
    // reset the cache
    this.reset()

    const now = Date.now()
    // A previous serialized cache has the most recent items first
    for (let l = arr.length - 1; l >= 0; l--) {
      const hit = arr[l]
      const expiresAt = hit.e || 0
      if (expiresAt === 0)
        // the item was created without expiration in a non aged cache
        this.set(hit.k, hit.v)
      else {
        const maxAge = expiresAt - now
        // dont add already expired items
        if (maxAge > 0) {
          this.set(hit.k, hit.v, maxAge)
        }
      }
    }
  }

  prune () {
    this[CACHE].forEach((value, key) => get(this, key, false))
  }
}

const get = (self, key, doUse) => {
  const node = self[CACHE].get(key)
  if (node) {
    const hit = node.value
    if (isStale(self, hit)) {
      del(self, node)
      if (!self[ALLOW_STALE])
        return undefined
    } else {
      if (doUse) {
        if (self[UPDATE_AGE_ON_GET])
          node.value.now = Date.now()
        self[LRU_LIST].unshiftNode(node)
      }
    }
    return hit.value
  }
}

const isStale = (self, hit) => {
  if (!hit || (!hit.maxAge && !self[MAX_AGE]))
    return false

  const diff = Date.now() - hit.now
  return hit.maxAge ? diff > hit.maxAge
    : self[MAX_AGE] && (diff > self[MAX_AGE])
}

const trim = self => {
  if (self[LENGTH] > self[MAX]) {
    for (let walker = self[LRU_LIST].tail;
      self[LENGTH] > self[MAX] && walker !== null;) {
      // We know that we're about to delete this one, and also
      // what the next least recently used key will be, so just
      // go ahead and set it now.
      const prev = walker.prev
      del(self, walker)
      walker = prev
    }
  }
}

const del = (self, node) => {
  if (node) {
    const hit = node.value
    if (self[DISPOSE])
      self[DISPOSE](hit.key, hit.value)

    self[LENGTH] -= hit.length
    self[CACHE].delete(hit.key)
    self[LRU_LIST].removeNode(node)
  }
}

class Entry {
  constructor (key, value, length, now, maxAge) {
    this.key = key
    this.value = value
    this.length = length
    this.now = now
    this.maxAge = maxAge || 0
  }
}

const forEachStep = (self, fn, node, thisp) => {
  let hit = node.value
  if (isStale(self, hit)) {
    del(self, node)
    if (!self[ALLOW_STALE])
      hit = undefined
  }
  if (hit)
    fn.call(thisp, hit.value, hit.key, self)
}

module.exports = LRUCache


/***/ }),

/***/ "f5df":
/***/ (function(module, exports, __webpack_require__) {

var TO_STRING_TAG_SUPPORT = __webpack_require__("00ee");
var classofRaw = __webpack_require__("c6b6");
var wellKnownSymbol = __webpack_require__("b622");

var TO_STRING_TAG = wellKnownSymbol('toStringTag');
// ES3 wrong here
var CORRECT_ARGUMENTS = classofRaw(function () { return arguments; }()) == 'Arguments';

// fallback for IE11 Script Access Denied error
var tryGet = function (it, key) {
  try {
    return it[key];
  } catch (error) { /* empty */ }
};

// getting tag from ES6+ `Object.prototype.toString`
module.exports = TO_STRING_TAG_SUPPORT ? classofRaw : function (it) {
  var O, tag, result;
  return it === undefined ? 'Undefined' : it === null ? 'Null'
    // @@toStringTag case
    : typeof (tag = tryGet(O = Object(it), TO_STRING_TAG)) == 'string' ? tag
    // builtinTag case
    : CORRECT_ARGUMENTS ? classofRaw(O)
    // ES3 arguments fallback
    : (result = classofRaw(O)) == 'Object' && typeof O.callee == 'function' ? 'Arguments' : result;
};


/***/ }),

/***/ "f772":
/***/ (function(module, exports, __webpack_require__) {

var shared = __webpack_require__("5692");
var uid = __webpack_require__("90e3");

var keys = shared('keys');

module.exports = function (key) {
  return keys[key] || (keys[key] = uid(key));
};


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

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.array.includes.js
var es_array_includes = __webpack_require__("caad");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.string.includes.js
var es_string_includes = __webpack_require__("2532");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.object.keys.js
var es_object_keys = __webpack_require__("b64b");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.regexp.exec.js
var es_regexp_exec = __webpack_require__("ac1f");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.string.replace.js
var es_string_replace = __webpack_require__("5319");

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/MainComponent.vue?vue&type=template&id=2b50dfdf&
var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('div',{attrs:{"id":"clearStorage"}},[_c('FormulateInput',{attrs:{"type":"button","label":_vm.$t('message.dataupload.label.clear')},on:{"click":_vm.clearStorage}})],1),_c('router-view',{attrs:{"name":_vm.configurationName}})],1)}
var staticRenderFns = []


// CONCATENATED MODULE: ./src/components/MainComponent.vue?vue&type=template&id=2b50dfdf&

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.string.ends-with.js
var es_string_ends_with = __webpack_require__("8a79");

// EXTERNAL MODULE: ./node_modules/vuex/dist/vuex.esm.js
var vuex_esm = __webpack_require__("2f62");

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/MainComponent.vue?vue&type=script&lang=js&

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

/* harmony default export */ var MainComponentvue_type_script_lang_js_ = ({
  props: {
    configurationName: String
  },
  methods: {
    clearStorage: function clearStorage() {
      localStorage.clear(); // refresh page

      this.$router.go();
    }
  },
  computed: Object(vuex_esm["b" /* mapGetters */])(['getRoutes', 'getBaseUrl']),
  mounted: function mounted() {
    var currentPath = this.$route.path;

    if (currentPath.endsWith(this.getBaseUrl + this.configurationName)) {
      var firstRoute = this.getRoutes(this.configurationName)[0];
      this.$router.push({
        path: firstRoute
      });
    }
  }
});
// CONCATENATED MODULE: ./src/components/MainComponent.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_MainComponentvue_type_script_lang_js_ = (MainComponentvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/components/MainComponent.vue?vue&type=style&index=0&lang=css&
var MainComponentvue_type_style_index_0_lang_css_ = __webpack_require__("5870");

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

// CONCATENATED MODULE: ./src/components/MainComponent.vue






/* normalize component */

var component = normalizeComponent(
  components_MainComponentvue_type_script_lang_js_,
  render,
  staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var MainComponent = (component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/InfoSlot.vue?vue&type=template&id=2e79fddb&
var InfoSlotvue_type_template_id_2e79fddb_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('label',{attrs:{"for":_vm.context.id}},[_vm._v(" "+_vm._s(_vm.context.label)+" "),(_vm.info)?_c('a',{staticClass:"infoButton",on:{"click":function($event){return _vm.displayInfo()}}},[_c('i',{staticClass:"material-icons"},[_vm._v("info")])]):_vm._e()])}
var InfoSlotvue_type_template_id_2e79fddb_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/InfoSlot.vue?vue&type=template&id=2e79fddb&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/InfoSlot.vue?vue&type=script&lang=js&
//
//
//
//
//
//
//
/* harmony default export */ var InfoSlotvue_type_script_lang_js_ = ({
  props: {
    context: {
      type: Object,
      required: true
    },
    info: {
      type: [String, Boolean],
      default: false
    }
  },
  methods: {
    displayInfo: function displayInfo() {
      document.getElementById('info').innerHTML = '<p>' + this.info + '</p>';
    }
  }
});
// CONCATENATED MODULE: ./src/components/InfoSlot.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_InfoSlotvue_type_script_lang_js_ = (InfoSlotvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/components/InfoSlot.vue?vue&type=style&index=0&lang=css&
var InfoSlotvue_type_style_index_0_lang_css_ = __webpack_require__("610b");

// CONCATENATED MODULE: ./src/components/InfoSlot.vue






/* normalize component */

var InfoSlot_component = normalizeComponent(
  components_InfoSlotvue_type_script_lang_js_,
  InfoSlotvue_type_template_id_2e79fddb_render,
  InfoSlotvue_type_template_id_2e79fddb_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var InfoSlot = (InfoSlot_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/SearchableSelect.vue?vue&type=template&id=0bfc112a&
var SearchableSelectvue_type_template_id_0bfc112a_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',_vm._g({class:("formulate-input-element formulate-input-element--" + (_vm.context.type)),attrs:{"data-type":_vm.context.type}},_vm.$listeners),[_c('FormulateSlot',{attrs:{"name":"prefix","context":_vm.context}},[(_vm.context.slotComponents.prefix)?_c(_vm.context.slotComponents.prefix,{tag:"component",attrs:{"context":_vm.context}}):_vm._e()],1),_c('v-select',_vm._b({attrs:{"type":_vm.context.type,"options":_vm.options,"reduce":function (label) { return label.code; },"label":"label"},on:{"blur":_vm.context.blurHandler,"input":function($event){return _vm.context.rootEmit('change')}},model:{value:(_vm.context.model),callback:function ($$v) {_vm.$set(_vm.context, "model", $$v)},expression:"context.model"}},'v-select',_vm.context.attributes,false)),_c('FormulateSlot',{attrs:{"name":"suffix","context":_vm.context}},[(_vm.context.slotComponents.suffix)?_c(_vm.context.slotComponents.suffix,{tag:"component",attrs:{"context":_vm.context}}):_vm._e()],1)],1)}
var SearchableSelectvue_type_template_id_0bfc112a_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/SearchableSelect.vue?vue&type=template&id=0bfc112a&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/SearchableSelect.vue?vue&type=script&lang=js&
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
//
//
//
//
/* harmony default export */ var SearchableSelectvue_type_script_lang_js_ = ({
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  computed: {
    model: function model() {
      return this.context.model;
    },
    options: function options() {
      var valueArray = [];

      for (var index in this.context.options) {
        var option = {
          label: this.context.options[index].label,
          code: this.context.options[index].value
        };
        valueArray.push(option);
      }

      return valueArray;
    }
  }
});
// CONCATENATED MODULE: ./src/components/SearchableSelect.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_SearchableSelectvue_type_script_lang_js_ = (SearchableSelectvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/components/SearchableSelect.vue?vue&type=style&index=0&lang=scss&
var SearchableSelectvue_type_style_index_0_lang_scss_ = __webpack_require__("21d0");

// CONCATENATED MODULE: ./src/components/SearchableSelect.vue






/* normalize component */

var SearchableSelect_component = normalizeComponent(
  components_SearchableSelectvue_type_script_lang_js_,
  SearchableSelectvue_type_template_id_0bfc112a_render,
  SearchableSelectvue_type_template_id_0bfc112a_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var SearchableSelect = (SearchableSelect_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/ConditionalInput.vue?vue&type=template&id=4b89f776&
var ConditionalInputvue_type_template_id_4b89f776_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',_vm._g({class:("formulate-input-element formulate-input-element--" + (_vm.context.type)),attrs:{"data-type":_vm.context.type}},_vm.$listeners),[_c('FormulateSlot',{attrs:{"name":"prefix","context":_vm.context}},[(_vm.context.slotComponents.prefix)?_c(_vm.context.slotComponents.prefix,{tag:"component",attrs:{"context":_vm.context}}):_vm._e()],1),_c('input',{directives:[{name:"model",rawName:"v-model",value:(_vm.context.model),expression:"context.model"}],attrs:{"type":"text","hidden":""},domProps:{"value":(_vm.context.model)},on:{"blur":_vm.context.blurHandler,"input":function($event){if($event.target.composing){ return; }_vm.$set(_vm.context, "model", $event.target.value)}}}),_c('FormulateInput',{attrs:{"type":"select","options":_vm.context.options,"name":_vm.context.name,"label":_vm.$t('message.dataupload.label.type'),"ignored":""},model:{value:(_vm.conditionalValues),callback:function ($$v) {_vm.conditionalValues=$$v},expression:"conditionalValues"}}),(_vm.conditionalValues)?_c('FormulateSchema',{attrs:{"schema":_vm.data[_vm.conditionalValues]},on:{"change":function($event){_vm.context.model = _vm.inputValues}},model:{value:(_vm.inputValues),callback:function ($$v) {_vm.inputValues=$$v},expression:"inputValues"}}):_vm._e(),_c('FormulateSlot',{attrs:{"name":"suffix","context":_vm.context}},[(_vm.context.slotComponents.suffix)?_c(_vm.context.slotComponents.suffix,{tag:"component",attrs:{"context":_vm.context}}):_vm._e()],1)],1)}
var ConditionalInputvue_type_template_id_4b89f776_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/ConditionalInput.vue?vue&type=template&id=4b89f776&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/ConditionalInput.vue?vue&type=script&lang=js&
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
/* harmony default export */ var ConditionalInputvue_type_script_lang_js_ = ({
  props: {
    context: {
      type: Object,
      required: true
    },
    data: {}
  },
  data: function data() {
    return {
      conditionalValues: {},
      inputValues: {}
    };
  }
});
// CONCATENATED MODULE: ./src/components/ConditionalInput.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_ConditionalInputvue_type_script_lang_js_ = (ConditionalInputvue_type_script_lang_js_); 
// CONCATENATED MODULE: ./src/components/ConditionalInput.vue





/* normalize component */

var ConditionalInput_component = normalizeComponent(
  components_ConditionalInputvue_type_script_lang_js_,
  ConditionalInputvue_type_template_id_4b89f776_render,
  ConditionalInputvue_type_template_id_4b89f776_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var ConditionalInput = (ConditionalInput_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/DataFetchingComponent.vue?vue&type=template&id=60b088fa&
var DataFetchingComponentvue_type_template_id_60b088fa_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div')}
var DataFetchingComponentvue_type_template_id_60b088fa_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/DataFetchingComponent.vue?vue&type=template&id=60b088fa&

// EXTERNAL MODULE: ./src/utils/converter/convertToInput.js
var convertToInput = __webpack_require__("5342");

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/DataFetchingComponent.vue?vue&type=script&lang=js&
//
//
//
//

/* harmony default export */ var DataFetchingComponentvue_type_script_lang_js_ = ({
  props: ['id', 'profile'],
  methods: {
    convertToInput: convertToInput["a" /* convertToInput */]
  },
  created: function created() {
    this.convertToInput(this.profile, this.id);
    var firstRoute = this.$store.getters.getRoutes(this.profile)[0];
    this.$router.push({
      path: firstRoute
    });
  }
});
// CONCATENATED MODULE: ./src/components/DataFetchingComponent.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_DataFetchingComponentvue_type_script_lang_js_ = (DataFetchingComponentvue_type_script_lang_js_); 
// CONCATENATED MODULE: ./src/components/DataFetchingComponent.vue





/* normalize component */

var DataFetchingComponent_component = normalizeComponent(
  components_DataFetchingComponentvue_type_script_lang_js_,
  DataFetchingComponentvue_type_template_id_60b088fa_render,
  DataFetchingComponentvue_type_template_id_60b088fa_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var DataFetchingComponent = (DataFetchingComponent_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/views/Wrapper.vue?vue&type=template&id=009d2f7c&
var Wrappervue_type_template_id_009d2f7c_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{attrs:{"id":"input"}},[_c('router-view',{attrs:{"configName":_vm.name}})],1)}
var Wrappervue_type_template_id_009d2f7c_staticRenderFns = []


// CONCATENATED MODULE: ./src/views/Wrapper.vue?vue&type=template&id=009d2f7c&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/views/Wrapper.vue?vue&type=script&lang=js&
//
//
//
//
//
//
/* harmony default export */ var Wrappervue_type_script_lang_js_ = ({
  props: ['name']
});
// CONCATENATED MODULE: ./src/views/Wrapper.vue?vue&type=script&lang=js&
 /* harmony default export */ var views_Wrappervue_type_script_lang_js_ = (Wrappervue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/views/Wrapper.vue?vue&type=style&index=0&lang=scss&
var Wrappervue_type_style_index_0_lang_scss_ = __webpack_require__("c112");

// CONCATENATED MODULE: ./src/views/Wrapper.vue






/* normalize component */

var Wrapper_component = normalizeComponent(
  views_Wrappervue_type_script_lang_js_,
  Wrappervue_type_template_id_009d2f7c_render,
  Wrappervue_type_template_id_009d2f7c_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var Wrapper = (Wrapper_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/views/Input.vue?vue&type=template&id=c07f24f0&
var Inputvue_type_template_id_c07f24f0_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('div',[_c('StepProgress',{attrs:{"steps":_vm.getRouteNames(_vm.configName),"current-step":_vm.routesArray.indexOf(this.$route.path),"line-thickeness":5}})],1),(_vm.subRoute)?_c('div',[(_vm.subRoute === 'overview')?_c('div',[_c('OverviewPage',{attrs:{"configName":_vm.configName,"mainRoute":_vm.mainRoute,"subRoute":_vm.subRoute,"routesArray":_vm.routesArray}})],1):_c('div',[_c('DataInput',{attrs:{"configName":_vm.configName,"mainRoute":_vm.mainRoute,"subRoute":_vm.subRoute,"routesArray":_vm.routesArray}})],1)]):_c('div',[(_vm.mainRoute === 'overview')?_c('div',[_c('OverviewPage',{attrs:{"configName":_vm.configName,"mainRoute":_vm.mainRoute,"subRoute":_vm.subRoute,"routesArray":_vm.routesArray}})],1):_c('div',[_c('DataInput',{attrs:{"configName":_vm.configName,"mainRoute":_vm.mainRoute,"subRoute":_vm.subRoute,"routesArray":_vm.routesArray}})],1)])])}
var Inputvue_type_template_id_c07f24f0_staticRenderFns = []


// CONCATENATED MODULE: ./src/views/Input.vue?vue&type=template&id=c07f24f0&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/OverviewPage.vue?vue&type=template&id=8fc48a92&
var OverviewPagevue_type_template_id_8fc48a92_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',[_c('h3',[_vm._v("Overview")]),_c('div',_vm._l((Object.keys(_vm.values)),function(profile){return _c('div',{key:profile},[_c('table',[_c('tr',[_c('th',[_vm._v(_vm._s(profile))]),_c('th',[_vm._v("Values")])]),_vm._l((Object.keys(_vm.values[profile])),function(currentValue){return _c('tr',{key:currentValue},[_c('td',[_vm._v(_vm._s(currentValue))]),_c('td',[_vm._v(_vm._s(_vm.values[currentValue]))])])})],2)])}),0),_c('div',{staticClass:"input_subpage_nav"},[_c('router-link',{attrs:{"to":_vm.getPath('previous')}},[_c('FormulateInput',{attrs:{"type":"button","label":_vm.$t('message.dataupload.label.preview'),"disabled":true ? _vm.routesArray.indexOf(this.$route.path) === 0 : undefined}})],1),_c('FormulateInput',{attrs:{"type":"button","label":_vm.$t('message.dataupload.label.next')},on:{"click":_vm.goToNext}})],1)])}
var OverviewPagevue_type_template_id_8fc48a92_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/OverviewPage.vue?vue&type=template&id=8fc48a92&

// CONCATENATED MODULE: ./src/utils/helpers/routing_helpers.js
/**
 * @description Function returning path to previous or next route depending on given direction.
 * @param {String} direction Either 'previous' or 'next'
 * @returns Path
 */
function getPath(direction) {
  var currentPath = this.$route.path;
  var goalPath = "";
  var currentRouteIndex = this.routesArray.indexOf(currentPath);

  if (direction === 'previous') {
    if (currentRouteIndex !== 0) {
      goalPath = this.routesArray[currentRouteIndex - 1];
    } // if the current path is already the first one no change is needed
    else {
        goalPath = currentPath;
      }
  } else if (direction === 'next') {
    if (currentRouteIndex !== this.routesArray.length - 1) {
      goalPath = this.routesArray[currentRouteIndex + 1];
    } // if the current path is already the last one no change is needed
    else {
        goalPath = currentPath;
      }
  }

  return goalPath;
}
// EXTERNAL MODULE: ./node_modules/@babel/runtime/helpers/esm/asyncToGenerator.js
var asyncToGenerator = __webpack_require__("1da1");

// EXTERNAL MODULE: ./node_modules/regenerator-runtime/runtime.js
var runtime = __webpack_require__("96cf");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.symbol.js
var es_symbol = __webpack_require__("a4d3");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.array.filter.js
var es_array_filter = __webpack_require__("4de4");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.object.get-own-property-descriptor.js
var es_object_get_own_property_descriptor = __webpack_require__("e439");

// EXTERNAL MODULE: ./node_modules/core-js/modules/web.dom-collections.for-each.js
var web_dom_collections_for_each = __webpack_require__("159b");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.object.get-own-property-descriptors.js
var es_object_get_own_property_descriptors = __webpack_require__("dbb4");

// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/defineProperty.js
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
// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/objectSpread2.js








function ownKeys(object, enumerableOnly) {
  var keys = Object.keys(object);

  if (Object.getOwnPropertySymbols) {
    var symbols = Object.getOwnPropertySymbols(object);
    if (enumerableOnly) symbols = symbols.filter(function (sym) {
      return Object.getOwnPropertyDescriptor(object, sym).enumerable;
    });
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
// EXTERNAL MODULE: ./node_modules/core-js/modules/es.string.starts-with.js
var es_string_starts_with = __webpack_require__("2ca0");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.object.assign.js
var es_object_assign = __webpack_require__("cca6");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.object.to-string.js
var es_object_to_string = __webpack_require__("d3b7");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.regexp.to-string.js
var es_regexp_to_string = __webpack_require__("25f0");

// EXTERNAL MODULE: ./src/utils/converter/context.json
var context = __webpack_require__("ca53");

// EXTERNAL MODULE: ./node_modules/jsonld/lib/jsonld.js
var lib_jsonld = __webpack_require__("46bd");

// CONCATENATED MODULE: ./src/utils/converter/convertToJsonld.js












/**
 * @description Extracts values from localStorage depending on their names
 * @param {String} profile Name of profile which is included within the value name
 * @returns Array containing multiple objects containing the input values
 */

function getLocalstorageValues(profile) {
  var values = [];

  for (var index in Object.keys(localStorage)) {
    var variableName = Object.keys(localStorage)[index];

    if (variableName.startsWith("inputValues_" + profile)) {
      values.push(JSON.parse(localStorage.getItem(variableName)));
    }
  }

  return values;
}
/**
 * @description Restructures extracted values from localStorage into objects
 * @param {String} profile Profile name determining which values to restruture
 * @returns Object containing the restructured values of the given profile
 */


function restructureData(profile) {
  var raw_data = getLocalstorageValues(profile);
  var values = {};
  var nested_values = [];

  for (var index in raw_data) {
    var current_object = raw_data[index]; // all input values of each page are saved into an object with only one key

    var object_key = Object.keys(current_object)[0];

    if (object_key.startsWith(profile)) {
      var value_array = current_object[object_key]; // dataset or catalogue are profiles with only one instance

      if (value_array.length <= 1) {
        var data = current_object[object_key][0];
        values = Object.assign(values, data);
      }
    } // the dataset profile also includes input values from the distribution profile
    else if (profile === "dataset") {
        if (object_key.startsWith("distribution")) {
          var distribution_array = current_object[object_key];

          for (var distribution_index in distribution_array) {
            // if there no object at the given index within the nested_values array -> create one and fill it with data
            if (typeof nested_values[distribution_index] === 'undefined') {
              nested_values[distribution_index] = {};
              var current_distribution_data = distribution_array[distribution_index];
              nested_values[distribution_index] = Object.assign(nested_values[distribution_index], current_distribution_data);
            } else {
              current_distribution_data = distribution_array[distribution_index];
              nested_values[distribution_index] = Object.assign(nested_values[distribution_index], current_distribution_data);
            }
          }
        }
      }
  }

  if (profile === "catalogue") {
    return {
      catalogue: values
    };
  } else if (profile === "dataset") {
    return {
      dataset: values,
      distribution: nested_values
    };
  }
}
/**
 * @description Add metadata for linked data to value objects
 * @param {String} profile Name of profile to add metadata to
 * @returns Object or Array containing the enriched data values
 */


function addMetadata(profile) {
  var data = restructureData(profile);

  if (profile === "catalogue") {
    var metadata = {
      "@id": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 20),
      "@type": "dcat:Catalogue"
    };

    var catalogueJsonld = _objectSpread2(_objectSpread2(_objectSpread2({}, context), metadata), data[profile]);

    return catalogueJsonld;
  } else if (profile === "dataset") {
    var datasetJsonld = []; // setting ids for all distributions

    var distribution_ids = [];

    for (var id_index in data["distribution"]) {
      distribution_ids[id_index] = Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 20);
    }

    var dataset_metadata = {
      "@id": Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 20),
      "@type": "dcat:Dataset",
      "dct:distribution": distribution_ids
    };

    var dataset_object = _objectSpread2(_objectSpread2(_objectSpread2({}, context), dataset_metadata), data[profile]);

    datasetJsonld.push(dataset_object);

    for (var dist_index in data["distribution"]) {
      var distribution_metadata = {
        "@id": String(distribution_ids[dist_index]),
        "@type": "dcat:Distribution"
      };

      var distribution_object = _objectSpread2(_objectSpread2(_objectSpread2({}, context), distribution_metadata), data["distribution"][dist_index]);

      datasetJsonld.push(distribution_object);
    }

    return datasetJsonld;
  }
}
/**
 * @description Converts given input values to JSON-LD
 * @param {String} profile Name of profile to convert
 * @returns Object containing converted data in JSON-LD format
 */


function convertToJsonld(_x) {
  return _convertToJsonld.apply(this, arguments);
}

function _convertToJsonld() {
  _convertToJsonld = Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee(profile) {
    var convertedJSONLD;
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            _context.next = 2;
            return lib_jsonld["compact"](addMetadata(profile), context);

          case 2:
            convertedJSONLD = _context.sent;
            return _context.abrupt("return", convertedJSONLD);

          case 4:
          case "end":
            return _context.stop();
        }
      }
    }, _callee);
  }));
  return _convertToJsonld.apply(this, arguments);
}
// CONCATENATED MODULE: ./src/utils/helpers/submit_helpers.js


// import {jsonldConverter} from '../jsonldConverter'

/**
 * @description Function which saves all values of current route into parameter which name contains current route into localStorage
 */

function saveChange() {
  var routeValueName = 'inputValues_' + this.configName + '_' + this.mainRoute;

  if (this.subRoute) {
    routeValueName = routeValueName + '_' + this.subRoute;
  }

  localStorage.setItem(routeValueName, JSON.stringify(this.formValues));
}
/**
 * @description Shows error message on validation fail of current input form
 */

function showValidationFields() {
  return "There are some fields missing of the input data doesn't match the wanted type!";
}
/**
 * @description Function triggered on submit (if validation succeeded). If current route isn't the last one
 * forwarding to next route is happening. Otherwise saved values from localStorage will be queried and given to json-ld-converter.
 */

function goToNext() {
  return _goToNext.apply(this, arguments);
}

function _goToNext() {
  _goToNext = Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee() {
    var currentPath, jsonld;
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            currentPath = this.$route.path;

            if (!(currentPath === this.getPath('next'))) {
              _context.next = 9;
              break;
            }

            _context.next = 4;
            return convertToJsonld(this.configName);

          case 4:
            jsonld = _context.sent;
            _context.next = 7;
            return this.$store.dispatch('uploadData', {
              values: jsonld
            });

          case 7:
            _context.next = 10;
            break;

          case 9:
            this.$router.push(this.getPath('next'));

          case 10:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, this);
  }));
  return _goToNext.apply(this, arguments);
}
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/OverviewPage.vue?vue&type=script&lang=js&
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

 // import {collectProfileData} from '../utils/jsonldConverter'

/* harmony default export */ var OverviewPagevue_type_script_lang_js_ = ({
  data: function data() {
    return {
      values: {}
    };
  },
  props: {
    configName: String,
    mainRoute: String,
    subRoute: String,
    routesArray: Array
  },
  methods: {
    getPath: getPath,
    goToNext: goToNext // getAllValues(){
    //   var rawData = [];
    //   var profiles = this.configName === 'datasets' 
    //     ? ["dataset", "distribution"]
    //     : ["catalogue"];
    //   var localStorageKeys = Object.keys(localStorage);
    //   for(var keyIndex in localStorageKeys){
    //     if(localStorageKeys[keyIndex].startsWith("inputValues_")){
    //       rawData.push(JSON.parse(localStorage.getItem(localStorageKeys[keyIndex])));
    //     }
    //   }
    //   for(var profileIndex in profiles){
    //     var profileData = collectProfileData(rawData, profiles[profileIndex]);
    //     if(Object.keys(profileData).length > 0){
    //       this.values[profiles[profileIndex]] = profileData;
    //     }
    //   }
    // }

  } // created(){
  //   this.getAllValues();
  // }

});
// CONCATENATED MODULE: ./src/components/OverviewPage.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_OverviewPagevue_type_script_lang_js_ = (OverviewPagevue_type_script_lang_js_); 
// CONCATENATED MODULE: ./src/components/OverviewPage.vue





/* normalize component */

var OverviewPage_component = normalizeComponent(
  components_OverviewPagevue_type_script_lang_js_,
  OverviewPagevue_type_template_id_8fc48a92_render,
  OverviewPagevue_type_template_id_8fc48a92_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var OverviewPage = (OverviewPage_component.exports);
// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"2310031e-vue-loader-template"}!./node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/DataInput.vue?vue&type=template&id=2a8868a3&
var DataInputvue_type_template_id_2a8868a3_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"inputContainer"},[_c('div',{staticClass:"formContainer"},[_c('FormulateForm',{attrs:{"schema":[_vm.getInputFields({main: _vm.configName, sub: _vm.mainRoute, subsub: _vm.subRoute})],"invalid-message":_vm.showValidationFields},on:{"change":_vm.saveChange,"submit":_vm.goToNext},model:{value:(_vm.formValues),callback:function ($$v) {_vm.formValues=$$v},expression:"formValues"}},[_c('div',{staticClass:"input_subpage_nav"},[_c('router-link',{attrs:{"to":_vm.getPath('previous')}},[_c('FormulateInput',{attrs:{"type":"button","label":_vm.$t('message.dataupload.label.preview'),"disabled":true ? _vm.routesArray.indexOf(this.$route.path) === 0 : undefined}})],1),_c('FormulateInput',{attrs:{"type":"submit","label":_vm.$t('message.dataupload.label.next')}})],1)])],1),_c('div',{staticClass:"infoContainer"},[_c('h5',[_c('i',{staticClass:"material-icons"},[_vm._v("info")]),_vm._v(_vm._s(_vm.$t('message.dataupload.info.information')))]),_c('div',{attrs:{"id":"info"}},[_vm._v(_vm._s(_vm.$t('message.dataupload.info.defaultdescription')))])])])}
var DataInputvue_type_template_id_2a8868a3_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/DataInput.vue?vue&type=template&id=2a8868a3&

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/components/DataInput.vue?vue&type=script&lang=js&

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



/* harmony default export */ var DataInputvue_type_script_lang_js_ = ({
  data: function data() {
    return {
      formValues: {}
    };
  },
  props: {
    configName: String,
    mainRoute: String,
    subRoute: String,
    routesArray: Array
  },
  methods: _objectSpread2(_objectSpread2({}, Object(vuex_esm["a" /* mapActions */])(['getValues'])), {}, {
    getPath: getPath,
    saveChange: saveChange,
    showValidationFields: showValidationFields,
    goToNext: goToNext
  }),
  computed: Object(vuex_esm["b" /* mapGetters */])(['getInputFields']),
  created: function created() {
    // triggers fetching of all values belonging to an input with a source-property
    this.getValues(this.configName);
  },
  mounted: function mounted() {
    // triggers reading values from localStorage on reload (auto fill in of values into input form)
    var valueName = 'inputValues_' + this.configName + '_' + this.mainRoute;

    if (this.subRoute) {
      valueName = valueName + "_" + this.subRoute;
    }

    if (localStorage.getItem(valueName)) {
      this.formValues = JSON.parse(localStorage.getItem(valueName));
    }
  },
  watch: {
    // triggers reading values from localStorage on route change withour reload (auto fill in of values into input form)
    $route: function $route(to, from) {
      var valueName = 'inputValues_' + this.configName + '_' + this.mainRoute;

      if (this.subRoute) {
        valueName = valueName + '_' + this.subRoute;
      }

      if (localStorage.getItem(valueName)) {
        this.formValues = JSON.parse(localStorage.getItem(valueName));
      }

      to;
      from;
    }
  }
});
// CONCATENATED MODULE: ./src/components/DataInput.vue?vue&type=script&lang=js&
 /* harmony default export */ var components_DataInputvue_type_script_lang_js_ = (DataInputvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/components/DataInput.vue?vue&type=style&index=0&lang=css&
var DataInputvue_type_style_index_0_lang_css_ = __webpack_require__("aed6");

// CONCATENATED MODULE: ./src/components/DataInput.vue






/* normalize component */

var DataInput_component = normalizeComponent(
  components_DataInputvue_type_script_lang_js_,
  DataInputvue_type_template_id_2a8868a3_render,
  DataInputvue_type_template_id_2a8868a3_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var DataInput = (DataInput_component.exports);
// EXTERNAL MODULE: ./node_modules/vue-step-progress/dist/vue-step-progress.min.js
var vue_step_progress_min = __webpack_require__("46e0");
var vue_step_progress_min_default = /*#__PURE__*/__webpack_require__.n(vue_step_progress_min);

// EXTERNAL MODULE: ./node_modules/vue-step-progress/dist/main.css
var main = __webpack_require__("c91a");

// CONCATENATED MODULE: ./node_modules/cache-loader/dist/cjs.js??ref--12-0!./node_modules/thread-loader/dist/cjs.js!./node_modules/babel-loader/lib!./node_modules/cache-loader/dist/cjs.js??ref--0-0!./node_modules/vue-loader/lib??vue-loader-options!./src/views/Input.vue?vue&type=script&lang=js&
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





/* harmony default export */ var Inputvue_type_script_lang_js_ = ({
  name: "Input",
  components: {
    OverviewPage: OverviewPage,
    DataInput: DataInput,
    StepProgress: vue_step_progress_min_default.a
  },
  props: ['mainRoute', 'configName', 'subRoute'],
  computed: Object(vuex_esm["b" /* mapGetters */])(['getRoutes', 'getRouteNames']),
  created: function created() {
    // fetches an ordered array of all available routes 
    this.routesArray = this.getRoutes(this.configName);
  }
});
// CONCATENATED MODULE: ./src/views/Input.vue?vue&type=script&lang=js&
 /* harmony default export */ var views_Inputvue_type_script_lang_js_ = (Inputvue_type_script_lang_js_); 
// EXTERNAL MODULE: ./src/views/Input.vue?vue&type=style&index=0&lang=css&
var Inputvue_type_style_index_0_lang_css_ = __webpack_require__("b3dd");

// CONCATENATED MODULE: ./src/views/Input.vue






/* normalize component */

var Input_component = normalizeComponent(
  views_Inputvue_type_script_lang_js_,
  Inputvue_type_template_id_c07f24f0_render,
  Inputvue_type_template_id_c07f24f0_staticRenderFns,
  false,
  null,
  null,
  null
  
)

/* harmony default export */ var Input = (Input_component.exports);
// EXTERNAL MODULE: ./node_modules/vue-select/dist/vue-select.js
var vue_select = __webpack_require__("4a7a");
var vue_select_default = /*#__PURE__*/__webpack_require__.n(vue_select);

// EXTERNAL MODULE: ./node_modules/is-url/index.js
var is_url = __webpack_require__("92c9");
var is_url_default = /*#__PURE__*/__webpack_require__.n(is_url);

// EXTERNAL MODULE: ./node_modules/nanoid/non-secure/index.js
var non_secure = __webpack_require__("ee2b");
var non_secure_default = /*#__PURE__*/__webpack_require__.n(non_secure);

// CONCATENATED MODULE: ./node_modules/@braid/vue-formulate/node_modules/is-plain-object/index.es.js
/*!
 * isobject <https://github.com/jonschlinkert/isobject>
 *
 * Copyright (c) 2014-2017, Jon Schlinkert.
 * Released under the MIT License.
 */

function isObject(val) {
  return val != null && typeof val === 'object' && Array.isArray(val) === false;
}

/*!
 * is-plain-object <https://github.com/jonschlinkert/is-plain-object>
 *
 * Copyright (c) 2014-2017, Jon Schlinkert.
 * Released under the MIT License.
 */

function isObjectObject(o) {
  return isObject(o) === true
    && Object.prototype.toString.call(o) === '[object Object]';
}

function isPlainObject(o) {
  var ctor,prot;

  if (isObjectObject(o) === false) return false;

  // If has modified constructor
  ctor = o.constructor;
  if (typeof ctor !== 'function') return false;

  // If has modified prototype
  prot = ctor.prototype;
  if (isObjectObject(prot) === false) return false;

  // If constructor does not have an Object-specific method
  if (prot.hasOwnProperty('isPrototypeOf') === false) {
    return false;
  }

  // Most likely a plain Object
  return true;
}

/* harmony default export */ var index_es = (isPlainObject);

// CONCATENATED MODULE: ./node_modules/@braid/vue-formulate-i18n/dist/locales.esm.js
function locales_esm_e(e){return"string"==typeof e?e[0].toUpperCase()+e.substr(1):e}var locales_esm_r={accepted:function(e){return"من فضلك اقبل ال "+e.name},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" يجب أن يأتي بعد "+a[0]+".":locales_esm_e(n)+" يجب أن يكون تاريخ أحدث"},alpha:function(r){return locales_esm_e(r.name)+" يجب أن يحتوى على حروف أبجدية فقط."},alphanumeric:function(r){return locales_esm_e(r.name)+" يمكن أن يحتوي على حروف أبجدية أو أرقام فقط."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" يجب أن يكون قبل "+a[0]+".":locales_esm_e(n)+" يجب أن يكون تاريخ أقدم"},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" يجب أن يقع بين "+t[0]+" و "+t[1]+".":locales_esm_e(n)+" يجب ان يكون طوله بين "+t[0]+" و "+t[1]+" حرف."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" غير متطابق."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ليس على الصيغة الصحيحة, من فضلك استخدم هذه الصيغة "+a[0]:locales_esm_e(n)+" ليس على الصيغة الصحيحة."},default:function(e){e.name;return"هذه القيمة غير مناسبة."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” ليس عنوان بريد الكتروني.":"من فضلك أدخل عنوان بريد الكتروني مناسب."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” لا تنتهي بنهاية صحيحة.":"نهاية هذه القيمة ليست صحيحة."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” ليس "+n+" صحيح.":"هذه القيمة ليست "+n+" صحيح."},matches:function(r){return locales_esm_e(r.name)+" ليست قيمة مسموح بها."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"يمكنك فقط ان تختار "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" لا يمكن أن يتجاوز "+t[0]+".":locales_esm_e(n)+" لا يجب ان يزيد طوله عن "+t[0]+" حرف."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" يجب ان يكون من نوع "+(a[0]||"لا يسمح بأي نوع.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"يجب أن تختار على الأقل "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" يجب أن يكون أكبر من "+t[0]+".":locales_esm_e(n)+" يجب أن يكون طوله أكبر من "+t[0]+" حرف."},not:function(e){var r=e.name;return"“"+e.value+"” ليست قيمة مسموح بها ك"+r+"."},number:function(r){return locales_esm_e(r.name)+" يجب أن يكون رقم."},required:function(r){return locales_esm_e(r.name)+" ضروري."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” لا تبدأ بقيمة صحيحة.":"هذه القيمة لا تبدأ بقيمة صحيحة."},url:function(e){e.name;return"من فضلك أدخل رابط صحيح."}};function locales_esm_n(e){var n;e.extend({locales:(n={},n.ar=locales_esm_r,n)})}var a={accepted:function(e){return"Si us plau accepta els "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ha de ser després de "+a[0]+".":locales_esm_e(n)+" ha de ser una data posterior."},alpha:function(r){return locales_esm_e(r.name)+" només pot contenir lletres."},alphanumeric:function(r){return locales_esm_e(r.name)+" només pot contenir lletres i números."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ha de ser abans de "+a[0]+".":locales_esm_e(n)+" ha de ser una data anterior"},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ha d'estar entre "+t[0]+" i "+t[1]+".":locales_esm_e(n)+" ha de tenir entre "+t[0]+" i "+t[1]+" caràcters."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" no coincideix."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" no és una data vàlida, si us plau usi el format "+a[0]:locales_esm_e(n)+" no és una data vàlida."},default:function(e){e.name;return"Aquest camp no és vàlid."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” no és un correu electrònic vàlid.":"Si us plau introdueixi un correu electrònic vàlid."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” no acaba en un valor vàlid.":"Aquest camp no acaba en un valor vàlid."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” no és un "+n+" permès.":"Això no és un "+n+" permès."},matches:function(r){return locales_esm_e(r.name)+" no és un valor permès."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Només pots seleccionar "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ha de ser menor o igual que "+t[0]+".":locales_esm_e(n)+" ha de ser menor o igual que "+t[0]+" caràcters."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" ha de ser de tipus: "+(a[0]||"No es permet el format d'arxius.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Necessites almenys "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ha de contenir almenys "+t[0]+".":locales_esm_e(n)+" ha de contenir almenys "+t[0]+" caràcters."},not:function(e){var r=e.name;return"“"+e.value+"” no és un "+r+" permès."},number:function(r){return locales_esm_e(r.name)+" ha de ser un número."},required:function(r){return locales_esm_e(r.name)+" és requerit."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” no comença amb un valor vàlid.":"Aquest camp no comença amb un valor vàlid."},url:function(e){e.name;return"Si us plau introdueixi una url vàlida."}};function locales_esm_t(e){var r;e.extend({locales:(r={},r.ca=a,r)})}var locales_esm_i={accepted:function(e){return"Prosím potvrďte "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musí bý po "+a[0]+".":locales_esm_e(n)+" musí být pozdější datum."},alpha:function(r){return locales_esm_e(r.name)+" může obsahovat pouze písmena."},alphanumeric:function(r){return locales_esm_e(r.name)+" může obsahovat pouze písmena a čísla."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musí být před "+a[0]+".":locales_esm_e(n)+" musí být dřívější datum."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí být mezi "+t[0]+" a "+t[1]+".":locales_esm_e(n)+" délka musí být mezi "+t[0]+" a "+t[1]+" znaky."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" se neshoduje."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" není platné datum, použijte formát "+a[0]:locales_esm_e(n)+" není platné datum."},default:function(e){e.name;return"Toto pole není vyplěno správně."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” není platná e-mailová adresa.":"Zadejte platnou e-mailovou adresu."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nekončí správnou hodnotou.":"Toto pole nekončí správnou hodnotou."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” není povolená hodnota "+n+".":"Toto není povolená hodnota "+n+"."},matches:function(r){return locales_esm_e(r.name)+" není povolená hodnota."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Můžete vybrat pouze "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí být menší nebo rovno "+t[0]+".":locales_esm_e(n)+" musí být menší nebo rovno "+t[0]+" znaků."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" musí být typ: "+(a[0]||"Žádné typy souborů nejsou povolené.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Je potřeba nejméně "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí být nejméně "+t[0]+".":locales_esm_e(n)+" musí být nejméně "+t[0]+" znaků."},not:function(e){var r=e.name;return"“"+e.value+"” není povolená hodnota "+r+"."},number:function(r){return locales_esm_e(r.name)+" musí být číslo."},required:function(r){return"Pole "+locales_esm_e(r.name)+" je povinné."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nezačíná platnou hodnotou.":"Toto pole nezačíná platnou hodnotou."},url:function(e){e.name;return"Zadejte platnou URL adresu."}};function u(e){var r;e.extend({locales:(r={},r.cs=locales_esm_i,r)})}var locales_esm_o={accepted:function(e){return"Accepter venligst "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" skal være efter "+a[0]+".":locales_esm_e(n)+" skal være en senere dato."},alpha:function(r){return locales_esm_e(r.name)+" kan kun indeholde bogstaver."},alphanumeric:function(r){return locales_esm_e(r.name)+" kan kun indeholde bogstaver og tal."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" skal være før "+a[0]+".":locales_esm_e(n)+" skal være en tidligere dato."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" skal være mellem "+t[0]+" og "+t[1]+".":locales_esm_e(n)+" skal være mellem "+t[0]+" og "+t[1]+" tegn."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" matcher ikke."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" er ikke gyldig, brug venligst formatet "+a[0]:locales_esm_e(n)+" er ikke en gyldig dato."},default:function(e){e.name;return"Dette felt er ikke gyldigt."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” er ikke en gyldig email-adresse.":"Indtast venligst en gyldig email-adresse."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” slutter ikke med en gyldig værdi.":"Dette felt slutter ikke med en gyldig værdi."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” er ikke en tilladt "+n+".":"Dette er ikke en tilladt "+n+"."},matches:function(r){return locales_esm_e(r.name)+" er ikke en gyldig værdi."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du kan kun vælge "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" skal være mindre end eller lig "+t[0]+".":locales_esm_e(n)+" skal være mindre end eller lig "+t[0]+" tegn."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" skal være af typen: "+(a[0]||"Ingen tilladte filformater.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du skal vælge mindst "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" skal være mere end "+t[0]+".":locales_esm_e(n)+" skal være mere end "+t[0]+" tegn."},not:function(e){var r=e.name;return"“"+e.value+"” er ikke en gyldig "+r+"."},number:function(r){return locales_esm_e(r.name)+" skal være et tal."},required:function(r){return locales_esm_e(r.name)+" er påkrævet."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” starter ikke med en gyldig værdi.":"Dette felt starter ikke med en gyldig værdi."},url:function(e){e.name;return"Indtast venligst en gyldig URL."}};function locales_esm_s(e){var r;e.extend({locales:(r={},r.da=locales_esm_o,r)})}var l={accepted:function(e){return e.name+" erfordert Zustimmung."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" muss auf "+a[0]+" folgen.":locales_esm_e(n)+" muss ein späteres Datum sein."},alpha:function(r){return locales_esm_e(r.name)+" darf nur Buchstaben enthalten."},alphanumeric:function(r){return locales_esm_e(r.name)+" darf nur Buchstaben und Zahlen enthalten."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" muss vor "+a[0]+" sein.":locales_esm_e(n)+" muss ein früheres Datum sein."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" muss zwischen "+t[0]+" und "+t[1]+".":locales_esm_e(n)+" muss zwischen "+t[0]+" und "+t[1]+" Zeichen lang sein."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" stimmt nicht überein."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ist nicht korrekt, bitte das Format "+a[0]+" benutzen.":locales_esm_e(n)+" ist kein gültiges Datum."},default:function(e){e.name;return"Das Feld hat einen Fehler."},email:function(e){e.name;var r=e.value;return r?"„"+r+"“ ist keine gültige E-Mail-Adresse.":"Bitte eine gültige E-Mail-Adresse eingeben."},endsWith:function(e){e.name;var r=e.value;return r?"„"+r+"” endet nicht mit einem gültigen Wert.":"Dieses Feld endet nicht mit einem gültigen Wert"},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"„"+locales_esm_e(a)+"“ ist kein gültiger Wert für "+n+".":"Dies ist kein gültiger Wert für "+n+"."},matches:function(r){return locales_esm_e(r.name)+" ist kein gültiger Wert."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Es dürfen nur "+t[0]+" "+n+" ausgewählt werden.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" muss kleiner oder gleich "+t[0]+" sein.":locales_esm_e(n)+" muss "+t[0]+" oder weniger Zeichen lang sein."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" muss den Typ "+(a[0]||"Keine Dateien erlaubt")+" haben."},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Es müssen mindestens "+t[0]+" "+n+" ausgewählt werden.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" muss größer als "+t[0]+" sein.":locales_esm_e(n)+" muss "+t[0]+" oder mehr Zeichen lang sein."},not:function(e){var r=e.name;return"„"+e.value+"“ ist kein erlaubter Wert für "+r+"."},number:function(r){return locales_esm_e(r.name)+" muss eine Zahl sein."},required:function(r){return locales_esm_e(r.name)+" ist ein Pflichtfeld."},startsWith:function(e){e.name;var r=e.value;return r?"„"+r+"” beginnt nicht mit einem gültigen Wert":"Dieses Feld beginnt nicht mit einem gültigen Wert"},url:function(r){return locales_esm_e(r.name)+" muss eine gültige URL sein."}};function m(e){var r;e.extend({locales:(r={},r.de=l,r)})}var v={accepted:function(e){return"Please accept the "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" must be after "+a[0]+".":locales_esm_e(n)+" must be a later date."},alpha:function(r){return locales_esm_e(r.name)+" can only contain alphabetical characters."},alphanumeric:function(r){return locales_esm_e(r.name)+" can only contain letters and numbers."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" must be before "+a[0]+".":locales_esm_e(n)+" must be an earlier date."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" must be between "+t[0]+" and "+t[1]+".":locales_esm_e(n)+" must be between "+t[0]+" and "+t[1]+" characters long."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" does not match."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" is not a valid date, please use the format "+a[0]:locales_esm_e(n)+" is not a valid date."},default:function(e){e.name;return"This field isn’t valid."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” is not a valid email address.":"Please enter a valid email address."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” doesn’t end with a valid value.":"This field doesn’t end with a valid value."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” is not an allowed "+n+".":"This is not an allowed "+n+"."},matches:function(r){return locales_esm_e(r.name)+" is not an allowed value."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"You may only select "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" must be less than or equal to "+t[0]+".":locales_esm_e(n)+" must be less than or equal to "+t[0]+" characters long."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" must be of the type: "+(a[0]||"No file formats allowed.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"You need at least "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" must be at least "+t[0]+".":locales_esm_e(n)+" must be at least "+t[0]+" characters long."},not:function(e){var r=e.name;return"“"+e.value+"” is not an allowed "+r+"."},number:function(r){return locales_esm_e(r.name)+" must be a number."},required:function(r){return locales_esm_e(r.name)+" is required."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” doesn’t start with a valid value.":"This field doesn’t start with a valid value."},url:function(e){e.name;return"Please include a valid url."}};function c(e){var r;e.extend({locales:(r={},r.en=v,r)})}var f={accepted:function(e){return"Por favor acepta los "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" debe ser luego de "+a[0]+".":locales_esm_e(n)+" debe ser una fecha posterior."},alpha:function(r){return locales_esm_e(r.name)+" solo puede contener letras."},alphanumeric:function(r){return locales_esm_e(r.name)+" solo puede contener letras y números."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" debe ser antes de "+a[0]+".":locales_esm_e(n)+" debe ser una fecha anterior."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" debe estar entre "+t[0]+" y "+t[1]+".":locales_esm_e(n)+" debe tener entre "+t[0]+" y "+t[1]+" caracteres."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" no coincide."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" no es una fecha válida, por favor use el formato "+a[0]:locales_esm_e(n)+" no es una fecha válida."},default:function(e){e.name;return"Este campo no es válido."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” no es un correo electrónico válido.":"Por favor introduzca un correo electrónico válido."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” no termina en un valor válido.":"Este campo no termina en un valor válido."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” no es un "+n+" permitido.":"Esto no es un "+n+" permitido."},matches:function(r){return locales_esm_e(r.name)+" no es un valor permitido."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Solo puedes seleccionar "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" debe ser menor o igual que "+t[0]+".":locales_esm_e(n)+" debe ser menor o igual que "+t[0]+" caracteres."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" debe ser de tipo: "+(a[0]||"No se permite el formato de archivos.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Necesitas al menos "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" debe contener al menos "+t[0]+".":locales_esm_e(n)+" debe contener al menos "+t[0]+" caracteres."},not:function(e){var r=e.name;return"“"+e.value+"” no es un "+r+" permitido."},number:function(r){return locales_esm_e(r.name)+" debe ser un número."},required:function(r){return locales_esm_e(r.name)+" es requerido."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” no comienza con un valor válido.":"Este campo no comienza con un valor válido."},url:function(e){e.name;return"Por favor introduzca una url válida."}};function d(e){var r;e.extend({locales:(r={},r.es=f,r)})}var g={accepted:function(e){return"Merci d'accepter les "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" doit être postérieur à "+a[0]+".":locales_esm_e(n)+" doit être une date ultérieure."},alpha:function(r){return locales_esm_e(r.name)+" peut uniquement contenir des lettres."},alphanumeric:function(r){return locales_esm_e(r.name)+" peut uniquement contenir des lettres ou des chiffres"},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" doit être antérieur à "+a[0]+".":locales_esm_e(n)+" doit être une date antérieure."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" doit être compris entre "+t[0]+" et "+t[1]+".":locales_esm_e(n)+" doit être compris entre "+t[0]+" et "+t[1]+" caractères."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" ne correspond pas."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" n'est pas valide.  Merci d'utiliser le format "+a[0]:locales_esm_e(n)+" n'est pas une date valide."},default:function(e){e.name;return"Ce champ n'est pas valide."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” n'est pas une adresse email valide.":"Merci d'entrer une adresse email valide."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” ne termine pas par une valeur correcte.":"Ce champ ne termine pas par une valeur correcte."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” n'est pas un(e) "+n+" autorisé(e).":"Cette valeur n'est pas un(e) "+n+" autorisé(e)."},matches:function(r){return locales_esm_e(r.name)+" n'est pas une valeur autorisée."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Vous pouvez uniquement sélectionner "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" doit être inférieur ou égal à "+t[0]+".":locales_esm_e(n)+" doit être inférieur ou égal à "+t[0]+" caractères."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" doit être de type: "+(a[0]||"Aucun format autorisé.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Vous devez sélectionner au moins "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" doit être supérieur à "+t[0]+".":locales_esm_e(n)+" doit être plus long que "+t[0]+" caractères."},not:function(e){var r=e.name;return"“"+e.value+"” n'est pas un(e) "+r+" autorisé(e)."},number:function(r){return locales_esm_e(r.name)+" doit être un nombre."},required:function(r){return locales_esm_e(r.name)+" est obligatoire."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” ne commence pas par une valeur correcte.":"Ce champ ne commence pas par une valeur correcte."},url:function(e){e.name;return"Merci d'entrer une URL valide."}};function y(e){var r;e.extend({locales:(r={},r.fr=g,r)})}var h={accepted:function(e){return"אנא קבל את ה"+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" חייב להיות אחרי "+a[0]+".":locales_esm_e(n)+" חייב להיות תאריך יותר מאוחר."},alpha:function(r){return locales_esm_e(r.name)+" יכול להכיל אותיות בלבד."},alphanumeric:function(r){return locales_esm_e(r.name)+" יכול להכיל אותיות ומספרים בלבד."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" חייב להיות לפני "+a[0]+".":locales_esm_e(n)+" חייב להיות תאריך יותר מוקדם."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" חייב להיות בין "+t[0]+" ו-"+t[1]+".":locales_esm_e(n)+" חייב להיות בין "+t[0]+" ו-"+t[1]+" אותיות."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" אינו תואם."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" אינו תאריך תקין, אנא השתמש בפורמט "+a[0]:locales_esm_e(n)+" אינו תאריך תקין."},default:function(e){e.name;return"השדה אינו תקין."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” אינו כתובת אימייל תקין.":"אנא הכנס כתובת אימייל תקין."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” אינו מסתיים בערך תקין.":"שדה זו אינו מסתיים בערך תקין."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” אינו "+n+" מורשה.":"ערך זו איננו "+n+" מורשה."},matches:function(r){return locales_esm_e(r.name)+" אינו ערך מורשה."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"אתה יכול לבחור רק "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" חייב להיות פחות או שוה ל-"+t[0]+".":locales_esm_e(n)+" חייב להיות פחות או שוה ל-"+t[0]+" אותיות."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" חייב להיות מסוג של: "+(a[0]||"סוגי קבצים לא מורשים.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"אתה צריך לפחות "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" חייב להיות לפחות "+t[0]+".":locales_esm_e(n)+" חייב להיות לפחות "+t[0]+" אותיות."},not:function(e){var r=e.name;return"“"+e.value+"” אינו "+r+" מורשה."},number:function(r){return locales_esm_e(r.name)+" חייב להיות מספר."},required:function(r){return locales_esm_e(r.name)+" נדרש."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” אינו מתחיל בערך תקף.":"שדה זה אינו מתחיל בערך תקף."},url:function(e){e.name;return"אנא כלול כתובת אתר חוקית."}};function A(e){var r;e.extend({locales:(r={},r.he=h,r)})}var p={accepted:function(e){return"Kérlek fogadd el a(z) "+e.name+" mezőt."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" után kell lennie "+a[0]+".":locales_esm_e(n)+" későbbi dátumnak kell lennie."},alpha:function(r){return locales_esm_e(r.name)+" csak ábécé szerinti karaktereket tartalmazhat."},alphanumeric:function(r){return locales_esm_e(r.name)+" csak betűket és számokat tartalmazhat."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" előtt kell lennie "+a[0]+".":locales_esm_e(n)+" korábbi dátumnak kell lennie."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" között kell lennie "+t[0]+" és "+t[1]+".":locales_esm_e(n)+" között kell lennie "+t[0]+" és "+t[1]+" karakter hosszú."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" nem egyezik."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" nem érvényes dátum, kérlek használd a "+a[0]+" formátumot.":locales_esm_e(n)+" nem érvényes dátum."},default:function(e){e.name;return"Ez a mező érvénytelen."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” nem érvényes e-mail cím.":"Kérlek valós e-mail címet adj meg."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nem ér véget érvényes értékkel.":"Ez a mező nem ér véget érvényes értékkel."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” nem megengedett "+n+".":"Ez nem megengedett "+n+"."},matches:function(r){return locales_esm_e(r.name)+" nem megengedett érték."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Csak választható "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" kisebbnek vagy egyenlőnek kell lennie "+t[0]+".":locales_esm_e(n)+" kisebbnek vagy egyenlőnek kell lennie "+t[0]+" karakter hosszú."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" típusúnak kell lennie: "+(a[0]||"Nem engedélyezett fájlformátumok.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Legalább szükséges "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" legalább "+t[0]+".":locales_esm_e(n)+" legalább "+t[0]+" karakter hosszú."},not:function(e){var r=e.name;return"“"+e.value+"” nem megengedett "+r+"."},number:function(r){return locales_esm_e(r.name)+" számnak kell lennie."},required:function(r){return locales_esm_e(r.name)+" kötelező."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nem érvényes értékkel kezdődik.":"Ez a mező nem érvényes értékkel kezdődik."},url:function(e){e.name;return"Kérlek érvényes ulr-t adj meg."}};function b(e){var r;e.extend({locales:(r={},r.hu=p,r)})}var k={accepted:function(e){return"Per favore, accetta il campo "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" deve essere una data successiva al "+a[0]+".":locales_esm_e(n)+" deve essere una data successiva a quella attuale."},alpha:function(r){return locales_esm_e(r.name)+" può contenere solo lettere."},alphanumeric:function(r){return locales_esm_e(r.name)+" può contenere solo lettere e numeri."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" deve essere una data precedente al "+a[0]+".":locales_esm_e(n)+" deve essere una data precedente a quella attuale."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve essere tra "+t[0]+" e "+t[1]+".":locales_esm_e(n)+" deve avere una lunghezza compresa tra "+t[0]+" e "+t[1]+" caratteri."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" non corrisponde."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" non è una data valida. Per favore usa il formato "+a[0]:locales_esm_e(n)+" non è una data valida."},default:function(e){e.name;return"Questo campo non è valido."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” non è un indirizzo email valido.":"Per favore, inserisci un indirizzo email valido."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” non termina con un valore valido.":"Questo campo non termina con un valore valido."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” non è un valore valido per il campo "+n+".":n+" invalido."},matches:function(r){return locales_esm_e(r.name)+" invalido."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Puoi selezionare al massimo "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve essere inferiore o uguale a "+t[0]+".":locales_esm_e(n)+" deve essere inferiore o uguale a "+t[0]+" caratteri."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" deve essere del tipo: "+(a[0]||"Nessun formato file autorizzato.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Devi selezionare almeno "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve essere maggiore di "+t[0]+".":locales_esm_e(n)+" deve essere più lungo di "+t[0]+" caratteri."},not:function(e){var r=e.name;return"“"+e.value+"” non è un valore valido per il campo "+r+"."},number:function(r){return locales_esm_e(r.name)+" deve essere un numero."},required:function(r){return locales_esm_e(r.name)+" è un campo obbligatorio."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” non inizia con un valore valido.":"Questo campo non inizia con un valore valido."},url:function(e){e.name;return"Per favore inserisci un URL valido."}};function N(e){var r;e.extend({locales:(r={},r.it=k,r)})}var z={accepted:function(e){return e.name+"を承認してください。"},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+"は "+a[0]+" 以降にしてください。":locales_esm_e(n)+"はより後にしてください。"},alpha:function(r){return locales_esm_e(r.name)+"にはアルファベットのみ使用できます。"},alphanumeric:function(r){return locales_esm_e(r.name)+"には英数字のみ使用できます。"},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+"は "+a[0]+" 以前にしてください。":locales_esm_e(n)+"はより前にしてください。"},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+"は"+t[0]+"から"+t[1]+"の間でなければなりません。":locales_esm_e(n)+"は"+t[0]+"文字から"+t[1]+"文字でなければなりません。"},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+"が一致しません。"},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+"は有効な形式ではありません。次のフォーマットで入力してください: "+a[0]:locales_esm_e(n)+"は有効な形式ではありません。"},default:function(e){e.name;return"有効な値ではありません。"},email:function(e){e.name;var r=e.value;return r?"“"+r+"” は有効なメールアドレスではありません。":"有効なメールアドレスを入力してください。"},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” は有効な値で終わっていません。":"有効な値で終わっていません。"},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” は許可された"+n+"ではありません。":"許可された"+n+"ではありません。"},matches:function(r){return locales_esm_e(r.name)+"は許可された値ではありません。"},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return n+"は"+t[0]+"項目しか選択できません。";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+"は"+t[0]+"以下でなければなりません。":locales_esm_e(n)+"は"+t[0]+"文字以下でなければなりません。"},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+"は次のファイル形式でなければなりません: "+(a[0]||"許可されたファイル形式がありません")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return n+"は"+t[0]+"項目以上選択してください。";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+"は"+t[0]+"以上でなければなりません。":locales_esm_e(n)+"は"+t[0]+"文字以上でなければなりません。"},not:function(e){var r=e.name;return"“"+e.value+"” は許可された"+r+"ではありません。"},number:function(r){return locales_esm_e(r.name)+"には数字のみ使用できます。"},required:function(r){return locales_esm_e(r.name)+"は必須項目です。"},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” は有効な値で始まっていません。":"有効な値で始まっていません。"},url:function(e){e.name;return"有効なURLを入力してください。"}};function j(e){var r;e.extend({locales:(r={},r.ja=z,r)})}var w={accepted:function(e){return e.name+" 승인해 주세요."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" "+a[0]+" 이후이어야 합니다.":locales_esm_e(n)+" 미래의 날짜이어야 합니다."},alpha:function(r){return locales_esm_e(r.name)+" 알파벳만 사용할 수 있습니다."},alphanumeric:function(r){return locales_esm_e(r.name)+" 문자와 숫자만 사용할 수 있습니다."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" "+a[0]+" 이전이어야 합니다.":locales_esm_e(n)+"이전이어야 합니다."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" "+t[0]+"와 "+t[1]+"사이이어야 합니다.":locales_esm_e(n)+" "+t[0]+"자애서 "+t[1]+"자 사이이어야 합니다."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" 일치하지 않습니다."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" 유효한 날짜 형식이 아닙니다. 다음과 같은 형식으로 입력해 주세요: "+a[0]:locales_esm_e(n)+"올바른 날짜 형식이 아닙니다."},default:function(e){e.name;return"유효하지 않은 값입니다."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” 유효한 이메일 주소가 아닙니다.":"유효한 이메일 주소를 입력해 주세요."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"”으로 끝내야합니다.":"유효한 값으로 끝나지 않습니다."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” 허용된 "+n+" 아닙니다.":n+" 허용된 값이 아닙니다."},matches:function(r){return locales_esm_e(r.name)+" 허용 된 값이 아닙니다."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return n+" "+t[0]+"개의 항목만 선택 가능합니다.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" "+t[0]+"이하이어야 합니다.":locales_esm_e(n)+" "+t[0]+"자 이하이어야 합니다."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" 다음과 같은 파일 형식이어야 합니다: "+(a[0]||"허용되는 파일 형식이 아닙니다.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return n+" "+t[0]+" 이상 선택해 주세요.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" "+t[0]+"이상이어야 합니다.":locales_esm_e(n)+" "+t[0]+"자 이상이어야 합니다."},not:function(e){var r=e.name;return"“"+e.value+"” 허용된 "+r+" 아닙니다."},number:function(r){return locales_esm_e(r.name)+" 숫자만 사용 가능합니다."},required:function(r){return locales_esm_e(r.name)+" 필수 항목입니다."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” 유효한 값으로 시작하지 않습니다.":"유효한 값으로 시작하지 않습니다."},url:function(e){e.name;return"유효한 URL을 입력해 주세요."}};function x(e){var r;e.extend({locales:(r={},r.ko=w,r)})}var W={accepted:function(e){return"Vennligst aksepter "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" må være etter "+a[0]+".":locales_esm_e(n)+" må være på en senere dato."},alpha:function(r){return locales_esm_e(r.name)+" kan kun inneholde bokstaver."},alphanumeric:function(r){return locales_esm_e(r.name)+" kan kun inneholde bokstaver og tall."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" må være før "+a[0]+".":locales_esm_e(n)+" må være en tidligere dato."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" må være mellom "+t[0]+" og "+t[1]+".":locales_esm_e(n)+" må være mellom "+t[0]+" og "+t[1]+" tegn."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" matcher ikke."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" er ikke gyldig. Vennligst bruk formatet "+a[0]:locales_esm_e(n)+" er ikke en gyldig dato."},default:function(e){e.name;return"Dette feltet er ikke gyldig."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” er ikke en gyldig e-postadresse.":"Vennligst skriv inn en gyldig e-postadresse."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” slutter ikke med en gyldig verdi.":"Dette feltet slutter ikke med en gyldig verdi."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” er ikke et tillatt "+n+".":"Dette er ikke et tillatt "+n+"."},matches:function(r){return locales_esm_e(r.name)+" er ikke en gyldig verdi."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du kan kun velge "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" må være mindre eller lik "+t[0]+".":locales_esm_e(n)+" må være mindre eller lik "+t[0]+" tegn."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" må være av typen: "+(a[0]||"Ingen tillatte filformater.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du skal velge minst "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" må være større enn "+t[0]+".":locales_esm_e(n)+" må være minst "+t[0]+" tegn."},not:function(e){var r=e.name;return"“"+e.value+"” er ikke et tillatt "+r+"."},number:function(r){return locales_esm_e(r.name)+" må være et tall."},required:function(r){return locales_esm_e(r.name)+" er påkrevd."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” starter ikke med en gyldig verdi.":"Dette feltet starter ikke med en gyldig verdi."},url:function(e){e.name;return"Vennligst skriv inn en gyldig URL."}};function q(e){var r;e.extend({locales:(r={},r.nb=W,r)})}var P={accepted:function(e){return"Sta "+e.name+" toe."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" moet na "+a[0]+" zijn.":locales_esm_e(n)+" moet een latere datum zijn."},alpha:function(r){return locales_esm_e(r.name)+" mag enkel letters bevatten."},alphanumeric:function(r){return locales_esm_e(r.name)+" mag enkel letters en cijfers bevatten."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" moet voor "+a[0]+" zijn.":locales_esm_e(n)+" moet een eerdere datum zijn."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" moet tussen "+t[0]+" en "+t[1]+" zitten.":locales_esm_e(n)+" moet tussen "+t[0]+" en "+t[1]+" lang zijn."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" komt niet overeen."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" is geen geldige datum, het juiste format is "+a[0]:locales_esm_e(n)+" is geen geldige datum."},default:function(e){e.name;return"De invoer voor dit veld is niet geldig"},email:function(e){e.name;var r=e.value;return r?"“"+r+"” is geen geldig e-mailadres.":"Voer een geldig e-mailadres in."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” eindigt niet op een geldige waarde.":"Dit veld eindigt niet op een geldige waarde."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” is niet toegestaan als "+n+".":"Deze "+n+" is niet toegestaan."},matches:function(r){return locales_esm_e(r.name)+" is niet toegestaan."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Je kunt maximaal "+t[0]+" selecteren als "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" moet kleiner of gelijk zijn aan "+t[0]+".":locales_esm_e(n)+" mag maximaal "+t[0]+" karakters bevatten."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" moet van dit type zijn: "+(a[0]||"Bestanden zijn niet toegestaan")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Je moet tenminste "+t[0]+" selecteren als "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" moet groter zijn dan "+t[0]+".":locales_esm_e(n)+" moet tenminste "+t[0]+" karakters bevatten."},not:function(e){var r=e.name;return"“"+e.value+"” is geen geldige "+r+"."},number:function(r){return locales_esm_e(r.name)+" moet een getal zijn."},required:function(r){return locales_esm_e(r.name)+" is verplicht."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” begint niet met een geldige waarde.":"Dit veld begint niet met een geldige waarde."},url:function(e){e.name;return"Voer een geldige URL in."}};function D(e){var r;e.extend({locales:(r={},r.nl=P,r)})}var T={accepted:function(e){return"Prašome priimti "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" turi būti po "+a[0]+".":locales_esm_e(n)+" turi būti vėlesnė data."},alpha:function(r){return locales_esm_e(r.name)+" gali būti tik abėcėlės raidės."},alphanumeric:function(r){return locales_esm_e(r.name)+" gali būti tik raidės ir skaičiai."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" turi būti prieš "+a[0]+".":locales_esm_e(n)+" turi būti ankstesnė data."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" turi būti tarp "+t[0]+" ir "+t[1]+".":locales_esm_e(n)+" turi būti tarp "+t[0]+" ir "+t[1]+" simbolių ilgio."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" nesutampa."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" neteisinga data, naudokite formatą "+a[0]:locales_esm_e(n)+" neteisinga data."},default:function(e){e.name;return"Šis laukas nėra validus."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” nėra teisingas el. pašto adresas.":"Prašome įvesti galiojantį el. pašto adresą."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nesibaigia galiojančia reikšme.":"Šis laukas nesibaigia galiojančia reikšme."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” nėra tinkamas "+n+".":"Tai netinkamas "+n+"."},matches:function(r){return locales_esm_e(r.name)+" nėra leistina reikšmė."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Galite pasirinkti tik "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" turi būti mažesnis arba lygus "+t[0]+".":locales_esm_e(n)+" turi turėti mažiau arba lygiai "+t[0]+" simbolių."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" turi būti tokio tipo: "+(a[0]||"Neleidžiami jokie failų formatai.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Turi būti ne mažiau nei "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" turi būti ne mažiau nei "+t[0]+".":locales_esm_e(n)+" turi būti ne mažiau "+t[0]+" simbolių."},not:function(e){var r=e.name;return"“"+e.value+"” nėra leistinas "+r+"."},number:function(r){return locales_esm_e(r.name)+" turi būti skaičius."},required:function(r){return locales_esm_e(r.name)+" yra privalomas."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” neprasideda galiojančia reikšme.":"Šis laukas neprasideda galiojančia reikšme."},url:function(e){e.name;return"Įveskite galiojantį URL."}};function L(e){var r;e.extend({locales:(r={},r.lt=T,r)})}var U={accepted:function(e){return"Proszę zaakceptować "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musi być po "+a[0]+".":locales_esm_e(n)+" musi być przyszłą datą."},alpha:function(r){return locales_esm_e(r.name)+" może zawierać wyłącznie znaki alfabetyczne."},alphanumeric:function(r){return locales_esm_e(r.name)+" może zawierać wyłącznie liczby i litery."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musi być przed "+a[0]+".":locales_esm_e(n)+" musi być wczesniejszą datą."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musi być pomiędzy "+t[0]+" oraz "+t[1]+".":locales_esm_e(n)+" musi być pomiędzy "+t[0]+" oraz "+t[1]+" znaków."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" nie pasuje."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" nie jest poprawną datą, proszę użyć formatu "+a[0]:locales_esm_e(n)+" nie jest poprawną datą."},default:function(e){e.name;return"Pole nie jest poprawne."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” nie jest poprawnym adresem email.":"Proszę podać poprawny adres email."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nie kończy się z poprawną wartością.":"Pole nie kończy się z poprawną wartością."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” jest niedozwoloną wartością pola "+n+".":"Wartość jest niedozwolona w polu "+n+"."},matches:function(r){return locales_esm_e(r.name)+" nie jest dozwoloną wartością."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Możesz wybrać maksymalnie "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musi być mniejszy lub równy "+t[0]+".":locales_esm_e(n)+" musi być mniejszy lub równy "+t[0]+" znaków."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" musi być typem: "+(a[0]||"Niedozwolone formaty plików.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Potrzeba przynajmniej "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musi mieć przynajmniej "+t[0]+".":locales_esm_e(n)+" musi mieć przynajmniej "+t[0]+" znaków."},not:function(e){var r=e.name;return"“"+e.value+"” jest niedozwoloną wartością "+r+"."},number:function(r){return locales_esm_e(r.name)+" musi być liczbą."},required:function(r){return locales_esm_e(r.name)+" jest wymagane."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nie zaczyna się z poprawną wartością.":"Pole nie zaczyna się z poprawną wartością."},url:function(e){e.name;return"Proszę wprowadzić poprawny adres URL."}};function V(e){var r;e.extend({locales:(r={},r.pl=U,r)})}var E={accepted:function(e){return"Por favor aceite o "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" deve ser posterior a "+a[0]+".":locales_esm_e(n)+" deve ser uma data posterior."},alpha:function(r){return locales_esm_e(r.name)+" pode conter apenas letras."},alphanumeric:function(r){return locales_esm_e(r.name)+" pode conter apenas letras e números."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" deve ser antes de "+a[0]+".":locales_esm_e(n)+" deve ser uma data anterior."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve ser entre "+t[0]+" e "+t[1]+".":locales_esm_e(n)+" deve ter entre "+t[0]+" e "+t[1]+" caracteres."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" não corresponde."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" não é válido, por favor use o formato "+a[0]:locales_esm_e(n)+" não é uma data válida."},default:function(e){e.name;return"Este campo não é válido."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” não é um e-mail válido.":"Por favor informe um e-mail válido."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” não termina com um valor válido.":"Este campo não termina com um valor válido."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” não é um "+n+" permitido.":"Isso não é um "+n+" permitido."},matches:function(r){return locales_esm_e(r.name)+" não é um valor válido."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Você deve selecionar apenas "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve ser menor ou igual a "+t[0]+".":locales_esm_e(n)+" deve ter no máximo "+t[0]+" caracteres."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" deve ser no formato: "+(a[0]||"Formato de arquivo não permitido.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Você deve selecionar pelo menos "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" deve ser maior que "+t[0]+".":locales_esm_e(n)+" deve ter mais de "+t[0]+" caracteres."},not:function(e){var r=e.name;return"“"+e.value+"” não é um "+r+" válido."},number:function(r){return locales_esm_e(r.name)+" deve ser um número."},required:function(r){return locales_esm_e(r.name)+" é obrigatório."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” não começa com um valor válido.":"Este campo não começa com um valor válido."},url:function(e){e.name;return"Por favor informe uma URL válida."}};function R(e){var r;e.extend({locales:(r={},r.pt=E,r)})}var M={accepted:function(e){return"Пожалуйста, подтвердите "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" должна быть после "+a[0]+".":locales_esm_e(n)+" должна быть дата после."},alpha:function(r){return locales_esm_e(r.name)+" может содержать только буквы."},alphanumeric:function(r){return locales_esm_e(r.name)+" может содержать только буквы и цифры."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" должно быть раньше "+a[0]+".":locales_esm_e(n)+" должно быть раньше."},between:function(r){var n=r.name,a=r.value,t=r.args;!(!Array.isArray(t)||!t[2])&&t[2];return isNaN(a),locales_esm_e(n)+" должно быть между "+t[0]+" и "+t[1]+"."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" не совпадает."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" не является допустимой датой, пожалуйста, используйте формат "+a[0]:locales_esm_e(n)+" не является допустимой датой."},default:function(e){e.name;return"Это поле не является допустимым."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” недействительный адрес электронной почты.":"Пожалуйста, введите действительный адрес электронной почты."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” не заканчивается допустимым значением.":"Это поле не заканчивается допустимым значением."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” является ошибочным для "+n+".":"Выбранное значение для "+n+" ошибочно."},matches:function(r){return locales_esm_e(r.name)+" не совпадает."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Вы можете выбрать только "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" должно быть меньше или равно "+t[0]+".":"Количество символов "+locales_esm_e(n)+" должно быть меньше или равно "+t[0]+"."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" должно быть файлом одного из следующих типов: "+(a[0]||"Не допустимые форматы файлов.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Должно быть не менее "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" должно быть не менее "+t[0]+".":"Количество символов "+locales_esm_e(n)+" должно быть не менее "+t[0]+"."},not:function(e){var r=e.name;return"“"+e.value+"” не является допустимым "+r+"."},number:function(r){return locales_esm_e(r.name)+" должны быть числом."},required:function(r){return locales_esm_e(r.name)+" обязательное поле."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” должно начинаться действительным значением.":"Поле должно начинаться действительным значением."},url:function(e){e.name;return"Пожалуйста, укажите действительный URL."}};function B(e){var r;e.extend({locales:(r={},r.ru=M,r)})}var F={accepted:function(e){return"Prosím príjmi "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musí byť neskôr ako "+a[0]+".":"Pre "+locales_esm_e(n)+" je potrebné zvoliť neskorší dátum."},alpha:function(r){return locales_esm_e(r.name)+" môže obsahovať len písmená."},alphanumeric:function(r){return locales_esm_e(r.name)+" môže obsahovať len písmená a čísla."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" musí byť skôr než "+a[0]+".":"Pre "+locales_esm_e(n)+" je potrebné zvoliť skorší dátum."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí byť medzi "+t[0]+" a "+t[1]+".":locales_esm_e(n)+" musí mať od "+t[0]+" do "+t[1]+" znakov."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" sa nezhoduje."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" neobsahuje korektný dátum. Je potrebné použiť formát "+a[0]:locales_esm_e(n)+" neobsahuje korektný dátum."},default:function(e){e.name;return"Toto pole obsahuje chybu."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” nie je platná emailová adresa.":"Prosím, uveď platnú emailovú adresu.."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nekončí povolenou hodnotou.":"Toto pole nekončí povolenou hodnotou."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” nie je povolená hodnota pre "+n+".":"Toto nie je povolená hodnota pre "+n+"."},matches:function(r){return locales_esm_e(r.name)+" nie je povolená hodnota."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Je možné vybrať najviac "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí byť nanajvýš "+t[0]+".":locales_esm_e(n)+" musí obsahovať nanajvýš "+t[0]+" znakov."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" musí byť typu: "+(a[0]||"Žiadne formáty nie sú povolené.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Je potrebné vybrať aspoň "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" musí byť aspoň "+t[0]+".":locales_esm_e(n)+" musí obsahovať aspoň "+t[0]+" znakov."},not:function(e){var r=e.name;return"“"+e.value+"” nie je povolená hodnota pre "+r+"."},number:function(r){return locales_esm_e(r.name)+" musí byť číslo."},required:function(r){return locales_esm_e(r.name)+" je povinné pole."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” nezačína povolenou hodnotou.":"Toto pole nezačína povolenou hodnotou."},url:function(e){e.name;return"Prosím, uveď platnú URL adresu."}};function Z(e){var r;e.extend({locales:(r={},r.sk=F,r)})}var C={accepted:function(e){return"Molimo Vas da prihvatite "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" mora biti posle "+a[0]+".":locales_esm_e(n)+" mora biti kasniji datum."},alpha:function(r){return locales_esm_e(r.name)+" može sadržati samo abecedne karaktere."},alphanumeric:function(r){return locales_esm_e(r.name)+" može sadržati samo slova i brojeve."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" mora biti pre "+a[0]+".":locales_esm_e(n)+" mora biti raniji datum."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" mora biti između "+t[0]+" i "+t[1]+".":locales_esm_e(n)+" mora biti između "+t[0]+" i "+t[1]+" karaktera."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" se ne podudara."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" nije važeći datum, koristite format "+a[0]:locales_esm_e(n)+" nije važeći datum."},default:function(e){e.name;return"Ovo polje nije važeće."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” nije važeća e-mail adresa.":"Unesite ispravnu e-mail adresu."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” se ne završava važećom vrednošću.":"Ovo polje se ne završava važećom vrednošću."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” nije dozvoljeno "+n+".":"Ovo nije dozvoljeno "+n+"."},matches:function(r){return locales_esm_e(r.name)+" nije dozvoljena vrednost za ovo polje."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Možete odabrati samo "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" mora biti manje ili jednako "+t[0]+".":locales_esm_e(n)+" mora biti manje ili jednako "+t[0]+" karaktera."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" mora biti jedan sledecih formata: "+(a[0]||"Format datoteke nije dozvoljen.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Treba Vam bar "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" treba da ima najmanje "+t[0]+".":locales_esm_e(n)+" treba da ima najmanje "+t[0]+" karaktera."},not:function(e){var r=e.name;return"“"+e.value+"” nije dozvoljena vrednost za polje "+r+"."},number:function(r){return locales_esm_e(r.name)+" mora biti broj."},required:function(r){return locales_esm_e(r.name)+" je obavezno polje."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” ne počinje sa važećom vrednošću.":"Ovo polje ne počinje sa važećom vrednošću."},url:function(e){e.name;return"Unesite važeći url."}};function I(e){var r;e.extend({locales:(r={},r.sr=C,r)})}var J={accepted:function(e){return"Var vänlig acceptera "+e.name+"."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" måste vara efter "+a[0]+".":locales_esm_e(n)+" måste vara ett senare datum."},alpha:function(r){return locales_esm_e(r.name)+" får bara innehålla bokstäver."},alphanumeric:function(r){return locales_esm_e(r.name)+" får bara innehålla bokstäver och nummer."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" måste vara innan "+a[0]+".":locales_esm_e(n)+" måste vara ett tidigare datum."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" måste vara mellan "+t[0]+" och "+t[1]+".":locales_esm_e(n)+" måste vara mellan "+t[0]+" och "+t[1]+" tecken ."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" matchar inte."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" är inte ett giltigt datum, var vänlig och använd formatet "+a[0]:locales_esm_e(n)+" är inte ett giltigt datum."},default:function(e){e.name;return"Fältet är inte giltigt."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” är inte en giltigt e-postadress.":"Var vänlig och ange en giltig e-postadress."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” slutar inte med ett giltigt värde.":"Detta fält slutar inte med ett giltigt värde."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” är inte ett tillåtet "+n+".":"Detta är inte ett tillåtet "+n+"."},matches:function(r){return locales_esm_e(r.name)+" är inte ett tillåtet värde."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du får bara välja "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" måste vara mer än eller lika med "+t[0]+".":locales_esm_e(n)+" måste vara mindre än eller lika med "+t[0]+" tecken."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" måste vara av typen: "+(a[0]||"Inga filformat tillåtna.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Du måste välja minst "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" måste vara minst "+t[0]+".":locales_esm_e(n)+" måste åtminstone vara "+t[0]+" tecken långt."},not:function(e){var r=e.name;return"“"+e.value+"” är inte tillåtet "+r+"."},number:function(r){return locales_esm_e(r.name)+" måste vara ett nummer."},required:function(r){return locales_esm_e(r.name)+" är obligatoriskt."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” börjar inte med ett giltigt värde.":"Detta fält börjar inte med ett giltigt värde."},url:function(e){e.name;return"Vänligen ange en giltig url."}};function K(e){var r;e.extend({locales:(r={},r.sv=J,r)})}var S={accepted:function(r){return"กรุณายอมรับ "+locales_esm_e(r.name)},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ต้องเป็นวันที่หลังจาก "+a[0]:locales_esm_e(n)+" ต้องเป็นวันที่ยังไม่มาถึง"},alpha:function(r){return locales_esm_e(r.name)+" มีได้เฉพาะตัวอักษรเท่านั้น"},alphanumeric:function(r){return locales_esm_e(r.name)+" มีได้เฉพาะตัวอักษรและตัวเลขเท่านั้น"},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ต้องเป็นวันที่ก่อนหน้า "+a[0]:locales_esm_e(n)+" ต้องเป็นวันที่ผ่านมาแล้ว"},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ต้องมีค่าระหว่าง "+t[0]+" ถึง "+t[1]:locales_esm_e(n)+" ต้องมีความยาว "+t[0]+" ถึง "+t[1]+" ตัว"},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" ไม่ตรงกัน"},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" ไม่ใช่วันที่ที่ถูกต้อง กรุณาใช้ตามรูปแบบ "+a[0]:locales_esm_e(n)+" ไม่ใช่วันที่ที่ถูกต้อง"},default:function(e){e.name;return"ข้อมูลช่องนี้ไม่ถูกต้อง"},email:function(e){e.name;var r=e.value;return r?"“"+r+"” ไม่ใช่ที่อยู่อีเมลที่ถูกต้อง":"กรุณากรอกที่อยู่อีเมลให้ถูกต้อง"},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” ไม่ได้ลงท้ายด้วยค่าที่ถูกต้อง":"ข้อมูลช่องนี้ไม่ได้ลงท้ายด้วยค่าที่ถูกต้อง"},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” ไม่ใช่ "+n+" ที่อนุญาตให้กรอก":"นี่ไม่ใช่ "+n+" ที่อนุญาตให้กรอก"},matches:function(r){return locales_esm_e(r.name)+" ไม่ใช่ค่าที่อนุญาตให้กรอก"},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"คุณเลือกได้เพียง "+t[0]+" "+n+" เท่านั้น";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ต้องมีไม่เกิน "+t[0]:locales_esm_e(n)+" ต้องยาวไม่เกิน "+t[0]+" ตัว"},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" ต้องเป็นประเภท: "+(a[0]||"ไม่มีประเภทไฟล์ที่อนุญาต")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"คุณต้องเลือกอย่างน้อย "+t[0]+" "+n;var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" ต้องมีค่าอย่างน้อย "+t[0]:locales_esm_e(n)+" ต้องยาวอย่างน้อย "+t[0]+" ตัว"},not:function(e){var r=e.name;return"“"+e.value+"” ไม่ใช่ค่า "+r+" ที่อนุญาตให้กรอก"},number:function(r){return locales_esm_e(r.name)+" ต้องเป็นตัวเลข"},required:function(r){return locales_esm_e(r.name)+" จำเป็นต้องกรอก"},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” ไม่ได้ขึ้นต้นด้วยค่าที่ถูกต้อง":"ข้อมูลช่องนี้ไม่ได้ขึ้นต้นด้วยค่าที่ถูกต้อง"},url:function(e){e.name;return"กรุณาแนบลิงก์ให้ถูกต้อง"}};function O(e){var r;e.extend({locales:(r={},r.th=S,r)})}var Q={accepted:function(e){return"Lütfen "+e.name+"'i kabul edin.."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+", "+a[0]+" sonrasında olmalıdır.":locales_esm_e(n)+" daha sonraki bir tarih olmalıdır."},alpha:function(r){return locales_esm_e(r.name)+" yalnızca alfabetik karakterler içerebilir."},alphanumeric:function(r){return locales_esm_e(r.name)+" yalnızca harf ve rakam içerebilir."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+", "+a[0]+" tarihinden önce olmalıdır.":locales_esm_e(n)+" daha erken bir tarih olmalıdır."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+", "+t[0]+" ile "+t[1]+" arasında olmalıdır.":locales_esm_e(n)+", "+t[0]+" ile "+t[1]+" karakter uzunluğunda olmalıdır."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" eşleşmiyor."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" geçerli bir tarih değil, lütfen "+a[0]+" biçimini kullanın":locales_esm_e(n)+" geçerli bir tarih değil."},default:function(e){e.name;return"Bu alan geçerli değil."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” geçerli bir e-posta adresi değil.":"Lütfen geçerli bir e-posta adresi giriniz."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” geçerli bir değerle bitmiyor.":"Bu alan geçerli bir değerle bitmiyor."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” izin verilen bir "+n+" değil.":"Bu izin verilen bir "+n+" değil."},matches:function(r){return locales_esm_e(r.name)+" izin verilen bir değer değil."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Yalnızca "+t[0]+" "+n+" seçebilirsiniz.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+", "+t[0]+" değerinden küçük veya ona eşit olmalıdır.":locales_esm_e(n)+", "+t[0]+" karakterden küçük veya ona eşit olmalıdır."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" şu türde olmalıdır: "+(a[0]||"Dosya formatına izin verilmez.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"En az "+t[0]+" "+n+" gerekiyor.";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" en az "+t[0]+" olmalıdır.":locales_esm_e(n)+" en az "+t[0]+" karakter uzunluğunda olmalıdır."},not:function(e){var r=e.name;return"“"+e.value+"” izin verilen bir "+r+" değil."},number:function(r){return locales_esm_e(r.name)+" bir sayı olmalıdır."},required:function(r){return locales_esm_e(r.name)+" gerekli."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” geçerli bir değerle başlamıyor.":"Bu alan geçerli bir değerle başlamıyor."},url:function(e){e.name;return"Lütfen geçerli bir url ekleyin."}};function Y(e){var r;e.extend({locales:(r={},r.tr=Q,r)})}var G={accepted:function(e){return e.name+" phải được chấp nhận."},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" phải sau ngày "+a[0]+".":locales_esm_e(n)+" phải sau ngày hôm nay."},alpha:function(r){return locales_esm_e(r.name)+" chỉ có thể chứa các kí tự chữ."},alphanumeric:function(r){return locales_esm_e(r.name)+" chỉ có thể chứa các kí tự chữ và số."},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" phải trước ngày ngày "+a[0]+".":locales_esm_e(n)+" phải trước ngày hôm nay."},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" phải có giá trị nằm trong khoảng giữa "+t[0]+" and "+t[1]+".":locales_esm_e(n)+" phải có giá trị dài từ "+t[0]+" đến "+t[1]+" ký tự."},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" không khớp."},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" không phải là định dạng của ngày, vui lòng sử dụng định dạng "+a[0]:locales_esm_e(n)+" không phải là định dạng của ngày."},default:function(e){e.name;return"Trường này không hợp lệ."},email:function(e){e.name;var r=e.value;return r?"“"+r+"” phải là một địa chỉ email hợp lệ.":"Vui lòng nhập địa chỉ email hợp lệ."},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” phải kết thúc bằng giá trị hợp lệ.":"Trường này phải kết thúc bằng giá trị hợp lệ."},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” phải khớp với "+n+".":n+" phải khớp với giá trị cho phép."},matches:function(r){return locales_esm_e(r.name)+" phải khớp với giá trị cho phép."},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Bạn chỉ có thể chọn "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" phải nhỏ hơn hoặc bằng "+t[0]+".":locales_esm_e(n)+" phải nhỏ hơn hoặc bằng "+t[0]+" ký tự."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" phải chứa kiểu tệp phù hợp: "+(a[0]||"Không có định dạng tệp nào được cho phép.")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"Phải chứa ít nhất "+t[0]+" "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" phải chứa ít nhất "+t[0]+".":locales_esm_e(n)+" phải chứa ít nhất "+t[0]+" ký tự."},not:function(e){var r=e.name;return"“"+e.value+"” phải là "+r+" hợp lệ."},number:function(r){return locales_esm_e(r.name)+" phải là số."},required:function(r){return locales_esm_e(r.name)+" là bắt buộc."},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” phải bắt đầu bằng giá trị hợp lệ.":"Trường này phải bắt đầu bằng giá trị hợp lệ."},url:function(e){e.name;return"Vui lòng nhập đúng định dạng url."}};function H(e){var r;e.extend({locales:(r={},r.vi=G,r)})}var X={accepted:function(e){return"请同意"+e.name+"。"},after:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" 必须在 "+a[0]+" 之后。":locales_esm_e(n)+" 必须是以后的日期。"},alpha:function(r){return locales_esm_e(r.name)+" 只能包含字母。"},alphanumeric:function(r){return locales_esm_e(r.name)+" 只能包含字母或数字。"},before:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" 必须在 "+a[0]+" 之前":locales_esm_e(n)+" 必须是以前的日期。"},between:function(r){var n=r.name,a=r.value,t=r.args,i=!(!Array.isArray(t)||!t[2])&&t[2];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" 必须在 "+t[0]+" 和 "+t[1]+" 之间。":locales_esm_e(n)+" 必须在 "+t[0]+" 和 "+t[1]+" 字符长度之间。"},confirm:function(r){var n=r.name;r.args;return locales_esm_e(n)+" 不匹配。"},date:function(r){var n=r.name,a=r.args;return Array.isArray(a)&&a.length?locales_esm_e(n)+" 日期无效，请使用 "+a[0]+" 格式。":locales_esm_e(n)+" 日期无效。"},default:function(e){e.name;return"此输入无效。"},email:function(e){e.name;var r=e.value;return r?"“"+r+"” 不是一个有效的电子邮箱地址。":"请输入有效的电子邮箱地址。"},endsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” 包含无效的结尾值。":"无效的结尾值。"},in:function(r){var n=r.name,a=r.value;return"string"==typeof a&&a?"“"+locales_esm_e(a)+"” 是 "+n+" 不允许的值。":n+" 包含不允许的值。"},matches:function(r){return locales_esm_e(r.name)+" 包含不允许的值。"},max:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"您最多可有 "+t[0]+" 个 "+n+"。";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" 必须小于或等于 "+t[0]+".":locales_esm_e(n)+" 必须小于或等于 "+t[0]+" 字符长度."},mime:function(r){var n=r.name,a=r.args;return locales_esm_e(n)+" 格式必须是: "+(a[0]||"无允许文件格式")},min:function(r){var n=r.name,a=r.value,t=r.args;if(Array.isArray(a))return"您需要最少 "+t[0]+" 个 "+n+".";var i=!(!Array.isArray(t)||!t[1])&&t[1];return!isNaN(a)&&"length"!==i||"value"===i?locales_esm_e(n)+" 最少是 "+t[0]+".":locales_esm_e(n)+" 最少 "+t[0]+" 字符长度。"},not:function(e){var r=e.name;return"“"+e.value+"” 是 "+r+" 不被允许的值。"},number:function(r){return locales_esm_e(r.name)+" 必须是数字。"},required:function(r){return locales_esm_e(r.name)+" 是必填项。"},startsWith:function(e){e.name;var r=e.value;return r?"“"+r+"” 包含无效的起始值":"无效的起始值"},url:function(e){e.name;return"请输入正确的网址。"}};function $(e){var r;e.extend({locales:(r={},r.zh=X,r)})}

// CONCATENATED MODULE: ./node_modules/@braid/vue-formulate/dist/formulate.esm.js
var formulate_esm_i=function(t,e){return{classification:t,component:"FormulateInput"+(e||t[0].toUpperCase()+t.substr(1))}},formulate_esm_n=Object.assign({},["text","email","number","color","date","hidden","month","password","search","tel","time","url","week","datetime-local"].reduce((function(t,e){var r;return Object.assign({},t,((r={})[e]=formulate_esm_i("text"),r))}),{}),{range:formulate_esm_i("slider"),textarea:formulate_esm_i("textarea","TextArea"),checkbox:formulate_esm_i("box"),radio:formulate_esm_i("box"),submit:formulate_esm_i("button"),button:formulate_esm_i("button"),select:formulate_esm_i("select"),file:formulate_esm_i("file"),image:formulate_esm_i("file"),group:formulate_esm_i("group")});function formulate_esm_s(t,e){var r={};for(var o in t)r[o]=e(o,t[o]);return r}function formulate_esm_a(t,e,r){if(void 0===r&&(r=!1),t===e)return!0;if(!t||!e)return!1;if("object"!=typeof t&&"object"!=typeof e)return t===e;var o=Object.keys(t),i=Object.keys(e),n=o.length;if(i.length!==n)return!1;for(var s=0;s<n;s++){var l=o[s];if(!r&&t[l]!==e[l]||r&&!formulate_esm_a(t[l],e[l],r))return!1}return!0}function formulate_esm_l(t){return"string"==typeof t?t.replace(/([_-][a-z0-9])/gi,(function(e){return 0===t.indexOf(e)||/[_-]/.test(t[t.indexOf(e)-1])?e:e.toUpperCase().replace(/[_-]/,"")})):t}function formulate_esm_u(t){return"string"==typeof t?t[0].toUpperCase()+t.substr(1):t}function formulate_esm_c(t){return t?"string"==typeof t?[t]:Array.isArray(t)?t:"object"==typeof t?Object.values(t):[]:[]}function formulate_esm_d(t,e){return"string"==typeof t?formulate_esm_d(t.split("|"),e):Array.isArray(t)?t.map((function(t){return function(t,e){if("function"==typeof t)return[t,[]];if(Array.isArray(t)&&t.length){var r=formulate_esm_p((t=t.map((function(t){return t}))).shift()),o=r[0],i=r[1];if("string"==typeof o&&e.hasOwnProperty(o))return[e[o],t,o,i];if("function"==typeof o)return[o,t,o,i]}if("string"==typeof t&&t){var n=t.split(":"),s=formulate_esm_p(n.shift()),a=s[0],l=s[1];if(e.hasOwnProperty(a))return[e[a],n.length?n.join(":").split(","):[],a,l];throw new Error("Unknown validation rule "+t)}return!1}(t,e)})).filter((function(t){return!!t})):[]}function formulate_esm_p(t){return/^[\^]/.test(t.charAt(0))?[formulate_esm_l(t.substr(1)),t.charAt(0)]:[formulate_esm_l(t),null]}function formulate_esm_h(t){switch(typeof t){case"symbol":case"number":case"string":case"boolean":case"undefined":return!0;default:return null===t}}function formulate_esm_f(t,e){return Object.prototype.hasOwnProperty.call(t,e)}function formulate_esm_m(t,r){return!formulate_esm_f(t,"__id")||r?Object.defineProperty(t,"__id",Object.assign(Object.create(null),{value:r||non_secure_default()(9)})):t}function formulate_esm_v(t){return"number"!=typeof t&&(void 0===t||""===t||null===t||!1===t||Array.isArray(t)&&!t.some((function(t){return!formulate_esm_v(t)}))||t&&!Array.isArray(t)&&"object"==typeof t&&formulate_esm_v(Object.values(t)))}function formulate_esm_x(t,e){return Object.keys(t).reduce((function(r,o){var i=formulate_esm_l(o);return e.includes(i)&&(r[i]=t[o]),r}),{})}var formulate_esm_y=function(t,e,r){void 0===r&&(r={}),this.input=t,this.fileList=t.files,this.files=[],this.options=Object.assign({},{mimes:{}},r),this.results=!1,this.context=e,this.dataTransferCheck(),e&&e.uploadUrl&&(this.options.uploadUrl=e.uploadUrl),this.uploadPromise=null,Array.isArray(this.fileList)?this.rehydrateFileList(this.fileList):this.addFileList(this.fileList)};formulate_esm_y.prototype.rehydrateFileList=function(t){var e=this,r=t.reduce((function(t,r){var o=r[e.options?e.options.fileUrlKey:"url"],i=!(!o||-1===o.lastIndexOf("."))&&o.substr(o.lastIndexOf(".")+1),n=e.options.mimes[i]||!1;return t.push(Object.assign({},r,o?{name:r.name||o.substr(o.lastIndexOf("/")+1||0),type:r.type?r.type:n,previewData:o}:{})),t}),[]);this.addFileList(r),this.results=this.mapUUID(t)},formulate_esm_y.prototype.addFileList=function(t){for(var r=this,o=function(o){var i=t[o],n=non_secure_default()();r.files.push({progress:!1,error:!1,complete:!1,justFinished:!1,name:i.name||"file-upload",file:i,uuid:n,path:!1,removeFile:function(){this.removeFile(n)}.bind(r),previewData:i.previewData||!1})},i=0;i<t.length;i++)o(i)},formulate_esm_y.prototype.hasUploader=function(){return!!this.context.uploader},formulate_esm_y.prototype.uploaderIsAxios=function(){return!(!this.hasUploader()||"function"!=typeof this.context.uploader.request||"function"!=typeof this.context.uploader.get||"function"!=typeof this.context.uploader.delete||"function"!=typeof this.context.uploader.post)},formulate_esm_y.prototype.getUploader=function(){for(var t,e=[],r=arguments.length;r--;)e[r]=arguments[r];if(this.uploaderIsAxios()){var o=new FormData;if(o.append(this.context.name||"file",e[0]),!1===this.context.uploadUrl)throw new Error("No uploadURL specified: https://vueformulate.com/guide/inputs/file/#props");return this.context.uploader.post(this.context.uploadUrl,o,{headers:{"Content-Type":"multipart/form-data"},onUploadProgress:function(t){e[1](Math.round(100*t.loaded/t.total))}}).then((function(t){return t.data})).catch((function(t){return e[2](t)}))}return(t=this.context).uploader.apply(t,e)},formulate_esm_y.prototype.upload=function(){var t=this;return this.uploadPromise=this.uploadPromise?this.uploadPromise.then((function(){return t.__performUpload()})):this.__performUpload(),this.uploadPromise},formulate_esm_y.prototype.__performUpload=function(){var t=this;return new Promise((function(e,r){if(!t.hasUploader())return r(new Error("No uploader has been defined"));Promise.all(t.files.map((function(e){return e.error=!1,e.complete=!!e.path,e.path?Promise.resolve(e.path):t.getUploader(e.file,(function(r){e.progress=r,t.context.rootEmit("file-upload-progress",r),r>=100&&(e.complete||(e.justFinished=!0,setTimeout((function(){e.justFinished=!1}),t.options.uploadJustCompleteDuration)),e.complete=!0,t.context.rootEmit("file-upload-complete",e))}),(function(o){e.progress=0,e.error=o,e.complete=!0,t.context.rootEmit("file-upload-error",o),r(o)}),t.options)}))).then((function(r){t.results=t.mapUUID(r),e(r)})).catch((function(t){throw new Error(t)}))}))},formulate_esm_y.prototype.removeFile=function(t){var e=this.files.length;if(this.files=this.files.filter((function(e){return e&&e.uuid!==t})),Array.isArray(this.results)&&(this.results=this.results.filter((function(e){return e&&e.__id!==t}))),this.context.performValidation(),window&&this.fileList instanceof FileList&&this.supportsDataTransfers){var r=new DataTransfer;this.files.forEach((function(t){return r.items.add(t.file)})),this.fileList=r.files,this.input.files=this.fileList}else this.fileList=this.fileList.filter((function(e){return e&&e.__id!==t}));e>this.files.length&&this.context.rootEmit("file-removed",this.files)},formulate_esm_y.prototype.mergeFileList=function(t){if(this.addFileList(t.files),this.supportsDataTransfers){var e=new DataTransfer;this.files.forEach((function(t){t.file instanceof File&&e.items.add(t.file)})),this.fileList=e.files,this.input.files=this.fileList,t.files=(new DataTransfer).files}this.context.performValidation(),this.loadPreviews(),"delayed"!==this.context.uploadBehavior&&this.upload()},formulate_esm_y.prototype.loadPreviews=function(){this.files.map((function(t){if(!t.previewData&&window&&window.FileReader&&/^image\//.test(t.file.type)){var e=new FileReader;e.onload=function(e){return Object.assign(t,{previewData:e.target.result})},e.readAsDataURL(t.file)}}))},formulate_esm_y.prototype.dataTransferCheck=function(){try{new DataTransfer,this.supportsDataTransfers=!0}catch(t){this.supportsDataTransfers=!1}},formulate_esm_y.prototype.getFiles=function(){return this.files},formulate_esm_y.prototype.mapUUID=function(t){var e=this;return t.map((function(t,r){return e.files[r].path=void 0!==t&&t,t&&formulate_esm_m(t,e.files[r].uuid)}))},formulate_esm_y.prototype.toString=function(){var t=this.files.length?this.files.length+" files":"empty";return this.results?JSON.stringify(this.results,null,"  "):"FileUpload("+t+")"};var formulate_esm_g,formulate_esm_b={accepted:function(t){var e=t.value;return Promise.resolve(["yes","on","1",1,!0,"true"].includes(e))},after:function(t,e){var r=t.value;void 0===e&&(e=!1);var o=Date.parse(e||new Date),i=Date.parse(r);return Promise.resolve(!isNaN(i)&&i>o)},alpha:function(t,e){var r=t.value;void 0===e&&(e="default");var o={default:/^[a-zA-ZÀ-ÖØ-öø-ÿĄąĆćĘęŁłŃńŚśŹźŻż]+$/,latin:/^[a-zA-Z]+$/},i=o.hasOwnProperty(e)?e:"default";return Promise.resolve(o[i].test(r))},alphanumeric:function(t,e){var r=t.value;void 0===e&&(e="default");var o={default:/^[a-zA-Z0-9À-ÖØ-öø-ÿĄąĆćĘęŁłŃńŚśŹźŻż]+$/,latin:/^[a-zA-Z0-9]+$/},i=o.hasOwnProperty(e)?e:"default";return Promise.resolve(o[i].test(r))},before:function(t,e){var r=t.value;void 0===e&&(e=!1);var o=Date.parse(e||new Date),i=Date.parse(r);return Promise.resolve(!isNaN(i)&&i<o)},between:function(t,e,r,o){var i=t.value;return void 0===e&&(e=0),void 0===r&&(r=10),Promise.resolve(null!==e&&null!==r&&!isNaN(e)&&!isNaN(r)&&(!isNaN(i)&&"length"!==o||"value"===o?(i=Number(i),e=Number(e),r=Number(r),i>e&&i<r):("string"==typeof i||"length"===o)&&(i=isNaN(i)?i:i.toString()).length>e&&i.length<r))},confirm:function(t,e){var r,o,i=t.value,n=t.getGroupValues,s=t.name;return Promise.resolve((r=n(),(o=e)||(o=/_confirm$/.test(s)?s.substr(0,s.length-8):s+"_confirm"),r[o]===i))},date:function(t,e){var r=t.value;return void 0===e&&(e=!1),Promise.resolve(e&&"string"==typeof e?function(t){var e="^"+t.replace(/[.*+?^${}()|[\]\\]/g,"\\$&")+"$",r={MM:"(0[1-9]|1[012])",M:"([1-9]|1[012])",DD:"([012][0-9]|3[01])",D:"([012]?[0-9]|3[01])",YYYY:"\\d{4}",YY:"\\d{2}"};return new RegExp(Object.keys(r).reduce((function(t,e){return t.replace(e,r[e])}),e))}(e).test(r):!isNaN(Date.parse(r)))},email:function(t){var e=t.value;return Promise.resolve(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i.test(e))},endsWith:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve("string"==typeof e&&r.length?void 0!==r.find((function(t){return e.endsWith(t)})):"string"==typeof e&&0===r.length)},in:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve(void 0!==r.find((function(t){return"object"==typeof t?formulate_esm_a(t,e):t===e})))},matches:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve(!!r.find((function(t){return"string"==typeof t&&"/"===t.substr(0,1)&&"/"===t.substr(-1)&&(t=new RegExp(t.substr(1,t.length-2))),t instanceof RegExp?t.test(e):t===e})))},mime:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve(function(){if(e instanceof formulate_esm_y)for(var t=e.getFiles(),o=0;o<t.length;o++){var i=t[o].file;if(!r.includes(i.type))return!1}return!0}())},min:function(t,e,r){var o=t.value;return void 0===e&&(e=1),Promise.resolve(Array.isArray(o)?(e=isNaN(e)?e:Number(e),o.length>=e):!isNaN(o)&&"length"!==r||"value"===r?(o=isNaN(o)?o:Number(o))>=e:("string"==typeof o||"length"===r)&&(o=isNaN(o)?o:o.toString()).length>=e)},max:function(t,e,r){var o=t.value;return void 0===e&&(e=10),Promise.resolve(Array.isArray(o)?(e=isNaN(e)?e:Number(e),o.length<=e):!isNaN(o)&&"length"!==r||"value"===r?(o=isNaN(o)?o:Number(o))<=e:("string"==typeof o||"length"===r)&&(o=isNaN(o)?o:o.toString()).length<=e)},not:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve(void 0===r.find((function(t){return"object"==typeof t?formulate_esm_a(t,e):t===e})))},number:function(t){var e=t.value;return Promise.resolve(!isNaN(e))},required:function(t,e){var r=t.value;return void 0===e&&(e="pre"),Promise.resolve(Array.isArray(r)?!!r.length:r instanceof formulate_esm_y?r.getFiles().length>0:"string"==typeof r?"trim"===e?!!r.trim():!!r:"object"!=typeof r||!!r&&!!Object.keys(r).length)},startsWith:function(t){for(var e=t.value,r=[],o=arguments.length-1;o-- >0;)r[o]=arguments[o+1];return Promise.resolve("string"==typeof e&&r.length?void 0!==r.find((function(t){return e.startsWith(t)})):"string"==typeof e&&0===r.length)},url:function(e){var r=e.value;return Promise.resolve(is_url_default()(r))},bail:function(){return Promise.resolve(!0)},optional:function(t){var e=t.value;return Promise.resolve(!formulate_esm_v(e))}},formulate_esm_E="image/",_={csv:"text/csv",gif:formulate_esm_E+"gif",jpg:formulate_esm_E+"jpeg",jpeg:formulate_esm_E+"jpeg",png:formulate_esm_E+"png",pdf:"application/pdf",svg:formulate_esm_E+"svg+xml"},formulate_esm_F=["outer","wrapper","label","element","input","help","errors","error","decorator","rangeValue","uploadArea","uploadAreaMask","files","file","fileName","fileAdd","fileAddInput","fileRemove","fileProgress","fileUploadError","fileImagePreview","fileImagePreviewImage","fileProgressInner","grouping","groupRepeatable","groupRepeatableRemove","groupAddMore","form","formErrors","formError"],formulate_esm_w={hasErrors:function(t){return t.hasErrors},hasValue:function(t){return t.hasValue},isValid:function(t){return t.isValid}},formulate_esm_O=function(t,e,r){var o=[];switch(e){case"label":o.push(t+"--"+r.labelPosition);break;case"element":var i="group"===r.classification?"group":r.type;o.push(t+"--"+i),"group"===i&&o.push("formulate-input-group");break;case"help":o.push(t+"--"+r.helpPosition);break;case"form":r.name&&o.push(t+"--"+r.name)}return o},formulate_esm_P=(formulate_esm_g=[""].concat(Object.keys(formulate_esm_w).map((function(t){return formulate_esm_u(t)}))),formulate_esm_F.reduce((function(t,e){return t.concat(formulate_esm_g.reduce((function(t,r){return t.push(""+e+r+"Class"),t}),[]))}),[]));function formulate_esm_V(t,e,r){switch(typeof e){case"string":return e;case"function":return e(r,formulate_esm_c(t));case"object":if(Array.isArray(e))return formulate_esm_c(t).concat(e);default:return t}}function formulate_esm_A(t){return formulate_esm_F.reduce((function(e,r){var o;return Object.assign(e,((o={})[r]=function(t,e){var r=t.replace(/[A-Z]/g,(function(t){return"-"+t.toLowerCase()})),o="formulate"+(["form","file"].includes(r.substr(0,4))?"":"-input")+(["decorator","range-value"].includes(r)?"-element":"")+("outer"!==r?"-"+r:"");return"input"===r?[]:[o].concat(formulate_esm_O(o,t,e))}(r,t),o))}),{})}function formulate_esm_S(t,e,r,o){return new Promise((function(r,i){var n=(o.fauxUploaderDuration||1500)*(.5+Math.random()),s=performance.now(),a=function(){return setTimeout((function(){var o=performance.now()-s,i=Math.min(100,Math.round(o/n*100));if(e(i),i>=100)return r({url:"http://via.placeholder.com/350x150.png",name:t.name});a()}),20)};a()}))}function formulate_esm_j(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}var formulate_esm_$={inheritAttrs:!1,functional:!0,render:function(t,e){for(var r=e.props,o=e.data,i=e.parent,n=e.children,s=i,a=(r.name,r.forceWrap),l=r.context,u=formulate_esm_j(r,["name","forceWrap","context"]);s&&"FormulateInput"!==s.$options.name;)s=s.$parent;if(!s)return null;if(s.$scopedSlots&&s.$scopedSlots[r.name])return s.$scopedSlots[r.name](Object.assign({},l,u));if(Array.isArray(n)&&(n.length>1||a&&n.length>0)){var c=o.attrs,d=(c.name,c.context,formulate_esm_j(c,["name","context"]));return t("div",Object.assign({},o,{attrs:d}),n)}return Array.isArray(n)&&1===n.length?n[0]:null}};function formulate_esm_C(t,e,r){if(void 0===e&&(e=0),void 0===r&&(r={}),t&&"object"==typeof t&&!Array.isArray(t)){var o=t.children;void 0===o&&(o=null);var i=t.component;void 0===i&&(i="FormulateInput");var n=t.depth;void 0===n&&(n=1);var s=t.key;void 0===s&&(s=null);var a=function(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}(t,["children","component","depth","key"]),l=a.class||{};delete a.class;var u={},c=Object.keys(a).reduce((function(t,e){var r;return/^@/.test(e)?Object.assign(t,((r={})[e.substr(1)]=a[e],r)):t}),{});Object.keys(c).forEach((function(t){delete a["@"+t],u[t]=function(t,e,r){return function(){for(var o,i,n=[],s=arguments.length;s--;)n[s]=arguments[s];return"function"==typeof e?e.call.apply(e,[this].concat(n)):"string"==typeof e&&formulate_esm_f(r,e)?(o=r[e]).call.apply(o,[this].concat(n)):formulate_esm_f(r,t)?(i=r[t]).call.apply(i,[this].concat(n)):void 0}}(t,c[t],r)}));var d="FormulateInput"===i?a.type||"text":i,p=a.name||d||"el";s||(s=a.id?a.id:"FormulateInput"!==i&&"string"==typeof o?d+"-"+function(t,e){void 0===e&&(e=0);for(var r=3735928559^e,o=1103547991^e,i=0,n=void 0;i<t.length;i++)n=t.charCodeAt(i),r=Math.imul(r^n,2654435761),o=Math.imul(o^n,1597334677);return r=Math.imul(r^r>>>16,2246822507)^Math.imul(o^o>>>13,3266489909),4294967296*(2097151&(o=Math.imul(o^o>>>16,2246822507)^Math.imul(r^r>>>13,3266489909)))+(r>>>0)}(o):d+"-"+p+"-"+n+(a.name?"":"-"+e));var h=Array.isArray(o)?o.map((function(t){return Object.assign(t,{depth:n+1})})):o;return Object.assign({key:s,depth:n,attrs:a,component:i,class:l,on:u},h?{children:h}:{})}return null}var formulate_esm_k={functional:!0,render:function(t,e){var r=e.props,o=e.listeners;return function t(e,r,o){return Array.isArray(r)?r.map((function(r,i){var n=formulate_esm_C(r,i,o);return e(n.component,{attrs:n.attrs,class:n.class,key:n.key,on:n.on},n.children?t(e,n.children,o):null)})):r}(t,r.schema,o)}};function formulate_esm_I(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}var formulate_esm_R=function(t){this.registry=new Map,this.errors={},this.ctx=t};function formulate_esm_D(t){return new formulate_esm_R(t).dataProps()}function formulate_esm_L(t){return{hasInitialValue:function(){return this.formulateValue&&"object"==typeof this.formulateValue||this.values&&"object"==typeof this.values||this.isGrouping&&"object"==typeof this.context.model[this.index]},isVmodeled:function(){return!!(this.$options.propsData.hasOwnProperty("formulateValue")&&this._events&&Array.isArray(this._events.input)&&this._events.input.length)},initialValues:function(){return formulate_esm_f(this.$options.propsData,"formulateValue")&&"object"==typeof this.formulateValue?Object.assign({},this.formulateValue):formulate_esm_f(this.$options.propsData,"values")&&"object"==typeof this.values?Object.assign({},this.values):this.isGrouping&&"object"==typeof this.context.model[this.index]?this.context.model[this.index]:{}},mergedGroupErrors:function(){var t=this,e=/^([^.\d+].*?)\.(\d+\..+)$/;return Object.keys(this.mergedFieldErrors).filter((function(t){return e.test(t)})).reduce((function(r,o){var i,n=o.match(e),s=n[1],a=n[2];return r[s]||(r[s]={}),Object.assign(r[s],((i={})[a]=t.mergedFieldErrors[o],i)),r}),{})}}}function formulate_esm_N(t){void 0===t&&(t=[]);var e={applyInitialValues:function(){this.hasInitialValue&&(this.proxy=Object.assign({},this.initialValues))},setFieldValue:function(t,e){var r;if(void 0===e){var o=this.proxy,i=(o[t],formulate_esm_I(o,[String(t)]));this.proxy=i}else Object.assign(this.proxy,((r={})[t]=e,r));this.$emit("input",Object.assign({},this.proxy))},valueDeps:function(t){var e=this;return Object.keys(this.proxy).reduce((function(r,o){return Object.defineProperty(r,o,{enumerable:!0,get:function(){var r=e.registry.get(o);return e.deps.set(t,e.deps.get(t)||new Set),r&&(e.deps.set(r,e.deps.get(r)||new Set),e.deps.get(r).add(t.name)),e.deps.get(t).add(o),e.proxy[o]}})}),Object.create(null))},validateDeps:function(t){var e=this;this.deps.has(t)&&this.deps.get(t).forEach((function(t){return e.registry.has(t)&&e.registry.get(t).performValidation()}))},hasValidationErrors:function(){return Promise.all(this.registry.reduce((function(t,e,r){return t.push(e.performValidation()&&e.getValidationErrors()),t}),[])).then((function(t){return t.some((function(t){return t.hasErrors}))}))},showErrors:function(){this.childrenShouldShowErrors=!0,this.registry.map((function(t){t.formShouldShowErrors=!0}))},hideErrors:function(){this.childrenShouldShowErrors=!1,this.registry.map((function(t){t.formShouldShowErrors=!1,t.behavioralErrorVisibility=!1}))},setValues:function(t){var e=this;Array.from(new Set(Object.keys(t||{}).concat(Object.keys(this.proxy)))).forEach((function(r){var o=e.registry.has(r)&&e.registry.get(r),i=t?t[r]:void 0;o&&!formulate_esm_a(o.proxy,i,!0)&&(o.context.model=i),formulate_esm_a(i,e.proxy[r],!0)||e.setFieldValue(r,i)}))},updateValidation:function(t){formulate_esm_f(this.registry.errors,t.name)&&(this.registry.errors[t.name]=t.hasErrors),this.$emit("validation",t)},addErrorObserver:function(t){this.errorObservers.find((function(e){return t.callback===e.callback}))||(this.errorObservers.push(t),"form"===t.type?t.callback(this.mergedFormErrors):"group"===t.type&&formulate_esm_f(this.mergedGroupErrors,t.field)?t.callback(this.mergedGroupErrors[t.field]):formulate_esm_f(this.mergedFieldErrors,t.field)&&t.callback(this.mergedFieldErrors[t.field]))},removeErrorObserver:function(t){this.errorObservers=this.errorObservers.filter((function(e){return e.callback!==t}))}};return Object.keys(e).reduce((function(r,o){var i;return t.includes(o)?r:Object.assign({},r,((i={})[o]=e[o],i))}),{})}function formulate_esm_B(t,e){void 0===e&&(e=[]);var r={formulateSetter:t.setFieldValue,formulateRegister:t.register,formulateDeregister:t.deregister,formulateFieldValidation:t.updateValidation,getFormValues:t.valueDeps,getGroupValues:t.valueDeps,validateDependents:t.validateDeps,observeErrors:t.addErrorObserver,removeErrorObserver:t.removeErrorObserver};return Object.keys(r).filter((function(t){return!e.includes(t)})).reduce((function(t,e){var o;return Object.assign(t,((o={})[e]=r[e],o))}),{})}formulate_esm_R.prototype.add=function(t,e){var r;return this.registry.set(t,e),this.errors=Object.assign({},this.errors,((r={})[t]=e.getErrorObject().hasErrors,r)),this},formulate_esm_R.prototype.remove=function(t){this.ctx.deps.delete(this.registry.get(t)),this.ctx.deps.forEach((function(e){return e.delete(t)}));var e=this.ctx.keepModelData;!e&&this.registry.has(t)&&"inherit"!==this.registry.get(t).keepModelData&&(e=this.registry.get(t).keepModelData),this.ctx.preventCleanup&&(e=!0),this.registry.delete(t);var r=this.errors,o=(r[t],formulate_esm_I(r,[String(t)]));if(this.errors=o,!e){var i=this.ctx.proxy,n=(i[t],formulate_esm_I(i,[String(t)]));this.ctx.uuid&&formulate_esm_m(n,this.ctx.uuid),this.ctx.proxy=n,this.ctx.$emit("input",this.ctx.proxy)}return this},formulate_esm_R.prototype.has=function(t){return this.registry.has(t)},formulate_esm_R.prototype.get=function(t){return this.registry.get(t)},formulate_esm_R.prototype.map=function(t){var e={};return this.registry.forEach((function(r,o){var i;return Object.assign(e,((i={})[o]=t(r,o),i))})),e},formulate_esm_R.prototype.keys=function(){return Array.from(this.registry.keys())},formulate_esm_R.prototype.register=function(t,e){var r=this;if(formulate_esm_f(e.$options.propsData,"ignored"))return!1;if(this.registry.has(t))return this.ctx.$nextTick((function(){return!r.registry.has(t)&&r.register(t,e)})),!1;this.add(t,e);var o=formulate_esm_f(e.$options.propsData,"formulateValue"),i=formulate_esm_f(e.$options.propsData,"value"),n=this.ctx.debounce||this.ctx.debounceDelay||this.ctx.context&&this.ctx.context.debounceDelay;n&&!formulate_esm_f(e.$options.propsData,"debounce")&&(e.debounceDelay=n),o||!this.ctx.hasInitialValue||formulate_esm_v(this.ctx.initialValues[t])?!o&&!i||formulate_esm_a(e.proxy,this.ctx.initialValues[t],!0)||this.ctx.setFieldValue(t,e.proxy):e.context.model=this.ctx.initialValues[t],this.childrenShouldShowErrors&&(e.formShouldShowErrors=!0)},formulate_esm_R.prototype.reduce=function(t,e){return this.registry.forEach((function(r,o){e=t(e,r,o)})),e},formulate_esm_R.prototype.dataProps=function(){var t=this;return{proxy:{},registry:this,register:this.register.bind(this),deregister:function(e){return t.remove(e)},childrenShouldShowErrors:!1,errorObservers:[],deps:new Map,preventCleanup:!1}};var formulate_esm_M=function(t){this.form=t};function formulate_esm_U(t,e,r,o,i,n,s,a,l,u){"boolean"!=typeof s&&(l=a,a=s,s=!1);var c,d="function"==typeof r?r.options:r;if(t&&t.render&&(d.render=t.render,d.staticRenderFns=t.staticRenderFns,d._compiled=!0,i&&(d.functional=!0)),o&&(d._scopeId=o),n?(c=function(t){(t=t||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext)||"undefined"==typeof __VUE_SSR_CONTEXT__||(t=__VUE_SSR_CONTEXT__),e&&e.call(this,l(t)),t&&t._registeredComponents&&t._registeredComponents.add(n)},d._ssrRegister=c):e&&(c=s?function(t){e.call(this,u(t,this.$root.$options.shadowRoot))}:function(t){e.call(this,a(t))}),c)if(d.functional){var p=d.render;d.render=function(t,e){return c.call(e),p(t,e)}}else{var h=d.beforeCreate;d.beforeCreate=h?[].concat(h,c):[c]}return r}formulate_esm_M.prototype.hasValidationErrors=function(){return this.form.hasValidationErrors()},formulate_esm_M.prototype.values=function(){var t=this;return new Promise((function(e,r){var o=[],i=function t(e){if("object"!=typeof e)return e;var r=Array.isArray(e)?[]:{};for(var o in e)e[o]instanceof formulate_esm_y||formulate_esm_h(e[o])?r[o]=e[o]:r[o]=t(e[o]);return r}(t.form.proxy),n=function(e){"object"==typeof t.form.proxy[e]&&t.form.proxy[e]instanceof formulate_esm_y&&o.push(t.form.proxy[e].upload().then((function(t){var r;return Object.assign(i,((r={})[e]=t,r))})))};for(var s in i)n(s);Promise.all(o).then((function(){return e(i)})).catch((function(t){return r(t)}))}))};var formulate_esm_G={name:"FormulateForm",inheritAttrs:!1,provide:function(){return Object.assign({},formulate_esm_B(this,["getGroupValues"]),{observeContext:this.addContextObserver,removeContextObserver:this.removeContextObserver})},model:{prop:"formulateValue",event:"input"},props:{name:{type:[String,Boolean],default:!1},formulateValue:{type:Object,default:function(){return{}}},values:{type:[Object,Boolean],default:!1},errors:{type:[Object,Boolean],default:!1},formErrors:{type:Array,default:function(){return[]}},schema:{type:[Array,Boolean],default:!1},keepModelData:{type:[Boolean,String],default:!1},invalidMessage:{type:[Boolean,Function,String],default:!1},debounce:{type:[Boolean,Number],default:!1}},data:function(){return Object.assign({},formulate_esm_D(this),{formShouldShowErrors:!1,contextObservers:[],namedErrors:[],namedFieldErrors:{},isLoading:!1,hasFailedSubmit:!1})},computed:Object.assign({},formulate_esm_L(),{schemaListeners:function(){var t=this.$listeners;t.submit;return function(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}(t,["submit"])},pseudoProps:function(){return formulate_esm_x(this.$attrs,formulate_esm_P.filter((function(t){return/^form/.test(t)})))},attributes:function(){var t=this,e=Object.keys(this.$attrs).filter((function(e){return!formulate_esm_f(t.pseudoProps,formulate_esm_l(e))})).reduce((function(e,r){var o;return Object.assign({},e,((o={})[r]=t.$attrs[r],o))}),{});return"string"==typeof this.name&&Object.assign(e,{name:this.name}),e},hasErrors:function(){return Object.values(this.registry.errors).some((function(t){return t}))},isValid:function(){return!this.hasErrors},formContext:function(){return{errors:this.mergedFormErrors,pseudoProps:this.pseudoProps,hasErrors:this.hasErrors,value:this.proxy,hasValue:!formulate_esm_v(this.proxy),isValid:this.isValid,isLoading:this.isLoading,classes:this.classes}},classes:function(){return this.$formulate.classes(Object.assign({},this.$props,this.pseudoProps,{value:this.proxy,errors:this.mergedFormErrors,hasErrors:this.hasErrors,hasValue:!formulate_esm_v(this.proxy),isValid:this.isValid,isLoading:this.isLoading,type:"form",classification:"form",attrs:this.$attrs}))},invalidErrors:function(){if(this.hasFailedSubmit&&this.hasErrors)switch(typeof this.invalidMessage){case"string":return[this.invalidMessage];case"object":return Array.isArray(this.invalidMessage)?this.invalidMessage:[];case"function":var t=this.invalidMessage(this.failingFields);return Array.isArray(t)?t:[t]}return[]},mergedFormErrors:function(){return this.formErrors.concat(this.namedErrors).concat(this.invalidErrors)},mergedFieldErrors:function(){var t={};if(this.errors)for(var e in this.errors)t[e]=formulate_esm_c(this.errors[e]);for(var r in this.namedFieldErrors)t[r]=formulate_esm_c(this.namedFieldErrors[r]);return t},hasFormErrorObservers:function(){return!!this.errorObservers.filter((function(t){return"form"===t.type})).length},failingFields:function(){var t=this;return Object.keys(this.registry.errors).reduce((function(e,r){var o;return Object.assign({},e,t.registry.errors[r]?((o={})[r]=t.registry.get(r),o):{})}),{})}}),watch:Object.assign({},{mergedFieldErrors:{handler:function(t){this.errorObservers.filter((function(t){return"input"===t.type})).forEach((function(e){return e.callback(t[e.field]||[])}))},immediate:!0},mergedGroupErrors:{handler:function(t){this.errorObservers.filter((function(t){return"group"===t.type})).forEach((function(e){return e.callback(t[e.field]||{})}))},immediate:!0}},{formulateValue:{handler:function(t){this.isVmodeled&&t&&"object"==typeof t&&this.setValues(t)},deep:!0},mergedFormErrors:function(t){this.errorObservers.filter((function(t){return"form"===t.type})).forEach((function(e){return e.callback(t)}))}}),created:function(){this.$formulate.register(this),this.applyInitialValues(),this.$emit("created",this)},destroyed:function(){this.$formulate.deregister(this)},methods:Object.assign({},formulate_esm_N(),{applyErrors:function(t){var e=t.formErrors,r=t.inputErrors;this.namedErrors=e,this.namedFieldErrors=r},addContextObserver:function(t){this.contextObservers.find((function(e){return e===t}))||(this.contextObservers.push(t),t(this.formContext))},removeContextObserver:function(t){this.contextObservers.filter((function(e){return e!==t}))},registerErrorComponent:function(t){this.errorComponents.includes(t)||this.errorComponents.push(t)},formSubmitted:function(){var t=this;if(!this.isLoading){this.isLoading=!0,this.showErrors();var e=new formulate_esm_M(this),r=this.$listeners["submit-raw"]||this.$listeners.submitRaw,o="function"==typeof r?r(e):Promise.resolve(e);return(o instanceof Promise?o:Promise.resolve(o)).then((function(t){var r=t instanceof formulate_esm_M?t:e;return r.hasValidationErrors().then((function(t){return[r,t]}))})).then((function(e){var r=e[0];return e[1]||"function"!=typeof t.$listeners.submit?t.onFailedValidation():r.values().then((function(e){t.hasFailedSubmit=!1;var r=t.$listeners.submit(e);return(r instanceof Promise?r:Promise.resolve()).then((function(){return e}))}))})).finally((function(){t.isLoading=!1}))}},onFailedValidation:function(){return this.hasFailedSubmit=!0,this.$emit("failed-validation",Object.assign({},this.failingFields)),this.$formulate.failedValidation(this)}})},formulate_esm_T=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("form",t._b({class:t.classes.form,on:{submit:function(e){return e.preventDefault(),t.formSubmitted(e)}}},"form",t.attributes,!1),[t.schema?r("FormulateSchema",t._g({attrs:{schema:t.schema}},t.schemaListeners)):t._e(),t._v(" "),t.hasFormErrorObservers?t._e():r("FormulateErrors",{attrs:{context:t.formContext}}),t._v(" "),t._t("default",null,null,t.formContext)],2)};formulate_esm_T._withStripped=!0;var formulate_esm_q=formulate_esm_U({render:formulate_esm_T,staticRenderFns:[]},void 0,formulate_esm_G,void 0,!1,void 0,!1,void 0,void 0,void 0);var formulate_esm_H={context:function(){return formulate_esm_K.call(this,Object.assign({},{addLabel:this.logicalAddLabel,removeLabel:this.logicalRemoveLabel,attributes:this.elementAttributes,blurHandler:formulate_esm_z.bind(this),classification:this.classification,component:this.component,debounceDelay:this.debounceDelay,disableErrors:this.disableErrors,errors:this.explicitErrors,formShouldShowErrors:this.formShouldShowErrors,getValidationErrors:this.getValidationErrors.bind(this),groupErrors:this.mergedGroupErrors,hasGivenName:this.hasGivenName,hasValue:this.hasValue,hasLabel:this.label&&"button"!==this.classification,hasValidationErrors:this.hasValidationErrors.bind(this),help:this.help,helpPosition:this.logicalHelpPosition,id:this.id||this.defaultId,ignored:formulate_esm_f(this.$options.propsData,"ignored"),isValid:this.isValid,imageBehavior:this.imageBehavior,label:this.label,labelPosition:this.logicalLabelPosition,limit:this.limit===1/0?this.limit:parseInt(this.limit,10),name:this.nameOrFallback,minimum:parseInt(this.minimum,10),performValidation:this.performValidation.bind(this),pseudoProps:this.pseudoProps,preventWindowDrops:this.preventWindowDrops,removePosition:this.mergedRemovePosition,repeatable:this.repeatable,rootEmit:this.$emit.bind(this),rules:this.ruleDetails,setErrors:this.setErrors.bind(this),showValidationErrors:this.showValidationErrors,slotComponents:this.slotComponents,slotProps:this.slotProps,type:this.type,uploadBehavior:this.uploadBehavior,uploadUrl:this.mergedUploadUrl,uploader:this.uploader||this.$formulate.getUploader(),validationErrors:this.validationErrors,value:this.value,visibleValidationErrors:this.visibleValidationErrors,isSubField:this.isSubField,classes:this.classes},this.typeContext))},nameOrFallback:function(){if(!0===this.name&&"button"!==this.classification){var t=this.id||this.elementAttributes.id.replace(/[^0-9]/g,"");return this.type+"_"+t}if(!1===this.name||"button"===this.classification&&!0===this.name)return!1;return this.name},hasGivenName:function(){return"boolean"!=typeof this.name},typeContext:function(){var t=this;switch(this.classification){case"select":return{options:formulate_esm_W.call(this,this.options),optionGroups:!!this.optionGroups&&formulate_esm_s(this.optionGroups,(function(e,r){return formulate_esm_W.call(t,r)})),placeholder:this.$attrs.placeholder||!1};case"slider":return{showValue:!!this.showValue};default:return this.options?{options:formulate_esm_W.call(this,this.options)}:{}}},elementAttributes:function(){var t=Object.assign({},this.filteredAttributes);this.id?t.id=this.id:t.id=this.defaultId;this.hasGivenName&&(t.name=this.name);this.help&&!formulate_esm_f(t,"aria-describedby")&&(t["aria-describedby"]=t.id+"-help");!this.classes.input||Array.isArray(this.classes.input)&&!this.classes.input.length||(t.class=this.classes.input);return t},logicalLabelPosition:function(){if(this.labelPosition)return this.labelPosition;switch(this.classification){case"box":return"after";default:return"before"}},logicalHelpPosition:function(){if(this.helpPosition)return this.helpPosition;switch(this.classification){case"group":return"before";default:return"after"}},mergedRemovePosition:function(){return"group"===this.type&&(this.removePosition||"before")},mergedUploadUrl:function(){return this.uploadUrl||this.$formulate.getUploadUrl()},mergedGroupErrors:function(){var t=this,e=Object.keys(this.groupErrors).concat(Object.keys(this.localGroupErrors)),r=/^(\d+)\.(.*)$/;return Array.from(new Set(e)).filter((function(t){return r.test(t)})).reduce((function(e,o){var i,n=o.match(r),s=n[1],a=n[2];formulate_esm_f(e,s)||(e[s]={});var l=Array.from(new Set(formulate_esm_c(t.groupErrors[o]).concat(formulate_esm_c(t.localGroupErrors[o]))));return e[s]=Object.assign(e[s],((i={})[a]=l,i)),e}),{})},hasValue:function(){var t=this,e=this.proxy;if("box"===this.classification&&this.isGrouped||"select"===this.classification&&formulate_esm_f(this.filteredAttributes,"multiple"))return Array.isArray(e)?e.some((function(e){return e===t.value})):this.value===e;return!formulate_esm_v(e)},visibleValidationErrors:function(){return this.showValidationErrors&&this.validationErrors.length?this.validationErrors:[]},slotComponents:function(){var t=this.$formulate.slotComponent.bind(this.$formulate);return{addMore:t(this.type,"addMore"),buttonContent:t(this.type,"buttonContent"),errors:t(this.type,"errors"),file:t(this.type,"file"),help:t(this.type,"help"),label:t(this.type,"label"),prefix:t(this.type,"prefix"),remove:t(this.type,"remove"),repeatable:t(this.type,"repeatable"),suffix:t(this.type,"suffix"),uploadAreaMask:t(this.type,"uploadAreaMask")}},logicalAddLabel:function(){if("file"===this.classification)return!0===this.addLabel?"+ Add "+formulate_esm_u(this.type):this.addLabel;if("boolean"==typeof this.addLabel){var t=this.label||this.name;return"+ "+("string"==typeof t?t+" ":"")+" Add"}return this.addLabel},logicalRemoveLabel:function(){if("boolean"==typeof this.removeLabel)return"Remove";return this.removeLabel},classes:function(){return this.$formulate.classes(Object.assign({},this.$props,this.pseudoProps,{attrs:this.filteredAttributes,classification:this.classification,hasErrors:this.hasVisibleErrors,hasValue:this.hasValue,helpPosition:this.logicalHelpPosition,isValid:this.isValid,labelPosition:this.logicalLabelPosition,type:this.type,value:this.proxy}))},showValidationErrors:function(){if(this.showErrors||this.formShouldShowErrors)return!0;if("file"===this.classification&&"live"===this.uploadBehavior&&formulate_esm_Z.call(this))return!0;return this.behavioralErrorVisibility},slotProps:function(){var t=this.$formulate.slotProps.bind(this.$formulate);return{label:t(this.type,"label",this.typeProps),help:t(this.type,"help",this.typeProps),errors:t(this.type,"errors",this.typeProps),repeatable:t(this.type,"repeatable",this.typeProps),addMore:t(this.type,"addMore",this.typeProps),remove:t(this.type,"remove",this.typeProps),component:t(this.type,"component",this.typeProps)}},pseudoProps:function(){return formulate_esm_x(this.localAttributes,formulate_esm_P)},isValid:function(){return!this.hasErrors},ruleDetails:function(){return this.parsedValidation.map((function(t){var e=t[1];return{ruleName:t[2],args:e}}))},isVmodeled:function(){return!!(this.$options.propsData.hasOwnProperty("formulateValue")&&this._events&&Array.isArray(this._events.input)&&this._events.input.length)},mergedValidationName:function(){var t=this,e=this.$formulate.options.validationNameStrategy||["validationName","name","label","type"];if(Array.isArray(e)){return this[e.find((function(e){return"string"==typeof t[e]}))]}if("function"==typeof e)return e.call(this,this);return this.type},explicitErrors:function(){return formulate_esm_c(this.errors).concat(this.localErrors).concat(formulate_esm_c(this.error))},allErrors:function(){return this.explicitErrors.concat(formulate_esm_c(this.validationErrors))},hasVisibleErrors:function(){return Array.isArray(this.validationErrors)&&this.validationErrors.length&&this.showValidationErrors||!!this.explicitErrors.length},hasErrors:function(){return!!this.allErrors.length},filteredAttributes:function(){var t=this,e=Object.keys(this.pseudoProps).concat(Object.keys(this.typeProps));return Object.keys(this.localAttributes).reduce((function(r,o){return e.includes(formulate_esm_l(o))||(r[o]=t.localAttributes[o]),r}),{})},typeProps:function(){return formulate_esm_x(this.localAttributes,this.$formulate.typeProps(this.type))},listeners:function(){var t=this.$listeners;t.input;return function(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}(t,["input"])}};function formulate_esm_W(t){return t?(Array.isArray(t)?t:Object.keys(t).map((function(e){return{label:t[e],value:e}}))).map(formulate_esm_Y.bind(this)):[]}function formulate_esm_Y(t){return"number"==typeof t&&(t=String(t)),"string"==typeof t?{label:t,value:t,id:this.elementAttributes.id+"_"+t}:("number"==typeof t.value&&(t.value=String(t.value)),Object.assign({value:"",label:"",id:this.elementAttributes.id+"_"+(t.value||t.label)},t))}function formulate_esm_z(){var t=this;"blur"!==this.errorBehavior&&"value"!==this.errorBehavior||(this.behavioralErrorVisibility=!0),this.$nextTick((function(){return t.$emit("blur-context",t.context)}))}function formulate_esm_K(t){var e=this;return Object.defineProperty(t,"model",{get:formulate_esm_Z.bind(this),set:function(t){if(!e.mntd||!e.debounceDelay)return formulate_esm_J.call(e,t);e.dSet(formulate_esm_J,[t],e.debounceDelay)},enumerable:!0})}function formulate_esm_Z(){var t=this.isVmodeled?"formulateValue":"proxy";return"checkbox"===this.type&&!Array.isArray(this[t])&&this.options?[]:this[t]||0===this[t]?this[t]:""}function formulate_esm_J(t){var e=!1;formulate_esm_a(t,this.proxy,"group"===this.type)||(this.proxy=t,e=!0),!this.context.ignored&&this.context.name&&"function"==typeof this.formulateSetter&&this.formulateSetter(this.context.name,t),e&&this.$emit("input",t)}var formulate_esm_X={name:"FormulateInput",inheritAttrs:!1,provide:function(){return{formulateRegisterRule:this.registerRule,formulateRemoveRule:this.removeRule}},inject:{formulateSetter:{default:void 0},formulateFieldValidation:{default:function(){return function(){return{}}}},formulateRegister:{default:void 0},formulateDeregister:{default:void 0},getFormValues:{default:function(){return function(){return{}}}},getGroupValues:{default:void 0},validateDependents:{default:function(){return function(){}}},observeErrors:{default:void 0},removeErrorObserver:{default:void 0},isSubField:{default:function(){return function(){return!1}}}},model:{prop:"formulateValue",event:"input"},props:{type:{type:String,default:"text"},name:{type:[String,Boolean],default:!0},formulateValue:{default:""},value:{default:!1},options:{type:[Object,Array,Boolean],default:!1},optionGroups:{type:[Object,Boolean],default:!1},id:{type:[String,Boolean,Number],default:!1},label:{type:[String,Boolean],default:!1},labelPosition:{type:[String,Boolean],default:!1},limit:{type:[String,Number],default:1/0,validator:function(t){return 1/0}},minimum:{type:[String,Number],default:0,validator:function(t){return parseInt(t,10)==t}},help:{type:[String,Boolean],default:!1},helpPosition:{type:[String,Boolean],default:!1},isGrouped:{type:Boolean,default:!1},errors:{type:[String,Array,Boolean],default:!1},removePosition:{type:[String,Boolean],default:!1},repeatable:{type:Boolean,default:!1},validation:{type:[String,Boolean,Array],default:!1},validationName:{type:[String,Boolean],default:!1},error:{type:[String,Boolean],default:!1},errorBehavior:{type:String,default:"blur",validator:function(t){return["blur","live","submit","value"].includes(t)}},showErrors:{type:Boolean,default:!1},groupErrors:{type:Object,default:function(){return{}},validator:function(t){var e=/^\d+\./;return!Object.keys(t).some((function(t){return!e.test(t)}))}},imageBehavior:{type:String,default:"preview"},uploadUrl:{type:[String,Boolean],default:!1},uploader:{type:[Function,Object,Boolean],default:!1},uploadBehavior:{type:String,default:"live"},preventWindowDrops:{type:Boolean,default:!0},showValue:{type:[String,Boolean],default:!1},validationMessages:{type:Object,default:function(){return{}}},validationRules:{type:Object,default:function(){return{}}},checked:{type:[String,Boolean],default:!1},disableErrors:{type:Boolean,default:!1},addLabel:{type:[Boolean,String],default:!0},removeLabel:{type:[Boolean,String],default:!1},keepModelData:{type:[Boolean,String],default:"inherit"},ignored:{type:[Boolean,String],default:!1},debounce:{type:[Boolean,Number],default:!1},preventDeregister:{type:Boolean,default:!1}},data:function(){return{defaultId:this.$formulate.nextId(this),localAttributes:{},localErrors:[],localGroupErrors:{},proxy:this.getInitialValue(),behavioralErrorVisibility:"live"===this.errorBehavior,formShouldShowErrors:!1,validationErrors:[],pendingValidation:Promise.resolve(),ruleRegistry:[],messageRegistry:{},touched:!1,debounceDelay:this.debounce,dSet:function(e,r,o){var i=this;t&&clearTimeout(t),t=setTimeout((function(){return e.call.apply(e,[i].concat(r))}),o)},mntd:!1};var t},computed:Object.assign({},formulate_esm_H,{classification:function(){var t=this.$formulate.classify(this.type);return"box"===t&&this.options?"group":t},component:function(){return"group"===this.classification?"FormulateInputGroup":this.$formulate.component(this.type)},parsedValidationRules:function(){var t=this,e={};return Object.keys(this.validationRules).forEach((function(r){e[formulate_esm_l(r)]=t.validationRules[r]})),e},parsedValidation:function(){return formulate_esm_d(this.validation,this.$formulate.rules(this.parsedValidationRules))},messages:function(){var t=this,e={};return Object.keys(this.validationMessages).forEach((function(r){e[formulate_esm_l(r)]=t.validationMessages[r]})),Object.keys(this.messageRegistry).forEach((function(r){e[formulate_esm_l(r)]=t.messageRegistry[r]})),e}}),watch:{$attrs:{handler:function(t){this.updateLocalAttributes(t)},deep:!0},proxy:{handler:function(t,e){this.performValidation(),this.isVmodeled||formulate_esm_a(t,e,"group"===this.type)||(this.context.model=t),this.validateDependents(this),!this.touched&&t&&(this.touched=!0)},deep:!0},formulateValue:{handler:function(t,e){this.isVmodeled&&!formulate_esm_a(t,e,"group"===this.type)&&(this.context.model=t)},deep:!0},showValidationErrors:{handler:function(t){this.$emit("error-visibility",t)},immediate:!0},validation:{handler:function(){this.performValidation()},deep:!0},touched:function(t){"value"===this.errorBehavior&&t&&(this.behavioralErrorVisibility=t)},debounce:function(t){this.debounceDelay=t}},created:function(){this.applyInitialValue(),this.formulateRegister&&"function"==typeof this.formulateRegister&&this.formulateRegister(this.nameOrFallback,this),this.applyDefaultValue(),this.disableErrors||"function"!=typeof this.observeErrors||(this.observeErrors({callback:this.setErrors,type:"input",field:this.nameOrFallback}),"group"===this.type&&this.observeErrors({callback:this.setGroupErrors,type:"group",field:this.nameOrFallback})),this.updateLocalAttributes(this.$attrs),this.performValidation(),this.hasValue&&(this.touched=!0)},mounted:function(){this.mntd=!0},beforeDestroy:function(){this.disableErrors||"function"!=typeof this.removeErrorObserver||(this.removeErrorObserver(this.setErrors),"group"===this.type&&this.removeErrorObserver(this.setGroupErrors)),"function"!=typeof this.formulateDeregister||this.preventDeregister||this.formulateDeregister(this.nameOrFallback)},methods:{getInitialValue:function(){var t=this.$formulate.classify(this.type);return"box"===(t="box"===t&&this.options?"group":t)&&this.checked?this.value||!0:formulate_esm_f(this.$options.propsData,"value")&&"box"!==t?this.value:formulate_esm_f(this.$options.propsData,"formulateValue")?this.formulateValue:"group"===t?Object.defineProperty("group"===this.type?[{}]:[],"__init",{value:!0}):""},applyInitialValue:function(){formulate_esm_a(this.context.model,this.proxy)||"box"===this.classification&&!formulate_esm_f(this.$options.propsData,"options")||(this.context.model=this.proxy,this.$emit("input",this.proxy))},applyDefaultValue:function(){"select"===this.type&&!this.context.placeholder&&formulate_esm_v(this.proxy)&&!this.isVmodeled&&!1===this.value&&this.context.options.length&&(formulate_esm_f(this.$attrs,"multiple")?this.context.model=[]:this.context.model=this.context.options[0].value)},updateLocalAttributes:function(t){formulate_esm_a(t,this.localAttributes)||(this.localAttributes=t)},performValidation:function(){var t=this,e=formulate_esm_d(this.validation,this.$formulate.rules(this.parsedValidationRules));return e=this.ruleRegistry.length?this.ruleRegistry.concat(e):e,this.pendingValidation=this.runRules(e).then((function(e){return t.didValidate(e)})),this.pendingValidation},runRules:function(t){var e=this,r=function(t){var r=t[0],o=t[1],i=t[2],n=(t[3],r.apply(void 0,[{value:e.context.model,getFormValues:function(){for(var t,r=[],o=arguments.length;o--;)r[o]=arguments[o];return(t=e).getFormValues.apply(t,[e].concat(r))},getGroupValues:function(){for(var t,r=[],o=arguments.length;o--;)r[o]=arguments[o];return(t=e)["get"+(e.getGroupValues?"Group":"Form")+"Values"].apply(t,[e].concat(r))},name:e.context.name}].concat(o)));return(n=n instanceof Promise?n:Promise.resolve(n)).then((function(t){return!t&&e.getMessage(i,o)}))};return new Promise((function(e){var o=function(t,i){void 0===i&&(i=[]);var n=t.shift();Array.isArray(n)&&n.length?Promise.all(n.map(r)).then((function(t){return t.filter((function(t){return!!t}))})).then((function(r){return(r=Array.isArray(r)?r:[]).length&&n.bail||!t.length?e(i.concat(r).filter((function(t){return!formulate_esm_v(t)}))):o(t,i.concat(r))})):e([])};o(function(t){var e=[],r=t.findIndex((function(t){return"bail"===t[2].toLowerCase()})),o=t.findIndex((function(t){return"optional"===t[2].toLowerCase()}));if(o>=0){var i=t.splice(o,1);e.push(Object.defineProperty(i,"bail",{value:!0}))}if(r>=0){var n=t.splice(0,r+1).slice(0,-1);n.length&&e.push(n),t.map((function(t){return e.push(Object.defineProperty([t],"bail",{value:!0}))}))}else e.push(t);return e.reduce((function(t,e){var r=function(t,e){if(void 0===e&&(e=!1),t.length<2)return Object.defineProperty([t],"bail",{value:e});var o=[],i=t.findIndex((function(t){return"^"===t[3]}));if(i>=0){var n=t.splice(0,i);n.length&&o.push.apply(o,r(n,e)),o.push(Object.defineProperty([t.shift()],"bail",{value:!0})),t.length&&o.push.apply(o,r(t,e))}else o.push(t);return o};return t.concat(r(e))}),[])}(t))}))},didValidate:function(t){var e=!formulate_esm_a(t,this.validationErrors);if(this.validationErrors=t,e){var r=this.getErrorObject();this.$emit("validation",r),this.formulateFieldValidation&&"function"==typeof this.formulateFieldValidation&&this.formulateFieldValidation(r)}},getMessage:function(t,e){var r=this;return this.getMessageFunc(t)({args:e,name:this.mergedValidationName,value:this.context.model,vm:this,formValues:this.getFormValues(this),getFormValues:function(){for(var t,e=[],o=arguments.length;o--;)e[o]=arguments[o];return(t=r).getFormValues.apply(t,[r].concat(e))},getGroupValues:function(){for(var t,e=[],o=arguments.length;o--;)e[o]=arguments[o];return(t=r)["get"+(r.getGroupValues?"Group":"Form")+"Values"].apply(t,[r].concat(e))}})},getMessageFunc:function(t){var e=this;if("optional"===(t=formulate_esm_l(t)))return function(){return[]};if(this.messages&&void 0!==this.messages[t])switch(typeof this.messages[t]){case"function":return this.messages[t];case"string":case"boolean":return function(){return e.messages[t]}}return function(r){return e.$formulate.validationMessage(t,r,e)}},hasValidationErrors:function(){var t=this;return new Promise((function(e){t.$nextTick((function(){t.pendingValidation.then((function(){return e(!!t.validationErrors.length)}))}))}))},getValidationErrors:function(){var t=this;return new Promise((function(e){t.$nextTick((function(){return t.pendingValidation.then((function(){return e(t.getErrorObject())}))}))}))},getErrorObject:function(){return{name:this.context.nameOrFallback||this.context.name,errors:this.validationErrors.filter((function(t){return"string"==typeof t})),hasErrors:!!this.validationErrors.length}},setErrors:function(t){this.localErrors=formulate_esm_c(t)},setGroupErrors:function(t){this.localGroupErrors=t},registerRule:function(t,e,r,o){void 0===o&&(o=null),this.ruleRegistry.some((function(t){return t[2]===r}))||(this.ruleRegistry.push([t,e,r]),null!==o&&(this.messageRegistry[r]=o))},removeRule:function(t){var e=this.ruleRegistry.findIndex((function(e){return e[2]===t}));e>=0&&(this.ruleRegistry.splice(e,1),delete this.messageRegistry[t])}}},formulate_esm_Q=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.outer,attrs:{"data-classification":t.classification,"data-has-errors":t.hasErrors,"data-is-showing-errors":t.hasVisibleErrors,"data-has-value":t.hasValue,"data-type":t.type}},[r("div",{class:t.context.classes.wrapper},["before"===t.context.labelPosition?t._t("label",[t.context.hasLabel?r(t.context.slotComponents.label,t._b({tag:"component",attrs:{context:t.context}},"component",t.context.slotProps.label,!1)):t._e()],null,t.context):t._e(),t._v(" "),"before"===t.context.helpPosition?t._t("help",[t.context.help?r(t.context.slotComponents.help,t._b({tag:"component",attrs:{context:t.context}},"component",t.context.slotProps.help,!1)):t._e()],null,t.context):t._e(),t._v(" "),t._t("element",[r(t.context.component,t._g(t._b({tag:"component",attrs:{context:t.context}},"component",t.context.slotProps.component,!1),t.listeners),[t._t("default",null,null,t.context)],2)],null,t.context),t._v(" "),"after"===t.context.labelPosition?t._t("label",[t.context.hasLabel?r(t.context.slotComponents.label,t._b({tag:"component",attrs:{context:t.context}},"component",t.context.slotProps.label,!1)):t._e()],null,t.context):t._e()],2),t._v(" "),"after"===t.context.helpPosition?t._t("help",[t.context.help?r(t.context.slotComponents.help,t._b({tag:"component",attrs:{context:t.context}},"component",t.context.slotProps.help,!1)):t._e()],null,t.context):t._e(),t._v(" "),t._t("errors",[t.context.disableErrors?t._e():r(t.context.slotComponents.errors,t._b({tag:"component",attrs:{type:"FormulateErrors"===t.context.slotComponents.errors&&"input",context:t.context}},"component",t.context.slotProps.errors,!1))],null,t.context)],2)};formulate_esm_Q._withStripped=!0;var tt=formulate_esm_U({render:formulate_esm_Q,staticRenderFns:[]},void 0,formulate_esm_X,void 0,!1,void 0,!1,void 0,void 0,void 0),et={inject:{observeErrors:{default:!1},removeErrorObserver:{default:!1},observeContext:{default:!1},removeContextObserver:{default:!1}},props:{context:{type:Object,default:function(){return{}}},type:{type:String,default:"form"}},data:function(){return{boundSetErrors:this.setErrors.bind(this),boundSetFormContext:this.setFormContext.bind(this),localErrors:[],formContext:{classes:{formErrors:"formulate-form-errors",formError:"formulate-form-error"}}}},computed:{visibleValidationErrors:function(){return Array.isArray(this.context.visibleValidationErrors)?this.context.visibleValidationErrors:[]},errors:function(){return Array.isArray(this.context.errors)?this.context.errors:[]},mergedErrors:function(){return this.errors.concat(this.localErrors)},visibleErrors:function(){return Array.from(new Set(this.mergedErrors.concat(this.visibleValidationErrors))).filter((function(t){return"string"==typeof t}))},outerClass:function(){return"input"===this.type&&this.context.classes?this.context.classes.errors:this.formContext.classes.formErrors},itemClass:function(){return"input"===this.type&&this.context.classes?this.context.classes.error:this.formContext.classes.formError},role:function(){return"form"===this.type?"alert":"status"},ariaLive:function(){return"form"===this.type?"assertive":"polite"},slotComponent:function(){return this.$formulate.slotComponent(null,"errorList")}},created:function(){"form"===this.type&&"function"==typeof this.observeErrors&&(Array.isArray(this.context.errors)||this.observeErrors({callback:this.boundSetErrors,type:"form"}),this.observeContext(this.boundSetFormContext))},destroyed:function(){"form"===this.type&&"function"==typeof this.removeErrorObserver&&(Array.isArray(this.context.errors)||this.removeErrorObserver(this.boundSetErrors),this.removeContextObserver(this.boundSetFormContext))},methods:{setErrors:function(t){this.localErrors=formulate_esm_c(t)},setFormContext:function(t){this.formContext=t}}},rt=function(){var t=this.$createElement;return(this._self._c||t)(this.slotComponent,{tag:"component",attrs:{"visible-errors":this.visibleErrors,"item-class":this.itemClass,"outer-class":this.outerClass,role:this.role,"aria-live":this.ariaLive,type:this.type}})};rt._withStripped=!0;var ot=formulate_esm_U({render:rt,staticRenderFns:[]},void 0,et,void 0,!1,void 0,!1,void 0,void 0,void 0),it={props:{context:{type:Object,required:!0}}},nt=function(){var t=this.$createElement,e=this._self._c||t;return this.context.help?e("div",{class:this.context.classes.help,attrs:{id:this.context.id+"-help"},domProps:{textContent:this._s(this.context.help)}}):this._e()};nt._withStripped=!0;var st=formulate_esm_U({render:nt,staticRenderFns:[]},void 0,it,void 0,!1,void 0,!1,void 0,void 0,void 0),at={props:{file:{type:Object,required:!0},imagePreview:{type:Boolean,default:!1},context:{type:Object,required:!0}}},lt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.file},[t.imagePreview&&t.file.previewData?r("div",{class:t.context.classes.fileImagePreview},[r("img",{class:t.context.classes.fileImagePreviewImage,attrs:{src:t.file.previewData}})]):t._e(),t._v(" "),r("div",{class:t.context.classes.fileName,attrs:{title:t.file.name},domProps:{textContent:t._s(t.file.name)}}),t._v(" "),!1!==t.file.progress?r("div",{class:t.context.classes.fileProgress,attrs:{"data-just-finished":t.file.justFinished,"data-is-finished":!t.file.justFinished&&t.file.complete}},[r("div",{class:t.context.classes.fileProgressInner,style:{width:t.file.progress+"%"}})]):t._e(),t._v(" "),t.file.complete&&!t.file.justFinished||!1===t.file.progress?r("div",{class:t.context.classes.fileRemove,on:{click:t.file.removeFile}}):t._e()])};lt._withStripped=!0;var ut=formulate_esm_U({render:lt,staticRenderFns:[]},void 0,at,void 0,!1,void 0,!1,void 0,void 0,void 0),ct={name:"FormulateGrouping",props:{context:{type:Object,required:!0}},provide:function(){return{isSubField:function(){return!0},registerProvider:this.registerProvider,deregisterProvider:this.deregisterProvider}},data:function(){return{providers:[],keys:[]}},inject:["formulateRegisterRule","formulateRemoveRule"],computed:{items:function(){var t=this;return Array.isArray(this.context.model)?this.context.repeatable||0!==this.context.model.length?this.context.model.length<this.context.minimum?new Array(this.context.minimum||1).fill("").map((function(e,r){return t.setId(t.context.model[r]||{},r)})):this.context.model.map((function(e,r){return t.setId(e,r)})):[this.setId({},0)]:new Array(this.context.minimum||1).fill("").map((function(e,r){return t.setId({},r)}))},formShouldShowErrors:function(){return this.context.formShouldShowErrors},groupErrors:function(){var t=this;return this.items.map((function(e,r){return formulate_esm_f(t.context.groupErrors,r)?t.context.groupErrors[r]:{}}))}},watch:{providers:function(){this.formShouldShowErrors&&this.showErrors()},formShouldShowErrors:function(t){t&&this.showErrors()},items:{handler:function(t,e){formulate_esm_a(t,e,!0)||(this.keys=t.map((function(t){return t.__id})))},immediate:!0}},created:function(){this.formulateRegisterRule(this.validateGroup.bind(this),[],"formulateGrouping",!0)},destroyed:function(){this.formulateRemoveRule("formulateGrouping")},methods:{validateGroup:function(){return Promise.all(this.providers.reduce((function(t,e){return e&&"function"==typeof e.hasValidationErrors&&t.push(e.hasValidationErrors()),t}),[])).then((function(t){return!t.some((function(t){return!!t}))}))},showErrors:function(){this.providers.forEach((function(t){return t&&"function"==typeof t.showErrors&&t.showErrors()}))},setItem:function(t,e){var r=this;Array.isArray(this.context.model)&&this.context.model.length>=this.context.minimum&&!this.context.model.__init?this.context.model.splice(t,1,this.setId(e,t)):this.context.model=this.items.map((function(o,i){return i===t?r.setId(e,t):o}))},removeItem:function(t){var e=this;Array.isArray(this.context.model)&&this.context.model.length>this.context.minimum?(this.context.model=this.context.model.filter((function(e,r){return r!==t&&e})),this.context.rootEmit("repeatableRemoved",this.context.model)):!Array.isArray(this.context.model)&&this.items.length>this.context.minimum&&(this.context.model=new Array(this.items.length-1).fill("").map((function(t,r){return e.setId({},r)})),this.context.rootEmit("repeatableRemoved",this.context.model))},registerProvider:function(t){this.providers.some((function(e){return e===t}))||this.providers.push(t)},deregisterProvider:function(t){this.providers=this.providers.filter((function(e){return e!==t}))},setId:function(t,e){return t.__id?t:formulate_esm_m(t,this.keys[e])}}},dt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("FormulateSlot",{class:t.context.classes.grouping,attrs:{name:"grouping",context:t.context,"force-wrap":t.context.repeatable}},t._l(t.items,(function(e,o){return r("FormulateRepeatableProvider",{key:e.__id,attrs:{index:o,context:t.context,uuid:e.__id,errors:t.groupErrors[o]},on:{remove:t.removeItem,input:function(e){return t.setItem(o,e)}}},[t._t("default")],2)})),1)};dt._withStripped=!0;var pt=formulate_esm_U({render:dt,staticRenderFns:[]},void 0,ct,void 0,!1,void 0,!1,void 0,void 0,void 0),ht={props:{context:{type:Object,required:!0}}},ft=function(){var t=this.$createElement;return(this._self._c||t)("label",{class:this.context.classes.label,attrs:{id:this.context.id+"_label",for:this.context.id},domProps:{textContent:this._s(this.context.label)}})};ft._withStripped=!0;var mt=formulate_esm_U({render:ft,staticRenderFns:[]},void 0,ht,void 0,!1,void 0,!1,void 0,void 0,void 0),vt={props:{context:{type:Object,required:!0},addMore:{type:Function,required:!0}}},xt=function(){var t=this.$createElement,e=this._self._c||t;return e("div",{class:this.context.classes.groupAddMore},[e("FormulateInput",{attrs:{type:"button",label:this.context.addLabel,"data-minor":"","data-ghost":""},on:{click:this.addMore}})],1)};xt._withStripped=!0;var yt=formulate_esm_U({render:xt,staticRenderFns:[]},void 0,vt,void 0,!1,void 0,!1,void 0,void 0,void 0),gt={props:{context:{type:Object,required:!0}},computed:{type:function(){return this.context.type},attributes:function(){return this.context.attributes||{}},hasValue:function(){return this.context.hasValue}}},bt={name:"FormulateInputBox",mixins:[gt],computed:{usesDecorator:function(){return this.$formulate.options.useInputDecorators}}},Et=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),"radio"===t.type?r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"radio"},domProps:{value:t.context.value,checked:t._q(t.context.model,t.context.value)},on:{blur:t.context.blurHandler,change:function(e){return t.$set(t.context,"model",t.context.value)}}},"input",t.attributes,!1),t.$listeners)):r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"checkbox"},domProps:{value:t.context.value,checked:Array.isArray(t.context.model)?t._i(t.context.model,t.context.value)>-1:t.context.model},on:{blur:t.context.blurHandler,change:function(e){var r=t.context.model,o=e.target,i=!!o.checked;if(Array.isArray(r)){var n=t.context.value,s=t._i(r,n);o.checked?s<0&&t.$set(t.context,"model",r.concat([n])):s>-1&&t.$set(t.context,"model",r.slice(0,s).concat(r.slice(s+1)))}else t.$set(t.context,"model",i)}}},"input",t.attributes,!1),t.$listeners)),t._v(" "),t.usesDecorator?r("label",{tag:"component",class:t.context.classes.decorator,attrs:{for:t.attributes.id}}):t._e(),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};Et._withStripped=!0;var _t=formulate_esm_U({render:Et,staticRenderFns:[]},void 0,bt,void 0,!1,void 0,!1,void 0,void 0,void 0),Ft={props:{visibleErrors:{type:Array,required:!0},itemClass:{type:[String,Array,Object,Boolean],default:!1},outerClass:{type:[String,Array,Object,Boolean],default:!1},role:{type:[String],default:"status"},ariaLive:{type:[String,Boolean],default:"polite"},type:{type:String,required:!0}}},wt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return t.visibleErrors.length?r("ul",{class:t.outerClass},t._l(t.visibleErrors,(function(e){return r("li",{key:e,class:t.itemClass,attrs:{role:t.role,"aria-live":t.ariaLive},domProps:{textContent:t._s(e)}})})),0):t._e()};wt._withStripped=!0;var Ot=formulate_esm_U({render:wt,staticRenderFns:[]},void 0,Ft,void 0,!1,void 0,!1,void 0,void 0,void 0),Pt={name:"FormulateInputText",mixins:[gt]},Vt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),"checkbox"===t.type?r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"checkbox"},domProps:{checked:Array.isArray(t.context.model)?t._i(t.context.model,null)>-1:t.context.model},on:{blur:t.context.blurHandler,change:function(e){var r=t.context.model,o=e.target,i=!!o.checked;if(Array.isArray(r)){var n=t._i(r,null);o.checked?n<0&&t.$set(t.context,"model",r.concat([null])):n>-1&&t.$set(t.context,"model",r.slice(0,n).concat(r.slice(n+1)))}else t.$set(t.context,"model",i)}}},"input",t.attributes,!1),t.$listeners)):"radio"===t.type?r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"radio"},domProps:{checked:t._q(t.context.model,null)},on:{blur:t.context.blurHandler,change:function(e){return t.$set(t.context,"model",null)}}},"input",t.attributes,!1),t.$listeners)):r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:t.type},domProps:{value:t.context.model},on:{blur:t.context.blurHandler,input:function(e){e.target.composing||t.$set(t.context,"model",e.target.value)}}},"input",t.attributes,!1),t.$listeners)),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};Vt._withStripped=!0;var At=formulate_esm_U({render:Vt,staticRenderFns:[]},void 0,Pt,void 0,!1,void 0,!1,void 0,void 0,void 0),St={name:"FormulateFiles",props:{files:{type:formulate_esm_y,required:!0},imagePreview:{type:Boolean,default:!1},context:{type:Object,required:!0}},computed:{fileUploads:function(){return this.files.files||[]},isMultiple:function(){return formulate_esm_f(this.context.attributes,"multiple")}},watch:{files:function(){this.imagePreview&&this.files.loadPreviews()}},mounted:function(){this.imagePreview&&this.files.loadPreviews()},methods:{appendFiles:function(){var t=this.$refs.addFiles;t.files.length&&this.files.mergeFileList(t)}}},jt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return t.fileUploads.length?r("ul",{class:t.context.classes.files},[t._l(t.fileUploads,(function(e){return r("li",{key:e.uuid,attrs:{"data-has-error":!!e.error,"data-has-preview":!(!t.imagePreview||!e.previewData)}},[r("FormulateSlot",{attrs:{name:"file",context:t.context,file:e,"image-preview":t.imagePreview}},[r(t.context.slotComponents.file,{tag:"component",attrs:{context:t.context,file:e,"image-preview":t.imagePreview}})],1),t._v(" "),e.error?r("div",{class:t.context.classes.fileUploadError,domProps:{textContent:t._s(e.error)}}):t._e()],1)})),t._v(" "),t.isMultiple&&t.context.addLabel?r("div",{class:t.context.classes.fileAdd,attrs:{role:"button"}},[t._v("\n    "+t._s(t.context.addLabel)+"\n    "),r("input",{ref:"addFiles",class:t.context.classes.fileAddInput,attrs:{type:"file",multiple:""},on:{change:t.appendFiles}})]):t._e()],2):t._e()};jt._withStripped=!0;var $t={name:"FormulateInputFile",components:{FormulateFiles:formulate_esm_U({render:jt,staticRenderFns:[]},void 0,St,void 0,!1,void 0,!1,void 0,void 0,void 0)},mixins:[gt],data:function(){return{isOver:!1}},computed:{hasFiles:function(){return!!(this.context.model instanceof formulate_esm_y&&this.context.model.files.length)}},created:function(){Array.isArray(this.context.model)&&"string"==typeof this.context.model[0][this.$formulate.getFileUrlKey()]&&(this.context.model=this.$formulate.createUpload({files:this.context.model},this.context))},mounted:function(){window&&this.context.preventWindowDrops&&(window.addEventListener("dragover",this.preventDefault),window.addEventListener("drop",this.preventDefault))},destroyed:function(){window&&this.context.preventWindowDrops&&(window.removeEventListener("dragover",this.preventDefault),window.removeEventListener("drop",this.preventDefault))},methods:{preventDefault:function(t){"INPUT"!==t.target.tagName&&"file"!==t.target.getAttribute("type")&&(t=t||event).preventDefault()},handleFile:function(){var t=this;this.isOver=!1;var e=this.$refs.file;e.files.length&&(this.context.model=this.$formulate.createUpload(e,this.context),this.$nextTick((function(){return t.attemptImmediateUpload()})))},attemptImmediateUpload:function(){var t=this;"live"===this.context.uploadBehavior&&this.context.model instanceof formulate_esm_y&&this.context.hasValidationErrors().then((function(e){e||t.context.model.upload()}))},handleDragOver:function(t){t.preventDefault(),this.isOver=!0},handleDragLeave:function(t){t.preventDefault(),this.isOver=!1}}},Ct=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type,"data-has-files":t.hasFiles}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),r("div",{class:t.context.classes.uploadArea,attrs:{"data-has-files":t.hasFiles}},[r("input",t._g(t._b({ref:"file",attrs:{"data-is-drag-hover":t.isOver,type:"file"},on:{blur:t.context.blurHandler,change:t.handleFile,dragover:t.handleDragOver,dragleave:t.handleDragLeave}},"input",t.attributes,!1),t.$listeners)),t._v(" "),r("FormulateSlot",{attrs:{name:"uploadAreaMask",context:t.context,"has-files":t.hasFiles}},[r(t.context.slotComponents.uploadAreaMask,{directives:[{name:"show",rawName:"v-show",value:!t.hasFiles,expression:"!hasFiles"}],tag:"component",class:t.context.classes.uploadAreaMask,attrs:{"has-files":"div"!==t.context.slotComponents.uploadAreaMask&&t.hasFiles,"data-has-files":"div"===t.context.slotComponents.uploadAreaMask&&t.hasFiles}})],1),t._v(" "),t.hasFiles?r("FormulateFiles",{attrs:{files:t.context.model,"image-preview":"image"===t.context.type&&"preview"===t.context.imageBehavior,context:t.context}}):t._e()],1),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};Ct._withStripped=!0;var kt=formulate_esm_U({render:Ct,staticRenderFns:[]},void 0,$t,void 0,!1,void 0,!1,void 0,void 0,void 0),It={props:{context:{type:Object,required:!0},removeItem:{type:Function,required:!0},index:{type:Number,required:!0}}},Rt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.groupRepeatable},["after"===t.context.removePosition?t._t("default"):t._e(),t._v(" "),r("FormulateSlot",{attrs:{name:"remove",context:t.context,index:t.index,"remove-item":t.removeItem}},[r(t.context.slotComponents.remove,t._b({tag:"component",attrs:{context:t.context,index:t.index,"remove-item":t.removeItem}},"component",t.context.slotProps.remove,!1))],1),t._v(" "),"before"===t.context.removePosition?t._t("default"):t._e()],2)};Rt._withStripped=!0;var Dt=formulate_esm_U({render:Rt,staticRenderFns:[]},void 0,It,void 0,!1,void 0,!1,void 0,void 0,void 0);function Lt(t,e){var r={};for(var o in t)Object.prototype.hasOwnProperty.call(t,o)&&-1===e.indexOf(o)&&(r[o]=t[o]);return r}var Nt={name:"FormulateInputGroup",props:{context:{type:Object,required:!0}},computed:{options:function(){return this.context.options||[]},subType:function(){return"group"===this.context.type?"grouping":"inputs"},optionsWithContext:function(){var t=this,e=this.context,r=e.attributes,o=(r.id,Lt(r,["id"])),i=(e.blurHandler,e.classification,e.component,e.getValidationErrors,e.hasLabel,e.hasValidationErrors,e.isSubField,e.isValid,e.labelPosition,e.options,e.performValidation,e.setErrors,e.slotComponents,e.slotProps,e.validationErrors,e.visibleValidationErrors,e.classes,e.showValidationErrors,e.rootEmit,e.help,e.pseudoProps,e.rules,e.model,Lt(e,["attributes","blurHandler","classification","component","getValidationErrors","hasLabel","hasValidationErrors","isSubField","isValid","labelPosition","options","performValidation","setErrors","slotComponents","slotProps","validationErrors","visibleValidationErrors","classes","showValidationErrors","rootEmit","help","pseudoProps","rules","model"]));return this.options.map((function(e){return t.groupItemContext(i,e,o)}))},totalItems:function(){return Array.isArray(this.context.model)&&this.context.model.length>this.context.minimum?this.context.model.length:this.context.minimum||1},canAddMore:function(){return this.context.repeatable&&this.totalItems<this.context.limit},labelledBy:function(){return this.context.label&&this.context.id+"_label"}},methods:{addItem:function(){if(Array.isArray(this.context.model))for(var t=this.context.minimum-this.context.model.length+1,e=Math.max(t,1),r=0;r<e;r++)this.context.model.push(formulate_esm_m({}));else this.context.model=new Array(this.totalItems+1).fill("").map((function(){return formulate_esm_m({})}));this.context.rootEmit("repeatableAdded",this.context.model)},groupItemContext:function(t,e,r){return Object.assign({},t,e,r,{isGrouped:!0},t.hasGivenName?{}:{name:!0})}}},Bt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-is-repeatable":t.context.repeatable,role:"group","aria-labelledby":t.labelledBy}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),"grouping"!==t.subType?t._l(t.optionsWithContext,(function(e){return r("FormulateInput",t._b({key:e.id,staticClass:"formulate-input-group-item",attrs:{"disable-errors":!0,"prevent-deregister":!0},on:{blur:t.context.blurHandler},model:{value:t.context.model,callback:function(e){t.$set(t.context,"model",e)},expression:"context.model"}},"FormulateInput",e,!1))})):[r("FormulateGrouping",{attrs:{context:t.context}},[t._t("default")],2),t._v(" "),t.canAddMore?r("FormulateSlot",{attrs:{name:"addmore",context:t.context,"add-more":t.addItem}},[r(t.context.slotComponents.addMore,t._b({tag:"component",attrs:{context:t.context,"add-more":t.addItem},on:{add:t.addItem}},"component",t.context.slotProps.addMore,!1))],1):t._e()],t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],2)};Bt._withStripped=!0;var Mt=formulate_esm_U({render:Bt,staticRenderFns:[]},void 0,Nt,void 0,!1,void 0,!1,void 0,void 0,void 0),Ut={name:"FormulateInputButton",mixins:[gt]},Gt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),r("button",t._g(t._b({attrs:{type:t.type}},"button",t.attributes,!1),t.$listeners),[t._t("default",[r(t.context.slotComponents.buttonContent,{tag:"component",attrs:{context:t.context}})],{context:t.context})],2),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};Gt._withStripped=!0;var Tt=formulate_esm_U({render:Gt,staticRenderFns:[]},void 0,Ut,void 0,!1,void 0,!1,void 0,void 0,void 0),qt={name:"FormulateInputSelect",mixins:[gt],computed:{options:function(){return this.context.options||{}},optionGroups:function(){return this.context.optionGroups||!1},placeholderSelected:function(){return!(this.hasValue||!this.context.attributes||!this.context.attributes.placeholder)}}},Ht=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type,"data-multiple":t.attributes&&void 0!==t.attributes.multiple}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),r("select",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{"data-placeholder-selected":t.placeholderSelected},on:{blur:t.context.blurHandler,change:function(e){var r=Array.prototype.filter.call(e.target.options,(function(t){return t.selected})).map((function(t){return"_value"in t?t._value:t.value}));t.$set(t.context,"model",e.target.multiple?r:r[0])}}},"select",t.attributes,!1),t.$listeners),[t.context.placeholder?r("option",{attrs:{value:"",hidden:"hidden",disabled:""},domProps:{selected:!t.hasValue}},[t._v("\n      "+t._s(t.context.placeholder)+"\n    ")]):t._e(),t._v(" "),t.optionGroups?t._l(t.optionGroups,(function(e,o){return r("optgroup",{key:o,attrs:{label:o}},t._l(e,(function(e){return r("option",t._b({key:e.id,attrs:{disabled:!!e.disabled},domProps:{value:e.value,textContent:t._s(e.label)}},"option",e.attributes||e.attrs||{},!1))})),0)})):t._l(t.options,(function(e){return r("option",t._b({key:e.id,attrs:{disabled:!!e.disabled},domProps:{value:e.value,textContent:t._s(e.label)}},"option",e.attributes||e.attrs||{},!1))}))],2),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};Ht._withStripped=!0;var Wt=formulate_esm_U({render:Ht,staticRenderFns:[]},void 0,qt,void 0,!1,void 0,!1,void 0,void 0,void 0),Yt={name:"FormulateInputSlider",mixins:[gt]},zt=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":t.context.type}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),"checkbox"===t.type?r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"checkbox"},domProps:{checked:Array.isArray(t.context.model)?t._i(t.context.model,null)>-1:t.context.model},on:{blur:t.context.blurHandler,change:function(e){var r=t.context.model,o=e.target,i=!!o.checked;if(Array.isArray(r)){var n=t._i(r,null);o.checked?n<0&&t.$set(t.context,"model",r.concat([null])):n>-1&&t.$set(t.context,"model",r.slice(0,n).concat(r.slice(n+1)))}else t.$set(t.context,"model",i)}}},"input",t.attributes,!1),t.$listeners)):"radio"===t.type?r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:"radio"},domProps:{checked:t._q(t.context.model,null)},on:{blur:t.context.blurHandler,change:function(e){return t.$set(t.context,"model",null)}}},"input",t.attributes,!1),t.$listeners)):r("input",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],attrs:{type:t.type},domProps:{value:t.context.model},on:{blur:t.context.blurHandler,input:function(e){e.target.composing||t.$set(t.context,"model",e.target.value)}}},"input",t.attributes,!1),t.$listeners)),t._v(" "),t.context.showValue?r("div",{class:t.context.classes.rangeValue,domProps:{textContent:t._s(t.context.model)}}):t._e(),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};zt._withStripped=!0;var Kt=formulate_esm_U({render:zt,staticRenderFns:[]},void 0,Yt,void 0,!1,void 0,!1,void 0,void 0,void 0),Zt={props:{context:{type:Object,required:!0}}},Jt=function(){var t=this.$createElement;return(this._self._c||t)("span",{class:"formulate-input-element--"+this.context.type+"--label",domProps:{textContent:this._s(this.context.value||this.context.label||this.context.name||"Submit")}})};Jt._withStripped=!0;var Xt=formulate_esm_U({render:Jt,staticRenderFns:[]},void 0,Zt,void 0,!1,void 0,!1,void 0,void 0,void 0),Qt={name:"FormulateInputTextArea",mixins:[gt]},te=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{class:t.context.classes.element,attrs:{"data-type":"textarea"}},[r("FormulateSlot",{attrs:{name:"prefix",context:t.context}},[t.context.slotComponents.prefix?r(t.context.slotComponents.prefix,{tag:"component",attrs:{context:t.context}}):t._e()],1),t._v(" "),r("textarea",t._g(t._b({directives:[{name:"model",rawName:"v-model",value:t.context.model,expression:"context.model"}],domProps:{value:t.context.model},on:{blur:t.context.blurHandler,input:function(e){e.target.composing||t.$set(t.context,"model",e.target.value)}}},"textarea",t.attributes,!1),t.$listeners)),t._v(" "),r("FormulateSlot",{attrs:{name:"suffix",context:t.context}},[t.context.slotComponents.suffix?r(t.context.slotComponents.suffix,{tag:"component",attrs:{context:t.context}}):t._e()],1)],1)};te._withStripped=!0;var ee=formulate_esm_U({render:te,staticRenderFns:[]},void 0,Qt,void 0,!1,void 0,!1,void 0,void 0,void 0),re={provide:function(){var t=this;return Object.assign({},formulate_esm_B(this,["getFormValues"]),{formulateSetter:function(e,r){return t.setGroupValue(e,r)}})},inject:{registerProvider:"registerProvider",deregisterProvider:"deregisterProvider"},props:{index:{type:Number,required:!0},context:{type:Object,required:!0},uuid:{type:String,required:!0},errors:{type:Object,required:!0}},data:function(){return Object.assign({},formulate_esm_D(this),{isGrouping:!0})},computed:Object.assign({},formulate_esm_L(),{mergedFieldErrors:function(){return this.errors}}),watch:Object.assign({},{mergedFieldErrors:{handler:function(t){this.errorObservers.filter((function(t){return"input"===t.type})).forEach((function(e){return e.callback(t[e.field]||[])}))},immediate:!0},mergedGroupErrors:{handler:function(t){this.errorObservers.filter((function(t){return"group"===t.type})).forEach((function(e){return e.callback(t[e.field]||{})}))},immediate:!0}},{"context.model":{handler:function(t){formulate_esm_a(t[this.index],this.proxy,!0)||this.setValues(t[this.index])},deep:!0}}),created:function(){this.applyInitialValues(),this.registerProvider(this)},beforeDestroy:function(){this.preventCleanup=!0,this.deregisterProvider(this)},methods:Object.assign({},formulate_esm_N(),{setGroupValue:function(t,e){formulate_esm_a(this.proxy[t],e,!0)||this.setFieldValue(t,e)},removeItem:function(){this.$emit("remove",this.index)}})},oe=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("FormulateSlot",{attrs:{name:"repeatable",context:t.context,index:t.index,"remove-item":t.removeItem}},[r(t.context.slotComponents.repeatable,t._b({tag:"component",attrs:{context:t.context,index:t.index,"remove-item":t.removeItem}},"component",t.context.slotProps.repeatable,!1),[r("FormulateSlot",{attrs:{context:t.context,index:t.index,name:"default"}})],1)],1)};oe._withStripped=!0;var ie=formulate_esm_U({render:oe,staticRenderFns:[]},void 0,re,void 0,!1,void 0,!1,void 0,void 0,void 0),ne={props:{index:{type:Number,default:null},context:{type:Object,required:!0},removeItem:{type:Function,required:!0}}},se=function(){var t=this,e=t.$createElement,r=t._self._c||e;return t.context.repeatable?r("a",{class:t.context.classes.groupRepeatableRemove,attrs:{"data-disabled":t.context.model.length<=t.context.minimum,role:"button"},domProps:{textContent:t._s(t.context.removeLabel)},on:{click:function(e){return e.preventDefault(),t.removeItem(e)},keypress:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.removeItem(e)}}}):t._e()};se._withStripped=!0;var ae=formulate_esm_U({render:se,staticRenderFns:[]},void 0,ne,void 0,!1,void 0,!1,void 0,void 0,void 0),le=function(){this.options={},this.defaults={components:{FormulateSlot:formulate_esm_$,FormulateForm:formulate_esm_q,FormulateFile:ut,FormulateHelp:st,FormulateLabel:mt,FormulateInput:tt,FormulateErrors:ot,FormulateSchema:formulate_esm_k,FormulateAddMore:yt,FormulateGrouping:pt,FormulateInputBox:_t,FormulateInputText:At,FormulateInputFile:kt,FormulateErrorList:Ot,FormulateRepeatable:Dt,FormulateInputGroup:Mt,FormulateInputButton:Tt,FormulateInputSelect:Wt,FormulateInputSlider:Kt,FormulateButtonContent:Xt,FormulateInputTextArea:ee,FormulateRepeatableRemove:ae,FormulateRepeatableProvider:ie},slotComponents:{addMore:"FormulateAddMore",buttonContent:"FormulateButtonContent",errorList:"FormulateErrorList",errors:"FormulateErrors",file:"FormulateFile",help:"FormulateHelp",label:"FormulateLabel",prefix:!1,remove:"FormulateRepeatableRemove",repeatable:"FormulateRepeatable",suffix:!1,uploadAreaMask:"div"},slotProps:{},library:formulate_esm_n,rules:formulate_esm_b,mimes:_,locale:!1,uploader:formulate_esm_S,uploadUrl:!1,fileUrlKey:"url",uploadJustCompleteDuration:1e3,errorHandler:function(t){return t},plugins:[c],locales:{},failedValidation:function(){return!1},idPrefix:"formulate-",baseClasses:function(t){return t},coreClasses:formulate_esm_A,classes:{},useInputDecorators:!0,validationNameStrategy:!1},this.registry=new Map,this.idRegistry={}};le.prototype.install=function(t,e){var r=this;t.prototype.$formulate=this,this.options=this.defaults;var o=this.defaults.plugins;for(var i in e&&Array.isArray(e.plugins)&&e.plugins.length&&(o=o.concat(e.plugins)),o.forEach((function(t){return"function"==typeof t?t(r):null})),this.extend(e||{}),this.options.components)t.component(i,this.options.components[i])},le.prototype.nextId=function(t){var e=!(!t.$route||!t.$route.path)&&t.$route.path?t.$route.path.replace(/[/\\.\s]/g,"-"):"global";return Object.prototype.hasOwnProperty.call(this.idRegistry,e)||(this.idRegistry[e]=0),""+this.options.idPrefix+e+"-"+ ++this.idRegistry[e]},le.prototype.extend=function(t){if("object"==typeof t)return this.options=this.merge(this.options,t),this;throw new Error("Formulate.extend expects an object, was "+typeof t)},le.prototype.merge=function(t,e,o){void 0===o&&(o=!0);var i={};for(var n in t)e.hasOwnProperty(n)?index_es(e[n])&&index_es(t[n])?i[n]=this.merge(t[n],e[n],o):o&&Array.isArray(t[n])&&Array.isArray(e[n])?i[n]=t[n].concat(e[n]):i[n]=e[n]:i[n]=t[n];for(var s in e)i.hasOwnProperty(s)||(i[s]=e[s]);return i},le.prototype.classify=function(t){return this.options.library.hasOwnProperty(t)?this.options.library[t].classification:"unknown"},le.prototype.classes=function(t){var e=this,r=this.options.coreClasses(t),o=this.options.baseClasses(r,t);return Object.keys(o).reduce((function(r,i){var n,s=formulate_esm_V(o[i],e.options.classes[i],t);return s=function(t,e,r,o){return Object.keys(formulate_esm_w).reduce((function(e,i){if(formulate_esm_w[i](o)){var n=""+t+formulate_esm_u(i),s=n+"Class";if(r[n])e=formulate_esm_V(e,"string"==typeof r[n]?formulate_esm_c(r[n]):r[n],o);if(o[s])e=formulate_esm_V(e,"string"==typeof o[s]?formulate_esm_c(o[s]):o[n+"Class"],o)}return e}),e)}(i,s=formulate_esm_V(s,t[i+"Class"],t),e.options.classes,t),Object.assign(r,((n={})[i]=s,n))}),{})},le.prototype.typeProps=function(t){var e=function(t){return Object.keys(t).reduce((function(e,r){return Array.isArray(t[r])?e.concat(t[r]):e}),[])},r=e(this.options.slotProps);return this.options.library[t]?r.concat(e(this.options.library[t].slotProps||{})):r},le.prototype.slotProps=function(t,e,r){var o=Array.isArray(this.options.slotProps[e])?this.options.slotProps[e]:[],i=this.options.library[t];return i&&i.slotProps&&Array.isArray(i.slotProps[e])&&(o=o.concat(i.slotProps[e])),o.reduce((function(t,e){var o;return Object.assign(t,((o={})[e]=r[e],o))}),{})},le.prototype.component=function(t){return!!this.options.library.hasOwnProperty(t)&&this.options.library[t].component},le.prototype.slotComponent=function(t,e){var r=this.options.library[t];return r&&r.slotComponents&&r.slotComponents[e]?r.slotComponents[e]:this.options.slotComponents[e]},le.prototype.rules=function(t){return void 0===t&&(t={}),Object.assign({},this.options.rules,t)},le.prototype.i18n=function(t){if(t.$i18n)switch(typeof t.$i18n.locale){case"string":return t.$i18n.locale;case"function":return t.$i18n.locale()}return!1},le.prototype.getLocale=function(t){var e=this;return this.selectedLocale||(this.selectedLocale=[this.options.locale,this.i18n(t),"en"].reduce((function(t,r){if(t)return t;if(r){var o=function(t){return t.split("-").reduce((function(t,e){return t.length&&t.unshift(t[0]+"-"+e),t.length?t:[e]}),[])}(r).find((function(t){return formulate_esm_f(e.options.locales,t)}));o&&(t=o)}return t}),!1)),this.selectedLocale},le.prototype.setLocale=function(t){formulate_esm_f(this.options.locales,t)&&(this.options.locale=t,this.selectedLocale=t,this.registry.forEach((function(t,e){t.hasValidationErrors()})))},le.prototype.validationMessage=function(t,e,r){var o=this.options.locales[this.getLocale(r)];return o.hasOwnProperty(t)?o[t](e):o.hasOwnProperty("default")?o.default(e):"Invalid field value"},le.prototype.register=function(t){"FormulateForm"===t.$options.name&&t.name&&this.registry.set(t.name,t)},le.prototype.deregister=function(t){"FormulateForm"===t.$options.name&&t.name&&this.registry.has(t.name)&&this.registry.delete(t.name)},le.prototype.handle=function(t,e,r){void 0===r&&(r=!1);var o=r?t:this.options.errorHandler(t,e);return e&&this.registry.has(e)&&this.registry.get(e).applyErrors({formErrors:formulate_esm_c(o.formErrors),inputErrors:o.inputErrors||{}}),o},le.prototype.reset=function(t,e){void 0===e&&(e={}),this.resetValidation(t),this.setValues(t,e)},le.prototype.submit=function(t){this.registry.get(t).formSubmitted()},le.prototype.resetValidation=function(t){var e=this.registry.get(t);e.hideErrors(t),e.namedErrors=[],e.namedFieldErrors={}},le.prototype.setValues=function(t,e){e&&!Array.isArray(e)&&"object"==typeof e&&this.registry.get(t).setValues(Object.assign({},e))},le.prototype.getUploader=function(){return this.options.uploader||!1},le.prototype.getUploadUrl=function(){return this.options.uploadUrl||!1},le.prototype.getFileUrlKey=function(){return this.options.fileUrlKey||"url"},le.prototype.createUpload=function(t,e){return new formulate_esm_y(t,e,this.options)},le.prototype.failedValidation=function(t){return this.options.failedValidation(this)};var ue=new le;/* harmony default export */ var formulate_esm = (ue);

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.array.map.js
var es_array_map = __webpack_require__("d81d");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.string.sub.js
var es_string_sub = __webpack_require__("4c53");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.array.iterator.js
var es_array_iterator = __webpack_require__("e260");

// EXTERNAL MODULE: ./node_modules/core-js/modules/web.dom-collections.iterator.js
var web_dom_collections_iterator = __webpack_require__("ddb0");

// EXTERNAL MODULE: ./node_modules/core-js/modules/es.function.name.js
var es_function_name = __webpack_require__("b0c0");

// CONCATENATED MODULE: ./src/utils/helpers/helpers.js





/**
 * @description Function for determining of given data is of type object
 * @param {*} data 
 * @returns Boolean determining, if data is object
 */
function matchesObjectStructure(data) {
  var dataKeys = Object.keys(data);
  var firstValue = data[dataKeys[0]];

  if (typeof firstValue === 'string' || typeof firstValue === 'number') {
    return true;
  } else {
    return false;
  }
}
/**
 * @description Function to search for all properties in inputconfiguration which provide a 'source'-property. 
 * Each name provided by a 'source'-property is saved inside the propertyNamesArray and returned as an array of property-names.
 * @param {Array} inputConfigArray Array of inputconfiguration containing information about components to render.
 * @param {Array} propertyNamesArray Array of names retrieved from components with a 'source'-property.
 * @returns {Array} Array with all names retireved (popertyNamesArray)
 */

function findPropertiesWithSources(inputConfigArray, propertyNamesArray) {
  for (var index in inputConfigArray) {
    // only subcomponents without a 'children'-property contain a 'source'-property
    // if there is a 'children'-property the current component isn't a subcomponent so 
    // the function needs be called again on this component to get to the subcomponent
    if (Object.keys(inputConfigArray[index]).includes('children')) {
      findPropertiesWithSources(inputConfigArray[index].children, propertyNamesArray);
    } else if (inputConfigArray[index].type === "conditional-input") {
      var conditionalInputKeys = Object.keys(inputConfigArray[index].data);

      for (var inputKeyIndex in conditionalInputKeys) {
        findPropertiesWithSources(inputConfigArray[index].data[conditionalInputKeys[inputKeyIndex]], propertyNamesArray);
      }
    } else {
      // not all subcomponents contain a 'source'-property
      if (Object.keys(inputConfigArray[index]).includes('source')) {
        // the 'source'-property provides a name which links to further information in the generalconfig-file
        if (!propertyNamesArray.includes(inputConfigArray[index].source.name)) {
          propertyNamesArray.push(inputConfigArray[index].source.name);
        }
      }
    }
  }

  return propertyNamesArray;
}
/**
 * 
 * @param {*} dataset 
 * @param {*} properties 
 * @param {*} translatableProperties 
 */

function setTranslation(dataset, properties, translatableProperties, languageInformation) {
  for (var propertyIndex in properties) {
    var propertyName = properties[propertyIndex];
    var propertyValue = dataset[propertyName];

    if (translatableProperties.includes(propertyName)) {
      var translationSelectoren = Object.keys(languageInformation.translation[languageInformation.locale].message.dataupload[propertyName]);

      if (propertyValue !== "") {
        if (translationSelectoren.includes(propertyValue)) {
          dataset[propertyName] = languageInformation.translation[languageInformation.locale].message.dataupload[propertyName][propertyValue];
        }
      }
    }
  }
}
// EXTERNAL MODULE: ./node_modules/core-js/modules/es.promise.js
var es_promise = __webpack_require__("e6cf");

// CONCATENATED MODULE: ./src/utils/formatter.js



/**
 * @description Fetching values from endpoint given by source (considering language settings)
 * @param {Object} values An object containing the data retrieved from endpoint
 * @param {Oject} languageInformation Object containing information about language settings
 * @returns {Object} Object of fetched values with key
 * 
 */
function DataEuropaFormatter(_x, _x2) {
  return _DataEuropaFormatter.apply(this, arguments);
}

function _DataEuropaFormatter() {
  _DataEuropaFormatter = Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee(values, languageInformation) {
    var fetchedValues, i, singleValue, valueId;
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            fetchedValues = {};

            for (i = 0; i < values.count; i++) {
              // grep result matching the local langauge setting
              singleValue = values.results[i].pref_label[languageInformation.locale]; // if there is no result available matching the local language, grep the result matching the fallback language setting

              if (singleValue === undefined) {
                singleValue = values.results[i].pref_label[languageInformation.fallbackLocale]; // if there is no result matching the local language setting or the fallback language setting grep the provided id

                if (singleValue === undefined) {
                  singleValue = values.results[i].id;
                }
              }

              valueId = values.results[i].id;
              fetchedValues[valueId] = singleValue;
            }

            return _context.abrupt("return", fetchedValues);

          case 3:
          case "end":
            return _context.stop();
        }
      }
    }, _callee);
  }));
  return _DataEuropaFormatter.apply(this, arguments);
}
// CONCATENATED MODULE: ./src/utils/adapter.js








/**
 * @description Function for retireving data dependant on given sourceInformation (fetching or reading from provided source, optional formatting)
 * @param {Object} sourceInformation Object of information about the data's source: 
 * {
 *      fecthFromEndpoint: Boolean,
 *      endpoint: "URL",
 *      values: Object,
 *      needsFormatting: Boolean,
 *      formatter: method
 * }
 * @param {Object} languageInformation Object containing local and fallback language settings
 * @returns {Object} Object of retrieved data
 */

function retrieveDataFromSource(_x, _x2) {
  return _retrieveDataFromSource.apply(this, arguments);
}

function _retrieveDataFromSource() {
  _retrieveDataFromSource = Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee(sourceInformation, languageInformation) {
    var result, status, response, _response, _response2;

    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            result = {};

            if (!(sourceInformation.fetchFromEndpoint === false)) {
              _context.next = 5;
              break;
            }

            // case 1: provided values are already in the right format
            if (sourceInformation.needsFormatting === false) {
              result = sourceInformation.values;

              if (!matchesObjectStructure(result)) {
                result = {
                  error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
                };
                console.error(languageInformation.translation[languageInformation.locale].message.error["type-error"]);
              }
            } // case 2: provided values need to be formatted
            else {
                try {
                  result = sourceInformation.formatter(sourceInformation.values);

                  if (!matchesObjectStructure(result)) {
                    result = {
                      error: "The provided data isn't of type object!"
                    };
                  }
                } catch (e) {
                  result = {
                    error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
                  };
                  console.error(e);
                }
              }

            _context.next = 65;
            break;

          case 5:
            if (!(sourceInformation.needsFormatting === false)) {
              _context.next = 24;
              break;
            }

            _context.prev = 6;
            _context.next = 9;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.status;
            });

          case 9:
            status = _context.sent;

            if (!(status === 200)) {
              _context.next = 16;
              break;
            }

            _context.next = 13;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.json();
            }).then(function (data) {
              return data.result;
            });

          case 13:
            response = _context.sent;
            result = response;

            if (!matchesObjectStructure(result)) {
              result = {
                error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
              };
              console.error(languageInformation.translation[languageInformation.locale].message.error["type-error"]);
            }

          case 16:
            _context.next = 22;
            break;

          case 18:
            _context.prev = 18;
            _context.t0 = _context["catch"](6);
            result = {
              error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
            };
            console.error(_context.t0);

          case 22:
            _context.next = 65;
            break;

          case 24:
            if (!(sourceInformation.formatter === "DataEuropa")) {
              _context.next = 42;
              break;
            }

            _context.prev = 25;
            _context.next = 28;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.status;
            });

          case 28:
            status = _context.sent;

            if (!(status === 200)) {
              _context.next = 34;
              break;
            }

            _context.next = 32;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.json();
            }).then(function (data) {
              return DataEuropaFormatter(data.result, languageInformation);
            });

          case 32:
            _response = _context.sent;
            result = _response;

          case 34:
            _context.next = 40;
            break;

          case 36:
            _context.prev = 36;
            _context.t1 = _context["catch"](25);
            result = {
              error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
            };
            console.error(_context.t1);

          case 40:
            _context.next = 65;
            break;

          case 42:
            _context.prev = 42;
            _context.next = 45;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.status;
            });

          case 45:
            status = _context.sent;

            if (!(status === 200)) {
              _context.next = 59;
              break;
            }

            _context.prev = 47;
            _context.next = 50;
            return fetch(sourceInformation.endpoint).then(function (response) {
              return response.json();
            }).then(function (data) {
              return sourceInformation.formatter(data.result);
            });

          case 50:
            _response2 = _context.sent;
            result = _response2;

            if (!matchesObjectStructure(result)) {
              result = {
                error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
              };
              console.error(languageInformation.translation[languageInformation.locale].message.error["type-error"]);
            }

            _context.next = 59;
            break;

          case 55:
            _context.prev = 55;
            _context.t2 = _context["catch"](47);
            result = {
              error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
            };
            console.error(_context.t2);

          case 59:
            _context.next = 65;
            break;

          case 61:
            _context.prev = 61;
            _context.t3 = _context["catch"](42);
            result = {
              error: languageInformation.translation[languageInformation.locale].message.error["retrieving-error"]
            };
            console.error(_context.t3);

          case 65:
            return _context.abrupt("return", result);

          case 66:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, null, [[6, 18], [25, 36], [42, 61], [47, 55]]);
  }));
  return _retrieveDataFromSource.apply(this, arguments);
}
// CONCATENATED MODULE: ./src/store/modules/datainput.js














var datainput_state = {
  // additional parameters will be defined on plugin call containing input configurations
  config: '' // will be set on component install     

};
var getters = {
  getEndpoint: function getEndpoint(state) {
    return state.config.input.jsonldendpoint;
  },
  getConfig: function getConfig(state) {
    return function (profile) {
      return state[profile];
    };
  },
  getBaseUrl: function getBaseUrl(state) {
    return state.baseUrl;
  },
  getRouteNames: function getRouteNames(state) {
    return function (configName) {
      var routes = [];
      var firstLevelRoutes = state[configName].map(function (inputObject) {
        return inputObject.routedescription;
      });

      for (var routeIndex in state[configName]) {
        var routeName = firstLevelRoutes[routeIndex];
        var subObject = state[configName].filter(function (inputObject) {
          return inputObject.routedescription === routeName;
        })[0].children;
        var subRouteArray = subObject.filter(function (inputObject) {
          return inputObject.route !== undefined;
        }).map(function (inputConfig) {
          return inputConfig.routedescription;
        });

        if (subRouteArray.length !== 0) {
          for (var subRouteIndex in subRouteArray) {
            routes.push(subRouteArray[subRouteIndex]);

            if (parseInt(subRouteIndex) === subRouteArray.length - 1) {
              routes.push("Overview");
            }
          }
        } else {
          routes.push(routeName);

          if (parseInt(routeIndex) === firstLevelRoutes.length - 1) {
            routes.push("Overview");
          }
        }
      }

      return routes;
    };
  },

  /**
   * @description Getter function collecting all available routes in order of their definition
   * @param {*} state 
   * @param {String} configName Defines parameter within state which contains input configurations and routes
   * @returns Array of route pathes
   */
  getRoutes: function getRoutes(state) {
    return function (configName) {
      var routes = [];
      var firstLevelRoutes = state[configName].map(function (inputObject) {
        return inputObject.route;
      });

      for (var routeIndex in state[configName]) {
        var routeName = firstLevelRoutes[routeIndex];
        var subObject = state[configName].filter(function (inputObject) {
          return inputObject.route === routeName;
        })[0].children;
        var subRouteArray = subObject.filter(function (inputObject) {
          return inputObject.route !== undefined;
        }).map(function (inputConfig) {
          return inputConfig.route;
        });

        if (subRouteArray.length !== 0) {
          for (var subRouteIndex in subRouteArray) {
            var currentSubRouteName = subRouteArray[subRouteIndex];
            var wholeRoute = state.baseUrl + configName + "/" + routeName + "/" + currentSubRouteName;
            routes.push(wholeRoute);

            if (parseInt(subRouteIndex) === subRouteArray.length - 1) {
              var subOverview = state.baseUrl + configName + "/" + routeName + "/overview";
              routes.push(subOverview);
            }
          }
        } else {
          var currentRoute = state.baseUrl + configName + "/" + routeName;
          routes.push(currentRoute);

          if (parseInt(routeIndex) === firstLevelRoutes.length - 1) {
            var mainOverview = state.baseUrl + configName + "/overview";
            routes.push(mainOverview);
          }
        }
      }

      return routes;
    };
  },

  /**
   * @description Function retrieving input content with already translated properties
   * @param {*} state 
   * @param {Object} routes Object containing the routes names (main, sub and subsub))
   * @returns Returns an object containing all input definitions with translated properties
   */
  getInputFields: function getInputFields(state) {
    return function (routes) {
      /**
       * @description Rekursive translation of all translatable properties contained in given data array
       * @param {Array} dataArray Array of objects containing input definitions 
       * @param {Object} languageInformation Object contain language information (locale, fallback, translations)
       */
      function translateInput(dataArray, languageInformation) {
        // look for translatable properties, if there are no translations continue without translation
        var translatableProperties = "";

        try {
          translatableProperties = Object.keys(languageInformation.translation[languageInformation.locale].message.dataupload);
        } catch (_unused) {
          console.warn("Please provide a 'dataupload'-property containing translataions for each language in the translation-file!");
        }

        if (translatableProperties) {
          for (var index in dataArray) {
            var properties = Object.keys(dataArray[index]); // some components have a 'children'-property which contains also components with translatable properties

            if (properties.includes("children")) {
              // translate current components properties
              setTranslation(dataArray[index], properties, translatableProperties, languageInformation); // continiue translating nested components properties

              translateInput(dataArray[index].children, languageInformation);
            } // conditional input fields have a data-property which contains an object which includes array of new configuration instructions 
            else if (dataArray[index].type === "conditional-input") {
                // the conditional selction field also includes properties which need to be translated
                setTranslation(dataArray[index], properties, translatableProperties, languageInformation);
                var objectKeys = Object.keys(dataArray[index].data);

                for (var objectIndex in objectKeys) {
                  translateInput(dataArray[index].data[objectKeys[objectIndex]], languageInformation);
                }
              } else {
                setTranslation(dataArray[index], properties, translatableProperties, languageInformation);
              }
          }
        }
      }

      var result = "";
      var firstLevelInputs = state[routes.main].filter(function (inputObject) {
        return inputObject.route === routes.sub;
      });

      if (routes.subsub) {
        var secondLevelInputs = firstLevelInputs[0].children.filter(function (inputObject) {
          return inputObject.route === routes.subsub;
        });
        result = secondLevelInputs[0];
        translateInput(result.children, state.config.languages);
      } else {
        result = firstLevelInputs[0];
        translateInput(result.children, state.config.languages);
      }

      return result;
    };
  }
};
var actions = {
  uploadData: function uploadData(_ref, data) {
    return Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee() {
      var state;
      return regeneratorRuntime.wrap(function _callee$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              state = _ref.state;
              _context.next = 3;
              return state.config.input.uploadMethod.method(data.values);

            case 3:
            case "end":
              return _context.stop();
          }
        }
      }, _callee);
    }))();
  },

  /**
   * @description Function for retireving all values which need to be fetched and are marked with the source-property within the config
   * @param commit
   * @returns Object of all properties which were fetched with the assigned values
   */
  getValues: function getValues(_ref2, configName) {
    return Object(asyncToGenerator["a" /* default */])( /*#__PURE__*/regeneratorRuntime.mark(function _callee2() {
      var commit, propertiesNames, resultObject, propertyIndex, property, message;
      return regeneratorRuntime.wrap(function _callee2$(_context2) {
        while (1) {
          switch (_context2.prev = _context2.next) {
            case 0:
              commit = _ref2.commit;
              // array of names retrieved from properties with a 'source'-parameter
              // these names are keys at the generalconfig-file containing additional information on how to retrieve values and from where
              propertiesNames = findPropertiesWithSources(datainput_state[configName], []);
              resultObject = {};
              _context2.t0 = regeneratorRuntime.keys(propertiesNames);

            case 4:
              if ((_context2.t1 = _context2.t0()).done) {
                _context2.next = 17;
                break;
              }

              propertyIndex = _context2.t1.value;
              property = propertiesNames[propertyIndex]; // check wether propertyNames from inputconfig match defined properties within generalconfig

              if (!(property in datainput_state.config.input)) {
                _context2.next = 13;
                break;
              }

              _context2.next = 10;
              return retrieveDataFromSource(datainput_state.config.input[property], datainput_state.config.languages);

            case 10:
              resultObject[property] = _context2.sent;
              _context2.next = 15;
              break;

            case 13:
              message = String("Parameter '".concat(property, "' is not defined within generalconfig.input!"));
              console.error(ReferenceError(message));

            case 15:
              _context2.next = 4;
              break;

            case 17:
              commit('setValues', {
                resultObject: resultObject,
                configName: configName
              });

            case 18:
            case "end":
              return _context2.stop();
          }
        }
      }, _callee2);
    }))();
  }
};
var mutations = {
  /**
   * @description
   * @param {*} state 
   * @param {Object} valueObject 
   */
  setValues: function setValues(state, valueObject) {
    /**
     * @description
     * @param {*} inputConfigArray
     * @param {*} valueObject
     */
    function findAndSetValues(inputConfigArray, valueObject) {
      for (var index in inputConfigArray) {
        if (Object.keys(inputConfigArray[index]).includes('children')) {
          findAndSetValues(inputConfigArray[index].children, valueObject);
        } else if (inputConfigArray[index].type === "conditional-input") {
          var inputObjects = Object.keys(inputConfigArray[index].data);

          for (var inputIndex in inputObjects) {
            findAndSetValues(inputConfigArray[index].data[inputObjects[inputIndex]], valueObject);
          }
        } else {
          if (Object.keys(inputConfigArray[index]).includes('source')) {
            var values = valueObject[inputConfigArray[index].source.name];

            if (Object.keys(values).includes("error")) {
              inputConfigArray[index].error = values.error;
            } else {
              inputConfigArray[index].options = values;
            }
          }
        }
      }
    }

    findAndSetValues(state[valueObject.configName], valueObject.resultObject);
  }
};
/* harmony default export */ var datainput = ({
  state: datainput_state,
  getters: getters,
  actions: actions,
  mutations: mutations
});
// CONCATENATED MODULE: ./src/dcatap-plugin.js

















var required = function required(name) {
  throw new Error("Parameter ".concat(name, " is required! This objects need to contain an json-input-configuration, a vuex store , a router and an general-configuration-object"));
};

/* harmony default export */ var dcatap_plugin = ({
  install: function install(Vue) {
    var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : required("options");

    // options containing a store, an input configuration and a general configuration is required
    if (!Object.keys(options).includes('store')) {
      throw new Error("Please provide a store named 'store'!");
    } else if (!Object.keys(options).includes("inputconfig")) {
      throw new Error("Please provide a JSON containing the input configurations nemd 'inputconfig'!");
    } else if (!Object.keys(options).includes("generalconfig")) {
      throw new Error("Please provide an object containing the general configurations named 'generalconfig'!");
    } else if (!Object.keys(options).includes('router')) {
      throw new Error("Please provide a router named 'router'");
    } else {
      // basic language settings are required
      if (!Object.keys(options.generalconfig).includes("languages")) {
        throw new Error("The general configuration should include language settings!");
      } else {
        if (options.generalconfig.languages.locale === "") {
          throw new Error("Please provide local language information!");
        } else if (options.generalconfig.languages.fallbackLocale === "") {
          throw new Error("Please provide fallbackLocale information!");
        } else if (options.generalconfig.languages.translation === "") {
          throw new Error("Please provide a JSON containing the translations!");
        }
      }

      if (!Object.keys(options.generalconfig.input).includes("uploadMethod")) {
        throw new Error("Please provide an uploadMethod wihtin the generalconfig!");
      } else {
        if (options.generalconfig.input.uploadMethod.method == "") {
          throw new Error("Please Please provide an uploadMethod wihtin the generalconfig!");
        }
      }

      Vue.component('v-select', vue_select_default.a);
      Vue.component('InfoSlot', InfoSlot);
      Vue.component('SearchableSelect', SearchableSelect);
      Vue.component('ConditionalInput', ConditionalInput);
      Vue.use(formulate_esm, {
        plugins: [locales_esm_t, u, locales_esm_s, D, m, c, y, b, N, L, q, V, R, B, I, Z, d, Y, K],
        validationNameStrategy: function validationNameStrategy(vm) {
          return vm.context.label;
        },
        // Define our custom slot component(s)
        slotComponents: {
          label: 'InfoSlot'
        },
        // Define any props we want to pass to our slot component
        slotProps: {
          label: ['info']
        },
        components: {
          SearchableSelect: SearchableSelect,
          ConditionalInput: ConditionalInput
        },
        library: {
          "searchable-select": {
            classification: 'select',
            component: 'SearchableSelect'
          },
          "conditional-input": {
            classification: 'text',
            component: 'ConditionalInput',
            slotProps: {
              component: ['data']
            }
          }
        }
      });
      Vue.component('DCATAPInput', MainComponent); // set input config and general config in store

      for (var configurationName in options.inputconfig) {
        datainput.state[configurationName] = options.inputconfig[configurationName];
      }

      datainput.state.config = options.generalconfig;
      options.store.registerModule('DCATAPstore', datainput);
      var mainViewNames = {};
      var mainViewProps = {};
      var basePath = "";

      for (var name in options.inputconfig) {
        mainViewNames[name] = Wrapper;
        mainViewProps[name] = {
          name: String(name)
        };
        var location = options.router.resolve({
          name: 'DCATAP-Upload',
          params: {
            name: name
          }
        });
        basePath = location.resolved.path.replace(name, '');
        basePath = basePath.replace('#', '');
      } // save baseUrl


      datainput.state["baseUrl"] = basePath; // add all routes

      options.router.addRoute("DCATAP-Upload", {
        path: '/',
        components: mainViewNames,
        props: mainViewProps,
        children: [{
          path: ':mainRoute/:subRoute?',
          component: Input,
          props: true
        }]
      });
      options.router.addRoute({
        path: "/edit/:profile/:id",
        component: DataFetchingComponent,
        props: true
      });
    }
  }
});
// CONCATENATED MODULE: ./node_modules/@vue/cli-service/lib/commands/build/entry-lib.js


/* harmony default export */ var entry_lib = __webpack_exports__["default"] = (dcatap_plugin);



/***/ }),

/***/ "fc6a":
/***/ (function(module, exports, __webpack_require__) {

// toObject with fallback for non-array-like ES3 strings
var IndexedObject = __webpack_require__("44ad");
var requireObjectCoercible = __webpack_require__("1d80");

module.exports = function (it) {
  return IndexedObject(requireObjectCoercible(it));
};


/***/ }),

/***/ "fdbc":
/***/ (function(module, exports) {

// iterable DOM collections
// flag - `iterable` interface - 'entries', 'keys', 'values', 'forEach' methods
module.exports = {
  CSSRuleList: 0,
  CSSStyleDeclaration: 0,
  CSSValueList: 0,
  ClientRectList: 0,
  DOMRectList: 0,
  DOMStringList: 0,
  DOMTokenList: 1,
  DataTransferItemList: 0,
  FileList: 0,
  HTMLAllCollection: 0,
  HTMLCollection: 0,
  HTMLFormElement: 0,
  HTMLSelectElement: 0,
  MediaList: 0,
  MimeTypeArray: 0,
  NamedNodeMap: 0,
  NodeList: 1,
  PaintRequestList: 0,
  Plugin: 0,
  PluginArray: 0,
  SVGLengthList: 0,
  SVGNumberList: 0,
  SVGPathSegList: 0,
  SVGPointList: 0,
  SVGStringList: 0,
  SVGTransformList: 0,
  SourceBufferList: 0,
  StyleSheetList: 0,
  TextTrackCueList: 0,
  TextTrackList: 0,
  TouchList: 0
};


/***/ }),

/***/ "fdbf":
/***/ (function(module, exports, __webpack_require__) {

/* eslint-disable es/no-symbol -- required for testing */
var NATIVE_SYMBOL = __webpack_require__("4930");

module.exports = NATIVE_SYMBOL
  && !Symbol.sham
  && typeof Symbol.iterator == 'symbol';


/***/ }),

/***/ "fea9":
/***/ (function(module, exports, __webpack_require__) {

var global = __webpack_require__("da84");

module.exports = global.Promise;


/***/ }),

/***/ "feb1":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/*
 * Copyright (c) 2017 Digital Bazaar, Inc. All rights reserved.
 */


const RDF = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#';
const XSD = 'http://www.w3.org/2001/XMLSchema#';

module.exports = {
  // TODO: Deprecated and will be removed later. Use LINK_HEADER_CONTEXT.
  LINK_HEADER_REL: 'http://www.w3.org/ns/json-ld#context',

  LINK_HEADER_CONTEXT: 'http://www.w3.org/ns/json-ld#context',

  RDF,
  RDF_LIST: RDF + 'List',
  RDF_FIRST: RDF + 'first',
  RDF_REST: RDF + 'rest',
  RDF_NIL: RDF + 'nil',
  RDF_TYPE: RDF + 'type',
  RDF_PLAIN_LITERAL: RDF + 'PlainLiteral',
  RDF_XML_LITERAL: RDF + 'XMLLiteral',
  RDF_JSON_LITERAL: RDF + 'JSON',
  RDF_OBJECT: RDF + 'object',
  RDF_LANGSTRING: RDF + 'langString',

  XSD,
  XSD_BOOLEAN: XSD + 'boolean',
  XSD_DOUBLE: XSD + 'double',
  XSD_INTEGER: XSD + 'integer',
  XSD_STRING: XSD + 'string',
};


/***/ })

/******/ });
//# sourceMappingURL=dcatap-frontend.common.js.map