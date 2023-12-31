package io.piveau.hub.search.util.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.piveau.hub.search.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {

    private String q;
    private String filter;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private HashMap<String, String[]> facets;
    private Integer from;
    private Integer size;
    private List<String> fields;
    private SearchParams searchParams;
    private HashMap<String, Float> boost;
    private Boolean globalAggregation;
    private Constants.Operator facetOperator;
    private Constants.Operator facetGroupOperator;
    private List<String> sort;
    private boolean aggregation;
    private boolean aggregationAllFields;
    private List<String> aggregationFields;
    private Integer aggregationLimit;
    private Integer aggregationMinCount;
    private boolean filterDistributions;
    private boolean elasticId;
    private List<String> includes;
    private boolean scroll;
    private String lang;
    private boolean showScore;
    private List<String> vocabulary;
    private boolean autocomplete;
    private List<String> exists;
    private String alias;

    public Query() {
        this.q = "";
        this.filter = "";
        this.facets = null;
        this.from = 0;
        this.size = 10;
        this.fields = null;
        this.searchParams = null;
        this.boost = new HashMap<>();
        this.globalAggregation = false;
        this.facetOperator = Constants.Operator.OR;
        this.facetGroupOperator = Constants.Operator.AND;
        this.sort = null;
        this.aggregation = true;
        this.aggregationAllFields = true;
        this.aggregationFields = new ArrayList<>();
        this.aggregationLimit = 0;
        this.aggregationMinCount = 0;
        this.filterDistributions = false;
        this.elasticId = false;
        this.includes = null;
        this.scroll = false;
        this.lang = "";
        this.showScore = false;
        this.vocabulary = null;
        this.autocomplete = false;
        this.exists = null;
        this.alias = null;
    }

    public Query(String q,
                 String filter,
                 HashMap<String, String[]> facets,
                 Integer from,
                 Integer size,
                 List<String> fields,
                 SearchParams searchParams,
                 HashMap<String, Float> boost,
                 Boolean globalAggregation,
                 Constants.Operator facetOperator,
                 Constants.Operator facetGroupOperator,
                 List<String> sort,
                 boolean aggregation,
                 boolean aggregationAllFields,
                 List<String> aggregationFields,
                 Integer aggregationLimit,
                 Integer aggregationMinCount,
                 boolean filterDistributions,
                 boolean elasticId,
                 List<String> includes,
                 boolean scroll,
                 String lang,
                 boolean showScore,
                 List<String> vocabulary,
                 boolean autocomplete,
                 List<String> exists,
                 String alias) {
        this.q = q;
        this.filter = filter;
        this.facets = facets;
        this.from = from;
        this.size = size;
        this.fields = fields;
        this.searchParams = searchParams;
        this.boost = boost;
        this.globalAggregation = globalAggregation;
        this.facetOperator = facetOperator;
        this.facetGroupOperator = facetGroupOperator;
        this.sort = sort;
        this.aggregation = aggregation;
        this.aggregationAllFields = aggregationAllFields;
        this.aggregationFields = aggregationFields;
        this.aggregationLimit = aggregationLimit;
        this.aggregationMinCount = aggregationMinCount;
        this.filterDistributions = filterDistributions;
        this.elasticId = elasticId;
        this.includes = includes;
        this.scroll = scroll;
        this.lang = lang;
        this.showScore = showScore;
        this.vocabulary = vocabulary;
        this.autocomplete = autocomplete;
        this.exists = exists;
        this.alias = alias;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public HashMap<String, String[]> getFacets() {
        return facets;
    }

    public void setFacets(HashMap<String, String[]> facets) {
        this.facets = facets;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public HashMap<String, Float> getBoost() {
        return boost;
    }

    public void setBoost(HashMap<String, Float> boost) {
        this.boost = boost;
    }

    public Boolean getGlobalAggregation() {
        return globalAggregation;
    }

    public void setGlobalAggregation(Boolean globalAggregation) {
        this.globalAggregation = globalAggregation;
    }

    public Constants.Operator getFacetOperator() {
        return facetOperator;
    }

    public void setFacetOperator(Constants.Operator facetOperator) {
        this.facetOperator = facetOperator;
    }

    public Constants.Operator getFacetGroupOperator() {
        return facetGroupOperator;
    }

    public void setFacetGroupOperator(Constants.Operator facetGroupOperator) {
        this.facetGroupOperator = facetGroupOperator;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public boolean isAggregation() {
        return aggregation;
    }

    public void setAggregation(boolean aggregation) {
        this.aggregation = aggregation;
    }

    public List<String> getAggregationFields() {
        return aggregationFields;
    }

    public void setAggregationFields(List<String> aggregationFields) {
        this.aggregationFields = aggregationFields;
    }

    public Integer getAggregationLimit() {
        return aggregationLimit;
    }

    public void setAggregationLimit(Integer aggregationLimit) {
        this.aggregationLimit = aggregationLimit;
    }

    public Integer getAggregationMinCount() {
        return aggregationMinCount;
    }

    public void setAggregationMinCount(Integer aggregationMinCount) {
        this.aggregationMinCount = aggregationMinCount;
    }

    public boolean isAggregationAllFields() {
        return aggregationAllFields;
    }

    public void setAggregationAllFields(boolean aggregationAllFields) {
        this.aggregationAllFields = aggregationAllFields;
    }

    public boolean isFilterDistributions() {
        return filterDistributions;
    }

    public void setFilterDistributions(boolean filterDistributions) {
        this.filterDistributions = filterDistributions;
    }

    public boolean isElasticId() {
        return elasticId;
    }

    public void setElasticId(boolean elasticId) {
        this.elasticId = elasticId;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isShowScore() {
        return showScore;
    }

    public void setShowScore(boolean showScore) {
        this.showScore = showScore;
    }

    public List<String> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<String> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public boolean isAutocomplete() {
        return autocomplete;
    }

    public void setAutocomplete(boolean autocomplete) {
        this.autocomplete = autocomplete;
    }

    public List<String> getExists() {
        return exists;
    }

    public void setExists(List<String> exists) {
        this.exists = exists;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Query{" +
                "q='" + q + '\'' +
                ", filter='" + filter + '\'' +
                ", facets=" + facets +
                ", from=" + from +
                ", size=" + size +
                ", fields=" + fields +
                ", searchParams=" + searchParams +
                ", boost=" + boost +
                ", globalAggregation=" + globalAggregation +
                ", facetOperator=" + facetOperator +
                ", facetGroupOperator=" + facetGroupOperator +
                ", sort=" + sort +
                ", aggregation=" + aggregation +
                ", aggregationAllFields=" + aggregationAllFields +
                ", aggregationFields=" + aggregationFields +
                ", aggregationLimit=" + aggregationLimit +
                ", aggregationMinCount=" + aggregationMinCount +
                ", filterDistributions=" + filterDistributions +
                ", elasticId=" + elasticId +
                ", includes=" + includes +
                ", scroll=" + scroll +
                ", lang=" + lang +
                ", showScore=" + showScore +
                ", vocabulary=" + vocabulary +
                ", autocomplete=" + autocomplete +
                ", exists=" + exists +
                ", alias=" + alias +
                '}';
    }
}
