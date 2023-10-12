<!-- HOME page -->

<template>
  <div>
    <p>{{ $t('message.home.msg') }}</p>
  </div>
</template>

<script>
export default {
  name: 'home',
  metaInfo: {
    title: this.$t('message.navigation.navItems.home'),
    meta: [
      { name: 'description', vmid: 'description', content: `${this.$t('message.navigation.navItems.home')} - data.europa.eu` },
      { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.navigation.navItems.home')}` },
    ],
  },
  data() {
    return {
      msg: 'Sorry, there is no content yet. Visit the Data tab to see something',
    };
  },
  methods: {
  },
};
</script>

<style lang="scss" scoped>
</style>
