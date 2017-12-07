package org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections;

import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface Section {
    void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception;
}
