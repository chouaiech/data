package io.piveau.metrics

import io.piveau.vocabularies.readTurtleResource
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.apache.jena.rdf.model.ModelFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.io.ByteArrayInputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(VertxExtension::class)
class ScoringTests {

    @Test
//    @Disabled
    fun `Scoring dataset`(vertx: Vertx, testContext: VertxTestContext) {
        vertx.fileSystem().readFileBlocking("piveau-dqv-vocabulary-default-score-values.ttl").also {
            PiveauDQVVocabulary.loadScoreValues(ByteArrayInputStream(it.bytes))
        }

        val dqvModel = ModelFactory.createDefaultModel().readTurtleResource("test-dqv-model.ttl")
        val datasetModel = ModelFactory.createDefaultModel().readTurtleResource("test-dataset-model.ttl")
        val score = scoreMetrics(dqvModel, datasetModel.getResource("https://europeandataportal.eu/set/data/j-imprese-attive"))

        testContext.verify {
            Assertions.assertEquals(405, score)
        }
        testContext.completeNow()
    }

}