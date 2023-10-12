<template>
  <div class="facet-container">
    <div class="mb-2 font-weight-bold facet-header"><span class="font-weight-bold">{{ header }}</span></div>
<!--    <select class="list-group-item col mb-1">-->
<!--      <option v-for="(item, index) in items.slice(0, numItemsAllowed)" :key="`field@${index}`">-->
<!--        <div style="display:flex; flex-direction: row; justify-content: space-between">-->
<!--                  <span>-->
<!--          {{itemTitles[index]}}-->
<!--        </span>-->
<!--          <span style="position:absolute; right: 80px;background: coral">-->
<!--          ({{item.count}})-->
<!--        </span>-->
<!--        </div>-->
<!--      </option>-->
<!--    </select>-->
    <div class="user-select-none">
      <div @click="open=true" class="value-display list-group-item col w-100 d-flex flex-row justify-content-between p-0 align-items-center"
              type="button"
              aria-haspopup="true"
              aria-expanded="false">
        <span data-toggle="tooltip"
              data-placement="center"
              class="ml-2">
              {{ header }}
        </span>
        <div class="dropdown-icon"></div>
<!--        <i class="material-icons small-icon float-right align-bottom">keyboard_arrow_down</i>-->
      </div>
      <div v-if="open" v-on-clickaway="away" class="dropdown w-100">
        <div v-for="(item, index) in items.slice(0, numItemsAllowed)" :key="`field@${index}`">
          <div style="display:flex; flex-direction: row; justify-content: space-between">
                    <div class="text-truncate">
            {{itemTitles[index]}}
          </div>
          <div>
            {{item.count}}
          </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from 'vue';
import {getFacetTranslation} from "@/modules/utils/helpers";
import { mixin as clickaway } from 'vue-clickaway';
import Dropdown from "@/modules/widgets/Dropdown";

export default {
  components: {Dropdown},
  mixins: [clickaway],
  name: 'Facet',
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
    }
  },
  data() {
    return {
      open: false,
      id: null,
      isExpanded: false,
      isGrown: false,
      numItemsAllowed: this.minItems,
    };
  },
  computed: {
    myId() {
      // Use Vue generated uid to set give each facet a unique id
      return `facet-${this.id}`;
    },
    itemTitles() {

      // const maxLength = this.items.reduce((curr, item) => Math.max(curr, item.title.length));
      return this.items.map(item => {
        const title = this.getTitle(item);
        const length = title.length;
        if (length < 20) {
          // return title;
        } else {
          // return title.substring(0, 17) + "...";
        }
        return title;
      });
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
    away() {
      this.open = false;
    }
  },
  mounted() {
    this.id = this._uid; // eslint-disable-line
  },
};
</script>

<style lang="scss" scoped>

select {
  border-radius: 0;
  border-color: #2c2c2c;
  appearance: none;
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 720 720" preserveAspectRatio="xMinYMin meet" xmlns:xlink="http://www.w3.org/1999/xlink" id="graphic-display_svg"><rect x="0" y="0" width="720" height="720" style="fill:rgb(44,44,44);"/><g transform="matrix(1 0 0 1 0 0)"><path d="M220,300L360,420L500,300" style="stroke:rgb(255,255,255);fill:none;stroke-width:50;stroke-linecap:round;stroke-linejoin:round"/></g></svg>') no-repeat right;
}

.dropdown-icon {
  height: 48px;
  margin: 0;
  padding: 0;
  width: 48px;
  background: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 720 720" preserveAspectRatio="xMinYMin meet" xmlns:xlink="http://www.w3.org/1999/xlink" id="graphic-display_svg"><rect x="0" y="0" width="720" height="720" style="fill:rgb(44,44,44);"/><g transform="matrix(1 0 0 1 0 0)"><path d="M220,300L360,420L500,300" style="stroke:rgb(255,255,255);fill:none;stroke-width:50;stroke-linecap:round;stroke-linejoin:round"/></g></svg>') no-repeat right;
}

.facet-container {
  padding: 0;
  font-family: Arial, sans-serif;
}

.facet-header > span {
  font-size: medium;
  color: #2c2c2c;
}

.tooltip-icon {
  font-size: 15px;
}


.btn-color {
  background-color: var(--primary);
  border-color: var(--primary);

  &:hover {
    background-color: #196fd2;
    border-color: #196fd2;
  }
}

.dropdown {
  background:#f8f9fa;
  padding: 6px;
  border: 1px solid #ccc;
  max-height: 300px;
  overflow: auto;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 5px 15px;
}

.value-display {
  border-color: #2c2c2c;
}

</style>
