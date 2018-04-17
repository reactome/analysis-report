package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.tools.analysis.report.document.Command;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class TextUtils {
	private static final Map<String, String> TO_COMMAND_CHARACTERS = new LinkedHashMap<>();
	private static final List<String> TO_SCAPE_CHARACTERS = Arrays.asList("&", "%", "\\$", "#", "_", "\\{", "\\}");

	static {
		TO_COMMAND_CHARACTERS.put("\\\\", "textbackslash");
		TO_COMMAND_CHARACTERS.put("~", "textasciitilde");
		TO_COMMAND_CHARACTERS.put("\\^", "textasciicircum");
	}

	public static String scape(String text) {
		// Order matters
		for (Map.Entry<String, String> entry : TO_COMMAND_CHARACTERS.entrySet()) {
			try {
				final String replacement = Matcher.quoteReplacement(new Command(entry.getValue()).toString());
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

	/**
	 * Calls <code>ellipsis(text, 10)</code>.
	 */
	public static String ellipsis(String text) {
		return ellipsis(text, 10);
	}

	public static String ellipsis(String text, int maxLength) {
		return text.length() > maxLength
				? text.substring(0, maxLength - 3) + "..."
				: text;
	}

	/**
	 * Scapes only the % symbol
	 */
	public static String scapeUrl(String link) {
		return link.replaceAll("%", "\\\\%");
	}
}
