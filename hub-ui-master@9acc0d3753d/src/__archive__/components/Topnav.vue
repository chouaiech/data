<!-- NAVIGATION BAR -->

<template>
  <nav class="navbar navbar-expand-md mb-3"
       :class="{'navbar-light': theme === 'light','navbar-dark': theme === 'dark' || theme === 'primary',
       'bg-light': theme === 'light', 'bg-dark': theme === 'dark', 'bg-primary': theme === 'primary'}">
    <div class="navbar-brand">
      <app-link class="navbar-item"
         v-for="(logo, i) in images"
         :key="i"
         :to="logo.href ? logo.href : '#'"
         :target="logo.target ? logo.target : null"
         @click="logo.href ? null : $router.push({ path: `/home` })">
        <img class="navbar-logo"
             :src="logo.src"
             :width="logo.width"
             :height="logo.height"
             :alt="logo.description">
      </app-link>
    </div>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-start" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item" v-if="navigation.main.home.show">
          <app-link class="nav-link"
                    :to="navigation.main.home.href ? navigation.main.home.href : { name: `Home` }"
                    :target="navigation.main.home.target">
            <i class="material-icons" v-if="navigation.main.icons">home</i>
            <span>{{ $t('message.navigation.navItems.home') }}</span>
          </app-link>
        </li>
        <li class="nav-item" v-if="navigation.main.data.show">
          <app-link class="nav-link"
                    :to="navigation.main.data.href ? navigation.main.data.href : {name: 'Datasets'}">
            <i class="material-icons" v-if="navigation.main.icons">storage</i>
            {{ $t('message.navigation.navItems.data') }}
          </app-link>
        </li>
        <li class="nav-item"  v-if="navigation.main.maps.show">
          <app-link class="nav-link"
                    :to="navigation.main.maps.href ? navigation.main.maps.href : { name: `MapComp` }">
            <i class="material-icons" v-if="navigation.main.icons">map</i>
            {{ $t('message.navigation.navItems.map') }}
          </app-link>
        </li>
        <li class="nav-item appended" v-for="navItem in navigation.main.append">
          <app-link class="nav-link"
                    :to="navItem.href ? navItem.href: {name: 'Home'}"
                    :target="navItem.target">
            <i class="material-icons" v-if="navigation.main.icons">{{ navItem.icon }}</i>
            {{ navItem.title }}
          </app-link>
        </li>
        <li class="nav-item d-md-none">
          <app-link class="nav-link"
                    :to="navigation.sub.imprint.href ? navigation.sub.imprint.href : { name: 'Imprint' }"
                    :target="navigation.sub.imprint.target">
            <i class="material-icons" v-if="navigation.main.icons">import_contacts</i>
            {{ $t('message.navigation.navItems.imprint') }}
          </app-link>
        </li>
        <li class="nav-item d-md-none">
          <app-link class="nav-link"
                    :to="navigation.main.privacyPolicy.href ? navigation.sub.privacyPolicy.href : { name: 'PrivacyPolicy' }">
            <i class="material-icons" v-if="navigation.main.icons">account_balance</i>
            {{ $t('message.navigation.navItems.privacyPolicy') }}
          </app-link>
        </li>
      </ul>
    </div>
    <div class="collapse navbar-collapse justify-content-end">
      <ul class="navbar-nav">
        <li class="nav-item small" v-if="navigation.sub.privacyPolicy.show">
          <app-link class="nav-link"
                    :to="navigation.sub.privacyPolicy.href ? navigation.sub.privacyPolicy.href : { name: 'PrivacyPolicy' }">
            {{ $t('message.navigation.navItems.privacyPolicy') }}
          </app-link>
        </li>
        <li class="nav-item small" v-if="navigation.sub.imprint.show">
          <app-link class="nav-link"
                    :to="navigation.sub.imprint.href ? navigation.sub.imprint.href : { name: 'Imprint' }">
            {{ $t('message.navigation.navItems.imprint') }}
          </app-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { AppLink } from "@piveau/piveau-hub-ui-modules";

export default {
  name: 'topnav',
  components: {
    appLink: AppLink,
  },
  data() {
    return {
      images: this.$env.images.top,
      navigation: this.$env.navigation.top,
      theme: this.$env.themes.header,
    };
  },
  computed: {},
  methods: {},
};
</script>

<style lang="scss" scoped>
  @import '../styles/bootstrap_theme';

  .navbar {
    i {
      vertical-align: middle;
      margin: 0 0 .1em 0;
    }
    img {
      &.navbar-logo {
        height: 2.5rem;
        width: auto;
      }
    }
  }
</style>
