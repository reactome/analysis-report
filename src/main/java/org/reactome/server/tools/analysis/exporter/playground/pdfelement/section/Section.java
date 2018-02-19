package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 * <p>
 * whole pdf report will be split into servals sections.
 */
public interface Section {
    /**
     * This method is to render the pdf document by give a exact {@see AnalysisReport} and {@see DataSet}.
     *
     * @param report {@see AnalysisReport}.
     * @param result {@see AnalysisStoredResult}.
     * @throws Exception when failed to manipulate with analysis report.
     */
    void render(AnalysisReport report, AnalysisStoredResult result) throws Exception;
}
