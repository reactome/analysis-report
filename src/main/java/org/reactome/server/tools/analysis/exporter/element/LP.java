package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;

public class LP extends P {

	public LP() {
		super();
	}

	public LP(String text) {
		super(text);
	}

	public LP(Text text) {
		super(text);
	}

	@Override
	protected void style() {
		super.style();
		setMultipliedLeading(1);
	}
}
