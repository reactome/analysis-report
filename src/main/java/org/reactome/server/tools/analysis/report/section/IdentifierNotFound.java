package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifierNotFound implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		final List<IdentifierSummary> sorted = analysisData.getResult().getNotFoundIdentifiers().stream()
				.sorted(Comparator.comparing(IdentifierSummary::getId))
				.distinct()
				.collect(Collectors.toList());
		if (sorted.isEmpty()) return;
		document.add(new AreaBreak());
		document.add(profile.getH1("Identifiers not found").setDestination("not-found"));
		document.add(profile.getParagraph(PdfUtils.getProperty("identifiers.not.found.section", sorted.size())));
		final Table table = new Table(UnitValue.createPercentArray(8));
		table.useAllAvailableWidth();
		int i = 0;
		int row = 0;
		for (IdentifierSummary summary : sorted) {
			row = i / 8;
			table.addCell(profile.getBodyCell(summary.getId(), row).setMargin(1.5f));
			i += 1;
		}
		final int n = 8 - sorted.size() % 8;
		for (int j = 0; j < n; j++) table.addCell(profile.getBodyCell("", row));
		document.add(table);
	}
}
