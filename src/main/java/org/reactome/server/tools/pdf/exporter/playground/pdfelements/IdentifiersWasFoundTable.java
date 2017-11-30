package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.domains.Identifier;

import java.util.Map;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundTable extends Table {
    public IdentifiersWasFoundTable(DataSet dataSet) {
        super(dataSet.getIdentifiersWasFounds()[0].getExpNames().length + 3);
        this.setWidthPercent(100);
        this.setMarginLeft(40)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        this.addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        if (dataSet.getResultAssociatedWithToken().getExpression().getColumnNames().length != 0) {
            String[] header = dataSet.getResultAssociatedWithToken().getExpression().getColumnNames();

            for (String head : header) {
                this.addHeaderCell(head);
            }
        } else {
            // TODO: 30/11/17 add another type of table
        }
        for (Map.Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            Cell cell = new Cell().add(entry.getKey());
            cell.setProperty(Property.DESTINATION, entry.getKey());
            this.addCell(cell);
            this.addCell(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", ""));
            this.addCell(entry.getValue().getMapsTo().get(0).getResource());
            for (Double exp : entry.getValue().getExp()) {
                this.addCell(exp + "");
            }
        }
    }
}
