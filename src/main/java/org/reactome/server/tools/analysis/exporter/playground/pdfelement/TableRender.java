package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.*;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.exception.NullLinkIconDestinationException;
import org.reactome.server.tools.analysis.exporter.playground.exception.TableTypeNotFoundException;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;
import org.reactome.server.tools.analysis.exporter.playground.model.Pathway;
import org.reactome.server.tools.analysis.exporter.playground.util.PdfUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TableRender {
    private DataSet dataSet;
    private static final int NUM_COLUMNS = 8;
    private static final float multipliedLeading = 1.0f;
    private static final float[] OVERVIEW_VALUES = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
    private static final String[] OVERVIEW_HEADERS = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio", "Species name"};
    private static final String[] IDENTIFIERS_FOUND_NO_EXP_HEADERS = {"Identifiers", "mapsTo", "Resource", "Identifiers", "mapsTo", "Resource"};

    public TableRender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void createTable(AnalysisReport report, TableTypeEnum type) throws TableTypeNotFoundException, NullLinkIconDestinationException {
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

    public void createTable(AnalysisReport report, List<Identifier> identifiers) {
        identifierFoundInPathwayTable(report, identifiers);
    }


    private void overviewTable(AnalysisReport report) throws NullLinkIconDestinationException {
        Table table = new Table(UnitValue.createPercentArray(OVERVIEW_VALUES), true);
        report.addTable(table);

        for (String header : OVERVIEW_HEADERS) {
            table.addHeaderCell(textCell(header, FontSize.H6));
        }
        int count = 0;
        for (Pathway pathway : dataSet.getAnalysisResult().getPathways()) {
            table.addCell(new Cell().add(new Paragraph(pathway.getName()).setFontSize(FontSize.H8)
                    .add(PdfUtils.createGoToLinkIcon(dataSet.getLinkIcon(), FontSize.H8, pathway.getStId())).
                            setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(textCell(String.valueOf(pathway.getEntity().getFound()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getEntity().getTotal()), FontSize.H8));
            table.addCell(textCell(String.format("%.4f", pathway.getEntity().getRatio()), FontSize.H8));
            table.addCell(textCell(String.format("%g", pathway.getEntity().getpValue()), FontSize.H8));
            table.addCell(textCell(String.format("%g", pathway.getEntity().getFdr()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getReaction().getFound()), FontSize.H8));
            table.addCell(textCell(String.valueOf(pathway.getReaction().getTotal()), FontSize.H8));
            table.addCell(textCell(String.format("%.4f", pathway.getReaction().getRatio()), FontSize.H8));
            table.addCell(textCell(pathway.getSpecies().getName(), FontSize.H8));

            if (count++ % 5 == 0) {
                table.flush();
            }
        }
        table.complete();
    }


    private void identifierFoundTable(AnalysisReport report) {
        int column = dataSet.getAnalysisResult().getExpression().getColumnNames().size() + 3;
        float[] widths = new float[column];
        for (int i = 0; i < column; i++) {
            if (i == 1) {
                // give more space for 'mapsTo' column since one identifier can map more than one identifiers.
                widths[i] = 1.5f / column;
            } else {
                widths[i] = 1f / column;
            }
        }
        Table table = new Table(UnitValue.createPercentArray(widths), true);
        // TODO: 06/02/18 table maybe collapse when it arrival at the end of page
        report.addTable(table);

        table.addHeaderCell(textCell("Identifiers", FontSize.H5));
        table.addHeaderCell(textCell("mapsTo", FontSize.H5));
        table.addHeaderCell(textCell("Resource", FontSize.H5));
        for (String header : dataSet.getAnalysisResult().getExpression().getColumnNames()) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }

        Cell cell;
        int count = 0;
        for (Map.Entry<String, Identifier> entry : dataSet.getIdentifierFiltered().entrySet()) {
            if (count++ % 5 == 0) {
                table.flush();
            }
            cell = new Cell().add(new Paragraph(entry.getKey())
                    .setFontSize(FontSize.H6)
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            table.addCell(cell);
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getIds().toString().replaceAll("\\[|\\]", ""), FontSize.H6));
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getResource(), FontSize.H6));
            for (Float exp : entry.getValue().getExp()) {
                table.addCell(textCell(String.valueOf(exp), FontSize.H6));
            }
        }
        table.complete();
    }

    private void identifierFoundTableNoEXP(AnalysisReport report) {
        Table table = new Table(new UnitValue[IDENTIFIERS_FOUND_NO_EXP_HEADERS.length], true);
        report.addTable(table);

        for (String header : IDENTIFIERS_FOUND_NO_EXP_HEADERS) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        Cell cell;
        int count = 0;
        for (Map.Entry<String, Identifier> entry : dataSet.getIdentifierFiltered().entrySet()) {
            if (count++ % 5 == 0) {
                table.flush();
            }
            cell = new Cell().add(new Paragraph(entry.getKey())
                    .setFontSize(FontSize.H6)
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            table.addCell(cell);
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getIds().toString().replaceAll("\\[|\\]", ""), FontSize.H6));
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getResource(), FontSize.H6));
        }
        if (dataSet.getIdentifierFiltered().entrySet().size() % 2 == 1) {
            table.addCell(new Cell());
            table.addCell(new Cell());
            table.addCell(new Cell());
        }
        table.complete();
    }


    private void identifierNotFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[dataSet.getAnalysisResult().getExpression().getColumnNames().size() + 1], true);
        report.addTable(table);

        table.addHeaderCell(textCell("Identifiers", FontSize.H5));
        for (String header : dataSet.getAnalysisResult().getExpression().getColumnNames()) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        int count = 0;
        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            table.addCell(textCell(identifier.getId(), FontSize.H6));
            for (Float exp : identifier.getExp()) {
                table.addCell(textCell(String.valueOf(exp), FontSize.H6));
            }
            if (count++ % 5 == 0) {
                table.flush();
            }
        }
        table.complete();
    }


    private void identifierNotFoundTableNoEXP(AnalysisReport report) {
        Table table = new Table(new UnitValue[NUM_COLUMNS], true);
        report.addTable(table);

        table.addHeaderCell(new Cell(1, NUM_COLUMNS).add(new Paragraph("Identifiers").setFontSize(FontSize.H5)).setTextAlignment(TextAlignment.CENTER));
        int count = 0;
        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            table.addCell(textCell(identifier.getId(), FontSize.H6));
            if (count++ % 5 == 0) {
                table.flush();
            }
        }

        for (int i = 0; i < NUM_COLUMNS - dataSet.getIdentifiersWasNotFounds().size() % NUM_COLUMNS; i++) {
            table.addCell(new Cell());
        }
        table.complete();
    }


    private void identifierFoundInPathwayTable(AnalysisReport report, List<Identifier> identifiers) {
        Table table = new Table(new UnitValue[NUM_COLUMNS], true);
        table.setMarginLeft(20)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.LEFT);
        identifiers.forEach(identifier -> table.addCell(
                new Cell().add(new Paragraph(identifier.getId())
                        .setFontColor(PdfUtils.createColor("#2F9EC2", 16))
                        .setMultipliedLeading(multipliedLeading))
                        .setAction(PdfAction.createGoTo(identifier.getId()))
                        .setBorder(Border.NO_BORDER)));
        for (int j = 0; j < NUM_COLUMNS - identifiers.size() % NUM_COLUMNS; j++) {
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
        report.addTable(table);
    }

    private void summaryTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[2]);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.addCell(textCell("Analysis name", FontSize.H6));
        table.addCell(textCell(report.getDataSet().getAnalysisResult().getSummary().getSampleName(), FontSize.H6));
        table.addCell(textCell("token", FontSize.H6));
        table.addCell(textCell(report.getDataSet().getAnalysisResult().getSummary().getToken(), FontSize.H6));
        table.addCell(textCell("type", FontSize.H6));
        table.addCell(textCell(report.getDataSet().getAnalysisResult().getSummary().getType(), FontSize.H6));
        table.addCell(textCell("interactors", FontSize.H6));
        table.addCell(textCell(report.getDataSet().getAnalysisResult().getSummary().getInteractors().toString(), FontSize.H6));
        report.addTable(table);
    }

    private Cell textCell(String text, float fontSize) {
        return new Cell().setKeepTogether(true)
                .add(new Paragraph(text)
                        .setFontSize(fontSize)
                        .setMultipliedLeading(multipliedLeading))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);
    }
}
