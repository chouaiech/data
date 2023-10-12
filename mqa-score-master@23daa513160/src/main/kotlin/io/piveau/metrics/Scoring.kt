package io.piveau.metrics

import io.piveau.dqv.replaceMeasurement
import io.piveau.vocabularies.vocabulary.DQV
import io.piveau.vocabularies.vocabulary.PV
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.Resource
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.RDF

val dimensions: List<Resource> = PiveauDQVVocabulary.model.listResourcesWithProperty(RDF.type, DQV.Dimension).toList()

fun scoreMetrics(metrics: Model, resource: Resource): Int {

    // Find the best scored distribution
    val distributions = resource.listProperties(DCAT.distribution)
        .filterKeep { it.`object`.isResource }
        .mapWith { it.resource }
        .toList()

    val (dimensionScoring, issued, modified) = if (distributions.isNotEmpty()) {
        distributions.map { scoreDistribution(it, metrics, dimensions) }
            .reduce { dim1, dim2 -> if (dim1.first.values.sum() >= dim2.first.values.sum()) dim1 else dim2 }
    } else {
        Triple(dimensions.associateWith { 0 }.toMutableMap(), second = false, third = false)
    }

    metrics.getResource(resource.uri)
        .listProperties(DQV.hasQualityMeasurement).asSequence()
        .filter { it.`object`.isResource }
        .map { it.`object`.asResource() }
        .forEach { measurement ->
            val score = measurement.score()
            val metric = measurement.isMeasurementOf()
            if ((metric != PV.dateIssuedAvailability || !issued)
                && (metric != PV.dateModifiedAvailability || !modified)
            ) {
                PiveauDQVVocabulary.dimensionOf(metric)?.apply {
                    dimensionScoring[this] = score + dimensionScoring.getOrDefault(this, 0)
                }
            }
        }

    dimensionScoring.forEach { (key, value) ->
        metrics.replaceMeasurement(resource, key.scoring, value)
    }

    return dimensionScoring.values.sum().also {
        metrics.replaceMeasurement(resource, PV.scoring, it)
    }
}

fun Resource.score(): Int = getProperty(DQV.isMeasurementOf)?.resource?.let {
    when (it) {
        PV.keywordAvailability -> it.booleanScore(this)
        PV.categoryAvailability -> it.booleanScore(this)
        PV.spatialAvailability -> it.booleanScore(this)
        PV.temporalAvailability -> it.booleanScore(this)
        PV.accessUrlStatusCode -> PiveauDQVVocabulary.metric(it.uri).let { res ->
            if (getProperty(DQV.value).int in (200..299)) res.getProperty(PV.trueScore)?.int ?: 0 else res.getProperty(PV.falseScore)?.int
                ?: 0
        }

        PV.downloadUrlAvailability -> it.booleanScore(this)
        PV.downloadUrlStatusCode -> PiveauDQVVocabulary.metric(it.uri).let { res ->
            if (getProperty(DQV.value).int in (200..299)) res.getProperty(PV.trueScore)?.int ?: 0 else res.getProperty(PV.falseScore)?.int
                ?: 0
        }

        PV.formatAvailability -> it.booleanScore(this)
        PV.mediaTypeAvailability -> it.booleanScore(this)
        PV.formatMediaTypeVocabularyAlignment -> it.booleanScore(this)
        PV.formatMediaTypeNonProprietary -> it.booleanScore(this)
        PV.formatMediaTypeMachineInterpretable -> it.booleanScore(this)
        PV.dcatApCompliance -> it.booleanScore(this)
        PV.formatMatch -> it.booleanScore(this)
        PV.syntaxValid -> it.booleanScore(this)
        PV.licenceAvailability -> it.booleanScore(this)
        PV.knownLicence -> it.booleanScore(this)
        PV.accessRightsAvailability -> it.booleanScore(this)
        PV.accessRightsVocabularyAlignment -> it.booleanScore(this)
        PV.contactPointAvailability -> it.booleanScore(this)
        PV.publisherAvailability -> it.booleanScore(this)
        PV.rightsAvailability -> it.booleanScore(this)
        PV.byteSizeAvailability -> it.booleanScore(this)
        PV.dateIssuedAvailability -> it.booleanScore(this)
        PV.dateModifiedAvailability -> it.booleanScore(this)
        PV.atLeastFourStars -> it.booleanScore(this)
        else -> 0
    }
} ?: 0

fun scoreDistribution(
    distribution: Resource,
    metrics: Model,
    dimensions: List<Resource>
): Triple<MutableMap<Resource, Int>, Boolean, Boolean> {

    val dimensionScoring = dimensions.associateWith { 0 }.toMutableMap()

    val accessUrlStatusCodes = metrics.listObjectsOfProperty(distribution, DQV.hasQualityMeasurement)
        .filterKeep {
            it.isResource && it.asResource().hasProperty(DQV.isMeasurementOf, PV.accessUrlStatusCode)
        }.mapWith { it.asResource() }.toList()

    dimensionScoring[PV.accessibility] = accessUrlStatusCodes.maxOfOrNull { it.score() } ?: 0

    val downloadUrlStatusCodes = metrics.listObjectsOfProperty(distribution, DQV.hasQualityMeasurement)
        .filterKeep {
            it.isResource && it.asResource().hasProperty(DQV.isMeasurementOf, PV.downloadUrlStatusCode)
        }.mapWith { it.asResource() }.toList()

    dimensionScoring[PV.accessibility] =
        dimensionScoring.getOrDefault(PV.accessibility, 0) + (downloadUrlStatusCodes.maxOfOrNull { it.score() } ?: 0)

    var issued = false
    var modified = false

    metrics.listObjectsOfProperty(distribution, DQV.hasQualityMeasurement).asSequence()
        .filterNot { accessUrlStatusCodes.contains(it) || downloadUrlStatusCodes.contains(it) }
        .filter { it.isResource }
        .map { it.asResource() }
        .forEach {
            val score = it.score()
            val metric = it.isMeasurementOf()
            when (metric) {
                PV.dateIssuedAvailability -> issued = true
                PV.dateModifiedAvailability -> modified = true
            }
            PiveauDQVVocabulary.dimensionOf(metric)?.apply {
                dimensionScoring[this] = score + dimensionScoring.getOrDefault(this, 0)
            }
        }

    return Triple(dimensionScoring, issued, modified)
}

val Resource.scoring: Resource
    get() = when (this) {
        PV.accessibility -> PV.accessibilityScoring
        PV.findability -> PV.findabilityScoring
        PV.interoperability -> PV.interoperabilityScoring
        PV.reusability -> PV.reusabilityScoring
        PV.contextuality -> PV.contextualityScoring
        else -> PV.scoring
    }

fun Resource.booleanScore(resource: Resource): Int = PiveauDQVVocabulary.metric(uri).let {
    if (resource.getProperty(DQV.value).boolean) it.getProperty(PV.trueScore)?.int
        ?: 0 else it.getProperty(PV.falseScore)?.int ?: 0
}

fun Resource.isMeasurementOf(): Resource = getPropertyResourceValue(DQV.isMeasurementOf)