package org.reactome.server.tools.analysis.exporter.playground.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathwayDetail {

    private String speciesName;
    private boolean isInDisease;
    private boolean isInferred;
    private boolean hasDiagram;
    private Summation[] summation;
    @JsonProperty("created")
    private Curator authors;
    @JsonProperty("modified")
    private Curator editors;
    @JsonProperty("reviewed")
    private Curator[] reviewers;
    private LiteratureReference[] literatureReference;
    private DisplayName[] compartment;
    private DisplayName[] disease;
    private DisplayName[] inferredFrom;
    private Event[] hasEvent;


    public PathwayDetail() {
    }

    public boolean isHasDiagram() {
        return hasDiagram;
    }

    public void setHasDiagram(boolean hasDiagram) {
        this.hasDiagram = hasDiagram;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public boolean isInDisease() {
        return isInDisease;
    }

    public void setInDisease(boolean inDisease) {
        isInDisease = inDisease;
    }

    public boolean isInferred() {
        return isInferred;
    }

    public void setInferred(boolean inferred) {
        isInferred = inferred;
    }

    public Summation[] getSummation() {
        return summation;
    }

    public void setSummation(Summation[] summation) {
        this.summation = summation;
    }

    public Curator getAuthors() {
        return authors;
    }

    public void setAuthors(Curator authors) {
        this.authors = authors;
    }

    public Curator getEditors() {
        return editors;
    }

    public void setEditors(Curator editors) {
        this.editors = editors;
    }

    public Curator[] getReviewers() {
        return reviewers;
    }

    public void setReviewers(Curator[] reviewers) {
        this.reviewers = reviewers;
    }

    public LiteratureReference[] getLiteratureReference() {
        return literatureReference;
    }

    public void setLiteratureReference(LiteratureReference[] literatureReference) {
        this.literatureReference = literatureReference;
    }

    public DisplayName[] getCompartment() {
        return compartment;
    }

    public void setCompartment(DisplayName[] compartment) {
        this.compartment = compartment;
    }

    public DisplayName[] getDisease() {
        return disease;
    }

    public void setDisease(DisplayName[] disease) {
        this.disease = disease;
    }

    public DisplayName[] getInferredFrom() {
        return inferredFrom;
    }

    public void setInferredFrom(DisplayName[] inferredFrom) {
        this.inferredFrom = inferredFrom;
    }

    public Event[] getHasEvent() {
        return hasEvent;
    }

    public void setHasEvent(Event[] hasEvent) {
        this.hasEvent = hasEvent;
    }
}
