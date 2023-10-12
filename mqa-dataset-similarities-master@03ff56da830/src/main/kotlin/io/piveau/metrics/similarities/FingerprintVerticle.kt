package io.piveau.metrics.similarities

import io.piveau.dcatap.DCATAPUriRef
import io.piveau.dcatap.DCATAPUriSchema
import io.piveau.dcatap.TripleStore
import io.piveau.vocabularies.Languages
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.Message
import io.vertx.core.file.CopyOptions
import io.vertx.core.file.OpenOptions
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.apache.jena.query.*
import org.apache.jena.vocabulary.DCTerms
import org.slf4j.LoggerFactory

import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class FingerprintVerticle : CoroutineVerticle() {

    private val log = LoggerFactory.getLogger(javaClass)

    private lateinit var tripleStore: TripleStore

    @Override
    override suspend fun start() {

        val sparqlUrl = config.getString(ApplicationConfig.ENV_SPARQL_URL, ApplicationConfig.DEFAULT_SPARQL_URL)

        tripleStore = TripleStore(vertx, JsonObject().put("address", sparqlUrl))

        vertx.fileSystem().mkdirs(ApplicationConfig.WORK_DIR).await()

        vertx.eventBus().consumer(ADDRESS_START_FINGERPRINT) {
            launch {
                fingerprinting(it)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun fingerprinting(message: Message<String>) {
        log.info("Start fingerprinting all catalogues")
        val list = tripleStore.catalogueManager.listUris().await()

        var counter = 0
        val duration = measureTime {
            list.asFlow()
                .map {
                    val resultSet = tripleStore.select("SELECT ?lang WHERE { <$it> <${DCTerms.language}> ?lang }").await()
                    val lang = if (resultSet.hasNext()) {
                        resultSet.next().getResource("lang").uri
                    } else {
                        "http://publications.europa.eu/resource/authority/language/ENG"
                    }
                    it to lang
                }
                .collect { (uriSchema, lang) ->
                    counter += fingerprintCatalogue(uriSchema, lang)
                }
        }
        log.info("$counter overall fingerprinted in $duration")
    }

    @OptIn(FlowPreview::class)
    private suspend fun fingerprintCatalogue(uriSchema: DCATAPUriRef, lang: String): Int {
        log.info("Fingerprinting catalogue {}", uriSchema.id)

        val targetFilename = "${ApplicationConfig.WORK_DIR}/${uriSchema.id}.fp"
        val tempFilename = "$targetFilename.tmp"

        val asyncFile = vertx.fileSystem().open(tempFilename, OpenOptions().setAppend(true)).await()

        var offset = 0
        val limit = 1000

        var counter = 0

        do {
            val query = if (lang == "http://publications.europa.eu/resource/authority/language/ENG") {
                String.format(QUERY_ENG, uriSchema.catalogueUriRef, offset, limit)
            } else {
                String.format(QUERY, uriSchema.catalogueUriRef, offset, limit)
            }

            offset += limit

            val resultSet = tripleStore.select(query).await()
            if (!resultSet.hasNext()) {
                break
            }

            resultSet.asFlow()
                .map {
                    DatasetFingerprint(
                        it.getResource("dataset").uri,
                        it.getLiteral("title").string,
                        it.getLiteral("description").string
                    )
                }
                .catch {
                    log.error("Dataset fingerprint failure", it)
                    emit(DatasetFingerprint("", "", ""))
                }
                .filter { it.isValid }
                .collect {
                    asyncFile.write(Buffer.buffer(it.generateFileEntry()))
                    counter += 1
                }
        } while(true)

        log.info("$counter datasets fingerprinted")

        asyncFile.close().await()

        val copyOptions = CopyOptions().setAtomicMove(true).setReplaceExisting(true)
        vertx.fileSystem().move(tempFilename, targetFilename, copyOptions).await()

        vertx.eventBus().send(SimilarityVerticle.ADDRESS_INDEX_CATALOGUE, targetFilename)

        return counter
    }

    companion object {
        private const val QUERY: String = """SELECT DISTINCT ?dataset ?title ?description
            WHERE
            {
                ?dataset    <http://purl.org/dc/terms/title> ?title ;
                            <http://purl.org/dc/terms/description> ?description 
                
                FILTER(langMatches(lang(?title), "EN") && langMatches(lang(?description), "EN"))
              
                {
                    SELECT ?dataset
                    WHERE
                    {
                        <%s> <http://www.w3.org/ns/dcat#dataset> ?dataset
                    } OFFSET %d LIMIT %d
                }
            }
            """

        private const val QUERY_ENG: String = """SELECT DISTINCT ?dataset ?title ?description
            WHERE
            {
                ?dataset    <http://purl.org/dc/terms/title> ?title ;
                            <http://purl.org/dc/terms/description> ?description 
                
                FILTER((langMatches(lang(?title), "EN") || lang(?title) = "") && (langMatches(lang(?description), "EN") || lang(?description) = ""))
              
                {
                    SELECT ?dataset
                    WHERE
                    {
                        <%s> <http://www.w3.org/ns/dcat#dataset> ?dataset
                    } OFFSET %d LIMIT %d
                }
            }
            """

        const val ADDRESS_START_FINGERPRINT: String = "io.piveau.event.startFingerprint"
    }

}

private fun ResultSet.asFlow(): Flow<QuerySolution> = flow {
    while (hasNext()) {
        emit(next())
    }
}
