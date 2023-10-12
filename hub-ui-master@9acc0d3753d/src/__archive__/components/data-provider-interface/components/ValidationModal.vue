<template>
  <div class="modal fade" id="validationModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true" data-cy="citation-modal">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h2 class="modal-title" id="deleteModalLabel">
              {{ $t('message.dataupload.validationtitle') }}
            </h2>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <h3>{{$t('message.dataupload.validationerror')}}</h3>
            <div class="mt-3 d-flex justify-content-start align-items-center">
              <ul>
                <li v-for="(property, i) in failedFields" :key="i">{{property}}</li>
              </ul>
            </div>
          </div>
          <div class="modal-footer">
            <button class="ok" data-dismiss="modal" @click="closeModal()">OK</button>
          </div>
        </div>
      </div>
    </div>
</template>
<script>
import $ from 'jquery';

export default {
  props: {
    failedFields: Array,
  },
  methods: {
    closeModal() {
      $('#validationModal').modal('hide');
    },
  },
};
</script>

<style scoped>
.ok {
  background-color: #001d85;
  border-color: #001d85;
  color: #fff;
  border-radius: 0.3em;
  font-size: 16px;
  font-family: "Ubuntu";
  padding: 0.75em;
  font-weight: 100;
}  
</style>
