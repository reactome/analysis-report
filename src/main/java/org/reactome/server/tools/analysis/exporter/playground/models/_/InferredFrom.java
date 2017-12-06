package org.reactome.server.tools.analysis.exporter.playground.models._;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class InferredFrom {
    private String displayName;
    private String speciesName;
    private boolean hasDiagram;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public boolean isHasDiagram() {
        return hasDiagram;
    }

    public void setHasDiagram(boolean hasDiagram) {
        this.hasDiagram = hasDiagram;
    }
}
