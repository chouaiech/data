# ChangeLog

# Unreleased

## 3.0.10 (2023-05-12)

**Changed:**
* Alignment between installed and internal use of vocabularies

## 3.0.9 (2023-05-11)

**Added:**
* English label for Stadt Jena to local corporate body vocabulary (temporarily)

## 3.0.8 (2023-05-10)

**Added:**
* Stadt Jena to local corporate body vocabulary (temporarily)

## 3.0.7 (2023-05-09)

**Fixed:**
* DCAT-AP.de vocabulary ids

## 3.0.6 (2023-05-08)

**Added:**
* DCAT-AP.de vocabularies to install command
* Tags for install vocabularies command

## 3.0.5 (2023-05-04)

**Changed:**
* Vocabularies catalogue visibility `hidden`

## 3.0.4 (2023-05-03)

**Fixed:**
* Vocabulary contributors filename

## 3.0.3 (2023-05-02)

**Added:**
* Integrated DCAT-AP.de contributors vocabulary

## 3.0.2 (2023-04-14)

**Added:**
* `installVocabularies` can now install from remote
* `installVocabularies` merges piveau extensions

**Changed:**
* Helm chart liveness probe set health check timeout to 3 seconds 
* Re-indexing vocabularies performance improvements

## 3.0.1 (2023-04-14)

**Removed:**
* Configurable install vocabularies routine for startup

**Added:**
* Shell command for installing vocabularies 

## 3.0.0 (2023-03-29)

**Added:**
* Configurable install vocabularies routine for startup 

## 2.2.5 (2023-03-23)

**Changed:**
* `piveau-utils` version. KeyCloak URL now requires additional /auth path for older KeyCloak version

## 2.2.4 (2023-03-23)

**Removed:**
* `PIVEAU_HUB_HISTORIC_METRICS` configuration. Moved it to `PIVEAU_HUB_VALIDATOR.history`

## 2.2.3 (2023-03-09)

**Fixed:**
* Dataset id output in `index` CLI command 
* Now really, hash query in `puDatasetOrigin`

## 2.2.2 (2023-03-08)

**Fixed:**
* Hash query in `puDatasetOrigin`
* Index CLI command

## 2.2.1 (2023-03-05)

**Added:**
* Paging for `removeDups` and `repair` cli command

## 2.2.0 (2023-02-28)

**Added:**
* dcat:endpointDescription, dct:accessRights and dct:license to access service indexing

## 2.1.1 (2023-02-24)

**Fixed:**
* Improved DOI endpoint
* Fix in Distribution API
* Fix in Get Metrics API

## 2.1.0 (2023-02-12)

**Added:**
* Final error handler for 304 service exceptions
* Dataset lists can now be returned with full metadata

**Fixed:**
* Remove duplicates CLI command deleting everything

## 2.0.0 (2023-02-01)

**Fixed:**
* CORS regex pattern
* Add dataset to catalogue no more requires lang and type in catalogue metadata
* Content negotiation without id

**Added:**
* Check for empty debug info in `ServiceException` 
* N-Quads in content negotiation
* Complete OpenAPI tests

**Changed:**
* Default API return type set to JSON-LD 
* Implementation of `ContentNegotiation`
* Renewal of API specification

## 1.23.4 (2023-01-02)

**Fixed:**
* CORS handler initialization

## 1.23.3 (2023-01-02)

**Changed:**
* Read build info once on startup for faster health check

## 1.23.2 (2022-12-20)

**Changed:**
* Using the one safe `failureResponse` method everywhere

## 1.23.1 (2022-11-21)

**Added:**
* Extended zombies CLI command to remove datasets with '~*' identifiers

## 1.23.0 (2022-11-21)

**Changed:**
* First improvements of API and the OpenAPI description
* General refactoring started...
* Return 403 when not authorized, because `401 Not Authorized` actually means not authenticated

**Added:**
* Support for several API keys associated to a list of resources via `PIVEAU_HUB_REPO_API_KEYS`
* A request handler `CatalogueIdentifierHandler` for injecting the catalogue for permission evaluation
* Specific handler for RDF parsing in `putMetrics`
* `data` query parameter with default value `false` to `putDatasetQuery` operation
* A proper health check
* CLI index command now travers also sub catalogues
* CLI remove duplicates command can now handle all catalogues at once
* CLI repair catalogue can now handle all catalogues at once
* CLI command for fixing corrupted publishers

**Fixed:**
* CLI launch command does not find system pipes
* Permission classes check for API key or JWT token
* Wrong API key header in translation request

## 1.22.1 (2022-10-15)

**Fixed:**
* Problem when a distribution contains a non-literal dct:identifier

## 1.22.0 (2022-10-12)

**Changed:**
* Move default values into code
* `conf/config.json` load after env (vertx default)
* Search service default is disabled

## 1.21.5 (2022-10-07)

**Added:**
* Exception handling for serialising metrics graphs

## 1.21.4 (2022-10-07)

**Fixed:**
* Revert config retriever sources

## 1.21.3 (2022-10-07)

**Fixed:**
* Processing of special service exceptions with invalid http response codes

## 1.21.2 (2022-10-07)

**Added:**
* Some config default values (e.g port, prefetch, remotes)

**Changed:**
* `indexingCatalogue` to asynchronous mode
* Startup procedure
* Concept schemes prefetching wrapped in `executeBlocking`
* Vocabularies remote default to false
* Vocabularies prefetch default to true

**Removed:**
* Config filter for specific ENV variables
* Explicit config store for config.json as it is read by default

## 1.21.1 (2022-09-15)

**Added:**
* API key header `X-API-Key` 

**Changed:**
* Multilingual subject indexing

**Removed:**
* `validating-shacl` segment config for metrics pipe

## 1.21.0 (2022-07-27)

**Added:**
* New `zombies` CLI command

**Fixed:**
* `fixChecksums` command also detecting checksum property, not only checksumValue

## 1.20.6 (2022-07-19)

**Fixed:**
* Delete index with `*` in identifier 
* Proper triple store 503 handling (updated piveau-utils)

## 1.20.5 (2022-07-17)

**Fixed:**
* Index CLI command handling bulk request failure

## 1.20.4 (2022-07-14)

**Changed:**
* Index CLI command sorts catalogues

**Added:**
* Offset and limit for catalogues in index CLI command 

## 1.20.3 (2022-07-11)

**Fixed:**
* Index CLI command exceptions when delete from index handled 

## 1.20.2 (2022-07-10)

**Fixed:**
* Pagination for CLI command `fixChecksums`
* Exception handling for CLI command `fixChecksums`

## 1.20.1 (2022-07-10)

**Added:**
* `pageSize` argument for CLI command `fixChecksum` 
* Verbose flag for CLI command `fixChecksum`

## 1.20.0 (2022-07-08)

**Added:**
* `fixChecksums` CLI command
* Many CLI commands now working with sub-catalogues
* putDataset recognizes old and new SPDX namespace

**Fixed:**
* SPDX namespace corrected in test files

## 1.19.6 (2022-07-03)

**Changed:**
## 1.20.3 (2022-07-11)

**Fixed:**
* Index CLI command exceptions when delete from index handled 

## 1.20.2 (2022-07-10)

**Fixed:**
* Pagination for CLI command `fixChecksums`
* Exception handling for CLI command `fixChecksums`

## 1.20.1 (2022-07-10)

**Added:**
* `pageSize` argument for CLI command `fixChecksum` 
* Verbose flag for CLI command `fixChecksum`

## 1.20.0 (2022-07-08)

**Added:**
* `fixChecksum` CLI command
* Many CLI commands now working with sub-catalogues
* putDataset recognizes old and new SPDX namespace

**Fixed:**
* SPDX namespace corrected in test files

## 1.19.6 (2022-07-03)

**Changed:**
* `removeDups` deletes all datasets with missing identifier

**Fixed:**
* Correct usage of `DCATAPUriSchema`

## 1.19.5 (2022-06-08)

**Added:**
* Remove inconsistency option for index command, removes obsolete indexed datasets and adds missing datasets

## 1.19.4 (2022-06-07)

**Changed:**
* Increase index client pool
* Set timeout to all index requests

## 1.19.3 (2022-06-07)

**Added:**
* Fresh index flag for index command, skips deletion of obsolete datasets 

## 1.19.2 (2022-06-03)

**Changed:**
* Use `~~` instead of `~*`. The `*` is not easy processable in URL context path (e.g. in path params)

## 1.19.1 (2022-06-02)

**Fixed:**
* Test for empty model in index command

**Changed:**
* Use `createFor` instead of `applyFor` when working on catalogue ids, prevent normalization

## 1.19.0 (2022-05-19)

**Added:**
* Test for validating OpenAPI specification

**Changed:**

**Changed:**
* Use `~~` instead of `~*`. The `*` is not easy processable in URL context path (e.g. in path params)

## 1.19.1 (2022-06-02)

**Fixed:**
* Test for empty model in index command

**Changed:**
* Use `createFor` instead of `applyFor` when working on catalogue ids, prevent normalization

## 1.19.0 (2022-05-19)

**Added:**
* Test for validating OpenAPI specification

**Changed:**
* Distribution API
* Update all authority vocabularies
* Set ignoring expiration of JWT tokens to false

**Fixed:**
* Repair CLI command removing non-existing graphs

## 1.18.0 (2022-04-22)

**Fixed:**
* Correct hash value storing during update

**Added:**
* Removing duplicates during harvesting

## 1.17.0 (2022-04-01)

**Fixed:**
* Issue #2032: Normalize id even when uriRef is already base uri

**Added:**
* Set prefix of returned model for catalogue, distribution and metrics

**Changed:**
* Use setNsPrefixesFiltered of piveau-utils (sets only necessary prefixes)

## 1.16.1 (2022-03-09)

**Added:**
* Keycloak sync groups command
* Introducing Identifier Service

**Fixed:**
* URL encode single "*" character in dataset ids, instead of using `URLEncoder`
* Record update when already provided in new model

**Removed:**
* Elasticsearch dependency

## 1.16.0 (2022-02-20)

**Changed:**
* Restructuring index command
* URL encode dataset ids in path parameters due to possible '*' character

## 1.15.4 (2022-02-20)

**Added:**
* Extended function for coroutine scoped command process handler

**Removed:**
* Verbose flag in some CLI commands

## 1.15.3 (2022-02-19)

**Removed:**
* Unnecessary distinct predicates in some queries

## 1.15.2 (2022-02-19)

**Fixed:**
* CLI repair command

## 1.15.1 (2022-02-19)

**Changed:**
* Coroutine scope handling in CLI commands

**Fixed:**
* CLI repair command
* CLI remove duplicates command

## 1.15.0 (2022-02-18)

**Changed:**
* Update indexservice according to hub-search:3.0.0 (no success: true/false in the response)

## 1.14.1 (2022-02-15)

**Changed:**
* Apply translations with removing old ones

**Fixed:**
* CLI translation command for dataset 

## 1.14.0 (2022-02-09)

**Added:**
* Indexing URIRef of dataset
* Indexing metadata extensions
* Option for forcing new translation

**Changed:**
* Consequently avoid blank strings in index

## 1.13.0 (2022-01-27)

**Added:** 
* Keycloak service implementation for:
* adding group, resource and policy/permission when a catalog is added
* remove before mentioned when a catalog is added
* Draft / private Dataset feature

## 1.12.0 (2022-01-27)

**Added:**
* Translation shell command

## 1.11.2 (2022-01-26)

**Added**
* Server in OpenApi to fix API call examples, see https://github.com/Redocly/redoc/issues/1172 

**Fixed:**
* Do not normalize id when init dataset helper

## 1.11.1 (2022-01-16)

**Fixed:**
* Filter keep only literals for translation
* Filter drop empty strings for translation

## 1.11.0 (2022-01-12)

**Changed:**
* Translation implementation

## 1.10.1 (2021-12-14)

**Changed:**
* Skip vocabularies for indexing which are empty after index transformation 
* Use released piveau-utils 8.3.1

## 1.10.0 (2021-12-08)

**Added:**
* Search service indexing of vocabularies after update via `VocabularyHelper.kt`

**Changed:**
* Usage of VocabularyService in vocabulary cli command
* Change response status of HEAD back to 200

**Fixed:**
* Content negotiation

## 1.9.2 (2021-12-02)

**Added:**
* Indexing of dcat:accessService: title, description, endpointURL
* HEAD method to all RDF serving endpoints

**Changed:**
* Response of HEAD to 204
* Content-Length in HEAD to bytes

## 1.9.1 (2021-11-19)

**Changed:**
* Use `RouterBuilder`
* Security schemes in openapi definition

**Added:**
* Add api key handler and provider
* Permission handler for datasets and catalogues
* KeyCloak service for creating and deleting resources
* Remove catalogue from index when deleted

**Removed:**
* APiKey classes, now in piveau-utils

## 1.9.0 (2021-10-27)

**Fixed:**
* Index service reply values

**Added:**
* Option for forcing updates

**Changed:**
* Utils dependency for geo datatype fix and corporate bodies update

## 1.8.3 (2021-10-22)

**Fixed:**
* Missing catalogue record type

**Added:**
* CLI command to re-type catalogue records
* Index vcard:hasURL

## 1.8.2 (2021-10-22)

**Added:**
* StatDCAT-AP support
* Proper CatalogueToIndex tests

## 1.8.1 (2021-10-13)

**Changed:**
* Write vocabulary chunk-wise application-wiese

## 1.8.0 (2021-10-12)

**Added:**
* Property for mark piveau record (`dct:creator`)
* Extended DCAT-AP schema configuration
* Warning when original identifier exists multiple times 

**Fixed:**
* Correct first hash value 
* CLI command for removing duplicates now checks also for blank identifiers

**Changed:**
* Remove foreign catalog records completely
* Use event loop instead of worker verticles
* Update catalogue chunks only the dataset list
* Duplicate appendix separator to `~*`
* Query hash value optionally
* Increased number of verticle instances and thread pools

## 1.7.12 (2021-10-04)

**Fixed:**
* Indexing temporal correctly

**Added:**
* Indexing temporal with schema properties

## 1.7.11 (2021-10-04)

**Fixed:**
* Index comand filter needs to use `id.raw` 

## 1.7.11 (2021-09-23)

**Changed:**
* Use `~*` instead of `_` as duplicate number separator
* Extend indexing according to DCAT-AP 2.0.1 schema
* Use Elasticsearch 7.10.2 in docker-compose
* Added missing vocabularies to vocabulary index command

## 1.7.10 (2021-09-17)

**Changed:**
* Update piveau-utils with new geo clearing method

## 1.7.9 (2021-09-01)

Revert to 1.7.6 status... Bug hunting finished

## 1.7.8 (2021-08-30)

**Added:**
* Even more debug output

## 1.7.7 (2021-08-30)

**Added:**
* More debug output

## 1.7.6 (2021-08-25)

**Added:**
* Wildcard for clearGeoDataCatalogues configuration

## 1.7.5 (2021-08-17)

**Changed:**
* Workaround: read big vocabularies without xml declaration
* Workaround: For big vocabularies use no xml declaration

## 1.7.4 (2021-08-17)

**Changed:**
* Workaround: read big vocabularies with format RDFXML_PLAIN
* Workaround: For big vocabularies use format RDFXML_PLAIN

## 1.7.3 (2021-08-17)

**Changed:**
* Use dataset_write alias for indexing

## 1.7.2 (2021-08-13)

**Added:**
* Indexing page title, description and format
* Handling for hidden catalogues 
* Usage of catalog "vocabularies" and vocabulary metadata datasets for vocabularies endpoint
* Added catalogRecord issued and modified to the search index

**Changed:**
* Use unicode normalization for indexing ids

## 1.7.1 (2021-07-06)

**Removed:**
* Indexing page title, description and format (temporarily)

## 1.7.0 (2021-07-05)

**Added:**
* Configuration of catalogues for clearing geo data before send to triple store

**Removed:**
* Any geo-data conversion before storing in triple store, previously introduced since version 1.6.5

## 1.6.7 (2021-07-04)

**Fixed:**
* Geo json conversion fixed

## 1.6.6 (2021-07-04)

**Changed:**
* Lib updates

## 1.6.5 (2021-07-04)

**Changed:**
* Convert geo json to wkt before send to triple store

## 1.6.4 (2021-06-27)

**Fixed:**
* Check for non uriRef catalog record
* Only get catalogue record with checksum

## 1.6.3 (2021-06-27)

**Changed:**
* Hash processing combined with record check and repair

## 1.6.2 (2021-06-27)

**Fixed:**
* No creation when hash failures

## 1.6.1 (2021-06-17)

**Fixed:**
* Delete metrics history always from shadow system when available

## 1.6.0 (2021-06-15)

**Added:**
* Introduced transfer history command
* Shadow triple store support for metrics history

## 1.5.10 (2021-06-11)

**Changed:**
* Accept distributions without any identification

## 1.5.9 (2021-06-06)

**Fixed:**
* Exception handling of triple store

## 1.5.8 (2021-06-03)

**Added:**
* Reference to metrics graph in dataset record
* Vocabularies api, service, handler
* Cache for catalogue info
* Verbose output flag for repairing duplicates
* Option to add XML declaration to RDF serializations
* Indexing keyword language

**Fixed:**
* Index all command
* Hash value query
* Delete dataset with normalized id
* Indexing temporal
* Handle hash exceptions

## 1.5.7 (2021-03-09)

**Changed:**
* RDF as default/fallback content-type in ContentNegotiation
* application/n-triples as content-type for n-triples in ContentNegotiation

## 1.5.6 (2021-03-05)

**Changed:**
* ReDoc reference to next 2.x

**Added:**
* Supporting configurable favicon and logo

## 1.5.5 (2021-02-26)

**Changed:**
* Improved catalogue clearing command

**Fixed:**
* Delete query in repair command

## 1.5.4 (2021-02-26)

**Fixed:**
* Completing fix for repair command error handling

## 1.5.3 (2021-02-26)

**Fixed:**
* Repair command error handling

## 1.5.2 (2021-02-18)

**Added:**
* Configurable logo and favicon
* Logo and favicon for piveau and edp2

**Changed:**
* Eventbus timeout increased to 2 min

**Fixed:**
* Fix algorithm checking for free id

## 1.5.1 (2021-02-03)

**Fixed:**
* Start shell service

## 1.5.0 (2021-02-02)

**Changed:**
* API get record method based on piveau id
* Switched to Vert.x 4.0.0

**Fixed:**
* Finding free id
* Delete dataset
* List datasets
* Put dataset
* Get record
* Get metrics
* Get distribution
* ...

## 1.4.11 (2021-01-04)

**Fixed:**
* Enabled DatasetIndexing test

**Added:**
* Added support for address and telephone in VCARD

## 1.4.10 (2021-01-04)

**Fixed:**
* Exception handling for index shell command

## 1.4.9 (2021-01-04)

**Fixed:**
* Bug in `DatasetHelper` json serialization
* Update old dataset helper from model only

## 1.4.8 (2021-01-04)

**Changed:**
* Complete redesigned sync index shell command

## 1.4.7 (2020-12-23)

**Changed:**
* Improve sync and score shell commands


## 1.4.6 (2020-12-18)

**Added:**
* Missing artifacts `CHANGELOG.md` and `LICENSE.md`
* New shell command `repairDups`
* Completion for `clear` and `repair` shell command

**Changed:**
* OpenAPI including 204 responses
* Refactored `deleteDataset()`
* API versioning alignment
* Add Support for normalizedId for dataset DELETE

## 1.4.5 (2020-11-17)

Era before changelog
