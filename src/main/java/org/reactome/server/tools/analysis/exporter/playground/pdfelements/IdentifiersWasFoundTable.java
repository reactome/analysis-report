package org.reactome.server.tools.analysis.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domains.Identifier;

import java.util.Map.Entry;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundTable extends Table {
    public IdentifiersWasFoundTable(DataSet dataSet) {
        super(dataSet.getIdentifiersWasFounds()[0].getExpNames().length + 3);
//        this.setWidthPercent(100);
        this.setMarginLeft(40)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER);
        this.addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        for (String header : dataSet.getResultAssociatedWithToken().getExpression().getColumnNames()) {
            this.addHeaderCell(header);
        }
        for (Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            Cell cell = new Cell().add(entry.getKey()).setVerticalAlignment(VerticalAlignment.MIDDLE);
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
