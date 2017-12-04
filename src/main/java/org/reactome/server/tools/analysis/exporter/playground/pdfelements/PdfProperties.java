package org.reactome.server.tools.analysis.exporter.playground.pdfelements;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;

import java.io.IOException;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfProperties {
    private static int margin = 30;
    private static int numberOfPathwaysToShow = 50;
    private static boolean immediateFlush = true;
    private static String token;
    private static PdfFont font = null;
    private static PageSize pageSize = PageSize.A4;

    public PdfProperties(String token) {
        setToken(token);
    }

    public int getMargin() {
        return margin;
    }

    public PdfProperties setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getNumberOfPathwaysToShow() {
        return numberOfPathwaysToShow;
    }

    public PdfProperties setNumberOfPathwaysToShow(int numberOfPathwaysToShow) {
        this.numberOfPathwaysToShow = numberOfPathwaysToShow;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PdfProperties setToken(String token) {
        this.token = token;
        return this;
    }

    public PdfFont getFont() {

        if (font == null)
            try {
                this.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
            } catch (IOException e) {
            }
        return font;
    }

    public PdfProperties setFont(PdfFont font) {
        this.font = font;
        return this;
    }

    public PageSize getPageSize() {
        return pageSize;
    }

    public PdfProperties setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    public PdfProperties setImmediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
        return this;
    }
}
