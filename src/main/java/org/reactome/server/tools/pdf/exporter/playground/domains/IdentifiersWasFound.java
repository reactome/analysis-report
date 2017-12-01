package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifiersWasFound {
    private String pathway;
    private Identifier[] entities;
    private String[] expNames;
    private int foundEntities;

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

    public int getFoundEntities() {
        return foundEntities;
    }

    public void setFoundEntities(int foundEntities) {
        this.foundEntities = foundEntities;
    }
}
