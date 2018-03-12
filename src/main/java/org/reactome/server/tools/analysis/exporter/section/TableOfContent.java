package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

public class TableOfContent implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {

		document.add(new AreaBreak());
		document.add(profile.getH1("Table of Contents"));

		document.add(profile.getToc1("1. Introduction", "introduction"));
		document.add(profile.getToc1("2. Summary of parameters", "parameters"));
		document.add(profile.getToc1("3. Genome-wide overview", "overview"));
		document.add(profile.getToc1("4. Most significant pathways", "pathway-list"));
		document.add(profile.getToc1("5. Pathway details", "pathway-details"));
		document.add(profile.getToc1("6. Identifiers found", "identifiers-found"));
		document.add(profile.getToc1("7. Identifiers not found", "not-found"));
	}
}
