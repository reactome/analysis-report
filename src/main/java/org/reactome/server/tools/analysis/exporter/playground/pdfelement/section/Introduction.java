package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.Header;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

    private static final String[] INTRODUCTION = PdfUtils.getText("src/main/resources/texts/introduction.txt");
    private static final String[] LITERATURE = PdfUtils.getText("src/main/resources/texts/references.txt");

    public void render(AnalysisReport report, AnalysisStoredResult result) throws NullLinkIconDestinationException {
        report.add(new Header("Introduction", FontSize.H2));
        for (String introduction : INTRODUCTION) {
            report.add(new Paragraph(introduction).setFontSize(FontSize.H5));
        }

        for (int i = 0; i < LITERATURE.length; i++) {
            report.add(new Paragraph(LITERATURE[i])
                    .setFontSize(FontSize.H5).add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H5, LITERATURE[++i])));
        }
    }
}
