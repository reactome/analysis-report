package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

    private String id;
    private List<Float> exp;
    private List<MapsTo> mapsTo;

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

    @Override
    public String toString() {
        return id + ',';
    }
}
