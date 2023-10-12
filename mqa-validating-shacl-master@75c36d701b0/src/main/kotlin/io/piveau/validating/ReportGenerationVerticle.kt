package io.piveau.validating

import io.piveau.rdf.asRdfLang
import io.piveau.rdf.asString
import io.piveau.rdf.toModel
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class ReportGenerationVerticle : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExperimentalTime
    override fun start(startPromise: Promise<Void>) {
        vertx.eventBus().consumer(ADDRESS_REPORT, this::handleReport)
        startPromise.complete()
    }

    companion object {
        const val ADDRESS_REPORT: String = "io.piveau.report.validating.shacl.queue"
    }

    @ExperimentalTime
    private fun handleReport(message: Message<JsonObject>) = with (message.body()) {
        val contentType = getString("contentType").asRdfLang()
        val content = getString("content").also { log.debug(it) }

        val acceptableContentType = getString("acceptableContentType")
        val shapeModel = getString("shapeModel", config().getString("defaultShapes", DEFAULT_SHAPES_VERSION))
        try {
            val model = content.toByteArray(StandardCharsets.UTF_8).toModel(contentType)
            val measured = measureTimedValue {
                validateModel(model, shapeModel)
            }
            log.debug("RDF validated against {} in {} milliseconds", shapeModel, measured.duration.inWholeMilliseconds)
            val report = measured.value
            message.reply(report.asString(acceptableContentType.asRdfLang()))
        } catch (t: Throwable) {
            message.fail(400, t.message)
        }
    }

}