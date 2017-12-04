package org.reactome.server.tools.analysis.exporter.playground.pdfoperator;

import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TitleAndLogo implements PdfOperator {
    private static final String logo = "src/main/resources/images/logo.png";
    private static final String title = "Report for Analysis tools Review";

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addLogo(logo)
                .addTopTitle(title);
    }
}
