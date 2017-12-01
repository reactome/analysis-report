package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {
    private String resource;
    private int total;
    private int found;
    private double ratio;
    private double pValue;
    private double fdr;
    private String[] exp;

    public Entities() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double getpValue() {
        return pValue;
    }

    public void setpValue(double pValue) {
        this.pValue = pValue;
    }

    public double getFdr() {
        return fdr;
    }

    public void setFdr(double fdr) {
        this.fdr = fdr;
    }

    public String[] getExp() {
        return exp;
    }

    public void setExp(String[] exp) {
        this.exp = exp;
    }
}
