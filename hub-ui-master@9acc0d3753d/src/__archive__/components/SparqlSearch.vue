<template>
  <div class="container-fluid">
    <div>
      <h1 class="text-primary">{{ $t('message.header.navigation.data.sparqlsearch') }}</h1>
      <hr>
      <p v-html="$t('message.sparql.subHeadlineText1')"></p>
      <p v-html="$t('message.sparql.subHeadlineText2', { sample_sparql_queries })"></p>
    </div>
    <div id="yasgui"></div>
  </div>
</template>

<script>

import Yasqe from '@triply/yasqe';
import Yasgui from '@triply/yasgui';
import Yasr from '@triply/yasr';

export default {
  data() {
    return {
      yasqe: null,
      yasr: null,
      yasgui: null,
      sample_sparql_queries: `/${this.$root.$i18n.locale}/about/sparql`,
    };
  },
  created() {
    this.$nextTick(() => {
      Yasgui.Yasqe.defaults.value = `PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX odp:  <http://data.europa.eu/euodp/ontologies/ec-odp#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>

SELECT * WHERE { ?d a dcat:Dataset } LIMIT 10`;
      const vm = this;
      vm.yasgui = new Yasgui(
        document.getElementById('yasgui'),
        {
          requestConfig: {
            endpoint: this.$env.api.sparqlUrl,
            method: 'POST',
          },
          copyEndpointOnNewTab: false,
          endpointCatalogueOptions: {
            /**
             catalogue list should be extended properly in case of multiple endpoints.
             */
            getData: () => [
              {
                endpoint: 'https://data.europa.eu/sparql',
              },
            ],
          },
          tabName: 'Query',
        },
      );

      vm.yasr = new Yasr(
        document.getElementById('yasr'),
        {
          /**
           obviously we don't want to enable a persistancy. in case of need set this value accordingly.
           */
          persistenceId: 'null',
        },
      );
      vm.yasqe = new Yasqe(
        document.getElementById('yasqe'),
        {
          showQueryButton: true,
          resizeable: true,
        },
      );
    });
  },
};
</script>

<style lang="scss">
@import '~@triply/yasqe/build/yasqe.min.css';
@import '~@triply/yasr/build/yasr.min.css';
@import '~@triply/yasgui/build/yasgui.min.css';

.yasqe {
  margin-top: 7px;
}
</style>
