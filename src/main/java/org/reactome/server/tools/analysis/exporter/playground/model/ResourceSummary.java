package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
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
