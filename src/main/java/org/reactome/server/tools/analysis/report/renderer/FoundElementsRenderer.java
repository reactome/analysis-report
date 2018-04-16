package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.model.FoundElements;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.analysis.core.result.model.ResourceSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.Tables;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class FoundElementsRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.newPage();
		document.commandln(TexDocument.SECTION, "Identifiers found");
		if (analysisData.getResource().equalsIgnoreCase("total")) {
			for (ResourceSummary summary : analysisData.getAnalysisStoredResult().getResourceSummary()) {
				if (summary.getResource().equalsIgnoreCase("total")) continue;
				final Set<FoundEntity> entities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
				for (PathwayData pathwayData : analysisData.getPathways()) {
					final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathwayData.getSummary().getStId(), summary.getResource());
					entities.addAll(foundElements.getEntities());
				}
				if (analysisData.getType() == AnalysisType.EXPRESSION)
					Tables.identifiersTable(entities, analysisData.beautify(summary.getResource()), analysisData.getAnalysisStoredResult().getExpressionSummary().getColumnNames()).render(document);
				else
					Tables.identifiersTable(entities, summary.getResource()).render(document);
			}
		} else {
			final Set<FoundEntity> entities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
			for (PathwayData pathwayData : analysisData.getPathways()) {
				final FoundElements foundElements = analysisData.getAnalysisStoredResult().getFoundElmentsForPathway(pathwayData.getSummary().getStId(), analysisData.getResource());
				entities.addAll(foundElements.getEntities());
			}
			if (analysisData.getType() == AnalysisType.EXPRESSION)
				Tables.identifiersTable(entities, analysisData.getBeautifiedResource(), analysisData.getAnalysisStoredResult().getExpressionSummary().getColumnNames()).render(document);
			else
				Tables.identifiersTable(entities, analysisData.getBeautifiedResource()).render(document);
		}
	}

}
