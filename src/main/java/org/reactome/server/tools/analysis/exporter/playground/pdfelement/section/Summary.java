package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {

    public void render(AnalysisReport report) throws Exception {
        report.addNormalTitle("Summary", FontSize.H2, 0)
                .addNormalTitle(new Paragraph(String.format("1. %s of %s identifiers you submitted was ", report.getDataSet().getIdentifierFound(), report.getDataSet().getTotalIdentifiers()))
                        .add("Found")
                        .add(PdfUtils.createGoToLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H4, "identifierFound"))
                        .add(" in Reactome.")
                        .setFontSize(FontSize.H4)
                        .setMarginLeft(MarginLeft.M3))
                .addNormalTitle(String.format("2. %s pathways was hit in Reactome total ${totalPathway} pathways.", report.getDataSet().getAnalysisResult().getPathwaysFound()), FontSize.H4, MarginLeft.M3)
                .addNormalTitle(String.format("3. %s of top Enhanced/Overrepresented pathways was list based on p-Value.", report.getDataSet().getPathwaysToShow()), FontSize.H4, MarginLeft.M3)
                .addNormalTitle("4. The Pathways Overview diagram for this analysis:", FontSize.H4, MarginLeft.M3);

        // add fireworks to report
//        Image fireworks = PdfUtils.createImage(FireworksHelper.getFireworks(report.getDataSet().getReportArgs()));
//        fireworks.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        float width = Math.min(fireworks.getImageWidth(), report.getCurrentPageArea().getWidth());
//        float height = Math.min(fireworks.getImageHeight(), report.getCurrentPageArea().getHeight());
//        report.addImage(fireworks.scaleToFit(width, height));
    }
}
