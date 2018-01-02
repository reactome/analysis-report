package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.Text;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Introduction implements Section {

    public void render(AnalysisReport report, DataSet dataSet) {
        report.addNormalTitle("Introduction");
        for (String introduction : Text.INTRODUCTION) {
            report.addParagraph(introduction, FontSize.H5, 0, MarginLeft.M1);
        }

        for (String literature : Text.REACTOME_LITERATRUE) {
            report.addParagraph(literature, FontSize.H5, 0, MarginLeft.M1);
        }
    }
}
