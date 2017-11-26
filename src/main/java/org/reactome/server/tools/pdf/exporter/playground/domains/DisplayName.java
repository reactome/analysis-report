package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisplayName {
    private String displayName;
    public DisplayName(){}

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
