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
import org.reactome.server.tools.analysis.exporter.playground.constant.Colors;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.ListParagraph;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.element.P;
import org.reactome.server.tools.analysis.exporter.playground.util.FireworksHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Section Summary contains analysis parameter in the analysis result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Summary implements Section {
    private static final Logger LOGGER = LoggerFactory.getLogger(Summary.class);
    private static final List<String> DESCRIPTION = PdfUtils.getText("texts/description.txt");


    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws IOException {
        report.add(new AreaBreak());
        report.add(new Header("2: Summary of Parameters and Results", FontSize.H1).setDestination("parametersAnaResults"));

        List<Paragraph> list = new ArrayList<>();

        list.add(new ListParagraph("Species: ".concat(GraphCoreHelper.getSpeciesName(report.getReportArgs().getSpecies()))));
        list.add(new ListParagraph("Interactors: ".concat(asr.getSummary().isInteractors() ? "interactors included" : "interactors not included")));
        list.add(new ListParagraph("Type: ".concat(asr.getSummary().getType())));
        list.add(new ListParagraph("Unique ID for Analysis: ".concat(asr.getSummary().getToken())));
        list.add(new ListParagraph(
                String.format("%s of %s identifiers submitted were "
                        , asr.getAnalysisIdentifiers().size()
                        , (asr.getNotFoundIdentifiers().size() + asr.getAnalysisIdentifiers().size())))
                .add(new Text("found").setFontColor(Colors.REACTOME_COLOR).setAction(PdfAction.createGoTo("identifiersFound")))
                .add(" in Reactome."));
        list.add(new ListParagraph(String.format("%s Reactome pathways were hit.", asr.getPathways().size())));
        list.add(new ListParagraph(String.format("%s top overrepresented pathways are listed based on their p-value.", report.getProfile().getPathwaysToShow())));
        list.add(new ListParagraph("Genome-wide overview of pathway analysis results: "));
        report.addAsList(list);

        // add fireworks to report
        addFireworks(report, asr);
        for (String description : DESCRIPTION) {
            report.add(new P(description));
        }
    }

    private void addFireworks(AnalysisReport report, AnalysisStoredResult asr) throws IOException {
        BufferedImage image = FireworksHelper.getFireworks(asr);

        if (image != null) {
            Image fireworks = new Image(ImageDataFactory.create(image, java.awt.Color.WHITE));
            fireworks.setHorizontalAlignment(HorizontalAlignment.CENTER);
            float width = Math.min(fireworks.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(fireworks.getImageHeight(), report.getCurrentPageArea().getHeight());
            report.add(fireworks.scaleToFit(width, height));
        } else {
            LOGGER.error("No fireworks found for analysis {}.", asr.getSummary().getToken());
        }
    }
}
