package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TitleProfile {
    private float marginTop;
    private float marginBottom;

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    @Override
    public String toString() {
        return "TitleProfile{" +
                "marginTop='" + marginTop + '\'' +
                ", marginBottom='" + marginBottom + '\'' +
                '}';
    }
}
