package org.reactome.server.tools.analysis.exporter.playground.model;

import com.itextpdf.layout.element.Image;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
// TODO: 06/12/17 this class need to refactor to fit the correct data from reactome server once another part was done
public class DataSet {
    private Integer DBVersion;
    // TODO: 11/01/18 need to get from server
    private Integer totalPathways;
    private Integer pathwaysToShow;
    private Image icon;
    private ReportArgs reportArgs;
    private FileOutputStream file;
    private AnalysisResult analysisResult;
    private List<IdentifierFound> identifierFounds;
    private List<Identifier> identifiersWasNotFounds;
    private Map<String, Identifier> identifiersWasFiltered;

    public DataSet(ReportArgs reportArgs) {
        this.reportArgs = reportArgs;
    }

    public Integer getDBVersion() {
        return DBVersion;
    }

    public void setDBVersion(Integer DBVersion) {
        this.DBVersion = DBVersion;
    }

    public Integer getTotalPathways() {
        return totalPathways;
    }

    public void setTotalPathways(Integer totalPathways) {
        this.totalPathways = totalPathways;
    }

    public int getTotalIdentifiers() {
        return getIdentifiersWasFound() + getIdentifiersWasNotFound();
    }

    public int getIdentifiersWasFound() {
        return identifiersWasFiltered.size();
    }

    public int getIdentifiersWasNotFound() {
        return identifiersWasNotFounds.size();
    }

    public Integer getPathwaysToShow() {
        return pathwaysToShow;
    }

    public void setPathwaysToShow(Integer pathwaysToShow) {
        this.pathwaysToShow = pathwaysToShow <= analysisResult.getPathwaysFound() ? pathwaysToShow : analysisResult.getPathwaysFound();
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public ReportArgs getReportArgs() {
        return reportArgs;
    }

    public void setReportArgs(ReportArgs reportArgs) {
        this.reportArgs = reportArgs;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public List<Identifier> getIdentifiersWasNotFounds() {
        return identifiersWasNotFounds;
    }

    public void setIdentifiersWasNotFounds(List<Identifier> identifiersWasNotFounds) {
        this.identifiersWasNotFounds = identifiersWasNotFounds;
    }

    public Map<String, Identifier> getIdentifiersWasFiltered() {
        return identifiersWasFiltered;
    }

    public void setIdentifiersWasFiltered(Map<String, Identifier> identifiersWasFiltered) {
        this.identifiersWasFiltered = identifiersWasFiltered;
    }

    public List<IdentifierFound> getIdentifierFounds() {
        return identifierFounds;
    }

    public void setIdentifierFounds(List<IdentifierFound> identifierFounds) {
        this.identifierFounds = identifierFounds;
    }

    public FileOutputStream getFile() {
        return file;
    }

    public void setFile(FileOutputStream file) {
        this.file = file;
    }

    public void release() {
        reportArgs = null;
        analysisResult = null;
        identifierFounds = null;
        identifiersWasNotFounds = null;
        identifiersWasFiltered = null;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "version=" + DBVersion +
                ", totalPathways=" + totalPathways +
                ", pathwaysToShow=" + pathwaysToShow +
                ", reportArgs=" + reportArgs +
                ", file=" + file +
                ", analysisResult=" + analysisResult +
                ", identifierFounds=" + identifierFounds +
                ", identifiersWasNotFounds=" + identifiersWasNotFounds +
                ", identifiersWasFiltered=" + identifiersWasFiltered +
                '}';
    }
}
