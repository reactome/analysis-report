package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;

/**
 * to contains
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Header extends Paragraph {
	public Header(String text, int fontSize) {
		super(text);
		setFontSize(fontSize);
		// leading is relative to the title font size.
		setMultipliedLeading(2 * fontSize / FontSize.TITLE);
	}
}
