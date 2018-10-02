package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.ListNumberingType;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;

import java.util.Arrays;

public class TableOfContent implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("Table of Contents", false));

		final List list = new List(ListNumberingType.DECIMAL)
				.setSymbolIndent(10)
				.setFontColor(profile.getLinkColor());
		Arrays.asList(
				new Paragraph("Introduction").setAction(PdfAction.createGoTo("introduction")),
				new Paragraph("Properties").setAction(PdfAction.createGoTo("properties")),
				new Paragraph("Genome-wide overview").setAction(PdfAction.createGoTo("overview")),
				new Paragraph("Most significant pathways").setAction(PdfAction.createGoTo("pathway-list")),
				new Paragraph("Pathways details").setAction(PdfAction.createGoTo("pathway-details")),
				new Paragraph("Identifiers found").setAction(PdfAction.createGoTo("identifiers-found")),
				new Paragraph("Identifiers not found").setAction(PdfAction.createGoTo("not-found"))
		).forEach(paragraph -> {
			final ListItem listItem = new ListItem();
			listItem.add(paragraph);
			list.add(listItem);
		});
		document.add(list);
	}
}
