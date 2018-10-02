package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import org.reactome.server.analysis.core.result.model.AnalysisSummary;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.Images;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.HtmlParser;
import org.reactome.server.tools.analysis.report.util.PdfUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Section ParameterAndResultSummary contains analysis parameter in the analysis
 * result, fireworks for this analysis.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class PropertiesSection implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(new AreaBreak());
		document.add(profile.getH1("Properties").setDestination("properties"));
		final List<Paragraph> list = new LinkedList<>();

		final String text = PdfUtils.getProperty(analysisData.getResult().getSummary().getType().toLowerCase());
		final int found = analysisData.getResult().getAnalysisIdentifiers().size();
		final int notFound = analysisData.getResult().getNotFound().size();
		final AnalysisSummary summary = analysisData.getResult().getSummary();


		list.add(HtmlParser.parseParagraph(text, profile)
				.add(" ")
				.add(Images.getLink(PdfUtils.getProperty("analysis.url"), profile.getFontSize())));

		list.add(profile.getParagraph(String.format(PdfUtils.getProperty("identifiers.found"),
				found, found + notFound, analysisData.getResult().getPathways().size())));

		if (analysisData.isProjection())
			list.add(profile.getParagraph(PdfUtils.getProperty("projected"))
					.add(" ")
					.add(Images.getLink(PdfUtils.getProperty("projected.url"), profile.getFontSize())));

		if (analysisData.isInteractors())
			list.add(profile.getParagraph(PdfUtils.getProperty("interactors")));

		list.add(profile.getParagraph((PdfUtils.getProperty("target.species.resource", analysisData.getSpecies(), analysisData.getBeautifiedResource()))));

		list.add(profile.getParagraph(String.format(PdfUtils.getProperty("token"), summary.getToken())));

		document.add(profile.getList(list));
		document.add(new AreaBreak());
	}

}
