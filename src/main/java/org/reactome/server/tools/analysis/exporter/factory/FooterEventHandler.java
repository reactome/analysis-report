package org.reactome.server.tools.analysis.exporter.factory;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;

/**
 * Event handler to add the footer text and page number.
 *
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
		String site = "reactome.org";
		String pageNum = String.format("- %s -", document.getPageNumber(page));
		canvas.beginText()
				.setFontAndSize(Fonts.REGULAR, FontSize.P)
				.moveText(report.getLeftMargin(), report.getBottomMargin() * 2 / 3)
				.showText(site)
				.moveText((page.getPageSize().getWidth() - Fonts.REGULAR.getWidth(pageNum.concat(site), FontSize.P)) * 0.5, 0)
				.showText(pageNum)
				.endText()
				.release();

		// Flush every page once it been finished.
		report.flush();
	}
}
