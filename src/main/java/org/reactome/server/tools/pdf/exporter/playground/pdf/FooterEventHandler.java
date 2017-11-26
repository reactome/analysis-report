package org.reactome.server.tools.pdf.exporter.playground.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.springframework.stereotype.Component;

/**
 * @author Chuan Deng <cdeng@ebi.ac.uk>
 */
@Component
public class FooterEventHandler implements IEventHandler {

    private float margin;
    private PdfFont font;

    public FooterEventHandler() {
    }

    public FooterEventHandler(PdfFont font, float margin) {
        this.font = font;
        this.margin = margin;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        PdfPage page = pdfDocumentEvent.getPage();
        int pageNumber = pdfDocument.getPageNumber(page);
        int numberOfPages = pdfDocument.getNumberOfPages();
//        float x = page.getPageSize().getLeft();
//        float y = page.getPageSize().getBottom();
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText()
                .setFontAndSize(font, 11)
                .moveText(margin * 2 / 3, margin * 2 / 3)
//                    .setColor(Color.BLUE,true)
                .showText("Reactome.org")
//                    .setColor(Color.BLACK,true)
                .moveText(page.getPageSize().getWidth() / 2 - 35, 0)
                .showText("- " + pageNumber + " -")
                .endText()
                .release();
    }
}
