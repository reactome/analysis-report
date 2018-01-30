package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.model.DataSet;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasNotFoundTable extends Table {
    private static final int LEFT_MARGIN = 40;

    public IdentifiersWasNotFoundTable(DataSet dataSet) {
        super(new float[dataSet.getIdentifiersWasNotFounds().get(0).getExp().size() + 1]);
        this.setWidthPercent(100)
                .setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .addHeaderCell("Identifiers");
        for (String header : dataSet.getAnalysisResult().getExpression().getColumnNames()) {
            this.addHeaderCell(header);
        }

        for (Identifier identifier : dataSet.getIdentifiersWasNotFounds()) {
            this.addCell(new Cell().add(identifier.getId()));
            for (Float exp : identifier.getExp()) {
                this.addCell(exp.toString());
            }
        }
    }
}
