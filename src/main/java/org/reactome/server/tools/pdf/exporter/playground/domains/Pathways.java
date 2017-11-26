package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pathways {
    private String stId;
    private int dbId;
    private String name;
    private Species species;
    private boolean llp;
    private Entities entities;
    private Reactions reactions;

    public Pathways() {
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public boolean isLlp() {
        return llp;
    }

    public void setLlp(boolean llp) {
        this.llp = llp;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }
}
