package org.reactome.server.tools.analysis.exporter.playground.models._;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Entity extends Identifier {
    private MapsTo[] mapsTo;

    public MapsTo[] getMapsTo() {
        return mapsTo;
    }

    public void setMapsTo(MapsTo[] mapsTo) {
        this.mapsTo = mapsTo;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "mapsTo=" + Arrays.toString(mapsTo) +
                '}';
    }
}
