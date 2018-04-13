package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;

public class TableOfContentsRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(TexDocument.TABLE_OF_CONTENTS);
		document.commandln(TexDocument.NEW_PAGE);
	}
}
