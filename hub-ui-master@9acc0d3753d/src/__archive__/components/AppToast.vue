<template>
  <div class="app-toast toast rounded-lg px-1" role="alert" aria-live="assertive" aria-atomic="true">
    <!-- If title slot exists -->
    <div v-if="$slots.title" class="toast-header">
        <strong class="me-auto">
          <!-- named slot -->
          <slot name="title" />
        </strong>
        <button type="button" class="close" data-dismiss="toast" aria-label="Close">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="toast-body">
        <div class="d-flex align-items-center">
          <small v-if="variant" class="pr-1 mr-auto">
            <i v-if="variant === 'success'" class="material-icons text-success">check</i>
            <i v-if="variant === 'error'" class="material-icons text-danger">error</i>
          </small>
          <div class="pr-1">
            <slot />
          </div>
          <div class="ml-auto">
            <button type="button" class="btn btn-link btn-sm" data-dismiss="toast" aria-label="Close" style="pointer-events: initial;">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery';

export default {
  name: 'Toast',
  props: {
    variant: {
      type: String,
      default: 'success',
    },
    toastId: {
      type: Number,
      default: 0,
    },
    toastOptions: {
      type: Object,
      default: () => ({
        delay: 5000,
        autohide: true,
        animation: true,
      }),
    },
  },
  mounted() {
    // Initialize toast
    const $toast = $(this.$el);
    $toast.toast({ ...this.toastOptions });
    $toast.on('hidden.bs.toast', () => {
      $toast.remove();
      this.$emit('closed', this.toastId);
    });
    $toast.toast('show');
  },
};
</script>

<style scoped>
  .app-toast {
    z-index: 1060;
    pointer-events: none;
  }
</style>