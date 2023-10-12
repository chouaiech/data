package io.piveau.duplication

import io.piveau.hub.Constants
import io.piveau.hub.Defaults
import io.piveau.hub.MainVerticle
import io.piveau.rdf.RDFMimeTypes
import io.piveau.rdf.presentAs
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
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@DisplayName("Testing identifier duplication")
@ExtendWith(VertxExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class DuplicatesTests {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private lateinit var client: WebClient

    private val testCatalogue1 = catalogue("test-catalogue-1") {
        title { "Test catalogue 1" lang "en" }
        description { "Some description" lang "en" }
        publisher {
            name { "FOKUS" lang "" }
        }
    }

    private val testCatalogue2 = catalogue("test-catalogue-2") {
        title { "Test catalogue 2" lang "en" }
        description { "Some description" lang "en" }
        publisher {
            name { "FOKUS" lang "" }
        }
    }

    private val testCatalogue3 = catalogue("test-catalogue-3") {
        title { "Test catalogue 3" lang "en" }
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
                .put("noPermissionsApiKey", JsonArray().add("foreign-catalogue")))

        MockTripleStore()
            .loadGraph(testCatalogue1.uri, testCatalogue1.model)
            .loadGraph(testCatalogue2.uri, testCatalogue2.model)
            .loadGraph(testCatalogue3.uri, testCatalogue3.model)
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
    fun `Create first test dataset in catalogue 1`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("${serviceAddress}/${testCatalogue1.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
            .sendBuffer(Buffer.buffer(testDataset.model.presentAs(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(201, it.statusCode())
                    Assertions.assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                    log.debug(it.getHeader(HttpHeaders.LOCATION.toString()))
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(2)
    fun `Create second test dataset in catalogue 2`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("${serviceAddress}/${testCatalogue2.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
            .sendBuffer(Buffer.buffer(testDataset.model.presentAs(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(201, it.statusCode())
                    Assertions.assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                    log.debug(it.getHeader(HttpHeaders.LOCATION.toString()))
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(3)
    fun `Create third test dataset in catalogue 3`(vertx: Vertx, testContext: VertxTestContext) {
        client.putAbs("${serviceAddress}/${testCatalogue3.id}/datasets/origin")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), RDFMimeTypes.NTRIPLES)
            .addQueryParam("originalId", testDataset.id)
            .sendBuffer(Buffer.buffer(testDataset.model.presentAs(Lang.NTRIPLES)))
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(201, it.statusCode())
                    Assertions.assertNotNull(it.getHeader(HttpHeaders.LOCATION.toString()))
                    log.debug(it.getHeader(HttpHeaders.LOCATION.toString()))
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(4)
    fun `Translation first test dataset`(vertx: Vertx, testContext: VertxTestContext) {
        val translation = JsonObject()
            .put("id", "https://piveau.io/set/data/test-dataset~~2")
            .put("data", JsonObject()
                .put("catalogueId", "test-catalogue-3")
                .put("defaultLanguage", "en")
                .put("dict", JsonObject()))

        client.postAbs("http://localhost:8080/translation")
            .putHeader("X-API-Key", Defaults.APIKEY)
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/json")
            .sendBuffer(Buffer.buffer(translation.encode()))
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(200, it.statusCode())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(5)
    fun `Get list of test datasets`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("http://localhost:8080/datasets")
//            .putHeader(HttpHeaders.ACCEPT.toString(), RDFMimeTypes.TURTLE)
            .send()
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(200, it.statusCode())
                    log.debug(it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    @Test
    @Order(6)
    fun `Get translation information`(vertx: Vertx, testContext: VertxTestContext) {
        client.getAbs("http://localhost:8080/datasets/test-dataset~~2")
            .putHeader(HttpHeaders.ACCEPT.toString(), RDFMimeTypes.TURTLE)
            .send()
            .onSuccess {
                testContext.verify {
                    Assertions.assertEquals(200, it.statusCode())
                    log.debug(it.body().toString())
                }
                testContext.completeNow()
            }
            .onFailure(testContext::failNow)
    }

    companion object {
        const val serviceAddress = "http://localhost:8080/catalogues"
    }

}