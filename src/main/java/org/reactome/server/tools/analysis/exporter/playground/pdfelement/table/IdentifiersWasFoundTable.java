package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.Identifier;

import java.util.Map.Entry;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasFoundTable extends Table {
    private static final int LEFT_MARGIN = 40;

    public IdentifiersWasFoundTable(DataSet dataSet) {
        super(new float[dataSet.getIdentifiersWasFounds()[0].getExpNames().length + 3]);
//        this.setWidthPercent(100);

        this.setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER)
                .addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        for (String header : dataSet.getResultAssociatedWithToken().getExpression().getColumnNames()) {
            this.addHeaderCell(header);
        }

        Cell cell;
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
