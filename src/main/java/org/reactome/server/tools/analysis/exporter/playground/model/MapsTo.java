package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapsTo {
    private String resource;
    private Set<String> ids;

    public MapsTo() {

    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Set<String> getIds() {
        return ids;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "MapsTo{" +
                "resource='" + resource + '\'' +
                ", ids=" + ids +
                '}';
    }
}
