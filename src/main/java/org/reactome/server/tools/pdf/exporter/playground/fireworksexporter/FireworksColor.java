package org.reactome.server.tools.pdf.exporter.playground.fireworksexporter;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public enum FireworksColor {
    Copper("Copper"), CopperPlus("Copper plus"), BariumLithium("Barium Lithium"), CalciumSalts("Calcium Salts");
    private String color;

    FireworksColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
