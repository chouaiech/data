package io.piveau.metrics.reporter.util;

import io.piveau.metrics.reporter.model.Translation;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.Locale;

public class Translator {

    private final Locale locale;
    private final JsonObject countries;

    private final Translation translations;
    private final JsonObject sectionTranslations;

    public Translator(Locale locale, JsonObject i18n, JsonObject countries) {
        this.locale = locale;
        this.countries = countries;

        translations = generateReportLabels(i18n);
        sectionTranslations = generateSectionTranslations(i18n);
    }

    public JsonObject getCountries() {
        return countries;
    }

    public Translation getTranslations() {
        return translations;
    }

    public JsonObject getSectionTranslations() {
        return sectionTranslations;
    }

    /*
     * Method to extract report labels from lang-old.json file.
     * The labels are saved into an instance of class ReportLabels.
     */
    private Translation generateReportLabels(JsonObject i18n) {
        JsonObject languageRoot = i18n.getJsonObject(locale.getLanguage().toLowerCase());

        if (languageRoot != null) {

            JsonObject msg = languageRoot.getJsonObject("message");

            Translation labels = new Translation();
            labels.setLanguage(locale);

            labels.setDashboardTitle(getKeySafe(msg, new String[]{"common", "deu", "title"}));
            labels.setMqaTitle(getKeySafe(msg, new String[]{"title"}));
            labels.setMqaIntro(getKeySafe(msg, new String[]{"methodology", "intro_text"}));
            labels.setPortalUrl(getKeySafe(msg, new String[]{"common", "deu", "url"}));

            labels.setMqaDescription(getKeySafe(msg, new String[]{"dashboard", "intro", "desc"}));

            labels.setMetricLabelYes(getKeySafe(msg, new String[]{"common", "yes"}));
            labels.setMetricLabelNo(getKeySafe(msg, new String[]{"common", "no"}));

            labels.setCatalogueCountry(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "country"}));
            labels.setCatalogueName(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "name"}));
            labels.setAccessibilityAccessUrl(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "accessibility_access_url"}));
            labels.setMachineReadability(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "machine_readability"}));
            labels.setDcatApCompliance(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "dcat_ap"}));
            labels.setKnownLicences(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "known_licences"}));
            labels.setAccessibilityDownloadUrl(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "accessibility_download_url"}));

            labels.setMethodologyHeadline(getKeySafe(msg, new String[]{"methodology", "headline"}));
            labels.setMethodologySubHeadline(getKeySafe(msg, new String[]{"methodology", "sub_headline"}));
            labels.setMethodologyIntroText(getKeySafe(msg, new String[]{"methodology", "intro_text"}));
            labels.setMethodologyQualityHeadline(getKeySafe(msg, new String[]{"methodology", "what_is_quality", "headline"}));
            labels.setMethodologyQualityMainText(getKeySafe(msg, new String[]{"methodology", "what_is_quality", "main_text"}));
            labels.setMethodologyScopeHeadline(getKeySafe(msg, new String[]{"methodology", "scope", "headline"}));
            labels.setMethodologyScopeMainText(getKeySafe(msg, new String[]{"methodology", "scope", "main_text"}));
            labels.setMethodologyCoverHeadline(getKeySafe(msg, new String[]{"methodology", "cover", "headline"}));
            labels.setMethodologyCoverMainText(getKeySafe(msg, new String[]{"methodology", "cover", "main_text"}));
            labels.setMethodologyCoverLinkText(getKeySafe(msg, new String[]{"methodology", "cover", "validator_ui"}));
            labels.setMethodologyCoverApiLinkText(getKeySafe(msg, new String[]{"methodology", "cover", "validator_service"}));
            labels.setMethodologyProcessHeadline(getKeySafe(msg, new String[]{"methodology", "process", "headline"}));
            labels.setMethodologyProcessMainText(getKeySafe(msg, new String[]{"methodology", "process", "main_text"}));
            labels.setMethodologyAssumptionsHeadline(getKeySafe(msg, new String[]{"methodology", "assumptions", "headline"}));
            labels.setMethodologyAssumptionsMainText(getKeySafe(msg, new String[]{"methodology", "assumptions", "main_text"}));

            labels.setMethodologyAssumptionsHeadline(getKeySafe(msg, new String[]{"methodology", "assumptions", "headline"}));
            labels.setMethodologyAssumptionsMainText(getKeySafe(msg, new String[]{"methodology", "assumptions", "main_text"}));

            // Dimensions
            labels.setMethodologyDimensionsHeadline(getKeySafe(msg, new String[]{"dashboard", "dimensions"}));
            labels.setMethodologyDimensionsMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "main_text"}));

            //Findability
            labels.setMethodologyFindabilityHeadline(getKeySafe(msg, new String[]{"dashboard", "findability", "title"}));
            labels.setMethodologyFindabilityMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "main_text"}));

            //Accessibility
            labels.setMethodologyAccessibilityHeadline(getKeySafe(msg, new String[]{"dashboard", "accessibility", "title"}));
            labels.setMethodologyAccessibilityMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "main_text"}));

            //Interoperability
            labels.setMethodologyInteroperabilityHeadline(getKeySafe(msg, new String[]{"dashboard", "interoperability", "title"}));
            labels.setMethodologyInteroperabilityMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "main_text"}));

            //Reusability
            labels.setMethodologyReusabilityHeadline(getKeySafe(msg, new String[]{"dashboard", "reusability", "title"}));
            labels.setMethodologyReusabilityMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "main_text"}));


            //Contextuality
            labels.setMethodologyContextualityHeadline(getKeySafe(msg, new String[]{"dashboard", "contextuality", "title"}));
            labels.setMethodologyContextualityMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "main_text"}));

            //Rating
            labels.setMethodologyRatingHeadline(getKeySafe(msg, new String[]{"methodology", "scoring", "headline"}));
            labels.setMethodologyRatingMainText(getKeySafe(msg, new String[]{"methodology", "scoring", "text"}));

            labels.setNoDataError(getKeySafe(msg, new String[]{"no_data"}));

            labels.setCatalogueRating(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "rating"}));
            labels.setNavigationDashboard(getKeySafe(msg, new String[]{"navigation", "dashboard"}));
            labels.setNotAvailable(getKeySafe(msg, new String[]{"common", "notavailable"}));
            labels.setPageTranslation(getKeySafe(msg, new String[]{"common", "page"}));

            labels.setScoreBad(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "bad"}));
            labels.setScoreExcellent(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "excellent"}));
            labels.setScoreGood(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "good"}));
            labels.setScoreSufficient(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "sufficient"}));


            labels.setMethodologyTableMetric(getKeySafe(msg, new String[]{"methodology", "dimensions", "table", "metric"}));
            labels.setMethodologyTableWeight(getKeySafe(msg, new String[]{"methodology", "dimensions", "table", "weight"}));
            labels.setMethodologyTableDesc(getKeySafe(msg, new String[]{"methodology", "dimensions", "table", "desc"}));
            labels.setMethodologyTableMetricName(getKeySafe(msg, new String[]{"methodology", "dimensions", "table", "metric_name"}));


            labels.setMethodologyFindabilityTableKeywords_name_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_name_1"}));
            labels.setMethodologyFindabilityTableKeywords_name_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_name_2"}));
            labels.setMethodologyFindabilityTableKeywords_name_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_name_3"}));
            labels.setMethodologyFindabilityTableKeywords_name_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_name_4"}));


            labels.setMethodologyFindabilityTableKeywords_metric_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_metric_1"}));
            labels.setMethodologyFindabilityTableKeywords_metric_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_metric_2"}));
            labels.setMethodologyFindabilityTableKeywords_metric_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_metric_3"}));
            labels.setMethodologyFindabilityTableKeywords_metric_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_metric_4"}));

            labels.setMethodologyFindabilityTableKeywords_desc_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_desc_1"}));
            labels.setMethodologyFindabilityTableKeywords_desc_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_desc_2"}));
            labels.setMethodologyFindabilityTableKeywords_desc_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_desc_3"}));
            labels.setMethodologyFindabilityTableKeywords_desc_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "findability", "keywords_desc_4"}));

            //Accessibility
            labels.setMethodologyAccessibilityTableKeywords_name_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_name_1"}));
            labels.setMethodologyAccessibilityTableKeywords_name_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_name_2"}));
            labels.setMethodologyAccessibilityTableKeywords_name_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_name_3"}));


            labels.setMethodologyAccessibilityTableKeywords_metric_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_metric_1"}));
            labels.setMethodologyAccessibilityTableKeywords_metric_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_metric_2"}));
            labels.setMethodologyAccessibilityTableKeywords_metric_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_metric_3"}));


            labels.setMethodologyAccessibilityTableKeywords_desc_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_desc_1"}));
            labels.setMethodologyAccessibilityTableKeywords_desc_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_desc_2"}));
            labels.setMethodologyAccessibilityTableKeywords_desc_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "accessible", "keywords_desc_3"}));

            //Interoperability
            labels.setMethodologyInteroperabilityTableKeywords_name_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_1"}));
            labels.setMethodologyInteroperabilityTableKeywords_name_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_2"}));
            labels.setMethodologyInteroperabilityTableKeywords_name_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_3"}));
            labels.setMethodologyInteroperabilityTableKeywords_name_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_4"}));
            labels.setMethodologyInteroperabilityTableKeywords_name_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_5"}));
            labels.setMethodologyInteroperabilityTableKeywords_name_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_name_6"}));

            labels.setMethodologyInteroperabilityTableKeywords_metric_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_1"}));
            labels.setMethodologyInteroperabilityTableKeywords_metric_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_2"}));
            labels.setMethodologyInteroperabilityTableKeywords_metric_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_3"}));
            labels.setMethodologyInteroperabilityTableKeywords_metric_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_4"}));
            labels.setMethodologyInteroperabilityTableKeywords_metric_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_5"}));
            labels.setMethodologyInteroperabilityTableKeywords_metric_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_metric_6"}));

            labels.setMethodologyInteroperabilityTableKeywords_desc_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_1"}));
            labels.setMethodologyInteroperabilityTableKeywords_desc_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_2"}));
            labels.setMethodologyInteroperabilityTableKeywords_desc_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_3"}));
            labels.setMethodologyInteroperabilityTableKeywords_desc_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_4"}));
            labels.setMethodologyInteroperabilityTableKeywords_desc_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_5"}));
            labels.setMethodologyInteroperabilityTableKeywords_desc_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "interoperable", "keywords_desc_6"}));


            //Reusability
            labels.setMethodologyReusabilityTableKeywords_name_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_1"}));
            labels.setMethodologyReusabilityTableKeywords_name_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_2"}));
            labels.setMethodologyReusabilityTableKeywords_name_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_3"}));
            labels.setMethodologyReusabilityTableKeywords_name_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_4"}));
            labels.setMethodologyReusabilityTableKeywords_name_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_5"}));
            labels.setMethodologyReusabilityTableKeywords_name_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_name_6"}));

            labels.setMethodologyReusabilityTableKeywords_metric_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_1"}));
            labels.setMethodologyReusabilityTableKeywords_metric_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_2"}));
            labels.setMethodologyReusabilityTableKeywords_metric_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_3"}));
            labels.setMethodologyReusabilityTableKeywords_metric_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_4"}));
            labels.setMethodologyReusabilityTableKeywords_metric_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_5"}));
            labels.setMethodologyReusabilityTableKeywords_metric_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_metric_6"}));

            labels.setMethodologyReusabilityTableKeywords_desc_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_1"}));
            labels.setMethodologyReusabilityTableKeywords_desc_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_2"}));
            labels.setMethodologyReusabilityTableKeywords_desc_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_3"}));
            labels.setMethodologyReusabilityTableKeywords_desc_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_4"}));
            labels.setMethodologyReusabilityTableKeywords_desc_5(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_5"}));
            labels.setMethodologyReusabilityTableKeywords_desc_6(getKeySafe(msg, new String[]{"methodology", "dimensions", "reuse", "keywords_desc_6"}));

            //Contextuality
            labels.setMethodologyContextualityTableKeywords_name_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_name_1"}));
            labels.setMethodologyContextualityTableKeywords_name_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_name_2"}));
            labels.setMethodologyContextualityTableKeywords_name_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_name_3"}));
            labels.setMethodologyContextualityTableKeywords_name_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_name_4"}));

            labels.setMethodologyContextualityTableKeywords_metric_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_metric_1"}));
            labels.setMethodologyContextualityTableKeywords_metric_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_metric_2"}));
            labels.setMethodologyContextualityTableKeywords_metric_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_metric_3"}));
            labels.setMethodologyContextualityTableKeywords_metric_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_metric_4"}));


            labels.setMethodologyContextualityTableKeywords_desc_1(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_desc_1"}));
            labels.setMethodologyContextualityTableKeywords_desc_2(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_desc_2"}));
            labels.setMethodologyContextualityTableKeywords_desc_3(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_desc_3"}));
            labels.setMethodologyContextualityTableKeywords_desc_4(getKeySafe(msg, new String[]{"methodology", "dimensions", "contextual", "keywords_desc_4"}));

            //Scoring
            labels.setMethodologyScoringTableMax_points(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "max_points"}));
            labels.setMethodologyScoringTableDimension(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "dimension"}));
            labels.setMethodologyScoringTableSum(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "sum"}));
            labels.setMethodologyScoringTableRange(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "range"}));

            return labels;
        } else {
            return null;
        }
    }

    private JsonObject generateSectionTranslations(JsonObject i18n) {
        JsonObject languageRoot = i18n.getJsonObject(locale.getLanguage().toLowerCase());

        if (languageRoot != null) {
            JsonObject msg = languageRoot.getJsonObject("message");

            return new JsonObject()
                    .put("accessibility", getKeySafe(msg, new String[]{"dashboard", "accessibility", "title"}))
                    .put("accessUrlStatusCodes", getKeySafe(msg, new String[]{"dashboard", "accessibility", "plots", "title", "accessUrlStatusCodes"}))
                    .put("downloadUrlAvailability", getKeySafe(msg, new String[]{"dashboard", "accessibility", "plots", "title", "downloadUrlAvailability"}))
                    .put("downloadUrlStatusCodes", getKeySafe(msg, new String[]{"dashboard", "accessibility", "plots", "title", "downloadUrlStatusCodes"}))

                    .put("contextuality", getKeySafe(msg, new String[]{"dashboard", "contextuality", "title"}))
                    .put("dateIssuedAvailability", getKeySafe(msg, new String[]{"dashboard", "contextuality", "plots", "title", "dateIssuedAvailability"}))
                    .put("dateModifiedAvailability", getKeySafe(msg, new String[]{"dashboard", "contextuality", "plots", "title", "dateModifiedAvailability"}))
                    .put("byteSizeAvailability", getKeySafe(msg, new String[]{"dashboard", "contextuality", "plots", "title", "byteSizeAvailability"}))
                    .put("rightsAvailability", getKeySafe(msg, new String[]{"dashboard", "contextuality", "plots", "title", "rightsAvailability"}))

                    .put("reusability", getKeySafe(msg, new String[]{"dashboard", "reusability", "title"}))
                    .put("contactPointAvailability", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "contactPointAvailability"}))
                    .put("licenceAvailability", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "licenceAvailability"}))
                    .put("licenceAlignment", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "licenceAlignment"}))
                    .put("accessRightsAvailability", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "accessRightsAvailability"}))
                    .put("publisherAvailability", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "publisherAvailability"}))
                    .put("accessRightsAlignment", getKeySafe(msg, new String[]{"dashboard", "reusability", "plots", "title", "accessRightsAlignment"}))

                    .put("interoperability", getKeySafe(msg, new String[]{"dashboard", "interoperability", "title"}))
                    .put("formatMediaTypeNonProprietary", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "formatMediaTypeNonProprietary"}))
                    .put("formatMediaTypeAlignment", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "formatMediaTypeAlignment"}))
                    .put("formatMediaTypeMachineReadable", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "formatMediaTypeMachineReadable"}))
                    .put("dcatApCompliance", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "dcatApCompliance"}))
                    .put("mediaTypeAvailability", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "mediaTypeAvailability"}))
                    .put("formatAvailability", getKeySafe(msg, new String[]{"dashboard", "interoperability", "plots", "title", "formatAvailability"}))

                    .put("findability", getKeySafe(msg, new String[]{"dashboard", "findability", "title"}))
                    .put("temporalAvailability", getKeySafe(msg, new String[]{"dashboard", "findability", "plots", "title", "temporalAvailability"}))
                    .put("spatialAvailability", getKeySafe(msg, new String[]{"dashboard", "findability", "plots", "title", "spatialAvailability"}))
                    .put("keywordAvailability", getKeySafe(msg, new String[]{"dashboard", "findability", "plots", "title", "keywordAvailability"}))
                    .put("categoryAvailability", getKeySafe(msg, new String[]{"dashboard", "findability", "plots", "title", "categoryAvailability"}))

                    .put("yes", getKeySafe(msg, new String[]{"common", "yes"}))
                    .put("no", getKeySafe(msg, new String[]{"common", "no"}))
                    .put("countries", countries);
        } else {
            return null;
        }
    }


    private static String getKeySafe(JsonObject root, String[] keys) {
        JsonObject lastNode = getKeyRecursively(root, keys);

        if (lastNode != null) {
            String label = lastNode.getString(keys[keys.length - 1]);
            return label != null ? label : "Key not found";
        } else {
            return "Key not found";
        }
    }

    private static JsonObject getKeyRecursively(JsonObject root, String[] keys) {
        if (root == null)
            return null;

        if (keys.length == 1)
            return root;
        return getKeyRecursively(root.getJsonObject(keys[0]), Arrays.copyOfRange(keys, 1, keys.length));
    }

    public static Translation createTranslation(JsonObject i18n, Locale locale) {
        JsonObject msg = i18n.getJsonObject("message");

        Translation translation = new Translation();

        translation.setLanguage(locale);

        translation.setDashboardTitle(getKeySafe(msg, new String[]{"common", "deu", "title"}));
        translation.setMqaTitle(getKeySafe(msg, new String[]{"title"}));
        translation.setMqaIntro(getKeySafe(msg, new String[]{"methodology", "intro_text"}));
        translation.setPortalUrl(getKeySafe(msg, new String[]{"common", "deu", "url"}));

        translation.setMqaDescription(getKeySafe(msg, new String[]{"dashboard", "intro", "desc"}));

        translation.setMetricLabelYes(getKeySafe(msg, new String[]{"common", "yes"}));
        translation.setMetricLabelNo(getKeySafe(msg, new String[]{"common", "no"}));

        translation.setCatalogueCountry(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "country"}));
        translation.setCatalogueName(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "name"}));
        translation.setAccessibilityAccessUrl(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "accessibility_access_url"}));
        translation.setMachineReadability(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "machine_readability"}));
        translation.setDcatApCompliance(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "dcat_ap"}));
        translation.setKnownLicences(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "known_licences"}));
        translation.setAccessibilityDownloadUrl(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "accessibility_download_url"}));

        translation.setMethodologyHeadline(getKeySafe(msg, new String[]{"methodology", "headline"}));
        translation.setMethodologySubHeadline(getKeySafe(msg, new String[]{"methodology", "sub_headline"}));
        translation.setMethodologyIntroText(getKeySafe(msg, new String[]{"methodology", "intro_text"}));
        translation.setMethodologyQualityHeadline(getKeySafe(msg, new String[]{"methodology", "what_is_quality", "headline"}));
        translation.setMethodologyQualityMainText(getKeySafe(msg, new String[]{"methodology", "what_is_quality", "main_text"}));
        translation.setMethodologyScopeHeadline(getKeySafe(msg, new String[]{"methodology", "scope", "headline"}));
        translation.setMethodologyScopeMainText(getKeySafe(msg, new String[]{"methodology", "scope", "main_text"}));
        translation.setMethodologyCoverHeadline(getKeySafe(msg, new String[]{"methodology", "cover", "headline"}));
        translation.setMethodologyCoverMainText(getKeySafe(msg, new String[]{"methodology", "cover", "main_text"}));
        translation.setMethodologyCoverLinkText(getKeySafe(msg, new String[]{"methodology", "cover", "validator_ui"}));
        translation.setMethodologyCoverApiLinkText(getKeySafe(msg, new String[]{"methodology", "cover", "validator_service"}));
        translation.setMethodologyProcessHeadline(getKeySafe(msg, new String[]{"methodology", "process", "headline"}));
        translation.setMethodologyProcessMainText(getKeySafe(msg, new String[]{"methodology", "process", "main_text"}));
        translation.setMethodologyAssumptionsHeadline(getKeySafe(msg, new String[]{"methodology", "assumptions", "headline"}));
        translation.setMethodologyAssumptionsMainText(getKeySafe(msg, new String[]{"methodology", "assumptions", "main_text"}));
        translation.setMethodologyDimensionsHeadline(getKeySafe(msg, new String[]{"dashboard", "dimensions"}));
        translation.setMethodologyDimensionsMainText(getKeySafe(msg, new String[]{"methodology", "dimensions", "main_text"}));
        translation.setNoDataError(getKeySafe(msg, new String[]{"no_data"}));

        translation.setCatalogueRating(getKeySafe(msg, new String[]{"dashboard", "overview", "table_title", "rating"}));
        translation.setNavigationDashboard(getKeySafe(msg, new String[]{"navigation", "dashboard"}));
        translation.setNotAvailable(getKeySafe(msg, new String[]{"common", "notavailable"}));
        translation.setPageTranslation(getKeySafe(msg, new String[]{"common", "page"}));

        translation.setScoreBad(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "bad"}));
        translation.setScoreExcellent(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "excellent"}));
        translation.setScoreGood(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "good"}));
        translation.setScoreSufficient(getKeySafe(msg, new String[]{"methodology", "scoring", "table", "sufficient"}));

        return translation;
    }

}
