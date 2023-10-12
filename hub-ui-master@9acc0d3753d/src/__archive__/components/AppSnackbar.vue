<template>
  <div>
    <app-toast
      v-for="toast in toasts"
      :key="`toast@${toast.id}`"
      :toastId="toast.id"
      :variant="toast.variant"
      @closed="handleClosed"
    >
      {{ toast.message }}
    </app-toast>
  </div>
</template>

<script>
export default {
  name: 'Snackbar',
  data() {
    return {
      nextId: 0,
      toasts: [],
    };
  },
  created() {
    this.$store.subscribe((mutation, state) => {
      if (mutation.type === 'snackbar/SHOW_MESSAGE') {
        const { message, variant } = state.snackbar;
        this.toasts.push({ message, variant, id: this.nextId });
        this.nextId += 1;
      }
    });
  },
  methods: {
    handleClosed(id) {
      this.toasts.splice(this.toasts.findIndex(t => t.id === id), 1);
    },
  },
};
</script>

<style>

</style>