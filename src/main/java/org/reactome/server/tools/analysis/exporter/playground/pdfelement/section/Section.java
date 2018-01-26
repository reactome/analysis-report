package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.aspectj.Monitor;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
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
     * @param report  {@see AnalysisReport}.
     * @param dataSet data set contains all the information needed to create the analysis report.
     * @throws Exception when failed to manipulate with analysis report.
     */
    @Monitor
    void render(AnalysisReport report, DataSet dataSet) throws Exception;
}
