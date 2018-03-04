package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Text;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

public class TOC1 extends P {

	public TOC1(String text, String destination) {
		super(text);
		setAction(PdfAction.createGoTo(destination));
	}

	public TOC1(Text text) {
		super(text);
		setAction(PdfAction.createGoTo(text.getText()));
	}

	@Override
	protected void style() {
		super.style();
		setFont(PdfProfile.BOLD);
		setMarginLeft(10);
		setMultipliedLeading(2);
	}
}
