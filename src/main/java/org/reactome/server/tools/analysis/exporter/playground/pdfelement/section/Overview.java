package org.reactome.server.tools.analysis.exporter.playground.pdfelement.section;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.jsoup.Jsoup;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.graph.domain.model.*;
import org.reactome.server.tools.analysis.exporter.playground.constant.Colors;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.URL;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.Header;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.ListParagraph;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableRenderer;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.table.TableTypeEnum;
import org.reactome.server.tools.analysis.exporter.playground.util.DiagramHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Overview implements Section {

    private static final Logger LOGGER = LoggerFactory.getLogger(Overview.class);


    public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception {
        TableRenderer tableRenderer = new TableRenderer(asr, sfr);
        addOverviewTable(report, tableRenderer);
        addPathwaysDetail(report, tableRenderer, asr, sfr);
        addIdentifierTable(report, tableRenderer, sfr);
    }

    private void addOverviewTable(AnalysisReport report, TableRenderer tableRendererT) throws TableTypeNotFoundException {
        report.add(new AreaBreak())
                .add(new Header(String.format("3: Top %s over-representation pathways sorted by p-Value.", report.getProfile().getPathwaysToShow()), FontSize.H1).setDestination("topPathways"));
        tableRendererT.createTable(report, TableTypeEnum.OVERVIEW_TABLE);
    }

    private void addIdentifierTable(AnalysisReport report, TableRenderer tableRenderer, SpeciesFilteredResult sfr) throws TableTypeNotFoundException {
        Paragraph identifierFound = new Header("5. Summary of identifiers found.", FontSize.H1);
        identifierFound.setDestination("identifiersFound");
        report.add(identifierFound);
        if (sfr.getExpressionSummary().getColumnNames().size() != 0) {
            tableRenderer.createTable(report, TableTypeEnum.IdentifierFound);
        } else {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_FOUND_NO_EXP);
        }
        report.add(new Header("6. Summary of identifiers not found.", FontSize.H1).setDestination("identitiferNotFound"));
        if (sfr.getExpressionSummary().getColumnNames().size() != 0) {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND);
        } else {
            tableRenderer.createTable(report, TableTypeEnum.IDENTIFIER_NOT_FOUND_NO_EXP);
        }
    }

    private void addPathwaysDetail(AnalysisReport report, TableRenderer tableRenderer, AnalysisStoredResult asr, SpeciesFilteredResult sfr) throws Exception {

        Pathway pathway;
        PathwayNodeSummary pathwayNodeSummary;
        report.add(new Header("4. Pathway details", FontSize.H1).setDestination("pathwayDetails"));
        for (int i = report.getReportArgs().getPagination(); i < report.getReportArgs().getPagination() + report.getProfile().getPathwaysToShow(); i++) {
            report.add(new AreaBreak());
            pathwayNodeSummary = asr.getPathway(sfr.getPathways().get(i).getStId());
            addTitleAndDiagram(report, pathwayNodeSummary, i);

            // add diagram to report.
            addAsPNG(report, asr, sfr, i);
            pathway = GraphCoreHelper.getPathway(sfr.getPathways().get(i).getStId());
            if (pathway.getCompartment() != null) {
                P p = new P(new Text("Compartment: ").setBold());
                StringBuilder names = new StringBuilder();
                pathway.getCompartment().forEach(compartment -> names.append(compartment.getDisplayName()).append(", "));
                report.add(p.add(names.substring(0, names.length() - 2)));
            }
            if (pathway.getIsInDisease()) {
                P p = new P(new Text("Appears in the following disease(s): ").setBold());
                StringBuilder names = new StringBuilder();
                for (Disease disease : pathway.getDisease()) {
                    if (!disease.getDisplayName().equals("disease")) {
                        names.append(disease.getDisplayName()).append(", ");
                    }
                }
                if (names.length() != 0) {
                    report.add(p.add(names.substring(0, names.length() - 2)));
                }
            }
            if (pathway.getIsInferred()) {
                P p = new P(new Text("Inferred: ").setBold());
                StringBuilder names = new StringBuilder();
                pathway.getInferredFrom().forEach(event -> names.append(event.getDisplayName()).append(", "));
                report.add(p.add(names.substring(0, names.length() - 2)));
            }

            if (pathway.getSummation() != null) {
                for (Summation summation : pathway.getSummation()) {
                    String[] paragraph = summation.getText().replaceAll("(?i)<br>+", "\r\n").split("<p>+");
                    Arrays.stream(paragraph).forEach(text -> report.add(new P(Jsoup.parseBodyFragment(text).body().text())));
                }
            }
            report.add(new Header("List of identifiers found in this pathway", FontSize.H3));
            tableRenderer.createTable(report, pathwayNodeSummary.getStId());

            addCuratorDetail(report, pathway);
            addLiteratureReference(report, pathway);
        }
    }

    private void addAsPNG(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr, int i) throws Exception {
        BufferedImage image = DiagramHelper.getPNGDiagram(sfr.getPathways().get(i).getStId(), asr, report.getReportArgs().getResource());
        if (image != null) {
            Image diagram = new Image(ImageDataFactory.create(image, java.awt.Color.WHITE));
            float scale = Math.min(report.getCurrentPageArea().getWidth() / diagram.getImageWidth(), 0.75f);
            diagram.scale(scale, scale).setHorizontalAlignment(HorizontalAlignment.CENTER);
            report.add(diagram);
        } else {
            LOGGER.warn("No diagram found for pathway : {}.", sfr.getPathways().get(i).getStId());
        }
    }

    private void addTitleAndDiagram(AnalysisReport report, PathwayNodeSummary pathway, int index) {
        Header identifierFound = new Header(String.format("4.%s. %s (", index + 1, pathway.getName()), FontSize.H2);
        identifierFound.setDestination(pathway.getStId());
        identifierFound.add(new Text(pathway.getStId()).setFontColor(Colors.REACTOME_COLOR).setAction(PdfAction.createURI(URL.QUERYFORPATHWAYDETAILS.concat(pathway.getStId())))).add(")");

        report.add(identifierFound);
    }

    private void addCuratorDetail(AnalysisReport report, Pathway pathway) {
        if (pathway.getAuthored() != null && pathway.getAuthored().get(0).getAuthor() != null) {
            addCurator(report, "Authors", pathway.getAuthored());
        }
        if (pathway.getCreated() != null) {
            addCurator(report, "Creators", pathway.getCreated());
        }
        if (pathway.getEdited() != null) {
            addCurator(report, "Editors", pathway.getEdited());
        }
        if (pathway.getModified() != null) {
            addCurator(report, "Modified", pathway.getModified());
        }
        if (pathway.getReviewed() != null) {
            addCurator(report, "Reviewed", pathway.getReviewed());
        }
        if (pathway.getRevised() != null) {
            addCurator(report, "Revised", pathway.getRevised());
        }

    }

    private void addLiteratureReference(AnalysisReport report, Pathway pathway) {
        org.reactome.server.graph.domain.model.URL url;
        Book book;
        LiteratureReference literatureReference;
        if (pathway.getLiteratureReference() != null) {
            int length = pathway.getLiteratureReference().size() > 5 ? 5 : pathway.getLiteratureReference().size();
            List<Paragraph> references = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                switch (pathway.getLiteratureReference().get(i).getSchemaClass()) {
                    case "LiteratureReference":
                        literatureReference = (LiteratureReference) pathway.getLiteratureReference().get(i);
                        references.add(new ListParagraph(String.format("%s \"%s\", %s, %s, %s, %s."
                                , PdfUtils.getAuthorDisplayName(literatureReference.getAuthor())
                                , literatureReference.getTitle()
                                , literatureReference.getJournal()
                                , literatureReference.getVolume()
                                , literatureReference.getYear()
                                , literatureReference.getPages()))
                                .add(PdfUtils.getLinkIcon(literatureReference.getUrl())));
                        break;
                    case "Book":
                        book = (Book) pathway.getLiteratureReference().get(i);
                        references.add(new ListParagraph(String.format("%s \"%s\", %s, %s."
                                , PdfUtils.getAuthorDisplayName(book.getAuthor())
                                , book.getTitle()
                                , book.getYear()
                                , book.getPages())));
                        break;
                    case "URL":
                        url = (org.reactome.server.graph.domain.model.URL) pathway.getLiteratureReference().get(i);
                        references.add(new ListParagraph(String.format("%s \"%s\", %s."
                                , PdfUtils.getAuthorDisplayName(url.getAuthor())
                                , url.getTitle()
                                , url.getDisplayName()))
                                .add(PdfUtils.getLinkIcon(url.getUniformResourceLocator())));
                        break;
                }
            }
            if (references.size() != 0) {
                report.add(new Header("References", FontSize.H3));
                report.addAsList(references);
            }
        }
    }

    private void addCurator(AnalysisReport report, String title, List<InstanceEdit> curators) {
        report.add(new Header(title, FontSize.H3));
        List<Paragraph> list = new ArrayList<>();
        curators.forEach(instanceEdit -> instanceEdit.getAuthor().forEach(person -> list.add(createListParagraph(instanceEdit, person))));
        report.addAsList(list);
    }

    private void addCurator(AnalysisReport report, String title, InstanceEdit instanceEdit) {
        report.add(new Header(title, FontSize.H3));
        List<Paragraph> list = new ArrayList<>();
        instanceEdit.getAuthor().forEach(person -> list.add(createListParagraph(instanceEdit, person)));
        report.addAsList(list);
    }

    private ListParagraph createListParagraph(InstanceEdit instanceEdit, Person person) {
        return new ListParagraph(person.getSurname()
                .concat(person.getFirstname() != null ? " ".concat(person.getFirstname()) : "")
                .concat(", ")
                .concat(instanceEdit.getDateTime().substring(0, 10))
                .concat("\r\n"));
    }
}