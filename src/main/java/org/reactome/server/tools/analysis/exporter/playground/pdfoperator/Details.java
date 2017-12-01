package org.reactome.server.tools.analysis.exporter.playground.pdfoperator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.PdfUtils;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constants.URL;
import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domains.IdentifiersWasFound;
import org.reactome.server.tools.analysis.exporter.playground.domains.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.domains.PathwayDetail;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.TableFactory;
import org.reactome.server.tools.analysis.exporter.playground.pdfelements.TableType;
import org.reactome.server.tools.analysis.exporter.playground.resttemplate.RestTemplateFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Details implements PdfOperator {
    private static TableFactory tableFactory;

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        tableFactory = new TableFactory(properties, dataSet);
        report.addNormalTitle("Details")
                .addNormalTitle("1. Top " + properties.getNumberOfPathwaysToShow() + " Overrepresentation pathways sorted by p-Value.", FontSize.H3, 30)
                .addTable(tableFactory.getTable(TableType.Overview));
        report.addNormalTitle("2. Pathway details.", FontSize.H3, 30);
        addPathwaysDetails(report, properties, dataSet);
        report.addNormalTitle(PdfUtils.setDestination(new Paragraph("3. Identifiers was found.").setFontSize(FontSize.H3).setFirstLineIndent(30), "IdentifiersWasFound"))
                .addTable((dataSet.getIdentifiersWasFounds()[0].getExpNames().length != 0) ? tableFactory.getTable(TableType.IdentifiersWasFound) : tableFactory.getTable(TableType.IdentifiersWasFoundNoEXP));
        report.addNormalTitle("4. Identifiers was not found.", FontSize.H3, 30)
                .addTable((dataSet.getResultAssociatedWithToken().getExpression().getColumnNames().length != 0) ? tableFactory.getTable(TableType.IdentifiersWasNotFound) : tableFactory.getTable(TableType.IdentifiersWasNotFoundNoEXP));
    }


    public void addPathwaysDetails(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        PathwayDetail pathwayDetail;
        RestTemplate restTemplate = RestTemplateFactory.getInstance();
        Paragraph paragraph;
        Pathway[] pathways = dataSet.getPathways();
        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
            pathwayDetail = restTemplate.getForObject(URL.QUERYFORPATHWAYDETAIL, PathwayDetail.class, pathways[i].getStId());
            paragraph = new Paragraph("2." + (i + 1) + ". " + pathways[i].getName())
                    .setFontSize(FontSize.H3)
                    .setFirstLineIndent(40)
                    .add(new Link(" (" + pathways[i].getStId() + ")", PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())));
            report.addParagraph(PdfUtils.setDestination(paragraph, pathways[i].getName()));

            // TODO: 29/11/17 add the correct diagram;
            report.addDiagram("R-HSA-169911")
                    .addNormalTitle("Summation", FontSize.H4, 40)
                    .addParagraph(new Paragraph("species name:" +
                            pathwayDetail.getSpeciesName() +
                            (pathwayDetail.getCompartment() != null ? ",compartment name:" + pathwayDetail.getCompartment()[0].getDisplayName() : "") +
                            (pathwayDetail.isInDisease() ? ",disease name:" + pathwayDetail.getDisease()[0].getDisplayName() : "") +
                            (pathwayDetail.isInferred() ? ",inferred from:" + pathwayDetail.getInferredFrom()[0].getDisplayName() : "") +
                            (pathwayDetail.getSummation() != null ? "," + pathwayDetail.getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                    ).setFontSize(12).setFirstLineIndent(50).setMarginLeft(40));

            report.addNormalTitle("List of identifiers was found at this pathway", FontSize.H4, 40)
                    .add(tableFactory.getTable(identifiersWasFounds[i].getEntities()));
            if (pathwayDetail.getAuthors() != null) {
                report.addNormalTitle("Authors", FontSize.H4, 40)
                        .addParagraph(new Paragraph(pathwayDetail.getAuthors().getDisplayName()).setFontSize(FontSize.H5).setFirstLineIndent(50));

            }
            if (pathwayDetail.getEditors() != null) {
                report.addNormalTitle("Editors", FontSize.H4, 40)
                        .addParagraph(new Paragraph(pathwayDetail.getEditors().getDisplayName()).setFontSize(FontSize.H5).setFirstLineIndent(50));

            }
            if (pathwayDetail.getReviewers() != null) {
                report.addNormalTitle("Reviewers", FontSize.H4, 40)
                        .addParagraph(new Paragraph(pathwayDetail.getReviewers()[0].getDisplayName()).setFontSize(FontSize.H5).setFirstLineIndent(50));
            }
            if (pathwayDetail.getLiteratureReference() != null) {
                report.addNormalTitle("References", FontSize.H4, 40)
                        .addParagraph(new Paragraph("\"" + pathwayDetail.getLiteratureReference()[0].getTitle() + "\"," +
                                pathwayDetail.getLiteratureReference()[0].getJournal() + "," +
                                pathwayDetail.getLiteratureReference()[0].getVolume() + "," +
                                pathwayDetail.getLiteratureReference()[0].getYear() + "," +
                                pathwayDetail.getLiteratureReference()[0].getPages() + ".").setFontSize(FontSize.H5).setFirstLineIndent(50));
            }
        }
    }
}
