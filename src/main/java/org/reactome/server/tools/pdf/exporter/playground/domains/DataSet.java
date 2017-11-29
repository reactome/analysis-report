package org.reactome.server.tools.pdf.exporter.playground.domains;

import java.util.Map;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class DataSet {
    private int version;
    private int numbetOfPathways;
    private int identifiersWasFound;
    private int identifiersWasNotFound;
    private ResultAssociatedWithToken resultAssociatedWithToken;
    private String token;
    private Identifier[] identifiersWasNotFounds;
    private Map<String, Identifier> identifiersWasFiltered;
    private Pathway[] pathways;
    IdentifiersWasFound[] identifiersWasFounds;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getNumbetOfPathways() {
        return numbetOfPathways;
    }

    public void setNumbetOfPathways(int numbetOfPathways) {
        this.numbetOfPathways = numbetOfPathways;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
