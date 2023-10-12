package io.piveau.metrics.cache.dqv;

import io.piveau.vocabularies.vocabulary.PV;

public enum Dimension {
    ACCESSIBILITY("Accessibility", 100, PV.accessibility.getURI()),
    FINDABILITY("Findability", 100, PV.findability.getURI()),
    INTEROPERABILITY("Interoperability", 110, PV.interoperability.getURI()),
    REUSABILIY("Reusability", 75, PV.reusability.getURI()),
    CONTEXTUALITY("Contextuality", 20, PV.contextuality.getURI());

    private final String label;
    private final Integer maxScore;
    private final String uriRef;

    Dimension(String label, Integer maxScore, String uriRef) {
        this.label = label;
        this.maxScore = maxScore;
        this.uriRef = uriRef;
    }

    public String getLabel() {
        return label;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public String getUriRef() {
        return uriRef;
    }

}
