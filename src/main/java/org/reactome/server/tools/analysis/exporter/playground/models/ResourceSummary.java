package org.reactome.server.tools.analysis.exporter.playground.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceSummary {
    private int pathways;

    public ResourceSummary() {
    }

    public int getPathways() {
        return pathways;
    }

    public void setPathways(int pathways) {
        this.pathways = pathways;
    }
}
