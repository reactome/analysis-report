package org.reactome.server.tools.analysis.report.section;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.TextAlignment;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.analysis.report.style.PdfProfile;
import org.reactome.server.tools.analysis.report.util.FireworksHelper;
import org.reactome.server.tools.analysis.report.util.PdfUtils;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;
import org.slf4j.LoggerFactory;

/**
 * Fireworks and AnalysisStoredResult.getSummary()
 */
public class OverviewSection implements Section {

	@Override
	public void render(Document document, PdfProfile profile, AnalysisData analysisData) {
		document.add(profile.getH1("Genome-wide overview").setDestination("overview"));
		addFireworks(document, analysisData);
		final String text = PdfUtils.getProperty("fireworks.caption");
		document.add(profile.getParagraph(text).setTextAlignment(TextAlignment.CENTER));
		document.add(new AreaBreak());
	}

	private void addFireworks(Document document, AnalysisData analysisData) {
		try {
			FireworksHelper.insertFireworks(document, analysisData);
		} catch (AnalysisServerError exception) {
			LoggerFactory.getLogger(OverviewSection.class).error("Couldn't add fireworks", exception);
		}
	}


}
