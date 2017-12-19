package org.reactome.server.tools.analysis.exporter.playground.model._;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Entities {
    private int total;
    private int found;
    private double ratio;
    private double pValue;
    private double fdr;

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

    @Override
    public String toString() {
        return "Entities{" +
                "total=" + total +
                ", found=" + found +
                ", ratio=" + ratio +
                ", pValue=" + pValue +
                ", fdr=" + fdr +
                '}';
    }
}
