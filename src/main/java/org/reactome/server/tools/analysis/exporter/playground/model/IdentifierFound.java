package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifierFound {
    private String pathway;
    private List<Identifier> entities;
    private List<String> expNames;

    public IdentifierFound() {
    }

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public List<Identifier> getEntities() {
        return entities;
    }

    public void setEntities(List<Identifier> entities) {
        this.entities = entities;
    }

    public List<String> getExpNames() {
        return expNames;
    }

    public void setExpNames(List<String> expNames) {
        this.expNames = expNames;
    }

    @Override
    public String toString() {
        return "IdentifierFound{" +
                "pathway='" + pathway + '\'' +
                ", entities=" + entities +
                ", expNames=" + expNames +
                '}';
    }
}
