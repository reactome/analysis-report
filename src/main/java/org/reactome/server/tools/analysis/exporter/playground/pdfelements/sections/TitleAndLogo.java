package org.reactome.server.tools.analysis.exporter.playground.pdfelements.sections;

import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TitleAndLogo implements Section{
    private static final String logo = "src/main/resources/images/logo.png";
    private static final String title = "Report for Analysis tools Review";

    public void render(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addLogo(logo)
                .addTopTitle(title);
    }
}
