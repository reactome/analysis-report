package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;

public class TOC2 extends P {

	public TOC2() {
		super();
	}

	public TOC2(String text) {
		super(text);
	}

	public TOC2(Text text) {
		super(text);
	}

	@Override
	protected void style() {
		super.style();
		setMarginLeft(20);
	}
}
