<!-- MAP component that receives bounds -->
<template>
  <div :id="mapContainerId" ref="mapref" style="z-index:0"></div>
</template>

<script>
import {
  isNil,
  isArray,
  isString,
  isNumber,
} from 'lodash';
import { mapActions, mapGetters } from 'vuex';
import Leaflet from 'leaflet';

export default {
  name: 'MapBoundsReceiver',
  dependencies: ['DatasetService'],
  data() {
    return {
      map: {},
      useAnimation: this.$env.maps.useAnimation,
      urlTemplate: this.$env.maps.urlTemplate,
      options: this.$env.maps.options,
      attributionPosition: this.$env.maps.receiver.attributionPosition,
      bounds: this.startBounds,
    };
  },
  props: {
    startBounds: {
      type: Array,
    },
    height: {
      type: String,
    },
    width: {
      type: String,
    },
    mapContainerId: {
      type: String,
    },
    boundsId: {
      required: true,
    },
  },
  computed: {
    ...mapGetters('geo', [
      'getGeoBoundsById',
    ]),
    geoStateBoundsWatcher() {
      return this.getGeoBoundsById(this.boundsId);
    },
  },
  methods: {
    ...mapActions('datasets', [
      'useService',
    ]),
    ...mapActions('geo', [
      'setGeoBoundsForId',
      'setHoldedGeoBoundsForId',
      'resetGeoBoundsForId',
    ]),
    isNil,
    isArray,
    isString,
    isNumber,
    initBounds() {
      let isInvalid = true;
      const bounds = this.$route.query.bounds;
      if (!isNil(bounds) && isArray(bounds) && bounds.length === 2) {
        if (isString(bounds[0]) && isString(bounds[1])) {
          bounds[0] = bounds[0].split(',');
          bounds[1] = bounds[1].split(',');
        }
        if ((isArray(bounds[0]) && bounds[0].length === 2)
          && (isArray(bounds[1]) && bounds[1].length === 2)) {
          if (isNumber(this.isFloat(bounds[0][0]))
            && isNumber(this.isFloat(bounds[0][1]))
            && isNumber(this.isFloat(bounds[1][0]))
            && isNumber(this.isFloat(bounds[1][1]))) {
            isInvalid = false;
          }
        }
      }
      if (isInvalid) {
        // Remove bounds url query params if format is not valid
        if (this.$route.query.bounds) {
          this.bounds = undefined;
          this.$router.replace({ query: Object.assign({}, this.$route.query, { bounds: this.bounds }) });
        }
      } else {
        this.bounds = bounds;
      }
    },
    initMap() {
      // Init Map
      const map = Leaflet.map(this.mapContainerId, {
        editable: true,
        attributionControl: false,
      }).fitBounds(this.bounds);

      Leaflet.control.attribution({
        position: this.attributionPosition,
      }).addTo(map);

      // Get Tiles
      Leaflet.tileLayer(this.urlTemplate, this.options).addTo(map);

      this.$refs.mapref.style.height = this.height;
      this.$refs.mapref.style.width = this.width;
      map.invalidateSize();
      map.setZoom(map.getBoundsZoom(this.bounds));
      return map;
    },
    isFloat(value) {
      if (/^(-|\+)?([0-9]+(\.[0-9]+)?|Infinity)$/.test(value)) return Number(value);
      return NaN;
    },
    resetBounds() {
      if (this.useAnimation) this.map.flyToBounds(this.bounds, this.map.getBoundsZoom(this.bounds));
      else this.map.fitBounds(this.bounds, this.map.getBoundsZoom(this.bounds));
    },
  },
  filters: {},
  watch: {
    height: {
      handler(height) {
        this.$refs.mapref.style.height = height;
        this.map.invalidateSize();
      },
    },
    width: {
      handler(width) {
        this.$refs.mapref.style.width = width;
        this.map.invalidateSize();
      },
    },
    geoStateBoundsWatcher: {
      deep: true,
      handler(bounds) {
        if (!bounds) {
          if (this.useAnimation) this.map.flyToBounds(this.bounds, this.map.getBoundsZoom(this.bounds));
          else this.map.fitBounds(this.bounds, this.map.getBoundsZoom(this.bounds));
        } else {
          this.bounds = bounds;
          const b1 = Leaflet.latLng(bounds[0][0], bounds[0][1]);
          const b2 = Leaflet.latLng(bounds[1][0], bounds[1][1]);
          const b = Leaflet.latLngBounds(b2, b1);
          if (this.useAnimation) this.map.flyToBounds(b, this.map.getBoundsZoom(b));
          else this.map.fitBounds(b, this.map.getBoundsZoom(b));
        }
      },
    },
  },
  created() {
    this.useService(this.DatasetService);
    this.initBounds();
  },
  mounted() {
    this.map = this.initMap();
    this.map.on('resize', () => {
      this.map.invalidateSize();
      this.resetBounds();
    });
  },
};
</script>

<style lang="scss" scoped>
</style>
<style lang="scss">
@import '../styles/bootstrap_theme';
@import '~leaflet/dist/leaflet.css';

.leaflet-zoom-anim .leaflet-zoom-animated {
  will-change: unset !important;
}
</style>
