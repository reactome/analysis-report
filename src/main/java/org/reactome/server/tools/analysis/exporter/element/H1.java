package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

public class H1 extends P {

	public H1() {
		style();
	}

	public H1(String text) {
		this(new Text(text));
	}

	public H1(Text text) {
		super(text);
		style();
	}

	@Override
	protected void style() {
		setFont(PdfProfile.BOLD);
		setFontSize(PdfProfile.H1);
		setMultipliedLeading(2f);
	}
}
