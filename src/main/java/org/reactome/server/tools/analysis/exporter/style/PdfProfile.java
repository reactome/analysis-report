package org.reactome.server.tools.analysis.exporter.style;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.apache.commons.io.IOUtils;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterRuntimeException;

import java.io.IOException;

/**
 * Profile model contains the report outlook setting. This settings include
 * margins, font family, font sizes and colors.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

	public static final Color REACTOME_COLOR = new DeviceRgb(47, 158, 194);
	public static final Color LINK_COLOR = REACTOME_COLOR;
	public static final Color LIGHT_GRAY = new DeviceGray(0.9f);
	public static float TITLE;
	public static float H1;
	public static float H2;
	public static float H3;
	public static float H4;
	public static float P;
	public static float TABLE;
	public static PdfFont ITALIC;
	public static PdfFont REGULAR;
	public static PdfFont BOLD;
	public static PdfFont LIGHT;
	public static Integer MAX_PATHWAYS;
	private MarginProfile margin;

	public static void reload() {
		// Every PDF must load the fonts again, as they are hold by one, and only one document
		try {
			byte[] bytes;
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("OpenSans-Regular.ttf"));
			REGULAR = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("OpenSans-Bold.ttf"));
			BOLD = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("OpenSans-Light.ttf"));
			LIGHT = PdfFontFactory.createFont(bytes, true);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("OpenSans-Italic.ttf"));
			ITALIC = PdfFontFactory.createFont(bytes, true);
		} catch (IOException e) {
			throw new AnalysisExporterRuntimeException("Internal error. Couldn't read fonts", e);
		}
	}

	public void setFontSize(Integer fontSize) {
		P = fontSize;
		TABLE = P - 2;
		H4 = P + 2;
		H3 = P + 4;
		H2 = P + 6;
		H1 = P + 10;
		TITLE = P + 14;
	}

	public MarginProfile getMargin() {
		return margin;
	}

	public void setMaxPathways(Integer maxPathways) {
		MAX_PATHWAYS = maxPathways;
	}

	@Override
	public String toString() {
		return "PdfProfile{" +
				", fontSize=" + P +
				", pathwaysToShow=" + MAX_PATHWAYS +
				", margin=" + margin +
				'}';
	}
}
