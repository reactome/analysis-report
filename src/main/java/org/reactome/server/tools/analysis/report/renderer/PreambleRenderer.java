package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.Command;
import org.reactome.server.tools.analysis.report.document.TexDocument;

import static org.reactome.server.tools.analysis.report.document.TexDocument.DOCUMENT_CLASS;
import static org.reactome.server.tools.analysis.report.document.TexDocument.USE_PACKAGE;

/**
 * Document class, dependencies and custom definitions
 */
public class PreambleRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(new Command(DOCUMENT_CLASS, "article").modifiers("titlepage"))
				.ln()
				.commandln(USE_PACKAGE, "graphicx")
				.commandln(USE_PACKAGE, "color")
				.commandln(USE_PACKAGE, "float")
				.commandln(USE_PACKAGE, "hyperref")
//				.commandln(USE_PACKAGE, "tabularx")
				.commandln(USE_PACKAGE, "ltablex")
				.commandln(USE_PACKAGE, "a4paper, margin=20mm, left=25mm", "geometry")
				.commandln(USE_PACKAGE, "dvipsnames, table, xcdraw", "xcolor");
		document.command("newcommand").commandln("myshade", "85")
				.command("colorlet", "mylinkcolor").textln("{violet}")
				.command("colorlet", "mycitecolor").textln("{YellowOrange}")
				.command("colorlet", "myurlcolor").textln("{Aquamarine}");
		document.command("hypersetup", "" +
				"linkcolor  = mylinkcolor!\\myshade!black," +
				"citecolor  = mycitecolor!\\myshade!black," +
				"urlcolor   = myurlcolor!\\myshade!black," +
				"colorlinks = true");

		document.ln();
		// Bigger space between paragraph
		document.command("setlength", "\\parskip").textln("{1em}");

		// Colors for nothing
		document.commandln(new Command("definecolor", "lightgray", "gray", "0.85"));

		// centered column with linebreak
		document.textln("\\newcolumntype{Z}{>{\\centering\\let\\newline\\\\\\arraybackslash\\hspace{0pt}}X}");

		// Help tables use the whole width
		document.commandln("keepXColumns");

		document.textln("\\renewcommand\\tabularxcolumn[1]{m{#1}}% for vertical centering text in X column");


	}
}
