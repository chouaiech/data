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
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.NullSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// TODO
// 1. Create dataset
// 2. Update dateset
// 3. Get dataset
// 4. Delete dataset
// 5. Failure cases

@DisplayName("Testing Catalogue Datasets Origin API")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CatalogueDatasetsOriginAPITest {

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
            title {
                "Test distribution" lang "en"
            }
            addProperty(DCAT.accessURL, ResourceFactory.createResource("http://example.com"))
        }
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
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(1)
    fun `Test create dataset from origin`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
            .sendBuffer(Buffer.buffer(testDataset.model.asString(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    assertEquals(201, it.statusCode())
                    assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(2)
    fun `Test update dataset from origin`(vertx: Vertx, testContext: VertxTestContext) {
        testDataset.addLiteral(DCAT.keyword, "keyword")

        client.putAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
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
    @Order(3)
    fun `Test no update dataset from origin`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
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

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(4)
    fun `Test get dataset from origin`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD

        val request = client.getAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }

        request
            .addQueryParam("originalId", testDataset.id)
            .send()
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

    @ParameterizedTest
    @NullSource
    @EnumSource(RDFMimeTypesEnum::class)
    @Order(5)
    fun `Test head get dataset from origin`(accept: RDFMimeTypesEnum?, vertx: Vertx, testContext: VertxTestContext) {
        val expected = accept ?: RDFMimeTypesEnum.JSONLD

        val request = client.headAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")

        accept?.let { request.putHeader(HttpHeaders.ACCEPT.toString(), it.value) }
        request
            .addQueryParam("originalId", testDataset.id)
            .send()
            .onSuccess {
                testContext.verify {
                    assertEquals(200, it.statusCode())
                    assertEquals(expected.value, it.getHeader(HttpHeaders.CONTENT_TYPE.toString()))
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
    @Order(6)
    fun `Test delete dataset from origin`(apiKey: String, statusCode: String, vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")
            .putHeader("X-API-Key", apiKey)
            .addQueryParam("originalId", testDataset.id)
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
    @Order(7)
    fun `Test delete dataset from origin 404 response`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/failure-catalogue/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("originalId", testDataset.id)
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
    @Order(8)
    fun `Test delete dataset from origin 404 response 2`(vertx: Vertx, testContext: VertxTestContext) {
        client.deleteAbs("$serviceAddress/${testCatalogue.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .addQueryParam("originalId", "failure-dataset")
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
        const val serviceAddress = "http://localhost:8080/catalogues"
    }

}