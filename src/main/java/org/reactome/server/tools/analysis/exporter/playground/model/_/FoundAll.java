package org.reactome.server.tools.analysis.exporter.playground.model._;

import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class FoundAll {
    private String pathway;
    private List<Entity> entities;

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "FoundAll{" +
                "pathway='" + pathway + '\'' +
                ", entities=" + entities +
                '}';
    }
}
