package org.reactome.server.tools.analysis.exporter.playground.pdfelement;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import org.reactome.server.analysis.core.model.identifier.MainIdentifier;

import java.util.Set;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class CellTextRenderer extends CellRenderer {
    private float fontSize;
    private Set<MainIdentifier> identifiers;

    CellTextRenderer(Cell cell, Set<MainIdentifier> identifiers, float fontSize) {
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
        for (MainIdentifier identifier : identifiers) {
            content.add(new Text(identifier.getValue().getId().concat(","))
                    .setFontSize(fontSize)
                    .setDestination(identifier.getValue().getId()));
        }
        this.childRenderers.add(content.createRendererSubTree().setParent(this));
        return super.layout(layoutContext);
    }
}