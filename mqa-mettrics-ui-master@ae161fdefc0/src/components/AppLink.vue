<template>
  <a
    @click="trackLink"
    @mousedown.middle="trackLink"
    :href="url"
    target="_blank"
    rel="noopener"
    v-if="isExternal"
  >
    <slot></slot>
  </a>
  <router-link :to="to" v-else :exact="exact">
    <slot></slot>
  </router-link>
</template>

<script>
/* eslint-disable */

export default {
  props: {
    to: {
      default: '',
      required: false
    },
    path: {
      default: '',
      required: false
    },
    query: {
      default: '',
      required: false
    },
    fragment: {
      default: '',
      required: false
    },
    exact: {
      default: false,
      required: false
    },
    // Occasionally, you want to use these inside your <a> elements to manually report clicked links to Matomo.
    // In this case, for URL destinations in the same domain where they are unable to track page views (e.g. RSS feed).
    matomoTrackDownload: Boolean,
    // Use this if you want to track a link explicitly as download instead of an external link.
    matomoTrackPageView: Boolean
  },
  computed: {
    readPath: {
      get () {
        return this.path && typeof this.path === 'string' ? this.path : ''
      }
    },
    readQuery: {
      get () {
        if (!this.query) return ''
        if (typeof this.query === 'object') {
          return `?${Object.keys(this.query)
            .map(key => `${key}=${this.query[key]}`)
            .join('&')}`
        }
        if (typeof this.query === 'string') {
          return /^\?([a-zA-Z]+[=].*)([&][a-zA-Z]+[=].*)*$/.test(this.query)
            ? this.query
            : ''
        }
        return ''
      }
    },
    readFragment: {
      get () {
        return this.fragment && typeof this.fragment === 'string'
          ? `${this.fragment}`
          : ''
      }
    },
    isExternal: {
      get () {
        return (
          !(typeof this.to === 'object') &&
          (/^(http(s)?|ftp|mailto|tel):/.test(this.to) ||
            /^\//.test(this.readPath) ||
            /^\?/.test(this.readQuery) ||
            /^#/.test(this.readFragment))
        )
      }
    },
    url: {
      get () {
        return `${this.to}${this.readPath}${this.readQuery}${this.readFragment}`
      }
    }
  },
  methods: {
    trackLink () {
      /* eslint-disable no-underscore-dangle */
      if (this.matomoTrackPageView) {
        this.$piwik.trackPageView(`${window.location.origin}${this.path}`)
        return
      }
      // Do not track links as external when navigating within the same domain.
      if (!this.isExternal) return
      if (this.to === '') return

      if (this.matomoTrackDownload) {
        this.$piwik.trackDownload(this.url);
      } else {
        this.$piwik.trackOutlink(this.url);
      }
    }
  }
}
</script>
