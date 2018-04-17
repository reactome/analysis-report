package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

public class ReportRenderer {

	private static final Collection<TexRenderer> RENDERERS = Arrays.asList(
			new PreambleRenderer(),
			(document, data) -> document.commandln(TexDocument.BEGIN, "document"),
			new TitlePageRenderer(),
			new TableOfContentsRenderer(),
			new IntroductionRenderer(),
			new OverviewRenderer(),
			new PathwayDetailRenderer(),
			new FoundElementsRenderer(),
			new NotFoundElementsRenderer(),
			(document, data) -> document.commandln(TexDocument.END, "document")
			);
	private final TexDocument document;

	public ReportRenderer(OutputStream outputStream) {
		this.document = new TexDocument(outputStream);
	}

	public void render(AnalysisData analysisData) {
		RENDERERS.forEach(texRenderer -> texRenderer.render(document, analysisData));
		document.flush();
	}


}
