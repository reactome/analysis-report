package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {
    private String token;
    private boolean projection;
    private boolean interactors;
    private String type;
    private String sampleName;
    private boolean text;

    public Summary() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isProjection() {
        return projection;
    }

    public void setProjection(boolean projection) {
        this.projection = projection;
    }

    public boolean isInteractors() {
        return interactors;
    }

    public void setInteractors(boolean interactors) {
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

    public boolean isText() {
        return text;
    }

    public void setText(boolean text) {
        this.text = text;
    }
}
