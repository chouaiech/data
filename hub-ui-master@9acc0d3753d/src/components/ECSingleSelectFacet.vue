<template>
  <div>
    <e-c-facet-header
      v-if="header"
      :title="header"
      :tooltip="toolTipTitle"
    />
    <div class="ecl-select__container ecl-select__container--m w-100">
      <select class="ecl-select" id="select-default" required v-model="selected">
        <option
          v-for="(item, index) in items"
          :value="item"
          :key="getTitle(item)+index">
          {{ getTitle(item) }}
        </option>
      </select>
      <div class="ecl-select__icon">
        <svg class="ecl-icon ecl-icon--s ecl-icon--rotate-180 ecl-select__icon-shape" focusable="false" aria-hidden="true">
          <use xlink:href="../assets/img/ecl/icons.svg#corner-arrow"></use>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import ECFacetHeader from "@/components/ECFacetHeader";
import Vue from "vue";
import { helpers } from '@piveau/piveau-hub-ui-modules';
const { getFacetTranslation } = helpers;
export default {
  name: "ECSingleSelectFacet",
  components: {ECFacetHeader},
  props: {
    header: {
      type: String,
      default: '',
    },
    fieldId: {
      type: String,
      default: '',
    },
    items: {
      type: Array,
      required: true,
    },
    toolTipTitle: {
      type: String,
      default: '',
    },
    showFilter: String,
    facetIsSelected: Function,
    facetClicked: Function
  },
  data() {
    return {
      selected: this.items.find(item => this.facetIsSelected(this.fieldId, item))
    }
  },
  methods: {
    getFacetTranslation,
    getTitle(item) {
      return Vue.i18n.te(`message.datasetFacets.facets.datascopeField.${item.id}`) ?
        Vue.i18n.t(`message.datasetFacets.facets.datascopeField.${item.id}`)
        : this.getFacetTranslationWrapper(this.fieldId, item.id, this.$route.query.locale, item.title);
    },
    getFacetTranslationWrapper(fieldId, facetId, userLocale, fallback) {
      return fieldId === 'scoring'
        ? Vue.i18n.t(`message.datasetFacets.facets.scoring.${facetId}`)
        : this.getFacetTranslation(fieldId, facetId, userLocale, fallback);
    },
  },
  watch: {
    items(newItems) {
      this.selected = newItems.find(item => this.facetIsSelected(this.fieldId, item));
    },
    selected() {
      this.facetClicked(this.fieldId, this.selected);
    }
  }
}
</script>

<style scoped>

</style>
