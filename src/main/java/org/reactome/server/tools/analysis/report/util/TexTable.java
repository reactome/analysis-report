package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.tools.analysis.report.document.Command;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.document.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TexTable {

	private static final String HEAEDER_CONF = "\\cellcolor[RGB]{47,158,194}{\\color{white}";

	private final List<String> headers;
	private final List<List<String>> values;
	private String floats = "H";
	private List<String> headerAlignment;
	private double padding = 1.5;
	private String alignment;

	public TexTable(List<String> headers, List<List<String>> values) {
		this.headers = headers;
		this.values = values;
		final StringBuilder builder = new StringBuilder();
		headers.forEach(s -> builder.append("c"));
		alignment = builder.toString();
		headerAlignment = new LinkedList<>();
		headers.forEach(s -> headerAlignment.add("c"));
	}

	/**
	 * default: H
	 */
	public void setFloats(String floats) {
		this.floats = floats;
	}

	/**
	 * By default all headers are centered (c): values are c,l,r, p and X.
	 */
	public void setHeaderAlignment(List<String> headerAlignment) {
		this.headerAlignment = headerAlignment;
	}

	/**
	 * as in tabluarx
	 */
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	/**
	 * By default 1.5
	 */
	public void setPadding(double padding) {
		this.padding = padding;
	}

	public void render(TexDocument document) {
		document.commandln(TexDocument.BEGIN, null, "table", floats)
				.commandln(TexDocument.CENTERING)
				.commandln("bgroup")
				.command("renewcommand").commandln(new Command("arraystretch", String.valueOf(padding)))
				.commandln(new Command(TexDocument.BEGIN, "tabularx", "\\textwidth", alignment));
		for (int i = 0; i < headers.size(); i++) {
			final String bold = new Command(TexDocument.TEXT_BF, headers.get(i)).toString();
			final Command command = new Command(TexDocument.MULTICOLUMN, "1", "" + headerAlignment.get(i), HEAEDER_CONF + bold + "}");
			document.command(command);
			if (i < headers.size() - 1) document.text(" & ");
		}
		document.textln(" \\\\");
		values.forEach(row -> {
			final String line = row.stream()
					.map(TextUtils::scape)
					.collect(Collectors.joining(" & ", "", " \\\\"));
			document.textln(line);
		});
		document.commandln(TexDocument.END, "tabularx")
				.commandln("egroup")
				.commandln(TexDocument.END, "table")
				.ln();
	}

}
