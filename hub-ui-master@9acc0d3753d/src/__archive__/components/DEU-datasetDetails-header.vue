<template>
  <!-- HEADER -->
  <div>
    <div class="row">
      <div class="col-12 col-lg-10 offset-lg-1">
        <!-- TITLE -->
        <h1 class="d-none d-lg-block dataset-details-title" data-cy="dataset-title">{{ getTranslationFor(getTitle, $route.query.locale, getLanguages) }}</h1>
      </div>
    </div>
    <div class="row mt-1">
      <div class="col-4 col-lg-3 offset-lg-1 d-flex align-items-center">
        <img v-if="showCountryFlag(getCountry)" class="mr-2 border border-dark flag" :class="{ 'io': getCountry.id === 'io' }" :src="getCountryFlagImg(getCountry.id)" alt="Catalog Flag">
        <app-link
                :to="getCatalogLink(getCatalog)"
                :title="$t('message.tooltip.datasetDetails.catalogue')"
                data-toggle="tooltip"
                data-placement="top">
                {{ getTranslationFor(getCatalog.title, $route.query.locale, getLanguages) }}
        </app-link>
      </div>
      <div class="col-4 text-break" v-if="showObject(getPublisher)">
          <span class="font-weight-bold"
                :title="$t('message.tooltip.datasetDetails.publisher')"
                data-toggle="tooltip"
                data-placement="top">
                {{ $t('message.metadata.publisher')}}:
          </span>
          <span v-if="has(getPublisher, 'name') && !isNil(getPublisher.name)">
              {{ getPublisher.name }}
            </span>
        </div>
      <div class="col-4 text-right text-break">
        <span class="font-weight-bold mx-1"
          :title="$t('message.tooltip.datasetDetails.updated')"
              data-toggle="tooltip"
              data-placement="top">
              {{ $t('message.metadata.updated') }}:</span>
          <dataset-date :date="getModificationDate"/>
      </div>
    </div>
    <hr>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex';
  import { has, isNil, isObject } from 'lodash';
  import DatasetDate from './DatasetDate';
  import AppLink from './AppLink';
  import dateFilters from '../filters/dateFilters';
  import { getTranslationFor, getCountryFlagImg, truncate } from '../utils/helpers';

  export default {
    name: 'datasetDetailsDataset',
    components: {
      DatasetDate,
      AppLink,
    },
    dependencies: 'DatasetService',
    data() {
      return {};
    },
    computed: {
      ...mapGetters('datasetDetails', [
        'getCatalog',
        'getCountry',
        'getLanguages',
        'getPublisher',
        'getModificationDate',
        'getTitle',
      ]),
    },
    methods: {
      has,
      isNil,
      isObject,
      truncate,
      getTranslationFor,
      getCountryFlagImg,
      filterDateFormatUS(date) {
        return dateFilters.formatUS(date);
      },
      filterDateFormatEU(date) {
        return dateFilters.formatEU(date);
      },
      showObject(object) {
        return !isNil(object) && isObject(object) && !Object.values(object).reduce((keyUndefined, currentValue) => keyUndefined && currentValue === undefined, true);
      },
      filterDateFromNow(date) {
        return dateFilters.fromNow(date);
      },
      showCountryFlag(country) {
        return has(country, 'id') && !isNil(country.id);
      },
      getCatalogLink(catalog) {
        return `/datasets?catalog=${catalog.id}&showcatalogdetails=true&locale=${this.$route.query.locale}`;
      },
    },
  };
</script>

<style scoped lang="scss">
@import '../styles/bootstrap_theme';
@import "~bootstrap/scss/bootstrap";

.flag {
  max-width: 30px;
  max-height: 19px;
}
.dataset-details-title {
  font-size: 1.4rem;
  margin-bottom: 0.5rem;
  font-family: inherit;
  font-weight: 500;
  line-height: 1.2;
  color: inherit;
}
.io {
  border: 0 !important;
  margin-bottom: 3px;
  opacity: 0.8;
}

@include media-breakpoint-up(md) {
  .dataset-details-title {
    font-size: 1.5rem;
  }
}

@include media-breakpoint-up(lg) {
  .dataset-details-title {
    font-size: 1.75rem;
  }
}
</style>
