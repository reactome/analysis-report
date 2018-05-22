package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierMap;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Tables {

	private Tables() {}

	/**
	 * Creates a table of identifiers of a expression analysis. Each row
	 * presents id, mapsTo, values[0], values[1]...
	 *
	 * @param entities    collection of entities to render
	 * @param resource    resource of entities
	 * @param profile     pdf profile to create elements
	 * @param columnNames list of column names for the expression columns
	 */
	static Table getExpressionTable(Collection<FoundEntity> entities, String resource, PdfProfile profile, List<String> columnNames) {
		final int rows = Math.min(6, columnNames.size());
		final Table table = new Table(UnitValue.createPercentArray(2 + rows));
		table.useAllAvailableWidth();
		table.addHeaderCell(profile.getHeaderCell("Input"));
		table.addHeaderCell(profile.getHeaderCell(resource + " Id"));
		for (int i = 0; i < rows; i++)
			table.addHeaderCell(profile.getHeaderCell(ellipsis(columnNames.get(i), 12)));
		int row = 0;
		for (FoundEntity entity : entities) {
			table.addCell(profile.getBodyCell(entity.getId(), row));
			table.addCell(profile.getBodyCell(toString(entity.getMapsTo()), row));
			for (int i = 0; i < rows; i++) {
				table.addCell(profile.getBodyCell(PdfUtils.formatNumber(entity.getExp().get(i)), row));
			}
			row++;
		}
		return table;
	}

	/**
	 * This is a custom made layout to present 3 tables side by side.
	 *
	 * @param entities list of items to add to the table
	 * @param resource name of the resource
	 * @param profile  pdf profile to create elements
	 *
	 * @return a table divided in 3 columns with id -> mapsTo sorted by id
	 */
	static Table getIdentifiersTable(Collection<FoundEntity> entities, String resource, PdfProfile profile) {
		final java.util.List<FoundEntity> identifiers = entities.stream()
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
		fillLastRow(table, identifiers.size(), 0, profile);
		return table;
	}


	private static String ellipsis(String text, int max) {
		if (text.length() < max) return text;
		return text.substring(0, Math.max(max - 3, 1)) + "...";
	}

	private static String toString(Set<IdentifierMap> identifier) {
		final java.util.List<String> mapsTo = identifier.stream()
				.flatMap(identifierMap -> identifierMap.getIds().stream())
				.collect(Collectors.toList());
		return String.join(", ", mapsTo);
	}


	private static void fillLastRow(Table table, int identifiers, int row, PdfProfile profile) {
		int n = identifiers % 3;
		if (n == 1) n = 5;
		if (n == 2) n = 2;
		if (n == 3) n = 0;
		for (int j = 0; j < n; j++)
			table.addCell(profile.getBodyCell("", row));
	}
}
