package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.List;
import java.util.stream.Collectors;

public class IntroductionRenderer implements TexRenderer {

	private static final List<String> INTRODUCTION = PdfUtils.getText(IntroductionRenderer.class.getResourceAsStream("introduction.txt"));
	private static final List<Reference> PUBLICATIONS = PdfUtils.getText(IntroductionRenderer.class.getResourceAsStream("references.txt"))
			.stream()
			.map(s -> s.split("\t"))
			.map(line -> new Reference(line[0], line[1]))
			.collect(Collectors.toList());

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.command(TexDocument.SECTION, "Introduction").ln();
		INTRODUCTION.forEach(document::paragraph);
		document.commandln(TexDocument.BEGIN, "itemize");
		for (Reference publication : PUBLICATIONS)
			document.command("item").textln(" " + publication.text);
		document.commandln(TexDocument.END, "itemize");
		document.newPage();
	}


	private static class Reference {
		String text;
		String link;

		Reference(String text, String link) {
			this.text = text;
			this.link = link;
		}
	}
}
