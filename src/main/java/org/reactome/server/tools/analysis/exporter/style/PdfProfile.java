package org.reactome.server.tools.analysis.exporter.style;

/**
 * Profile model contains the report outlook setting.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PdfProfile {

	private Integer fontSize = 10;// font size of paragraph in report,another font size like 'Title','Table','H1' are relative to this value.
	private Integer pathwaysToShow = 25;
	private MarginProfile margin;

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getPathwaysToShow() {
		return pathwaysToShow;
	}

	public void setPathwaysToShow(Integer pathwaysToShow) {
		this.pathwaysToShow = pathwaysToShow;
	}

	public MarginProfile getMargin() {
		return margin;
	}

	public void setMargin(MarginProfile margin) {
		this.margin = margin;
	}

	@Override
	public String toString() {
		return "PdfProfile{" +
				", fontSize=" + fontSize +
				", pathwaysToShow=" + pathwaysToShow +
				", margin=" + margin +
				'}';
	}
}
