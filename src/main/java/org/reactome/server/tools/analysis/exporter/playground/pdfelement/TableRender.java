package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.*;
import org.reactome.server.analysis.core.model.AnalysisIdentifier;
import org.reactome.server.analysis.core.model.identifier.Identifier;
import org.reactome.server.analysis.core.model.identifier.MainIdentifier;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TableRender {

    private static final int NUM_COLUMNS = 8;
    private static final float multipliedLeading = 1.0f;
    private static final float[] OVERVIEW_VALUES = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
    private static final String[] OVERVIEW_HEADERS = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio", "Species name"};
    private static final String[] IDENTIFIERS_FOUND_NO_EXP_HEADERS = {"Identifier", "Resource", "mapsTo", "Identifier", "Resource", "mapsTo"};
    // TODO: 12/02/18 use large table once it been fixed by iText team
    private AnalysisStoredResult result;

    public TableRender(AnalysisStoredResult result) {
        this.result = result;
    }

    public void createTable(AnalysisReport report, TableTypeEnum type) throws TableTypeNotFoundException {
        switch (type) {
            case SUMMARY:
                summaryTable(report);
                break;
            case OVERVIEW_TABLE:
                overviewTable(report);
                break;
            case IdentifierFound:
                identifierFoundTable(report);
                break;
            case IDENTIFIER_FOUND_NO_EXP:
                identifierFoundTableNoEXP(report);
                break;
            case IDENTIFIER_NOT_FOUND:
                identifierNotFoundTable(report);
                break;
            case IDENTIFIER_NOT_FOUND_NO_EXP:
                identifierNotFoundTableNoEXP(report);
                break;
            default:
                throw new TableTypeNotFoundException(String.format("No table type : %s was found", type));
        }
    }

    public void createTable(AnalysisReport report, PathwayNodeSummary pathway) {
        identifierFoundInPathwayTable(report, pathway);
    }

    private void overviewTable(AnalysisReport report) {
        Table table = new Table(UnitValue.createPercentArray(OVERVIEW_VALUES));
        for (String header : OVERVIEW_HEADERS) {
            table.addHeaderCell(textCell(header, FontSize.H6));
        }
        for (PathwayNodeSummary pathway : result.getPathways().subList(0, report.getProfile().getPathwaysToShow())) {
            table.addCell(new Cell().add(new Paragraph(pathway.getName())
                    .setFontSize(FontSize.H8)
                    .setFontColor(report.getReactomeColor())
                    .setAction(PdfAction.createGoTo(pathway.getStId()))
                    .setKeepTogether(true)
//                    .add(PdfUtils.createGoToLinkIcon(report.getLinkIcon(), FontSize.H8, pathway.getStId()))
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(textCell(String.valueOf(pathway.getData().getEntitiesFound()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getData().getEntitiesCount()), FontSize.H8));
            table.addCell(textCell(String.format("%.4f", pathway.getData().getEntitiesRatio()), FontSize.H8));
            table.addCell(textCell(String.format("%g", pathway.getData().getEntitiesPValue()), FontSize.H8));
            table.addCell(textCell(String.format("%g", pathway.getData().getEntitiesFDR()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getData().getReactionsFound()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getData().getReactionsCount()), FontSize.H8));
            table.addCell(textCell(String.format("%.4f", pathway.getData().getReactionsRatio()), FontSize.H8));
            table.addCell(textCell(pathway.getSpecies().getName(), FontSize.H8));
        }
        report.add(table);
    }


    private void identifierFoundTable(AnalysisReport report) {
        int column = result.getExpressionSummary().getColumnNames().size() + 3;
//        float[] widths = new float[column];
//        for (int i = 0; i < column; i++) {
//            if (i == 2) {
//                // give more space for 'mapsTo' column since one identifier can map more than one identifiers.
//                widths[i] = 1.5f / column;
//            } else {
//                widths[i] = 1f / column;
//            }
//        }
//        Table table = new Table(UnitValue.createPercentArray(widths));
        Table table = new Table(new UnitValue[column]);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(textCell("Identifier", FontSize.H5));
        table.addHeaderCell(textCell("Resource", FontSize.H5));
        table.addHeaderCell(textCell("mapsTo", FontSize.H5));
        for (String header : result.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        Cell cell;
        for (Map.Entry<Identifier, Set<MainIdentifier>> entry : PdfUtils.getFilteredIdentifiers(result.getPathways()).entrySet()) {
            cell = new Cell().add(new Paragraph(entry.getKey().getValue().getId())
                    .setFontSize(FontSize.H6)
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT);
            table.addCell(cell);
            table.addCell(textCell(entry.getKey().getResource().getName().replaceAll("#", ""), FontSize.H6));
            StringBuilder mapsTo = new StringBuilder();
            entry.getValue().forEach(mainIdentifier -> mapsTo.append(mainIdentifier.getValue().getId()).append(','));
            if (mapsTo.length() < 25) {
                cell = new Cell();
                cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
                for (String identifier : mapsTo.toString().split(",")) {
                    cell.add(new Paragraph(identifier.concat(" "))).setFontSize(FontSize.H6).setDestination(identifier);
                }
                table.addCell(cell);
            } else {
                cell = new Cell();
//                cell.setNextRenderer(new CellTextRenderer(cell, mapsTo.deleteCharAt(mapsTo.lastIndexOf(",")).toString(), FontSize.H6));
                cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
                table.addCell(cell);
            }
            for (double exp : entry.getKey().getValue().getExp()) {
                table.addCell(textCell(String.valueOf(exp), FontSize.H6));
            }
        }
        report.add(table);
    }

    private void identifierFoundTableNoEXP(AnalysisReport report) {
//        Table table = new Table(new UnitValue[IDENTIFIERS_FOUND_NO_EXP_HEADERS.length]);
        Table table = new Table(UnitValue.createPercentArray(new float[]{1 / 8f, 1 / 8f, 2 / 8f, 1 / 8f, 1 / 8f, 2 / 8f,}));
        table.setWidth(UnitValue.createPercentValue(100));
        for (String header : IDENTIFIERS_FOUND_NO_EXP_HEADERS) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        Map<Identifier, Set<MainIdentifier>> filteredIdentifiers = PdfUtils.getFilteredIdentifiers(result.getPathways());
        Cell cell;
        for (Map.Entry<Identifier, Set<MainIdentifier>> entry : filteredIdentifiers.entrySet()) {
            cell = new Cell().add(new Paragraph(entry.getKey().getValue().getId())
                    .setFontSize(FontSize.H6)
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER);
            cell.setProperty(Property.DESTINATION, entry.getKey().getValue().getId());
            table.addCell(cell);
            table.addCell(textCell(entry.getKey().getResource().getName().replaceAll("#", ""), FontSize.H6));
//            StringBuilder mapsTo = new StringBuilder();
            cell = new Cell();
            cell.setNextRenderer(new CellTextRenderer(cell, entry.getValue(), FontSize.H6));
            cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
//            for (MainIdentifier mainIdentifier : entry.getValue()) {
//                cell.add(new Paragraph(mainIdentifier.getValue().getId()).add(","))
//                        .setFontSize(FontSize.H6)
//                        .setDestination(mainIdentifier.getResource().getName().concat(mainIdentifier.getValue().getId()));
//            }
            HyphenationConfig hyphenation = new HyphenationConfig(3, 3);
            hyphenation.setHyphenSymbol('\u0000');
            cell.setHyphenation(hyphenation);
            table.addCell(cell);
        }
//            if (mapsTo.length() < 25) {
//                cell = new Cell();
//                cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
//                for (String identifier : mapsTo.toString().split(",")) {
//                    cell.add(new Paragraph(identifier).add(",")).setFontSize(FontSize.H6).setDestination();
//                }
//                table.addCell(cell);
//            } else {
//                cell = new Cell();
//                cell.setNextRenderer(new CellTextRenderer(cell, mapsTo.deleteCharAt(mapsTo.lastIndexOf(",")).toString(), FontSize.H6));
//                cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
//                table.addCell(cell);
//            }
//    }

        if (filteredIdentifiers.size() % 2 == 1) {
            table.addCell(new Cell());
            table.addCell(new Cell());
            table.addCell(new Cell());
        }
        report.add(table);
    }

    private void identifierNotFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[result.getExpressionSummary().getColumnNames().size() + 1]);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(textCell("Identifiers", FontSize.H5));
        for (String header : result.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        for (AnalysisIdentifier identifier : result.getNotFound()) {
            table.addCell(textCell(identifier.getId(), FontSize.H6));
            for (double exp : identifier.getExp()) {
                table.addCell(textCell(String.valueOf(exp), FontSize.H6));
            }
        }
        report.add(table);
    }


    private void identifierNotFoundTableNoEXP(AnalysisReport report) {
        Table table = new Table(new UnitValue[NUM_COLUMNS]);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(new Cell(1, NUM_COLUMNS).add(new Paragraph("Identifiers").setFontSize(FontSize.H5)).setTextAlignment(TextAlignment.CENTER));
        for (AnalysisIdentifier identifier : result.getNotFound()) {
            table.addCell(textCell(identifier.getId(), FontSize.H6));
        }

        for (int i = 0; i < NUM_COLUMNS - result.getNotFound().size() % NUM_COLUMNS; i++) {
            table.addCell(new Cell());
        }
        report.add(table);
    }


    private void identifierFoundInPathwayTable(AnalysisReport report, PathwayNodeSummary pathway) {
        Table table = new Table(new UnitValue[NUM_COLUMNS]);
        table.setMarginLeft(20)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.LEFT)
                .setWidth(UnitValue.createPercentValue(100));
        pathway.getData().getFoundEntities().forEach(analysisIdentifier -> table.addCell(
                new Cell().add(new Paragraph(analysisIdentifier.getId())
                        .setFontColor(report.getReactomeColor())
                        .setMultipliedLeading(multipliedLeading))
                        .setAction(PdfAction.createGoTo(analysisIdentifier.getId()))
                        .setBorder(Border.NO_BORDER)
        ));
        for (int j = 0; j < NUM_COLUMNS - pathway.getData().getFoundEntities().size() % NUM_COLUMNS; j++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        report.add(table);
    }

    private void summaryTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[2]);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        if (result.getSummary().getSampleName() != null) {
            table.addCell(textCell("Analysis name", FontSize.H6));
            table.addCell(textCell(result.getSummary().getSampleName(), FontSize.H6));
        }
        table.addCell(textCell("token", FontSize.H6));
        table.addCell(textCell(result.getSummary().getToken(), FontSize.H6));
        table.addCell(textCell("type", FontSize.H6));
        table.addCell(textCell(result.getSummary().getType(), FontSize.H6));
        table.addCell(textCell("interactors", FontSize.H6));
        table.addCell(textCell(result.getSummary().isInteractors().toString(), FontSize.H6));
        report.add(table);
    }

    private Cell textCell(String text, float fontSize) {
        return new Cell().add(new Paragraph(text)
                .setFontSize(fontSize)
                .setMultipliedLeading(multipliedLeading))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);
    }
}