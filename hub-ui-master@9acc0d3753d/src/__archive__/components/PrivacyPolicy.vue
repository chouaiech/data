<!-- Wraps all PRIVACY POLICY components -->

<template>
  <user-privacy-policy></user-privacy-policy>
</template>

<script>
import UserPrivacyPolicy from '@/components/user/UserPrivacyPolicy';

export default {
  name: 'imprint',
  components: {
    userPrivacyPolicy: UserPrivacyPolicy,
  },
  metaInfo: {
    title: this.$t('message.navigation.navItems.privacyPolicy'),
    meta: [
      { name: 'description', vmid: 'description', content: `${this.$t('message.navigation.navItems.privacyPolicy')} - data.europa.eu` },
      { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.navigation.navItems.privacyPolicy')}` },
    ],
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
</style>
