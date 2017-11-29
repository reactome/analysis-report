package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultAssociatedWithToken {
    private Summary summary;
    private Expression expression;
    private int identifiersNotFound;
    private int pathwaysFound;
//    private List<Pathways> pathways;
    private Pathway[] pathways;
    //    private List<ResourceSummary> resourceSummary;
    private ResourceSummary[] resourceSummary;

    private String[] warnings;

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

    public int getPathwaysFound() {
        return pathwaysFound;
    }

    public void setPathwaysFound(int pathwaysFound) {
        this.pathwaysFound = pathwaysFound;
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

    public String[] getWarnings() {
        return warnings;
    }

    public void setWarnings(String[] warnings) {
        this.warnings = warnings;
    }
}
