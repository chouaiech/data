<template>
  <div>
    <div id="nav" class="d-flex justify-content-between">
      <div class="left-form-nav">
        <!-- PREVIOUS STEP -->
        <FormulateInput type="button" :label="$t('message.dataupload.preview')" @click="previous()" :disabled="!isPreviousPage && !property === 'distributions'" class="prev-btn mr-2"></FormulateInput>

        <!-- CLEAR FORM -->
        <FormulateInput type="button" :label="$t('message.dataupload.clear')" @click="handleClear" class="clear-btn"></FormulateInput> 
      </div>
      <div class="right-form-nav">

        <!-- PUBLISH NEW DATASET -->
        <FormulateInput type="button" @click="submit('createdataset')" v-if="isOverviewPage && !getIsEditMode && !getIsDraft" class="mr-2"><span v-if="uploading.createdataset" class="loading-spinner"></span>{{$t('message.dataupload.publishdataset')}}</FormulateInput>
        <!-- SAVE NEW DATASET AS DRAFT -->
        <FormulateInput type="button" @click="submit('createdraft')" v-if="(mandatoryFieldsFilled(property) || isOverviewPage) && !getIsEditMode && !getIsDraft" class="mr-2"><span v-if="uploading.createdraft" class="loading-spinner"></span>{{$t('message.dataupload.saveasdraft')}}</FormulateInput>

        <!-- PUBLISH EDITED DATASET -->
        <FormulateInput type="button" @click="submit('createdataset')" v-if="getIsEditMode && !getIsDraft" class="mr-2"><span v-if="uploading.createdataset" class="loading-spinner"></span>{{$t('message.dataupload.savedataset')}}</FormulateInput>

        <!-- SAVE EDITED DRAFT  -->
        <FormulateInput type="button" @click="submit('createdraft')" v-if="(mandatoryFieldsFilled(property) || isOverviewPage) && getIsEditMode && getIsDraft" class="mr-2"><span v-if="uploading.createdraft" class="loading-spinner"></span>{{$t('message.dataupload.savedraft')}}</FormulateInput>
      
        <!-- NEXT STEP -->
        <!-- label triggers form submit and therefore handles error mesaages if required values are missing -->
        <label for="submit-form" v-if="!(isOverviewPage || page === 'distoverview')" class="submit-label">{{$t('message.dataupload.next')}}</label>
        <FormulateInput type="button" :label="$t('message.dataupload.next')" v-if="(!isOverviewPage && page === 'distoverview')" @click="next()" />
      </div>
    </div>

    <app-confirmation-dialog id="modal" :confirm="modal.confirm" @confirm="modal.callback">
      {{ modal.message }}
    </app-confirmation-dialog>
  </div>
</template>

<script>
/* eslint-disable */
import $ from 'jquery';
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'Navigation',
  components: {},
  data() {
    return {
      uploading: {
        createdataset: false,
        createdraft: false,
        publishdraft: false,
      },
      modal: {
        confirm: '',
        message: '',
        callback: '',
      },
      modals: {
        clear: {
          confirm: 'Clear form',
          message: 'Are your sure you want to clear the form?',
          callback: this.clearStorage,
        },
      },
      property: this.$route.params.property,
      page: this.$route.params.page,
      id: this.$route.params.id,
    };
  },
  computed: {
    ...mapGetters('auth', [
      'getIsEditMode',
      'getIsDraft',
      'getUserData',
    ]),
    ...mapGetters('dataProviderInterface', [
      'getData',
      'getNavSteps',
      'mandatoryFieldsFilled',
    ]),      
    isPreviousPage() {
      const currentPageIndex = this.getNavSteps[this.property].indexOf(this.page);
      return currentPageIndex > 0;
    },
    isOverviewPage() {
      // overview part of url not given as route parameter
      const path = this.$route.path;
      return path.includes('/overview');
    }
  },
  methods: {
    ...mapActions('auth', [
      'setIsEditMode',
      'setIsDraft',
    ]),
    ...mapActions('snackbar', [
      'showSnackbar',
    ]),
    ...mapActions('dataProviderInterface', [
      'finishJsonld',
    ]),
    closeModal() {
      $('#modal').modal('hide');
    },
    handleClear() {
      this.modal = this.modals.clear;
      $('#modal').modal({ show: true });
    },
    clearStorage() {
      this.closeModal();
      this.$emit('clearStorage'); // clear gets called within main DPI component
    },
    previous() {
      let currentPage;
      if (this.isOverviewPage) {
        currentPage = 'overview';
      } else {
        currentPage = this.page;
      }

      const pageIndex = this.getNavSteps[this.property].indexOf(currentPage);
      const nextIndex = pageIndex - 1;

      if (nextIndex > -1) {
          const nextPage = this.getNavSteps[this.property][nextIndex];
          // preserve distribution index
          if (this.id) {
            this.$router.push(`${this.$env.upload.basePath}/${this.property}/${nextPage}/${this.id}?locale=${this.$i18n.locale}`).catch(() => {});
          } else {
            this.$router.push(`${this.$env.upload.basePath}/${this.property}/${nextPage}?locale=${this.$i18n.locale}`).catch(() => {});
          }
      } else if (nextIndex === -1 && this.property === 'distributions') {
        // when on the first page of the distributions form the previous button directs to the first distribution overview page
        this.$router.push(`${this.$env.upload.basePath}/datasets/distoverview?locale=${this.$i18n.locale}`).catch(() => {});
      }
    },
    next() {
      const pageIndex = this.getNavSteps[this.property].indexOf(this.page);
      const numberOfPages = this.getNavSteps[this.property].length;
      const nextIndex = pageIndex + 1;

      if (nextIndex < numberOfPages) {
        const nextPage = this.getNavSteps[this.property][nextIndex];
        if (this.id) {
          // preserve distribution id in path 
          this.$router.push(`${this.$env.upload.basePath}/${this.property}/${nextPage}/${this.id}?locale=${this.$i18n.locale}`).catch(() => {});
        } else {
          this.$router.push(`${this.$env.upload.basePath}/${this.property}/${nextPage}?locale=${this.$i18n.locale}`).catch(() => {});
        }
      } else if (nextIndex === numberOfPages) {
        if (this.property === 'distributions') {
          // when within distributions the next button lead to datasets overview page
          this.$router.push(`${this.$env.upload.basePath}/datasets/overview?locale=${this.$i18n.locale}`).catch(() => {});
        }
      }
    },
    async submit(mode) {
      this.uploading[mode] = true;
      this.$Progress.start();

      const jsonld = await this.finishJsonld(this.$route.params.property).then((response) => {
        return response;
      });

      const rtpToken = this.getUserData.rtpToken;
      const datasetId = this.getData(this.property)['@id'];
      const catalogName = this.getData(this.property)['dct:catalog'] ? this.getData(this.property)['dct:catalog'] : '';
      let uploadUrl;
      let actionName;
      let actionParams = {
        id: datasetId,
        catalog: catalogName,
        body: jsonld,
        // TODO: populate title and description
        title: {},
        description: {},
      };

      if (mode === 'createdataset') {
        uploadUrl = `${this.$env.api.hubUrl}datasets/${datasetId}?catalogue=${catalogName}`;
        actionParams = { data: jsonld, token: rtpToken, url: uploadUrl };
        actionName = 'auth/createDataset';
      } else if (mode === 'createdraft') {
        uploadUrl = `${this.$env.api.hubUrl}drafts/datasets/${datasetId}?catalogue=${catalogName}`;
        actionName = 'auth/createUserDraft';
      } else if (mode === 'publishdraft') {
        uploadUrl = `${this.$env.api.hubUrl}drafts/datasets/publish/${datasetId}?catalogue=${catalogName}`;
        actionName = 'auth/publishUserDraft';
      }

      try {
        // Dispatch the right action depending on the mode
        await this.$store.dispatch(actionName, actionParams);
        await new Promise(resolve => setTimeout(resolve, 250));

        this.$Progress.finish();
        this.uploading = false;

        if (mode === 'createdataset' || mode === 'publishdraft') this.createDataset(datasetId);
        if (mode === 'createdraft') this.createDraft();
      } catch (err) {
        this.uploading[mode] = false;
        this.$Progress.fail();
        this.showSnackbar({ message: 'Network Error', variant: 'error' });
      }
    },
    createDataset(datasetId) {
      this.setIsEditMode(false);
      this.setIsDraft(false);
      this.showSnackbar({ message: 'Dataset published successfully', variant: 'success' });
      this.$router.push({ name: 'DatasetDetailsDataset', params: { ds_id: datasetId }, query: { locale: this.$route.query.locale }}).catch(() => {});
    },
    createDraft() {
      this.setIsEditMode(false);
      this.setIsDraft(false);
      this.showSnackbar({ message: 'Draft saved successfully', variant: 'success' });
      this.$router.push({ name: 'DataProviderInterface-Draft', query: { locale: this.$route.query.locale }}).catch(() => {});
    },
  },
  mounted() {
    this.$root.$on('go-to-next', () => this.next());
  },
};
</script>

<style lang="scss">
  @import '../../../styles/bootstrap_theme';
  @import '../../../styles/utils/css-animations';

#nav {
  .clear-btn button {
    background-color: #ffffff;
    border-color: #949494;
    color: rgb(79, 79, 79);
  }
  .dist-btn button {
    background-color: #ffffff;
    border-color: #2b2b2b;
    color: rgb(48, 48, 48);
  }
  .prev-btn button {
    background-color: #767676;
    border-color: #767676;
    color: #fff;
  }

  .submit-label button {
    background-color: #001d85;
    border-color: #001d85;
    color: #fff;
    border-radius: 0.3em;
    font-size: 16px;
    font-family: "Ubuntu";
    padding: 0.75em;
    font-weight: 100;
    display: inline-flex;
    align-items: center;
    margin-bottom: 0px;
    margin-left: auto;
    height: 50px;
  }

  button {
    background-color: #001d85;
    border-color: #001d85;
    color: #fff;
    border-radius: 0.3em;
    font-size: 16px;
    font-family: "Ubuntu";
    padding: 0.75em;
    font-weight: 100;
    display: inline-flex;
    align-items: center;
    margin-bottom: 0px;
    margin-left: auto;
    height: 50px;
    margin-top: 10px;
  }
}

.left-form-nav {
  width: 40%;
  display: flex;
}

.right-form-nav {
  display: flex;
}

.submit-label {
    background-color: #001d85;
    border-color: #001d85;
    color: #fff;
    border-radius: 0.3em;
    font-size: 16px;
    font-family: "Ubuntu";
    padding: 0.75em;
    font-weight: 100;
    display: inline-flex;
    align-items: center;
    margin-bottom: 0px;
    margin-left: auto;
    height: 50px;
    margin-top: 10px;
}

.submit-label:hover {
  cursor: pointer;
}
</style>
