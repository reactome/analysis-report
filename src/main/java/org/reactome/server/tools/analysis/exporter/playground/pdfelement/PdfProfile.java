package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.NoSuchPageSizeException;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

    private Float margin = 30.0F;
    private Integer numberOfPathwaysToShow = 50;
    private Float multipliedLeading = 1.0F;
    private String titleColor = "#000000";
    private String paragraphColor = "#000000";
    private String tableColor = "#000000";
    private String pageSize = "A4";
    private String font = "Helvetica";

    public Float getMargin() {
        return margin;
    }

    public void setMargin(Float margin) {
        this.margin = margin;
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
                "margin=" + margin +
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