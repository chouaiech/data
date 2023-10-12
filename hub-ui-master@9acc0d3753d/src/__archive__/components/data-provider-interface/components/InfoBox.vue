<template>
  <vue-position-sticky :offsetTop="50">
    <div class="infoBox">
      <h5><i class="material-icons">info</i>{{$t('message.dataupload.info.information')}}</h5>
      <div id="info">{{$t('message.dataupload.info.defaultdescription')}}</div> 
    </div>
  </vue-position-sticky>
</template>

<script>
export default {};
</script>

<style>
</style>
