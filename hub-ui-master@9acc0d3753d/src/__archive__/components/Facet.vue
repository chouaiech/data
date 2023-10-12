<template>
  <!-- Component for a collapsible facet -->
  <div class="list-group">
    <slot name="before" />
    <template v-if="header">
      <div class="d-none d-md-block list-group-item facet-header">
        <h2 class="h5 mb-0 float-left">{{ header }}</h2>
        <i class="tooltip-icon material-icons small-icon align-right text-dark pl-1"
           data-toggle="tooltip"
           data-placement="right"
           :title="toolTipTitle">
          help_outline
        </i>
      </div>
      <a
        class="d-flex d-md-none list-group-item justify-content-between align-items-baseline"
        data-toggle="collapse"
        :data-target="`#${myId}`"
        @click="isExpanded = !isExpanded"
      >
        <h2 class="h5 mb-0">{{ header }}</h2>
        <button class="btn">
          <i class="material-icons small-icon expand-more animated" v-if="!isExpanded">expand_more</i>
          <i class="material-icons small-icon expand-less animated" v-else>expand_less</i>
        </button>
      </a>
    </template>

    <div
      :id="myId"
      class="collapse dont-collapse-sm">
      <template v-if="items && items.length > 0">
        <div
          class="list-item-container"
          v-for="(items, index) in items.slice(0, numItemsAllowed)"
          :key="`field@${index}`"
        >
          <slot
            :item="items"
            :index="index"
          />
        </div>
        <button
          v-if="items.length > minItems"
          class="d-block btn btn-primary btn-color w-100"
          @click="handleGrowToggle"
        >
          <i class="material-icons align-bottom expand-more animated">{{ isGrown ? 'expand_less' : 'expand_more' }}</i>
        </button>
      </template>

      <slot name="after" />
    </div>
  </div>
</template>

<script>

export default {
  name: 'Facet',
  props: {
    header: {
      type: String,
      default: '',
    },
    items: {
      type: Array,
      required: true,
    },
    maxItems: {
      type: Number,
      default: 50,
    },
    minItems: {
      type: Number,
      default: 5,
    },
    toolTipTitle: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      id: null,
      isExpanded: false,
      isGrown: false,
      numItemsAllowed: this.minItems,
    };
  },
  computed: {
    myId() {
      // Use Vue generated uid to set give each facet a unique id
      return `facet-${this.id}`;
    },
  },
  methods: {
    handleGrowToggle() {
      this.isGrown = !this.isGrown;
      this.numItemsAllowed = this.isGrown
        ? this.maxItems
        : this.minItems;
    },
  },
  mounted() {
    this.id = this._uid; // eslint-disable-line
  },
};
</script>

<style lang="scss" scoped>
.facet-header {
  background-color: rgba(0, 29, 133,0.05);
}
.tooltip-icon {
  font-size: 15px;
}

@media (min-width: 768px) {
  .collapse.dont-collapse-sm {
    display: block;
    height: auto !important;
    visibility: visible;
  }
}

.list-item-container {
  margin-bottom: -1px;
}

.btn-color {
  background-color: var(--primary);
  border-color: var(--primary);

  &:hover {
    background-color: #196fd2;
    border-color: #196fd2;
  }
}
</style>
