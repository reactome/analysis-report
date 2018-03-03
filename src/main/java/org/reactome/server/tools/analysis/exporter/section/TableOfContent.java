package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.element.H1;
import org.reactome.server.tools.analysis.exporter.element.TOC1;

public class TableOfContent implements Section {

	@Override
	public void render(Document document, AnalysisData analysisData) {

		document.add(new AreaBreak());
		document.add(new H1("Table of Contents"));

		document.add(new TOC1("1. Introduction", "introduction"));
		document.add(new TOC1("2. Summary of parameters", "parameters"));
		document.add(new TOC1("3. Genome-wide overview", "overview"));
		document.add(new TOC1("4. Most significant pathways", "pathway-list"));
		document.add(new TOC1("5. Pathway details", "pathway-details"));
		document.add(new TOC1("6. Identifiers not found", "not-found"));
	}
}
