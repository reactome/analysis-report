package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfoperator.*;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatePdf {
    private static PdfOperatorChain pdfOperatorChain;
    private static DataSet dataSet;
    private static AnalysisReport report;

    public static void manipulate(PdfProperties properties) throws Exception {
        dataSet = PdfUtils.getDataSet(properties);
        report = new AnalysisReport(properties);
        pdfOperatorChain = new PdfOperatorChain();
        pdfOperatorChain.addManipulator(new FooterEvent())
                .addManipulator(new TitleAndLogo())
                .addManipulator(new Administrative())
                .addManipulator(new Introduction())
                .addManipulator(new Summary())
                .addManipulator(new Details());
        pdfOperatorChain.manipulate(report, properties, dataSet);
        report.close();
        properties.close();
    }
}
