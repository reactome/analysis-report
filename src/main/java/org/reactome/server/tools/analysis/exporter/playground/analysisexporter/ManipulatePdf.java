package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfsections.*;
import org.reactome.server.tools.analysis.exporter.playground.utils.PdfUtil;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ManipulatePdf {

    private static DataSet dataSet;
    private static PdfDocument document;
    private static AnalysisReport report;
    private static Administrative administrative;
    private static Introduction introduction;
    private static Summary summary;
    private static Details details;
    private static TitleAndLogo titleAndLogo;
    private static FooterEvent footerEvent;

    public static void manipulate(PdfProperties properties, PdfWriter writer) throws Exception {
        dataSet = PdfUtil.getDataSet(properties);
        document = new PdfDocument(writer);
        report = new AnalysisReport(properties, document);
        //render the file

        footerEvent = new FooterEvent(report,properties,dataSet);//must be the first render
        titleAndLogo = new TitleAndLogo(report, properties, dataSet);
        administrative = new Administrative(report, properties, dataSet);
        introduction = new Introduction(report, properties, dataSet);
        summary = new Summary(report, properties, dataSet);
        details = new Details(report, properties, dataSet);

        footerEvent.flush();
        titleAndLogo.flush();
        administrative.flush();
        introduction.flush();
        summary.flush();
        details.flush();
        report.close();
        document.close();
    }
}
