<template>
  <div class="container mb-5" id="container">
    <h2 class="headline">{{ $t("message.statistics.currentState") }}</h2>
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
  name: 'CurrentState',
  metaInfo () {
    return {
      title: this.$t("message.statistics.page.title"),
      meta: [{
        name: 'description',
        content: this.$t("message.statistics.meta.description")}
      ]
    }
  },
  data () {
    return {
      tabs: [
        {
          name: 'CurrentState - Datasets per Category',
          text: this.$t("message.statistics.datasetsPerCategory"),
        },
        {
          name: 'CurrentState - Datasets per Country',
          text: this.$t("message.statistics.datasetsPerCountry"),
        },
        {
          name: 'CurrentState - Datasets per Country and Catalogue',
          text: this.$t("message.statistics.datasetsPerCountryAndCatalogue"),
        },
      ]
    }
  },
}
</script>

<style lang="scss" scoped>
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
  padding-right: 17px;
  padding-bottom: 20px;

}
.headline {
  color: #004494;
  font-weight: 500;
  margin-bottom: 0;
  padding-bottom: 20px;
}
</style>

<style lang="scss">
// .icon-change:hover > .row-img > .flag-icon {
//   &[type="Economy and finance"] {
//     background: url('../../assets/img/categories/mouse_hover/economy.png') !important;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Environment"] {
//     background: url('../../assets/img/categories/mouse_hover/environment.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Regions and cities"] {
//     background: url('../../assets/img/categories/mouse_hover/regions.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Population and society"] {
//     background: url('../../assets/img/categories/mouse_hover/population.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Education, culture and sport"] {
//     background: url('../../assets/img/categories/mouse_hover/education.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Health"] {
//     background: url('../../assets/img/categories/mouse_hover/health.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Science and technology"] {
//     background: url('../../assets/img/categories/mouse_hover/science.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Transport"] {
//     background: url('../../assets/img/categories/mouse_hover/transport.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 35px !important;
//     background-position: left -4px bottom 6px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Agriculture, fisheries, forestry and food"] {
//     background: url('../../assets/img/categories/mouse_hover/agriculture.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Justice, legal system and public safety"] {
//     background: url('../../assets/img/categories/mouse_hover/justice.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Energy"] {
//     background: url('../../assets/img/categories/mouse_hover/energy.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="International issues"] {
//     background: url('../../assets/img/categories/mouse_hover/international.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
//   &[type="Government and public sector"] {
//     background: url('../../assets/img/categories/mouse_hover/government.png');
//     background-size: cover;
//     background-color: white !important;
//     background-size: 45px !important;
//     background-position: left -10px bottom 4px !important;
//     background-repeat: no-repeat !important;
//   }
// }

.category-icon{
  height: 50px;
  width: 50px;
  background-repeat: no-repeat !important;
  margin: 0 auto;
  background-position: center !important;

  &[type="Economy and finance"] {
    background: url('../../assets/img/categories/mouse_hover/economy.png');
  }
  &[type="Environment"] {
    background: url('../../assets/img/categories/mouse_hover/environment.png');
  }
  &[type="Regions and cities"] {
    background: url('../../assets/img/categories/mouse_hover/regions.png');
  }
  &[type="Population and society"] {
    background: url('../../assets/img/categories/mouse_hover/population.png');
  }
  &[type="Education, culture and sport"] {
    background: url('../../assets/img/categories/mouse_hover/education.png');
  }
  &[type="Health"] {
    background: url('../../assets/img/categories/mouse_hover/health.png');
  }
  &[type="Science and technology"] {
    background: url('../../assets/img/categories/mouse_hover/science.png');
  }
  &[type="Transport"] {
    background: url('../../assets/img/categories/mouse_hover/transport.png');
  }
  &[type="Agriculture, fisheries, forestry and food"] {
    background: url('../../assets/img/categories/mouse_hover/agriculture.png');
  }
  &[type="Justice, legal system and public safety"] {
    background: url('../../assets/img/categories/mouse_hover/justice.png');
  }
  &[type="Energy"] {
    background: url('../../assets/img/categories/mouse_hover/energy.png');
  }
  &[type="International issues"] {
    background: url('../../assets/img/categories/mouse_hover/international.png');
  }
  &[type="Government and public sector"] {
    background: url('../../assets/img/categories/mouse_hover/government.png');
  }
}

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}

.total-sum{
  font-weight: 500;
}

.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}
.down {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  margin-bottom:3px
}
.up {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
  margin-bottom:1px
}

.table-img {
  cursor: pointer;
  text-align: left;
  width: 100px;
}
.table-name {
  cursor: pointer;
  text-align: left;
  width: 350px;
}
.table-trend {
  cursor: pointer;
  text-align: center;
  width: 150px;
  padding-right: 20px !important;
}
.table-percent {
  cursor: pointer;
  text-align: left;
  width: 160px;
}
.table-number {
  cursor: pointer;
  text-align: center;
  width: 150px;
}
.table-updated {
  cursor: pointer;
  text-align: left;
  width: 110px;
}

.row-img {
  text-align: left;
  width: 100px;
}
.row-name {
  text-align: left;
  width: 350px;

  div {
    color: #004494 !important;
  }
}
.row-trend {
  text-align: left;
  width: 100px;
  padding-right: 20px !important;

  .trend {
    display: block !important;
    width: 75px !important;
    text-align: center !important;

    &.success {
      background-color: #467A39;
    }
    &.error {
      background-color: #DA2131;
    }
  }
}
.row-percent {
  text-align: left;
  width: 160px;
}
.row-number {
  text-align: center;
  width: 150px;
}
.row-updated {
  text-align: left;
  width: 110px;
  font-size: 15px;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

  .progress-bar {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    background-color: #3B6AA0;
    -webkit-transition: width 0.6s ease;
    transition: width 0.6s ease;
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



#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}

.total-sum{
  font-weight: 500;
}

.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}
.down {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  margin-bottom:3px
}
.up {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
  margin-bottom:1px
}

.table-img {
  cursor: pointer;
  text-align: left;
  width: 100px;
}
.table-name {
  cursor: pointer;
  text-align: left;
  width: 450px;
}
.table-trend {
  cursor: pointer;
  text-align: center;
  width: 150px;
  padding-right: 20px !important;
}
.table-percent {
  cursor: pointer;
  text-align: left;
  width: 160px;
}
.table-number {
  cursor: pointer;
  text-align: center;
  width: 150px;
}
.table-updated {
  cursor: pointer;
  text-align: left;
  width: 110px;
}

.row-img {
  text-align: left;
  width: 100px;
}
.row-name {
  text-align: left;
  width: 450px;
}
.row-trend {
  text-align: left;
  width: 150px;
  padding-right: 20px !important;
}
.row-percent {
  text-align: left;
  width: 160px;
}
.row-number {
  text-align: center;
  width: 150px;
}
.row-updated {
  text-align: left;
  width: 110px;
  font-size: 15px;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

  .flag-icon{
    width: 25px;
    border-radius: 50%;
    margin-right: 14px;
    margin-left: 20px;
  }

  .progress-bar {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    background-color: #3B6AA0;
    -webkit-transition: width 0.6s ease;
    transition: width 0.6s ease;
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

[v-cloak] { display: none; }

#detail-table{
  margin-top:25px;
}

.total-number{
  padding-left: 5px;
  margin-bottom: 10px;
}

.total-sum{
  font-weight: 500;
}

.active-table-head{
  text-decoration:underline;
}

.active{
   text-decoration: none;
}
.down {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  margin-bottom:3px
}
.up {
  border: solid black;
  border-width: 0 1px 1px 0;
  display: inline-block;
  padding: 2px;
  transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
  margin-bottom:1px
}

.table-img {
  cursor: pointer;
  text-align: left;
  width: 100px;
}
.table-img-sort {
  cursor: pointer;
  text-align: center;
  width: 130px;
}
.table-name-small {
  cursor: pointer;
  text-align: left;
  width: 420px;
}
.table-trend {
  cursor: pointer;
  text-align: center;
  width: 150px;
  padding-right: 20px !important;
}
.table-name {
  cursor: pointer;
  text-align: left;
  width: 450px;
}
.table-percent {
  cursor: pointer;
  text-align: left;
  width: 160px;
}
.table-number {
  cursor: pointer;
  text-align: center;
  width: 150px;
}
.table-updated {
  cursor: pointer;
  text-align: left;
  width: 110px;
}

.row-img {
  text-align: left;
  width: 100px;
}
.row-name {
  text-align: left;
  width: 450px;
}
.row-percent {
  text-align: left;
  width: 160px;
}
.row-number {
  text-align: center;
  width: 150px;
}
.row-updated {
  text-align: left;
  width: 110px;
  font-size: 15px;
}

.hide{
  visibility: hidden;
  font-size: 0px;
}

  .flag-icon{
    width: 25px;
    border-radius: 50%;
    margin-right: 14px;
    margin-left: 20px;
  }
  .flag-icon{
    width: 25px;
    border-radius: 50%;
    margin-right: 14px;
    margin-left: 20px;
  }

  .progress-bar {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: normal;
    -ms-flex-direction: column;
    flex-direction: column;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    background-color: #3B6AA0;
    -webkit-transition: width 0.6s ease;
    transition: width 0.6s ease;
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


.ior {
  opacity: 0.8;
}

thead {
  background-color: #F2F5F9 !important;
}
.table td, .table th { padding: 0.75rem; vertical-align: middle; border-top: 1px solid #dee2e6; }
</style>
