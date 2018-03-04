package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.style.Images;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;
import org.reactome.server.tools.analysis.exporter.util.HtmlParser;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class CoverPage implements Section {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static List<String> SUMMARY_TEXT = PdfUtils.getText(CoverPage.class.getResourceAsStream("administrative.txt"));

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(Images.getLogo().scaleToFit(150, 150).setHorizontalAlignment(HorizontalAlignment.CENTER));
		// Empty space
		document.add(profile.getTitle(""));
		document.add(profile.getTitle("Pathway Analysis Report"));

		document.add(profile.getH3(analysisData.getName()).setTextAlignment(TextAlignment.CENTER));

		// Paragraph 1: argument details
		final String p1 = String.format(SUMMARY_TEXT.get(0),
				analysisData.getName(),
				AnalysisData.getDBVersion(),
				DATE_FORMAT.format(new Date()),
				analysisData.getBeautifiedResource());
		document.add(HtmlParser.parseParagraph(p1, profile));

		// Paragraph 2: centered link
		final String link = "https://reactome.org/PathwayBrowser/#/ANALYSIS=" + analysisData.getAnalysisStoredResult().getSummary().getToken();
		final String p2 = String.format(SUMMARY_TEXT.get(1), link, link);
		document.add(HtmlParser.parseParagraph(p2, profile).setTextAlignment(TextAlignment.CENTER));

		// Paragraph 3: bla bla
		document.add(HtmlParser.parseParagraph(SUMMARY_TEXT.get(2), profile));
	}


}
