package org.reactome.server.tools.analysis.exporter.util;

import org.apache.commons.io.IOUtils;
import org.reactome.server.graph.domain.model.Person;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfUtils {

	public static String getTimeStamp() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}

	public static String getAuthorDisplayName(List<Person> authors) {
		StringBuilder name = new StringBuilder();
		int length = authors.size() > 5 ? 5 : authors.size();
		for (int i = 0; i < length; i++) {
			name.append(authors.get(i).getDisplayName().replace(", ", " ")).append(", ");
		}
		if (length == 5) name.append("et al.");
		return name.toString();
	}

	public static List<String> getText(InputStream resource) {
		try {
			return IOUtils.readLines(resource, Charset.defaultCharset());
		} catch (IOException e) {
			throw new AnalysisExporterRuntimeException("Couldn't read internal resource", e);
		}
	}
}
