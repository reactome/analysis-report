package org.reactome.server.tools.analysis.exporter.playground.models._;

import java.util.Arrays;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class _DataSet {

    private int version;
    private int identifiersFound;
    private int identifiersNotFound;
    private int pathwaysFound;
    private int pathwaysTotal;
    private String[] expNames;
    private Overview[] overview ;
    private Details[] details;
    private FoundAll[] foundAll;
    private Identifier[] notFound;

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

    public String[] getExpNames() {
        return expNames;
    }

    public void setExpNames(String[] expNames) {
        this.expNames = expNames;
    }

    public Overview[] getOverview() {
        return overview;
    }

    public void setOverview(Overview[] overview) {
        this.overview = overview;
    }

    public Details[] getDetails() {
        return details;
    }

    public void setDetails(Details[] details) {
        this.details = details;
    }

    public FoundAll[] getFoundAll() {
        return foundAll;
    }

    public void setFoundAll(FoundAll[] foundAll) {
        this.foundAll = foundAll;
    }

    public Identifier[] getNotFound() {
        return notFound;
    }

    public void setNotFound(Identifier[] notFound) {
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
                ", expNames=" + Arrays.toString(expNames) +
                ", overview=" + Arrays.toString(overview) +
                ", details=" + Arrays.toString(details) +
                ", foundAll=" + Arrays.toString(foundAll) +
                ", notFound=" + Arrays.toString(notFound) +
                '}';
    }
}
