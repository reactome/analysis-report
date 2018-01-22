package org.reactome.server.tools.analysis.exporter.playground.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import java.io.FileOutputStream;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
// TODO: 06/12/17 this class need to refactor to fit the correct data from reactome server once another part was done
public class DataSet {
    private int version;
    // TODO: 11/01/18 need to get from server
    private int totalPathways;
    private int numOfPathwaysToShow;
    @JsonIgnore
    private ReportArgs reportArgs;
    private ResultAssociatedWithToken resultAssociatedWithToken;
    private IdentifiersWasFound[] identifiersWasFounds;
    private Identifier[] identifiersWasNotFounds;
    private Map<String, Identifier> identifiersWasFiltered;
    private FileOutputStream file;

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

    public int getTotalIdentifiers() {
        return getIdentifiersWasFound() + getIdentifiersWasNotFound();
    }

    public int getIdentifiersWasFound() {
        return identifiersWasFiltered.size();
    }

    public int getIdentifiersWasNotFound() {
        return identifiersWasNotFounds.length;
    }

    public int getNumOfPathwaysToShow() {
        return numOfPathwaysToShow;
    }

    public void setNumOfPathwaysToShow(int numOfPathwaysToShow) {
        this.numOfPathwaysToShow = numOfPathwaysToShow <= resultAssociatedWithToken.getPathwaysFound() ? numOfPathwaysToShow : resultAssociatedWithToken.getPathwaysFound();
    }

    public ReportArgs getReportArgs() {
        return reportArgs;
    }

    public void setReportArgs(ReportArgs reportArgs) {
        this.reportArgs = reportArgs;
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

    public IdentifiersWasFound[] getIdentifiersWasFounds() {
        return identifiersWasFounds;
    }

    public void setIdentifiersWasFounds(IdentifiersWasFound[] identifiersWasFounds) {
        this.identifiersWasFounds = identifiersWasFounds;
    }

    public FileOutputStream getFile() {
        return file;
    }

    public void setFile(FileOutputStream file) {
        this.file = file;
    }

    public void release() {
        reportArgs = null;
        resultAssociatedWithToken = null;
        identifiersWasFounds = null;
        identifiersWasNotFounds = null;
        identifiersWasFiltered = null;
    }
}
