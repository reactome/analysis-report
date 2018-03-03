package org.reactome.server.tools.analysis.exporter.factory;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;

/**
 * Event handler to add the footer text and page number.
 *
 * @author Chuan Deng dengchuanbio@gmail.com
 */
public class FooterEventHandler implements IEventHandler {

	private final Document document;

	FooterEventHandler(Document document) {
		this.document = document;
	}

	@Override
	public void handleEvent(Event event) {
		final PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
		final PdfPage page = documentEvent.getPage();
		final PdfCanvas canvas = new PdfCanvas(page);

		final int pageNumber = document.getPdfDocument().getPageNumber(page);
		if (pageNumber == 1) return;  // Cover page
		final String paging = String.format("Page %d", pageNumber - 1);
		final float yCenter = document.getBottomMargin() * 0.5f;
		final float width = page.getMediaBox().getWidth();
		final float pagingWidth = Fonts.REGULAR.getWidth(paging, 8);

		canvas.setFontAndSize(Fonts.REGULAR, 8)
				.moveTo(document.getLeftMargin(), yCenter)
				.showText("reactome.org")
				.moveTo(width - document.getRightMargin() - pagingWidth, yCenter)
				.showText(paging);
	}
}
