package org.reactome.server.tools.analysis.exporter.playground.pdfelement.table;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.AnalysisFont;
import org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements.P;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FitTextRenderer extends CellRenderer {
    private String text;

    /**
     * Creates a CellRenderer from its corresponding layout object.
     *
     * @param modelElement the {@link Cell} which this object should manage
     */
    FitTextRenderer(Cell modelElement, String text) {
        super(modelElement);
        this.text = text;
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        PdfFont font = AnalysisFont.REGULAR;
        int fontSize = FontSize.P;
        int length = text.length();
        float cellWidth = layoutContext.getArea().getBBox().getWidth();
        if (font.getWidth(text, fontSize) > cellWidth) {
            int left = 0;
            int right = length - 1;
            cellWidth -= font.getWidth("...", fontSize);
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
            P p = new P(text.substring(0, --left).concat("...").concat(text.substring(++right)));
            this.childRenderers.add(p.createRendererSubTree().setParent(this));
        } else {
            this.childRenderers.add(new P(text).createRendererSubTree().setParent(this));
        }
        return super.layout(layoutContext);
    }
}