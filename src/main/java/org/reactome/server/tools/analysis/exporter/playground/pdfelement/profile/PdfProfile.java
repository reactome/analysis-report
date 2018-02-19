package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

import com.itextpdf.kernel.font.PdfFont;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

    private String font;
    private PdfFont pdfFont;
    private Integer fontSize;
    private Integer pathwaysToShow;
    private MarginProfile margin;
    private ParagraphProfile paragraph;

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public PdfFont getPdfFont() {
        try {
            pdfFont = PdfUtils.createFont(getFont());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFont;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getPathwaysToShow() {
        return pathwaysToShow;
    }

    public void setPathwaysToShow(Integer pathwaysToShow) {
        this.pathwaysToShow = pathwaysToShow;
    }

    public MarginProfile getMargin() {
        return margin;
    }

    public void setMargin(MarginProfile margin) {
        this.margin = margin;
    }

    public ParagraphProfile getParagraph() {
        return paragraph;
    }

    public void setParagraph(ParagraphProfile paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public String toString() {
        return "PdfProfile{" +
                "font='" + font + '\'' +
                ", fontSize=" + fontSize +
                ", pathwaysToShow=" + pathwaysToShow +
                ", margin=" + margin +
                ", paragraph=" + paragraph +
                '}';
    }
}