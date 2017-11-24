package com.example.demo.itext;

import com.example.demo.constants.Introduction;
import com.example.demo.constants.URL;
import com.example.demo.domains.*;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Byron on 2017/10/21.
 */
@Component
public class ItextDemo {

    public static void test(String token) throws Exception {

        String filePath = "src/main/resources/pdf/" + token + "@" + new Date().getTime() + ".pdf";
//        String filePath = "C:\\Users\\Byron\\PDFs\\" + token + "@" + new Date().getTime() + ".pdf";
//        String filePath = "C:\\Users\\Byron\\PDFs\\" + token + ".pdf";
//        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        try {
//            PdfDocument pdfDocument = new PdfDocument(new PdfReader("src/main/resources/pdf/template.pdf"), new PdfWriter("src/main/resources/pdf/test.pdf"));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String date = simpleDateFormat.format(new Date().getTime());
            RestTemplate restTemplate = new RestTemplate();
            final int numberOfPathwaysToShow = 50;
            ResultAssociatedWithToken resultAssociatedWithToken = restTemplate.getForObject(URL.RESULTASSCIATEDWITHTOKEN, ResultAssociatedWithToken.class, token, numberOfPathwaysToShow);
//            System.out.println(resultAssociatedWithToken.toString())

            Pathways[] pathways = resultAssociatedWithToken.getPathways();
            StringBuilder stIds = new StringBuilder();
            for (Pathways pathway : pathways) {
                stIds.append(pathway.getStId()).append(',');
            }
            IdentifiersWasFound[] identifiersWasFounds = restTemplate.postForObject(URL.IDENTIFIERSWASFOUND, stIds.deleteCharAt(stIds.length() - 1).toString(), IdentifiersWasFound[].class, token);
            Map<String, Identifier> identifiersWasFiltered = identifiersFilter(identifiersWasFounds);
            Identifier[] identifiersWasNotFounds = restTemplate.getForObject(URL.IDENTIFIERSWASNOTFOUND, Identifier[].class, token);

            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(filePath));
            pdfDocument.addNewPage(PageSize.A4);
            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(font, 30));

            Document document = new Document(pdfDocument, PageSize.A4, true);
            document.setTextAlignment(TextAlignment.JUSTIFIED)
                    .setHyphenation(new HyphenationConfig(3, 3));
            Paragraph paragraph = new Paragraph();
            document.setMargins(30, 30, 30, 30);

//            System.out.println(content);
            Image logo;
            logo = new Image(ImageDataFactory.create("src/main/resources/images/logo.png"));
            logo.scale(0.3f, 0.3f);
//            logo.setMarginLeft(10).setMarginTop(10);
            logo.setFixedPosition(document.getLeftMargin() * 0.3f, pdfDocument.getDefaultPageSize().getHeight() - document.getTopMargin() * 0.3f - logo.getImageScaledHeight());
            document.add(logo);
            document.add(new Paragraph("Report for Analysis tools Review").setFont(font).setFontSize(22).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Administrative").setFontSize(16));
//            PdfLinkAnnotation annotation = new PdfLinkAnnotation(new Rectangle(0,0)).setAction();
//            Link link = new Link(token,PdfAction.createURI());
            paragraph.add("This report is intended for reviewers of the pathway analysis:")
                    .add(new Link("\" https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=\"" + token, PdfAction.createURI("https://reactome.org/PathwayBrowser/#/DTAB=AN&ANALYSIS=" + token)))
                    .add("(please note that this URL maybe out of date because of the token can expired at our server end)")
                    .add("and your input identifiers are :");
            for (int i = 0; i < 5; i++) {
                paragraph.add(identifiersWasNotFounds[i].getId() + ",");
            }
            paragraph.add(".... It has been automatically generated in Reactome version " + restTemplate.getForEntity(URL.VERSION, String.class).getBody())
                    .add((" at " + date)).setFontSize(12).setMarginLeft(20).setFirstLineIndent(30);
            document.add(paragraph);
            paragraph = new Paragraph();

            document.add(new Paragraph().add("Introduction").setFont(font).setFontSize(16));
            //set text alignment
            for (String introduction : Introduction.INTRODUCTION) {
                document.add(new Paragraph().add(introduction).setMarginLeft(20).setFirstLineIndent(30));
            }

            paragraph = new Paragraph();
            for (int i = 0; i < Introduction.REACTOMELITERATRUE.length; i++) {
                paragraph.add(new Link(Introduction.REACTOMELITERATRUE[i] + "\n", PdfAction.createURI("https://reactome.org"))).setMarginLeft(20);
            }

            document.add(paragraph);

            //create paragraph summary
            document.add(new Paragraph("Summary").setFontSize(16))
                    .add(new Paragraph("1. " + identifiersWasFiltered.size() + " of " + (identifiersWasFiltered.size() + resultAssociatedWithToken.getIdentifiersNotFound()) + " identifiers you submitted was ")
                            .setFirstLineIndent(30)
                            .add(new Link("found", PdfAction.createGoTo("IdentifiersWasFound")))
                            .add(" in Reactome."))
                    .add(new Paragraph("2. " + resultAssociatedWithToken.getResourceSummary()[1].getPathways() + " pathways was hit in Reactome total {} pathways.")
                            .setFirstLineIndent(30))
                    .add(new Paragraph("3. " + numberOfPathwaysToShow + " of top Enhanced/Overrepresented pathways was list based on p-Value.")
                            .setFirstLineIndent(30))
                    .add(new Paragraph("4. The \"fireworks\" diagram for this pathway analysis:")
                            .setFirstLineIndent(30));


            //create paragraph details
            document.add(new Paragraph("Details").setFontSize(16))
                    .add(new Paragraph("1. Top " + numberOfPathwaysToShow + " Overrepresentation pathways sorted by p-Value.").setFirstLineIndent(30))
                    .add(new Paragraph("Results for:")
                            .add(resultAssociatedWithToken.getResourceSummary()[1].getResource())
                            .add("(" + resultAssociatedWithToken.getResourceSummary()[1].getPathways() + ")")
                            .add("\tType:" + resultAssociatedWithToken.getSummary().getType())
                            .add("\tData:" + resultAssociatedWithToken.getSummary().getSampleName())
                            .add("\tIdentifiers not found:" + resultAssociatedWithToken.getIdentifiersNotFound()).setFontSize(10));
//            Table table = new Table(UnitValue.createPercentArray(new float[]{4 / 19, 1 / 19, 1 / 19, 1 / 19, 3 / 19, 3 / 19, 1 / 19, 1 / 19, 1 / 19, 3 / 19}));
            Table table = new Table(UnitValue.createPercentArray(new float[]{5, 1, 1, 1, 3, 3, 1, 1, 1, 2}));
            table.setWidthPercent(100);
            table.setFont(font).setFontSize(6).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

            table.addHeaderCell("Pathway name");
            table.addHeaderCell("Entities found");
            table.addHeaderCell("Entities Total");
            table.addHeaderCell("Entities ratio");
            table.addHeaderCell("Entities pValue");
            table.addHeaderCell("Entities FDR");
            table.addHeaderCell("Reactions found");
            table.addHeaderCell("Reactions total");
            table.addHeaderCell("Reactions ratio");
            table.addHeaderCell("Species name");

            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(4);
            for (int i = 0; i < numberOfPathwaysToShow; i++) {
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
            document.add(table);

            //craete pathways details
            document.add(new Paragraph("2. Pathway details.").setFontSize(16).setFirstLineIndent(30));

            PathwayDetail pathwayDetail;
            for (int i = 0; i < numberOfPathwaysToShow; i++) {
                pathwayDetail = restTemplate.getForObject(URL.QUERYFORPATHWAYDETAIL, PathwayDetail.class, pathways[i].getStId());
                paragraph = new Paragraph("2." + (i + 1) + ". " + pathways[i].getName())
                        .add(new Link(" (" + pathways[i].getStId() + ")", PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS + pathways[i].getStId())))
                        .setFontSize(14)
                        .setFirstLineIndent(40);
                paragraph.setProperty(Property.DESTINATION, pathways[i].getName());
                logo = new Image(ImageDataFactory.create("src/main/resources/images/logo.png"));
                logo.scale(0.3f, 0.3f);
                paragraph.add(logo);
                document.add(paragraph);
                document.add(new Paragraph("Summation")
                        .setFontSize(14)
                        .setFirstLineIndent(40))//correct intend
                        .add(new Paragraph("species name:" +
                                pathwayDetail.getSpeciesName() +
                                (pathwayDetail.getCompartment() != null ? ",compartment name:" + pathwayDetail.getCompartment()[0].getDisplayName() : "") +
                                (pathwayDetail.isInDisease() ? ",disease name:" + pathwayDetail.getDisease()[0].getDisplayName() : "") +
                                (pathwayDetail.isInferred() ? ",inferred from:" + pathwayDetail.getInferredFrom()[0].getDisplayName() : "") +
                                (pathwayDetail.getSummation() != null ? "," + pathwayDetail.getSummation()[0].getText().replaceAll("</?[a-zA-Z]{1,2}>", "") : "")
                        ).setFont(font).setFontSize(12).setFirstLineIndent(50).setMarginLeft(40));

                document.add(new Paragraph("List of identifiers was found at this pathway")
                        .setFontSize(14)
                        .setFirstLineIndent(40));
                //add the table for identifiers was found at
                table = new Table(6);
                table.setMarginLeft(60).setFont(font).setFontSize(10).setTextAlignment(TextAlignment.LEFT);
                for (Identifier identifier : identifiersWasFounds[i].getEntities()) {
                    table.addCell(new Cell().add(identifier.getId()).setAction(PdfAction.createGoTo(identifier.getId())).setBorder(Border.NO_BORDER));
                }
                for (int j = 0; j < 6 - identifiersWasFounds[i].getEntities().length % 6; j++) {
                    table.addCell(new Cell().setBorder(Border.NO_BORDER));
                }
                document.add(table);
                if (pathwayDetail.getAuthors() != null) {
                    document.add(new Paragraph("Authors")
                            .setFontSize(14)
                            .setFirstLineIndent(40))
                            .add(new Paragraph(pathwayDetail.getAuthors().getDisplayName()).setFont(font).setFontSize(12).setFirstLineIndent(50));

                }
                if (pathwayDetail.getEditors() != null) {
                    document.add(new Paragraph("Editors")
                            .setFontSize(14)
                            .setFirstLineIndent(40))
                            .add(new Paragraph(pathwayDetail.getEditors().getDisplayName()).setFont(font).setFontSize(12).setFirstLineIndent(50));

                }
                if (pathwayDetail.getReviewers() != null) {
                    document.add(new Paragraph("Reviewers")
                            .setFontSize(14)
                            .setFirstLineIndent(40))
                            .add(new Paragraph(pathwayDetail.getReviewers()[0].getDisplayName()).setFont(font).setFontSize(12).setFirstLineIndent(50));
                }
                if (pathwayDetail.getLiteratureReference() != null) {
                    document.add(new Paragraph("References")
                            .setFontSize(14)
                            .setFirstLineIndent(40))
                            .add(new Paragraph("\"" + pathwayDetail.getLiteratureReference()[0].getTitle() + "\"," +
                                    pathwayDetail.getLiteratureReference()[0].getJournal() + "," +
                                    pathwayDetail.getLiteratureReference()[0].getVolume() + "," +
                                    pathwayDetail.getLiteratureReference()[0].getYear() + "," +
                                    pathwayDetail.getLiteratureReference()[0].getPages() + ".").setFont(font).setFontSize(12).setFirstLineIndent(50));

                }


            }

            //create table of identifiers was found
            paragraph = new Paragraph("3. Identifiers was found.").setFontSize(16).setFirstLineIndent(30);
            paragraph.setProperty(Property.DESTINATION, "IdentifiersWasFound");
            document.add(paragraph);
            table = new Table(identifiersWasFounds[0].getExpNames().length + 3);
            table.setWidthPercent(100);
            table.setMarginLeft(40)
                    .setFont(font).setFontSize(10)
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
//                final StringBuilder ids = new StringBuilder();
//                entry.getValue().getMapsTo().forEach(mapsTo -> ids.append(mapsTo.getIds().stream().collect(Collectors.joining()) + ", "));
//                table.addCell(ids.deleteCharAt(ids.length() - 1).deleteCharAt(ids.length() - 1).toString());
                for (Double exp : entry.getValue().getExp()) {
                    table.addCell(new Cell().add(exp + ""));

                }
            }
//            for (IdentifiersWasFound identifiers : identifiersWasFounds) {
//                for (Identifier identifier : identifiers.getEntities()) {
//                    Cell cell = new Cell().add(identifier.getId());
//                    System.out.println(identifier.getId());
//                    cell.setProperty(Property.DESTINATION, identifier.getId());
//                    table.addCell(cell);
//                    table.addCell(new Cell().add(identifier.getMapsTo().get(0).getIds().toString()));
//                    table.addCell(new Cell().add(identifier.getMapsTo().get(0).getResource()));
//                    for (Double exp : identifier.getExp()) {
//                        table.addCell(new Cell().add(exp + ""));
//
//                    }
//                }
//            }
            document.add(table);

            //create table of identifiers was not found
            document.add(new Paragraph("4. Identifiers was not found.").setFontSize(16).setFirstLineIndent(30));
            table = new Table(identifiersWasNotFounds[0].getExp().length + 1);

//            table.setWidthPercent(100);
            table.setMarginLeft(40).setFont(font).setFontSize(10).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
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
                    table.addCell(new Cell().add(exp.toString()));
                }
            }
            document.add(table);
//            document.flush();
//            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
//                PdfCanvas canvas = new PdfCanvas(pdfDocument.getPage(i));
//                canvas.beginText();
//                canvas.newlineShowText("Reactome.org");
//                canvas.endText();
//                canvas.release();;
//            }
            document.close();
            pdfDocument.close();
        } catch (Exception e) {
//            System.exit(1);
            e.printStackTrace();
        }
//        return fileSystemResource;
    }

    public static void addOverviewTable(Document document, String[] header, ResultAssociatedWithToken resultAssociatedWithToken) {

    }

    public static void addFooter(PdfDocument pdfDocument, Document document) {

    }

    public static Map<String, Identifier> identifiersFilter(IdentifiersWasFound[] identifiersWasFounds) {
        Map<String, Identifier> filteredIdentifiers = new HashMap<>();
        for (IdentifiersWasFound identifiersWasFound : identifiersWasFounds) {
            for (Identifier identifier : identifiersWasFound.getEntities()) {
                if (!filteredIdentifiers.containsKey(identifier.getId())) {
                    filteredIdentifiers.put(identifier.getId(), identifier);
                } else {
//                    identifier.getMapsTo().forEach(
//                            mapsTo -> filteredIdentifiers.get(identifier.getId()).getMapsTo().add(mapsTo)
//                    );
                    filteredIdentifiers.get(identifier.getId()).getMapsTo().addAll(identifier.getMapsTo());
                }
            }
        }
        for (Map.Entry<String, Identifier> entry : filteredIdentifiers.entrySet()) {
            for (MapsTo mapsTo : entry.getValue().getMapsTo()) {
                if (!entry.getValue().getResourceMapsToIds().containsKey(mapsTo.getResource())) {
                    entry.getValue().getResourceMapsToIds().put(mapsTo.getResource(), mapsTo.getIds().toString());
                } else {
                    entry.getValue().getResourceMapsToIds().get(mapsTo.getResource()).concat("," + mapsTo.getIds().toString());
                }
            }
        }
        return filteredIdentifiers;
    }
}
