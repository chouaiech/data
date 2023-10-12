<template>
    <div class="dsd-distribution-quality container">
        <div class="mt-5">
            <h2>{{ $t('message.datasetDetails.quality.distributionQuality') }}</h2>
            <div class="markdown-content">
                <p v-html="$t('message.datasetDetails.intro.distribution', { locale: $route.query.locale })" />
            </div>
        </div>
        <div class="row">
            <div class="col-12 ecl-accordion" data-ecl-auto-init="Accordion" data-ecl-accordion="">
                <div class="ecl-accordion__item" v-for="(distribution, index) in displayedQualityDistributions" :key="index" :id="distribution.id">
                    <!-- Distribution Quality -->
                    <h3 class="ecl-accordion__title" @click="toggleDistribution(index)">
                        <button
                            type="button"
                            class="ecl-accordion__toggle"
                            data-ecl-accordion-toggle=""
                            data-ecl-label-expanded="Close"
                            data-ecl-label-collapsed="Open"
                            aria-controls="accordion-example-content">
                            <span class="ecl-accordion__toggle-flex">
                                <span class="ecl-accordion__toggle-indicator align-center">
                                    <svg class="ecl-icon ecl-icon--fluid ecl-button__icon ecl-button__icon--after"
                                        :ref="`distPlus${index}`"
                                        focusable="false"
                                        aria-hidden="true"
                                        data-ecl-icon="">
                                        <use xlink:href="@/assets/img/ecl/icons.svg#plus"></use>
                                    </svg>
                                    <svg class="collapsed ecl-icon ecl-icon--fluid ecl-button__icon ecl-button__icon--after"
                                        :ref="`distMinus${index}`"
                                        focusable="false"
                                        aria-hidden="true"
                                        data-ecl-icon="">
                                        <use xlink:href="@/assets/img/ecl/icons.svg#minus"></use>
                                    </svg>
                                </span>
                                <span class="ecl-accordion__toggle-title">
                                    {{ distribution.title }}
                                </span>
                                <span class="align-center">
                                    <PvBadge
                                        class="format-badge"
                                        v-if="has(distribution, 'format')"
                                        :value="distribution.format"
                                        :type="distribution.format.id"></PvBadge>
                                </span>
                            </span>
                        </button>
                    </h3>
                    <div class="collapsed px-5 ecl-accordion__content ecl-u-border-top ecl-u-border-color-grey-25" :ref="`dist${index}`">
                        <div class="row dsd-distribution-quality-property" v-for="(qualityElement, index) in fullQualityDistributionData[distribution.id]" :key="index">
                            <h4 class="col-12 mt-5 font-weight-bold">{{ $t(`message.datasetDetails.quality.${qualityElement.title}`) }}</h4>
                            <div class="col-4 mt-3" v-for="(el, index) in qualityElement.items" :key="index">
                                <span class="row" v-for="(property, index) in printObject(el)" :key="index">
                                    <span class="col-8 text-truncate" :title="property.key">{{ property.key }}</span>
                                    <span class="col-4">{{ property.value }}</span>
                                </span>
                            </div>
                        </div>
                        <!-- CSV Linter -->
                        <CSVLinter v-if="showCSVLinter(distribution)" :validation="qualityDistributionValidation[distribution.id]"></CSVLinter>
                    </div>
                </div>
                <ECMore class="col-12 text-primary mt-4"
                    v-if="useECMore"
                    :label="displayAll ? 'Show less' : 'Show more'"
                    :upArrow="displayAll"
                    :action="() => toggleDisplayAll()"></ECMore>
            </div>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { has } from 'lodash-es';
import { helpers, PvBadge, CSVLinter } from '@piveau/piveau-hub-ui-modules';
import ECMore from "@/components/ECMore";

const { getTranslationFor } = helpers;

export default {
    name: 'datasetDetailsDistributionQuality',
    dependencies: 'DatasetService',
    components: {
        PvBadge,
        CSVLinter,
        ECMore,
    },
    data() {
        return {
            displayAll: this.$env.datasetDetails.quality.displayAll,
            numberOfDisplayedQualityDistributions: this.$env.datasetDetails.quality.numberOfDisplayedQualityDistributions,
        };
    },
    computed: {
        ...mapGetters('datasetDetails', [
            'getLanguages',
            'getDistributions',
            'getQualityDistributionData',
        ]),
        useECMore() {
            return this.qualityDistributions.length > this.numberOfDisplayedQualityDistributions;
        },
        displayedQualityDistributions() {
            return this.displayAll
                ? this.qualityDistributions
                : this.qualityDistributions.slice(0, this.numberOfDisplayedQualityDistributions);
        },
        qualityDistributions() {
            return this.getDistributions.map(dist => {
                let d = dist;
                d.title = this.getTranslationFor(d.title, this.$route.query.locale, this.getLanguages);
                return d;
            });
        },
        qualityDistributionData() {
            if (!this.getQualityDistributionData.result) return [];

            let results = this.getQualityDistributionData.result.results;

            let distributionResult = {};

            results.forEach(result => {
                let data = result[0];

                let id = has(data, 'info') && has(data.info, 'distribution-id')
                    ? data.info['distribution-id']
                    : '';

                let properties = Object.keys(data).filter(prop => prop !== 'info' && prop !== 'validation');

                distributionResult[id] = properties.map(prop => {
                    return {
                        title: prop,
                        items: data[prop],
                    }
                });
            });

            return distributionResult;
        },
        fullQualityDistributionData() {
            let result = this.qualityDistributionData;

            // Prevent accessing undefined values in result
            this.qualityDistributions.forEach(dist => {
                if (!result[dist.id]) result[dist.id] = [];
            });

            return result;
        },
        qualityDistributionValidation() {
            if (!this.getQualityDistributionData.result) return [];

            let results = this.getQualityDistributionData.result.results;

            let validationResult = {};

            results.forEach(result => {
                let data = result[0];

                let id = has(data, 'info') && has(data.info, 'distribution-id')
                    ? data.info['distribution-id']
                    : '';

                validationResult[id] = has(data, 'validation')
                    ? data.validation
                    : {};
            });

            return validationResult;
        },
    },
    methods: {
        has,
        getTranslationFor,
        toggleDisplayAll() {
            this.displayAll = !this.displayAll;
        },
        toggleDistribution(index) {
            // Close all Distributions
            this.getDistributions.forEach((dist, i) => {
                if (i === index) return;
                this.$refs[`dist${i}`][0].classList.add('collapsed');
                this.$refs[`distPlus${i}`][0].classList.remove('collapsed');
                this.$refs[`distMinus${i}`][0].classList.add('collapsed');
            });

            // Open current Distribution
            this.$refs[`dist${index}`][0].classList.toggle('collapsed');
            this.$refs[`distPlus${index}`][0].classList.toggle('collapsed');
            this.$refs[`distMinus${index}`][0].classList.toggle('collapsed');
        },
        printObject(object) {
            return Object.keys(object).map(o => {
                return { key: this.$t(`message.datasetDetails.quality.${o}`), value: this.convertValue(JSON.stringify(object[o])) }
            });
        },
        convertValue(value) {
            return value === 'true' ? 'yes'
                : value === 'false' ? 'no'
                : value === '{}' ? 'n/a'
                : value;
        },
        showCSVLinter(distribution) {
            if (!has(distribution, 'id') || !has(distribution, 'format') || !has(distribution.format, 'id')) return false
            let id = distribution.id;
            let format = distribution.format.id === 'CSV';
            return this.qualityDistributionValidation[id] && format;
        },
    },
    created() {},
    mounted() {},
};
</script>

<style lang="scss" scoped>
.dsd-distribution-quality {
    .collapsed {
        display: none;
    }

    .align-center {
        align-self: center;
    }
}
</style>
