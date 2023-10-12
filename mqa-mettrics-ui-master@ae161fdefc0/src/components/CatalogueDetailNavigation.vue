<template>
  <nav class="ecl-tabs mb-0">
    <div class="ecl-tabs__container">
      <ul class="ecl-tabs__list" role="tablist">
        <li class="ecl-tabs__item" role="presentation">
          <router-link class="block ecl-link ecl-tabs__link" role="tab" exact
                       :to="{ name: 'CatalogueDetailDashboard', params: {id: id}, query: Object.assign({}, { locale: $i18n.locale }) }"
                       active-class="ecl-tabs__link--active">
            {{ $t("message.catalogue_detail.title") }}
          </router-link>
        </li>
        <li class="ecl-tabs__item" role="presentation">
          <router-link class="block ecl-link ecl-tabs__link" role="tab"
                       :to="{ name: 'CatalogueDetailDistributions', params: {id: id}, query: Object.assign({}, { locale: $i18n.locale }) }"
                       active-class="ecl-tabs__link--active">
              {{ $t("message.catalogue_detail.distributions.title") }} ({{getCatalogueDistributionsSize.toLocaleString('fi')}})
          </router-link>
        </li>
        <li class="ecl-tabs__item" role="presentation">
          <router-link class="block ecl-link ecl-tabs__link" role="tab"
                       :to="{ name: 'CatalogueDetailViolations', params: {id: id}, query: Object.assign({}, { locale: $i18n.locale }) }"
                       active-class="ecl-tabs__link--active">
              {{ $t("message.catalogue_detail.violations.own_headline") }} ({{getCatalogueViolationsSize.toLocaleString('fi')}})
          </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
/* eslint-disable */
import {mapActions, mapGetters} from 'vuex'

export default {
  name: 'CatalogueDetailNavigation',
  data() {
    return {}
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  mounted() {
  },
  computed: {
    // import store-getters
    ...mapGetters([
      'getCatalogueDistributionsSize',
      'getCatalogueViolationsSize'
    ])
  },
  methods: {
    // import store-actions
    ...mapActions([
      'loadCatalogueDistributionsSize',
      'loadCatalogueViolationsSize'
    ])
  },
  created() {
    if (this.id) {
      this.loadCatalogueDistributionsSize(this.id)
      this.loadCatalogueViolationsSize(this.id);
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../styles/metrics-style.scss';

.badge-warning {
  background-color:  $dark-orange;
  border-radius: 1.875rem;
}
</style>
