package io.piveau.indexing

import io.piveau.dqv.listMetricsModels
import io.piveau.hub.indexing.indexingCatalogue
import io.piveau.hub.indexing.indexingDataset
import io.piveau.rdf.asString
import io.piveau.utils.JenaUtils.readDataset
import io.piveau.vocabularies.loadResource
import io.piveau.vocabularies.readTurtleResource
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.apache.jena.riot.Lang
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("Indexing test")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexingTest {
    private val log = LoggerFactory.getLogger("IndexingTest")

    private val modelDataset: Model = ModelFactory.createDefaultModel().apply {
        readTurtleResource("example_dataset.ttl")
    }

    private val modelCatalog: Model = ModelFactory.createDefaultModel().apply {
        readTurtleResource("example_catalog.ttl")
    }

    private val modelMetrics: Model = readDataset("example_metric_1.ttl".loadResource(), null).listMetricsModels().first()

    private val modelMetrics2: Model = ModelFactory.createDefaultModel().apply {
        readTurtleResource("test-dataset-1-metrics-graph.ttl")
    }

    @Test
    fun `test publisher homepage and email with example dataset`(vertx: Vertx, testContext: VertxTestContext) {
        val res: Resource = modelDataset.getResource("https://piveau.io/set/data/test-dataset")
        val resRecord: Resource = modelDataset.getResource("https://piveau.io/set/record/test-dataset")
        val obj: JsonObject = indexingDataset(res, resRecord,"test-catalog", "en")

        val publisher: JsonObject = obj.getJsonObject("publisher")

        assertNotNull(publisher)
        assertEquals("http://www.fokus.fraunhofer.de", publisher.getString("homepage"))
        assertEquals("mailto:info@fokus.fraunhofer.de", publisher.getString("email"))

        log.info("Indexing dataset: {}", obj.encodePrettily())

        testContext.completeNow()
    }

    @Test
    fun `test publisher homepage and email with example catalog`(vertx: Vertx, testContext: VertxTestContext) {
        val res: Resource = modelCatalog.getResource("https://piveau.io/id/catalogue/test-catalog")
        indexingCatalogue(res)
            .onSuccess {
                val publisher = it.getJsonObject("publisher")

                testContext.verify {
                    assertNotNull(publisher)
                    assertEquals("http://www.fokus.fraunhofer.de", publisher.getString("homepage"))
                    assertEquals("mailto:info@fokus.fraunhofer.de", publisher.getString("email"))
                }

                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    fun `test metrics indexing`(vertx: Vertx, testContext: VertxTestContext) {

        val obj: JsonObject = io.piveau.hub.indexing.indexingMetrics(modelMetrics)

        assertEquals("test-dataset", obj.getString("id"))
        val qualityMeas: JsonObject = obj.getJsonObject("quality_meas")

        assertNotNull(qualityMeas)
        assertEquals(100, qualityMeas.getInteger("scoring"))

        assertNotNull(qualityMeas.getJsonArray("accessUrlStatusCode"))
        assertEquals(3, qualityMeas.getJsonArray("accessUrlStatusCode").size())

        log.info("Indexing catalogue: {}", obj.encodePrettily())

        testContext.completeNow()
    }

    @Test
    fun `test metrics indexing 2`(vertx: Vertx, testContext: VertxTestContext) {

        val obj: JsonObject = io.piveau.hub.indexing.indexingMetrics(modelMetrics2)

        assertEquals("test-dataset", obj.getString("id"))
        val qualityMeas: JsonObject = obj.getJsonObject("quality_meas")

        assertNotNull(qualityMeas)
        assertEquals(180, qualityMeas.getInteger("scoring"))

        assertNotNull(obj.getJsonArray("quality_ann"))
        assertEquals(10, obj.getJsonArray("quality_ann").size())

        log.info("Indexing catalogue: {}", obj.encodePrettily())

        testContext.completeNow()
    }

}
