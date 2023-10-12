/**
 * Configuration template file to bind specific properties to environment variables.
 * All values must have the prefix $VUE_APP_.
 * Their corresponding environment variable key labels must be the values without the $ character.
 * This object should be structurally identical (name and path) to the standard configuration file.
 */
export default {
  ROOT_API: '$VUE_APP_ROOT_API',
  MATOMO_URL: '$VUE_APP_MATOMO_URL',
  PIWIK_ID: '$VUE_APP_PIWIK_ID',
  TRACKER_IS_PIWIK_PRO: '$VUE_APP_TRACKER_IS_PIWIK_PRO',
  TRACKER_TRACKER_URL: '$VUE_APP_TRACKER_TRACKER_URL',
  TRACKER_SITE_ID: '$VUE_APP_TRACKER_SITE_ID',
  SHOW_SPARQL: '$VUE_APP_SHOW_SPARQL',
}
