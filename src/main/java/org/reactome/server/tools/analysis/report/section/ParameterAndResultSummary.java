package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Section ParameterAndResultSummary contains analysis parameter in the analysis
 * result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class ParameterAndResultSummary implements Section {

	private static final DecimalFormat FORMAT = (DecimalFormat) DecimalFormat.getIntegerInstance(Locale.ENGLISH);
	private static final Properties properties = new Properties();

	static {
		FORMAT.setGroupingUsed(true);
		try {
			final InputStream resource = ParameterAndResultSummary.class.getResourceAsStream("properties.properties");
			final InputStreamReader reader = new InputStreamReader(resource, Charset.forName("utf8"));
			properties.load(reader);
		} catch (IOException e) {
			LoggerFactory.getLogger(ParameterAndResultSummary.class).error("Couldn't load resource properties.properties");
		}
	}

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("2. Properties of analysis").setDestination("properties"));
		final AnalysisStoredResult result = analysisData.getAnalysisStoredResult();
		final List<Paragraph> list = new LinkedList<>();
//		list.add(getDescriptionListItem("Analysis type: ", properties.getProperty(String.valueOf(analysisData.getType()).toLowerCase()), profile));
//		list.add(getDescriptionListItem("Pathways found: ", String.format("%,d", result.getPathways().size()), profile));
		final int found = analysisData.getAnalysisStoredResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getAnalysisStoredResult().getNotFound().size();
//		final String identifiers = String.format("%,d / %,d", found, found + notFound);
//		list.add(getDescriptionListItem("Identifiers found: ", identifiers, profile));
//		final boolean projection = result.getSummary().isProjection() != null && result.getSummary().isProjection();
//		list.add(getDescriptionListItem("Projected to human: ", projection ? "yes" : "no", profile));
//		final boolean interactors = result.getSummary().isInteractors() != null && result.getSummary().isInteractors();
//		list.add(getDescriptionListItem("Include interactors: ", interactors ? "yes" : "no", profile));
//		list.add(getDescriptionListItem("Results are shown for species: ", analysisData.getSpecies(), profile));
//		list.add(getDescriptionListItem("Unique ID for analysis: ", analysisData.getAnalysisStoredResult().getSummary().getToken(), profile));
		list.add(profile.getParagraph(properties.getProperty(analysisData.getAnalysisStoredResult().getSummary().getType().toLowerCase()))
				.add(Images.getLink(properties.getProperty("analysis.url"), profile.getFontSize())));
		list.add(profile.getParagraph(String.format(properties.getProperty("identifiers.found"),
				found, found + notFound, analysisData.getAnalysisStoredResult().getPathways().size())));
//		if (analysisData.getAnalysisStoredResult().getSummary().isProjection())
		list.add(profile.getParagraph(properties.getProperty("projected"))
				.add(Images.getLink(properties.getProperty("projected.url"), profile.getFontSize())));
//		if (analysisData.getAnalysisStoredResult().getSummary().isInteractors())
		list.add(profile.getParagraph(properties.getProperty("interactors")));
		list.add(profile.getParagraph(String.format(properties.getProperty("target.species"), analysisData.getSpecies())));
		list.add(profile.getParagraph(String.format(properties.getProperty("token"), analysisData.getAnalysisStoredResult().getSummary().getToken())));
		document.add(profile.getList(list));
	}

	private Paragraph getDescriptionListItem(String title, String description, PdfProfile profile) {
		return profile.getParagraph("")
				.add(new Text(title).setFont(profile.getBoldFont()))
				.add(new Text(description));
	}


}
