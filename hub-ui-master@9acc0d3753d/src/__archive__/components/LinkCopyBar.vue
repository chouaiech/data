<!-- This component should be used for every link that should be copied to clipboard on click -->

<template>
  <div class="input-group input-group-sm w-50">
    <div class="input-group-prepend">
      <app-link :target="link.target"
         :to="link.url">
        <button class="btn btn-sm btn-outline-secondary" type="button">
          {{ link.title }}
        </button>
      </app-link>
    </div>
    <input type="text" class="form-control border-secondary" aria-label="Url to copy"
           :value="link.url" @focus="$event.target.select()" ref="linkTextInput" readonly>
    <div class="input-group-append">
      <button class="btn btn-outline-secondary" type="button" title="copy" @click="copyLink()">
        <i class="material-icons">content_copy</i>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LinkCopyBar',
  components: {},
  props: {
    /*
      * @example [{
      *  title: 'Download',
      *  url: 'https://lol.de',
      * }]
      */
    link: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {};
  },
  computed: {},
  methods: {
    copyLink() {
      this.$refs.linkTextInput.select();
      document.execCommand('Copy');
    },
  },
  created() {},
  mounted() {},
  beforeDestroy() {
  },
};
</script>

<style lang="scss" scoped>
  @import '../styles/bootstrap_theme';

  .material-icons {
    font-size: 1em;
  }
</style>
