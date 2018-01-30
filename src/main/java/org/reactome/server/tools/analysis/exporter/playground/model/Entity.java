package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entity {
    private Integer total;
    private Integer found;
    private Float ratio;
    private Float pValue;
    private Float fdr;

    public Entity() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public Float getRatio() {
        return ratio;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public Float getpValue() {
        return pValue;
    }

    public void setpValue(Float pValue) {
        this.pValue = pValue;
    }

    public Float getFdr() {
        return fdr;
    }

    public void setFdr(Float fdr) {
        this.fdr = fdr;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "total=" + total +
                ", found=" + found +
                ", ratio=" + ratio +
                ", pValue=" + pValue +
                ", fdr=" + fdr +
                '}';
    }
}
