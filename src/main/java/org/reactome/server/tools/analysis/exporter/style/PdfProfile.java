package org.reactome.server.tools.analysis.exporter.style;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.io.IOUtils;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.tools.analysis.exporter.exception.AnalysisExporterRuntimeException;

import java.io.IOException;

/**
 * Profile model contains the report outlook setting. This settings include
 * margins, font family, font sizes and colors.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

	private static final HyphenationConfig HYPHENATION_CONFIG = new HyphenationConfig("en", "uk", 2, 2);
	private static final Color REACTOME_COLOR = new DeviceRgb(47, 158, 194);
	private static final Color LINK_COLOR = REACTOME_COLOR;
	private static final Color LIGHT_GRAY = new DeviceGray(0.9f);
	private float H1;
	private float H2;
	private float H3;
	private float H4;
	private float P;
	private float TITLE;
	private float TABLE;

	private PdfFont ITALIC;
	private PdfFont REGULAR;
	private PdfFont BOLD;
	private Integer maxPathways;
	private PdfFont LIGHT;
	private MarginProfile margin;

	public PdfProfile() {
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

	public Integer getMaxPathways() {
		return maxPathways;
	}

	@Override
	public String toString() {
		return "PdfProfile{" +
				", fontSize=" + P +
				", pathwaysToShow=" + maxPathways +
				", margin=" + margin +
				'}';
	}

	public PdfFont getItalic() {
		return ITALIC;
	}

	public PdfFont getBold() {
		return BOLD;
	}

	public Color getLinkColor() {
		return LINK_COLOR;
	}

	public float getP() {
		return P;
	}

	public Paragraph getParagraph(String text) {
		return new Paragraph(text)
				.setFont(REGULAR)
				.setFontSize(P)
				.setMultipliedLeading(1.2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.JUSTIFIED);
	}

	public Paragraph getH1(String text) {
		return new Paragraph(text)
				.setFont(BOLD)
				.setFontSize(H1)
				.setMultipliedLeading(2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.LEFT);
	}

	public Paragraph getH2(String text) {
		return new Paragraph(text)
				.setFont(BOLD)
				.setFontSize(H2)
				.setMultipliedLeading(1.5f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.LEFT);
	}

	public Paragraph getH3(String text) {
		return new Paragraph(text)
				.setFont(REGULAR)
				.setFontSize(H3)
				.setMultipliedLeading(1.2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.LEFT);
	}

	public Paragraph getH4(String text) {
		return new Paragraph(text)
				.setFont(REGULAR)
				.setFontSize(H4)
				.setMultipliedLeading(1.2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.LEFT);
	}

	public Paragraph getTitle(String text) {
		return new Paragraph(text)
				.setFontSize(TITLE)
				.setFont(LIGHT)
				.setTextAlignment(TextAlignment.CENTER)
				.setHyphenation(HYPHENATION_CONFIG)
				.setMultipliedLeading(2);

	}

	public Cell getHeaderCell(String text) {
		final Cell cell = new Cell()
				.setKeepTogether(true)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(Border.NO_BORDER)
				.setFontColor(DeviceGray.WHITE)
				.setFont(BOLD)
				.setFontSize(TABLE + 1)
				.setBackgroundColor(REACTOME_COLOR);
		if (text != null)
			cell.add(new Paragraph(text)
					.setTextAlignment(TextAlignment.CENTER)
					.setMultipliedLeading(1.0f));
		return cell;
	}

	public Cell getBodyCell(String text, int row) {
		final Cell cell = new Cell()
				.setKeepTogether(true)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(row % 2 == 0 ? null : LIGHT_GRAY);
		if (text != null)
			cell.add(new Paragraph(text)
					.setFont(REGULAR)
					.setFontSize(TABLE)
					.setTextAlignment(TextAlignment.CENTER)
					.setMultipliedLeading(1.0f));
		return cell;
	}

	public Cell getPathwayCell(int i, PathwayNodeSummary pathway) {
		final Cell cell = new Cell()
				.setKeepTogether(true)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(Border.NO_BORDER)
				.setBackgroundColor(i % 2 == 0 ? null : LIGHT_GRAY);
		cell.add(new Paragraph(pathway.getName())
				.setFont(BOLD)
				.setFontSize(TABLE)
				.setFontColor(LINK_COLOR)
				.setTextAlignment(TextAlignment.LEFT)
				.setMultipliedLeading(1.0f))
				.setAction(PdfAction.createGoTo(pathway.getStId()))
				.setPadding(5);
		return cell;

	}

	public List getList(java.util.List<Paragraph> paragraphList) {
		final List list = new List()
				.setMarginLeft(10)
				.setSymbolIndent(10)
				.setListSymbol("\u2022");
		for (Paragraph paragraph : paragraphList) {
			final ListItem item = new ListItem();
			item.add(paragraph.setMultipliedLeading(1.0f));
			list.add(item);
		}
		return list;
	}

	public Paragraph getToc1(String text, String destination) {
		return new Paragraph(text)
				.setFont(BOLD)
				.setFontSize(P + 2)
				.setMarginLeft(10)
				.setMultipliedLeading(2)
				.setAction(PdfAction.createGoTo(destination));
	}

	public Paragraph getToc2(String text, String destination) {
		return new Paragraph(text)
				.setFont(BOLD)
				.setFontSize(P)
				.setMarginLeft(20)
				.setMultipliedLeading(2)
				.setDestination(destination);
	}

	public PdfFont getRegularFont() {
		return REGULAR;
	}

	public PdfFont getBoldFont() {
		return BOLD;
	}
}
