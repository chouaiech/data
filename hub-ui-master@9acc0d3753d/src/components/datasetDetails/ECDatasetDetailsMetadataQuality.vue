<template>
    <div class="container dsd-metadata-quality">
        <div class="header">
            <h2>{{ $t('message.header.navigation.data.metadataquality') }}</h2>
            <div class="markdown-content">
                <p v-html="$t('message.datasetDetails.intro.metadataQuality', { locale: $route.query.locale })"></p>
            </div>
        </div>
        <div class="row col-12 content">
            <article class="ecl-card" v-for="(result, index) in qualityDataResults" :key="index">
                <div class="ecl-card__body">
                    <div class="ecl-content-block ecl-card__content-block">
                        <h1 class="ecl-content-block__title ecl-u-type-color-blue">
                            {{ result.title }}
                        </h1>
                        <div class="row ecl-u-type-color-blue">
                            <div class="col-3 mt-5" v-for="(item, index) in result.items" :key="index">
                                <div class="ecl-u-border-bottom ecl-u-border-color-blue">
                                    <div class="row">
                                        <div class="col-8 text-truncate" :title="item.title">
                                            <span class="mr-2">{{ item.title }}</span>
                                        </div>
                                        <div class="col-4">
                                            <span v-if="has(item.items, 'name') && has(item.items, 'percentage') && item.items.name === 'yes'">{{ `${item.items.percentage}%` }}</span>
                                            <span v-else-if="has(item.items, 'name') && has(item.items, 'percentage') && item.items.name === '200'">{{ item.items.name }}</span>
                                            <span v-else-if="has(item.items, 'name') && has(item.items, 'percentage')">{{ `${item.items.percentage} : ${item.items.percentage}%` }}</span>
                                            <span v-else-if="item.items === undefined">n/a</span>
                                            <span v-else>{{ item.items }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { has, isArray } from 'lodash-es';

export default {
    name: 'datasetDetailsMetadataQuality',
    dependencies: 'DatasetService',
    data() {
        return {};
    },
    computed: {
        ...mapGetters('datasetDetails', [
            'getLanguages',
            'getQualityData',
            'getDistributions',
            'getQualityDistributionData',
        ]),
        qualityDataResults() {
            if (!this.getQualityData.result) return;

            let data = this.getQualityData.result.results[0];

            let properties = Object.keys(data).filter(prop => prop !== 'info');

            return properties.map(prop => {

                let propData = data[prop];
                let propDataItems = propData.map(pdi => {

                    let propDataKeys = Object.keys(pdi);

                    return propDataKeys.map(pdki => {
                        let propDataID, propDataKeysItems;

                        // TODO: Skipping sub properties here
                        if (pdki === 'dataset' || pdki === 'distributions') {
                            propDataID = Object.keys(pdi[pdki][0])[0];
                            propDataKeysItems = isArray(pdi[pdki][0][propDataID])
                                ? pdi[pdki][0][propDataID][0]
                                : pdi[pdki][0][propDataID];
                        }
                        else if (isArray(pdi[pdki])) {
                            propDataID = pdki;
                            propDataKeysItems = pdi[pdki][0];
                        }
                        else {
                            propDataID = pdki;
                            propDataKeysItems = pdi[pdki];
                        }

                        return {
                            title: this.$t(`message.datasetDetails.quality.${propDataID}`),
                            id: propDataID,
                            items: propDataKeysItems
                        }
                    })[0];
                });

                return {
                    title: this.$t(`message.datasetDetails.quality.${prop}`),
                    id: prop,
                    items: propDataItems,
                };
            });
        },
    },
    methods: {
        has,
    },
    created() {},
    mounted() {},
};
</script>

<style lang="scss" scoped>
.dsd-metadata-quality {
    .ecl-card {
        margin-top: 2%;
    }
}
</style>
