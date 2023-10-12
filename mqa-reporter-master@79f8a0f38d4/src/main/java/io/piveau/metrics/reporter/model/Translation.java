package io.piveau.metrics.reporter.model;

import java.util.Locale;

public class Translation {

    private Locale language;

    private String mqaTitle;
    private String mqaIntro;
    private String portalUrl;
    private String dashboardTitle;

    private String metricLabelYes;
    private String metricLabelNo;

    private String catalogueCountry;
    private String catalogueName;
    private String accessibilityAccessUrl;
    private String machineReadability;
    private String dCatApCompliance;
    private String knownLicences;
    private String accessibilityDownloadUrl;

    private String methodologyHeadline;
    private String methodologySubHeadline;
    private String methodologyIntroText;
    private String methodologyQualityHeadline;
    private String methodologyQualityMainText;
    private String methodologyScopeHeadline;
    private String methodologyScopeMainText;
    private String methodologyCoverHeadline;
    private String methodologyCoverMainText;
    private String methodologyProcessHeadline;
    private String methodologyProcessMainText;
    private String methodologyAssumptionsHeadline;
    private String methodologyAssumptionsMainText;
    private String methodologyDimensionsHeadline;
    private String methodologyDimensionsMainText;

    private String noDataError;
    private String mqaDescription;
    private String catalogueRating;

    private String notAvailable;
    private String pageTranslation;

    private String scoreExcellent;
    private String scoreGood;
    private String scoreBad;
    private String scoreSufficient;

    private String methodologyCoverLinkText;
    private String methodologyCoverApiLinkText;
    private String methodologyRatingHeadline;
    private String methodologyRatingMainText;
    private String methodologyContextualityHeadline;
    private String methodologyContextualityMainText;
    private String methodologyAccessibilityHeadline;
    private String methodologyAccessibilityMainText;

    private String methodologyFindabilityHeadline;
    private String methodologyFindabilityMainText;
    private String methodologyReusabilityMainText;
    private String methodologyReusabilityHeadline;
    private String methodologyInteroperabilityHeadline;
    private String methodologyInteroperabilityMainText;
    private String methodologyTableMetric;
    private String methodologyTableWeight;
    private String methodologyTableMetricName;
    private String methodologyTableDesc;
    private String methodologyTableKeywordusage;
    private String methodologyTableCategories;
    private String methodologyTableGeosearch;
    private String methodologyTableTimebasedsearch;
    private String methodologyFindabilityTableKeywords_metric_1;
    private String methodologyFindabilityTableKeywords_metric_2;
    private String methodologyFindabilityTableKeywords_metric_3;
    private String methodologyFindabilityTableKeywords_metric_4;
    private String methodologyFindabilityTableKeywords_desc_1;
    private String methodologyFindabilityTableKeywords_desc_2;
    private String methodologyFindabilityTableKeywords_desc_3;
    private String methodologyFindabilityTableKeywords_desc_4;

    private String methodologyAccessibilityTableKeywords_name_1;
    private String methodologyAccessibilityTableKeywords_name_2;
    private String methodologyAccessibilityTableKeywords_name_3;
    private String methodologyAccessibilityTableKeywords_metric_1;
    private String methodologyAccessibilityTableKeywords_metric_2;
    private String methodologyAccessibilityTableKeywords_metric_3;
    private String methodologyAccessibilityTableKeywords_desc_1;
    private String methodologyAccessibilityTableKeywords_desc_2;
    private String methodologyAccessibilityTableKeywords_desc_3;


    private String methodologyInteroperabilityTableKeywords_name_1;
    private String methodologyInteroperabilityTableKeywords_name_2;
    private String methodologyInteroperabilityTableKeywords_name_3;
    private String methodologyInteroperabilityTableKeywords_name_4;
    private String methodologyInteroperabilityTableKeywords_name_5;
    private String methodologyInteroperabilityTableKeywords_name_6;
    private String methodologyInteroperabilityTableKeywords_metric_1;
    private String methodologyInteroperabilityTableKeywords_metric_2;
    private String methodologyInteroperabilityTableKeywords_metric_3;
    private String methodologyInteroperabilityTableKeywords_metric_4;
    private String methodologyInteroperabilityTableKeywords_metric_5;
    private String methodologyInteroperabilityTableKeywords_metric_6;
    private String methodologyInteroperabilityTableKeywords_desc_1;
    private String methodologyInteroperabilityTableKeywords_desc_2;
    private String methodologyInteroperabilityTableKeywords_desc_3;
    private String methodologyInteroperabilityTableKeywords_desc_4;
    private String methodologyInteroperabilityTableKeywords_desc_5;
    private String methodologyInteroperabilityTableKeywords_desc_6;


    private String methodologyReusabilityTableKeywords_name_1;
    private String methodologyReusabilityTableKeywords_name_2;
    private String methodologyReusabilityTableKeywords_name_3;
    private String methodologyReusabilityTableKeywords_name_4;
    private String methodologyReusabilityTableKeywords_name_5;
    private String methodologyReusabilityTableKeywords_name_6;
    private String methodologyReusabilityTableKeywords_metric_1;
    private String methodologyReusabilityTableKeywords_metric_2;
    private String methodologyReusabilityTableKeywords_metric_3;
    private String methodologyReusabilityTableKeywords_metric_4;
    private String methodologyReusabilityTableKeywords_metric_5;
    private String methodologyReusabilityTableKeywords_metric_6;
    private String methodologyReusabilityTableKeywords_desc_1;
    private String methodologyReusabilityTableKeywords_desc_2;
    private String methodologyReusabilityTableKeywords_desc_3;
    private String methodologyReusabilityTableKeywords_desc_4;
    private String methodologyReusabilityTableKeywords_desc_5;
    private String methodologyReusabilityTableKeywords_desc_6;

    //Contextuality
    private String methodologyContextualityTableKeywords_name_1;
    private String methodologyContextualityTableKeywords_name_2;
    private String methodologyContextualityTableKeywords_name_3;
    private String methodologyContextualityTableKeywords_name_4;

    private String methodologyContextualityTableKeywords_metric_1;
    private String methodologyContextualityTableKeywords_metric_2;
    private String methodologyContextualityTableKeywords_metric_3;
    private String methodologyContextualityTableKeywords_metric_4;

    private String methodologyContextualityTableKeywords_desc_1;
    private String methodologyContextualityTableKeywords_desc_2;
    private String methodologyContextualityTableKeywords_desc_3;
    private String methodologyContextualityTableKeywords_desc_4;
    private String methodologyScoringTableMax_points;
    private String methodologyScoringTableRange;
    private String methodologyScoringTableSum;
    private String methodologyScoringTableDimension;


    public Translation() {
    }

    public String getNavigationDashboard() {
        return navigationDashboard;
    }

    public void setNavigationDashboard(String navigationDashboard) {
        this.navigationDashboard = navigationDashboard;
    }

    private String navigationDashboard;

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public String getMqaTitle() {
        return mqaTitle;
    }

    public void setMqaTitle(String mqaTitle) {
        this.mqaTitle = mqaTitle;
    }

    public String getMqaIntro() {
        return mqaIntro;
    }

    public void setMqaIntro(String mqaIntro) {
        this.mqaIntro = mqaIntro;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public void setPortalUrl(String portalUrl) {
        this.portalUrl = portalUrl;
    }

    public String getDashboardTitle() {
        return dashboardTitle;
    }

    public void setDashboardTitle(String dashboardTitle) {
        this.dashboardTitle = dashboardTitle;
    }

    public String getMetricLabelYes() {
        return metricLabelYes;
    }

    public void setMetricLabelYes(String metricLabelYes) {
        this.metricLabelYes = metricLabelYes;
    }

    public String getMetricLabelNo() {
        return metricLabelNo;
    }

    public void setMetricLabelNo(String metricLabelNo) {
        this.metricLabelNo = metricLabelNo;
    }

    public String getCatalogueCountry() {
        return catalogueCountry;
    }

    public void setCatalogueCountry(String catalogueCountry) {
        this.catalogueCountry = catalogueCountry;
    }

    public String getCatalogueName() {
        return catalogueName;
    }

    public void setCatalogueName(String catalogueName) {
        this.catalogueName = catalogueName;
    }

    public String getAccessibilityAccessUrl() {
        return accessibilityAccessUrl;
    }

    public void setAccessibilityAccessUrl(String accessibilityAccessUrl) {
        this.accessibilityAccessUrl = accessibilityAccessUrl;
    }

    public String getMachineReadability() {
        return machineReadability;
    }

    public void setMachineReadability(String machineReadability) {
        this.machineReadability = machineReadability;
    }

    public String getDcatApCompliance() {
        return dCatApCompliance;
    }

    public void setDcatApCompliance(String dCatApCompliance) {
        this.dCatApCompliance = dCatApCompliance;
    }

    public String getKnownLicences() {
        return knownLicences;
    }

    public void setKnownLicences(String knownLicences) {
        this.knownLicences = knownLicences;
    }

    public String getAccessibilityDownloadUrl() {
        return accessibilityDownloadUrl;
    }

    public void setAccessibilityDownloadUrl(String accessibilityDownloadUrl) {
        this.accessibilityDownloadUrl = accessibilityDownloadUrl;
    }

    public String getMethodologyHeadline() {
        return methodologyHeadline;
    }

    public void setMethodologyHeadline(String methodologyHeadline) {
        this.methodologyHeadline = methodologyHeadline;
    }

    public String getMethodologySubHeadline() {
        return methodologySubHeadline;
    }

    public void setMethodologySubHeadline(String methodologySubHeadline) {
        this.methodologySubHeadline = methodologySubHeadline;
    }

    public String getMethodologyIntroText() {
        return methodologyIntroText;
    }

    public void setMethodologyIntroText(String methodologyIntroText) {
        this.methodologyIntroText = methodologyIntroText;
    }

    public String getMethodologyQualityHeadline() {
        return methodologyQualityHeadline;
    }

    public void setMethodologyQualityHeadline(String methodologyQualityHeadline) {
        this.methodologyQualityHeadline = methodologyQualityHeadline;
    }

    public String getMethodologyQualityMainText() {
        return methodologyQualityMainText;
    }

    public void setMethodologyQualityMainText(String methodologyQualityMainText) {
        this.methodologyQualityMainText = methodologyQualityMainText;
    }

    public String getMethodologyScopeHeadline() {
        return methodologyScopeHeadline;
    }

    public void setMethodologyScopeHeadline(String methodologyScopeHeadline) {
        this.methodologyScopeHeadline = methodologyScopeHeadline;
    }

    public String getMethodologyScopeMainText() {
        return methodologyScopeMainText;
    }

    public void setMethodologyScopeMainText(String methodologyScopeMainText) {
        this.methodologyScopeMainText = methodologyScopeMainText;
    }

    public String getMethodologyCoverHeadline() {
        return methodologyCoverHeadline;
    }

    public void setMethodologyCoverHeadline(String methodologyCoverHeadline) {
        this.methodologyCoverHeadline = methodologyCoverHeadline;
    }

    public String getMethodologyCoverMainText() {
        return methodologyCoverMainText;
    }

    public void setMethodologyCoverMainText(String methodologyCoverMainText) {
        this.methodologyCoverMainText = methodologyCoverMainText;
    }

    public String getMethodologyProcessHeadline() {
        return methodologyProcessHeadline;
    }

    public void setMethodologyProcessHeadline(String methodologyProcessHeadline) {
        this.methodologyProcessHeadline = methodologyProcessHeadline;
    }

    public String getMethodologyProcessMainText() {
        return methodologyProcessMainText;
    }

    public void setMethodologyProcessMainText(String methodologyProcessMainText) {
        this.methodologyProcessMainText = methodologyProcessMainText;
    }

    public String getMethodologyAssumptionsHeadline() {
        return methodologyAssumptionsHeadline;
    }

    public void setMethodologyAssumptionsHeadline(String methodologyAssumptionsHeadline) {
        this.methodologyAssumptionsHeadline = methodologyAssumptionsHeadline;
    }

    public String getMethodologyAssumptionsMainText() {
        return methodologyAssumptionsMainText;
    }

    public void setMethodologyAssumptionsMainText(String methodologyAssumptionsMainText) {
        this.methodologyAssumptionsMainText = methodologyAssumptionsMainText;
    }

    public String getMethodologyDimensionsHeadline() {
        return methodologyDimensionsHeadline;
    }

    public void setMethodologyDimensionsHeadline(String methodologyDimensionsHeadline) {
        this.methodologyDimensionsHeadline = methodologyDimensionsHeadline;
    }

    public String getMethodologyDimensionsMainText() {
        return methodologyDimensionsMainText;
    }

    public void setMethodologyDimensionsMainText(String methodologyDimensionsMainText) {
        this.methodologyDimensionsMainText = methodologyDimensionsMainText;
    }

    public String getNoDataError() {
        return noDataError;
    }

    public void setNoDataError(String noDataError) {
        this.noDataError = noDataError;
    }

    public String getMqaDescription() {
        return mqaDescription;
    }

    public void setMqaDescription(String mqaDescription) {
        this.mqaDescription = mqaDescription;
    }

    public String getCatalogueRating() {
        return catalogueRating;
    }

    public void setCatalogueRating(String catalogueRating) {
        this.catalogueRating = catalogueRating;
    }

    public String getNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(String notAvailable) {
        this.notAvailable = notAvailable;
    }

    public String getPageTranslation() {
        return pageTranslation;
    }

    public void setPageTranslation(String pageTranslation) {
        this.pageTranslation = pageTranslation;
    }

    public String getScoreExcellent() {
        return scoreExcellent;
    }

    public void setScoreExcellent(String scoreExcellent) {
        this.scoreExcellent = scoreExcellent;
    }

    public String getScoreGood() {
        return scoreGood;
    }

    public void setScoreGood(String scoreGood) {
        this.scoreGood = scoreGood;
    }

    public String getScoreBad() {
        return scoreBad;
    }

    public void setScoreBad(String scoreBad) {
        this.scoreBad = scoreBad;
    }

    public String getScoreSufficient() {
        return scoreSufficient;
    }

    public void setScoreSufficient(String scoreSufficient) {
        this.scoreSufficient = scoreSufficient;
    }

    public String getMethodologyCoverLinkText() {
        return methodologyCoverLinkText;
    }

    public String getMethodologyCoverApiLinkText() {
        return methodologyCoverApiLinkText;
    }

    public void setMethodologyCoverLinkText(String methodologyCoverLinkText) {
        this.methodologyCoverLinkText = methodologyCoverLinkText;
    }

    public void setMethodologyCoverApiLinkText(String methodologyCoverApiLinkText) {
        this.methodologyCoverApiLinkText = methodologyCoverApiLinkText;
    }

    public String getMethodologyFindabilityHeadline() {
        return methodologyFindabilityHeadline;
    }

    public String getMethodologyFindabilityMainText() {
        return methodologyFindabilityMainText;
    }

    public String getMethodologyAccessibilityHeadline() {
        return methodologyAccessibilityHeadline;
    }

    public String getMethodologyAccessibilityMainText() {
        return methodologyAccessibilityMainText;
    }

    public String getMethodologyInteroperabilityHeadline() {
        return methodologyInteroperabilityHeadline;
    }

    public String getMethodologyInteroperabilityMainText() {
        return methodologyInteroperabilityMainText;
    }

    public String getMethodologyReusabilityHeadline() {
        return methodologyReusabilityHeadline;
    }

    public String getMethodologyReusabilityMainText() {
        return methodologyReusabilityMainText;
    }

    public String getMethodologyContextualityHeadline() {
        return methodologyContextualityHeadline;
    }

    public String getMethodologyContextualityMainText() {
        return methodologyContextualityMainText;
    }

    public String getMethodologyRatingHeadline() {
        return methodologyRatingHeadline;
    }

    public String getMethodologyRatingMainText() {
        return methodologyRatingMainText;
    }

    public void setMethodologyRatingMainText(String methodologyRatingMainText) {
        this.methodologyRatingMainText = methodologyRatingMainText;
    }

    public void setMethodologyRatingHeadline(String methodologyRatingHeadline) {
        this.methodologyRatingHeadline = methodologyRatingHeadline;
    }

    public void setMethodologyContextualityHeadline(String methodologyContextualityHeadline) {
        this.methodologyContextualityHeadline = methodologyContextualityHeadline;
    }

    public void setMethodologyContextualityMainText(String methodologyContextualityMainText) {
        this.methodologyContextualityMainText = methodologyContextualityMainText;
    }

    public void setMethodologyFindabilityHeadline(String methodologyFindabilityHeadline) {
        this.methodologyFindabilityHeadline = methodologyFindabilityHeadline;
    }

    public void setMethodologyFindabilityMainText(String methodologyFindabilityMainText) {
        this.methodologyFindabilityMainText = methodologyFindabilityMainText;
    }

    public void setMethodologyAccessibilityHeadline(String methodologyAccessibilityHeadline) {
        this.methodologyAccessibilityHeadline = methodologyAccessibilityHeadline;
    }

    public void setMethodologyAccessibilityMainText(String methodologyAccessibilityMainText) {
        this.methodologyAccessibilityMainText = methodologyAccessibilityMainText;
    }

    public void setMethodologyInteroperabilityHeadline(String methodologyInteroperabilityHeadline) {
        this.methodologyInteroperabilityHeadline = methodologyInteroperabilityHeadline;
    }

    public void setMethodologyInteroperabilityMainText(String methodologyInteroperabilityMainText) {
        this.methodologyInteroperabilityMainText = methodologyInteroperabilityMainText;
    }

    public void setMethodologyReusabilityHeadline(String methodologyReusabilityHeadline) {
        this.methodologyReusabilityHeadline = methodologyReusabilityHeadline;
    }

    public void setMethodologyReusabilityMainText(String methodologyReusabilityMainText) {
        this.methodologyReusabilityMainText = methodologyReusabilityMainText;
    }

    public void setMethodologyTableMetric(String keySafe) {
        this.methodologyTableMetric = keySafe;
    }

    public void setMethodologyTableWeight(String keySafe) {
        this.methodologyTableWeight = keySafe;
    }

    public void setMethodologyTableDesc(String keySafe) {
        this.methodologyTableDesc = keySafe;
    }

    public void setMethodologyTableMetricName(String keySafe) {
        this.methodologyTableMetricName = keySafe;
    }

    public String getMethodologyTableMetric() {
        return methodologyTableMetric;
    }

    public String getMethodologyTableWeight() {
        return methodologyTableWeight;
    }

    public String getMethodologyTableMetricName() {
        return methodologyTableMetricName;
    }

    public String getMethodologyTableDesc() {
        return methodologyTableDesc;
    }

    public void setMethodologyFindabilityTableKeywords_name_1(String keySafe) {
        this.methodologyTableKeywordusage = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_name_2(String keySafe) {
        this.methodologyTableCategories = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_name_3(String keySafe) {
        this.methodologyTableGeosearch = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_name_4(String keySafe) {
        this.methodologyTableTimebasedsearch = keySafe;
    }

    public String getMethodologyTableKeywordusage() {
        return methodologyTableKeywordusage;
    }

    public String getMethodologyTableGeosearch() {
        return methodologyTableGeosearch;
    }

    public String getMethodologyTableCategories() {
        return methodologyTableCategories;
    }

    public String getMethodologyTableTimebasedsearch() {
        return methodologyTableTimebasedsearch;
    }

    public void setMethodologyFindabilityTableKeywords_metric_1(String keySafe) {
        this.methodologyFindabilityTableKeywords_metric_1 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_metric_2(String keySafe) {
        this.methodologyFindabilityTableKeywords_metric_2 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_metric_3(String keySafe) {
        this.methodologyFindabilityTableKeywords_metric_3 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_metric_4(String keySafe) {
        this.methodologyFindabilityTableKeywords_metric_4 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_desc_1(String keySafe) {
        this.methodologyFindabilityTableKeywords_desc_1 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_desc_2(String keySafe) {
        this.methodologyFindabilityTableKeywords_desc_2 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_desc_3(String keySafe) {
        this.methodologyFindabilityTableKeywords_desc_3 = keySafe;
    }

    public void setMethodologyFindabilityTableKeywords_desc_4(String keySafe) {
        this.methodologyFindabilityTableKeywords_desc_4 = keySafe;
    }

    public String getMethodologyFindabilityTableKeywords_metric_1() {
        return methodologyFindabilityTableKeywords_metric_1;
    }

    public String getMethodologyFindabilityTableKeywords_metric_2() {
        return methodologyFindabilityTableKeywords_metric_2;
    }

    public String getMethodologyFindabilityTableKeywords_metric_3() {
        return methodologyFindabilityTableKeywords_metric_3;
    }

    public String getMethodologyFindabilityTableKeywords_metric_4() {
        return methodologyFindabilityTableKeywords_metric_4;
    }

    public String getMethodologyFindabilityTableKeywords_desc_1() {
        return methodologyFindabilityTableKeywords_desc_1;
    }

    public String getMethodologyFindabilityTableKeywords_desc_2() {
        return methodologyFindabilityTableKeywords_desc_2;
    }

    public String getMethodologyFindabilityTableKeywords_desc_3() {
        return methodologyFindabilityTableKeywords_desc_3;
    }

    public String getMethodologyFindabilityTableKeywords_desc_4() {
        return methodologyFindabilityTableKeywords_desc_4;
    }

    public void setMethodologyAccessibilityTableKeywords_name_1(String keySafe) {
        this.methodologyAccessibilityTableKeywords_name_1 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_name_2(String keySafe) {
        this.methodologyAccessibilityTableKeywords_name_2 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_name_3(String keySafe) {
        this.methodologyAccessibilityTableKeywords_name_3 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_metric_1(String keySafe) {
        this.methodologyAccessibilityTableKeywords_metric_1 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_metric_2(String keySafe) {
        this.methodologyAccessibilityTableKeywords_metric_2 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_metric_3(String keySafe) {
        this.methodologyAccessibilityTableKeywords_metric_3 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_desc_1(String keySafe) {
        this.methodologyAccessibilityTableKeywords_desc_1 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_desc_2(String keySafe) {
        this.methodologyAccessibilityTableKeywords_desc_2 = keySafe;
    }

    public void setMethodologyAccessibilityTableKeywords_desc_3(String keySafe) {
        this.methodologyAccessibilityTableKeywords_desc_3 = keySafe;
    }

    public String getMethodologyAccessibilityTableKeywords_name_1() {
        return methodologyAccessibilityTableKeywords_name_1;
    }

    public String getMethodologyAccessibilityTableKeywords_name_2() {
        return methodologyAccessibilityTableKeywords_name_2;
    }

    public String getMethodologyAccessibilityTableKeywords_name_3() {
        return methodologyAccessibilityTableKeywords_name_3;
    }

    public String getMethodologyAccessibilityTableKeywords_metric_1() {
        return methodologyAccessibilityTableKeywords_metric_1;
    }

    public String getMethodologyAccessibilityTableKeywords_metric_2() {
        return methodologyAccessibilityTableKeywords_metric_2;
    }

    public String getMethodologyAccessibilityTableKeywords_metric_3() {
        return methodologyAccessibilityTableKeywords_metric_3;
    }

    public String getMethodologyAccessibilityTableKeywords_desc_1() {
        return methodologyAccessibilityTableKeywords_desc_1;
    }

    public String getMethodologyAccessibilityTableKeywords_desc_2() {
        return methodologyAccessibilityTableKeywords_desc_2;
    }

    public String getMethodologyAccessibilityTableKeywords_desc_3() {
        return methodologyAccessibilityTableKeywords_desc_3;
    }

    //Interoperability

    public String getMethodologyInteroperabilityTableKeywords_name_1() {
        return methodologyInteroperabilityTableKeywords_name_1;
    }
    public String getMethodologyInteroperabilityTableKeywords_name_2() {
        return methodologyInteroperabilityTableKeywords_name_2;
    }
    public String getMethodologyInteroperabilityTableKeywords_name_3() {
        return methodologyInteroperabilityTableKeywords_name_3;
    }
    public String getMethodologyInteroperabilityTableKeywords_name_4() {
        return methodologyInteroperabilityTableKeywords_name_4;
    }
    public String getMethodologyInteroperabilityTableKeywords_name_5() {
        return methodologyInteroperabilityTableKeywords_name_5;
    }
    public String getMethodologyInteroperabilityTableKeywords_name_6() {
        return methodologyInteroperabilityTableKeywords_name_6;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_1() {
        return methodologyInteroperabilityTableKeywords_desc_1;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_2() {
        return methodologyInteroperabilityTableKeywords_desc_2;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_3() {
        return methodologyInteroperabilityTableKeywords_desc_3;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_4() {
        return methodologyInteroperabilityTableKeywords_desc_4;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_5() {
        return methodologyInteroperabilityTableKeywords_desc_5;
    }
    public String getMethodologyInteroperabilityTableKeywords_desc_6() {
        return methodologyInteroperabilityTableKeywords_desc_6;
    }

    public String getMethodologyInteroperabilityTableKeywords_metric_1() {
        return methodologyInteroperabilityTableKeywords_metric_1;
    }
    public String getMethodologyInteroperabilityTableKeywords_metric_2() {
        return methodologyInteroperabilityTableKeywords_metric_2;
    }
    public String getMethodologyInteroperabilityTableKeywords_metric_3() {
        return methodologyInteroperabilityTableKeywords_metric_3;
    }
    public String getMethodologyInteroperabilityTableKeywords_metric_4() {
        return methodologyInteroperabilityTableKeywords_metric_4;
    }
    public String getMethodologyInteroperabilityTableKeywords_metric_5() {
        return methodologyInteroperabilityTableKeywords_metric_5;
    }
    public String getMethodologyInteroperabilityTableKeywords_metric_6() {
        return methodologyInteroperabilityTableKeywords_metric_6;
    }
    public void setMethodologyInteroperabilityTableKeywords_name_1(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_1 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_name_2(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_2 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_name_3(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_3 = keySafe;
    }
    public void setMethodologyInteroperabilityTableKeywords_name_4(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_4 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_name_5(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_5 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_name_6(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_name_6 = keySafe;
    }


     public void setMethodologyInteroperabilityTableKeywords_metric_1(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_1 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_metric_2(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_2 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_metric_3(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_3 = keySafe;
    }
    public void setMethodologyInteroperabilityTableKeywords_metric_4(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_4 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_metric_5(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_5 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_metric_6(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_metric_6 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_1(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_1 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_2(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_2 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_3(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_3 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_4(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_4 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_5(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_5 = keySafe;
    }

    public void setMethodologyInteroperabilityTableKeywords_desc_6(String keySafe) {
        this.methodologyInteroperabilityTableKeywords_desc_6 = keySafe;
    }

    //Reusability

    public String getMethodologyReusabilityTableKeywords_name_1() {
        return methodologyReusabilityTableKeywords_name_1;
    }
    public String getMethodologyReusabilityTableKeywords_name_2() {
        return methodologyReusabilityTableKeywords_name_2;
    }
    public String getMethodologyReusabilityTableKeywords_name_3() {
        return methodologyReusabilityTableKeywords_name_3;
    }
    public String getMethodologyReusabilityTableKeywords_name_4() {
        return methodologyReusabilityTableKeywords_name_4;
    }
    public String getMethodologyReusabilityTableKeywords_name_5() {
        return methodologyReusabilityTableKeywords_name_5;
    }
    public String getMethodologyReusabilityTableKeywords_name_6() {
        return methodologyReusabilityTableKeywords_name_6;
    }
    public String getMethodologyReusabilityTableKeywords_desc_1() {
        return methodologyReusabilityTableKeywords_desc_1;
    }
    public String getMethodologyReusabilityTableKeywords_desc_2() {
        return methodologyReusabilityTableKeywords_desc_2;
    }
    public String getMethodologyReusabilityTableKeywords_desc_3() {
        return methodologyReusabilityTableKeywords_desc_3;
    }
    public String getMethodologyReusabilityTableKeywords_desc_4() {
        return methodologyReusabilityTableKeywords_desc_4;
    }
    public String getMethodologyReusabilityTableKeywords_desc_5() {
        return methodologyReusabilityTableKeywords_desc_5;
    }
    public String getMethodologyReusabilityTableKeywords_desc_6() {
        return methodologyReusabilityTableKeywords_desc_6;
    }

    public String getMethodologyReusabilityTableKeywords_metric_1() {
        return methodologyReusabilityTableKeywords_metric_1;
    }
    public String getMethodologyReusabilityTableKeywords_metric_2() {
        return methodologyReusabilityTableKeywords_metric_2;
    }
    public String getMethodologyReusabilityTableKeywords_metric_3() {
        return methodologyReusabilityTableKeywords_metric_3;
    }
    public String getMethodologyReusabilityTableKeywords_metric_4() {
        return methodologyReusabilityTableKeywords_metric_4;
    }
    public String getMethodologyReusabilityTableKeywords_metric_5() {
        return methodologyReusabilityTableKeywords_metric_5;
    }
    public String getMethodologyReusabilityTableKeywords_metric_6() {
        return methodologyReusabilityTableKeywords_metric_6;
    }
    public void setMethodologyReusabilityTableKeywords_name_1(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_1 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_name_2(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_2 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_name_3(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_3 = keySafe;
    }
    public void setMethodologyReusabilityTableKeywords_name_4(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_4 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_name_5(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_5 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_name_6(String keySafe) {
        this.methodologyReusabilityTableKeywords_name_6 = keySafe;
    }


    public void setMethodologyReusabilityTableKeywords_metric_1(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_1 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_metric_2(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_2 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_metric_3(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_3 = keySafe;
    }
    public void setMethodologyReusabilityTableKeywords_metric_4(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_4 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_metric_5(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_5 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_metric_6(String keySafe) {
        this.methodologyReusabilityTableKeywords_metric_6 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_1(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_1 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_2(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_2 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_3(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_3 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_4(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_4 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_5(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_5 = keySafe;
    }

    public void setMethodologyReusabilityTableKeywords_desc_6(String keySafe) {
        this.methodologyReusabilityTableKeywords_desc_6 = keySafe;
    }

    //Contextuality
    public String getMethodologyContextualityTableKeywords_name_1() {
        return methodologyContextualityTableKeywords_name_1;
    }
    public String getMethodologyContextualityTableKeywords_name_2() {
        return methodologyContextualityTableKeywords_name_2;
    }
    public String getMethodologyContextualityTableKeywords_name_3() {
        return methodologyContextualityTableKeywords_name_3;
    }
    public String getMethodologyContextualityTableKeywords_name_4() {
        return methodologyContextualityTableKeywords_name_4;
    }

    public String getMethodologyContextualityTableKeywords_desc_1() {
        return methodologyContextualityTableKeywords_desc_1;
    }
    public String getMethodologyContextualityTableKeywords_desc_2() {
        return methodologyContextualityTableKeywords_desc_2;
    }
    public String getMethodologyContextualityTableKeywords_desc_3() {
        return methodologyContextualityTableKeywords_desc_3;
    }
    public String getMethodologyContextualityTableKeywords_desc_4() {
        return methodologyContextualityTableKeywords_desc_4;
    }


    public String getMethodologyContextualityTableKeywords_metric_1() {
        return methodologyContextualityTableKeywords_metric_1;
    }
    public String getMethodologyContextualityTableKeywords_metric_2() {
        return methodologyContextualityTableKeywords_metric_2;
    }
    public String getMethodologyContextualityTableKeywords_metric_3() {
        return methodologyContextualityTableKeywords_metric_3;
    }
    public String getMethodologyContextualityTableKeywords_metric_4() {
        return methodologyContextualityTableKeywords_metric_4;
    }

    public void setMethodologyContextualityTableKeywords_name_1(String keySafe) {
        this.methodologyContextualityTableKeywords_name_1 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_name_2(String keySafe) {
        this.methodologyContextualityTableKeywords_name_2 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_name_3(String keySafe) {
        this.methodologyContextualityTableKeywords_name_3 = keySafe;
    }
    public void setMethodologyContextualityTableKeywords_name_4(String keySafe) {
        this.methodologyContextualityTableKeywords_name_4 = keySafe;
    }



    public void setMethodologyContextualityTableKeywords_metric_1(String keySafe) {
        this.methodologyContextualityTableKeywords_metric_1 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_metric_2(String keySafe) {
        this.methodologyContextualityTableKeywords_metric_2 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_metric_3(String keySafe) {
        this.methodologyContextualityTableKeywords_metric_3 = keySafe;
    }
    public void setMethodologyContextualityTableKeywords_metric_4(String keySafe) {
        this.methodologyContextualityTableKeywords_metric_4 = keySafe;
    }


    public void setMethodologyContextualityTableKeywords_desc_1(String keySafe) {
        this.methodologyContextualityTableKeywords_desc_1 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_desc_2(String keySafe) {
        this.methodologyContextualityTableKeywords_desc_2 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_desc_3(String keySafe) {
        this.methodologyContextualityTableKeywords_desc_3 = keySafe;
    }

    public void setMethodologyContextualityTableKeywords_desc_4(String keySafe) {
        this.methodologyContextualityTableKeywords_desc_4 = keySafe;
    }


    public void setMethodologyScoringTableMax_points(String keySafe) {
        this.methodologyScoringTableMax_points = keySafe;
    }

    public void setMethodologyScoringTableDimension(String keySafe) {
        this.methodologyScoringTableDimension = keySafe;
    }

    public void setMethodologyScoringTableSum(String keySafe) {
        this.methodologyScoringTableSum = keySafe;
    }

    public void setMethodologyScoringTableRange(String keySafe) {
        this.methodologyScoringTableRange = keySafe;
    }

    public String getMethodologyScoringTableMax_points() {
        return methodologyScoringTableMax_points;
    }

    public String getMethodologyScoringTableRange() {
        return methodologyScoringTableRange;
    }

    public String getMethodologyScoringTableSum() {
        return methodologyScoringTableSum;
    }

    public String getMethodologyScoringTableDimension() {
        return methodologyScoringTableDimension;
    }
}

