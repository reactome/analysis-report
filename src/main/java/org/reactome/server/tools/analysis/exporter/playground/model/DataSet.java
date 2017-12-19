package org.reactome.server.tools.analysis.exporter.playground.model;

import java.util.Map;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
// TODO: 06/12/17 this class need to refactor to fit the correct data from reactome server once another part was done
public class DataSet {
    IdentifiersWasFound[] identifiersWasFounds;
    private int version;
    private int totalPathways;
    private int identifiersWasFound;
    private int identifiersWasNotFound;
    private ResultAssociatedWithToken resultAssociatedWithToken;
    private Identifier[] identifiersWasNotFounds;
    private Map<String, Identifier> identifiersWasFiltered;
    private Pathway[] pathways;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getTotalPathways() {
        return totalPathways;
    }

    public void setTotalPathways(int totalPathways) {
        this.totalPathways = totalPathways;
    }

    public int getIdentifiersWasFound() {
        return identifiersWasFound;
    }

    public void setIdentifiersWasFound(int identifiersWasFound) {
        this.identifiersWasFound = identifiersWasFound;
    }

    public int getIdentifiersWasNotFound() {
        return identifiersWasNotFound;
    }

    public void setIdentifiersWasNotFound(int identifiersWasNotFound) {
        this.identifiersWasNotFound = identifiersWasNotFound;
    }

    public ResultAssociatedWithToken getResultAssociatedWithToken() {
        return resultAssociatedWithToken;
    }

    public void setResultAssociatedWithToken(ResultAssociatedWithToken resultAssociatedWithToken) {
        this.resultAssociatedWithToken = resultAssociatedWithToken;
    }

    public Identifier[] getIdentifiersWasNotFounds() {
        return identifiersWasNotFounds;
    }

    public void setIdentifiersWasNotFounds(Identifier[] identifiersWasNotFounds) {
        this.identifiersWasNotFounds = identifiersWasNotFounds;
    }

    public Map<String, Identifier> getIdentifiersWasFiltered() {
        return identifiersWasFiltered;
    }

    public void setIdentifiersWasFiltered(Map<String, Identifier> identifiersWasFiltered) {
        this.identifiersWasFiltered = identifiersWasFiltered;
    }

    public Pathway[] getPathways() {
        return pathways;
    }

    public void setPathways(Pathway[] pathways) {
        this.pathways = pathways;
    }

    public IdentifiersWasFound[] getIdentifiersWasFounds() {
        return identifiersWasFounds;
    }

    public void setIdentifiersWasFounds(IdentifiersWasFound[] identifiersWasFounds) {
        this.identifiersWasFounds = identifiersWasFounds;
    }

}
