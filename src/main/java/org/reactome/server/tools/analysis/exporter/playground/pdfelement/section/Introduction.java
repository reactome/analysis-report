package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Indent;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.Text;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Introduction implements Section {

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1).setFirstLineIndent(Indent.I2));
        }

        for (String literature : Text.REACTOME_LITERATRUE) {
            report.addParagraph(new Paragraph(literature).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M1));
        }
    }
}
