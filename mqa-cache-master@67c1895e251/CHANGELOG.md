# ChangeLog

## Unreleased

## 5.6.0 (2023-04-25)

**Added**

* support for scheduling refresh of all

## 5.5.0 (2022-11-01)

**Fixed:**

* code generation for vertx

**Added:**

* support for linting results in distributions

## 5.4.1 (2022-11-01)

**Changed:**
* caching the query results for violations, with an expiry check

**Fixed:**
* Counting Query for violations and violations query itself are now using the same clauses
* Normalization of piveau id

## 5.4.0 (2022-07-04)

**Changed:**
* Moved date modified from distribution to dataset

## 5.3.6 (2022-04-27)

**Changed:**
* Lock for refresh, preventing from multiple runs in parallel

**Fixed:**
* Use getProperty instead of getRequiredProperty to get rid of exceptions

## 5.3.5 (2022-02-25)

**Changed:**
* Queries using sub selects for better performance

## 5.3.4 (2022-02-24)

**Added:**
* Rudiment support of `Accept-Language` header

**Changed:**
* Dataset metrics handling

**Fixed:**
* Bug in refresh promise complete

## 5.3.3 (2022-02-04)

**Fixed:**
* Issue #1957: 404 when graph was found instead of when not found

## 5.3.2 (2022-01-13)

**Changed:**
* MongoDB configuration

## 5.3.2 (2022-01-26)

**Added**
* Server in OpenApi to fix API call examples, see https://github.com/Redocly/redoc/issues/1172 

## 5.3.1 (2022-01-12)

**Changed:**
* Lib dependencies

## 5.3.0 (2021-10-17)

**Fixed:**
* Hard coded dcat-ap schema context

**Changed:**
* Complete queries rework

## 5.2.1 (2021-10-15)

**Changed:**
* Use Vert.x 4 router builder
* use actual api key security handler

## 5.2.0 (2021-10-09)

**Added:**
* Extended DCAT-AP schema configuration

**Removed:**
* 404 error log output

## 5.1.1 (2021-09-29)

**Fixed:**
* Some log outputs

## 5.1.0 (2021-06-15)

**Fixed:**
* Jena lib update with bug fixes

## 5.0.0 (2021-06-07)

**Changed:**
* Query metrics with reference from record

**Fixed:**
* Required property disarmed

## 4.0.6 (2021-03-18)

**Changed:**
* Status error count query split in two queries

**Fixed:**
* Dataset quality calculation reliability
* Dataset metrics "tokenizer"

## 4.0.5 (2021-03-18)

**Changed:**
* Counting error codes is disarmed due to huge performance issues. Returns always 0

## 4.0.4 (2021-03-17)

**Fixed:**
* SPARQL queries

## 4.0.3 (2021-03-16)

**Changed:**
* Eventbus timeouts increased
* Small query optimizations

## 4.0.2 (2021-03-05)

**Added:**
* Average score for each dimension
* Dimension score fields to OpenApi

**Changed:**
* Get average scores from latest, not history anymore

**Fixed**
* Dimension scores are returned when filtering for that dimension, the score or both

## 3.1.1 (2020-10-26)

**Changed:**
* Return -1.0 when dqv result is empty

## 3.1.0 (2020-10-23)

**Changed:**
* Almost all queries to select latest or historic metrics graphs
 
## 3.0.2 (2020-09-17)

**Fixed:**
* Single metric refresh checks for valid catalogue id
* The current endpoint will now return the last value, when there are multiple for the last day

## 3.0.1 (2020-09-10)

**Added**
* Debug output

**Removed**
* Some stupid log output

## 3.0.0 (2020-09-xx)

**Added**
* Licence

**Changed:**
* Updated README to align with template used in other services

**Removed**
* Hot-deployment scripts

## 2.2.0 (2020-07-01)

**Added:**
* REST API for refreshing single catalogue
* REST API for deleting single catalogue metrics
 
## 2.1.0 (2020-06-15)

**Added:**
* Warnings in SHACL results
* Severity in SHACL violation reports

## 2.0.1 (2020-05-29)

**Fixed:**
* Missing time values for status code errors

## 2.0.0 (2020-05-25)

**Changed:**
* Almost everything, except the API itself

**Added:**

**Changed:**
* dateIssuedAvailability is calculated only on Datasets
* Apikey now must be set via environment variables

**Removed:**

**Fixed:**
* Queries for violations
