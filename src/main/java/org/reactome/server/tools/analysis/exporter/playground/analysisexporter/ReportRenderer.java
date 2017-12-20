package org.reactome.server.tools.analysis.exporter.playground.analysisexporter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToRenderReportException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.section.*;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

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

    protected static void render(PdfProperties properties, PdfWriter writer) throws Exception {
        dataSet = PdfUtils.getDataSet(properties);
        document = new PdfDocument(writer);
        report = new AnalysisReport(properties, document);
        sections = new ArrayList<>();

        sections.add(new Footer());
        sections.add(new TitleAndLogo());
        sections.add(new Administrative());
        sections.add(new Introduction());
        sections.add(new Summary());
        sections.add(new Overview());

//        sections.stream().forEach(section -> section.render(report,properties,dataSet));

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
