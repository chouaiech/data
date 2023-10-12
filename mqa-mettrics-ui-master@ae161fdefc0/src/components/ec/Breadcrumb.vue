<template>
<nav class="ecl-breadcrumb" aria-label="You are here:" data-ecl-breadcrumb="true" data-ecl-auto-init="Breadcrumb">
  <ol class="ecl-breadcrumb__container">
    <li class="ecl-breadcrumb__segment" data-ecl-breadcrumb-item="static"><a href="/component-library/example"
        class="ecl-link ecl-link--standalone ecl-link--no-visited ecl-breadcrumb__link">Home</a><svg
        class="ecl-icon ecl-icon--2xs ecl-icon--rotate-90 ecl-breadcrumb__icon" focusable="false" aria-hidden="true"
        role="presentation">
        <use xlink:href="/component-library/dist/media/icons.ccfd2174.svg#corner-arrow"></use>
      </svg></li>
    <li class="ecl-breadcrumb__segment ecl-breadcrumb__segment--ellipsis" data-ecl-breadcrumb-ellipsis=""><button
        class="ecl-button ecl-button--ghost ecl-breadcrumb__ellipsis" type="button"
        data-ecl-breadcrumb-ellipsis-button="" aria-label="Click to expand">…</button><svg
        class="ecl-icon ecl-icon--2xs ecl-icon--rotate-90 ecl-breadcrumb__icon" focusable="false" aria-hidden="true"
        role="presentation">
        <use xlink:href="/component-library/dist/media/icons.ccfd2174.svg#corner-arrow"></use>
      </svg></li>
    <li class="ecl-breadcrumb__segment" data-ecl-breadcrumb-item="expandable"><a href="/component-library/example"
        class="ecl-link ecl-link--standalone ecl-link--no-visited ecl-breadcrumb__link">Datasets</a><svg
        class="ecl-icon ecl-icon--2xs ecl-icon--rotate-90 ecl-breadcrumb__icon" focusable="false" aria-hidden="true"
        role="presentation">
        <use xlink:href="/component-library/dist/media/icons.ccfd2174.svg#corner-arrow"></use>
      </svg></li>
    <li class="ecl-breadcrumb__segment ecl-breadcrumb__current-page" data-ecl-breadcrumb-item="static"
      aria-current="page">Metadata quality</li>
  </ol>
</nav>
</template>

<script>
export default {
  name: 'Breadcrumb'
}
</script>

<style lang="scss" scoped>
.ecl-breadcrumb {
  border-bottom: 1px solid #D9D9D9;
}
</style>
