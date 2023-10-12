<template>
  <div class="flex-grow-1 d-flex distribution-details">
    <div class="col-sm-12 col-md-9 col-lg-7">
      <h3 class="m-0">{{ getDistributionTitle(distribution) }}</h3>
      <distribution-description
        v-if="expanded"
        :distribution="distribution"
        :distributions="distributions"
        :distributionDescriptionIsExpanded="distributionDescriptionIsExpanded"
        :getDistributionDescription="getDistributionDescription"
        :toggleDistributionDescription="toggleDistributionDescription"
        :distributionDescriptionIsExpandable="distributionDescriptionIsExpandable"
      />
      <distribution-expanded-content
        v-if="expanded"
        :distribution="distribution"
        :distributionIsExpanded="distributionIsExpanded"
        :showLicensingAssistant="showLicensingAssistant"
        :showLicence="showLicence"
        :filterDateFormatEU="filterDateFormatEU"
        :showArray="showArray"
        :showNumber="showNumber"
        :showObject="showObject"
        :showObjectArray="showObjectArray"
        :appendCurrentLocaleToURL="appendCurrentLocaleToURL"
      />
      <e-c-more
        :label="expanded ? 'Show less' : 'Show more'"
        :upArrow="expanded"
        :action="() => toggleDistribution(distribution.id)"
        class="row ecl-button--secondary ecl-tabs__toggle text-primary mb-3 mt-2"
      />
    </div>
  </div>
</template>

<script>
import {
  DistributionDescription,
  DistributionExpandedContent
  
} from '@piveau/piveau-hub-ui-modules';

import ECMore from "@/components/ECMore";

export default {
  name: "ECDistributionDetails",
  components: {
    ECMore,
    DistributionExpandedContent,
    DistributionDescription
    
  },
  props: [
    "getDistributionTitle",
    "distribution",
    "distributions",
    "distributionDescriptionIsExpanded",
    "getDistributionDescription",
    "toggleDistributionDescription",
    "distributionDescriptionIsExpandable",
    "distributionIsExpanded",
    "showLicensingAssistant",
    "showLicence",
    "filterDateFormatEU",
    "showArray",
    "showNumber",
    "showObject",
    "showObjectArray",
    "appendCurrentLocaleToURL",
    "distributionCanShowMore",
    "toggleDistribution"
  ],
  computed: {
    expanded() {
      return this.distributionIsExpanded(this.distribution.id)
    }
  }
}
</script>

<style lang="scss" scoped>
.distribution-details {

}
</style>
