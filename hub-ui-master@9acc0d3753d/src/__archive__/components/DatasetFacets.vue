<template>
  <div class="container dataset-facets">
    <div class="row mx-3 mr-md-0">
      <div class="col">
        <!-- Location suggestions and Map -->
        <div v-if="useDatasetFacetsMap && !showCatalogDetails">
          <div class="row">
              <div class="input-group suggestion-input-group mb-3">
                  <input type="text" class="form-control suggestion-input"
                        :aria-label="$t('message.datasets.findLocation')"
                        :placeholder="$t('message.datasets.findLocation')"
                        :title="$t('message.tooltip.locationFilter')"
                        data-toggle="tooltip"
                        data-placement="right"
                        v-model="gazetteer.searchbarText"
                        @focus="gazetteer.selected = false; gazetteer.searchbarText = ''"
                        @input="getAutocompleteSuggestions(gazetteer.searchbarText)"
                        @keyup.enter="getAutocompleteSuggestions(gazetteer.searchbarText)">
                  <div class="input-group-append">
                    <button class="btn btn-primary rounded-right search-button" :title="$t('message.tooltip.locationFilter')">
                      <i class="material-icons align-bottom">search</i>
                    </button>
                  </div>
                  <div class="suggestion-list-group">
                    <ul class="list-group suggestion-list">
                      <button class="list-group-item list-group-item-action"
                          v-for="(suggestion, i) in gazetteer.suggestions"
                          :key="i"
                          v-if="i <= 9 && !gazetteer.selected"
                          @click="handleSuggestionSelection(suggestion)">
                        {{suggestion.name}}
                      </button>
                    </ul>
                  </div>
              </div>
          </div>
          <div class="row position-relative mb-3">
            <map-bounds-receiver class="border-secondary map focus-border"
                                 :start-bounds="map.receiver.startBounds"
                                 :height="map.receiver.height"
                                 :width="map.receiver.width"
                                 :map-container-id="map.receiver.mapContainerId"
                                 :bounds-id="map.geoBoundsId"
                                 :title="$t('message.tooltip.locationFilter')"
                                  data-toggle="tooltip"
                                  data-placement="top"
                                 ref="mapReceiver"></map-bounds-receiver>
            <button class="btn btn-highlight reset-bounds-button" v-if="getGeoBoundsById(map.geoBoundsId)" @click="resetBoundsFor(map.geoBoundsId)">Reset Bounds</button>
            <button class="btn btn-sm btn-secondary map-modal-button" data-toggle="modal" data-target=".map-modal" @click="triggerResize()">
              <i class="material-icons">fullscreen</i>
            </button>
            <!-- Modal Map Start -->
            <div id="modal-map-wrapper" class="modal fade map-modal pr-md-4 pl-md-4 pr-0 pl-0" tabindex="-1" role="dialog" aria-labelledby="Large map view" aria-hidden="true">
              <div class="modal-dialog mt-md-4 mb-md-4 m-0">
                <div class="modal-content">
                  <div class="modal-header">
                    <h2 class="modal-title">{{ $t('message.mapModal.drawRectangleMsg') }}</h2>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div class="modal-body d-flex flex-row flex-wrap p-md-3 p-0">
                    <map-bounds-sender :start-bounds="map.sender.startBounds"
                                       :height="map.sender.height"
                                       :width="map.sender.width"
                                       :map-container-id="map.sender.mapContainerId"
                                       :bounds-id="map.geoBoundsId"
                                       ref="mapSender"></map-bounds-sender>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal">{{ $t('message.mapModal.close') }}</button>
                    <button type="button" class="btn btn-highlight" data-dismiss="modal" @click="resetBoundsFor(map.geoBoundsId)">{{ $t('message.mapModal.reset') }}</button>
                    <button type="button" class="btn btn-primary" @click="applyHoldedBounds()" data-dismiss="modal">{{ $t('message.mapModal.findDatasets') }}</button>
                  </div>
                </div>
              </div>
            </div>
            <!-- Modal Map End -->
          </div>
        </div>
        <!-- CATALOG DETAILS -->
        <div class="row position-relative" v-if="showCatalogDetails">
          <span class="px-3">
            <img v-if="has(catalog, 'country.id')"
                  class="catalog-flag border border-dark"
                  :class="{ 'io': catalog.country.id === 'io' }"
                  :src="getImg(getCatalogImage(catalog))"
                  width="100%"
                  height="auto"
                  :alt="catalog.country.id">
          </span>
          <div class="col mt-3" v-if="showObject(catalog)">
            <div class="small">
              <!-- TITLE -->
              <dl v-if="has(catalog, 'title') && showObject(catalog.title)">
                <dt>
                  <span :title="$t('message.tooltip.catalogDetails.title')"
                        data-toggle="tooltip"
                        data-placement="right">
                        {{ $t('message.metadata.title') }}
                  </span>
                </dt>
                <dd>{{ getTranslationFor(catalog.title, $route.query.locale, catalog.languages) }}</dd>
              </dl>
              <!-- DESCRIPTION -->
              <dl v-if="has(catalog, 'description') && showObject(catalog.description)">
                <dt>
                    <span :title="$t('message.tooltip.catalogDetails.description')"
                          data-toggle="tooltip"
                          data-placement="right">
                         {{ $t('message.metadata.description') }}
                  </span>
                </dt>
                <dd>{{ getTranslationFor(catalog.description, $route.query.locale, catalog.languages) }}</dd>
              </dl>
              <!-- PUBLISHER -->
              <dl v-if="has(catalog, 'publisher') && showObject(catalog.publisher)">
                <dt>
                  <span :title="$t('message.tooltip.catalogDetails.publisher')"
                          data-toggle="tooltip"
                          data-placement="right">
                          {{ $t('message.metadata.publisher') }}
                  </span>
                </dt>
                <dd v-if="has(catalog, 'publisher.name')  && showString(catalog.publisher.name)">{{ catalog.publisher.name }}</dd>
                <dd>
                  <app-link v-if="has(catalog, 'publisher.homepage') && showString(catalog.publisher.homepage)" :to="catalog.publisher.homepage">
                    {{ catalog.publisher.homepage }}
                  </app-link>
                </dd>
                <dd>
                  <app-link v-if="has(catalog, 'publisher.email') && showString(catalog.publisher.email)" :to="catalog.publisher.email">
                    {{ catalog.publisher.email }}
                  </app-link>
                </dd>
              </dl>
              <!-- CREATOR -->
              <dl v-if="has(catalog, 'creator') && showObject(catalog.creator)">
                <dt>{{ $t('message.metadata.creator') }}</dt>
                <dd v-if="has(catalog, 'creator.name')  && showString(catalog.creator.name)">{{ catalog.creator.name }}</dd>
                <dd>
                  <app-link v-if="has(catalog, 'creator.homepage') && showString(catalog.creator.homepage)" :to="catalog.creator.homepage">
                    {{ catalog.creator.homepage }}
                  </app-link>
                </dd>
                <dd>
                  <app-link v-if="has(catalog, 'creator.email') && showString(catalog.creator.email)" :to="catalog.creator.email">
                    {{ catalog.creator.email }}
                  </app-link>
                </dd>
              </dl>
              <!-- HOMEPAGE -->
              <dl v-if="has(catalog, 'homepage') && showString(catalog.homepage)">
                <dt>{{ $t('message.metadata.homepage') }}</dt>
                <dd>
                  <app-link :to="catalog.homepage">
                    {{ catalog.homepage }}
                  </app-link>
                </dd>
              </dl>
              <!-- LANGUAGES -->
              <dl v-if="has(catalog, 'languages') && showArray(catalog.languages)">
                <dt>
                  <span :title="$t('message.tooltip.catalogDetails.language')"
                        data-toggle="tooltip"
                        data-placement="right">
                         {{ $t('message.metadata.languages') }}
                  </span>
                </dt>
                <dd v-for="lang in catalogLanguageIds" :key="lang">{{ lang }}</dd>
              </dl>
              <!-- LICENCE -->
              <dl v-if="has(catalog, 'licence') && showObject(catalog.licence)">
                <dt>{{ $t('message.catalogFacets.facets.licences') }}</dt>
                <dd v-if="has(catalog, 'licence.label') && showString(catalog.licence.label)">{{ catalog.licence.label }}</dd>
                <dd v-if="has(catalog, 'licence.description') && showString(catalog.licence.description)">{{ catalog.licence.description }}</dd>
                <dd>
                  <app-link v-if="has(catalog, 'licence.resource') && showString(catalog.licence.resource)" :to="catalog.licence.resource">
                    {{ catalog.licence.resource }}
                  </app-link>
                </dd>
                <dd>
                  <app-link v-if="has(catalog, 'licence.la_url') && showString(catalog.licence.la_url)" :to="catalog.licence.la_url">
                    {{ catalog.licence.la_url }}
                  </app-link>
                </dd>
              </dl>
              <!-- ISSUED -->
              <dl v-if="has(catalog, 'issued') && !isNil(catalog.issued)">
                <dt>
                    <span :title="$t('message.tooltip.catalogDetails.created')"
                    data-toggle="tooltip"
                    data-placement="right">
                  {{ $t('message.metadata.created') }}
                  </span>
                </dt>
                <dd>{{ filterDateFormatEU(catalog.issued) }}</dd>
              </dl>
              <!-- MODIFIED -->
              <dl v-if="has(catalog, 'modified') && !isNil(catalog.modified)">
                <dt :title="$t('message.tooltip.catalogDetails.updated')">
                   <span :title="$t('message.tooltip.catalogDetails.updated')"
                    data-toggle="tooltip"
                    data-placement="right">
                    {{ $t('message.metadata.updated') }}
                  </span>
                  </dt>
                <dd>{{ filterDateFormatEU(catalog.modified) }}</dd>
              </dl>
              <!-- RIGHTS -->
              <dl v-if="has(catalog, 'rights') && showObject(catalog.rights)">
                <dt>{{ $t('message.metadata.rights') }}</dt>
                <dd v-if="has(catalog, 'rights.label')  && showString(catalog.rights.label)">{{ catalog.rights.label }}</dd>
                <dd>
                  <app-link v-if="has(catalog, 'rights.resource') && showString(catalog.rights.resource)" :to="catalog.rights.resource">
                    {{ catalog.rights.resource }}
                  </app-link>
                </dd>
              </dl>
              <!-- IS PART OF -->
              <dl v-if="has(catalog, 'isPartOf') && showString(catalog.isPartOf)">
                <dt>{{ $t('message.metadata.isPartOf') }}</dt>
                <dd>{{ catalog.isPartOf }}</dd>
              </dl>
              <!-- HAS PART -->
              <dl v-if="has(catalog, 'hasPart') && showArrayOfStrings(catalog.hasPart)">
                <dt>{{ $t('message.metadata.hasPart') }}</dt>
                <dl v-for="(hasPart, i) of catalog.hasPart" :key="i">
                  {{ hasPart }}
                </dl>
              </dl>
              <!-- THEME TAXONOMY -->
              <dl v-if="has(catalog, 'themeTaxonomy') && showArrayOfStrings(catalog.themeTaxonomy)">
                <dt>{{ $t('message.metadata.themeTaxonomy') }}</dt>
                <dl v-for="(themeTaxonomy, i) of catalog.themeTaxonomy" :key="i">
                  {{ themeTaxonomy }}
                </dl>
              </dl>
              <!-- SPATIAL -->
              <dl v-if="has(catalog, 'spatial') && showObjectArray(catalog.spatial)">
                <dt>{{ $t('message.metadata.spatial') }}</dt>
                <dl v-for="(spatial, i) of catalog.spatial" :key="i">
                  <dd v-if="has(spatial, 'coordinates') && showString(spatial.coordinates)">{{ $t('message.metadata.coordinates') }}: {{ spatial.coordinates }}</dd>
                  <dd v-if="has(spatial, 'type') && showString(spatial.type)">{{ $t('message.metadata.type') }}: {{ spatial.type }}</dd>
                </dl>
              </dl>
              <!-- SPATIAL RESOURCE -->
              <dl v-if="has(catalog, 'spatialResource') && showArray(catalog.spatialResource)">
                <dt>{{ $t('message.metadata.spatialResource') }}</dt>
                <dl v-for="(spatialResource, i) of catalog.spatialResource" :key="i">
                   <dd>
                     <app-link v-if="showString(spatialResource)" :to="spatialResource">
                    {{ spatialResource }}
                    </app-link>
                   </dd>
                </dl>
              </dl>
              <!-- LINKED DATA -->
              <dl>
                <div class="dropdown d-inline-block">
                <app-link class="text-dark p-0 font-weight-bold nav-link underline dropdown-toggle text-nowrap" fragment="#" role="button" id="linkedDataDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                     <span :title="$t('message.tooltip.catalogDetails.metadata')"
                          data-toggle="tooltip"
                          data-placement="right">
                          {{ $t('message.catalogs.downloadAsLinkedData') }}
                  </span>
                </app-link>
                <div class="dropdown-menu" aria-labelledby="linkedDataDropdownMenuLink">
                  <resourceDetailsLinkedDataButton class="dropdown-item" format="rdf" text="RDF/XML" resources="catalogues" v-bind:resources-id="catalog.id"></resourceDetailsLinkedDataButton>
                  <resourceDetailsLinkedDataButton class="dropdown-item" format="ttl" text="Turtle" resources="catalogues" v-bind:resources-id="catalog.id"></resourceDetailsLinkedDataButton>
                  <resourceDetailsLinkedDataButton class="dropdown-item" format="n3" text="Notation3" resources="catalogues" v-bind:resources-id="catalog.id"></resourceDetailsLinkedDataButton>
                  <resourceDetailsLinkedDataButton class="dropdown-item" format="nt" text="N-Triples" resources="catalogues" v-bind:resources-id="catalog.id"></resourceDetailsLinkedDataButton>
                  <resourceDetailsLinkedDataButton class="dropdown-item" format="jsonld" text="JSON-LD" resources="catalogues" v-bind:resources-id="catalog.id"></resourceDetailsLinkedDataButton>
                </div>
              </div>
              </dl>
            </div>
          </div>
        </div>
        <!-- Facet settings -->
        <div class="row facet-field mb-3" v-if="!showCatalogDetails">
          <facet
            :header="$t('message.datasetFacets.settings')"
            :items="[]"
            :toolTipTitle="$t('message.helpIcon.settings')"
            class="col pr-0"
          >
            <template #after>
              <div class="form-group list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                {{ $t('message.datasetFacets.operator') }}
                <span class="ml-2 d-flex flex-wrap">
                  <div class="custom-control custom-radio">
                    <input type="radio" id="radio-and" name="radio-facet-operator" class="custom-control-input" @click="changeFacetOperator(FACET_OPERATORS.and)" :checked="getFacetOperator === FACET_OPERATORS.and">
                    <label class="custom-control-label" for="radio-and">{{ $t('message.datasetFacets.and') }}</label>
                  </div>
                  <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="radio-or" name="radio-facet-operator" @click="changeFacetOperator(FACET_OPERATORS.or)" :checked="getFacetOperator === FACET_OPERATORS.or">
                    <label class="custom-control-label" for="radio-or">{{ $t('message.datasetFacets.or').toUpperCase() }}</label>
                  </div>
                </span>
              </div>
            </template>
          </facet>
        </div>
        <!-- Facets -->
        <div class="row facet-field mb-3"
          v-for="(field, index) in getSortedFacets"
          :key="`facet@${index}`"
          :class="{'mt-3': (index > 0)}"
        >
          <facet
            v-if="field.id === 'dataScope'"
            :header="$t('message.datasetFacets.facets.datascope')"
            :items="field.items"
            :minItems="MIN_FACET_LIMIT"
            :maxItems="MAX_FACET_LIMIT"
            :toolTipTitle="$t('message.helpIcon.dataScope')"
            class="col pr-0"
            v-slot="{ item: facet }"
          >
            <dataset-facets-item
              class="d-flex facet list-group-item list-group-item-action justify-content-between align-items-center"
              :title="$te(`message.datasetFacets.facets.datascopeField.${facet.id}`)
                ? $t(`message.datasetFacets.facets.datascopeField.${facet.id}`)
                : getFacetTranslationWrapper(field.id, facet.id, $route.query.locale, facet.title)"
              :count="getFacetCount(field, facet)"
              :hide-count="true"
              :class="{active: dataScopeFacetIsSelected(facet.id)}"
              @click.native="dataScopeFacetClicked(facet.id)"
            />
          </facet>
          <facet
              v-else-if="(field.id === 'dataServices')"
              :header="$t('message.metadata.dataServices')"
              :items="[]"
              :toolTipTitle="$t('message.helpIcon.dataServices')"
              class="col pr-0"
          >
            <template #after>
              <div class="form-group list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                {{ $t('message.datasetFacets.facets.dataServices.dataServicesOnly') }}
                <span class="ml-2 d-flex flex-wrap">
                  <div class="custom-control custom-radio">
                    <input type="radio" id="radio-yes" name="radio-facet-data-services" class="custom-control-input" @click="changeDataServices('true')" :checked="getDataServices === 'true'">
                    <label class="custom-control-label" for="radio-yes">{{ $t('message.metadata.yes') }}</label>
                  </div>
                  <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="radio-no" name="radio-facet-data-services" @click="changeDataServices('false')" :checked="getDataServices === 'false'">
                    <label class="custom-control-label" for="radio-no">{{ $t('message.metadata.no') }}</label>
                  </div>
                </span>
              </div>
            </template>
          </facet>
          <facet
            v-else
            :header="field.id === 'scoring'
              ? $t('message.header.navigation.data.metadataquality')
              : $t(`message.datasetFacets.facets.${field.id.toLowerCase()}`)"
            :items="sortByCount(field.items, field.id)"
            :minItems="MIN_FACET_LIMIT"
            :maxItems="MAX_FACET_LIMIT"
            :toolTipTitle="$t(`message.helpIcon.${field.id.toLowerCase()}`)"
            class="col pr-0"
            v-slot="{ item: facet }"
          >
            <dataset-facets-item
              class="d-flex facet list-group-item list-group-item-action justify-content-between align-items-center"
              :title="getFacetTranslationWrapper(field.id, facet.id, $route.query.locale, facet.title)"
              :count="getFacetCount(field, facet)"
              :hide-count="field.id === 'dataScope'"
              :class="{active: field.id === 'scoring' ? scoringFacetIsSelected(facet.minScoring) : facetIsSelected(field.id, facet.id)}"
              @click.native="field.id === 'scoring' ? scoringFacetClicked(facet.minScoring): facetClicked(field.id, facet.id)"
            />
          </facet>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import {
  isObject,
  isArray,
  isString,
  has,
  isNil,
  isNumber,
} from 'lodash';
import Facet from './Facet';
import DatasetFacetsItem from './DatasetFacetsItem';
import MapBoundsSender from './MapBoundsSender';
import { AppLink, Tooltip, helpers, ResourceDetailsLinkedDataButton, dateFilters } from '@piveau/piveau-hub-ui-modules';
const { getTranslationFor, getImg, getFacetTranslation } = helpers;

const MapBoundsReceiver = () => import(/* webpackChunkName: "MapBoundsReceiver" */'./MapBoundsReceiver');

export default {
  name: 'datasetFacets',
  dependencies: ['GazetteerService', 'catalogService'],
  components: {
    AppLink,
    Facet,
    DatasetFacetsItem,
    MapBoundsSender,
    MapBoundsReceiver,
    resourceDetailsLinkedDataButton: ResourceDetailsLinkedDataButton,
    Tooltip,
  },
  TooltipmetaInfo() {
    const catalogTitle = this.getTranslationFor(this.catalog.title, this.$route.query.locale, this.catalogLanguageIds) || this.catalog.id;
    const catalogDescription = this.getTranslationFor(this.catalog.description, this.$route.query.locale, this.catalogLanguageIds) || this.catalog.id;
    const title = this.currentSearchQuery
      ? `${this.currentSearchQuery}${this.showCatalogDetails ? ` - ${catalogTitle}` : ''}`
      : `${this.showCatalogDetails ? catalogTitle : this.$t('message.header.navigation.data.datasets')}`;
    return {
      title,
      meta: [
        { name: 'description', vmid: 'description', content: this.showCatalogDetails ? catalogDescription : `${this.$t('message.header.navigation.data.datasets')} - data.europa.eu` },
        { name: 'keywords', vmid: 'keywords', content: this.showCatalogDetails ? `${this.$env.keywords} ${this.$t('message.header.navigation.data.catalogs')}` : `${this.$env.keywords} ${this.$t('message.header.navigation.data.datasets')}` },
      ],
    };
  },
  data() {
    return {
      gazetteer: {
        searchbarText: '',
        suggestions: [],
        selected: false,
        bounds: [],
      },
      map: {
        sender: {
          startBounds: this.$env.maps.sender.startBounds,
          height: this.$env.maps.sender.height,
          width: this.$env.maps.sender.width,
          mapContainerId: this.$env.maps.sender.mapContainerId,
        },
        receiver: {
          startBounds: this.$env.maps.receiver.startBounds,
          height: this.$env.maps.receiver.height,
          width: this.$env.maps.receiver.width,
          mapContainerId: this.$env.maps.receiver.mapContainerId,
        },
        geoBoundsId: this.$env.maps.geoBoundsId,
      },
      defaultFacetOrder: this.$env.datasets.facets.defaultFacetOrder,
      useDatasetFacetsMap: this.$env.datasets.facets.useDatasetFacetsMap,
      useScoringFacets: this.$env.datasets.facets.scoringFacets.useScoringFacets,
      useDataScopeFacets: this.$route.query.catalog.length === 0,
      showCatalogDetails: false,
      catalog: {},
      browser: {
        /* eslint-disable-next-line */
        isIE: /*@cc_on!@*/false || !!document.documentMode,
      },
      MIN_FACET_LIMIT: this.$env.datasets.facets.MIN_FACET_LIMIT,
      MAX_FACET_LIMIT: this.$env.datasets.facets.MAX_FACET_LIMIT,
      FACET_OPERATORS: this.$env.datasets.facets.FACET_OPERATORS,
      FACET_GROUP_OPERATORS: this.$env.datasets.facets.FACET_GROUP_OPERATORS,
    };
  },
  props: {
    dataScope: {
      type: String,
      default: null,
    },
  },
  computed: {
    ...mapGetters('catalogDetails', [
      'getCatalog',
    ]),
    ...mapGetters('datasets', [
      'getAllAvailableFacets',
      'getDatasetsCount',
      'getFacetOperator',
      'getFacetGroupOperator',
      'getDataServices',
      'getLimit',
      'getMinScoring',
      'getPage',
      'getDatasetGeoBounds',
      'getScoringFacets',
    ]),
    ...mapGetters('geo', [
      'getGeoBoundsById',
      'getHoldedGeoBoundsById',
    ]),
    ...mapGetters('gazetteer', [
      'getSuggestions',
    ]),
    geoStateBoundsWatcher() {
      return this.getGeoBoundsById(this.map.geoBoundsId);
    },
    datasetBoundsWatcher() {
      return this.getDatasetGeoBounds;
    },
    facetOperatorWatcher() {
      return this.getFacetOperator;
    },
    facetGroupOperatorWatcher() {
      return this.getFacetGroupOperator;
    },
    dataServicesWatcher() {
      return this.getDataServices;
    },
    catalogWatcher() {
      return this.getCatalog;
    },
    showCatalogDetailsWatcher() {
      return this.$route.query.showcatalogdetails;
    },
    useCatalogFacets() {
      return !this.showCatalogDetails;
    },
    currentSearchQuery() {
      return this.$route.query.query;
    },
    getSortedFacets() {
      const availableFacets = this.getAllAvailableFacets;
      const sortedFacets = [];

      this.defaultFacetOrder.forEach((facet) => {
        availableFacets.forEach((field) => {
          if (facet === field.id
              && field.items.length > 0
              && (field.id !== 'country' || this.dataScope)
              && (field.id !== 'catalog' || this.useCatalogFacets)
              && (field.id !== 'scoring' || this.useScoringFacets)
              && (field.id !== 'dataScope' || this.useDataScopeFacets)) sortedFacets.push(field);
        });
      });

      return sortedFacets;
    },
    // Returns the current catalog's available language ids
    // example: ['en', 'de', 'sv']
    catalogLanguageIds() {
      const languages = this.getCatalog && this.getCatalog.languages;
      if (!isArray(languages)) return [];
      return languages
        .map(lang => lang && lang.id)
        .filter(lang => lang);
    },
  },
  methods: {
    isObject,
    isArray,
    isString,
    has,
    isNil,
    isNumber,
    getImg,
    getTranslationFor,
    getFacetTranslation,
    ...mapActions('catalogDetails', [
      'loadCatalog',
      'useCatalogService',
    ]),
    ...mapActions('datasets', [
      'toggleFacet',
      'addFacet',
      'removeFacet',
      'setFacetOperator',
      'setFacetGroupOperator',
      'setDataServices',
      'setPage',
      'setPageCount',
      'setDatasetGeoBounds',
      'setMinScoring',
    ]),
    ...mapActions('geo', [
      'setGeoBoundsForId',
      'resetGeoBoundsForId',
      'resetHoldedGeoBoundsForId',
    ]),
    ...mapActions('gazetteer', [
      'autocomplete',
      'useService',
    ]),
    getFacetTranslationWrapper(fieldId, facetId, userLocale, fallback) {
      return fieldId === 'scoring'
        ? `${this.$t(`message.datasetFacets.facets.scoring.${facetId}`)}${facetId === 'sufficientScoring' || facetId === 'goodScoring' ? '+' : ''}`
        : this.getFacetTranslation(fieldId, facetId, userLocale, fallback);
    },
    sortByCount(facets, fieldId) {
      if (fieldId === 'scoring') return facets;
      return facets.slice().sort((a, b) => {
        const n = b.count - a.count;
        if (n !== 0) return b.count - a.count;
        if (a.name < b.name) return -1;
        return 1;
      });
    },
    facetIsSelected(field, facet) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, field)) {
        return false;
      }
      let qField = this.$route.query[field];
      if (!Array.isArray(qField)) qField = [qField];
      if (field === 'categories') {
        // Ignore Case for categories
        facet.toUpperCase();
        qField = qField.map(f => f.toUpperCase());
      }
      return qField.indexOf(facet) > -1;
    },
    facetClicked(field, facet) {
      this.toggleFacet(field, facet);
      this.resetPage();
    },
    toggleFacet(field, facet) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, [field])) {
        return this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: [], page: 1 }) }).catch(() => {});
      }
      let facets = this.$route.query[field].slice();
      if (!Array.isArray(facets)) facets = [facets];
      if (field === 'categories') {
        // Ignore Case for categories
        facet.toUpperCase();
        facets = facets.map(f => f.toUpperCase());
      }
      const index = facets.indexOf(facet);
      if (index > -1) facets.splice(index, 1);
      else facets.push(facet);
      return this.$router.push({ query: Object.assign({}, this.$route.query, { [field]: facets, page: 1 }) }).catch(() => {});
    },
    scoringFacetIsSelected(minScoring) {
      const qMinScoring = parseInt(this.getMinScoring, 10);
      return qMinScoring === minScoring;
    },
    scoringFacetClicked(minScoring) {
      this.setMinScoring(minScoring);
      localStorage.setItem('minScoring', JSON.stringify(minScoring));
      this.resetPage();
      window.scrollTo(0, 0);
    },
    dataScopeFacetIsSelected(dataScope) {
      if (!Object.prototype.hasOwnProperty.call(this.$route.query, 'dataScope')) return false;
      return this.$route.query.dataScope === dataScope;
    },
    dataScopeFacetClicked(dataScope) {
      if (this.dataScopeFacetIsSelected(dataScope)) {
        this.$router.push({ query: Object.assign({}, this.$route.query, { dataScope: [], country: [], page: 1 }) }).catch(() => {});
      } else {
        const country = [];
        if (dataScope !== 'countryData') country.push(dataScope);
        this.$router.push({ query: Object.assign({}, this.$route.query, { dataScope, country, page: 1 }) }).catch(() => {});
      }
    },
    changeFacetOperator(op) {
      this.setFacetOperator(op);
      this.setFacetGroupOperator(op);
    },
    toggleFacetGroupOperator() {
      let op = this.getFacetGroupOperator;
      op = op === this.FACET_GROUP_OPERATORS.and ? this.FACET_GROUP_OPERATORS.or : this.FACET_GROUP_OPERATORS.and;
      this.setFacetGroupOperator(op);
    },
    changeDataServices(ds) {
      this.setDataServices(ds);
    },
    resetPage() {
      this.$router.replace({ query: Object.assign({}, this.$route.query, { page: 1 }) }).catch(() => {});
    },
    applyHoldedBounds() {
      const holdedBounds = this.getHoldedGeoBoundsById(this.map.geoBoundsId);
      this.setGeoBoundsForId({
        bounds: holdedBounds,
        boundsId: this.map.geoBoundsId,
      });
    },
    resetBoundsFor(boundsId) {
      this.$refs.mapSender.resetBounds();
      this.$refs.mapReceiver.resetBounds();
      this.resetGeoBoundsForId(boundsId);
      this.resetHoldedGeoBoundsForId(boundsId);
    },
    getAutocompleteSuggestions(query) {
      if (!query || isNil(query)) this.clearAutocompleteSuggestions();
      else {
        this.autocomplete(query).then(() => {
          this.$nextTick(() => {
            this.gazetteer.suggestions = this.getSuggestions;
          });
        });
      }
    },
    clearAutocompleteSuggestions() {
      this.gazetteer.suggestions = [];
    },
    handleSuggestionSelection(suggestion) {
      this.gazetteer.searchbarText = suggestion.name;
      this.gazetteer.selected = true;
      const location = suggestion.geometry.split(',');
      this.gazetteer.bounds = [[location[1], location[0]], [location[3], location[2]]]
        .map(point => point.map(coord => parseFloat(coord)));
      this.setGeoBoundsForId({
        bounds: this.gazetteer.bounds,
        boundsId: this.map.geoBoundsId,
      });
    },
    triggerResize() {
      if (this.browser.isIE) {
        // Note: Trigger resize after 500ms (IE11 needs longer than modern browsers) in IE11 when Modal element is visible to properly display the map component
        setTimeout(() => {
          const evt = document.createEvent('UIEvents');
          evt.initUIEvent('resize', true, false, window, 0);
          window.dispatchEvent(evt);
        }, 500);
      } else {
        // Note: Trigger resize after 200ms when Modal element is visible to properly display the map component
        setTimeout(() => {
          window.dispatchEvent(new Event('resize'));
        }, 200);
      }
    },
    initShowCatalogDetails() {
      const showCatalogDetails = this.$route.query.showcatalogdetails;
      if (showCatalogDetails === 'true') {
        this.showCatalogDetails = true;
        this.loadCatalog(this.$route.query.catalog);
      } else this.showCatalogDetails = false;
    },
    filterDateFormatEU(date) {
      return dateFilters.formatEU(date);
    },
    getCatalogImage(catalog) {
      return this.$env.catalogs.useCatalogCountries
        ? `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'country.id') ? catalog.country.id : this.$env.catalogs.defaultCatalogCountryID}`
        : `${this.$env.catalogs.defaultCatalogImagePath}/${has(catalog, 'id') ? catalog.id : this.$env.catalogs.defaultCatalogID}`;
    },
    getFacetCount(field, facet) {
      if (field.id === 'scoring') return '';
      return facet.count;
    },
    /* ABSTRACT SHOW FUNCTIONS */
    showString(string) {
      return !isNil(string) && isString(string);
    },
    showObject(object) {
      return !isNil(object) && isObject(object) && !Object.values(object).reduce((keyUndefined, currentValue) => keyUndefined && currentValue === undefined, true);
    },
    showArray(array) {
      return !isNil(array) && isArray(array) && array.length > 0;
    },
    showObjectArray(objectArray) {
      return this.showArray(objectArray) && !objectArray.reduce((objectUndefined, currentObject) => objectUndefined && Object.values(currentObject).reduce((keyUndefined, currentValue) => keyUndefined && currentValue === undefined, true), true);
    },
    showArrayOfStrings(stringArray) {
      return this.showArray(stringArray) && stringArray.every(currentString => this.showString(currentString));
    },
  },
  watch: {
    geoStateBoundsWatcher: {
      deep: true,
      handler(bounds) {
        this.setDatasetGeoBounds(bounds);
      },
    },
    datasetBoundsWatcher: {
      deep: true,
      handler() {
      },
    },
    facetOperatorWatcher: {
      handler(facetOperator) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { facetOperator }) }).catch(() => {});
      },
    },
    facetGroupOperatorWatcher: {
      handler(facetGroupOperator) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { facetGroupOperator }) }).catch(() => {});
      },
    },
    dataServicesWatcher: {
      handler(dataServices) {
        this.$router.replace({ query: Object.assign({}, this.$route.query, { dataServices }) }).catch(() => {});
      },
    },
    showCatalogDetailsWatcher: {
      handler(showCatalogDetails) {
        this.showCatalogDetails = showCatalogDetails;
      },
    },
    catalogWatcher: {
      handler(catalog) {
        this.catalog = catalog;
      },
    },
  },
  created() {
    this.useService(this.GazetteerService);
    this.useCatalogService(this.catalogService);
    this.initShowCatalogDetails();
  },
  mounted() {},
  destroyed() {},
};
</script>

<style lang="scss" scoped>

  .search-button {
    &:hover {
      background-color: #196fd2;
      border-color: #196fd2
    }
  }

  .facet:hover {
    cursor: pointer;
  }

  .custom-control {
    padding-left: 1.5rem;
    margin-right: 1rem;
  }
  .custom-control-label {
    &::before {
      left: -1.5rem !important;
    }
    &::after {
      left: -1.5rem !important;
    }
  }
  .custom-control-input:checked ~ .custom-control-label::before {
    border-color: var(--primary);
    background-color: var(--primary);
  }
  .map {
    z-index: 0;
  }
  .suggestion-input-group {
    position: relative;
  }
  .suggestion-input {
    // Position absolute is causing the input box to be invisible
    // position: absolute;
    position: relative;
    top: 0;
    height: 100%;
  }
  .suggestion-list-group {
    position: relative;
    width: 100%;
  }
  .suggestion-list {
    width: 100%;
    position: absolute;
    top: 0;
    z-index: 100;
  }

  .map-modal-button {
    position: absolute;
    bottom: 0;
    right: 0;
  }

  #modal-map-wrapper .modal-dialog {
    max-width: 100%;
  }

  .modal-content {
    min-height: 100%;
    min-height: 100vh;
  }

  #modalMap {
    display: flex;
    flex: 1 1;
  }

  .focus-border {
    transition: box-shadow 200ms ease;

    &:focus {
      box-shadow: 0px 0px 7px #202020;
    }
  }

  .active {
    background-color: var(--primary);
    border-color: var(--primary);
  }

  @media (min-width: 768px) {
    .modal-content {
      min-height: auto!important;
    }
    #modalMap {
      height: 500px;
    }
  }

  .reset-bounds-button {
    position: absolute;
    bottom: 0;
    left: 0;
  }


  .io {
    border: 0 !important;
    margin-bottom: 3px;
    opacity: 0.8;
  }
  .dropdown-menu.show{
    transform: translate3d(0px, 15px, 0px) !important;
    width: max-content;
    display: block;

  }

  /*** MATERIAL ICONS ***/
  .material-icons.small-icon {
    font-size: 20px;
  }
</style>
