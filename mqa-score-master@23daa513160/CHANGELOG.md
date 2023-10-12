# ChangeLog

## Unreleased

## 3.1.5 (2023-07-14)

**Fixed:**
* JSON serialization issue with limited string size

## 3.1.4 (2023-05-25)

**Changed:**
* Bump up dependencies

## 3.1.3 (2022-11-09)

**Fixed:**
* RDF checks for higher reliability when parsing metrics

## 3.1.2 (2021-10-18)

**Changed:**
* Important connector lib update

## 3.1.1 (2021-06-23)

**Changed:**
* Connector pipe handling

## 3.1.0 (2021-06-07)

**Fixed:**
* Scoring algorithm
* No reducing of empty collection  

## 3.0.1 (2021-03-19)

**Removed:**
* Default false negative scoring of format and media type availability

## 3.0.0 (2021-01-31)

**Added:**
* Dimension scoring

**Fixed:**
* Scoring issued and modified only once

**Changed:**
* Switched to Vert.x 4.0.0

## 2.1.0 (2020-10-25)

**Changed:**
* Replace old score instead of adding it

## 2.0.0 (2020-xx-xx)

Missed release!

## 1.0.2 (2020-03-11)

**Changed:**
* Explicitly close dataset

## 1.0.1 (2020-03-08)

**Fixed:**
* Scoring only best urls, not all

## 1.0.0 (2020-02-28)

Initial production release

**Added:**
* `PV.formatMatch` and `PV.syntaxValid` scoring
* Default score values separated from main vocabulary
* Load score values from file, otherwise load default score values
 
**Changed**
* Score is stored as integer and not as concept anymore
* Score only best distribution 

**Fixed:**
* Reuse one single web client in verticle
* Score calculation for status codes