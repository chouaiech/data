/** A component to wrap all components that should be present in the HTML-footer **/

<template>
  <footer>
    <deu-footer></deu-footer>
  </footer>
</template>

<script>
import DEUFooter from './DEU-footer';

export default {
  name: 'bottom',
  components: {
    deuFooter: DEUFooter,
  },
  data() {
    return {
      footerLogos: this.$env.images.footerLogos,
    };
  },

};
</script>

<style scoped>
</style>
