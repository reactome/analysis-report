package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.reactome.server.analysis.core.model.AnalysisType;
import org.reactome.server.analysis.core.result.model.*;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.PathwayData;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;
import org.reactome.server.tools.analysis.exporter.util.PdfUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class IdentifiersFound implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(profile.getH1(("6. Identifiers found")).setDestination("identifiers-found"));

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
					addSimpleTable(document, entities, summary.getResource(), profile);
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
		final int rows = Math.min(6, columnNames.size());
		final Table table = new Table(UnitValue.createPercentArray(2 + rows));
		table.useAllAvailableWidth();
		table.addHeaderCell(profile.getHeaderCell("Input"));
		table.addHeaderCell(profile.getHeaderCell(resource + " Id"));
		for (int i = 0; i < rows; i++)
			table.addHeaderCell(profile.getHeaderCell(columnNames.get(i)));
		int row = 0;
		for (FoundEntity entity : entities) {
			table.addCell(profile.getBodyCell(entity.getId(), row));
			table.addCell(profile.getBodyCell(toString(entity.getMapsTo()), row));
			for (int i = 0; i < rows; i++) {
				table.addCell(profile.getBodyCell(PdfUtils.formatNumber(entity.getExp().get(i)), row));
			}
			row++;
		}
		document.add(table);
	}


	private void addSimpleTable(Document document, Set<FoundEntity> elements, String resource, PdfProfile profile) {
		// This is a custom made layout to present 3 tables side by side
		final java.util.List<FoundEntity> identifiers = elements.stream()
				.sorted(Comparator.comparing(IdentifierSummary::getId))
				.distinct()
				.collect(Collectors.toList());
		final Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 0.1f, 1, 1, 0.1f, 1, 1}));
		table.useAllAvailableWidth();
		final String input = "Input";
		final String mapping = String.format("%s Id", resource);
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		table.addHeaderCell(profile.getBodyCell("", 0));
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		table.addHeaderCell(profile.getBodyCell("", 0));
		table.addHeaderCell(profile.getHeaderCell(input));
		table.addHeaderCell(profile.getHeaderCell(mapping));
		int i = 0;
		int row = 0;
		int column;
		for (FoundEntity identifier : identifiers) {
			column = i % 3;
			row = i / 3;
			final String join = toString(identifier.getMapsTo());
			table.addCell(profile.getBodyCell(identifier.getId(), row));
			table.addCell(profile.getBodyCell(join, row));
			if (column == 0 || column == 1)
				table.addCell(profile.getBodyCell("", 0));
			i += 1;
		}
		fillLastRow(table, identifiers.size(), row, profile);
		document.add(table);
	}

	private String toString(Set<IdentifierMap> identifier) {
		final java.util.List<String> mapsTo = identifier.stream()
				.flatMap(identifierMap -> identifierMap.getIds().stream())
				.collect(Collectors.toList());
		return String.join(", ", mapsTo);
	}

	private void fillLastRow(Table table, int identifiers, int row, PdfProfile profile) {
		int n = identifiers % 3;
		if (n == 1) n = 5;
		if (n == 2) n = 2;
		if (n == 3) n = 0;
		for (int j = 0; j < n; j++)
			table.addCell(profile.getBodyCell("", 0));
	}

}
