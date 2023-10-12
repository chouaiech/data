<template>
  <div class="p-0">
    <e-c-multi-select-facet
      v-if="multiSelect"
      :header="header"
      :fieldId="fieldId"
      :items="items"
      :toolTipTitle="toolTipTitle"
      :facetIsSelected="facetIsSelected"
      :facetClicked="facetClicked"
    />
    <e-c-single-select-facet
      v-else
      :header="header"
      :fieldId="fieldId"
      :items="items"
      :toolTipTitle="toolTipTitle"
      :facetIsSelected="facetIsSelected"
      :facetClicked="facetClicked"
    />
  </div>
</template>

<script>
import ECSingleSelectFacet from "@/components/ECSingleSelectFacet";
import ECMultiSelectFacet from "@/components/ec-multiselect/ECMultiSelectFacet";
export default {
  name: "ECSelectFacet",
  components: {ECMultiSelectFacet, ECSingleSelectFacet},
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
    facetIsSelected: Function,
    facetClicked: Function,
    multiSelect: Boolean
  }
}
</script>
