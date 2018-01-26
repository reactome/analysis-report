package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.domain.model.Identifier;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class IdentifiersWasFoundInPathwayTable extends Table {

    public static final int LEFT_MARGIN = 60;
    private static final int NUM_COLUMNS = 6;

    public IdentifiersWasFoundInPathwayTable(Identifier[] identifiers) {
        super(new float[NUM_COLUMNS]);
        this.setWidthPercent(100)
                .setMarginLeft(LEFT_MARGIN)
                .setFontSize(FontSize.H6)
                .setTextAlignment(TextAlignment.LEFT);
        for (Identifier identifier : identifiers) {
            this.addCell(new Cell().add(identifier.getId()).setAction(PdfAction.createGoTo(identifier.getId())).setBorder(Border.NO_BORDER));
        }
        for (int j = 0; j < NUM_COLUMNS - identifiers.length % NUM_COLUMNS; j++) {
            this.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
    }
}
