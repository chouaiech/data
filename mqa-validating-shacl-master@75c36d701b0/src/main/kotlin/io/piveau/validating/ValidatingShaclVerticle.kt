package io.piveau.validating

import io.piveau.dqv.addAnnotation
import io.piveau.dqv.createMetricsGraph
import io.piveau.dqv.listMetricsModels
import io.piveau.dqv.replaceAnnotation
import io.piveau.pipe.PipeContext
import io.piveau.rdf.*
import io.piveau.vocabularies.vocabulary.*
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.DCTerms
import org.apache.jena.vocabulary.OA
import org.apache.jena.vocabulary.RDF
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class ValidatingShaclVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExperimentalTime
    override fun start(startPromise: Promise<Void>) {
        vertx.eventBus().consumer(ADDRESS_PIPE, this::handlePipe)
        startPromise.complete()
    }

    companion object {
        const val ADDRESS_PIPE: String = "io.piveau.pipe.validating.shacl.queue"
    }

    @ExperimentalTime
    private fun handlePipe(message: Message<PipeContext>) {
        val pipeContext = message.body()

        if (pipeContext.dataInfo.getString("source", "") != "dcat-ap") {
            pipeContext.log().debug("Data validation skipped: {}", pipeContext.dataInfo)
            pipeContext.pass()
            return
        }

        var content =
            if (pipeContext.pipeManager.isBase64Payload) pipeContext.binaryData else pipeContext.stringData.toByteArray()

        pipeContext.pipeManager.freeData()

        val dataset = content.toDataset(pipeContext.mimeType?.asRdfLang() ?: Lang.TRIG)

        content = byteArrayOf()

        val model = ModelFactory.createDefaultModel().add(dataset.defaultModel)

        val defaultShapes = config().getString("defaultPipeShapes", DEFAULT_SHAPES_VERSION)
        val shapeModel = pipeContext.config.getString("shapeModel", defaultShapes)

        val measured = measureTimedValue {
            validateModel(model, shapeModel)
        }
        pipeContext.log().info("Dataset validated ({}, {}): {}", measured.duration.inWholeMilliseconds, shapeModel, pipeContext.dataInfo)
        val report = measured.value

        val resource = model.listSubjectsWithProperty(RDF.type, model.resourceType()).next()

        if (dataset.listMetricsModels().isEmpty()) {
            val uri = "urn:${pipeContext.pipe.header.context?.asNormalized()}:${pipeContext.pipe.header.name}"
            val metricsModel = dataset.createMetricsGraph(uri)
            metricsModel.add(resource, DQV.hasQualityMetadata, metricsModel.getResource(uri))
            metricsModel.add(report)
            val annotation = metricsModel.addAnnotation(resource, OA.describing, report.listSubjectsWithProperty(RDF.type, SHACL.ValidationReport).next())
            annotation.addProperty(DCTerms.isVersionOf, shapeModel)
        } else {
            val metricsModel = dataset.listMetricsModels()[0]
            metricsModel.add(report)
            metricsModel.replaceAnnotation(resource, OA.describing, report.listSubjectsWithProperty(RDF.type, SHACL.ValidationReport).next())
        }
        pipeContext.log().debug("Dataset content: {}", dataset.asString(Lang.TRIG))
        pipeContext.setResult(dataset.asString(Lang.TRIG), RDFMimeTypes.TRIG, pipeContext.dataInfo).forward()
        report.close()
        model.close()
        dataset.close()
    }

}

private fun Model.resourceType(): Resource = when {
    contains(null, RDF.type, DCAT.Catalog) -> DCAT.Catalog
    contains(null, RDF.type, DCAT.Dataset) -> DCAT.Dataset
    contains(null, RDF.type, DCAT.Distribution) -> DCAT.Distribution
    contains(null, RDF.type, DCAT.CatalogRecord) -> DCAT.CatalogRecord
    // fallback
    else -> DCAT.Dataset
}
