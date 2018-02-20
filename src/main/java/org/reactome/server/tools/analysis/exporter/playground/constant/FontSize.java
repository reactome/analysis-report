package org.reactome.server.tools.analysis.exporter.playground.constant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FontSize {

    // H1..H6, P, TITLE, [TABLE]
    public static int P;
    public static int H1;
    public static int H2;
    public static int H3;
    public static int H4;
    public static int H5;
    public static int H6;
    public static int TABLE;
    public static int TITLE;

    public static void setUp(int fontSize) {
        TABLE = fontSize;
        H6 = TABLE + 4;
        H5 = H6 + 2;
        H4 = H5 + 2;
        H3 = H4 + 2;
        H2 = H3 + 2;
        H1 = H2 + 2;
        TITLE = H1 + 2;
        P = H5;
    }
}
