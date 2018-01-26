package org.reactome.server.tools.analysis.exporter.playground.domain.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PdfProfile {

    private LogoProfile logoProfile;
    private TitleProfile titleProfile;
    private Float topMargin = 30.0F;
    private Float rightMargin = 30.0F;
    private Float bottomMargin = 30.0F;
    private Float leftMargin = 30.0F;
    private Integer numberOfPathwaysToShow = 50;
    private Float multipliedLeading = 1.0F;
    private String titleColor = "#000000";
    private String paragraphColor = "#000000";
    private String tableColor = "#000000";
    private String pageSize = "A4";
    private String font = "Helvetica";

    // TODO: 22/01/18 extract another more properties to profile file


    public LogoProfile getLogoProfile() {
        return logoProfile;
    }

    public void setLogoProfile(LogoProfile logoProfile) {
        this.logoProfile = logoProfile;
    }

    public TitleProfile getTitleProfile() {
        return titleProfile;
    }

    public void setTitleProfile(TitleProfile titleProfile) {
        this.titleProfile = titleProfile;
    }

    public Float getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Float topMargin) {
        this.topMargin = topMargin;
    }

    public Float getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Float rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Float getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Float bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public Float getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Float leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Integer getNumberOfPathwaysToShow() {
        return numberOfPathwaysToShow;
    }

    public void setNumberOfPathwaysToShow(Integer numberOfPathwaysToShow) {
        this.numberOfPathwaysToShow = numberOfPathwaysToShow;
    }

    public Float getMultipliedLeading() {
        return multipliedLeading;
    }

    public void setMultipliedLeading(Float multipliedLeading) {
        this.multipliedLeading = multipliedLeading;
    }

    public Color getTitleColor() {
        return PdfUtils.createColor(titleColor);
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public Color getParagraphColor() {
        return PdfUtils.createColor(paragraphColor);
    }

    public void setParagraphColor(String paragraphColor) {
        this.paragraphColor = paragraphColor;
    }

    public Color getTableColor() {
        return PdfUtils.createColor(tableColor);
    }

    public void setTableColor(String tableColor) {
        this.tableColor = tableColor;
    }

    public PageSize getPageSize() throws NoSuchPageSizeException {
        return PdfUtils.createPageSize(pageSize);
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public PdfFont getFont() throws Exception {
        return PdfUtils.createFont(font);
    }

    public void setFont(String font) {
        this.font = font;
    }

    @Override
    public String toString() {
        return "PdfProfile{" +
                "logoProfile=" + logoProfile +
                ", titleProfile=" + titleProfile +
                ", topMargin=" + topMargin +
                ", rightMargin=" + rightMargin +
                ", bottomMargin=" + bottomMargin +
                ", leftMargin=" + leftMargin +
                ", numberOfPathwaysToShow=" + numberOfPathwaysToShow +
                ", multipliedLeading=" + multipliedLeading +
                ", titleColor='" + titleColor + '\'' +
                ", paragraphColor='" + paragraphColor + '\'' +
                ", tableColor='" + tableColor + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", font='" + font + '\'' +
                '}';
    }
}