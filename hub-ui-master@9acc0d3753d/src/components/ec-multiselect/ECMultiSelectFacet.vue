<template>
  <div class="facet-container user-select-none">
    <e-c-facet-header
      v-if="header"
      :title="header"
      :tooltip="toolTipTitle"
    />
    <div>
      <e-c-select-display
        :text="selection"
        placeholder="Select"
        :showOptions="showOptions"
        :away="away"
      />
      <div v-if="open" v-on-clickaway="away" class="dropdown">
        <input v-if="displayFilterInputBox" type="text" class="ecl-text-input col" placeholder="Filter" v-model="filter"/>
        <div class="dropdown-options">
          <div v-for="(item, index) in filteredItems" :key="getTitle(item)+index" class="select-item">
            <e-c-checkbox
              :id="`${fieldId}_${itemTitles[index]}`"
              :label="itemTitles[index]"
              :label-right="item.count.toLocaleString('fi')"
              :checked="facetIsSelected(fieldId, item)"
              :onClick="() => facetClicked(fieldId, item)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import { helpers } from '@piveau/piveau-hub-ui-modules';
const { getFacetTranslation } = helpers;
import { mixin as clickaway } from 'vue-clickaway';
import ECCheckbox from "@/components/ECCheckbox";
import ECFacetHeader from "@/components/ECFacetHeader";
import ECSelectDisplay from "@/components/ec-multiselect/ECSelectDisplay";

export default {
  name: "ECMultiSelectFacet",
  components: {ECSelectDisplay, ECFacetHeader, ECCheckbox},
  mixins: [clickaway],
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
      open: false,
      id: null,
      filter: "",
      filterTimeout: null,
    };
  },
  computed: {
    myId() {
      // Use Vue generated uid to set give each facet a unique id
      return `facet-${this.id}`;
    },
    itemTitles() {
      return this.filteredItems.map(this.getTitle);
    },
    selection() {
      const selectedItems = this.items.filter(item => this.facetIsSelected(this.fieldId, item));
      return selectedItems.map(item => this.getTitle(item)).join(", ");
    },
    displayFilterInputBox() {
      const show = (this.showFilter || "").toLowerCase();
      if (show === "never") return false;
      const count = this.items.length;
      return show === "always" || count > 10;
    },
    filteredItems() {
      const lcFilter = this.filter.toLowerCase();
      return this.items.filter(item => this.getTitle(item).toLowerCase().includes(lcFilter));
    }
  },
  methods: {
    getFacetTranslation,
    getTitle(item) {
      return this.fieldId === 'dataScope' && Vue.i18n.te(`message.datasetFacets.facets.datascopeField.${item.id}`) ?
        Vue.i18n.t(`message.datasetFacets.facets.datascopeField.${item.id}`)
        : this.getFacetTranslationWrapper(this.fieldId, item.id, this.$route.query.locale, item.title);
    },
    getFacetTranslationWrapper(fieldId, facetId, userLocale, fallback) {
      return fieldId === 'scoring'
        ? Vue.i18n.t(`message.datasetFacets.facets.scoring.${facetId}`)
        : this.getFacetTranslation(fieldId, facetId, userLocale, fallback);
    },
    away() {
      this.open = false;
    },
    showOptions() {
      this.open = true;
    }
  },
  mounted() {
    this.id = this._uid; // eslint-disable-line
  },
  beforeDestroy() {
    if (this.filterTimeout) {
      clearTimeout(this.filterTimeout);
    }
  }
}
</script>

<style lang="scss" scoped>

.select-item {
  padding: 16px;
  &:hover {
    background-color: #e3e3e3;
  }
}

.highlighted-row {
  background-color: #e3e3e3;
}

.facet-container {
  padding: 0;
  font-family: Arial, sans-serif;
}

.dropdown {
  width: 100%;
  background: #f5f5f5;
  border: 1px solid #e3e3e3;
}

.dropdown-options {
  width: 100%;
  max-height: 300px;
  overflow: auto;
}

</style>
