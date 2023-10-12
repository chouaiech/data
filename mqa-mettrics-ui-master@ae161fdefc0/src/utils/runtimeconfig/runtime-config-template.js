/**
 * Configuration template file to bind specific properties to environment variables.
 * All values must have the prefix $VUE_APP_.
 * Their corresponding environment variable key labels must be the values without the $ character.
 * This object should be structurally identical (name and path) to the standard configuration file.
 */
export default {
  ROOT_API: '$VUE_APP_ROOT_API',
  ROOT_URL: '$VUE_APP_ROOT_URL',
  DATA_URL: '$VUE_APP_DATA_URL',
  MATOMO_URL: '$VUE_APP_MATOMO_URL',
  PIWIK_ID: '$VUE_APP_PIWIK_ID',
  REPORT_URL: '$VUE_APP_REPORT_URL',
  SHACL_VALIDATOR_URL: '$VUE_APP_SHACL_VALIDATOR_URL',
  SHACL_API_URL: '$VUE_APP_SHACL_API_URL',
  SCORING_START_DATE: '$VUE_APP_SCORING_START_DATE',
  SCORING_STEP_SIZE: '$VUE_APP_SCORING_STEP_SIZE',
  SCORING_MAX_POINTS: '$VUE_APP_SCORING_MAX_POINTS',
  HISTORY_START_DATE: '$VUE_APP_HISTORY_START_DATE',
  HISTORY_RESOLUTION: '$VUE_APP_HISTORY_RESOLUTION',
  TRACKER_IS_PIWIK_PRO: '$VUE_APP_TRACKER_IS_PIWIK_PRO',
  TRACKER_TRACKER_URL: '$VUE_APP_TRACKER_TRACKER_URL',
  TRACKER_SITE_ID: '$VUE_APP_TRACKER_SITE_ID',
  SHOW_SPARQL: '$VUE_APP_SHOW_SPARQL'
  // Warning: everything here will be available client side.
  // Do not use runtime configurations for sensible information.
  // USERNAME_ENV: '$VUE_APP_USERNAME_ENV',
  // PASSWORD_ENV: '$VUE_APP_PASSWORD_ENV'
}
