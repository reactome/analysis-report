package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)

public class Entity {
    private String[] ids;
    private String resource;

    public Entity(){

    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
