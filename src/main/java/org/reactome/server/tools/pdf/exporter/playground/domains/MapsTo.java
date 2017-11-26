package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapsTo {
    private String resource;
    private ArrayList<String> ids;

    public MapsTo() {

    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
