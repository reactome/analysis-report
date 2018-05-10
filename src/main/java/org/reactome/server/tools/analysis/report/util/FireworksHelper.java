package org.reactome.server.tools.analysis.report.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.report.AnalysisData;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.reactome.server.tools.fireworks.exporter.common.analysis.exception.AnalysisServerError;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;

import java.awt.image.BufferedImage;

/**
 * Help to create the fireworks image by invoking the Reactome {@link
 * FireworksExporter}.
 *
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FireworksHelper {

	private static final double QUALITY = 3.;
	private static FireworksExporter exporter;
	private static String profile;

	/**
	 * get the fireworks image from {@link FireworksExporter} by given analysis
	 * token.
	 *
	 * @param analysisData {@link AnalysisStoredResult}.
	 *
	 * @return fireworks.
	 *
	 * @see FireworksExporter
	 */
	public static BufferedImage getFireworks(AnalysisData analysisData) throws AnalysisServerError {
		final FireworkArgs args = new FireworkArgs(analysisData.getSpecies().replace(" ", "_"), "png");
		args.setFactor(QUALITY);
		args.setWriteTitle(false);
		args.setProfile(profile);
//		args.setProfile("Copper plus");
		return exporter.renderRaster(args, analysisData.getAnalysisStoredResult());
	}

	public static void setPaths(String fireworksPath, String analysisPath) {
		exporter = new FireworksExporter(fireworksPath, analysisPath);
	}

	public static void setProfile(String profile) {
		FireworksHelper.profile = profile;
	}
}
