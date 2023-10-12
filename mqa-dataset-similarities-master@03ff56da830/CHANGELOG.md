# Changelog

## Unreleased

## 3.0.2 (2023-06-14)

**Changed:**
* bump up dependencies
* Use english as fallback when catalogue has no language tag

## 3.0.1 (2023-03-17)

**Fixed:**
* Fingerprinting complete catalogue list

## 3.0.0 (2022-02-23)

**Added:**
* Indexing all fingerprints on startup
* Admin endpoint for manually trigger fingerprinting

**Changed:**
* Schedule only one run iterating through all catalogues at once
* ReDoc served from local

**Fixed:**
* Sanitizing strings correctly

## 2.1.0 (2022-02-18)

**Added:**
* Configurable DCAT-AP schema

## 2.0.0 (2020-06-07)

**Added**
* Internal scheduler for fingerprinting
* Configurable OpenAPI logo

**Changed**
* Bump Vert.X to version 4
* Configurable work dir
* Services uses port 8080 by default

**Removed**
* Endpoint for manual fingerprinting

## 1.0.0 (2020-07-28)

Initial production release
