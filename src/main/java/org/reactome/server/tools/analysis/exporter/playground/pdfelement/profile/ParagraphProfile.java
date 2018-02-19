package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ParagraphProfile {
    private Float lineSpacing;
    private Float topLeading;
    private Float bottomLeading;

    public Float getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(Float lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public Float getTopLeading() {
        return topLeading;
    }

    public void setTopLeading(Float topLeading) {
        this.topLeading = topLeading;
    }

    public Float getBottomLeading() {
        return bottomLeading;
    }

    public void setBottomLeading(Float bottomLeading) {
        this.bottomLeading = bottomLeading;
    }

    @Override
    public String toString() {
        return "ParagraphProfile{" +
                "lineSpacing=" + lineSpacing +
                ", topLeading=" + topLeading +
                ", bottomLeading=" + bottomLeading +
                '}';
    }
}
