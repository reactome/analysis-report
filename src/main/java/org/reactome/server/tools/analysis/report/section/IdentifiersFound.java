package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.reactome.server.analysis.core.model.AnalysisIdentifier;
import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.model.identifier.Identifier;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.FoundInteractor;
import org.reactome.server.analysis.core.result.model.FoundInteractors;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class IdentifiersFound implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(profile.getH1(("Identifiers found")).setDestination("identifiers-found"));
		document.add(profile.getParagraph(PdfUtils.getProperty("identifiers.found.section")));
		final long entities = analysisData.getResult().getFoundEntitiesMap().keySet().stream()
				.map(Identifier::getValue)
				.map(AnalysisIdentifier::getId)
				.distinct()
				.count();
		document.add(profile.getH3(String.format("Entities (%d)", entities)));
		for (String resource : analysisData.getResources()) {
			document.add(profile.getParagraph(""));
			addAllTable(document, profile, analysisData, resource);
		}

		if (analysisData.isInteractors()) {
			final long interactors = analysisData.getResult().getPathways().stream()
					.map(PathwayNodeSummary::getStId)
					.map(analysisData.getResult()::getFoundInteractors)
					.map(FoundInteractors::getIdentifiers)
					.flatMap(Collection::stream)
					.distinct()
					.count();
			if (interactors > 0){
				document.add(profile.getH3(String.format("Interactors (%d)", interactors)));
				for (String resource : analysisData.getResources()) {
					addInteractorsTable(document, profile, analysisData, resource);
				}
			}
		}
	}

	private void addAllTable(Document document, PdfProfile profile, AnalysisData analysisData, String resource) {
		final Set<FoundEntity> entities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
		for (PathwayNodeSummary pathway : analysisData.getResult().getPathways()) {
			entities.addAll(analysisData.getResult().getFoundEntities(pathway.getStId()).filter(resource).getIdentifiers());
		}
		if (entities.isEmpty()) return;
		final Table expressionTable = analysisData.getType() == AnalysisType.EXPRESSION
				? Tables.getExpressionTable(entities, resource, profile, analysisData.getResult().getExpressionSummary().getColumnNames())
				: Tables.createEntitiesTable(entities, resource, profile);
		document.add(expressionTable);
	}

	private void addInteractorsTable(Document document, PdfProfile profile, AnalysisData analysisData, String resource) {
		final Set<FoundInteractor> interactors = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
		for (PathwayNodeSummary pathway : analysisData.getResult().getPathways()) {
			interactors.addAll(analysisData.getResult().getFoundInteractors(pathway.getStId()).filter(resource).getIdentifiers());
		}
		if (interactors.isEmpty()) return;
		final Table table = analysisData.getType() == AnalysisType.EXPRESSION
				? Tables.getInteractorsExpressionTable(interactors, resource, profile, analysisData.getResult().getExpressionSummary().getColumnNames())
				: Tables.getInteractorsTable(interactors, resource, profile);
		document.add(table);
	}

}
