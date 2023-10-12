<template>
  <div :class="`formulate-input-element formulate-input-element--${context.type}`" :data-type="context.type" v-on="$listeners">
    <FormulateSlot name="prefix" :context="context">
      <component :is="context.slotComponents.prefix" v-if="context.slotComponents.prefix" :context="context"/>
    </FormulateSlot>

    <input type="text" v-model="context.model" @blur="context.blurHandler" hidden/>
    <FormulateForm v-model="conditionalValues" key="intern">
      <FormulateInput type="select" :options="context.options" :name="context.name" :label="$t('message.dataupload.type')" :placeholder="context.attributes.placeholder"></FormulateInput>
    </FormulateForm>

    <div v-if="conditionalValues === 'file'">
      <FormulateForm v-model="inputValues" v-if="conditionalValues" :schema="data[conditionalValues[context.name]]" @input="setContext"></FormulateForm>
    </div>
    <div v-else>
      <FormulateForm v-model="inputValues" v-if="conditionalValues" :schema="data[conditionalValues[context.name]]" @change="setContext"></FormulateForm>
    </div>

    <FormulateSlot name="suffix" :context="context">
      <component :is="context.slotComponents.suffix" v-if="context.slotComponents.suffix" :context="context"/>
    </FormulateSlot>
  </div>
</template>

<script>
export default {
  props: {
    context: {
      type: Object,
      required: true,
    },
    data: {},
  },
  data() {
    return {
      conditionalValues: {},
      inputValues: {},
    };
  },
  computed: {},
  methods: {
    /**
     * Saving changed values to context which will be given to parent form
     */
    setContext() {
      const dataKey = Object.keys(this.inputValues);
      if (dataKey.length > 0) {
        this.context.model = this.inputValues[dataKey[0]];
      }
      this.context.rootEmit('change');
    },
    fillValues() {
      const semanticName = this.context.attributes.name;
      if (semanticName === 'dct:issued' || semanticName === 'dct:modified') {
        //   // date time includes an 'T' to delimit date and time
        if (this.context.model.includes('T')) {
          this.conditionalValues[this.context.name] = 'datetime';
        } else {
          this.conditionalValues[this.context.name] = 'date';
        }
        this.inputValues = {'@value': this.context.model }; // string with special characters won't be added to empty object anymore
      } else if (semanticName === 'dct:license') {
        // either an array containing an object with multiple properties
        if (Array.isArray(this.context.model)) {
          this.conditionalValues[this.context.name] = 'man';
          this.inputValues = {'dct:license': this.context.model};
        } else { // or a single URI
          this.conditionalValues[this.context.name] = 'voc';
          this.inputValues = { '@id': this.context.model};
        }
      } else if (this.context.attributes.identifier === 'accessUrl') {
        this.conditionalValues[this.context.name] = 'url';
        this.inputValues = { '@id': this.context.model };
      } else if (semanticName === 'dct:spatial') {

        // both options return an URI
        this.conditionalValues[this.context.name] = 'man';
        this.inputValues = {'@id': this.context.model };

        // TODO: implement display of conditional choice when vocabulary was used

      }
    },
  },
  watch: {
    context: {
      handler() {
        if (this.context.model !== "") {
          this.fillValues();
        }
      }
    }
  }
};
</script>

<style>

</style>
