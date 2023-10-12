<template>
  <div>
    <div class="dist-list">
      <h3>{{ $t('message.metadata.distributions') }}</h3>
      <hr>
      <ul class="list-unstyled" v-if="getData('distributions').length > 0">
        <li v-for="(dist, i) in getData('distributions')" :key="i" class="dist-item">
          <div class="pr-md-4">
            <div class="circle float-md-right text-center text-white" :type="getDistributionFormat(dist)"><span>{{ truncate(getDistributionFormat(dist), 4, true) }}</span></div>
          </div>
          <div class="dist-details">
            <div class="dist-info">
              <span style="font-weight: bold">{{ dist['dct:title'] }}</span><br />
              <span style="color: #868e96">{{ dist['dct:description'] }}</span>
            </div>
            <div class="dist-edit">
              <span @click="redirectToDistributionForm(i)" class="dist-edit-button">{{ $t('message.datasetDetails.edit') }}</span>
              <span @click="triggerDeleteModal(i)" class="dist-delete-button">&times;</span>
            </div>
          </div>
        </li>
      </ul>
      <ul v-else class="list-unstyled">
        <li>{{ $t('message.dataupload.noDistributions') }}</li>
      </ul>
      <button class="default" v-if="distributionOverviewPage" @click="createDistribution">{{ $t('message.dataupload.newDistribution') }}</button>
    </div>
    <div class="modal fade" id="deleteDistributionModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true" data-cy="citation-modal">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title" id="deleteModalLabel">
              {{ $t('message.dataupload.deletemodal.deleteDistribution') }}
            </h2>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <h3>{{$t('message.dataupload.deletemodal.areyousure')}}</h3>
            <div class="mt-3 d-flex justify-content-start align-items-center">
            </div>
          </div>
          <div class="modal-footer">
            <button data-dismiss="modal" id="cancel">{{ $t('message.datasetDetails.datasets.modal.cancel') }}</button>
            <button @click="deleteDist()" id="delete">{{ $t('message.datasetDetails.delete') }}</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';
import { isEmpty } from 'lodash';
import { helpers } from '@piveau/piveau-hub-ui-modules';
import { mapActions, mapGetters } from 'vuex';
const { truncate } = helpers;

export default {
  props: ['distributionOverviewPage'],
  data() {
    return {
      distributionToDelete: 0,
      distributionData: [],
    };
  },
  computed: {
    ...mapGetters('dataProviderInterface', [
      'getNumberOfDistributions',
      'getNavSteps',
      'getData',
    ]),
  },
  methods: {
    ...mapActions('dataProviderInterface', [
      'addDistribution',
      'saveExistingJsonld',
      'deleteDistribution',
    ]),
    truncate,
    deleteDist() {
      this.deleteDistribution(this.distributionToDelete);
      $('#deleteDistributionModal').modal('hide');

      this.$router.push(`${this.$env.upload.basePath}/${this.$route.params.property}/distoverview`).catch(() => {});
    },
    triggerDeleteModal(index) {
      this.distributionToDelete = index;
      $('#deleteDistributionModal').modal({ show: true });
    },
    getDistributionFormat(dist) {
      if (!isEmpty(dist['dct:format'])) {
        return dist['dct:format']['@id'].substring(dist['dct:format']['@id'].lastIndexOf('/') + 1);
      } else {
        return 'UNKNOWN';
      }
    },
    redirectToDistributionForm(distributionIndex) {
      const firstDistPage = this.getNavSteps.distributions[0];
      this.$router.push(`${this.$env.upload.basePath}/distributions/${firstDistPage}/${distributionIndex}`).catch(() => {});
    },
    createDistribution(){
      // create an new distribution within store
      this.addDistribution();
      const distNumber = this.getNumberOfDistributions;
      const distIndex = distNumber - 1; // distributions are stored within an array and indexed by their position in array
      const firstDistPage = this.getNavSteps.distributions[0];
      
      // direct to distribution input form
      this.$router.push(`${this.$env.upload.basePath}/distributions/${firstDistPage}/${distIndex}?locale=${this.$i18n.locale}`);
    }
  },
  mounted() {
    // saving existing dataset and distrbution data from localStorage to vuex store
    this.saveExistingJsonld('datasets');
  }
};
</script>

<style lang="scss" scoped>
.circle {
  width: 40px;
  height: 40px;
  margin: 0 auto;
  padding: 20px 0;
  font-size: 12px;
  line-height: 1px;
  border-radius: 50%;
  background-color: #595959;
  &[type="HTML"] {
    background-color: #285C76;
  }
  &[type="JSON"] {
    background-color: var(--dark-orange);
  }
  &[type="XML"] {
    background-color: #8F4300;
  }
  &[type="TXT"] {
    background-color: #2B5E73;
  }
  &[type="CSV"] {
    background-color: var(--badge-green);
  }
  &[type="XLS"] {
    background-color: #1A6537;
  }
  &[type="ZIP"] {
    background-color: #252525;
  }
  &[type="API"] {
    background-color: #923560;
  }
  &[type="PDF"] {
    background-color: #B30519;
  }
  &[type="SHP"] {
    background-color: var(--badge-black);
  }
  &[type="RDF"],
  &[type="NQUAD"],
  &[type="NTRIPLES"],
  &[type="TURTLE"] {
    background-color: #0b4498;
  }
}

.dist {
  &-list {
    margin: 50px;
  }
  &-details {
    display: flex;
    flex: 1;
  }
  &-edit {
    width: 25%;
    display: flex;
    align-items: center;
    justify-content: space-evenly;
    margin-left: 5px;
    &-button {
        cursor:pointer;
    }
  }
  &-info {
    width: 75%;
  }
  &-item {
    margin: 5px;
    display: flex;
    padding: 20px;
    align-items: center;
    border-bottom: .1em solid #e5e5e5;
  }
  &-delete{
    &-button {
    cursor: pointer;
    font-size:18pt;
    }
  }
}

button {
  &#delete {
    background-color: #B30519;
    color: #fff;
    border-color: #B30519;
  }
  &#cancel {
    background-color: #767676;
    border-color: #767676;
    color: #fff;
  }
  &.default {
    background-color: #001d85;
    border-color: #001d85;
    color: #fff;
  }
  border-radius: 0.3em;
  font-size: 16px;
  font-family: "Ubuntu";
  padding: 0.75em;
  font-weight: 100;
}
</style>
