package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceSummary {
    private String resource;
    private int pathways;

    public ResourceSummary() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getPathways() {
        return pathways;
    }

    public void setPathways(int pathways) {
        this.pathways = pathways;
    }
}
