package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Title extends Paragraph {
	public Title(String title) {
		super(title);
		setFontSize(FontSize.TITLE);
		setTextAlignment(TextAlignment.CENTER);
	}
}
