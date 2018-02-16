package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetFireworksException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;

import java.awt.*;
import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {

    public void render(AnalysisReport report, AnalysisStoredResult result) {
        report.addNormalTitle("Summary", FontSize.H2, 0)
                // TODO: 12/02/18 fill correct how many identifiers was found
                .addNormalTitle(new Paragraph(String.format("1. %s of %s identifiers you submitted was ", result.getPathways().size(), (result.getNotFound().size() + result.getPathways().size())))
                        .add(new Paragraph("found").setFontColor(report.getReactomeColor()).setAction(PdfAction.createGoTo("identifierFound")))
//                        .add(PdfUtils.createGoToLinkIcon(report.getLinkIcon(), FontSize.H4, "identifierFound"))
                        .add(" in Reactome.")
                        .setFontSize(FontSize.H4)
                        .setMarginLeft(MarginLeft.M3))
                .addNormalTitle(String.format("2. %s pathways was hit in Reactome total ${totalPathway} pathways.", result.getPathways().size()), FontSize.H4, MarginLeft.M3)
                .addNormalTitle(String.format("3. %s of top Enhanced/Overrepresented pathways was list based on p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H4, MarginLeft.M3)
                .addNormalTitle("4. The Pathways Overview diagram for this analysis:", FontSize.H4, MarginLeft.M3);

        // add fireworks to report

        try {

            final Image fireworks = new Image(ImageDataFactory.create(FireworksHelper.getFireworks(result), Color.WHITE));
            fireworks.setHorizontalAlignment(HorizontalAlignment.CENTER);
            float width = Math.min(fireworks.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(fireworks.getImageHeight(), report.getCurrentPageArea().getHeight());
            report.addImage(fireworks.scaleToFit(width, height));
        } catch (FailToGetFireworksException | IOException e) {
            e.printStackTrace();
        }
    }
}
