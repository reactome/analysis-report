package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.constant.Images;
import org.reactome.server.tools.analysis.exporter.element.H3;
import org.reactome.server.tools.analysis.exporter.element.Title;
import org.reactome.server.tools.analysis.exporter.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.util.HtmlParser;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TitleAndLogo implements Section {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static List<String> SUMMARY_TEXT = PdfUtils.getText(TitleAndLogo.class.getResourceAsStream("administrative.txt"));

	@Override
	public void render(Document document, AnalysisData analysisData) {
		document.add(Images.getLogo().scaleToFit(150, 150).setHorizontalAlignment(HorizontalAlignment.CENTER));
		// Empty space
		document.add(new Title(""));
		document.add(new Title("Pathway Analysis Report"));

		document.add(new H3(analysisData.getName()).setTextAlignment(TextAlignment.CENTER));
		final String link = "https://reactome.org/PathwayBrowser/#/ANALYSIS=" + analysisData.getAnalysisStoredResult().getSummary().getToken();
		final String p1 = String.format(SUMMARY_TEXT.get(0),
				analysisData.getName(),
				GraphCoreHelper.getDBVersion(),
				DATE_FORMAT.format(new Date()),
				analysisData.getBeautifiedResource(),
				link, link);
		document.add(HtmlParser.parseParagraph(p1));
		document.add(HtmlParser.parseParagraph(SUMMARY_TEXT.get(1)));
	}


}
