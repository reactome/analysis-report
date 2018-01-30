package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pathway {
    private String stId;
    private String name;
    private Species species;
    @JsonProperty("entities")
    private Entity entity;
    @JsonProperty("reactions")
    private Reaction reaction;

    public Pathway() {
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    @Override
    public String toString() {
        return "Pathway{" +
                "stId='" + stId + '\'' +
                ", name='" + name + '\'' +
                ", species=" + species +
                ", entity=" + entity +
                ", reaction=" + reaction +
                '}';
    }
}
