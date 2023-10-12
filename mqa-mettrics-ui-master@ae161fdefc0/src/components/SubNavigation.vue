<template>
  <div class="subnavigation">
    <div>
      <form class="form-inline my-2 my-lg-0" id="download-report-form">
          <!-- <button type="button" class="btn"> -->
            <!-- <span class="dl-icon-main"><i class="fa fa-download" aria-hidden="true"></i></span> -->
             <div class="btn-group">
                 <a class="ecl-link--secondary ecl-u-pa-m test" :href="getRequestReportUrl('pdf')" matomo-track-download download>
                   <button type="button" class="ecl-u-type-color-blue btn-download"> {{ $t("message.navigation.download_report.direct")}}<span class="dl-icon-main"><i class="fa fa-download ecl-u-ml-s" aria-hidden="true"></i></span></button></a>
                 <button type="button" class="btn-toogle ecl-u-bg-blue ecl-u-type-color-white ecl-u-border-width-0 dropdown-toggle dropdown-toggle-split rounded-left-0" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" :aria-label="$t('message.navigation.download_report.direct')">
                   <span class="sr-only"></span>
                 </button>
                 <div class="dropdown-menu dropdown-menu-right">
                   <a  v-for="format in formats" :key="format[0]" :href="getRequestReportUrl(format[1])" class="dropdown-item" matomo-track-download download><span class="dl-icon-main"><i class="fa fa-download ecl-u-mr-s" aria-hidden="true"></i></span>{{ $t("message.navigation.download_report.second_"+format[2]) }}</a>
                 </div>
               </div>
            <!-- <app-link :to="`${rootURL}/data/datasets?catalog=edp&showcataloguedetails=true&locale=${$i18n.locale}`" matomo-track-download>{{ $t("message.navigation.download_report.direct") }}</app-link> -->
          <!-- </button> -->
      </form>
      <nav class="navbar navbar-expand-lg navbar-light">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
          <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <li class="ecl-u-mr-l">
              <router-link :to="{ name: 'Dashboard', query: { locale: $i18n.locale }} " class="ecl-link ecl-link--secondary ecl-u-pa-m ecl-u-type-color-blue" exact-active-class="active" active-class="">{{ $t("message.navigation.dashboard") }}</router-link>
            </li>
            <li class="ecl-u-mr-l">
              <router-link :to="{ name: 'Catalogues', query: { locale: $i18n.locale }}" class="ecl-link ecl-link--secondary ecl-u-pa-m ecl-u-type-color-blue" exact-active-class="active" active-class="">{{ $t("message.navigation.catalogues") }}</router-link>
            </li>
            <li class="ecl-u-mr-l">
              <router-link :to="{ name: 'Methodology', query: { locale: $i18n.locale }}" class="ecl-link ecl-link--secondary ecl-u-pa-m ecl-u-type-color-blue" exact-active-class="active" active-class="">{{ $t('message.navigation.methodology') }}</router-link>
            </li>
          </ul>
        </div>
      </nav>
    </div>
  </div>
</template>
<script>
import appLink from './AppLink'

export default {
  props: ['language'],
  components: {
    appLink
  },
  data () {
    return {
      lang: 'en',
      reportURL: this.$env.REPORT_URL,
      rootURL: this.$env.ROOT_URL,
      formats: [
        ['application/pdf', 'pdf', 'pdf'],
        ['application/vnd.oasis.opendocument.spreadsheet', 'ods', 'ods'],
        ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'xlsx', 'xls']
      ]
    }
  },
  methods: {
    getRequestReportUrl (format) {
      if (this.$route.params.id) {
        return this.reportURL + 'report/' + this.$i18n.locale + '/' + format + '?catalogueId=' + this.$route.params.id
      } else {
        return this.reportURL + 'report/' + this.$i18n.locale + '/' + format
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.active {
  background-color: #004494 !important;
  color: white !important;
}
.ecl-link:hover {
    text-decoration: none!important;
    background: #004494 !important;
    color: white !important;
    border-color:  #004494 !important;

}
  .test:hover {
    border-color:  #004494 !important;
  }
.btn-download {
  font-weight: 700;
  border: none;
  padding: 0;
  background: none;
}
.btn-toogle {
  width: 56px;
}

.dropdown-item {
  cursor: pointer;
}

.rounded-right-0 {
  border-top-right-radius: 0px !important;
  border-bottom-right-radius: 0px !important;
}

.rounded-left-0 {
  border-top-left-radius: 0px !important;
  border-bottom-left-radius: 0px !important;
}

</style>
