package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.Command;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertiesRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(TexDocument.SECTION, "Properties");
		final AnalysisStoredResult result = analysisData.getAnalysisStoredResult();
		final List<List<String>> values = new ArrayList<>();
		values.add(Arrays.asList("Analysis type", String.valueOf(analysisData.getType())));
		values.add(Arrays.asList("Pathways found", String.format("%,d", result.getPathways().size())));
		final int found = analysisData.getAnalysisStoredResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getAnalysisStoredResult().getNotFound().size();
		final String identifiers = String.format("%,d / %,d", found, found + notFound);
		values.add(Arrays.asList("Identifiers found", identifiers));
		final boolean projection = result.getSummary().isProjection() != null && result.getSummary().isProjection();
		values.add(Arrays.asList("Projected to human", projection ? "yes" : "no"));
		final boolean interactors = result.getSummary().isInteractors() != null && result.getSummary().isInteractors();
		values.add(Arrays.asList("Include interactors", interactors ? "yes" : "no"));
		values.add(Arrays.asList("Results are shown for species", analysisData.getSpecies()));
		values.add(Arrays.asList("Unique ID for analysis", analysisData.getAnalysisStoredResult().getSummary().getToken()));

		document.commandln(TexDocument.BEGIN, null, "table", "H")
				.commandln("centering")
				.commandln("bgroup")
				.command("renewcommand").commandln(new Command("arraystretch", "1.5"))
				.commandln(new Command(TexDocument.BEGIN, "tabular", "ll"))
				.commandln("hline");
		values.forEach(strings -> document
				.command(TexDocument.TEXT_IT, TextUtils.scape(strings.get(0)))
				.text(" & ")
				.textln(TextUtils.scape(strings.get(1)) + " \\\\"));
		document.commandln("hline")
				.commandln(TexDocument.END, "tabular")
				.commandln("egroup")
				.commandln(TexDocument.END, "table");
//		final TexTable table = new TexTable(headers, values);
//		table.render(document);
		document.newPage();
	}
}
