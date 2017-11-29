package org.reactome.server.tools.pdf.exporter.playground.manipulator;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.domains.*;
import org.reactome.server.tools.pdf.exporter.playground.pdfexporter.PdfProperties;
import org.reactome.server.tools.pdf.exporter.playground.resttemplate.RestTemplateFactory;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.util.Map;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class Details implements Manipulator {

    @Override
    public void manipulatePDF(PdfReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        report.addNormalTitle("Details")
                .addNormalTitle("1. Top " + properties.getNumberOfPathwaysToShow() + " Overrepresentation pathways sorted by p-Value.", FontSize.H4, 30);
        addOverviewTable(report, properties, dataSet);
        //craete pathways details
        report.add(new Paragraph("2. Pathway details.").setFontSize(FontSize.H3).setFirstLineIndent(30));
        addPathwaysDetails(report, properties, dataSet);
        addIdentifiersWasFoundTable(report, properties, dataSet);
        report.addNormalTitle("4. Identifiers was not found.", FontSize.H3, 30);
        addIdentifiersWasNotFoundTable(report, properties, dataSet);
    }

    public void addOverviewTable(PdfReport report, PdfProperties properties, DataSet dataSet) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{5, 1, 1, 1, 3, 3, 1, 1, 1, 2}));
        table.setWidthPercent(100);
        table.setFontSize(6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        String[] headers = {"Pathway name", "Entities found", "Entities Total", "Entities ratio", "Entities pValue", "Entities FDR", "Reactions found", "Reactions total", "Reactions ratio", "Species name"};
        for (String header : headers)
            table.addHeaderCell(header);

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(4);

        Pathway[] pathways = dataSet.getPathways();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
            table.addCell(new Paragraph(new Link(pathways[i].getName(), PdfAction.createGoTo(pathways[i].getName()))));
            table.addCell(pathways[i].getEntities().getFound() + "");
            table.addCell(pathways[i].getEntities().getTotal() + "");
            table.addCell(numberFormat.format(pathways[i].getEntities().getRatio()));
            table.addCell(pathways[i].getEntities().getpValue() + "");
            table.addCell(pathways[i].getEntities().getFdr() + "");
            table.addCell(pathways[i].getReactions().getFound() + "");
            table.addCell(pathways[i].getReactions().getTotal() + "");
            table.addCell(numberFormat.format(pathways[i].getReactions().getRatio()));
            table.addCell(pathways[i].getSpecies().getName());
        }
        report.add(table);
    }

    public void addIdentifiersWasFoundTable(PdfReport report, PdfProperties properties, DataSet dataSet) {

        Paragraph paragraph = new Paragraph("3. Identifiers was found.").setFontSize(16).setFirstLineIndent(30);
        paragraph.setProperty(Property.DESTINATION, "IdentifiersWasFound");
        report.addNormalTitle(paragraph);
        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
        ResultAssociatedWithToken resultAssociatedWithToken = dataSet.getResultAssociatedWithToken();
        Map<String, Identifier> identifiersWasFiltered = dataSet.getIdentifiersWasFiltered();
        Table table = new Table(identifiersWasFounds[0].getExpNames().length + 3);
        table.setWidthPercent(100);
        table.setMarginLeft(40)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        if (resultAssociatedWithToken.getExpression().getColumnNames().length != 0) {
            String[] header = resultAssociatedWithToken.getExpression().getColumnNames();

            for (String head : header) {
                table.addHeaderCell(head);
            }
        } else {

        }
        for (Map.Entry<String, Identifier> entry : identifiersWasFiltered.entrySet()) {
            Cell cell = new Cell().add(entry.getKey());
            cell.setProperty(Property.DESTINATION, entry.getKey());
            table.addCell(cell);
            table.addCell(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", ""));
            table.addCell(entry.getValue().getMapsTo().get(0).getResource());
            for (Double exp : entry.getValue().getExp()) {
                table.addCell(exp + "");
            }
        }
        report.add(table);
    }

    public void addIdentifiersWasNotFoundTable(PdfReport report, PdfProperties properties, DataSet dataSet) {
        Identifier[] identifiersWasNotFounds = dataSet.getIdentifiersWasNotFounds();
        ResultAssociatedWithToken resultAssociatedWithToken = dataSet.getResultAssociatedWithToken();
        Table table = new Table(identifiersWasNotFounds[0].getExp().length + 1);

//            table.setWidthPercent(100);
        table.setMarginLeft(40).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        table.addHeaderCell("Identifiers");
        if (resultAssociatedWithToken.getExpression().getColumnNames().length != 0) {
            String[] header = resultAssociatedWithToken.getExpression().getColumnNames();

            for (String head : header) {
                table.addHeaderCell(head);
            }
        } else {
            //another table style
        }

        for (Identifier identifier : identifiersWasNotFounds) {
            table.addCell(new Cell().add(identifier.getId()));
            for (Double exp : identifier.getExp()) {
                table.addCell(exp.toString());
            }
        }
        report.add(table);
    }

    public void addPathwaysDetails(PdfReport report, PdfProperties properties, DataSet dataSet) throws Exception {
        PathwayDetail pathwayDetail = null;
        RestTemplate restTemplate = RestTemplateFactory.getInstance();
        Paragraph paragraph = null;
        Table table = null;
        Pathway[] pathways = dataSet.getPathways();
        IdentifiersWasFound[] identifiersWasFounds = dataSet.getIdentifiersWasFounds();
        for (int i = 0; i < properties.getNumberOfPathwaysToShow(); i++) {
//            for (int i = 0; i < 2; i++) {
            pathwayDetail = restTemplate.getForObject(org.reactome.server.tools.pdf.exporter.playground.constants.URL.QUERYFORPATHWAYDETAIL, PathwayDetail.class, pathways[i].getStId());
            paragraph = new Paragraph("2." + (i + 1) + ". " + pathways[i].getName())
                    .add(new Link(" (" + pathways[i].getStId() + ")", PdfAction.createURI(org.reactome.server.tools.pdf.exporter.playground.constants.URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())))
                    .setFontSize(FontSize.H3)
                    .setFirstLineIndent(40);
            paragraph.setProperty(Property.DESTINATION, pathways[i].getName());
            report.add(paragraph);
            // TODO: 29/11/17 add the diagram;
            report.addDiagram("R-HSA-169911");
            report.add(new Paragraph("Summation")
                    .setFontSize(14)
                    .setFirstLineIndent(40))
                    .add(new Paragraph("species name:" +
                            pathwayDetail.getSpeciesName() +
                            (pathwayDetail.getCompartment() != null ? ",compartment name:" + pathwayDetail.getCompartment()[0].getDisplayName() : "") +
                            (pathwayDetail.isInDisease() ? ",disease name:" + pathwayDetail.getDisease()[0].getDisplayName() : "") +
                            (pathwayDetail.isInferred() ? ",inferred from:" + pathwayDetail.getInferredFrom()[0].getDisplayName() : "") +
                            (pathwayDetail.getSummation() != null ? "," + pathwayDetail.getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                    ).setFontSize(12).setFirstLineIndent(50).setMarginLeft(40));

            report.add(new Paragraph("List of identifiers was found at this pathway")
                    .setFontSize(14)
                    .setFirstLineIndent(40));
            //add the table for identifiers was found at

            table = new Table(6);
            table.setMarginLeft(60).setFontSize(10).setTextAlignment(TextAlignment.LEFT);
            for (Identifier identifier : identifiersWasFounds[i].getEntities()) {
                table.addCell(new Cell().add(identifier.getId()).setAction(PdfAction.createGoTo(identifier.getId())).setBorder(Border.NO_BORDER));
            }
            for (int j = 0; j < 6 - identifiersWasFounds[i].getEntities().length % 6; j++) {
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
            }
            report.add(table);
            if (pathwayDetail.getAuthors() != null) {
                report.add(new Paragraph("Authors")
                        .setFontSize(14)
                        .setFirstLineIndent(40))
                        .add(new Paragraph(pathwayDetail.getAuthors().getDisplayName()).setFontSize(12).setFirstLineIndent(50));

            }
            if (pathwayDetail.getEditors() != null) {
                report.add(new Paragraph("Editors")
                        .setFontSize(14)
                        .setFirstLineIndent(40))
                        .add(new Paragraph(pathwayDetail.getEditors().getDisplayName()).setFontSize(12).setFirstLineIndent(50));

            }
            if (pathwayDetail.getReviewers() != null) {
                report.add(new Paragraph("Reviewers")
                        .setFontSize(14)
                        .setFirstLineIndent(40))
                        .add(new Paragraph(pathwayDetail.getReviewers()[0].getDisplayName()).setFontSize(12).setFirstLineIndent(50));
            }
            if (pathwayDetail.getLiteratureReference() != null) {
                report.add(new Paragraph("References")
                        .setFontSize(14)
                        .setFirstLineIndent(40))
                        .add(new Paragraph("\"" + pathwayDetail.getLiteratureReference()[0].getTitle() + "\"," +
                                pathwayDetail.getLiteratureReference()[0].getJournal() + "," +
                                pathwayDetail.getLiteratureReference()[0].getVolume() + "," +
                                pathwayDetail.getLiteratureReference()[0].getYear() + "," +
                                pathwayDetail.getLiteratureReference()[0].getPages() + ".").setFontSize(12).setFirstLineIndent(50));

            }

        }
    }
}
