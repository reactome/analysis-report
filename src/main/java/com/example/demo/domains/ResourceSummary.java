package com.example.demo.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
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
