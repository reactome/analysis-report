package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierMap;

import java.util.*;
import java.util.stream.Collectors;

public class Tables {


	private Tables() {
	}

	public static TexTable identifiersTable(Collection<FoundEntity> entities, String resource) {
		final List<String> headers = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			headers.add("Input");
			headers.add(resource + " Id");
		}

		final List<List<String>> values = new LinkedList<>();
		List<String> row = new LinkedList<>();
		for (FoundEntity entity : entities) {
			row.add(entity.getId());
			row.add(toString(entity.getMapsTo()));
			if (row.size() == 6) {
				values.add(row);
				row = new LinkedList<>();
			}
		}
		if (!row.isEmpty()) {
			while (row.size() < 6) row.add("");
			values.add(row);
		}

		final TexTable table = new TexTable(headers, values);
		// Add white vertical lines
		table.setAlignment("cZ!{\\color{white}\\vrule}cZ!{\\color{white}\\vrule}cZ");
		table.setHeaderAlignment(Arrays.asList("c", "c!{\\color{white}\\vrule}", "c", "c!{\\color{white}\\vrule}", "c", "c"));
		return table;
	}

	public static TexTable identifiersTable(Collection<FoundEntity> entities, String resource, List<String> expNames) {
		final int numberOfColumns = Math.min(5, expNames.size());
		final List<String> headers = new LinkedList<>(Arrays.asList("Input", resource + " Id"));
		for (int i = 0; i < numberOfColumns; i++)
			headers.add(TextUtils.ellipsis(expNames.get(i), 15));

		final List<List<String>> rows = new LinkedList<>();
		for (FoundEntity entity : entities) {
			final LinkedList<String> row = new LinkedList<>(Arrays.asList(entity.getId(), toString(entity.getMapsTo())));
			for (int i = 0; i < numberOfColumns; i++)
				row.add(PdfUtils.formatNumber(entity.getExp().get(i)));
			rows.add(row);
		}
		final TexTable table = new TexTable(headers, rows);
		// Allow fill width to mapsTo column
		final StringBuilder builder = new StringBuilder("cZ");
		for (int i = 0; i < numberOfColumns; i++) builder.append("c");
		table.setAlignment(builder.toString());
		return table;
	}
	private static String toString(Set<IdentifierMap> identifier) {
		return identifier.stream()
				.flatMap(identifierMap -> identifierMap.getIds().stream())
				.collect(Collectors.joining(", "));
	}


}
