package io.piveau.metrics.cache.persistence;

/**
 * A representation of all available mongoDB collections GLOBAL, CATALOGUE & COUNTRY
 * SCORE is Deprecated and should only be used for backwards compatibility, when needed
 */
public enum DocumentScope {
    GLOBAL, CATALOGUE, COUNTRY, SCORE
}
