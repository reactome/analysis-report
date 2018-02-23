package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;

import java.util.List;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class CellTextRenderer extends CellRenderer {
    private float fontSize;
    private List<String> identifiers;

    CellTextRenderer(Cell cell, List<String> identifiers, float fontSize) {
        super(cell);
        this.identifiers = identifiers;
        this.fontSize = fontSize;
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
//        HyphenationConfig hyphenation = new HyphenationConfig(3, 3);
//        hyphenation.setHyphenSymbol('\uff0c');
        Paragraph content = new Paragraph()
                .setFontSize(fontSize)
                .setMultipliedLeading(1.0f);
//                .setHyphenation(hyphenation);
        String[] identifier = this.identifiers.toArray(new String[identifiers.size()]);
        for (int i = 0; i < identifier.length; i++) {
            content.add(new Text(identifier[i].concat(i == identifier.length - 1 ? " " : ", "))
                    .setFontSize(fontSize));
        }
        this.childRenderers.add(content.createRendererSubTree().setParent(this));
        return super.layout(layoutContext);
    }
}