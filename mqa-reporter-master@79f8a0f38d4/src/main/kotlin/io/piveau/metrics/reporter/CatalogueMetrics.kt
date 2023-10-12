package io.piveau.metrics.reporter

import io.piveau.metrics.reporter.model.Translation
import io.piveau.metrics.reporter.model.chart.BarChart
import io.piveau.metrics.reporter.model.chart.Chart
import io.piveau.metrics.reporter.model.chart.DoughnutChart
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class CatalogueMetrics(val config: JsonObject, translation: Translation) {

    val catalogueInfo: JsonObject
        get() = config.getJsonObject("info", JsonObject())

    val catalogueId: String
        get() = catalogueInfo.getString("id", "")

    val catalogueTitle: String
        get() = catalogueInfo.getString("title", "")

    val accessibility: JsonObject
        get() = config.getJsonObject("accessibility", JsonObject())

    val contextuality: JsonObject
        get() = config.getJsonObject("contextuality", JsonObject())

    val findability: JsonObject
        get() = config.getJsonObject("findability", JsonObject())

    val interoperability: JsonObject
        get() = config.getJsonObject("interoperability", JsonObject())

    val reusability: JsonObject
        get() = config.getJsonObject("reusability", JsonObject())

    val downloadUrlAvailability: JsonArray
        get() = accessibility.getJsonArray("downloadUrlAvailability", JsonArray())

    val accessUrlStatusCodes: JsonArray
        get() = accessibility.getJsonArray("accessUrlStatusCodes", JsonArray())

    val downloadUrlStatusCodes: JsonArray
        get() = accessibility.getJsonArray("downloadUrlStatusCodes", JsonArray())

    val rightsAvailability: JsonArray
        get() = contextuality.getJsonArray("rightsAvailability", JsonArray())

    val byteSizeAvailability: JsonArray
        get() = contextuality.getJsonArray("byteSizeAvailability", JsonArray())

    val dateIssuedAvailability: JsonArray
        get() = contextuality.getJsonArray("dateIssuedAvailability", JsonArray())

    val dateModifiedAvailability: JsonArray
        get() = contextuality.getJsonArray("dateModifiedAvailability", JsonArray())

    val keywordAvailability: JsonArray
        get() = findability.getJsonArray("dateModifiedAvailability", JsonArray())

    val categoryAvailability: JsonArray
        get() = findability.getJsonArray("categoryAvailability", JsonArray())

    val spatialAvailability: JsonArray
        get() = findability.getJsonArray("spatialAvailability", JsonArray())

    val temporalAvailability: JsonArray
        get() = findability.getJsonArray("temporalAvailability", JsonArray())

    val formatAvailability: JsonArray
        get() = interoperability.getJsonArray("formatAvailability", JsonArray())

    val mediaTypeAvailability: JsonArray
        get() = interoperability.getJsonArray("mediaTypeAvailability", JsonArray())

    val formatMediaTypeAlignment: JsonArray
        get() = interoperability.getJsonArray("formatMediaTypeAlignment", JsonArray())

    val formatMediaTypeNonProprietary: JsonArray
        get() = interoperability.getJsonArray("formatMediaTypeNonProprietary", JsonArray())

    val formatMediaTypeMachineReadable: JsonArray
        get() = interoperability.getJsonArray("formatMediaTypeMachineReadable", JsonArray())

    val dcatApCompliance: JsonArray
        get() = interoperability.getJsonArray("dcatApCompliance", JsonArray())

    val licenceAvailability: JsonArray
        get() = reusability.getJsonArray("licenceAvailability", JsonArray())

    val licenceAlignment: JsonArray
        get() = reusability.getJsonArray("licenceAlignment", JsonArray())

    val accessRightsAvailability: JsonArray
        get() = reusability.getJsonArray("accessRightsAvailability", JsonArray())

    val accessRightsAlignment: JsonArray
        get() = reusability.getJsonArray("accessRightsAlignment", JsonArray())

    val contactPointAvailability: JsonArray
        get() = reusability.getJsonArray("contactPointAvailability", JsonArray())

    val publisherAvailability: JsonArray
        get() = reusability.getJsonArray("publisherAvailability", JsonArray())

    val accessibilityCharts = mutableListOf<Chart>()

    val contextualityCharts = mutableListOf<Chart>()

    val findabilityCharts = mutableListOf<Chart>()

    val interoperabilityCharts = mutableListOf<Chart>()

    val reusabilityCharts = mutableListOf<Chart>()

    init {
        val labels = JsonObject()
            .put("yes", translation.metricLabelYes)
            .put("no", translation.metricLabelNo)

        accessibilityCharts.add(DoughnutChart(translation.accessibilityDownloadUrl, downloadUrlAvailability, labels, null))
        accessibilityCharts.add(BarChart(translation.accessibilityAccessUrl, accessUrlStatusCodes, null))
        accessibilityCharts.add(BarChart(translation.accessibilityDownloadUrl, downloadUrlStatusCodes, null))

        contextualityCharts.add(DoughnutChart("rightsAvailability", rightsAvailability, labels, null))
        contextualityCharts.add(DoughnutChart("byteSizeAvailability", byteSizeAvailability, labels, null))
        contextualityCharts.add(DoughnutChart("dateIssuedAvailability", dateIssuedAvailability, labels, null))
        contextualityCharts.add(DoughnutChart("dateModifiedAvailability", dateModifiedAvailability, labels, null))

        findabilityCharts.add(DoughnutChart("keywordAvailability", keywordAvailability, labels, null))
        findabilityCharts.add(DoughnutChart("categoryAvailability", categoryAvailability, labels, null))
        findabilityCharts.add(DoughnutChart("spatialAvailability", spatialAvailability, labels, null))
        findabilityCharts.add(DoughnutChart("temporalAvailability", temporalAvailability, labels, null))

        interoperabilityCharts.add(DoughnutChart("formatAvailability", formatAvailability, labels, null))
        interoperabilityCharts.add(DoughnutChart("mediaTypeAvailability", mediaTypeAvailability, labels, null))
        interoperabilityCharts.add(DoughnutChart("formatMediaTypeAlignment", formatMediaTypeAlignment, labels, null))
        interoperabilityCharts.add(DoughnutChart("formatMediaTypeNonProprietary", formatMediaTypeNonProprietary, labels, null))
        interoperabilityCharts.add(DoughnutChart("formatMediaTypeMachineReadable", formatMediaTypeMachineReadable, labels, null))
        interoperabilityCharts.add(DoughnutChart("dcatApCompliance", dcatApCompliance, labels, null))

        reusabilityCharts.add(DoughnutChart("licenceAvailability", licenceAvailability, labels, null))
        reusabilityCharts.add(DoughnutChart("licenceAlignment", licenceAlignment, labels, null))
        reusabilityCharts.add(DoughnutChart("accessRightsAvailability", accessRightsAvailability, labels, null))
        reusabilityCharts.add(DoughnutChart("accessRightsAlignment", accessRightsAlignment, labels, null))
        reusabilityCharts.add(DoughnutChart("contactPointAvailability", contactPointAvailability, labels, null))
        reusabilityCharts.add(DoughnutChart("publisherAvailability", publisherAvailability, labels, null))
    }

    val dimensionCharts
        get() = listOf(
            accessibilityCharts,
            contextualityCharts,
            findabilityCharts,
            interoperabilityCharts,
            reusabilityCharts).flatten()

}
