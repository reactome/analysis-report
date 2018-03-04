package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

public class HeaderCell extends Cell {

	public HeaderCell(String text) {
		if (text != null) {
			add(new P(text)
					.setFont(PdfProfile.BOLD)
					.setFontColor(DeviceRgb.WHITE)
					.setFontSize(PdfProfile.P - 1)
					.setTextAlignment(TextAlignment.CENTER)
					.setMultipliedLeading(1.0f));
			setKeepTogether(true)
					.setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setBackgroundColor(PdfProfile.REACTOME_COLOR)
					.setBorder(Border.NO_BORDER);
		}
	}
}
