package org.reactome.server.tools.analysis.report.document;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class TexDocument {

	public static final String USE_PACKAGE = "usepackage";
	public static final String BEGIN = "begin";
	public static final String END = "end";
	public static final String DOCUMENT_CLASS = "documentclass";
	public static final String CENTERING = "centering";
	public static final String FIGURE = "figure";
	public static final String TEXT_BF = "textbf";
	public static final String TEXT_IT = "textit";
	public static final String HYPERREF = "hyperref";
	public static final String TEXT_SUBSCRIPT = "textsubscript";
	public static final String NEW_PAGE = "newpage";
	public static final String SUB_SUB_SECTION = "subsubsection";
	public static final String SUB_SECTION = "subsection";
	public static final String SECTION = "section";
	public static final String TABLE_OF_CONTENTS = "tableofcontents";
	public static final String LARGE = "LARGE";
	public static final String Large = "Large";
	public static final String V_SPACE = "vspace";
	public static final String TITLEPAGE = "titlepage";
	public static final String MULTICOLUMN = "multicolumn";
	public static final String INCLUDE_GRAPHICS = "includegraphics";


	private final PrintStream output;

	public TexDocument(OutputStream outputStream) {
		this.output = outputStream instanceof BufferedOutputStream
				? new PrintStream(outputStream)
				: new PrintStream(new BufferedOutputStream(outputStream));

	}

	public TexDocument command(Command command) {
		output.print(command);
		return this;
	}

	public TexDocument commandln(Command command) {
		output.println(command);
		return this;
	}

	public TexDocument ln() {
		output.println();
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null)</code>. Appends
	 * <code>\command</code> and newline.
	 */
	public TexDocument commandln(String command) {
		return commandln(command, null);
	}

	/**
	 * Shorthand for <code>commandln(command, null)</code>. Appends
	 * <code>\command</code>.
	 */
	public TexDocument command(String command) {
		return command(command, null);
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command{params}</code> and newline
	 */
	public TexDocument commandln(String command, String params) {
		return command(command, null, params).ln();
	}

	/**
	 * Shorthand for <code>commandln(command, null, value)</code>.
	 * Appends<code>\command{value}</code>
	 */
	public TexDocument command(String command, String value) {
		return command(command, null, value);
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command[modifier]{params}</code> and newline
	 */
	public TexDocument commandln(String command, String modifier, String params) {
		return command(command, modifier, params, null).ln();
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command[modifier]{params}</code>
	 */
	public TexDocument command(String command, String modifier, String params) {
		return command(command, modifier, params, null);
	}

	/**
	 * Appends <code>\command[modifier]{params}[floats]</code> and newline. If
	 * any of the values is null, it won't be added. If you want to insert empty
	 * brackets, use empty String.
	 */
	public TexDocument commandln(String command, String modifier, String params, String floats) {
		return command(command, modifier, params, floats).ln();
	}

	/**
	 * Appends <code>\command[modifier]{params}[floats]</code>. If any of the
	 * values is null, it won't be added. If you want to insert empty brackets,
	 * use empty String.
	 */
	public TexDocument command(String command, String modifier, String params, String floats) {
		output.print(new Command(command).values(params).modifiers(modifier).floats(floats));
		return this;
	}

	public TexDocument text(String text) {
		output.print(text);
		return this;
	}

	public TexDocument textln(String text) {
		output.println(text);
		return this;
	}

	/**
	 * Appends command followed by two newlines, enforcing a new paragraph in
	 * latex.
	 */
	public TexDocument paragraph(String text) {
		output.println(text);
		output.println();
		return this;
	}

	public void flush() {
		output.flush();
	}

	public TexDocument newPage() {
		return commandln(NEW_PAGE);
	}
}
