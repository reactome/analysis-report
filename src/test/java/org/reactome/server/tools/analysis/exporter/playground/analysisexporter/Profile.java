package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import java.util.Arrays;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Profile {
    float[] titleColor = new float[3];
    float[] paragraphColor = new float[3];
    float[] tableColor = new float[3];
    private int margin = 30;
    private int numberOfPathwaysToShow = 1;
    private float multipliedLeading = 1.0f;
    private String font = "Helvetica";
    private String pageSize = "A4";

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getNumberOfPathwaysToShow() {
        return numberOfPathwaysToShow;
    }

    public Profile setNumberOfPathwaysToShow(int numberOfPathwaysToShow) {
        this.numberOfPathwaysToShow = numberOfPathwaysToShow;
        return this;
    }

    public float getMultipliedLeading() {
        return multipliedLeading;
    }

    public void setMultipliedLeading(float multipliedLeading) {
        this.multipliedLeading = multipliedLeading;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public float[] getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(float[] titleColor) {
        this.titleColor = titleColor;
    }

    public float[] getParagraphColor() {
        return paragraphColor;
    }

    public void setParagraphColor(float[] paragraphColor) {
        this.paragraphColor = paragraphColor;
    }

    public float[] getTableColor() {
        return tableColor;
    }

    public void setTableColor(float[] tableColor) {
        this.tableColor = tableColor;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "margin=" + margin +
                ", numberOfPathwaysToShow=" + numberOfPathwaysToShow +
                ", multipliedLeading=" + multipliedLeading +
                ", font='" + font + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", titleColor=" + Arrays.toString(titleColor) +
                ", paragraphColor=" + Arrays.toString(paragraphColor) +
                ", tableColor=" + Arrays.toString(tableColor) +
                '}';
    }
}
