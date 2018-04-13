package org.reactome.server.tools.analysis.report.document;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

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

	private static final Map<String, String> TO_COMMAND_CHARACTERS = new LinkedHashMap<>();
	private static final List<String> TO_SCAPE_CHARACTERS = Arrays.asList("&", "%", "\\$", "#", "_", "\\{", "\\}");

	static {
		TO_COMMAND_CHARACTERS.put("\\\\", "textbackslash");
		TO_COMMAND_CHARACTERS.put("~", "textasciitilde");
		TO_COMMAND_CHARACTERS.put("\\^", "textasciicircum");
	}

	private final PrintStream output;

	public TexDocument(OutputStream outputStream) {
		this.output = outputStream instanceof BufferedOutputStream
				? new PrintStream(outputStream)
				: new PrintStream(new BufferedOutputStream(outputStream));

	}

	private static String createCommand(String command, String modifier, String value, String floats) {
		final StringBuilder line = new StringBuilder("\\")
				.append(command);
		if (modifier != null)
			line.append("[").append(modifier).append("]");
		if (value != null)
			line.append("{").append(value).append("}");
		if (floats != null)
			line.append("[").append(floats).append("]");
		return line.toString();
	}

	public static String scape(String text) {
		// Order matters
		for (Map.Entry<String, String> entry : TO_COMMAND_CHARACTERS.entrySet()) {
			try {
				final String replacement = Matcher.quoteReplacement(createCommand(entry.getValue(), null, null, null));
				text = text.replaceAll(entry.getKey(), replacement + " ");
			} catch (PatternSyntaxException e) {
				System.out.println(text);
				System.out.println(entry.getKey());
				throw (e);
			}
		}
		for (String s : TO_SCAPE_CHARACTERS) {
			try {
				final String replacement = Matcher.quoteReplacement("\\" + s);
				text = text.replaceAll(s, replacement);
			} catch (PatternSyntaxException e) {
				System.out.println(text);
				System.out.println(s);
				throw (e);
			}
		}

		return text;
	}

	public TexDocument newLine() {
		output.println();
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null)</code>. Appends
	 * <code>\command</code> and newline.
	 */
	public TexDocument commandln(String command) {
		commandln(command, null);
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null)</code>. Appends
	 * <code>\command</code>.
	 */
	public TexDocument command(String command) {
		command(command, null);
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command{params}</code> and newline
	 */
	public TexDocument commandln(String command, String params) {
		commandln(command, null, params);
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null, value)</code>.
	 * Appends<code>\command{value}</code>
	 */
	public TexDocument command(String command, String value) {
		command(command, null, value);
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command[modifier]{params}</code> and newline
	 */
	public TexDocument commandln(String command, String modifier, String params) {
		commandln(command, modifier, params, null);
		return this;
	}

	/**
	 * Shorthand for <code>commandln(command, null, params)</code>.
	 * Appends<code>\command[modifier]{params}</code>
	 */
	public TexDocument command(String command, String modifier, String params) {
		command(command, modifier, params, null);
		return this;
	}

	/**
	 * Appends <code>\command[modifier]{params}[floats]</code> and newline. If
	 * any of the values is null, it won't be added. If you want to insert empty
	 * brackets, use empty String.
	 */
	public TexDocument commandln(String command, String modifier, String params, String floats) {
		command(command, modifier, params, floats);
		output.println();
		return this;
	}

	/**
	 * Appends <code>\command[modifier]{params}[floats]</code>. If any of the
	 * values is null, it won't be added. If you want to insert empty brackets,
	 * use empty String.
	 */
	public TexDocument command(String command, String modifier, String params, String floats) {
		final String line = createCommand(command, modifier, params, floats);
		output.print(line);
		return this;
	}

	public TexDocument text(String text) {
		output.print(text);
		return this;
	}

	public TexDocument textln(String text) {
		text(text);
		output.println();
		return this;
	}

	/**
	 * Appends text followed by two newlines, enforcing a new paragraph in
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

	public void newPage() {
		commandln(NEW_PAGE);
	}
}
