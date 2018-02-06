package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
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
    private static final float multipliedLeading = 1.0f;
    private DataSet dataSet;

    public TableRender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void createTable(AnalysisReport report, TableTypeEnum type) throws TableTypeNotFoundException, NullLinkIconDestinationException {
        switch (type) {
            case OVERVIEW_TABLE:
                overviewTable(report);
                break;
            case IdentifiersWasFound:
                identifiersWasFoundTable(report);
                break;
            case IDENTIFIERS_WAS_FOUND_NO_EXP:
                identifiersWasFoundTableNoEXP(report);
                break;
            case IDENTIFIERS_WAS_NOT_FOUND:
                identifiersWasNotFoundTable(report);
                break;
            case IDENTIFIERS_WAS_NOT_FOUND_NO_EXP:
                identifiersWasNotFoundTableNoEXP(report);
                break;
            default:
                throw new TableTypeNotFoundException(String.format("No table type : %s was found", type));
        }
    }

    public void createTable(AnalysisReport report, List<Identifier> identifiers) {
        identifiersWasFoundInPathwayTable(report, identifiers);
    }


    private void overviewTable(AnalysisReport report) throws NullLinkIconDestinationException {
        float[] COLUMNS_RELATIVE_WIDTH = new float[]{5 / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2f / 20f, 2f / 20f, 2 / 20f, 2 / 20f, 2 / 20f, 2 / 20f};
        String[] headers = {"Pathway name", "Entity found", "Entity Total", "Entity ratio", "Entity pValue", "Entity FDR", "Reaction found", "Reaction total", "Reaction ratio", "Species name"};

        Table table = new Table(UnitValue.createPercentArray(COLUMNS_RELATIVE_WIDTH), true);
        report.addTable(table);

        for (String header : headers) {
            table.addHeaderCell(textCell(header, FontSize.H6));
        }
        int count = 0;
        for (Pathway pathway : dataSet.getAnalysisResult().getPathways()) {
            table.addCell(new Cell().add(new Paragraph(pathway.getName()).setFontSize(FontSize.H8)
                    .add(PdfUtils.createGoToLinkIcon(dataSet.getLinkIcon(), FontSize.H8, pathway.getName())).
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


    private void identifiersWasFoundTable(AnalysisReport report) {
        int column = dataSet.getIdentifierFounds().get(0).getExpNames().size() + 3;
        float[] widths = new float[column];
        widths[0] = 1.5f / ++column;
        for (int i = 1; i < column - 1; i++) {
            widths[i] = 1f / column;
        }
        Table table = new Table(UnitValue.createPercentArray(widths), true);
        // TODO: 06/02/18 table maybe calliope when it arrival at the end of edge
        report.addTable(table.setMarginLeft(40));

        table.addHeaderCell(textCell("Identifiers", FontSize.H5));
        table.addHeaderCell(textCell("mapsTo", FontSize.H5));
        table.addHeaderCell(textCell("Resource", FontSize.H5));
        for (String header : dataSet.getAnalysisResult().getExpression().getColumnNames()) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }

        Cell cell;
        int count = 0;
        for (Map.Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            if (count++ % 5 == 0) {
                table.flush();
            }
            cell = new Cell().add(new Paragraph(entry.getKey())
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            table.addCell(cell);
            table.addCell(textCell(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", ""), FontSize.H6));
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getResource(), FontSize.H6));
            for (Float exp : entry.getValue().getExp()) {
                table.addCell(textCell(String.valueOf(exp), FontSize.H6));
            }
        }
        table.complete();
    }

    private void identifiersWasFoundTableNoEXP(AnalysisReport report) {
        String[] headers = {"Identifiers", "mapsTo", "Resource", "Identifiers", "mapsTo", "Resource"};
        Table table = new Table(new UnitValue[headers.length], true);
        report.addTable(table.setMarginLeft(40));

        for (String header : headers) {
            table.addHeaderCell(textCell(header, FontSize.H5));
        }
        Cell cell;
        int count = 0;
        for (Map.Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            cell = new Cell().add(new Paragraph(entry.getKey())
                    .setMultipliedLeading(multipliedLeading))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            table.addCell(cell);
            table.addCell(textCell(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", ""), FontSize.H6));
            table.addCell(textCell(entry.getValue().getMapsTo().get(0).getResource(), FontSize.H6));
            if (count++ % 5 == 0) {
                table.flush();
            }
        }
        if (dataSet.getIdentifiersWasFiltered().entrySet().size() % 2 == 1) {
            table.addCell(new Cell());
            table.addCell(new Cell());
            table.addCell(new Cell());
        }
        table.complete();
    }


    private void identifiersWasNotFoundTable(AnalysisReport report) {
        Table table = new Table(new UnitValue[dataSet.getAnalysisResult().getExpression().getColumnNames().size() + 1], true);
        report.addTable(table.setMarginLeft(40));

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


    private void identifiersWasNotFoundTableNoEXP(AnalysisReport report) {
        int NUM_COLUMNS = 8;
        Table table = new Table(new UnitValue[NUM_COLUMNS], true);
        report.addTable(table.setMarginLeft(40));

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


    private void identifiersWasFoundInPathwayTable(AnalysisReport report, List<Identifier> identifiers) {
        int NUM_COLUMNS = 8;
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

    private Cell textCell(String text, float fontSize) {
        return new Cell().setKeepTogether(true)
                .add(new Paragraph(text)
                        .setFontSize(fontSize)
                        .setMultipliedLeading(multipliedLeading))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.CENTER);
    }
}
