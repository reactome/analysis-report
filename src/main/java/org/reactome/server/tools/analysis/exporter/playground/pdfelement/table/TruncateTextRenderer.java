package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.constant.Fonts;

/**
 * Truncate text to fit in the cell.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TruncateTextRenderer extends CellRenderer {
    private String text;

    /**
     * Creates a CellRenderer from its corresponding layout object.
     *
     * @param cell the {@link Cell} which this object should manage
     */
    TruncateTextRenderer(Cell cell, String text) {
        super(cell);
        this.text = text;
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        PdfFont font = Fonts.REGULAR;
        String symbol = "...";
        int fontSize = FontSize.TABLE;
        int length = text.length();
        float cellWidth = layoutContext.getArea().getBBox().getWidth();
        Paragraph content = new Paragraph()
                .setFontSize(fontSize)
                .setMultipliedLeading(1.0f);
        // do truncate when text length less than cell width
        if (font.getWidth(text, fontSize) > cellWidth) {
            int left = 0;
            int right = length - 1;
            cellWidth -= font.getWidth(symbol, fontSize);
            while (left < length && right != left) {
                cellWidth -= font.getWidth(text.charAt(left), fontSize);
                if (cellWidth > 0)
                    left++;
                else
                    break;
                cellWidth -= font.getWidth(text.charAt(right), fontSize);
                if (cellWidth > 0)
                    right--;
                else
                    break;
            }
            content.add(text.substring(0, --left).concat(symbol).concat(text.substring(++right)));
            this.childRenderers.add(content.createRendererSubTree().setParent(this));
        } else {
            this.childRenderers.add(content.add(text).createRendererSubTree().setParent(this));
        }
        return super.layout(layoutContext);
    }
}