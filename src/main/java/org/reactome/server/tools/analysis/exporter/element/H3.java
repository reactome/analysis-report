package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;
import org.reactome.server.tools.analysis.exporter.style.Fonts;
import org.reactome.server.tools.analysis.exporter.style.Profile;

public class H3 extends P {
	public H3(String text) {
		this(new Text(text));
	}

	public H3(Text text) {
		super(text);
		style();
	}

	public H3() {
		style();
	}

	protected void style() {
		setFontSize(Profile.H3);
		setFont(Fonts.REGULAR);
		setMultipliedLeading(1.2f);
	}
}
