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
                .setFontAndSize(AnalysisFont.REGULAR, FontSize.H6)
                .moveText(report.getLeftMargin(), report.getBottomMargin() * 2 / 3)
                .showText("reactome.org")
                .moveText(page.getPageSize().getWidth() / 2 - 35, 0)
                .showText(String.format("- %s -", document.getPageNumber(page)))
                .endText()
                .release();
        report.flush();
    }

    /*
    String text = String.format("- %s -", document.getPageNumber(page));
        float width = AnalysisFont.REGULAR.getWidth(text, FontSize.H6);
        canvas
                .setFontAndSize(AnalysisFont.REGULAR, FontSize.H6)
                .moveTo(report.getLeftMargin(), report.getBottomMargin() * .5f)
                .showText("reactome.org")
        ;
        canvas
                .moveTo((page.getPageSize().getWidth() - width) * .5f, report.getBottomMargin() * .5f)
                .showText(text);
        report.flush();
     */
}