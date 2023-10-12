# ChangeLog

## Unreleased

## 4.1.2 (2023-05-25)

**Changed:**
* Bump up dependencies

## 4.1.1 (2023-03-24)

**Changed:**
* Missing body returns 400 Bad Request
* Set default shacl shapes model for pipes to `dcatap211level1`

**Fixed:**
* Simplified CORS definition

## 4.1.0 (2022-12-27)

**Removed:**
* Draft SHACL shapes for DCAT-AP version 2.1.1
* Strict mode parameter (replaced by level modes)

**Added:**
* Final SHACL Shapes for DCAT-AP version 2.1.1
* 2.1.1 Level 1 mode (only basic shapes, no ranges shapes)
* 2.1.1 Level 2 mode (level 1 plus recommended shapes)
* 2.1.1 Level 3 mode (level 2 plus vocabulary shapes)
* 2.1.0 Level 1 mode (only basic shapes, no ranges shapes)

**Changed:**
* Configurable default shapes for pipe module 
* Default pipe module shapes to 2.1.1 level 2

## 4.0.0 (2022-10-31)

**Changed:**
* Skip based on source type in dataInfo 

**Added:**
* Draft SHACL shapes for DCAT-AP version 2.1.1
* SHACL shapes for DCAT-AP version 2.1.0

## 3.2.2 (2021-11-11)

**Fixed:**
* OpenAPI displaying on sub route

## 3.2.1 (2021-10-18)

**Changed:**
* Important connector update  

## 3.2.0 (2021-10-06)

**Added:**
* Configurable Jena shacl engine alternative

## 3.1.6 (2021-09-30)

**Added:**
* GDPR compliance via imprint and privacy url

## 3.1.5 (2021-06-07)

**Changed:**
* Connector pipe handling

## 3.1.4 (2021-06-07)

**Fixed:**
* Maintenance

## 3.1.3 (2021-03-19)

**Fixed:**
* Vert.x issues (update to 4.0.3)

## 3.1.2 (2021-03-05)

**Fixed:**
* Finally favicon and logo configuration

## 3.1.1 (2021-03-04)

**Fixed:**
* Favicon and logo configuration

## 3.1.0 (2021-03-01)

**Changed:**
* Decouple validation service and pipe module

## 3.0.0 (2021-01-22)

**Changed:**
* Switched to Vert.x 4
* Switched to new graalvm image repository

**Added:**
* `dqv:hasQualityMetadata` property for resource when metrics graph is created  
* Support for json-ld in api
* Validation for `DataService`, `Relationship`, `Checksum`, `Location`, `PeriodOfTime`, `LicenseDocument`
  and `Identifier` nodes

## 2.2.2 (2020-10-28)

**Removed:**
* Fix from 2.2.1 which was made by mistake

## 2.2.1 (2020-10-28)

**Fixed:**
* Doubled validation of embedded nodes

**Added:**
* Validation of DCAT-AP.de v1.0.2 

## 2.2.0 (2020-10-01)

**Changed:**
* Validation timeout after 5 min instead of 30 sec
 
**Added:**
* More informative 400 responses

## 2.1.1 (2020-08-31)

**Fixed:**
* Update connector for publish fix

## 2.1.0 (2020-08-31)

**Added:**
* DCAT-AP Shacl Shapes 2.0.1

**Changed:**
* Default Shacl Shapes set to version 2.0.1

## 1.1.3 (2020-04-03)

**Fixed:**
* Include report in annotation

**Changed:**
* Vert.x and dependencies update

## 1.1.2 (2020-03-11)

**Changed:**
* Explicitly closing models and datasets after use

## 1.1.1 (2020-03-08)

**Changed:**
* Use utils metrics API instead of own implementation

## 1.1.0 (2020-03-06)

**Added:**
* Configuration of verticle instances and worker pool size
 
## 1.0.3 (2020-03-03)

**Fixed:**
* Pass payload correctly when skipped

## 1.0.2 (2020-03-02)

**Fixed:**
* Hot-fix for openapi yaml load in index html 

## 1.0.1 (2020-03-02)

**Fixed:**
* Hot-fix for shacl validation service index page 

## 1.0.0 (2020-02-28)

Initial production release

**Added:**
* check for existing quality metadata
* `/validation/report` for direct validation interface
* All shacl shape versions
* Configuration shape version via pipe and and REST API 
  
**Changed:**
* Updated dcat-ap shacl files
* `attached` default value to true
* Read data as dataset
* Pre-load vocabularies and shapes on start-up
* Sub-route `/validation/report` joint now at `/shacl` for compliance
 
**Fixed:**
* Validation report with conforms
* Validate all nodes in a model
