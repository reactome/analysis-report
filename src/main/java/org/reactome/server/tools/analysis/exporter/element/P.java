package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class P extends Paragraph {

	private static final HyphenationConfig HYPHENATION_CONFIG = new HyphenationConfig("en", "uk", 2, 2);

	public P(String text) {
		this(new Text(text));
	}

	public P(Text text) {
		super(text);
		setFontSize(FontSize.P);
		setHyphenation(HYPHENATION_CONFIG);
	}
}
