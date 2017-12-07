package org.reactome.server.tools.analysis.exporter.playground.pdfelements.tables;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.models.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasNotFoundTableNoEXP extends Table {

    private static final int numColmns = 6;
    private static final int leftMargin = 40;

    public IdentifiersWasNotFoundTableNoEXP(DataSet dataSet) {
        super(numColmns);
        this.setMarginLeft(leftMargin).setFontSize(FontSize.H6).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

        this.addHeaderCell(new Cell(1, numColmns).add("Identifiers"));
        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            this.addCell(new Cell().add(identifier.getId()));
        }
        for (int i = 0; i < numColmns - dataSet.getIdentifiersWasNotFounds().length % numColmns; i++) {
            this.addCell(new Cell());
        }
    }
}
