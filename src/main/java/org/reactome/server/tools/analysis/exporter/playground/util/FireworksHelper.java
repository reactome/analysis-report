package org.reactome.server.tools.analysis.exporter.playground.util;

import org.reactome.server.analysis.core.result.AnalysisStoredResult;
import org.reactome.server.tools.analysis.exporter.playground.exception.FailToGetFireworksException;
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
     * @param result
     * @return {@see BufferedImage}.
     * @throws FailToGetFireworksException
     */
    public static BufferedImage getFireworks(AnalysisStoredResult result) throws FailToGetFireworksException {
        FireworkArgs args = new FireworkArgs(SPECIES, "png");
        args.setFactor(QUALITY);
        args.setProfile(FireworksColor.COPPER_PLUS.getColor());
        /**
         * looks add flags will result in a lot of time consumption for to request info
         * about 28s to request for resources data to render image
         */
//        ContentServiceClient.setHost("https://reactome.org/");
//        args.setFlags(Arrays.asList("CTP"));

        try {
            return exporter.renderRaster(args, result);
        } catch (Exception pascual) {
            throw new FailToGetFireworksException("Failed to get fireworks for token : ", pascual);
        }
    }

    public static void setPaths(String fireworksPath, String analysisPath) {
        exporter = new FireworksExporter(fireworksPath, analysisPath);
    }
}
