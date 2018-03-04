package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.PathwayData;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

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
		document.add(new AreaBreak());
		document.add(profile.getH2(String.format("4. Top %d pathways", min)).setDestination("pathway-list"));
		final float[] COLUMNS_RELATIVE_WIDTH = new float[]{0.4f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
		final Table table = new Table(UnitValue.createPercentArray(COLUMNS_RELATIVE_WIDTH));
		table.setBorder(Border.NO_BORDER);
		table.useAllAvailableWidth();
		for (String header : HEADERS)
			table.addHeaderCell(profile.getHeaderCell(header));
		int i = 0;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final PathwayBase pathwayBase = pathwayData.getBase();
			final PathwayNodeSummary pathway = analysisData.getAnalysisStoredResult().getPathway(pathwayBase.getStId());
			table.addCell(profile.getPathwayCell(i, pathway));
			final String entities = String.format("%d / %d", pathwayBase.getEntities().getFound(), pathwayBase.getEntities().getTotal());
			table.addCell(profile.getBodyCell(entities, i));
			table.addCell(profile.getBodyCell(formatNumber(pathwayBase.getEntities().getRatio()), i));
			table.addCell(profile.getBodyCell(formatNumber(pathwayBase.getEntities().getpValue()), i));
			table.addCell(profile.getBodyCell(formatNumber(pathwayBase.getEntities().getFdr()), i));
			final String reactions = String.format("%d / %d",
					pathway.getData().getReactionsFound(),
					pathway.getData().getReactionsCount());
			table.addCell(profile.getBodyCell(reactions, i));
			table.addCell(profile.getBodyCell(formatNumber(pathway.getData().getReactionsRatio()), i));
			i++;

		}
		document.add(table);
	}


	private String formatNumber(Number number) {
		if (number instanceof Integer || number instanceof Long)
			return number.toString();
		if (number.doubleValue() < 1e-3)
			return String.format("%.2e", number);
		return String.format("%.3f", number);
	}

}
