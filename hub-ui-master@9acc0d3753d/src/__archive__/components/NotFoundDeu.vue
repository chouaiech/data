<!-- ERROR page -->

<template>
  <div class="container-fluid">
    <div class="row mt-1 mb-3">
      <div class="col-10 offset-1">
        <slot>
          <h1>Error 404</h1>
          <p>The requested resource has not been found. You can start again from the <app-link :to="{name: 'Datasets'}">dataset search page</app-link>.</p>
          <p><app-link :href="`/${$route.query.locale || 'en'}/feedback/form`">Contact</app-link> us about this error message.</p>
        </slot>
      </div>
    </div>
  </div>
</template>

<script>
import { AppLink } from "@piveau/piveau-hub-ui-modules";

export default {
  name: 'NotFound',
  components: { AppLink },
  metaInfo() {
    return {
      title: this.$t('message.metadata.notFound'),
      meta: [
        { name: 'description', vmid: 'description', content: `${this.$t('message.metadata.notFound')} - data.europa.eu` },
        { name: 'keywords', vmid: 'keywords', content: `${this.$env.keywords} ${this.$t('message.metadata.notFound')}` },
        { name: 'robots', content: 'noindex, follow' },
      ],
    };
  },
  mounted() {
    if (this.$piwik) {
      this.$piwik.trackInteraction('not_found', {
        screen_title: window.title,
        page_url: window.location.href,
        dataset_ID: this.$route.query.dataset || '',
      });
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
