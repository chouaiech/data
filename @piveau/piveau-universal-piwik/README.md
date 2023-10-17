# piveau-universal-piwik

A Vue plugin exposing common tracking methods for piveau web apps using Matomo or Piwik Pro.

## Installation

Note that this package is not publicly available and must be installed using the private npm registry https://paca.okd.fokus.fraunhofer.de/repository/npm-hosted. It is recommended to use a .npmrc file at the root of your project folder. For more information about npm configuration, see [here](https://docs.npmjs.com/cli/v6/configuring-npm/npmrc)

```bash
npm i @piveau/piveau-universal-piwik
```

for latest builds:

```bash
npm i @piveau/piveau-universal-piwik@beta
```

## Usage

Setup the plugin in `main.js`. This will add `$piwik` to `Vue` to be accessible as `Vue.prototype.$Vue` or `this.$piwik` in all Vue components.

Refer to the [demo](./demo) for an example on how to use this package.

Common usage could look like this:

```javascript
// In main.js
import UniversalPiwik from '@piveau/piveau-universal-piwik';
import { router } from './router'; // Optional
Vue.use(UniversalPiwik, {
  router, // Optional
  isPiwikPro: true
  trackerUrl: 'http://piwik-pro-host.example.org/',
  siteId: 'your-site-id',
  debug: process.env.NODE_ENV === 'development',
});
```

```html
<template>
  <div id="app">
    <div id="cookies">
      Change consent:
      <div class="flexy consent-action">
        <button class="btn" @click="accept">Accept</button>
        <button class="btn" @click="decline">Decline</button>
        <button class="btn" @click="postpone">Postpone</button>
      </div>
    </div>
    <div id="control-panel">
      <h2>Simulate interactions</h2>
      <div class="flexy">
        <button class="btn" @click="external">External link</button>
        <button class="btn" @click="download">Download</button>
      </div>
    </div>
    <div id="nav">
      <router-link to="/home">Home</router-link> |
      <router-link to="/about">About</router-link>
    </div>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  created() {
    this.$piwik.init();
  },
  methods: {
    accept() {
      this.$piwik.consentGiven();
    },
    decline() {
      this.$piwik.consentDeclined();
    },
    postpone() {
      this.$piwik.consentNoDecision();
    },
    external() {
      this.$piwik.trackOutlink('https://example.org/outlink');
    },
    download() {
      this.$piwik.trackDownload('https://example.org/download');
    }
  }
}
</script>
```

## Plugin reference

### Plugin construction options

```javascript
options = {
    router: VueRouter,
    isPiwikPro: true,
    trackerUrl: 'http://piwik-pro-host.example.org/',
    siteId: 'your-site-id',
    debug: process.env.NODE_ENV === 'development',
    immediate: false,
    removeCookiesWhenNoConsent: true,
    stopWhenNoConsent: true,
    verbose: false,
    pageViewOptions: { onlyTrackWithLocale: true, delay: 1000, beforeTrackPageView: (tracker, to, from) => {} },
    disabled: false,
}
```

#### router

- type: `VueRouter`

when specified, uses default router hooks in order to track page changes. Set it to `undefined` when it is desired to handle page views manually.

#### isPiwikPro

- type: `boolean`
- default: `false`

if `true`, uses Piwik Pro. If `false`, uses Matomo.

#### trackerUrl

- type: `string`

Tracker url for Matomo or Piwik Pro

#### siteId

- type: `string`

Tracker site id for Matomo or Piwik Pro

#### pageViewOptions

- type: `Object<{ useDatasetsMinScoreFix: boolean, onlyTrackWithLocale: boolean, delay: number, beforeTrackPageView(tracker, to, from): Function }>`

Options for the default page view tracking implementation. Set `onlyTrackWithLocale` to true to only track when there is a `locale` query in the destination route when navigating. This avoids duplicated page views for piveau applications. Set `useDatasetsMinScoreFix` to `true` to only track pages named 'Dataset' when there is a `minScore` query.

Provide `beforeTrackPageView` hook to do tasks before a page is being tracked but after a navigation is registered as valid.

Set `delay` for Piwik Pro tracker for delayed page view tracking after a navigation has been confirmed in milliseconds. This avoids `undefined` page titles for piveau applications.

#### debug

- type: `boolean`
- default: `false`

If true, enables debug logging on console. Useful for development.

#### verbose

- type: `boolean`
- default: `false`

If `true` and `debug` is `true`, additional logging on console.

#### immediate

- type: `boolean`
- default: `false`

if `true`, executes piwik init script immediately.

#### removeCookiesWhenNoConsent

- type: `boolean`
- default: `true`

(PiwikPro only) if `true`, removes all PiwikPro related cookies when user does not give consent (called by `$piwik.declineConsent()`)
#### stopWhenNoConsent

- type: `boolean`
- default: `true`

(PiwikPro only) if `true`, stops tracking when uder does not give consent (called by `$piwik.declineConsent()`)

#### disabled

- type: `boolean`
- default: `false`

If `true`, disables all tracker instance methods (the tracker instance still exists). Cannot be changed after initialization.

#### useSuspendFeature

- type: `boolean`
- default: `false`

(PiwikPro only) If `true`, enables `suspendFilter` and `resume` methods.

### $piwik instance properties

#### isStopped

- type: `boolean`

if `true`, indicates that $piwik is stopped.

### $piwik instance methods

#### init

- Signature: 

```JavaScript
$piwik.init()
```

Initializes Tracker script. Note that this may set cookies and execute external $piwik.JavaScript code.

#### consentGiven

#### consentDeclined
#### consentNoDecision

- Signatures: 

```JavaScript
$piwik.consentGiven()
$piwik.consentDeclined()
$piwik.consentNoDecision()
```

Accepts, declines, and postpones cookies for tracking

#### trackPageView

#### trackDatasetDetailsPageView

#### trackDownload

#### trackOutlink

#### trackEvent
#### trackGotoResource

- Signatures: 

```JavaScript
$piwik.trackPageView(url, title)
$piwik.trackDatasetDetailsPageView()
$piwik.trackDownload(url)
$piwik.trackOutlink(url)
$piwik.trackEvent(category, action, name, value)
$piwik.trackGotoResource()
```

#### trackInteraction

- Signature:
```JavaScript
$piwik.trackInteraction(eventType = 'screen_load', variables = {})
```

(Piwik Pro tracker only) track interaction with specific event type and variables.

#### stop

- Signature:
```JavaScript
$piwik.stop()
```

#### suspendFilter

- Signature:
```JavaScript
$piwik.suspendFilter(filterFn: (data: Object) => boolean)
```

(Piwik Pro tracker only) Suspends PiwikPro by intercepting all incoming events.
If `filterFn` is given, will not intercept incoming event, if `filterFn(event) === true`

#### resume

- Signature:
```JavaScript
$piwik.resume()
```

(Piwik Pro tracker only) Resumes PiwikPro by emitting all events that were emitted during suspension.

### $universalPiwik instance methods

#### beforeTrackPageView

Calls `callbackFn()` when a valid route is entered and is going to track a page view.
Only works when `router` option is set on initialization.

- Signature:
```JavaScript
$universalPiwik.beforeTrackPageView(callbackFn)
```

- `callbackFn` Signature:
```JavaScript
callbackFn(to: Route, from: Route)
```

## Project setup
```
npm install
```

### Compiles and minifies for production
```
npm run build:lib
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
