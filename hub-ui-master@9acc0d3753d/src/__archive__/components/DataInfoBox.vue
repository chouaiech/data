<template>
  <div class="data-info-box card mt-3">
    <app-link data-toggle="collapse"
       :fragment="`#dist-${collapseId}`"
       v-if="collapse">
      <div class="card-header">
        <h2 class="card-title text-dark" data-cy="dataset-title">{{ title || 'no title given' }}</h2>
      </div>
    </app-link>
    <app-link class="text-dark text-decoration-none" :to="{ path: linkTo, query: Object.assign({}, { locale: $route.query.locale }) }">
      <h2 class="card-header"
           v-if="!collapse"
           data-cy="dataset-title">
        <span class="">{{ title || 'no title given' }}</span>
      </h2>
    </app-link>

    <app-link class="text-dark text-decoration-none description-badges-link" :to="{ path: linkTo, query: Object.assign({}, { locale: $route.query.locale }) }">
      <div class="card-body" :class="{'collapse': collapse}" :id="`${collapse ? `dist-${collapseId}` : ''}`">
        <div class="row">
          <div class="col-12 col-md-2" v-if="bodyImg">
            <img class="body-img border border-dark big-flag" :class="{ 'io': bodyImg === 'io' }" ref="bodyImg" :src="getImg(bodyImg)" alt="Body Info Box Image">
          </div>
          <div v-if="description !== 'No description available'" :class="{'col-12 col-md-12': !bodyImg && !metadata,
                                                    'col-12 col-md-10': bodyImg && !metadata,
                                                    'col-12 col-md-7': bodyImg && metadata,
                                                    'col-12 col-md-9': !bodyImg && metadata}
                                                    ">
            <p v-if="!browser.isIE" class="card-text line-clamp" style="word-wrap:break-word;" data-cy="dataset-description">{{ truncate(description, descriptionLength) | stripHtml }}</p>
            <p v-else class="card-text" style="word-wrap:break-word;" data-cy="dataset-description">{{ truncate(description, descriptionLength) | stripHtml }}</p>
          </div>
          <div v-else class="text-muted font-italic"
                      :class="{'col-12 col-md-12': !bodyImg && !metadata,
                               'col-12 col-md-10': bodyImg && !metadata,
                               'col-12 col-md-7': bodyImg && metadata,
                               'col-12 col-md-9': !bodyImg && metadata}
                               ">
            <p class="card-text" style="word-wrap:break-word;" data-cy="dataset-description">
              {{ $t('message.catalogsAndDatasets.noDescriptionAvailable') }}
            </p>
          </div>
          <div class="col-12 col-md-3" v-if="!isNil(metadata) && isObject(metadata)">
            <span class="formats badge mr-1"
                  v-for="(tag, i) in bodyTags"
                  v-if="showDistributionFormat(tag) && i < 10"
                  :key="i"
                  :type="tag.label"
                  data-toggle="tooltip"
                  data-placement="top"
                  :title="$t('message.tooltip.datasetDetails.format')"
                  >
            {{ tag.label }}
            </span>
            <span v-if="bodyTags.length >= 10">...</span>
          </div>
        </div>
      </div>
    </app-link>

    <div class="card-footer d-flex justify-content-between align-items-center">
      <linkCopyBar :link="footerLink" v-if="hasFooterLink()"></linkCopyBar>
      <div class="d-flex flex-row-reverse">
        <div class="small d-flex mr-4 flex-wrap"
          v-for="(md, i) in metadata"
          :key="i"
          v-if="has(md, 'title') && !isNil(md.title) && has(md, 'value') && md.value !== undefined"
        >
          <div v-if="md.title == $t('message.metadata.created')" class="mr-1 font-weight-bold">
            <tooltip :title="$t('message.tooltip.datasetDetails.created')"> {{ md.title + ":"}} </tooltip>
          </div>
          <div v-else class="mr-1 font-weight-bold">
            <tooltip :title="$t('message.tooltip.datasetDetails.updated')"> {{ md.title + ":"}} </tooltip>
          </div>
          <div class="h-100">
            <div v-if="i == 'releaseDate'">
                <dataset-date class="align-self-start" :date="md.value"/>
              </div>
              <div v-else>
                <dataset-date class="align-self-start" :date="md.value"/>
              </div>
          </div>
        </div>
      </div>
      <!--
      <div class="w-50">
        <i class="material-icons feature-icon float-right" v-for="(fi, index) in featureIndicators" :key="index">{{ fi }}</i>
      </div>
      <span class="align-middle mr-1"
            v-for="(tag, index) in footerTags"
            :key="index">
          <span class="badge badge-secondary">{{ tag }}</span>
      </span>
      -->
      <span
        class="d-inline-block"
        data-toggle="tooltip"
        data-placement="top"
        :title="$t('message.tooltip.datasetDetails.catalogue')"
      >
        <span class="mr-1" v-if="!isEmpty(source)">
          <img v-if="has(source, 'sourceImage')" class="mr-1 border border-dark flag" :class="{ 'io': source.sourceImage === 'io' }" :src="getImg(source.sourceImage)" alt="Catalog Flag" >
          <small v-if="has(source, 'sourceTitle') && !isNil(source.sourceTitle)" >{{ getTranslationFor(source.sourceTitle, $route.query.locale, []) }}</small>
        </span>
      </span>
    </div>
  </div>
</template>

<script>
import {
  has,
  isNil,
  isEmpty,
  isObject,
} from 'lodash';
import moment from 'moment';
import LinkCopyBar from './LinkCopyBar';
import { helpers, dateFilters, DatasetDate, filtersMixin, AppLink, Tooltip, animations } from '@piveau/piveau-hub-ui-modules';
const {
  getTranslationFor,
  getImg,
  getCountryFlagImg,
  truncate,
} = helpers;

export default {
  name: 'dataInfoBox',
  components: {
    appLink: AppLink,
    linkCopyBar: LinkCopyBar,
    DatasetDate,
    Tooltip,
  },
  props: {
    collapse: {
      type: Boolean,
      default: false,
    },
    collapseId: {
      type: Number,
    },
    linkTo: {
      type: String,
    },
    title: {
      type: String,
      default: 'pass a title property, please',
    },
    description: {
      type: String,
    },
    descriptionLength: {
      type: Number,
      default: 100,
    },
    metadata: {
      type: Object,
    },
    bodyTags: {
      type: Array,
      default: () => [],
    },
    bodyImg: {
    },
    source: {
      type: Object,
    },
    footerTags: {
      type: Array,
      default: () => [],
    },
    /** link: {title: 'MyTitle', url: 'MyLink', target: '_blank'} */
    footerLink: {
      type: Object,
      default: () => {},
    },
    featureIndicators: {
      type: Array,
      default: () => [],
    },
  },
  mixins: [filtersMixin, animations],
  data() {
    return {
      browser: {
        /* eslint-disable-next-line */
        isIE: /*@cc_on!@*/false || !!document.documentMode,
      },
    };
  },
  computed: {},
  methods: {
    has,
    isNil,
    isEmpty,
    isObject,
    getTranslationFor,
    getImg,
    getCountryFlagImg,
    truncate,
    hasFooterLink() {
      return Boolean(this.footerLink) && Object.prototype.hasOwnProperty.call(this.footerLink, 'url');
    },
    showDistributionFormat(tag) {
      return has(tag, 'label') && !isNil(tag.label);
    },
    pathEndsWithSlash(path) {
      return (path.slice(-1) === '/');
    },
    filterDateFormatUS(date) {
      return dateFilters.formatUS(date);
    },
    filterDateFormatEU(date) {
      return dateFilters.formatEU(date);
    },
    isIncorrectDate(date) {
      // Falsy dates are considered as intentionally blank and are correct
      if (!date) return false;

      const m = moment(String(date));
      if (!m.isValid()) return true;

      // Dates in the future are incorrect.
      return moment().diff(m) < 0;
    },
  },
  created() {
  },
  mounted() {
    // this.fade('.data-info-box.fade-animation');
    // this.swingDown('.data-info-box.swing-down-animation');
    // this.pointExpansion('.data-info-box');
    // this.randomSideEnter('.data-info-box');
  },
  beforeDestroy() {
  },
};
</script>

<style lang="scss" scoped>
  @import '../styles/bootstrap_theme';

  .flag {
    max-width: 30px;
    max-height: 19px;
  }
  .big-flag {
    max-width: 126.41px;
    max-height: 85.09px;
  }
  // Truncated description text
  .truncated-gradient {
    height: 100%;
    max-height: 10rem;
    overflow: hidden;
    position: relative;
    &:after {
      content: "";
      position: absolute;
      bottom: 0;
      left: 0;
      background: linear-gradient(to bottom, rgba(255, 255, 255, 0), rgba(255, 255, 255, 1));
      height: 4rem;
      width: 100%;
    }
  }

  .data-info-box {
    border-left: 3px solid rgba($primary, .8);
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    transition: all .3s cubic-bezier(.25, .8, .25, 1);
    &:hover {
      border-left: 3px solid rgba($primary, 1.0);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.25), 0 4px 8px rgba(0, 0, 0, 0.22);
      cursor: pointer;
    }

    .description-badges-link {
      flex-grow: 1;
      min-height: 150px;
    }

    .line-clamp {
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 4;
      overflow: hidden;
    }

    h2 {
      font-size: 1.5rem;
    }

    .formats {
      color: #fff;
      background-color: #595959;

      &[type="HTML"] {
        background-color: #285C76;
      }
      &[type="JSON"] {
        background-color: var(--dark-orange);
      }
      &[type="XML"] {
        background-color: #8F4300;
      }
      &[type="TXT"] {
        background-color: #2B5E73;
      }
      &[type="CSV"] {
        background-color: var(--badge-green);
      }
      &[type="XLS"] {
        background-color: #1A6537;
      }
      &[type="ZIP"] {
        background-color: #252525;
      }
      &[type="API"] {
        background-color: #923560;
      }
      &[type="PDF"] {
        background-color: #B30519;
      }
      &[type="SHP"] {
        background-color: var(--badge-black);
      }
      &[type="RDF"],
      &[type="NQUAD"],
      &[type="NTRIPLES"],
      &[type="TURTLE"] {
        background-color: #0b4498;
      }
    }

    .card-footer {
      display:flex;
      flex-direction: row;
      justify-content: space-between;

      i {
        font-size: 32px;
        vertical-align: middle;
        margin: 0 0 .1em 0;
      }
    }

    .io {
      border: 0 !important;
      margin-bottom: 3px;
      opacity: 0.8;
    }
  }

</style>
