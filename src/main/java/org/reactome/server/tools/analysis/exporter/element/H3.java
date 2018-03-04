package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

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
		setFontSize(PdfProfile.H3);
		setFont(PdfProfile.REGULAR);
		setMultipliedLeading(1.2f);
	}
}
