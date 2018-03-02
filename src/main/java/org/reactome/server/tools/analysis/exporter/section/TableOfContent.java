package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.analysis.core.result.model.SpeciesFilteredResult;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.element.Header;
import org.reactome.server.tools.analysis.exporter.element.P;
import org.reactome.server.tools.analysis.exporter.factory.AnalysisReport;

/**
 * Table of content.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TableOfContent implements Section {
	@Override
	public void render(AnalysisReport report, AnalysisStoredResult asr, SpeciesFilteredResult sfr) {
		report.add(new Header("Content", FontSize.H1).setHorizontalAlignment(HorizontalAlignment.CENTER))
				.add(new P("1: Introduction").setAction(PdfAction.createGoTo("introduction")))
				.add(new P("2: Summary of Parameters and Results").setAction(PdfAction.createGoTo("parametersAnaResults")))
				.add(new P("3: Top 25 pathways").setAction(PdfAction.createGoTo("topPathways")))
				.add(new P("4: Pathway details").setAction(PdfAction.createGoTo("pathwayDetails")))
				.add(new P("5: Summary of identifiers found").setAction(PdfAction.createGoTo("identifiersFound")))
				.add(new P("6: Summary of identifiers not found").setAction(PdfAction.createGoTo("identitiferNotFound")));
	}
}
