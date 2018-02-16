package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableRender;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    private static final Logger LOGGER = LoggerFactory.getLogger(Overview.class);


    public void render(AnalysisReport report, AnalysisStoredResult result) throws Exception {
        TableRender tableRender = new TableRender(result);

        tableRender.createTable(report, TableTypeEnum.SUMMARY);

        addOverviewTable(report, tableRender);

        addPathwaysDetail(report, tableRender, result);

        long start = Instant.now().toEpochMilli();
        addIdentifierTable(report, tableRender, result);
        LOGGER.info("spent {}ms to create tables.", Instant.now().toEpochMilli() - start);
    }

    private void addOverviewTable(AnalysisReport report, TableRender tableRender) throws TableTypeNotFoundException {
        report.addNormalTitle("Overview", FontSize.H2, 0)
                .addNormalTitle(String.format("1. Top %s Over/representation pathways sorted by p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H3, MarginLeft.M3);
        tableRender.createTable(report, TableTypeEnum.OVERVIEW_TABLE);

        report.addNormalTitle("2. Pathway details.", FontSize.H3, MarginLeft.M3);
    }

    private void addIdentifierTable(AnalysisReport report, TableRender tableRender, AnalysisStoredResult result) throws NullLinkIconDestinationException, TableTypeNotFoundException {
        Paragraph identifierFound = new Paragraph("3. Identifier found.");
        identifierFound.setProperty(Property.DESTINATION, "identifierFound");
        report.addNormalTitle(identifierFound, FontSize.H3, MarginLeft.M3);
        if (result.getExpressionSummary().getColumnNames().size() != 0) {
            tableRender.createTable(report, TableTypeEnum.IdentifierFound);
        } else {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_FOUND_NO_EXP);
        }
        report.addNormalTitle("4. Identifier not found.", FontSize.H3, MarginLeft.M3);
        if (result.getExpressionSummary().getColumnNames().size() != 0) {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND);
        } else {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND_NO_EXP);
        }
    }

    private void addPathwaysDetail(AnalysisReport report, TableRender tableRender, AnalysisStoredResult result) throws Exception {

//        List<Pathway> pathways = GraphCoreHelper.getPathway(result.getPathways());
        Pathway[] pathways = GraphCoreHelper.getPathway(result.getPathways().subList(0, report.getProfile().getPathwaysToShow()));
        for (int i = 0; i < report.getProfile().getPathwaysToShow(); i++) {
            addTitleAndDiagram(report, result.getPathways().get(i), i);

//             add diagram to report.
//            addAsSVG(report, result, i);
            addAsPNG(report, result, i);

            report.addNormalTitle("Summation", FontSize.H4, MarginLeft.M4)
                    .addParagraph(new Paragraph("Species name:" +
                            pathways[i].getSpeciesName() +
                            (pathways[i].getCompartment() != null ? ",Compartment name:" + pathways[i].getCompartment().get(0).getDisplayName() : "") +
                            (pathways[i].getIsInDisease() ? ",Disease name:" + pathways[i].getDisease().get(0).getDisplayName() : "") +
                            (pathways[i].getIsInferred() ? ",Inferred from:" + pathways[i].getInferredFrom().iterator().next().getDisplayName() : "") +
//                                    (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "").trim()
                            (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText().replaceAll("(<br>)+", "\r\n") : "").trim()
//                                    (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText() : "").trim()
                    ).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M4));

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, MarginLeft.M4);
            tableRender.createTable(report, result.getPathways().get(i));

            addCuratorDetail(report, pathways[i]);
            addLiteratureReference(report, pathways[i]);
            pathways[i] = null;
        }
    }

    private void addAsPNG(AnalysisReport report, AnalysisStoredResult result, int i) throws IOException {
        BufferedImage image = DiagramHelper.getPNGDiagram(result.getPathways().get(i).getStId(), result);
        if (image != null) {
            Image diagram = new Image(ImageDataFactory.create(image, Color.WHITE));
            float width = Math.min(diagram.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(diagram.getImageHeight(), report.getCurrentPageArea().getHeight());
            report.addImage(diagram.scaleToFit(width, height));
        } else {
            LOGGER.warn("No diagram found for pathway : {}({}).", result.getPathways().get(i).getName(), result.getPathways().get(i).getStId());
        }
    }

    private void addAsSVG(AnalysisReport report, AnalysisStoredResult result, int i) throws TranscoderException, IOException {
        SVGDocument document = DiagramHelper.getSVGDiagram(result.getPathways().get(i).getStId(), result);
        if (document != null) {
            final BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

            TranscoderInput input = new TranscoderInput(document);
            String viewbox = document.getRootElement().getAttribute(SVGConstants.SVG_VIEW_BOX_ATTRIBUTE);
            String[] split = viewbox.split(" ");
            float width = Math.min(Float.valueOf(split[2]), report.getCurrentPageArea().getWidth());
            float height = Math.min(Float.valueOf(split[3]), report.getCurrentPageArea().getHeight());
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
            transcoder.transcode(input, null);

            final BufferedImage image = transcoder.image;
            Image diagram = new Image(ImageDataFactory.create(image, Color.WHITE));
            diagram.setHorizontalAlignment(HorizontalAlignment.CENTER);

//                diagram.scaleToFit(width, height);
            report.addImage(diagram);
        } else {
            LOGGER.warn("No diagram found for pathway : {}({}).", result.getPathways().get(i).getName(), result.getPathways().get(i).getStId());
        }
    }

    private void addTitleAndDiagram(AnalysisReport report, PathwayNodeSummary pathway, int index) throws Exception {
        Paragraph identifierFound = new Paragraph(String.format("2.%s. %s (%s", index + 1, pathway.getName(), pathway.getStId()));
//        identifierFound.setProperty(Property.DESTINATION, pathway.getStId());
        identifierFound.setDestination(pathway.getStId());
        identifierFound.add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H3, URL.QUERYFORPATHWAYDETAILS.concat(pathway.getStId()))).add(")");
        report.addNormalTitle(identifierFound, FontSize.H3, MarginLeft.M4);
    }

    private void addCuratorDetail(AnalysisReport report, Pathway pathway) {
        if (pathway.getAuthored() != null && pathway.getAuthored().get(0).getAuthor() != null) {
            addCurator(report, "Authors", PdfUtils.getInstanceEditNames(pathway.getAuthored()));
        }
        if (pathway.getEdited() != null && pathway.getEdited().get(0).getAuthor() != null) {
            addCurator(report, "Editors", PdfUtils.getInstanceEditNames(pathway.getEdited()));
        }
        if (pathway.getModified().getAuthor() != null) {
            addCurator(report, "Reviewers", PdfUtils.getInstanceEditName(pathway.getModified()));
        }

    }

    private void addLiteratureReference(AnalysisReport report, Pathway pathway) throws NullLinkIconDestinationException {
        LiteratureReference literatureReference;
        if (pathway.getLiteratureReference() != null) {
            int length = pathway.getLiteratureReference().size() > 5 ? 5 : pathway.getLiteratureReference().size();
            boolean hasReferences = false;
            for (int j = 0; j < length; j++) {
                if ("LiteratureReference".equals(pathway.getLiteratureReference().get(j).getSchemaClass())) {
                    if (!hasReferences) {
                        report.addNormalTitle("References", FontSize.H4, MarginLeft.M4);
                        hasReferences = true;
                    }
                    literatureReference = (LiteratureReference) pathway.getLiteratureReference().get(j);
                    report.addParagraph(new Paragraph(String.format("%s \"%s\", %s, %s, %s, %s."
                            , PdfUtils.getAuthorDisplayName(literatureReference.getAuthor())
                            , literatureReference.getTitle()
                            , literatureReference.getJournal()
                            , literatureReference.getVolume()
                            , literatureReference.getYear()
                            , literatureReference.getPages()))
                            .add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H5, literatureReference.getUrl()))
                            .setFontSize(FontSize.H5)
                            .setMarginLeft(MarginLeft.M5)
                    );
                }
            }
        }
    }

    private void addCurator(AnalysisReport report, String title, String content) {
        report.addNormalTitle(new Paragraph(title), FontSize.H4, MarginLeft.M4)
                .addParagraph(new Paragraph(content).setFontSize(FontSize.H5).setMarginLeft(MarginLeft.M5));
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {
        final boolean transparent = true;
        private BufferedImage image;

        BufferedImageTranscoder() {
        }

        public BufferedImage createImage(int w, int h) {
            if (transparent) {
                return new BufferedImage(w, h, 2);
            } else {
                BufferedImage image = new BufferedImage(w, h, 1);
                Graphics2D graphics = image.createGraphics();
                graphics.setBackground(Color.WHITE);
                graphics.clearRect(0, 0, image.getWidth(), image.getHeight());
                return image;
            }
        }

        public void writeImage(BufferedImage image, TranscoderOutput output) {
            this.image = image;
        }

        public BufferedImage getImage() {
            return this.image;
        }
    }
}