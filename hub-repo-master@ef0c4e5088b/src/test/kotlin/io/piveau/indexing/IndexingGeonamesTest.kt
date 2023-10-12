package io.piveau.indexing;

import io.piveau.hub.indexing.indexingCatalogue
import io.piveau.vocabularies.initRemotes
import io.piveau.vocabularies.readTurtleResource
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Resource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Indexing geonames test")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IndexingGeonamesTest {

    private val model: Model = ModelFactory.createDefaultModel().apply {
        readTurtleResource("example_dataset_geonames.ttl")
    }

    @Test
    fun `example indexing dataset for geonames`(vertx: Vertx, testContext: VertxTestContext) {
        initRemotes(vertx)

        val res: Resource = model.getResource("https://piveau.io/set/data/test-dataset")
        indexingCatalogue(res)
            .onSuccess {
                val country = it.getJsonObject("country")
                testContext.verify {
                    Assertions.assertNotNull(country)
                    Assertions.assertEquals("Germany", country.getString("label"))
                    Assertions.assertEquals("DE", country.getString("id"))
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

}
