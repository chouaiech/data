package io.piveau.hub.search.util.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.piveau.hub.search.util.geo.BoundingBox;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParams {

    private Date minDate;
    private Date maxDate;
    private BoundingBox boundingBox;
    private Integer minScoring;
    private Integer maxScoring;
    private Boolean countryData;
    private Boolean dataServices;

    public SearchParams() {
        this.minDate = null;
        this.maxDate = null;
        this.boundingBox = null;
        this.minScoring = null;
        this.maxScoring = null;
        this.countryData = null;
        this.dataServices = null;
    }

    public SearchParams(Date minDate, Date maxDate, BoundingBox boundingBox, Integer minScoring, Integer maxScoring,
                        Boolean countryData, Boolean dataServices) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.boundingBox = boundingBox;
        this.minScoring = minScoring;
        this.maxScoring = maxScoring;
        this.countryData = countryData;
        this.dataServices = dataServices;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Integer getMinScoring() {
        return minScoring;
    }

    public void setMinScoring(Integer minScoring) {
        this.minScoring = minScoring;
    }

    public Integer getMaxScoring() {
        return maxScoring;
    }

    public void setMaxScoring(Integer maxScoring) {
        this.maxScoring = maxScoring;
    }

    public Boolean getCountryData() {
        return countryData;
    }

    public void setCountryData(Boolean countryData) {
        this.countryData = countryData;
    }

    public Boolean getDataServices() {
        return dataServices;
    }

    public void setDataServices(Boolean dataServices) {
        this.dataServices = dataServices;
    }

    @Override
    public String toString() {
        return "SearchParams{" +
                "minDate=" + minDate +
                ", maxDate=" + maxDate +
                ", boundingBox=" + boundingBox +
                ", minScoring=" + minScoring +
                ", maxScoring=" + maxScoring +
                ", countryData=" + countryData +
                ", dataServices=" + dataServices +
                '}';
    }
}
