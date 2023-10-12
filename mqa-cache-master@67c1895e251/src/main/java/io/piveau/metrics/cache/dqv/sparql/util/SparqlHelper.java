package io.piveau.metrics.cache.dqv.sparql.util;

import io.piveau.vocabularies.vocabulary.PV;
import org.apache.jena.rdf.model.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparqlHelper {

    private SparqlHelper(){
        throw new IllegalStateException("Utility class");
    }

    static final List<Resource> datasetMetrics = Arrays.asList(
            PV.keywordAvailability,
            PV.categoryAvailability,
            PV.spatialAvailability,
            PV.temporalAvailability,
            PV.dcatApCompliance,
            PV.accessRightsAvailability,
            PV.accessRightsVocabularyAlignment,
            PV.contactPointAvailability,
            PV.publisherAvailability,
            PV.dateIssuedAvailability,
            PV.dateModifiedAvailability,
            PV.scoring
    );

    static final List<Resource> distributionMetrics = Arrays.asList(
            PV.accessUrlStatusCode,
            PV.downloadUrlAvailability,
            PV.downloadUrlStatusCode,
            PV.formatAvailability,
            PV.mediaTypeAvailability,
            PV.formatMediaTypeVocabularyAlignment,
            PV.formatMediaTypeNonProprietary,
            PV.formatMediaTypeMachineInterpretable,
            PV.licenceAvailability,
            PV.knownLicence,
            PV.rightsAvailability,
            PV.byteSizeAvailability
    );

    private static final Map<String, String> countries = new HashMap<>();

    static {
        countries.put("CHE", "http://publications.europa.eu/resource/authority/country/CHE");
        countries.put("EST", "http://publications.europa.eu/resource/authority/country/EST");
        countries.put("ESP", "http://publications.europa.eu/resource/authority/country/ESP");
        countries.put("CZE", "http://publications.europa.eu/resource/authority/country/CZE");
        countries.put("PRT", "http://publications.europa.eu/resource/authority/country/PRT");
        countries.put("POL", "http://publications.europa.eu/resource/authority/country/POL");
        countries.put("DNK", "http://publications.europa.eu/resource/authority/country/DNK");
        countries.put("GRC", "http://publications.europa.eu/resource/authority/country/GRC");
        countries.put("BEL", "http://publications.europa.eu/resource/authority/country/BEL");
        countries.put("IRL", "http://publications.europa.eu/resource/authority/country/IRL");
        countries.put("LVA", "http://publications.europa.eu/resource/authority/country/LVA");
        countries.put("ROU", "http://publications.europa.eu/resource/authority/country/ROU");
        countries.put("SVK", "http://publications.europa.eu/resource/authority/country/SVK");
        countries.put("HRV", "http://publications.europa.eu/resource/authority/country/HRV");
        countries.put("NLD", "http://publications.europa.eu/resource/authority/country/NLD");
        countries.put("ITA", "http://publications.europa.eu/resource/authority/country/ITA");
        countries.put("FIN", "http://publications.europa.eu/resource/authority/country/FIN");
        countries.put("FRA", "http://publications.europa.eu/resource/authority/country/FRA");
        countries.put("SWE", "http://publications.europa.eu/resource/authority/country/SWE");
        countries.put("HUN", "http://publications.europa.eu/resource/authority/country/HUN");
        countries.put("LTU", "http://publications.europa.eu/resource/authority/country/LTU");
        countries.put("LUX", "http://publications.europa.eu/resource/authority/country/LUX");
        countries.put("SVN", "http://publications.europa.eu/resource/authority/country/SVN");
        countries.put("MLT", "http://publications.europa.eu/resource/authority/country/MLT");
        countries.put("CYP", "http://publications.europa.eu/resource/authority/country/CYP");
        countries.put("BGR", "http://publications.europa.eu/resource/authority/country/BGR");
        countries.put("SRB", "http://publications.europa.eu/resource/authority/country/SRB");
        countries.put("DEU", "http://publications.europa.eu/resource/authority/country/DEU");
        countries.put("GBR", "http://publications.europa.eu/resource/authority/country/GBR");
        countries.put("AUT", "http://publications.europa.eu/resource/authority/country/AUT");
    }

    public static Map<String, String> getCountries() {
        return countries;
    }

    public static boolean isDatasetMetric(Resource metric) {
        return datasetMetrics.contains(metric);
    }

    public static boolean isDistributionMetric(Resource metric) {
        return distributionMetrics.contains(metric);
    }

}
