<template>
  <label :for="context.id">
    <span class="form-label">{{ context.label }}</span>
    <a v-if="info" @click="displayInfo()" class="infoButton"><i class="material-icons">info</i></a>
  </label>
</template>

<script>
export default {
  props: {
    context: {
      type: Object,
      required: true,
    },
    info: {
      type: [String, Boolean],
      default: false,
    },
  },
  methods: {
    displayInfo() {
      document.getElementById('info').innerHTML = `<p>${this.info}</p>`;
    },
  },
};
</script>

<style scoped>
.infoButton{
  border-radius: 5px;
  cursor: pointer;
}

.infoButton .material-icons {
  font-size: 20px;
  vertical-align: text-bottom;
  margin-left: 5px;
  margin-bottom: 1px;
}

.form-label {
  font-size: 20px;
}
</style>
