<template>
 
  <div class="dsd-item d-flex flex-column">
    <h2 :title="$t('message.tooltip.datasetDetails.distribution')"
        class="mb-lg-4"
        data-toggle="tooltip"
        data-placement="top"
        data-cy="dataset-distributions"
    >
      {{ $t('message.metadata.distributions') }} ({{ getDistributions ? getDistributions.length.toLocaleString('fi') : 0 }})
    </h2>
    <div class="d-none d-md-flex distributions-list-top">
      <div id="description"><div><span>Link to the data</span></div></div>
      <div id="format"><div><span>Format</span></div></div>
      <div id="added" class="d-none d-md-block"><div><span>Distribution added</span></div></div>
      <div id="actions"><div><span>Actions</span></div></div>
    </div>
    <SimpleFormAccess></SimpleFormAccess> 
  </div>
</template>

<script>
import {mapGetters} from "vuex";

import SimpleFormAccess from "./SimpleFormAccessRequestDistribution.vue";

export default {
  name: "ECDistributionsHeader",
  components: {
    SimpleFormAccess
  },
  computed: {
    ...mapGetters('datasetDetails', [
      'getDistributions'
    ])
  },

   mounted() {
    
  }
}
</script>

<style lang="scss" scoped>
.distributions-list-top {
  background-color: #f2f4f8;
  > div {
    font-weight: bold;
    > div {
      padding: 1rem;
      > span {
        white-space: nowrap;
      }
    }
  }
}

#description {
  flex: 12 0;
}

#format {
  flex: 2 0;
  display: flex;
  justify-content: center;

}

#added {
  flex: 0 0 160px;
  > div {
    padding-left: 0;
    padding-right: 0;
    display: flex;
    justify-content: center;
  }
}

#actions {
  flex: 0 0 200px;
}

@media (min-width: 992px) { // lg
  #actions {
    flex: 0 0 350px;
    padding-left: 0.5rem !important;
  }
}

</style>
