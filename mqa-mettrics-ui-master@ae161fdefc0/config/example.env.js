"use strict"
const merge = require("webpack-merge")
const prodEnv = require("./prod.env")

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  ROOT_API: '"https://data.europa.eu/api/mqa/cache/"',
  ROOT_URL: '"https://data.europa.eu/mqa"',
  DATA_URL: '"https://data.europa.eu/data/datasets"',
  TRACKER_IS_PIWIK_PRO: 'true',
  TRACKER_TRACKER_URL: '"https://opanalytics.containers.piwik.pro/"',
  TRACKER_SITE_ID: '"fed9dbb7-42d1-4ebc-a8bf-3c0b8fd03e09"',
  MATOMO_URL: '"https://ppe.data.europa.eu/piwik/"',
  PIWIK_ID: '"fed9dbb7-42d1-4ebc-a8bf-3c0b8fd03e09"',
  REPORT_URL: '"https://data.europa.eu/api/mqa/reporter/"',
  SHACL_VALIDATOR_URL: '"https://data.europa.eu/mqa/shacl-validator-ui/"',
  SHACL_API_URL: '"https://data.europa.eu/api/mqa/shacl/"',
  DEBUG_DEV: true,
  SCORING_START_DATE: '"2020-01-24"',
  SCORING_STEP_SIZE: '"10"',
  SCORING_MAX_POINTS: '"405"',
  HISTORY_START_DATE: '"2020-01-24"',
  HISTORY_RESOLUTION: '"month"',
  SHOW_SPARQL: true,
})
