<template>
  <div class="dashboard" :key="$route.params.id">
    <div class="row" id="detail-sub-header">
      <div class="col-sm">
        <div class="detail-header">
          <nav class="navbar navbar-expand-lg navbar-light">
            <div>
              <app-link class="catalogue-title" :to="getUrlToCatalogue()" matomo-track-download>
                <h2 class="ecl-u-type-heading-2">{{ title }}</h2>
              </app-link>
            </div>
          </nav>
          <div class="detail-desc">
            <div class="d-flex ecl-u-mt-m">
              <img v-if="spatial == 'IOR'" :src="getCountryFlagImg(spatial)" width="30px" height="30px" class="flag-icon-detail ior" alt="Catalog flag">
              <img v-else :src="getCountryFlagImg(spatial)" width="30px" height="30px" class="flag-icon-detail" alt="Catalog flag">
              <p class="ecl-u-type-paragraph-m" >{{ description }}</p>
            </div>
          </div>
          <catalogue-detail-navigation :id="$route.params.id" />
        </div>
      </div>
    </div>
    <!-- ROUTER-VIEW -->
    <router-view class="detail-content mx-0" name="catalogueDetailNavigation" :id="$route.params.id" :title="title"></router-view>
  </div>
</template>

<script>
import CatalogueDetailNavigation from '@/components/CatalogueDetailNavigation'
import {mapActions, mapGetters} from 'vuex'
import AppLink from './AppLink'

export default {
  name: 'CatalogueDetail',
  components: {
    CatalogueDetailNavigation,
    AppLink
  },
  data () {
    return {
      dataURL: this.$env.DATA_URL
    }
  },
  computed: {
    // import store-getters
    ...mapGetters([
      'getCatalogue',
      'getAllCatalogues'
    ]),
    title () {
      return this.getCatalogue && this.getCatalogue.info ? this.getCatalogue.info.title : ''
    },
    description () {
      return this.getCatalogue && this.getCatalogue.info ? this.getCatalogue.info.description : ''
    },
    spatial () {
      return this.getCatalogue && this.getCatalogue.info ? this.getCatalogue.info.spatial : ''
    }
  },
  methods: {
    // import store-actions
    ...mapActions([
      'loadCatalogue'
    ]),
    getUrlToCatalogue () {
      return this.dataURL + '?locale=' + this.$i18n.locale + '&catalog=' + this.$route.params.id
    },
    getCountryFlagImg (countryId) {
      let img
      import(`@/assets/img/flags/${countryId.toLowerCase()}.png`)
        .then(module => {
          img = module.default;
        })
        .catch(error => {
          // eslint-disable-next-line no-console
          console.error(error);
          import('@/assets/img/flags/europe.png')
            .then(module => {
              img = module.default;
            })
            .catch(error => {
              // eslint-disable-next-line no-console
              console.error(error);
            });
        });

     return img
    }
  },
  created () {
    this.loadCatalogue(this.$route.params.id)
  }
}
</script>

<style scoped>
.catalogue-title{
  color: #212529;
  text-decoration: none;
  font-size: 1.75rem;
}
.ior {
  opacity: 0.8;
}
</style>
