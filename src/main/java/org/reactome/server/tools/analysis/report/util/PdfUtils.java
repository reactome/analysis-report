package org.reactome.server.tools.analysis.report.util;

import org.apache.commons.io.IOUtils;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);
	private static final NumberFormat EXP_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);

	static {
		NUMBER_FORMAT.setMaximumFractionDigits(3);
		NUMBER_FORMAT.setGroupingUsed(true);
		EXP_FORMAT.setMaximumFractionDigits(2);
	}

	public static List<String> getText(InputStream resource) {
		try {
			return IOUtils.readLines(resource, Charset.defaultCharset());
		} catch (IOException e) {
			throw new AnalysisExporterRuntimeException("Couldn't read internal resource", e);
		}
	}

	public static String formatNumber(Number number) {
		if (number instanceof Integer || number instanceof Long)
			return number.toString();
		if (number.doubleValue() == 0.0) return "0";
		if (number.doubleValue() < 1e-3)
			return String.format("%.2e", number);
		return NUMBER_FORMAT.format(number);
	}

//	public static Paragraph format(Number number) {
//		if (number instanceof Integer || number instanceof Long)
//			return new Paragraph(number.toString());
//		if (number.doubleValue() < 1e-3) {
//			double result = number.doubleValue();
//			int mantis = 0;
//			while (result < 1) {
//				result *= 10;
//				mantis += 1;
//			}
//			return new Paragraph(EXP_FORMAT.format(result) + "x10")
//					.add(new Text("-" + mantis).setFontSize(6).setTextRise(4));
//		}
//		return new Paragraph(NUMBER_FORMAT.format(number));
//	}
}
