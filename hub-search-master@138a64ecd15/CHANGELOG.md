# ChangeLog

## Unreleased

**Added:**
* Generic resource endpoint based on shacl shapes
* Openapi generation based on shacl shapes

**Changed:**
* New SearchClient interface for mudolar search backends
* Vocabulary replacement, replace all occurences of the field, includes/excludes parameters

## 4.2.0 (2023-03-16)

**Added:**
* Wildcard search, using `?` to replace a single character, and `*` to replace zero or more characters.
* Exact phrase search, using double quotes: `"This exact phrase in exactly the same order"`.

## 4.1.1 (2023-03-06)

**Changed:**
* non-blocking listIds function
* increase search service proxy timeout to 500000ms

## 4.1.0 (2023-02-28)

**Added:**
* `dcat:endpointDescription`, `dct:accessRights` and `dct:license` to access service in dataset mapping
* `alt_label` in vocabulary mapping
* `listCommands` command for cli
* `exists` query parameter for returning documents which have certain fields existent

**Changed:**
* Refactored shell service
* Camelcase for all cli commands

## 4.0.10 (2023-01-18)

**Changed:**
* Use catalog_record.modified for lastmod in sitemap
* Non-blocking sitemap generation

## 4.0.9 (2022-11-28)

**Changed:**
* Skip all future dates for lastmod in sitemap

## 4.0.8 (2022-11-17)

**Changed:**
* Default aggregation size to 250

## 4.0.7 (2022-11-10)

**Added:**
* Lastmod in sitemap
* Term query on a arbitrary field

## 4.0.6 (2022-09-17)

**Fixed:**
* Modify dataset bug (hardcoded govdata catalog)

## 4.0.5 (2022-09-15)

**Changed:**
* Multilingual mapping for categories and subject, vocabulary resolution on index time 
* Id hashing to allow long ids

## 4.0.4 (2022-07-07)

**Fixed:**
* Dataset revision rollover mapping/settings

## 4.0.3 (2022-05-02)

**Fixed:**
* Feed, use first element in access_url array

## 4.0.2 (2022-04-26)

**Added:**
* Create index command can specify number of shards

**Changed:**
* Disabled catalog update by query

**Fixed:**
* Geo query

## 4.0.1 (2022-04-13)

**Changed:**
* Use scroll for listIds

## 4.0.0 (2022-04-12)

**Changed:**
* Use elastic restclient 7.17.2 
* Use breaker for every request
* Fixed bulk request, remove bulk post request

## 3.0.4 (2022-04-04)

**Changed:**
* Increase elastic socket timeout to 60s and connection timeout to 5s

## 3.0.3 (2022-04-04)

**Changed:**
* Show date and time for logging
* Do not print payload for index failure

## 3.0.2 (2022-02-22)

**Added**
* List dataset and catalogue ids endpoint

## 3.0.1 (2022-02-21)

**Changed**
* Increase minimum and maximum of edge gram tokenizer for autocomplete

## 3.0.0 (2022-02-18)

**Changed**
* Use ServiceException class
* Increase eventbus timeouts
* Don't update dataset during PUT/PATCH in case of an elasticsearch failure

## 2.0.16 (2022-02-11)

**Changed**
* Reduce logging

## 2.0.15 (2022-02-09)

**Added**
* List vocabularies endpoint
* Mapping update for dataset resourece (UriRef) and extended metadata 

## 2.0.14 (2022-01-26)

**Added**
* Server in OpenApi to fix API call examples, see https://github.com/Redocly/redoc/issues/1172 

## 2.0.13 (2021-12-15)

**Changed**
* Update log4j-to-slf4j to 2.16 due to log4jshell bug

## 2.0.12 (2021-12-10)

**Changed:**
* Autocomplete feature is now available via query parameter `autocomplete`
* Return first error code of bulk vocabulary indexing

**Added:**
* `in_scheme` to vocabulary mapping

## 2.0.11 (2021-12-03)

**Changed:**
* Same behavior with query parameter `dataServices=false` and not set

## 2.0.10 (2021-12-02)

**Added:**
* Update mapping of dcat:accessService: title, description, endpointURL

## 2.0.9 (2021-11-25)

**Added:**
* First draft of vocabulary search

**Changed:**
* More generic term aggregation facet, e.g. publisher

## 2.0.8 (2021-11-03)
**Fixed:**
* feed item date now based on catalogue record data

## 2.0.7 (2021-10-28)
**Fixed:**
* link in RSS feed to dataset version

## 2.0.6 (2021-10-28)
**Added**
* endpoint for each dataset version

## 2.0.5 (2021-10-25)

**Added**
* single dataset history
* rss feed for single dataset history

## 2.0.4 (2021-10-22)

**Added:**
* contact_point.url

## 2.0.3 (2021-10-21)

**Added:**
* StatDCAT-AP support

## 2.0.2 (2021-10-14)

**Changed:**
* Ignore language with missing ID for payload processing

## 2.0.1 (2021-10-13)

**Changed:**
* Allow write alias for max result window command

## 2.0.0 (2021-10-12)

**Changed:**
* Extended schema to DCAT AP 2.0.1
* Changed scheme to use label instead of title for all non multi-language fields  

## 1.3.0

**Added:**

* Added catalogRecord issued and modified to the search index
* New mapping for foaf:page
* Reindex process via alias

## 1.2.13

**Fixed:**

* Boosting and scoring

**Changed:**

* Gazetteer config is not required anymore

**Added:**

* showScore query parameter
* OpenApi `spatial_resource`
* OpenApi GDPR complience

## 1.2.12

**Fixed:**

* Gazetteer geometry bug

## 1.2.11

**Changed:**

* Remove leading slash for imprint and privacy page

## 1.2.10

**Added:**

* OpenApi GDPR compliance

**Changed:**

* Set globalAggregation false by default

## 1.2.9

**Added:**

* Title caching of facet titles
* Allow exact filtering via "includes" of subfields of catalog
* Fallback mechanism for langauge based fields for "includes"

**Changed:**

* Use of Elasticsearch 7.10.2
* PUT preserves stored score, if not set

## 1.2.8

**Removed:**
* Title requirements in Open API

## 1.2.7

**Added:**
* CLI command number of replica per index

**Changed:**
* Default config for data scope facet

## 1.2.6

**Added:**
* Supporting configurable favicon and logo

## 1.2.5

**Changed:**

* Improved logging with PiveauContext

## 1.2.4

**Added:**

* PiveauContext logging
* ELK stack configuration
* Field contact_point/address in dataset mapping
* Field contact_point/telepone in dataset mapping

## 1.2.3

**Added:**

* Missing artifact LICENSE.md
* Must match and must not match aggregation
* Caching for vocabulary and catalog replacement in dataset requests
* Vocabulary index and endpoint
* Field dct:deadline in dataset mapping
* Field dct:type in dataset mapping

**Changed:**

* Increase breaker tries in standard config

## 1.2.2

Era Before changelog
