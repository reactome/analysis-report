package org.reactome.server.tools.analysis.exporter.playground.models._;

import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Entity extends Identifier {
    private List<MapsTo> mapsTo;

    public List<MapsTo> getMapsTo() {
        return mapsTo;
    }

    public void setMapsTo(List<MapsTo> mapsTo) {
        this.mapsTo = mapsTo;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "mapsTo=" + mapsTo +
                '}';
    }
}
