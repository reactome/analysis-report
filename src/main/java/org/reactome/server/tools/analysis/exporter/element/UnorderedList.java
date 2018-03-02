package org.reactome.server.tools.analysis.exporter.element;

import com.itextpdf.layout.element.List;
import com.itextpdf.layout.property.ListNumberingType;

public class UnorderedList extends List {

	public UnorderedList() {
		super();
		style();
	}

	public UnorderedList(ListNumberingType listNumberingType) {
		super(listNumberingType);
		style();
	}

	private void style() {
		setMarginLeft(10);
		setSymbolIndent(10);
		setListSymbol("\u2022");
	}
}
