package org.reactome.server.tools.analysis.exporter.playground.pdfsections;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.DocumentRenderer;
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
public class Introduction extends DocumentRenderer {

    public Introduction(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        super(report, properties.isImmediateFlush());
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1).setFirstLineIndent(Indent.I2));
        }

        for (String literature : Text.REACTOMELITERATRUE) {
            report.addParagraph(new Paragraph(literature).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1));
        }
    }
}
