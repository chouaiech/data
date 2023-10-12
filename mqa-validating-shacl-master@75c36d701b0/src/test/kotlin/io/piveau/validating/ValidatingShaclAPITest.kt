package io.piveau.validating

import io.piveau.MainVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(VertxExtension::class)
class ValidatingShaclAPITest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @BeforeAll
    fun setup(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(
            MainVerticle::class.java,
            DeploymentOptions()
        )
            .onSuccess { testContext.completeNow() }
            .onFailure(testContext::failNow)
    }

    @Test
    fun `Test validating API`(vertx: Vertx, testContext: VertxTestContext) {
        val buffer = vertx.fileSystem().readFileBlocking("test.ttl")
        val client: WebClient = WebClient.create(vertx)
        client.postAbs("http://localhost:8080/shacl/validation/report")
            .putHeader("Content-Type", "text/turtle")
            .addQueryParam("shapeModel", "dcatap211level2")
            .expect(ResponsePredicate.SC_SUCCESS)
            .sendBuffer(buffer)
            .onSuccess {
                log.debug("${it.statusCode()} - ${it.statusMessage()}\n${it.bodyAsString() ?: "no body"}")
                testContext.completeNow()
            }
            .onFailure { testContext.failNow(it) }
    }
}
