package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;

import java.util.Map.Entry;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundTableNoEXP extends Table {
    private static final int numColumns = 6;
    private static final int leftMargin = 40;
    private static final String[] headers = {"Identifiers", "mapsTo", "Resource", "Identifiers", "mapsTo", "Resource"};

    public IdentifiersWasFoundTableNoEXP(DataSet dataSet) {
        super(numColumns);
//        this.setWidthPercent(100);
        this.setMarginLeft(leftMargin)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER);
        for (String header : headers) {
            this.addHeaderCell(header);
        }
        Cell cell = null;
        for (Entry<String, Identifier> entry : dataSet.getIdentifiersWasFiltered().entrySet()) {
            cell = new Cell().add(entry.getKey()).setVerticalAlignment(VerticalAlignment.MIDDLE);
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
