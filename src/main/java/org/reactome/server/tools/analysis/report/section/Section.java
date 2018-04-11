package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterException;
import org.reactome.server.tools.analysis.report.style.PdfProfile;

/**
 * Whole PDF report will be split into different sections.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public interface Section {
	/**
	 * This method is to create the pdf document according to the analysis
	 * result data set.
	 */
	void render(Document document, PdfProfile profile, AnalysisData analysisData) throws AnalysisExporterException;
}
