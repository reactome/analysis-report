package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToCreateFontException;

import java.io.IOException;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class PdfProfile {

    private static Integer margin = 30;
    private static Integer numberOfPathwaysToShow = 1;
    private static Float multipliedLeading = 1.0f;
    //    private static Boolean immediateFlush = false;
    private static Color titleColor = Color.BLACK;
    private static Color paragraphColor = Color.BLACK;
    private static Color tableColor = Color.BLACK;
    //    private static PageSize pageSize = PageSize.A4;
    // PdfFont can't be static or may lead to some bug from itext
    private PdfFont font = null;

    public int getMargin() {
        return margin;
    }

    public PdfProfile setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getNumberOfPathwaysToShow() {
        return numberOfPathwaysToShow;
    }

    public PdfProfile setNumberOfPathwaysToShow(int numberOfPathwaysToShow) {
        this.numberOfPathwaysToShow = numberOfPathwaysToShow;
        return this;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    public Color getParagraphColor() {
        return paragraphColor;
    }

    public void setParagraphColor(Color paragraphColor) {
        this.paragraphColor = paragraphColor;
    }

    public Color getTableColor() {
        return tableColor;
    }

    public void setTableColor(Color tableColor) {
        this.tableColor = tableColor;
    }

    public float getMultipliedLeading() {
        return multipliedLeading;
    }

    public void setMultipliedLeading(float multipliedLeading) {
        this.multipliedLeading = multipliedLeading;
    }


    // TODO: 01/01/18 create font by config file
    public PdfFont getFont() throws Exception {
        try {
            this.setFont(PdfFontFactory.createFont("Helvetica"));
//                this.setFont(PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA), PdfEncodings.WINANSI,false));
        } catch (IOException e) {
            throw new FailToCreateFontException("Fail to create PdfFont.", e);
        }
        return font;
    }

    public PdfProfile setFont(PdfFont font) {
        this.font = font;
        return this;
    }

//    public PageSize getPageSize() {
//        return pageSize;
//    }
//
//    public PdfProfile.json setPageSize(PageSize pageSize) {
//        this.pageSize = pageSize;
//        return this;
//    }

//    public boolean isImmediateFlush() {
//        return immediateFlush;
//    }
//
//    public PdfProfile.json setImmediateFlush(boolean immediateFlush) {
//        this.immediateFlush = immediateFlush;
//        return this;
//    }

    @Override
    public String toString() {
        return "PdfProfile{" +
                "font=" + font +
                '}';
    }
}