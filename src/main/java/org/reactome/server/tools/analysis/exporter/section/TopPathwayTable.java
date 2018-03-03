package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.reactome.server.analysis.core.result.PathwayNodeSummary;
import org.reactome.server.analysis.core.result.model.PathwayBase;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.PathwayData;
import org.reactome.server.tools.analysis.exporter.constant.FontSize;
import org.reactome.server.tools.analysis.exporter.constant.Fonts;
import org.reactome.server.tools.analysis.exporter.element.*;
import org.reactome.server.tools.analysis.exporter.profile.Profile;

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
	public void render(Document document, AnalysisData analysisData) throws Exception {
		document.add(new AreaBreak())
				.add(new Header(String.format("3: Top %s over-representation pathways sorted by p-Value.", analysisData.getPathways().size()), FontSize.H1).setDestination("topPathways"));
		addTopPathwaysTable(document, analysisData);
	}

	private void addTopPathwaysTable(Document document, AnalysisData analysisData) {
		final int min = analysisData.getPathways().size();
		document.add(new AreaBreak());
		document.add(new H2(String.format("4. Top %d pathways", min)).setDestination("pathway-list"));
		final float[] COLUMNS_RELATIVE_WIDTH = new float[]{0.4f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
		final Table table = new Table(UnitValue.createPercentArray(COLUMNS_RELATIVE_WIDTH));
		table.setBorder(Border.NO_BORDER);
		table.useAllAvailableWidth();
		for (String header : HEADERS)
			table.addHeaderCell(new HeaderCell(header));
		int i = 0;
		for (PathwayData pathwayData : analysisData.getPathways()) {
			final PathwayBase pathwayBase = pathwayData.getBase();
			final PathwayNodeSummary pathway = analysisData.getAnalysisStoredResult().getPathway(pathwayBase.getStId());
			table.addCell(getPathwayCell(i, pathway));
			final String entities = String.format("%d / %d", pathwayBase.getEntities().getFound(), pathwayBase.getEntities().getTotal());
			table.addCell(new BodyCell(entities, i));
			table.addCell(new BodyCell(formatNumber(pathwayBase.getEntities().getRatio()), i));
			table.addCell(new BodyCell(formatNumber(pathwayBase.getEntities().getpValue()), i));
			table.addCell(new BodyCell(formatNumber(pathwayBase.getEntities().getFdr()), i));
			final String reactions = String.format("%d / %d",
					pathway.getData().getReactionsFound(),
					pathway.getData().getReactionsCount());
			table.addCell(new BodyCell(reactions, i));
			table.addCell(new BodyCell(formatNumber(pathway.getData().getReactionsRatio()), i));
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

	private Cell getPathwayCell(int i, PathwayNodeSummary pathway) {
		return new Cell().setKeepTogether(true)
				.add(new P(pathway.getName())
						.setFontSize(Profile.P - 2)
						.setFontColor(Profile.REACTOME_COLOR)
						.setFont(Fonts.BOLD)
						.setMultipliedLeading(1.0f)
						.setTextAlignment(TextAlignment.LEFT)
						.setAction(PdfAction.createGoTo(pathway.getStId())))
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBorder(Border.NO_BORDER)
				.setPadding(5)
				.setBackgroundColor(i % 2 == 0 ? null : Profile.LIGHT_GRAY);
	}

}
