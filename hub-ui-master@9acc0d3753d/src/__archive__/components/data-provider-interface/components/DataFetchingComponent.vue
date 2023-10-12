<template>
  <div style="height:370px;">
    <div class="spinner"></div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  props: ['id', 'property', 'catalog'],
  data() {
    return {};
  },
  computed: {
    ...mapGetters('auth', [
      'getIsDraft',
      'getUserData',
    ]),
    ...mapGetters('dataProviderInterface', [
      'getNavSteps',
    ]),
    token() {
      return this.getUserData.rtpToken;
    },
  },
  methods: {
    ...mapActions('auth', [
      'setIsEditMode',
      'setIsDraft',
    ]),
    ...mapActions('dataProviderInterface', [
      'saveJsonldFromBackend',
    ]),
    async setupEditPage() {
      let endpoint;
      this.setIsEditMode(true);

      if (this.getIsDraft) {
        this.setIsDraft(true);
        endpoint = `${this.$env.api.hubUrl}drafts/datasets/${this.id}.jsonld?catalogue=${this.catalog}`;
        await this.saveJsonldFromBackend({endpoint, token: this.token, property: this.property, id: this.id});
      } else {
        this.setIsDraft(false);
        endpoint = `${this.$env.api.hubUrl}datasets/${this.id}.jsonld?useNormalizedId=true`;
        await this.saveJsonldFromBackend({endpoint, token: this.token, property: this.property, id: this.id});
      }

      const firstStep = this.getNavSteps[this.property][0];
      const path = `${this.$env.upload.basePath}/${this.property}/${firstStep}?locale=${this.$i18n.locale}`;
      this.$router.push(path).catch(() => {});
    },
  },
  created() {
    this.setupEditPage();
  },
};
</script>
