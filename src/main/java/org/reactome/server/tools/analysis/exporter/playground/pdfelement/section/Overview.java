package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.graph.domain.model.LiteratureReference;
import org.reactome.server.graph.domain.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.MarginLeft;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.model.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableRender;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    private static final Logger LOGGER = LoggerFactory.getLogger(Overview.class);

    public void render(AnalysisReport report) throws Exception {
        TableRender tableRender = new TableRender(report.getDataSet());

        tableRender.createTable(report, TableTypeEnum.SUMMARY);

        addOverviewTable(report, tableRender);

        addPathwaysDetail(report, tableRender);

        long start = Instant.now().toEpochMilli();
        addIdentifierTable(report, tableRender);
        LOGGER.info("spent {}ms to create tables.", Instant.now().toEpochMilli() - start);
    }

    private void addOverviewTable(AnalysisReport report, TableRender tableRender) throws NullLinkIconDestinationException, TableTypeNotFoundException {
        report.addNormalTitle("Overview", FontSize.H2, 0)
                .addNormalTitle(String.format("1. Top %s Over/representation pathways sorted by p-Value.", report.getDataSet().getPathwaysToShow()), FontSize.H3, MarginLeft.M3);
        tableRender.createTable(report, TableTypeEnum.OVERVIEW_TABLE);

        report.addNormalTitle("2. Pathway details.", FontSize.H3, MarginLeft.M3);
    }

    private void addIdentifierTable(AnalysisReport report, TableRender tableRender) throws NullLinkIconDestinationException, TableTypeNotFoundException {
        Paragraph identifierFound = new Paragraph("3. Identifier found.");
        identifierFound.setProperty(Property.DESTINATION, "identifierFound");
        report.addNormalTitle(identifierFound, FontSize.H3, MarginLeft.M3);
        if (report.getDataSet().getIdentifierFounds().get(0).getExpNames().size() != 0) {
            tableRender.createTable(report, TableTypeEnum.IdentifierFound);
        } else {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_FOUND_NO_EXP);
        }
        report.addNormalTitle("4. Identifier not found.", FontSize.H3, MarginLeft.M3);
        if (report.getDataSet().getAnalysisResult().getExpression().getColumnNames().size() != 0) {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND);
        } else {
            tableRender.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND_NO_EXP);
        }
    }

    private void addPathwaysDetail(AnalysisReport report, TableRender tableRender) throws Exception {

//        List<Pathway> pathways = GraphCoreHelper.getPathway(report.getDataSet().getAnalysisResult().getPathways());
        Pathway[] pathways = GraphCoreHelper.getPathway(report.getDataSet().getAnalysisResult().getPathways());
        PathwayDetail pathwayDetail;
        for (int i = 0; i < report.getDataSet().getPathwaysToShow(); i++) {
            pathwayDetail = new PathwayDetail(pathways[i]);

            addTitleAndDiagram(report, report.getDataSet().getAnalysisResult().getPathways().get(0), i);

            report.addNormalTitle("Summation", FontSize.H4, MarginLeft.M4)
                    .addParagraph("Species name:" +
                                    pathways[i].getSpeciesName() +
                                    (pathwayDetail.getCompartments() != null ? ",Compartment name:" + pathwayDetail.getCompartments().get(0).getDisplayName() : "") +
                                    (pathways[i].getIsInDisease() ? ",Disease name:" + pathwayDetail.getDiseases().get(0).getDisplayName() : "") +
                                    (pathways[i].getIsInferred() ? ",Inferred from:" + pathwayDetail.getEvents().iterator().next().getDisplayName() : "") +
//                                    (pathwayDetail.getSummations() != null ? "," + pathwayDetail.getSummations().get(0).getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "").trim()
                                    (pathwayDetail.getSummations() != null ? "," + pathwayDetail.getSummations().get(0).getText().replaceAll("(<br>)+", "\r\n") : "").trim()
//                                    (pathwayDetail.getSummations() != null ? "," + pathwayDetail.getSummations().get(0).getText() : "").trim()
                            , FontSize.H5, MarginLeft.M4);

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, MarginLeft.M4);
            tableRender.createTable(report, report.getDataSet().getIdentifierFounds().get(i).getEntities());

            addCuratorDetail(report, pathwayDetail);
            addLiteratureReference(report, pathwayDetail);
            report.getDataSet().getAnalysisResult().getPathways().remove(0);
            pathways[i] = null;
        }
        report.getDataSet().getAnalysisResult().setPathways(null);
    }

    private void addTitleAndDiagram(AnalysisReport report, org.reactome.server.tools.analysis.exporter.playground.model.Pathway pathway, int order) throws Exception {
        Paragraph identifierFound = new Paragraph(String.format("2.%s. %s (%s", order + 1, pathway.getName(), pathway.getStId()));
        identifierFound.add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H3, URL.QUERYFORPATHWAYDETAILS + pathway.getStId())).add(")");
        identifierFound.setProperty(Property.DESTINATION, pathway.getStId());
        report.addNormalTitle(identifierFound, FontSize.H3, MarginLeft.M4);

        // add diagram to report.
//        BufferedImage image = DiagramHelper.getDiagram(pathway.getStId(), report.getDataSet().getReportArgs());
//        if (image != null) {
//            Image diagram = PdfUtils.createImage(image);
//            diagram.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            float width = Math.min(diagram.getImageWidth(), report.getCurrentPageArea().getWidth());
//            float height = Math.min(diagram.getImageHeight(), report.getCurrentPageArea().getHeight());
//            report.addImage(diagram.scaleToFit(width, height));
//        }
    }

    private void addCuratorDetail(AnalysisReport report, PathwayDetail pathwayDetail) {
        if (pathwayDetail.getAuthored() != null && pathwayDetail.getAuthored().get(0).getAuthor() != null) {
            addCurator(report, "Authors", PdfUtils.getInstanceEditNames(pathwayDetail.getAuthored()));
        }
        if (pathwayDetail.getEdited() != null && pathwayDetail.getEdited().get(0).getAuthor() != null) {
            addCurator(report, "Editors", PdfUtils.getInstanceEditNames(pathwayDetail.getEdited()));
        }
        if (pathwayDetail.getModified().getAuthor() != null) {
            addCurator(report, "Reviewers", PdfUtils.getInstanceEditName(pathwayDetail.getModified()));
        }

    }

    private void addLiteratureReference(AnalysisReport report, PathwayDetail pathwayDetail) throws NullLinkIconDestinationException {
        LiteratureReference literatureReference;
        if (pathwayDetail.getPublications() != null) {
            int length = pathwayDetail.getPublications().size() > 5 ? 5 : pathwayDetail.getPublications().size();
            boolean hasReferences = false;
            for (int j = 0; j < length; j++) {
                if ("LiteratureReference".equals(pathwayDetail.getPublications().get(j).getSchemaClass())) {
                    if (!hasReferences) {
                        report.addNormalTitle("References", FontSize.H4, MarginLeft.M4);
                        hasReferences = true;
                    }
                    literatureReference = (LiteratureReference) pathwayDetail.getPublications().get(j);
                    report.addParagraph(new Paragraph(String.format("%s \"%s\", %s, %s, %s, %s."
                            , PdfUtils.getAuthorDisplayName(literatureReference.getAuthor())
                            , literatureReference.getTitle()
                            , literatureReference.getJournal()
                            , literatureReference.getVolume()
                            , literatureReference.getYear()
                            , literatureReference.getPages()))
                            .add(PdfUtils.createUrlLinkIcon(report.getDataSet().getLinkIcon(), FontSize.H5, literatureReference.getUrl()))
                            .setFontSize(FontSize.H5)
                            .setMarginLeft(MarginLeft.M5)
                    );
                }
            }
        }
    }

    private void addCurator(AnalysisReport report, String title, String content) {
        report.addNormalTitle(new Paragraph(title), FontSize.H4, MarginLeft.M4)
                .addParagraph(content, FontSize.H5, MarginLeft.M5);
    }
}