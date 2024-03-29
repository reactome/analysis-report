package org.reactome.server.tools.analysis.report.style;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.apache.commons.io.IOUtils;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.tools.analysis.report.exception.AnalysisExporterRuntimeException;

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
	private float fontSize;
	private float TITLE;
	private float TABLE;

	private PdfFont ITALIC;
	private PdfFont REGULAR;
	private PdfFont BOLD;
	private PdfFont LIGHT;
	private MarginProfile margin;

	private int toc = 1;

	public PdfProfile() {
		// Every PDF must load the fonts again, as they are hold by one, and only one document
		try {
			byte[] bytes;
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("SourceSerifPro-Regular.ttf"));
			REGULAR = PdfFontFactory.createFont(bytes, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("SourceSerifPro-Bold.ttf"));
			BOLD = PdfFontFactory.createFont(bytes, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("SourceSerifPro-Semibold.ttf"));
			LIGHT = PdfFontFactory.createFont(bytes, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
			bytes = IOUtils.toByteArray(PdfProfile.class.getResourceAsStream("OpenSans-Italic.ttf"));
			ITALIC = PdfFontFactory.createFont(bytes, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
		} catch (IOException e) {
			throw new AnalysisExporterRuntimeException("Internal error. Couldn't read fonts", e);
		}
	}

	public MarginProfile getMargin() {
		return margin;
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

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
		TABLE = this.fontSize - 2;
		H3 = this.fontSize + 2;
		H2 = this.fontSize + 4;
		H1 = this.fontSize + 6;
		TITLE = this.fontSize + 14;
	}

	public Paragraph getParagraph(String text) {
		return new Paragraph(text)
				.setFont(REGULAR)
				.setKeepTogether(true)
				.setFontSize(fontSize)
				.setMultipliedLeading(1.2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.JUSTIFIED);
	}

	public Paragraph getH1(String text) {
		return getH1(text, true);
	}

	public Paragraph getH1(String text, boolean indexed) {
		return new Paragraph(indexed ? (toc++ + ". " + text) : text)
				.setFont(BOLD)
				.setFontSize(H1)
				.setMultipliedLeading(2f)
				.setHyphenation(HYPHENATION_CONFIG)
				.setTextAlignment(TextAlignment.LEFT);
	}

	public Paragraph getH3(String text) {
		return new Paragraph(text)
				.setFont(BOLD)
				.setFontSize(H3)
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

	public PdfFont getRegularFont() {
		return REGULAR;
	}

	public PdfFont getBoldFont() {
		return BOLD;
	}

	public Cell getHeaderCell(String text) {
		return getHeaderCell(text, 1, 1);
	}

	public Cell getHeaderCell(String text, int rowspan, int colspan) {
		final Cell cell = new Cell(rowspan, colspan)
				.setKeepTogether(true)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(new SolidBorder(DeviceGray.WHITE, 1))
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
}
