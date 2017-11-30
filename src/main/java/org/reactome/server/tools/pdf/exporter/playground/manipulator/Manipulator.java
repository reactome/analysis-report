package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public interface Manipulator {
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception;
}
