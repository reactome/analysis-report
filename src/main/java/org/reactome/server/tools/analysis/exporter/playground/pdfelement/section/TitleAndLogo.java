package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TitleAndLogo implements Section {
    private static final String LOGO = "src/main/resources/images/logo.png";
    private static final String TITLE = "Report for Analysis tools Review";

    public void render(AnalysisReport report) throws Exception {
        report.addLogo(LOGO)
                .addTopTitle(TITLE);
    }
}
