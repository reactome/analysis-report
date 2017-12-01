package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.pdf.exporter.playground.constants.FontSize;
import org.reactome.server.tools.pdf.exporter.playground.domains.DataSet;
import org.reactome.server.tools.pdf.exporter.playground.domains.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasNotFoundTableNoEXP extends Table {
    public IdentifiersWasNotFoundTableNoEXP(DataSet dataSet) {
        super(6);
//            table.setWidthPercent(100);
        this.setMarginLeft(40).setFontSize(FontSize.H6).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);

        this.addHeaderCell(new Cell(1, 6).add("Identifiers"));
        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            this.addCell(new Cell().add(identifier.getId()));
        }
        for (int i = 0; i < 6 - dataSet.getIdentifiersWasNotFounds().length % 6; i++) {
            this.addCell(new Cell());
        }
    }
}
