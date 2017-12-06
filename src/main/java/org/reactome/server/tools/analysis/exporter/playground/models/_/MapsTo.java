package org.reactome.server.tools.analysis.exporter.playground.models._;

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
}
