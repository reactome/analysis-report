package org.reactome.server.tools.analysis.exporter.playground.pdfelement.profile;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class MarginProfile {
    private Float top;
    private Float bottom;
    private Float left;
    private Float right;

    public Float getTop() {
        return top;
    }

    public void setTop(Float top) {
        this.top = top;
    }

    public Float getBottom() {
        return bottom;
    }

    public void setBottom(Float bottom) {
        this.bottom = bottom;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getRight() {
        return right;
    }

    public void setRight(Float right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "MarginProfile{" +
                "top=" + top +
                ", bottom=" + bottom +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
