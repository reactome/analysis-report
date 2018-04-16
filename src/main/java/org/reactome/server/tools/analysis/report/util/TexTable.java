package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.tools.analysis.report.document.Command;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.document.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TexTable {

	// \cellcolor[RGB]{47,158,194}{\color{white} Column}
	private static final Command HEADER_CELL_COLOR = new Command("cellcolor", "RGB", "47,158,194");
	private static final Command HEADER_TEXT_COLOR = new Command("color", "white");

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
		document
				.commandln("bgroup")
				.command("renewcommand").commandln(new Command("arraystretch", String.valueOf(padding)))
				.commandln(new Command("rowcolors", "3", "lightgray", "white"))
				.commandln(new Command(TexDocument.BEGIN, "tabularx", "\\textwidth", alignment));
		// Header row
		final List<String> columnHeaders = IntStream.range(0, headers.size())
				.mapToObj(this::createColumnHeader)
				.collect(Collectors.toList());
		document.text(String.join(" & ", columnHeaders)).textln(" \\\\");
		document.commandln("endhead");
		// scape content and join rows by & and values by \\
		final String rows = values.stream()
				.map(row -> row.stream()
						.map(TextUtils::scape)
						.collect(Collectors.joining(" & ")))
				.collect(Collectors.joining(" \\\\" + System.lineSeparator()));
		document.textln(rows);
		document.commandln(TexDocument.END, "tabularx")
				.commandln("egroup")
				.ln();
	}

	private String createColumnHeader(int i) {
		// scape
		final String scape = TextUtils.scape(headers.get(i));
		// bold
		final String bold = new Command(TexDocument.TEXT_BF, scape).toString();
		// color
		final String colored = new Command("cellcolor").modifiers("RGB").values("47,158,194", HEADER_TEXT_COLOR + bold).toString();
		// align
		return new Command(TexDocument.MULTICOLUMN, "1", "" + headerAlignment.get(i), colored).toString();

	}

}
