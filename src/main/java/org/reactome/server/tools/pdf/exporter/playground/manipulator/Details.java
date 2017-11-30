package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.domains.IdentifiersWasFound;
import org.reactome.server.tools.pdf.exporter.playground.domains.Pathway;
import org.reactome.server.tools.pdf.exporter.playground.domains.PathwayDetail;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.AnalysisReport;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.PdfProperties;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.TableFactory;
import org.reactome.server.tools.pdf.exporter.playground.pdfelements.TableType;
import org.reactome.server.tools.pdf.exporter.playground.resttemplate.RestTemplateFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Details implements Manipulator {
    private static TableFactory tableFactory;

    @Override
    public void manipulatePDF(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        tableFactory = new TableFactory(properties, dataSet);
        report.addNormalTitle("Details")
                .addNormalTitle("1. Top " + properties.getNumberOfPathwaysToShow() + " Overrepresentation pathways sorted by p-Value.", FontSize.H3, 30);
        report.add(tableFactory.getTable(TableType.Overview));
        report.addNormalTitle("2. Pathway details.", FontSize.H3, 30);
        addPathwaysDetails(report, properties, dataSet);
        Paragraph paragraph = new Paragraph("3. Identifiers was found.").setFontSize(FontSize.H3).setFirstLineIndent(30);
        paragraph.setProperty(Property.DESTINATION, "IdentifiersWasFound");
        report.addNormalTitle(paragraph)
                .add(tableFactory.getTable(TableType.IdentifiersWasFound));
        report.addNormalTitle("4. Identifiers was not found.", FontSize.H3, 30)
                .add(tableFactory.getTable(TableType.IdentifiersWasNotFound));
    }


    public void addPathwaysDetails(AnalysisReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        PathwayDetail pathwayDetail = null;
        RestTemplate restTemplate = RestTemplateFactory.getInstance();
        Paragraph paragraph;
        Pathway[] pathways = dataSet.getPathways();
        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
            pathwayDetail = restTemplate.getForObject(org.reactome.server.tools.pdf.exporter.playground.constants.URL.QUERYFORPATHWAYDETAIL, PathwayDetail.class, pathways[i].getStId());
            paragraph = new Paragraph("2." + (i + 1) + ". " + pathways[i].getName())
                    .add(new Link(" (" + pathways[i].getStId() + ")", PdfAction.createURI(org.reactome.server.tools.pdf.exporter.playground.constants.URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())))
                    .setFontSize(FontSize.H3)
                    .setFirstLineIndent(40);
            paragraph.setProperty(Property.DESTINATION, pathways[i].getName());
            report.add(paragraph);

            // TODO: 29/11/17 add the diagram;

            report.addDiagram("R-HSA-169911")
                    .addNormalTitle("Summation", FontSize.H4, 40)
                    .add(new Paragraph("species name:" +
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
                        .add(new Paragraph(pathwayDetail.getAuthors().getDisplayName()).setFontSize(12).setFirstLineIndent(50));

            }
            if (pathwayDetail.getEditors() != null) {
                report.addNormalTitle("Editors", FontSize.H4, 40)
                        .add(new Paragraph(pathwayDetail.getEditors().getDisplayName()).setFontSize(12).setFirstLineIndent(50));

            }
            if (pathwayDetail.getReviewers() != null) {
                report.addNormalTitle("Reviewers", FontSize.H4, 40)
                        .add(new Paragraph(pathwayDetail.getReviewers()[0].getDisplayName()).setFontSize(12).setFirstLineIndent(50));
            }
            if (pathwayDetail.getLiteratureReference() != null) {
                report.addNormalTitle("References", FontSize.H4, 40)
                        .add(new Paragraph("\"" + pathwayDetail.getLiteratureReference()[0].getTitle() + "\"," +
                                pathwayDetail.getLiteratureReference()[0].getJournal() + "," +
                                pathwayDetail.getLiteratureReference()[0].getVolume() + "," +
                                pathwayDetail.getLiteratureReference()[0].getYear() + "," +
                                pathwayDetail.getLiteratureReference()[0].getPages() + ".").setFontSize(12).setFirstLineIndent(50));

            }

        }
    }
}
