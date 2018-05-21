package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.PathwayData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.Arrays;

/**
 * Table of top pathways sorted by p-value.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class TopPathwayTable implements Section {

	private static final java.util.List<String> HEADERS = Arrays.asList(
			"Pathway name",
			"Found entities",
			"Entity ratio",
			"Entity p-value",
			"Entity FDR",
			"Found reactions",
			"Reactions rate"
	);

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		final int min = analysisData.getPathways().size();
		document.add(profile.getH1(String.format("Top %d pathways", min)).setDestination("pathway-list"));
		// Let iText decide the width of the columns
		final Table table = new Table(7);
		table.setBorder(Border.NO_BORDER);
		table.useAllAvailableWidth();
		for (String header : HEADERS)
			table.addHeaderCell(profile.getHeaderCell(header));
		int i = 0;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final PathwayBase pathwayBase = pathwayData.getBase();
			final PathwayNodeSummary pathway = analysisData.getAnalysisStoredResult().getPathway(pathwayBase.getStId());
			table.addCell(profile.getPathwayCell(i, pathway));
			final String entities = String.format("%,d / %,d", pathwayBase.getEntities().getFound(), pathwayBase.getEntities().getTotal());
			table.addCell(profile.getBodyCell(entities, i));
			table.addCell(profile.getBodyCell(PdfUtils.formatNumber(pathwayBase.getEntities().getRatio()), i));
			table.addCell(profile.getBodyCell(PdfUtils.formatNumber(pathwayBase.getEntities().getpValue()), i));
			table.addCell(profile.getBodyCell(PdfUtils.formatNumber(pathwayBase.getEntities().getFdr()), i));
			final String reactions = String.format("%,d / %,d",
					pathway.getData().getReactionsFound(),
					pathway.getData().getReactionsCount());
			table.addCell(profile.getBodyCell(reactions, i));
			table.addCell(profile.getBodyCell(PdfUtils.formatNumber(pathway.getData().getReactionsRatio()), i));
			i++;

		}
		document.add(table);
	}


}
