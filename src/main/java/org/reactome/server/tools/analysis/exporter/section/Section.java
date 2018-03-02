package org.reactome.server.tools.analysis.exporter.section;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
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
	 * @param report {@link AnalysisReport}.
	 * @param asr    {@link AnalysisStoredResult} analysis result retrieved from
	 *               binary file contains all the information.
	 * @param sfr    {@link SpeciesFilteredResult} filtered asr by species and
	 *               resource.
	 *
	 * @throws Exception when failed to manipulate with analysis report.
	 */
	void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception;
}
