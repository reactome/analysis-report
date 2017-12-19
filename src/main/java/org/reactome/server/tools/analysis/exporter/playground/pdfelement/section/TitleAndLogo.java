package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TitleAndLogo implements Section {
    private static final String logo = "src/main/resources/images/logo.png";
    private static final String title = "Report for Analysis tools Review";

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addLogo(logo)
                .addTopTitle(title);
    }
}
