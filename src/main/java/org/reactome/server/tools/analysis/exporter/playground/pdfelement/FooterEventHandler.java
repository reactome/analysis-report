package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Fonts;

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
        String reactome = "reactome.org";
        String pageNum = String.format("- %s -", document.getPageNumber(page));
        canvas.beginText()
//                .setColor(Colors.GRAY, false)
                .setFontAndSize(Fonts.REGULAR, FontSize.P)
                .moveText(report.getLeftMargin(), report.getBottomMargin() * 2 / 3)
                .showText(reactome)
                .moveText((page.getPageSize().getWidth() - Fonts.REGULAR.getWidth(pageNum.concat(reactome), FontSize.P)) * 0.5, 0)
                .showText(pageNum)
                .endText()
                .release();
        report.flush();
    }
}