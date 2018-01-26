package org.reactome.server.tools.analysis.exporter.playground.domain.model;

import org.reactome.server.graph.domain.model.*;
import org.reactome.server.graph.domain.model.Pathway;

import java.util.List;
import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PathwayDetail {
    private List<Summation> summations;
    private List<Compartment> compartments;
    private List<Disease> diseases;
    private Set<Event> events;
    private List<InstanceEdit> edited;
    private InstanceEdit modified;
    private List<InstanceEdit> authored;
    private List<Publication> publications;

    public PathwayDetail(Pathway pathway) {
        this.summations = pathway.getSummation();
        this.compartments = pathway.getCompartment();
        this.diseases = pathway.getDisease();
        this.events = pathway.getInferredFrom();
        this.edited = pathway.getEdited() != null ? pathway.getEdited() : null;
        this.modified = pathway.getModified() != null ? pathway.getModified() : null;
        this.authored = pathway.getAuthored() != null ? pathway.getAuthored() : null;
        this.publications = pathway.getLiteratureReference();
    }

    public List<Summation> getSummations() {
        return summations;
    }

    public List<Compartment> getCompartments() {
        return compartments;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public List<InstanceEdit> getEdited() {
        return edited;
    }

    public InstanceEdit getModified() {
        return modified;
    }

    public List<InstanceEdit> getAuthored() {
        return authored;
    }

    public List<Publication> getPublications() {
        return publications;
    }
}
