package org.reactome.server.tools.analysis.exporter.playground.model;

import com.itextpdf.layout.element.Image;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;

import java.util.List;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class DataSet {
    private Integer DBVersion;
    // TODO: 11/01/18 need to get from server
    private Integer totalPathways;
    private Integer pathwaysToShow;
    private Image linkIcon;
    private ReportArgs reportArgs;
    private AnalysisResult analysisResult;
    private List<IdentifierFound> identifierFounds;
    private List<Identifier> identifierNotFounds;
    private Map<String, Identifier> identifierFiltered;

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
        return getIdentifierFound() + getIdentifierNotFound();
    }

    public int getIdentifierFound() {
        return identifierFiltered.size();
    }

    public int getIdentifierNotFound() {
        return identifierNotFounds.size();
    }

    public Integer getPathwaysToShow() {
        return pathwaysToShow;
    }

    public void setPathwaysToShow(Integer pathwaysToShow) {
        this.pathwaysToShow = pathwaysToShow;
    }

    public Image getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(Image linkIcon) {
        this.linkIcon = linkIcon;
    }

    public ReportArgs getReportArgs() {
        return reportArgs;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public List<Identifier> getIdentifierNotFounds() {
        return identifierNotFounds;
    }

    public void setIdentifierNotFounds(List<Identifier> identifiersWasNotFounds) {
        this.identifierNotFounds = identifiersWasNotFounds;
    }

    public Map<String, Identifier> getIdentifierFiltered() {
        return identifierFiltered;
    }

    public void setIdentifierFiltered(Map<String, Identifier> identifiersWasFiltered) {
        this.identifierFiltered = identifiersWasFiltered;
    }

    public List<IdentifierFound> getIdentifierFounds() {
        return identifierFounds;
    }

    public void setIdentifierFounds(List<IdentifierFound> identifierFounds) {
        this.identifierFounds = identifierFounds;
    }

    public void release() {
        reportArgs = null;
        analysisResult = null;
        identifierFounds = null;
        identifierNotFounds = null;
        identifierFiltered = null;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "version=" + DBVersion +
                ", totalPathways=" + totalPathways +
                ", pathwaysToShow=" + pathwaysToShow +
                ", reportArgs=" + reportArgs +
                ", analysisResult=" + analysisResult +
                ", identifierFounds=" + identifierFounds +
                ", identifiersWasNotFounds=" + identifierNotFounds +
                ", identifiersWasFiltered=" + identifierFiltered +
                '}';
    }
}
