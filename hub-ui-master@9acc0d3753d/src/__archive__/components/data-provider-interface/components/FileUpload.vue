<template>
  <div :class="`formulate-input-element formulate-input-element--${context.type}`" :data-type="context.type" v-on="$listeners">
    <input type="text" v-model="context.model" @blur="context.blurHandler" hidden/>
    <div class="file-div">
      <input type="file" @change="uploadFile($event.target.files[0])">
      <div class="upload-feedback">
        <div v-if="isLoading" class="loading-spinner"></div>
        <div v-if="success"><i class="material-icons check-icon">check_circle</i></div>
        <div v-if="fail"><i class="material-icons close-icon">error</i></div>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable consistent-return, no-unused-vars */
import { mapGetters, mapActions } from 'vuex';
import axios from 'axios';

export default {
  props: {
    context: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      isLoading: false,
      success: false,
      fail: false,
    };
  },
  computed: {
    ...mapGetters('auth', [
      'getUserData',
    ]),
    ...mapGetters('dataProviderInterface', [
      'getData',
    ]),
    getCatalogue() {
      const catalog = this.getData('datasets')['dct:catalog'];
      return catalog;
    },
  },
  methods: {
    ...mapActions('dataProviderInterface', [
      'saveExistingJsonld',
    ]),
    async uploadFile(file) {
      this.isLoading = true;

      const form = new FormData();
      form.append('file', file);

      const catalog = this.getCatalogue;
      const token = this.getUserData.rtpToken;

      const requestOptions = {
        method: 'POST',
        url: `${this.$env.api.fileUploadUrl}data?catalog=${catalog}`,
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
        data: form,
      };

      try {
        const result = await axios.request(requestOptions);
        const path = result.data.result.location.substring(result.data.result.location.indexOf('/') + 1);
        this.context.model = `${this.$env.api.fileUploadUrl}${path}`;
        this.isLoading = false;
        this.success = true;
        this.context.rootEmit('change');
      } catch (err) {
        this.isLoading = false;
        this.fail = true;
        console.error(err); // eslint-disable-line
      }
    },
  },
  mounted() {
    this.saveExistingJsonld('datasets');
  }
};
</script>

<style lang="scss" scoped>
@import '../../../styles/bootstrap_theme';
@import '../../../styles/utils/css-animations';

.file-div {
  display: flex;
  align-items: center;
}

.upload-feedback {
  padding: 10px;
}

  /*** MATERIAL ICONS ***/
  %modal-icon {
    font-size: 20px;
    cursor: default;
  }

  .check-icon {
    @extend %modal-icon;
    color: #28a745;
  }

  .close-icon {
    @extend %modal-icon;
    color: red;
  }
</style>
