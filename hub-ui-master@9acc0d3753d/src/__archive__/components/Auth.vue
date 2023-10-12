<!-- AUTHENTICATION -->
<template>
  <div class="container">
    <div class="row">
      <div class="col">
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'auth',
  dependencies: 'authService',
  data() {
    return {
      headerData: '',
    };
  },
  computed: {
    ...mapGetters('auth', [
      'securityAuth',
      'getRTPToken',
      'getKeycloak',
    ]),
  },
  methods: {
    ...mapActions('auth', [
      'authLogin',
      'authLogout',
      'rtpToken',
      'setKeycloak',
    ]),
    logout() {
      this.authService.logout(this.getKeycloak);
      this.authLogout();
    },
    getHeader() {
      this.headerData = this.authService.getHeader(this.getKeycloak);
    },
  },
};
</script>

<style lang="scss" scoped>
  p {
    word-break: break-all
  }
</style>
