package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.HtmlParser;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class CoverPage implements Section {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(Images.getLogo().scaleToFit(100, 100).setHorizontalAlignment(HorizontalAlignment.LEFT));
		// Empty space
		document.add(profile.getTitle(""));
		document.add(profile.getTitle("Pathway Analysis Report"));
		document.add(profile.getTitle(""));

		document.add(profile.getH3(analysisData.getName()).setTextAlignment(TextAlignment.CENTER));
		document.add(profile.getTitle(""));
		final String link = "https://reactome.org/PathwayBrowser/#/ANALYSIS=" + analysisData.getResult().getSummary().getToken();

		final String text = PdfUtils.getProperty("cover.page",
				analysisData.getName(),
				AnalysisData.getDBVersion(),
				DATE_FORMAT.format(new Date()),
				link, link);

		final Collection<Paragraph> paragraphs = HtmlParser.parseText(profile, text);
		for (Paragraph paragraph : paragraphs)
			document.add(paragraph.setTextAlignment(TextAlignment.CENTER));
	}


}
