package org.reactome.server.tools.analysis.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

    private String id;
    private Double[] exp;
    private ArrayList<MapsTo> mapsTo;
    private Map<String,String> resourceMapsToIds = new HashMap<>();

    public Identifier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double[] getExp() {
        return exp;
    }

    public void setExp(Double[] exp) {
        this.exp = exp;
    }

    public ArrayList<MapsTo> getMapsTo() {
        return mapsTo;
    }

    public void setMapsTo(ArrayList<MapsTo> mapsTo) {
        this.mapsTo = mapsTo;
    }

    public Map<String, String> getResourceMapsToIds() {
        return resourceMapsToIds;
    }

    public void setResourceMapsToIds(Map<String, String> resourceMapsToIds) {
        this.resourceMapsToIds = resourceMapsToIds;
    }
}
