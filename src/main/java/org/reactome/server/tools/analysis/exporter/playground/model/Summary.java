package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {
    private String token;

    public Summary() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
