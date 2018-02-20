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
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.ListParagraph;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {
    private static final Logger LOGGER = LoggerFactory.getLogger(Summary.class);

    public void render(AnalysisReport report, AnalysisStoredResult result) {
        report.add(new Header("Summary", FontSize.H2));

        List<Paragraph> list = new ArrayList<>();
        list.add(new ListParagraph(String.format("%s of %s identifiers you submitted was ", result.getAnalysisIdentifiers().size(), (result.getNotFoundIdentifiers().size() + result.getAnalysisIdentifiers().size())))
                .add(new Text("found").setFontColor(report.getLinkColor()).setAction(PdfAction.createGoTo("identifierFound")))
                .add(" in Reactome."));
        list.add(new ListParagraph(String.format("%s pathways was hit in Reactome total ${totalPathway} pathways.", result.getPathways().size())));
        list.add(new ListParagraph(String.format("%s of top Enhanced/Overrepresented pathways was list based on p-Value.", report.getProfile().getPathwaysToShow())));
        list.add(new ListParagraph("The Pathway Overview diagram for this analysis:"));
        report.addAsList(list);

        // add fireworks to report
//        addFireworks(report, result);

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
