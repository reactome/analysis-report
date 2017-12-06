package org.reactome.server.tools.analysis.exporter.playground.pdfsections;

import com.itextpdf.layout.renderer.DocumentRenderer;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class TitleAndLogo extends DocumentRenderer {
    private static final String logo = "src/main/resources/images/logo.png";
    private static final String title = "Report for Analysis tools Review";

    public TitleAndLogo(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        super(report, properties.isImmediateFlush());
        report.addLogo(logo)
                .addTopTitle(title);
    }
}
