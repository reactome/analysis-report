package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.TexTable;

import java.util.*;

public class NotFoundElementsRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.newPage();
		document.commandln(TexDocument.SECTION, "Identifiers not found");

		final List<List<String>> values = new LinkedList<>();
		List<String> row = new LinkedList<>();
		final List<IdentifierSummary> identifiers = new ArrayList<>(analysisData.getAnalysisStoredResult().getNotFoundIdentifiers());
		identifiers.sort(Comparator.comparing(IdentifierSummary::getId));
		for (IdentifierSummary summary : identifiers) {
			row.add(summary.getId());
			if (row.size() == 6) {
				values.add(row);
				row = new LinkedList<>();
			}
		}
		if (!row.isEmpty()) {
			while (row.size() < 6) row.add("");
			values.add(row);
		}
		final TexTable table = new TexTable(null, values);
		table.setHeaderAlignment(Collections.singletonList("c"));
		table.setAlignment("ZZZZZZ");
		table.render(document);
	}
}
