package org.reactome.server.tools.analysis.exporter.element;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ListParagraph extends P {

	public ListParagraph(String text) {
		super(text);
		setMultipliedLeading(1.0f);
	}
}
