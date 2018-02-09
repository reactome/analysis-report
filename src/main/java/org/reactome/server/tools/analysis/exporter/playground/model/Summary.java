package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {
    private String token;
    private Boolean projection;
    private Boolean interactors;
    private String type;
    private String sampleName;

    public Summary() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getProjection() {
        return projection;
    }

    public void setProjection(Boolean projection) {
        this.projection = projection;
    }

    public Boolean getInteractors() {
        return interactors;
    }

    public void setInteractors(Boolean interactors) {
        this.interactors = interactors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "token='" + token + '\'' +
                ", projection=" + projection +
                ", interactors=" + interactors +
                ", type='" + type + '\'' +
                ", sampleName='" + sampleName + '\'' +
                '}';
    }
}
