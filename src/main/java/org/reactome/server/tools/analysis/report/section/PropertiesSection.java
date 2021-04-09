package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.reactome.server.analysis.core.result.model.AnalysisSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.HtmlParser;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Section ParameterAndResultSummary contains analysis parameter in the analysis
 * result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PropertiesSection implements Section {

	private final static String PROJECTED = "/documentation/inferred-events";
	private final static String ANALYSIS_PATH = "/user/guide/analysis";
	private final static String GSA_ANALYSIS_PATH = "/user/guide/analysis/gsa";

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("Properties").setDestination("properties"));
		final List<Paragraph> paragraphs = new LinkedList<>();

		final AnalysisSummary summary = analysisData.getResult().getSummary();
		final int found = analysisData.getResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getResult().getNotFound().size();

		String gsaMethod = summary.getGsaMethod();
		final String serverName = analysisData.getServerName();
		String analysisTypeDescription;
		Image analysisLink;

		if (gsaMethod == null) {
			String analysisType = summary.getType().toLowerCase();
			analysisLink = Images.getLink(serverName + ANALYSIS_PATH, profile.getFontSize());
			analysisTypeDescription = PdfUtils.getProperty(analysisType);
		} else {
			analysisLink = Images.getLink(serverName + GSA_ANALYSIS_PATH, profile.getFontSize());
			analysisTypeDescription = PdfUtils.getProperty(gsaMethod.toLowerCase());
		}

		if (analysisLink != null && analysisTypeDescription != null) {
			paragraphs.add(HtmlParser.parseParagraph(analysisTypeDescription, profile)
					.add(" ")
					.add(analysisLink));
		}

		paragraphs.add(profile.getParagraph(String.format(PdfUtils.getProperty("identifiers.found"),
				found, found + notFound, analysisData.getResult().getPathways().size())));

		if (analysisData.isProjection())
			paragraphs.add(profile.getParagraph(PdfUtils.getProperty("projected"))
					.add(" ")
					.add(Images.getLink(serverName + PROJECTED, profile.getFontSize())));

		if (analysisData.isInteractors())
			paragraphs.add(profile.getParagraph(PdfUtils.getProperty("interactors")));

		paragraphs.add(profile.getParagraph((PdfUtils.getProperty("target.species.resource", analysisData.getSpecies(), analysisData.getBeautifiedResource()))));

		paragraphs.add(profile.getParagraph(String.format(PdfUtils.getProperty("token"), summary.getToken())));

		document.add(profile.getList(paragraphs));
		document.add(new AreaBreak());
	}

}
