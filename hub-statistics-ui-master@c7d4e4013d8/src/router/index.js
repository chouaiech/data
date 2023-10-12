import Vue from 'vue'
import Router from 'vue-router'

const CurrentState = () => import(/* webpackChunkName: "CurrentState" */'@/components/current-state/CurrentState')
const Categories = () => import(/* webpackChunkName: "CurrentState" */'@/components/current-state/Categories')
const Countries = () => import(/* webpackChunkName: "CurrentState" */'@/components/current-state/Countries')
const CountryAndCatalogue = () => import(/* webpackChunkName: "CurrentState" */'@/components/current-state/CountryAndCatalogue')
const Evolution = () => import(/* webpackChunkName: "Evolution" */'@/components/evolution/Evolution')
const ChartEvolutionTotal = () => import(/* webpackChunkName: "Evolution" */'@/components/evolution/ChartEvolutionTotal')
const ChartEvolutionCategories = () => import(/* webpackChunkName: "Evolution" */'@/components/evolution/ChartEvolutionCategories')
const ChartEvolutionCountries = () => import(/* webpackChunkName: "Evolution" */'@/components/evolution/ChartEvolutionCountries')
const ChartEvolutionCountryAndCatalogue = () => import(/* webpackChunkName: "Evolution" */'@/components/evolution/ChartEvolutionCountryAndCatalogue')


Vue.use(Router)

const rootBreadcrumb = [
  { text: 'Home', to: null, href: '/' }
]

const router = new Router({
  mode: 'history',
  base: '/catalogue-statistics/',
  routes: [
    {
      path: '/currentState',
      name: 'CurrentState',
      component: CurrentState,
      meta: {
        breadcrumbs ({t}) {
          return [
            ...rootBreadcrumb,
            { text: t.call(this, 'message.header.navigation.data.statistics'), to: { name: 'CurrentState - Datasets per Category' } },
            { text: t.call(this, 'message.common.currentCatalogueStatistics'), to: { name: 'CurrentState - Datasets per Category' } },
          ]
        }
      },
      children: [
        {
          path: '',
          redirect: { name: 'CurrentState - Datasets per Category' },
        },
        {
          path: 'category',
          name: 'CurrentState - Datasets per Category',
          component: Categories,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCategory'), to: { name: 'CurrentState - Datasets per Category' } },
              ]
            }
          },
        },
        {
          path: 'countries',
          name: 'CurrentState - Datasets per Country',
          component: Countries,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCountry'), to: { name: 'CurrentState - Datasets per Country' } },
              ]
            }
          },
        },
        {
          path: 'countryCatalogue',
          name: 'CurrentState - Datasets per Country and Catalogue',
          component: CountryAndCatalogue,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCountryAndCatalogue'), to: { name: 'CurrentState - Datasets per Country and Catalogue' } },
              ]
            }
          },
        },
      ],
    },
    {
      path: '/evolution',
      name: 'Evolution',
      component: Evolution,
      meta: {
        breadcrumbs ({t}) {
          return [
            ...rootBreadcrumb,
            { text: t.call(this, 'message.header.navigation.data.statistics'), to: { name: 'CurrentState - Datasets per Category' } },
            { text: t.call(this, 'message.common.historicalCatalogueStatistics'), to: { name: 'Evolution - Total number of Datasets' } }
          ]
        }
      },
      children: [
        {
          path: '',
          name: 'Evolution - Total number of Datasets',
          component: ChartEvolutionTotal,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.totalNumber'), to: { name: 'Evolution - Total number of Datasets' } },
              ]
            }
          },
        },
        {
          path: 'category',
          name: 'Evolution - Datasets per Category',
          component: ChartEvolutionCategories,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCategory'), to: { name: 'Evolution - Datasets per Category' } },
              ]
            }
          },
        },
        {
          path: 'countries',
          name: 'Evolution - Datasets per Country',
          component: ChartEvolutionCountries,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCountry'), to: { name: 'Evolution - Datasets per Country' } },
              ]
            }
          },
        },
        {
          path: 'countryCatalogue',
          name: 'Evolution - Datasets per Country and Catalogue',
          component: ChartEvolutionCountryAndCatalogue,
          meta: {
            breadcrumbs ({t}) {
              return [
                { text: t.call(this, 'message.statistics.datasetsPerCountryAndCatalogue'), to: { name: 'Evolution - Datasets per Country and Catalogue' } },
              ]
            }
          },
        },
      ],
    },
    {
      path: '*',
      redirect: { name: 'CurrentState - Datasets per Category' }
    }
  ]
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title
  next()
})

export default router
