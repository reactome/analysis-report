package org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class P extends Paragraph {
    public P(String text) {
        super(text);
        setFontSize(FontSize.P);
    }
}