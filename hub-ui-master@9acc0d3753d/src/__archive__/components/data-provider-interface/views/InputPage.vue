<template>
    <div class="form-container">
      <slot></slot>
      <div class="inputContainer" v-if="isInput">
        <div class="formContainer formulate">
          <FormulateForm name="form" v-model.lazy="formValues" :schema="getSchema" @failed-validation="showValidationFields" @submit="handleSubmit" 
          @change="saveToJsonld({property: property, values: formValues, distid: id})" 
          @repeatableRemoved="saveToJsonld({property: property, values: formValues, distid: id})">
            <FormulateInput type="submit" id="submit-form" class="display-none"></FormulateInput>
          </FormulateForm>
          <FormulateInput type="hidden" class="display-none"></FormulateInput>
        </div>
        <InfoBox class="infoContainer"></InfoBox>
      </div>
      <div v-if="isDistributionOverview">
        <DistributionOverview :distributionOverviewPage="isDistributionOverview"></DistributionOverview>
      </div>
      <ValidationModal :failedFields="failedFields"></ValidationModal>
      <!-- not the prettiest way but calling it within navigation component seems quiet complicated -->
      <app-confirmation-dialog id="modal" :confirm="modal.confirm" @confirm="modal.callback">
        {{ modal.message }}
      </app-confirmation-dialog>

    </div>
</template>

<script>
/* eslint-disable no-alert,arrow-parens,no-param-reassign,no-lonely-if */
import { mapActions, mapGetters } from 'vuex';
import $ from 'jquery';
import {
  has,
  isNil,
  isArray,
} from 'lodash';
import ValidationModal from '../components/ValidationModal.vue';
import DistributionOverview from './DistributionOverview.vue';
import InfoBox from '../components/InfoBox.vue';
import axios from 'axios';

export default {
  props: {
    property: {
      required: true,
      type: String,
    },
    page: {
      required: true,
      type: String,
    },
    id: {
      type: String,
    },
    isDistributionOverview: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      formValues: {},
      failedFields: [],
      modal: {
        confirm: '',
        message: '',
        callback: '',
      },
      mandatoryModal: {
        confirm: '',
        message: 'Mandatory Properties missing',
        callback: $('#modal').modal('hide'),
      },
      idModal: {
          confirm: '',
          message: 'Dataset ID already exists',
          callback: $('#modal').modal('hide'),
      }
    };
  },
  components: {
    ValidationModal,
    DistributionOverview,
    InfoBox,
  },
  computed: {
    ...mapGetters('auth', [
      'getIsEditMode',
      'getUserCatalogIds',
    ]),
    ...mapGetters('dataProviderInterface', [
      'getSchema',
      'mandatoryFieldsFilled',
      'getNavSteps',
      'getData',
    ]),
    getFirstTitleFromForm() {
      return has(this.formValues, 'dct:title')
        && this.formValues['dct:title'].length > 0
        && has(this.formValues['dct:title'][0], '@value')
        && !isNil(this.formValues['dct:title'][0], '@value')
        ? this.formValues['dct:title'][0]['@value']
        : '';
    },
    createIDFromTitle() {
      const title = this.getFirstTitleFromForm;
      return title
        .toLowerCase()
        .replace(/ /g, '-');
    },
    isInput() {
      return this.$route.params.page !== 'overview' && this.$route.params.page !== 'distoverview';
    },
  },
  methods: {
    ...mapActions('auth', [
      'setIsEditMode',
      'setIsDraft',
    ]),
    ...mapActions('dataProviderInterface', [
      'createSchema',
      'translateSchema',
      'saveToJsonld',
      'saveToForm',
      'saveExistingJsonld',
      'addCatalogOptions',
      'clearAll',
    ]),
    clearValues() {
      this.formValues = {};
      this.failedFields = [];
    },
    showValidationFields(fields) {
      const fieldNames = Object.keys(fields);
      const translatedFields = [];
      for (let index = 0; index < fieldNames.length; index += 1) {
        const fieldLabel = fields[fieldNames[index]].label;
        if (fields[fieldNames[index]].id !== 'datasetIDFormHidden') translatedFields.push(fieldLabel);
      }
      this.failedFields = translatedFields;
      $('#validationModal').modal({ show: true });
    },
    handleSubmit() {
      this.$root.$emit('go-to-next');
    },
    createDatasetID() {
      if (this.property === 'datasets' && this.page === 'step1') {
        // Create Dataset ID from title if not existing
        if (has(this.formValues, 'dct:title')
        && !isNil(this.formValues['dct:title']
        && this.formValues['dct:title'].length > 0)
        && has(this.formValues['dct:title'][0], '@value')
        && !isNil(this.formValues['dct:title'][0], '@value')
        && (!has(this.formValues, 'datasetID')
        || (has(this.formValues, 'datasetID') && this.createIDFromTitle.startsWith(this.formValues.datasetID))
        || (has(this.formValues, 'datasetID') && this.formValues.datasetID.startsWith(this.createIDFromTitle)))) {
          this.formValues.datasetID = this.createIDFromTitle;
        }

        if (has(this.formValues, 'dct:title')
        && isArray(this.formValues['dct:title'])
        && !isNil(this.formValues['dct:title']
        && this.formValues['dct:title'].length > 0)
        && has(this.formValues['dct:title'][0], '@value')
        && isNil(this.formValues['dct:title'][0], '@value')
        && has(this.formValues, 'datasetID')) this.formValues.datasetID = '';
      }
    },
  },
  created() {
    if (this.$route.query.edit === false) {
      this.clearAll();
    }
    // form content (schema) created based on defined page properties included in inputconfigMin
    if (this.page !== 'overview' && this.page !== 'distoverview') {
      this.createSchema({ property: this.property, page: this.page });
      this.translateSchema({ property: this.property });
    }
  },
  mounted() {
    if (this.page !== 'overview' && this.page !== 'distoverview') {
      this.addCatalogOptions({property: this.property, catalogs: this.getUserCatalogIds});
      this.saveExistingJsonld(this.property);
      this.saveToForm({property: this.property, page: this.page, distid: this.id }).then((response) => {
        const preexistingValues = response;
        // vuex returns observer of the data which will not be accepted by the input form so the data gets converted to 'real' data by using JSON conversion functions
        this.formValues = JSON.parse(JSON.stringify(preexistingValues));
      });
    }
  },
  watch: {
    getFirstTitleFromForm: {
      handler() {
        // only create id from title if the user is not editing an existing dataset with an existing datasetID
        if (!this.getIsEditMode) this.createDatasetID();
      },
    },
    // the schema is a computed value which gets computed only once so on language change this value must be re-computed
    '$i18n.locale': {
      handler() {
        this.createSchema({ property: this.property, page: this.page });
        this.translateSchema({ property: this.property });
      }
    }
  },
  beforeRouteEnter(to, from, next) {
    // Always clear storage when entering DPI
    next(vm => {
      if (from.name !== null && !from.name.startsWith('DataProviderInterface')) {
        vm.clearAll();
      }
    });
  },
  async beforeRouteLeave(to, from, next) {
    // only check mandatory properties and show modal if next route is overview page
    if (to.path.includes('/overview')) {
      if (!this.mandatoryFieldsFilled(this.property)) {
        this.modal = this.mandatoryModal;
        $('#modal').modal({ show: true });
      } else {
        // check unique id
        if (this.property === 'datasets' || this.property === 'distributions') {
          if (!this.getIsEditMode) {
            const datasetid = this.getData('datasets')['@id'];
            const request = `${this.$env.api.hubUrl}datasets/${datasetid}?useNormalizedId=true`;
            const unique = await axios.get(request)
            .then(() => {
              return false;
            })
            .catch(() => {
              return true;
            });

            if (unique) {
              next();
            } else {
              this.modal = this.idModal;
              $('#modal').modal({ show: true });
            }
          }          
        }
      }
    } else {
      next();
    }
  },
};
</script>
<style lang="scss">
select {
  line-height: unset !important;
}

.form-container {
  padding-top: 20px;
  margin-top: 30px;
  border: solid 1px #d5d5d5;
  border-radius: 3px;
  margin-bottom: 20px;
}

.inputContainer {
  display: flex;
}

.formContainer {
  width: 70%;
}

.infoContainer {
  top: 20px;
  width: 35%;
  padding: 10px;
  margin: 0 10px 0 10px;
  background-color: transparent;
}

.distributionPage0 {
  display: block;
}

.distributionPage1 {
  display: none;
}

.distributionPage2 {
  display: none;
}

.grid2r2c {
  .formulate-input-group-repeatable {
    display: grid;
    grid-template-columns: 70% 28%;
    grid-template-rows: auto auto;
    grid-gap: 10px;
    background-color: transparent;
  }
}

.grid1r2c {
  .formulate-input-group-repeatable {
    display: grid;
    grid-template-columns: 70% 28%;
    grid-template-rows: auto;
    grid-gap: 10px;
    background-color: transparent;
  }
}

.row1 {
  grid-row-start: 1;
  grid-row-end: 2;
}

.row2 {
  grid-row-start: 2;
  grid-row-end: 3;
}

.grow3 {
  grid-row-start: 3;
  grid-row-end: 4;
}

.column1 {
  grid-column-start: 1;
  grid-column-end: 2;
}

.column2 {
  grid-column-start: 2;
  grid-column-end: 3;
}

.columnboth {
  grid-column-start: 1;
  grid-column-end: 3;
}

.display-none {
  display: none;
}
</style>
