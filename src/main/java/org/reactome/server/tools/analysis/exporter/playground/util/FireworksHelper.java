package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.analysisexporter.ReportArgs;
import org.reactome.server.tools.fireworks.exporter.FireworksExporter;
import org.reactome.server.tools.fireworks.exporter.common.api.FireworkArgs;

import java.awt.image.BufferedImage;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
public class FireworksHelper {

    //this file must contain the *.json files,you down it from
    private static final String SPECIES = "Homo_sapiens";
    private static final double QUALITY = 2.5;


    private static FireworksExporter exporter;

    /**
     * get the buffered image from fireworks exporter by given token
     *
     * @param result {@see AnalysisStoredResult}.
     * @return {@see BufferedImage}.
     */
    public static BufferedImage getFireworks(AnalysisStoredResult result) {
        FireworkArgs args = new FireworkArgs(SPECIES, "png");
        args.setFactor(QUALITY);
        args.setProfile(FireworksColor.COPPER_PLUS.getColor());

        try {
            return exporter.renderRaster(args, result);
        } catch (Exception pascual) {
            return null;
        }
    }

    public static void setPaths(ReportArgs reportArgs) {
        exporter = new FireworksExporter(reportArgs.getFireworksPath(), reportArgs.getAnalysisPath());
    }
}
