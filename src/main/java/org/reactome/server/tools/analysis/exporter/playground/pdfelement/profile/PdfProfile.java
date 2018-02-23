package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

    private Integer fontSize = 6;
    private Integer pathwaysToShow = 25;
    private MarginProfile margin;
    private ParagraphProfile paragraph;

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
                ", fontSize=" + fontSize +
                ", pathwaysToShow=" + pathwaysToShow +
                ", margin=" + margin +
                ", paragraph=" + paragraph +
                '}';
    }
}