<template>
  <pv-data-info-box
    v-bind="$props"
    compact
  />
</template>

<script>
import Vue from 'vue';
import { PvDataInfoBox } from '@piveau/piveau-hub-ui-modules';

export default Vue.extend({
  name: 'ECDataInfoBox',
  props: ['dataset', 'to', 'descriptionMaxLength', 'src', 'catalogMode', 'compact'],
  components: {
    PvDataInfoBox,
  },
});
</script>
