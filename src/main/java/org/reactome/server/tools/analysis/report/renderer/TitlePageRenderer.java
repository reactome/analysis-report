package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.PdfUtils;
import org.reactome.server.tools.analysis.report.util.TextUtils;

import java.util.List;

public class TitlePageRenderer implements TexRenderer {

	private static List<String> SUMMARY_TEXT = PdfUtils.getText(TitlePageRenderer.class.getResourceAsStream("administrative.txt"));

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(TexDocument.BEGIN, TexDocument.TITLEPAGE);

		// Paragraph 1: argument details
		final String p1 = String.format(SUMMARY_TEXT.get(0),
				analysisData.getName(),
				AnalysisData.getDBVersion(),
				analysisData.getBeautifiedResource());

		String link = "https://reactome.org/PathwayBrowser/#/ANALYSIS=" + analysisData.getAnalysisStoredResult().getSummary().getToken();
		// Links only scape %
		final String link2 = TextUtils.scapeUrl(link);
		final String escaped = TextUtils.scape(link);
		final String p2 = "\\href{" + link2 + "}{\\url{" + escaped + "}}";

		final String p3 = SUMMARY_TEXT.get(1);

		document.commandln(TexDocument.CENTERING)
				.text("{").command(TexDocument.LARGE).text(" Pathway Analysis Report").command("par").textln("}")
				.commandln(TexDocument.V_SPACE, "10mm")
				.text("{").command(TexDocument.Large).text(" " + analysisData.getName()).command("par").textln("}")
				.commandln(TexDocument.V_SPACE, "15mm")
				.paragraph(p1)
				.paragraph(p2)
				.paragraph(p3)
				.commandln("vfill")
				.text("{").command("large").text(" ").command("today").command("par").textln("}");

		document.commandln(TexDocument.END, TexDocument.TITLEPAGE);
		document.commandln(TexDocument.NEW_PAGE);
	}

}
