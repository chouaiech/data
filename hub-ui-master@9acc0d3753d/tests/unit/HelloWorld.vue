<template functional>
  <h1>{{ props.msg }}</h1>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: {
      type: String,
      default: '',
    },
  },
};
</script>

<style>

</style>
