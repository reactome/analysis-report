package org.reactome.server.tools.analysis.exporter.playground.model._;

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

    @Override
    public String toString() {
        return "InferredFrom{" +
                "displayName='" + displayName + '\'' +
                ", speciesName='" + speciesName + '\'' +
                ", hasDiagram=" + hasDiagram +
                '}';
    }
}
