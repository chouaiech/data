<template>
  <nav class="ecl-pagination" aria-label="Pagination">
    <ul class="ecl-pagination__list">
      <li class="ecl-pagination__item ecl-pagination__item--previous" v-if="page > 1" @click="setPage(page - 1)" key="previous">
        <a href="#" class="ecl-link ecl-link--standalone ecl-link--icon ecl-link--icon-before ecl-pagination__link" aria-label="previous">
          <svg class="ecl-icon ecl-icon--xs ecl-icon--rotate-270 ecl-link__icon" focusable="false" aria-hidden="true">
            <use xlink:href="@/assets/img/ecl/icons.svg#corner-arrow"></use>
          </svg>
          <span class="ecl-link__label">
            Previous
          </span>
        </a>
      </li>
      <li class="ecl-pagination__item" key="first" v-if="firstShownPageLink > 1" @click="setPage(1)">
        <a href="#" class="ecl-link ecl-link--standalone ecl-pagination__link">1</a><span class="ml-3">&#8230;</span>
      </li>
      <li class="ecl-pagination__item" v-for="i in shownPageLinks" :key="i" :class="{'ecl-pagination__item--current': i === page}" @click="setPage(i)">
        <a href="#" class="ecl-link ecl-link--standalone ecl-pagination__link mx-1">
          {{ i }}
        </a>
      </li>
      <li class="ecl-pagination__item" key="last" v-if="lastShownPageLink < maxPage" @click="setPage(maxPage)">
        <span class="mr-3">&#8230;</span><a href="#" class="ecl-link ecl-link--standalone ecl-pagination__link">{{ maxPage }}</a>
      </li>
      <li class="ecl-pagination__item ecl-pagination__item--next" v-if="page < maxPage" @click="setPage(page + 1)" key="next">
        <a href="#" class="ecl-link ecl-link--standalone ecl-link--icon ecl-link--icon-after ecl-pagination__link" aria-label="next">
          <span class="ecl-link__label">
            Next
          </span>
          <svg class="ecl-icon ecl-icon--xs ecl-icon--rotate-90 ecl-link__icon" focusable="false" aria-hidden="true">
            <use xlink:href="@/assets/img/ecl/icons.svg#corner-arrow"></use>
          </svg>
        </a>
      </li>
    </ul>
    <slot />
  </nav>
</template>

<script>
export default {
  name: "Pagination",
  props: {
    page: Number,
    maxPage: Number,
    onChange: Function,
  },
  computed: {
    numberShownLinks() {
      return this.page > 100 ? 3 : 5; // Show less items when numbers are large!
    },
    firstShownPageLink() {
      return Math.max(1, this.page - 2);
    },
    lastShownPageLink() {
      return Math.min(this.firstShownPageLink + this.numberShownLinks - 1, this.maxPage);
    },
    shownPageLinks() {
      const links = [];
      for (let i = this.firstShownPageLink; i <= this.lastShownPageLink; ++i) {
        links.push(i);
      }
      return links;
    }
  },
  methods: {
    setPage(n) {
      if (n !== this.page) this.onChange(n);
    }
  }
}
</script>

<style lang="scss" scoped>
.ecl-pagination__item--current:before {
  min-width: calc(1.5rem + 4px);
  width: 110% !important;
}
.ecl-link {
  color: #004494;
}
.ecl-pagination {
  display: flex;
  justify-content: space-between;
}

a {
  white-space: nowrap;
}

</style>
