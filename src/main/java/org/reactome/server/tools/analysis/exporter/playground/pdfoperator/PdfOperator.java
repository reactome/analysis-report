package org.reactome.server.tools.analysis.exporter.playground.pdfoperator;

import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface PdfOperator {
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception;
}
