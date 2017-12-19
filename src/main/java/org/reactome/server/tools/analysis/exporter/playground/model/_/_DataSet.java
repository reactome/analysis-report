package org.reactome.server.tools.analysis.exporter.playground.model._;

import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class _DataSet {

    private int version;
    private int identifiersFound;
    private int identifiersNotFound;
    private int pathwaysFound;
    private int pathwaysTotal;
    // TODO: 15/12/17 change array to list to use stream filter by lambda expression(parallelStream().forEach())
//    private String[] expNames;
//    private Overview[] overview ;
//    private Details[] details;
//    private FoundAll[] foundAll;
//    private Identifier[] notFound;
    private List<String> expNames;
    private List<Overview> overview;
    private List<Details> details;
    private List<FoundAll> foundAll;
    private List<Identifier> notFound;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getIdentifiersFound() {
        return identifiersFound;
    }

    public void setIdentifiersFound(int identifiersFound) {
        this.identifiersFound = identifiersFound;
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

    public int getPathwaysTotal() {
        return pathwaysTotal;
    }

    public void setPathwaysTotal(int pathwaysTotal) {
        this.pathwaysTotal = pathwaysTotal;
    }

    public List<String> getExpNames() {
        return expNames;
    }

    public void setExpNames(List<String> expNames) {
        this.expNames = expNames;
    }

    public List<Overview> getOverview() {
        return overview;
    }

    public void setOverview(List<Overview> overview) {
        this.overview = overview;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public List<FoundAll> getFoundAll() {
        return foundAll;
    }

    public void setFoundAll(List<FoundAll> foundAll) {
        this.foundAll = foundAll;
    }

    public List<Identifier> getNotFound() {
        return notFound;
    }

    public void setNotFound(List<Identifier> notFound) {
        this.notFound = notFound;
    }

    @Override
    public String toString() {
        return "_DataSet{" +
                "version=" + version +
                ", identifiersFound=" + identifiersFound +
                ", identifiersNotFound=" + identifiersNotFound +
                ", pathwaysFound=" + pathwaysFound +
                ", pathwaysTotal=" + pathwaysTotal +
                ", expNames=" + expNames +
                ", overview=" + overview +
                ", details=" + details +
                ", foundAll=" + foundAll +
                ", notFound=" + notFound +
                '}';
    }
}
