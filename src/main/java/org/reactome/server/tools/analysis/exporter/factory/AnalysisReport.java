package org.reactome.server.tools.analysis.exporter.factory;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.ReportArgs;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;
import org.reactome.server.tools.analysis.exporter.profile.PdfProfile;

import java.io.OutputStream;

/**
 * Report extends from Document{@see Docuemnt}.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class AnalysisReport extends Document {

	private PdfProfile profile;
	private ReportArgs reportArgs;
	// contains the width and height can be used in current area.
	private Rectangle currentPageArea;

	/**
	 * @param profile     the profile file determine the outlook of PDF.
	 * @param reportArgs  args contains the token and paths used to retrieve
	 *                    image.
	 * @param destination destination to save the PDF document.
	 */
	public AnalysisReport(PdfProfile profile, ReportArgs reportArgs, OutputStream destination) {
		super(new PdfDocument(new PdfWriter(destination, new WriterProperties()
				.setFullCompressionMode(false))));
		this.profile = profile;
		this.reportArgs = reportArgs;
		getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(this));
		Fonts.setUp();
		setFont(Fonts.REGULAR)
				.setTextAlignment(TextAlignment.JUSTIFIED);
		setMargins(profile.getMargin().getTop()
				, profile.getMargin().getRight()
				, profile.getMargin().getBottom()
				, profile.getMargin().getLeft());
		currentPageArea = getPageEffectiveArea(this.getPdfDocument().getDefaultPageSize());
	}

	public PdfProfile getProfile() {
		return profile;
	}

	public ReportArgs getReportArgs() {
		return reportArgs;
	}

	public Rectangle getCurrentPageArea() {
		return currentPageArea;
	}

	public void addAsList(java.util.List<? extends Paragraph> paragraphList) {
		// list symbol is a dot.
		List list = new List().setListSymbol("\u2022").setSymbolIndent(this.getLeftMargin() * 0.5f);
		ListItem item;
		for (Paragraph paragraph : paragraphList) {
			item = new ListItem();
			item.add(paragraph);
			list.add(item);
		}
		add(list);
	}
}
