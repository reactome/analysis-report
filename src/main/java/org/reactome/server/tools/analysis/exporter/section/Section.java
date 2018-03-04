package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;

/**
 * Whole PDF report will be split into different sections.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public interface Section {
	/**
	 * This method is to render the pdf document according to the analysis
	 * result data set.
	 */
	void render(Document document, AnalysisData analysisData) throws AnalysisExporterException;
}
