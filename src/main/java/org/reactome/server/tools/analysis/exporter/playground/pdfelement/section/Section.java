package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface Section {
    /**
     * This method is to render the pdf document by give a exact {@see AnalysisReport}
     *
     * @param report  AnalysisReport.
     * @param dataSet DataSet.
     * @throws Exception may throe exception when some exact section to render the pdf document.
     */
    @Monitor
    void render(AnalysisReport report, DataSet dataSet) throws Exception;
}
