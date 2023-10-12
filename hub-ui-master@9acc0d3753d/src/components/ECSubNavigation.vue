<template>
  <nav class="col-12 navbar navbar-expand-lg navbar-light px-4 pb-3 mt-0 sub-navigation">
    <div class="d-flex flex-row flex-nowrap w-100">
      <h1 class="d-inline-block d-lg-none title px-0 mb-0 text-break">{{ title }}</h1>
      <div class="col d-flex d-lg-none justify-content-end flex-wrap px-0">
        <button class="btn btn-primary mb-3 text-right text-white text-nowrap more-button" data-toggle="collapse" data-target="#subNavigation"
                aria-controls="subNavigation" aria-expanded="false" aria-label="Toggle navigation"
                data-cy="subnavigation-toggle" @click="contentVisible = !contentVisible">
            More
          <i class="material-icons small-icon align-bottom" v-if="contentVisible">arrow_drop_up</i>
          <i class="material-icons small-icon align-bottom" v-else>arrow_drop_down</i>
        </button>
      </div>
    </div>
    <div class="col-12 collapse navbar-collapse px-0 mt-3 mt-0-lg" id="subNavigation">
      <slot/>
    </div>
  </nav>
</template>

<script>
import $ from "jquery";

export default {
  name: "ECSubNavigation",
  props: {
    to: {
      default: '',
      required: false,
    },
    title: {
      type: String,
      default: '',
      required: false,
    },
  },
  data() {
    return {
      contentVisible: false
    }
  },
  beforeDestroy() {
    $('.tooltip').remove();
  },
}
</script>

<style scoped lang="scss">
.sub-navigation {
  background-color: white !important;
  box-shadow: 0 2px 4px -4px grey;
  min-height: 56px;
}

.title {
  font-size: 1.5rem;
  font-family: inherit;
  font-weight: 500;
  line-height: 1.2;
  color: inherit;
}

.more-button {
  max-height: 3rem;
}
</style>
