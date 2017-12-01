package org.reactome.server.tools.analysis.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String displayName;
    private String stId;
    private boolean isInDisease;
    private boolean isInferred;
    private boolean hasDiagram;

    public Event() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public boolean isInDisease() {
        return isInDisease;
    }

    public void setInDisease(boolean inDisease) {
        isInDisease = inDisease;
    }

    public boolean isInferred() {
        return isInferred;
    }

    public void setInferred(boolean inferred) {
        isInferred = inferred;
    }

    public boolean isHasDiagram() {
        return hasDiagram;
    }

    public void setHasDiagram(boolean hasDiagram) {
        this.hasDiagram = hasDiagram;
    }
}
