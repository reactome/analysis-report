package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class FooterEventHandler implements IEventHandler {

    private AnalysisReport report;

    FooterEventHandler(AnalysisReport report) {
        this.report = report;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument document = pdfDocumentEvent.getDocument();
        PdfPage page = pdfDocumentEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText()
                .setFontAndSize(report.getProfile().getFont(), FontSize.H6)
                .moveText(report.getBottomMargin() * 2 / 3, report.getBottomMargin() * 2 / 3)
                .showText("Reactome.org")
                .moveText(page.getPageSize().getWidth() / 2 - 35, 0)
                .showText(String.format("- %s -", document.getPageNumber(page)))
                .endText()
                .release();
        report.flush();
    }
}