package org.reactome.server.tools.pdf.exporter.playground.pdfoperator;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.constants.Text;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Introduction implements PdfOperator {

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.add(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(20).setFirstLineIndent(30));
        }

        for (String literature:Text.REACTOMELITERATRUE) {
            report.add(new Paragraph(literature).setFontSize(FontSize.H5).setMarginLeft(20));
        }
    }
}
