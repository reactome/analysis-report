package org.reactome.server.tools.analysis.exporter.playground.pdfoperator;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.Text;
import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Introduction implements PdfOperator {

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(20).setFirstLineIndent(30));
        }

        for (String literature:Text.REACTOMELITERATRUE) {
            report.addParagraph(new Paragraph(literature).setFontSize(FontSize.H5).setMarginLeft(20));
        }
    }
}
