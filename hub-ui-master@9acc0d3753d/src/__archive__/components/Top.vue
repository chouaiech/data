/** A component to wrap all components that should be present in the HTML-header
    note: When naming this component 'Header' instead of 'Top' the page will break.
    Native HTML-Tag names are not allowed for Vue components. **/

<template>
    <header>
      <deu-header></deu-header>
    </header>
</template>

<script>
import DEUHeader from './DEU-header';

export default {
  name: 'top',
  components: {
    deuHeader: DEUHeader,
  },
};
</script>

<style scoped>

</style>
