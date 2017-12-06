package org.reactome.server.tools.analysis.exporter.playground.models._;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FoundAll {
    private String pathway;
    private Entity[] entities;

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }
}
