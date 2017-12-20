package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.model.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundInPathwayTable extends Table {

    private static final int numColumns = 6;
    public static final int leftMargin = 60;


    public IdentifiersWasFoundInPathwayTable(Identifier[] identifiers) {
        super(new float[numColumns]);
        this.setWidthPercent(100)
                .setMarginLeft(leftMargin)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.LEFT);
        for (Identifier identifier : identifiers) {
            this.addCell(new Cell().add(identifier.getId()).setAction(PdfAction.createGoTo(identifier.getId())).setBorder(Border.NO_BORDER));
        }
        for (int j = 0; j < numColumns - identifiers.length % numColumns; j++) {
            this.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
    }
}
