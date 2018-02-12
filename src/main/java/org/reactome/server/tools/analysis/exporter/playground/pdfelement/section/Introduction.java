package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Introduction implements Section {

    private static final String[] INTRODUCTION = PdfUtils.getText("src/main/resources/texts/introduction.txt");
    private static final String[] LITERATURE = PdfUtils.getText("src/main/resources/texts/references.txt");

    public void render(AnalysisReport report) throws NullLinkIconDestinationException {
        report.addNormalTitle("Introduction", FontSize.H2, 0);
        for (String introduction : INTRODUCTION) {
            report.addParagraph(new Paragraph(introduction).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M2));
        }

        for (int i = 0; i < LITERATURE.length; i++) {
            report.addParagraph(new Paragraph(LITERATURE[i])
                    .setFontSize(FontSize.H5)
                    .setMarginLeft(MarginLeft.M2).add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H5, LITERATURE[++i])));
        }
    }
}
