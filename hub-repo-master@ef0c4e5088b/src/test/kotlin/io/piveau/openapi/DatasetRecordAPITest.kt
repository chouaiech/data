package io.piveau.openapi

import io.piveau.RDFMimeTypesEnum
import io.piveau.hub.Constants
import io.piveau.hub.Defaults
import io.piveau.hub.MainVerticle
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.asString
import io.piveau.test.MockTripleStore
import io.piveau.utils.dcatap.dsl.catalogue
import io.piveau.utils.dcatap.dsl.dataset
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.apache.jena.rdf.model.ResourceFactory
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.apache.jena.vocabulary.DCTerms
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@DisplayName("Testing Dataset Record API")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DatasetRecordAPITest {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private lateinit var client: WebClient

    private val testCatalogue = catalogue("test-catalogue") {
        title { "Test catalogue" lang "en" }
        description { "Some description" lang "en" }
        publisher {
            name { "FOKUS" lang "" }
        }
    }

    private val testDataset = dataset("test-dataset") {
        title { "Test Dataset" lang "en" }
        description { "Some description" lang "en" }
        distribution("test-distribution") {
            title { "Test distribution" lang "en" }
            addProperty(DCAT.accessURL, ResourceFactory.createResource("http://example.com"))
        }
    }

    @BeforeAll
    fun setUp(vertx: Vertx, testContext: VertxTestContext) {
        client = WebClient.create(vertx)

        val config = JsonObject()
            .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig())

        MockTripleStore()
            .loadGraph(testCatalogue.uri, testCatalogue.model)
            .deploy(vertx)
            .compose {
                vertx.deployVerticle(MainVerticle::class.java, DeploymentOptions().setConfig(config))
            }
            .compose {
                client.putAbs("http://localhost:8080/catalogues/test-catalogue/datasets/origin")
                    .addQueryParam("originalId", "test-dataset")
                    .putHeader("X-API-Key", Defaults.APIKEY)
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
                    .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            }
            .onSuccess {
                when (it.statusCode()) {
                    201 -> testContext.completeNow()
                    else -> testContext.failNow(it.statusMessage())
                }
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    fun `Test get record`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.getAbs("$serviceAddress/${testDataset.id}/record")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertNotNull(it.body())
                    assertEquals(it.getHeader(HttpHeaders.CONTENT_LENGTH.toString()).toInt(), it.body().bytes.size)
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    fun `Test get record 404 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("$serviceAddress/failure-dataset/record")
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue record not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    fun `Test head record`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.headAbs("$serviceAddress/${testDataset.id}/record")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertNull(it.body())
                    assertTrue(it.getHeader(HttpHeaders.CONTENT_LENGTH.toString()).toInt() > 0)
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    companion object {
        const val serviceAddress = "http://localhost:8080/datasets"
    }

}