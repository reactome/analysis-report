package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToCreateFontException;

import java.io.IOException;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfProperties {
    private static int margin = 30;
    private static int numberOfPathwaysToShow = 50;
    private static boolean immediateFlush = false;
    private static String token;
    private static PageSize pageSize = PageSize.A4;
    //PdfFont can't be static or may lead to some bug from itext
    private PdfFont font = null;

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

    public PdfFont getFont() throws Exception {
        try {
            if (font == null) {
                this.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));
//                this.setFont(PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA), PdfEncodings.WINANSI,false));
            }
        } catch (IOException e) {
            throw new FailToCreateFontException("Fail to create PdfFont.", e);
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