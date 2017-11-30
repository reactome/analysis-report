package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;

import java.net.MalformedURLException;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TitleAndLogo implements Manipulator {
    private static final String logo = "src/main/resources/images/logo.png";
    private static final String title = "Report for Analysis tools Review";

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws MalformedURLException {
        report.addLogo(logo)
                .addTopTitle(title);
    }
}
