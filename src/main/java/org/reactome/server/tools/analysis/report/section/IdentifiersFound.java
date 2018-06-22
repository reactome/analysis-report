package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.model.FoundElements;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class IdentifiersFound implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(profile.getH1(("Identifiers found")).setDestination("identifiers-found"));

		if (analysisData.getResource().equalsIgnoreCase("total")) {
			for (ResourceSummary summary : analysisData.getAnalysisStoredResult().getResourceSummary()) {
				if (summary.getResource().equalsIgnoreCase("total")) continue;
				final Set<FoundEntity> entities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
				for (PathwayData pathwayData : analysisData.getPathways()) {
					final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathwayData.getSummary().getStId(), summary.getResource());
					entities.addAll(foundElements.getEntities());
				}
				if (analysisData.getType() == AnalysisType.EXPRESSION)
					addExpressionTable(document, entities, analysisData.beautify(summary.getResource()), profile, analysisData.getAnalysisStoredResult().getExpressionSummary().getColumnNames());
				else
					addSimpleTable(document, entities, analysisData.beautify(summary.getResource()), profile);
			}
		} else {
			final Set<FoundEntity> entities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
			for (PathwayData pathwayData : analysisData.getPathways()) {
				final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathwayData.getSummary().getStId(), analysisData.getResource());
				entities.addAll(foundElements.getEntities());
			}
			if (analysisData.getType() == AnalysisType.EXPRESSION)
				addExpressionTable(document, entities, analysisData.getBeautifiedResource(), profile, analysisData.getAnalysisStoredResult().getExpressionSummary().getColumnNames());
			else
				addSimpleTable(document, entities, analysisData.getBeautifiedResource(), profile);
		}
	}

	private void addExpressionTable(Document document, Set<FoundEntity> entities, String resource, PdfProfile profile, List<String> columnNames) {
		final Table expressionTable = Tables.getExpressionTable(entities, resource, profile, columnNames);
		document.add(expressionTable);
	}

	private void addSimpleTable(Document document, Set<FoundEntity> elements, String resource, PdfProfile profile) {
		final Table identifiersTable = Tables.getIdentifiersTable(elements, resource, profile);
		document.add(identifiersTable);
	}

}
