<template>
  <div class="container mb-5" id="container">
    <h2 class="headline">{{ $t("message.statistics.evolution") }}</h2>
    <nav class="ecl-tabs" data-ecl-tabs="true" data-ecl-auto-init="Tabs">
      <div class="ecl-tabs__container">
        <ul class="ecl-tabs__list" role="tablist">
          <li class="ecl-tabs__item" role="presentation" v-for="tab in tabs" :key="tab.name">
            <router-link :to="{ name: tab.name, query: { locale: $i18n.locale } }"
              class="ecl-link ecl-tabs__link"
              :class="{ 'ecl-tabs__link--active': $route.name === tab.name }"
              role="tab"
              aria-selected="false">{{ tab.text }}</router-link>
          </li>
        </ul>
      </div>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'Evolution',
  metaInfo () {
    return {
      title: `${this.$t('message.common.historicalCatalogueStatistics')} - data.europa.eu`
    }
  },
  data () {
    return {
      tabs: [
        {
          name: 'Evolution - Total number of Datasets',
          text: this.$t("message.statistics.totalNumber"),
        },
        {
          name: 'Evolution - Datasets per Category',
          text: this.$t("message.statistics.datasetsPerCategory"),
        },
        {
          name: 'Evolution - Datasets per Country',
          text: this.$t("message.statistics.datasetsPerCountry"),
        },
        {
          name: 'Evolution - Datasets per Country and Catalogue',
          text: this.$t("message.statistics.datasetsPerCountryAndCatalogue"),
        },
      ]
    }
  },
}
</script>

<style lang="scss" scoped>
.btn {
  border-radius: 1.875rem;
}

.btn-primary {
  background-color: var(--medium-blue);
  border-color: var(--medium-blue);

  &:hover {
    background-color: #4070d6;
    border-color: #4476e2;
  }
}

.active {
  background-color: #001D85 !important;
  border-color: #001D85 !important;
}

#container{
//font-family: 'Ubuntu', 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: #fff;
}

.filter {
  float: right;
  padding-right: 18px;
  padding-bottom: 20px;
}

.headline {
  color: #004494;
  font-weight: 500;
  margin-bottom: 0;
  padding-bottom: 20px;
}

.spacer {
  margin-top: 20px;
}
</style>

<style lang="scss">
.line-chart{
  padding-top: 50px;
}

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}


.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

 #percent {
    text-align: center;
    color: #000;
    margin-right: 135px;
    font-size: 16px;
  }

  #less{
    font-size: 12px;
    color: darkgrey;
  }

  #sticky {
  background-color: #fff !important;
  background: solid;
  min-width: 952px !important;
}

.line-chart{
  padding-top: 50px;
}

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}


.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

 #percent {
    text-align: center;
    color: #000;
    margin-right: 135px;
    font-size: 16px;
  }

  #less{
    font-size: 12px;
    color: darkgrey;
  }

  #sticky {
  background-color: #fff !important;
  background: solid;
  min-width: 952px !important;
}

.line-chart{
  padding-top: 50px;
}

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}


.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

 #percent {
    text-align: center;
    color: #000;
    margin-right: 135px;
    font-size: 16px;
  }

  #less{
    font-size: 12px;
    color: darkgrey;
  }

  #sticky {
  background-color: #fff !important;
  background: solid;
  min-width: 952px !important;
}
.line-chart{
  padding-top: 50px;
}

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}


.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

 #percent {
    text-align: center;
    color: #000;
    margin-right: 135px;
    font-size: 16px;
  }

  #less{
    font-size: 12px;
    color: darkgrey;
  }

  #sticky {
  background-color: #fff !important;
  background: solid;
  min-width: 952px !important;
}

.ul-line-chart-country{
  display: -webkit-flex;
  display: flex;
  -webkit-flex-flow: column wrap;
  flex-flow: wrap;
  padding:0;
  column-gap: 40px;
}
.hidden{
  text-decoration: line-through;
  text-decoration-thickness: 2px;
}
.line-chart-country-clickable-legend{
  margin: 0 0px 8px 0;
}
#line-chart-country-js-legend ul li{
  list-style-type: none;
  font: 11px Verdana;
  white-space: nowrap;
  color: #666666;
  display: flex;
  padding-right: 10px;
}
#line-chart-country-js-legend ul li span{
  width: 40px;
  height: 15px;
  display: inline-block;
  margin: 0 5px 0px 0;
}

.ul-line-chart-category{
  display: -webkit-flex;
  display: flex;
  -webkit-flex-flow: column wrap;
  flex-flow: wrap;
  padding:0;
  column-gap: 40px;
}
.hidden{
  text-decoration: line-through;
  text-decoration-thickness: 2px;
}
.line-chart-country-clickable-legend{
  margin: 0 0px 8px 0;
}
#line-chart-category-js-legend ul li{
  list-style-type: none;
  font: 11px Verdana;
  white-space: nowrap;
  color: #666666;
  display: flex;
  padding-right: 10px;
}
#line-chart-category-js-legend ul li span{
  width: 40px;
  height: 15px;
  display: inline-block;
  margin: 0 5px 0px 0;
}

.ul-line-chart-catalogue {
  display: -webkit-flex;
  display: flex;
  -webkit-flex-flow: column wrap;
  flex-flow: wrap;
  justify-content: space-between;
  padding: 0;
  margin-top: 1rem;
}
.hidden {
  text-decoration: line-through;
  text-decoration-thickness: 2px;
}
.line-chart-catalogue-clickable-legend {
  margin: 0 0px 8px 0;
}
#line-chart-catalogue-js-legend ul li {
  width: 30%;
  list-style-type: none;
  font: 11px Verdana;
  white-space: nowrap;
  color: #666666;
  display: flex;
  padding-right: 10px;
}
#line-chart-catalogue-js-legend ul li span {
  height: 15px;
  display: inline-block;
  margin: 0 5px 0px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
#line-chart-catalogue-js-legend ul li span:first-child {
  min-width: 40px;
}
</style>
