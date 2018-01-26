package org.reactome.server.tools.analysis.exporter.playground.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifiersWasFound {
    private String pathway;
    private Identifier[] entities;
    private String[] expNames;

    public IdentifiersWasFound() {
    }

    public String getPathway() {
        return pathway;
    }

    public void setPathway(String pathway) {
        this.pathway = pathway;
    }

    public Identifier[] getEntities() {
        return entities;
    }

    public void setEntities(Identifier[] entities) {
        this.entities = entities;
    }

    public String[] getExpNames() {
        return expNames;
    }

    public void setExpNames(String[] expNames) {
        this.expNames = expNames;
    }

    @Override
    public String toString() {
        return "IdentifiersWasFound{" +
                "pathway='" + pathway + '\'' +
                ", entities=" + Arrays.toString(entities) +
                ", expNames=" + Arrays.toString(expNames) +
                '}';
    }
}
