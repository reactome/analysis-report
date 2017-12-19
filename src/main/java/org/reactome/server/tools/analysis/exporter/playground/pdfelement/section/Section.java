package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface Section {
    /**
     * @param report     AnalysisReport.
     * @param properties PdfProperties.
     * @param dataSet    DataSet.
     * @throws Exception may throe exception when some exact section to render the pdf document.
     */
    void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception;
}
