package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
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

    public void render(AnalysisReport report, AnalysisStoredResult analysisStoredResult, SpeciesFilteredResult speciesFilteredResult) throws IOException {
        report.add(new AreaBreak());
        report.add(new Header("Summary", FontSize.H2));

        List<Paragraph> list = new ArrayList<>();
        list.add(new ListParagraph("Interactors: ".concat(analysisStoredResult.getSummary().isInteractors() ? "interactors included" : "interactors not included")));
        list.add(new ListParagraph("Type: ".concat(analysisStoredResult.getSummary().getType())));
        list.add(new ListParagraph("Token: ".concat(analysisStoredResult.getSummary().getToken())));
        list.add(new ListParagraph(
                String.format("%s of %s identifiers submitted were "
                        , analysisStoredResult.getAnalysisIdentifiers().size()
                        , (analysisStoredResult.getNotFoundIdentifiers().size() + analysisStoredResult.getAnalysisIdentifiers().size())))
                .add(new Text("found").setFontColor(report.getLinkColor()).setAction(PdfAction.createGoTo("identifiersFound")))
                .add(" in Reactome."));
        list.add(new ListParagraph(String.format("%s pathways were hit in Reactome.", analysisStoredResult.getPathways().size())));
        list.add(new ListParagraph(String.format("%s of top Enhanced/Overrepresented pathways are listed based on their p-Value.", report.getProfile().getPathwaysToShow())));
        list.add(new ListParagraph("The Pathway Overview diagram for this analysis is presented below: "));
        report.addAsList(list);

        // add fireworks to report
        addFireworks(report, analysisStoredResult);
    }

    private void addFireworks(AnalysisReport report, AnalysisStoredResult analysisStoredResult) throws IOException {
        BufferedImage image = FireworksHelper.getFireworks(analysisStoredResult);

        if (image != null) {
            Image fireworks = new Image(ImageDataFactory.create(image, Color.WHITE));
            fireworks.setHorizontalAlignment(HorizontalAlignment.CENTER);
            float width = Math.min(fireworks.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(fireworks.getImageHeight(), report.getCurrentPageArea().getHeight());
            report.add(fireworks.scaleToFit(width, height));
        } else {
            LOGGER.warn("No fireworks found for analysis {}.", analysisStoredResult.getSummary().getToken());
        }
    }
}
