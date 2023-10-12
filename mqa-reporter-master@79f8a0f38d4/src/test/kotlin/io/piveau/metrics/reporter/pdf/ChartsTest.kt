package io.piveau.metrics.reporter.pdf

import io.piveau.metrics.reporter.CatalogueMetrics
import io.piveau.metrics.reporter.model.Translation
import io.piveau.metrics.reporter.model.chart.DoughnutChart
import io.piveau.metrics.reporter.model.chart.BarChart
import io.piveau.metrics.reporter.model.chart.Chart
import io.piveau.metrics.reporter.util.Translator
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class ChartsTest {

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var client: WebClient

    private lateinit var translation: Translation

    @BeforeAll
    fun setUp(vertx: Vertx, testContext: VertxTestContext) {
        client = WebClient.create(vertx)

        translation = Translator.createTranslation(
            vertx.fileSystem().readFileBlocking("i18n/lang.json").toJsonObject(),
            Locale.ENGLISH
        )

        DoughnutChart.template = vertx.fileSystem().readFileBlocking("charts/doughnut.json").toJsonObject()
        BarChart.template = vertx.fileSystem().readFileBlocking("charts/bar.json").toJsonObject()

        testContext.completeNow()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun generateCatalogueCharts(vertx: Vertx, testContext: VertxTestContext) {
        val govdataData = vertx.fileSystem().readFileBlocking("govdata.json").toJsonObject()

        val catalogueMetrics = CatalogueMetrics(govdataData, translation)

        val dimensionCharts = catalogueMetrics.dimensionCharts

        CoroutineScope(vertx.dispatcher() as CoroutineContext).launch {
            measureTime {
                dimensionCharts
                    .forEach {
                        val response = client.postAbs("http://localhost:3400/chart")
                            .expect(ResponsePredicate.SC_OK)
                            .sendJsonObject(it.chart).await()

                        it.imageBytes = response.body().bytes
                    }
            }.also { log.info(it.toString()) }
            testContext.completeNow()
        }
    }

}
