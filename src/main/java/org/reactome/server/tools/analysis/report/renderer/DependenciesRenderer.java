package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;

import static org.reactome.server.tools.analysis.report.document.TexDocument.DOCUMENT_CLASS;
import static org.reactome.server.tools.analysis.report.document.TexDocument.USE_PACKAGE;

public class DependenciesRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(DOCUMENT_CLASS, "titlepage", "article");
		document.newLine();
		document.commandln(USE_PACKAGE, "graphicx")
				.commandln(USE_PACKAGE, "color")
				.commandln(USE_PACKAGE, "float")
				.commandln(USE_PACKAGE, "hyperref")
				.commandln(USE_PACKAGE, "a4paper, margin=20mm, left=25mm", "geometry")
				.commandln(USE_PACKAGE, "dvipsnames", "xcolor");
		document.command("newcommand").commandln("myshade", "85")
				.command("colorlet", "mylinkcolor").textln("{violet}")
				.command("colorlet", "mycitecolor").textln("{YellowOrange}")
				.command("colorlet", "myurlcolor").textln("{Aquamarine}");
		document.command("hypersetup", "" +
				"linkcolor  = mylinkcolor!\\myshade!black," +
				"citecolor  = mycitecolor!\\myshade!black," +
				"urlcolor   = myurlcolor!\\myshade!black," +
				"colorlinks = true");

		document.newLine();
		document.command("setlength", "\\parskip").textln("{1em}");
	}
}
