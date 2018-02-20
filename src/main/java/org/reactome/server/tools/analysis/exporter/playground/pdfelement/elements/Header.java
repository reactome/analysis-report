package org.reactome.server.tools.analysis.exporter.playground.pdfelement.elements;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.playground.constant.FontSize;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Header extends Paragraph {
    public Header(String text, int fontSize) {
        super(text);
        setFontSize(fontSize);
        setMultipliedLeading(2 * fontSize / FontSize.TITLE);
    }
}
