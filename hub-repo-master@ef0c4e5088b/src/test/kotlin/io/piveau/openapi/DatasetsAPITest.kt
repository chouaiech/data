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
import org.apache.jena.riot.Lang
import org.apache.jena.vocabulary.DCAT
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@DisplayName("Testing Datasets API")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DatasetsAPITest {

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
    }

    @BeforeAll
    fun setUp(vertx: Vertx, testContext: VertxTestContext) {
        client = WebClient.create(vertx)

        val config = JsonObject()
            .put(Constants.ENV_PIVEAU_TRIPLESTORE_CONFIG, MockTripleStore.getDefaultConfig())
            .put(
                Constants.ENV_PIVEAU_HUB_API_KEYS, JsonObject()
                    .put(Defaults.APIKEY, JsonArray().add("*"))
                    .put("noPermissionsApiKey", JsonArray().add("foreign-catalogue"))
            )

        MockTripleStore()
            .loadGraph(testCatalogue.uri, testCatalogue.model)
            .deploy(vertx)
            .compose {
                vertx.deployVerticle(MainVerticle::class.java, DeploymentOptions().setConfig(config))
            }
            .compose {
                client.putAbs("http://localhost:8080/catalogues/${testCatalogue.id}/datasets/origin")
                    .addQueryParam("originalId", testDataset.id)
                    .putHeader("X-API-Key", Defaults.APIKEY)
                    .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
                    .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            }
            .onSuccess {
                client = WebClient.create(vertx)
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "uriRefs",
            "identifiers",
//            "originalIds"
        ]
    )
    @Order(1)
    fun `Test list datasets with value types`(valueType: String, vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs(serviceAddress)
            .addQueryParam("valueType", valueType)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals("application/json", it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    val content = it.body().toJsonArray()
                    assertEquals(1, content.size())
                    log.debug(content.encodePrettily())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "uriRefs",
            "identifiers",
//            "originalIds"
        ]
    )
    @Order(2)
    fun `Test head list datasets with value types`(valueType: String, vertx: Vertx, testContext: VertxTestContext) {
        client.headAbs(serviceAddress)
            .addQueryParam("valueType", valueType)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals("application/json", it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertTrue(it.getHeader(HttpHeaders.CONTENT_LENGTH.toString()).toInt() > 0)
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(3)
    fun `Test list datasets with value type 'metadata'`(
        accept: RDFMimeTypesEnum?,
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD

        val request = client.getAbs(serviceAddress)
            .addQueryParam("valueType", "metadata")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request
            .send()
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

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(4)
    fun `Test head list datasets with value type 'metadata'`(
        accept: RDFMimeTypesEnum?,
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD

        val request = client.headAbs(serviceAddress)
            .addQueryParam("valueType", "metadata")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertTrue(it.getHeader(HttpHeaders.CONTENT_LENGTH.toString()).toInt() > 0)
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @CsvSource(
        "invalidApiKey,401",
        "noPermissionsApiKey,403",
        "${Defaults.APIKEY},204"
    )
    @Order(5)
    fun `Test put dataset`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "keyword")
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", apiKey)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(statusCode.toInt(), it.statusCode())
                    when (it.statusCode()) {
                        204 -> assertNull(it.body())
                        401 -> assertEquals("Unauthorized", it.body().toString())
                        403 -> assertEquals("Forbidden", it.body().toString())
                    }
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(6)
    fun `Test put dataset response 404`(vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "keyword2")
        client.putAbs("$serviceAddress/failure-dataset")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Dataset not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(7)
    fun `Test put dataset response 400`(vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "keyword2")
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(400, it.statusCode())
                    log.debug(it.body().toString())
                    assertEquals("Body required", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(8)
    fun `Test get dataset`(
        accept: RDFMimeTypesEnum?,
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.getAbs("$serviceAddress/${testDataset.id}")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertNotNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(9)
    fun `Test get dataset response 404`(
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        client.getAbs("$serviceAddress/failure-dataset")
            .putHeader(HttpHeaders.ACCEPT.toString(), RDFMimeTypes.NTRIPLES)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Dataset not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(10)
    fun `Test head get dataset`(
        accept: RDFMimeTypesEnum?,
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.headAbs("$serviceAddress/${testDataset.id}")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    assertTrue(it.getHeader(HttpHeaders.CONTENT_LENGTH.toString()).toInt() > 0)
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(11)
    fun `Test head get dataset response 404`(
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        client.headAbs("$serviceAddress/failure-dataset")
            .putHeader(HttpHeaders.ACCEPT.toString(), RDFMimeTypes.NTRIPLES)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @CsvSource(
        "invalidApiKey,401",
        "noPermissionsApiKey,403",
        "${Defaults.APIKEY},204"
    )
    @Order(12)
    fun `Test delete dataset`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", apiKey)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(statusCode.toInt(), it.statusCode())
                    when (statusCode.toInt()) {
                        204 -> assertNull(it.body())
                        401 -> assertEquals("Unauthorized", it.body().toString())
                        403 -> assertEquals("Forbidden", it.body().toString())
                    }
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    fun `Test delete dataset 404 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/failed-distribution")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Dataset not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    companion object {
        const val serviceAddress = "http://localhost:8080/datasets"
    }

}