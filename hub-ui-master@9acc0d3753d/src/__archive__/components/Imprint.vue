<!-- Wraps all IMPRINT components -->

<template>
    <user-imprint></user-imprint>
</template>

<script>
import UserImprint from '@/components/user/UserImprint';

export default {
  name: 'imprint',
  components: {
    userImprint: UserImprint,
  },
  metaInfo: {
    title: this.$t('message.navigation.navItems.imprint'),
    meta: [
      { name: 'description', vmid: 'description', content: `${this.$t('message.navigation.navItems.imprint')} - data.europa.eu` },
      { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.navigation.navItems.imprint')}` },
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
