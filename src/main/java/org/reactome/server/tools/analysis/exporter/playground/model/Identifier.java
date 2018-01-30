package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

    private String id;
    private List<Float> exp;
    private List<MapsTo> mapsTo;
    private Map<String, String> resourceMapsToIds = new HashMap<String, String>();

    public Identifier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Float> getExp() {
        return exp;
    }

    public void setExp(List<Float> exp) {
        this.exp = exp;
    }

    public List<MapsTo> getMapsTo() {
        return mapsTo;
    }

    public void setMapsTo(List<MapsTo> mapsTo) {
        this.mapsTo = mapsTo;
    }

    public Map<String, String> getResourceMapsToIds() {
        return resourceMapsToIds;
    }

    public void setResourceMapsToIds(Map<String, String> resourceMapsToIds) {
        this.resourceMapsToIds = resourceMapsToIds;
    }

    @Override
    public String toString() {
        return id;
    }
}
