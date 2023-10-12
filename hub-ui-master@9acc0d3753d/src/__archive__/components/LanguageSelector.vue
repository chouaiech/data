<!-- LANGUAGE SELECTOR -->

<template>
  <select class="form-control" v-model="locale">
    <option v-for="lang in Object.keys(languages)" :key="lang" :value="lang">
      {{ languages[lang] }}
    </option>
  </select>
</template>

<script>
import { has } from 'lodash';
import languages from '../../config/langs.json';

export default {
  name: 'language-selector',
  components: {
  },
  data() {
    return {
      languages,
    };
  },
  computed: {
    locale: {
      get() {
        return this.getLocale();
      },
      set(locale) {
        this.$i18n.locale = locale;
        // Wait until router is ready before changing it
        // Necessary when routing to lazy-loaded components
        // Fixes https://gitlab.fokus.fraunhofer.de/viaduct/piveau-ui/piveau-ui/issues/210
        this.$router.onReady(() => {
          if (locale !== this.$route.query.locale) {
            this.$router.push({ query: Object.assign({}, this.$route.query, { locale }) });
          }
        });
      },
    },
  },
  methods: {
    has,
    initLocale() {
      this.$router.onReady(() => {
        this.locale = this.getLocale();
      });
    },
    getLocale() {
      if (this.$route.query.locale) return this.$route.query.locale;
      if (this.has(this.languages, (navigator.language.substring(0, 2)))) return navigator.language.substring(0, 2);
      if (this.$env.languages.locale) return this.$env.languages.locale;
      return this.$env.languages.fallbackLocale;
    },
  },
  created() {
    this.initLocale();
  },
};
</script>

<style lang="scss" scoped>
  @import '../../node_modules/bootstrap/scss/bootstrap.scss';
  .language-selector {
    width: 150px;
  }
</style>
