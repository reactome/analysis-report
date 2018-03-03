package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.property.TextAlignment;
import org.reactome.server.tools.analysis.exporter.style.Profile;
import org.reactome.server.tools.analysis.exporter.style.Fonts;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class Title extends P {
	public Title(String title) {
		super(title);
	}

	@Override
	protected void style() {
		super.style();
		setFontSize(Profile.TITLE);
		setFont(Fonts.LIGHT);
		setTextAlignment(TextAlignment.CENTER);
		setMultipliedLeading(2);
	}
}
