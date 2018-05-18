package org.reactome.server.tools.analysis.report.util;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Help to create the fireworks image by invoking the Reactome {@link
 * FireworksExporter}.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FireworksHelper {

	private static final double QUALITY = 2.;
	private static FireworksExporter exporter;
	private static String profile;

	public static void insertFireworks(Document document, AnalysisData data) throws AnalysisServerError {
		final FireworkArgs args = new FireworkArgs(data.getSpecies().replace(" ", "_"), "png");
		args.setFactor(QUALITY);
		args.setWriteTitle(false);
		args.setProfile(profile);
		try {
			final Document fireworks = exporter.renderPdf(args, data.getAnalysisStoredResult());
			final PdfFormXObject object = fireworks.getPdfDocument().getFirstPage().copyAsFormXObject(document.getPdfDocument());
			final float wi = document.getPdfDocument().getLastPage().getPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin() - 0.1f;  // avoid image too large
			final float he = 0.5f * document.getPdfDocument().getLastPage().getPageSize().getHeight() - document.getTopMargin() - document.getBottomMargin();
			document.add(new Image(object).scaleToFit(wi, he).setHorizontalAlignment(HorizontalAlignment.CENTER));
			document.flush();
		} catch (IOException e) {
			LoggerFactory.getLogger(FireworksHelper.class).error("Couldn't insert fireworks", e);
		}

	}

	public static void setPaths(String fireworksPath, String analysisPath) {
		exporter = new FireworksExporter(fireworksPath, analysisPath);
	}

	public static void setProfile(String profile) {
		FireworksHelper.profile = profile;
	}
}
