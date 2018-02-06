package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class FooterEventHandler implements IEventHandler {

    private PdfFont font;
    private float margin;
    private FileOutputStream file;
    private AnalysisReport report;

    public FooterEventHandler(PdfFont font, float margin, AnalysisReport report) {
        this.font = font;
        this.margin = margin;
        this.file = report.getDataSet().getFile();
        this.report = report;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument document = pdfDocumentEvent.getDocument();
        PdfPage page = pdfDocumentEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page);
        canvas.beginText()
                .setFontAndSize(font, FontSize.H6)
                .moveText(margin * 2 / 3, margin * 2 / 5)
                .showText("Reactome.org")
                .moveText(page.getPageSize().getWidth() / 2 - 35, 0)
                .showText(String.format("- %s -", document.getPageNumber(page)))
                .endText()
                .release();
        /**
         * force to flush data into disk after every page been completed
         */
        try {
//            document.getWriter().flush();
//            FileOutputStream fileOutputStream = new FileOutputStream(new File("src/test/resources/page#" + document.getPageNumber(page) + ".pdf"));
//            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(fileOutputStream).setSmartMode(true));
//            document.copyPagesTo(document.getPageNumber(page), document.getPageNumber(page), pdfDocument);
//            pdfDocument.close();
//            ((ByteArrayOutputStream) document.getWriter().getOutputStream()).reset();
//            report.flush();
            document.getWriter().flush();
            file.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}