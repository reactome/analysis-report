package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.style.Profile;

public class BodyCell extends Cell {


	public BodyCell(String text, int row) {
		if (text != null)
		add(new P(text)
				.setFontSize(Profile.P - 2)
				.setTextAlignment(TextAlignment.CENTER)
				.setMultipliedLeading(1.0f));
		setKeepTogether(true)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(row % 2 == 0 ? null : Profile.LIGHT_GRAY);


	}
}
