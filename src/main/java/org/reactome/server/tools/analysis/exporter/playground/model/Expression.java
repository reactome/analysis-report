package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Expression {
    private List<String> columnNames;

    public Expression() {
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String toString() {
        return "Expression{" +
                "columnNames=" + columnNames +
                '}';
    }
}