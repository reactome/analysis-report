package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;

import java.util.stream.Stream;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasNotFoundTableNoEXP extends Table {

    private static final int NUM_COLUMNS = 6;
    private static final int LEFT_MARGIN = 40;

    public IdentifiersWasNotFoundTableNoEXP(DataSet dataSet) {
        super(new float[NUM_COLUMNS]);
        this.setWidthPercent(100)
                .setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addHeaderCell(new Cell(1, NUM_COLUMNS).add("Identifiers"));
        Stream.of(dataSet.getIdentifiersWasNotFounds()).forEach(identifier -> this.addCell(new Cell().add(identifier.getId())));

        for (int i = 0; i < NUM_COLUMNS - dataSet.getIdentifiersWasNotFounds().length % NUM_COLUMNS; i++) {
            this.addCell(new Cell());
        }
    }
}
