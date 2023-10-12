import Vue from 'vue'
import Router from 'vue-router'
import vueSmoothScroll from 'vue2-smooth-scroll'

const Dashboard = () => import(/* webpackChunkName: "Dashboard" */'@/components/Dashboard')
const DimensionsDetailsModal = () => import(/* webpackChunkName: "Dashboard" */'@/components/dimensions-cards/DimensionsDetailsModal')

const Catalogues = () => import(/* webpackChunkName: "Catalogues" */'@/components/Catalogues')
const CatalogueDetail = () => import(/* webpackChunkName: "Catalogues" */'@/components/CatalogueDetail')
const CatalogueDetailDashboard = () => import(/* webpackChunkName: "Catalogues" */'@/components/CatalogueDetailDashboard')
const CatalogueDetailDistributions = () => import(/* webpackChunkName: "Catalogues" */'@/components/CatalogueDetailDistributions')
const CatalogueDetailViolations = () => import(/* webpackChunkName: "Catalogues" */'@/components/CatalogueDetailViolations')

const Methodology = () => import(/* webpackChunkName: "Methodology" */'@/components/Methodology')

Vue.use(vueSmoothScroll)
Vue.use(Router)

const rootBreadcrumb = [
  { text: 'Home', to: null, href: '/' },
  { text: 'Metadata Quality Dashboard', to: { name: 'Dashboard' } }
]

export default new Router({
  mode: 'history',
  base: '/mqa',
  routes: [
    {
      path: '/',
      name: 'Dashboard',
      component: Dashboard,
      children: [{
        path: 'dimensions',
        redirect: { name: 'Dashboard' }
      },
      {
        path: 'dimensions/:dimension',
        name: 'dimensions-details',
        component: DimensionsDetailsModal,
        props: {
          dimensionsType: 'dimensions-details'
        }
      }],
      meta: {
        breadcrumbs ({ t }) {
          return [
            ...rootBreadcrumb,
            { text: t.call(this, 'message.navigation.dashboard'), to: { name: 'Dashboard' } }
          ]
        }
      }
    },
    {
      path: '/methodology',
      name: 'Methodology',
      component: Methodology,
      meta: {
        breadcrumbs ({ t }) {
          return [
            ...rootBreadcrumb,
            { text: t.call(this, 'message.navigation.methodology'), to: { name: 'Methodology' } }
          ]
        }
      }
    },
    {
      path: '/catalogues',
      name: 'Catalogues',
      component: Catalogues,
      props: true,
      children: [
        {
          path: ':id',
          components: {
            catalogueDetail: CatalogueDetail
          },
          props: true,
          children: [
            {
              path: '',
              name: 'CatalogueDetailDashboard',
              components: {
                catalogueDetailNavigation: CatalogueDetailDashboard
              },
              children: [{
                path: 'dimensions',
                redirect: { name: 'CatalogueDetailDashboard' }
              },
              {
                path: 'dimensions/:dimension',
                name: 'catalogue-dimensions-details',
                component: DimensionsDetailsModal,
                props: {
                  dimensionsType: 'catalogue-dimensions-details'
                }
              }]
            },
            {
              path: 'distributions',
              name: 'CatalogueDetailDistributions',
              components: {
                catalogueDetailNavigation: CatalogueDetailDistributions
              },
              meta: {
                breadcrumbs ({ t }) {
                  return [
                    { text: t.call(this, 'message.catalogue_detail.distributions.title'), to: { name: 'CatalogueDetailDistributions' } }
                  ]
                }
              }
            },
            {
              path: 'violations',
              name: 'CatalogueDetailViolations',
              components: {
                catalogueDetailNavigation: CatalogueDetailViolations
              },
              meta: {
                breadcrumbs ({ t }) {
                  return [
                    { text: t.call(this, 'message.common.site_title_violations'), to: { name: 'CatalogueDetailViolations' } }
                  ]
                }
              }
            }
          ]
        }
      ],
      meta: {
        breadcrumbs ({ route, store, t }) {
          const catalogTitle = store.getters.getCatalogue && store.getters.getCatalogue.info && store.getters.getCatalogue.info.title
          return [
            ...rootBreadcrumb,
            { text: t.call(this, 'message.navigation.catalogues'), to: { name: 'Catalogues' } },
            ...(route.params.id ? [{ text: catalogTitle, to: { name: 'CatalogueDetailDashboard' } }] : [])
          ]
        }
      }
    }
  ]
})
