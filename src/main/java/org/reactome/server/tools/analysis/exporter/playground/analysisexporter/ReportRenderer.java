package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exceptions.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections.*;
import org.reactome.server.tools.analysis.exporter.playground.utils.PdfUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class ReportRenderer {

    private static DataSet dataSet;
    private static PdfDocument document;
    private static AnalysisReport report;
    private static List<Section> sections;

    public static void render(PdfProperties properties, PdfWriter writer) throws Exception {
        dataSet = PdfUtil.getDataSet(properties);
        document = new PdfDocument(writer);
        report = new AnalysisReport(properties, document);
        sections = new ArrayList<>();

        sections.add(new Footer());
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

        try {
            for (Section section : sections) {
                section.render(report, properties, dataSet);
            }
        } catch (Exception e) {
            throw new FailToRenderReportException("Fail to render report.", e);
        }

        report.close();
        document.close();
    }
}
