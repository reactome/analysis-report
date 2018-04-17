package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

public class OverviewRenderer implements TexRenderer {

	private static final String FIREWORKS_TEXT = String.join(
			System.lineSeparator() + System.lineSeparator(),
			PdfUtils.getText(OverviewRenderer.class.getResourceAsStream("fireworks.txt")));

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		final String species = analysisData.getSpecies().replaceAll(" ", "_") + ".pdf";
		document.commandln(TexDocument.SECTION, "Genome wide overview (" +analysisData.getSpecies() + ")");
		document.commandln(TexDocument.BEGIN, null, "figure", "H")
				.commandln("caption", "Genome wide overview", FIREWORKS_TEXT)
				.commandln(TexDocument.INCLUDE_GRAPHICS, "width=165mm", species)
				.commandln(TexDocument.END, "figure");
		document.newPage();
	}
}
