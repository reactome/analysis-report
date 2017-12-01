package org.reactome.server.tools.analysis.exporter.playground.pdfelements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.domains.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domains.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasNotFoundTable extends Table {
    public IdentifiersWasNotFoundTable(DataSet dataSet) {
        super(dataSet.getIdentifiersWasNotFounds()[0].getExp().length + 1);
//            table.setWidthPercent(100);
        this.setMarginLeft(40).setFontSize(FontSize.H6).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        this.addHeaderCell("Identifiers");
        String[] header = dataSet.getResultAssociatedWithToken().getExpression().getColumnNames();
        for (String head : header) {
            this.addHeaderCell(head);
        }
        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            this.addCell(new Cell().add(identifier.getId()));
            for (Double exp : identifier.getExp()) {
                this.addCell(exp.toString());
            }
        }

    }
}
