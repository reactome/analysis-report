package org.reactome.server.tools.analysis.report.renderer;

import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.document.TexDocument;
import org.reactome.server.tools.analysis.report.util.PdfUtils;
import org.reactome.server.tools.analysis.report.util.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TopPathwayRenderer implements TexRenderer {

	@Override
	public void render(TexDocument document, AnalysisData analysisData) {
		document.commandln(TexDocument.SECTION, "Top pathways summary");
		final List<List<String>> values = analysisData.getPathways().stream()
				.map(pathwayData -> {
					final PathwayBase pathwayBase = pathwayData.getBase();
					final PathwayNodeSummary pathway = pathwayData.getSummary();
					return Arrays.asList(TextUtils.scape(pathway.getName()),
							String.format("%,d / %,d", pathwayBase.getEntities().getFound(), pathwayBase.getEntities().getTotal()),
							PdfUtils.formatNumber(pathwayBase.getEntities().getRatio()),
							PdfUtils.formatNumber(pathwayBase.getEntities().getpValue()),
							PdfUtils.formatNumber(pathwayBase.getEntities().getFdr()),
							String.format("%,d / %,d", pathway.getData().getReactionsFound(), pathway.getData().getReactionsCount()),
							PdfUtils.formatNumber(pathway.getData().getReactionsRatio())
					);
				})
				.collect(Collectors.toList());


		document.textln("\\bgroup\n" +
				"\\renewcommand\\arraystretch{1.5}\n" +
				"\\rowcolors{3}{}{lightgray}\n" +
				"\\begin{tabularx}{\\textwidth}{Xcccccc}\n" +
				"\\multicolumn{1}{c!{\\color{white}\\vrule}}{\\cellcolor{reactome}{\\color{white}\\textbf{Pathway}}} &\n" +
				"\\multicolumn{4}{c!{\\color{white}\\vrule}}{\\cellcolor{reactome}{\\color{white}\\textbf{Entities}}} &\n" +
				"\\multicolumn{2}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{Reactions}}} \\\\ \n" +
				"\\multicolumn{1}{c!{\\color{white}\\vrule}}{\\cellcolor{reactome}} &\n" +
				"\\multicolumn{1}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{found}}} &\n" +
				"\\multicolumn{1}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{ratio}}} &\n" +
				"\\multicolumn{1}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{p-value}}} &\n" +
				"\\multicolumn{1}{c!{\\color{white}\\vrule}}{\\cellcolor{reactome}{\\color{white}\\textbf{FDR}}} &\n" +
				"\\multicolumn{1}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{found}}} &\n" +
				"\\multicolumn{1}{c}{\\cellcolor{reactome}{\\color{white}\\textbf{rate}}} \\\\\n" +
				"\\endhead");
		values.forEach(strings -> document.textln(String.join(" & ", strings) + "\\\\"));
		document.textln("\\end{tabularx}\n" +
				"\\egroup");
		document.newPage();
	}
}
