package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.Identifier;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasNotFoundTable extends Table {
    private static final int LEFT_MARGIN = 40;

    public IdentifiersWasNotFoundTable(DataSet dataSet) {
        super(new float[dataSet.getIdentifiersWasNotFounds()[0].getExp().length + 1]);
        this.setWidthPercent(100)
                .setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addHeaderCell("Identifiers");
        for (String header : dataSet.getResultAssociatedWithToken().getExpression().getColumnNames()) {
            this.addHeaderCell(header);
        }

        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            this.addCell(new Cell().add(identifier.getId()));
            for (Double exp : identifier.getExp()) {
                this.addCell(exp.toString());
            }
        }
    }
}
