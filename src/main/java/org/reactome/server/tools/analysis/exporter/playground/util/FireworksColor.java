package org.reactome.server.tools.analysis.exporter.playground.util;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public enum FireworksColor {
    COPPER("COPPER"), COPPER_PLUS("COPPER plus"), BARIUM_LITHIUM("Barium Lithium"), CALCIUM_SALTS("Calcium Salts");
    private String color;

    FireworksColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
