package org.reactome.server.tools.analysis.exporter.playground.models._;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Identifier {
    private String id;
    private double[] exp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double[] getExp() {
        return exp;
    }

    public void setExp(double[] exp) {
        this.exp = exp;
    }
}
