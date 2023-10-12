package io.piveau.metrics

import io.piveau.dqv.listMetricsModels
import io.piveau.pipe.PipeContext
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.asRdfLang
import io.piveau.rdf.asString
import io.piveau.rdf.toDataset
import io.piveau.vocabularies.PiveauScoring
import io.vertx.core.eventbus.Message
import io.vertx.kotlin.core.file.readFileAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.RDF
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import kotlin.coroutines.CoroutineContext

class MetricsScoreVerticle : CoroutineVerticle() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {
        vertx.eventBus().consumer(ADDRESS) {
            launch(vertx.dispatcher() as CoroutineContext) {
                handlePipe(it)
            }
        }

        val buffer = try {
            vertx.fileSystem().readFile("config/piveau-dqv-vocabulary-score-values.ttl").await()
        } catch (t: Throwable) {
            vertx.fileSystem().readFile("piveau-dqv-vocabulary-default-score-values.ttl").await()
        }
        logger.info("Loaded score values from file")
        PiveauDQVVocabulary.loadScoreValues(buffer.bytes.inputStream())
    }

    private suspend fun handlePipe(message: Message<PipeContext>): Unit = with(message.body()) {
        if (config.getBoolean("skip", false)) {
            pass()
            log.debug("Data scoring skipped: {}", dataInfo)
        } else {
            val content = if (pipeManager.isBase64Payload) binaryData else stringData.toByteArray()
            pipeManager.freeData()
            try {
                if (log.isDebugEnabled) {
                    log.debug("Incoming content: {}", content.toString())
                }

                val dataset = content.toDataset(mimeType?.asRdfLang() ?: Lang.TRIG)

                val metricModels = dataset.listMetricsModels()
                if (metricModels.isEmpty()) {
                    pass()
                    log.debug("No metrics, metadata scoring skipped: {}", dataInfo)
                    return@with
                }

                val metricsModel = metricModels.first()
                val resource = dataset.defaultModel.listSubjectsWithProperty(RDF.type, DCAT.Dataset).next()

                val score = scoreMetrics(metricsModel, resource)

                log.info(
                    "Dataset scored as {} ({}): {}",
                    PiveauScoring.getConcept(PiveauScoring.abstract(score))?.label("en") ?: "nothing",
                    score,
                    dataInfo
                )

                val result = dataset.asString(Lang.TRIG)
                if (log.isDebugEnabled) {
                    log.debug("Dataset content: {}", result)
                }
                setResult(result, RDFMimeTypes.TRIG, dataInfo).forward()
                dataset.close()
            } catch (t: Throwable) {
                setFailure(t)
            }
        }
    }

    companion object {
        const val ADDRESS: String = "io.piveau.pipe.metrics.score.queue"
    }

}
