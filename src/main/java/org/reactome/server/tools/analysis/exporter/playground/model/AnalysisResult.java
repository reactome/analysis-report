package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisResult {
    private Summary summary;
    private Expression expression;
    private Integer pathwaysFound;
    private List<Pathway> pathways = null;

    public AnalysisResult() {
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

    public Integer getPathwaysFound() {
        return pathwaysFound;
    }

    public void setPathwaysFound(Integer pathwaysFound) {
        this.pathwaysFound = pathwaysFound;
    }

    public List<Pathway> getPathways() {
        return pathways;
    }

    public void setPathways(List<Pathway> pathways) {
        this.pathways = pathways;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                ", expression=" + expression +
                ", pathwaysFound=" + pathwaysFound +
                ", pathways=" + pathways +
                '}';
    }
}
