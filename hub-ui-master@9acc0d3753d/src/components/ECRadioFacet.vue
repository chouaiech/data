<template>
  <div class="facet-container list-group">
    <e-c-facet-header
      v-if="title"
      :title="title"
      :tooltip="toolTipTitle"
    />
    <div class="value-display list-group-item">
      {{ property }}
      <span class="ml-2 d-flex flex-wrap">
        <div v-for="(id, index) in optionIds" class="custom-control custom-radio" :key="id+index">
          <input type="radio" :id="id" :name="title" class="custom-control-input" @click="onChange(id)" :checked="option === id">
          <label class="custom-control-label" :for="id">{{ optionLabels[index] }}</label>
        </div>
      </span>
    </div>
  </div>
</template>

<script>
import ECFacetHeader from "@/components/ECFacetHeader";
export default {
  name: "ECRadioFacet",
  components: {ECFacetHeader},
  props: {
    title: String,
    toolTipTitle: String,
    property: String,
    initialOption: String,
    optionIds: Array,
    optionLabels: Array,
    change: Function
  },
  data() {
    return {
      option: this.initialOption
    };
  },
  computed: {
    myId() {
      // Use Vue generated uid to set give each facet a unique id
      return `facet-${this.id}`;
    }
  },
  methods: {
    onChange(id) {
      this.option = id;
      this.change(id);
    }
  },
  mounted() {
    // this.id = this.myId; // eslint-disable-line
    this.id = this._uid; // eslint-disable-line
  },
}
</script>

<style scoped lang="scss">

.custom-control {
  padding-left: 1.5rem;
  margin-right: 1rem;
}
.custom-control-label {
  &::before {
    left: -1.5rem !important;
  }
  &::after {
    left: -1.5rem !important;
  }
}
.custom-control-input:checked ~ .custom-control-label::before {
  border-color: var(--primary);
  background-color: var(--primary);
}

.value-display {
  min-height: 48px;
  border-color: #707070;
  border-radius: 0 !important;
  padding: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  &:hover {
    border-color: var(--primary);
  }
}

.facet-container {
  width: 100%;
  padding: 0;
  font-family: Arial, sans-serif;
}

</style>
