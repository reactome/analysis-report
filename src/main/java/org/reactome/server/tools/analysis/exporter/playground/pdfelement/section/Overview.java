package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Property;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.jsoup.Jsoup;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableRenderer;
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

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    private static final Logger LOGGER = LoggerFactory.getLogger(Overview.class);


    public void render(AnalysisReport report, AnalysisStoredResult result) throws Exception {
        TableRenderer tableRenderer = new TableRenderer(result);
        addOverviewTable(report, tableRenderer);
        addPathwaysDetail(report, tableRenderer, result);
        addIdentifierTable(report, tableRenderer, result);
    }

    private void addOverviewTable(AnalysisReport report, TableRenderer tableRenderer) throws TableTypeNotFoundException {
        report.add(new Header("Overview", FontSize.H2))
                .add(new Header(String.format("\u20221. Top %s Over/representation pathways sorted by p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H3));
        tableRenderer.createTable(report, TableTypeEnum.OVERVIEW_TABLE);

        report.add(new Header("\u20222. Pathway details.", FontSize.H3));
    }

    private void addIdentifierTable(AnalysisReport report, TableRenderer tableRenderer, AnalysisStoredResult result) throws TableTypeNotFoundException {
        Paragraph identifierFound = new Paragraph("\u20023. Identifier found.");
        identifierFound.setProperty(Property.DESTINATION, "identifierFound");
        identifierFound.setFontSize(FontSize.H3);
        report.add(identifierFound);
        if (result.getExpressionSummary().getColumnNames().size() != 0) {
            tableRenderer.createTable(report, TableTypeEnum.IdentifierFound);
        } else {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_FOUND_NO_EXP);
        }
        report.add(new Header("\u20224. Identifier not found.", FontSize.H3));
        if (result.getExpressionSummary().getColumnNames().size() != 0) {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND);
        } else {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND_NO_EXP);
        }
    }

    private void addPathwaysDetail(AnalysisReport report, TableRenderer tableRenderer, AnalysisStoredResult result) throws Exception {

//        List<Pathway> pathways = GraphCoreHelper.getPathway(result.getPathways());
        Pathway[] pathways = GraphCoreHelper.getPathway(result.getPathways().subList(0, report.getProfile().getPathwaysToShow()));
        for (int i = 0; i < report.getProfile().getPathwaysToShow(); i++) {
            addTitleAndDiagram(report, result.getPathways().get(i), i);

//             add diagram to report.
//            addAsSVG(report, result, i);
            addAsPNG(report, result, i);
            report.add(new Header("Summation", FontSize.H4))
                    .add(new Paragraph("Species name:" +
                            pathways[i].getSpeciesName() +
                            (pathways[i].getCompartment() != null ? ",Compartment name:" + pathways[i].getCompartment().get(0).getDisplayName() : "") +
                            (pathways[i].getIsInDisease() ? ",Disease name:" + pathways[i].getDisease().get(0).getDisplayName() : "") +
                            (pathways[i].getIsInferred() ? ",Inferred from:" + pathways[i].getInferredFrom().iterator().next().getDisplayName() : "") +
//                                    (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "").trim()
//                            (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText().replaceAll("(<br>)+", "\r\n") : "").trim()
                            (pathways[i].getSummation() != null ? "," + Jsoup.parseBodyFragment(pathways[i].getSummation().get(0).getText()).body().text() : "").trim()
//                                    (pathways[i].getSummation() != null ? "," + pathways[i].getSummation().get(0).getText() : "").trim()
                    ).setFontSize(FontSize.H5).setMarginLeft(report.getLeftMargin()));

//            System.out.println(Jsoup.parseBodyFragment(pathways[i].getSummation().get(0).getText()).body().text());
            report.add(new Header("List of identifiers found at this pathway", FontSize.H4));
            tableRenderer.createTable(report, result.getPathways().get(i));

            addCuratorDetail(report, pathways[i]);
            addLiteratureReference(report, pathways[i]);
            pathways[i] = null;
        }
    }

    private void addAsPNG(AnalysisReport report, AnalysisStoredResult result, int i) throws Exception {
        BufferedImage image = DiagramHelper.getPNGDiagram(result.getPathways().get(i).getStId(), result, report.getCurrentPageArea().getWidth(), report.getCurrentPageArea().getHeight());
        if (image != null) {
            Image diagram = new Image(ImageDataFactory.create(image, Color.WHITE));
            report.add(diagram.scaleToFit(report.getCurrentPageArea().getWidth() * 0.8f, report.getCurrentPageArea().getHeight() * 0.8f).setHorizontalAlignment(HorizontalAlignment.CENTER));
        } else {
            LOGGER.warn("No diagram found for pathway : {}({}).", result.getPathways().get(i).getName(), result.getPathways().get(i).getStId());
        }
    }

    private void addAsSVG(AnalysisReport report, AnalysisStoredResult result, int i) throws TranscoderException, IOException {
        SVGDocument document = DiagramHelper.getSVGDiagram(result.getPathways().get(i).getStId(), result);
        if (document != null) {
            final BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

            TranscoderInput input = new TranscoderInput(document);
            transcoder.transcode(input, null);

            final BufferedImage image = transcoder.image;
            Image diagram = new Image(ImageDataFactory.create(image, Color.WHITE));
            float width = Math.min(diagram.getImageWidth(), report.getCurrentPageArea().getWidth());
            float height = Math.min(diagram.getImageHeight(), report.getCurrentPageArea().getHeight());
            diagram.setHorizontalAlignment(HorizontalAlignment.CENTER);
            report.add(diagram.scaleToFit(width, height));
        } else {
            LOGGER.warn("No diagram found for pathway : {}({}).", result.getPathways().get(i).getName(), result.getPathways().get(i).getStId());
        }
    }

    private void addTitleAndDiagram(AnalysisReport report, PathwayNodeSummary pathway, int index) {
//        Paragraph identifierFound = new Paragraph(String.format("2.%s. %s (%s", index + 1, pathway.getName(), pathway.getStId()));
        Paragraph identifierFound = new Paragraph(String.format("2.%s. %s (", index + 1, pathway.getName()));
        identifierFound.setDestination(pathway.getStId());
        identifierFound.setFontSize(FontSize.H3);
//        identifierFound.add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H3, URL.QUERYFORPATHWAYDETAILS.concat(pathway.getStId()))).add(")");
        identifierFound.add(new Text(pathway.getStId()).setFontColor(report.getLinkColor()).setAction(PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS.concat(pathway.getStId())))).add(")");

        report.add(identifierFound);
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
            boolean hasReference = false;
            for (int j = 0; j < length; j++) {
                if ("LiteratureReference".equals(pathway.getLiteratureReference().get(j).getSchemaClass())) {
                    if (!hasReference) {
                        report.add(new Header("References", FontSize.H4));
                        hasReference = true;
                    }
                    literatureReference = (LiteratureReference) pathway.getLiteratureReference().get(j);
                    report.add(new Paragraph("\u2002 ").add(String.format("%s \"%s\", %s, %s, %s, %s."
                            , PdfUtils.getAuthorDisplayName(literatureReference.getAuthor())
                            , literatureReference.getTitle()
                            , literatureReference.getJournal()
                            , literatureReference.getVolume()
                            , literatureReference.getYear()
                            , literatureReference.getPages()))
                            .add(PdfUtils.createUrlLinkIcon(report.getLinkIcon(), FontSize.H5, literatureReference.getUrl()))
                            .setFontSize(FontSize.H5)
                            .setMarginLeft(report.getLeftMargin())
                    );
                }
            }
        }
    }

    private void addCurator(AnalysisReport report, String title, String content) {
        report.add(new Header(title, FontSize.H4))
                .add(new Paragraph(content).setFontSize(FontSize.H5).setMarginLeft(report.getLeftMargin()));
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage image;

        BufferedImageTranscoder() {
        }

        public BufferedImage createImage(int w, int h) {
            return new BufferedImage(w, h, 2);
        }

        public void writeImage(BufferedImage image, TranscoderOutput output) {
            this.image = image;
        }

        public BufferedImage getImage() {
            return this.image;
        }
    }
}