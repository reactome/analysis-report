package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

    private Integer margin;
    private Integer numberOfPathwaysToShow;
    private Float multipliedLeading;
    private Color titleColor;
    private Color paragraphColor;
    private Color tableColor;
    private PageSize pageSize;
    private String fontName;
    private PdfFont font;

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer margin) {
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
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    public Color getParagraphColor() {
        return paragraphColor;
    }

    public void setParagraphColor(Color paragraphColor) {
        this.paragraphColor = paragraphColor;
    }

    public Color getTableColor() {
        return tableColor;
    }

    public void setTableColor(Color tableColor) {
        this.tableColor = tableColor;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public PdfFont getFont() {
        return font;
    }

    public void setFont(PdfFont font) {
        this.font = font;
    }

    @Override
    public String toString() {
        return "PdfProfile{" +
                "margin=" + margin +
                ", numberOfPathwaysToShow=" + numberOfPathwaysToShow +
                ", multipliedLeading=" + multipliedLeading +
//                ", titleColor=" + titleColor +
//                ", paragraphColor=" + paragraphColor +
//                ", tableColor=" + tableColor +
                ", fontName='" + fontName + '\'' +
//                ", font=" + font +
                '}';
    }
}