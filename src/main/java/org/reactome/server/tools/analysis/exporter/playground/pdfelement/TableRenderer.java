package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.analysis.core.model.AnalysisIdentifier;
import org.reactome.server.analysis.core.model.identifier.Identifier;
import org.reactome.server.analysis.core.model.identifier.MainIdentifier;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TableRenderer {

    private static final Color HEADER_BACKGROUND_COLOR = PdfUtils.createColor("#B9B9B9");
    private static final int NUM_COLUMNS = 8;
    private static final float multipliedLeading = 1.0f;
    //    private static final float[] OVERVIEW_VALUES = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
    private static final float[] OVERVIEW_VALUES = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
    //    private static final String[] OVERVIEW_HEADERS = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio", "Species name"};
    private static final String[] OVERVIEW_HEADERS = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio"};
    private static final String[] IDENTIFIERS_FOUND_NO_EXP_HEADERS = {"Identifier", "Resource", "mapsTo", "Identifier", "Resource", "mapsTo"};
    // TODO: 12/02/18 use large table once it been fixed by iText team
    private AnalysisStoredResult asr;
    private SpeciesFilteredResult sfr;

    public TableRenderer(AnalysisStoredResult asr, SpeciesFilteredResult sfr) {
        this.asr = asr;
        this.sfr = sfr;
    }

    public void createTable(AnalysisReport report, TableTypeEnum type) throws TableTypeNotFoundException {
        switch (type) {
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
                throw new TableTypeNotFoundException(String.format("No table type : %s found", type));
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
        for (PathwayBase pathway : sfr.getPathways().subList(report.getReportArgs().getPagination(), report.getReportArgs().getPagination() + report.getProfile().getPathwaysToShow())) {
            PathwayNodeSummary pathwayNodeSummary = asr.getPathway(pathway.getStId());
            table.addCell(new Cell().add(new Paragraph(pathwayNodeSummary.getName())
                    .setFontSize(FontSize.TABLE)
                    .setFontColor(report.getLinkColor())
                    .setAction(PdfAction.createGoTo(pathwayNodeSummary.getStId()))
                    .setKeepTogether(true)
//                    .add(PdfUtils.createGoToLinkIcon(report.getLinkIcon(), FontSize.H8, pathway.getStId()))
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getEntitiesFound()), FontSize.TABLE));
            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getEntitiesCount()), FontSize.TABLE));
            table.addCell(textCell(String.format("%.4f", pathwayNodeSummary.getData().getEntitiesRatio()), FontSize.TABLE));
            table.addCell(textCell(String.format("%g", pathwayNodeSummary.getData().getEntitiesPValue()), FontSize.TABLE));
            table.addCell(textCell(String.format("%g", pathwayNodeSummary.getData().getEntitiesFDR()), FontSize.TABLE));
            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getReactionsFound()), FontSize.TABLE));
            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getReactionsCount()), FontSize.TABLE));
            table.addCell(textCell(String.format("%.4f", pathwayNodeSummary.getData().getReactionsRatio()), FontSize.TABLE));
//            table.addCell(textCell(pathway.getSpecies().getName(), FontSize.TABLE));
        }
        report.add(table);
    }


    private void identifierFoundTable(AnalysisReport report) {
        int column = sfr.getExpressionSummary().getColumnNames().size() + 3;
        Table table = new Table(new UnitValue[column]);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(headerCell("Identifier", FontSize.H5));
        table.addHeaderCell(headerCell("Resource", FontSize.H5));
        table.addHeaderCell(headerCell("mapsTo", FontSize.H5));
        for (String header : sfr.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(headerCell(header, FontSize.H5));
        }
        Cell cell;
        for (Map.Entry<Identifier, Set<MainIdentifier>> entry : PdfUtils.getFilteredIdentifiers(asr, sfr, report.getReportArgs().getResource().getName()).entrySet()) {
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
        Table table = new Table(UnitValue.createPercentArray(new float[]{1 / 8f, 1 / 8f, 2 / 8f, 1 / 8f, 1 / 8f, 2 / 8f,}));
        table.setWidth(UnitValue.createPercentValue(100));
        for (String header : IDENTIFIERS_FOUND_NO_EXP_HEADERS) {
            table.addHeaderCell(headerCell(header, FontSize.H5));
        }
        Map<Identifier, Set<MainIdentifier>> filteredIdentifiers = PdfUtils.getFilteredIdentifiers(asr, sfr, report.getReportArgs().getResource().getName());
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
            cell = new Cell();
            cell.setNextRenderer(new CellTextRenderer(cell, entry.getValue(), FontSize.H6));
            cell.setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
        }

        if (filteredIdentifiers.size() % 2 == 1) {
            table.addCell(new Cell());
            table.addCell(new Cell());
            table.addCell(new Cell());
        }
        report.add(table);
    }

    private void identifierNotFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[asr.getExpressionSummary().getColumnNames().size() + 1]);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(headerCell("Identifiers", FontSize.H5));
        for (String header : asr.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(headerCell(header, FontSize.H5));
        }
        for (AnalysisIdentifier identifier : asr.getNotFound()) {
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
        table.addHeaderCell(new Cell(1, NUM_COLUMNS).add(new Paragraph("Identifiers").setFontSize(FontSize.H5)).setBold().setBackgroundColor(HEADER_BACKGROUND_COLOR).setTextAlignment(TextAlignment.CENTER));
        for (AnalysisIdentifier identifier : asr.getNotFound()) {
            table.addCell(textCell(identifier.getId(), FontSize.H6));
        }

        for (int i = 0; i < NUM_COLUMNS - asr.getNotFound().size() % NUM_COLUMNS; i++) {
            table.addCell(new Cell());
        }
        report.add(table);
    }

    private void identifierFoundInPathwayTable(AnalysisReport report, PathwayNodeSummary summary) {
        Table table = new Table(new UnitValue[NUM_COLUMNS]);
        table.setMarginLeft(20)
                .setFontSize(FontSize.P)
                .setTextAlignment(TextAlignment.LEFT)
                .setWidth(UnitValue.createPercentValue(100));
        PathwayNodeSummary nodeSummary = asr.getPathway(summary.getStId());
        nodeSummary.getData().getFoundEntities(report.getReportArgs().getResource()).forEach(analysisIdentifier -> table.addCell(
                new Cell().add(new Paragraph(analysisIdentifier.getId())
                        .setFontColor(report.getLinkColor())
                        .setMultipliedLeading(multipliedLeading))
                        .setAction(PdfAction.createGoTo(analysisIdentifier.getId()))
                        .setBorder(Border.NO_BORDER)));
        for (int j = 0; j < NUM_COLUMNS - nodeSummary.getData().getFoundEntities().size() % NUM_COLUMNS; j++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        report.add(table);
    }

    private Cell textCell(String text, float fontSize) {
        return new Cell().add(new Paragraph(text)
                .setFontSize(fontSize)
                .setMultipliedLeading(multipliedLeading))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell headerCell(String text, float fontSize) {
        return new Cell().add(new Paragraph(text)
                .setFontSize(fontSize)
                .setMultipliedLeading(multipliedLeading))
                .setBold()
                .setBackgroundColor(HEADER_BACKGROUND_COLOR)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);
    }
}