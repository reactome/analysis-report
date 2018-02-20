package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.Header;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {
    private static final Logger LOGGER = LoggerFactory.getLogger(Summary.class);

    public void render(AnalysisReport report, AnalysisStoredResult result) throws IOException {

        report.add(new Header("Summary", FontSize.H2))
                // TODO: 12/02/18 fill correct how many identifiers was found
                .add(new Paragraph(String.format("\t\u2022 %s of %s identifiers you submitted was ", result.getPathways().size(), (result.getNotFound().size() + result.getPathways().size())))
                        .add(new Text("found").setFontColor(report.getLinkColor()).setAction(PdfAction.createGoTo("identifierFound")))
//                        .add(PdfUtils.createGoToLinkIcon(report.getLinkIcon(), FontSize.H4, "identifierFound"))
                        .add(" in Reactome.")
                        .setFontSize(FontSize.H4))
                .add(new Header(String.format("\t\u2022 %s pathways was hit in Reactome total ${totalPathway} pathways.", result.getPathways().size()), FontSize.H4))
                .add(new Header(String.format("\t\u2022 %s of top Enhanced/Overrepresented pathways was list based on p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H4))
                .add(new Header("\t\u2022 The Pathways Overview diagram for this analysis:", FontSize.H4));

        // add fireworks to report
        addFireworks(report, result);

    }

    private void addFireworks(AnalysisReport report, AnalysisStoredResult result) throws IOException {
        BufferedImage image = FireworksHelper.getFireworks(result);

        if (image != null) {
            Image fireworks = new Image(ImageDataFactory.create(image, Color.WHITE));
            fireworks.setHorizontalAlignment(HorizontalAlignment.CENTER);
            float width = Math.min(fireworks.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(fireworks.getImageHeight(), report.getCurrentPageArea().getHeight());
            report.add(fireworks.scaleToFit(width, height));
        } else {
            LOGGER.warn("No fireworks found for analysis {}.", result.getSummary().getToken());
        }
    }
}
