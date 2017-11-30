package org.reactome.server.tools.pdf.exporter.playground.pdfelements;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.pdf.exporter.playground.domains.Identifier;

/**
 * @author Chuan-Deng <dengchuanbio@gmail.com>
 */
public class IdentifiersWasFoundInPathwayTable extends Table {
    public IdentifiersWasFoundInPathwayTable(Identifier[] identifiers) {
        super(6);
        this.setMarginLeft(60).setFontSize(10).setTextAlignment(TextAlignment.LEFT);
        for (Identifier identifier : identifiers) {
            this.addCell(new Cell().add(identifier.getId()).setAction(PdfAction.createGoTo(identifier.getId())).setBorder(Border.NO_BORDER));
        }
        for (int j = 0; j < 6 - identifiers.length % 6; j++) {
            this.addCell(new Cell().setBorder(Border.NO_BORDER));
        }
    }
}
