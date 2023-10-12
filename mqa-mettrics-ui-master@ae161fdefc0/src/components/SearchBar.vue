<template>
  <form @submit.prevent="search()">
    <div class="input-group">
      <div class="input-group-prepend">
        <!-- <input class="form-control" name="term"> -->
        <input class="form-control round"
              type="text"
              v-model="q">
      </div>
      <select class="form-control violet" name="searchdomain" v-model="selected">
        <option value="site" selected>{{ $t('message.header.searchOptions.siteContent') }}</option>
        <option value="data">{{ $t('message.header.searchOptions.datasets') }}</option>
        </select>
      <div class="input-group-append ">
        <app-link
          ref="searchButton"
          class="btn-search round-right edp-btn edp-btn--primary"
          :path="searchPath"
        >
          <i class="fas fa-search icon"></i>
        </app-link>
      </div>
    </div>
  </form>
</template>

<script>
import AppLink from './AppLink'

export default {
/* eslint-disable */ 
  name: 'SearchBar',
  components: {
    appLink: AppLink,
  },
  data() {
    return {
      q: '',
      selected: 'site'
    };
  },
  computed: {
    searchPath() {
      return this.selected === 'site'
        ? `/${this.$i18n.locale}/search?term=${this.q}&searchdomain=${this.selected}`
        : `/data/datasets/?query=${this.q}&locale=${this.$i18n.locale}`
    }
  },
  methods: {
    search() {
      // Trigger click on search button
      this.$refs.searchButton.$el.click();
    },
  },
  created() {},
};
</script>

<style lang="scss" scoped>
  @import '../styles/bootstrap_theme';

  .round {
    border-radius: 4px 0px 0px 4px!important;
  }

  .round-right {
    border-radius: 0px 4px 4px 0px!important;
  }

  .violet {
    background-color: #D5D6F7;
  }

  .btn-search {
    background-color: #202272;
    border-color: #202272;
  }

  .icon {
    font-size: 17px;
    color: #fff;
    padding: 10px;
    padding-left: 10px;
    margin-top: 1px;
    width: 38px;
    margin-left: 3px;
  }

  .input-group-append {
    margin-left: 0px !important;
}

</style>
