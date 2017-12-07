package org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constants.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constants.Text;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Introduction implements Section{

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1).setFirstLineIndent(Indent.I2));
        }

        for (String literature : Text.REACTOMELITERATRUE) {
            report.addParagraph(new Paragraph(literature).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1));
        }
    }
}
