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
import org.apache.jena.vocabulary.DCTerms
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

@DisplayName("Testing Datasets Legacy API")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DatasetsLegacyAPITest {

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
            .onSuccess {
                client = WebClient.create(vertx)
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @CsvSource(
        "invalidApiKey,401",
        "noPermissionsApiKey,403",
        "${Defaults.APIKEY},201"
    )
    @Order(1)
    fun `Test put (create) dataset legacy`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs(serviceAddress)
            .putHeader("X-API-Key", apiKey)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("id", testDataset.id)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(statusCode.toInt(), it.statusCode())
                    when (statusCode.toInt()) {
                        201 -> {
                            assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                            assertNull(it.body())
                        }
                        401 -> assertEquals("Unauthorized", it.body().toString())
                        403 -> assertEquals("Forbidden", it.body().toString())
                    }
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(2)
    fun `Test put (skip) dataset legacy`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("id", testDataset.id)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(304, it.statusCode())
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(3)
    fun `Test put (update) dataset legacy`(vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "dragonfly")
        client.putAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("id", testDataset.id)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(204, it.statusCode())
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(4)
    fun `Test get record legacy`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.getAbs("$recordsServiceAddress/${testDataset.id}")

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

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(5)
    fun `Test head get record legacy`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.headAbs("$recordsServiceAddress/${testDataset.id}")

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

    @Test
    @Order(6)
    fun `Test get record legacy 404 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("$recordsServiceAddress/failure-dataset")
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
    @ValueSource(
        booleans = [
            true,
            false
        ]
    )
    @Order(7)
    fun `Test list datasets legacy`(sourceIds: Boolean, vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs(serviceAddress)
            .addQueryParam("catalogue", testCatalogue.id)
            .addQueryParam("sourceIds", sourceIds.toString())
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals("application/json", it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
                    val content = it.body().toJsonArray()
                    assertTrue(content.size() >= 1)
                    log.debug(content.encodePrettily())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(8)
    fun `Test list datasets legacy response 404`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs(serviceAddress)
            .addQueryParam("catalogue", "failure-catalogue")
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @ValueSource(
        booleans = [
            true,
            false
        ]
    )
    @Order(9)
    fun `Test head list datasets legacy`(sourceIds: Boolean, vertx: Vertx, testContext: VertxTestContext) {
        client.headAbs(DatasetsAPITest.serviceAddress)
            .addQueryParam("catalogue", testCatalogue.id)
            .addQueryParam("sourceIds", sourceIds.toString())
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

    @Test
    @Order(10)
    fun `Test head list datasets legacy response 404`(vertx: Vertx, testContext: VertxTestContext) {
        client.headAbs(serviceAddress)
            .addQueryParam("catalogue", "failure-catalogue")
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
        "${Defaults.APIKEY},201"
    )
    @Order(11)
    fun `Test add dataset legacy to catalogue`(
        apiKey: String,
        statusCode: String,
        vertx: Vertx,
        testContext: VertxTestContext
    ) {
        client.postAbs(serviceAddress)
            .putHeader("X-API-Key", apiKey)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(statusCode.toInt(), it.statusCode())
                    when (it.statusCode()) {
                        201 -> {
                            assertNull(it.body())
                            assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                            log.debug(it.getHeader(HttpHeaders.LOCATION.toString()))
                        }

                        401 -> assertEquals("Unauthorized", it.body().toString())
                        403 -> assertEquals("Forbidden", it.body().toString())
                    }
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(12)
    fun `Test add dataset legacy to catalogue 400 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.postAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", testCatalogue.id)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(400, it.statusCode())
                    assertEquals("Body required", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(13)
    fun `Test add dataset to catalogue 404 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.postAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", "failure-catalogue")
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(14)
    fun `Test delete dataset legacy 404 response (catalogue)`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("id", testDataset.id)
            .addQueryParam("catalogue", "failure-catalogue")
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(15)
    fun `Test delete dataset legacy 404 response (dataset)`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("id", "failure-dataset")
            .addQueryParam("catalogue", testCatalogue.id)
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

    @Test
    @Order(16)
    fun `Test delete dataset legacy 400 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs(serviceAddress)
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("id", testDataset.id)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(400, it.statusCode())
                    val body = it.body().toJsonObject()
                    assertEquals("catalogue", body.getString("parameterName"))
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
    @Order(17)
    fun `Test delete dataset legacy`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs(serviceAddress)
            .putHeader("X-API-Key", apiKey)
            .addQueryParam("id", testDataset.id)
            .addQueryParam("catalogue", testCatalogue.id)
            .send()
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
    @Order(18)
    fun `Test put (create) dataset legacy (path)`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    it.body()?.let { body -> log.debug(body.toString()) }
                    assertEquals(201, it.statusCode())
                    assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(19)
    fun `Test put (update) dataset legacy (path)`(vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "firefly")
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(204, it.statusCode())
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(20)
    fun `Test put (skip) dataset legacy (path)`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", testCatalogue.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(304, it.statusCode())
                    assertNull(it.body())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(21)
    fun `Test put dataset legacy (path) 404 response (catalogue)`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("catalogue", "failure-catalogue")
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(22)
    fun `Test get dataset legacy`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.getAbs("$serviceAddress/${testDataset.id}")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.addQueryParam("catalogue", testCatalogue.id)
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

    @Test
    @Order(23)
    fun `Test get dataset legacy 404 response (catalogue)`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("$serviceAddress/${testDataset.id}")
            .addQueryParam("catalogue", "failure-catalogue")
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(24)
    fun `Test get dataset legacy 404 response (dataset)`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("$serviceAddress/failure-dataset")
            .addQueryParam("catalogue", testCatalogue.id)
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
    @Order(25)
    fun `Test head get dataset legacy`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD
        val request = client.headAbs("$serviceAddress/${testDataset.id}")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request.addQueryParam("catalogue", testCatalogue.id)
            .send()
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

    @Test
    @Order(26)
    fun `Test head get dataset legacy 404 response (catalogue)`(vertx: Vertx, testContext: VertxTestContext) {
        client.headAbs("$serviceAddress/${testDataset.id}")
            .addQueryParam("catalogue", "failure-catalogue")
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

    @Test
    @Order(27)
    fun `Test head get dataset legacy 404 response (dataset)`(vertx: Vertx, testContext: VertxTestContext) {
        client.headAbs("$serviceAddress/failure-dataset")
            .addQueryParam("catalogue", testCatalogue.id)
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

    @Test
    @Order(28)
    fun `Test delete dataset legacy (path) 404 response (catalogue)`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("catalogue", "failure-catalogue")
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(404, it.statusCode())
                    assertEquals("Catalogue not found", it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(29)
    fun `Test delete dataset legacy (path) 404 response (dataset)`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/failure-dataset")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("catalogue", testCatalogue.id)
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
    @CsvSource(
        "invalidApiKey,401",
        "noPermissionsApiKey,403",
        "${Defaults.APIKEY},204"
    )
    @Order(30)
    fun `Test delete dataset legacy (path)`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/${testDataset.id}")
            .putHeader("X-API-Key", apiKey)
            .addQueryParam("catalogue", testCatalogue.id)
            .send()
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

    companion object {
        const val recordsServiceAddress = "http://localhost:8080/records"
        const val serviceAddress = "http://localhost:8080/datasets"
    }

}