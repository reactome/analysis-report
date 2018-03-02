package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;
import org.reactome.server.tools.analysis.exporter.profile.Profile;

public class P extends Paragraph {

	private static final HyphenationConfig HYPHENATION_CONFIG = new HyphenationConfig("en", "uk", 2, 2);

	public P() {
		this(new Text(""));
	}

	public P(String text) {
		this(new Text(text));
	}

	public P(Text text) {
		super(text);
		style();
	}

	protected void style() {
		setFontSize(Profile.P);
		setFont(Fonts.REGULAR);
		setMultipliedLeading(1.2f);
		setHyphenation(HYPHENATION_CONFIG);
	}
}
