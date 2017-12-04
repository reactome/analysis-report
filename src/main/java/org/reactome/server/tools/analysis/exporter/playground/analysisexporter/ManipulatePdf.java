package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfoperator.*;
import org.reactome.server.tools.analysis.exporter.playground.utils.PdfUtil;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatePdf {
    private static PdfOperatorChain pdfOperatorChain;
    private static DataSet dataSet;
    private static AnalysisReport report;
    private static PdfDocument document;

    public static void manipulate(PdfProperties properties, PdfWriter writer) throws Exception {
        dataSet = PdfUtil.getDataSet(properties);
        document = new PdfDocument(writer);
        report = new AnalysisReport(properties, document);
        pdfOperatorChain = new PdfOperatorChain();
        pdfOperatorChain.addManipulator(new FooterEvent())
                .addManipulator(new TitleAndLogo())
                .addManipulator(new Administrative())
                .addManipulator(new Introduction())
                .addManipulator(new Summary())
                .addManipulator(new Details());
        pdfOperatorChain.manipulate(report, properties, dataSet);
        report.close();
        document.close();
    }
}
