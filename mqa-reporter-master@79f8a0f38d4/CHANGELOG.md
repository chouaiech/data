# ChangeLog

## Unreleased

## 4.0.3 (2023-06-23)

**Changed:**
* Dump library dependencies

## 4.0.2 (2023-04-11)

* updated kotlin.version to 1.8.20
* updated vertx.version to 4.4.0
* updated java to 17
* Fixed different bugs
* TabularReportVerticle Reading vertx.eventBus().<JsonObject> failed =>  changed to vertx.eventBus().<String> line 40
* Using Roboto TTF fonts in class Style throws java.io.IOException: The TrueType font null does not contain a 'cmap' table => changed to HELVETICA font
* creating excel sheet for each catalogue title failed, if the catalogue title is longer then 31 chars=> truncate catalogue Title for creating excel sheet to 30 chars

## 4.0.1 (2022-01-26)

**Added**
* Server in OpenApi to fix API call examples, see https://github.com/Redocly/redoc/issues/1172 

## 4.0.0 

**Added:**
* API endpoint to generate reports on demand

**Removed:**

* DEFAULT_QUICKCHART_ADDRESS value

**Changed**

* Application now requires an API key for launch
* PDF report generation is skipped when the QUICKCHART_ADDRESS variable is not set
* The following libraries are used for PDF generation instead of iText
    * Apache PDF Box, Apache License 2.0: Creating PDF documents. PDFBox does not contain features to create complex
      layouts (like iText) pdfbox-layout, MIT License: Text layout library on top of PDFBox.
    * easytable, MIT License : Library for creating extensive tables based on PDFBox.
    * JSoup, MIT license: Java HTML Parser for parsing HTML formatted methodology descriptions.
* PDF generation logic implemented in class PdfReport based on the listed libraries
* Global report is generated from scratch and not by merging PDF files

## 3.0.0 (2021-06-07)

**Added**

* Introduced a scheduler for triggering report generation

**Changed**

* Revert back to content negotiation via path param, since accept header was buggy

**Fixed**

* Handling of metrics dimension scores

**Removed**

* API endpoint for triggering report generation manually

## 2.0.2 (2021-03-04)

**Added:**

* Configuration change listener
* `PIVEAU_LOG_LEVEL` for general log level configuration of the `io.piveau`package
* Reports are generated on per-catalogue basis, in addition to the global report
    * Catalogue reports can be requested using the `catalogueId` query parameter
* Reports are generated once on service startup
* Supporting configurable favicon an logo

**Changed:**

* Requires now latest LTS Java 11
* Docker base image to openjdk:11-jre
* Cache historic API compliance
* Report format is now toggled via `accept` header instead of path parameter
* Reports are not generated for all languages at once
    * Instead, the endpoint accepts languages as query parameters for generation

**Removed:**

* Hash calculation removed
* Buggy ToC for PDF reports

**Fixed:**

* Update all dependencies
