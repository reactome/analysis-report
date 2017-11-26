package org.reactome.server.tools.pdf.exporter.playground.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Expression {
    private String[] columnNames;

    public Expression() {
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String toString() {
        return "expression{" +
                "columnNames=" + columnNames +
                "}";
    }
}
