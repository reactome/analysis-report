package org.reactome.server.tools.analysis.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.models.Identifier;

import java.util.Map.Entry;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundTable extends Table {
    private static final int leftMargin = 40;

    public IdentifiersWasFoundTable(DataSet dataSet) {
        super(dataSet.getIdentifiersWasFounds()[0].getExpNames().length + 3);
//        this.setWidthPercent(100);
        this.setMarginLeft(leftMargin)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER);
        this.addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        for (String header : dataSet.getResultAssociatedWithToken().getExpression().getColumnNames()) {
            this.addHeaderCell(header);
        }
        Cell cell = null;
        for (Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            cell = new Cell().add(entry.getKey()).setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            this.addCell(cell);
            this.addCell(new Cell().add(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", "")).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(entry.getValue().getMapsTo().get(0).getResource()).setVerticalAlignment(VerticalAlignment.MIDDLE));
            for (Double exp : entry.getValue().getExp()) {
                this.addCell(new Cell().add(exp + "").setVerticalAlignment(VerticalAlignment.MIDDLE));
            }
        }

    }
}
