<template>
  <div class="violations mb-5">
    <div class="container mb-4" id="overview-intro">
      <div class="row">
        <div class="col-sm pl-0">
          <div class="catalogue_intro-section-text">
            <h3 class="col-12 ecl-u-type-heading-3 px-0 ecl-u-mb-l">{{ $t("message.catalogue_detail.violations.own_headline") }}</h3>
              <p class="col-8 px-0 ecl-u-type-paragraph-m">{{ $t("message.catalogue_detail.violations.introduction") }}</p>
<!--            <div class="col pag">-->
<!--              <ul class="pagination pagination-sm float-right">-->
<!--                <li class="page-item"-->
<!--                    v-for="(n, index) in totalPages"-->
<!--                    v-if="Math.abs(n - currentPage)  < 2 || n === totalPages || n === 1"-->
<!--                    :key="n"-->
<!--                    :class="{ 'active': index === currentPage-1, 'last': n === totalPages && Math.abs(n - currentPage)  > 3, 'first': n === 1 && Math.abs(n - currentPage) > 3 }">-->
<!--                  <a href="" @click.prevent="fetchCatalogueViolations(n)" class="page-link" >{{ n.toLocaleString('fi') }}</a>-->
<!--                </li>-->
<!--              </ul>-->
<!--            </div>-->
          </div>
          <span v-if="this.loading === true">
            <div class="spinner-space">
              <div class="lds-ring" id="spinner-distributions">
                <div></div>
                <div></div>
                <div></div>
                <div></div>
              </div>
            </div>
          </span>
          <span v-else-if="this.loading === false && getCatalogueViolationsSize === 0">
            <div class="alert alert-success" role="alert">
              <strong>0</strong>
              {{ $t("message.catalogue_detail.violations.own_headline") }}
            </div>
          </span>
          <div v-else>
            <div v-for="(violation, index) in getCatalogueViolations" class="distribution-detail" :key="index">
              <hr class="ecl-u-m-2xl">
              <div class="distribution-headline">
                <app-link :to="violation.reference">
                  <h4 class="ecl-u-type-heading-4 ecl-u-type-color-blue">{{violation.title}}</h4>
                </app-link>
              </div>
              <h5 class="ecl-u-type-heading-5">{{ $t("message.catalogue_detail.violations.resultPath") }}</h5>
              <small>{{violation.resultPath || '--'}}</small>
              <h5 class="ecl-u-type-heading-5">{{ $t("message.catalogue_detail.violations.resultMessage") }}</h5>
              <small>{{violation.resultMessage || '--'}}</small>
              <h5 class="ecl-u-type-heading-5">{{ $t("message.catalogue_detail.violations.value") }}</h5>
              <small>{{violation.resultValue || '--'}}</small>
<!--              <div>-->
<!--                <table class="table table-borderless pl-1">-->
<!--                  <tr>-->
<!--                    <td class="ecl-u-type-heading-5">{{$t('message.catalogue_detail.violations.resultPath')}}</td>-->
<!--                    <td v-if="violation.resultPath">{{ violation.resultPath }}</td>-->
<!--                    <td v-else></td>-->
<!--                  </tr>-->
<!--                  <tr>-->
<!--                    <td class="w-25 font-weight-bold">{{$t('message.catalogue_detail.violations.resultMessage')}}</td>-->
<!--                    <td v-if="violation.resultMessage">{{ violation.resultMessage }}</td>-->
<!--                    <td v-else></td>-->
<!--                  </tr>-->
<!--                  <tr>-->
<!--                    <td class="w-25 font-weight-bold">{{$t('message.catalogue_detail.violations.value')}}</td>-->
<!--                    <td v-if="violation.resultValue">{{ violation.resultValue }}</td>-->
<!--                    <td v-else></td>-->
<!--                  </tr>-->
<!--                </table>-->
<!--              </div>-->
            </div>
          </div>
        </div>
      </div>
    </div>
    <pagination :page="currentPage" :maxPage="totalPages" :onChange="fetchCatalogueViolations">
      <items-per-page
        :value="itemsPerPage"
        :onChange="setItemsPerPage"
      />
    </pagination>
  </div>
</template>

<script>
/* eslint-disable */
import {mapActions, mapGetters} from 'vuex'
import AppLink from './AppLink';
import Pagination from "./widgets/Pagination";
import ItemsPerPage from "./widgets/ItemsPerPage";

export default {
  name: 'CatalogueDetailViolations',
  components: {
    ItemsPerPage,
    Pagination,
    AppLink,
  },
  metaInfo () {
    return {
      title: `${this.$t('message.common.site_title_violations')} - ${this.title} - ${this.$t('message.common.site_title')}`
    }
  },
  data () {
    return {
      currentPage: 1,
      itemsPerPage: 10,
      loading: true
    }
  },
  props: {
    id: {
      type: String,
      required: true
    },
    title: {
      type: String,
    },
  },
  computed: {
    // import store-getters
    ...mapGetters([
      'getCatalogueViolations',
      'getCatalogueViolationsSize'
    ]),
    totalPages () {
      if (this.getCatalogueViolationsSize > 20) {
        return Math.ceil(this.getCatalogueViolationsSize / this.itemsPerPage);
      } else {
        return 1;
      }
    }
  },
  methods: {
    // import store-actions
    ...mapActions([
      'loadCatalogueViolations'
    ]),
    setItemsPerPage(n) {
      this.itemsPerPage = n;
      this.fetchCatalogueViolations(1);
    },
    fetchCatalogueViolations (n) {
      this.currentPage = n;
      this.loading = true;
      this.loadCatalogueViolations(
        { id: this.id, currentPage: this.currentPage, itemsPerPage: this.itemsPerPage })
        .then(() => {
          this.loading = false;
        });
    }
  },
  created() {
    if (this.id) {
      this.loadCatalogueViolations(
        { id: this.id, currentPage: this.currentPage, itemsPerPage: this.itemsPerPage })
        .then(() => {
          this.loading = false;
        });
    }
  }
}
</script>

<style lang="scss" scoped>
.distribution-detail {
  h5 {
    margin-bottom: 0 !important;
  }
  small {
    display: block;
    margin-bottom: 0.5rem !important;
  }
}
</style>
