package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.exporter.factory.AnalysisReport;

/**
 * Whole PDF report will be split into different sections.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public interface Section {
	/**
	 * This method is to render the pdf document according to the analysis
	 * result data set.
	 *
	 * @throws Exception when failed to manipulate with analysis report.
	 */
	void render(Document document, AnalysisData analysisData) throws Exception, AnalysisExporterException;
}
