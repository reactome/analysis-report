package org.reactome.server.tools.analysis.exporter.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.AnalysisData;
import org.reactome.server.tools.analysis.exporter.ReportArgs;
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

	private static final double QUALITY = 2.;
	private static FireworksExporter exporter;

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
		args.setProfile(FireworksColor.COPPER_PLUS.getColor());
		return exporter.renderRaster(args, analysisData.getAnalysisStoredResult());
	}

	/**
	 * Set the necessary file before use it.
	 *
	 * @param reportArgs args contains the fireworks json file and analysis
	 *                   binary file path.
	 */
	public static void setPaths(ReportArgs reportArgs) {
		exporter = new FireworksExporter(reportArgs.getFireworksPath(), reportArgs.getAnalysisPath());
	}
}
