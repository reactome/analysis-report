package org.reactome.server.tools.analysis.exporter.playground.model._;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return "Identifier{" +
                "id='" + id + '\'' +
                ", exp=" + Arrays.toString(exp) +
                '}';
    }
}
