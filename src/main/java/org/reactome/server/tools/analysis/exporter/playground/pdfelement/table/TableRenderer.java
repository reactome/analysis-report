package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.analysis.core.model.AnalysisIdentifier;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.FoundEntity;
import org.reactome.server.analysis.core.result.model.IdentifierSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.playground.constant.Colors;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisReport;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;
import org.reactome.server.tools.analysis.exporter.playground.util.GraphCoreHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TableRenderer {

    private static final int NUM_COLUMNS = 8;
    private static final float MULTIPLIED_LEADING = 1.0f;
    //    private static final float[] OVERVIEW_VALUES = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
    private static final float[] OVERVIEW_VALUES = new float[]{8 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f};
    private static final String[] OVERVIEW_HEADERS = {"Pathway name", "Entity found", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction ratio"};
    private static final String[] IDENTIFIERS_FOUND_NO_EXP_HEADERS = {"Identifier", "mapsTo", "Identifier", "mapsTo", "Identifier", "mapsTo"};
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

    public void createTable(AnalysisReport report, String stId) {
        identifierFoundInPathwayTable(report, stId);
    }

    private void overviewTable(AnalysisReport report) {
        Table table = new Table(UnitValue.createPercentArray(OVERVIEW_VALUES));
        for (String header : OVERVIEW_HEADERS) {
            table.addHeaderCell(headerCell(header));
        }
        for (PathwayBase pathway : sfr.getPathways().subList(report.getReportArgs().getPagination(), report.getReportArgs().getPagination() + report.getProfile().getPathwaysToShow())) {
            PathwayNodeSummary pathwayNodeSummary = asr.getPathway(pathway.getStId());
            table.addCell(new Cell().add(new Paragraph(pathwayNodeSummary.getName())
                    .setFontSize(FontSize.TABLE)
                    .setFontColor(Colors.REACTOME_COLOR)
                    .setAction(PdfAction.createGoTo(pathwayNodeSummary.getStId()))
                    .setKeepTogether(true)
                    .setMultipliedLeading(MULTIPLIED_LEADING))
                    .setBold()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(textCell(String.format("%s / %s", pathwayNodeSummary.getData().getEntitiesFound(), pathwayNodeSummary.getData().getEntitiesCount())));
//            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getEntitiesCount()), FontSize.TABLE));
            table.addCell(textCell(String.format("%.4f", pathwayNodeSummary.getData().getEntitiesRatio())));
            table.addCell(textCell(String.format("%g", pathwayNodeSummary.getData().getEntitiesPValue())));
            table.addCell(textCell(String.format("%g", pathwayNodeSummary.getData().getEntitiesFDR())));
            table.addCell(textCell(String.format("%s / %s", pathwayNodeSummary.getData().getReactionsFound(), pathwayNodeSummary.getData().getReactionsCount())));
//            table.addCell(textCell(String.valueOf(pathwayNodeSummary.getData().getReactionsCount()), FontSize.TABLE));
            table.addCell(textCell(String.format("%.4f", pathwayNodeSummary.getData().getReactionsRatio())));
        }
        table.setNextRenderer(new BackgroundColorRenderer(table));
        report.add(table);
    }


    private void identifierFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[sfr.getExpressionSummary().getColumnNames().size() + 2]);
        table.addHeaderCell(headerCell("Identifier"));
        table.addHeaderCell(headerCell("mapsTo"));
        for (String header : sfr.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(headerCell(header));
        }
        Set<FoundEntity> foundEntities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
        for (PathwayBase pathwayBase : sfr.getPathways()) {
            List<FoundEntity> identifiers = asr.getFoundEntities(pathwayBase.getStId())
                    .filter(report.getReportArgs().getResource()).getIdentifiers();
            foundEntities.addAll(identifiers);
        }
        Cell cell;
        for (FoundEntity foundEntity : foundEntities) {
            cell = new Cell().add(new Paragraph(foundEntity.getId())
                    .setDestination(foundEntity.getId())
                    .setFontSize(FontSize.TABLE)
                    .setMultipliedLeading(MULTIPLIED_LEADING))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);
            table.addCell(cell);
            List<String> mapsTo = foundEntity.getMapsTo().stream()
                    .flatMap(identifierMap -> identifierMap.getIds().stream())
                    .collect(Collectors.toList());
            cell = new Cell();
            cell.setNextRenderer(new CellTextRenderer(cell, mapsTo));
            cell.setBorder(Border.NO_BORDER).setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
            for (double exp : foundEntity.getExp()) {
                table.addCell(textCell(String.valueOf(exp)));
            }
        }
        table.setNextRenderer(new BackgroundColorRenderer(table));
        report.add(table);
    }

    private void identifierFoundTableNoEXP(AnalysisReport report) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{1 / 10f, 2 / 10f, 1 / 10f, 2 / 10f, 1 / 10f, 2 / 10f,}));
        for (String header : IDENTIFIERS_FOUND_NO_EXP_HEADERS) {
            table.addHeaderCell(headerCell(header));
        }

        Set<FoundEntity> foundEntities = new TreeSet<>(Comparator.comparing(IdentifierSummary::getId));
        for (PathwayBase pathwayBase : sfr.getPathways()) {
            List<FoundEntity> identifiers = asr.getFoundEntities(pathwayBase.getStId()).filter(report.getReportArgs().getResource()).getIdentifiers();
            foundEntities.addAll(identifiers);
        }

        Cell cell;
        for (FoundEntity foundEntity : foundEntities) {
            cell = new Cell().add(new Paragraph(foundEntity.getId())
                    .setDestination(foundEntity.getId())
                    .setFontSize(FontSize.TABLE)
                    .setMultipliedLeading(MULTIPLIED_LEADING))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);
            table.addCell(cell);
            List<String> mapsTo = foundEntity.getMapsTo().stream()
                    .flatMap(identifierMap -> identifierMap.getIds().stream())
                    .collect(Collectors.toList());
            cell = new Cell();
            cell.setNextRenderer(new CellTextRenderer(cell, mapsTo));
            cell.setBorder(Border.NO_BORDER).setKeepTogether(true).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
        }

        if (foundEntities.size() % 3 != 0) {
            for (int i = 0; i < 3 - foundEntities.size() % 3; i++) {
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
                table.addCell(new Cell().setBorder(Border.NO_BORDER));
            }
        }
        table.setNextRenderer(new BackgroundColorRenderer(table));
        report.add(table);
    }

    private void identifierNotFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[asr.getExpressionSummary().getColumnNames().size() + 1]);
        table.addHeaderCell(headerCell("Identifiers"));
        for (String header : asr.getExpressionSummary().getColumnNames()) {
            table.addHeaderCell(headerCell(header));
        }
        for (AnalysisIdentifier identifier : asr.getNotFound()) {

            table.addCell(textCell(identifier.getId()));
            for (double exp : identifier.getExp()) {
                table.addCell(textCell(String.valueOf(exp)));
            }
        }
        table.setNextRenderer(new BackgroundColorRenderer(table));
        report.add(table);
    }


    private void identifierNotFoundTableNoEXP(AnalysisReport report) {
        Table table = new Table(new UnitValue[NUM_COLUMNS]);
        table.addHeaderCell(new Cell(1, NUM_COLUMNS).add(new P("Identifiers").setFontColor(Colors.WHITE)).setBold().setBackgroundColor(Colors.REACTOME_COLOR).setTextAlignment(TextAlignment.CENTER));
        for (AnalysisIdentifier identifier : asr.getNotFound()) {
            table.addCell(textCell(identifier.getId()));
        }

        for (int i = 0; i < NUM_COLUMNS - asr.getNotFound().size() % NUM_COLUMNS; i++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        table.setNextRenderer(new BackgroundColorRenderer(table));
        report.add(table);
    }

    private void identifierFoundInPathwayTable(AnalysisReport report, String stId) {
        Table table = new Table(new UnitValue[NUM_COLUMNS]);
        table.setTextAlignment(TextAlignment.LEFT)
                .setWidth(UnitValue.createPercentValue(100));
        List<FoundEntity> foundEntities = asr.getFoundEntities(stId).filter(report.getReportArgs().getResource()).getIdentifiers();

        Cell cell;
        for (FoundEntity foundEntity : foundEntities) {
            cell = new Cell().setFontColor(Colors.REACTOME_COLOR)
                    .setAction(PdfAction.createGoTo(foundEntity.getId()))
                    .setBorder(Border.NO_BORDER);
            cell.setNextRenderer(new FitTextRenderer(cell, GraphCoreHelper.getFoundEntityName(foundEntity.getId())));
            table.addCell(cell);
        }

        for (int j = 0; j < NUM_COLUMNS - foundEntities.size() % NUM_COLUMNS; j++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        report.add(table);
    }

    private Cell textCell(String text) {
        return new Cell().add(new Paragraph(text)
                .setFontSize(FontSize.TABLE)
                .setMultipliedLeading(MULTIPLIED_LEADING))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER);
    }

    private Cell headerCell(String text) {
        return new Cell().add(new P(text)
                .setFontSize(FontSize.P)
                .setFontColor(Colors.WHITE)
                .setMultipliedLeading(MULTIPLIED_LEADING))
                .setBold()
                .setBackgroundColor(Colors.REACTOME_COLOR)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER)
                .setBorder(Border.NO_BORDER);
    }
}