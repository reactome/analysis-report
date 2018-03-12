package org.reactome.server.tools.analysis.exporter.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.style.PdfProfile;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Section ParameterAndResultSummary contains analysis parameter in the analysis
 * result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ParameterAndResultSummary implements Section {

	private static final DecimalFormat FORMAT = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.ENGLISH);
	static {
		FORMAT.setGroupingUsed(true);
	}

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("2. Summary of parameters").setDestination("parameters"));
		final AnalysisStoredResult result = analysisData.getAnalysisStoredResult();
		final List<Paragraph> list = new LinkedList<>();
		list.add(getDescriptionListItem("Analysis type: ", String.valueOf(analysisData.getType()), profile));
		list.add(getDescriptionListItem("Pathways found: ", String.format("%,d", result.getPathways().size()), profile));
		final int found = analysisData.getAnalysisStoredResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getAnalysisStoredResult().getNotFound().size();
		final String identifiers = String.format("%,d / %,d", found, found + notFound);
		list.add(getDescriptionListItem("Identifiers found: ", identifiers, profile));
		final boolean projection = result.getSummary().isProjection() != null && result.getSummary().isProjection();
		list.add(getDescriptionListItem("Projected to human: ", projection ? "yes" : "no", profile));
		final boolean interactors = result.getSummary().isInteractors() != null && result.getSummary().isInteractors();
		list.add(getDescriptionListItem("Include interactors: ", interactors ? "yes" : "no", profile));
		list.add(getDescriptionListItem("Results are shown for species: ", analysisData.getSpecies(), profile));
		list.add(getDescriptionListItem("Unique ID for analysis: ", analysisData.getAnalysisStoredResult().getSummary().getToken(), profile));
		document.add(profile.getList(list));
	}

	private Paragraph getDescriptionListItem(String title, String description, PdfProfile profile) {
		return profile.getParagraph("")
				.add(new Text(title).setFont(profile.getBoldFont()))
				.add(new Text(description));
	}


}
