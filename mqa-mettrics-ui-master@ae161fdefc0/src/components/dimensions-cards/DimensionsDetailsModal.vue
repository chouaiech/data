<template>
  <modal
      name="dimensions-details-modal"
      height="auto"
      width="100%"
      :max-width="1199.98"
      :adaptive="true"
      :scrollable="true"
      @before-close="beforeClose"
  >
    <div class="close-button-container">
      <button type="button" class="close close-button" aria-label="Close" @click="beforeClose">
        <i class="fas fa-times close-icon"></i>
      </button>
    </div>
    <dimensions-details
      :dimensionsType="dimensionsType"
    />
  </modal>
</template>

<script>
import DimensionsDetails from './DimensionsDetails'
import dimensionsAttributes from './utils/dimensions-attributes'

export default {

  name: 'DimensionsDetailsModal',
  components: {
    DimensionsDetails
  },
  props: {
    dimensionsType: {
      type: String,
      required: true,
      default () {
        return 'dimensions-details'
      }
    }
  },
  beforeRouteEnter (to, from, next) {
    // called before the route that renders this component is confirmed.
    // does NOT have access to `this` component instance,
    // because it has not been created yet when this guard is called!
    const toDimension = to.params.dimension
    if (!toDimension || !dimensionsAttributes[toDimension]) {
      // Don't confirm navigation when the dimension parameter is non-existant or falsy
      next(false)
    } else {
      // Show modal window the dimension details as content
      next(vm => vm.$modal.show('dimensions-details-modal'))
    }
  },
  methods: {
    beforeClose () {
      this.$router.push({
        name: (this.dimensionsType === 'dimensions-details' ? 'Dashboard' : 'CatalogueDetailDashboard'),
        query: { locale: this.$i18n.locale }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
//@import "@/styles/metrics-style.scss";
@import "~bootstrap/scss/bootstrap";

.close-button-container {
  position: absolute;
  top: 15px;
  right: 15px;
  z-index: 1000;
  .close-icon {
    font-size: 2rem;
  }
}

.modal {
  background: rgb(255, 255, 255);
  /*     -webkit-box-shadow: 0px 0px 0px 0px #ccc;  /* Safari 3-4, iOS 4.0.2 - 4.2, Android 2.3+ */
  /*     -moz-box-shadow:    0px 0px 0px 0px #ccc;  /* Firefox 3.5 - 3.6 */
  /*      box-shadow:        1px 2px 1px 1px #ccc; */
  overflow: scroll;
  display: flex;
  flex-direction: column;
  text-align: center;
}

//.modal-header {
//  display: flex;
//  text-align: center;
//  align-items: center;
//  padding-bottom: 10px;
//  border-bottom: 0px solid #e9ecef !important;
//  margin-bottom: 15px;
//
//}
//
//.modal-body {
//  position: relative;
//  padding: 20px 10px;
//  font-size: 12px;
//  text-align: left;
//  overflow: scroll;
//
//  @include for-size(desktop-up) {
//    position: relative;
//    overflow: scroll;
//    padding: 20px 10px;
//    font-size: 12px;
//    text-align: left;
//  }
//}

</style>
