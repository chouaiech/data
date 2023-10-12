<template>
  <!-- Modals -->
  <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="confirmationDialogLabel" aria-hidden="true" data-backdrop="false" data-cy="create-doi-modal">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h2 class="modal-title" id="confirmationDialogLabel">
            Confirmation
          </h2>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <slot>
            Are you sure? This process can not be reverted.
          </slot>
        </div>
        <!-- Export options -->
        <!-- Currently unused but might be useful in the future -->
        <div class="modal-footer">
          <!-- <div class="alert alert-danger mr-auto" v-if="modal.error">
            {{ modal.error.message || "An unexpected network error occured." }}
          </div> -->
          <button
            class="btn btn-link"
            @click="$emit('confirm')"
            :disabled="loading"
          >
            <span 
              v-show="loading"
              class="spinner-border spinner-border-sm"
              role="status"
              aria-hidden="true"
            />
            {{ confirm }}
          </button>
          <button class="btn btn-secondary" data-dismiss="modal" :disabled="loading">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmationDialog',
  props: {
    confirm: {
      type: String,
      default: 'confirm',
    },
    loading: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style>

</style>