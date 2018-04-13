package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;

public interface TexRenderer {

	void render(TexDocument document, AnalysisData analysisData);
}
