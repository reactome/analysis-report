package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

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
		setFont(PdfProfile.REGULAR);
		setFontSize(PdfProfile.P);
		setMultipliedLeading(1.2f);
		setHyphenation(HYPHENATION_CONFIG);
		setTextAlignment(TextAlignment.JUSTIFIED);
	}
}
