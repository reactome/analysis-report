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
import java.util.stream.Stream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasFoundTableNoEXP extends Table {
    private static final int NUM_COLUMNS = 6;
    private static final int LEFT_MARGIN = 40;
    private static final String[] HEADERS = {"Identifiers", "mapsTo", "Resource", "Identifiers", "mapsTo", "Resource"};

    public IdentifiersWasFoundTableNoEXP(DataSet dataSet) {
        super(NUM_COLUMNS);
//        super(new float[NUM_COLUMNS]);
//        super(new UnitValue[NUM_COLUMNS]);
        this.setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER);
        Stream.of(HEADERS).forEach(this::addHeaderCell);

        Cell cell;
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
