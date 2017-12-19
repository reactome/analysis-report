package org.reactome.server.tools.analysis.exporter.playground.model._;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class MapsTo {
    private String resource;
    private String[] ids;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "MapsTo{" +
                "resource='" + resource + '\'' +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
