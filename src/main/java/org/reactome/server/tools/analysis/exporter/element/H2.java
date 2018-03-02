package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.Text;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;
import org.reactome.server.tools.analysis.exporter.profile.Profile;


public class H2 extends P {

	public H2() {
		style();
	}

	public H2(String text) {
		this(new Text(text));
	}

	public H2(Text text) {
		super(text);
		style();
	}


	@Override
	protected void style() {
		setFontSize(Profile.H2);
		setFont(Fonts.BOLD);
		setMultipliedLeading(1.5f);
	}
}
