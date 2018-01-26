package org.reactome.server.tools.analysis.exporter.playground.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultAssociatedWithToken {
    private Summary summary;
    private Expression expression;

    private int identifiersNotFound;
    private int pathwaysFound;
    private Pathway[] pathways = null;

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
}
