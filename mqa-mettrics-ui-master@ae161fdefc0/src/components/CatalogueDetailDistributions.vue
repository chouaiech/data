<template>
  <div class="distributions mb-5">
    <div class="container mb-4" id="overview-intro" >
      <div class="row">
        <div class="col-sm px-0">
          <div class="catalogue_intro-section-text">
            <h3 class="col-12 ecl-u-type-heading-3 px-0 ecl-u-mb-l">{{ $t("message.catalogue_detail.distributions.title") }}</h3>
            <p class="col-8 px-0 ecl-u-type-paragraph-m">{{ $t("message.catalogue_detail.distributions.introduction") }} </p>
<!--            <div class="col pag">-->
<!--              <ul class="pagination pagination-sm float-right">-->
<!--                <li class="page-item"-->
<!--                    v-for="(n, index) in totalPages"-->
<!--                    v-if="Math.abs(n - currentPage)  < 2 || n === totalPages || n === 1"-->
<!--                    :key="n"-->
<!--                    :class="{ 'active': index === currentPage-1, 'last': n === totalPages && Math.abs(n - currentPage)  > 3, 'first': n === 1 && Math.abs(n - currentPage) > 3 }">-->
<!--                  <a href="" @click.prevent="fetchCatalogueDistributions(n)" class="page-link" >{{ n.toLocaleString('fi') }}</a>-->
<!--                </li>-->
<!--              </ul>-->
<!--            </div>-->
          </div>
          <div v-if="this.loading === true" class="spinner-space">
            <div class="lds-ring" id="spinner-distributions">
              <div></div>
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>
          <div v-else v-for="(distribution, index) in getCatalogueDistributions" class="distribution-detail" :key="index">
            <hr class="ecl-u-m-2xl">
            <div class="distribution-headline">
              <app-link :to="distribution.reference">
                <h4 class="ecl-u-type-heading-4 ecl-u-type-color-blue">{{distribution.title}}</h4>
              </app-link>
              <!-- <small>{{ distribution.accessUrlTimeStamp }}</small> -->
            </div>

            <!-- AccessURLs -->
            <div class="distribution-subheadline">
               <h5 class="ecl-u-type-heading-5">{{ $t("message.catalogue_detail.distributions.access_url") }}</h5>
            </div>
            <div class="distribution-info">
              <div v-if="!distribution.accessUrl">
                <span class="distribution-no-dl-url ecl-u-type-color-blue">{{ $t("message.catalogue_detail.distributions.not_available_access") }}</span>
                <br>
              </div>
              <div v-else>
                <a class="d-block" :href="distribution.accessUrl" target="_blank">
                  <small class="ecl-u-type-color-blue">{{distribution.accessUrl}}</small>
                </a>
                <small>
                  <template v-if="$te('message.catalogue_detail.distributions.availability_checked_on')">
                    {{ $t('message.catalogue_detail.distributions.availability_checked_on', { date: parseDate(distribution.accessUrlTimeStamp) }) }}
                  </template>
                  <template v-else>
                    Availability checked on {{ parseDate(distribution.accessUrlTimeStamp) }}
                  </template>
                </small>
                <br>
                <span class="distribution-subheadline2"> {{ $t("message.common.status") }}: </span>
                <span class="distribution-subheadline2">{{ formatFriendlyStatusCodeMessage(distribution.accessUrlStatusCode) }}</span>
                <br>
              </div>
            </div>
            <!-- DownloadURLs -->
            <div class="distribution-subheadline mt-3">
              <h5 class="ecl-u-type-heading-5">{{ $t("message.catalogue_detail.distributions.download_url") }}</h5>
            </div>
            <div class="distribution-info">
              <div v-if="!distribution.downloadUrl">
                <span class="distribution-no-dl-url">{{ $t("message.catalogue_detail.distributions.not_available_dl") }}</span>
                <br>
              </div>
              <div v-else>
                <a class="d-block" :href="distribution.downloadUrl" target="_blank">
                  <small class="ecl-u-type-color-blue">{{ distribution.downloadUrl }}</small>
                </a>
                <small>
                  <template v-if="$te('message.catalogue_detail.distributions.availability_checked_on')">
                    {{ $t('message.catalogue_detail.distributions.availability_checked_on', { date: parseDate(distribution.downloadUrlTimeStamp) }) }}
                  </template>
                  <template v-else>
                    Availability checked on {{ parseDate(distribution.downloadUrlTimeStamp) }}
                  </template>
                </small>
                <br>
                <span class="distribution-subheadline2">{{ $t("message.common.status") }}: </span>
                <span class="distribution-subheadline2">{{ formatFriendlyStatusCodeMessage(distribution.downloadUrlStatusCode) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <pagination :page="currentPage" :maxPage="totalPages" :onChange="fetchCatalogueDistributions">
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
import AppLink from '@/components/AppLink'
import httpStatusCodes from '@/utils/httpStatusCodes'
import Pagination from "./widgets/Pagination";
import ItemsPerPage from "./widgets/ItemsPerPage";

export default {
  name: 'CatalogueDetailDistributions',
  components: {ItemsPerPage, Pagination, AppLink },
  metaInfo () {
    return {
      meta: [
        { name: 'title', vmid: 'title', content: `${this.$t('message.common.site_title_distributions')} - ${this.title} - ${this.$t('message.common.site_title')}` },
      ],
    }
  },
  data () {
    return {
      currentPage: 1,
      itemsPerPage: 10,
      loading: true,
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
  mounted () {},
  computed: {
    // import store-getters
    ...mapGetters([
      'getCatalogueDistributions',
      'getCatalogueDistributionsSize'
    ]),
    totalPages () {
      if (this.getCatalogueDistributionsSize > 20) {
        return Math.ceil(this.getCatalogueDistributionsSize / this.itemsPerPage);
      }
    }
  },
  methods: {
    // import store-actions
    ...mapActions([
      'loadCatalogueDistributions'
    ]),
    setItemsPerPage(n) {
      this.itemsPerPage = n;
      this.fetchCatalogueDistributions(1)
    },
    fetchCatalogueDistributions (n) {
      this.currentPage = n;
      this.loading = true;
      this.loadCatalogueDistributions(
        { id: this.id, currentPage: this.currentPage, itemsPerPage: this.itemsPerPage })
        .then(() => {
          this.loading = false;
        });
    },
    formatFriendlyStatusCodeMessage(statusCode) {
      if (!statusCode) return ''
      return statusCode < 1000
        ? `${statusCode} - ${httpStatusCodes[statusCode]}` || 'Unknown'
        : 'No HTTP connection could be established'
    },
    parseDate(utcTimeString) {
      const date = new Date(utcTimeString)
      return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`
    }
  },
  created() {
    if (this.id) {
      this.loadCatalogueDistributions(
        { id: this.id, currentPage: this.currentPage, itemsPerPage: this.itemsPerPage })
        .then(() => {
          this.loading = false;
        });
    }
  }
}
</script>

<style scoped>
</style>
