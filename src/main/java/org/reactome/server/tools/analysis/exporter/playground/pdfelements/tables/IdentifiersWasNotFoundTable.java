package org.reactome.server.tools.analysis.exporter.playground.pdfelements.tables;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constants.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.models.DataSet;

import java.util.stream.Stream;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasNotFoundTable extends Table {
    private static final int leftMargin = 40;

    public IdentifiersWasNotFoundTable(DataSet dataSet) {
        super(dataSet.getIdentifiersWasNotFounds()[0].getExp().length + 1);
        this.setMarginLeft(leftMargin).setFontSize(FontSize.H6).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE);
        this.addHeaderCell("Identifiers");
        String[] header = dataSet.getResultAssociatedWithToken().getExpression().getColumnNames();
        for (String head : header) {
            this.addHeaderCell(head);
        }
//        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
//            this.addCell(new Cell().add(identifier.getId()));
//            for (Double exp : identifier.getExp()) {
//                this.addCell(exp.toString());
//            }
//        }

        Stream.of(dataSet.getIdentifiersWasNotFounds()).forEach(
                identifier -> {
                    this.addCell(new Cell().add(identifier.getId()));
                    Stream.of(identifier.getExp()).forEach(exp -> this.addCell(exp.toString()));
                });
    }
}
