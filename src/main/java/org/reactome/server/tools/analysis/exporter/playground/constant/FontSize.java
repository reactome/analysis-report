package org.reactome.server.tools.analysis.exporter.playground.constant;

/**
 * Contains the constant value of font size.
 * <p>just set the font size of paragraph and all anther font size will been set relative to that.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FontSize {

    public static int P;
    public static int H1;
    public static int H2;
    public static int H3;
    public static int TABLE;
    public static int TITLE;

    public static void setUp(int fontSize) {
        P = fontSize;
        TABLE = P - 2;
        TITLE = P + 12;
        H3 = P + 3;
        H2 = H3 + 3;
        H1 = H2 + 3;
    }
}