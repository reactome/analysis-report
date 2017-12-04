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
public class IdentifiersWasFoundTableNoEXP extends Table {
    public static final int numColumns = 6;
    public static final int leftMargin = 40;

    public IdentifiersWasFoundTableNoEXP(DataSet dataSet) {
        super(numColumns);
//        this.setWidthPercent(100);
        this.setMarginLeft(leftMargin)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER);
        this.addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource")
                .addHeaderCell("Identifiers")
                .addHeaderCell("mapsTo")
                .addHeaderCell("Resource");
        for (Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            Cell cell = new Cell().add(entry.getKey()).setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.setProperty(Property.DESTINATION, entry.getKey());
            this.addCell(cell);
            this.addCell(new Cell().add(entry.getValue().getResourceMapsToIds().get(entry.getValue().getMapsTo().get(0).getResource()).replaceAll("[\\[|\\]]", "")).setVerticalAlignment(VerticalAlignment.MIDDLE));
            this.addCell(new Cell().add(entry.getValue().getMapsTo().get(0).getResource()).setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
        if (dataSet.getIdentifiersWasFiltered().entrySet().size() % 2 == 1) {
            this.addCell(new Cell());
            this.addCell(new Cell());
            this.addCell(new Cell());
        }
    }
}
