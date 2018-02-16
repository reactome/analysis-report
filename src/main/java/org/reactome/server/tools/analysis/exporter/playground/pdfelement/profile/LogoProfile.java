package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class LogoProfile {
    private float logoScaling;
    private float marginTop;
    private float marginLeft;

    public float getLogoScaling() {
        return logoScaling;
    }

    public void setLogoScaling(float logoScaling) {
        this.logoScaling = logoScaling;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    @Override
    public String toString() {
        return "LogoProfile{" +
                "logoScaling=" + logoScaling +
                ", marginTop=" + marginTop +
                ", marginLeft=" + marginLeft +
                '}';
    }
}
