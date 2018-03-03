package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.style.Fonts;
import org.reactome.server.tools.analysis.exporter.element.H2;
import org.reactome.server.tools.analysis.exporter.element.LP;
import org.reactome.server.tools.analysis.exporter.element.UnorderedList;

/**
 * Section ParameterAndResultSummary contains analysis parameter in the analysis
 * result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ParameterAndResultSummary implements Section {

	@Override
	public void render(Document document, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(new H2("2. Summary of parameters").setDestination("parameters"));
		final AnalysisStoredResult result = analysisData.getAnalysisStoredResult();
		final com.itextpdf.layout.element.List list = new UnorderedList();
		list.add(getDescriptionListItem("Analysis type: ", String.valueOf(analysisData.getType())));
		list.add(getDescriptionListItem("Pathways found: ", String.valueOf(result.getPathways().size())));
		final int found = analysisData.getAnalysisStoredResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getAnalysisStoredResult().getNotFound().size();
		final String identifiers = String.format("%d / %d", found, found + notFound);
		list.add(getDescriptionListItem("Identifiers found: ", identifiers));
		final boolean projection = result.getSummary().isProjection() != null && result.getSummary().isProjection();
		list.add(getDescriptionListItem("Projected to human: ", projection ? "yes" : "no"));
		final boolean interactors = result.getSummary().isInteractors() != null && result.getSummary().isInteractors();
		list.add(getDescriptionListItem("Include interactors: ", interactors ? "yes" : "no"));
		list.add(getDescriptionListItem("Results are shown for species: ", analysisData.getSpecies()));
		list.add(getDescriptionListItem("Unique ID for analysis: ", analysisData.getAnalysisStoredResult().getSummary().getToken()));
		document.add(list);
	}

	private ListItem getDescriptionListItem(String title, String description) {
		final ListItem item = new ListItem();
		item.add(new LP().add(new Text(title).setFont(Fonts.BOLD)).add(new Text(description)));
		return item;
	}


}
