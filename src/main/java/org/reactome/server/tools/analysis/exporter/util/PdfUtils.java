package org.reactome.server.tools.analysis.exporter.util;

import org.apache.commons.io.IOUtils;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

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
		if (number.doubleValue() < 1e-3)
			return String.format("%.2e", number);
		return String.format("%.3f", number);
	}
}
