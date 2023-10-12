<template>
  <div
    :class="`formulate-input-element formulate-input-element--${context.type}`"
    id="datasetID"
    ref="datasetID"
    :data-type="context.type"
    v-on="$listeners"
  >
    <div v-if="getIsEditMode">
      <FormulateInput
        v-model="uniqueID"
        type="text"
        :disabled="true">
      </FormulateInput>
    </div>
    <div v-else>
      <FormulateInput
        v-model="uniqueID"
        @input="checkUniqueID()"
        id="datasetIDForm"
        type="text"
        :label="context.label"
        :name="context.attributes.name"
        :placeholder="$t('message.dataupload.createUniqueID')"
        :validation="validation"
        :validation-rules="validationRules"
        :validation-messages="validationMessages">
      </FormulateInput>
      <FormulateInput
        v-model="uniqueIDHidden"
        id="datasetIDFormHidden"
        type="hidden"
        :label="context.label"
        :validation="validationHidden">
      </FormulateInput>
    </div>
  </div>
</template>

<script>
/* eslint-disable,arrow-parens,no-param-reassign */
import axios from 'axios';
import { mapGetters } from 'vuex';
import { isNil } from 'lodash';

export default {
  props: {
    context: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isUniqueID: true,
      uniqueID: '',
      uniqueIDHidden: '',
      validation: 'optional|validateID',
      validationHidden: 'required',
      validationRules: {
        validateID: () => /^[a-z0-9-]*$/.test(this.uniqueID),
      },
      validationMessages: {
        validateID: 'Dataset ID must only contain lower case letters, numbers and dashes (-). Please choose a different ID.',
      },
    };
  },
  computed: {
    ...mapGetters('auth', [
      'getIsEditMode',
    ]),
  },
  methods: {
    populateID() {
      // Populate ID field if existing (EDIT)
      if (this.context.model) this.uniqueID = this.context.model;
    },
    checkUniqueID() {
      return new Promise(() => {
        if (isNil(this.uniqueID) || this.uniqueID === '' || this.uniqueID === undefined) this.isUniqueID = true;
        else {
          const request = `${this.$env.api.hubUrl}datasets/${this.uniqueID}?useNormalizedId=true`;
          axios.head(request)
            .then(() => {
              this.isUniqueID = false;
            })
            .catch(() => {
              this.isUniqueID = true;
            });
        }
      });
    },
    handleDatasetIDError(newValue) {
      if (!newValue) {
        const datasetID = document.getElementById('datasetID').children[0];
        const text = document.createTextNode('This Dataset ID already exists. Please choose a different one.');
        const LI = document.createElement('LI');
        LI.setAttribute('role', 'status');
        LI.setAttribute('aria-live', 'polite');
        LI.setAttribute('class', 'formulate-input-error');
        LI.appendChild(text);
        const UL = document.createElement('UL');
        UL.setAttribute('class', 'formulate-input-errors');
        UL.setAttribute('id', 'datasetIDError');
        UL.appendChild(LI);
        datasetID.appendChild(UL);
      } else document.getElementById('datasetIDError').remove();
    },
  },
  beforeMount() {
    this.populateID();
  },
  mounted() {
    this.checkUniqueID();
  },
  watch: {
    context: {
      handler() {
        this.populateID();
      },
    },
    uniqueID: {
      handler(newValue) {
        this.uniqueIDHidden = this.validationRules.validateID()
          ? newValue
          : '';
      },
    },
    isUniqueID: {
      handler(newValue) {
        if (this.getIsEditMode) return;
        this.handleDatasetIDError(newValue);
      },
    },
  },
};
</script>

<style>
#datasetID.formulate-input-element--unique-identifier-input label {
  display: none !important;
}

#datasetID.formulate-input-element--unique-identifier-input .formulate-input {
  margin-bottom: 0.1em;
}
</style>
