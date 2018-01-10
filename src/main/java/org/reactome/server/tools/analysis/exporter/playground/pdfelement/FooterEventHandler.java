package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class FooterEventHandler implements IEventHandler {

    private PdfFont font;
    private float margin;

    public FooterEventHandler(PdfFont font, float margin) {
        this.font = font;
        this.margin = margin;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        PdfPage page = pdfDocumentEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText()
                .setFontAndSize(font, FontSize.H6)
                .moveText(margin * 2 / 3, margin * 2 / 3)
                .showText("Reactome.org")
                .moveText(page.getPageSize().getWidth() / 2 - 35, 0)
                .showText(String.format("- %s -", pdfDocument.getPageNumber(page)))
                .endText()
                .release();
    }
}
