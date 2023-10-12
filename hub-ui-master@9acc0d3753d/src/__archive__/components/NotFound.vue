<!-- ERROR page -->

<template>
  <div class="row mt-1 mb-3">
    <div class="col-10 offset-1">
      <p>ERROR: THIS PAGE DOES NOT EXIST!</p>
      <button class="btn btn-sm btn-secondary" @click="$router.go(-1)">GO BACK</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
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
};
</script>

<style lang="scss" scoped>
</style>
