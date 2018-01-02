package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultAssociatedWithToken {
    private Summary summary;
    private Expression expression;
    private int identifiersNotFound;
    private Pathway[] pathways = null;
    private ResourceSummary[] resourceSummary;

    public ResultAssociatedWithToken() {
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public int getIdentifiersNotFound() {
        return identifiersNotFound;
    }

    public void setIdentifiersNotFound(int identifiersNotFound) {
        this.identifiersNotFound = identifiersNotFound;
    }

    public Pathway[] getPathways() {
        return pathways;
    }

    public void setPathways(Pathway[] pathways) {
        this.pathways = pathways;
    }

    public ResourceSummary[] getResourceSummary() {
        return resourceSummary;
    }

    public void setResourceSummary(ResourceSummary[] resourceSummary) {
        this.resourceSummary = resourceSummary;
    }

}
